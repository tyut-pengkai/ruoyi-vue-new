package com.ruoyi.xkt.dto.storeProduct;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.xkt.dto.storeProdColor.StoreProdColorDTO;
import com.ruoyi.xkt.dto.storeProdCateAttr.StoreProdCateAttrDTO;
import com.ruoyi.xkt.dto.storeProdColorPrice.StoreProdColorPriceDTO;
import com.ruoyi.xkt.dto.storeProdDetail.StoreProdDetailDTO;
import com.ruoyi.xkt.dto.storeProdSvc.StoreProdSvcDTO;
import com.ruoyi.xkt.dto.storeProductFile.StoreProdFileDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
public class StoreProdDTO {

    @ApiModelProperty("档口商品名称")
    private Long storeProdId;
    @ApiModelProperty("档口商品名称")
    private String prodName;
    @ApiModelProperty(name = "商品分类ID")
    private Long prodCateId;
    @ApiModelProperty(name = "工厂货号")
    private String factoryArtNum;
    @ApiModelProperty(name = "商品货号")
    private String prodArtNum;
    @ApiModelProperty(name = "商品标题")
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
    private String listingWay;
    @ApiModelProperty(name = "定时发货时间(精确到小时)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date listingWaySchedule;
    @ApiModelProperty(name = "档口文件列表")
    private List<StoreProdFileDTO> fileList;
    @ApiModelProperty(name = "档口类目属性列表")
    private List<StoreProdCateAttrDTO> cateAttrList;
    @ApiModelProperty(name = "档口颜色列表")
    private List<StoreProdColorDTO> colorList;
    @ApiModelProperty(name = "档口颜色列表")
    private List<StoreProdColorPriceDTO> priceList;
    @ApiModelProperty(name = "档口服务承诺")
    private StoreProdSvcDTO svc;
    @ApiModelProperty(name = "详情内容")
    private StoreProdDetailDTO detail;

}
