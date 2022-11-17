package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.TreeEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 首页导航对象 sys_navigation
 *
 * @author zwgu
 * @date 2022-11-12
 */
public class SysNavigation extends TreeEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 导航主键
     */
    private Long navId;

    /**
     * 导航标题
     */
    @Excel(name = "导航标题")
    private String navName;

    /**
     * 路由地址
     */
    @Excel(name = "路由地址")
    private String path;

    /**
     * 是否为外链（0是 1否）
     */
    // 暂时未用到
    @Excel(name = "是否为外链", readConverterExp = "0=是,1=否")
    private String isFrame;

    /**
     * 是否设为首页
     */
    @Excel(name = "是否设为首页", readConverterExp = "Y=是,N=否")
    private String isIndex;

    /**
     * 导航类型（M目录 C菜单）
     */
    // 暂时未用到
    @Excel(name = "导航类型", readConverterExp = "M=目录,C=菜单")
    private String navType;

    /**
     * 导航状态（0显示 1隐藏）
     */
    @Excel(name = "导航状态", readConverterExp = "0=显示,1=隐藏")
    private String visible;

    public String getIsIndex() {
        return isIndex;
    }

    public void setIsIndex(String isIndex) {
        this.isIndex = isIndex;
    }

    public Long getNavId() {
        return navId;
    }

    public void setNavId(Long navId) {
        this.navId = navId;
    }

    public String getNavName() {
        return navName;
    }

    public void setNavName(String navName) {
        this.navName = navName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getIsFrame() {
        return isFrame;
    }

    public void setIsFrame(String isFrame) {
        this.isFrame = isFrame;
    }

    public String getNavType() {
        return navType;
    }

    public void setNavType(String navType) {
        this.navType = navType;
    }

    public String getVisible() {
        return visible;
    }

    public void setVisible(String visible) {
        this.visible = visible;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("navId", getNavId())
                .append("navName", getNavName())
                .append("path", getPath())
//            .append("isFrame", getIsFrame())
                .append("parentId", getParentId())
//            .append("navType", getNavType())
                .append("visible", getVisible())
                .append("orderNum", getOrderNum())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
