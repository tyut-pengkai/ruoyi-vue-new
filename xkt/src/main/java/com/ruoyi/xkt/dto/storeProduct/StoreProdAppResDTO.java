package com.ruoyi.xkt.dto.storeProduct;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.xkt.dto.storeProdCateAttr.StoreProdCateAttrDTO;
import com.ruoyi.xkt.dto.storeProdColorPrice.StoreProdColorPriceDTO;
import com.ruoyi.xkt.dto.storeProdDetail.StoreProdDetailDTO;
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
@ApiModel("档口商品APP详情")
@Data
@Accessors(chain = true)
public class StoreProdAppResDTO {

    @ApiModelProperty(value = "档口商品名称")
    private Long storeProdId;
    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "档口商品名称")
    private String prodName;
    @ApiModelProperty(value = "商品货号")
    private String prodArtNum;
    @ApiModelProperty(value = "商品标题")
    private String prodTitle;
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;
    @ApiModelProperty(value = "是否已收藏商品")
    private Boolean collectProd;
    @ApiModelProperty(value = "最低的商品价格")
    private BigDecimal minPrice;
    @ApiModelProperty(value = "规格")
    private String specification;
    @ApiModelProperty(value = "标签列表")
    private List<String> tagList;
    @ApiModelProperty(value = "档口文件列表")
    private List<StoreProdFileResDTO> fileList;
    @ApiModelProperty(value = "档口类目属性列表")
    private List<StoreProdCateAttrDTO> cateAttrList;
    @ApiModelProperty(value = "档口服务承诺")
    private StoreProdSvcDTO svc;
    @ApiModelProperty(value = "详情内容")
    private StoreProdDetailDTO detail;


}
