package com.cdut.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceBinding {

   // 主键ID /

   private Long id;

   // 设备ID /

   private Long deviceId;

   // 老人ID（可空） /

   private Long elderId;

   // 床位ID（可空） /

   private Long bedId;

   // 绑定时间 /

   private LocalDateTime bindTime;

   // 解绑时间 /

   private LocalDateTime unbindTime;

   // 绑定状态：1-绑定中 2-已解绑 /

   private Integer bindStatus;

   // 创建时间 /

   private LocalDateTime createTime;

   // 更新时间 /

   private LocalDateTime updateTime;

   // 逻辑删除 /

   private Integer isDeleted;

}