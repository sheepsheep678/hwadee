package com.cdut.dto;

import com.cdut.pojo.HealthRecord;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class HealthRecordDTO extends HealthRecord {

   @NotNull(message = "记录类型不能为空")
   private Integer recordType;

   @NotBlank(message = "疾病/事项名称不能为空")
   private String diseaseName;

}