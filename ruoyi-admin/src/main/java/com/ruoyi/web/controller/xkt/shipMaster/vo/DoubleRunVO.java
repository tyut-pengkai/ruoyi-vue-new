package com.ruoyi.web.controller.xkt.shipMaster.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author liangyq
 * @date 2025-05-11 23:46
 */
@Data
public class DoubleRunVO {

    private DRIDataVO data;

    @Data
    public static class DRIDataVO {
        private List<DRIArtNoVO> data;
    }

    @Data
    public static class DRIArtNoVO {
        private Integer id;
        private Integer user_id;
        private String article_number;
        private List<DRIArtNoSkuVO> skus;
    }

    @Data
    @Accessors(chain = true)
    public static class DRIArtNoSkuVO {
        private Integer product_id;
        private String article_number;
        private String color;
        private Integer size;
        private BigDecimal weight;
        private BigDecimal price;
    }


}
