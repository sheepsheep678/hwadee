package com.cdut.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 老人标签（重点人群分类标签）
 */
@Data
@TableName("elder_tag")
public class ElderTag implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 标签名称 */
    private String tagName;

    /** 标签类型 */
    private Integer tagType;

    /** 描述 */
    private String description;

    /** 标签颜色 */
    private String color;

    private LocalDateTime createTime;

}
