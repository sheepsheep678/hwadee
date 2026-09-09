package com.cdut.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cdut.common.BusinessException;
import com.cdut.common.PageInfo;
import com.cdut.dto.DeviceQueryDTO;
import com.cdut.entity.DeviceInfo;
import com.cdut.mapper.DeviceInfoMapper;
import com.cdut.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 设备查看服务实现（医生端只读）。
 */
@Service
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {

    private final DeviceInfoMapper deviceInfoMapper;

    @Override
    public PageInfo<DeviceInfo> page(DeviceQueryDTO query) {
        Page<DeviceInfo> page = new Page<>(query.safePageNum(), query.safePageSize());
        return PageInfo.of(deviceInfoMapper.selectDevicePage(page, query));
    }

    @Override
    public DeviceInfo detail(Long id) {
        DeviceInfo device = deviceInfoMapper.selectDeviceDetail(id);
        if (device == null) {
            throw new BusinessException("设备不存在");
        }
        return device;
    }

}
