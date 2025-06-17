package com.ruoyi.xkt.dto.advertStoreFile;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("推广营销图片管理返回数据")
@Data
@Accessors(chain = true)
public class AdvertStoreFileResDTO {

    @ApiModelProperty(value = "推广图片ID")
    private Long advertStoreFileId;
    @ApiModelProperty(value = "档口名称")
    private String storeName;
    @ApiModelProperty(value = "推广图片类型")
    private Integer typeId;
    @ApiModelProperty(value = "图片类型")
    private String typeName;
    @ApiModelProperty(value = "图片路径")
    private String fileUrl;

}
