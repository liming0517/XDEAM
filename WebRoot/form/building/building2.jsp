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
  <div data-options="region:'north'" title="<%=request.getParameter("JZWMC")%>教室" style="background:#fafafa;height:91px;">
       <!--教室信息  -->     
    	<table>
    		<tr>
				<td><a href="#" id="new3" class="easyui-linkbutton" plain="true" iconcls="icon-new" onclick="doToolbar(this.id);" title="">新建</a></td>
				<td><a href="#" id="edit3" class="easyui-linkbutton" plain="true" iconcls="icon-edit" onclick="doToolbar(this.id);" title="">编辑</a></td>
				<td><a href="#" id="deljs" onclick="doToolbar(this.id)"   class="easyui-linkbutton" href="javascript:void(0);" data-options="plain:true,iconCls:'icon-cancel'">删除</a></td>
				<td><a href="#" id="back" class="easyui-linkbutton" plain="true" iconcls="icon-back" onClick="doToolbar(this.id);" title="">返回</a></td>
			</tr>
		</table>  
		<table class="tablestyle" width="100%">
			<tr>
				<td class="titlestyle" width='150'>
					教室名称
				</td>
				<td class="titlestyle">
					<input class="easyui-combobox" editable="false" id="comboxjsmc">
				</td>
				<td class="titlestyle"  width='150px' align="center" >
					<a href="#" id="search" class="easyui-linkbutton" plain="true" iconcls="icon-search" onClick="doToolbar(this.id);" title="">查询</a>
				</td>
			</tr>
		</table>
    </div>
    <div data-options="region:'center'">     
	<table id='list3' class="tablestyle" width="100%"></table>
	<form id="form4"   method='post'>
		<input type="hidden" id="active1" name="active1">
		<input type="hidden" id="JZWH1" name="JZWH">
		<input type="hidden" id="numberxq1" name="XQH">
		<input type="hidden" id="JSBH1" name="JSBH">
	</form>    		
	</div>
    <!-- 新建教室 -->
<div id="xjjs" style="overflow:hidden;" title="新建教室">
<form id="form3"   method='post' >
	<table class="tablestyle" width="100%" >
		<tr>
			<td class="titlestyle">校区名称</td><td class="titlestyle"><%=request.getParameter("XQMC")%></td>
			<input type="hidden" id="XQDM" name="XQDM"/>
		</tr>
		<tr id="hid">
			<td class="titlestyle">建筑物名称</td><td class="titlestyle"><%=request.getParameter("JZWMC")%></td>
			<input type="hidden" id="JZWH" name="JZWH" />
		</tr>
		<tr>
			<td class="titlestyle">教室编号</td><td class="titlestyle"><input id="JSBH" name="JSBH" class="easyui-textbox" style="width:150px;" maxlength="4" required="true"></td>
		</tr>
		<tr>
			<td class="titlestyle">教室名称</td><td class="titlestyle"><input id="JSMC" name="JSMC" class="easyui-textbox" style="width:150px;" maxlength="4" required="true"></td>
		</tr>
		<tr>
			<td class="titlestyle">教室类型</td><td class="titlestyle"><input id="JSLXDM" name="JSLXDM" class="easyui-combobox" panelHeight="105" editable="false" style="width:150px;"></td>
			<input type="hidden" id="LXBH" name="LXBH" />
		</tr>
		<tr>
			<td class="titlestyle" >是否可用</td>
			<td class="titlestyle">
				<select name="SFKY" id="SFKY" class="easyui-combobox" panelHeight="auto" style="width:150px;">
					<option value="1">是</option>
					<option value="2">否</option>
				</select>
			</td>
		</tr>
	</table>
		<input type="hidden" id="active" name="active" >
		<input type="hidden" id="numberjs" name="numberjs">
		<input type="hidden" id="namejs" name="namejs">
