package com.ruoyi.web.controller.common.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liangyq
 * @date 2025-11-03
 */
@Data
public class AppVersionVO {

    @ApiModelProperty("APP最新版本号")
    private String version;

    @ApiModelProperty("安卓APK下载地址（IOS直接跳转商店更新）")
    private String downloadUrl;

    @ApiModelProperty("更新说明")
    private String updateNotes;

    @ApiModelProperty("是否强制更新")
    private Boolean forcedUpdate;
}
