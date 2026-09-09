package com.cdut.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 老人健康档案
 */
@Data
@TableName("health_record")
public class HealthRecord implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 老人ID */
    private Long elderId;

    /** 记录类型 */
    private Integer recordType;

    /** 身高(cm) */
    private BigDecimal height;

    /** 体重(kg) */
    private BigDecimal weight;

    /** 血压 */
    private String bloodPressure;

    /** 心率 */
    private Integer heartRate;

    /** 血糖 */
    private BigDecimal bloodSugar;

    /** 记录日期 */
    private LocalDate recordDate;

    /** 描述 */
    private String description;

    private LocalDateTime createTime;

}
