package com.ruoyi.web.controller.xkt.vo.storeProd;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.web.controller.xkt.vo.storeColor.StoreColorVO;
import com.ruoyi.web.controller.xkt.vo.storePordColor.StoreProdColorVO;
import com.ruoyi.web.controller.xkt.vo.storeProdCateAttr.StoreProdCateAttrVO;
import com.ruoyi.web.controller.xkt.vo.storeProdColorPrice.StoreProdColorPriceVO;
import com.ruoyi.web.controller.xkt.vo.storeProdDetail.StoreProdDetailVO;
import com.ruoyi.web.controller.xkt.vo.storeProdSvc.StoreProdSvcVO;
import com.ruoyi.web.controller.xkt.vo.storeProductFile.StoreProdFileResVO;
import com.ruoyi.web.controller.xkt.vo.storeProductFile.StoreProdFileVO;
import com.ruoyi.xkt.dto.storeColor.StoreColorDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
public class StoreProdResVO {

    @ApiModelProperty("档口商品名称")
    private Long storeProdId;
    @ApiModelProperty("档口ID")
    private Long storeId;
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
    private List<StoreProdFileResVO> fileList;
    @ApiModelProperty(name = "档口类目属性列表")
    private List<StoreProdCateAttrVO> cateAttrList;
    @ApiModelProperty(name = "档口宿友颜色列表")
    private List<StoreColorVO> allColorList;
    @ApiModelProperty(name = "档口颜色列表")
    private List<StoreProdColorVO> colorList;
    @ApiModelProperty(name = "档口颜色价格列表")
    private List<StoreProdColorPriceVO> priceList;
    @ApiModelProperty(name = "档口服务承诺")
    private StoreProdSvcVO svc;
    @ApiModelProperty(name = "详情内容")
    private StoreProdDetailVO detail;

}
