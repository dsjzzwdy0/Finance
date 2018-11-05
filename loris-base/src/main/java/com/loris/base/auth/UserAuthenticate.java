package com.loris.base.auth;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Base64.Encoder;

import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.loris.base.bean.User;

public class UserAuthenticate
{
	private static final String CHARSET_NAME = "utf-8";
	
	private static final String hexDigIts[] = { 
			"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D",
			"E", "F" };
	
	/**
	 * MD5算法加密
	 * @param origin
	 * @return
	 */
	public static String encodeMD5(String origin)
	{
		return encodeMD5(origin, CHARSET_NAME);
	}
	
	/**
	 * MD5加密
	 * 
	 * @param origin
	 *            字符
	 * @param charsetname
	 *            编码
	 * @return
	 */
	public static String encodeMD5(String origin, String charsetname)
	{
		String resultString = null;
		try
		{
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			if (StringUtils.isEmpty(charsetname))
			{
				resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
			}
			else
			{
				resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetname)));
			}
		}
		catch (Exception e)
		{
		}
		return resultString;
	}

	public static String byteArrayToHexString(byte b[])
	{
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++)
		{
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	public static String byteToHexString(byte b)
	{
		int n = b;
		if (n < 0)
		{
			n += 256;
		}
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigIts[d1] + hexDigIts[d2];
	}

	/**
	 * 加密数据
	 * 
	 * @param password
	 * @param type
	 * @return
	 */
	public static String encode(String password, String type)
			throws NoSuchAlgorithmException, UnsupportedEncodingException
	{
		return encoderByMd5(password);
	}

	/**
	 * 验证密码的正确与否
	 * 
	 * @param user
	 * @param password
	 * @return
	 */
	public static boolean verifyPassword(User user, String password)
			throws NoSuchAlgorithmException, UnsupportedEncodingException
	{
		String pwd = encodeMD5(password);
		//System.out.println("User: " + user.getUsername() + ", pwd: " + user.getPassword() + ", Verify Pwd: " + pwd);
		if (pwd.equals(user.getPassword()))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * 给用户的密码加密
	 * @param user
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static void encodePassword(User user) throws NoSuchAlgorithmException, UnsupportedEncodingException
	{
		user.setPassword(encodeMD5(user.getPassword()));
	}

	/**
	 * 加密数据
	 * 
	 * @param str
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static String encoderByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException
	{
		// 确定计算方法
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		Encoder encoder = Base64.getEncoder();
		// 加密后的字符串
		String newstr = encoder.encodeToString(md5.digest(str.getBytes("utf-8")));
		return newstr;
	}
}
