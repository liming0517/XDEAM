<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<%@ page 
language="java"
contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"
%>
<%@ page import="com.pantech.base.common.tools.*"%>
<jsp:useBean class="com.pantech.base.common.tags.security.PurViewTools" id="pc" scope="page" />
<%@ taglib uri="/WEB-INF/taglib/securityTag.tld" prefix="security"%>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<link href="<%=request.getContextPath()%>/css/wiserun.css" rel="stylesheet" type="text/css">
<TITLE>【<%=MyTools.getProp(request,session.getAttribute("language"),"[PerDataConfig_01]")%>】</TITLE>
</HEAD>
<%
String mcode = pc.GetModuleCodeFromUrl(request);
%>
<security:securityForPage pagePermissionID="<%=mcode%>">
<frameset cols="50%,*" frameborder="NO" border="0" framespacing="0" style="overflow:hidden">
	<frame src="D-CLeftpage.jsp" name="left">
	<frame src="D-CRight.jsp" name="right1">	
</frameset>
</security:securityForPage>
</HTML>
