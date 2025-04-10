package com.ruoyi.xkt.dto.storeCusProdDiscount;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口客户销售管理分页数据")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoreCusProdDiscPageResDTO {

    @ApiModelProperty(value = "档口客户商品优惠ID")
    private Long storeCusProdDiscId;
    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(name = "档口商品ID")
    private Long storeProdId;
    @ApiModelProperty(name = "档口客户ID")
    private Long storeCusId;
    @ApiModelProperty(name = "档口客户名称")
    private String storeCusName;
    @ApiModelProperty(name = "档口商品颜色ID")
    private Long storeProdColorId;
    @ApiModelProperty(name = "优惠金额")
    private Integer discount;
    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

}
