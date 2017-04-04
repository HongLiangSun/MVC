package cn.util.provide;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

/**
 * javaBean的操作工具
 * @author honglinag.sun
 *
 */
public class BeanUtils {
	/**
	 * 根据bean对象和属性名称设置对应的值
	 * @param bean
	 * @param stopClass
	 * @param propertyName
	 * @param propertyVal
	 */
	public static boolean setProperty(Object bean , Class<?> stopClass,String propertyName,Object propertyVal){
		if(StringUtils.isEmpty(propertyName)) return false;
		boolean flg = false;
		try {
			PropertyDescriptor[] propertyDescriptors = getAllProperty(bean.getClass(), stopClass);
			for(PropertyDescriptor p : propertyDescriptors){
				if(propertyName.equals(p.getName())){
					Method writeMethod = p.getWriteMethod();
					writeMethod.invoke(bean,propertyVal);
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flg;
	}
	
	/**
	 * 根据Bean类获取所有的属性
	 * @param beanClass
	 * @param stopClass
	 * @return
	 */
	public static PropertyDescriptor[] getAllProperty(Class<?> beanClass,Class<?> stopClass){
		BeanInfo beanInfo = null;
		try {
			beanInfo = Introspector.getBeanInfo(beanClass, stopClass);
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
		return beanInfo == null ? null : beanInfo.getPropertyDescriptors();
	}
	
	/**
	 * 根据bean对象和属性名称获取对应的属性的写方法
	 * @param bean
	 * @param stopClass
	 * @param propertyName
	 * @return
	 */
	public static Method getPropertyWriterMethod(Object bean , Class<?> stopClass,String propertyName){
		Method method = null;
		try {
			PropertyDescriptor[] propertyDescriptors = getAllProperty(bean.getClass(), stopClass);
			for(PropertyDescriptor p : propertyDescriptors){
				if(propertyName.equals(p.getName())){
					method = p.getWriteMethod();
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return method;
	}
}