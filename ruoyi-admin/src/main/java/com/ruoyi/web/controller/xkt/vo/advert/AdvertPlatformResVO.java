package com.ruoyi.web.controller.xkt.vo.advert;

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
@ApiModel("推广营销返回分组数据")
@Data
@Accessors(chain = true)
public class AdvertPlatformResVO {

    @ApiModelProperty(value = "平台ID")
    private Integer platformId;
    @ApiModelProperty(value = "平台名称")
    private String platformName;
    @ApiModelProperty(value = "tabList")
    private List<APTabVO> tabList;

    @Data
    @Accessors(chain = true)
    public static class APTabVO {
        @ApiModelProperty(value = "tabId")
        private Integer tabId;
        @ApiModelProperty(value = "tabName")
        private String tabName;
        @ApiModelProperty(value = "类型列表")
        List<APTypeVO> typeList;
    }

    @Data
    @Accessors(chain = true)
    public static class APTypeVO {
        @ApiModelProperty("广告ID")
        private Long advertId;
        @ApiModelProperty(value = "typeId")
        private Integer typeId;
        @ApiModelProperty(value = "typeName")
        private String typeName;
        @ApiModelProperty(value = "示例url")
        private String demoUrl;
    }

}
