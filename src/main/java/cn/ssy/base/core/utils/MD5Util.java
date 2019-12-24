package cn.ssy.base.core.utils;

import java.security.MessageDigest;

/**
 * 
 * <p>
 * 文件功能说明： md5相关处理
 * </p>
 * 
 * @Author sunshaoyu
 *         <p>
 *         <li>2019年3月7日-下午3:58:11</li>
 *         <li>修改记录</li>
 *         <li>-----------------------------------------------------------</li>
 *         <li>标记：修订内容</li>
 *         <li>2019年3月7日-sunshaoyu：创建注释模板</li>
 *         <li>-----------------------------------------------------------</li>
 *         </p>
 */
public class MD5Util {

	private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "A", "B", "C",
			"D", "F", "E", "G", "H", "I", "J", "K" };

	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年3月7日-下午3:58:33</li>
	 *         <li>功能说明：byte数组转十六进制</li>
	 *         </p>
	 * @param b
	 * @return
	 */
	private static String byteArrayToHexString(byte b[]) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++)
			resultSb.append(byteToHexString(b[i]));

		return resultSb.toString();
	}

	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年3月7日-下午3:58:55</li>
	 *         <li>功能说明：byte转十六进制</li>
	 *         </p>
	 * @param b
	 * @return
	 */
	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n += 256;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	public static String MD5Encode(String origin) {
		if (null == origin) {
			return null;
		}
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			resultString = byteArrayToHexString(md.digest(resultString
					.getBytes()));

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return resultString;

	}
}
