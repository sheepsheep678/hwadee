package com.cdut.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 随访记录：新增入参 / 查询条件复用。
 */
@Data
public class FollowUpRecordDTO implements Serializable {

    /** 随访计划ID */
    private Long planId;

    /** 老人ID */
    private Long elderId;

    /** 随访日期 */
    private LocalDate followDate;

    /** 随访方式：1-电话 2-上门 3-远程问诊 */
    private Integer method;

    /** 随访内容 */
    private String content;

    /** 下次随访日期 */
    private LocalDate nextFollowDate;

    /** 查询：日期起 */
    private LocalDate startDate;

    /** 查询：日期止 */
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
