package com.cdut.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 评估报告查询条件
 */
@Data
public class AssessmentReportQueryDTO implements Serializable {

    /** 老人ID */
    private Long elderId;

    /** 评估类型 */
    private Integer assessType;

    /** 评估医生ID */
    private Long assessorId;

    /** 评估日期起 */
    private LocalDate startDate;

    /** 评估日期止 */
    private LocalDate endDate;

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
