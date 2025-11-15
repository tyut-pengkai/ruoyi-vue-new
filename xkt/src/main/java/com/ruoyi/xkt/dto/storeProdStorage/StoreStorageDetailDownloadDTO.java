package com.ruoyi.xkt.dto.storeProdStorage;

import com.ruoyi.common.annotation.Excel;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.apache.poi.ss.usermodel.IndexedColors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口入库导出数据")
@Data

public class StoreStorageDetailDownloadDTO {

    @Excel(name = "序号", cellType = Excel.ColumnType.NUMERIC, height = 28, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private Integer orderNum;
    @Excel(name = "单据编号", width = 26, height = 28, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private String code;
    @Excel(name = "工厂名称", width = 20,  height = 28, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private String facName;
    @Excel(name = "入库类型", readConverterExp = "1=生产入库,2=其它入库,3=维修入库", height = 28, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private Integer storageType;
    @Excel(name = "入库日期", width = 20, height = 28, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private String createTime;
    @Excel(name = "总生产成本", height = 28, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private BigDecimal produceAmount;
    @Excel(name = "货号", width = 20, height = 28, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private String prodArtNum;
    @Excel(name = "颜色", width = 20, height = 28, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private String colorName;
    @Excel(name = "30", height = 28, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private Integer size30Quantity;
    @Excel(name = "31", height = 28, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private Integer size31Quantity;
    @Excel(name = "32", height = 28, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private Integer size32Quantity;
    @Excel(name = "33", height = 28, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private Integer size33Quantity;
    @Excel(name = "34", height = 28, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private Integer size34Quantity;
    @Excel(name = "35", height = 28, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private Integer size35Quantity;
    @Excel(name = "36", height = 28, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private Integer size36Quantity;
    @Excel(name = "37", height = 28, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private Integer size37Quantity;
    @Excel(name = "38", height = 28, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private Integer size38Quantity;
    @Excel(name = "39", height = 28, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private Integer size39Quantity;
    @Excel(name = "40", height = 28, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private Integer size40Quantity;
    @Excel(name = "41", height = 28, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private Integer size41Quantity;
    @Excel(name = "42", height = 28, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private Integer size42Quantity;
    @Excel(name = "43", height = 28, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private Integer size43Quantity;
    @Excel(name = "入库总量", height = 28, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private Integer totalQuantity;
    @Excel(name = "操作人名称", height = 28, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private String operatorName;

}
