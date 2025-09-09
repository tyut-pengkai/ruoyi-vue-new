package com.ruoyi.xkt.dto.storeMember;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
public class StoreMemberPageResDTO {

    @ApiModelProperty(value = "档口会员ID")
    private Long storeMemberId;
    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "档口名称")
    private String storeName;
    @ApiModelProperty(value = "档口负责人")
    private String userName;
    @ApiModelProperty(value = "联系电话")
    private String contactPhone;
    @ApiModelProperty(value = "生效开始时间")
    private Date startTime;
    @ApiModelProperty(value = "生效结束时间")
    private Date endTime;

}
