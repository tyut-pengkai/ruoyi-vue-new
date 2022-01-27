//package com.ruoyi.api.v1.utils.encrypt;
//
//import org.apache.commons.codec.binary.Base64;
//import org.apache.commons.codec.digest.DigestUtils;
//
//import javax.crypto.BadPaddingException;
//import javax.crypto.Cipher;
//import javax.crypto.IllegalBlockSizeException;
//import javax.crypto.NoSuchPaddingException;
//import javax.crypto.spec.IvParameterSpec;
//import javax.crypto.spec.SecretKeySpec;
//import java.nio.charset.Charset;
//import java.nio.charset.StandardCharsets;
//import java.security.InvalidAlgorithmParameterException;
//import java.security.InvalidKeyException;
//import java.security.NoSuchAlgorithmException;
//
//public class AesCbcNoPaddingUtil {
//    private final static int AES_KEY_LENGTH = 16;//密钥长度16字节，128位
//    private final static String AES_ALGORITHM = "AES";//算法名字
//    private final static String AES_TRANSFORMATION = "AES/CBC/NoPadding";//算法/模式/填充
//    private final static String AES_STRING = "abcdefghijklmnopqrstuvwxyzABCDEFGHIGKLOP";
//    private final static Charset UTF_8 = StandardCharsets.UTF_8;//编码格式
//
//    /**
//     * 使用AES加密
//     *
//     * @param aesKey AES Key
//     * @param data   被加密的数据
//     * @return AES加密后的数据
//     * @throws NoSuchPaddingException
//     * @throws NoSuchAlgorithmException
//     * @throws InvalidAlgorithmParameterException
//     * @throws InvalidKeyException
//     * @throws BadPaddingException
//     * @throws IllegalBlockSizeException
//     */
//    public static String encode(String data, String aesKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
//        if (aesKey == null) {
//            return data;
//        }
//        if (aesKey.getBytes().length != AES_KEY_LENGTH) {
//            aesKey = DigestUtils.md5Hex(aesKey).substring(8, 24);
//        }
//        SecretKeySpec keySpec = new SecretKeySpec(aesKey.getBytes(), AES_ALGORITHM);
//        Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
//        String ivStr = DigestUtils.md5Hex(aesKey).substring(0, 16);
//        IvParameterSpec iv = new IvParameterSpec(ivStr.getBytes(UTF_8));
//        cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
//        return Base64.encodeBase64String(cipher.doFinal(data.getBytes(UTF_8)));
//    }
//
//    /**
//     * 使用AES加密
//     *
//     * @param aesKey AES Key
//     * @param data   被加密的数据
//     * @return AES加密后的数据
//     * @throws NoSuchPaddingException
//     * @throws NoSuchAlgorithmException
//     * @throws InvalidAlgorithmParameterException
//     * @throws InvalidKeyException
//     * @throws BadPaddingException
//     * @throws IllegalBlockSizeException
//     */
//    public static String encodeSafe(String data, String aesKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
//        if (aesKey == null) {
//            return data;
//        }
//        if (aesKey.getBytes().length != AES_KEY_LENGTH) {
//            aesKey = DigestUtils.md5Hex(aesKey).substring(8, 24);
//        }
//        SecretKeySpec keySpec = new SecretKeySpec(aesKey.getBytes(), AES_ALGORITHM);
//        Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
//        String ivStr = DigestUtils.md5Hex(aesKey).substring(0, 16);
//        IvParameterSpec iv = new IvParameterSpec(ivStr.getBytes(UTF_8));
//        cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
//        return Base64.encodeBase64URLSafeString(cipher.doFinal(data.getBytes(UTF_8)));
//    }
//
//    /**
//     * 使用AES解密
//     *
//     * @param aesKey AES Key
//     * @param data   被解密的数据
//     * @return AES解密后的数据
//     * @throws NoSuchPaddingException
//     * @throws NoSuchAlgorithmException
//     * @throws InvalidAlgorithmParameterException
//     * @throws InvalidKeyException
//     * @throws BadPaddingException
//     * @throws IllegalBlockSizeException
//     */
//    public static String decode(String data, String aesKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
//        if (aesKey == null) {
//            return data;
//        }
//        if (aesKey.getBytes().length != AES_KEY_LENGTH) {
//            aesKey = DigestUtils.md5Hex(aesKey).substring(8, 24);
//        }
//        byte[] decodeBase64 = Base64.decodeBase64(data.getBytes());
//        SecretKeySpec keySpec = new SecretKeySpec(aesKey.getBytes(), AES_ALGORITHM);
//        Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
//        String ivStr = DigestUtils.md5Hex(aesKey).substring(0, 16);
//        IvParameterSpec iv = new IvParameterSpec(ivStr.getBytes(UTF_8));
//        cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);
//        return new String(cipher.doFinal(decodeBase64), UTF_8);
//    }
//
//    private static int getRandom(int count) {
//        return (int) Math.round(Math.random() * (count));
//    }
//
//    /**
//     * 生成AES key
//     *
//     * @return AES key
//     */
//    public static String genAESKey() {
//        StringBuilder sb = new StringBuilder();
//        int len = AES_STRING.length();
//        for (int i = 0; i < AES_KEY_LENGTH; i++) {
//            sb.append(AES_STRING.charAt(getRandom(len - 1)));
//        }
//        return sb.toString();
//    }
//}
