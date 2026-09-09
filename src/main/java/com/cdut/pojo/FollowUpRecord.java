package com.cdut.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 随访记录表（follow_up_record）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowUpRecord {

    /** 主键ID */
    private Long id;

    /** 随访计划ID */
    private Long planId;

    /** 老人ID */
    private Long elderId;

    /** 随访医生ID */
    private Long doctorId;

    /** 随访日期 */
    private LocalDate followDate;

    /** 随访方式：1-电话 2-上门 3-远程问诊 */
    private Integer method;

    /** 随访内容 */
    private String content;

    /** 下次随访日期 */
    private LocalDate nextFollowDate;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;

    /** 逻辑删除：0-未删除 1-已删除 */
    private Integer isDeleted;
}
