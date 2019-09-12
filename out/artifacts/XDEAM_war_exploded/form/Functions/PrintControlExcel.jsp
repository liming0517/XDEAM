<%//@page contentType="application/vnd.ms-excel;charset=GBK" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<%@ page 
language="java"
contentType="application/vnd.ms-excel;charset=GBK"
%>
<%@ page import="java.net.*" %>
<%@ page import ="java.util.*"%>
<%@ page import ="com.pantech.base.common.tags.util.*"%>
<%@ page import ="java.sql.ResultSet"%>
<%@ page import="com.pantech.base.common.tools.PageMgr"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page errorPage="/form/ErrHandler.jsp" %>
<%@ page import="java.util.HashMap"%> 
<%@ taglib uri="/WEB-INF/taglib/util.tld" prefix="PrintCon" %>
<META http-equiv="Content-Type" content="text/html; charset=GBK">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">

<TITLE>Print</TITLE>
</HEAD>
<BODY>

<%

String decode =MyTools.StrFiltr( request.getParameter("decode")).trim();
String sqlQuery ="";
if(decode.equals(MyTools.getProp(request,"Module","[DECODE]"))){
	sqlQuery=URLDecoder.decode(MyTools.StrFiltr( request.getParameter("sqlstate")));
}else{
	sqlQuery=MyTools.StrFiltr( request.getParameter("sqlstate"));
}
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

%>

<!-- 权限标签 -->
<!-- security:securityForPage pagePermissionID=""> -->

<!-- 表单区 -->
<form name="form1" action="" method="post">
<center>
<!-- 图表显示区 -->
<PrintCon:PrintControl sqlStatement="<%=sqlQuery%>" queryType="<%=queryType%>" startCol="<%=startCol%>" endCol="<%=endCol%>" endRow="<%=endRow%>" tbTitle="<%=tbTitle%>"/>
</center>
<!-- 隐藏变量区--> 
<INPUT type="hidden" name="sqlstate" value="<%=sqlQuery%>"> <!--java.net.URLEncoder.encode(-->
<INPUT type="hidden" name="startCol" value="<%=startCol%>">
<INPUT type="hidden" name="endCol" value="<%=endCol%>">
<INPUT type="hidden" name="endRow" value="<%=endRow%>">
<INPUT type="hidden" name="queryType" value="<%=queryType%>">
<INPUT type="hidden" name="tbTitle" value="<%=tbTitle%>">
<INPUT type="hidden" name="decode" value="<%=decode%>">
<!--专门用来处理标题 -->

</form>
</BODY>
</HTML>
