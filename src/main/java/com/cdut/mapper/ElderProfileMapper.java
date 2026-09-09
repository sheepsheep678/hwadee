package com.cdut.mapper;

import com.cdut.dto.ElderProfileQueryDTO;
import com.cdut.pojo.ElderProfile;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 老人档案 Mapper（原生 MyBatis，医生端档案管理）。
 * 分页查询 SQL 见 resources/mapper/ElderProfileMapper.xml。
 */
@Mapper
public interface ElderProfileMapper {

    /** 分页查询老人档案，支持按姓名/身份证/电话/居住类型/标签ID 过滤 */
    List<ElderProfile> selectProfilePage(@Param("query") ElderProfileQueryDTO query);

    @Select("SELECT * FROM elder_profile WHERE id = #{id} AND is_deleted = 0")
    ElderProfile selectById(@Param("id") Long id);

    @Insert("INSERT INTO elder_profile (elder_no, account_id, name, gender, birth_date, id_card, phone, living_type, " +
            "marital_status, education, address, medicare_no, gov_aid_type, photo_url, status, create_time, update_time) " +
            "VALUES (#{elderNo}, #{accountId}, #{name}, #{gender}, #{birthDate}, #{idCard}, #{phone}, #{livingType}, " +
            "#{maritalStatus}, #{education}, #{address}, #{medicareNo}, #{govAidType}, #{photoUrl}, #{status}, #{createTime}, #{updateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(ElderProfile profile);

    @Update("<script>" +
            "UPDATE elder_profile <set>" +
            "<if test='elderNo != null'>elder_no = #{elderNo},</if>" +
            "<if test='name != null'>name = #{name},</if>" +
            "<if test='gender != null'>gender = #{gender},</if>" +
            "<if test='birthDate != null'>birth_date = #{birthDate},</if>" +
            "<if test='idCard != null'>id_card = #{idCard},</if>" +
            "<if test='phone != null'>phone = #{phone},</if>" +
            "<if test='livingType != null'>living_type = #{livingType},</if>" +
            "<if test='maritalStatus != null'>marital_status = #{maritalStatus},</if>" +
            "<if test='education != null'>education = #{education},</if>" +
            "<if test='address != null'>address = #{address},</if>" +
            "<if test='medicareNo != null'>medicare_no = #{medicareNo},</if>" +
            "<if test='govAidType != null'>gov_aid_type = #{govAidType},</if>" +
            "<if test='photoUrl != null'>photo_url = #{photoUrl},</if>" +
            "<if test='status != null'>status = #{status},</if>" +
            "update_time = #{updateTime}" +
            "</set> WHERE id = #{id} AND is_deleted = 0" +
            "</script>")
    int update(ElderProfile profile);

    @Update("UPDATE elder_profile SET is_deleted = 1 WHERE id = #{id}")
    int deleteById(@Param("id") Long id);

    @Select("<script>" +
            "SELECT COUNT(*) FROM elder_profile WHERE id_card = #{idCard} AND is_deleted = 0" +
            "<if test='excludeId != null'> AND id != #{excludeId}</if>" +
            "</script>")
    long countByIdCard(@Param("idCard") String idCard, @Param("excludeId") Long excludeId);
}
