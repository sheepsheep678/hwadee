package com.cdut.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 老人登录入参
 *
 * <p>account 支持三种：账号（account_no）/ 手机号（bind_phone）/ 身份证号（id_card）
 */
@Data
public class ElderLoginDTO {

    @NotBlank(message = "账号不能为空")
    private String account;

    @NotBlank(message = "密码不能为空")
    private String password;

    /** 图形验证码：本期不校验，前端可不传 */
    private String captcha;

    /** 图形验证码 key：本期不校验，前端可不传 */
    private String captchaKey;
}
