package com.ruoyi.api.v1.encrypt;

public interface IEncryptType {

    public String encrypt(String data, String key) throws Exception;

    public String decrypt(String data, String key) throws Exception;
}
