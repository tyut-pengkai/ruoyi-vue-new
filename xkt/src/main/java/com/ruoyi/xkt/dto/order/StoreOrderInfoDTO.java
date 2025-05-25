package com.ruoyi.xkt.dto.order;

import com.ruoyi.xkt.dto.express.ExpressTrackDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * @author liangyq
 * @date 2025-04-02 22:37
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StoreOrderInfoDTO extends StoreOrderDTO {

    private String storeName;

    private String brandName;

    private String expressName;

    private String originProvinceName;

    private String originCityName;

    private String originCountyName;

    private String destinationProvinceName;

    private String destinationCityName;

    private String destinationCountyName;

    private String orderUserNickName;

    private String orderUserPhoneNumber;

    private Date payTime;

    private List<StoreOrderDetailInfoDTO> orderDetails;

    private List<ExpressTrackDTO> expressTracks;
}
