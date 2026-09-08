package com.cdut.controller;

import com.cdut.dto.ElderProfileDetailDTO;
import com.cdut.dto.ElderProfileUpdateDTO;
import com.cdut.pojo.FamilyContact;
import com.cdut.pojo.HealthRecord;
import com.cdut.pojo.Result;
import com.cdut.service.ProfileService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        return profileService.getElderProfile(Integer.valueOf(userId));
    }


    @PutMapping("/profile")
    public Result<Void> modifyElderProfile(ElderProfileUpdateDTO elderProfileUpdateDTO) {
        return profileService.updateElderProfile(elderProfileUpdateDTO);
    }

    @GetMapping("/profile/health-records")
    public Result<List<HealthRecord>> getHealthRecords(Integer elderId) {
        return profileService.getHealthRecords(elderId);
    }

    @GetMapping("/profile/family-contacts")
    public Result<List<FamilyContact>> getFamilyContacts(Integer elderId) {
        return profileService.getFamilyContacts(elderId);
    }
}
