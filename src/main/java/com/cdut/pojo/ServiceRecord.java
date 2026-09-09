package com.cdut.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 服务记录表（service_record）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceRecord {

    /** 主键ID */
    private Long id;

    /** 老人ID */
    private Long elderId;

    /** 服务人员ID */
    private Long doctorId;

    /** 服务类型：1-居家上门 2-机构服务 3-远程问诊 */
    private Integer serviceType;

    /** 服务项目 */
    private String serviceItems;

    /** 服务时间 */
    private LocalDateTime serviceDate;

    /** 服务时长（分钟） */
    private Integer duration;

    /** 服务费用 */
    private BigDecimal fee;

    /** 状态：1-待服务 2-已完成 3-已取消 */
    private Integer status;

    /** 备注 */
    private String remark;

    /** 老人评价：1-5星 */
    private Integer rating;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;

    /** 逻辑删除：0-未删除 1-已删除 */
    private Integer isDeleted;
}
