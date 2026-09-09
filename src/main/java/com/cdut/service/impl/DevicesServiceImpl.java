package com.cdut.service.impl;

import com.cdut.dto.DeviceQueryDTO;
import com.cdut.mapper.DevicesMapper;
import com.cdut.pojo.DeviceInfo;
import com.cdut.pojo.PageResult;
import com.cdut.service.DevicesService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.catalina.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DevicesServiceImpl implements DevicesService {

    @Autowired
    private DevicesMapper devicesMapper;
    @Override
    public List<DeviceQueryDTO> getDevices(Integer elderId) {
        List<DeviceInfo> deviceInfoList = devicesMapper.selectDevicesById(elderId);
        List<DeviceQueryDTO> deviceQueryDTOList = new ArrayList<>();
        for (DeviceInfo deviceInfo : deviceInfoList) {
            DeviceQueryDTO deviceQueryDTO = new DeviceQueryDTO();
            BeanUtils.copyProperties(deviceInfo, deviceQueryDTO);
            deviceQueryDTOList.add(deviceQueryDTO);
        }
        return deviceQueryDTOList;
    }

    @Override
    public PageInfo<DeviceQueryDTO> listByPage(int pageNum, int pageSize, Integer elderId) {
        // 1. 开启分页：只对"紧随其后"的第一条查询生效
        PageHelper.startPage(pageNum, pageSize);
        // 2. 紧接着执行 Mapper 查询，PageHelper 会自动重写 SQL 加上 LIMIT
        List<DeviceInfo> deviceList = devicesMapper.selectDevicesById(elderId);
        // 3. 用 PageInfo 包装，自动包含 total、pages、navigatepageNums 等
        List<DeviceQueryDTO> deviceQueryDTOList = new ArrayList<>();
        for (DeviceInfo deviceInfo : deviceList) {
            DeviceQueryDTO deviceQueryDTO = new DeviceQueryDTO();
            BeanUtils.copyProperties(deviceInfo, deviceQueryDTO);
            deviceQueryDTOList.add(deviceQueryDTO);
        }
        return new PageInfo<>(deviceQueryDTOList);
    }
}
