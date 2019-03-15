package com.ecmp.apigateway.manager.entity.vo;

/**
 * 实现功能：菜单
 *
 * @author cs
 * @version 1.0.00
 */
public class MenuVo {
    private String path;

    private String url;

    private String name;

    private String title;

    private String iconClass;

    public MenuVo(String path, String url, String name, String title, String iconClass) {
        this.path = path;
        this.url = url;
        this.name = name;
        this.title = title;
        this.iconClass = iconClass;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIconClass() {
        return iconClass;
    }

    public void setIconClass(String iconClass) {
        this.iconClass = iconClass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
