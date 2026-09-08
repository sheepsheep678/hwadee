package com.cdut.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TagMapper {
    @Select("select tag_name from elder_tag where id=#{id}")
    public List<String> selectTagNameById(Integer id);


}
