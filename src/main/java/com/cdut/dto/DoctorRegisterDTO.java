package com.cdut.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 医生注册（入驻申请）入参
 */
@Data
public class DoctorRegisterDTO implements Serializable {

    /** 姓名 */
    @NotBlank(message = "姓名不能为空")
    private String name;

    /** 手机号 */
    @NotBlank(message = "手机号不能为空")
    private String phone;

    /** 密码 */
    @NotBlank(message = "密码不能为空")
    private String password;

    /** 医生类型：1-家庭医生 2-专科医生 3-康复师 4-护理师 */
    @NotNull(message = "医生类型不能为空")
    private Integer doctorType;

    /** 职称 */
    private String title;

    /** 科室 */
    private String dept;

    /** 所属机构ID */
    private Long orgId;

    /** 资质证书列表 */
    @Valid
    private List<DoctorQualificationDTO> qualifications;

}
