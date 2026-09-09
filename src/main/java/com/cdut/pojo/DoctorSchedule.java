package com.cdut.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 医生排班表（doctor_schedule）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorSchedule {

    /** 主键ID */
    private Long id;

    /** 医生ID */
    private Long doctorId;

    /** 排班日期 */
    private LocalDate scheduleDate;

    /** 时间段：1-上午 2-下午 3-夜间 */
    private Integer timeSlot;

    /** 排班类型：1-门诊 2-上门 3-值班 */
    private Integer scheduleType;

    /** 可接待容量 */
    private Integer capacity;

    /** 已预约数 */
    private Integer bookedCount;

    /** 状态：1-正常 2-已取消 */
    private Integer status;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;

    /** 逻辑删除：0-未删除 1-已删除 */
    private Integer isDeleted;
}
