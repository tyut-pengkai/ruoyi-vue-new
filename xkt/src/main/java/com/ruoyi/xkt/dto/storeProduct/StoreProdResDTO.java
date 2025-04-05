package com.ruoyi.xkt.dto.storeProduct;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.xkt.dto.storeColor.StoreColorDTO;
import com.ruoyi.xkt.dto.storeProdCateAttr.StoreProdCateAttrDTO;
import com.ruoyi.xkt.dto.storeProdColor.StoreProdColorDTO;
import com.ruoyi.xkt.dto.storeProdColorPrice.StoreProdColorPriceDTO;
import com.ruoyi.xkt.dto.storeProdColorSize.StoreProdColorSizeDTO;
import com.ruoyi.xkt.dto.storeProdDetail.StoreProdDetailDTO;
import com.ruoyi.xkt.dto.storeProdProcess.StoreProdProcessDTO;
import com.ruoyi.xkt.dto.storeProdSvc.StoreProdSvcDTO;
import com.ruoyi.xkt.dto.storeProductFile.StoreProdFileResDTO;
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
@ApiModel("创建档口商品")
@Data
@Accessors(chain = true)
public class StoreProdResDTO {

    @ApiModelProperty("档口商品ID")
    private Long storeProdId;
    @ApiModelProperty("档口ID")
    private Long storeId;
    @ApiModelProperty("档口商品名称")
    private String prodName;
    @ApiModelProperty(value = "商品分类ID")
    private Long prodCateId;
    @ApiModelProperty(value = "工厂货号")
    private String factoryArtNum;
    @ApiModelProperty(value = "商品货号")
    private String prodArtNum;
    @ApiModelProperty(value = "商品标题")
    private String prodTitle;
    @ApiModelProperty(value = "商品重量")
    private BigDecimal prodWeight;
    @ApiModelProperty(value = "生产价格")
    private Integer producePrice;
    @ApiModelProperty(value = "大小码加价")
    private Integer overPrice;
    @ApiModelProperty(value = "发货时效")
    private Integer deliveryTime;
    @ApiModelProperty(value = "上架方式")
    private Integer listingWay;
    @ApiModelProperty(value = "定时发货时间(精确到小时)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date listingWaySchedule;
    @ApiModelProperty(value = "档口文件列表")
    private List<StoreProdFileResDTO> fileList;
    @ApiModelProperty(value = "档口类目属性列表")
    private List<StoreProdCateAttrDTO> cateAttrList;
    @ApiModelProperty(value = "档口宿友颜色列表")
    private List<StoreColorDTO> allColorList;
    @ApiModelProperty(value = "档口颜色列表")
    private List<StoreProdColorDTO> colorList;
    @ApiModelProperty(value = "档口商品尺码列表")
    private List<StoreProdColorSizeDTO> sizeList;
    @ApiModelProperty(value = "档口颜色价格列表")
    private List<StoreProdColorPriceDTO> priceList;
    @ApiModelProperty(value = "档口服务承诺")
    private StoreProdSvcDTO svc;
    @ApiModelProperty(value = "详情内容")
    private StoreProdDetailDTO detail;
    @ApiModelProperty(value = "档口生产工艺信息")
    private StoreProdProcessDTO process;

}
