<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="java.util.Vector"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.src.develop.store.user.*"%>
<%
	/**
		创建人：yeq
		Create date: 2016.01.29
		功能说明：用于设置科目信息
		页面类型:列表及模块入口
		修订信息(有多次时,可有多个)
		修订日期:
		原因:
		修订人:
		修订时间:
	**/
 %>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
  <head>
	<title>科目信息</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/icon.css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/publicScript.js"></script>
</head>
<%
	/*
		获得角色信息
	*/
	UserProfile up = new UserProfile(request,MyTools.getSessionUserCode(request));
	String userName =up.getUserName();
	if(userName==null){
		userName="";
	}
	String root = request.getContextPath();
	//out.println("当前在线人数： "+SessionListener.getCount());
	//人员对象		
	Vector v = new Vector(); 
	String usercode = "";
	usercode = MyTools.getSessionUserCode(request);
	String sAuth = "";
	//int i=0;
	AuthObject auth;
	//获取人员所属角色
	v = up.getUserAuth();
	//up.setUserAuth();
	if(v!=null && v.size()>0){
		for(int i=0; i<v.size(); i++){
			sAuth += "@"+MyTools.StrFiltr(v.get(i))+"@O";
		}
		sAuth = sAuth.substring(0, sAuth.length()-1);
	}
	// 如果无法获取人员信息，则自动跳转到登陆界面
%>
<body class="easyui-layout">
	<div id="north" region="north" title="科目信息" style="height:116px;">
		<table>
			<tr>
				<td><a href="#" id="wk" class="easyui-linkbutton" plain="true" iconcls="icon-edit" onClick="doToolbar(this.id);">设为文科</a></td>
				<td><a href="#" id="lk" class="easyui-linkbutton" plain="true" iconcls="icon-edit" onClick="doToolbar(this.id);">设为理科</a></td>
				<td><a href="#" id="cyjd" class="easyui-linkbutton" plain="true" iconcls="icon-edit" onClick="doToolbar(this.id);">参与绩点</a></td>
				<td><a href="#" id="bcyjd" class="easyui-linkbutton" plain="true" iconcls="icon-edit" onClick="doToolbar(this.id);">不参与绩点</a></td>
				<td><a href="#" id="cylj" class="easyui-linkbutton" plain="true" iconcls="icon-edit" onClick="doToolbar(this.id);">参与留级</a></td>
				<td><a href="#" id="bcylj" class="easyui-linkbutton" plain="true" iconcls="icon-edit" onClick="doToolbar(this.id);">不参与留级</a></td>
			</tr>
		</table>
		<table id="ee" singleselect="true" width="100%" class="tablestyle">
			<tr>
				<td style="width:150px;" class="titlestyle">学年学期名称</td>
				<td>
					<select name="CK_XNXQMC_CX" id="CK_XNXQMC_CX" class="easyui-combobox" style="width:150px;">
					</select>
				</td>
				<td style="width:150px;" class="titlestyle">教学性质</td><!-- 根据当前登录人权限 -->
				<td>
					<select name="CK_JXXZ_CX" id="CK_JXXZ_CX" class="easyui-combobox" style="width:150px;">
					</select>
				</td>
				<td style="width:150px;" class="titlestyle zycx">专业名称</td>
				<td>
					<input name="CK_ZYMC_CX" id="CK_ZYMC_CX" class="easyui-textbox" style="width:150px;"/>
				</td>
				<td style="width:150px;" class="titlestyle">年级名称</td>
				<td>
					<select name="CK_NJDM_CX" id="CK_NJDM_CX" class="easyui-combobox" style="width:150px;">
					</select>
				</td>
				<td style="width:150px;" align="center">
					<a href="#" onclick="doToolbar(this.id)" id="queList" class="easyui-linkbutton" plain="true" iconcls="icon-search">查询</a>
				</td>
			</tr>
			<tr>
				<td style="width:150px;" class="titlestyle zycx">课程名称</td>
				<td>
					<input name="CK_KCMC_CX" id="CK_KCMC_CX" class="easyui-textbox" style="width:150px;"/>
				</td>
				<td style="width:150px;" class="titlestyle zycx">科目类型</td>
				<td>
					<select name="CK_KMLX_CX" id="CK_KMLX_CX" class="easyui-combobox" panelHeight="auto" style="width:150px;" editable="false">
						<option value="">请选择</option>
						<option value="1">文科</option>
						<option value="2">理科</option>
					</select>
				</td>
				<td style="width:150px;" class="titlestyle zycx">是否参与绩点</td>
				<td>
					<select name="CK_CYJD_CX" id="CK_CYJD_CX" class="easyui-combobox" panelHeight="auto" style="width:150px;" editable="false">
						<option value="">请选择</option>
						<option value="1">是</option>
						<option value="0">否</option>
					</select>
				</td>
				<td style="width:150px;" class="titlestyle zycx">课程类型</td>
				<td>
					<select name="CK_KCLX_CX" id="CK_KCLX_CX" class="easyui-combobox" panelHeight="auto" style="width:150px;" editable="false">
						<option value="">请选择</option>
						<option value="1">普通课程</option>
						<option value="2">添加课程</option>
						<option value="3">选修课程</option>
						<option value="4">分层课程</option>
					</select>
				</td>
				<td></td>
			</tr>
	    </table>
	</div>
	<div region="center">
		<table id="subjectList" style="width:100%;"></table>
		<form id="form1" method="post">
			<input type="hidden" id="active" name="active"/>
			<input type="hidden" id="selectId" name="selectId"/>
			<input type="hidden" id="setType" name="setType"/>
			<input type="hidden" id="CK_ID" name="CK_ID"/>
		</form>
	</div>
