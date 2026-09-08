package com.cdut.mapper;

import com.cdut.pojo.HealthRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface HealthMapper {
    @Select("select * from health_record where elder_id = #{elderId}")
    public List<HealthRecord> selectHealthRecordById(int elderId);
}
