package com.cdut.mapper;

import com.cdut.pojo.HealthRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface HealthMapper {
    @Select("select * from health_record where elder_id = #{elderId} and is_deleted = 0")
    List<HealthRecord> selectHealthRecordById(@Param("elderId") Long elderId);
}
