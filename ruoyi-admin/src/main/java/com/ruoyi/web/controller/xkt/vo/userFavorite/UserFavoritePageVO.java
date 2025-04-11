package com.ruoyi.web.controller.xkt.vo.userFavorite;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ruoyi.web.controller.xkt.vo.BasePageVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel("用户收藏列表查询入参")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserFavoritePageVO extends BasePageVO {

    @NotNull(message = "商品状态不可为空!")
    @ApiModelProperty(value = "商品状态，在售传：2， 已失效传：4,5")
    private List<Integer> statusList;
    @ApiModelProperty(value = "商品货号")
    private String prodArtNum;
    @ApiModelProperty(value = "档口名称")
    private String storeName;

}
