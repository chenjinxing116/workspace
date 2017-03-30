package com.gosun.core.utils.security;

import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * DES算法加解密工具类
 * @author wx
 *
 */
public class DESUtils {
	private Key key;
	private byte[] byteMi = null;
	private byte[] byteMing = null;

	
	/**
	 * 根据参数密钥生成KEY
	 * @param strKey 密钥参数 
	 */
	public void setKey(String strKey) {
		try {
			KeyGenerator _generator = KeyGenerator.getInstance("DES");
			_generator.init(new SecureRandom(strKey.getBytes()));
			this.key = _generator.generateKey();
			_generator = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 用 DES 算法加密密码
	 * @param strDecrypt 密码字符串
	 * @return 返回加密后的密码字符串
	 */
	public String getEncrypt(String strDecrypt) {
		BASE64Encoder base64en = new BASE64Encoder();
		String strEncrypt ="";
		try {
			this.byteMing = strDecrypt.getBytes("UTF8");
			this.byteMi = this.getEncCode(this.byteMing);
			strEncrypt = base64en.encode(this.byteMi);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			this.byteMing = null;
			this.byteMi = null;
		}
		return strEncrypt;
	}

	/**
	 * 用 DES 算法加密密码
	 * @param byteS 密码字节数组
	 * @return 返回加密后的密码字节数组
	 */
	private byte[] getEncCode(byte[] byteS) {
		byte[] byteFina = null;
		Cipher cipher;
		try {
			cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byteFina = cipher.doFinal(byteS);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cipher = null;
		}

		return byteFina;
	}

	/**
	 * DES 解密
	 * @param strEncrypt 待解密字符串
	 * @return 返回解密后的字符串
	 */
	public String getDecrypt(String strEncrypt) {
		BASE64Decoder base64De = new BASE64Decoder();
		String strDecrypt ="";
		try {
			this.byteMi = base64De.decodeBuffer(strEncrypt);
			this.byteMing = this.getDesCode(byteMi);
			strDecrypt = new String(byteMing, "UTF8");
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			base64De = null;
			byteMing = null;
			byteMi = null;
		}
		return strDecrypt;
	}

	/**
	 * DES 解密
	 * @param byteD 待解密字节数组
	 * @return 返回解密后的字节数组
	 */
	private byte[] getDesCode(byte[] byteD) {
		Cipher cipher;
		byte[] byteFina = null;
		try {
			cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.DECRYPT_MODE, key);
			byteFina = cipher.doFinal(byteD);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cipher = null;
		}
		return byteFina;
	}

	public static void main(String[] args) {
		DESUtils des = new DESUtils();
		des.setKey("GOSUN");
		String enStr = des.getEncrypt("wuxiao");
		System.out.println(enStr);
		System.out.println(des.getDecrypt("3ZfSmcI+OC4="));
	}
}