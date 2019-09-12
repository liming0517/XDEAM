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
	String xnxqbm = MyTools.StrFiltr(request.getParameter("xnxqbm"));
	String exportType = MyTools.StrFiltr(request.getParameter("exportType"));
	String parentId = MyTools.StrFiltr(request.getParameter("parentId"));
	String code = MyTools.StrFiltr(request.getParameter("code"));
	String timetableName = URLDecoder.decode(URLDecoder.decode(StringEscapeUtils.escapeJava(request.getParameter("timetableName")), "utf-8"), "utf-8");
	String sAuth = MyTools.StrFiltr(request.getParameter("sAuth"));
	String userCode = MyTools.StrFiltr(request.getParameter("userCode"));
	
	if("classKcb".equalsIgnoreCase(exportType) || "classKcbAll".equalsIgnoreCase(exportType) ||
		"teaKcb".equalsIgnoreCase(exportType) || "teaKcbAll".equalsIgnoreCase(exportType) ||
		"courseKcb".equalsIgnoreCase(exportType) || "courseKcbAll".equalsIgnoreCase(exportType)|| 
		"studentKcb".equalsIgnoreCase(exportType)){
		ExportExcelBean.exportSingleTimetable(request, sAuth, userCode, xnxqbm, exportType, parentId, code, timetableName);
	}else{
		ExportExcelBean.exportMultipleTimetable(request, sAuth, userCode, xnxqbm, exportType, code, timetableName);
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
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
</head>
	<body style="margin:0;" scroll="no">
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
			
			//文档打开后执行
			function AfterDocumentOpened(){
				$('#mask').hide();
			}
		</script>
		<table id="mask" style="width:100%; height:100%; text-align:center; display:none;">
			<tr><td><img src="<%=request.getContextPath()%>/images/loading_3.gif"/></td></tr>
		</table>
		<div id="mainDiv" style="width:100%; height:100%;">
			<po:PageOfficeCtrl id="PageOfficeCtrl1" />
		</div>
	</body>
</html>