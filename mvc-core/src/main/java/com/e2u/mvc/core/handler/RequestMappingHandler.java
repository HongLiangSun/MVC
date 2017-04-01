package com.e2u.mvc.core.handler;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import cn.util.provide.ClassUtils;

import com.e2u.mvc.core.annotation.Controller;
import com.e2u.mvc.core.annotation.RequestMapping;
import com.e2u.mvc.core.common.CommonConfig;
import com.e2u.mvc.core.common.RequestType;
import com.e2u.mvc.core.entity.RequestBody;
import com.e2u.mvc.core.entity.RequestWrap;

/**
 * @author shell
 * 处理RequestMapping注解类
 */
public class RequestMappingHandler {
	
	//请求的映射集合
	private final Map<RequestWrap , RequestBody> requestMapping = new HashMap<RequestWrap , RequestBody>();
	
	//方法参数对应关系
	private final Map<String,Map<String, List<String>>> methodParamMapping = new HashMap<String, Map<String, List<String>>>();
	
	private static RequestMappingHandler requestMappingHandler = new RequestMappingHandler();
	
	private Logger log = Logger.getLogger(RequestMappingHandler.class);
	
	private RequestMappingHandler() {}
	
	public static RequestMappingHandler getRequestMappingHandler(){
		return requestMappingHandler;
	}
	
	
	/**
	 * 扫描特定注解
	 */
	public void initMapping() {
		try {
			log.info("初始化映射关系start...");
			//获取所有的类
			Map<File,Class<?>> classMap = ClassUtils.getAllClassesMapByPackage(CommonConfig.BASE_PACKAGE);
			for(Iterator<Entry<File, Class<?>>> iterator = classMap.entrySet().iterator(); iterator.hasNext() ;){
				Entry<File, Class<?>> entry = iterator.next();
				Class<?> clazz = entry.getValue();
				File classFile = entry.getKey();
				//获取Contoller的所有method
				Method[] methods = clazz.getMethods();
				boolean controller = clazz.isAnnotationPresent(Controller.class);
				if(!controller) continue;
				RequestMapping controllerRequestMapping = clazz.getAnnotation(RequestMapping.class);
				String controllerRequestVal = controllerRequestMapping != null ? controllerRequestMapping.value() : "";
				for(Method method : methods){
					//过滤出RquestMapping注解
					handlerMapping(clazz,method,controllerRequestVal);
				}
				Map<String, List<String>> methodMap = ClassUtils.getMethodMap(classFile);
				methodParamMapping.put(clazz.getName(), methodMap);
			}
			log.info("初始化映射关系end。");
		} catch (Exception e) {
			log.error("类读取发成异常", e);
		}
	}
	
	/**
	 * 处理请求映射关系放到Map中
	 * @param clazz
	 * @param method
	 */
	private void handlerMapping(Class<?> clazz, Method method,String controllerRequestVal) {
		if(method.isAnnotationPresent(RequestMapping.class)){
			parse2RequestMapping(clazz, method,controllerRequestVal);
		}
	}

	private void parse2RequestMapping(Class<?> clazz, Method method,String controllerRequestVal) {
		//获取RquestMethod注解
		RequestMapping annotation = method.getAnnotation(RequestMapping.class);
		String path = parameterPath(controllerRequestVal,annotation.value());
		//获取请求方式GET,POST,PUT,DELETE
		RequestType requestType = annotation.method();
		RequestBody value = new RequestBody(clazz,method);
		if(requestType.name().equals(RequestType.ALL.name())){
			for(RequestType type : RequestType.values()){
				if(type.name().equals(RequestType.ALL.name())) continue;
				RequestWrap requestWrapper = new RequestWrap(type, path);
				if(requestMapping.get(requestWrapper) != null){
					throw new RuntimeException("映射路径["+path+"]重复");
				}
				requestMapping.put(requestWrapper, value);
				log.info(requestWrapper);
			}
		}else{
			RequestWrap requestWrapper = new RequestWrap(requestType, path);
			if(requestMapping.get(requestWrapper) != null){
				throw new RuntimeException("映射路径["+path+"]重复");
			}
			requestMapping.put(requestWrapper, value);
			log.info(requestWrapper);
		}
	}

	private String parameterPath(String controllerRequestVal, String value) {
		StringBuilder sb = new StringBuilder();
		//去除尾部‘/’
		String tempResStr = controllerRequestVal + value;
		if(tempResStr.endsWith("/")){
			sb.append(tempResStr.substring(0, tempResStr.lastIndexOf('/')));
		}else{
			sb.append(tempResStr);
		}
		return sb.toString();
	}

	public Map<RequestWrap, RequestBody> getRequestMapping() {
		return requestMapping;
	}

	public Map<String, Map<String, List<String>>> getMethodParamMapping() {
		return methodParamMapping;
	}

}
