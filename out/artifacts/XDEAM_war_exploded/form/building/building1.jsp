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
  
  <body class="easyui-layout">
  <div data-options="region:'north'" title="<%=request.getParameter("XQMC")%>教学楼" style="background:#fafafa;height:91px;">
     <!--教学楼信息  -->   
	   	<table>
    		<tr>
				<td><a href="#" id="new2" class="easyui-linkbutton" plain="true" iconcls="icon-new" onClick="doToolbar(this.id);" title="">新建</a></td>
				<td><a href="#" id="edit2" class="easyui-linkbutton" plain="true" iconcls="icon-edit" onClick="doToolbar(this.id);" title="">编辑</a></td>
				<td><a href="#" id="deljz" onclick="doToolbar(this.id)"   class="easyui-linkbutton" href="javascript:void(0);" data-options="plain:true,iconCls:'icon-cancel'">删除</a></td>
				<td><a href="#" id="back" class="easyui-linkbutton" plain="true" iconcls="icon-back" onClick="doToolbar(this.id);" title="">返回</a></td>
			</tr>
		</table>
		<table class="tablestyle" width="100%">
			<tr>
				<td class="titlestyle" width='150px'>
					教学楼名称
				</td>
				<td class="titlestyle">
					<input class="easyui-combobox" editable="false" id="comboxjzwmc">
				</td>
				<td class="titlestyle"  width='150px;' align="center" >
					<a href="#" id="search" class="easyui-linkbutton" plain="true" iconcls="icon-search" onClick="doToolbar(this.id);" title="">查询</a>
				</td>
			</tr>
		</table>
    </div>
    <div data-options="region:'center'">     
    			<table id='list2' class="tablestyle" width="100%">
    			</table>
    <!--新建楼  -->
<div id="xjl" style="overflow:hidden;" title="新建教学楼">
<form id="form2"  method='post' >
	<table class="tablestyle" width="100%" >
		<tr>
			<td class="titlestyle">建筑物号</td><td class="titlestyle"><input id="JZWH" name="JZWH" class="easyui-textbox" style="width:150px;" maxlength="4" required="true"></td>
		</tr>
		<tr>
			<td class="titlestyle">建筑物名称</td><td class="titlestyle"><input id="JZWMC" name="JZWMC" class="easyui-textbox" style="width:150px;" maxlength="4" required="true"></td>
		</tr>
		<tr>
			<td class="titlestyle">校区名称</td>
			<td class="titlestyle">
			<%=request.getParameter("XQMC")%>
			<input type="hidden" id="XQH" name="XQH" value="<%=request.getParameter("XQDM")%>" />
			</td>
		</tr>
	</table>
	<input type="hidden" id="active" name="active">
	<input type="hidden" id="numberjz" name="numberjz">
	<input type="hidden" id="namejz" name="namejz">
</form>
</div>
	<form id="form3"   method='post'>
	<input type="hidden" id="active1" name="active1">
	<input type="hidden" id="numberjz1" name="JZWH">
	<input type="hidden" id="numberxq1" name="XQH">
	</form>
	</div>
  </body>
  <script>
