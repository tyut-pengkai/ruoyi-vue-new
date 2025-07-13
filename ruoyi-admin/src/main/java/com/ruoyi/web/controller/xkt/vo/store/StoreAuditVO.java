package com.ruoyi.web.controller.xkt.vo.store;

import com.ruoyi.web.controller.xkt.vo.storeCertificate.StoreCertVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
@ApiModel
@Accessors(chain = true)
public class StoreAuditVO {

    @ApiModelProperty(value = "档口ID", required = true)
    @NotNull(message = "档口ID不能为空")
    private Long storeId;
    @NotNull(message = "审核状态不能为空")
    @ApiModelProperty(value = "审核状态", required = true)
    private Boolean approve;
    @ApiModelProperty(value = "拒绝理由")
    private String rejectReason;
    @ApiModelProperty(value = "档口认证信息")
    private StoreCertVO storeCert;

}
