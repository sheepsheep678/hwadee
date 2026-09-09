package com.cdut.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 评估模板
 */
@Data
@TableName("assessment_template")
public class AssessmentTemplate implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 模板名称 */
    private String templateName;

    /** 评估类型 */
    private Integer assessType;

    /** 描述 */
    private String description;

    /** 评估题目（JSON） */
    private String itemsJson;

    /** 状态：1-启用 0-停用 */
    private Integer status;

    private LocalDateTime createTime;

}
