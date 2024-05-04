package com.coordsoft.hysdk;

import com.coordsoft.commons.codec.digest.DigestUtils;
import com.coordsoft.hysdk.utils.AesCbcPKCS5PaddingUtil;
import com.coordsoft.hysdk.utils.AesCbcZeroPaddingUtil;
import org.junit.Assert;
import org.junit.Test;

public class DevTest {

    @Test
    public void TestMd5Hash() throws Exception {
        String text = "测试！abc@#￥%……&*123";
        String s = DigestUtils.md5Hex(text);
        System.out.println(s); //3de7880c3a4f4e1ab1ed7797213d680a
    }

    @Test
    public void TestPkcs5() throws Exception {
        String text = "测试！abc@#￥%……&*123";
        String pass = "测试！abc@#￥%……&*123";
        String encodeStr = AesCbcPKCS5PaddingUtil.encode(text, pass);
        System.out.println(encodeStr); //WGl8jb82IsBdP/qo72AnkRHLyEc6cYhy0QeqlC9OYTk=
        String decodeStr = AesCbcPKCS5PaddingUtil.decode(encodeStr, pass);
        System.out.println(decodeStr);
        Assert.assertEquals(text, decodeStr);
    }

    @Test
    public void TestZeroPadding() throws Exception {
        String text = "测试！abc@#￥%……&*123";
        String pass = "测试！abc@#￥%……&*123";
        String encodeStr = AesCbcZeroPaddingUtil.encode(text, pass);
        System.out.println(encodeStr); //WGl8jb82IsBdP/qo72AnkRHLyEc6cYhy0QeqlC9OYTk=
        String decodeStr = AesCbcZeroPaddingUtil.decode(encodeStr, pass);
        System.out.println(decodeStr);
        Assert.assertEquals(text, decodeStr);
    }

}
