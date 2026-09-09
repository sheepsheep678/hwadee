package com.cdut.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cdut.common.BusinessException;
import com.cdut.common.PageInfo;
import com.cdut.dto.ElderImportResultDTO;
import com.cdut.dto.ElderProfileDetailDTO;
import com.cdut.dto.ElderProfileQueryDTO;
import com.cdut.dto.ElderProfileSaveDTO;
import com.cdut.entity.ElderProfile;
import com.cdut.entity.ElderTag;
import com.cdut.entity.ElderTagRelation;
import com.cdut.entity.FamilyContact;
import com.cdut.entity.HealthRecord;
import com.cdut.mapper.ElderProfileMapper;
import com.cdut.mapper.ElderTagMapper;
import com.cdut.mapper.ElderTagRelationMapper;
import com.cdut.mapper.FamilyContactMapper;
import com.cdut.mapper.HealthRecordMapper;
import com.cdut.service.ElderProfileService;
import com.cdut.util.ExcelUtil;
import com.cdut.util.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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

    @Value("${file.upload-dir:./uploads}")
    private String uploadDir;

    @Override
    public PageInfo<ElderProfile> page(ElderProfileQueryDTO query) {
        Page<ElderProfile> page = new Page<>(query.safePageNum(), query.safePageSize());
        IPage<ElderProfile> result = elderProfileMapper.selectProfilePage(page, query);
        return PageInfo.of(result);
    }

    @Override
    public ElderProfileDetailDTO detail(Long id) {
        ElderProfile profile = elderProfileMapper.selectById(id);
        if (profile == null) {
            throw new BusinessException("老人档案不存在");
        }
        ElderProfileDetailDTO dto = new ElderProfileDetailDTO();
        BeanUtils.copyProperties(profile, dto);
        dto.setHealthRecords(healthRecordMapper.selectList(
                new LambdaQueryWrapper<HealthRecord>()
                        .eq(HealthRecord::getElderId, id)
                        .orderByDesc(HealthRecord::getRecordDate)));
        dto.setFamilyContacts(familyContactMapper.selectList(
                new LambdaQueryWrapper<FamilyContact>().eq(FamilyContact::getElderId, id)));
        dto.setTags(queryTagsByElder(id));
        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(ElderProfileSaveDTO dto) {
        checkIdCardUnique(dto.getIdCard(), null);
        ElderProfile profile = new ElderProfile();
        BeanUtils.copyProperties(dto, profile);
        profile.setCreateBy(UserContext.requireDoctorId());
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
            throw new BusinessException("老人档案不存在");
        }
        checkIdCardUnique(dto.getIdCard(), id);
        ElderProfile profile = new ElderProfile();
        BeanUtils.copyProperties(dto, profile);
        profile.setId(id);
        profile.setUpdateTime(LocalDateTime.now());
        elderProfileMapper.updateById(profile);
        // 重建子表数据
        deleteChildren(id);
        saveChildren(id, dto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        if (elderProfileMapper.selectById(id) == null) {
            throw new BusinessException("老人档案不存在");
        }
        elderProfileMapper.deleteById(id);
        deleteChildren(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ElderImportResultDTO importExcel(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("请上传 Excel 文件");
        }
        List<ElderProfileSaveDTO> rows;
        try {
            rows = ExcelUtil.parseElderImport(file.getInputStream());
        } catch (Exception e) {
            throw new BusinessException("Excel 文件解析失败");
        }
        ElderImportResultDTO result = new ElderImportResultDTO();
        result.setTotal(rows.size());
        int rowNo = 2;
        for (ElderProfileSaveDTO dto : rows) {
            try {
                save(dto);
                result.setSuccessCount(result.getSuccessCount() + 1);
            } catch (Exception e) {
                result.setFailCount(result.getFailCount() + 1);
                result.addFail("第" + rowNo + "行-" + e.getMessage());
            }
            rowNo++;
        }
        return result;
    }

    @Override
    public String exportExcel(ElderProfileQueryDTO query) {
        List<ElderProfile> list = elderProfileMapper.selectProfilePage(new Page<>(1, 100000), query).getRecords();
        byte[] bytes = ExcelUtil.exportElderProfiles(list);
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String fileName = "elder_profiles_" + System.currentTimeMillis() + ".xlsx";
        try (FileOutputStream fos = new FileOutputStream(new File(dir, fileName))) {
            fos.write(bytes);
        } catch (IOException e) {
            throw new BusinessException("导出 Excel 失败");
        }
        return "/files/" + fileName;
    }

    private void checkIdCardUnique(String idCard, Long excludeId) {
        LambdaQueryWrapper<ElderProfile> wrapper = new LambdaQueryWrapper<ElderProfile>()
                .eq(ElderProfile::getIdCard, idCard);
        if (excludeId != null) {
            wrapper.ne(ElderProfile::getId, excludeId);
        }
        Long count = elderProfileMapper.selectCount(wrapper);
        if (count != null && count > 0) {
            throw new BusinessException("该身份证号已存在档案记录");
        }
    }

    private void saveChildren(Long elderId, ElderProfileSaveDTO dto) {
        if (dto.getHealthRecords() != null) {
            for (HealthRecord record : dto.getHealthRecords()) {
                record.setId(null);
                record.setElderId(elderId);
                record.setCreateTime(LocalDateTime.now());
                healthRecordMapper.insert(record);
            }
        }
        if (dto.getFamilyContacts() != null) {
            for (FamilyContact contact : dto.getFamilyContacts()) {
                contact.setId(null);
                contact.setElderId(elderId);
                familyContactMapper.insert(contact);
            }
        }
        if (dto.getTagIds() != null) {
            for (Long tagId : dto.getTagIds()) {
                ElderTagRelation relation = new ElderTagRelation();
                relation.setElderId(elderId);
                relation.setTagId(tagId);
                relation.setCreateTime(LocalDateTime.now());
                tagRelationMapper.insert(relation);
            }
        }
    }

    private void deleteChildren(Long elderId) {
        healthRecordMapper.delete(new LambdaQueryWrapper<HealthRecord>().eq(HealthRecord::getElderId, elderId));
        familyContactMapper.delete(new LambdaQueryWrapper<FamilyContact>().eq(FamilyContact::getElderId, elderId));
        tagRelationMapper.delete(new LambdaQueryWrapper<ElderTagRelation>().eq(ElderTagRelation::getElderId, elderId));
    }

    private List<ElderTag> queryTagsByElder(Long elderId) {
        List<ElderTagRelation> relations = tagRelationMapper.selectList(
                new LambdaQueryWrapper<ElderTagRelation>().eq(ElderTagRelation::getElderId, elderId));
        if (relations.isEmpty()) {
            return Collections.emptyList();
        }
        List<Long> tagIds = relations.stream().map(ElderTagRelation::getTagId).collect(Collectors.toList());
        return elderTagMapper.selectBatchIds(tagIds);
    }

}
