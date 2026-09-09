package com.cdut.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 登录响应
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRespDTO implements Serializable {

    private String accessToken;

    private String refreshToken;

    /** 有效期（秒） */
    private Long expiresIn;

    private UserInfoDTO userInfo;

}
