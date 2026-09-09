package com.cdut.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cdut.common.BusinessException;
import com.cdut.dto.*;
import com.cdut.entity.DoctorAccount;
import com.cdut.entity.DoctorQualification;
import com.cdut.mapper.DoctorAccountMapper;
import com.cdut.mapper.DoctorQualificationMapper;
import com.cdut.service.DoctorAuthService;
import com.cdut.util.JwtUtil;
import com.cdut.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 医生登录认证服务实现。
 */
@Service
@RequiredArgsConstructor
public class DoctorAuthServiceImpl implements DoctorAuthService {

    private static final int USER_TYPE_DOCTOR = 2;

    private final DoctorAccountMapper doctorAccountMapper;
    private final DoctorQualificationMapper qualificationMapper;
    private final JwtUtil jwtUtil;

    @Override
    public LoginRespDTO login(DoctorLoginDTO dto) {
        DoctorAccount account = doctorAccountMapper.selectOne(
                new LambdaQueryWrapper<DoctorAccount>().eq(DoctorAccount::getPhone, dto.getPhone()));
        if (account == null || !PasswordUtil.matches(dto.getPassword(), account.getPassword())) {
            throw new BusinessException("账号或密码错误");
        }
        if (account.getStatus() == null || account.getStatus() != 1) {
            throw new BusinessException("账号待审核，请联系管理员");
        }
        String accessToken = jwtUtil.generateAccessToken(account.getId(), USER_TYPE_DOCTOR, account.getDoctorType());
        String refreshToken = jwtUtil.generateRefreshToken(account.getId(), USER_TYPE_DOCTOR, account.getDoctorType());
        UserInfoDTO userInfo = new UserInfoDTO(
                account.getId(),
                account.getPhone(),
                account.getName(),
                USER_TYPE_DOCTOR,
                account.getAvatar(),
                List.of("DOCTOR"),
                account.getDoctorType(),
                account.getDept(),
                account.getTitle());
        return new LoginRespDTO(accessToken, refreshToken, jwtUtil.getExpire(), userInfo);
    }

    @Override
    public void logout() {
        // JWT 为无状态令牌，服务端无需维护会话，客户端丢弃令牌即可
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(DoctorRegisterDTO dto) {
        Long count = doctorAccountMapper.selectCount(
                new LambdaQueryWrapper<DoctorAccount>().eq(DoctorAccount::getPhone, dto.getPhone()));
        if (count != null && count > 0) {
            throw new BusinessException("该手机号已注册");
        }
        DoctorAccount account = new DoctorAccount();
        account.setPhone(dto.getPhone());
        account.setPassword(PasswordUtil.encrypt(dto.getPassword()));
        account.setName(dto.getName());
        account.setDoctorType(dto.getDoctorType());
        account.setTitle(dto.getTitle());
        account.setDept(dto.getDept());
        account.setOrgId(dto.getOrgId());
        account.setStatus(0);
        account.setCreateTime(LocalDateTime.now());
        doctorAccountMapper.insert(account);

        if (dto.getQualifications() != null) {
            for (DoctorQualificationDTO q : dto.getQualifications()) {
                DoctorQualification qualification = new DoctorQualification();
                qualification.setDoctorId(account.getId());
                qualification.setCertType(q.getCertType());
                qualification.setCertNo(q.getCertNo());
                qualification.setCertImgUrl(q.getCertImgUrl());
                qualification.setValidDate(q.getValidDate());
                qualification.setCreateTime(LocalDateTime.now());
                qualificationMapper.insert(qualification);
            }
        }
    }

}
