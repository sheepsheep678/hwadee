package com.cdut.interceptor;

import com.cdut.common.Result;
import com.cdut.common.ResultCode;
import com.cdut.util.JwtUtil;
import com.cdut.util.LoginUser;
import com.cdut.util.UserContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import java.nio.charset.StandardCharsets;

/**
 * 登录鉴权 + 角色校验拦截器。
 * <p>除登录/注册外，所有请求需携带 {@code Authorization: Bearer {accessToken}}；</p>
 * <p>访问 /api/doctor/** 的接口仅允许 userType=2（医生）角色。</p>
 */
@Component
@RequiredArgsConstructor
public class LoginInterceptor implements HandlerInterceptor {

    private static final String TOKEN_PREFIX = "Bearer ";

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // CORS 预检请求直接放行
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String token = resolveToken(request);
        LoginUser user = token == null ? null : jwtUtil.parseToken(token);
        if (user == null) {
            writeUnauthorized(response, "未登录或登录已过期");
            return false;
        }

        // 医生端接口仅允许医生角色(userType=2)访问
        if (request.getRequestURI().startsWith("/api/doctor/") && (user.getUserType() == null || user.getUserType() != 2)) {
            writeUnauthorized(response, "无权限访问");
            return false;
        }

        UserContext.set(user);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UserContext.clear();
    }

    private String resolveToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (StringUtils.hasText(header) && header.startsWith(TOKEN_PREFIX)) {
            return header.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    private void writeUnauthorized(HttpServletResponse response, String message) throws Exception {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(Result.error(ResultCode.ERROR, message)));
    }

}
