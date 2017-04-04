package cn.wvc.demo.entity;

import java.util.Date;

import com.e2u.mvc.core.annotation.DatePattern;

public class User {
	private String id;
	private int age;
	@DatePattern("yyyy/MM/dd")
	private Date inSchool;

	public User() {
		// TODO Auto-generated constructor stub
	}

	public User(String id, int age, Date inSchool) {
		super();
		this.id = id;
		this.age = age;
		this.inSchool = inSchool;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Date getInSchool() {
		return inSchool;
	}

	public void setInSchool(Date inSchool) {
		this.inSchool = inSchool;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", age=" + age + ", inSchool=" + inSchool
				+ "]";
	}
}
