package com.ruoyi.xkt.dto.express;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liangyq
 * @date 2025-04-18 15:15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpressPrintDTO {
    /**
     * 运单号
     */
    private String waybillNo;
    /**
     * 文件流Base64编码
     */
    private String result;
}
