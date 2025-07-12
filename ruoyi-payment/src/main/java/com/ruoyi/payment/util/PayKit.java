package com.ruoyi.payment.util;

import java.util.Base64;
import java.util.Map;
import java.util.TreeMap;

/**
 * 支付工具类
 */
public class PayKit {
    
    /**
     * Base64编码
     * 
     * @param value 待编码的字符串
     * @return 编码后的字符串
     */
    public static String base64Encode(String value) {
        if (value == null) {
            return null;
        }
        return Base64.getEncoder().encodeToString(value.getBytes());
    }
    
    /**
     * 将Map转换为URL查询字符串
     * 
     * @param params Map参数
     * @param encode 是否需要URL编码
     * @return URL查询字符串
     */
    public static String createLinkString(Map<String, Object> params, boolean encode) {
        if (params == null || params.isEmpty()) {
            return "";
        }
        
        // 使用TreeMap，默认按照字母顺序排序
        Map<String, Object> sortedParams = new TreeMap<>(params);
        
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> entry : sortedParams.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value == null) {
                continue;
            }
            sb.append(key).append("=").append(value).append("&");
        }
        
        // 删除最后一个&符号
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        
        return sb.toString();
    }
} 