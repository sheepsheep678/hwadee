package com.cdut.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cdut.common.BusinessException;
import com.cdut.common.PageInfo;
import com.cdut.dto.ElderTagBindDTO;
import com.cdut.dto.FollowUpPlanQueryDTO;
import com.cdut.dto.FollowUpPlanSaveDTO;
import com.cdut.dto.FollowUpRecordDTO;
import com.cdut.entity.ElderTag;
import com.cdut.entity.ElderTagRelation;
import com.cdut.entity.FollowUpPlan;
import com.cdut.entity.FollowUpRecord;
import com.cdut.mapper.ElderProfileMapper;
import com.cdut.mapper.ElderTagMapper;
import com.cdut.mapper.ElderTagRelationMapper;
import com.cdut.mapper.FollowUpPlanMapper;
import com.cdut.mapper.FollowUpRecordMapper;
import com.cdut.service.FocusService;
import com.cdut.util.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 重点人群与随访服务实现。
 */
@Service
@RequiredArgsConstructor
public class FocusServiceImpl implements FocusService {

    private final ElderTagMapper tagMapper;
    private final ElderTagRelationMapper relationMapper;
    private final FollowUpPlanMapper planMapper;
    private final FollowUpRecordMapper recordMapper;
    private final ElderProfileMapper elderProfileMapper;

    @Override
    public List<ElderTag> tags() {
        return tagMapper.selectList(new LambdaQueryWrapper<ElderTag>().orderByAsc(ElderTag::getId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bindTag(ElderTagBindDTO dto) {
        if (elderProfileMapper.selectById(dto.getElderId()) == null) {
            throw new BusinessException("老人档案不存在");
        }
        if (tagMapper.selectById(dto.getTagId()) == null) {
            throw new BusinessException("标签不存在");
        }
        Long count = relationMapper.selectCount(new LambdaQueryWrapper<ElderTagRelation>()
                .eq(ElderTagRelation::getElderId, dto.getElderId())
                .eq(ElderTagRelation::getTagId, dto.getTagId()));
        if (count != null && count > 0) {
            throw new BusinessException("该老人已绑定此标签");
        }
        ElderTagRelation relation = new ElderTagRelation();
        relation.setElderId(dto.getElderId());
        relation.setTagId(dto.getTagId());
        relation.setCreateTime(LocalDateTime.now());
        relationMapper.insert(relation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unbindTag(Long elderId, Long tagId) {
        relationMapper.delete(new LambdaQueryWrapper<ElderTagRelation>()
                .eq(ElderTagRelation::getElderId, elderId)
                .eq(ElderTagRelation::getTagId, tagId));
    }

    @Override
    public PageInfo<FollowUpPlan> planPage(FollowUpPlanQueryDTO query) {
        Long doctorId = UserContext.requireDoctorId();
        LambdaQueryWrapper<FollowUpPlan> wrapper = new LambdaQueryWrapper<FollowUpPlan>()
                .eq(FollowUpPlan::getDoctorId, doctorId)
                .eq(query.getElderId() != null, FollowUpPlan::getElderId, query.getElderId())
                .eq(query.getStatus() != null, FollowUpPlan::getStatus, query.getStatus())
                .like(StringUtils.hasText(query.getPlanName()), FollowUpPlan::getPlanName, query.getPlanName())
                .orderByDesc(FollowUpPlan::getCreateTime);
        return PageInfo.of(planMapper.selectPage(new Page<>(query.safePageNum(), query.safePageSize()), wrapper));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void savePlan(FollowUpPlanSaveDTO dto) {
        if (elderProfileMapper.selectById(dto.getElderId()) == null) {
            throw new BusinessException("老人档案不存在");
        }
        FollowUpPlan plan = new FollowUpPlan();
        BeanUtils.copyProperties(dto, plan);
        plan.setDoctorId(UserContext.requireDoctorId());
        plan.setStatus(dto.getStatus() == null ? 1 : dto.getStatus());
        plan.setCreateTime(LocalDateTime.now());
        plan.setUpdateTime(LocalDateTime.now());
        planMapper.insert(plan);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePlan(Long id, FollowUpPlanSaveDTO dto) {
        if (planMapper.selectById(id) == null) {
            throw new BusinessException("随访计划不存在");
        }
        FollowUpPlan plan = new FollowUpPlan();
        BeanUtils.copyProperties(dto, plan);
        plan.setId(id);
        plan.setUpdateTime(LocalDateTime.now());
        planMapper.updateById(plan);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePlan(Long id) {
        if (planMapper.selectById(id) == null) {
            throw new BusinessException("随访计划不存在");
        }
        planMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addRecord(FollowUpRecordDTO dto) {
        if (dto.getElderId() == null) {
            throw new BusinessException("老人ID不能为空");
        }
        if (elderProfileMapper.selectById(dto.getElderId()) == null) {
            throw new BusinessException("老人档案不存在");
        }
        FollowUpRecord record = new FollowUpRecord();
        record.setPlanId(dto.getPlanId());
        record.setElderId(dto.getElderId());
        record.setDoctorId(UserContext.requireDoctorId());
        record.setFollowUpDate(dto.getFollowUpDate());
        record.setContent(dto.getContent());
        record.setResult(dto.getResult());
        record.setNextFollowUpDate(dto.getNextFollowUpDate());
        record.setCreateTime(LocalDateTime.now());
        recordMapper.insert(record);
    }

    @Override
    public PageInfo<FollowUpRecord> recordPage(FollowUpRecordDTO query) {
        Long doctorId = UserContext.requireDoctorId();
        LambdaQueryWrapper<FollowUpRecord> wrapper = new LambdaQueryWrapper<FollowUpRecord>()
                .eq(FollowUpRecord::getDoctorId, doctorId)
                .eq(query.getElderId() != null, FollowUpRecord::getElderId, query.getElderId())
                .eq(query.getPlanId() != null, FollowUpRecord::getPlanId, query.getPlanId())
                .ge(query.getStartDate() != null, FollowUpRecord::getFollowUpDate, query.getStartDate())
                .le(query.getEndDate() != null, FollowUpRecord::getFollowUpDate, query.getEndDate())
                .orderByDesc(FollowUpRecord::getFollowUpDate);
        return PageInfo.of(recordMapper.selectPage(new Page<>(query.safePageNum(), query.safePageSize()), wrapper));
    }

}
