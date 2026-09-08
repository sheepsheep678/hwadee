package com.cdut.controller;

import com.cdut.dto.DeviceQueryDTO;
import com.cdut.pojo.PageResult;
import com.cdut.pojo.Result;
import com.cdut.service.DevicesService;
import com.github.pagehelper.PageInfo;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/elder")
public class DevicesController {

    @Autowired
    private DevicesService deviceService;

    @GetMapping("/devices")
    public Result<List<DeviceQueryDTO>> queryAllDevices(@RequestParam Integer elderId) {
        return Result.success(deviceService.getDevices(elderId));
    }

    @GetMapping("/devices/page")
    public PageResult<DeviceQueryDTO> list(@RequestParam(defaultValue = "1") int pageNum,
                                    @RequestParam(defaultValue = "10") int pageSize,
                                    @RequestParam(required = false) Integer elderId) {
        PageInfo<DeviceQueryDTO> pageInfo = deviceService.listByPage(pageNum, pageSize, elderId);

        return PageResult.of(pageInfo.getList(), pageInfo.getTotal());
    }
}
