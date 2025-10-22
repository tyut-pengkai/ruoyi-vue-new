package com.ruoyi.xkt.dto.storeSale;

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

public class StoreSaleDownloadDTO {

    @Excel(name = "序号", cellType = Excel.ColumnType.NUMERIC, height = 28)
    private Integer orderNum;
    @Excel(name = "单据编号", height = 28)
    private String code;
    @Excel(name = "客户", height = 28)
    private String storeCusName;
    @Excel(name = "出库日期", width = 30, dateFormat = "yyyy-MM-dd HH:mm", height = 28)
    private Date createTime;
    @Excel(name = "销售总额", height = 28)
    private BigDecimal amount;
    @Excel(name = "销售数量", height = 28)
    private String quantity;
    @Excel(name = "抹零金额", height = 28)
    private BigDecimal roundOff;
    @Excel(name = "结款状态", readConverterExp = "1=已结清,2=欠款", height = 28)
    private Integer paymentStatus;
    @Excel(name = "支付方式", readConverterExp = "1=支付宝,2=微信支付,3=现金,4=欠款", height = 28)
    private Integer payWay;
    @Excel(name = "销售类型", readConverterExp = "1=销售,2=退货,3=销售/退货", height = 28)
    private Integer saleType;
    @Excel(name = "操作人名称", height = 28)
    private String operatorName;

}
