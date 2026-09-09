package com.cdut.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 医生账号
 */
@Data
@TableName("doctor_account")
public class DoctorAccount implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 手机号（登录账号） */
    private String phone;

    /** 密码（加密存储） */
    private String password;

    /** 姓名 */
    private String name;

    /** 医生类型：1-家庭医生 2-专科医生 3-康复师 4-护理师 */
    private Integer doctorType;

    /** 职称 */
    private String title;

    /** 科室 */
    private String dept;

    /** 所属机构ID */
    private Long orgId;

    /** 头像 */
    private String avatar;

    /** 状态：0-待审核 1-已审核 2-已拒绝 3-冻结 */
    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;

}
