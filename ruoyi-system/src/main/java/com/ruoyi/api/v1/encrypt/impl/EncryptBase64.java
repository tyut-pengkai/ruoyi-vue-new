package com.ruoyi.api.v1.encrypt.impl;

import com.ruoyi.api.v1.encrypt.IEncryptType;
import com.ruoyi.api.v1.utils.encrypt.Base64Util;

public class EncryptBase64 implements IEncryptType {

    @Override
    public String encrypt(String data, String key) throws Exception {
        return Base64Util.encode(data);
    }

    @Override
    public String decrypt(String data, String key) throws Exception {
        return Base64Util.decode(data);
    }

}
