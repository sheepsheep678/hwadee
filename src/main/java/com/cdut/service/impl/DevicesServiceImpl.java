package com.cdut.service.impl;

import com.cdut.dto.DeviceQueryDTO;
import com.cdut.mapper.DevicesMapper;
import com.cdut.pojo.DeviceInfo;
import com.cdut.pojo.PageResult;
import com.cdut.service.DevicesService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
}
