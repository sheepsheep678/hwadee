package com.cdut.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)

   private List<HealthRecord> healthRecords;

   private List<FamilyContact> familyContacts;


}
