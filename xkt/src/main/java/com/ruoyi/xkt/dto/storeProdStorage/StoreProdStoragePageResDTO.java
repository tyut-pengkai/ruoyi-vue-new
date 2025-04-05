package com.ruoyi.xkt.dto.storeProdStorage;

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
@ApiModel("档口商品入库分页返回数据")
@Data
public class StoreProdStoragePageResDTO {

    @ApiModelProperty(value = "storeProdStorageId")
    private Long storeProdStorageId;
    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "单据编号")
    private String code;
    @ApiModelProperty(value = "入库类型")
    private Integer storageType;
    @ApiModelProperty(value = "数量")
    private Integer quantity;
    @ApiModelProperty(value = "生产成本金额")
    private BigDecimal produceAmount;
    @ApiModelProperty(value = "操作人名称")
    private String operatorName;
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

}
