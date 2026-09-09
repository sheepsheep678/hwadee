package com.cdut.common;

import lombok.Getter;

/**
 * 业务异常。业务流转中的可预期错误（参数错误、数据不存在、规则冲突等）
 * 统一抛出该异常，由 {@code GlobalExceptionHandler} 捕获并封装为 Result(500)。
 */
@Getter
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /** 业务状态码，默认 500 */
    private final int code;

    public BusinessException(String message) {
        super(message);
        this.code = ResultCode.ERROR;
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

}
