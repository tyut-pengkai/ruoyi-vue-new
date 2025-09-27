package com.ruoyi.web.controller.xkt.migartion.vo.ty;

import com.ruoyi.common.annotation.Excel;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel
@Data
@Accessors(chain = true)
public class TyCusDiscImportVO {

    // 客户名称
    private String cusName;
    @Excel(name = "货号")
    private String prodArtNum;
    @Excel(name = "颜色")
    private String colorName;
    @Excel(name = "基础价格")
    private Integer basicPrice;
    @Excel(name = "客户价格")
    private Integer customerPrice;
    // 客户优惠diff
    private Integer discount;

}
