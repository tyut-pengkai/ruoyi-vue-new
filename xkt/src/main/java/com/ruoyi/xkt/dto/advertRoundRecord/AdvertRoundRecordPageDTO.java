package com.ruoyi.xkt.dto.advertRoundRecord;

import com.ruoyi.xkt.dto.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel("档口竞价失败列表入参")
@Data
public class AdvertRoundRecordPageDTO extends BasePageDTO {

    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "平台ID")
    private Long platformId;
    @ApiModelProperty(value = "推广类型")
    private Integer typeId;
    @ApiModelProperty(value = "投放开始时间")
    private Date startTime;
    @ApiModelProperty(value = "投放结束时间")
    private Date endTime;

}
