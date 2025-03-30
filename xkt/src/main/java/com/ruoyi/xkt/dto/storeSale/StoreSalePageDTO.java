package com.ruoyi.xkt.dto.storeSale;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.xkt.dto.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel("档口销售出库单分页查询入参")
@Data
public class StoreSalePageDTO extends BasePageDTO {

    @NotNull(message = "档口ID不能为空")
    @ApiModelProperty(name = "档口ID")
    private Long storeId;
    @ApiModelProperty(name = "商品货号")
    private String prodArtNum;
    @ApiModelProperty(name = "客户名称")
    private String cusName;
    @ApiModelProperty(name = "销售类型", notes = "普通销售、销售退货、普通销售/销售退货 GENERAL_SALE SALE_REFUND SALE_AND_REFUND")
    private String saleType;
    @ApiModelProperty(name = "结款状态")
    private String paymentStatus;
    @ApiModelProperty(name = "支付方式")
    private String payWay;
    @ApiModelProperty(name = "销售开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTimeStart;
    @ApiModelProperty(name = "销售结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTimeEnd;

}
