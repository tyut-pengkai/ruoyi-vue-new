package com.ruoyi.web.controller.xkt.migartion.vo.fhb;

import lombok.Data;
import lombok.experimental.Accessors;

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
    @Accessors(chain = true)
    public static class SMIVO {
        private Integer supplierId;
        private Integer supplierSkuId;
        private String artNo;
        private String color;
        private String size;
        private String outStuff;
        private String innerStuff;
        private BigDecimal addPrice;
        private String addPriceSize;
        private BigDecimal salePrice;
    }

}
