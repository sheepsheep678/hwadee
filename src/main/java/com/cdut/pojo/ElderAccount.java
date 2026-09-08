package com.cdut.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 老人账户实体（对应表 elder_account）
 *
 * <p>状态说明：
 * <ul>
 *   <li>authStatus     审核状态：0-待审核 1-已通过 2-已驳回</li>
 *   <li>accountStatus  账户状态：1-正常 2-冻结 3-注销</li>
 *   <li>registerChannel 注册渠道：1-自助 2-家属代注册 3-机构录入</li>
 * </ul>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ElderAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 账号 */
    private String accountNo;

    /** 密码（BCrypt 加密，禁止明文返回给前端） */
    private String password;

    /** 绑定手机号 */
    private String bindPhone;

    /** 审核状态：0-待审核 1-已通过 2-已驳回 */
    private Integer authStatus;

    /** 账户状态：1-正常 2-冻结 3-注销 */
    private Integer accountStatus;

    /** 注册渠道：1-自助 2-家属代注册 3-机构录入 */
    private Integer registerChannel;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    /** 逻辑删除：0-未删除 1-已删除 */
    private Integer isDeleted;

    /** 审核状态常量 */
    public static final class AuthStatus {
        public static final int WAITING = 0;
        public static final int PASSED = 1;
        public static final int REJECTED = 2;
    }

    /** 账户状态常量 */
    public static final class AccountStatus {
        public static final int NORMAL = 1;
        public static final int FROZEN = 2;
        public static final int CANCELLED = 3;
    }
}
