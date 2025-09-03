package com.ruoyi.xkt.dto.storeProdStorage;

import com.ruoyi.common.annotation.Excel;
import io.swagger.annotations.ApiModel;
import lombok.Data;

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

    @Excel(name = "序号", cellType = Excel.ColumnType.NUMERIC, needMerge = true)
    private Integer orderNum;
    @Excel(name = "单据编号", needMerge = true)
    private String code;
    @Excel(name = "出库日期", width = 30, dateFormat = "yyyy-MM-dd", needMerge = true)
    private Date createTime;
    @Excel(name = "总生产成本", needMerge = true)
    private BigDecimal produceAmount;
    @Excel(name = "结款状态", readConverterExp = "1=生产入库,2=其它入库,3=维修入库", needMerge = true)
    private Integer storageType;
    @Excel(name = "货号", needMerge = true)
    private String prodArtNum;
    @Excel(name = "颜色", needMerge = true)
    private String colorName;
    @Excel(name = "30")
    private Integer size30Quantity;
    @Excel(name = "31")
    private Integer size31Quantity;
    @Excel(name = "32")
    private Integer size32Quantity;
    @Excel(name = "33")
    private Integer size33Quantity;
    @Excel(name = "34")
    private Integer size34Quantity;
    @Excel(name = "35")
    private Integer size35Quantity;
    @Excel(name = "36")
    private Integer size36Quantity;
    @Excel(name = "37")
    private Integer size37Quantity;
    @Excel(name = "38")
    private Integer size38Quantity;
    @Excel(name = "39")
    private Integer size39Quantity;
    @Excel(name = "40")
    private Integer size40Quantity;
    @Excel(name = "41")
    private Integer size41Quantity;
    @Excel(name = "42")
    private Integer size42Quantity;
    @Excel(name = "43")
    private Integer size43Quantity;
    @Excel(name = "入库总量")
    private Integer totalQuantity;
    @Excel(name = "操作人名称")
    private String operatorName;

}
