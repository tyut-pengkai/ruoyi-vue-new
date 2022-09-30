package com.ruoyi.api.v1.encrypt.impl;

import com.ruoyi.api.v1.encrypt.IEncryptType;
import org.apache.commons.codec.binary.Base64;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

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

    @Test
    void test() throws Exception {
        String encrypt = enc.encrypt("你好啊", "abcd12345!@#$%！@#￥%你好世界");
        System.out.println(encrypt);
        String decrypt = enc.decrypt(encrypt, "abcd12345!@#$%！@#￥%你好世界");
        System.out.println(decrypt);

    }

    @Test
    void test1() throws Exception {
        System.out.println(Base64.encodeBase64String("你好".getBytes(StandardCharsets.UTF_8)));
        System.out.println(Base64.encodeBase64String("你好".getBytes()));

        String decrypt = enc.decrypt("/Gq0pHJI+6WSnM9LBQWDng==", "1234@#awd");
        System.out.println(decrypt);

    }
}