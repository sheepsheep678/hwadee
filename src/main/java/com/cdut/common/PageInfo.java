package com.cdut.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 通用分页响应结构，data 内容形如：
 * <pre>{ "total": 156, "pages": 16, "pageNum": 1, "pageSize": 10, "list": [] }</pre>
 */
@Data
public class PageInfo<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 总记录数 */
    private long total;

    /** 总页数 */
    private long pages;

    /** 当前页码 */
    private long pageNum;

    /** 每页条数 */
    private long pageSize;

    /** 数据列表 */
    private List<T> list;

    public PageInfo() {
        this.list = Collections.emptyList();
    }

    public PageInfo(long total, long pages, long pageNum, long pageSize, List<T> list) {
        this.total = total;
        this.pages = pages;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.list = list == null ? Collections.emptyList() : list;
    }

    /** 由 MyBatis-Plus 分页对象转换 */
    public static <T> PageInfo<T> of(IPage<T> page) {
        return new PageInfo<>(page.getTotal(), page.getPages(), page.getCurrent(), page.getSize(), page.getRecords());
    }

}
