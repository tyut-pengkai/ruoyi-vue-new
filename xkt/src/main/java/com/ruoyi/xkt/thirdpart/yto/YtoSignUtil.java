package com.ruoyi.xkt.thirdpart.yto;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;

/**
 * @author liangyq
 * @date 2025-03-14
 */
public class YtoSignUtil {
    /**
     * 序号	步骤
     * 1	在POST时用“sign”字段进行签名验证。
     * 2	将 param+method（方法）+v（版本） 拼接得到 data，将 data和 客户密钥 拼接。（通过 控制台——接口管理，添加自己所需接口，即可得到相应的测试地址、客户编码、客户密钥、方法和版本）
     * 3	假设data内容为： opentest， partnerId（客户密钥）为123456。 则要签名的内容为opentest123456，然后对opentest123456先进行MD5加密，然后转换为base64字符串。 即经过md5(16位byte)和base64后的内容就为 YLstCNa3x8ijQx16e/jqOA==
     *
     * @return
     */
    public static String sign(String method, String version, String param, String secret) {
        String data = param + method + version;
        return sign(data, secret);
    }


    public static String sign(String data1, String data2) {
        //进行md5加密,然后对数组进行base64编码
        byte[] bytes = DigestUtil.md5(data1 + data2);
        return Base64.encodeStr(bytes, false, false);
    }

    /**
     * 假设xml内容为：<order></order> ， partnerId（客户密钥）为123456，
     * 则要签名的内容为<order></order>123456，然后对<order></order>123456先进行MD5加密，再转换为base64字符串。
     * 即经过md5(16位byte)和base64后的内容就为LghTkEmsD2tbQ3fsIBRcBg== 。
     *
     * @param digest
     * @param param
     * @param secret
     * @return
     */
    public static boolean verify(String digest, String param, String secret) {
        return StrUtil.equals(digest, sign(StrUtil.emptyIfNull(param), StrUtil.emptyIfNull(secret)));
    }
}
