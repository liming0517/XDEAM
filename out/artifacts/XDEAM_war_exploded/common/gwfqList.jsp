<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.base.common.tools.PublicTools"%>
<%@ page import="com.pantech.src.develop.Logs.*"%>
<%@ page import="com.pantech.base.common.tools.*"%>
<%@ page import="com.pantech.src.develop.store.user.*"%>
<%@ page import="com.pantech.src.develop.manage.workremind.WorkRemind"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.*"%>
<%@ page import="com.pantech.base.common.db.DBSource"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> 
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
  </head>
  
  <body>
    <table id="auditxf" name="auditxf"></table>
  </body>
  <div id="gwfkedit" name="gwfkedit" title="公文反馈" class="easyui-window"  style="width:450px;height:300px" resizable=true closed=true modal=true>
		<div id="gwfk" name="gwfk" style="width:100%;height:100%"></div>	
  </div>
  <script type="text/javascript">
  	var ywzj='<%=MyTools.StrFiltr(request.getParameter("ywzj"))%>';
  	var xfid="";
  	//var sffk='';
 	$(document).ready(function(){
		loadGrid();
	});
 	
 	
	
 	function loadGrid(){
		$('#auditxf').datagrid({
			title:'公文反馈情况',
			iconCls:'icon-edit',
			singleSelect:true,
			width: '100%',
			nowrap: false,
			fit:true,
			showFooter:true,
			striped:true, //隔行变色
			pagination:true,
			pageSize:10,
			singleSelect:false,
			pageNumber:1,
			rownumbers:true,
			fitColumns: true,
			url: '<%=request.getContextPath()%>/Svl_AuditConduct1',
			queryParams:{"active":"gwfqlist"},
			toolbar:[
				{
					text:'反馈',
					iconCls:'icon-ok',
					handler:function(){
						if(xfid!=""){
							$("#gwfk").html(xfid);
							$("#gwfkedit").window("open");
						}else{
							alertMsg("请选择一条数据");
						}
					}
				}
			],
			columns:[[
				{field:'公文ID',title:'公文ID',width:100,hidden:true},
				{field:'名称',title:'公文名称',width:100},
				{field:'执行人员',title:'执行人员',width:100},
				{field:'接收人员',title:'接收人员',width:100}
			]],
			onClickRow:function(rowIndex,rowData){
				xfid=rowData.反馈内容;
			},
			onLoadSuccess:function(){

			}
		});
	}
 	

 	 
  </script>
</html>
