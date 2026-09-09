package com.cdut.service.impl;

import com.cdut.common.BusinessException;
import com.cdut.dto.DoctorLoginDTO;
import com.cdut.dto.LoginRespDTO;
import com.cdut.entity.DoctorAccount;
import com.cdut.mapper.DoctorAccountMapper;
import com.cdut.mapper.DoctorQualificationMapper;
import com.cdut.util.JwtUtil;
import com.cdut.util.PasswordUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

/**
 * 医生登录认证服务单元测试（Mockito 隔离 Mapper 与 JWT）。
 */
@ExtendWith(MockitoExtension.class)
class DoctorAuthServiceImplTest {

    @Mock
    private DoctorAccountMapper doctorAccountMapper;

    @Mock
    private DoctorQualificationMapper qualificationMapper;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private DoctorAuthServiceImpl doctorAuthService;

    private DoctorLoginDTO loginDTO;

    @BeforeEach
    void setUp() {
        loginDTO = new DoctorLoginDTO();
        loginDTO.setPhone("13800138000");
        loginDTO.setPassword("123456");
    }

    @Test
    void loginSuccess() {
        DoctorAccount account = new DoctorAccount();
        account.setId(30001L);
        account.setPhone("13800138000");
        account.setPassword(PasswordUtil.encrypt("123456"));
        account.setName("王医生");
        account.setDoctorType(1);
        account.setTitle("主治医师");
        account.setDept("全科");
        account.setStatus(1);

        when(doctorAccountMapper.selectOne(any())).thenReturn(account);
        when(jwtUtil.generateAccessToken(anyLong(), anyInt(), anyInt())).thenReturn("access-token");
        when(jwtUtil.generateRefreshToken(anyLong(), anyInt(), anyInt())).thenReturn("refresh-token");
        when(jwtUtil.getExpire()).thenReturn(7200L);

        LoginRespDTO resp = doctorAuthService.login(loginDTO);

        assertNotNull(resp);
        assertEquals("access-token", resp.getAccessToken());
        assertEquals("王医生", resp.getUserInfo().getRealName());
        assertEquals(2, resp.getUserInfo().getUserType());
    }

    @Test
    void loginWrongPasswordShouldThrow() {
        DoctorAccount account = new DoctorAccount();
        account.setPhone("13800138000");
        account.setPassword(PasswordUtil.encrypt("other"));
        account.setStatus(1);

        when(doctorAccountMapper.selectOne(any())).thenReturn(account);

        BusinessException ex = assertThrows(BusinessException.class, () -> doctorAuthService.login(loginDTO));
        assertEquals("账号或密码错误", ex.getMessage());
    }

    @Test
    void loginPendingShouldThrow() {
        DoctorAccount account = new DoctorAccount();
        account.setPhone("13800138000");
        account.setPassword(PasswordUtil.encrypt("123456"));
        account.setStatus(0);

        when(doctorAccountMapper.selectOne(any())).thenReturn(account);

        BusinessException ex = assertThrows(BusinessException.class, () -> doctorAuthService.login(loginDTO));
        assertEquals("账号待审核，请联系管理员", ex.getMessage());
    }

    @Test
    void loginNotFoundShouldThrow() {
        when(doctorAccountMapper.selectOne(any())).thenReturn(null);

        BusinessException ex = assertThrows(BusinessException.class, () -> doctorAuthService.login(loginDTO));
        assertEquals("账号或密码错误", ex.getMessage());
    }

}
