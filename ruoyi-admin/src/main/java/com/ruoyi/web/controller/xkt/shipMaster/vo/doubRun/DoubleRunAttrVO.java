package com.ruoyi.web.controller.xkt.shipMaster.vo.doubRun;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author liangyq
 * @date 2025-05-11 23:46
 */
@Data
public class DoubleRunAttrVO {

    private Map<String, DRAAttrVO> data;

    @Data
    public static class DRAAttrVO {
        private Integer must;
        private Integer multi;
        private Integer has_value;
        // eg: 帮面材质
        private String name;
        private Integer props_id;
        private List<DRAAttrItemVO> attr;

        @Data
        public static class DRAAttrItemVO {
            private Integer props_id;
            // eg: 帮面材质
            private String props_name;
            private Integer propsvalue_id;
            // eg: 头层牛皮
            private String propsvalue_name;
            private Integer choosed;
        }
    }


}
