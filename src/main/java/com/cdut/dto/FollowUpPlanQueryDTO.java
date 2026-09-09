package com.cdut.dto;

import com.cdut.common.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 随访计划查询条件
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FollowUpPlanQueryDTO extends PageQuery implements Serializable {

    /** 老人ID */
    private Long elderId;

    /** 状态：1-进行中 0-已结束 */
    private Integer status;

    /** 计划名称（模糊） */
    private String planName;

}
