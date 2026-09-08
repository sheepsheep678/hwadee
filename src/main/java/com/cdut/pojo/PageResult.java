package com.cdut.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

// 统一分页结果封装：总记录数 + 总页数 + 当前页数据
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> implements Serializable {

   // 总记录数 /
   private Long total;

   // 总页数 /
   private Integer pages;

   // 当前页码 /
   private Integer pageNum=1;

   // 每页条数 /
   private Integer pageSize=10;

   // 当前页数据列表 /
   private List<T> list;

   // 快捷构建分页结果 /
   public static <T> PageResult<T> of(List<T> list, long total, int pageNum, int pageSize) {
       int pages = pageSize > 0 ? (int) ((total + pageSize - 1) / pageSize) : 0;
       return new PageResult<>(total, pages, pageNum, pageSize, list);
   }
   public static <T> PageResult<T> of(List<T> list, long total) {
       return new PageResult<>(total, (int) ((total + 10 - 1) / 10), 1, 10, list);
   }
}