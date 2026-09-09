package com.cdut.service;

import com.cdut.dto.DeviceQueryDTO;
import com.cdut.pojo.DeviceInfo;
import com.cdut.pojo.PageResult;

/**
 * 设备查看服务（医生端只读）
 */
public interface DeviceService {

    /** 分页查看设备 */
    PageResult<DeviceInfo> page(DeviceQueryDTO query);

    /** 查看设备详情（含绑定老人信息） */
    DeviceInfo detail(Long id);

}
