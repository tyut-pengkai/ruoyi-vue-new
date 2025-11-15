package com.ruoyi.xkt.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.XktBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 档口商品工艺信息对象 store_product_process
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class StoreProductProcess extends XktBaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 档口商品工艺信息ID
     */
    @TableId
    private Long id;

    /**
     * 档口商品ID
     */
    @Excel(name = "档口商品ID")
    private Long storeProdId;
    /**
     * 客户
     */
    private String partnerName;
    /**
     * 商标
     */
    private String trademark;
    /**
     * 鞋型
     */
    @Excel(name = "鞋型")
    private String shoeType;

    /**
     * 楦号
     */
    @Excel(name = "楦号")
    private String shoeSize;

    /**
     * 主皮
     */
    @Excel(name = "主皮")
    private String mainSkin;

    /**
     * 主皮用量
     */
    @Excel(name = "主皮用量")
    private String mainSkinUsage;

    /**
     * 配皮
     */
    @Excel(name = "配皮")
    private String matchSkin;

    /**
     * 配皮用量
     */
    @Excel(name = "配皮用量")
    private String matchSkinUsage;

    /**
     * 领口
     */
    @Excel(name = "领口")
    private String neckline;

    /**
     * 膛底
     */
    @Excel(name = "膛底")
    private String insole;

    /**
     * 扣件/拉头
     */
    @Excel(name = "扣件/拉头")
    private String fastener;

    /**
     * 辅料
     */
    @Excel(name = "辅料")
    private String shoeAccessories;

    /**
     * 包头
     */
    @Excel(name = "包头")
    private String toeCap;

    /**
     * 包边
     */
    @Excel(name = "包边")
    private String edgeBinding;

    /**
     * 中大底
     */
    @Excel(name = "中大底")
    private String midOutsole;

    /**
     * 防水台
     */
    @Excel(name = "防水台")
    private String platformSole;

    /**
     * 中底厂家编码
     */
    @Excel(name = "中底厂家编码")
    private String midsoleFactoryCode;

    /**
     * 外底厂家编码
     */
    @Excel(name = "外底厂家编码")
    private String outsoleFactoryCode;

    /**
     * 跟厂编码
     */
    @Excel(name = "跟厂编码")
    private String heelFactoryCode;

    /**
     * 配料
     */
    @Excel(name = "配料")
    private String components;

    /**
     * 第二底料
     */
    @Excel(name = "第二底料")
    private String secondSoleMaterial;

    /**
     * 第二配料
     */
    @Excel(name = "第二配料")
    private String secondUpperMaterial;

    /**
     * 自定义
     */
    @Excel(name = "自定义")
    private String customAttr;


}
