package com.cdut.mapper;

import com.cdut.dto.DoctorPerformanceDTO;
import com.cdut.pojo.ServiceRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

/**
 * 服务记录 Mapper（原生 MyBatis）。绩效统计 SQL 见 resources/mapper/ServiceRecordMapper.xml。
 */
@Mapper
public interface ServiceRecordMapper {

    @Select("<script>" +
            "SELECT * FROM service_record WHERE doctor_id = #{doctorId} AND is_deleted = 0" +
            "<if test='serviceType != null'> AND service_type = #{serviceType}</if>" +
            "<if test='elderId != null'> AND elder_id = #{elderId}</if>" +
            "<if test='startDate != null'> AND service_date &gt;= #{startDate}</if>" +
            "<if test='endDate != null'> AND service_date &lt;= #{endDate}</if>" +
            " ORDER BY service_date DESC" +
            "</script>")
    List<ServiceRecord> selectPage(@Param("doctorId") Long doctorId,
                                   @Param("serviceType") Integer serviceType,
                                   @Param("elderId") Long elderId,
                                   @Param("startDate") LocalDate startDate,
                                   @Param("endDate") LocalDate endDate);

    /** 统计医生绩效（服务数/服务老人数/随访数/平均评分） */
    DoctorPerformanceDTO selectPerformance(@Param("doctorId") Long doctorId,
                                           @Param("startDate") LocalDate startDate,
                                           @Param("endDate") LocalDate endDate);
}
