<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/icon.css">
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/publicScript.js"></script>
	</head>
	<body class="easyui-layout" >
		<div data-options="region:'north'" title="校区信息" style="background:#fafafa;height:91px;">
			<table>
    		<tr>
				<td><a href="#" id="new1" class="easyui-linkbutton" plain="true" iconcls="icon-new" onClick="doToolbar(this.id);" title="">新建</a></td>
				<td><a href="#" id="edit1" class="easyui-linkbutton" plain="true" iconcls="icon-edit" onClick="doToolbar(this.id);" title="">编辑</a></td>
				<td><a href="#" id="delxq" onclick="doToolbar(this.id)"   class="easyui-linkbutton"  data-options="plain:true,iconCls:'icon-cancel'">删除</a></td>
			</tr>
		</table>
		<table class="tablestyle" width="100%">
			<tr>
				<td class="titlestyle" width='150px'>
					校区名称
				</td>
				<td class="titlestyle">
					<input class="easyui-combobox" editable="false" id="comboxxqmc">
				</td>
				<td width='150px' align="center" >
						<a href="#" onclick="doToolbar(this.id)" id="search" class="easyui-linkbutton" plain="true" iconcls="icon-search" >查询</a>
				</td>
			</tr>
		</table>

		</div>
		<div data-options="region:'center'">
		<table id='list1' class="tablestyle" width="100%"></table>
			<form id="form2"   method='post'>
			<input type="hidden" id="active1" name="active1">
			<input type="hidden" id="numberxq1" name="numberxq1">
			</form>
		</div>
		<div id="xjxq"  title="新建校区">
		<form id="form1"   method='post'>
		<table  class = "tablestyle"width="100%" >
			<tr>
				<td class="titlestyle">校区代码</td><td class="titlestyle"><input id="XQDM" name="XQDM" class="easyui-textbox"  style="width:150px;" maxlength="4" required="true"></td>
			</tr>
			<tr>
				<td class="titlestyle">校区名称</td><td class="titlestyle"><input id="XQMC" name="XQMC" class="easyui-textbox" style="width:150px;" maxlength="4" required="true"></td>
			</tr>
			<tr>
				<td class="titlestyle">校区地址</td><td class="titlestyle"><input id="XQDZ" name="XQDZ" class="easyui-textbox" style="width:150px;" maxlength="4" required="true"></td>
			</tr>
		</table>
		<input type="hidden" id="active" name="active">
		<input type="hidden" id="numberxq" name="numberxq">
		<input type="hidden" id="namexq" name="namexq">
		</form>
		</div>
  </body>

<script>
$(document).ready(function(){
		loadGrid();
		initDialog();
});
var xqh="";
var numberxq="";
var numberjz="";
var xqdm="";
var xqdz="";
var xqmc=""; 
var namexq="";
var query="";
var addressxq="";
var row="";
var type="";
//加载表格
function loadGrid(){
	$('#list1').datagrid({
		url:"<%=request.getContextPath()%>/Svl_building?active=list1&XQDM="+query,
		loadMsg : "信息加载中请稍后!",//载入时信息
		width:'100%',
		nowrap: false,
		fit:true, //自适应父节点宽度和高度
		striped:true,
		singleSelect:true,
		rownumbers:true,
		fitColumns: true,
		columns:[[
			{field:'XQDM',title:'校区代码',width:fillsize(0.1),align:'center'},
			{field:'XQMC',title:'校区名称',width:fillsize(0.3),align:'center'},
			{field:'XQDZ',title:'校区地址',width:fillsize(0.5),align:'center'},
			{field:'col4',title:'操作',width:fillsize(0.1),align:'center',
				formatter:function(value,rowData,rowIndex){
					return  '<input id="pracDetail" type="button" value="[详情]" onclick="openlist(\'' + rowData.XQDM+ '\',\'' + rowData.XQMC + '\');" style="cursor:pointer;">';
				}}
			]],	
		onDblClickRow:function(rowIndex, rowData){
			openlist(rowData.XQDM,rowData.XQMC);
		},
		onClickRow:function(rowIndex, rowData){
			row=rowData;
			numberxq=rowData.XQDM;
			namexq=rowData.XQMC;
			addressxq=rowData.XQDZ;
		},
		onLoadSuccess: function(data){
			comboxxqmc();		
		},
			onLoadError:function(none){				
		}
	});		
}

