package com.ruoyi.xkt.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @author liangyq
 * @date 2025-04-30 13:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AfterSaleApplyResultDTO {

    private Long storeOrderId;

    private Set<ExpressSimpleDTO> needStopExpresses;

}
