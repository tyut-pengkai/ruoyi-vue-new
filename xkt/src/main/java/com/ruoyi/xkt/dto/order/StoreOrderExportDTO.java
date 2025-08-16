package com.ruoyi.xkt.dto.order;

import lombok.Data;

import java.util.List;

/**
 * @author liangyq
 * @date 2025-08-17
 */
@Data
public class StoreOrderExportDTO {

    private String title;

    private List<Item> items;

    @Data
    public static class Item {

        private Integer seqNo;

        private String orderNo;

        private String createTime;

        private String prodArtNum;

        private String colorName;

        private Integer size;

        private Integer goodsQuantity;

        private String detailStatus;

    }
}
