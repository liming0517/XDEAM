<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<%@ page 
language="java"
contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"
%>
<%@ page import="com.pantech.base.common.tools.*"%>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">

<LINK href="../../theme/Master.css" rel="stylesheet" type="text/css">
<TITLE><%=MyTools.getProp(request,session.getAttribute("language"),"[DeptConfig_01]")%></TITLE>

</HEAD>
<jsp:useBean class="com.pantech.base.common.tags.security.PurViewTools" id="pc" scope="page" />
<%@ taglib uri="/WEB-INF/taglib/securityTag.tld" prefix="security"%>
<%
String mcode = pc.GetModuleCodeFromUrl(request);
%>
<security:securityForPage pagePermissionID="<%=mcode%>">

<frameset cols="60%,40%" frameborder="NO" border="0" framespacing="0" style="overflow:hidden">
  <frame src="DeptList.jsp" name="left">
  <frame src="DeptTree.jsp?type=Config" name="right">
</frameset>

</security:securityForPage>
</HTML>