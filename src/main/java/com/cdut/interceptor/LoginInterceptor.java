package com.cdut.interceptor;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    // 你的密钥，必须和生成 Token 时用的密钥一样，且长度至少 32 字节（HS256 要求）
    private final String SECRET_KEY = "your-very-secret-key-must-be-at-least-32-bytes-long";

    // 根据字符串密钥生成 SecretKey 对象
    private final SecretKey signingKey = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 获取请求头中的 Authorization
        String token = request.getHeader("Authorization");

        // 实际情况：前端通常会带 "Bearer " 前缀，例如 "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
        // 我们需要先去掉 "Bearer " 前缀
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // 去掉前缀，拿到真正的 token 字符串
        }

        // 2. 解析 Token (如果 token 为空或格式不对，这里会抛出异常)
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(signingKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            // 3. 从 Token 中提取用户 ID
            // 这里的 "userId" 是你生成 token 时存进去的 key，要对应上
            String userId = claims.get("userId", String.class);

            // 4. 将 userId 存入 request 的属性中，供后续的 Controller 使用
            request.setAttribute("currentUserId", userId);

            // 5. 放行，让请求继续到达 Controller
            return true;

        } catch (Exception e) {
            // 解析失败（token 过期、被篡改等）
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 返回 401 状态码
            response.getWriter().write("Token无效或已过期");
            return false; // 拦截请求
        }
    }
}
