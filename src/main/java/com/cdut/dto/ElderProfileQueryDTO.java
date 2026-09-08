package com.cdut.dto;

import com.cdut.pojo.ElderProfile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ElderProfileQueryDTO extends ElderProfile {



   // 标签ID（重点人群筛选） /

   private Long tagId;

}