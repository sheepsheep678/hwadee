package com.cdut.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 医生服务记录
 */
@Data
@TableName("service_record")
public class ServiceRecord implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 老人ID */
    private Long elderId;

    /** 医生ID */
    private Long doctorId;

    /** 服务类型 */
    private Integer serviceType;

    /** 服务日期 */
    private LocalDate serviceDate;

    /** 服务内容 */
    private String content;

    /** 评分（1-5，老人评价） */
    private Integer rating;

    /** 评价备注 */
    private String remark;

    /** 状态：1-已完成 0-进行中 */
    private Integer status;

    private LocalDateTime createTime;

}
