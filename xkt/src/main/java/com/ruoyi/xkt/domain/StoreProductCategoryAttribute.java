package com.ruoyi.xkt.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.core.domain.XktBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 档口商品类目信息对象 store_product_category_attribute
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class StoreProductCategoryAttribute extends XktBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 档口商品类目属性ID
     */
    @TableId
    private Long id;
    /**
     * 档口ID
     */
    private Long storeId;
    /**
     * 档口商品ID
     */
    private Long storeProdId;
    /**
     * 帮面材质
     */
    private String upperMaterial;
    /**
     * 内里材质
     */
    private String liningMaterial;
    /**
     * 鞋垫材质
     */
    private String insoleMaterial;
    /**
     * 上市季节年份
     */
    private String releaseYearSeason;
    /**
     * 后跟高
     */
    private String heelHeight;
    /**
     * 跟底款式
     */
    private String heelType;
    /**
     * 鞋头款式
     */
    private String toeStyle;
    /**
     * 适合季节
     */
    private String suitableSeason;
    /**
     * 开口深度
     */
    private String collarDepth;
    /**
     * 鞋底材质
     */
    private String outsoleMaterial;
    /**
     * 风格
     */
    private String style;
    /**
     * 款式
     */
    private String design;
    /**
     * 皮质特征
     */
    private String leatherFeatures;
    /**
     * 制作工艺
     */
    private String manufacturingProcess;
    /**
     * 图案
     */
    private String pattern;
    /**
     * 闭合方式
     */
    private String closureType;
    /**
     * 适用场景
     */
    private String occasion;
    /**
     * 适用年龄
     */
    private String suitableAge;
    /**
     * 厚薄
     */
    private String thickness;
    /**
     * 流行元素
     */
    private String fashionElements;
    /**
     * 适用对象
     */
    private String suitablePerson;

}
