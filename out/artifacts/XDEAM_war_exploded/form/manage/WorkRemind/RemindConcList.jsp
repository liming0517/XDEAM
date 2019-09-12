<%
/*
文件说明
Create date: 2012.09.24
页面类型:联系单
创建人：zhangjiping
*/
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<!-- 设置编码格式 -->
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!--用户区 ：JAVA引用-这里可以列出需要引用的包  -->
<%@ page import="com.pantech.base.common.tools.MyTools" %>
<%@ page import="com.pantech.base.common.tools.PublicTools"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.*"%>
<%@ page import="com.pantech.base.common.db.DBSource" %>
<!--用户区 ：JAVA引用-结束  -->	
<html>
<head>
	<title></title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<!--引用 JQuery 专用的文件 -->
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/icon.css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
	<!--引用 JQuery 专用的文件结束 -->
</head>

<form method="Post" id="form1" ><!-- 定义 easyui界面风格 -->
	<body class="easyui-layout">
		<div region = "north" align = "left" title ="我的联系单"style = "background:#fafafa;height:119px;width:100%;">
			<div  style="padding:5px;background:#fafafa;width:100%;">
				<a href="#" onclick="doToolbar(this.id);" id="new"  class="easyui-linkbutton" plain="true" iconCls="icon-new">新建</a>
				<a href="#" onclick="doToolbar(this.id);" id="edit"  class="easyui-linkbutton" plain="true" iconCls="icon-edit">编辑</a>
				<a href="#" onclick="doToolbar(this.id);" id="print"  class="easyui-linkbutton" plain="true" iconCls="icon-print">打印</a>
				<!-- 根据用户需求 增加列表导出功能，此处增加导出列表按钮修改人：张继平 -->
				<a href="#" onclick="doToolbar(this.id)" id="export" class="easyui-linkbutton" iconCls="icon-site" plain="true">导出列表</a>
			</div>
			<table width="100%" class="tablestyle"><!-- 引用表格样式 -->
				<tr>
					<td class="titlestyle" >单位名称</td><!-- 引用标题样式 -->
					<td>
						<input id="icWC_NAME" name="icWC_NAME"  style="width:190px;" type="text" maxlength="100"/>
					</td>
					<td class="titlestyle">联系时间</td>
					<td>
						<input class="easyui-datebox"id="icWC_TIME1" name="icWC_TIME1" style="width:85px;"  required="false"  editable="false"/>
							---
						<input class="easyui-datebox"id="icWC_TIME2" name="icWC_TIME2" style="width:85px;"  required="false"  editable="false"/>
					</td>
					<td class="titlestyle">联系方式</td>
					<td >
						<select id="icWC_LXFS" name="icWC_LXFS" class="easyui-combobox" style="width:190px;" required="false">
							<option value="请选择">请选择</option>
							<option value="1">电话联系</option>
							<option value="0">实地联系</option>
						</select>
					</td>
					<td align = "left">
						<a href = "#" onclick = "doToolbar(this.id);" id = "que" class = "easyui-linkbutton" iconCls = "icon-search" plain = "true">查询</a>
					</td>
				</tr>

				<tr>
					<td class="titlestyle">联系人</td>
					<td>
						<input id="icWC_LXR" name="icWC_LXR"  style="width:190px;" type="text" maxlength="10"/>
					</td>
					<td class="titlestyle">受访人</td>
					<td>
						<input id="icWC_SFR" name="icWC_SFR"  style="width:190px;" type="text" maxlength="10"/>
					</td>
					<td class="titlestyle">是否存档</td>
					<td colspan="2">
						<select id="icWC_ISNO" name="icWC_ISNO" class="easyui-combobox" style="width:190px;" required="false">
							<option value="请选择">请选择</option>
							<option value="0">否</option>
							<option value="1">是</option>
						</select>
					</td>
				</tr>
			</table>
		</div>
		<!--用户区 ：1d-数据输入-这里可以放置数据录入控件或查询条件 仅需对tr 和 td 做处理  -->
		<div region="center" style="background:#fafafa;width:100%">
			<table id="formlist" name="formlist" width="100%">
			</table>
		</div>
	</body>
	<!--隐含域,保存参数值  -->
	<input id="active" name="active" type="hidden" >
</form>

