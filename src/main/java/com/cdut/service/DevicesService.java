package com.cdut.service;

import com.cdut.dto.DeviceQueryDTO;
import com.cdut.pojo.PageResult;
import com.github.pagehelper.PageInfo;
import org.apache.catalina.User;

import java.util.List;

public interface DevicesService {
    List<DeviceQueryDTO> getDevices(Integer elderId);

    PageInfo<DeviceQueryDTO> listByPage(int pageNum, int pageSize, Integer elderId);
}
