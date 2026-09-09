package com.cdut.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 设备信息
 */
@Data
@TableName("device_info")
public class DeviceInfo implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 设备名称 */
    private String deviceName;

    /** 设备类型 */
    private String deviceType;

    /** 设备编号 */
    private String deviceNo;

    /** 绑定老人ID */
    private Long elderId;

    /** 状态：1-正常 2-维修 3-停用 */
    private Integer status;

    /** 在线状态：0-离线 1-在线 */
    private Integer onlineStatus;

    /** 最后在线时间 */
    private LocalDateTime lastOnlineTime;

    /** 设备位置 */
    private String location;

    /** 备注 */
    private String remark;

    private LocalDateTime createTime;

    /** 绑定老人姓名（非表字段，详情/列表查询时联表填充） */
    @TableField(exist = false)
    private String elderName;

    /** 绑定老人联系电话（非表字段） */
    @TableField(exist = false)
    private String elderPhone;

}
