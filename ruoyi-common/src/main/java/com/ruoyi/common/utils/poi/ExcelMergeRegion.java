package com.ruoyi.common.utils.poi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liangyq
 * @date 2025-08-16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExcelMergeRegion {

    private Integer firstRow;

    private Integer lastRow;

    private Integer firstCol;

    private Integer lastCol;
}
