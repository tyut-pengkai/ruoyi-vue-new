package com.ruoyi.xkt.dto.storeSale;

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
@ApiModel("档口销售导出数据")
@Data
public class StoreSaleDownloadDTO {

    @Excel(name = "序号", cellType = Excel.ColumnType.NUMERIC, height = 28, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private Integer orderNum;
    @Excel(name = "单据编号", width = 28, height = 28, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private String code;
    @Excel(name = "客户", width = 20, height = 28, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private String storeCusName;
    @Excel(name = "出库日期", width = 26, height = 28, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private String createTime;
    @Excel(name = "销售总额", height = 28, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private BigDecimal amount;
    @Excel(name = "销售数量", height = 28, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private String quantity;
    @Excel(name = "结款状态", readConverterExp = "1=已结清,2=欠款", height = 28, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private Integer paymentStatus;
    @Excel(name = "支付方式", readConverterExp = "1=支付宝,2=微信支付,3=现金,4=欠款", height = 28, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private Integer payWay;
    @Excel(name = "销售类型", readConverterExp = "1=销售,2=退货,3=销售/退货", height = 28, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private Integer saleType;
    @Excel(name = "操作人名称", height = 28, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private String operatorName;

}
