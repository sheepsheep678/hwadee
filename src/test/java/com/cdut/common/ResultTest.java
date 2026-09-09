package com.cdut.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * 统一响应封装单元测试。
 */
class ResultTest {

    @Test
    void successShouldReturnCode200() {
        Result<String> result = Result.success("data");
        assertEquals(200, result.getCode());
        assertEquals("操作成功", result.getMessage());
        assertEquals("data", result.getData());
    }

    @Test
    void errorShouldReturnCode500() {
        Result<Void> result = Result.error("参数错误");
        assertEquals(500, result.getCode());
        assertEquals("参数错误", result.getMessage());
        assertNull(result.getData());
    }

}
