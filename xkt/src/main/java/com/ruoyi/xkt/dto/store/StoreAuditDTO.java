package com.ruoyi.xkt.dto.store;

import com.ruoyi.xkt.dto.storeCertificate.StoreCertDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
@Accessors(chain = true)
public class StoreAuditDTO {

    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "审核状态")
    private Boolean approve;
    @ApiModelProperty(value = "审核信息")
    private String rejectReason;
    @ApiModelProperty(value = "档口认证信息")
    private StoreCertDTO storeCert;

}
