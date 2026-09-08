package com.cdut.mapper;


import com.cdut.pojo.FamilyContact;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FamilyMapper {
    @Select("select * from family_contact where elder_id = #{elderId} and is_deleted = 0")
    List<FamilyContact> selectFamilyContactById(@Param("elderId") Long elderId);
}
