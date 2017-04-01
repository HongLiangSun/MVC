package com.e2u.mvc.core.entity;

import java.lang.reflect.Method;


public class RequestBody {
	//请求方法所在的类
	private Class<?> clazz;
	//请求方法
	private Method method;
	
	public RequestBody() {
		
	}
	public RequestBody(Class<?> clazz, Method method) {
		this.clazz = clazz;
		this.method = method;
	}
	public Class<?> getClazz() {
		return clazz;
	}
	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}
	public Method getMethod() {
		return method;
	}
	public void setMethod(Method method) {
		this.method = method;
	}
	@Override
	public String toString() {
		return "RequestBody [clazz=" + clazz + ", method=" + method + "]";
	}
	
}
