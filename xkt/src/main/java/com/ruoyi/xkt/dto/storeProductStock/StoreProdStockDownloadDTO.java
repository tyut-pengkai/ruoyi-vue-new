package com.ruoyi.xkt.dto.storeProductStock;

import com.ruoyi.common.annotation.Excel;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.apache.poi.ss.usermodel.IndexedColors;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("库存明细导出")
@Data

public class StoreProdStockDownloadDTO {

    @Excel(name = "序号", cellType = Excel.ColumnType.NUMERIC, width = 6, height = 28, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private Integer orderNum;
    @Excel(name = "货号", width = 20, height = 28, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private String prodArtNum;
    @Excel(name = "颜色", width = 20, height = 28, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private String colorName;
    @Excel(name = "30", width = 6, height = 28, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private Integer size30Quantity;
    @Excel(name = "31", width = 6, height = 28, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private Integer size31Quantity;
    @Excel(name = "32", width = 6, height = 28, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private Integer size32Quantity;
    @Excel(name = "33", width = 6, height = 28, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private Integer size33Quantity;
    @Excel(name = "34", width = 6, height = 28, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private Integer size34Quantity;
    @Excel(name = "35", width = 6, height = 28, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private Integer size35Quantity;
    @Excel(name = "36", width = 6, height = 28, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private Integer size36Quantity;
    @Excel(name = "37", width = 6, height = 28, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private Integer size37Quantity;
    @Excel(name = "38", width = 6, height = 28, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private Integer size38Quantity;
    @Excel(name = "39", width = 6, height = 28, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private Integer size39Quantity;
    @Excel(name = "40", width = 6, height = 28, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private Integer size40Quantity;
    @Excel(name = "41", width = 6, height = 28, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private Integer size41Quantity;
    @Excel(name = "42", width = 6, height = 28, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private Integer size42Quantity;
    @Excel(name = "43", width = 6, height = 28, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private Integer size43Quantity;
    @Excel(name = "总库存", width = 6, height = 28, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private Integer totalQuantity;

}
