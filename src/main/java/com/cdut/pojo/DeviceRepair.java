package com.cdut.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceRepair {

   // 主键ID /

   private Long id;

   // 设备ID /

   private Long deviceId;

   // 故障描述 /

   private String faultDesc;

   // 报修时间 /

   private LocalDateTime reportTime;

   // 维修公司 /

   private String repairCompany;

   // 修复时间 /

   private LocalDateTime repairTime;

   // 维修费用 /

   private BigDecimal cost;

   // 维修结果 /

   private String result;

   // 创建时间 /

   private LocalDateTime createTime;

   // 更新时间 /

   private LocalDateTime updateTime;

   // 逻辑删除 /

   private Integer isDeleted;

}
