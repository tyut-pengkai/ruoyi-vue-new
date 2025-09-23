package com.ruoyi.web.controller.xkt.migartion.vo.gt;

import lombok.Data;

import java.util.List;

/**
 * @author liangyq
 * @date 2025-05-11 23:46
 */
@Data
public class GtProdVO {

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
        private String characters;
        private List<GtProdSkuVO> skus;
    }

}
