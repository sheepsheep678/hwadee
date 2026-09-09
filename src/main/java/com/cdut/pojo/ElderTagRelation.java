package com.cdut.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 老人标签关联表（elder_tag_relation，多对多中间表）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ElderTagRelation {

    /** 主键ID */
    private Long id;

    /** 老人ID */
    private Long elderId;

    /** 标签ID */
    private Long tagId;
}