<script type = "text/JavaScript">
	var ContextPath = "<%=request.getContextPath()%>"; //当前URL根路径 
	var USERCODE = "<%=MyTools.getSessionUserCode(request)%>"; //登陆用户
	var icWC_CODE="";//编号
	var USE="";//数据库录入人
	var FKKSFZR="";//反馈科室负责人
	var FKR="";//反馈人
	var LXR="";//联系人
	var sqlstate="";//接收后台传来的导出列表查询SQL语句

	$(document).ready(function(){
		loadGrid(); //加载数据
	});

	//加载数据
	function loadGrid(){
		$('#formlist').datagrid({
			url: '<%=request.getContextPath()%>/Svl_RemindConc',
			queryParams:{"active":"que","icWC_NAME":$('#icWC_NAME').val(),"icWC_TIME1":$('#icWC_TIME1').datebox('getValue'),
				"icWC_TIME2":$('#icWC_TIME2').datebox('getValue'),"icWC_LXFS":$('#icWC_LXFS').combobox('getValue'),
				"icWC_LXR":$('#icWC_LXR').val(),"icWC_SFR":$('#icWC_SFR').val(),"icWC_ISNO":$('#icWC_ISNO').combobox('getValue')
			},
			width: '100%',//表格宽度
			nowrap: false,//不自动换行
			fit:true,//自动适应父容器大小
			showFooter:true,
			striped:true,//
			pagination:true,//显示底部分页工具栏
			pageSize:20,//每页显示数据行数
			singleSelect:true,//只能单选行
			pageNumber:1,//起始页
			rownumbers:true,//显示序号
			fitColumns: true,//自动适应列宽
			columns:[[
				{field:'联系单位名称',title:'联系单位名称',width:100},
				{field:'联系时间',title:'联系时间',width:100},
				{field:'联系人员',title:'联系人',width:100},
				{field:'联系人',title:'联系人',width:0,hidden:true},
				{field:'受访人',title:'受访人',width:100},
				{field:'联系方式',title:'联系方式',width:100},
				{field:'存档日期',title:'存档日期',width:100},
				{field:'USERCODE',title:'USERCODE',width:0,hidden:true},
				{field:'反馈科室负责人',title:'反馈科室负责人',width:0,hidden:true},
				{field:'反馈人',title:'反馈人',width:0,hidden:true}
			]],
			//单击触发
			onClickRow:function(rowIndex,rowData){
				icWC_CODE = rowData.编号;//单击时 传递编号
				USE = rowData.USERCODE;//单击时 传递  当前用户编号
				FKR = rowData.反馈人;//单击时 传递反馈人 
				FKKSFZR = rowData.反馈科室负责人;//单击时传递 反馈科室负责人
				LXR = rowData.联系人;//单击时 传递 联系人 
				doToolbarAuth();//单击时  调用  doToolAuth函数  判断  按钮是否可用
			},
			//双击某行时触发
			onDblClickRow:function(rowIndex,rowData){
				//if(USERCODE==USE||USERCODE==LXR){//如果当前用户是 数据录入用户  那么 可以编辑 
					doToolbar("edit");//
					//return;
				//}
				//切割数据库中负责人
				//if(FKR!=""&&FKR!=null){//判断反馈人 不为空  时  执行
				//	var a=FKR.split(",");
				//	for(var i=0;i<a.length;i++){//如果当前登录用户 是反馈人 时  双击 可编辑
				//		if(USERCODE==a[i]){
				//			doToolbar("edit");
				//			return;
				//		}
				//	}
				//}
				//切割数据库中反馈科室负责人
				//if(FKKSFZR!=""&&FKKSFZR!=null){//判断 反馈科室负责人 不为空时  执行 
				//	var b=FKKSFZR.split(",");
				//	for(var j=0;j<b.length;j++){
				//		if(USERCODE==b[j]){//当前登录用户  是  反馈科室负责人时  双击 可编辑
				//			doToolbar("edit");
				//			return;
				//		}
				//	}
				//}
			},
			onLoadSuccess:function(data){//查询成功后 执行
				$('#formlist').datagrid('selectRow',0);//选择第一行
				trow=$('#formlist').datagrid('getSelected');//获得行内容
				if(trow!=null){
					sqlstate=trow.listsql;
				}
				$('#formlist').datagrid('unselectAll');//取消选择
			}
		});
	}
	//按钮全暗
	function doToolbarAuth1(){
		$("#edit").attr({"disabled":"disabled"});//编辑按钮不可用（暗）
	}
	//按钮亮暗控制
	function doToolbarAuth(){
		//doToolbarAuth1();
		if(USERCODE==USE||USERCODE==LXR){//当前用户 是 数据录入用户时   可编辑
			$("#edit").removeAttr("disabled");//编辑按钮可用（亮）
			return;
		}
		//切割数据库中负责人
		if(FKR!=""&&FKR!=null){
			var a=FKR.split(",");
			for(var i=0;i<a.length;i++){
				if(USERCODE==a[i]){//当前用户  是 反馈人 时  编辑 可用 
					$("#edit").removeAttr("disabled");//编辑按钮可用（亮）
					return;
				}
			}
		}
		//切割数据库中反馈科室负责人
		if(FKKSFZR!=""&&FKKSFZR!=null){
			var b=FKKSFZR.split(",");
			for(var j=0;j<b.length;j++){
				if(USERCODE==b[j]){//当前用户是 反馈科室负责人时   编辑按钮可用 
					$("#edit").removeAttr("disabled");//编辑按钮可用（亮）
					return;
				}
			}	
		}
	}

	//点击 按钮 事件
	function doToolbar(iToolbar){
		//新建
		if (iToolbar == "new"){
			window.open("<%=request.getContextPath()%>/form/manage/WorkRemind/RemindConcInfo.jsp");
		}
		//编辑
		if (iToolbar == "edit"){
			if(icWC_CODE == ""){
				alertMsg("请选择一行数据");
				return;
			}
			window.open("<%=request.getContextPath()%>/form/manage/WorkRemind/RemindConcInfo.jsp?icWC_CODE=" + icWC_CODE);
		}
		//打印
		if (iToolbar=="print"){
			if(icWC_CODE==""){
				alertMsg("请选择一行数据");
				return;
			}
			window.open("RemindConcPrint.jsp?icWC_CODE="+icWC_CODE);
		}
		if(iToolbar=="print"){
		 	$('#active').val("print");
			loadGrid();
		}
		//查询
		if(iToolbar=="que"){
		 	$('#active').val("que");
			loadGrid();
		}
		if(iToolbar=="export"){
			window.open('../../Functions/PrintSql.jsp');
		}
	}
</script>
</html>