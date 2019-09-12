<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="java.util.Vector"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.src.develop.store.user.*"%>
<%
	/**
		创建人：yeq
		Create date: 2016.11.14
		功能说明：成绩覆盖
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
	<title>成绩覆盖</title>
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
	<div id="north" region="north" title="" style="height:166px; overflow:hidden;">
		<table style="width:100%;" class="tablestyle">
			<tr>
				<td style="width:150px;" class="titlestyle">覆盖成绩科目</td>
				<td>
					<select name="score_subject" id="score_subject" class="easyui-combobox" style="width:330px;">
					</select>
				</td>
			<tr>
			<tr>
				<td class="titlestyle">覆盖成绩类型</td>
				<td>
					<select name="score_type" id="score_type" class="easyui-combobox" style="width:150px;" panelHeight="auto" editable="false">
						<option value="0">总评</option>
						<option value="1">补考</option>
						<option value="2">大补考</option>
					</select>
				</td>
			<tr>
			<tr>
				<td class="titlestyle">覆盖成绩范围</td>
				<td>
					<input id="score_start" name="score_start" class="easyui-numberbox" max="100" min="0" precision="1" style="width:50px;"/>
					分&nbsp;&nbsp;到&nbsp;&nbsp;&nbsp;&nbsp;
					<input id="score_end" name="score_end" class="easyui-numberbox" max="100" min="0" precision="1" style="width:50px;"/>分
					&nbsp;&nbsp;&nbsp;&nbsp;<font color="blue">提示：如不填写，默认覆盖所有学号范围内的学生成绩。</font>
				</td>
			<tr>
			<tr>
				<td class="titlestyle">覆盖成绩目标</td>
				<td>
					<input id="score_target" name="score_target" class="easyui-numberbox" max="100" min="-16" precision="0" style="width:50px;"/>
					<a href="#" id="dmsm" class="easyui-linkbutton" plain="true" iconcls="icon-assets" onClick="doToolbar(this.id);" style="margin-left:10px;">成绩代码说明</a>
				</td>
			<tr>
			<tr>
				<td class="titlestyle">覆盖学号范围</td>
				<td>
					<input id="stu_start" style="width:150px;"/>
					&nbsp;&nbsp;到&nbsp;&nbsp;
					<input id="stu_end" style="width:150px;"/>
				</td>
			<tr>
			<tr>
				<td colspan="2" style="text-align:center;">
					<a id="coverPreview" href="#" onclick="doToolbar(id);" class="easyui-linkbutton" plain="true" iconcls="icon-search">预览</a>
					<a id="confirmCover" href="#" onclick="doToolbar(id);" class="easyui-linkbutton" plain="true" iconcls="icon-ok">确认覆盖</a>
				</td>				
			</tr>
	    </table>
	</div>
	<div region="center">
		<table id="stuScoreList"></table>
	</div>
	
	<!-- 文字成绩代码说明 -->
	<div id="dmsmDialog">
		<table class="tablestyle" style="width:100%; height:100%; text-align:center;" cellspacing="0" cellpadding="0">
			<tr><td style="width:50%; font-weight:bold;">代码</td><td style="width:50%; font-weight:bold;">名称</td></tr>
			<tr><td>-1</td><td>免修</td></tr>
			<tr><td>-2</td><td>作弊</td></tr>
			<tr><td>-3</td><td>取消资格</td></tr>
			<tr><td>-4</td><td>缺考</td></tr>
			<tr><td>-5</td><td>缓考</td></tr>
			<tr><td>-6</td><td>优</td></tr>
			<tr><td>-7</td><td>良</td></tr>
			<tr><td>-8</td><td>中</td></tr>
			<tr><td>-9</td><td>及格</td></tr>
			<tr><td>-10</td><td>不及格</td></tr>
			<tr><td>-11</td><td>合格</td></tr>
			<tr><td>-12</td><td>不合格</td></tr>
			<tr><td>-13</td><td>补及</td></tr>
			<tr><td>-15</td><td>达标</td></tr>
			<tr><td>-16</td><td>重考</td></tr>
		</table>
	</div>
</body>
<script type="text/javascript">
	var scoreName = '';

	$(document).ready(function(){
		initDialog();//初始化对话框
		initCombobox();//页面初始化加载数据
	});
	
	/**加载combobox控件*/
	function initCombobox(){
		$('#score_subject').combobox({
			url:'<%=request.getContextPath()%>/Svl_Cjcx?active=loadCoverSubject',
			valueField:'comboValue',
			textField:'comboName',
			editable:true,
			panelHeight:'140', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data != ''){
					//初始化combobox时赋值
					$(this).combobox('setValue', '');
				}
			}
		});
	}
	
	/**加载 dialog控件**/
	function initDialog(){
		$('#dmsmDialog').dialog({   
			title: '文字成绩代码说明',
			width: 300,//宽度设置   
			height: 450,//高度设置 
			modal:true,
			closed: true,   
			cache: false, 
			draggable:true,//是否可移动dialog框设置
			//读取事件
			onLoad:function(data){
			},
			//关闭事件
			onClose:function(data){
			}
		});
	}
	
	/**加载学生成绩列表列表datagrid控件，读取页面信息**/
	function loadStuScoreList(){
		$('#stuScoreList').datagrid({
			url: '<%=request.getContextPath()%>/Svl_Cjcx',
			queryParams:{'active':'queStuScoreList','score_subject':$('#score_subject').combobox('getValue'),
				'score_type':$('#score_type').combobox('getValue'),'score_start':$('#score_start').numberbox('getValue'),
				'score_end':$('#score_end').numberbox('getValue'),'score_target':$('#score_target').numberbox('getValue'),
				'stu_start':$('#stu_start').val(),'stu_end':$('#stu_end').val()},
			title:'学生成绩信息列表',
			width:'100%',
			nowrap: false,
			fit:true, //自适应父节点宽度和高度
			showFooter:true,
			striped:true,
			pagination:true,
			pageSize:100,
			pageList: [20,50,100,150,200],
			pageNumber:1,
			singleSelect:true,
			rownumbers:true,
			fitColumns: true,
			columns:[[
				//field为读取数据的数据名，title为显示的数据名，width宽度设置，align数字在表格中显示的位置
				{field:'学号',title:'学号',width:fillsize(0.15),align:'center'},
				{field:'姓名',title:'姓名',width:fillsize(0.15),align:'center'},
				{field:'行政班名称',title:'班级名称',width:fillsize(0.35),align:'center'},
				{field:'学年学期',title:'学年学期',width:fillsize(0.15),align:'center'},
				{field:scoreName,title:scoreName,width:fillsize(0.1),align:'center'},
				{field:'覆盖成绩',title:'覆盖成绩',width:fillsize(0.1),align:'center'}
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
		//代码说明
		if(id == 'dmsm'){
			$('#dmsmDialog').dialog('open');
		}
		
		//确认覆盖
		if(id == 'confirmCover'){
			if($('#score_subject').combobox('getValue') == ''){
				alertMsg('请选择覆盖成绩的科目');
				return;
			}
			/*
			if($('#score_target').numberbox('getValue') == ''){
				alertMsg('请填写覆盖成绩');
				return;
			}
			*/
			var reg = /^\+?[1-9][0-9]*$/;//正整数
			if($('#stu_start').val()!='' && !reg.test($('#stu_start').val())){
				alertMsg('起始学号格式不正确');
				return;
			}
			if($('#stu_end').val()!='' && !reg.test($('#stu_end').val())){
				alertMsg('结束学号格式不正确');
				return;
			}
			
			ConfirmMsg('请仔细确认预览列表中将要覆盖的学生成绩信息，是否确定要执行覆盖成绩操作？', 'confirmScoreCover();', '');
		}
		
		//预览
		if(id == 'coverPreview'){
			scoreName = '';
			var str = $('#score_type').combobox('getValue');
			if(str == '0')
				scoreName = '总评';
			if(str == '1')
				scoreName = '补考';
			if(str == '2')
				scoreName = '大补考';
			
			if($('#score_subject').combobox('getValue') == ''){
				alertMsg('请选择覆盖成绩的科目');
				return;
			}
			
			var reg = /^\+?[1-9][0-9]*$/;//正整数
			if($('#stu_start').val()!='' && !reg.test($('#stu_start').val())){
				alertMsg('起始学号格式不正确');
				return;
			}
			if($('#stu_end').val()!='' && !reg.test($('#stu_end').val())){
				alertMsg('结束学号格式不正确');
				return;
			}
			
			loadStuScoreList();
		}
	}
	
	/**确认覆盖成绩*/
	function confirmScoreCover(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Cjcx',
			data : 'active=confirmScoreCover&score_subject=' + $('#score_subject').combobox('getValue') + 
				'&score_type=' + $('#score_type').combobox('getValue') + '&score_start=' + $('#score_start').numberbox('getValue') +
				'&score_end=' + $('#score_end').numberbox('getValue') + '&score_target=' + $('#score_target').numberbox('getValue') +
				'&stu_start=' + $('#stu_start').val() + '&stu_end=' + $('#stu_end').val(),
			dataType:"json",
			success : function(data){
				if(data[0].MSG == '覆盖成功'){
					showMsg(data[0].MSG);
					loadStuScoreList();
				}else{
					alertMsg(data[0].MSG);
				}
			}
		});
	}
</script>
</html>