package com.cdut.service.impl;

import com.cdut.dto.DoctorPerformanceDTO;
import com.cdut.dto.DoctorSaveDTO;
import com.cdut.dto.PasswordChangeDTO;
import com.cdut.dto.ServiceRecordQueryDTO;
import com.cdut.exception.BizException;
import com.cdut.mapper.DoctorAccountMapper;
import com.cdut.mapper.DoctorScheduleMapper;
import com.cdut.mapper.ServiceRecordMapper;
import com.cdut.pojo.DoctorAccount;
import com.cdut.pojo.DoctorSchedule;
import com.cdut.pojo.PageResult;
import com.cdut.pojo.ServiceRecord;
import com.cdut.service.DoctorProfileService;
import com.cdut.utils.UserContext;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
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
    private final PasswordEncoder passwordEncoder;

    @Override
    public DoctorAccount me() {
        DoctorAccount account = doctorAccountMapper.selectById(UserContext.getUserId());
        if (account == null) {
            throw new BizException("账号不存在");
        }
        account.setPassword(null);
        return account;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMe(DoctorSaveDTO dto) {
        DoctorAccount account = doctorAccountMapper.selectById(UserContext.getUserId());
        if (account == null) {
            throw new BizException("账号不存在");
        }
        if (StringUtils.hasText(dto.getPhone()) && !dto.getPhone().equals(account.getPhone())) {
            if (doctorAccountMapper.countByPhone(dto.getPhone()) > 0) {
                throw new BizException("该手机号已被使用");
            }
            account.setPhone(dto.getPhone());
        }
        if (StringUtils.hasText(dto.getName())) {
            account.setName(dto.getName());
        }
        if (dto.getTitle() != null) {
            account.setTitle(dto.getTitle());
        }
        if (dto.getDept() != null) {
            account.setDept(dto.getDept());
        }
        doctorAccountMapper.update(account);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changePassword(PasswordChangeDTO dto) {
        DoctorAccount account = doctorAccountMapper.selectById(UserContext.getUserId());
        if (account == null) {
            throw new BizException("账号不存在");
        }
        if (account.getPassword() == null
                || !passwordEncoder.matches(dto.getOldPassword(), account.getPassword())) {
            throw new BizException("原密码错误");
        }
        account.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        doctorAccountMapper.update(account);
    }

    @Override
    public List<DoctorSchedule> schedules(String month) {
        if (!StringUtils.hasText(month)) {
            throw new BizException("月份不能为空");
        }
        LocalDate start;
        try {
            start = LocalDate.parse(month + "-01");
        } catch (Exception e) {
            throw new BizException("月份格式错误，应为 yyyy-MM");
        }
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
        return scheduleMapper.selectByDoctorAndDateRange(UserContext.getUserId(), start, end);
    }

    @Override
    public PageResult<ServiceRecord> serviceRecords(ServiceRecordQueryDTO query) {
        PageHelper.startPage(query.safePageNum(), query.safePageSize());
        List<ServiceRecord> list = serviceRecordMapper.selectPage(
                UserContext.getUserId(), query.getServiceType(), query.getElderId(),
                query.getStartDate(), query.getEndDate());
        PageInfo<ServiceRecord> pageInfo = new PageInfo<>(list);
        return PageResult.of(pageInfo.getList(), pageInfo.getTotal(), query.safePageNum(), query.safePageSize());
    }

    @Override
    public DoctorPerformanceDTO performance(LocalDate startDate, LocalDate endDate) {
        DoctorPerformanceDTO dto = serviceRecordMapper.selectPerformance(UserContext.getUserId(), startDate, endDate);
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
