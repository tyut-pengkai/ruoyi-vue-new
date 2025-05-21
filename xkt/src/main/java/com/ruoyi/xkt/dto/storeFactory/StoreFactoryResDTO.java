package com.ruoyi.xkt.dto.storeFactory;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口合作工厂数据")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoreFactoryResDTO {

    @ApiModelProperty(value = "档口工厂ID")
    @JsonProperty("storeFactoryId")
    private Long id;
    @ApiModelProperty(value = "store.id")
    private Long storeId;
    @ApiModelProperty(value = "工厂名称")
    private String facName;
    @ApiModelProperty(value = "工厂地址")
    private String facAddress;
    @ApiModelProperty(value = "联系人")
    private String facContact;
    @ApiModelProperty(value = "工厂联系电话")
    private String facPhone;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

}
