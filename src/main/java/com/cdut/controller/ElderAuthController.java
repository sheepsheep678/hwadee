package com.cdut.controller;

import com.cdut.dto.ElderLoginDTO;
import com.cdut.dto.ElderRegisterDTO;
import com.cdut.dto.LoginRespDTO;
import com.cdut.pojo.Result;
import com.cdut.service.ElderAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 老人端登录认证
 *
 * <p>路径：/api/elder/auth（已在 WebConfig 中放行，不需要 token）
 */
@RestController
@RequestMapping("/api/elder/auth")
@Validated
public class ElderAuthController {

    @Autowired
    private ElderAuthService elderAuthService;

    /** 注册：提交后进入待审核状态 */
    @PostMapping("/register")
    public Result<Void> register(@RequestBody @Validated ElderRegisterDTO dto) {
        elderAuthService.register(dto);
        return Result.success("注册成功，请等待审核", null);
    }

    /** 登录：账号 / 手机号 / 身份证号 + 密码 */
    @PostMapping("/login")
    public Result<LoginRespDTO> login(@RequestBody @Validated ElderLoginDTO dto) {
        return Result.success(elderAuthService.login(dto));
    }

    /** 退出登录：JWT 无状态，前端删除 token 即可 */
    @PostMapping("/logout")
    public Result<Void> logout() {
        elderAuthService.logout();
        return Result.success("退出成功", null);
    }
}
