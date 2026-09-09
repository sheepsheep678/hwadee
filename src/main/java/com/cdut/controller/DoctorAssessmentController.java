package com.cdut.controller;

import com.cdut.common.PageInfo;
import com.cdut.common.Result;
import com.cdut.dto.AssessmentReportQueryDTO;
import com.cdut.dto.AssessmentSubmitDTO;
import com.cdut.entity.AssessmentReport;
import com.cdut.entity.AssessmentTemplate;
import com.cdut.service.AssessmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 医生端 · 评估报告接口
 */
@RestController
@RequestMapping("/api/doctor/assessment")
@RequiredArgsConstructor
public class DoctorAssessmentController {

    private final AssessmentService assessmentService;

    /** 获取评估模板列表 */
    @GetMapping("/templates")
    public Result<List<AssessmentTemplate>> templates(@RequestParam(required = false) Integer assessType) {
        return Result.success(assessmentService.templates(assessType));
    }

    /** 提交评估结果 */
    @PostMapping("/reports")
    public Result<AssessmentReport> submit(@RequestBody @Validated AssessmentSubmitDTO dto) {
        return Result.success(assessmentService.submit(dto));
    }

    /** 评估报告分页查询 */
    @GetMapping("/reports")
    public Result<PageInfo<AssessmentReport>> page(AssessmentReportQueryDTO query) {
        return Result.success(assessmentService.page(query));
    }

    /** 评估报告详情 */
    @GetMapping("/reports/{id}")
    public Result<AssessmentReport> detail(@PathVariable Long id) {
        return Result.success(assessmentService.detail(id));
    }

    /** 删除评估报告 */
    @DeleteMapping("/reports/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        assessmentService.delete(id);
        return Result.success();
    }

}
