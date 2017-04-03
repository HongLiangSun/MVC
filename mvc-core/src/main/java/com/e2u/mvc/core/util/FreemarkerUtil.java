package com.e2u.mvc.core.util;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.e2u.mvc.core.common.CommonConfig;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

public class FreemarkerUtil {
	private static Configuration cfg = null;
	private static Logger log = Logger.getLogger(FreemarkerUtil.class);
	private static FreemarkerUtil util = new FreemarkerUtil();
	static {
		// 通过Freemaker的Configuration读取相应的ftl
		cfg = new Configuration(Configuration.VERSION_2_3_23);
		// 设定去哪里读取相应的ftl模板文件
		cfg.setClassForTemplateLoading(FreemarkerUtil.class, CommonConfig.VIEW_PRIFEX);
		cfg.setDefaultEncoding(CommonConfig.VIEW_RESOLVER_CHARSET);
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		cfg.setLogTemplateExceptions(true);
	}

	private FreemarkerUtil() {
	}

	private Template getTemplate(String name) {
		try {
			// 在模板文件目录中找到名称为name的文件
			Template temp = cfg.getTemplate(name);
			return temp;
		} catch (IOException e) {
			log.error("Freemarker没有找到名称为" + name + "的文件");
		}
		return null;
	}

	/**
	 * 输出HTML文件
	 */
	public void fprint(String name, Map<String, Object> modelMap, ServletResponse response) {
		try {
			// 通过一个文件输出流，就可以写到相应的文件中，此处用的是绝对路径
			Template temp = this.getTemplate(name);
			if(temp == null){
				if(response instanceof HttpServletResponse) 
					((HttpServletResponse)response).sendError(404);
			}else{
				response.setCharacterEncoding(CommonConfig.VIEW_RESOLVER_CHARSET);
				temp.process(modelMap, response.getWriter());
			}
		} catch (Exception e) {
			log.error(name + "的文件解析异常");
		}
	}
	
	public static FreemarkerUtil getFreemarker(){
		return util;
	}
}
