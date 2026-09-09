package com.cdut.dto;

import com.cdut.entity.FamilyContact;
import com.cdut.entity.HealthRecord;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * 老人档案新增/修改入参
 */
@Data
public class ElderProfileSaveDTO implements Serializable {

    /** 姓名 */
    @NotBlank(message = "姓名不能为空")
    private String name;

    /** 身份证号 */
    @NotBlank(message = "身份证号不能为空")
    private String idCard;

    /** 联系电话 */
    private String phone;

    /** 性别：1-男 2-女 */
    private Integer gender;

    /** 出生日期 */
    private LocalDate birthDate;

    /** 年龄 */
    private Integer age;

    /** 居住类型：1-居家 2-社区 3-机构 4-独居 */
    private Integer livingType;

    /** 居住地址 */
    private String address;

    /** 照片地址 */
    private String photo;

    /** 紧急联系人 */
    private String emergencyContact;

    /** 紧急联系电话 */
    private String emergencyPhone;

    /** 病史 */
    private String medicalHistory;

    /** 备注 */
    private String remark;

    /** 健康档案 */
    private List<HealthRecord> healthRecords;

    /** 家属联系人 */
    private List<FamilyContact> familyContacts;

    /** 标签ID列表 */
    private List<Long> tagIds;

}
