package com.ruoyi.web.controller.xkt.vo.express;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author liangyq
 * @date 2025-04-15 14:57
 */
@ApiModel
@Data
public class ExpressRegionTreeNodeVO {
    /**
     * ID
     */
    @ApiModelProperty(value = "ID")
    private Long id;
    /**
     * 地区编码，基于行政区划代码做扩展，唯一约束
     */
    @ApiModelProperty(value = "地区编码")
    private String regionCode;
    /**
     * 地区名称
     */
    @ApiModelProperty(value = "地区名称")
    private String regionName;
    /**
     * 地区级别[1:省 2:市 3:区县]
     */
    @ApiModelProperty(value = "地区级别[1:省 2:市 3:区县]")
    private Integer regionLevel;
    /**
     * 上级地区编码，没有上级的默认空
     */
    @ApiModelProperty(value = "上级地区编码，没有上级的默认空")
    private String parentRegionCode;
    /**
     * 上级地区名称，冗余
     */
    @ApiModelProperty(value = "上级地区名称")
    private String parentRegionName;
    /**
     * 下级地区列表
     */
    @ApiModelProperty(value = "下级地区列表")
    private List<ExpressRegionTreeNodeVO> children;

}
