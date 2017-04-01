package com.e2u.mvc.core.servlet;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import cn.util.provide.JsonUtils;
import cn.util.provide.StringUtils;

import com.e2u.mvc.core.common.CommonConfig;
import com.e2u.mvc.core.common.RequestType;
import com.e2u.mvc.core.entity.ModelAndView;
import com.e2u.mvc.core.entity.RequestBody;
import com.e2u.mvc.core.entity.RequestWrap;
import com.e2u.mvc.core.factory.BeanFactory;
import com.e2u.mvc.core.handler.RequestMappingHandler;
import com.e2u.mvc.core.handler.ViewHandler;
import com.e2u.mvc.core.view.LocationResolver;
/**
 * 
 * @author shell
 * mvc 启始Servlet。 拦截所有的用户请求
 */
public class DispatcherServlet extends HttpServlet{

	private static final long serialVersionUID = -733544258689660716L;
	
	private RequestMappingHandler requestMappingHandler = RequestMappingHandler.getRequestMappingHandler();
	
	private Logger log = Logger.getLogger(DispatcherServlet.class);
	
	private static final String DEFAULT_SERVLET = "default";
	
	/**
	 * 进行初始化操作
	 */
	@Override
	public void init() throws ServletException {
		//初始化配置文件
		CommonConfig.initConfig();
		//初始化Bean工厂
		initBeansFactory();
		log.info("初始化【DispatcherServlet】start...");
		//初始化映射关系
		requestMappingHandler.initMapping();
		log.info("初始化【DispatcherServlet】end。");
	}
	
	/**
	 * 初始化Bean工厂
	 */
	private void initBeansFactory() {
		log.info("初始化Bean工厂start...");
		try {
			BeanFactory.initBeans();
		} catch (Exception e) {
			log.error("初始化Bean失败");
			throw new RuntimeException("初始化Bean失败");
		}
		log.info("初始化Bean工厂end。");
	}

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		//获取映射关系
		Map<RequestWrap, RequestBody> requestMap = requestMappingHandler.getRequestMapping();
		String path = getServletPath(request);
		//判断请求的是否是静态资源
		if(isStaticResource(path)){
			this.getServletContext().getNamedDispatcher(DEFAULT_SERVLET).forward(request, response);
			return;
		}
		RequestType requestType = Enum.valueOf(RequestType.class, request.getMethod().toUpperCase());
		//得到映射对象
		RequestBody requestMapping = requestMap.get(new RequestWrap(requestType, path));
		if(requestMapping != null){
			ModelAndView modelAndView = null;
			try {
				//根据映射对象获取视图
				modelAndView = new ViewHandler().getModelAndView(request,response,requestMapping);
			} catch (Exception e) {
				log.error("获取modelAndView出错", e);
				response.sendError(400, e.getMessage());
				return;
			}
			if(modelAndView == null || modelAndView.getView() == null || modelAndView.getView().getName() == null){
				response.sendError(404);
			}else{
				if(modelAndView.getIsJson() != null && modelAndView.getIsJson()){
					Object jsonObj = modelAndView.getJsonObj();
					try {
						response.setCharacterEncoding("UTF-8");
						response.setContentType("application/json; charset=utf-8");
						response.getWriter().write(JsonUtils.object2Str(jsonObj));
					} catch (Exception e) {
						log.error("json数据转换出错", e);
					}
				}else{
					//没有配置时候使用默认的视图解析器
					if(StringUtils.isEmpty(CommonConfig.VIEW_RESOLVER)){
						new LocationResolver().viewResolver(request, response, modelAndView);
					}else{
						//TODO
					}
				}
			}
		}else{
			response.sendError(404,"没有找到对应路径映射");
		}
		
	}

	private boolean isStaticResource(String path) {
		if(StringUtils.isEmpty(CommonConfig.STATIC_RESOURCE)){
			return false;
		}
		Pattern pattern = Pattern.compile(CommonConfig.STATIC_RESOURCE);
		Matcher matcher = pattern.matcher(path);
		return matcher.matches();
	}

	private String getServletPath(HttpServletRequest request) {
		String path = request.getServletPath();
		if(!StringUtils.isEmpty(request.getPathInfo())){
			path += request.getPathInfo();
		}
		return path;
	}
}
