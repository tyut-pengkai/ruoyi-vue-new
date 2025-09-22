package com.ruoyi.web.controller.xkt.shipMaster.vo;

import lombok.Data;

import java.util.List;

/**
 * @author liangyq
 * @date 2025-05-11 23:46
 */
@Data
public class ShipMasterRQVO {

    private SMIDataVO data;

    @Data
    public static class SMIDataVO {
        private List<SMIVO> records;
    }

    @Data
    public static class SMIVO {
        private Integer supplierId;
        private Integer supplierSkuId;
        private String artNo;
        private String color;
        private String size;
        private String snPrefix;
    }

}
