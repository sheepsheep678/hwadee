package com.cdut.dto;

import com.cdut.entity.ElderProfile;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 老人档案分页查询条件（extends ElderProfile）
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ElderProfileQueryDTO extends ElderProfile implements Serializable {

    /** 当前页码 */
    private Integer pageNum = 1;

    /** 每页条数 */
    private Integer pageSize = 10;

    /** 标签ID（重点人群筛选） */
    private Long tagId;

    public int safePageNum() {
        return pageNum == null || pageNum < 1 ? 1 : pageNum;
    }

    public int safePageSize() {
        if (pageSize == null || pageSize < 1) {
            return 10;
        }
        return Math.min(pageSize, 100);
    }

}
