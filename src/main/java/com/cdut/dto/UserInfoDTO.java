package com.cdut.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 登录响应中的用户信息
 */
@Data
@NoArgsConstructor
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

    /** 老人档案ID（老人端） */
    private Long elderId;

    /** 审核状态：0-待审核 1-已通过 2-已驳回（老人端） */
    private Integer authStatus;

    /** 医生端登录构造器 */
    public UserInfoDTO(Long userId, String username, String realName, Integer userType,
                       String avatar, List<String> roles, Integer doctorType, String dept, String title) {
        this.userId = userId;
        this.username = username;
        this.realName = realName;
        this.userType = userType;
        this.avatar = avatar;
        this.roles = roles;
        this.doctorType = doctorType;
        this.dept = dept;
        this.title = title;
    }

}
