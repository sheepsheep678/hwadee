package com.cdut.service.impl;

import com.cdut.dto.ElderProfileDetailDTO;
import com.cdut.dto.ElderProfileUpdateDTO;
import com.cdut.mapper.FamilyMapper;
import com.cdut.mapper.HealthMapper;
import com.cdut.mapper.ProfileMapper;
import com.cdut.mapper.TagMapper;
import com.cdut.pojo.ElderProfile;
import com.cdut.pojo.FamilyContact;
import com.cdut.pojo.HealthRecord;
import com.cdut.pojo.Result;
import com.cdut.service.ProfileService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfileServiceImpl implements ProfileService {


    @Autowired
    private ProfileMapper profileMapper;
    @Autowired
    private HealthMapper healthMapper;
    @Autowired
    private FamilyMapper familyMapper;
    @Autowired
    private TagMapper tagMapper;


    @Override
    public Result<ElderProfileDetailDTO> getElderProfile(Integer elderId) {
        ElderProfile elderProfile = profileMapper.selectElderProfileById(elderId);
        ElderProfileDetailDTO elderProfileDetailDTO = new ElderProfileDetailDTO();
        BeanUtils.copyProperties(elderProfile, elderProfileDetailDTO);
        //获取健康档案
        List<HealthRecord> healthRecords = healthMapper.selectHealthRecordById(elderId);
        elderProfileDetailDTO.setHealthRecords(healthRecords);
        //获取家人联系人
        List<FamilyContact> familyContacts = familyMapper.selectFamilyContactById(elderId);
        elderProfileDetailDTO.setFamilyContacts(familyContacts);
        //获取标签
        List<String> tags = tagMapper.selectTagNameById(elderId);
        elderProfileDetailDTO.setTags(tags);

        return Result.success(elderProfileDetailDTO);
    }

    @Override
    public Result<Void> updateElderProfile(ElderProfileUpdateDTO elderProfileUpdateDTO) {
        profileMapper.updateElderProfile(elderProfileUpdateDTO);
        return Result.success("更新成功");
    }

    @Override
    public Result<List<HealthRecord>> getHealthRecords(Integer elderId) {
        List<HealthRecord> healthRecords = healthMapper.selectHealthRecordById(elderId);
        return Result.success(healthRecords);
    }

    @Override
    public Result<List<FamilyContact>> getFamilyContacts(Integer elderId) {
        List<FamilyContact> familyContacts = familyMapper.selectFamilyContactById(elderId);
        return Result.success(familyContacts);
    }
}
