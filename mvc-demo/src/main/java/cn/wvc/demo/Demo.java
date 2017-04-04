package cn.wvc.demo;

import java.util.Map;

import cn.wvc.demo.entity.User;
import cn.wvc.demo.service.UserApi;

import com.e2u.mvc.core.annotation.Autowired;
import com.e2u.mvc.core.annotation.Controller;
import com.e2u.mvc.core.annotation.ModelAttribute;
import com.e2u.mvc.core.annotation.RequestMapping;
import com.e2u.mvc.core.annotation.RequestParam;
import com.e2u.mvc.core.annotation.ResponseBody;
import com.e2u.mvc.core.common.RequestType;

@Controller
public class Demo {

	@Autowired
	private UserApi userApi;
	
	@RequestMapping("/userEdit")
	public String page() {
		return "userEdit";
	}
	
	@RequestMapping("/getUser")
	public String getUesr(Map<String, Object> map,@RequestParam String uid) {
		map.put("user", userApi.findUserById(uid));
		return "userinfo";
	}
	
	@RequestMapping(value="/addUser", method = RequestType.POST)
	@ResponseBody
	public User addUesr(Map<String, Object> map,@ModelAttribute User user) {
		return user;
	}

	@RequestMapping(value = "/login", method = RequestType.GET)
	public String login() {
		System.out.println("login");
		return "rd:/index";
	}

}
