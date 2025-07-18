package com.ruoyi.xkt.dto.express;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liangyq
 * @date 2025-07-18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpressWaybillNoInfoDTO {

    private Long expressId;

    private String expressName;

    private String expressWaybillNo;
}
