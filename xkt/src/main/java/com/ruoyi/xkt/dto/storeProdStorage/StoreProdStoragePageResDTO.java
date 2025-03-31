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

    @ApiModelProperty(name = "storeProdStorId")
    private Long storeProdStorId;
    @ApiModelProperty(name = "档口ID")
    private Long storeId;
    @ApiModelProperty(name = "单据编号")
    private String code;
    @ApiModelProperty(name = "入库类型")
    private String storageType;
    @ApiModelProperty(name = "数量")
    private Integer quantity;
    @ApiModelProperty(name = "生产成本金额")
    private BigDecimal produceAmount;
    @ApiModelProperty(name = "操作人名称")
    private String operatorName;
    @ApiModelProperty(name = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

}
