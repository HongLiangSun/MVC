package cn.util.provide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期处理工具累
 * @author hongliang.sun
 */
public class DateUtils {
	private static SimpleDateFormat simpleDateFormat;
	public static Date str2Date(String dateStr , String pattern){
		if(StringUtils.isEmpty(dateStr)) return null;
		simpleDateFormat = new SimpleDateFormat(pattern);
		try {
			return simpleDateFormat.parse(dateStr);
		} catch (ParseException e) {
			return null;
		}
	}
}
