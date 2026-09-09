package com.cdut.dto;

import com.cdut.pojo.DeviceInfo;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DeviceSaveDTO extends DeviceInfo {

   @NotBlank(message = "设备SN不能为空")

   private String deviceSn;

   @NotBlank(message = "设备名称不能为空")

   private String deviceName;

}