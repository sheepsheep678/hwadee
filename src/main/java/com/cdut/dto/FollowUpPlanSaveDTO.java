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

    /** 计划类型：1-电话 2-上门 3-远程问诊 */
    private Integer planType;

    /** 开始日期 */
    private LocalDate startDate;

    /** 结束日期 */
    private LocalDate endDate;

    /** 随访频次 */
    private String frequency;

    /** 执行状态：1-进行中 2-已完成 3-已终止 */
    private Integer status;

}
