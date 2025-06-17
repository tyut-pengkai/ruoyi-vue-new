package com.ruoyi.web.controller.xkt.vo.advertStoreFile;

import com.ruoyi.web.controller.xkt.vo.BasePageVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel("推广营销图片管理分页查询入参")
@Data
public class AdvertStoreFilePageVO extends BasePageVO {

    @ApiModelProperty(value = "档口名称")
    private String storeName;
    @ApiModelProperty(value = "类型ID")
    private Integer typeId;

}
