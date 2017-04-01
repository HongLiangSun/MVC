package cn.wvc.demo;

import com.e2u.mvc.core.annotation.Controller;
import com.e2u.mvc.core.annotation.RequestMapping;
import com.e2u.mvc.core.annotation.RequestParam;
import com.e2u.mvc.core.annotation.ResponseBody;

@Controller
public class Demo {

	@ResponseBody
	@RequestMapping("/getUser")
	public String getUesr(@RequestParam(value = "userName") String userName) {
		return userName;
	}
	
}
