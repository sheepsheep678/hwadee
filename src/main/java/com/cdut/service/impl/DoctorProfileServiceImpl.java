package com.cdut.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cdut.common.BusinessException;
import com.cdut.common.PageInfo;
import com.cdut.dto.DoctorPerformanceDTO;
import com.cdut.dto.DoctorSaveDTO;
import com.cdut.dto.PasswordChangeDTO;
import com.cdut.dto.ServiceRecordQueryDTO;
import com.cdut.entity.DoctorAccount;
import com.cdut.entity.DoctorSchedule;
import com.cdut.entity.ServiceRecord;
import com.cdut.mapper.DoctorAccountMapper;
import com.cdut.mapper.DoctorScheduleMapper;
import com.cdut.mapper.ServiceRecordMapper;
import com.cdut.service.DoctorProfileService;
import com.cdut.util.PasswordUtil;
import com.cdut.util.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 医生个人中心服务实现。
 */
@Service
@RequiredArgsConstructor
public class DoctorProfileServiceImpl implements DoctorProfileService {

    private final DoctorAccountMapper doctorAccountMapper;
    private final DoctorScheduleMapper scheduleMapper;
    private final ServiceRecordMapper serviceRecordMapper;

    @Override
    public DoctorAccount me() {
        DoctorAccount account = doctorAccountMapper.selectById(UserContext.requireDoctorId());
        if (account == null) {
            throw new BusinessException("账号不存在");
        }
        account.setPassword(null);
        return account;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMe(DoctorSaveDTO dto) {
        DoctorAccount account = doctorAccountMapper.selectById(UserContext.requireDoctorId());
        if (account == null) {
            throw new BusinessException("账号不存在");
        }
        if (StringUtils.hasText(dto.getPhone()) && !dto.getPhone().equals(account.getPhone())) {
            Long count = doctorAccountMapper.selectCount(
                    new LambdaQueryWrapper<DoctorAccount>().eq(DoctorAccount::getPhone, dto.getPhone()));
            if (count != null && count > 0) {
                throw new BusinessException("该手机号已被使用");
            }
            account.setPhone(dto.getPhone());
        }
        if (StringUtils.hasText(dto.getName())) {
            account.setName(dto.getName());
        }
        if (dto.getAvatar() != null) {
            account.setAvatar(dto.getAvatar());
        }
        if (dto.getTitle() != null) {
            account.setTitle(dto.getTitle());
        }
        if (dto.getDept() != null) {
            account.setDept(dto.getDept());
        }
        account.setUpdateTime(LocalDateTime.now());
        doctorAccountMapper.updateById(account);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changePassword(PasswordChangeDTO dto) {
        DoctorAccount account = doctorAccountMapper.selectById(UserContext.requireDoctorId());
        if (account == null) {
            throw new BusinessException("账号不存在");
        }
        if (!PasswordUtil.matches(dto.getOldPassword(), account.getPassword())) {
            throw new BusinessException("原密码错误");
        }
        account.setPassword(PasswordUtil.encrypt(dto.getNewPassword()));
        account.setUpdateTime(LocalDateTime.now());
        doctorAccountMapper.updateById(account);
    }

    @Override
    public List<DoctorSchedule> schedules(String month) {
        if (!StringUtils.hasText(month)) {
            throw new BusinessException("月份不能为空");
        }
        Long doctorId = UserContext.requireDoctorId();
        LocalDate start;
        try {
            start = LocalDate.parse(month + "-01");
        } catch (Exception e) {
            throw new BusinessException("月份格式错误，应为 yyyy-MM");
        }
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
        return scheduleMapper.selectList(new LambdaQueryWrapper<DoctorSchedule>()
                .eq(DoctorSchedule::getDoctorId, doctorId)
                .ge(DoctorSchedule::getScheduleDate, start)
                .le(DoctorSchedule::getScheduleDate, end)
                .orderByAsc(DoctorSchedule::getScheduleDate));
    }

    @Override
    public PageInfo<ServiceRecord> serviceRecords(ServiceRecordQueryDTO query) {
        Long doctorId = UserContext.requireDoctorId();
        LambdaQueryWrapper<ServiceRecord> wrapper = new LambdaQueryWrapper<ServiceRecord>()
                .eq(ServiceRecord::getDoctorId, doctorId)
                .eq(query.getServiceType() != null, ServiceRecord::getServiceType, query.getServiceType())
                .eq(query.getElderId() != null, ServiceRecord::getElderId, query.getElderId())
                .ge(query.getStartDate() != null, ServiceRecord::getServiceDate, query.getStartDate())
                .le(query.getEndDate() != null, ServiceRecord::getServiceDate, query.getEndDate())
                .orderByDesc(ServiceRecord::getServiceDate);
        return PageInfo.of(serviceRecordMapper.selectPage(new Page<>(query.safePageNum(), query.safePageSize()), wrapper));
    }

    @Override
    public DoctorPerformanceDTO performance(LocalDate startDate, LocalDate endDate) {
        Long doctorId = UserContext.requireDoctorId();
        DoctorPerformanceDTO dto = serviceRecordMapper.selectPerformance(doctorId, startDate, endDate);
        if (dto == null) {
            dto = new DoctorPerformanceDTO();
            dto.setServiceCount(0L);
            dto.setElderCount(0L);
            dto.setFollowUpCount(0L);
            dto.setAvgRating(BigDecimal.ZERO);
        }
        return dto;
    }

}
