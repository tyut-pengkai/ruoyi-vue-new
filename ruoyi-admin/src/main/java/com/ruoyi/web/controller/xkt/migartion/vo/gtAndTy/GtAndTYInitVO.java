package com.ruoyi.web.controller.xkt.migartion.vo.gtAndTy;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel
@Data
@Accessors(chain = true)
public class GtAndTYInitVO {

    private Integer userId;
    private Long storeId;
    @NotBlank(message = "diffStr不能为空")
    private String diffStr;
    // 大小码加价金额 0 or other
    private BigDecimal addOverPrice;
    // GT 和 TY 能匹配但因 两边颜色冲突，需要手动处理的货号
    private List<String> excludeArtNoList;

}
