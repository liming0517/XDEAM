<%//@page contentType="application/vnd.ms-excel;charset=GBK" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<%@ page
language="java"
contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"
%>
<%@ page import ="java.util.*"%>
<%@ page import ="com.pantech.base.common.tags.util.*"%>
<%@ page import ="java.sql.ResultSet"%>
<%@ page import="com.pantech.base.common.tools.PageMgr"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page errorPage="/form/ErrHandler.jsp" %>
<%@ page import="java.util.HashMap"%> 
<%@ taglib uri="/WEB-INF/taglib/util.tld" prefix="PrintCon" %>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<link href="<%=request.getContextPath()%>/css/wiserunoa.css" rel="stylesheet" type="text/css">

<TITLE>Print</TITLE>
</HEAD>
<BODY>

<%

String sqlQuery =MyTools.StrFiltr( request.getParameter("sqlstate"));

if(sqlQuery.equalsIgnoreCase("")==true){
	sqlQuery =MyTools.StrFiltr( request.getAttribute("sqlstate"));
}
System.out.println("sqlQuery==="+sqlQuery);
sqlQuery = java.net.URLDecoder.decode(sqlQuery,"utf-8");//先将隐藏域sql编码，再次调用此页面时再将sql解码
System.out.println("sqlQuery---"+sqlQuery);
//sqlQuery=sqlQuery +";"+ sqlQuery;
//sqlQuery=sqlQuery + ";" +sqlQuery;

//sqlQuery = java.net.URLEncoder.encode(sqlQuery);
String queryType= MyTools.StrFiltr( request.getParameter("queryType")); // print mode when "OWC" THEN EXPORT to OWC
 //the col is start print (first col=0) exp:startCol=1  then the no 1 col can not print
 // defaule  startCol=1 endcolCol=colcount
String startCol= MyTools.StrFiltr( request.getParameter("startCol"));
String endCol= MyTools.StrFiltr( request.getParameter("endCol"));
String endRow = MyTools.StrFiltr( request.getParameter("endRow"));
//指定列头
String tbTitle = MyTools.StrFiltr( request.getParameter("tbTitle"));

//2006-8-20 hades 
//总标题
String listTitle = MyTools.StrFiltr( request.getParameter("listTitle"));
//是否需要权限
String authLimitTmp = MyTools.StrFiltr( request.getParameter("authLimit"));
boolean  authLimit=false;
if (authLimitTmp.toUpperCase().equals("TRUE")){
  authLimit=true;
}
//权限表名 2008-12-12 hades
String authTableName= MyTools.StrFiltr( request.getParameter("authTableName"));
%>

<!-- 权限标签 -->
<!-- security:securityForPage pagePermissionID=""> -->

<!-- 表单区 -->
<form name="form1" action="" method="post">
<center>
<!-- 图表显示区 -->
<PrintCon:PrintControl sqlStatement="<%=sqlQuery%>" queryType="<%=queryType%>" startCol="<%=startCol%>" endCol="<%=endCol%>" endRow="<%=endRow%>" tbTitle="<%=tbTitle%>"  listTitle="<%=listTitle%>" authLimit="<%=authLimit%>" authTableName="<%=authTableName%>"/>
</center>
<!-- 隐藏变量区--> 
<INPUT type="hidden" name="sqlstate" value='<%=java.net.URLEncoder.encode(sqlQuery,"utf-8")%>'> <!--java.net.URLEncoder.encode(-->
<INPUT type="hidden" name="startCol" value="<%=startCol%>">
<INPUT type="hidden" name="endCol" value="<%=endCol%>">
<INPUT type="hidden" name="endRow" value="<%=endRow%>">
<INPUT type="hidden" name="queryType" value="<%=queryType%>">

<!--专门用来处理标题 -->
<INPUT type="hidden" name="tbTitle" value="<%=tbTitle%>">
<INPUT type="hidden" name="listTitle" value="<%=listTitle%>">

<INPUT type="hidden" name="authLimitTmp" value="<%=authLimit%>">
<INPUT type="hidden" name="authLimit" value="<%=authLimit%>">
<INPUT type="hidden" name="authTableName" value="<%=authTableName%>">
</form>
</BODY>
</HTML>
