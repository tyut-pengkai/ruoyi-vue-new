package com.ruoyi.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户数量统计 sys_app_user_count
 *
 * @author zwgu
 * @date 2023-11-14
 */
public class SysAppUserCount implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    /**
     * 软件ID
     */
    private Long appId;

    /**
     * 额度
     */
    private Long totalNum;
    private Long loginNum;
    private Long vipNum;
    private Long maxOnlineNum;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Long getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Long totalNum) {
        this.totalNum = totalNum;
    }

    public Long getLoginNum() {
        return loginNum;
    }

    public void setLoginNum(Long loginNum) {
        this.loginNum = loginNum;
    }

    public Long getVipNum() {
        return vipNum;
    }

    public void setVipNum(Long vipNum) {
        this.vipNum = vipNum;
    }

    public Long getMaxOnlineNum() {
        return maxOnlineNum;
    }

    public void setMaxOnlineNum(Long maxOnlineNum) {
        this.maxOnlineNum = maxOnlineNum;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
