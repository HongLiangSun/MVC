package cn.wvc.demo.entity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.e2u.mvc.core.annotation.Service;

import cn.wvc.demo.service.UserApi;
@Service
public class UserService implements UserApi {
	private Map<String,User> users = new HashMap<>();
	public UserService() {
		initUser();
	}

	private void initUser() {
		users.put("1111", new User("1111",21,new Date()));
		users.put("2222", new User("2222",22,new Date()));
		users.put("3333", new User("3333",23,new Date()));
		users.put("4444", new User("4444",24,new Date()));
	}

	@Override
	public User findUserById(String id) {
		return users.get(id);
	}

}
