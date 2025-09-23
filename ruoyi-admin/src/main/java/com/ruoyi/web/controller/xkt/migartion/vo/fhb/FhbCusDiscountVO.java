package com.ruoyi.web.controller.xkt.migartion.vo.fhb;

import lombok.Data;

import java.util.List;

/**
 * @author liangyq
 * @date 2025-05-11 23:46
 */
@Data
public class FhbCusDiscountVO {

    private SMCDDataVO data;

    @Data
    public static class SMCDDataVO {
        private List<SMCDRecordVO> records;
    }

    @Data
    public static class SMCDRecordVO {
        private String artNo;
        private String color;
        private Integer supplyPrice;
        private Integer customerPrice;
        private String customerName;
        private Integer supplierId;
        private Integer discount;
    }


}
