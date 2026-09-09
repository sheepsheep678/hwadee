package com.cdut.dto;

import lombok.Data;

/**
 * 我的消息查询入参
 */
@Data
public class MessageQueryDTO {

    /** 消息类型：1-系统公告 2-工单提醒 3-审核提醒，不传则全部 */
    private Integer msgType;

    /** 是否已读：0-未读 1-已读，不传则全部 */
    private Integer isRead;

    private Integer pageNum = 1;

    private Integer pageSize = 10;

    /** 页码兜底，防止前端传 0 或负数 */
    public int safePageNum() {
        return (pageNum == null || pageNum < 1) ? 1 : pageNum;
    }

    public int safePageSize() {
        if (pageSize == null || pageSize < 1) {
            return 10;
        }
        return Math.min(pageSize, 100);
    }
}
