package com.ruoyi.xkt.dto.useSearchHistory;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("用户搜索记录")
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserSearchHistoryDTO {

    @ApiModelProperty(value = "用户搜索ID")
    private Long id;
    @ApiModelProperty(value = "用户ID")
    private Long userId;
    @ApiModelProperty(value = "用户名称")
    private String userName;
    @ApiModelProperty(value = "搜索内容")
    private String searchContent;
    @ApiModelProperty(value = "平台ID")
    private Integer platformId;
    @ApiModelProperty(value = "搜索时间")
    private Date searchTime;

}
