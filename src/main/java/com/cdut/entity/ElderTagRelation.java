package com.cdut.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 老人-标签关联（重点人群）
 */
@Data
@TableName("elder_tag_relation")
public class ElderTagRelation implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 老人ID */
    private Long elderId;

    /** 标签ID */
    private Long tagId;

    private LocalDateTime createTime;

}
