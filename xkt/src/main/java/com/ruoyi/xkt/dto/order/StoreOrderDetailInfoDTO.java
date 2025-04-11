package com.ruoyi.xkt.dto.order;

import com.ruoyi.xkt.dto.storeProductFile.StoreProdFileResDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * @author liangyq
 * @date 2025-04-02 22:39
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StoreOrderDetailInfoDTO extends StoreOrderDetailDTO {

    private List<StoreProdFileResDTO> fileList;
}
