package com.cdut.service.impl;

import com.cdut.dto.DoctorLoginDTO;
import com.cdut.dto.DoctorQualificationDTO;
import com.cdut.dto.DoctorRegisterDTO;
import com.cdut.dto.LoginRespDTO;
import com.cdut.dto.UserInfoDTO;
import com.cdut.exception.BizException;
import com.cdut.mapper.DoctorAccountMapper;
import com.cdut.mapper.DoctorQualificationMapper;
import com.cdut.pojo.DoctorAccount;
import com.cdut.pojo.DoctorQualification;
import com.cdut.service.DoctorAuthService;
import com.cdut.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 医生登录认证服务实现。
 */
@Service
@RequiredArgsConstructor
public class DoctorAuthServiceImpl implements DoctorAuthService {

    private final DoctorAccountMapper doctorAccountMapper;
    private final DoctorQualificationMapper qualificationMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Override
    public LoginRespDTO login(DoctorLoginDTO dto) {
        DoctorAccount account = doctorAccountMapper.selectByPhone(dto.getPhone());
        if (account == null || account.getPassword() == null
                || !passwordEncoder.matches(dto.getPassword(), account.getPassword())) {
            throw new BizException("账号或密码错误");
        }
        if (account.getAuditStatus() == null || account.getAuditStatus() != 1) {
            throw new BizException("账号待审核，请联系管理员");
        }
        if (account.getAccountStatus() != null && account.getAccountStatus() != 1) {
            throw new BizException("账号已禁用，请联系管理员");
        }
        String accessToken = jwtUtils.generateToken(account.getId(), JwtUtils.USER_TYPE_DOCTOR, account.getPhone());
        UserInfoDTO userInfo = new UserInfoDTO(
                account.getId(),
                account.getPhone(),
                account.getName(),
                JwtUtils.USER_TYPE_DOCTOR,
                null,
                List.of("DOCTOR"),
                account.getDoctorType(),
                account.getDept(),
                account.getTitle());
        return new LoginRespDTO(accessToken, accessToken, jwtUtils.getExpireSeconds(), userInfo);
    }

    @Override
    public void logout() {
        // JWT 为无状态令牌，服务端无需维护会话，客户端丢弃令牌即可
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(DoctorRegisterDTO dto) {
        if (doctorAccountMapper.countByPhone(dto.getPhone()) > 0) {
            throw new BizException("该手机号已注册");
        }
        DoctorAccount account = new DoctorAccount();
        account.setPhone(dto.getPhone());
        account.setPassword(passwordEncoder.encode(dto.getPassword()));
        account.setName(dto.getName());
        account.setDoctorType(dto.getDoctorType());
        account.setTitle(dto.getTitle());
        account.setDept(dto.getDept());
        account.setOrgId(dto.getOrgId());
        account.setAuditStatus(0);
        account.setAccountStatus(1);
        doctorAccountMapper.insert(account);

        if (dto.getQualifications() != null) {
            for (DoctorQualificationDTO q : dto.getQualifications()) {
                DoctorQualification qualification = new DoctorQualification();
                qualification.setDoctorId(account.getId());
                qualification.setCertType(q.getCertType());
                qualification.setCertNo(q.getCertNo());
                qualification.setCertImgUrl(q.getCertImgUrl());
                qualification.setValidDate(q.getValidDate());
                qualification.setAuditStatus(0);
                qualificationMapper.insert(qualification);
            }
        }
    }

}
