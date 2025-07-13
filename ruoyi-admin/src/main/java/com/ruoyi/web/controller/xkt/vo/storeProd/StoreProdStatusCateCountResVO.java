package com.ruoyi.web.controller.xkt.vo.storeProd;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
@ApiModel
public class StoreProdStatusCateCountResVO {

    @ApiModelProperty(value = "商品状态")
    private Integer prodStatus;
    @ApiModelProperty(value = "状态下分类数量")
    List<SPSCCCateCountVO> cateCountList;

    @Data
    @ApiModel
    @Accessors(chain = true)
    public static class SPSCCCateCountVO {
        @ApiModelProperty(value = "商品分类ID")
        private Long prodCateId;
        @ApiModelProperty(value = "商品分类名称")
        private String prodCateName;
        @ApiModelProperty(value = "商品分类数量")
        private Integer cateCount;
    }

}
