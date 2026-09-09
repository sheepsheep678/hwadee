package com.cdut.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cdut.dto.DeviceQueryDTO;
import com.cdut.entity.DeviceInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DeviceInfoMapper extends BaseMapper<DeviceInfo> {

    /** 分页查询设备（联表填充绑定老人姓名/电话） */
    IPage<DeviceInfo> selectDevicePage(Page<DeviceInfo> page, @Param("query") DeviceQueryDTO query);

    /** 查询设备详情（联表填充绑定老人信息） */
    DeviceInfo selectDeviceDetail(@Param("id") Long id);

}
