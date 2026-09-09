package com.cdut.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 给老人打标签入参
 */
@Data
public class ElderTagBindDTO implements Serializable {

    /** 老人ID */
    @NotNull(message = "老人ID不能为空")
    private Long elderId;

    /** 标签ID */
    @NotNull(message = "标签ID不能为空")
    private Long tagId;

}
