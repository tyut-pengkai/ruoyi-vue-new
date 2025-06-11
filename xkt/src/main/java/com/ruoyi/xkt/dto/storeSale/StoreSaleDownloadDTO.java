package com.ruoyi.xkt.dto.storeSale;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@ApiModel("档口销售导出数据")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoreSaleDownloadDTO {

    @Excel(name = "序号", cellType = Excel.ColumnType.NUMERIC)
    private Integer orderNum;
    @Excel(name = "单据编号")
    private String code;
    @Excel(name = "客户")
    private String storeCusName;
    @Excel(name = "出库日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date voucherDate;
    @Excel(name = "销售总额")
    private BigDecimal amount;
    @Excel(name = "销售数量")
    private String quantity;
    @Excel(name = "抹零金额")
    private BigDecimal roundOff;
    @Excel(name = "结款状态", readConverterExp = "1=已结清,2=欠款")
    private Integer paymentStatus;
    @Excel(name = "支付方式", readConverterExp = "1=支付宝,2=微信支付,3=现金,4=欠款")
    private Integer payWay;
    @Excel(name = "销售类型", readConverterExp = "1=销售,2=退货,3=销售/退货")
    private Integer saleType;
    @Excel(name = "操作人名称")
    private String operatorName;

}
