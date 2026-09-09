package com.cdut.service;

import com.cdut.dto.ElderProfileDetailDTO;
import com.cdut.dto.ElderProfileQueryDTO;
import com.cdut.dto.ElderProfileUpdateDTO;
import com.cdut.pojo.FamilyContact;
import com.cdut.pojo.HealthRecord;
import com.cdut.pojo.Result;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ProfileService {
    Result<ElderProfileDetailDTO> getElderProfile(Long id);

    Result<Void> updateElderProfile(ElderProfileUpdateDTO elderProfileUpdateDTO);

    Result<List<HealthRecord>> getHealthRecords(Long elderId);

    Result<List<FamilyContact>> getFamilyContacts(Long elderId);

    PageInfo<ElderProfileQueryDTO> listByPage(int pageNum, int pageSize, Integer elderId);
}
