package com.ruoyi.web.controller.xkt.migartion.vo.gtBizAfter;

import com.ruoyi.common.annotation.Excel;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel
@Data
@Accessors(chain = true)
public class GtBizAfterImportVO {

    @Excel(name = "货号对比结果")
    private String gtAfterArtNum;

}
