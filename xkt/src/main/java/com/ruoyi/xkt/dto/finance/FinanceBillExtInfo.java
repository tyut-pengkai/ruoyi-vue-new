package com.ruoyi.xkt.dto.finance;

import cn.hutool.json.JSONUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liangyq
 * @date 2025-08-21
 */
public class FinanceBillExtInfo {

    private FinanceBillExtInfo() {
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PaymentWithdraw {
        /**
         * 支付渠道
         * {@link com.ruoyi.xkt.enums.EPayChannel}
         */
        private Integer payChannel;
        /**
         * 账户
         */
        private String accountOwnerNumber;
        /**
         * 真实姓名
         */
        private String accountOwnerName;
        /**
         * 手机号
         */
        private String accountOwnerPhoneNumber;

        public static PaymentWithdraw parse(String jsonStr) {
            return JSONUtil.toBean(jsonStr, PaymentWithdraw.class);
        }

        public String toJsonStr() {
            return JSONUtil.toJsonStr(this);
        }
    }
}
