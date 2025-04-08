package com.ruoyi.xkt.dto.payment;

import com.ruoyi.xkt.domain.PaymentBill;
import com.ruoyi.xkt.domain.PaymentBillDetail;
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
public class PaymentBillInfo {

    private PaymentBill paymentBill;

    private List<PaymentBillDetail> paymentBillDetails;
}
