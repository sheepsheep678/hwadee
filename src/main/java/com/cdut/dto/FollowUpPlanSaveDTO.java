package com.cdut.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 随访计划新增/修改入参
 */
@Data
public class FollowUpPlanSaveDTO implements Serializable {

    /** 老人ID */
    @NotNull(message = "老人ID不能为空")
    private Long elderId;

    /** 计划名称 */
    private String planName;

    /** 随访类型 */
    private Integer followUpType;

    /** 开始日期 */
    private LocalDate startDate;

    /** 结束日期 */
    private LocalDate endDate;

    /** 随访频率 */
    private String frequency;

    /** 随访内容 */
    private String content;

    /** 状态：1-进行中 0-已结束 */
    private Integer status;

}
