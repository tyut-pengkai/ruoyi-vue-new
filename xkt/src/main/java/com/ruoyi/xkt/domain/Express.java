package com.ruoyi.xkt.domain;

import com.baomidou.mybatisplus.annotation.Version;
import com.ruoyi.common.core.domain.SimpleEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 物流信息
 *
 * @author liangyq
 * @date 2025-04-01 11:57:52.434
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Express extends SimpleEntity {
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
     * 版本号
     */
    @Version
    private Long version;
}
