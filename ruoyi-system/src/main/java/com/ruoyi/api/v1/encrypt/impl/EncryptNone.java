package com.ruoyi.api.v1.encrypt.impl;

import com.ruoyi.api.v1.encrypt.IEncryptType;

public class EncryptNone implements IEncryptType {

    @Override
    public String encrypt(String data, String key) throws Exception {
        return data;
    }

    @Override
    public String decrypt(String data, String key) throws Exception {
        return data;
    }

}
