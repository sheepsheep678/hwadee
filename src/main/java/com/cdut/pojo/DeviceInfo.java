package com.cdut.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceInfo {

   // 主键ID /

   private Long id;

   // 设备序列号（唯一） /

   private String deviceSn;

   // 设备名称 /

   private String deviceName;

   // 设备类型：1-智能床垫 2-手环 3-呼叫器 4-门磁 5-雷达 /

   private Integer deviceType;

   // 型号 /

   private String model;

   // 品牌 /

   private String brand;

   // 购买日期 /

   private LocalDate purchaseDate;

   // 价格 /

   private BigDecimal price;

   // 归属机构ID /

   private Long orgId;

   // 在线状态：0-离线 1-在线 /

   private Integer onlineStatus;

   // 电量 /

   private Integer batteryLevel;

   // 状态：1-正常 2-维修中 3-报废 /

   private Integer status;

   // 创建时间 /

   private LocalDateTime createTime;

   // 更新时间 /

   private LocalDateTime updateTime;

   // 逻辑删除 /

   private Integer isDeleted;

   // 绑定老人姓名（联表填充，非表字段） /

   private String elderName;

   // 绑定老人联系电话（联表填充，非表字段） /

   private String elderPhone;

}