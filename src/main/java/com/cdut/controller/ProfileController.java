package com.cdut.controller;

import com.cdut.dto.DeviceQueryDTO;
import com.cdut.dto.ElderProfileDetailDTO;
import com.cdut.dto.ElderProfileQueryDTO;
import com.cdut.dto.ElderProfileUpdateDTO;
import com.cdut.pojo.*;
import com.cdut.service.ProfileService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/elder")
public class ProfileController {
    @Autowired
    private ProfileService profileService;
    @GetMapping("/profile")
    public Result<ElderProfileDetailDTO> queryElderProfile(@RequestParam("id") Long id){
        if (id == null) {
            return Result.error("用户信息获取失败");
        }
        return profileService.getElderProfile(id);
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
