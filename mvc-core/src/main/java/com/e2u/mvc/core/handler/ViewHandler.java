package com.e2u.mvc.core.handler;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.e2u.mvc.core.annotation.DatePattern;
import com.e2u.mvc.core.annotation.ModelAttribute;
import com.e2u.mvc.core.annotation.RequestParam;
import com.e2u.mvc.core.annotation.ResponseBody;
import com.e2u.mvc.core.common.CommonConfig;
import com.e2u.mvc.core.entity.ModelAndView;
import com.e2u.mvc.core.entity.RequestBody;
import com.e2u.mvc.core.factory.BeanFactory;
import com.e2u.mvc.core.view.View;

import cn.util.provide.BeanUtils;
import cn.util.provide.DateUtils;
import cn.util.provide.StringUtils;

public class ViewHandler {
	
	private Logger logger = Logger.getLogger(ViewHandler.class);
	
	public ModelAndView getModelAndView(HttpServletRequest req ,HttpServletResponse res, RequestBody requestMapping) throws Exception {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//获取映射关系对应的Class对象
		Class<?> clazz = requestMapping.getClazz();
		//获取映射关系对应的方法
		Method method = requestMapping.getMethod();
		//获取方法参数的名称
		Parameter[] parameters = method.getParameters();
		//参数列表
		List<Object> values = new ArrayList<Object>(parameters.length);
		//要调用方法参数传递
		bindMethodParameter(req, res, modelMap, clazz, method, parameters,values);
		//获取方法执行的返回值
		method.setAccessible(true);//提高反射速度
		Object invoke = method.invoke(BeanFactory.getBeanByType(clazz), values.toArray());
		return wrapModelAndView(modelMap, invoke , method);
	}

	/**
	 * 包装成ModeAndView
	 */
	private ModelAndView wrapModelAndView(Map<String, Object> modelMap,Object invoke,Method mothod) {
		ModelAndView result = null;
		if(invoke instanceof ModelAndView){
			result = (ModelAndView)invoke;
		}else{
			View view = getView(invoke);
			result = new ModelAndView(modelMap, view);
		}
		if(mothod.isAnnotationPresent(ResponseBody.class)){
			result.setIsJson(true);
			result.setJsonObj(invoke);
		}
		return result;
	}
	
	/**
	 * 进行方法执行参数的绑定
	 */
	private void bindMethodParameter(HttpServletRequest req,HttpServletResponse res, Map<String, Object> modelMap,Class<?> clazz, Method method, Parameter[] parameters,List<Object> values) throws Exception {
		for(int i = 0 ; i < parameters.length ; i++){
			//获取参数类型
			Class<?> type = parameters[i].getType();
			Object paramVal = extractDefaultParam(req, res,modelMap,type);
			if(paramVal != null){
				values.add(paramVal);
				continue;
			}
			//获取含有参数RequestParam注解
			RequestParam requestParam = parameters[i].getAnnotation(RequestParam.class);
			ModelAttribute modelAttribute = parameters[i].getAnnotation(ModelAttribute.class);
			if(modelAttribute != null){
		    	Object newInstance = bindModelAttributeParam("",req, type);
		    	values.add(newInstance);
		    }else{
		    	//从RequestParam注解中获取绑定参数的名称
		    	String paramName = (requestParam == null)?"":requestParam.value();
		    	if(StringUtils.isEmpty(paramName)){
		    		try {
		    			String methodName = method.getName();
		    			boolean isStatic = Modifier.isStatic(method.getModifiers());
		    			//获取类对应方法局部变量表的映射
		    			Map<String, Map<String, List<String>>> methodParamMapping = RequestMappingHandler.getRequestMappingHandler().getMethodParamMapping();
		    			Map<String, List<String>> map = methodParamMapping.get(clazz.getName());
		    			List<String> list = map.get(methodName);
		    			if(isStatic){
		    				paramName = list.get(i + 2);
		    			}else{
		    				paramName = list.get(i + 1);
		    			}
					} catch (Exception e) {
						logger.error("方法参数名称解析异常");
					}
		    	}
		    	//参数值
		    	if(type == String[].class){
		    		paramVal = req.getParameterValues(paramName);
		    	}else if(type == List.class){
		    		paramVal = Arrays.asList(req.getParameterValues(paramName));
		    	}else{
		    		paramVal = req.getParameter(paramName);
		    	}
		    	if(paramVal==null && requestParam.required()){
		    		logger.error("需要的参数【"+paramName+"】没有传递");
		    		res.sendError(400);
		    	}
		    	values.add(paramVal);
			}
			
		}
	}

