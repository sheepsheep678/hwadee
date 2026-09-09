package com.cdut.mapper;

import com.cdut.pojo.FollowUpPlan;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 随访计划 Mapper（原生 MyBatis）
 */
@Mapper
public interface FollowUpPlanMapper {

    @Select("<script>" +
            "SELECT * FROM follow_up_plan WHERE doctor_id = #{doctorId} AND is_deleted = 0" +
            "<if test='elderId != null'> AND elder_id = #{elderId}</if>" +
            "<if test='status != null'> AND status = #{status}</if>" +
            "<if test='planType != null'> AND plan_type = #{planType}</if>" +
            " ORDER BY create_time DESC" +
            "</script>")
    List<FollowUpPlan> selectPage(@Param("doctorId") Long doctorId,
                                  @Param("elderId") Long elderId,
                                  @Param("status") Integer status,
                                  @Param("planType") Integer planType);

    @Select("SELECT * FROM follow_up_plan WHERE id = #{id} AND is_deleted = 0")
    FollowUpPlan selectById(@Param("id") Long id);

    @Insert("INSERT INTO follow_up_plan (elder_id, doctor_id, plan_type, frequency, start_date, end_date, status) " +
            "VALUES (#{elderId}, #{doctorId}, #{planType}, #{frequency}, #{startDate}, #{endDate}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(FollowUpPlan plan);

    @Update("<script>" +
            "UPDATE follow_up_plan <set>" +
            "<if test='elderId != null'>elder_id = #{elderId},</if>" +
            "<if test='planType != null'>plan_type = #{planType},</if>" +
            "<if test='frequency != null'>frequency = #{frequency},</if>" +
            "<if test='startDate != null'>start_date = #{startDate},</if>" +
            "<if test='endDate != null'>end_date = #{endDate},</if>" +
            "<if test='status != null'>status = #{status},</if>" +
            "update_time = NOW()" +
            "</set> WHERE id = #{id} AND is_deleted = 0" +
            "</script>")
    int update(FollowUpPlan plan);

    @Update("UPDATE follow_up_plan SET is_deleted = 1 WHERE id = #{id}")
    int deleteById(@Param("id") Long id);
}
