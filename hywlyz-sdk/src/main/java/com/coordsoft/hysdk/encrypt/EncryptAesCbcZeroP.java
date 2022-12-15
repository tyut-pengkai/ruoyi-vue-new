package com.coordsoft.hysdk.encrypt;

import com.coordsoft.hysdk.utils.AesCbcZeroPaddingUtil;

public class EncryptAesCbcZeroP {

    public static String encrypt(String data, String key) throws Exception {
        return AesCbcZeroPaddingUtil.encode(data, key);
    }

    public static String decrypt(String data, String key) throws Exception {
        return AesCbcZeroPaddingUtil.decode(data, key);
    }

}
