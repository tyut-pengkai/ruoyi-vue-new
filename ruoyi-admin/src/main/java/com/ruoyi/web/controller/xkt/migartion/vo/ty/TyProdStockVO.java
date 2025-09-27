package com.ruoyi.web.controller.xkt.migartion.vo.ty;

import lombok.Data;

import java.util.List;

/**
 * @author liangyq
 * @date 2025-05-11 23:46
 */
@Data
public class TyProdStockVO {

    private SMPSDataVO data;

    @Data
    public static class SMPSDataVO {
        private SMPSListVO list;
    }

    @Data
    public static class SMPSListVO {
        private List<SMPSRecordVO> records;
    }

    @Data
    public static class SMPSRecordVO {
        private Integer supplierId;
        private String artNo;
        private String color;
        private Integer size30;
        private Integer size31;
        private Integer size32;
        private Integer size33;
        private Integer size34;
        private Integer size35;
        private Integer size36;
        private Integer size37;
        private Integer size38;
        private Integer size39;
        private Integer size40;
        private Integer size41;
        private Integer size42;
        private Integer size43;
    }

}
