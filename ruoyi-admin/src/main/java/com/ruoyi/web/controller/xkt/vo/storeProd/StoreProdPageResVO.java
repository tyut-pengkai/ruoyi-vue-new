package com.ruoyi.web.controller.xkt.vo.storeProd;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口商品列表数据")
@Data
public class StoreProdPageResVO {

    @ApiModelProperty(value = "档口商品主图url")
    private String mainPicUrl;
    @ApiModelProperty(value = "商品货号")
    private String prodArtNum;
    @ApiModelProperty(value = "颜色")
    private String colorName;
    @ApiModelProperty(value = "分类类目")
    private String prodCateName;
    @ApiModelProperty(value = "标准尺码")
    private List<Integer> standardSizeList;
    @ApiModelProperty(value = "销售金额（元）")
    private BigDecimal price;
    @ApiModelProperty(value = "状态")
    private Integer prodStatus;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

}
