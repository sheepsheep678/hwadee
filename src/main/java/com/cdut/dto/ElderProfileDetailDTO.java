package com.cdut.dto;

import com.cdut.entity.ElderProfile;
import com.cdut.entity.ElderTag;
import com.cdut.entity.FamilyContact;
import com.cdut.entity.HealthRecord;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 老人档案详情（含健康档案、家属联系人、标签）
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ElderProfileDetailDTO extends ElderProfile implements Serializable {

    /** 健康档案 */
    private List<HealthRecord> healthRecords;

    /** 家属联系人 */
    private List<FamilyContact> familyContacts;

    /** 标签 */
    private List<ElderTag> tags;

}
