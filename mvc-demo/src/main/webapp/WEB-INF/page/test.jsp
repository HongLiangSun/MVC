<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form action="demo" method="get" >
		User名称:<input type="text" name="name" value="${user.name}" /><br>
		User年龄:<input type="text" name="age" value="${user.age }" /><br>
		Student名称:<input type="text" name="student.name" value="${user.student.name }" /><br>
		Student年龄:<input type="text" name="student.age" value="${user.student.age }" /><br>
		tt:<input type="text" name="tt" value="${tt}" /><br>
		<ul id="list">
			<li><label><input type="checkbox" value="1" name="hh" >
					1.我是记录来的呢</label></li>
			<li><label><input type="checkbox" value="2"  name="hh" >
					2.哈哈，真的太天真了</label></li>
		</ul>
		<input type="submit" />
	</form>
	<p style="font-size: 18px; color: blue;">hello:${user}</p>
</body>
</html>