package com.cdut.mapper;

import com.cdut.pojo.FamilyContact;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 家属联系人 Mapper（原生 MyBatis，医生端档案管理使用）
 */
@Mapper
public interface FamilyContactMapper {

    @Insert("INSERT INTO family_contact (elder_id, name, relation, phone, is_primary, address, create_time) " +
            "VALUES (#{elderId}, #{name}, #{relation}, #{phone}, #{isPrimary}, #{address}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(FamilyContact contact);

    @Update("UPDATE family_contact SET is_deleted = 1 WHERE elder_id = #{elderId}")
    int deleteByElderId(@Param("elderId") Long elderId);

    @Select("SELECT * FROM family_contact WHERE elder_id = #{elderId} AND is_deleted = 0")
    List<FamilyContact> selectByElderId(@Param("elderId") Long elderId);
}
