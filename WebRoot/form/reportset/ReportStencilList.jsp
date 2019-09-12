<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.base.common.tools.PublicTools"%>
<%@ page import="com.pantech.src.develop.Logs.*"%>
<%@ page import="com.pantech.base.common.tools.*"%>
<%@ page import="com.pantech.src.develop.store.user.*"%>
<%@ page import="com.pantech.src.develop.manage.workremind.WorkRemind"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.*"%>
<%@ page import="com.pantech.base.common.db.DBSource"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
    <link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/css/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css"
		href="<%=request.getContextPath()%>/css/themes/icon.css">
	<script type="text/javascript"
		src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
	<script type="text/javascript"
		src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
	<script type="text/javascript"
		src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript"
		src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>

	  <body class="easyui-layout">
		<div region="center" style="background: #fafafa; width: 100%;height:100%">
				<table id="userreportList" name="userreportList"></table>
		</div>
	  </body>
	
  	  
  	   
  <script type="text/javascript">
  	var curuserreportid="";
  	var rows = null;
  
  	var address="";
  	$(document).ready(function(){
  		loadGrid();
  	});
  	
	

  	function loadGrid(){
  		$('#userreportList').datagrid({
				title:'我的模版列表',
				iconCls:'icon-edit',
				singleSelect:true,
				url: '<%=request.getContextPath()%>/Svl_userreportList',
				queryParams:{"active":"questencil"},
				width: '100%',
				nowrap: false,
				fit:true,
				showFooter:true,
				striped:true, //隔行变色
				pagination:true,
				pageSize:10,
				singleSelect:true,
				pageNumber:1,
				rownumbers:true,
				fitColumns: true,	
				toolbar: [{
					text:'显示报表',
					iconCls:'icon-edit',
					handler:function(){
						if(curuserreportid!=""){
							location.href="<%=request.getContextPath()%>/JSONDrawExcel/ReportExcel.jsp?userreportid="+curuserreportid;
						}else{
							alertMsg("请选择一条数据");
						}
					}
				}],
				columns:[[
					{field:'用户报表id',title:'用户报表id',width:100,hidden:true},
					{field:'报表id',title:'报表id',width:100,hidden:true},
					{field:'样式id',title:'样式id',width:100,hidden:true},
					{field:'名称',title:'名称',width:100},
					{field:'描述',title:'描述',width:100},
					{field:'类型',title:'类型',width:100,formatter: function(value,row,index){
						if(row.用户编号=="all"){
							return "系统报表";
						}else{
							return "用户报表";
						}
					}},
					{field:'创建时间',title:'创建时间',width:100}
				]],
				onClickRow:function(rowIndex,rowData){
					curuserreportid=rowData.用户报表id;
				},
				onLoadSuccess:function(){
					
				}
			});
  	}
  			
  </script>
  
</html>
