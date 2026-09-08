package com.cdut.dto;

import com.cdut.pojo.DeviceInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DeviceQueryDTO extends DeviceInfo {

   // 关键字：设备SN/名称模糊查询 /

   private String keyword;
   // 归属老人ID（可选筛选） /

   private Integer elderId;

}