package com.ruoyi.api.v1.encrypt.impl;

import com.ruoyi.api.v1.encrypt.IEncryptType;
import org.junit.jupiter.api.Test;

class EncryptBase64Test {

    // private final IEncryptType enc = new EncryptBase64();
    // private final IEncryptType enc = new EncryptAesCbcPKCS5P();
    private final IEncryptType enc = new EncryptAesCbcZeroP();

    @Test
    void encrypt() throws Exception {
        String encrypt = enc.encrypt("abcd12345!@#$%！@#￥%你好世界", "abcd12345!@#$%！@#￥%你好世界");
        System.out.println(encrypt);
    }

    @Test
    void decrypt() throws Exception {
        String encrypt = enc.decrypt("YWJjZDEyMzQ1IUAjJCXvvIFAI++/pSXkvaDlpb3kuJbnlYw=", "abcd12345!@#$%！@#￥%你好世界");
        System.out.println(encrypt);
    }
}