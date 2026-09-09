package com.cdut.controller;

import com.cdut.dto.FollowUpPlanQueryDTO;
import com.cdut.dto.FollowUpPlanSaveDTO;
import com.cdut.dto.FollowUpRecordDTO;
import com.cdut.pojo.ElderTag;
import com.cdut.pojo.FollowUpPlan;
import com.cdut.pojo.FollowUpRecord;
import com.cdut.pojo.PageResult;
import com.cdut.pojo.Result;
import com.cdut.service.FocusService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 医生端 · 重点人群与随访接口
 */
@RestController
@RequestMapping("/api/doctor/focus")
@RequiredArgsConstructor
public class DoctorFocusController {

    private final FocusService focusService;

    /** 标签列表 */
    @GetMapping("/tags")
    public Result<List<ElderTag>> tags() {
        return Result.success(focusService.tags());
    }

    /** 取消老人标签 */
    @DeleteMapping("/tag-relations")
    public Result<Void> unbindTag(@RequestParam Long elderId, @RequestParam Long tagId) {
        focusService.unbindTag(elderId, tagId);
        return Result.success("取消成功", null);
    }

    /** 随访计划分页查询 */
    @GetMapping("/follow-up/plans")
    public Result<PageResult<FollowUpPlan>> planPage(FollowUpPlanQueryDTO query) {
        return Result.success(focusService.planPage(query));
    }

    /** 新增随访计划 */
    @PostMapping("/follow-up/plans")
    public Result<Void> savePlan(@RequestBody @Validated FollowUpPlanSaveDTO dto) {
        focusService.savePlan(dto);
        return Result.success("新增成功", null);
    }

    /** 修改随访计划 */
    @PutMapping("/follow-up/plans/{id}")
    public Result<Void> updatePlan(@PathVariable Long id, @RequestBody @Validated FollowUpPlanSaveDTO dto) {
        focusService.updatePlan(id, dto);
        return Result.success("修改成功", null);
    }

    /** 删除随访计划 */
    @DeleteMapping("/follow-up/plans/{id}")
    public Result<Void> deletePlan(@PathVariable Long id) {
        focusService.deletePlan(id);
        return Result.success("删除成功", null);
    }

    /** 新增随访记录 */
    @PostMapping("/follow-up/records")
    public Result<Void> addRecord(@RequestBody FollowUpRecordDTO dto) {
        focusService.addRecord(dto);
        return Result.success("新增成功", null);
    }

    /** 随访记录分页查询 */
    @GetMapping("/follow-up/records")
    public Result<PageResult<FollowUpRecord>> recordPage(FollowUpRecordDTO query) {
        return Result.success(focusService.recordPage(query));
    }

}
