package com.cdut.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT 工具类：生成 / 解析访问令牌与刷新令牌。
 */
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expire}")
    private long expire;

    @Value("${jwt.refresh-expire}")
    private long refreshExpire;

    private SecretKey key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /** 生成访问令牌 */
    public String generateAccessToken(Long userId, Integer userType, Integer doctorType) {
        return buildToken(userId, userType, doctorType, expire);
    }

    /** 生成刷新令牌 */
    public String generateRefreshToken(Long userId, Integer userType, Integer doctorType) {
        return buildToken(userId, userType, doctorType, refreshExpire);
    }

    private String buildToken(Long userId, Integer userType, Integer doctorType, long ttlSeconds) {
        Date now = new Date();
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("userType", userType)
                .claim("doctorType", doctorType)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + ttlSeconds * 1000))
                .signWith(key)
                .compact();
    }

    /** 解析令牌，返回用户上下文；非法/过期令牌返回 null */
    public LoginUser parseToken(String token) {
        try {
            Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
            Long userId = Long.valueOf(claims.getSubject());
            Integer userType = claims.get("userType", Integer.class);
            Integer doctorType = claims.get("doctorType", Integer.class);
            return new LoginUser(userId, userType, doctorType);
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }

    public long getExpire() {
        return expire;
    }

}
