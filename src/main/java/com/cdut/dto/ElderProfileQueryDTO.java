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

   /** 当前页码，默认 1 */
   private Integer pageNum = 1;

   /** 每页条数，默认 10 */
   private Integer pageSize = 10;

   // 标签ID（重点人群筛选） /

   private Long tagId;

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