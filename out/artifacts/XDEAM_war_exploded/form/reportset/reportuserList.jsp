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
	  <div id="userreporedit" name="userreporedit" title="系统报表编辑" class="easyui-window" style="width:500px;"  resizable=false closed=true modal=true>
	  	<form id="form1" name="form1" method="POST">
				<center>
					<table class="tablestyle" style="width:100%" cellpadding="4">
						<tr>
							<td>名称</td>
							<td align="left">
								<input  style="width:100%;" maxlength=50 id="userreport" name="userreport">
							</td>
							<td>类型</td>
							<td align="left">
							<select
								class="easyui-combobox" name="template" id="template" style="width: 180px;">
							</select>
							</td>
						</tr>
						<tr>
							<td>样式</td>
							<td align="left">
								<select
									class="easyui-combobox" name="stylereport" id="stylereport" style="width: 180px;">
								</select>
							</td>
							<td>描述</td>
							<td align="left">
								<input  style="width:100%;" maxlength=200 id="describe" name="describe">
							</td>
						</tr>
					</table>
					<input type="hidden" id="active" name="active"/>
					<input type="hidden" id="userreportid" name="userreportid"/>
				</center>
		</form>
  	  </div>

  	  
  	   
  <script type="text/javascript">
  	var curuserreportid="";
  	var rows = null;
  	var view=<%=PublicTools.getComboboxOptions("V_自定义报表主表","报表id","报表名称","where 状态='1'",request)%>;
  	var style=<%=PublicTools.getComboboxOptions("V_报表信息主表","模版id","模版名称","where 状态='1'",request)%>;
  	var address="";
  	$(document).ready(function(){
  		loadGrid();
  	});
  	
  	$('#userreporedit').dialog({
	});
	
	$('#template').combobox({
		data:view,
		valueField:'comboValue',
		textField:'comboName'
	});
		
	$('#stylereport').combobox({
		data:style,
		valueField:'comboValue',
		textField:'comboName'
	});
	 
	
		
	$('#form1').form({
			//定位到servlet位置的url
			url:'<%=request.getContextPath()%>/Svl_userreportList', 
			//当点击事件后触发的事件
		    onSubmit: function(data){
		    	//alert();
		    }, 
		    //当点击事件成功提交后触发的事件
		    success:function(data){
				//alert();
				var json = eval("("+data+")");
				$("#userreporedit").window('close');
				curuserreportid=json[0].ID;
				location.href="<%=request.getContextPath()%>/form/reportset/reportuserinfo.jsp?userreportid="+curuserreportid;
				//loadGrid();
		    }
	});

	
 	
		
  	function loadGrid(){
  		$('#userreportList').datagrid({
				title:'系统报表列表',
				iconCls:'icon-edit',
				singleSelect:true,
				url: '<%=request.getContextPath()%>/Svl_userreportList',
				queryParams:{"active":"que"},
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
					iconCls: 'icon-add',
					text:'添加',
					handler: function(){
						doToolbar("add");
					}
				},{
					text:'基本信息维护',
					iconCls:'icon-edit',
					handler:function(){
						doToolbar("edit");
					}
				},{
					text:'编辑',
					iconCls:'icon-edit',
					handler:function(){
						if(curuserreportid!=""){
							location.href="<%=request.getContextPath()%>/form/reportset/reportuserinfo.jsp?userreportid="+curuserreportid;
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
					{field:'报表名称',title:'模版名称',width:100},
					{field:'模版名称',title:'样式名称',width:100},
					{field:'描述',title:'描述',width:100},
					{field:'状态',title:'状态',width:100,formatter: function(value,row,index){
						if (value=="1"){
							return "启用";
						}
						if (value=="2"){
							return "禁用";
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
  			
  	function doToolbar(iToolbar) {
  		if(iToolbar=="add"){
			$('#form1').form('clear');
  			$("#active").val("add");
  			$("#template").combobox('enable');
  			$('#userreporedit').dialog({
					toolbar:[{
						text:'确定',
						iconCls:'icon-save',
						handler:function(){
							$("#form1").submit();
						}
					}]
			});
  			$("#userreporedit").window('open');

  		}
  		if(iToolbar=="edit"){
  			if(curuserreportid!=""){
  				rows = $('#userreportList').datagrid('getSelected');
  				$("#userreport").val(rows.名称);
  				$("#describe").val(rows.描述);
  				$("#userreportid").val(rows.用户报表id);
  				$("#active").val("edit");
  				$("#template").combobox('setValue', rows.报表id);
  				$("#stylereport").combobox('setValue', rows.样式id);
  				$("#template").combobox('disable');
  				$('#userreporedit').dialog({
						toolbar:[{
							text:'确定',
							iconCls:'icon-save',
							handler:function(){
								$("#form1").submit();
							}
						},{
								text:'启用',
								iconCls:'icon-ok',
								handler:function(){
									doToolbar("qiyong");
								}
							},{
								text:'禁用',
								iconCls:'icon-cancel',
								handler:function(){
									doToolbar("cancel");
								}
							}]
				});
  				$("#userreporedit").window('open');
  			}else{
  				alertMsg("请选择一条数据");
  			}
  		}
  		if(iToolbar=="cancel"){
  			if(curuserreportid!=""){
  				rows = $('#userreportList').datagrid('getSelected');
				$.ajax({
					type: "POST",
					url: '<%=request.getContextPath()%>/Svl_userreportList',   //设置 SVL
					async:false,
					data: "active=cancel&userreportid=" + rows.用户报表id,  // 设置模式，一般为view ，主关键字值		
					dataType: 'json',
					success: function(datas){
						$("#userreporedit").window('close');
						loadGrid();
						showMsg("该报表已禁用");
					}
				});
  			}else{
  				alertMsg("请选择一条数据");
  			}
  		}
  		if(iToolbar=="qiyong"){
  			if(curuserreportid!=""){
  				rows = $('#userreportList').datagrid('getSelected');
				$.ajax({
					type: "POST",
					url: '<%=request.getContextPath()%>/Svl_userreportList',   //设置 SVL
					async:false,
					data: "active=qiyong&userreportid=" + rows.用户报表id,  // 设置模式，一般为view ，主关键字值		
					dataType: 'json',
					success: function(datas){
						$("#userreporedit").window('close');
						loadGrid();
						showMsg("该报表已启用");
					}
				});
  			}else{
  				alertMsg("请选择一条数据");
  			}
  		}
  	}
  </script>
  
</html>
