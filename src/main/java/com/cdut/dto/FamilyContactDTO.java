package com.cdut.dto;

import com.cdut.pojo.FamilyContact;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class FamilyContactDTO extends FamilyContact {

   @NotBlank(message = "家属姓名不能为空")
   private String name;

   @NotBlank(message = "与老人关系不能为空")
   private String relation;

   @NotBlank(message = "联系电话不能为空")
   private String phone;

}
