package com.ruoyi.xkt.dto.storeSale;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口销售出库列表数据")
@Data
public class StoreSalePageResDTO {

    @ApiModelProperty(name = "档口销售ID")
    private Long storeSaleId;
    @ApiModelProperty(name = "档口ID")
    private Long storeId;
    @ApiModelProperty("code")
    private String code;
    @ApiModelProperty(name = "客户名称")
    private String cusName;
    @ApiModelProperty(name = "销售时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    @ApiModelProperty(name = "销售类型（普通销售、销售退货、普通销售/销售退货）GENERAL_SALE SALE_REFUND SALE_AND_REFUND")
    private String saleType;
    @ApiModelProperty(name = "销售数量")
    private Integer quantity;
    @ApiModelProperty(name = "总金额")
    private BigDecimal amount;
    @ApiModelProperty(name = "结款状态")
    private String paymentStatus;
    @ApiModelProperty(name = "支付方式")
    private String payWay;
    @ApiModelProperty(name = "操作人")
    private String operatorName;
    @ApiModelProperty(name = "备注")
    private String remark;

}
