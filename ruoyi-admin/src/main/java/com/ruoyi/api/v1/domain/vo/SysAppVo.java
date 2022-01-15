package com.ruoyi.api.v1.domain.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ruoyi.common.core.domain.entity.SysApp;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.enums.EncrypType;
import com.ruoyi.common.utils.bean.BeanUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysAppVo extends SysApp {
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
     * 软件ID
     */
    @JSONField(serialize = false)
    @JsonIgnore
    private Long appId;

    /**
     * API接口地址
     */
    @JSONField(serialize = false)
    @JsonIgnore
    private String apiUrl;

    /**
     * 数据输入加密方式
     */
    @JSONField(serialize = false)
    @JsonIgnore
    private EncrypType dataInEnc;

    /**
     * 数据输入加密密码
     */
    @JSONField(serialize = false)
    @JsonIgnore
    private String dataInPwd;

    /**
     * 数据输出加密方式
     */
    @JSONField(serialize = false)
    @JsonIgnore
    private EncrypType dataOutEnc;

    /**
     * 数据输出加密密码
     */
    @JSONField(serialize = false)
    @JsonIgnore
    private String dataOutPwd;

    /**
     * APP KEY
     */
    @JSONField(serialize = false)
    @JsonIgnore
    private String appKey;

    /**
     * APP SECRET
     */
    @JSONField(serialize = false)
    @JsonIgnore
    private String appSecret;

    /**
     * API匿名密码
     */
    @JSONField(serialize = false)
    @JsonIgnore
    private String apiPwd;

    /**
     * 软件开发者信息
     */
    @JSONField(serialize = false)
    @JsonIgnore
    private SysUser developer;

    @JSONField(serialize = false)
    @JsonIgnore
    private List<Map<String, String>> enApi;


    public SysAppVo(SysApp v) {
        BeanUtils.copyProperties(v, this);
    }
}
