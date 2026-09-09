package com.cdut.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 登录响应中的用户信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDTO implements Serializable {

    private Long userId;

    private String username;

    private String realName;

    private Integer userType;

    private String avatar;

    private List<String> roles;

    private Integer doctorType;

    private String dept;

    private String title;

}
