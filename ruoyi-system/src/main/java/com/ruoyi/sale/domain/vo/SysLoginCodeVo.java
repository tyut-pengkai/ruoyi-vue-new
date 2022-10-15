package com.ruoyi.sale.domain.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ruoyi.common.core.domain.entity.SysApp;
import com.ruoyi.common.enums.BillType;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.system.domain.SysLoginCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysLoginCodeVo extends SysLoginCode {
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
     * 卡密ID
     */
    @JSONField(serialize = false)
    @JsonIgnore
    private Long cardId;

    /**
     * 软件ID
     */
    @JSONField(serialize = false)
    @JsonIgnore
    private Long appId;

    /**
     * 价格
     */
    @JSONField(serialize = false)
    @JsonIgnore
    private BigDecimal price;

    /**
     * 是否售出
     */
    @JSONField(serialize = false)
    @JsonIgnore
    private String isSold;

    /**
     * 是否上架
     */
    @JSONField(serialize = false)
    @JsonIgnore
    private String onSale;

    /**
     * 卡类ID
     */
    @JSONField(serialize = false)
    @JsonIgnore
    private Long templateId;

    /**
     * 是否代理制卡
     */
    @JSONField(serialize = false)
    @JsonIgnore
    private String isAgent;

    /**
     * 卡密自定义参数
     */
    @JSONField(serialize = false)
    @JsonIgnore
    private String cardCustomParams;

    /**
     * 制卡批次号
     */
    @JSONField(serialize = false)
    @JsonIgnore
    private String batchNo;

    /**
     * 所属软件信息
     */
    @JSONField(serialize = false)
    @JsonIgnore
    private SysApp app;

    private String appName;

    private BillType billType;

    public SysLoginCodeVo(SysLoginCode v) {
        BeanUtils.copyProperties(v, this);
        this.appName = v.getApp().getAppName();
        this.billType = v.getApp().getBillType();
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
