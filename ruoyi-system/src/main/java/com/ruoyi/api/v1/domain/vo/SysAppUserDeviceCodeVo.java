package com.ruoyi.api.v1.domain.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ruoyi.common.core.domain.entity.SysAppUser;
import com.ruoyi.common.core.domain.entity.SysAppUserDeviceCode;
import com.ruoyi.common.core.domain.entity.SysDeviceCode;
import com.ruoyi.common.utils.bean.BeanUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysAppUserDeviceCodeVo extends SysAppUserDeviceCode {
    private static final long serialVersionUID = 1L;

    /**
     * 创建者
     */
    @JSONField(serialize = false)
    @JsonIgnore
    private String createBy;

    /**
     * 创建时间
     */
    @JSONField(serialize = false)
    @JsonIgnore
//    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新者
     */
    @JSONField(serialize = false)
    @JsonIgnore
    private String updateBy;

    /**
     * 更新时间
     */
    @JSONField(serialize = false)
    @JsonIgnore
//    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 搜索值
     */
    @JSONField(serialize = false)
    @JsonIgnore
    private String searchValue;

    /**
     * 请求参数
     */
    @JSONField(serialize = false)
    @JsonIgnore
    private Map<String, Object> params;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @JSONField(serialize = false)
    @JsonIgnore
    private String delFlag;

    /**
     * 软件用户ID
     */
    @JSONField(serialize = false)
    @JsonIgnore
    private Long id;

    @JSONField(serialize = false)
    @JsonIgnore
    private Long deviceCodeId;

    /**
     * 用户ID
     */
    @JSONField(serialize = false)
    @JsonIgnore
    private Long appUserId;

    /**
     * 密码连续错误次数
     */
    @JSONField(serialize = false)
    @JsonIgnore
    private SysAppUser appUser;

    /**
     * 单码
     */
    @JSONField(serialize = false)
    @JsonIgnore
    private SysDeviceCode deviceCode;

    private String deviceCodeStr;

    public SysAppUserDeviceCodeVo(SysAppUserDeviceCode v) {
        BeanUtils.copyProperties(v, this);
        this.deviceCodeStr = v.getDeviceCode().getDeviceCode();
    }
}
