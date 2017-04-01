package com.e2u.mvc.core.view;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.e2u.mvc.core.entity.ModelAndView;
import com.e2u.mvc.core.util.FreemarkerUtil;

public class FreemarkerResolver extends ViewResolver {

	@Override
	public void viewResolver(ServletRequest request, ServletResponse response,ModelAndView modelAndView) {
		FreemarkerUtil.getFreemarker().fprint(modelAndView.getView().getName()+ modelAndView.getView().getSuffix(),modelAndView.getModelMap(), response);
	}

}
