package com.ruoyi.web.controller.xkt.vo.storeProdStorage;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口商品入库详情")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Accessors(chain = true)
public class StoreProdStorageResVO {

    @ApiModelProperty(name = "storeProdStorId")
    @JsonProperty("storeProdStorId")
    private Long id;
    @ApiModelProperty(name = "档口ID")
    private Long storeId;
    @ApiModelProperty(name = "单据编号")
    private String code;
    @ApiModelProperty(name = "入库类型")
    private String storageType;
    @ApiModelProperty(name = "数量")
    private Integer quantity;
    @ApiModelProperty(name = "生产成本金额")
    private BigDecimal produceAmount;
    @ApiModelProperty(name = "操作人名称")
    private String operatorName;
    @ApiModelProperty(name = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    @ApiModelProperty(name = "商品入库明细列表")
    private List<StorageDetailVO> detailList;

    @Data
    public static class StorageDetailVO {

        @ApiModelProperty(name = "商品货号")
        private String prodArtNum;
        @ApiModelProperty(name = "颜色名称")
        private String colorName;
        @ApiModelProperty(name = "尺码30")
        private Integer size30;
        @ApiModelProperty(name = "尺码31")
        private Integer size31;
        @ApiModelProperty(name = "尺码32")
        private Integer size32;
        @ApiModelProperty(name = "尺码33")
        private Integer size33;
        @ApiModelProperty(name = "尺码34")
        private Integer size34;
        @ApiModelProperty(name = "尺码35")
        private Integer size35;
        @ApiModelProperty(name = "尺码36")
        private Integer size36;
        @ApiModelProperty(name = "尺码37")
        private Integer size37;
        @ApiModelProperty(name = "尺码38")
        private Integer size38;
        @ApiModelProperty(name = "尺码39")
        private Integer size39;
        @ApiModelProperty(name = "尺码40")
        private Integer size40;
        @ApiModelProperty(name = "尺码41")
        private Integer size41;
        @ApiModelProperty(name = "尺码42")
        private Integer size42;
        @ApiModelProperty(name = "尺码43")
        private Integer size43;
        @ApiModelProperty(name = "总数量")
        private Integer quantity;

    }

}
