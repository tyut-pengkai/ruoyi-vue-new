package com.ruoyi.xkt.dto.storeProductDemand;

import com.ruoyi.common.annotation.Excel;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.poi.ss.usermodel.IndexedColors;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
@Accessors(chain = true)
public class StoreProdDemandDownloadDTO {

    @Excel(name = "客户", width = 10, height = 24, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private String partnerName;
    @Excel(name = "商标", width = 10, height = 24, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private String trademark;
    @Excel(name = "工厂名称", width = 10, height = 24, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private String facName;
    @Excel(name = "需求单号", needMerge = true, width = 18, height = 24, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private String code;
    @Excel(name = "日期", width = 4, height = 24, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private String createTime;
    @Excel(name = "工厂货号", width = 10, height = 24, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private String factoryArtNum;
    @Excel(name = "商品货号", width = 10, height = 24, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private String prodArtNum;
    @Excel(name = "颜色", width = 10, height = 24, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private String colorName;
    @Excel(name = "内里", width = 10, height = 24, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private String shoeUpperLiningMaterial;
    @Excel(name = "面料", width = 10, height = 24, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private String shaftMaterial;
    @Excel(name = "状态", readConverterExp = "1=待生产,2=生产中,3=生产完成", width = 6, height = 24, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private Integer demandStatus;
    @Excel(name = "紧急度", readConverterExp = "0=正常,1=紧急", width = 5, height = 24, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private Integer emergency;
    @Excel(name = "鞋型", width = 10, height = 24, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private String shoeType;
    @Excel(name = "楦号", width = 10, height = 24, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private String shoeSize;
    @Excel(name = "主皮", width = 10, height = 24, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private String mainSkin;
    @Excel(name = "主皮用量", width = 10, height = 24, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private String mainSkinUsage;
    @Excel(name = "配皮", width = 10, height = 24, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private String matchSkin;
    @Excel(name = "配皮用量", width = 10, height = 24, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private String matchSkinUsage;
    @Excel(name = "领口", width = 10, height = 24, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private String neckline;
    @Excel(name = "膛底", width = 10, height = 24, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private String insole;
    @Excel(name = "扣件/拉头", width = 10, height = 24, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private String fastener;
    @Excel(name = "辅料", width = 10, height = 24, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private String shoeAccessories;
    @Excel(name = "包头", width = 10, height = 24, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private String toeCap;
    @Excel(name = "包边", width = 10, height = 24, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private String edgeBinding;
    @Excel(name = "中大底", width = 10, height = 24, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private String midOutsole;
    @Excel(name = "防水台", width = 10, height = 24, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private String platformSole;
    @Excel(name = "中底厂家编码", width = 10, height = 24, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private String midsoleFactoryCode;
    @Excel(name = "外底厂家编码", width = 10, height = 24, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private String outsoleFactoryCode;
    @Excel(name = "跟厂编码", width = 10, height = 24, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private String heelFactoryCode;
    @Excel(name = "配料", width = 10, height = 24, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private String components;
    @Excel(name = "第二底料", width = 10, height = 24, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private String secondSoleMaterial;
    @Excel(name = "第二配料", width = 10, height = 24, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private String secondUpperMaterial;
    @Excel(name = "30", width = 2, height = 24, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private Integer size30Quantity;
    @Excel(name = "31", width = 2, height = 24, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private Integer size31Quantity;
    @Excel(name = "32", width = 2, height = 24, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private Integer size32Quantity;
    @Excel(name = "33", width = 2, height = 24, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private Integer size33Quantity;
    @Excel(name = "34", width = 2, height = 24, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private Integer size34Quantity;
    @Excel(name = "35", width = 2, height = 24, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private Integer size35Quantity;
    @Excel(name = "36", width = 2, height = 24, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private Integer size36Quantity;
    @Excel(name = "37", width = 2, height = 24, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private Integer size37Quantity;
    @Excel(name = "38", width = 2, height = 24, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private Integer size38Quantity;
    @Excel(name = "39", width = 2, height = 24, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private Integer size39Quantity;
    @Excel(name = "40", width = 2, height = 24, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private Integer size40Quantity;
    @Excel(name = "41", width = 2, height = 24, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private Integer size41Quantity;
    @Excel(name = "42", width = 2, height = 24, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private Integer size42Quantity;
    @Excel(name = "43", width = 2, height = 24, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private Integer size43Quantity;
    @Excel(name = "总数", width = 3, height = 24, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private Integer quantity;

}
