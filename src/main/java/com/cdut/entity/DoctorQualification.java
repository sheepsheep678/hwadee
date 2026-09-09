package com.cdut.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 医生资质证书
 */
@Data
@TableName("doctor_qualification")
public class DoctorQualification implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 医生ID */
    private Long doctorId;

    /** 证书类型 */
    private String certType;

    /** 证书编号 */
    private String certNo;

    /** 证书图片地址 */
    private String certImgUrl;

    /** 有效期 */
    private LocalDate validDate;

    private LocalDateTime createTime;

}
