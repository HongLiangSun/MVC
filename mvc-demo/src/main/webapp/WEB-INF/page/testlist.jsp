<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form action="demolist" method="get">
		<ul id="list">
			<li><label><input type="checkbox" value="1" name="name" >
					1.我是记录来的呢</label></li>
			<li><label><input type="checkbox" value="2"  name="name" >
					2.哈哈，真的太天真了</label></li>
			<li><label><input type="checkbox" value="3"  name="name" >
					3.爱上你是我的错吗？</label></li>
			<li><label><input type="checkbox" value="4"  name="name" >
					4.从开始你就不应用爱上我</label></li>
			<li><label><input type="checkbox" value="5"  name="name" >
					5.喜欢一个人好难</label></li>
			<li><label><input type="checkbox" value="6"  name="name" >
					6.你在那里呢</label></li>
		</ul>
		<input type="submit" />
	</form>
	<p style="font-size: 18px; color: blue;">hello:${user}</p>
</body>
</html>