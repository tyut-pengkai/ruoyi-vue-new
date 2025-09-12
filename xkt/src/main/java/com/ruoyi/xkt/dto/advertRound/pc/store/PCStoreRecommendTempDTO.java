package com.ruoyi.xkt.dto.advertRound.pc.store;

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
@ApiModel("PC 档口馆 档口列表")
@Data
@Accessors(chain = true)
public class PCStoreRecommendTempDTO {

    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "档口名称")
    private String storeName;
    @ApiModelProperty(value = "会员等级")
    private Integer memberLevel;
    @ApiModelProperty(value = "档口标签")
    private List<String> tags;
    @ApiModelProperty(value = "是否广告")
    private Boolean advert;
    @ApiModelProperty(value = "联系电话")
    private String contactPhone;
    @ApiModelProperty(value = "微信账号")
    private String wechatAccount;
    @ApiModelProperty(value = "QQ账号")
    private String qqAccount;
    @ApiModelProperty(value = "档口地址")
    private String storeAddress;
    @ApiModelProperty(value = "推荐数值")
    private Long recommendWeight;
    @ApiModelProperty(value = "档口权重")
    private Integer storeWeight;
    @ApiModelProperty(value = "最新上新列表")
    List<PCSRNewProdDTO> prodList;

    @Data
    public static class PCSRNewProdDTO {
        @ApiModelProperty(value = "商品编号")
        private String prodArtNum;
        @ApiModelProperty(value = "档口商品ID")
        private Long storeProdId;
        @ApiModelProperty(value = "商品第一张主图路径")
        private String mainPicUrl;
    }

}
