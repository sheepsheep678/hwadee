package com.cdut.mapper;

import com.cdut.dto.ElderProfileDetailDTO;
import com.cdut.dto.ElderProfileUpdateDTO;
import com.cdut.pojo.DeviceInfo;
import com.cdut.pojo.ElderProfile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ProfileMapper {
    @Select("select * from elder_profile where elder_no=#{elderId}")
    public ElderProfile selectElderProfileById(Integer elderId);


    @Select("select * from elder_profile where elder_no=#{elderId}")
    public List<ElderProfile> selectAllElderProfileById(Integer elderId);

    @Update("update elder_profile set phone=#{phone},address=#{address},photo_url=#{photoUrl} where elder_no=#{elderNo}")
    public void updateElderProfile(ElderProfileUpdateDTO elderProfileUpdateDTO);

}
