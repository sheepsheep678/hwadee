package com.cdut.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 医生资质证书表（doctor_qualification）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorQualification {

    /** 主键ID */
    private Long id;

    /** 医生ID */
    private Long doctorId;

    /** 证书类型：1-执业证 2-职称证 */
    private Integer certType;

    /** 证书编号 */
    private String certNo;

    /** 证书图片URL */
    private String certImgUrl;

    /** 有效期 */
    private LocalDate validDate;

    /** 审核状态：0-待审核 1-已通过 2-已驳回 */
    private Integer auditStatus;

    /** 审核人ID */
    private Long auditorId;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;

    /** 逻辑删除：0-未删除 1-已删除 */
    private Integer isDeleted;
}
