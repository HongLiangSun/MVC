package com.e2u.mvc.core.view;
/**
 * 逻辑视图类 
 * 视图url=试图前缀+视图名称+视图后缀
 * @author shell
 */
public class View {
	//视图前缀
	private String prefix;
	//视图名称
	private String name;
	//视图后缀
	private String suffix;
	
	public View() {
	}
	
	public View(String prefix, String name, String suffix) {
		super();
		this.prefix = prefix;
		this.name = name;
		this.suffix = suffix;
	}

	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSuffix() {
		return suffix;
	}
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	
	
}
