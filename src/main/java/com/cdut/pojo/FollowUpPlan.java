package com.cdut.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 随访计划表（follow_up_plan）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowUpPlan {

    /** 主键ID */
    private Long id;

    /** 老人ID */
    private Long elderId;

    /** 责任医生ID */
    private Long doctorId;

    /** 计划类型：1-电话 2-上门 3-远程问诊 */
    private Integer planType;

    /** 频次（如每周1次） */
    private String frequency;

    /** 计划开始日期 */
    private LocalDate startDate;

    /** 计划结束日期 */
    private LocalDate endDate;

    /** 执行状态：1-进行中 2-已完成 3-已终止 */
    private Integer status;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;

    /** 逻辑删除：0-未删除 1-已删除 */
    private Integer isDeleted;
}
