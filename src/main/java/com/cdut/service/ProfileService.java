package com.cdut.service;

import com.cdut.dto.ElderProfileDetailDTO;
import com.cdut.pojo.Result;

public interface ProfileService {

    /**
     * 查询老人档案详情（含健康档案、家属联系人、标签）
     *
     * @param elderId 档案ID（elder_profile.id），不是账户ID
     */
    Result<ElderProfileDetailDTO> getElderProfile(Long elderId);
}
