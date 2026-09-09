package com.cdut.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 人群标签表（elder_tag）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ElderTag {

    /** 主键ID */
    private Long id;

    /** 标签名称（高龄/失能/独居/失智/慢病等） */
    private String tagName;

    /** 标签级别：1-一般 2-重点 3-紧急 */
    private Integer tagLevel;

    /** 标签描述 */
    private String description;

    /** 状态：0-停用 1-启用 */
    private Integer status;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;

    /** 逻辑删除：0-未删除 1-已删除 */
    private Integer isDeleted;
}
