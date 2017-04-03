package com.e2u.mvc.core.view;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.e2u.mvc.core.entity.ModelAndView;
import com.e2u.mvc.core.util.FreemarkerUtil;

public class FreemarkerResolver implements ViewResolver {

	public void viewResolver(ServletRequest request, ServletResponse response,ModelAndView modelAndView) {
		View view = modelAndView.getView();
		FreemarkerUtil.getFreemarker().fprint(view.getName()+ view.getSuffix(),modelAndView.getModelMap(), response);
	}

}
