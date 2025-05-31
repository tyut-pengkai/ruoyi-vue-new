package com.ruoyi.xkt.dto.store;

import com.ruoyi.xkt.dto.storeCertificate.StoreCertResDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口基本信息")
@Data
public class StoreApproveResDTO {

    @ApiModelProperty(value = "档口基本信息")
    private StoreResDTO basic;
    @ApiModelProperty(value = "档口认证信息")
    private StoreCertResDTO certificate;

}
