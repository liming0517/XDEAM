<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<%@ page 
language="java"
contentType="text/html; charset=UTF-8"
pageEncoding="GB18030"
%>
<jsp:useBean class="com.pantech.base.common.tags.security.PurViewTools" id="pc" scope="page" />
<%@ taglib uri="/WEB-INF/taglib/securityTag.tld" prefix="security"%>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<link href="<%=request.getContextPath()%>/css/wiserunoa.css" rel="stylesheet" type="text/css">
<TITLE>权限设置</TITLE>
</HEAD>
<%
String mcode = pc.GetModuleCodeFromUrl(request);
%>
<security:securityForPage pagePermissionID="<%=mcode%>">


<frameset cols="60%,40%" frameborder="NO" border="0" framespacing="0" style="overflow:hidden">
  <frame id="editarea" src="AuthDeptList.jsp" name="left">
  <frame id="treearea" src="<%=request.getContextPath()%>/form/system/TreeList.jsp?type=Config&selectMode=1&tableName=V_Node_Module" name="right">
</frameset>
</security:securityForPage>
</HTML>
