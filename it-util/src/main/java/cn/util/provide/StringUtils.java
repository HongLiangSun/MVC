package cn.util.provide;

/**
 * 字符串操作工具
 * 
 * @author hongliang.sun
 */
public class StringUtils {

	/**
	 * 判断字符串是否为空
	 */
	public static boolean isEmpty(String str) {
		return (str == null || "".equals(str))?true:false;
	}
	
	/**
	 * 获取文件扩展名
	 */
	public static String getFileExtendName(String fileAbsolutePath) {
		return fileAbsolutePath.substring(fileAbsolutePath.indexOf(".") + 1);
	}
}
