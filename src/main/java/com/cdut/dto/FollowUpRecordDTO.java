package com.cdut.dto;

import com.cdut.common.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 随访记录：新增入参 / 查询条件复用。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FollowUpRecordDTO extends PageQuery implements Serializable {

    /** 随访计划ID */
    private Long planId;

    /** 老人ID */
    private Long elderId;

    /** 随访日期 */
    private LocalDate followUpDate;

    /** 随访内容 */
    private String content;

    /** 随访结果 */
    private String result;

    /** 下次随访日期 */
    private LocalDate nextFollowUpDate;

    /** 查询：日期起 */
    private LocalDate startDate;

    /** 查询：日期止 */
    private LocalDate endDate;

}
