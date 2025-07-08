package com.ruoyi.xkt.dto.storeProduct;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ruoyi.xkt.dto.storeColor.StoreColorDTO;
import com.ruoyi.xkt.dto.storeProdCateAttr.StoreProdCateAttrDTO;
import com.ruoyi.xkt.dto.storeProdProcess.StoreProdProcessDTO;
import com.ruoyi.xkt.dto.storeProdSvc.StoreProdSvcDTO;
import com.ruoyi.xkt.dto.storeProductFile.StoreProdFileDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoreProdUpdateDTO {

    @ApiModelProperty(value = "档口商品名称")
    private String prodName;
    @ApiModelProperty(value = "档口ID", required = true)
    @NotNull(message = "档口ID不能为空!")
    private Long storeId;
    @ApiModelProperty(value = "商品分类ID", required = true)
    @NotNull(message = "商品分类ID不能为空!")
    private Long prodCateId;
    @ApiModelProperty(value = "商品分类名称", required = true)
    @NotBlank(message = "商品分类名称不能为空!")
    private String prodCateName;
    @ApiModelProperty(value = "工厂货号")
    @Size(min = 0, max = 15, message = "工厂货号不能超过60个字!")
    private String factoryArtNum;
    @ApiModelProperty(value = "商品货号", required = true)
    @Size(min = 0, max = 15, message = "商品货号不能超过60个字!")
    @NotBlank(message = "商品货号不能为空!")
    private String prodArtNum;
    @ApiModelProperty(value = "商品标题", required = true)
    @Size(min = 0, max = 60, message = "商品标题不能超过60个字!")
    @NotBlank(message = "商品标题不能为空!")
    private String prodTitle;
    @ApiModelProperty(value = "商品重量")
    private BigDecimal prodWeight;
    @ApiModelProperty(value = "生产价格")
    private Integer producePrice;
    @ApiModelProperty(value = "大小码加价")
    private Integer overPrice;
    @ApiModelProperty(value = "发货时效")
    private Integer deliveryTime;
    @ApiModelProperty(value = "上架方式:1 立即上架 2 定时上架", required = true)
    @NotNull(message = "上架方式不能为空!")
    private Integer listingWay;
    @ApiModelProperty(value = "商品状态：1.未发布 2. 在售 3. 尾货 4.已下架 5. 已删除", required = true)
    @NotNull(message = "商品状态不能为空!")
    private Integer prodStatus;
    @ApiModelProperty(value = "定时发货时间(精确到小时)")
    @JsonFormat(pattern = "yyyy-MM-dd HH")
    private Date listingWaySchedule;
    @ApiModelProperty(value = "档口文件列表", required = true)
    @NotNull(message = "档口文件不能为空!")
    @Valid
    private List<StoreProdFileDTO> fileList;
    @NotNull(message = "档口类目属性不能为空!")
    @Valid
    @ApiModelProperty(value = "档口类目属性", required = true)
    private StoreProdCateAttrDTO cateAttr;
    @NotNull(message = "档口所有颜色列表不能为空!")
    @ApiModelProperty(value = "档口所有颜色列表", required = true)
    @Valid
    private List<StoreColorDTO> allColorList;
    @NotNull(message = "档口尺码列表不能为空!")
    @Valid
    @ApiModelProperty(value = "档口尺码列表", required = true)
    private List<SPCSizeDTO> sizeList;
    @Valid
    @NotNull(message = "商品颜色价格列表不能为空!")
    @ApiModelProperty(value = "档口颜色价格列表", required = true)
    private List<SPCColorPriceDTO> colorPriceList;
    @ApiModelProperty(value = "档口服务承诺")
    private StoreProdSvcDTO svc;
    @NotBlank(message = "详情内容不能为空!")
    @ApiModelProperty(value = "详情内容", required = true)
//    @Xss
    private String detail;
    @ApiModelProperty(value = "档口生产工艺")
    private StoreProdProcessDTO process;


    @Data
    @Valid
    public static class SPCColorPriceDTO {
        @ApiModelProperty(value = "档口商品颜色ID")
        private Long storeColorId;
        @NotBlank(message = "颜色名称不能为空!")
        @ApiModelProperty(value = "颜色名称", required = true)
        private String colorName;
        @NotNull(message = "档口商品定价不能为空!")
        @ApiModelProperty(value = "档口商品定价", required = true)
        private BigDecimal price;
        @NotNull(message = "排序不能为空!")
        @ApiModelProperty(value = "排序", required = true)
        private Integer orderNum;
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Valid
    public static class SPCSizeDTO {
        @ApiModelProperty(value = "商品尺码", required = true)
        @NotNull(message = "档口商品定价不能为空!")
        private Integer size;
        @NotNull(message = "是否是标准尺码不能为空!")
        @ApiModelProperty(value = "是否是标准尺码", required = true)
        private Integer standard;
    }


}
