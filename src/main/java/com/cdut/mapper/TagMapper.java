package com.cdut.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TagMapper {

    /**
     * 查某位老人身上的标签名
     *
     * <p>修正：原 SQL 是 select tag_name from elder_tag where id = #{id}，
     * 把「老人ID」当成「标签ID」用了，必须经中间表 elder_tag_relation 关联。
     */
    @Select("SELECT t.tag_name FROM elder_tag t " +
            "INNER JOIN elder_tag_relation r ON r.tag_id = t.id " +
            "WHERE r.elder_id = #{elderId} AND t.is_deleted = 0")
    List<String> selectTagNameByElderId(@Param("elderId") Long elderId);
}
