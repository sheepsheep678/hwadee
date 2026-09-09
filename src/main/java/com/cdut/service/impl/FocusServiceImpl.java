package com.cdut.service.impl;

import com.cdut.dto.FollowUpPlanQueryDTO;
import com.cdut.dto.FollowUpPlanSaveDTO;
import com.cdut.dto.FollowUpRecordDTO;
import com.cdut.exception.BizException;
import com.cdut.mapper.ElderProfileMapper;
import com.cdut.mapper.ElderTagMapper;
import com.cdut.mapper.ElderTagRelationMapper;
import com.cdut.mapper.FollowUpPlanMapper;
import com.cdut.mapper.FollowUpRecordMapper;
import com.cdut.pojo.ElderTag;
import com.cdut.pojo.FollowUpPlan;
import com.cdut.pojo.FollowUpRecord;
import com.cdut.pojo.PageResult;
import com.cdut.service.FocusService;
import com.cdut.utils.UserContext;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return tagMapper.selectAll();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unbindTag(Long elderId, Long tagId) {
        relationMapper.deleteByElderAndTag(elderId, tagId);
    }

    @Override
    public PageResult<FollowUpPlan> planPage(FollowUpPlanQueryDTO query) {
        PageHelper.startPage(query.safePageNum(), query.safePageSize());
        List<FollowUpPlan> list = planMapper.selectPage(
                UserContext.getUserId(), query.getElderId(), query.getStatus(), query.getPlanType());
        PageInfo<FollowUpPlan> pageInfo = new PageInfo<>(list);
        return PageResult.of(pageInfo.getList(), pageInfo.getTotal(), query.safePageNum(), query.safePageSize());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void savePlan(FollowUpPlanSaveDTO dto) {
        if (elderProfileMapper.selectById(dto.getElderId()) == null) {
            throw new BizException("老人档案不存在");
        }
        FollowUpPlan plan = new FollowUpPlan();
        BeanUtils.copyProperties(dto, plan);
        plan.setDoctorId(UserContext.getUserId());
        if (plan.getStatus() == null) {
            plan.setStatus(1);
        }
        planMapper.insert(plan);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePlan(Long id, FollowUpPlanSaveDTO dto) {
        if (planMapper.selectById(id) == null) {
            throw new BizException("随访计划不存在");
        }
        FollowUpPlan plan = new FollowUpPlan();
        BeanUtils.copyProperties(dto, plan);
        plan.setId(id);
        planMapper.update(plan);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePlan(Long id) {
        if (planMapper.selectById(id) == null) {
            throw new BizException("随访计划不存在");
        }
        planMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addRecord(FollowUpRecordDTO dto) {
        if (dto.getElderId() == null) {
            throw new BizException("老人ID不能为空");
        }
        if (elderProfileMapper.selectById(dto.getElderId()) == null) {
            throw new BizException("老人档案不存在");
        }
        FollowUpRecord record = new FollowUpRecord();
        record.setPlanId(dto.getPlanId());
        record.setElderId(dto.getElderId());
        record.setDoctorId(UserContext.getUserId());
        record.setFollowDate(dto.getFollowDate());
        record.setMethod(dto.getMethod());
        record.setContent(dto.getContent());
        record.setNextFollowDate(dto.getNextFollowDate());
        recordMapper.insert(record);
    }

    @Override
    public PageResult<FollowUpRecord> recordPage(FollowUpRecordDTO query) {
        PageHelper.startPage(query.safePageNum(), query.safePageSize());
        List<FollowUpRecord> list = recordMapper.selectPage(
                UserContext.getUserId(), query.getElderId(), query.getPlanId(),
                query.getStartDate(), query.getEndDate());
        PageInfo<FollowUpRecord> pageInfo = new PageInfo<>(list);
        return PageResult.of(pageInfo.getList(), pageInfo.getTotal(), query.safePageNum(), query.safePageSize());
    }

}
