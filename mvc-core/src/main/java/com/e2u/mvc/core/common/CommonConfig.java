package com.e2u.mvc.core.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.e2u.mvc.core.annotation.Controller;

/**
 * 
 * @author shell
 * @Desc:获取mvc一些系统配置
 */
@Controller
public class CommonConfig {
	// 获取mvc扫描的基包
	public static String BASE_PACKAGE;
	// 视图前缀
	public static String VIEW_PRIFEX;
	// 视图后缀
	public static String VIEW_SUFFIX;
	// 视图解析器类
	public static String VIEW_RESOLVER;
	//静态资源路径(例如：html,js,image,css)
	public static String STATIC_RESOURCE;

	public static final String BASE_PACKAGE_KEY = "e2u.mvc.compan.basepackage";
	public static final String VIEW_PRIFEX_KEY = "e2u.mvc.view.prefix";
	public static final String VIEW_SUFFIXE_KEY = "e2u.mvc.view.suffix";
	public static final String VIEW_RESOLVER_KEY = "e2u.mvc.view.resolver";
	public static String STATIC_RESOURCE_KEY = "e2u.mvc.static.resources";

	private static Logger log = Logger.getLogger(CommonConfig.class);

	public static void initConfig() {
		InputStream resourceAsStream = null;
		try {
			log.info("读取【dispatcher.properties】配置文件内容start...");
			resourceAsStream = CommonConfig.class.getClassLoader().getResourceAsStream("dispatcher.properties");
			Properties properties = new Properties();
			properties.load(resourceAsStream);
			BASE_PACKAGE = properties.getProperty(BASE_PACKAGE_KEY, "");
			VIEW_PRIFEX = properties.getProperty(VIEW_PRIFEX_KEY, "");
			VIEW_SUFFIX = properties.getProperty(VIEW_SUFFIXE_KEY, "");
			VIEW_RESOLVER = properties.getProperty(VIEW_RESOLVER_KEY, "");
			STATIC_RESOURCE=properties.getProperty(STATIC_RESOURCE_KEY,"");
			log.info("读取配置文件内容结束。");
		} catch (Exception e) {
			log.error("配置文件读取异常【检查是否含有dispatcher.properties】", e);
		} finally {
			if (resourceAsStream != null) {
				try {
					resourceAsStream.close();
				} catch (IOException e) {
					log.error("配置文件流关闭异常", e);
				}
			}
		}
	}
}
