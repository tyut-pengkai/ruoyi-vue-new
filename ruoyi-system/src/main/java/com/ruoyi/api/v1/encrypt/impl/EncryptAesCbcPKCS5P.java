package com.ruoyi.api.v1.encrypt.impl;

import com.ruoyi.api.v1.encrypt.IEncryptType;
import com.ruoyi.api.v1.utils.encrypt.AesCbcPKCS5PaddingUtil;

public class EncryptAesCbcPKCS5P implements IEncryptType {

    @Override
    public String encrypt(String data, String key) throws Exception {
        return AesCbcPKCS5PaddingUtil.encode(data, key);
    }

    @Override
    public String decrypt(String data, String key) throws Exception {
        return AesCbcPKCS5PaddingUtil.decode(data, key);
    }

}
