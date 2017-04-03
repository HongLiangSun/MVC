package com.e2u.mvc.core.view;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import cn.util.provide.StringUtils;

import com.e2u.mvc.core.entity.ModelAndView;

/**
 * 本地试图解析器
 * 
 * @author shell
 *
 */
public class LocationResolver implements ViewResolver {

	@Override
	public void viewResolver(ServletRequest request, ServletResponse response,ModelAndView modelAndView){
		View view = modelAndView.getView();
		Map<String, Object> modelMap = modelAndView.getModelMap();
		// 遍历modelMap将将值存储到request域对象中
		modelMap.forEach((key,value) -> request.setAttribute(key, modelMap.get(key)));
		// 获取物理视图路径
		String resourcePath = getResourcePath(view);
		try {
			request.getRequestDispatcher(resourcePath).forward(request, response);
		} catch (ServletException | IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取物理视图路径
	 */
	private String getResourcePath(View view) {
		String prefix = "";
		String name = "";
		String suffix = "";
		if (!StringUtils.isEmpty(view.getPrefix())) {
			prefix = view.getPrefix();
		}
		if (!StringUtils.isEmpty(view.getName())) {
			name = view.getName();
		}
		if (!StringUtils.isEmpty(view.getSuffix())) {
			suffix = view.getSuffix();
		}
		return prefix + name + suffix;
	}

}
