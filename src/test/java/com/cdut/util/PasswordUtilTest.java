package com.cdut.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 密码加密工具单元测试。
 */
class PasswordUtilTest {

    @Test
    void encryptShouldBeStable() {
        String first = PasswordUtil.encrypt("123456");
        String second = PasswordUtil.encrypt("123456");
        assertNotEquals("123456", first);
        assertTrue(first.length() > 0);
        assertTrue(first.equals(second), "相同明文应产生相同密文");
    }

    @Test
    void matchesShouldVerifyCorrectPassword() {
        String encrypted = PasswordUtil.encrypt("abc123");
        assertTrue(PasswordUtil.matches("abc123", encrypted));
        assertFalse(PasswordUtil.matches("wrong", encrypted));
        assertFalse(PasswordUtil.matches(null, encrypted));
    }

}
