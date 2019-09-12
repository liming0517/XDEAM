<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!--createTime::2013-11-28
	createUser:luweibin V1.0
	description:招生网站提醒
-->

<%@ page import="com.pantech.base.common.tools.MyTools" %>
<%@ taglib uri="/WEB-INF/taglib/util.tld" prefix="util" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>招生网站</title>
		<!-- JQuery文件引用 开始 -->
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/icon.css">
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
		<!-- JQuery文件引用 结束-->
		<!-- 公共 JS文件引用 开始-->
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
		<!-- 公共 JS文件引用 结束-->
	</head>
	<body class="easyui-layout">
		<div region='north' style="background: #fafafa; height:58px;" title="招生网站提醒">
				<table width='100%' class='tablestyle'>
					<tr>
						<td class="titlestyle">
							网站名称:
						</td>
						<td>
							<input name='icRW_WEBNAME1' id='icRW_WEBNAME1' class='easyui-validatebox'  maxlength="60">
						</td>
						<td class="titlestyle">
							网址:
						</td>
						<td>
							<input name='icRW_WEBADR1' id='icRW_WEBADR1' class='easyui-validatebox'  maxlength="60">
						</td>
						<td class="titlestyle">
							提醒日期:
						</td>
						<td>
							<input id="icRW_TXDATE" name="icRW_TXDATE" class="easyui-datebox" editable='false' >
						</td>
						<td class="titlestyle">
							上报日期:
						</td>
						<td>
							<input id="icRW_SBDATE" name="icRW_SBDATE" class="easyui-datebox" editable='false' >
						</td>
						<td>
							<a id="search" href="#" onClick="doToolbar(this.id)" class="easyui-linkbutton" iconcls="icon-search" plain="true">查询</a>
						</td>
					</tr>
				</table>
			</div>
		</div>

		<div region='center'>
			<table id='recruitWeb'></table>
		</div>
	</body>
</html>
<script type="text/javascript">
var date='<%=StringEscapeUtils.escapeJavaScript(request.getParameter("iDateBegin"))%>';//获取时间
$(document).ready(function() {
	//alert(date);
	loadGrid();//页面加载
});

//页面加载
function loadGrid() {

	initRecruitWeb($('#icRW_WEBNAME1').val(), $('#icRW_WEBADR1').val(),$('#icRW_TXDATE').datebox('getValue'),$('#icRW_SBDATE').datebox('getValue'),date);
}

//初始化表格
function initRecruitWeb(icRW_WEBNAME1, icRW_WEBADR1,icRW_TXDATE,icRW_SBDATE,date) {

	$('#recruitWeb').datagrid( {
		url : '<%=request.getContextPath()%>/Svl_WorkRemind',
		queryParams : 
		{"active" : "que","icRW_WEBNAME1" : icRW_WEBNAME1,
		"icRW_WEBADR1" : icRW_WEBADR1,"icRW_TXDATE":icRW_TXDATE,"icRW_SBDATE":icRW_SBDATE,"date":date},
		loadMsg : "信息加载中请稍侯!",//载入时信息
		width : '100%',//宽度        
		height : 533, //此高度需要根据表格之上的输入区域高度进行调整
		nowrap : false,//截取当数据长度超出列宽时会自动截取
		fit : true,//自适应
		fitColumns : true,//自适应列宽防止出现水平滚动
		striped : true, //隔行变色
		pagination : true,//分页
		//showFooter:true,//显示页脚信息
		pageSize : 20,//每页记录数
		singleSelect : true,//单选模式 这里是指只能选一行不能多选
		pageNumber :1,//当前页码
		rownumbers:true,
		columns : [ [ 
			{field : '网站名称',title : '网站名称',width : fillsize(0.2)}, 
			{field : '网址',title : '网址',width : fillsize(0.2)}, 
			{field : '上报日期',title : '上报日期',width : fillsize(0.2)}, 
			{field : '提醒日期',title : '提醒日期',width : fillsize(0.2)}
		] ],
		//双击某行时触发
		onDblClickRow : function(rowIndex, rowData) {
			window.open(rowData.网址);
		},
		onBeforeLoad : function() {
					

		},
		onClickRow : function(rowIndex, rowData) {
			iKeyCode = rowData.编号;
		},
		onLoadSuccess : function(data) {
			
			//an();
		}
	});
}

//点击查询按钮
function doToolbar(iToolbar) {
	if (iToolbar == 'search') {
		loadGrid();
	}
}


</script>
