package com.ruoyi.api.v1.utils;

import org.apache.commons.codec.digest.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class SignUtil {
    /**
     * 除去数组中的空值和签名参数
     *
     * @param sArray 签名参数组
     * @return 去掉空值与签名参数后的新签名参数组
     */
    public static Map<String, String> paramFilter(Map<String, String> sArray) {
        Map<String, String> result = new HashMap<>();
        if (sArray == null || sArray.size() <= 0) {
            return result;
        }
        for (String key : sArray.keySet()) {
            String value = sArray.get(key);
            if (value == null || value.equals("") || key.equalsIgnoreCase("sign")) {
                continue;
            }
            result.put(key, value);
        }
        return result;
    }

    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     *
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, String> params) {
        List<String> keys = new ArrayList<>(params.keySet());
        Collections.sort(keys);
        String prestr = "";
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }
        return prestr;
    }

    /**
     * 签名字符串
     *
     * @param text 需要签名的字符串
     * @param salt 密钥
     * @return 签名结果
     */
    public static String sign(String text, String salt) {
        text = text + salt;
        return DigestUtils.md5Hex(text.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 签名字符串
     *
     * @param params 需要签名的参数
     * @param salt   密钥
     */
    public static String sign(Map<String, String> params, String salt) {
        Map<String, String> map = SignUtil.paramFilter(params);
        String text = SignUtil.createLinkString(map);
        return SignUtil.sign(text, salt);
    }

    /**
     * 验证sign
     *
     * @param text 需要签名的字符串
     * @param sign 签名结果
     * @param salt 密钥
     * @return 验证是否通过
     */
    public static boolean verify(String text, String sign, String salt) {
        text = text + salt;
        String mySign = DigestUtils.md5Hex(text.getBytes(StandardCharsets.UTF_8));
        return mySign.equals(sign);
    }

    /**
     * 验证sign
     *
     * @param params 需要签名的参数
     * @param sign   签名结果
     * @param salt   密钥
     * @return 验证是否通过
     */
    public static Boolean verifySign(Map<String, String> params, String sign, String salt) {
        Map<String, String> map = SignUtil.paramFilter(params);
        String text = SignUtil.createLinkString(map);
        return SignUtil.verify(text, sign, salt);
    }
}
