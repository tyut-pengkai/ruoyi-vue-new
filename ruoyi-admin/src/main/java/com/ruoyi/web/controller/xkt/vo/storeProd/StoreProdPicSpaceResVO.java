package com.ruoyi.web.controller.xkt.vo.storeProd;

import com.ruoyi.web.controller.xkt.vo.storeProductFile.StoreProdFilePicSpaceResVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
public class StoreProdPicSpaceResVO {

    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "档口名称")
    private String storeName;
    @ApiModelProperty(value = "档口文件列表")
    private List<StoreProdFilePicSpaceResVO> fileList;

}
