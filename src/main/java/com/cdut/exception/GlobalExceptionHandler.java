package com.cdut.exception;

import com.cdut.common.BusinessException;
import com.cdut.common.Result;
import com.cdut.common.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * 全局异常处理：将所有异常统一封装为 {@link Result}，保证接口返回格式一致。
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** 业务异常 */
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException ex) {
        log.warn("业务异常: {}", ex.getMessage());
        return Result.error(ex.getCode(), ex.getMessage());
    }

    /** @RequestBody @Validated 校验失败 */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleValidException(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        String message = fieldError == null ? "参数校验失败" : fieldError.getDefaultMessage();
        return Result.error(ResultCode.ERROR, message);
    }

    /** 表单绑定校验失败 */
    @ExceptionHandler(BindException.class)
    public Result<Void> handleBindException(BindException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        String message = fieldError == null ? "参数校验失败" : fieldError.getDefaultMessage();
        return Result.error(ResultCode.ERROR, message);
    }

    /** 缺少必填请求参数 */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result<Void> handleMissingParam(MissingServletRequestParameterException ex) {
        return Result.error(ResultCode.ERROR, "缺少必填参数: " + ex.getParameterName());
    }

    /** 参数类型不匹配 */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result<Void> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return Result.error(ResultCode.ERROR, "参数类型错误: " + ex.getName());
    }

    /** 请求体不可读（JSON 格式错误等） */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<Void> handleNotReadable(HttpMessageNotReadableException ex) {
        return Result.error(ResultCode.ERROR, "请求体格式错误");
    }

    /** 兜底异常 */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception ex) {
        log.error("系统异常", ex);
        return Result.error(ResultCode.ERROR, "系统繁忙，请稍后重试");
    }

}
