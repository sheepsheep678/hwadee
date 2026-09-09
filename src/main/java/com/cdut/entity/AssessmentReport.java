package com.cdut.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 评估报告
 */
@Data
@TableName("assessment_report")
public class AssessmentReport implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 老人ID */
    private Long elderId;

    /** 模板ID */
    private Long templateId;

    /** 评估类型 */
    private Integer assessType;

    /** 评估日期 */
    private LocalDate assessDate;

    /** 总分 */
    private BigDecimal totalScore;

    /** 评估等级 */
    private String level;

    /** 评估结论 */
    private String conclusion;

    /** 报告文件地址 */
    private String reportUrl;

    /** 评估医生ID */
    private Long doctorId;

    private LocalDateTime createTime;

    @TableLogic
    private Integer deleted;

}
