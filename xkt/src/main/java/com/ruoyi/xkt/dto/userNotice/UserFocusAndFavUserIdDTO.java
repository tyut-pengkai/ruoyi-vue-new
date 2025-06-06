package com.ruoyi.xkt.dto.userNotice;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("用户关注档口或者收藏商品用户ID列表")
@Data
@Accessors(chain = true)
public class UserFocusAndFavUserIdDTO {

    @ApiModelProperty(value = "用户接收消息类型")
    private Integer targetNoticeType;
    @ApiModelProperty(value = "用户ID")
    private Long userId;

}
