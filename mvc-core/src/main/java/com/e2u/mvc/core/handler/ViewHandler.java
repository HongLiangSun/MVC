package com.e2u.mvc.core.handler;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import cn.util.provide.StringUtils;

import com.e2u.mvc.core.annotation.ModelAttribute;
import com.e2u.mvc.core.annotation.RequestParam;
import com.e2u.mvc.core.annotation.ResponseBody;
import com.e2u.mvc.core.common.CommonConfig;
import com.e2u.mvc.core.entity.ModelAndView;
import com.e2u.mvc.core.entity.RequestBody;
import com.e2u.mvc.core.factory.BeanFactory;
import com.e2u.mvc.core.view.View;

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
		    		res.sendError(400,"需要的参数【"+paramName+"】没有传递");
		    	}
		    	values.add(paramVal);
			}
			
		}
	}

	/**
	 * 绑定modelAttributeParam对象
	 */
	private Object bindModelAttributeParam(String baseName,HttpServletRequest req, Class<?> type)throws InstantiationException, IllegalAccessException {
		Object newInstance = null;
		Field[] fields = type.getDeclaredFields();
		String[] val = {};
		newInstance = type.newInstance();
		for(Field f : fields){
			f.setAccessible(true);
			String name = f.getName();
			val = req.getParameterValues((StringUtils.isEmpty(baseName)?name:(baseName+"."+name)));
 			val = (val == null) ? new String[1] :val;
			Class<?> fileType = f.getType();
			if(fileType == String.class){
				f.set(newInstance,val[0]);
			}else if(fileType == Integer.class || fileType == int.class){
				f.set(newInstance,Integer.valueOf(val[0]));
			}else if(fileType == Double.class || fileType == double.class){
				f.set(newInstance,Double.valueOf(val[0]));
			}else if(fileType == Byte.class || fileType == byte.class){
				f.set(newInstance,Byte.valueOf(val[0]));
			}else if(fileType == Boolean.class || fileType == boolean.class){
				f.set(newInstance,Boolean.valueOf(val[0]));
			}else if(fileType == Short.class || fileType == short.class){
				f.set(newInstance,Short.valueOf(val[0]));
			}else if(fileType == List.class){
				f.set(newInstance, Arrays.asList(val));
			}else if(fileType == String[].class){
				f.set(newInstance, val);
			}else{
				f.set(newInstance,bindModelAttributeParam(StringUtils.isEmpty(baseName)?name:(baseName+"."+name),req, fileType));
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
