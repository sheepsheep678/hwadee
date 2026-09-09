package com.cdut.controller;

import com.cdut.dto.DoctorPerformanceDTO;
import com.cdut.dto.DoctorSaveDTO;
import com.cdut.dto.PasswordChangeDTO;
import com.cdut.dto.ServiceRecordQueryDTO;
import com.cdut.pojo.DoctorAccount;
import com.cdut.pojo.DoctorSchedule;
import com.cdut.pojo.PageResult;
import com.cdut.pojo.Result;
import com.cdut.pojo.ServiceRecord;
import com.cdut.service.DoctorProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 医生端 · 个人中心接口
 */
@RestController
@RequestMapping("/api/doctor/profile")
@RequiredArgsConstructor
public class DoctorProfileController {

    private final DoctorProfileService doctorProfileService;

    /** 查询我的个人信息 */
    @GetMapping("/me")
    public Result<DoctorAccount> me() {
        return Result.success(doctorProfileService.me());
    }

    /** 修改我的个人信息 */
    @PutMapping("/me")
    public Result<Void> updateMe(@RequestBody DoctorSaveDTO dto) {
        doctorProfileService.updateMe(dto);
        return Result.success("修改成功", null);
    }

    /** 修改密码 */
    @PutMapping("/password")
    public Result<Void> changePassword(@RequestBody @Validated PasswordChangeDTO dto) {
        doctorProfileService.changePassword(dto);
        return Result.success("修改成功，请重新登录", null);
    }

    /** 我的排班（按月） */
    @GetMapping("/schedules")
    public Result<List<DoctorSchedule>> schedules(@RequestParam String month) {
        return Result.success(doctorProfileService.schedules(month));
    }

    /** 我的服务记录 */
    @GetMapping("/service-records")
    public Result<PageResult<ServiceRecord>> serviceRecords(ServiceRecordQueryDTO query) {
        return Result.success(doctorProfileService.serviceRecords(query));
    }

    /** 我的绩效 */
    @GetMapping("/performance")
    public Result<DoctorPerformanceDTO> performance(@RequestParam(required = false) LocalDate startDate,
                                                    @RequestParam(required = false) LocalDate endDate) {
        return Result.success(doctorProfileService.performance(startDate, endDate));
    }

}
