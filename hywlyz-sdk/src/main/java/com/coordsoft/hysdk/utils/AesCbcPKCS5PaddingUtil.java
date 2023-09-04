package com.coordsoft.hysdk.utils;

import com.coordsoft.commons.codec.binary.Base64;
import com.coordsoft.commons.codec.digest.DigestUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class AesCbcPKCS5PaddingUtil {
    private final static int AES_KEY_LENGTH = 16;//密钥长度16字节，128位
    private final static String AES_ALGORITHM = "AES";//算法名字
    private final static String AES_TRANSFORMATION = "AES/CBC/PKCS5Padding";//算法/模式/填充
    private final static String AES_STRING = "abcdefghijklmnopqrstuvwxyzABCDEFGHIGKLOP";
    private final static Charset UTF_8 = StandardCharsets.UTF_8;//编码格式

    /**
     * 使用AES加密
     *
     * @param aesKey AES Key
     * @param data   被加密的数据
     * @return AES加密后的数据
     */
    public static String encode(String data, String aesKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        if (aesKey == null) {
            return data;
        }
        if (aesKey.getBytes(UTF_8).length != AES_KEY_LENGTH) {
            aesKey = DigestUtils.md5Hex(aesKey).substring(8, 24);
        }
        SecretKeySpec keySpec = new SecretKeySpec(aesKey.getBytes(UTF_8), AES_ALGORITHM);
        Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
        String ivStr = DigestUtils.md5Hex(aesKey).substring(0, 16);
        IvParameterSpec iv = new IvParameterSpec(ivStr.getBytes(UTF_8));
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
        return Base64.encodeBase64String(cipher.doFinal(data.getBytes(UTF_8)));
    }

    /**
     * 使用AES解密
     *
     * @param aesKey AES Key
     * @param data   被解密的数据
     * @return AES解密后的数据
     */
    public static String decode(String data, String aesKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        if (aesKey == null) {
            return data;
        }
        if (aesKey.getBytes(UTF_8).length != AES_KEY_LENGTH) {
            aesKey = DigestUtils.md5Hex(aesKey).substring(8, 24);
        }
        byte[] decodeBase64 = Base64.decodeBase64(data.getBytes(UTF_8));
        SecretKeySpec keySpec = new SecretKeySpec(aesKey.getBytes(UTF_8), AES_ALGORITHM);
        Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
        String ivStr = DigestUtils.md5Hex(aesKey).substring(0, 16);
        IvParameterSpec iv = new IvParameterSpec(ivStr.getBytes(UTF_8));
        cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);
        return new String(cipher.doFinal(decodeBase64), UTF_8);
    }

}
