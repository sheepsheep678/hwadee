package com.cdut.service;

import com.cdut.common.PageInfo;
import com.cdut.dto.DeviceQueryDTO;
import com.cdut.entity.DeviceInfo;

/**
 * 设备查看服务（医生端只读）
 */
public interface DeviceService {

    /** 分页查看设备 */
    PageInfo<DeviceInfo> page(DeviceQueryDTO query);

    /** 查看设备详情（含绑定老人信息） */
    DeviceInfo detail(Long id);

}
