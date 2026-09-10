package com.cdut.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 老人端评估报告出参
 *
 * <p>只暴露老人本人需要查看的字段，刻意不包含 assessorId、resultJson、isDeleted。
 */
@Data
public class ElderAssessmentReportVO {

    /** 报告ID */
    private Long id;

    /** 报告编号 */
    private String reportNo;

    /** 评估类型：1-老年能力 2-健康风险 3-中医体质 4-膳食营养 */
    private Integer assessType;

    /** 评估总分 */
    private BigDecimal score;

    /** 评估等级 */
    private String grade;

    /** 评估日期 */
    private LocalDate assessDate;

    /** 评估结论 */
    private String conclusion;
}
