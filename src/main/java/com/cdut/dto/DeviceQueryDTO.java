package com.cdut.dto;

import com.cdut.common.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 设备查询条件
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DeviceQueryDTO extends PageQuery implements Serializable {

    /** 设备类型 */
    private String deviceType;

    /** 状态：1-正常 2-维修 3-停用 */
    private Integer status;

    /** 在线状态：0-离线 1-在线 */
    private Integer onlineStatus;

    /** 绑定老人ID */
    private Long elderId;

    /** 关键词（设备名称/编号） */
    private String keyword;

}
