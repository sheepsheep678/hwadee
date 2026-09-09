package com.cdut.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 服务记录查询条件
 */
@Data
public class ServiceRecordQueryDTO implements Serializable {

    /** 服务类型 */
    private Integer serviceType;

    /** 老人ID */
    private Long elderId;

    /** 开始日期 */
    private LocalDate startDate;

    /** 结束日期 */
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