//弹窗
function initDialog(){
	$('#xjxq').dialog({   
		width: 350,//宽度设置   
		height: 145,//高度设置 
		modal:true,
		closed: true,   
		draggable:false,//是否可移动dialog框设置
		toolbar:[{
				//保存编辑
			text:'保存',
			iconCls:'icon-save',
			handler:function(){
				//传入save值进入doToolbar方法，用于保存
				doToolbar(type);
			}
		}],
			//打开事件
		onOpen:function(data){
		},
		//读取事件
		onLoad:function(data){
		},
		//关闭事件
		onClose:function(data){	
		}
	});
}

function doToolbar(iToolbar){
//新建
	if(iToolbar=="new1"){
		$('#xjxq').dialog({title: '新建校区'});
		empty();
		numberxq="";
		$('#xjxq').dialog("open");
		type="savexq";
	}
//保存
	if(iToolbar=="savexq"){
		$('#active').val(iToolbar);
		$("#form1").submit();
	}
//编辑	
	if(iToolbar=="edit1"){
		if(numberxq==""){
			alertMsg("请选择校区");
			return;
		}
		$('#xjxq').dialog({title: '编辑校区'});
		$('#xjxq').dialog("open");
		type="updatexq";
		if(row!=undefined && row!=''){
			$('#form1').form('load', row);
				}
	}
//保存编辑内容
	if(iToolbar=="updatexq"){
		$('#active').val(iToolbar);
		$("#numberxq").val(numberxq);
		$("#namexq").val(namexq);
		$("#form1").submit();
	}
//查询
	if(iToolbar=="search"){
		query=$('#comboxxqmc').combobox('getValue');
		loadGrid();
		numberxq=="";
	}
//删除
	if(iToolbar=="delxq"){
		if(numberxq==""){
			alertMsg("请选择校区");
			return;
		}
		$("#active1").val(iToolbar);
		$("#numberxq1").val(numberxq);
		$("#form2").submit();
	}
}
//表单提交
$('#form1').form({
	url:'<%=request.getContextPath()%>/Svl_building', 
	success:function(data){
		var	data1=eval(data);
		if(data1[0].MSG=="保存成功"){
			$('#xjxq').dialog("close");
			loadGrid();
			numberxq=="";
		}else if(data1[0].MSG=="修改成功"){
			$('#xjxq').dialog("close");
			loadGrid();
			numberxq=="";
		}else{
		   		alertMsg(data1[0].MSG);
		   	}
		   		
	}
});
$('#form2').form({
	url:'<%=request.getContextPath()%>/Svl_building', 
	success:function(data){
		var	data1=eval(data);
		if(data1[0].MSG=="删除成功"){
			loadGrid();
			$("#active").val("");
			empty();
			numberxq=="";
			$('#xjjs').dialog("close");
		}else{
		   	alertMsg("删除失败");
		}
	}
});
//校区下拉菜单
function comboxxqmc(){
	$('#comboxxqmc').combobox({
		valueField:"校区代码",
		textField:"校区名称",
		editable:false,
		url:"<%=request.getContextPath()%>/Svl_building?active=comboxxqmc",
		onLoadSuccess:function(){
		},
		onSelect:function(){
		numberxq="";
		type="";
		}
	});		
}
//操作按钮
function openlist(xqdm,xqmc){
		location.href = '<%=request.getContextPath()%>/form/building/building1.jsp?XQDM='+xqdm+"&XQMC="+xqmc;
	}
//清空赋值
function empty(){
	$('#XQDM').textbox('setValue', '');
	$('#XQMC').textbox('setValue', '');
	$('#XQDZ').textbox('setValue', '');
}
</script>  
  
</html>
				