</form> 
</div>
</body>
<script>
var numberxq = '<%=request.getParameter("XQDM")%>';
var jzh = '<%=request.getParameter("JZWH")%>';
var xqmc = '<%=request.getParameter("XQMC")%>';
var jzwmc = '<%=request.getParameter("JZWMC")%>';
var jsbh="";
var jslx="";
var row="";
var type="";
$(document).ready(function(){
	loadGrid();
	initDialog();
});
var numberjs="";
function loadGrid(){
$('#list3').datagrid({
	url:"<%=request.getContextPath()%>/Svl_building?active=list3&XQH="+numberxq+"&JZWH="+jzh+"&JSBH="+jsbh+"&SFKY="+$('#SFKY').combobox('getValue'),
	loadMsg : "信息加载中请稍后!",//载入时信息
	width : '100%',//宽度
	rownumbers: true,
	animate:true,
	striped : true,//隔行变色
	singleSelect : true,//单选模式
	fit:true,
	fitColumns: true,//设置边距
	idField:'id',
	treeField:'名称',
	columns:[[
		{field:'XQMC',title:'校区名称',width:fillsize(0.2),align:'center'},
		{field:'JZWMC',title:'建筑物名称',width:fillsize(0.2),align:'center'},
		{field:'JSBH',title:'教室编号',width:fillsize(0.2),align:'center'},
		{field:'JSMC',title:'教室名称',width:fillsize(0.2),align:'center'},
		{field:'JJ_MC',title:'教室类型',width:fillsize(0.1),align:'center'},
		{field:'SFKY',title:'是否可用',width:fillsize(0.1),align:'center',
			formatter:function(value,rec){
				var zt=rec.SFKY;
				if(rec.SFKY=="1"){
					zt="是";
				}else{
					zt="否";
				}
				return zt;
			}
		}
	]],
	onClickRow:function(rowIndex, rowData){
		row=rowData;
		numberjs=rowData.JSBH;
		namejs=rowData.JSMC;
		jslx=rowData.JJ_MC;
	},
	onLoadSuccess: function(data){
		comboxjsmc();	
	},
	onLoadError:function(none){		
	}
});
}
//弹窗
function initDialog(){
	$('#xjjs').dialog({   
		width: 350,//宽度设置   
		height: 217,//高度设置 
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
		}],//是否可移动dialog框设置
		//打开事件
		onOpen:function(data){
			combox();
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
//表三
if(iToolbar=="new3"){
		$('#xjjs').dialog({title: '新建教室'});
		$('#xjjs').dialog("open");
		empty();
		numberjs="";
		type="savejs";
	}
	if(iToolbar=="savejs"){
		$("#XQDM").val(numberxq);
		$("#LXBH").val($('#JSLXDM').combobox('getValue'));
		$("#JZWH").val(jzh);
		$("#active").val(iToolbar);
		if($("#XQDM").val()==""||$("#JSBH").val()==""||$("#JSMC").val()==""){
			alertMsg("不能为空,请填写完整");
			return;
		}
		if($("#LXBH").val()==""){
		alertMsg("请选择下拉菜单");
		return;
		}
		$("#form3").submit();
		comboxjsmc();	
	}
	if(iToolbar=="edit3"){
		$('#xjjs').dialog({title: '编辑教室'});
		type="updatejs";
		if(numberjs==""){
			alertMsg("请选择教室");
			return;
		}
		$('#xjjs').dialog("open");
		if(row!=undefined && row!=''){
			$('#form3').form('load', row);
		}
		
	}
	if(iToolbar=="updatejs"){
		$("#LXBH").val($('#JSLXDM').combobox('getValue'));
		$("#XQDM").val(numberxq);
		$("#numberjs").val(numberjs);
		$("#namejs").val(namejs);
		if($("#JSBH").val()==""||$("#JSMC").val()==""){
			alertMsg("不能为空，请填写完整");
			return;
		}
		if($("#LXBH").val()==""){
			alertMsg("请选择教室类型");
			return;
		}
		$("#active").val(iToolbar);
		$("#form3").submit();	
	}
	if(iToolbar=="back"){
		location.href = '<%=request.getContextPath()%>/form/building/building1.jsp?XQDM=' + numberxq+"&XQMC="+xqmc;
	}
	if(iToolbar=="search"){
		jsbh=$('#comboxjsmc').combobox('getValue');
		loadGrid();
		numberjs=="";
	}
	if(iToolbar=="deljs"){
		if(numberjs==""){
		alertMsg("请选择教室");
		return;
		}
		$("#active1").val(iToolbar);
		$("#numberxq1").val(numberxq);
		$("#JZWH1").val(jzh);
		$("#JSBH1").val(numberjs);
		$("#form4").submit();		
	}
}
$('#form3').form({
	url:'<%=request.getContextPath()%>/Svl_building', 
//当点击事件并成功提交后触发的事件
	success:function(data){
		var	data1=eval(data);
		if(data1[0].MSG=="保存成功"){
			$('#xjjs').dialog("close");
			loadGrid();
			numberxq=="";
		}else 
		if(data1[0].MSG=="修改成功"){
			$('#xjjs').dialog("close");
			loadGrid();
			numberxq=="";
		}else{
		   	alertMsg(data1[0].MSG);
		}
	}		
});
$("#form4").form({
	url:'<%=request.getContextPath()%>/Svl_building', 
//当点击事件并成功提交后触发的事件
	success:function(data){
		var	data1=eval(data);
		if(data1[0].MSG=="删除成功"){
			$('#xjjs').dialog("close");
			loadGrid();
			numberxq=="";
		}else{
		   	alertMsg("删除失败");
		}
	}
});
//下拉框
function combox(){
	$('#JSLXDM').combobox({
		url:"<%=request.getContextPath()%>/Svl_building?active=comboxtype",
		valueField:"编号",
		textField:"名称",
		editable:false,
		onLoadSuccess:function(){
		},
		onSelect:function(){
		}
		});		
}
function comboxjsmc(){
	$('#comboxjsmc').combobox({
		url:"<%=request.getContextPath()%>/Svl_building?active=comboxjsmc&XQH="+numberxq+"&JZWH="+jzh,
		valueField:"教室编号",
		textField:"教室名称",
		editable:false,
		onLoadSuccess:function(){
		},
		onSelect:function(){
		numberjs="";
		}
	});		
}
function empty(){
	$('#JSBH').textbox('setValue', '');
	$('#JSMC').textbox('setValue', '');
	$('#JSLXDM').combobox('setValue', '');
	
}
</script>
</html>
