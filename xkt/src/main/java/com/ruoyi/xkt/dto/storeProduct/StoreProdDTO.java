package com.ruoyi.xkt.dto.storeProduct;

import com.ruoyi.xkt.dto.storeColor.StoreColorDTO;
import com.ruoyi.xkt.dto.storeProdCateAttr.StoreProdCateAttrDTO;
import com.ruoyi.xkt.dto.storeProdProcess.StoreProdProcessDTO;
import com.ruoyi.xkt.dto.storeProdSvc.StoreProdSvcDTO;
import com.ruoyi.xkt.dto.storeProductFile.StoreProdFileDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.Valid;
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
public class StoreProdDTO {

    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "商品分类ID")
    private Long prodCateId;
    @ApiModelProperty(value = "商品分类名称")
    private String prodCateName;
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
    @ApiModelProperty(value = "上架方式:1 立即上架 2 定时上架")
    private Integer listingWay;
    @ApiModelProperty(value = "商品状态：1.未发布 2. 在售 3. 尾货 4.已下架 5. 已删除")
    private Integer prodStatus;
    @ApiModelProperty(value = "定时发货时间(精确到小时)")
    private Date listingWaySchedule;
    @ApiModelProperty(value = "档口文件列表")
    private List<StoreProdFileDTO> fileList;
    @ApiModelProperty(value = "档口类目属性")
    private StoreProdCateAttrDTO cateAttr;
    @ApiModelProperty(value = "档口所有颜色列表")
    private List<StoreColorDTO> allColorList;
    @ApiModelProperty(value = "档口尺码列表")
    private List<SPCSizeDTO> sizeList;
    @ApiModelProperty(value = "档口服务承诺")
    private StoreProdSvcDTO svc;
    @ApiModelProperty(value = "详情内容")
    private String detail;
    @ApiModelProperty(value = "档口生产工艺")
    private StoreProdProcessDTO process;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Valid
    public static class SPCSizeDTO {
        @ApiModelProperty(value = "档口商品颜色ID")
        private Long storeColorId;
        @ApiModelProperty(value = "颜色名称")
        private String colorName;
        @ApiModelProperty(value = "商品尺码")
        private Integer size;
        @ApiModelProperty(value = "档口商品定价")
        private BigDecimal price;
        @ApiModelProperty(value = "是否是标准尺码")
        private Integer standard;
    }


}
