package com.ruoyi.xkt.dto.advertRound.pc.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("PC 档口馆 档口列表")
@Data
@Accessors(chain = true)
public class PCStoreRecommendDTO {

    @ApiModelProperty(value = "会员等级")
    private Integer memberLevel;
    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "档口名称")
    private String storeName;
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
    @ApiModelProperty(value = "档口出的推广价格")
    private BigDecimal payPrice;
    @ApiModelProperty(value = "排序")
    private Integer orderNum;
    @ApiModelProperty(value = "最新上新列表")
    private List<PCSRNewProdDTO> prodList;

    @Data
    public static class PCSRNewProdDTO {
        @ApiModelProperty(value = "档口商品ID")
        private Long storeProdId;
        @ApiModelProperty(value = "商品第一张主图路径")
        private String mainPicUrl;
    }

}
