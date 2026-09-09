package com.cdut.mapper;

import com.cdut.pojo.DoctorAccount;
import org.apache.ibatis.annotations.*;

/**
 * 医生账户 Mapper（原生 MyBatis）
 */
@Mapper
public interface DoctorAccountMapper {

    @Select("SELECT * FROM doctor_account WHERE phone = #{phone} AND is_deleted = 0 LIMIT 1")
    DoctorAccount selectByPhone(@Param("phone") String phone);

    @Select("SELECT * FROM doctor_account WHERE id = #{id} AND is_deleted = 0")
    DoctorAccount selectById(@Param("id") Long id);

    @Select("SELECT COUNT(*) FROM doctor_account WHERE phone = #{phone} AND is_deleted = 0")
    long countByPhone(@Param("phone") String phone);

    @Insert("INSERT INTO doctor_account (name, phone, password, doctor_type, title, dept, org_id, audit_status, account_status) " +
            "VALUES (#{name}, #{phone}, #{password}, #{doctorType}, #{title}, #{dept}, #{orgId}, #{auditStatus}, #{accountStatus})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(DoctorAccount account);

    @Update("<script>" +
            "UPDATE doctor_account <set>" +
            "<if test='phone != null'>phone = #{phone},</if>" +
            "<if test='name != null'>name = #{name},</if>" +
            "<if test='password != null'>password = #{password},</if>" +
            "<if test='title != null'>title = #{title},</if>" +
            "<if test='dept != null'>dept = #{dept},</if>" +
            "<if test='orgId != null'>org_id = #{orgId},</if>" +
            "<if test='auditStatus != null'>audit_status = #{auditStatus},</if>" +
            "<if test='accountStatus != null'>account_status = #{accountStatus},</if>" +
            "update_time = NOW()" +
            "</set> WHERE id = #{id} AND is_deleted = 0" +
            "</script>")
    int update(DoctorAccount account);
}
