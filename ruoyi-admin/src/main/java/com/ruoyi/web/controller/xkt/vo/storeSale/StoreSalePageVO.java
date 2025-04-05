package com.ruoyi.web.controller.xkt.vo.storeSale;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.web.controller.xkt.vo.BasePageVO;
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
public class StoreSalePageVO extends BasePageVO {

    @NotNull(message = "档口ID不能为空")
    @ApiModelProperty(value = "档口ID", required = true)
    private Long storeId;
    @ApiModelProperty(value = "商品货号")
    private String prodArtNum;
    @ApiModelProperty(value = "客户名称")
    private String cusName;
    @ApiModelProperty(value = "销售类型", notes = "销售、退货、销售/退货 1 2 3")
    private Integer saleType;
    @ApiModelProperty(value = "结款状态")
    private Integer paymentStatus;
    @ApiModelProperty(value = "支付方式")
    private Integer payWay;
    @ApiModelProperty(value = "销售开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTimeStart;
    @ApiModelProperty(value = "销售结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTimeEnd;

}
