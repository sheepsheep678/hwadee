package com.cdut.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * 评估结果提交入参
 */
@Data
public class AssessmentSubmitDTO implements Serializable {

    /** 老人ID */
    @NotNull(message = "老人ID不能为空")
    private Long elderId;

    /** 模板ID */
    @NotNull(message = "模板ID不能为空")
    private Long templateId;

    /** 评估类型 */
    @NotNull(message = "评估类型不能为空")
    private Integer assessType;

    /** 评估日期 */
    @NotNull(message = "评估日期不能为空")
    private LocalDate assessDate;

    /** 答案列表 */
    @Valid
    @NotEmpty(message = "评估答案不能为空")
    private List<AnswerItem> answers;

}
