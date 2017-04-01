package com.e2u.mvc.core.entity;

import java.util.Map;

import com.e2u.mvc.core.view.View;

/**
 * 视图模板对象
 * 
 * @author shell
 */
public class ModelAndView {
	private Map<String, Object> modelMap;
	private View view;
	private Boolean isJson;
	private Object jsonObj;

	public ModelAndView(Map<String, Object> modelMap, View view) {
		this.modelMap = modelMap;
		this.view = view;
	}

	public Map<String, Object> getModelMap() {
		return modelMap;
	}

	public void setModelMap(Map<String, Object> modelMap) {
		this.modelMap = modelMap;
	}

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	public Boolean getIsJson() {
		return isJson;
	}

	public void setIsJson(Boolean isJson) {
		this.isJson = isJson;
	}

	public Object getJsonObj() {
		return jsonObj;
	}

	public void setJsonObj(Object jsonObj) {
		this.jsonObj = jsonObj;
	}
	
	

}
