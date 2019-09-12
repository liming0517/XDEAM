
<!-- 
编写人员：luweibin
页面作用：登出
创建日期：2013-07-23
 -->
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%
//	request.getSession().getAttribute("UserProfile")
	request.getSession().setAttribute("UserProfile",null);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="refresh" content="3; url=<%=request.getContextPath()%>/Login.jsp">
<TITLE>注销</TITLE>
</HEAD>
<BODY>
<table width="100%" height="100%">
<tr>
<TD ALIGN="center" VALIGN="middle" class="bj">
	
	系统已注销，三秒后跳转到登陆页</br>
	<a href="<%=request.getContextPath()%>/Login.jsp">如果您的浏览器没有自动跳转，请点击这里</a>
	
</tr>
</table>
</BODY>
</HTML>
