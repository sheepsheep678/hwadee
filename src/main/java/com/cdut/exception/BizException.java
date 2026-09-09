package com.cdut.exception;

/**
 * 业务异常：Service 层校验不通过时抛出，由 GlobalExceptionHandler 统一转为 Result.error(...)
 *
 * <pre>
 *     throw new BizException("账号或密码错误");
 * </pre>
 */
public class BizException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public BizException(String message) {
        super(message);
    }

    public BizException(String message, Throwable cause) {
        super(message, cause);
    }
}
