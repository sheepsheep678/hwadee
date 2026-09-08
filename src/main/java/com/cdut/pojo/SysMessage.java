package com.cdut.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 系统消息实体（对应表 sys_message）
 *
 * <p>约定：老人端 userId 存 elder_account.id（账户ID），由管理端同学保持一致。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 接收人ID（老人端 = elder_account.id） */
    private Long userId;

    /** 消息类型：1-系统公告 2-工单提醒 3-审核提醒 */
    private Integer msgType;

    /** 消息标题 */
    private String title;

    /** 消息内容 */
    private String content;

    /** 是否已读：0-未读 1-已读 */
    private Integer isRead;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    /** 逻辑删除：0-未删除 1-已删除 */
    private Integer isDeleted;
}
