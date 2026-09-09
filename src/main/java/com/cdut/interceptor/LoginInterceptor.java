package com.cdut.interceptor;

import com.cdut.utils.JwtUtils;
import com.cdut.utils.UserContext;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

/**
 * 登录拦截器：校验 JWT + 端隔离 + 写入 UserContext
 *
 * <p>端隔离规则（按路径前缀）：
 * <pre>
 *   /api/admin/**  -> userType 必须为 1
 *   /api/doctor/** -> userType 必须为 2
 *   /api/elder/**  -> userType 必须为 3
 * </pre>
 * 放行的认证路径在 WebConfig 里配置（各端的 auth 路径）。
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 静态资源 / 跨域预检等非 Controller 请求直接放行
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        String token = resolveToken(request);
        if (token == null) {
            return unauthorized(response, "请先登录");
        }

        try {
            Claims claims = jwtUtils.parseToken(token);
            Long userId = Long.valueOf(claims.get("userId", String.class));
            Integer userType = Integer.valueOf(claims.get("userType", String.class));

            // 端隔离：医生 token 不能调老人接口，反之亦然
            Integer required = requiredUserType(request.getRequestURI());
            if (required != null && !required.equals(userType)) {
                return unauthorized(response, "无权访问该端接口");
            }

            UserContext.set(userId, userType);
            return true;
        } catch (Exception e) {
            return unauthorized(response, "登录已失效，请重新登录");
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        // 必须清理，否则线程池复用会串号
        UserContext.clear();
    }

    /** 从 Authorization 头取 token，兼容 "Bearer " 前缀 */
    private String resolveToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || header.isBlank()) {
            return null;
        }
        if (header.startsWith("Bearer ")) {
            header = header.substring(7).trim();
        }
        return header.isEmpty() ? null : header;
    }

    /** 路径前缀 -> 允许的 userType，返回 null 表示不限制 */
    private Integer requiredUserType(String uri) {
        if (uri.startsWith("/api/admin/")) {
            return JwtUtils.USER_TYPE_ADMIN;
        }
        if (uri.startsWith("/api/doctor/")) {
            return JwtUtils.USER_TYPE_DOCTOR;
        }
        if (uri.startsWith("/api/elder/")) {
            return JwtUtils.USER_TYPE_ELDER;
        }
        return null;
    }

    /** 返回统一的 Result JSON（而不是纯文本，否则前端解析会崩） */
    private boolean unauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"code\":500,\"message\":\"" + message + "\",\"data\":null}");
        return false;
    }
}
