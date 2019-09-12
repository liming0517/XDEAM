<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import ="java.util.*"%>
<%@ page import="com.pantech.src.develop.message.*"%>
<%@ page import="com.pantech.src.develop.store.user.*" %>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%

	//UserProfile up = (UserProfile)request.getSession().getAttribute("UserProfile");
	String userCode = "";

	if(request.getSession().getAttribute("UserCode")!=null){
		userCode = MyTools.StrFiltr(request.getSession().getAttribute("UserCode"));
	}else{
		MyTools.GoToErrPage(request,response,"会话超时，请重新登陆。");
	}
	Svl_message mes = new Svl_message();
	Vector vList = mes.QueryMessage("unread",userCode,request);
%>
<HEAD>
<%@ page 
language="java"
contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"
%>
<link href="<%=request.getContextPath()%>/css/wiserunoa.css" rel="stylesheet" type="text/css">
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<META http-equiv="refresh" content="120">

</HEAD>
<BODY>
<%
	if(vList.size()>0){
%>
		<table width="100%" border="0" cellpadding="0" cellspacing="0" background="<%=request.getContextPath()%>/images/onlinebg2.jpg" >
		<tr><td class="onlinestyle">
		<A HREF="<%=request.getContextPath()%>/form/Message/QueryMessage.jsp" name="msg" target="right">
			<IMG border="0" src="<%=request.getContextPath()%>/images/mail.gif">
			<font color=white>您有新消息，请注意查收!</font>
		</A> 
		</td></tr>
		</table>
<%
	}
%>
</BODY>
