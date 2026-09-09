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

   /** 当前页码，默认 1 */
   private Integer pageNum = 1;

   /** 每页条数，默认 10 */
   private Integer pageSize = 10;

   // 关键字：设备SN/名称模糊查询 /

   private String keyword;
   // 归属老人ID（可选筛选） /

   private Integer elderId;

   /** 获取安全页码 */
   public int safePageNum() {
      return pageNum == null || pageNum < 1 ? 1 : pageNum;
   }

   /** 获取安全页大小 */
   public int safePageSize() {
      if (pageSize == null || pageSize < 1) {
         return 10;
      }
      return Math.min(pageSize, 100);
   }

}