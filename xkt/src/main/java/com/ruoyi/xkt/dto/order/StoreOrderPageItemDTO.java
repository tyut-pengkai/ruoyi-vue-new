package com.ruoyi.xkt.dto.order;

import com.ruoyi.xkt.dto.express.ExpressWaybillNoInfoDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * @author liangyq
 * @date 2025-04-14 11:18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StoreOrderPageItemDTO extends StoreOrderDTO {

    private String storeName;

    private String brandName;

    private String expressName;

    private String originProvinceName;

    private String originCityName;

    private String originCountyName;

    private String destinationProvinceName;

    private String destinationCityName;

    private String destinationCountyName;

    private List<StoreOrderDetailInfoDTO> orderDetails;

    private List<ExpressWaybillNoInfoDTO> expressWaybillNoInfos;

}
