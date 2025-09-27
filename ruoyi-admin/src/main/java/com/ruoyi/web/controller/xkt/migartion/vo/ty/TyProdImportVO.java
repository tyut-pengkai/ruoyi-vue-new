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
public class TyProdImportVO {

    @Excel(name = "HUOH")
    private String prodArtNum;
    @Excel(name = "COL")
    private String colorName;
    @Excel(name = "jg")
    private BigDecimal price;
    @Excel(name = "SN")
    private String tySnPrefix;

}
