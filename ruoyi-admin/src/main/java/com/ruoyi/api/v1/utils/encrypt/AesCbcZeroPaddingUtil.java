package com.ruoyi.api.v1.utils.encrypt;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

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

public class AesCbcZeroPaddingUtil {
	private final static int AES_KEY_LENGTH = 16;//密钥长度16字节，128位
	private final static String AES_ALGORITHM = "AES";//算法名字
	private final static String AES_TRANSFORMATION = "AES/CBC/NoPadding";//算法/模式/填充
	private final static Charset UTF_8 = StandardCharsets.UTF_8;//编码格式

	/**
	 * AES加密，CBC模式，zeropadding填充，偏移量为key
	 */

	// 加密
	public static String encode(String data, String aesKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		if (aesKey == null) {
			return data;
		}
		if (aesKey.getBytes().length != AES_KEY_LENGTH) {
			aesKey = DigestUtils.md5Hex(aesKey).substring(8, 24);
		}
		String ivStr = DigestUtils.md5Hex(aesKey).substring(0, 16);
		// 偏移量
		byte[] iv = ivStr.getBytes(UTF_8);
		Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
		int blockSize = cipher.getBlockSize();
		byte[] dataBytes = data.getBytes();
		int length = dataBytes.length;
		// 计算需填充长度
		if (length % blockSize != 0) {
			length = length + (blockSize - (length % blockSize));
		}
		byte[] plaintext = new byte[length];
		// 填充
		System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
		SecretKeySpec keySpec = new SecretKeySpec(aesKey.getBytes(), AES_ALGORITHM);
		// 设置偏移量参数
		IvParameterSpec ivSpec = new IvParameterSpec(iv);
		cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
		byte[] encryped = cipher.doFinal(plaintext);
		return Base64.encodeBase64String(encryped);
	}

	// 加密
	public static String encodeSafe(String data, String aesKey) throws InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, NoSuchAlgorithmException {
		if (aesKey == null) {
			return data;
		}
		if (aesKey.getBytes().length != AES_KEY_LENGTH) {
			aesKey = DigestUtils.md5Hex(aesKey).substring(8, 24);
		}
		String ivStr = DigestUtils.md5Hex(aesKey).substring(0, 16);
		// 偏移量
		byte[] iv = ivStr.getBytes(UTF_8);
		Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
		int blockSize = cipher.getBlockSize();
		byte[] dataBytes = data.getBytes();
		int length = dataBytes.length;
		// 计算需填充长度
		if (length % blockSize != 0) {
			length = length + (blockSize - (length % blockSize));
		}
		byte[] plaintext = new byte[length];
		// 填充
		System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
		SecretKeySpec keySpec = new SecretKeySpec(aesKey.getBytes(), AES_ALGORITHM);
		// 设置偏移量参数
		IvParameterSpec ivSpec = new IvParameterSpec(iv);
		cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
		byte[] encryped = cipher.doFinal(plaintext);
		return Base64.encodeBase64URLSafeString(encryped);
	}

	// 解密
	public static String decode(String data, String aesKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		if (aesKey == null) {
			return data;
		}
		if (aesKey.getBytes().length != AES_KEY_LENGTH) {
			aesKey = DigestUtils.md5Hex(aesKey).substring(8, 24);
		}
		String ivStr = DigestUtils.md5Hex(aesKey).substring(0, 16);
		// 偏移量
		byte[] iv = ivStr.getBytes(UTF_8);
		byte[] encryp = Base64.decodeBase64(data);
		Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
		SecretKeySpec keySpec = new SecretKeySpec(aesKey.getBytes(), AES_ALGORITHM);
		IvParameterSpec ivSpec = new IvParameterSpec(iv);
		cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
		byte[] original = cipher.doFinal(encryp);
		return new String(original).replace("\0", "");
	}

	// 测试
	public static void main(String[] args) {
		String data = "12345 6 ";
		String key = "test";
		System.out.println("密码：" + DigestUtils.md5Hex(key).substring(8, 24));
		System.out.println("IV：" + DigestUtils.md5Hex(key).substring(0, 16));
		try {
			String encrypt = encode(data, key);
			String decrypt = decode(encrypt, key);
			System.out.println("加密后:" + encrypt);
			System.out.println("解密后:" + decrypt);
		} catch (NoSuchPaddingException | IllegalBlockSizeException | InvalidKeyException | InvalidAlgorithmParameterException | NoSuchAlgorithmException | BadPaddingException e) {
			e.printStackTrace();
		}
	}
}
