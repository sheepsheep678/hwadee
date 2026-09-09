package com.cdut.controller;

import com.cdut.common.Result;
import com.cdut.dto.DoctorLoginDTO;
import com.cdut.dto.DoctorRegisterDTO;
import com.cdut.dto.LoginRespDTO;
import com.cdut.service.DoctorAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 医生端 · 登录认证接口
 */
@RestController
@RequestMapping("/api/doctor/auth")
@RequiredArgsConstructor
public class DoctorAuthController {

    private final DoctorAuthService doctorAuthService;

    /** 医生登录 */
    @PostMapping("/login")
    public Result<LoginRespDTO> login(@RequestBody @Validated DoctorLoginDTO dto) {
        return Result.success(doctorAuthService.login(dto));
    }

    /** 医生退出登录 */
    @PostMapping("/logout")
    public Result<Void> logout() {
        doctorAuthService.logout();
        return Result.success();
    }

    /** 医生注册（入驻申请） */
    @PostMapping("/register")
    public Result<Void> register(@RequestBody @Validated DoctorRegisterDTO dto) {
        doctorAuthService.register(dto);
        return Result.success();
    }

}
