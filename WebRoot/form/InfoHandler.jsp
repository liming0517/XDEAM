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

String refreshFlag = request.getParameter("refreshflag");
if(refreshFlag != null && "Y".equalsIgnoreCase(refreshFlag)){
%>
<script>window.opener.location.reload();</script>
<%	
}
//String temp_p=message.trim();
//byte[] temp_t=temp_p.getBytes("ISO8859-1");
//String temp=new String(temp_t);
//message = temp;
String message="" ; //= request.getParameter("message");
 //获取消息 2005-6-23 HADES 为防止中文乱码
message=message + " " + MyTools.StrFiltr((String)  request.getAttribute("MSG"));
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
<TITLE>「提示信息」</TITLE>
</HEAD>
<BODY>
<fieldset>
<legend>·提示信息：
</legend>

<CENTER><TABLE border="0" width="90%" cellspacing="1" cellpadding="1">
	<TBODY>
		<TR>
			<TD colspan="2" class="ets_normal_top" align="center">====提示信息====</TD></TR>
		<TR>
			<TD width="84" rowspan="2">提示:</TD>
			<TD width="768" bgcolor="#eceafb">
			<%="<li>"+(exception==null?"无系统异常。":exception.toString())%></TD>
		</TR>
		<TR>
			<TD width="768" bgcolor="#eceafb"><%="<li>"+message+"</li>"%></TD>
		</TR>
	</TBODY>
</TABLE>
<BR><input type="button" value="关闭" onclick="window.close()">
</CENTER>
</fieldset>
</BODY>
</HTML>
