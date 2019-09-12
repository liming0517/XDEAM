<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.base.common.tools.PublicTools"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.*"%>
<%@ page import="com.pantech.base.common.db.DBSource"%>

<!--
	createTime:2015-6-23
	createUser:刘金璋
	description:整班课程
 -->
<html>
<head>
<title>整班课程</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- JQuery 专用4个文件 -->
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/icon.css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
</head>
<body>
	<!--实施设施：1b-页面标题-这里设置title 值,一般根据模块要求修改  -->
	<div id="aa" class="easyui-layout" data-options="fit:true,border:false">
		<!--实施设施 ：页面标题-结束  -->
		<div data-options="region:'north',title:'',split:false,border:false"
			style="height:27px;">
			<table class="tablestyle" width="1024">
				<tr>
					<td class="titlestyle">学年学期：</td>
					<td><input id="XNXQ" name="XNXQ" class="easyui-textbox"
						style="width:150px;"></input></td>
					<td class="titlestyle">班级：</td>
					<td><input id="BJ" name="BJ" class="easyui-textbox"
						style="width:150px;"></input></td>
					<td class="titlestyle">加入课程：</td>
					<td><select name="JRKC" id="JRKC" class="easyui-combobox"
						style="width:150px;"></select></td>
				</tr>
			</table>
			</div>
		<div id="index"
			data-options="region:'center',split:false,border:false">
		<table class="tablestyle" width="1024" style="text-align:center;">
		<tr><td></td>
		<td>星期一</td><td>星期二</td><td>星期三</td><td>星期四</td><td>星期五</td></tr>
		<tr><td>上午1</td>
		<td>java高级程序设计</td>
		<td></td>
		<td>体育</td>
		<td>高级网页技术</td>
		<td>网络数据库应用</td>
		</tr>
		<tr><td>上午2</td>
		<td>java高级程序设计</td>
		<td></td>
		<td>体育</td>
		<td>高级网页技术</td>
		<td>网络数据库应用</td>
		</tr><tr><td>上午3</td>
		<td>java高级程序设计</td>
		<td></td>
		<td>网络数据库应用</td>
		<td>高级网页技术</td>
		<td>网络数据库应用</td>
		</tr><tr><td>上午4</td>
		<td>java高级程序设计</td>
		<td></td>
		<td>网络数据库应用</td>
		<td>高级网页技术</td>
		<td>网络数据库应用</td>
		</tr>
		<tr><td>下午1</td>
		<td>职场英语口语</td>
		<td>电子商务应用</td>
		<td>选修课</td>
		<td>Linux操作系统及应用</td>
		<td></td>
		</tr>
		<tr><td>下午2</td>
		<td>职场英语口语</td>
		<td>电子商务应用</td>
		<td>选修课</td>
		<td>Linux操作系统及应用</td>
		<td></td>
		</tr>
		<tr><td>下午3</td>
		<td>职场英语口语</td>
		<td>电子商务应用</td>
		<td>选修课</td>
		<td>Linux操作系统及应用</td>
		<td></td>
		</tr>
		<tr><td>下午4</td>
		<td>职场英语口语</td>
		<td>电子商务应用</td>
		<td>选修课</td>
		<td>Linux操作系统及应用</td>
		<td></td>
		</tr>
		</table>	
		</div>	
	</div>
</body>
<script type="text/javascript"></script>
</html>