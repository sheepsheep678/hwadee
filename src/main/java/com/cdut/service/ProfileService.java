package com.cdut.service;

import com.cdut.dto.ElderProfileDetailDTO;
import com.cdut.pojo.Result;

public interface ProfileService {
    Result<ElderProfileDetailDTO> getElderProfile(Integer id);
}
