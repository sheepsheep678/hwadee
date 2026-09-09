package com.cdut.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 密码加密工具：SHA-256 + 固定盐，单向哈希存储。
 */
public final class PasswordUtil {

    private static final String SALT = "hwadee-yiyang#salt@2026";

    private PasswordUtil() {
    }

    /** 加密密码 */
    public static String encrypt(String rawPassword) {
        if (rawPassword == null || rawPassword.isEmpty()) {
            return "";
        }
        return sha256(rawPassword + SALT);
    }

    /** 校验密码 */
    public static boolean matches(String rawPassword, String encrypted) {
        if (rawPassword == null || encrypted == null) {
            return false;
        }
        return encrypt(rawPassword).equals(encrypted);
    }

    private static String sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 算法不可用", e);
        }
    }

}
