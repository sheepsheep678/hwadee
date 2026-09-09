package com.cdut.service;

import com.cdut.dto.DoctorLoginDTO;
import com.cdut.dto.DoctorRegisterDTO;
import com.cdut.dto.LoginRespDTO;

/**
 * 医生登录认证服务
 */
public interface DoctorAuthService {

    /** 医生登录 */
    LoginRespDTO login(DoctorLoginDTO dto);

    /** 医生退出登录 */
    void logout();

    /** 医生注册（入驻申请，提交后待审核） */
    void register(DoctorRegisterDTO dto);

}
