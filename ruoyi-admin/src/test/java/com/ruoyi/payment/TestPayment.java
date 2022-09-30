package com.ruoyi.payment;


import com.alibaba.fastjson.JSON;
import com.ruoyi.payment.domain.EasypayConfig;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class TestPayment {

    public static void main(String[] args) {

        System.out.println(JSON.toJSONString(new EasypayConfig("", "", "", "", "")));

        String url = "http://codepay.coordsoft.com";//支付地址
        String pid = "1000";//商户id
        String type = "pc";//支付类型
        String outTradeNo = "1111";//商户单号
        String notifyUrl = "";//异步通知
        String returnUrl = "history.go(-1)";//跳转地址
        String name = "商品测试";//商品名
        String money = "1.00";//价格
        String signType = "MD5";//签名类型
        String key = "0f8sx9X20F16040FOX10C000LFzcaXZF";//商户密钥

        //参数存入 map
        Map<String, String> sign = new HashMap<>();
        sign.put("pid", pid);
        sign.put("type", type);
        sign.put("out_trade_no", outTradeNo);
        sign.put("notify_url", notifyUrl);
        sign.put("return_url", returnUrl);
        sign.put("name", name);
        sign.put("money", money);

        //根据key升序排序
        sign = sortByKey(sign);
        StringBuilder signStr = new StringBuilder();
        //遍历map 转成字符串
        for (Map.Entry<String, String> m : sign.entrySet()) {
            signStr.append(m.getKey()).append("=").append(m.getValue()).append("&");
        }
        //去掉最后一个 &
        signStr = new StringBuilder(signStr.substring(0, signStr.length() - 1));
        //最后拼接上KEY
        signStr.append(key);
        //转为MD5
        signStr = new StringBuilder(DigestUtils.md5DigestAsHex(signStr.toString().getBytes(StandardCharsets.UTF_8)));
        sign.put("sign_type", signType);
        sign.put("sign", signStr.toString());
        System.out.println("<form id='paying' action='" + url + "/submit.php' method='post'>");
        for (Map.Entry<String, String> m : sign.entrySet()) {
            System.out.println("<input type='hidden' name='" + m.getKey() + "' value='" + m.getValue() + "'/>");
        }
        System.out.println("<input type='submit' value='正在跳转'>");
        System.out.println("</form>");
        System.out.println("<script>document.forms['paying'].submit();</script>");
    }

    public static <K extends Comparable<? super K>, V> Map<K, V> sortByKey(Map<K, V> map) {
        Map<K, V> result = new LinkedHashMap<>();
        map.entrySet().stream()
                .sorted(Map.Entry.<K, V>comparingByKey()).forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        return result;
    }

}