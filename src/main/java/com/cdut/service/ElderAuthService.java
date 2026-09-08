package com.cdut.service;

import com.cdut.dto.ElderLoginDTO;
import com.cdut.dto.ElderRegisterDTO;
import com.cdut.dto.LoginRespDTO;

public interface ElderAuthService {

    /**
     * 老人注册：同时写入账户与档案，账户初始为「待审核」
     */
    void register(ElderRegisterDTO dto);

    /**
     * 老人登录：支持账号 / 手机号 / 身份证号
     */
    LoginRespDTO login(ElderLoginDTO dto);

    /**
     * 退出登录（JWT 无状态，前端删除 token 即可，后端仅返回成功）
     */
    void logout();
}
