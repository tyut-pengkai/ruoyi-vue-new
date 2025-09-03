package com.ruoyi.web.controller.xkt.vo.storeProdStorage;

import com.fasterxml.jackson.annotation.JsonFormat;
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
@Data
@ApiModel

@Accessors(chain = true)
public class StoreProdStorageResVO {

    @ApiModelProperty(value = "storeProdStorageId")
    @JsonProperty("storeProdStorageId")
    private Long id;
    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "单据编号")
    private String code;
    @ApiModelProperty(value = "入库类型")
    private Integer storageType;
    @ApiModelProperty(value = "数量")
    private Integer quantity;
    @ApiModelProperty(value = "生产成本金额")
    private BigDecimal produceAmount;
    @ApiModelProperty(value = "操作人名称")
    private String operatorName;
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    @ApiModelProperty(value = "商品入库明细列表")
    private List<StorageDetailVO> detailList;

    @Data
    @ApiModel
    public static class StorageDetailVO {

        @ApiModelProperty(value = "商品货号")
        private String prodArtNum;
        @ApiModelProperty(value = "颜色名称")
        private String colorName;
        @ApiModelProperty(value = "尺码30")
        private Integer size30;
        @ApiModelProperty(value = "尺码31")
        private Integer size31;
        @ApiModelProperty(value = "尺码32")
        private Integer size32;
        @ApiModelProperty(value = "尺码33")
        private Integer size33;
        @ApiModelProperty(value = "尺码34")
        private Integer size34;
        @ApiModelProperty(value = "尺码35")
        private Integer size35;
        @ApiModelProperty(value = "尺码36")
        private Integer size36;
        @ApiModelProperty(value = "尺码37")
        private Integer size37;
        @ApiModelProperty(value = "尺码38")
        private Integer size38;
        @ApiModelProperty(value = "尺码39")
        private Integer size39;
        @ApiModelProperty(value = "尺码40")
        private Integer size40;
        @ApiModelProperty(value = "尺码41")
        private Integer size41;
        @ApiModelProperty(value = "尺码42")
        private Integer size42;
        @ApiModelProperty(value = "尺码43")
        private Integer size43;
        @ApiModelProperty(value = "总数量")
        private Integer quantity;

    }

}
