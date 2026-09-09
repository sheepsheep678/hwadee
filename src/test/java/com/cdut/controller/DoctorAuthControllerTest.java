package com.cdut.controller;

import com.cdut.dto.LoginRespDTO;
import com.cdut.exception.GlobalExceptionHandler;
import com.cdut.service.DoctorAuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 医生登录接口测试（MockMvc 独立启动，隔离 Service）。
 */
@ExtendWith(MockitoExtension.class)
class DoctorAuthControllerTest {

    @Mock
    private DoctorAuthService doctorAuthService;

    @InjectMocks
    private DoctorAuthController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void loginShouldReturnToken() throws Exception {
        LoginRespDTO resp = new LoginRespDTO();
        resp.setAccessToken("access-token");
        resp.setRefreshToken("refresh-token");
        resp.setExpiresIn(7200L);
        when(doctorAuthService.login(any())).thenReturn(resp);

        mockMvc.perform(post("/api/doctor/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"phone\":\"13800138000\",\"password\":\"123456\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.accessToken").value("access-token"));
    }

    @Test
    void loginMissingPhoneShouldFail() throws Exception {
        mockMvc.perform(post("/api/doctor/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"password\":\"123456\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("手机号不能为空"));
    }

}
