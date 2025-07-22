package com.ruoyi.web.controller.xkt.vo.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口首页数据概览")
@Data
public class StoreIndexOverviewResVO {

    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "销售出库金额")
    private BigDecimal saleAmount;
    @ApiModelProperty(value = "销售退货金额")
    private BigDecimal refundAmount;
    @ApiModelProperty(value = "累计销量")
    private Integer saleNum;
    @ApiModelProperty(value = "累计退货量")
    private Integer refundNum;
    @ApiModelProperty(value = "累计入库数量")
    private Integer storageNum;
    @ApiModelProperty(value = "累计客户数")
    private Integer customerNum;
    @ApiModelProperty(value = "销售大客户")
    private String topSaleCusName;
    @ApiModelProperty(value = "销售大客户金额")
    private BigDecimal topSaleCusAmount;

}
