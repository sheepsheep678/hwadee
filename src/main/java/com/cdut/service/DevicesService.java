package com.cdut.service;

import com.cdut.dto.DeviceQueryDTO;
import com.cdut.pojo.PageResult;

import java.util.List;

public interface DevicesService {
    List<DeviceQueryDTO> getDevices(Integer elderId);

}
