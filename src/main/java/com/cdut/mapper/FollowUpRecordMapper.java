package com.cdut.mapper;

import com.cdut.pojo.FollowUpRecord;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 随访记录 Mapper（原生 MyBatis）
 */
@Mapper
public interface FollowUpRecordMapper {

    @Select("<script>" +
            "SELECT * FROM follow_up_record WHERE doctor_id = #{doctorId} AND is_deleted = 0" +
            "<if test='elderId != null'> AND elder_id = #{elderId}</if>" +
            "<if test='planId != null'> AND plan_id = #{planId}</if>" +
            "<if test='startDate != null'> AND follow_date &gt;= #{startDate}</if>" +
            "<if test='endDate != null'> AND follow_date &lt;= #{endDate}</if>" +
            " ORDER BY follow_date DESC" +
            "</script>")
    List<FollowUpRecord> selectPage(@Param("doctorId") Long doctorId,
                                    @Param("elderId") Long elderId,
                                    @Param("planId") Long planId,
                                    @Param("startDate") LocalDate startDate,
                                    @Param("endDate") LocalDate endDate);

    @Insert("INSERT INTO follow_up_record (plan_id, elder_id, doctor_id, follow_date, method, content, next_follow_date) " +
            "VALUES (#{planId}, #{elderId}, #{doctorId}, #{followDate}, #{method}, #{content}, #{nextFollowDate})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(FollowUpRecord record);
}
