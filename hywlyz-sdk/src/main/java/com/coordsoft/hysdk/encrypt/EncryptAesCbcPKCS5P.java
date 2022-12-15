package com.coordsoft.hysdk.encrypt;

import com.coordsoft.hysdk.utils.AesCbcPKCS5PaddingUtil;

public class EncryptAesCbcPKCS5P {

    public static String encrypt(String data, String key) throws Exception {
        return AesCbcPKCS5PaddingUtil.encode(data, key);
    }

    public static String decrypt(String data, String key) throws Exception {
        return AesCbcPKCS5PaddingUtil.decode(data, key);
    }

}
