package com.ruoyi.xkt.dto.storeMember;

import com.ruoyi.xkt.dto.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
public class StoreMemberPageDTO extends BasePageDTO {

    @ApiModelProperty(value = "档口名称")
    private String storeName;
    @ApiModelProperty(value = "会员状态")
    private List<Integer> memberStatusList;

}
