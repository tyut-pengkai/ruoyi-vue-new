package com.ruoyi.web.controller.xkt.migartion.vo.fhbBizAfter;

import com.ruoyi.common.annotation.Excel;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel
@Data
@Accessors(chain = true)
public class FhbBizAfterImportVO {

    @Excel(name = "货号对比结果")
    private String fhbAfterArtNum;

}
