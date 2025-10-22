package com.ruoyi.xkt.dto.storeProductDemand;

import com.ruoyi.common.annotation.Excel;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
@Accessors(chain = true)

public class StoreProdDemandDownloadDTO {

    @Excel(name = "序号", cellType = Excel.ColumnType.NUMERIC, needMerge = true, height = 28)
    private Integer orderNum;
    @Excel(name = "货号", needMerge = true, height = 28)
    private String prodArtNum;
    @Excel(name = "需求单号", needMerge = true, height = 28)
    private String code;
    @Excel(name = "工厂名称", needMerge = true, height = 28)
    private String facName;
    @Excel(name = "是否紧急单", height = 28)
    private String emergency;
    @Excel(name = "颜色", height = 28)
    private String colorName;
    @Excel(name = "30", height = 28)
    private Integer size30Quantity;
    @Excel(name = "31", height = 28)
    private Integer size31Quantity;
    @Excel(name = "32", height = 28)
    private Integer size32Quantity;
    @Excel(name = "33", height = 28)
    private Integer size33Quantity;
    @Excel(name = "34", height = 28)
    private Integer size34Quantity;
    @Excel(name = "35", height = 28)
    private Integer size35Quantity;
    @Excel(name = "36", height = 28)
    private Integer size36Quantity;
    @Excel(name = "37", height = 28)
    private Integer size37Quantity;
    @Excel(name = "38", height = 28)
    private Integer size38Quantity;
    @Excel(name = "39", height = 28)
    private Integer size39Quantity;
    @Excel(name = "40", height = 28)
    private Integer size40Quantity;
    @Excel(name = "41", height = 28)
    private Integer size41Quantity;
    @Excel(name = "42", height = 28)
    private Integer size42Quantity;
    @Excel(name = "43", height = 28)
    private Integer size43Quantity;
    @Excel(name = "合计", height = 28)
    private Integer totalQuantity;

}
