package cn.util.provide;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;


public class BeanUtils {
	/**
	 * 为bean对象属性赋值
	 * @param bean
	 * @param stopClass
	 * @param propertyName
	 * @param propertyVal
	 * @return hongliang.sun
	 */
	public static boolean setProperty(Object bean , Class<?> stopClass,String propertyName,Object propertyVal){
		if(StringUtils.isEmpty(propertyName)) return false;
		boolean flg = false;
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass(), stopClass);
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
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
	
	public static PropertyDescriptor[] getAllProperty(Class<?> beanClass,Class<?> stopClass){
		BeanInfo beanInfo = null;
		try {
			beanInfo = Introspector.getBeanInfo(beanClass, stopClass);
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
		return beanInfo == null ? null : beanInfo.getPropertyDescriptors();
	}
}