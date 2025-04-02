package com.ruoyi.xkt.dto.express;

import lombok.Data;

import java.util.Date;

/**
 * 物流信息
 *
 * @author liangyq
 * @date 2025-04-01 11:57:52.434
 **/
@Data
public class ExpressDTO {
    /**
     * 物流ID
     */
    private Long id;
    /**
     * 物流编码
     */
    private String expressCode;
    /**
     * 物流名称
     */
    private String expressName;
    /**
     * 系统发货可选
     */
    private Boolean systemDeliverAccess;
    /**
     * 档口发货可选
     */
    private Boolean storeDeliverAccess;
    /**
     * 用户退货可选
     */
    private Boolean userRefundAccess;
    /**
     * 系统配置
     */
    private String systemConfig;
    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;
    /**
     * 创建者
     */
    private String createBy;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新者
     */
    private String updateBy;
    /**
     * 更新时间
     */
    private Date updateTime;
}
