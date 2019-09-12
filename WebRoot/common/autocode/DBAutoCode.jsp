<!--
Author:Zedee
CreateDate:20050631
Function:代码自动化框架
-->
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<%@ page 
language="java"
contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"
%>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="../../theme/Master.css" rel="stylesheet" type="text/css">
<TITLE>代码自动化工具</TITLE>
</HEAD>
<frameset id="ServeInfo" cols="20%,*" frameborder="NO" border="0" framespacing="0" style="overflow:hidden">
	<frame id="listarea" src="<%=request.getContextPath()%>/common/autocode/DBStructList.jsp?type=V" scrolling=auto frameborder=0 noresize>	
	<frame id="editarea" src="<%=request.getContextPath()%>/common/autocode/DBStructAnalyze.jsp" scrolling=auto frameborder=0 noresize>
</frameset>
<BODY>
</BODY>
</HTML>
