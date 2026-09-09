package com.cdut.service.impl;

import com.cdut.dto.DeviceQueryDTO;
import com.cdut.exception.BizException;
import com.cdut.mapper.DeviceInfoMapper;
import com.cdut.pojo.DeviceInfo;
import com.cdut.pojo.PageResult;
import com.cdut.service.DeviceService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 设备查看服务实现（医生端只读）。
 */
@Service
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {

    private final DeviceInfoMapper deviceInfoMapper;

    @Override
    public PageResult<DeviceInfo> page(DeviceQueryDTO query) {
        PageHelper.startPage(query.safePageNum(), query.safePageSize());
        List<DeviceInfo> list = deviceInfoMapper.selectDevicePage(query);
        PageInfo<DeviceInfo> pageInfo = new PageInfo<>(list);
        return PageResult.of(pageInfo.getList(), pageInfo.getTotal(), query.safePageNum(), query.safePageSize());
    }

    @Override
    public DeviceInfo detail(Long id) {
        DeviceInfo device = deviceInfoMapper.selectDeviceDetail(id);
        if (device == null) {
            throw new BizException("设备不存在");
        }
        return device;
    }

}
