package com.cdut.service.impl;

import com.cdut.dto.ElderProfileDetailDTO;
import com.cdut.exception.BizException;
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
    public Result<ElderProfileDetailDTO> getElderProfile(Long elderId) {
        ElderProfile elderProfile = profileMapper.selectElderProfileById(elderId);
        if (elderProfile == null) {
            throw new BizException("老人档案不存在");
        }

        ElderProfileDetailDTO detail = new ElderProfileDetailDTO();
        BeanUtils.copyProperties(elderProfile, detail);

        List<HealthRecord> healthRecords = healthMapper.selectHealthRecordById(elderId);
        detail.setHealthRecords(healthRecords);

        List<FamilyContact> familyContacts = familyMapper.selectFamilyContactById(elderId);
        detail.setFamilyContacts(familyContacts);

        List<String> tags = tagMapper.selectTagNameByElderId(elderId);
        detail.setTags(tags);

        return Result.success(detail);
    }
}
