package com.ruoyi.xkt.dto.storeSale;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
public class StoreSalePageResDTO {

    @ApiModelProperty(value = "档口销售ID")
    private Long storeSaleId;
    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty("code")
    private String code;
    @ApiModelProperty(value = "客户名称")
    private String cusName;
    @ApiModelProperty(value = "销售时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    @ApiModelProperty(value = "销售类型（1销售、2退货、3销售/退货）")
    private Integer saleType;
    @ApiModelProperty(value = "总数量")
    private Integer quantity;
    @ApiModelProperty(value = "销售数量")
    private Integer saleQuantity;
    @ApiModelProperty(value = "退货数量")
    private Integer refundQuantity;
    @ApiModelProperty(value = "总金额")
    private BigDecimal amount;
    @ApiModelProperty(value = "结款状态")
    private Integer paymentStatus;
    @ApiModelProperty(value = "支付方式")
    private Integer payWay;
    @ApiModelProperty(value = "操作人")
    private String operatorName;
    @ApiModelProperty(value = "备注")
    private String remark;

}
