package com.cdut.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 老人家属联系人
 */
@Data
@TableName("family_contact")
public class FamilyContact implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 老人ID */
    private Long elderId;

    /** 姓名 */
    private String name;

    /** 关系 */
    private String relation;

    /** 联系电话 */
    private String phone;

    /** 是否默认联系人：0-否 1-是 */
    private Integer isDefault;

}
