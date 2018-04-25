package com.ecmp.apigateway.model;

import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author:wangdayin
 * @date:2018/4/25
 * @remark: 分页返回对象
 */
public class PageModel<T> implements Serializable {
    //当前页
    private int pageNum;
    //每页的数量
    private int pageSize;
    //当前页的数量
    private int size;
    //排序
    private Iterable<Sort> sort;

    //总记录数
    private long total;
    //总页数
    private int pages;
    //结果集
    private List<T> list;

    //是否为第一页
    private boolean first = false;
    //是否为最后一页
    private boolean last = false;

    public static <T> PageModel<T> from(Page<T> page) {
        return new PageModel<>(page);
    }

    public PageModel(Page<T> page) {
        //pageable页数从0开始
        this.pageNum = page.getNumber() + 1;
        this.pageSize = page.getNumberOfElements();
        this.size = page.getSize();
        this.list = page.getContent();
        this.total = page.getTotalElements();
        this.pages = page.getTotalPages();
        this.first = page.isFirst();
        this.last = page.isLast();
        if (page.getSort() != null) {
            List<Sort> sorts = new ArrayList<>();
            page.getSort().forEach(order -> sorts.add(new Sort(order.getDirection().name(), order.getProperty())));
            this.sort = sorts;
        }
    }


    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Iterable<Sort> getSort() {
        return sort;
    }

    public void setSort(Iterable<Sort> sort) {
        this.sort = sort;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    static class Sort implements Serializable {
        private final String direction;
        private final String property;

        Sort(String direction, String property) {
            this.direction = direction;
            this.property = property;
        }

        public String getDirection() {
            return direction;
        }

        public String getProperty() {
            return property;
        }
    }
}
