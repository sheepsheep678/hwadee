package com.cdut.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 医生排班
 */
@Data
@TableName("doctor_schedule")
public class DoctorSchedule implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 医生ID */
    private Long doctorId;

    /** 排班日期 */
    private LocalDate scheduleDate;

    /** 班次：1-早班 2-中班 3-晚班 */
    private Integer shiftType;

    /** 开始时间 */
    private LocalTime startTime;

    /** 结束时间 */
    private LocalTime endTime;

    /** 备注 */
    private String remark;

}
