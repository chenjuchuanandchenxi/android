package com.qdzq.comm;
import java.security.Key;  
import javax.crypto.Cipher;  
import javax.crypto.SecretKeyFactory;  
import javax.crypto.spec.DESedeKeySpec;  
import javax.crypto.spec.IvParameterSpec;

public class DataDesUtil {
	// 密钥:加密和解密必须知道这个,必须是8的倍数
	private final static String secretKey = "chenjuchuan@lx100$#365#$";
	// 向量:8位
	private final static String iv = "01234567";
	// 加解密统一使用的编码方式
	private final static String encoding = "utf-8";
	
	public static final String ALGORITHM_DES = "desede/CBC/PKCS5Padding";
	/*3DES加密*/
	public static String encode(String plainText) throws Exception {
		Key deskey = null;
		DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
		SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
		
		deskey = keyfactory.generateSecret(spec);

		Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
		IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
		cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
		byte[] encryptData = cipher.doFinal(plainText.getBytes(encoding));
		return DataDesBase64.encode(encryptData);
	}
	/*3DES解密*/
	public static String decode(String encryptText) throws Exception {
		Key deskey = null;
		DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
		SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
		deskey = keyfactory.generateSecret(spec);
		Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
		IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
		cipher.init(Cipher.DECRYPT_MODE, deskey, ips);

		byte[] decryptData = cipher.doFinal(DataDesBase64.decode(encryptText));

		return new String(decryptData, encoding);
	}
}
