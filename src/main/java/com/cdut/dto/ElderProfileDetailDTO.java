package com.cdut.dto;

import com.cdut.pojo.ElderProfile;
import com.cdut.pojo.FamilyContact;
import com.cdut.pojo.HealthRecord;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ElderProfileDetailDTO extends ElderProfile {

   // 健康档案列表
   private List<HealthRecord> healthRecords;

   // 家属联系人列表
   private List<FamilyContact> familyContacts;

   // 已打标签名称
   private List<String> tags;

}
