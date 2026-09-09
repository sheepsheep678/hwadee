package com.cdut.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 评估报告表（assessment_report）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentReport {

    /** 主键ID */
    private Long id;

    /** 报告编号 */
    private String reportNo;

    /** 老人ID */
    private Long elderId;

    /** 评估类型：1-老年能力 2-健康风险 3-中医体质 4-膳食营养 */
    private Integer assessType;

    /** 模板ID */
    private Long templateId;

    /** 总分 */
    private BigDecimal score;

    /** 评估等级 */
    private String grade;

    /** 各维度明细得分（JSON） */
    private String resultJson;

    /** 评估结论与建议 */
    private String conclusion;

    /** 评估医生ID */
    private Long assessorId;

    /** 评估日期 */
    private LocalDate assessDate;

    /** PDF报告地址 */
    private String reportUrl;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;

    /** 逻辑删除：0-未删除 1-已删除 */
    private Integer isDeleted;
}
