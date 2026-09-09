package com.cdut.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT 工具类（签发 / 解析）
 *
 * <p><b>重要约定</b>：JJWT 0.12 反序列化 JSON 数字时类型不固定（Long / Integer 都可能出现），
 * 直接 claims.get("userId", Long.class) 会抛 RequiredTypeException。
 * 因此这里<b>统一以 String 存入</b>，解析后再自行转型。
 */
@Component
public class JwtUtils {

    /** userType：1-管理端 2-医生端 3-老人端 */
    public static final int USER_TYPE_ADMIN = 1;
    public static final int USER_TYPE_DOCTOR = 2;
    public static final int USER_TYPE_ELDER = 3;

    private final SecretKey key;
    private final long expireSeconds;

    public JwtUtils(@Value("${jwt.secret}") String secret,
                    @Value("${jwt.expire:7200}") long expireSeconds) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expireSeconds = expireSeconds;
    }

    /** 签发 token */
    public String generateToken(Long userId, Integer userType, String accountNo) {
        Date now = new Date();
        return Jwts.builder()
                .claim("userId", String.valueOf(userId))
                .claim("userType", String.valueOf(userType))
                .claim("accountNo", accountNo == null ? "" : accountNo)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expireSeconds * 1000))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    /** 解析 token，失败（过期/篡改/格式错误）会抛异常 */
    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public long getExpireSeconds() {
        return expireSeconds;
    }
}
