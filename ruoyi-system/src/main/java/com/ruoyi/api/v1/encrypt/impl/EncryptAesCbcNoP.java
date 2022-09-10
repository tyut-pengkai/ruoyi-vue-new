//package com.ruoyi.api.v1.encrypt.impl;
//
//import com.ruoyi.api.v1.encrypt.IEncryptType;
//import com.ruoyi.api.v1.utils.encrypt.AesCbcNoPaddingUtil;
//
//public class EncryptAesCbcNoP implements IEncryptType {
//
//    @Override
//    public String encrypt(String data, String key) throws Exception {
//        return AesCbcNoPaddingUtil.encode(data, key);
//    }
//
//    @Override
//    public String decrypt(String data, String key) throws Exception {
//        return AesCbcNoPaddingUtil.decode(data, key);
//    }
//
//}
