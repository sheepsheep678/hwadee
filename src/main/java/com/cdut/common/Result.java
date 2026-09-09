package com.cdut.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 统一响应封装：code + message + data。
 * <p>响应码约定：200-成功，500-失败（参数错误/无权限/数据不存在/业务规则冲突等，具体原因见 message）。</p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 响应状态码 */
    private Integer code;

    /** 提示信息 */
    private String message;

    /** 响应数据 */
    private T data;

    /** 成功响应（无数据） */
    public static <T> Result<T> success() {
        return new Result<>(ResultCode.SUCCESS, "操作成功", null);
    }

    /** 成功响应（带数据） */
    public static <T> Result<T> success(T data) {
        return new Result<>(ResultCode.SUCCESS, "操作成功", data);
    }

    /** 成功响应（自定义提示 + 数据） */
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(ResultCode.SUCCESS, message, data);
    }

    /** 失败响应（默认 500） */
    public static <T> Result<T> error(String message) {
        return new Result<>(ResultCode.ERROR, message, null);
    }

    /** 失败响应（自定义状态码 + 提示） */
    public static <T> Result<T> error(Integer code, String message) {
        return new Result<>(code, message, null);
    }

}
