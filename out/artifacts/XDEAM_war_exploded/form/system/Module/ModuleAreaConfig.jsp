<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<%@ page 
language="java"
contentType="text/html; charset=GBK"
pageEncoding="GBK"
%>
<jsp:useBean class="com.pantech.base.common.tags.security.PurViewTools" id="pc" scope="page" />
<%@ taglib uri="/WEB-INF/taglib/securityTag.tld" prefix="security"%>
<META http-equiv="Content-Type" content="text/html; charset=GBK">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<link href="<%=request.getContextPath()%>/css/wiserunoa.css" rel="stylesheet" type="text/css">
<TITLE>【模块设置】</TITLE>
</HEAD>
<%
String mcode = pc.GetModuleCodeFromUrl(request);
%>
<security:securityForPage pagePermissionID="<%=mcode%>">
<frameset cols="40%,*" frameborder="NO" border="0" framespacing="0" style="overflow:hidden">
	<frame src="M-ALeftpage.jsp" name="left">
	<frame src="M-ARightDown.jsp" name="rightdown">
</frameset>
</security:securityForPage>
</HTML>
