package com.ruoyi.web.controller.xkt.vo.storeHomepage;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口首页返回数据")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoreHomeResVO {

    @ApiModelProperty(value = "档口基本信息")
    private StoreBasicVO storeBasic;
    @ApiModelProperty(value = "档口首页商品基本信息")
    private StoreProdBasicVO storeProdBasic;

    @Data
    @ApiModel(value = "档口基本信息")
    public static class StoreBasicVO {
        @ApiModelProperty(value = "档口模板ID")
        private Integer templateNum;
        @ApiModelProperty(value = "档口ID")
        private Long storeId;
        @ApiModelProperty(value = "档口名称")
        private String storeName;
        @ApiModelProperty(value = "经营年限")
        private Integer operateYears;
        @ApiModelProperty(value = "联系电话")
        private String contactPhone;
        @ApiModelProperty(value = "备选联系电话")
        private String contactBackPhone;
        @ApiModelProperty(value = "微信账号")
        private String wechatAccount;
        @ApiModelProperty(value = "QQ账号")
        private String qqAccount;
        @ApiModelProperty(value = "档口地址")
        private String storeAddress;
    }

    @Data
    @ApiModel(value = "档口商品基本信息")
    public static class StoreProdBasicVO {
        @ApiModelProperty(value = "在售数量")
        private Integer onSaleNum;
        @ApiModelProperty(value = "尾货数量")
        private Integer tailGoodsNum;
        @ApiModelProperty(value = "已下架数量")
        private Integer offSaleNum;
    }

}
