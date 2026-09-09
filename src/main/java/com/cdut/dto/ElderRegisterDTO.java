package com.cdut.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 老人注册入参
 *
 * <p>注册同时写入 elder_account 与 elder_profile 两张表，账户初始为「待审核」。
 */
@Data
public class ElderRegisterDTO {

    @NotBlank(message = "姓名不能为空")
    @Size(max = 64, message = "姓名长度不能超过64")
    private String name;

    @NotBlank(message = "身份证号不能为空")
    @Pattern(regexp = "^\\d{17}[\\dXx]$", message = "身份证号格式错误")
    private String idCard;

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式错误")
    private String phone;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度需为6-20位")
    private String password;

    @NotBlank(message = "确认密码不能为空")
    private String confirmPassword;

    /** 注册渠道：1-自助 2-家属代注册 3-机构录入，默认 1 */
    private Integer registerChannel;
}
