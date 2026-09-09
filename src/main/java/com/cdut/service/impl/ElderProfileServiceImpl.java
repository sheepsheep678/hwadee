package com.cdut.service.impl;

import com.cdut.dto.ElderProfileDetailDTO;
import com.cdut.dto.ElderProfileQueryDTO;
import com.cdut.dto.ElderProfileSaveDTO;
import com.cdut.dto.FamilyContactDTO;
import com.cdut.dto.HealthRecordDTO;
import com.cdut.exception.BizException;
import com.cdut.mapper.ElderProfileMapper;
import com.cdut.mapper.ElderTagMapper;
import com.cdut.mapper.ElderTagRelationMapper;
import com.cdut.mapper.FamilyContactMapper;
import com.cdut.mapper.HealthRecordMapper;
import com.cdut.pojo.ElderProfile;
import com.cdut.pojo.ElderTag;
import com.cdut.pojo.ElderTagRelation;
import com.cdut.pojo.FamilyContact;
import com.cdut.pojo.HealthRecord;
import com.cdut.pojo.PageResult;
import com.cdut.service.ElderProfileService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 老人档案管理服务实现。
 */
@Service
@RequiredArgsConstructor
public class ElderProfileServiceImpl implements ElderProfileService {

    private final ElderProfileMapper elderProfileMapper;
    private final HealthRecordMapper healthRecordMapper;
    private final FamilyContactMapper familyContactMapper;
    private final ElderTagMapper elderTagMapper;
    private final ElderTagRelationMapper tagRelationMapper;

    @Override
    public PageResult<ElderProfile> page(ElderProfileQueryDTO query) {
        PageHelper.startPage(query.safePageNum(), query.safePageSize());
        List<ElderProfile> list = elderProfileMapper.selectProfilePage(query);
        PageInfo<ElderProfile> pageInfo = new PageInfo<>(list);
        return PageResult.of(pageInfo.getList(), pageInfo.getTotal(), query.safePageNum(), query.safePageSize());
    }

    @Override
    public ElderProfileDetailDTO detail(Long id) {
        ElderProfile profile = elderProfileMapper.selectById(id);
        if (profile == null) {
            throw new BizException("老人档案不存在");
        }
        ElderProfileDetailDTO dto = new ElderProfileDetailDTO();
        BeanUtils.copyProperties(profile, dto);
        dto.setHealthRecords(healthRecordMapper.selectByElderId(id));
        dto.setFamilyContacts(familyContactMapper.selectByElderId(id));
        dto.setTags(queryTagsByElder(id));
        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(ElderProfileSaveDTO dto) {
        checkIdCardUnique(dto.getIdCard(), null);
        ElderProfile profile = new ElderProfile();
        BeanUtils.copyProperties(dto, profile);
        if (profile.getStatus() == null) {
            profile.setStatus(1);
        }
        profile.setCreateTime(LocalDateTime.now());
        profile.setUpdateTime(LocalDateTime.now());
        elderProfileMapper.insert(profile);
        saveChildren(profile.getId(), dto);
        return profile.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, ElderProfileSaveDTO dto) {
        if (elderProfileMapper.selectById(id) == null) {
            throw new BizException("老人档案不存在");
        }
        checkIdCardUnique(dto.getIdCard(), id);
        ElderProfile profile = new ElderProfile();
        BeanUtils.copyProperties(dto, profile);
        profile.setId(id);
        profile.setUpdateTime(LocalDateTime.now());
        elderProfileMapper.update(profile);
        // 重建子表数据
        deleteChildren(id);
        saveChildren(id, dto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        if (elderProfileMapper.selectById(id) == null) {
            throw new BizException("老人档案不存在");
        }
        elderProfileMapper.deleteById(id);
        deleteChildren(id);
    }

    @Override
    public String exportExcel(ElderProfileQueryDTO query) {
        // TODO 导出功能依赖 POI（当前未引入 Excel 依赖），暂未实现
        return null;
    }

    private void checkIdCardUnique(String idCard, Long excludeId) {
        if (elderProfileMapper.countByIdCard(idCard, excludeId) > 0) {
            throw new BizException("该身份证号已存在档案记录");
        }
    }

    private void saveChildren(Long elderId, ElderProfileSaveDTO dto) {
        if (dto.getHealthRecords() != null) {
            for (HealthRecordDTO record : dto.getHealthRecords()) {
                HealthRecord hr = new HealthRecord();
                BeanUtils.copyProperties(record, hr);
                hr.setId(null);
                hr.setElderId(elderId);
                hr.setCreateTime(LocalDateTime.now());
                healthRecordMapper.insert(hr);
            }
        }
        if (dto.getFamilyContacts() != null) {
            for (FamilyContactDTO contact : dto.getFamilyContacts()) {
                contact.setId(null);
                contact.setElderId(elderId);
                contact.setCreateTime(LocalDateTime.now());
                familyContactMapper.insert(contact);
            }
        }
        if (dto.getTagIds() != null) {
            for (Long tagId : dto.getTagIds()) {
                ElderTagRelation relation = new ElderTagRelation();
                relation.setElderId(elderId);
                relation.setTagId(tagId);
                tagRelationMapper.insert(relation);
            }
        }
    }

    private void deleteChildren(Long elderId) {
        healthRecordMapper.deleteByElderId(elderId);
        familyContactMapper.deleteByElderId(elderId);
        tagRelationMapper.deleteByElderId(elderId);
    }

    private List<String> queryTagsByElder(Long elderId) {
        List<ElderTagRelation> relations = tagRelationMapper.selectByElderId(elderId);
        if (relations.isEmpty()) {
            return Collections.emptyList();
        }
        List<Long> tagIds = relations.stream().map(ElderTagRelation::getTagId).collect(Collectors.toList());
        return elderTagMapper.selectByIds(tagIds).stream()
                .map(ElderTag::getTagName)
                .collect(Collectors.toList());
    }

}
