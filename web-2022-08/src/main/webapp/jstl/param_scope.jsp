<%@page import="student.StudentVo"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>jstl/param_scope.jsp</title>
</head>
<body>

<%
StudentVo vo = new StudentVo();
vo.setName(request.getParameter("name"));
vo.setAddress(request.getParameter("address"));
vo.setPhone("010-7777-7777");

request.setAttribute("vo", vo); // "vo"가 li태그 안의 vo와 같은거다. 37행의 li태그 안의 vo는 13행의 vo 객체다
%>

<h3>param + scope</h3>
<form name='frm' method='post'>
	<span>성명</span>
	<input type='text' name='name' value='${param.name }'/><br/>
	<span>주소</span>
	<input type='text' name='address' value='${param.address }'/><br/>
	<input type='submit' value='전송'/>
</form>
<hr/>

<div>
	<ul>
		<li>Name : ${vo.name }</li>
		<li>Address : ${vo.address }</li>
		<li>Phone : ${vo.phone }</li>
		
		<li>vo.getName : <%=vo.getName() %></li>
	</ul>
</div>
</body>
</html>