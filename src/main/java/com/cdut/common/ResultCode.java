package com.cdut.common;

/**
 * 统一响应状态码。按接口文档约定，仅保留两种：
 * 200-成功；500-失败。
 */
public final class ResultCode {

    private ResultCode() {
    }

    /** 成功 */
    public static final int SUCCESS = 200;

    /** 失败 */
    public static final int ERROR = 500;

}
