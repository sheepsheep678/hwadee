package com.cdut.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


//统一响应封装：code + message + data
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> implements Serializable {


   // 响应状态码 /

   private Integer code;

   // 提示信息 /

   private String message;

   // 响应数据 /

   private T data;

   // 成功响应（带数据） /

   public static <T> Result<T> success(T data) {

       return new Result<>(200, "操作成功", data);

   }
   // 成功响应（自定义提示） /

   public static <T> Result<T> success(String message) {

       return new Result<>(200, message, null);

   }

   // 成功响应（自定义提示 + 数据） /

   public static <T> Result<T> success(String message, T data) {

       return new Result<>(200, message, data);

   }

   // 失败响应（状态码 + 错误信息） /

   public static <T> Result<T> error(String message) {

       return new Result<>(500, message, null);

   }

}
