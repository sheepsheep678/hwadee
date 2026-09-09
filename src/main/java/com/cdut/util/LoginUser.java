package com.cdut.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 登录用户上下文信息（从 JWT 解析得到，存入 ThreadLocal）。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 用户ID */
    private Long userId;

    /** 用户类型：2-医生 3-老人 */
    private Integer userType;

    /** 医生类型（仅医生有效）：1-家庭医生 2-专科医生 3-康复师 4-护理师 */
    private Integer doctorType;

}
