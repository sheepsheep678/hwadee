package com.cdut.mapper;

import com.cdut.pojo.DoctorQualification;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 医生资质证书 Mapper（原生 MyBatis）
 */
@Mapper
public interface DoctorQualificationMapper {

    @Insert("INSERT INTO doctor_qualification (doctor_id, cert_type, cert_no, cert_img_url, valid_date, audit_status) " +
            "VALUES (#{doctorId}, #{certType}, #{certNo}, #{certImgUrl}, #{validDate}, #{auditStatus})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(DoctorQualification qualification);

    @Select("SELECT * FROM doctor_qualification WHERE doctor_id = #{doctorId} AND is_deleted = 0")
    List<DoctorQualification> selectByDoctorId(@Param("doctorId") Long doctorId);
}
