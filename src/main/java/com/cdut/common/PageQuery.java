package com.cdut.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 分页查询基础参数，供各查询 DTO 继承。
 */
@Data
public class PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 当前页码，默认 1 */
    private Integer pageNum = 1;

    /** 每页条数，默认 10 */
    private Integer pageSize = 10;

    /** 获取安全页码 */
    public int safePageNum() {
        return pageNum == null || pageNum < 1 ? 1 : pageNum;
    }

    /** 获取安全页大小 */
    public int safePageSize() {
        if (pageSize == null || pageSize < 1) {
            return 10;
        }
        return Math.min(pageSize, 100);
    }

}
