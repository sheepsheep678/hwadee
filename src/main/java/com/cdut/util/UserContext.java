package com.cdut.util;

import com.cdut.common.BusinessException;

/**
 * 登录用户上下文，基于 ThreadLocal，拦截器写入、请求结束清除。
 */
public final class UserContext {

    private static final ThreadLocal<LoginUser> HOLDER = new ThreadLocal<>();

    private UserContext() {
    }

    public static void set(LoginUser user) {
        HOLDER.set(user);
    }

    public static LoginUser get() {
        return HOLDER.get();
    }

    /** 获取当前登录用户，未登录时抛出业务异常 */
    public static LoginUser require() {
        LoginUser user = HOLDER.get();
        if (user == null) {
            throw new BusinessException("未登录或登录已过期");
        }
        return user;
    }

    /** 获取当前医生ID（仅医生端调用） */
    public static Long requireDoctorId() {
        LoginUser user = require();
        if (user.getUserType() == null || user.getUserType() != 2) {
            throw new BusinessException("无权限访问");
        }
        return user.getUserId();
    }

    public static void clear() {
        HOLDER.remove();
    }

}
