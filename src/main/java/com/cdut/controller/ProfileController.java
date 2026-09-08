package com.cdut.controller;

import com.cdut.dto.ElderProfileDetailDTO;
import com.cdut.pojo.Result;
import com.cdut.service.ProfileService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        return profileService.getElderProfile(Integer.valueOf(1));
    }
}
