package com.cdut.mapper;

import com.cdut.pojo.DoctorSchedule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

/**
 * 医生排班 Mapper（原生 MyBatis）
 */
@Mapper
public interface DoctorScheduleMapper {

    @Select("<script>" +
            "SELECT * FROM doctor_schedule WHERE doctor_id = #{doctorId} AND is_deleted = 0" +
            "<if test='startDate != null'> AND schedule_date &gt;= #{startDate}</if>" +
            "<if test='endDate != null'> AND schedule_date &lt;= #{endDate}</if>" +
            " ORDER BY schedule_date ASC, time_slot ASC" +
            "</script>")
    List<DoctorSchedule> selectByDoctorAndDateRange(@Param("doctorId") Long doctorId,
                                                    @Param("startDate") LocalDate startDate,
                                                    @Param("endDate") LocalDate endDate);
}
