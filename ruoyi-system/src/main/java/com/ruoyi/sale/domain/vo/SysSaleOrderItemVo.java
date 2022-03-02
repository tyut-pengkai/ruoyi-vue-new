package com.ruoyi.sale.domain.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.sale.domain.SysSaleOrderItem;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysSaleOrderItemVo extends SysSaleOrderItem {
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
     * 主键ID
     */
    @JSONField(serialize = false)
    @JsonIgnore
    private Long itemId;

    /**
     * 订单ID
     */
    @JSONField(serialize = false)
    @JsonIgnore
    private Long orderId;

    /**
     * 模板ID
     */
    @JSONField(serialize = false)
    @JsonIgnore
    private Long templateId;

    public SysSaleOrderItemVo(SysSaleOrderItem v) {
        BeanUtils.copyProperties(v, this);
    }
}
