package com.cdut.mapper;

import com.cdut.pojo.HealthRecord;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 健康档案 Mapper（原生 MyBatis，医生端档案管理使用）
 */
@Mapper
public interface HealthRecordMapper {

    @Insert("INSERT INTO health_record (elder_id, record_type, disease_name, icd_code, diagnose_date, hospital, detail, medication, create_time) " +
            "VALUES (#{elderId}, #{recordType}, #{diseaseName}, #{icdCode}, #{diagnoseDate}, #{hospital}, #{detail}, #{medication}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(HealthRecord record);

    @Update("UPDATE health_record SET is_deleted = 1 WHERE elder_id = #{elderId}")
    int deleteByElderId(@Param("elderId") Long elderId);

    @Select("SELECT * FROM health_record WHERE elder_id = #{elderId} AND is_deleted = 0 ORDER BY create_time DESC")
    List<HealthRecord> selectByElderId(@Param("elderId") Long elderId);
}
