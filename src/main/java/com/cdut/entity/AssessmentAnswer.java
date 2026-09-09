package com.cdut.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 评估答案明细
 */
@Data
@TableName("assessment_answer")
public class AssessmentAnswer implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 报告ID */
    private Long reportId;

    /** 题目ID */
    private Long itemId;

    /** 答案内容 */
    private String answer;

    /** 得分 */
    private BigDecimal score;

    private LocalDateTime createTime;

}
