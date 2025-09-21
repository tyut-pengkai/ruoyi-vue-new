package com.ruoyi.web.controller.xkt.shipMaster.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author liangyq
 * @date 2025-05-11 23:46
 */
@Data
public class DoubleRunImportVO {

    private DRIDataVO data;

    @Data
    public static class DRIDataVO {
        private List<DRIArtNoVO> data;
    }

    @Data
    public static class DRIArtNoVO {
        private String article_number;
        private List<DRIArtNoSkuVO> skus;
    }

    @Data
    public static class DRIArtNoSkuVO {
        private String color;
        private Integer size;
        private BigDecimal weight;
        private BigDecimal price;
    }


}
