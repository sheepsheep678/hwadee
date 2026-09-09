package com.cdut.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 医生个人资料修改入参
 */
@Data
public class DoctorSaveDTO implements Serializable {

    /** 姓名 */
    private String name;

    /** 职称 */
    private String title;

    /** 科室 */
    private String dept;

    /** 手机号 */
    private String phone;

}
