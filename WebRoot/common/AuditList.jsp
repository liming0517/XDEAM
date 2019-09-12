

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
	<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	<%@ page import="com.pantech.base.common.tools.MyTools"%>
	<%@ page import="com.pantech.base.common.tools.PublicTools"%>
    <%@ page import="com.pantech.src.develop.Logs.*" %>
    <%@ page import="com.pantech.base.common.tools.*"%>
    <%@ page import="com.pantech.src.develop.store.user.*" %>
    <%@ page import="com.pantech.src.develop.manage.workremind.WorkRemind" %>
    <%@ page import="java.util.Vector"%>
    <%@ page import="java.util.*"%>
    <%@ page import="com.pantech.base.common.db.DBSource" %>
<html>
	<head>
		<title>配置列表</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/icon.css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
	</head>
<jsp:useBean class="com.pantech.base.common.tags.security.PurViewTools" id="pc" scope="page" />
	
		<body class="easyui-layout">
			<form id = "form1" name = "form1" method = "POST">
			<div region = "north" align = "left" title="审核配置"  style = "background:#fafafa;height:90px;width:800px;">
			<!-- 操作按钮区域 -->
			<div>
				<table>
					<tr>
						<td><a  onClick="doToolbar(this.id);" id="new"  class="easyui-linkbutton" plain="true" iconCls="icon-add">新建</a>|</td>
						<td><a  onClick="doToolbar(this.id);" id="edit"  class="easyui-linkbutton" plain="true" iconCls="icon-edit">编辑</a>|</td>
						<td><a  onClick="doToolbar(this.id);" id="cancel"  class="easyui-linkbutton" plain="true" iconCls="icon-cancel">删除</a></td>
					</tr>
				</table>
			</div>
				<!-- 查询条件区域 -->
				<table id = "search" class = "tablestyle" width = "100%">
					<tr>
						<td>
							<label>模块名称</label>
						</td>
						<td>
							<input maxlength="200" id="modename" name="modename"/>
						</td>
						<td>
							<label>审核方式</label>
						</td>
						<td>
							<select
								class="easyui-combobox" name="way" id="way" style="width: 200px;">
								<option value="">请选择</option>
								<option value="001">直线型</option>
								<option value="002">分支型</option>
								<option value="003">并行型</option>
								<option value="004">迭代型</option>
								<option value="004">复合型</option>
							</select>
						</td>
						<td>
							<label>是否重复</label>
						</td>
						<td>
							<select
								class="easyui-combobox" name="repeat" id="repeat" style="width: 200px;">
								<option value="">请选择</option>
								<option value="Y">是</option>
								<option value="N">否</option>
							</select>
						</td>
						<td>
							<a href="#" class="easyui-linkbutton" id="que" name="que" iconCls="icon-search" onClick="doToolbar(this.id)">查询</a>
						</td>
					</tr>
				</table>
			</div>
			<!-- 表单显示区域 -->
			<div region="center" style="background:#fafafa;width:100%;">
	  			<table id="modelist" name="modelist" width="100%"></table>
			</div>
				<input type="hidden" id="active" name="active" ></input>
				<input type="hidden" id="modeid" name="modeid" ></input>
				<input type="hidden" id="edition" name="edition" ></input>
				<input type="hidden" id="USERCODE" name="USERCODE" ></input>
			</form>
		</body>


</html>
<script type = "text/JavaScript">
	var modeid = "";
	var edition="";
	//页面初始化
	$(document).ready(function(){
		loadGrid();
	});
	
	
	$('#form1').form({
		//定位到servlet位置的url
		url: '<%=request.getContextPath()%>/Svl_Auditconfig',
		//当点击事件后触发的事件
	    onSubmit: function(data){
	    	//alert("Submit");
	    }, 
	    //当点击事件成功提交后触发的事件
	    success:function(data){
	    	//alert("success");
	    	var datas = eval("("+data+")");
	    	modeid="";
	    	//alert(modeid);
	    	showMsg(datas[0].MSG);
	    	dosubmit();
	    },
	    onLoadError:function(){
	    	//alert("error");
	    }
	});
		
	function dosubmit(){
		//alert($("#modename").val());
		loadGrid($("#modename").val(),$("#way").combobox("getValue"),$("#repeat").combobox("getValue"));
	}
	
	//获取表格中的数据（传入所有查询参数）
	function loadGrid(icmodename,icway,icrepeat) {
		//alert(icmodename);
		//alert(icway);
		//alert(icrepeat);
		$(function() {
		var lastIndex = -1;
			$('#modelist').datagrid({
				url: '<%=request.getContextPath()%>/Svl_Auditconfig',
				queryParams:{"active":"quemode","modename":icmodename,"way":icway,"repeat":icrepeat},
				title: '审核模块列表',
				width: '100%',
				nowrap: false,
				fit:true,
				showFooter:true,
				striped:true, //隔行变色
				pagination:true,
				pageSize:10,
				//pageList:null,
				singleSelect:true,
				pageNumber:1,
				rownumbers:true,
				fitColumns: true,
				columns:[[
					{field:'模块编号',title:'模块编号',width:100,hidden:true},
					{field:'版本号',title:'版本号',width:100,hidden:true},
					{field:'模块名称',title:'模块名称',width:100},
					{field:'审核方式',title:'审核方式',width:100,formatter:function(value,row,index){
							if(value=="001"){
								return "普通";
							}else if(value=="002"){
								return "流转";
							}
						}
					},
					{field:'重复提交',title:'是否重复',width:100,formatter:function(value,row,index){
							if(value=="Y"){
								return "是";
							}else if(value=="N"){
								return "否";
							}
						}}
				]],
				onClickRow:function(rowIndex,rowData){
					modeid = rowData.模块编号;
					edition=rowData.版本号;
				}
			});
		});
	}
	
	function doToolbar(iToolbar) {
		if(iToolbar=="que"){
			dosubmit();
		}
		if(iToolbar=="new") {
			//location.href="Aduditdf.jsp";
			parent.addTab("新建流程","<%=request.getContextPath()%>/common/Aduditdf.jsp","");
		}
		if(iToolbar=="edit") {
			if(modeid=="") {
				alertMsg("请选择一行数据");
				return;
			}
			//location.href="Aduditdf.jsp?modeid="+modeid+"&edition="+edition;
			parent.addTab("编辑流程","<%=request.getContextPath()%>/common/Aduditdf.jsp?modeid="+modeid+"&edition="+edition,"");
		}
		if(iToolbar=="cancel") {
			if(modeid=="") {
				alertMsg("请选择一行数据");
				return;
			}
			$("#active").val("deletemodeid");
			$("#edition").val(edition);
			$("#modeid").val(modeid);
			ConfirmMsg("你确认要删除该记录吗?","$('#form1').submit();","");
		}
		if(iToolbar=="start") {
			if(modeid=="") {
				alertMsg("请选择一行数据");
				return;
			}
			rows = $('#audit').datagrid('getSelected');
			$('#auditname').combobox('setValue', rows.编号);
			$("#start").window('open');
		}
	}
</script>