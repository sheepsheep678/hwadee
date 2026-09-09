package com.cdut.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FamilyContact {

   // 主键ID /

   private Long id;

   // 老人ID /

   private Long elderId;

   // 家属姓名 /

   private String name;

   // 与老人关系 /

   private String relation;

   // 联系电话 /

   private String phone;

   // 是否紧急联系人：0-否 1-是 /

   private Integer isPrimary;

   // 家属地址 /

   private String address;

   // 创建时间 /

   private LocalDateTime createTime;

   // 更新时间 /

   private LocalDateTime updateTime;

   // 逻辑删除 /

   private Integer isDeleted;

}
