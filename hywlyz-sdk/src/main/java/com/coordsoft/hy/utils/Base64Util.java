package com.coordsoft.hy.utils;

import org.apache.commons.codec.binary.Base64;

public class Base64Util {

    /**
     * 对数据进行Base64编码，使用的是{@link Base64}
     *
     * @param input 来源数据
     * @return Base64编码的数据
     */
    public static String encode(String input) {
        return Base64.encodeBase64String(input.getBytes());
    }

    /**
     * 对数据进行Base64解码，使用的是{@link Base64}
     *
     * @param str 需要解码的数据
     * @return Base64解码后的数据
     */
    public static String decode(String str) {
        return new String(Base64.decodeBase64(str));
    }

}
