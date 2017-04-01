# 项目概述 #
`一个模仿springmvc基于Servlet的web框架，通过本框架能够降低Servlet代码量。符合Restful风格。`
# 运行机制概述 #
---
每一次应用开始处理 HTTP 请求时，它都会进行一个近似的流程。

1. 用户提交请求经过DispatcherServlet拦截
1. 应用会通过 request（请求） 应用组件解析被请求的路由。找到对应的映射关系。
1. 应用创建一个 controller（控制器） 实例具体处理请求。
1. 执行controller中的方法进行参数的绑定。
1. 如果参数验证成功后，通过反射执行动作。
1. 方法中含有Map或者返回ModelAndView时候会加载一个数据模型。
1. 动作会渲染一个 View（视图），并为其提供所需的数据模型。
1. 视图解析器渲染得到的结果会返回给 response（响应） 应用组件。
1. 响应组件会把渲染结果发回给用户的浏览器。

# 快速开始 #
---
**增加项目依赖:**

```
https://git.oschina.net/hongliangsun/mvc.git
```

**在web.xml增加：**
```Xml
<servlet>
	<servlet-name>mvc</servlet-name>
	<servlet-class>com.e2u.mvc.core.servlet.DispatcherServlet</servlet-class>
	<load-on-startup>1</load-on-startup>
	</servlet>
<servlet-mapping>
	<servlet-name>mvc</servlet-name>
	<url-pattern>/</url-pattern>
</servlet-mapping>
```
**在resource目录下增加dispatcher.properties文件，例如配置如下**

```
#基础配置
#基包路径
e2u.mvc.compan.basepackage=cn
#静态资源的配置
e2u.mvc.static.resources=
#视图配置(集成Freemarker配置)
e2u.mvc.view.prefix=/WEB-INF/page
e2u.mvc.view.suffix=.ftl
e2u.mvc.view.charset=UTF-8
```
**控制器（Controllers）:**

```Java
@Controller
@RequestMapping("/hello")
public class Demo {
	@Autowired
	private UserServiceApi userServiceApi;

	@RequestMapping("")
	public String index() {
		return "index";
	}

	@RequestMapping(value = "/user", method = RequestType.GET)
	@ResponseBody
	public User getUserJson(@RequestParam String userId) {
		User user = userServiceApi.getUser(userId);
		return user;
	}
	@RequestMapping(value = "/user", method = RequestType.POST)
	public String addUser(Map<String,Object> map,@ModelAttribute User user) {
		userServiceApi.addUser(user);
		map.put("user",user);
		return "userList";
	}
	
	@RequestMapping(value = "/user", method = RequestType.PUT)
	public String editUser(@ModelAttribute User user) {
		userServiceApi.updateUser(user);
		return "testlist";
	}

	@RequestMapping(value = "/user", method = RequestType.DELETE)
	public String deleteUser(@RequestParam String userId) {
		userServiceApi.deleteUserById(userId);
		return "testlist";
	}

}
```
**模型（Models）**

模型通过参数Map<String,Object>或者用返回ModelAndMap进行封装。默认域范围是Request域

```Java
@RequestMapping(value = "/user", method = RequestType.POST)
public String addUser(Map<String,Object> map,@ModelAttribute User user) {
	userServiceApi.addUser(user);
	map.put("user",user);
	return "userList";
}
```
**视图（Views）**
目前只使用能解析JSP以及使用default展示静态Html等文件。通过${user}实现对域对象内容的获取。

具体使用案例可以看：mvc-demo项目
