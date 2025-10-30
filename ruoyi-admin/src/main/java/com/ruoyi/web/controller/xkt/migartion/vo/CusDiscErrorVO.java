package com.ruoyi.web.controller.xkt.migartion.vo;

import com.ruoyi.common.annotation.Excel;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author liangyq
 * @date 2025-05-11 23:46
 */
@Data
@Accessors(chain = true)
public class CusDiscErrorVO {

    @Excel(name = "序号")
    private int orderNum;
    @Excel(name = "客户名称")
    private String cusName;
    @Excel(name = "货号")
    private String prodArtNum;
    @Excel(name = "颜色名称")
    private String colorName;
    @Excel(name = "错误信息")
    private String errMsg;
    @Excel(name = "错误详情")
    private String detail;

}
