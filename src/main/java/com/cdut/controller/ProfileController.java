package com.cdut.controller;

import com.cdut.dto.ElderProfileDetailDTO;
import com.cdut.mapper.ProfileMapper;
import com.cdut.pojo.Result;
import com.cdut.service.ProfileService;
import com.cdut.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 老人端 · 我的档案
 *
 * <p>路径 /api/elder/profile —— 与个人中心 /api/elder/center 分开。
 * <p>注意：JWT 里存的是账户ID，查档案/健康档案/家属联系人/标签都要先换算成档案ID。
 */
@RestController
@RequestMapping("/api/elder/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;
    @Autowired
    private ProfileMapper profileMapper;

    @GetMapping
    public Result<ElderProfileDetailDTO> queryElderProfile() {
        Long accountId = UserContext.getUserId();
        if (accountId == null) {
            return Result.error("用户信息获取失败");
        }

        Long elderId = profileMapper.selectElderIdByAccountId(accountId);
        if (elderId == null) {
            return Result.error("未查询到老人档案");
        }

        return profileService.getElderProfile(elderId);
    }
}
