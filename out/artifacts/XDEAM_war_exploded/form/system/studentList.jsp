
<%
	/**
	 Create date: 2012.7.13
	 说明：用于编辑学员信息
	 创建人：yeqiang
	 **/
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><%@page
	language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.pantech.base.common.tools.MyTools" %>		
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
<title>人员信息</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/icon.css">
<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
</head>



<form action="<%=request.getContextPath()%>/Svl_Student?active=upload"
	method="post" name="form1" id="form1">
<body class="easyui-layout">
<div style="overflow: hidden; height: 101px; background: #fafafa;"
	title="人员信息" region="north">
<div>
<table>
	<tr>
		<td><a href="#" onclick="doToolbar(this.id)" id="new"
			class="easyui-linkbutton" plain="true" iconcls="icon-new">新增</a></td>
		<td><a href="#" onclick="doToolbar(this.id)" id="edit"
			class="easyui-linkbutton" plain="true" iconcls="icon-edit">编辑</a></td>
		<td><a href="#" onclick="doToolbar(this.id)" id="role"
			class="easyui-linkbutton" plain="true" iconcls="icon-khlx">角色管理</a></td>
		<td><a href="#" onclick="doToolbar(this.id)" id="dept"
			class="easyui-linkbutton" plain="true" iconcls="icon-ktlx">部门管理</a></td>
	</tr>
</table>
</div>
<table id="ff" width="100%" border="0" class="tablestyle">
	<tr>
		<td><label>用户编号：</label></td>
		<td><input name="ic_userCode" id="ic_userCode"
			class="easyui-vaildatebox" maxlength="30" required="false"></td>

		<td><label>用户名：</label></td>
		<td><input name="ic_userName" id="ic_userName"
			class="easyui-vaildatebox" required="false"></td>

		<td align="center">
		<div style="padding: 5px; background: #efefef; width: 100%;"><a
			href="#" class="easyui-linkbutton" iconcls="icon-search" plain="true"
			onClick="doSubmit()">查询</a></div>
		</td>
	</tr>
</table>
</div>

<div style="background: #fafafa;" region="center">
<table id="userInfoList" width="100%">
</table>
</div>
<!-- 动作的属性传参用 -->
<input type="hidden" name="active" id="active">
<input type="hidden" name="UserCode" id="UserCode">
<input type="hidden" name="oldUserCode" id="oldUserCode">
<input type="hidden" name="UserName" id="UserName">
<input type="hidden" name="Email" id="Email">
<input type="hidden" name="oldUserName" id="oldUserName">
<input type="hidden" value="User" name="hModuleID">
</body>
</form>



<script type="text/javascript">
		
		var iKeyCode = ""; //被选中的条目（rowData.用户名）
		
		var active = "query";  //需要执行的操作
		
		
		//页面加载完后执行loadGrid()
		$(document).ready(function(){
			//alert("权限:"+sAuth);
		    /*if(sAuth=="03"){
				$("#ic_userCode").attr("disabled", "disabled");
				$("#ic_userName").attr("disabled", "disabled");
			}
			*/
			loadGrid($("#ic_userCode").val(), $("#ic_userName").val(), active, null);
		});
		
		//读取学员信息列表
		function loadGrid(ic_userCode, ic_userName, active, st_count){
			$(function(){
				var lastIndex = -1;
				$("#userInfoList").datagrid({
					url:'<%=request.getContextPath()%>/Svl_StudentYeq',
					queryParams:{"ic_userCode":ic_userCode,
								 "ic_userName":ic_userName,
								 "active":active,
								 "st_count":st_count},
					loadMsg:"信息加载中请稍后！",  //当远程加载数据时，现实的等待信息提示
					title:"人员信息列表",
					width:"100%",
					height:1000,   //此高度需要根据表格之上的输入区域高度进行调整
					nowrap:false,  //是否在一行显示数据
					fit:true,  //是否允许表格自动缩放，以适应父容器
					fitColums:true,
					striped:true,    //是否显示斑马线
					pagination:true,  //是否显示底部分页工具栏
					pageSize:20,
					singleSelect:true,  //是否允许只选择一行
					pageNumber:1,
					rownumbers:true,   //显示行号
					frozenColumns:[[
						{field:'UserCode',title:'用户编号',width:fillsize(0.165)},
						{field:'UserName',title:'用户名',width:fillsize(0.165)}
					]],
					columns:[[    //表格的列的配置对象,field 列字段名称
						{field:'email',title:'帐号',width:fillsize(0.165)},
						{field:'MOBILENO',title:'联系电话',width:fillsize(0.165)},
						{field:'state',title:'状态',width:fillsize(0.165)},
						{field:'操作',title:'操作',width:fillsize(0.175),editor:'text',
							formatter:function(value,rec){
								rec.操作="<input type='button' id='limit' style='height:22px;line-height:20px' onClick='doToolbar(this.id, \""+rec.UserCode+"\")' value='设置权限'/> "+
								"<input type='button' id='resetPass' style='height:22px;line-height:20px' onClick='doToolbar(this.id,\""+rec.UserCode+"\",\""+rec.UserName+"\",\""+rec.email+"\")' value='密码重置' />";
								return rec.操作;
						}}
					]],
					
					//当用户点击某行时触发
					//选中某行既给iKeyCode赋值当前行学员学号
					onClickRow:function(rowIndex, rowData){
						iKeyCode = rowData.UserCode;
						//alert("iKeyCode:"+iKeyCode);
					}
				});
			});
		}
				
		//工具栏调用
		function doToolbar(iToolbar,ids,names,emails){
			//新建用户
			if(iToolbar=="new"){
				location.href="studentInfo.jsp";
			}
			//编辑
			if(iToolbar=="edit"){
				//判断如果iKeyCode为“”，提示信息
				if(iKeyCode==""){
					alertMsg("请先选择一行数据");
					return;
				}
				location.href="studentInfo.jsp?ic_userCode="+iKeyCode+"&active=view";
				//return;
			}
			//角色管理
			if(iToolbar=="role"){
				location.href="<%=request.getContextPath()%>/form/system/UserAuth/UserAuthConfig.jsp";
			}
			//部门管理
			if(iToolbar=="dept"){
				location.href="<%=request.getContextPath()%>/form/system/UserDept/UserDeptConfig.jsp";
			}
			//权限设置
			if(iToolbar=="limit"){
				//alert(ids);
				location.href="<%=request.getContextPath()%>/form/system/PerCodeList.jsp?UserCode="+ids;
			}
			//密码重置
			if(iToolbar=="resetPass"){
				//alert(iKeyCode);
				if(confirm("进行此操作后\n对应人员的密码将被重新初始化成11111111(8个1) !","确定","取消")){
					//alert(ids);
					var o = eval(document.form1);
					o.UserCode.value = ids;
					o.oldUserCode.value = ids;
					
					o.UserName.value = names;
					
					o.Email.value = emails;
						
					o.oldUserName.value = names;
					o.action = "<%=request.getContextPath()%>/Svl_System?type=IniPass";
					o.submit();
				}
			}
		}
		
		//提交查询
		function doSubmit(){
			loadGrid($("#ic_userCode").val(), $("#ic_userName").val(), active, null);
			iKeyCode = "";
		}
		
		//提示错误信息
		function alertMsg(str){
			$.messager.alert("提示", str);
		}
	</script>
</html>