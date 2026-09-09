package com.cdut.dto;

import com.cdut.pojo.ElderProfile;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ElderProfileSaveDTO extends ElderProfile {

   @NotBlank(message = "姓名不能为空")
   private String name;

   @NotNull(message = "性别不能为空")
   private Integer gender;

   @NotBlank(message = "身份证号不能为空")
   @Pattern(regexp = "^\\d{17}[\\dXx]$", message = "身份证号格式错误")
   private String idCard;

   @NotBlank(message = "联系电话不能为空")
   @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式错误")
   private String phone;

   @NotNull(message = "居住方式不能为空")
   private Integer livingType;

   // 家属联系人列表 /

   private List<FamilyContactDTO> familyContacts;

   // 健康档案列表 /

   private List<HealthRecordDTO> healthRecords;

   // 重点人群标签ID列表 /

   private List<Long> tagIds;

}