package com.coordsoft.hysdk.encrypt;

import com.coordsoft.hysdk.utils.Base64Util;

public class EncryptBase64 {

    public static String encrypt(String data, String key) throws Exception {
        return Base64Util.encode(data);
    }

    public static String decrypt(String data, String key) throws Exception {
        return Base64Util.decode(data);
    }

}
