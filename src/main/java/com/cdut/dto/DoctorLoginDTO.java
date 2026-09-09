package com.cdut.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * 医生登录入参
 */
@Data
public class DoctorLoginDTO implements Serializable {

    /** 手机号（医生账号） */
    @NotBlank(message = "手机号不能为空")
    private String phone;

    /** 密码（加密传输） */
    @NotBlank(message = "密码不能为空")
    private String password;

    /** 图形验证码（可选） */
    private String captcha;

    /** 验证码 key（可选） */
    private String captchaKey;

}
