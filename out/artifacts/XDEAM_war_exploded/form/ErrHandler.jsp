<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page 
language="java"
contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"
%>
<%@page isErrorPage="true" %>
<%
request.setCharacterEncoding("UTF-8");

		
//String temp_p=message.trim();
//byte[] temp_t=temp_p.getBytes("ISO8859-1");
//String temp=new String(temp_t);
//message = temp;
//System.out.println("++"+request.getAttribute("MSG"));

String message="" ; //= request.getParameter("message");
 //获取消息 2005-6-23 HADES 为防止中文乱码
message=message + " " + MyTools.StrFiltr ((String)  request.getAttribute("MSG"));
request.removeAttribute("MSG");

String user = request.getParameter("user");
if(message ==null){
	message = "...";
}
if(user==null){
	user = " ";
}else{
	message = "对不起，您没有权限访问该页。请与管理员联系。";
}
%>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<link href="<%=request.getContextPath()%>/css/wiserun.css" rel="stylesheet" type="text/css">
<TITLE>「错误信息」</TITLE>
</HEAD>
<BODY>
<fieldset>
<legend>·对不起;您访问的页面或操作发生了错误,错误信息如下：
</legend>

<CENTER><TABLE border="0" width="90%" cellspacing="1" cellpadding="1">
	<TBODY>

		<TR>
			<TD width="90%" bgcolor="#eceafb" colspan="2">
			<%="<li>"+(exception==null?"无系统异常。":exception.toString())%></TD>
		</TR>
		<TR>
			<TD width="90%" bgcolor="#eceafb"><%="<li>"+message+"</li>"%></TD>
		</TR>
		<TR>
			<TD width="90%" bgcolor="#eceafb" colspan="2" align="center"><input type="button" value="返回上一页" onclick="history.back(1)"></TD></TR>
	</TBODY>
</TABLE></CENTER>
</fieldset>
</BODY>
</HTML>
