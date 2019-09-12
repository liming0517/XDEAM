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
		<title>待审核列表列表</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta name="GENERATOR" content="Rational® Application Developer™ for WebSphere® Software">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/themes/icon.css">
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
	</head>
<jsp:useBean class="com.pantech.base.common.tags.security.PurViewTools" id="pc" scope="page" />

	<form id = "form1" name = "form1" method = "POST">
		<body class="easyui-layout">
			<div region="north" align="left" title="文档编辑"
				style="overflow: hidden; height: 50px; width: 800px;">
				<a href="#"
					 onclick="doToolbar(this.id);"
					id="add" name="add" class="easyui-linkbutton" plain="true"
					iconCls="icon-add">添加文件</a>	
				<a href="#"
					 onclick="doToolbar(this.id);"
					id="edittemp" name="edittemp" class="easyui-linkbutton" plain="true"
					iconCls="icon-edit">编辑</a>
				<a href="#"
					 onclick="doToolbar(this.id);"
					id="audittemp" name="audittemp" class="easyui-linkbutton" plain="true"
					iconCls="icon-undo">审核</a>
			</div>
			<!-- 表单显示区域 -->
			<div region="center" style="background:#fafafa;width:100%;">
	  			<table id="editpagelist" name="editpagelist" width="100%"></table>
	  			<input type="hidden" id="active" name="active" ></input>
	  			<input type="hidden" id="delid" name="delid" ></input>
			</div>
		</body>
	</form>
	
	<div id="pageedit" name="pageedit" title="文档编辑" class="easyui-window"  style="width:700px" resizable=false closed=true modal=true>
		<form id="form2" name="form2" method="POST">
			<center>
				<table class="tablestyle" style="width:100%" cellpadding="4">
					<tr>
						<td>标题</td>
						<td>
							<input type='text' id='title' name='title' style="width: 200px;"/>
						</td>
						<td>所属部门</td>
						<td>
							<select
									class="easyui-combobox" name="fqdep" id="fqdep" style="width: 200px;">
							</select>
						</td>

					</tr>
					<tr>
						<td>模版</td>
						<td>
							<select
									class="easyui-combobox" name="temp" id="temp" style="width: 200px;">
							</select>
						</td>
						<td colspan="2"></td>
					</tr>
				</table>
			</center>
			<input type="hidden" id="depid" name="depid" ></input>
			<input type="hidden" id="depname" name="depname" ></input>
			<input type="hidden" id="cactive" name="cactive" ></input>
		</form>
	</div>
