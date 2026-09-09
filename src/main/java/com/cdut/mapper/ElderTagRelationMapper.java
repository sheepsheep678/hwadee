package com.cdut.mapper;

import com.cdut.pojo.ElderTagRelation;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 老人标签关联 Mapper（原生 MyBatis，中间表无逻辑删除列，直接物理删除）
 */
@Mapper
public interface ElderTagRelationMapper {

    @Insert("INSERT INTO elder_tag_relation (elder_id, tag_id) VALUES (#{elderId}, #{tagId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(ElderTagRelation relation);

    @Delete("DELETE FROM elder_tag_relation WHERE elder_id = #{elderId} AND tag_id = #{tagId}")
    int deleteByElderAndTag(@Param("elderId") Long elderId, @Param("tagId") Long tagId);

    @Delete("DELETE FROM elder_tag_relation WHERE elder_id = #{elderId}")
    int deleteByElderId(@Param("elderId") Long elderId);

    @Select("SELECT * FROM elder_tag_relation WHERE elder_id = #{elderId}")
    List<ElderTagRelation> selectByElderId(@Param("elderId") Long elderId);

    @Select("SELECT COUNT(*) FROM elder_tag_relation WHERE elder_id = #{elderId} AND tag_id = #{tagId}")
    long countByElderAndTag(@Param("elderId") Long elderId, @Param("tagId") Long tagId);
}
