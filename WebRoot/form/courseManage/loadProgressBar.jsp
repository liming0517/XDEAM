<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page import="com.pantech.base.common.tools.MyTools"%>
<%
	/**
		创建人：yeq
		Create date: 2015.06.03
		功能说明：用于读取排课进度
		页面类型:列表及模块入口
		修订信息(有多次时,可有多个)
		修订日期:
		原因:
		修订人:
		修订时间:
	**/
 %>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
	<title></title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/icon.css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
</head>
<%
	String progressStr = MyTools.StrFiltr(session.getAttribute("pkjd"));
	String firstEnter = MyTools.StrFiltr(request.getParameter("firstEnter"));
	if("yes".equalsIgnoreCase(firstEnter) && progressStr==null){
		session.setAttribute("pkjd", "0");
		progressStr = "0";
	}
%>
<body class="easyui-layout">
</body>
<script type="text/javascript">
	var progress = <%=progressStr%>;//排课进度
	
	$(document).ready(function(){
		window.parent.$('#progressBar').css('width', parseInt(285*progress/100)+'px');
		window.parent.$('#percent').html(progress + '%');
		if(parseInt(progress) > 60){
			window.parent.$('#percent').css('color', 'white');
		}
	});
</script>
</html>