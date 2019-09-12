<!-- 导出报表中间文件，处理sql过长导致直接跳转时候sql被截断 sqlstate为接受前面页面的sql，然后用post方法提交到下一个页面-->
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><%@page
	language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import="com.pantech.base.common.tools.MyTools" %>
<html>
<head>
<link rel="stylesheet" href="../../theme/Master.css" type="text/css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/icon.css">
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
<title>PrintSql</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="GENERATOR"
	content="Rational® Application Developer™ for WebSphere® Software">
</head>
<form  method="Post" id="form1">
<body>
<input type="hidden" id="sqlstate" name="sqlstate" value=""/>
<input type="hidden" id="authLimit" name="authLimit" value=""/>
</body>
</form>
</html>
<script type = "text/JavaScript"> 
	   var sqlstate=window.opener.sqlstate;//获取父窗体的sqlstate变量的值--返回的sql
	   var queryType=window.opener.queryType;//获取父窗体的queryType变量的值--图表显示参数
	   form1.action="PrintControl.jsp";
	   $('#sqlstate').val(sqlstate);
	   $('#queryType').val(queryType);
	   form1.submit();//以post方法提交到printcontrol.jsp
</script>