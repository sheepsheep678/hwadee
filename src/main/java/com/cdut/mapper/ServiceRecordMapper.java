package com.cdut.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cdut.dto.DoctorPerformanceDTO;
import com.cdut.entity.ServiceRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;

@Mapper
public interface ServiceRecordMapper extends BaseMapper<ServiceRecord> {

    /** 统计医生绩效（服务数/服务老人数/随访数/平均评分） */
    DoctorPerformanceDTO selectPerformance(@Param("doctorId") Long doctorId,
                                           @Param("startDate") LocalDate startDate,
                                           @Param("endDate") LocalDate endDate);

}
