package com.cdut.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 统一分页返回体
 *
 * <p>字段与 PageHelper 的 PageInfo 保持一致（total / pages / pageNum / pageSize / list），
 * 前端按同一套结构解析，无需改动。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 总记录数 */
    private long total;

    /** 总页数 */
    private int pages;

    /** 当前页码 */
    private int pageNum;

    /** 每页条数 */
    private int pageSize;

    /** 当前页数据 */
    private List<T> list;

    public static <T> PageResult<T> of(long total, int pageNum, int pageSize, List<T> list) {
        int pages = pageSize <= 0 ? 0 : (int) ((total + pageSize - 1) / pageSize);
        return new PageResult<>(total, pages, pageNum, pageSize,
                list == null ? Collections.emptyList() : list);
    }

    /** 空分页 */
    public static <T> PageResult<T> empty(int pageNum, int pageSize) {
        return new PageResult<>(0, 0, pageNum, pageSize, Collections.emptyList());
    }
}
