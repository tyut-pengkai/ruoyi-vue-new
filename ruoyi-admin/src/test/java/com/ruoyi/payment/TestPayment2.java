package com.ruoyi.payment;


import com.alibaba.fastjson.JSON;
import com.ruoyi.payment.domain.WechatpayConfig;

public class TestPayment2 {

    public static void main(String[] args) {
        System.out.println(JSON.toJSONString(new WechatpayConfig("", "wxe87e4ab05c3bed73", "1604321483", "henanWangliang410182198210276535")));
    }

}