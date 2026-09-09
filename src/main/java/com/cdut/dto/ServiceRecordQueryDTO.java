package com.cdut.dto;

import com.cdut.common.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 服务记录查询条件
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ServiceRecordQueryDTO extends PageQuery implements Serializable {

    /** 服务类型 */
    private Integer serviceType;

    /** 老人ID */
    private Long elderId;

    /** 开始日期 */
    private LocalDate startDate;

    /** 结束日期 */
    private LocalDate endDate;

}
