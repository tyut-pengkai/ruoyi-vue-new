package com.ruoyi.web.controller.xkt.migartion.vo.ty;

import com.ruoyi.common.annotation.Excel;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.AccessType;

/**
 * @author liangyq
 * @date 2025-05-11 23:46
 */
@Data
@Accessors(chain = true)
public class TyProdStockVO {

    @Excel(name = "货号")
    private String prodArtNum;
    @Excel(name = "颜色")
    private String colorName;
    @Excel(name = "size30")
    private Integer size30;
    @Excel(name = "size31")
    private Integer size31;
    @Excel(name = "size32")
    private Integer size32;
    @Excel(name = "size33")
    private Integer size33;
    @Excel(name = "size34")
    private Integer size34;
    @Excel(name = "size35")
    private Integer size35;
    @Excel(name = "size36")
    private Integer size36;
    @Excel(name = "size37")
    private Integer size37;
    @Excel(name = "size38")
    private Integer size38;
    @Excel(name = "size39")
    private Integer size39;
    @Excel(name = "size40")
    private Integer size40;
    @Excel(name = "size41")
    private Integer size41;
    @Excel(name = "size42")
    private Integer size42;
    @Excel(name = "size43")
    private Integer size43;

}
