package cn.util.provide;

import com.google.gson.Gson;

public class JsonUtils {

	/**
	 * 将Object转换成json字符串
	 */
	public static String object2Str(Object obj) {
		Gson gson = new Gson();
		return gson.toJson(obj);
	}
}
