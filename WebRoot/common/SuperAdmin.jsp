<!-- 审核查询界面 -->
<%
/**
文件说明
Create date: 2011.1.4
说明：贷款管理	查询页面
页面类型:编辑列表
创建人：Ma Yue

注意：只有指定的用户区可以修改或增加内容！！！

包括
1-页面布局显示（一般不引用除公共类外的任何JAVA代码）-----------------------------------美工

1a-页面设置:(根据需求文挡界面样例) //style="width:800px; height:550px;"
1b-页面标题:(根据需求文挡界面样例) //title="审核"
1c-按钮设置:(根据需求文挡界面样例)
1d-数据输入:(根据需求文挡界面样例布局，根据数据库设计文挡设置元素的id- "ic"+大写字段名)
/

2-前台代码执行（JS 一般在页面显示后，调用数据初始化AJAX代码）-----------------------------------美工/程序员

2a-id:(主关键字变量)  //id="" +  < % =request.getParameter("ic_ID") % > + "";    
2b-showMsg:(标题) //title:'审核',
2c-ajax提交事件:    //程序员根据要求处理
2d-工具按钮--代码:(按钮点后的动作)    //程序员根据要求处理
2e-loadData(读取界面信息):(根据数据库设计文挡 设置 //在页面上显示数据） //主键编号$("#ic字段名").val(data.字段名);)

3-模块特定代码（JS 一般在页面提交前执行）-----------------------------------程序员

**/
 %>
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
		<meta name="GENERATOR" content="Rational® Application Developer™ for WebSphere® Software">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/icon.css">
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
	</head>
<jsp:useBean class="com.pantech.base.common.tags.security.PurViewTools" id="pc" scope="page" />
	<form id = "form1" name = "form1" method = "POST">
		<body class="easyui-layout">
			<div region = "north" align = "left" title="审核配置"  style = "background:#fafafa;height:58px;width:800px;">
				<table id = "search" class = "tablestyle" width = "100%">
					<tr>
						<td>
							<label>业务名称</label>
						</td>
						<td>
							<input maxlength="200" id="modename" name="modename"/>
						</td>
						<td>
							<label>创建时间</label>
						</td>
						<td>
							<input type="text"  id="createdate1"  name="createdate1"  class="easyui-datebox"  style="width:200px"  required="false"  editable="false">
						</td>
						<td>至</td>
						<td>
							<input type="text"  id="createdate2"  name="createdate2"  class="easyui-datebox"  style="width:200px"  required="false"  editable="false">
						</td>
						<td>
							<a href="#" class="easyui-linkbutton" id="que" name="que" iconCls="icon-search" onClick="doToolbar(this.id)">查询</a>
						</td>
					</tr>
				</table>
			</div>
			<!-- 表单显示区域 -->
			<div region="center" style="background:#fafafa;width:100%;">
	  			<table id="resultlist" name="resultlist" width="100%"></table>
			</div>
			<input type="hidden" id="active" name="active" ></input>
			<input type="hidden" id="MouldID" name="MouldID" ></input>
		</body>
	</form>

</html>
<script type = "text/JavaScript">
	//页面初始化
	$(document).ready(function(){
		loadGrid("","","");
	});
	
	
	$('#form1').form({
		//定位到servlet位置的url
		url: '<%=request.getContextPath()%>/Svl_AuditConduct1',
		//当点击事件后触发的事件
	    onSubmit: function(data){
	    	//alert("Submit");
	    }, 
	    //当点击事件成功提交后触发的事件
	    success:function(data){
	    	//alert("success");
	    	var datas = eval("("+data+")");
	    	modeid="";
	    	showMsg(datas[0].MSG);
	    	dosubmit();
	    },
	    onLoadError:function(){
	    	//alert("error");
	    }
	});
		
	function dosubmit(){
		loadGrid($("#modename").val(),$("#createdate1").datebox('getValue'),$("#createdate2").datebox('getValue'));
	}
	
	//获取表格中的数据（传入所有查询参数）
	function loadGrid(icmodename,iccreatedate1,iccreatedate2) {
		$(function() {
		var lastIndex = -1;
			$('#resultlist').datagrid({
				url: '<%=request.getContextPath()%>/Svl_AuditConduct1',
				queryParams:{"active":"queresult","modename":icmodename,"createdate1":iccreatedate1,"createdate2":iccreatedate2},
				title: '审核强制驳回列表',
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
					{field:'业务主键',title:'业务主键',width:100,hidden:true},
					{field:'模块名称',title:'模块名称',width:100},
					{field:'业务名称',title:'业务名称',width:100},
					{field:'创建时间',title:'创建时间',width:100},
					{field:'强制驳回',title:'强制驳回',width:40,align:'center',
						formatter:function(value,row,index){
							var btn="";
							btn="<input type='button' onclick='reject(\""+row.业务主键+"\");',\"\")' value='强制驳回'>";
							return btn;
						}
					}
				]]
			});
		});
	}
	
	function reject(business){
		$("#active").val("reject");
		$("#MouldID").val(business);
		ConfirmMsg("是否强制驳回该业务?","$('#form1').submit();","");
	}
	
	$('#form1').form({
			//定位到servlet位置的url
			url: '<%=request.getContextPath()%>/Svl_AuditConduct1',
			//当点击事件后触发的事件
		    onSubmit: function(data){
		    	//alert($('#modeid').combobox('getValue'));
		    }, 
		    //当点击事件成功提交后触发的事件
		    success:function(data){
		    	//alert("success");
		    	var datas = eval("("+data+")");
		    	showMsg(datas[0].MSG);
		    	loadGrid("","","");
		    },
		    onLoadError:function(){
		    	//alert("error");
		    }
	});
		
	function doToolbar(iToolbar) {
		if(iToolbar=="que"){
			dosubmit();
		}
	}
</script>