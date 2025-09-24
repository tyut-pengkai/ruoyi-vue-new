package com.ruoyi.web.controller.xkt.migartion.vo.gt;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author liangyq
 * @date 2025-05-11 23:46
 */
@Data
@Accessors(chain = true)
public class GtCateVO {

    private List<GCIDataVO> data;

    @Data
    public static class GCIDataVO {
        private Integer id;
        private Integer has_child;
        private String name;
    }

}
