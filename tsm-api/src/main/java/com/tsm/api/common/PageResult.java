package com.tsm.api.common;

import java.io.Serializable;
import java.util.List;

/**
 * 分页查询结果类
 * 
 * @author TSM Team
 * @since 1.0.0
 */
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 当前页码
     */
    private long current;

    /**
     * 每页大小
     */
    private long size;

    /**
     * 总记录数
     */
    private long total;

    /**
     * 总页数
     */
    private long pages;

    /**
     * 数据列表
     */
    private List<T> records;

    public PageResult() {
    }

    public PageResult(long current, long size, long total, List<T> records) {
        this.current = current;
        this.size = size;
        this.total = total;
        this.records = records;
        this.pages = (total + size - 1) / size;
    }

    /**
     * 是否有上一页
     */
    public boolean hasPrevious() {
        return current > 1;
    }

    /**
     * 是否有下一页
     */
    public boolean hasNext() {
        return current < pages;
    }

    // Getter and Setter methods
    public long getCurrent() {
        return current;
    }

    public void setCurrent(long current) {
        this.current = current;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
        this.pages = (total + size - 1) / size;
    }

    public long getPages() {
        return pages;
    }

    public void setPages(long pages) {
        this.pages = pages;
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }
}