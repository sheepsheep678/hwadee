package com.cdut.controller;

import com.cdut.dto.ElderProfileDetailDTO;
import com.cdut.dto.ElderProfileQueryDTO;
import com.cdut.dto.ElderProfileSaveDTO;
import com.cdut.pojo.ElderProfile;
import com.cdut.pojo.PageResult;
import com.cdut.pojo.Result;
import com.cdut.service.ElderProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 医生端 · 老人档案管理接口
 */
@RestController
@RequestMapping("/api/doctor/elder/profiles")
@RequiredArgsConstructor
public class DoctorElderController {

    private final ElderProfileService elderProfileService;

    /** 分页查询老人档案 */
    @GetMapping
    public Result<PageResult<ElderProfile>> page(ElderProfileQueryDTO query) {
        return Result.success(elderProfileService.page(query));
    }

    /** 查询老人档案详情 */
    @GetMapping("/{id}")
    public Result<ElderProfileDetailDTO> detail(@PathVariable Long id) {
        return Result.success(elderProfileService.detail(id));
    }

    /** 新增老人档案 */
    @PostMapping
    public Result<Long> save(@RequestBody @Validated ElderProfileSaveDTO dto) {
        return Result.success(elderProfileService.save(dto));
    }

    /** 修改老人档案 */
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody @Validated ElderProfileSaveDTO dto) {
        elderProfileService.update(id, dto);
        return Result.success("修改成功", null);
    }

    /** 删除老人档案 */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        elderProfileService.delete(id);
        return Result.success("删除成功", null);
    }

    /** 批量导出档案，返回文件地址 */
    @PostMapping("/export")
    public Result<String> export(ElderProfileQueryDTO query) {
        return Result.success(elderProfileService.exportExcel(query));
    }

}
