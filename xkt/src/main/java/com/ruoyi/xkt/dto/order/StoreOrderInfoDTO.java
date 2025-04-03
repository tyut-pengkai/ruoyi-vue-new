package com.ruoyi.xkt.dto.order;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * @author liangyq
 * @date 2025-04-02 22:37
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StoreOrderInfoDTO extends StoreOrderDTO {

    private List<StoreOrderDetailInfoDTO> detailList;
}
