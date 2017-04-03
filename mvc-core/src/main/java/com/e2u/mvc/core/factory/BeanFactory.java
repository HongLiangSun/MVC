package com.e2u.mvc.core.factory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;

import cn.util.provide.ClassUtils;

import com.e2u.mvc.core.annotation.Autowired;
import com.e2u.mvc.core.annotation.Component;
import com.e2u.mvc.core.annotation.Controller;
import com.e2u.mvc.core.annotation.Service;
import com.e2u.mvc.core.common.CommonConfig;

/**
 * Bean工厂类
 * 
 * @author hongliang.sun
 */
public class BeanFactory {
	private static Logger logger = Logger.getLogger(BeanFactory.class);
	private static Map<Class<?>, Object> beans = new HashMap<Class<?>, Object>();
	/**
	 * 根据类型从
	 */
	public static Object getBeanByType(Class<?> clazz) {
		return beans.get(clazz);
	}

	/**
	 * 初始化Bean工厂
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	@SuppressWarnings("unchecked")
	public static void initBeans() throws ClassNotFoundException, IOException, InstantiationException, IllegalAccessException {
		logger.info("初始化BeansFactory start ....");
		//解析注解中的Beans
		Set<Class<?>> classSet = ClassUtils.getAllClassesByPackage(CommonConfig.BASE_PACKAGE, Controller.class,Component.class,Service.class);
		for(Class<?> clazz : classSet){
			if(beans.get(clazz) == null){
				beans.put(clazz, clazz.newInstance());
				logger.info("add bean" + clazz);
			}
		}
		injectFieldByType();
		logger.info("初始化BeansFactory end。");
	}
	
	/**
	 * 进行依赖注入,实现bean工厂的初始化
	 */
	private static void injectFieldByType(){
		for(Iterator<Entry<Class<?>, Object>> iterator = beans.entrySet().iterator();iterator.hasNext();){
			Entry<Class<?>, Object> next = iterator.next();
			Class<?> clazz = next.getKey();
			Object object = next.getValue();
			Field[] fields = clazz.getDeclaredFields();
			for(Field field : fields){
				if(field.isAnnotationPresent(Autowired.class)){
					field.setAccessible(true);
					try {
						Object fieldBean = getFieldBean(field);
						if(fieldBean == null) throw new ClassNotFoundException("没有找到" + field.getClass());
						field.set(object, fieldBean);
					} catch (Exception e) {
						throw new RuntimeException("属性赋值异常");
					}
				}
			}
		}
	}

	/**
	 * 根据字段类型，从常量池中取出来
	 */
	private static Object getFieldBean(Field field) {
		Class<?> fileldClass = field.getType();
		Object obj = beans.get(fileldClass);
		int i = 0;
		if(obj == null){
			for(Iterator<Entry<Class<?>, Object>> iterator = beans.entrySet().iterator();iterator.hasNext();){
				Entry<Class<?>, Object> next = iterator.next();
				Object val = next.getValue();
				if(fileldClass.isInstance(val)){
					obj = val;
					i++;
					if(i > 1){
						throw new RuntimeException(fileldClass+"查找到多个对应");
					}
				}
			}
		}
		return obj;
	}
}
