package com.ruoyi.web.controller.xkt.migartion.vo.gt;

import com.ruoyi.common.annotation.Excel;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author liangyq
 * @date 2025-05-11 23:46
 */
@Data
@Accessors(chain = true)
public class GtCateVO {

    @Excel(name = "货号")
    private String article_number;
    @Excel(name = "分类")
    private String cate_name;
    // 步橘的分类id
    private Long buju_cate_id;

}
