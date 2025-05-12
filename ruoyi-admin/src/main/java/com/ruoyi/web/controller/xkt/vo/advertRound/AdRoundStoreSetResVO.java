package com.ruoyi.web.controller.xkt.vo.advertRound;

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
@ApiModel("当前位置枚举类型设置的推广图")
@Data
@Accessors(chain = true)
public class AdRoundStoreSetResVO {

    @ApiModelProperty(value = "推广轮次ID")
    private Long advertRoundId;
    @ApiModelProperty(value = "档口的推广图")
    private String fileUrl;
    @ApiModelProperty(value = "档口设置的商品")
    private List<ARSSProdVO> prodList;

    @Data
    @ApiModel(value = "档口设置的商品")
    @Accessors(chain = true)
    public static class ARSSProdVO {
        @ApiModelProperty("档口商品ID")
        private Long storeProdId;
        @ApiModelProperty(value = "商品货号")
        private String prodArtNum;
    }

}
