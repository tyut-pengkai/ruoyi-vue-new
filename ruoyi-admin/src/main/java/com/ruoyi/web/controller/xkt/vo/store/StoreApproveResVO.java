package com.ruoyi.web.controller.xkt.vo.store;

import com.ruoyi.web.controller.xkt.vo.storeCertificate.StoreCertResVO;
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
public class StoreApproveResVO {

    @ApiModelProperty(value = "档口基本信息")
    private StoreResVO basic;
    @ApiModelProperty(value = "档口认证信息")
    private StoreCertResVO certificate;

}
