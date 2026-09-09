package com.cdut.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ElderProfile {

   // 主键ID /

   private Long id;

   // 老人编号 /

   private String elderNo;

   // 关联老人账户ID /

   private Long accountId;

   // 姓名 /

   private String name;

   // 性别：1-男 2-女 /

   private Integer gender;

   // 出生日期 /

   private LocalDate birthDate;

   // 身份证号 /

   private String idCard;

   // 联系电话 /

   private String phone;

   // 居住方式：1-居家 2-社区 3-机构 4-独居 /

   private Integer livingType;

   // 婚姻状况 /

   private Integer maritalStatus;

   // 文化程度 /

   private String education;

   // 居住地址 /

   private String address;

   // 医保号 /

   private String medicareNo;

   // 政府救助类型（低保/特困/高龄补贴） /

   private String govAidType;

   // 照片URL /

   private String photoUrl;

   // 档案状态：1-在管 2-注销 3-转出 /

   private Integer status;

   // 创建时间 /

   private LocalDateTime createTime;

   // 更新时间 /

   private LocalDateTime updateTime;

   // 逻辑删除：0-未删除 1-已删除 /

   private Integer isDeleted;

}