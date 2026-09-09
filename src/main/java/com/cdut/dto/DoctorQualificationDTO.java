package com.cdut.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 医生资质证书
 */
@Data
public class DoctorQualificationDTO implements Serializable {

    /** 证书类型 */
    private String certType;

    /** 证书编号 */
    private String certNo;

    /** 证书图片地址 */
    private String certImgUrl;

    /** 有效期 */
    private LocalDate validDate;

}
