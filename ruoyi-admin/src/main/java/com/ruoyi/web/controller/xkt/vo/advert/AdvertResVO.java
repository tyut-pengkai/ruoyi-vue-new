package com.ruoyi.web.controller.xkt.vo.advert;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("推广营销返回数据")
@Data
@Accessors(chain = true)
public class AdvertResVO {

    @ApiModelProperty(value = "推广平台ID")
    private Integer platformId;
    @ApiModelProperty(value = "推广类型")
    private Integer typeId;
    @ApiModelProperty(value = "推广tab")
    private Integer tabId;
    @ApiModelProperty(value = "展示类型 1推广图、2商品、3推广图及商品")
    private Integer displayType;
    @ApiModelProperty(value = "起拍价格")
    private BigDecimal startPrice;
    @ApiModelProperty(value = "播放间隔")
    private Integer playInterval;
    @ApiModelProperty(value = "播放轮次")
    private Integer playRound;
    @ApiModelProperty(value = "播放数量")
    private Integer playNum;
    @ApiModelProperty(value = "推广状态")
    private Integer onlineStatus;
    @ApiModelProperty(value = "推广范例图片")
    private AdvertFileVO example;
    @ApiModelProperty(value = "推广图片尺寸")
    private String picPixel;
    @ApiModelProperty(value = "推广图片大小")
    private String picSize;
    @ApiModelProperty(value = "推广折扣类型（1现金、2直接打折）")
    private Integer discountType;
    @ApiModelProperty(value = "折扣数额（现金：直接输入折扣金额；直接打折：输入折后数值）")
    private BigDecimal discountAmount;
    @ApiModelProperty(value = "折扣生效时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date discountStartTime;
    @ApiModelProperty(value = "折扣失效时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date discountEndTime;

    @Data
    @ApiModel(value = "广告范例图")
    public static class AdvertFileVO {
        @ApiModelProperty(value = "文件名称", required = true)
        private String fileName;
        @ApiModelProperty(value = "文件路径", required = true)
        private String fileUrl;
        @ApiModelProperty(value = "文件大小", required = true)
        private BigDecimal fileSize;
        @ApiModelProperty(value = "文件类型", required = true)
        private Integer typeId;
    }

}
