<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<%@ page 
language="java"
contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"
%>
<jsp:useBean class="com.pantech.base.common.tags.security.PurViewTools" id="pc" scope="page" />
<%@ taglib uri="/WEB-INF/taglib/securityTag.tld" prefix="security"%>
<%@ page import="com.pantech.src.develop.system.SystemClass"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<link href="<%=request.getContextPath()%>/css/wiserun.css" rel="stylesheet" type="text/css">
<TITLE>【人员所属角色设置】</TITLE>
</HEAD>
<%
SystemClass Auth=new SystemClass(request);
String mcode = pc.GetModuleCodeFromUrl(request);
String AuthList=MyTools.StrFiltr(request.getParameter("AuthList"));
//out.println(mcode);
%>
<security:securityForPage pagePermissionID="<%=mcode%>">
<frameset cols="50%,*" frameborder="NO" border="0" framespacing="0" style="overflow:hidden">
	<frame src="U-ALeftpage.jsp" name="left">
		<frame src="U-ARightDown.jsp?AuthList=<%=AuthList%>" name="rightdown">
	</frameset>
</security:securityForPage>
</HTML>
