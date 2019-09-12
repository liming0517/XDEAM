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
		<title>部门负责人定义</title>
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
			<div region="north" align="left" title="审核绑定配置"
					style="overflow: hidden; height: 50px; width: 800px;">
					<a href="#"
						 onclick="doToolbar(this.id);"
						id="add" name="add" class="easyui-linkbutton" plain="true"
						iconCls="icon-add">添加</a>	
					<a href="#"
						 onclick="doToolbar(this.id);"
						id="edit" name="edit" class="easyui-linkbutton" plain="true"
						iconCls="icon-edit">编辑</a>
					<a href="#"
						 onclick="doToolbar(this.id);"
						id="cancel" name="cancel" class="easyui-linkbutton" plain="true"
						iconCls="icon-cancel">删除</a>
				</div>
				<div region="center" style="background:#fafafa;width:100%;">
		  			<table id="deflist" name="deflist" width="100%"></table>
				</div>
					<input type="hidden" id="active" name="active" ></input>
				<input type="hidden" id="delid" name="delid" ></input>
			</form>
		</body>

<div id="depuseredit" name="depuseredit" title="审核绑定编辑" class="easyui-window"  style="width:600px" resizable=false closed=true modal=true>
<form id="form2" name="form2" method="POST">
	<center>
	<table class="tablestyle" style="width:100%" cellpadding="4">
	<tr>
	<td>负责人</td>
	<td>
		<select
		class="easyui-combobox" name="dep" id="dep" style="width: 200px;">
		</select>
	</td>
	<td>审核人</td>
	<td>
		<select
		class="easyui-combobox" name="user" id="user" style="width: 200px;">
		</select>
	</td>
	</tr>
	</table>
	</center>
		<input type="hidden" id="cactive" name="cactive" ></input>
		<input type="hidden" id="depid" name="depid"></input>
		<input type="hidden" id="depname" name="depname"></input>
		<input type="hidden" id="userid" name="userid"></input>
		<input type="hidden" id="username" name="username"></input>
	</form>
</div>

</html>
<script type = "text/JavaScript">
	var lastIndex = -1;
	var user=<%=PublicTools.getComboboxOptions("dbo.V_USERINFO","distinct UserCode","UserName","where 1=1",request)%>;
	var userid="";
	var username="";
	var curid="";
	var dep=<%=PublicTools.getComboboxOptions("dbo.V_USERINFO","distinct UserCode","UserName","where UserCode not in(select 人员编号  from dbo.V_人员审核绑定)",request)%>;
	var depname="";
	var depid="";
	var rows = null;
	//页面初始化
	$(document).ready(function(){
		//alert();
		loadGrid();
		//loadmodeneirong();
	});
	
	//alert(modeneirong);

	
	$('#dep').combobox({
		//data:modeneirong,
		valueField:'comboValue',
		textField:'comboName',
		data:dep,
		onSelect:function(record){
		}
							
	});
	
	$('#user').combobox({
		data:user,
		valueField:'comboValue',
		textField:'comboName',
		multiple:true,
		onSelect:function(record){
			//depid=record.Name;
		}
	}); 
	
	
	//获取表格中的数据（传入所有查询参数）
	function loadGrid() {
		//alert();
		$(function() {
			$('#deflist').datagrid({
				url: '<%=request.getContextPath()%>/Svl_DefUser',
				queryParams:{"active":"que"},
				title: '审核绑定列表',
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
					{field:'负责人',title:'负责人',width:100},
					{field:'审核人员编号',title:'审核人',width:100}
				]],
				onClickRow:function(rowIndex,rowData){
					curid=rowData.负责人编号;
				}
			});
		});
	}
	
	$('#form1').form({
			//定位到servlet位置的url
			url: '<%=request.getContextPath()%>/Svl_DefUser',
			//当点击事件后触发的事件
		    onSubmit: function(data){
		    	//alert($('#modeid').combobox('getValue'));
		    }, 
		    //当点击事件成功提交后触发的事件
		    success:function(data){
		    	//alert("success");
		    	var datas = eval("("+data+")");
		    	showMsg(datas[0].MSG);
		    	loadGrid();
		    },
		    onLoadError:function(){
		    	//alert("error");
		    }
	});
		
	$('#form2').form({
		//定位到servlet位置的url
		url: '<%=request.getContextPath()%>/Svl_DefUser',
		//当点击事件后触发的事件
	    onSubmit: function(data){
	    	//alert($('#modeid').combobox('getValue'));
	    }, 
	    //当点击事件成功提交后触发的事件
	    success:function(data){
	    	//alert("success");
	    	var datas = eval("("+data+")");
	    	$("#depuseredit").window('close');
	    	showMsg(datas[0].MSG);
	    	loadGrid();
	    },
	    onLoadError:function(){
	    	//alert("error");
	    }
	});
	
	$('#depuseredit').dialog({
			toolbar:[{
				text:'保存',
				iconCls:'icon-save',
				handler:function(){
					$('#depid').val($('#dep').combobox('getValue'));
					$('#depname').val($('#dep').combobox('getText'));
					$('#userid').val($('#user').combobox('getValues'));
					//alert($('#user').combobox('getText'));
					$('#username').val($('#user').combobox('getText'));
					$('#form2').submit();
				}
			}]
	});
		
	function doToolbar(id){
			if(id=="add"){
				$('#dep').combobox('clear');
				$('#user').combobox('clear');
				$('#form2').form('clear');
				$('#cactive').val('save');
				$("#depuseredit").window('open');
			}
			if(id=="edit"){
				if(curid==""){
					alertMsg("请选择一条数据");
				}else{
					rows = $('#deflist').datagrid('getSelected');
					$('#dep').combobox('setValue', rows.负责人编号);
					//$('#orgid').val(rows.部门号);
					$('#dep').combobox('disable');
					$("#cactive").val("edit");
					$('#user').combobox('setValue', rows.审核人编号);
					$("#depuseredit").window('open');
				}
			}
		if(id=="cancel"){
			if(curid==""){
				alertMsg("请选择一条数据");
			}else{
				$('#deflist').datagrid('endEdit', lastIndex);
				$("#active").val("del");
				//alert(curspid);
				//alert(curid);
				$("#delid").val(curid);
				curid="";
				ConfirmMsg("你确认要删除该记录吗?","$('#form1').submit();","");
			}
		}
	}
	
</script>