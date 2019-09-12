<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'sysAuth.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery-1.8.0.min.js"></script>
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/icon.css">
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
	<link href="<%=request.getContextPath()%>/css/wiserun.css" rel="stylesheet" type="text/css">
  </head>
  
  <body>
  <form id="form1" name="form1"  method="post">   
   <div >
		<table width="400px" border="0" cellspacing="0" cellpadding="0" bgcolor="FFFFFF">
				<TR>
				<TD width="1%" height="28"><IMG src="<%=request.getContextPath()%>/images/border_r1_c1.gif" width="16" height="28"></TD>
				<TD width="97%" class="tabletop"><P class="talbetitle">权限列表</TD>
				<TD width="2%"><IMG src="<%=request.getContextPath()%>/images/border_r1_c4.gif"	width="16" height="28"></TD>
    			</TR>
		</table>
		<div style="width:398px;float:left; border:1px solid #36F;">
			<table width="100%" cellspacing="0" cellpadding="5"  >
				
				<tr>
				<td colspan="2" >
				<b>权限列表</b>
				</td>
					
						
				</tr>
				<tr>
				<td colspan="2">
						<table id="authlist" name="authlist" height="550px"></table>
				</td>
				</tr>
			</table>	
			</div>
			<div style="display:block; float:left; width:250px; cellpadding:50px; align:center;" >
			<table WIDTH='70%' cellspacing="20" cellpadding="0" style="margin:20px;" align="center">
			<tr>
				<td width="100%" height="100px;">
				</td>
			</tr>
			<tr>
				<td><a id="qureysave" name="qureysave" value="保存权限" class="easyui-linkbutton" data-options="iconCls:'layout-button-right'" width="20px" plain="true" onclick="doToolbar(this.id)">添加权限</a></td>
			</tr>
			<tr>
				<td>
				<a id="querydel" name="qureydel" value="删除权限" class="easyui-linkbutton" data-options="iconCls:'layout-button-left'" width="20px" plain="true" onclick="doToolbar(this.id)">删除权限</a>
				</td>
			</tr>
			<tr>
				<td><a id="back" name="back" value="返回前页" class="easyui-linkbutton" data-options="iconCls:'icon-return'" width="20px" plain="true" onclick="doToolbar(this.id)">返回上一页</a>
				</td>
			</tr>
			
			
			
			</table>
			</div>
			    <!--列表 包含层级内的权限 --> 
			   
			<div style="display:block; float:left; width:400px; border:1px solid #36F;">
			
			<table width="100%" cellspacing="0" cellpadding="5" >
				
				<tr>
				<td colspan="2" >
				<b><%=request.getParameter("EcheName")%>权限列表</b>
					</td>
					
						
				</tr>
				<tr>
				<td colspan="2">
						<table id="authlist1" name="authlist" ></table>
				</td>
				</tr>
			</table>	
			</div>
	</div>
	<input type="hidden" id="active" name="saveAuth" value="saveAuth">
	</form>
  </body>
  <script type="text/javascript">
//加载页面
$(document).ready(function(){
			loadGrid();
			loadGrid1();
		});
//保存权限号
var AuthCodeIn="";
//删除权限号
var AuthCodeOut="";
//加载特殊规则列表（右表）
function loadGrid(){
		$('#authlist').datagrid({
		url : '<%=request.getContextPath()%>/Svl_EchelonsManage?active=load&EcheCode=<%=request.getParameter("EcheCode")%>',
		singleSelect:true,
		animate:true,
		loadMsg:'数据加载中，请稍后...',
		rownumbers: true,
		checkOnSelect:true,
		selectOnCheck:true,
		animate:true,
		showHeader:false,
		fitColumns: true,//设置边距
		idField:'AuthCode',
		columns:[[
			{field:'ck',checkbox:true},
			{field:'AuthCode',title:'ID'},
			{field:'AuthDesc',title:'权限',width:120}
				]],
		onClickRow:function(rowIndex, rowData){	
			row = rowData;//获取行数据
	//				alert(rowData.AuthCode);
			AuthCodeIn=rowData.AuthCode;
		},		
		onLoadSuccess: function(data){
		},
		onLoadError:function(none){			
		}
	});
}
		
		
//加载列表1（左表）
function loadGrid1(){
		$('#authlist1').datagrid({
		url : '<%=request.getContextPath()%>/Svl_EchelonsManage?active=load1&EcheCode=<%=request.getParameter("EcheCode")%>',
		singleSelect:true,
		animate:true,
		loadMsg:'数据加载中，请稍后...',
		rownumbers: true,
		checkOnSelect:true,
		selectOnCheck:true,
		fitColumns: true,//设置边距
		showHeader:false,
		idField:'AuthCode',
		columns:[[
		{field:'ck',checkbox:true},
		{field:'AuthCode',title:'ID'},
		{field:'AuthDesc',title:'权限',width:120}
		]],
		onClickRow:function(rowIndex, rowData){	
//row = rowData;//获取行数据
//				alert(rowData.AuthCode);
		AuthCodeOut=rowData.AuthCode;
		},
		onLoadSuccess: function(data){
		},
		onLoadError:function(none){
					
		}
	});
}
		
//工具按键		
function doToolbar(iToolbar){
//返回
  	if(iToolbar=="back"){
  		window.location.href="sysAuthEche.jsp";
  	}
//添加按键  	
  	if(iToolbar=="qureysave"){
//判定是否有值  	
  		if(AuthCodeIn==""){
  		alertMsg("请选择一行");
  		return;
  		}
  		$.ajax({
		   type: "POST",
		   url: "<%=request.getContextPath()%>/Svl_EchelonsManage",
		   data:"active=saveAuth&EcheCode=<%=request.getParameter("EcheCode")%>&AuthCode="+AuthCodeIn,												
		   dataType: 'json',
		   success: function(datas){
//重载页面	
		   $("#authlist").datagrid("reload");
		   $("#authlist").datagrid("clearSelections");
		   $("#authlist1").datagrid("reload");
		   $("#authlist1").datagrid("clearSelections");
//初始化变量
		   AuthCodeIn="";
		   AuthCodeOut="";	
		   	}
		   });
	
  	}
//删除按键
	if(iToolbar=="querydel"){
 //判定是否有值  	
  		if(AuthCodeOut==""){
  		alertMsg("请选择一行");
  		return;
  		}
  		$.ajax({
		   type: "POST",
		   url: "<%=request.getContextPath()%>/Svl_EchelonsManage",
		   data:"active=delAuth&EcheCode=<%=request.getParameter("EcheCode")%>&AuthCode="+AuthCodeOut,												
		   dataType: 'json',
		   success: function(datas){
//重载页面
		   	$("#authlist").datagrid("reload");
		 	$("#authlist").datagrid("clearSelections");
		   	$("#authlist1").datagrid("reload");
		   	$("#authlist1").datagrid("clearSelections");
//初始化变量
		    AuthCodeIn="";
		   	AuthCodeOut="";	
		   		}
			});
  	}
 }
  </script>
</html>
					