package com.ruoyi.web.controller.xkt.migartion.vo.fhb;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author liangyq
 * @date 2025-05-11 23:46
 */
@Data
public class FhbProdVO {

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
        private BigDecimal addPrice;
        private String addPriceSize;
        private String snPrefix;
    }

}
