package com.ruoyi.web.controller.xkt.shipMaster.vo.shipMaster;

import lombok.Data;

import java.util.List;

/**
 * @author liangyq
 * @date 2025-05-11 23:46
 */
@Data
public class ShipMasterCusVO {

    private SMCDataVO data;

    @Data
    public static class SMCDataVO {
        private List<SMCVO> records;
    }

    @Data
    public static class SMCVO {
        private Integer supplierId;
        private String name;
    }

}