</body>
<script type="text/javascript">
	var sAuth = '<%=sAuth%>';
	var curSelId = '';
	var tempIndex = '';
	
	$(document).ready(function(){
		loadSubjectGrid();
		initComboData();//页面初始化加载数据
	});
	
	/**页面初始化加载数据**/
	function initComboData(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Kmsz',
			data : 'active=initComboData&sAuth=' + sAuth,
			dataType:"json",
			success : function(data) {
				initCombobox(data[0].xnxqData, data[0].jxxzData, data[0].njdmData);
			}
		});
	}
	
	/**加载combobox控件
		@jxxzData 教学性质
		@njdmData 年级名称
	**/
	function initCombobox(xnxqData, jxxzData, njdmData){
		$('#CK_XNXQMC_CX').combobox({
			data:xnxqData,
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			panelHeight:'140', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data != ''){
					//初始化combobox时赋值
					$(this).combobox('setValue', '');
				}
			},
			//下拉列表值改变事件
			onChange:function(data){}
		});
	
		$('#CK_JXXZ_CX').combobox({
			data:jxxzData,
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			panelHeight:'140', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data != ''){
					//初始化combobox时赋值
					$(this).combobox('setValue', '');
				}
			},
			//下拉列表值改变事件
			onChange:function(data){}
		});
		
		$('#CK_NJDM_CX').combobox({
			data:njdmData,
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			panelHeight:'140', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data != ''){
					//初始化combobox时赋值
					$(this).combobox('setValue', '');
				}
			},
			//下拉列表值改变事件
			onChange:function(data){}
		});
	}
	
	/**加载科目列表datagrid控件，读取页面信息**/
	function loadSubjectGrid(){
		$('#subjectList').datagrid({
			url: '<%=request.getContextPath()%>/Svl_Kmsz',
			queryParams:{'active':'queSubjectList','sAuth':sAuth,'CK_XNXQMC_CX':$('#CK_XNXQMC_CX').combobox('getValue'),
				'CK_JXXZ_CX':$('#CK_JXXZ_CX').combobox('getValue'),'CK_ZYMC_CX':encodeURI($('#CK_ZYMC_CX').textbox('getValue')),
				'CK_KCMC_CX':encodeURI($('#CK_KCMC_CX').textbox('getValue')),'CK_KMLX_CX':$('#CK_KMLX_CX').combobox('getValue'),
				'CK_CYJD_CX':$('#CK_CYJD_CX').combobox('getValue'),'CK_KCLX_CX':$('#CK_KCLX_CX').combobox('getValue'),'CK_NJDM_CX':$('#CK_NJDM_CX').combobox('getValue')},
			title:'科目信息列表',
			width:'100%',
			nowrap: false,
			fit:true, //自适应父节点宽度和高度
			showFooter:true,
			striped:true,
			pagination:true,
			pageSize:20,
			pageNumber:1,
			singleSelect:false,
			rownumbers:true,
			fitColumns: true,
			columns:[[
				//field为读取数据的数据名，title为显示的数据名，width宽度设置，align数字在表格中显示的位置
				{field:'ck',checkbox:true},
				{field:'CK_ID',hidden:true},
				{field:'CK_XNXQMC',title:'学年学期名称',width:fillsize(0.1),align:'center'},
				{field:'CK_JXXZ',title:'教学性质',width:fillsize(0.05),align:'center'},
				{field:'CK_ZYMC',title:'专业名称',width:fillsize(0.2),align:'center'},
				{field:'CK_NJMC',title:'年级名称',width:fillsize(0.08),align:'center'},
				{field:'CK_KCMC',title:'课程名称',width:fillsize(0.2),align:'center'},
				{field:'CK_KMLX',title:'专业类型',width:fillsize(0.08),align:'center'},
				{field:'CK_SFCYJD',title:'是否参与绩点',width:fillsize(0.08),align:'center'},
				{field:'CK_SFCYLJ',title:'是否参与留级',width:fillsize(0.08),align:'center'},
				{field:'CK_XF',title:'学分',width:fillsize(0.05),align:'center'},
				{field:'CK_KCLX',title:'课程类型',width:fillsize(0.08),align:'center'}
			]],
			//双击某行时触发
			onDblClickRow:function(rowIndex,rowData){},
			//读取datagrid之前加载
			onBeforeLoad:function(){},
			//单击某行时触发
			onClickRow:function(rowIndex,rowData){
			},
			//加载成功后触发
			onLoadSuccess: function(data){
			}
		});
	};
	
	/**工具栏按钮调用方法，传入按钮的id
		@id 当前按钮点击事件
	**/
	function doToolbar(id){
		//查询科目列表
		if(id == 'queList'){
			loadSubjectGrid();
		}
		
		//设为文科、设为理科、参与绩点、不参与绩点、参与留级、不参与留级
		if(id=='wk' || id=='lk' || id=='cyjd' || id=='bcyjd' || id=='cylj' || id=='bcylj'){
			curSelId = loadSelectID();
			
			if(curSelId.length == 0){
				alertMsg('请至少选择一个科目');
				return;
			}
			
			$('#active').val('setSubject');
			$('#selectId').val(curSelId);
			$('#setType').val(id);
			$("#form1").submit();
		}
	}
	
	/**获取当前选中的所有数据ID**/
	function loadSelectID(){
		var selId = '';
		var selArray = $('#subjectList').datagrid('getSelections');
		
		for(var i=0; i<selArray.length; i++){
			selId += selArray[i].CK_ID+',';
		}
		selId = selId.substring(0, selId.length-1);
		
		return selId;
	}
	
	/**表单提交**/
	$('#form1').form({
		//定位到servlet位置的url
		url:'<%=request.getContextPath()%>/Svl_Kmsz',
		//当点击事件后触发的事件
		onSubmit: function(data){}, 
		//当点击事件并成功提交后触发的事件
		success:function(data){
			var json  =  eval("("+data+")");
			if(json[0].MSG == '保存成功'){
				showMsg(json[0].MSG);
				loadSubjectGrid();
			}else{
				alertMsg(json[0].MSG);
			}
		}
	});
</script>
</html>