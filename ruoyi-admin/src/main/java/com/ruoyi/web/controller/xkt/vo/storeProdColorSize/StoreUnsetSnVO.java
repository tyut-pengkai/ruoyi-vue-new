package com.ruoyi.web.controller.xkt.vo.storeProdColorSize;

import com.ruoyi.xkt.dto.storeProdColorSize.StoreUnsetSnDTO;
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
public class StoreUnsetSnVO {

    @ApiModelProperty(value = "未设置条码商品列表")
    List<SNSProdVO> unsetList;

    @Data
    @Accessors(chain = true)
    public static class SNSProdVO {
        @ApiModelProperty(value = "档口商品ID")
        private Long storeProdId;
        @ApiModelProperty(value = "商品货号")
        private String prodArtNum;
        @ApiModelProperty(value = "商品颜色列表")
        private List<SNSProdColorVO> colorList;
    }

    @Data
    @Accessors(chain = true)
    public static class SNSProdColorVO {
        @ApiModelProperty(value = "档口颜色ID")
        private Long storeColorId;
        @ApiModelProperty(value = "颜色名称")
        private String colorName;
    }


}
