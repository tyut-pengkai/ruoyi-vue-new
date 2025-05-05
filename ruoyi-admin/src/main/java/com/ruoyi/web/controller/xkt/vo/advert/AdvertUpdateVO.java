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
@ApiModel("编辑推广营销")
@Data
@Accessors(chain = true)
public class AdvertUpdateVO {

    @ApiModelProperty(value = "推广ID")
    @NotNull(message = "推广ID不能为空!")
    private Long advertId;
    @NotNull(message = "推广平台ID不能为空!")
    @ApiModelProperty(value = "推广平台ID")
    private Integer platformId;
    @NotNull(message = "推广类型不能为空!")
    @ApiModelProperty(value = "推广类型")
    private Integer typeId;
    @NotNull(message = "推广tab不能为空!")
    @ApiModelProperty(value = "推广tab")
    private Integer tabId;
    @NotNull(message = "展示类型不能为空!")
    @ApiModelProperty(value = "展示类型 1推广图、2商品、3推广图及商品")
    private Integer displayType;
    @NotNull(message = "起拍价格不能为空!")
    @ApiModelProperty(value = "起拍价格")
    private BigDecimal startPrice;
    @NotNull(message = "播放间隔不能为空!")
    @ApiModelProperty(value = "播放间隔")
    private Integer playInterval;
    @NotNull(message = "播放轮次不能为空!")
    @ApiModelProperty(value = "播放轮次")
    private Integer playRound;
    @NotNull(message = "播放数量不能为空!")
    @ApiModelProperty(value = "播放数量")
    private Integer playNum;
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
        @NotBlank(message = "文件名称不能为空!")
        @ApiModelProperty(value = "文件名称", required = true)
        private String fileName;
        @NotBlank(message = "文件路径不能为空!")
        @ApiModelProperty(value = "文件路径", required = true)
        private String fileUrl;
        @NotNull(message = "文件大小不能为空!")
        @ApiModelProperty(value = "文件大小", required = true)
        private BigDecimal fileSize;
        @NotNull(message = "文件类型不能为空!")
        @ApiModelProperty(value = "文件类型", required = true)
        private Integer typeId;
    }

}
