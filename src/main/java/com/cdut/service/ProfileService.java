package com.cdut.service;

import com.cdut.dto.ElderProfileDetailDTO;
import com.cdut.dto.ElderProfileUpdateDTO;
import com.cdut.pojo.FamilyContact;
import com.cdut.pojo.HealthRecord;
import com.cdut.pojo.Result;

import java.util.List;

public interface ProfileService {
    Result<ElderProfileDetailDTO> getElderProfile(Integer id);

    Result<Void> updateElderProfile(ElderProfileUpdateDTO elderProfileUpdateDTO);

    Result<List<HealthRecord>> getHealthRecords(Integer elderId);

    Result<List<FamilyContact>> getFamilyContacts(Integer elderId);
}
