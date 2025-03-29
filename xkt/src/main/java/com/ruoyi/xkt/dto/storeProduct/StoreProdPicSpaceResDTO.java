package com.ruoyi.xkt.dto.storeProduct;

import com.ruoyi.xkt.dto.storeProductFile.StoreProdFilePicSpaceResDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口图片空间")
@Data
@Builder
@Accessors(chain = true)
public class StoreProdPicSpaceResDTO {

    @ApiModelProperty(name = "档口ID")
    private Long storeId;
    @ApiModelProperty(name = "档口名称")
    private String storeName;
    @ApiModelProperty(name = "档口文件列表")
    private List<StoreProdFilePicSpaceResDTO> fileList;

}
