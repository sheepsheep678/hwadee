package com.cdut.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 随访计划查询条件
 */
@Data
public class FollowUpPlanQueryDTO implements Serializable {

    /** 老人ID */
    private Long elderId;

    /** 执行状态：1-进行中 2-已完成 3-已终止 */
    private Integer status;

    /** 计划类型：1-电话 2-上门 3-远程问诊 */
    private Integer planType;

    /** 当前页码，默认 1 */
    private Integer pageNum = 1;

    /** 每页条数，默认 10 */
    private Integer pageSize = 10;

    /** 获取安全页码 */
    public int safePageNum() {
        return pageNum == null || pageNum < 1 ? 1 : pageNum;
    }

    /** 获取安全页大小 */
    public int safePageSize() {
        if (pageSize == null || pageSize < 1) {
            return 10;
        }
        return Math.min(pageSize, 100);
    }

}
