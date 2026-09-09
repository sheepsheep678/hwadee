package com.cdut.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 老人账户信息出参
 *
 * <p>单独建 VO 而不是直接返回 ElderAccount，避免把 password 泄露给前端。
 */
@Data
public class ElderAccountVO {

    /** 账户ID */
    private Long id;

    /** 账号 */
    private String accountNo;

    /** 绑定手机号 */
    private String bindPhone;

    /** 审核状态：0-待审核 1-已通过 2-已驳回 */
    private Integer authStatus;

    /** 账户状态：1-正常 2-冻结 3-注销 */
    private Integer accountStatus;

    /** 注册渠道 */
    private Integer registerChannel;

    private LocalDateTime createTime;

    // ===== 关联的档案信息（联表带出，方便前端一次性渲染个人中心）=====

    /** 档案ID */
    private Long elderId;

    /** 老人编号 */
    private String elderNo;

    private String name;

    private String idCard;

    private String phone;

    private String photoUrl;
}
