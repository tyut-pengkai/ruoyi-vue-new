package com.ruoyi.xkt.dto.finance;

import com.ruoyi.xkt.domain.FinanceBill;
import com.ruoyi.xkt.domain.FinanceBillDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author liangyq
 * @date 2025-04-08 21:18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FinanceBillInfo {

    private FinanceBill financeBill;

    private List<FinanceBillDetail> financeBillDetails;
}
