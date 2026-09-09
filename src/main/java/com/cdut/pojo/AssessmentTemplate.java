package com.cdut.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 评估模板表（assessment_template）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentTemplate {

    /** 主键ID */
    private Long id;

    /** 模板名称 */
    private String templateName;

    /** 评估类型：1-老年能力 2-健康风险 3-中医体质 4-膳食营养 */
    private Integer assessType;

    /** 版本号 */
    private String version;

    /** 题目与评分规则（JSON） */
    private String itemsJson;

    /** 状态：0-停用 1-启用 */
    private Integer status;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;

    /** 逻辑删除：0-未删除 1-已删除 */
    private Integer isDeleted;
}
