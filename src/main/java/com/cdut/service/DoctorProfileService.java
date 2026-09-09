package com.cdut.service;

import com.cdut.common.PageInfo;
import com.cdut.dto.DoctorPerformanceDTO;
import com.cdut.dto.DoctorSaveDTO;
import com.cdut.dto.PasswordChangeDTO;
import com.cdut.dto.ServiceRecordQueryDTO;
import com.cdut.entity.DoctorAccount;
import com.cdut.entity.DoctorSchedule;
import com.cdut.entity.ServiceRecord;

import java.time.LocalDate;
import java.util.List;

/**
 * 医生个人中心服务
 */
public interface DoctorProfileService {

    /** 查询我的个人信息 */
    DoctorAccount me();

    /** 修改我的个人信息 */
    void updateMe(DoctorSaveDTO dto);

    /** 修改密码 */
    void changePassword(PasswordChangeDTO dto);

    /** 我的排班（按月） */
    List<DoctorSchedule> schedules(String month);

    /** 我的服务记录 */
    PageInfo<ServiceRecord> serviceRecords(ServiceRecordQueryDTO query);

    /** 我的绩效 */
    DoctorPerformanceDTO performance(LocalDate startDate, LocalDate endDate);

}
