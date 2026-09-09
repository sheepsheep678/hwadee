package com.cdut.controller;

import com.cdut.dto.DeviceQueryDTO;
import com.cdut.dto.ElderProfileDetailDTO;
import com.cdut.dto.ElderProfileQueryDTO;
import com.cdut.dto.ElderProfileUpdateDTO;
import com.cdut.pojo.*;
import com.cdut.service.ProfileService;
import com.github.pagehelper.PageInfo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/elder")
public class ProfileController {
    @Autowired
    private ProfileService profileService;
    @GetMapping("/profile")
    public Result<ElderProfileDetailDTO> queryElderProfile(HttpServletRequest request){
        // 1. 从 request 中获取拦截器解析并放入的 userId
        String userId = (String) request.getAttribute("currentUserId");

        // 如果没有取到，说明拦截器放行但没设置好，或者token里没有存这个信息
        if (userId == null) {
            return Result.error("用户信息获取失败");
        }
        return profileService.getElderProfile(Long.valueOf(userId));
    }
    @GetMapping("/profile/page")
    public PageResult<ElderProfileQueryDTO> list(@RequestParam(defaultValue = "1") int pageNum,
                                                 @RequestParam(defaultValue = "10") int pageSize,
                                                 @RequestParam(required = false) Integer elderId) {
        PageInfo<ElderProfileQueryDTO> pageInfo = profileService.listByPage(pageNum, pageSize, elderId);

        return PageResult.of(pageInfo.getList(), pageInfo.getTotal(),pageNum, pageSize);
    }


    @PutMapping("/profile")
    public Result<Void> modifyElderProfile(@RequestBody ElderProfileUpdateDTO elderProfileUpdateDTO) {
        return profileService.updateElderProfile(elderProfileUpdateDTO);
    }

    @GetMapping("/profile/health-records")
    public Result<List<HealthRecord>> getHealthRecords(@RequestParam Long elderId) {
        return profileService.getHealthRecords(elderId);
    }

    @GetMapping("/profile/family-contacts")
    public Result<List<FamilyContact>> getFamilyContacts(@RequestParam Long elderId) {
        return profileService.getFamilyContacts(elderId);
    }
}
