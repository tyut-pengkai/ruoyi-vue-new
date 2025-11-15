package com.ruoyi.xkt.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.core.domain.XktBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 下载生产需求模板 store_product_demand_template
 * 0 代表未选中 1代表选中
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class StoreProductDemandTemplate extends XktBaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 档口商品需求ID
     */
    @TableId
    private Long id;
    /**
     * 档口ID
     */
    private Long storeId;
    /**
     * 尺码30
     */
    private Integer selectSize30;
    /**
     * 尺码31
     */
    private Integer selectSize31;
    /**
     * 尺码32
     */
    private Integer selectSize32;
    /**
     * 尺码33
     */
    private Integer selectSize33;
    /**
     * 尺码34
     */
    private Integer selectSize34;
    /**
     * 尺码35
     */
    private Integer selectSize35;
    /**
     * 尺码36
     */
    private Integer selectSize36;
    /**
     * 尺码37
     */
    private Integer selectSize37;
    /**
     * 尺码38
     */
    private Integer selectSize38;
    /**
     * 尺码39
     */
    private Integer selectSize39;
    /**
     * 尺码40
     */
    private Integer selectSize40;
    /**
     * 尺码41
     */
    private Integer selectSize41;
    /**
     * 尺码42
     */
    private Integer selectSize42;
    /**
     * 尺码43
     */
    private Integer selectSize43;

    /**
     * 商品信息 工厂名称
     */
    private Integer selectFacName;
    /**
     * 商品信息 需求单号
     */
    private Integer selectDemandCode;
    /**
     * 商品信息 提单时间
     */
    private Integer selectMakeTime;
    /**
     * 商品信息 工厂货号
     */
    private Integer selectFactoryArtNum;
    /**
     * 商品信息 商品货号
     */
    private Integer selectProdArtNum;
    /**
     * 商品信息 颜色
     */
    private Integer selectColorName;
    /**
     * 商品信息 内里材质
     */
    private Integer selectShoeUpperLiningMaterial;
    /**
     * 商品信息 鞋面材质
     */
    private Integer selectShaftMaterial;
    /**
     * 商品信息 生产状态 1 待生产 2 生产中 3 生产完成
     */
    private Integer selectDemandStatus;
    /**
     * 商品信息 紧急程度 0正常 1紧急
     */
    private Integer selectEmergency;
    /**
     * 商品信息 总需求数量
     */
    private Integer selectQuantity;
    /**
     * 工艺信息 客户名称
     */
    private Integer selectPartnerName;
    /**
     * 工艺信息 商标
     */
    private Integer selectTrademark;
    /**
     * 工艺信息 鞋型
     */
    private Integer selectShoeType;
    /**
     * 工艺信息 楦号
     */
    private Integer selectShoeSize;
    /**
     * 工艺信息 主皮
     */
    private Integer selectMainSkin;
    /**
     * 工艺信息 主皮用量
     */
    private Integer selectMainSkinUsage;
    /**
     * 工艺信息 配皮
     */
    private Integer selectMatchSkin;
    /**
     * 工艺信息 配皮用量
     */
    private Integer selectMatchSkinUsage;
    /**
     * 工艺信息 领口
     */
    private Integer selectNeckline;
    /**
     * 工艺信息 膛底
     */
    private Integer selectInsole;
    /**
     * 工艺信息 扣件/拉头
     */
    private Integer selectFastener;
    /**
     * 工艺信息 辅料
     */
    private Integer selectShoeAccessories;
    /**
     * 工艺信息 包头
     */
    private Integer selectToeCap;
    /**
     * 工艺信息 包边
     */
    private Integer selectEdgeBinding;
    /**
     * 工艺信息 中大底
     */
    private Integer selectMidOutsole;
    /**
     * 工艺信息 防水台
     */
    private Integer selectPlatformSole;
    /**
     * 工艺信息 中底厂家编码
     */
    private Integer selectMidsoleFactoryCode;
    /**
     * 工艺信息 外底厂家编码
     */
    private Integer selectOutsoleFactoryCode;
    /**
     * 工艺信息 跟厂编码
     */
    private Integer selectHeelFactoryCode;
    /**
     * 工艺信息 配料
     */
    private Integer selectComponents;
    /**
     * 工艺信息 第二底料
     */
    private Integer selectSecondSoleMaterial;
    /**
     * 工艺信息 第二配料
     */
    private Integer selectSecondUpperMaterial;

}
