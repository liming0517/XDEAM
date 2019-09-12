<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>My JSP 'Echelons.jsp' starting page</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/icon.css">
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
	<link href="<%=request.getContextPath()%>/css/wiserun.css" rel="stylesheet" type="text/css">
</head>

<body>
	<div id="zhubiaoti" name="主标题栏区">
	<table width="400px" border="0" cellspacing="0" cellpadding="0" bgcolor="FFFFFF">
	<TR>
	<TD width="1%" height="28"><IMG src="<%=request.getContextPath()%>/images/border_r1_c1.gif" width="16" height="28"></TD>
	<TD width="97%" class="tabletop"><P class="talbetitle">层级管理</TD>
	<TD width="2%"><IMG src="<%=request.getContextPath()%>/images/border_r1_c4.gif"	width="16" height="28"></TD>
    </TR>
    
	</table>
	
	<div id="anniuqu"  name="按钮区" style="width:398px;border:1px solid #F00">	
	<table width="100%" border="0" cellspacing="5" cellpadding="5" >
		<tr aligen="center">
		<td width="25%">
		<input type="button" value="新建" id="xj" onclick="doToolbar(this.id)" width="20" />
		</td>
		<td width="25%">
		<input type="button" value="编辑" id="bj" onclick="doToolbar(this.id)" />
		</td>
		<td width="25%">
		<input type="button" value="删除" id="sc" onclick="doToolbar(this.id)" width="20" />
		</td>
		<td width="25%">
		<input type="button" value="关联权限" id="glqx" onclick="doToolbar(this.id)" width="20" />
		</td>
		
		
		</tr>
		<tr aligen="center">
				<td>
					层级
				</td>
    			<td colspan="2"><input class="easyui-combobox" name="browser" style="width:200px;" id="combox">
    			</td>
    			<td>
					<input type="button" id="que" onclick="doToolbar(this.id)" value="查询">
				</td>
    			</tr>
	</table>
	</div>
	<div style="height:10%"></div>
	<div name="列表区" id="liebiaoqu" height="70%">
			<table width="398px" border="0" cellspacing="0" cellpadding="0" bgcolor="FFFFFF" >
				<TR>
				<TD width="1%" height="28"><IMG src="<%=request.getContextPath()%>/images/border_r1_c1.gif" width="16" height="28"></TD>
				<TD width="97%" class="tabletop"><P class="talbetitle">层级权限关系列表</TD>
				<TD width="2%"><IMG src="<%=request.getContextPath()%>/images/border_r1_c4.gif"	width="16" height="28"></TD>
    			</TR>
    			
			</table>
			<table id="List" title="层级/权限" class='tablestyle' style="width:398px;height:500px">
			</table>
			
	</div>


</div>
	
		<input type="hidden" id="active" value= "loadlist" />
</body>
<script>
	//初始化界面
$(document).ready(function(){
		combox();		
});
var EcheCode="";
var EcheName="";
var _parentId="";

function initData(){
		$.ajax({
			type : "POST",
			url : "<%=request.getContextPath()%>/Svl_EchelonsManage",
			data:"active="+$("#active").val()+"&EcheCode="+$('#combox').combobox('getValue'),
			dataType:"json",
			success : function(data) {
				load(data[0].listData);
				EcheCode="";
				EcheName="";
			}
		});
}

function combox(){
		$('#combox').combobox({
		    valueField:"层级编号",
			textField:"层级名称",
			editable:false,
			url:"<%=request.getContextPath()%>/Svl_EchelonsManage?active=combox",
			onLoadSuccess:function(){
			initData();
			},
			onSelect:function(){
//初始化参数
			EcheCode="";
			EcheName="";
			_parentId="";
			}
		});
}
var AuthCode="";
function load(listData){
	$("#List").treegrid({
		data:listData,
		loadMsg:'数据加载中，请稍后...',
		rownumbers: true,
		animate:true,
		fitColumns: true,//设置边距
		idField:'id',
		treeField:'名称',
		columns:[[
	         {field:'名称',title:'<b>权限/层级 </b>',width:200}
			]],
		onClickRow:function(row){
//初始化参数
			EcheCode="";
			EcheName="";
			_parentId=row._parentId;
			AuthCode=row.id;
			if(_parentId==""){
				EcheCode=row.id;
				EcheName=row.名称;
			}
		},
		onLoadSuccess:function(data){
		
		}
	});
}
//工具功能
function doToolbar(iToolbar){
//新建功能
	if(iToolbar=="xj"){
		//alert(iToolbar);
		window.location.href="sysEchelonsInfo.jsp";
	}else
			//编辑功能
	if(iToolbar=="bj"){
//			alert(EcheName);
		//判定是否有层级
		if(EcheCode==""){	
			EcheCode=$('#combox').combobox('getValue');
			EcheName=$('#combox').combobox('getText');
			}			
		if(EcheCode==""){
			alertMsg("请选择层级");
			return;
		}
		var url="sysEchelonsInfo.jsp?btn=bj&EcheCode="+EcheCode+"&EcheName="+EcheName;
		window.location.href=url;
	}else
	if(iToolbar=="glqx"){
//alert(iToolbar);
//判定层级功能
		if(EcheCode==""){
		//  EcheCode=$('#combox').combobox('getValue');
			 if(EcheCode==""){
				alertMsg("请选择层级");
				return;
			}
		}
		var url1="sysAuth.jsp?EcheCode="+EcheCode+"&EcheName="+EcheName;
		window.location.href=url1;
	}else
//删除
	if(iToolbar=="sc"){
//alert(iToolbar);
		if(AuthCode==""&&_parentId==""&&EcheCode==""){
		alertMsg("请选择");
		return;
		}
		if(EcheCode==""&&AuthCode!=""&&_parentId==""){
		alertMsg("请选择");
		return;
		}
//判定是否选取了权限或者层级
		if(AuthCode==EcheCode){
			$.ajax({
				type: "POST",
		  		url: "<%=request.getContextPath()%>/Svl_EchelonsManage",
		  		data:"active=querydel&EcheCode="+EcheCode,
				dataType:"json",
				success : function(data) {
						data1=data[0];					
							alertMsg(data1.MSG);
							initData();					
				}
			});
		}
		if(AuthCode!=""&&_parentId!=""&&EcheCode=="")
		{
			$.ajax({
				type: "POST",
		  		url: "<%=request.getContextPath()%>/Svl_EchelonsManage",
		  		data:"active=querydel&AuthCode="+AuthCode,
				dataType:"json",
				success : function(data) {
						data1=data[0];
							alertMsg(data1.MSG);
							initData();
				}
			});
		}
	}else 
	if(iToolbar=="que"){
		$("#active").val('que');
		initData();
		$('#List').treegrid('reload');
	}
}
</script>
</html>
	