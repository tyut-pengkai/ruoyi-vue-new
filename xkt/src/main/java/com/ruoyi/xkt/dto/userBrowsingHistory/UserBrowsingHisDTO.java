package com.ruoyi.xkt.dto.userBrowsingHistory;

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
@ApiModel("用户浏览历史")
@Data
@Accessors(chain = true)
public class UserBrowsingHisDTO {

    @ApiModelProperty(value = "浏览历史ID")
    private Long id;
    @ApiModelProperty(value = "用户ID")
    private Long userId;
    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "档口名称")
    private String storeName;
    @ApiModelProperty(value = "档口商品ID")
    private Long storeProdId;
    @ApiModelProperty(value = "档口商品标题")
    private String prodTitle;
    @ApiModelProperty(value = "档口商品主图url")
    private String mainPicUrl;
    @ApiModelProperty(value = "商品货号")
    private String prodArtNum;
    @ApiModelProperty(value = "商品价格")
    private BigDecimal prodPrice;
    @ApiModelProperty(value = "浏览日期")
    private Date browsingTime;

}
