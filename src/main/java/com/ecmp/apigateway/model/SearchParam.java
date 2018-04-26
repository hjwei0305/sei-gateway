package com.ecmp.apigateway.model;


import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * @author:wangdayin
 * @date:2018/4/25
 * @remark: 查询参数
 */
public class SearchParam {
    //查询关键
    private String keywords;
    //当前页数
    private int page = 1;
    //每页查询数量
    private int rows = 15;
    //排序字段名
    private String sortName = "id";
    //排序规则
    private Sort.Direction direction = Sort.Direction.DESC;

    //得到jpa pageable对象
    public Pageable getPageable() {
        Sort sort = new Sort(this.direction, this.sortName);
        Pageable pageable = new PageRequest(this.page == 1 ? 0 : this.page - 1, this.rows, sort);
        return pageable;
    }

    public String getKeywords() {
        return keywords;
    }

    public String getLikeKeywords() {
        return "%" + keywords + "%";
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    public Sort.Direction getDirection() {
        return direction;
    }

    public void setDirection(Sort.Direction direction) {
        this.direction = direction;
    }
}
