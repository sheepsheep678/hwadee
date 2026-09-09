package com.cdut.mapper;

import com.cdut.dto.DeviceQueryDTO;
import com.cdut.pojo.DeviceInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Mapper
public interface DevicesMapper {
    @Select("SELECT * FROM device_binding WHERE elder_id = #{elderId}")
    public List<DeviceInfo> selectDevicesById(Integer elderId);

}
