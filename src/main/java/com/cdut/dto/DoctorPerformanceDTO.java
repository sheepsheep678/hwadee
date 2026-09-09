package com.cdut.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 医生绩效统计
 */
@Data
public class DoctorPerformanceDTO implements Serializable {

    /** 服务次数 */
    private Long serviceCount;

    /** 服务老人数 */
    private Long elderCount;

    /** 随访次数 */
    private Long followUpCount;

    /** 平均评分 */
    private BigDecimal avgRating;

}
