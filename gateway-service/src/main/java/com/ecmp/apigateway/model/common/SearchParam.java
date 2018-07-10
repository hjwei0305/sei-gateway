package com.ecmp.apigateway.model.common;


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
    private String sidx = "id";
    //排序字段接收
    private String sord = "asc";
    //排序规则
    private Sort.Direction direction = Sort.Direction.DESC;

    //得到jpa pageable对象
    public Pageable getPageable() {
        Sort sort = null;
        if (sord.toUpperCase().equals(Sort.Direction.ASC.name())) {
            sort = new Sort(Sort.Direction.ASC, this.sidx);
        } else {
            sort = new Sort(Sort.Direction.DESC, this.sidx);
        }
        Pageable pageable = new PageRequest(this.page == 1 ? 0 : this.page - 1, this.rows, sort);
        return pageable;
    }

    public void setSord(String sord) {
        this.sord = sord;
    }

    public Sort.Direction getDirection() {
        return direction;
    }

    public void setDirection(Sort.Direction direction) {
        this.direction = direction;
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

    public String getSidx() {
        return sidx;
    }

    public void setSidx(String sidx) {
        this.sidx = sidx;
    }

}
