package cn.util.provide;

import java.io.ByteArrayOutputStream;

/**
 * 处理字节工具
 */
public class ByteUtils {
	private static String hexStr = "0123456789ABCDEF";

	/**
	 * 将16进制的数据装换成10进制的数据
	 */
	public static int parseStr2Int10(String str) {
		return Integer.parseInt(str, 16);
	}

	/**
	 * 将2进制字节数组装换成16进制字符串
	 */
	public static String bin2HexStr(byte[] bytes, int len) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < len; i++) {
			//高四位转换
			sb.append(hexStr.charAt((bytes[i] & 0xF0) >> 4));
			//低四位转换
			sb.append(hexStr.charAt(bytes[i] & 0x0F));
		}
		return sb.toString();
	}

	/**
	 * 将16进制的字符串解码
	 */
	public static String decode(String bytes) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(bytes.length() >> 1);
		for (int i = 0; i < bytes.length(); i += 2)
			baos.write((hexStr.indexOf(bytes.charAt(i)) << 4 | hexStr.indexOf(bytes.charAt(i + 1))));
		return new String(baos.toByteArray());
	}
}
