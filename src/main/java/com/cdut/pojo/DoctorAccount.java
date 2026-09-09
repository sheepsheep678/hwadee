package com.cdut.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 医生账户表（doctor_account）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorAccount {

    /** 主键ID */
    private Long id;

    /** 姓名 */
    private String name;

    /** 手机号 */
    private String phone;

    /** 密码（加密） */
    private String password;

    /** 医生类型：1-家庭医生 2-专科医生 3-康复师 4-护理师 */
    private Integer doctorType;

    /** 职称 */
    private String title;

    /** 科室 */
    private String dept;

    /** 所属机构ID */
    private Long orgId;

    /** 入驻审核状态：0-待审核 1-已通过 2-已驳回 */
    private Integer auditStatus;

    /** 账户状态：1-正常 2-禁用 */
    private Integer accountStatus;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;

    /** 逻辑删除：0-未删除 1-已删除 */
    private Integer isDeleted;
}
