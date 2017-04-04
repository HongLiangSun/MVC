package com.e2u.mvc.core.factory;

import com.e2u.mvc.core.view.FreemarkerResolver;
import com.e2u.mvc.core.view.LocationResolver;
import com.e2u.mvc.core.view.ViewResolver;

/**
 * 创建视图解析器的静态工厂类
 * @author shell
 */
public class ViewResolverFactory {
	public static final String LOCAL_VIEW_RESOLVER_NAME = "localViewResolver";
	public static final String FREMARKER_VIEW_RESOLVER_NAME = "fremarkerViewResolver";
	/**
	 * 根据视图解析器名称来创建对应的视图解析器
	 */
	public static ViewResolver getViewResolver(String viewResolverName) {
		if(LOCAL_VIEW_RESOLVER_NAME.equals(viewResolverName)){
			return new LocationResolver();
		}else if(FREMARKER_VIEW_RESOLVER_NAME.equals(viewResolverName)){
			return new FreemarkerResolver();
		}
		return null;
	}
}
