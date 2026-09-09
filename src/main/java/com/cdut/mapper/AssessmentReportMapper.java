package com.cdut.mapper;

import com.cdut.pojo.AssessmentReport;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 评估报告 Mapper（原生 MyBatis）
 */
@Mapper
public interface AssessmentReportMapper {

    @Select("<script>" +
            "SELECT * FROM assessment_report WHERE is_deleted = 0" +
            "<if test='elderId != null'> AND elder_id = #{elderId}</if>" +
            "<if test='assessType != null'> AND assess_type = #{assessType}</if>" +
            "<if test='assessorId != null'> AND assessor_id = #{assessorId}</if>" +
            "<if test='startDate != null'> AND assess_date &gt;= #{startDate}</if>" +
            "<if test='endDate != null'> AND assess_date &lt;= #{endDate}</if>" +
            " ORDER BY create_time DESC" +
            "</script>")
    List<AssessmentReport> selectPage(@Param("elderId") Long elderId,
                                      @Param("assessType") Integer assessType,
                                      @Param("assessorId") Long assessorId,
                                      @Param("startDate") LocalDate startDate,
                                      @Param("endDate") LocalDate endDate);

    @Select("SELECT * FROM assessment_report WHERE id = #{id} AND is_deleted = 0")
    AssessmentReport selectById(@Param("id") Long id);

    @Insert("INSERT INTO assessment_report (report_no, elder_id, assess_type, template_id, score, grade, result_json, " +
            "conclusion, assessor_id, assess_date, report_url) " +
            "VALUES (#{reportNo}, #{elderId}, #{assessType}, #{templateId}, #{score}, #{grade}, #{resultJson}, " +
            "#{conclusion}, #{assessorId}, #{assessDate}, #{reportUrl})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(AssessmentReport report);

    @Update("UPDATE assessment_report SET is_deleted = 1 WHERE id = #{id}")
    int deleteById(@Param("id") Long id);
}
