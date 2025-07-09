package com.ruoyi.web.controller.xkt.vo.storeProdStorage;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.web.controller.xkt.vo.BasePageVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel(value = "入库单列表查询入参")
@EqualsAndHashCode(callSuper = true)
@Data
public class StoreProdStoragePageVO extends BasePageVO {

    @ApiModelProperty(value = "档口ID", required = true)
    @NotNull(message = "档口ID不能为空!")
    private Long storeId;
    @ApiModelProperty(value = "商品货号")
    private String prodArtNum;
    @ApiModelProperty(value = "入库类型")
    private Integer storageType;
    @ApiModelProperty(value = "销售开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTimeStart;
    @ApiModelProperty(value = "销售结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTimeEnd;

}
