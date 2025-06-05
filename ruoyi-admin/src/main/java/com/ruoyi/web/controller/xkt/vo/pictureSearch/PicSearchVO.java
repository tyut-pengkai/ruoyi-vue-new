package com.ruoyi.web.controller.xkt.vo.pictureSearch;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("以图搜款入参")
@Data
public class PicSearchVO {

    @ApiModelProperty(value = "以图搜款图片路径")
    private String picKey;
    @ApiModelProperty(value = "图片名称")
    private String picName;
    @ApiModelProperty(value = "图片大小 搜索的图片大小，不超过4M")
    private BigDecimal picSize;

}
