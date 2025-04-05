package com.ruoyi.web.controller.xkt.vo.storeSaleRefundRecord;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口商品销售返单数据")
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoreSaleRefundRecordVO {

    @ApiModelProperty("返单ID")
    private Long storeSaleRefundRecordId;
    @ApiModelProperty(name = "单据编号")
    private String code;
    @ApiModelProperty(name = "档口销售客户名称")
    private String storeCusName;
    @JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
    private Date createTime;
    @ApiModelProperty(name = "支付方式（支付宝、微信、现金、欠款）ALIPAY WECHAT_PAY CASH DEBT")
    private Integer payWay;
    @ApiModelProperty(name = "数量")
    private Integer quantity;
    @ApiModelProperty(name = "总金额")
    private BigDecimal amount;

}
