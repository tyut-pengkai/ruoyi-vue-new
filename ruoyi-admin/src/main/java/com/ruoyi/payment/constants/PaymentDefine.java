package com.ruoyi.payment.constants;

import com.alibaba.fastjson.JSON;
import com.ruoyi.api.v1.utils.MyUtils;
import com.ruoyi.payment.domain.Payment;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class PaymentDefine {

    public static String BASE_PACKAGE = "com.ruoyi.payment.payment";

    public static Map<String, Payment> paymentMap = new HashMap<>();

    static {
        List<Class<?>> classList = MyUtils.getClassesFromPackage(BASE_PACKAGE);
        for (Class<?> clazz : classList) {
            try {
                Constructor<?> ct = clazz.getDeclaredConstructor();
                Object obj = ct.newInstance();
                Payment payment = (Payment) obj;
                paymentMap.put(payment.getCode(), payment);
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        log.info("总共加载支付方式 {} 个，详情如下：", paymentMap.size());
        log.info(JSON.toJSONString(paymentMap.keySet()));
        log.debug(JSON.toJSONString(paymentMap));
    }

}
