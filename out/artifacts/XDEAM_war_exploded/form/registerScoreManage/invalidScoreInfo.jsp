<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="java.util.Vector"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.src.develop.store.user.*"%>
<%
	/**
		创建人：yeq
		Create date: 2016.08.08
		功能说明：查询无效成绩信息
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
	<title>无效成绩信息</title>
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
	<div id="north" region="north" title="" style="height:90px; overflow:hidden;">
		<table>
			<tr>
				<td><a href="#" id="recoveryScore" class="easyui-linkbutton" plain="true" iconcls="icon-ok" onClick="doToolbar(this.id);">恢复</a></td>
				<!-- <td><a href="#" id="delScore" class="easyui-linkbutton" plain="true" iconcls="icon-cancel" onClick="doToolbar(this.id);">删除</a></td> -->
			</tr>
		</table>
		<table id="ee" singleselect="true" width="100%" class="tablestyle">
			<tr>
				<td style="width:150px;" class="titlestyle">学年学期</td>
				<td>
					<select name="ic_xnxq" id="ic_xnxq" class="easyui-combobox" style="width:150px;">
					</select>
				</td>
				<td style="width:150px;" class="titlestyle">课程名称</td>
				<td>
					<input name="ic_courseName" id="ic_courseName" style="width:150px;"/>
				</td>
				<td style="width:150px;" align="center">
					<div id="query" style="width:56px; height:26px; border:1px solid #EFEFEF; background-color:#EFEFEF; cursor:pointer;" onclick="doToolbar(this.id)" 
						onmouseenter="$(this).css('border','1px solid #B7D2FF'); $(this).css('background-color','#E9F1FF');" 
						onmouseleave="$(this).css('border','1px solid #EFEFEF'); $(this).css('background-color','#EFEFEF');">
						<table style="width:100%; height:100%; border:0;" cellpadding="0" cellspacing="0">
							<tr>
								<td style="border:0;"><img src="<%=request.getContextPath()%>/css/themes/icons/search.png"/></td>
								<td style="border:0;">查询</td>
							</tr>
						</table>
					</div>
					<!-- <a href="#" onclick="doToolbar(this.id)" id="query" class="easyui-linkbutton" plain="true" iconcls="icon-search">查询</a> -->
				</td>
			</tr>
			<tr>
				<td style="width:150px;" class="titlestyle">学号</td>
				<td>
					<input name="ic_stuCode" id="ic_stuCode" style="width:150px;"/>
				</td>
				<td style="width:150px;" class="titlestyle">姓名</td>
				<td>
					<input name="ic_stuName" id="ic_stuName" style="width:150px;"/>
				</td>
				<td>&nbsp;</td>
			</tr>
	    </table>
	</div>
	<div region="center">
		<table id="scoreList"></table>
	</div>
</body>
<script type="text/javascript">
	$(document).ready(function(){
		initCombobox();//页面初始化加载数据
		loadScoreGrid();
	});
	
	/**加载combobox控件*/
	function initCombobox(){
		$('#ic_xnxq').combobox({
			url:'<%=request.getContextPath()%>/Svl_Cjcx?active=loadXnxqCombo', 
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
			onChange:function(data){
			}
		});
	}
	
	/**加载班级列表datagrid控件，读取页面信息**/
	function loadScoreGrid(){
		$('#scoreList').datagrid({
			url: '<%=request.getContextPath()%>/Svl_Cjcx',
			queryParams:{'active':'queInvalidScoreList','XNXQ':$('#ic_xnxq').combobox('getValue'),'KCMC':encodeURI($('#ic_courseName').val()),
				'STUCODE':encodeURI($('#ic_stuCode').val()),'STUNAME':encodeURI($('#ic_stuName').val())},
			title:'无效成绩信息列表',
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
				{field:'编号',hidden:true},
				{field:'ck',checkbox:true},
				{field:'学号',title:'学号',width:fillsize(0.1),align:'center'},
				{field:'姓名',title:'姓名',width:fillsize(0.1),align:'center'},
				{field:'行政班名称',title:'班级名称',width:fillsize(0.2),align:'center'},
				{field:'学年学期',title:'学年学期',width:fillsize(0.1),align:'center'},
				{field:'课程名称',title:'课程名称',width:fillsize(0.2),align:'center'},
				{field:'平时',title:'平时',width:fillsize(0.065),align:'center'},
				{field:'期中',title:'期中',width:fillsize(0.065),align:'center'},
				{field:'实训',title:'实训',width:fillsize(0.065),align:'center'},
				{field:'期末',title:'期末',width:fillsize(0.065),align:'center'},
				{field:'总评',title:'总评',width:fillsize(0.065),align:'center'},
				{field:'重修1',title:'重修1',width:fillsize(0.065),align:'center'},
				{field:'重修2',title:'重修2',width:fillsize(0.065),align:'center'},
				{field:'补考',title:'补考',width:fillsize(0.065),align:'center'},
				{field:'大补考',title:'大补考',width:fillsize(0.065),align:'center'}
			]],
			//加载成功后触发
			onLoadSuccess: function(data){
			}
		});
	};
	
	/**工具栏按钮调用方法，传入按钮的id
		@id 当前按钮点击事件
	**/
	function doToolbar(id){
		if(id == 'query'){
			loadScoreGrid();
		}
		
		//恢复成绩
		if(id=='recoveryScore' || id=='delScore'){
			var scoreData = $('#scoreList').datagrid('getSelections');
			if(scoreData.length == 0){
				alertMsg('请至少选择一条成绩信息');
				return;
			}
			
			var curSelCode = '';
			for(var i=0; i<scoreData.length; i++){
				curSelCode += scoreData[i].编号+',';
			}
			curSelCode = curSelCode.substring(0, curSelCode.length-1);
		
			if(id == 'recoveryScore') ConfirmMsg('是否确定要恢复当前选中的成绩信息？', 'recoveryScore("' + curSelCode + '");', '');
			if(id == 'delScore') ConfirmMsg('是否确定要彻底删除当前选中的成绩信息（无法恢复）？', 'delScore("' + curSelCode + '");', '');
		}
	}
	
	/**恢复成绩信息*/
	function recoveryScore(code){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Cjcx',
			data : 'active=recoveryScore&iKeyCode=' + code,
			dataType:"json",
			success : function(data) {
				if(data[0].MSG == '恢复成功'){
					showMsg(data[0].MSG);
					loadScoreGrid();
				}else{
					alertMsg(data[0].MSG);
				}
			}
		});
	}
	
	/**删除成绩信息*/
	/*
	function delScore(code){
		$.ajax({
			type : "POST",
			url : '<-%=request.getContextPath()%>/Svl_Cjcx',
			data : 'active=delScore&iKeyCode=' + code,
			dataType:"json",
			success : function(data) {
				if(data[0].MSG == '删除成功'){
					showMsg(data[0].MSG);
					loadScoreGrid();
				}else{
					alertMsg(data[0].MSG);
				}
			}
		});
	}
	*/
</script>
</html>