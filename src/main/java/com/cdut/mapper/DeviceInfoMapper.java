package com.cdut.mapper;

import com.cdut.dto.DeviceQueryDTO;
import com.cdut.pojo.DeviceInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 设备台账 Mapper（原生 MyBatis，医生端只读）。
 * 查询 SQL 见 resources/mapper/DeviceInfoMapper.xml。
 */
@Mapper
public interface DeviceInfoMapper {

    /** 分页查询设备（联表填充绑定老人姓名/电话） */
    List<DeviceInfo> selectDevicePage(@Param("query") DeviceQueryDTO query);

    /** 查询设备详情（联表填充绑定老人信息） */
    DeviceInfo selectDeviceDetail(@Param("id") Long id);
}