var numberxq = '<%=request.getParameter("XQDM")%>';
var xqmc='<%=request.getParameter("XQMC")%>';
$(document).ready(function(){
		loadGrid();
		initDialog();
});
var numberjz="";
var namejz="";
var addressjz="";
var jzh="";
var type="";
var row="";
function loadGrid(){
	$('#list2').datagrid({
		url:"<%=request.getContextPath()%>/Svl_building?active=list2&XQH="+numberxq+"&JZWH="+jzh,
		loadMsg : "信息加载中请稍后!",//载入时信息
		width : '100%',//宽度
		rownumbers: true,
		animate:true,
		striped : true,//隔行变色
		singleSelect : true,//单选模式
		fit:true,
		fitColumns: true,//设置边距
			columns:[[
				{field:'JZWH',title:'教学楼号',width:fillsize(0.2),align:'center'},
				{field:'JZWMC',title:'教学楼名称',width:fillsize(0.4),align:'center'},
				{field:'XQMC',title:'校区名称',width:fillsize(0.3),align:'center'},
				{field:'col4',title:'操作',width:fillsize(0.1),align:'center',
					formatter:function(value,rowData,rowIndex){
						return  '<input id="pracDetail" type="button" value="[详情]" onclick="openlist(\'' + rowData.JZWH + '\',\'' + rowData.JZWMC+ '\');" style="cursor:pointer;">';
				}}
			]],
		onDblClickRow:function(rowIndex, rowData){
			openlist(rowData.JZWH,rowData.JZWMC );
		},
		onClickRow:function(rowIndex, rowData){
			row=rowData;
			numberjz=rowData.JZWH;
			namejz=rowData.JZWMC;
		},
		onLoadSuccess: function(data){
			comboxjzwmc();				
		},
			onLoadError:function(none){				
		}
	});
}
function initDialog(){
	$('#xjl').dialog({   
		width: 350,//宽度设置   
		height: 140,//高度设置 
		modal:true,
		closed: true,   
		cache: false, 
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
		onOpen:function(data){
			$("#XQH").val(xqmc);
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
//表二
//新建
	if(iToolbar=="new2"){
		$('#xjl').dialog({title: '新建教学楼'});
		empty();
		numberjz="";
		$('#xjl').dialog("open");
		type="savejz";
	}
//保存
	if(iToolbar=="savejz"){
		$("#XQH").val(numberxq);
		if($("#JZWH").val()==""||$("#JZWMC").val()==""){
			alertMsg("填写完整");
			return;
		}
		$("#active").val(iToolbar);
		$("#form2").submit();
	}
//编辑
	if(iToolbar=="edit2"){
		if(numberjz==""){
			alertMsg("请选择教学楼");
			return;
		}
		$('#xjl').dialog({title: '编辑教学楼'});
		$('#xjl').dialog("open");
		type="updatejz";
		if(row!=undefined && row!=''){
			$('#form2').form('load', row);
		}
	}
//保存修改
	if(iToolbar=="updatejz"){
		$("#active").val(iToolbar);
		$("#numberjz").val(numberjz);
		$("#namejz").val(namejz);
		$("#form2").submit();	
	}
//返回
	if(iToolbar=="back"){
	location.href = '<%=request.getContextPath()%>/form/building/building.jsp?';
	}
//查询
	if(iToolbar=="search"){
		jzh=$('#comboxjzwmc').combobox('getValue');
		loadGrid();
		numberjz=="";
	}
//删除
	if(iToolbar=="deljz"){
		if(numberjz==""){
		alertMsg("请选择教学楼");
		return;
		}
		$("#active1").val(iToolbar);
		$("#numberxq1").val(numberxq);
		$("#numberjz1").val(numberjz);
		$("#form3").submit();		
		numberjz="";
	}
}
//表单
$('#form2').form({
	url:'<%=request.getContextPath()%>/Svl_building', 
	//当点击事件并成功提交后触发的事件
	success:function(data){
		var	data1=eval(data);
		if(data1[0].MSG=="保存成功"){
			$('#xjl').dialog("close");
			loadGrid();
		 }else if(data1[0].MSG=="修改成功"){
			$('#xjl').dialog("close");
			loadGrid();
		  }else{
		   		alertMsg(data1[0].MSG);
		  }
		}
});
$("#form3").form({
	url:'<%=request.getContextPath()%>/Svl_building',
	success:function(data){
		var	data1=eval(data);
		if(data1[0].MSG=="删除成功"){
			$('#xjl').dialog("close");
			loadGrid();
			numberxq==""; 
		}else{
		   	alertMsg("删除失败");
		}
	} 
});
//下拉菜单
function comboxjzwmc(){
$('#comboxjzwmc').combobox({
	valueField:"建筑物号",
	textField:"建筑物名称",
	editable:false,
	url:"<%=request.getContextPath()%>/Svl_building?active=comboxjzwmc&XQH="+numberxq,
	onLoadSuccess:function(){
	},
	onSelect:function(){
		numberjz="";
	}
});
}
//操作按键
function openlist(jzh,jzwmc){
	location.href = '<%=request.getContextPath()%>/form/building/building2.jsp?XQDM=' + numberxq+"&JZWH="+jzh+"&XQMC="+xqmc+"&JZWMC="+jzwmc;
	}
//清楚
function empty(){
	$('#JZWH').textbox('setValue', '');
	$('#JZWMC').textbox('setValue', '');
}
</script>
</html>
		