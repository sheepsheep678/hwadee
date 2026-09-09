package com.cdut.mapper;

import com.cdut.pojo.AssessmentTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 评估模板 Mapper（原生 MyBatis）
 */
@Mapper
public interface AssessmentTemplateMapper {

    @Select("<script>" +
            "SELECT * FROM assessment_template WHERE status = #{status} AND is_deleted = 0" +
            "<if test='assessType != null'> AND assess_type = #{assessType}</if>" +
            " ORDER BY id DESC" +
            "</script>")
    List<AssessmentTemplate> selectByTypeAndStatus(@Param("assessType") Integer assessType,
                                                   @Param("status") Integer status);
}
