package com.cdut.utils;

/**
 * 当前登录用户上下文（ThreadLocal）
 *
 * <p>由 LoginInterceptor 在 preHandle 中写入，Controller / Service 直接
 * {@code UserContext.getUserId()} 取值，不再从 request.getAttribute 里捞。
 *
 * <p><b>必须在拦截器的 afterCompletion 里调用 clear()</b>，
 * 否则 Tomcat 线程池复用线程会导致用户串号（A 看到 B 的数据）。
 */
public final class UserContext {

    private static final ThreadLocal<Long> USER_ID = new ThreadLocal<>();
    private static final ThreadLocal<Integer> USER_TYPE = new ThreadLocal<>();

    private UserContext() {
    }

    public static void set(Long userId, Integer userType) {
        USER_ID.set(userId);
        USER_TYPE.set(userType);
    }

    public static Long getUserId() {
        return USER_ID.get();
    }

    public static Integer getUserType() {
        return USER_TYPE.get();
    }

    public static void clear() {
        USER_ID.remove();
        USER_TYPE.remove();
    }
}
