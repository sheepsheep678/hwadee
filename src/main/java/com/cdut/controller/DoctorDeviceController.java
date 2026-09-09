package com.cdut.controller;

import com.cdut.common.PageInfo;
import com.cdut.common.Result;
import com.cdut.dto.DeviceQueryDTO;
import com.cdut.entity.DeviceInfo;
import com.cdut.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 医生端 · 设备查看接口（只读）
 */
@RestController
@RequestMapping("/api/doctor/devices")
@RequiredArgsConstructor
public class DoctorDeviceController {

    private final DeviceService deviceService;

    /** 分页查看设备 */
    @GetMapping
    public Result<PageInfo<DeviceInfo>> page(DeviceQueryDTO query) {
        return Result.success(deviceService.page(query));
    }

    /** 查看设备详情（含绑定老人信息） */
    @GetMapping("/{id}")
    public Result<DeviceInfo> detail(@PathVariable Long id) {
        return Result.success(deviceService.detail(id));
    }

}
