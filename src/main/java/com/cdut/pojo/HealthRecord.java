package com.cdut.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HealthRecord {

   // 主键ID /

   private Long id;

   // 老人ID /

   private Long elderId;

   // 记录类型：1-既往史 2-过敏史 3-用药史 4-手术史 5-家族史 /

   private Integer recordType;

   // 疾病/事项名称 /

   private String diseaseName;

   // ICD-10编码 /

   private String icdCode;

   // 确诊日期 /

   private LocalDate diagnoseDate;

   // 就诊医院 /

   private String hospital;

   // 详情描述 /

   private String detail;

   // 当前用药 /

   private String medication;

   // 创建时间 /

   private LocalDateTime createTime;

   // 更新时间 /

   private LocalDateTime updateTime;

   // 逻辑删除 /

   private Integer isDeleted;

}