package cn.wvc.demo;

import java.util.Map;

import com.e2u.mvc.core.annotation.Controller;
import com.e2u.mvc.core.annotation.RequestMapping;
import com.e2u.mvc.core.annotation.RequestParam;

@Controller
public class Demo {

	@RequestMapping("/getUser")
	public String getUesr(Map<String, Object> map,@RequestParam(value = "userName") String userName) {
		map.put("user", "sunhongliang");
		return "hello";
	}

}
