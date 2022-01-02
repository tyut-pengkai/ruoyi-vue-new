package com.ruoyi.api.v1.encrypt.impl;

import com.ruoyi.api.v1.encrypt.IEncryptType;
import com.ruoyi.api.v1.utils.encrypt.AesCbcZeroPaddingUtil;

public class EncryptAesCbcZeroP implements IEncryptType {

    @Override
    public String encrypt(String data, String key) throws Exception {
        return AesCbcZeroPaddingUtil.encode(data, key);
    }

    @Override
    public String decrypt(String data, String key) throws Exception {
        return AesCbcZeroPaddingUtil.decode(data, key);
    }

}