</html>
<script type = "text/JavaScript">
	var pageurl = "";
	var pageid = "";
	var temp=<%=PublicTools.getComboboxOptions("dbo.V_模版配置表","模版编号","模板名称","where 状态='3'",request)%>;
	var dep=<%=PublicTools.getComboboxOptions("sysDepartment","distinct DeptCode","CName","where 1=1",request)%>;
	//页面初始化
	$(document).ready(function(){
		loadGrid();
		$("#edittemp").linkbutton('disable');
		$("#audittemp").linkbutton('disable');
	});
	

	
	$('#fqdep').combobox({
		data:eval(dep),
		valueField:'comboValue',
		textField:'comboName',
		onSelect:function(record){
			getfieldlist();
		}
	});
	
	$('#pageedit').dialog({
		toolbar:[{
			text:'开始编辑',
			iconCls:'icon-save',
			handler:function(){
				$('#depid').val($('#fqdep').combobox('getValues'));
				$('#depname').val($('#fqdep').combobox('getText'));
				$('#form2').submit();
			}
		}]
			
	});
	//获取表格中的数据（传入所有查询参数）
	function loadGrid() {
		$(function() {
			var lastIndex = -1;
			$('#editpagelist').datagrid({
				url: '<%=request.getContextPath()%>/Svl_PaggofficeEdit',
				queryParams:{"active":"que"},
				title: '待审核列表',
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
				columns:[[
					{field:'文档名称',title:'文档名称',width:100},
					{field:'状态',title:'文档状态',width:100,formatter:function(value,row,index){
							if(value=="0"){
								return "编辑";
							}
							if(value=="1"){
								return "提交";
							}
							if(value=="2"){
								return "驳回";
							}
							if(value=="3"){
								return "通过";
							}
						}
					},
					{field:'创建时间',title:'创建时间',width:100}
				]],
				onClickRow:function(rowIndex,rowData){
					//editpagelist = rowData.审核地址;
					pageid=rowData.文档编号;
					pageurl = rowData.路径;
					if(rowData.状态=="0"){
						$("#edittemp").linkbutton('enable');
						$("#audittemp").linkbutton('disable');
					}
					if(rowData.状态=="1"){
						$("#edittemp").linkbutton('disable');
						$("#audittemp").linkbutton('enable');
					}
					if(rowData.状态=="2"){
						$("#edittemp").linkbutton('disable');
						$("#audittemp").linkbutton('disable');
					}
					if(rowData.状态=="3"){
						$("#edittemp").linkbutton('disable');
						$("#audittemp").linkbutton('disable');
					}
				}
			});
		});
	}
	
	$('#form1').form({
		//定位到servlet位置的url
		url: '<%=request.getContextPath()%>/Svl_PaggofficeEdit',
		//当点击事件后触发的事件
	    onSubmit: function(data){
	    	//alert($('#modeid').combobox('getValue'));
	    }, 
	    //当点击事件成功提交后触发的事件
	    success:function(data){
	    	//alert("success");
	    	var datas = eval("("+data+")");
	    	showMsg(datas[0].MSG);
	    	//location.reload();
	    	loadGrid();
	    },
	    onLoadError:function(){
	    	//alert("error");
	    }
	});
	
	$('#form2').form({
		url: '<%=request.getContextPath()%>/Svl_PaggofficeEdit',
		//当点击事件后触发的事件
		//async:false,
	    onSubmit: function(data){
	    	//alert(active);
	    }, 
	    //当点击事件成功提交后触发的事件
	    success:function(data){
	    	//alert(111);
	   		var datas = eval("("+data+")");
	    	$("#pageedit").window("close");
	    	
	    	location.href="./word.jsp?code="+datas[0].code+"&tablename="+encodeURI("V_文档编辑")+"&filed="+encodeURI("文档编号");
	    	//showMsg(datas[0].MSG);
	    	//loadGrid();
	    }
	});
	
	
	function doToolbar(iToolbar) {
		if(iToolbar=="add"){
			$('#fqdep').combobox('clear');
			$('#temp').combobox('clear');
			$('#temp').combobox('loadData','');
			$('#form2').form('clear');
			$("#cactive").val("save");
			
			$("#pageedit").window("open");
		}
		if(iToolbar=="submit") {
			if(pageid=="") {
				alertMsg("请选择一行数据"); 
				return;
			}
			$("#active").val("submit");
		}
		if(iToolbar=="edittemp") {
			if(pageid=="") {
				alertMsg("请选择一行数据"); 
				return;
			}
			location.href="./word.jsp?code="+pageid+"&tablename="+encodeURI("V_文档编辑")+"&filed="+encodeURI("文档编号");
		}
		
		if(iToolbar=="audittemp") {
			if(pageid=="") {
				alertMsg("请选择一行数据"); 
				return;
			}
			location.href="./auditword.jsp?code="+pageid+"&tablename="+encodeURI("V_文档编辑")+"&filed="+encodeURI("文档编号");
		}
	}
	
	function getfieldlist(){
			$.ajax({
				type: "POST",
				url: '<%=request.getContextPath()%>/Svl_PaggofficeEdit',   //设置 SVL
				async:false,
				data: "active=fieldlist&fqdep=" + $('#fqdep').combobox("getValue"),  // 设置模式，一般为view ，主关键字值		
				dataType: 'json',
				success: function(datas){
						$('#temp').combobox({
							data:datas,
							valueField:'comboValue',
							textField:'comboName'
						});
					//fieldlist=datas;
				}
			});
	}
</script>