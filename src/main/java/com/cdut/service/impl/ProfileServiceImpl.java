package com.cdut.service.impl;

import com.cdut.dto.DeviceQueryDTO;
import com.cdut.dto.ElderProfileDetailDTO;
import com.cdut.dto.ElderProfileQueryDTO;
import com.cdut.dto.ElderProfileUpdateDTO;
import com.cdut.mapper.FamilyMapper;
import com.cdut.mapper.HealthMapper;
import com.cdut.mapper.ProfileMapper;
import com.cdut.mapper.TagMapper;
import com.cdut.pojo.*;
import com.cdut.service.ProfileService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Override
    public PageInfo<ElderProfileQueryDTO> listByPage(int pageNum, int pageSize, Integer elderId) {
        // 1. 开启分页：只对"紧随其后"的第一条查询生效
        PageHelper.startPage(pageNum, pageSize);
        // 2. 紧接着执行 Mapper 查询，PageHelper 会自动重写 SQL 加上 LIMIT
        List<ElderProfile> elderProfileList = profileMapper.selectAllElderProfileById(elderId);
        // 3. 用 PageInfo 包装，自动包含 total、pages、navigatepageNums 等
        List<ElderProfileQueryDTO> elderProfileQueryDTOList = new ArrayList<>();
        for (ElderProfile elderProfile : elderProfileList) {
            ElderProfileQueryDTO elderProfileQueryDTO = new ElderProfileQueryDTO();
            BeanUtils.copyProperties(elderProfile, elderProfileQueryDTO);
            elderProfileQueryDTOList.add(elderProfileQueryDTO);
        }
        return new PageInfo<>(elderProfileQueryDTOList);
    }
}
