package com.cdut.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 老人档案
 */
@Data
@TableName("elder_profile")
public class ElderProfile implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 姓名 */
    private String name;

    /** 身份证号 */
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

    /** 创建医生ID */
    private Long createBy;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;

}
