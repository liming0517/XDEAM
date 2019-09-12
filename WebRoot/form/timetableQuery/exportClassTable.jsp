<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="com.pantech.base.common.tools.*"%>
<%@page import="com.pantech.develop.pageoffice.*"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="java.net.URLDecoder"%>
<%@ taglib uri="http://java.pageoffice.cn" prefix="po"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String exportType = MyTools.StrFiltr(request.getParameter("exportType"));
	String school = MyTools.StrFiltr(request.getParameter("school"));
	//String house = MyTools.StrFiltr(request.getParameter("house"));
	//String floor = MyTools.StrFiltr(request.getParameter("floor"));
	String week = MyTools.StrFiltr(request.getParameter("week"));
	String xnxq = MyTools.StrFiltr(request.getParameter("xnxq"));
	String jxxz = MyTools.StrFiltr(request.getParameter("jxxz"));
	String titleinfo = MyTools.StrFiltr(URLDecoder.decode(URLDecoder.decode(request.getParameter("titleinfo"),"utf-8"),"utf-8"));
	if("classTable".equalsIgnoreCase(exportType)){
		ExportExcelBean.exportClassTable(request, exportType, school, week, xnxq, jxxz, titleinfo);
	}
%>
<html>
<head>
  <base href="<%=basePath%>">
   <title>导出预览</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/JQueryUI/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/JQueryUI/themes/icon.css">
	
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
</head>
	<body style="margin:0;">
		<!-- *********************pageoffice组件的使用 **************************-->
		<script language="javascript" type="text/javascript">
			//保存
			function exportExcel(){
				document.getElementById("PageOfficeCtrl1").ShowDialog(3); 
			}
			
			//全屏
			function SetFullScreen() {
				document.getElementById("PageOfficeCtrl1").FullScreen = !document.getElementById("PageOfficeCtrl1").FullScreen;
			}
			
			//返回
			function goBack() {
				window.parent.closeDialog();
			}
			
			//打印
			function print(){
				document.getElementById("PageOfficeCtrl1").PrintPreview();
			}
		</script>
		<div id="mainDiv" style="width:100%; height:100%;">
			<po:PageOfficeCtrl id="PageOfficeCtrl1" />
		</div>
	</body>
</html>