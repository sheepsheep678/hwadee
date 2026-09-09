package com.cdut.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 随访记录
 */
@Data
@TableName("follow_up_record")
public class FollowUpRecord implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 随访计划ID */
    private Long planId;

    /** 老人ID */
    private Long elderId;

    /** 医生ID */
    private Long doctorId;

    /** 随访日期 */
    private LocalDate followUpDate;

    /** 随访内容 */
    private String content;

    /** 随访结果 */
    private String result;

    /** 下次随访日期 */
    private LocalDate nextFollowUpDate;

    private LocalDateTime createTime;

}
