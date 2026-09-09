package com.cdut.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 评估答案项
 */
@Data
public class AnswerItem implements Serializable {

    /** 题目ID */
    private Long itemId;

    /** 答案内容 */
    private String answer;

    /** 得分 */
    private BigDecimal score;

}
