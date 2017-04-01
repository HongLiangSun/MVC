package com.e2u.mvc.core.view;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.e2u.mvc.core.entity.ModelAndView;

/**
 * 试图解析器抽象类
 * 
 * @author shell
 */
abstract class ViewResolver {

	/**
	 * 将逻辑视图渲染成物理视图
	 */
	public abstract void viewResolver(ServletRequest request,ServletResponse response, ModelAndView modelAndView);

}
