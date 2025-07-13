package com.ruoyi.web.controller.xkt.vo.storeProdColorSize;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "打印条码返回前端数据model")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StorePrintSnResVO {

    @ApiModelProperty(value = "档口商品颜色ID")
    private Long storeProdColorId;
    @ApiModelProperty(value = "打印size条码列表")
    private List<SPSizeSnVO> sizeSnList;

    @Data
    @ApiModel
    @Accessors(chain = true)
    public static class SPSizeSnVO {
        @ApiModelProperty(value = "尺码")
        private Integer size;
        @ApiModelProperty(value = "条码列表")
        private List<String> snList;
    }

}
