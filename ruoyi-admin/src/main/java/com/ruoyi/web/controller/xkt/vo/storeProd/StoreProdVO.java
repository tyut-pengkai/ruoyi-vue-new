package com.ruoyi.web.controller.xkt.vo.storeProd;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ruoyi.web.controller.xkt.vo.storePordColor.StoreProdColorVO;
import com.ruoyi.web.controller.xkt.vo.storeProdCateAttr.StoreProdCateAttrVO;
import com.ruoyi.web.controller.xkt.vo.storeProdColorPrice.StoreProdColorPriceVO;
import com.ruoyi.web.controller.xkt.vo.storeProdColorSize.StoreProdColorSizeVO;
import com.ruoyi.web.controller.xkt.vo.storeProdDetail.StoreProdDetailVO;
import com.ruoyi.web.controller.xkt.vo.storeProdSvc.StoreProdSvcVO;
import com.ruoyi.web.controller.xkt.vo.storeProductFile.StoreProdFileVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
@ApiModel("档口商品")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoreProdVO {

    @ApiModelProperty("档口商品名称")
    private String prodName;
    @ApiModelProperty(name = "档口ID")
    @NotNull(message = "档口ID不能为空!")
    private Long storeId;
    @ApiModelProperty(name = "商品分类ID")
    @NotNull(message = "商品分类ID不能为空!")
    private Long prodCateId;
    @ApiModelProperty(name = "工厂货号")
    @Size(max = 15, message = "工厂货号不能超过60个字!")
    private String factoryArtNum;
    @ApiModelProperty(name = "商品货号")
    @Size(max = 15, message = "商品货号不能超过60个字!")
    @NotBlank(message = "商品货号不能为空!")
    private String prodArtNum;
    @ApiModelProperty(name = "商品标题")
    @Size(max = 60, message = "商品标题不能超过60个字!")
    @NotBlank(message = "商品标题不能为空!")
    private String prodTitle;
    @ApiModelProperty(name = "商品重量")
    private BigDecimal prodWeight;
    @ApiModelProperty(name = "生产价格")
    private Integer producePrice;
    @ApiModelProperty(name = "大小码加价")
    private Integer overPrice;
    @ApiModelProperty(name = "发货时效")
    private Integer deliveryTime;
    @ApiModelProperty(name = "上架方式")
    @NotBlank(message = "上架方式不能为空!")
    private String listingWay;
    @ApiModelProperty(name = "商品状态")
    @NotBlank(message = "商品状态不能为空!")
    private String prodStatus;
    @ApiModelProperty(name = "定时发货时间(精确到小时)")
    @JsonFormat(pattern = "yyyy-MM-dd HH")
    private Date listingWaySchedule;
    @ApiModelProperty(name = "档口文件列表")
    @NotNull(message = "档口文件不能为空!")
    @Valid
    private List<StoreProdFileVO> fileList;
    @NotNull(message = "档口类目属性列表不能为空!")
    @Valid
    @ApiModelProperty(name = "档口类目属性列表")
    private List<StoreProdCateAttrVO> cateAttrList;
    @NotNull(message = "档口颜色列表不能为空!")
    @Valid
    @ApiModelProperty(name = "档口颜色列表")
    private List<StoreProdColorVO> colorList;
    @NotNull(message = "档口尺码列表不能为空!")
    @Valid
    @ApiModelProperty(name = "档口尺码列表")
    private List<StoreProdColorSizeVO> sizeList;
    @NotNull(message = "档口颜色价格列表不能为空!")
    @Valid
    @ApiModelProperty(name = "档口颜色价格列表")
    private List<StoreProdColorPriceVO> priceList;
    @ApiModelProperty(name = "档口服务承诺")
    private StoreProdSvcVO svc;
    @Valid
    @ApiModelProperty(name = "详情内容")
    private StoreProdDetailVO detail;

}
