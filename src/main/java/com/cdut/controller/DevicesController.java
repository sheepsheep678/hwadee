package com.cdut.controller;

import com.cdut.dto.DeviceQueryDTO;
import com.cdut.pojo.Result;
import com.cdut.service.DevicesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/elder")
public class DevicesController {

    @Autowired
    private DevicesService deviceService;

    @GetMapping("/devices")
    public Result<List<DeviceQueryDTO>> queryDevices(@RequestParam Integer elderId) {
        return Result.success(deviceService.getDevices(elderId));
    }
}
