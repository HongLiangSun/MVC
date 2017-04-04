package cn.wvc.demo.service;

import cn.wvc.demo.entity.User;

public interface UserApi {
	User findUserById(String id);
}
