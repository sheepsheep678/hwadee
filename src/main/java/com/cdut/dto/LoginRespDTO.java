package com.cdut.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 登录出参（三端通用）
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRespDTO {

    private String accessToken;

    /** 本期未实现刷新逻辑，返回与 accessToken 相同的值 */
    private String refreshToken;

    /** 有效期（秒） */
    private Long expiresIn;

    private UserInfoDTO userInfo;

    public LoginRespDTO(String accessToken, String refreshToken, long expire, UserInfoDTO userInfo) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresIn = expire;
        this.userInfo = userInfo;
    }

    @Data
    public static class UserInfo {

        /** 账户ID（老人端 = elder_account.id） */
        private Long userId;

        private String username;

        private String realName;

        /** 1-管理端 2-医生端 3-老人端 */
        private Integer userType;

        private String avatar;

        private List<String> roles;

        // ===== 老人端扩展字段 =====

        /** 老人档案ID（elder_profile.id），业务表查询用这个 */
        private Long elderId;

        /** 审核状态：0-待审核 1-已通过 2-已驳回 */
        private Integer authStatus;
    }
}