	/**
	 * 绑定modelAttributeParam对象
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 */
	private Object bindModelAttributeParam(String baseName,HttpServletRequest req, Class<?> type)throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Object newInstance = type.newInstance();
		PropertyDescriptor[] propertys = BeanUtils.getAllProperty(type, Object.class);
		String[] val = null;
		for(PropertyDescriptor property : propertys){
			Class<?> propertyType = property.getPropertyType();
			String propertyName = property.getName();
			val = req.getParameterValues((StringUtils.isEmpty(baseName)?propertyName:(baseName+"."+propertyName)));
 			if(val == null || val.length <= 0){
				val = new String[1];
				if(propertyType.isPrimitive()){
					val[0] = "0";
				}
 			}
 			Method method = BeanUtils.getPropertyWriterMethod(newInstance, Object.class, propertyName);
			if(propertyType == String.class){
				method.invoke(newInstance, val[0]);
			}else if(propertyType == Date.class){
				DatePattern datePattern = propertyType.getAnnotation(DatePattern.class);
				method.invoke(newInstance, DateUtils.str2Date(val[0], datePattern == null ? "yyyy-MM-dd" :datePattern.value()));
			}else if(propertyType == Integer.class || propertyType == int.class){
				method.invoke(newInstance,Integer.valueOf(val[0]));
			}else if(propertyType == Double.class || propertyType == double.class){
				method.invoke(newInstance,Double.valueOf(val[0]));
			}else if(propertyType == Byte.class || propertyType == byte.class){
				method.invoke(newInstance,Byte.valueOf(val[0]));
			}else if(propertyType == Boolean.class || propertyType == boolean.class){
				method.invoke(newInstance,Boolean.valueOf(val[0]));
			}else if(propertyType == Short.class || propertyType == short.class){
				method.invoke(newInstance,Short.valueOf(val[0]));
			}else if(propertyType == List.class){
				method.invoke(newInstance,Arrays.asList(val));
			}else if(propertyType == String[].class){
				method.invoke(newInstance,(Object)val);
			}else{
				method.invoke(newInstance,bindModelAttributeParam(StringUtils.isEmpty(baseName)?propertyName:(baseName+"."+propertyName),req, propertyType));
			}
		}
		return newInstance;
	}

	private View getView(Object invoke) {
		View view = new View();
		view.setName(invoke.toString());
		view.setPrefix(CommonConfig.VIEW_PRIFEX);
		view.setSuffix(CommonConfig.VIEW_SUFFIX);
		return view;
	}

	/**
	 * 提取参数类型进行初始化操作(携带HttpServletRequest,HttpServletResponse)
	 */
	private Object extractDefaultParam(HttpServletRequest req,HttpServletResponse res, Map<String,Object> modelMap , Class<?> type) {
		if(type == int.class || type == short.class || type == byte.class){
			return 0;
		}else if(type == double.class){
			return  0.0;
		}else if(type == float.class){
			return  0.0f;
		}else if(type == HttpServletRequest.class){
			return  req;
		}else if(type == HttpServletResponse.class){
			return  res;
		}else if(type == Map.class || type == HashMap.class){
			return  modelMap;
		}
		return null;
	}
}
