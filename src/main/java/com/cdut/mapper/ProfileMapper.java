package com.cdut.mapper;

import com.cdut.dto.ElderProfileDetailDTO;
import com.cdut.pojo.ElderProfile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ProfileMapper {
    @Select("select * from elder_profile where elder_no=#{elderId}")
    public ElderProfile selectElderProfileById(Integer elderId);
}
