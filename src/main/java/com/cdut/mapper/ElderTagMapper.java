package com.cdut.mapper;

import com.cdut.pojo.ElderTag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 人群标签 Mapper（原生 MyBatis）
 */
@Mapper
public interface ElderTagMapper {

    @Select("SELECT * FROM elder_tag WHERE is_deleted = 0 ORDER BY id ASC")
    List<ElderTag> selectAll();

    @Select("SELECT * FROM elder_tag WHERE id = #{id} AND is_deleted = 0")
    ElderTag selectById(@Param("id") Long id);

    @Select("<script>" +
            "SELECT * FROM elder_tag WHERE is_deleted = 0 AND id IN " +
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>#{id}</foreach>" +
            "</script>")
    List<ElderTag> selectByIds(@Param("ids") List<Long> ids);
}
