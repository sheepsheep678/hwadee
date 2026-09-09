package com.cdut.service.impl;

import com.cdut.dto.ElderLoginDTO;
import com.cdut.dto.ElderRegisterDTO;
import com.cdut.dto.LoginRespDTO;
import com.cdut.dto.UserInfoDTO;
import com.cdut.exception.BizException;
import com.cdut.mapper.ElderAccountMapper;
import com.cdut.mapper.ProfileMapper;
import com.cdut.pojo.ElderAccount;
import com.cdut.pojo.ElderProfile;
import com.cdut.service.ElderAuthService;
import com.cdut.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class ElderAuthServiceImpl implements ElderAuthService {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.BASIC_ISO_DATE;

    @Autowired
    private ElderAccountMapper elderAccountMapper;
    @Autowired
    private ProfileMapper profileMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtils jwtUtils;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(ElderRegisterDTO dto) {
        // 0. 两次密码一致性校验
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new BizException("两次输入的密码不一致");
        }

        // 1. 查重
        if (elderAccountMapper.countByPhone(dto.getPhone()) > 0) {
            throw new BizException("该手机号已注册");
        }
        if (elderAccountMapper.countByIdCard(dto.getIdCard()) > 0) {
            throw new BizException("该身份证号已注册");
        }

        // 2. 生成账号
        String accountNo = generateAccountNo();

        // 3. 写账户表（初始待审核）
        ElderAccount account = new ElderAccount();
        account.setAccountNo(accountNo);
        account.setPassword(passwordEncoder.encode(dto.getPassword()));
        account.setBindPhone(dto.getPhone());
        account.setAuthStatus(ElderAccount.AuthStatus.WAITING);
        account.setAccountStatus(ElderAccount.AccountStatus.NORMAL);
        account.setRegisterChannel(dto.getRegisterChannel() == null ? 1 : dto.getRegisterChannel());
        try {
            elderAccountMapper.insert(account);

            // 4. 同步建档案（elder_no 与 account_no 一致，方便对照）
            ElderProfile profile = new ElderProfile();
            profile.setElderNo(accountNo);
            profile.setAccountId(account.getId());
            profile.setName(dto.getName());
            profile.setIdCard(dto.getIdCard());
            profile.setPhone(dto.getPhone());
            profileMapper.insertForRegister(profile);
        } catch (DuplicateKeyException e) {
            // 并发注册撞唯一索引时的兜底（常规查重在上方已做）
            throw new BizException("该手机号或身份证号已注册", e);
        }
    }

    @Override
    public LoginRespDTO login(ElderLoginDTO dto) {
        ElderAccount account = elderAccountMapper.selectByAccount(dto.getAccount());
        if (account == null) {
            throw new BizException("账号不存在");
        }

        // 先校验密码，再校验状态：否则拿手机号就能试出别人的审核状态（信息泄露）
        if (account.getPassword() == null
                || !passwordEncoder.matches(dto.getPassword(), account.getPassword())) {
            throw new BizException("账号或密码错误");
        }

        Integer authStatus = account.getAuthStatus();
        if (authStatus == null || authStatus == ElderAccount.AuthStatus.WAITING) {
            throw new BizException("账号待审核，请联系管理员");
        }
        if (authStatus == ElderAccount.AuthStatus.REJECTED) {
            throw new BizException("账号审核未通过，请联系管理员");
        }

        Integer accountStatus = account.getAccountStatus();
        if (accountStatus != null && accountStatus == ElderAccount.AccountStatus.FROZEN) {
            throw new BizException("账号已冻结，请联系管理员");
        }
        if (accountStatus != null && accountStatus == ElderAccount.AccountStatus.CANCELLED) {
            throw new BizException("账号已注销");
        }

        // 带出档案信息
        ElderProfile profile = profileMapper.selectByAccountId(account.getId());

        String token = jwtUtils.generateToken(account.getId(), JwtUtils.USER_TYPE_ELDER, account.getAccountNo());

        LoginRespDTO resp = new LoginRespDTO();
        resp.setAccessToken(token);
        resp.setRefreshToken(token);
        resp.setExpiresIn(jwtUtils.getExpireSeconds());

        UserInfoDTO userInfo = new UserInfoDTO();
        userInfo.setUserId(account.getId());
        userInfo.setUsername(account.getAccountNo());
        userInfo.setRealName(profile == null ? null : profile.getName());
        userInfo.setUserType(JwtUtils.USER_TYPE_ELDER);
        userInfo.setAvatar(profile == null ? null : profile.getPhotoUrl());
        userInfo.setRoles(Collections.singletonList("ELDER"));
        userInfo.setElderId(profile == null ? null : profile.getId());
        userInfo.setAuthStatus(authStatus);
        resp.setUserInfo(userInfo);

        return resp;
    }

    @Override
    public void logout() {
        // JWT 无状态，后端不做失效处理，由前端删除 token
    }

    /** 生成账号：EL + 年月日 + 4 位随机，冲突则重试 */
    private String generateAccountNo() {
        String date = LocalDate.now().format(DATE_FMT);
        for (int i = 0; i < 10; i++) {
            String no = "EL" + date + String.format("%04d", ThreadLocalRandom.current().nextInt(10000));
            if (elderAccountMapper.countByAccountNo(no) == 0) {
                return no;
            }
        }
        throw new BizException("账号生成失败，请重试");
    }
}
