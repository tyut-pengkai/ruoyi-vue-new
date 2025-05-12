package com.easycode.cloud.domain.dto;

import com.easycode.cloud.domain.PrdReturnOrderDetail;
import org.apache.ibatis.type.Alias;


/**
 * 生产发料退货明细dto
 * @author bcp
 */
@Alias("PrdReturnOrderDetailDto")
public class PrdReturnOrderDetailDto extends PrdReturnOrderDetail {

    /**
     * 明细id
     */
    private Long detailId;

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }
}
