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
	createTime:2015-6-24
	createUser:刘金璋
	description:教师固排、静排
 -->
<html>
<head>
<title>教师固排禁排</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- JQuery 专用4个文件 -->
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/icon.css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div
		data-options="region:'west',split:false,border:false,collapsed:false"
		style="width:350px">
		<div class="easyui-layout" data-options="fit:true,split:false">
			<div data-options="region:'north',collapsed:false"
				style="height:54px">
				<table width="100%" class="tablestyle">
					<tr>
					<td class="titlestyle">学年学期</td>
					<td>
						<select id="XNXQ" name="XNXQ" class="easyui-combobox" style="width:150px;">
						<option value="volvo">2015学年第一学期</option>
						</select>
					</td>
					<td rowspan="2">
						<a href="#" onclick="doToolbar(this.id);" id="que"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-search'">查询</a>
					</td>
				</tr>
				<tr>
					<td class="titlestyle">教学性质</td>
					<td>
						<select id="JXXZ" name="JXXZ" class="easyui-combobox" style="width:150px;">
						<option value="volvo">高职</option>
						</select>
					</td>
				</tr>
				</table>
			</div>
			<div data-options="region:'center'">
				<table class="easyui-datagrid" id="List"></table>
			</div>
		</div>
	</div>
	<div
		data-options="region:'center',split:false,border:false,collapsed:false"
		style="width:300px">
		<div class="easyui-layout"
			data-options="fit:true,split:false,collapsed:false">
			<div data-options="region:'north'" style="height:110px;">
				<table class="easyui-datagrid" id="List2"></table>
			</div>
			<div data-options="region:'center'">
				<table width="100%" height="100%" border="1" cellspacing="0"
					cellpadding="0"
					style="text-align:center;border-collapse:collapse; border-color:#000000">
					<tr height="25">
						<td colspan="6" style="text-align:left;"><a href="#"
							onclick="doToolbar(this.id);" id="add" class="easyui-linkbutton"
							data-options="iconCls:'icon-save',plain:true">保存</a></td>
					</tr>
					<tr height="25" style="font-weight: bolder;">
						<td width="10%">蔡红</td>
						<td width="14%">星期一</td>
						<td width="14%">星期二</td>
						<td width="14%">星期三</td>
						<td width="14%">星期四</td>
						<td width="14%">星期五</td>
					</tr>
					<tr>
						<td><b>上午1</b></td>
						<td>禁排</td>
						<td></td>
						<td></td>
						<td>C语言程序设计</td>
						<td></td>
					</tr>
					<tr>
						<td><b>上午2</b></td>
						<td>禁排</td>
						<td></td>
						<td></td>
						<td>C语言程序设计</td>
						<td></td>
					</tr>
					<tr>
						<td><b>上午3</b></td>
						<td>禁排</td>
						<td></td>
						<td></td>
						<td>C语言程序设计</td>
						<td></td>
					</tr>
					<tr>
						<td><b>上午4</b></td>
						<td>禁排</td>
						<td></td>
						<td></td>
						<td>C语言程序设计</td>
						<td></td>
					</tr>
					<tr>
						<td><b>下午5</b></td>
						<td></td>
						<td></td>
						<td>禁排</td>
						<td></td>
						<td></td>
					</tr>
					<tr>
						<td><b>下午6</b></td>
						<td></td>
						<td></td>
						<td>禁排</td>
						<td></td>
						<td></td>
					</tr>
					<tr>
						<td><b>下午7</b></td>
						<td></td>
						<td></td>
						<td>禁排</td>
						<td></td>
						<td></td>
					</tr>
					<tr>
						<td><b>下午8</b></td>
						<td></td>
						<td></td>
						<td>禁排</td>
						<td></td>
						<td></td>
					</tr>

				</table>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
	var JSdata = [ {
		"id" : 1,
		"JS" : "刘彦妮"
	}, {
		"id" : 2,
		"JS" : "石海深"
	}, {
		"id" : 3,
		"JS" : "王烨芳"
	}, {
		"id" : 4,
		"JS" : "庞燕"
	}, {
		"id" : 5,
		"JS" : "李嘉伶"
	}, {
		"id" : 6,
		"JS" : "王红"
	}, {
		"id" : 7,
		"JS" : "陈伟"
	}, {
		"id" : 8,
		"JS" : "周麟娅"
	}, {
		"id" : 9,
		"JS" : "卞炜"
	}, {
		"id" : 10,
		"JS" : "张卫伟"
	}, {
		"id" : 11,
		"JS" : "蔡红"
	} ];//模拟教师数据
	var kc = [ {
		"KMMC" : "C语言程序设计",
		"RJBJ" : "15软件",
		"CDYQ" : "机房",
		"YPJS" : "4",
		"MPJS" : "0",
		"SKZS" : "1-18"
	} ];
	$(document).ready(function() {
		loaddata();
		loaddata2();
	});
	function loaddata() {
		$('#List').treegrid({
			data : null,
			idField : 'id',
			treeField : 'JS',
			title : '',
			width : '100%',
			fit : true,
			rownumbers : true,
			fitColumns : true,
			columns : [ [ {
				title : '教师',
				field : 'JS',
				width : fillsize(0.8)
			} ] ]
		});
		$('#List').treegrid('loadData', JSdata);
	}

	function loaddata2() {
		$('#List2').datagrid({
			data : null,
			title : '',
			fit : true,
			width : '100%',
			nowrap : false,
			striped : true,
			//pageSize:20,
			//pageNumber:1,
			showFooter : true,
			rownumbers : true,
			singleSelect : true,
			//pagination:true,
			fitColumns : true,
			columns : [ [ {
				field : 'KMMC',
				title : '科目名称',
				width : fillsize(0.07),
				align : 'left'
			}, {
				field : 'RJBJ',
				title : '任教班级',
				width : fillsize(0.07),
				align : 'left'
			}, {
				field : 'CDYQ',
				title : '场地要求',
				width : fillsize(0.07),
				align : 'left'
			}, {
				field : 'YPJS',
				title : '已排节数',
				width : fillsize(0.07),
				align : 'left'
			}, {
				field : 'MPJS',
				title : '末排节数',
				width : fillsize(0.07),
				align : 'left'
			}, {
				field : 'SKZS',
				title : '授课周数',
				width : fillsize(0.07),
				align : 'left'
			} ] ],
			onClickRow : function(rowIndex, rowData) {
				dt = rowData;//一行的数据
			},
			//双击某行时触发
			onDblClickRow : function(rowIndex, rowData) {
				dt = rowData;//一行的数据
				doToolbar("edit");
			},
			//成功加载时
			onLoadSuccess : function(data) {

			}
		});
		$('#List2').datagrid('loadData', kc);
	}
</script>
</html>