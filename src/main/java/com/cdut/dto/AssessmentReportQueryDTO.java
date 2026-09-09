package com.cdut.dto;

import com.cdut.common.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 评估报告查询条件
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AssessmentReportQueryDTO extends PageQuery implements Serializable {

    /** 老人ID */
    private Long elderId;

    /** 评估类型 */
    private Integer assessType;

    /** 评估医生ID */
    private Long doctorId;

    /** 评估日期起 */
    private LocalDate startDate;

    /** 评估日期止 */
    private LocalDate endDate;

}
