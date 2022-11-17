package com.ruoyi.web.controller.sale.vo;

import com.ruoyi.common.utils.tree.vo.IBaseTree;

import java.util.ArrayList;
import java.util.List;

public class NavVo implements IBaseTree {

    private Long id;
    private Long parentId;
    private Boolean isIndex;
    private NavMetaVo meta;
    private String path;
    private Boolean hidden;
    private List<NavVo> children = new ArrayList<>(0);

    public Boolean getIndex() {
        return isIndex;
    }

    public void setIndex(Boolean index) {
        isIndex = index;
    }

    public NavMetaVo getMeta() {
        return meta;
    }

    public void setMeta(NavMetaVo meta) {
        this.meta = meta;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Boolean getHidden() {
        return hidden;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    @Override
    public List<NavVo> getChildren() {
        return children;
    }

    public void setChildren(List<NavVo> children) {
        this.children = children;
    }
}
