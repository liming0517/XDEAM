<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%
	/**
		创建人：yeq
		Create date: 2015.05.26
		功能说明：首页
		页面类型:
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
<title>欢迎</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/icon.css">
<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/publicScript.js"></script>
<script language="JavaScript">
	$(document).ready(function(){
		var systemName = "";
		var sysType = window.parent.id;
		
		if(sysType == '336018791') systemName = "系统管理";
		if(sysType == '336028791') systemName = "教学管理系统";
		if(sysType == '336038791') systemName = "智能排课系统";
		if(sysType == '336048791') systemName = "评教管理系统";
		if(sysType == '336058791') systemName = "考务管理系统";
		if(sysType == '336068791') systemName = "成绩管理系统";
		if(sysType == '336078791') systemName = "选修课管理系统";
		if(sysType == '336088791') systemName = "外考管理系统";
		if(sysType == '336098791') systemName = "基础信息管理";
		
		$('#systemName').html(systemName);
		
		if(sysType=='336038791' || sysType=='336068791'){
			showTips();
		}
	});
	
	function showTips(){
		var msgStr = '<font color="red">注意：系统打印功能需安装<font color="#3B73C1">Microsoft Office</font>和<font color="#3B73C1">PageOffice客户端</font></font><br/><br/>如出现下列情况，请下载安装插件：<br/>1、首次使用本系统<br/>2、点击各类信息查看按钮无反应<br/>3、点击各类信息查看按钮后提示需安装程序<br/>4、点击各类信息查看按钮后一直停留在读取状态 <br/><br/><a href="<%=request.getContextPath()%>/posetup.exe">点击下载PageOffice客户端</a>';
		$.messager.show({
			title:'提示',
			width:380,
			height:200,
			msg:msgStr,
			showType:'slide',
			timeout:0
		});
	}
</script>
</head>
<body style="margin:0 0 0 0;" scroll="no">
	<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0" background="<%=request.getContextPath()%>/images/bg.png" >
	  <tr height="350">
	    <td colspan="2" align="center" valign="middle"><h1>欢迎进入上海市现代职业技术学校<span id="systemName"></span></h1></td>
	  </tr>
	</table>
</body>
</HTML>