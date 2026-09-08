package com.cdut.mapper;


import com.cdut.pojo.FamilyContact;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FamilyMapper {
    @Select("select * from family_contact where elder_id = #{elderId}")
    public List<FamilyContact> selectFamilyContactById(int elderId);
}
