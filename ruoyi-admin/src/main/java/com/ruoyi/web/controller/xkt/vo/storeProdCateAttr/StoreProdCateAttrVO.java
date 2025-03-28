package com.ruoyi.web.controller.xkt.vo.storeProdCateAttr;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口商品类目属性")
@Data
public class StoreProdCateAttrVO {

    @ApiModelProperty(name = "系统设置类目")
    private String dictType;
    @ApiModelProperty(name = "系统设置类目值")
    private String dictValue;

}
