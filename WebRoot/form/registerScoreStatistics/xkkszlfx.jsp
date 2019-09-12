<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="java.util.*"%>
<%@ page import="java.net.URLDecoder"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.src.develop.store.user.*"%>
<%@ page import="com.zhuozhengsoft.pageoffice.*"%>
<%
	/**
		创建人：yeq
		Create date: 2017.03.16
		功能说明：学科考试质量分析
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
	<title>考试质量分析</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/icon.css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/publicScript.js"></script>
	<style>
		.button {
            display:inline-block;
            width:88px;
            text-align:center;
            border: 1px solid #99BBE8;
            background-color:#E8F5FC;
            color:#000000;
            text-decoration:none;
            padding:3px;
            cursor:pointer;
        }
	</style>
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
	<div id="north" region="north" title="课程信息" style="height:59px; overflow:hidden;">
		<table id="ee" singleselect="true" width="100%" class="tablestyle">
			<tr>
				<td style="width:80px;" class="titlestyle">学年学期名称</td>
				<td>
					<select name="XNXQ_CX" id="XNXQ_CX" class="easyui-combobox" style="width:150px;">
					</select>
				</td>
				<td style="width:80px;" class="titlestyle">课程名称</td>
				<td>
					<input name="KCMC_CX" id="KCMC_CX" class="easyui-textbox" style="width:150px;"/>
				</td>
				<td style="width:80px;" class="titlestyle">教师工号</td>
				<td>
					<input name="JSGH_CX" id="JSGH_CX" class="easyui-textbox" style="width:150px;"/>
				</td>
				<td style="width:80px;" class="titlestyle">教师姓名</td>
				<td>
					<input name="JSXM_CX" id="JSXM_CX" class="easyui-textbox" style="width:150px;"/>
				</td>
				<td style="width:150px;" align="center">
					<a href="#" onclick="doToolbar(this.id)" id="queList" class="easyui-linkbutton" plain="true" iconcls="icon-search">查询</a>
				</td>
			</tr>
	    </table>
	</div>
	<div region="center">
		<table id="teaCourseList" style="width:100%;"></table>
	</div>
	
	<!-- 成绩打印预览页面 -->
	<div id="printViewDialog">
		<iframe id="printViewIframe" name="printViewIframe" src='' style='width:100%; height:100%;' frameborder="0" scrolling="no"></iframe>
	</div>
	
   	<!-- 下载excel -->
	<iframe id="exportIframe" src="" style="width:0; height:0;"></iframe>
</body>
<script type="text/javascript">
	var sAuth = '<%=sAuth%>';
	var enterType = '<%=MyTools.StrFiltr(request.getParameter("enterType"))%>';
	var pofOpenType = '<%=MyTools.getProp(request, "Base.pofOpenType")%>';
	var viewFilePath = '';
	var curType = '';
	var curXnxq = '';
	var curKcmc = '';
	var curJsbh = '';
	var curJsxm = '';
	
	$(document).ready(function(){
		if(enterType == 'scoreRegister'){
			showTips();
		}
	
		loadTeaCourseGrid();
		initDialog();//初始化对话框
		initCombobox();
	});
	
	function showTips(){
		var msgStr = '<font color="red">注意：使用本模块需安装<font color="#3B73C1">Microsoft Office</font>和<font color="#3B73C1">PageOffice客户端</font></font><br/><br/>如出现下列情况，请下载安装插件：<br/>1、首次使用当前页面<br/>2、点击考试按钮无反应<br/>3、点击考试按钮后提示无法显示该网页<br/>4、点击考试按钮后一直停留在读取状态 <br/><br/><a href="<%=request.getContextPath()%>/posetup.exe">点击下载PageOffice客户端</a>';
	
		$.messager.show({
			title:'提示',
			width:350,
			height:200,
			msg:msgStr,
			showType:'slide',
			timeout:0
		});
	}
	
	/**加载 dialog控件**/
	function initDialog(){
		$('#printViewDialog').dialog({
			title:'预览',
			maximized:true,
			modal:true,
			closed: true,   
			cache: false, 
			draggable:false,//是否可移动dialog框设置
			//关闭事件
			onClose:function(data){
				$("#printViewIframe").attr('src', '');
				//删除预览文件
				delViewFile();
			}
		});
	}
	
	/**加载combobox控件**/
	function initCombobox(){
		$('#XNXQ_CX').combobox({
			url:'<%=request.getContextPath()%>/Svl_Xkkszlfx?active=loadXnxqCombo',
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
			}
		});
	}
	
	/**加载班级列表datagrid控件，读取页面信息**/
	function loadTeaCourseGrid(){
		$('#teaCourseList').datagrid({
			url: '<%=request.getContextPath()%>/Svl_Xkkszlfx',
			queryParams:{'active':'queTeaCourseList','sAuth':sAuth,'enterType':enterType,'XNXQBM':$('#XNXQ_CX').combobox('getValue'),
				'COURSENAME':encodeURI($('#KCMC_CX').textbox('getValue')),'TEACODE':encodeURI($('#JSGH_CX').textbox('getValue')),
				'TEANAME':encodeURI($('#JSXM_CX').val())},
			title:'课程信息列表',
			width:'100%',
			nowrap: false,
			fit:true, //自适应父节点宽度和高度
			showFooter:true,
			striped:true,
			pagination:true,
			pageSize:20,
			pageNumber:1,
			singleSelect:true,
			rownumbers:true,
			fitColumns: true,
			columns:[[
				//field为读取数据的数据名，title为显示的数据名，width宽度设置，align数字在表格中显示的位置
				{field:'学年学期名称',title:'学年学期名称',width:fillsize(0.25),align:'center'},
				{field:'课程名称',title:'课程名称',width:fillsize(0.3),align:'center'},
				{field:'任课教师',title:'任课教师',width:fillsize(0.25),align:'center'},
				{field:'col',title:'操作',width:fillsize(0.2),align:'center',formatter:function(value,rec){
					var buttonStr = '';
					
					if(pofOpenType == 'normal'){
						buttonStr = "<input type='button' value='[期中考试]' onclick='loadStatisticsInfo(\"qz\", \"" + rec.学年学期编码 +"\", \"" + rec.课程名称 + "\", \"" + rec.登分教师编号 + "\", \"" + rec.任课教师 + "\")' style='cursor:pointer;'>" + 
							"<input type='button' value='[期末考试]' onclick='loadStatisticsInfo(\"qm\", \"" + rec.学年学期编码 +"\", \"" + rec.课程名称 + "\", \"" + rec.登分教师编号 + "\", \"" + rec.任课教师 + "\")' style='margin-left:5px; cursor:pointer;'>";
					}else{
						buttonStr = '<a class="button" href="<%=PageOfficeLink.openWindow(request, "xkkszlfxView.jsp?EXAMTYPE=qz&XNXQBM=' + rec.学年学期编码 + '&COURSENAME=' + encodeURI(encodeURI(rec.课程名称)) + '&TEACODE=' + rec.登分教师编号 + '&TEANAME=' + encodeURI(encodeURI(rec.任课教师)) + '","width=1000px;height=800px;")%>">[期中考试]</a>' +
							'<a class="button" style="margin-left:5px;" href="<%=PageOfficeLink.openWindow(request, "xkkszlfxView.jsp?EXAMTYPE=qm&XNXQBM=' + rec.学年学期编码 + '&COURSENAME=' + encodeURI(encodeURI(rec.课程名称)) + '&TEACODE=' + rec.登分教师编号 + '&TEANAME=' + encodeURI(encodeURI(rec.任课教师)) + '","width=1000px;height=800px;")%>">[期末考试]</a>';
					}
					return buttonStr;
				}}
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
		//查询课程列表
		if(id == 'queList'){
			loadTeaCourseGrid();
		}
	}
	
	/**读取统计信息*/
	function loadStatisticsInfo(type, xnxqbm, kcmc, jsbh, jsxm){
		curType = type;
		curXnxq = xnxqbm;
		curKcmc = kcmc;
		curJsbh = jsbh;
		curJsxm = jsxm;
		
		$('#printViewDialog').dialog('open');
			$("#printViewIframe").attr("src","<%=request.getContextPath()%>/form/registerScoreStatistics/xkkszlfxView.jsp?EXAMTYPE=" + type + 
				"&XNXQBM=" + xnxqbm + "&COURSENAME=" + encodeURI(kcmc) + "&TEACODE=" + jsbh + "&TEANAME=" + encodeURI(jsxm));
	}
	
	/**删除预览文件**/
	function delViewFile(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Cjcx',
			data : 'active=delViewFile&filePath=' + viewFilePath,
			dataType:"json",
			success : function(data){
				if(data[0].MSG == '删除成功') viewFilePath = '';
			}
		});
	}
	
	function closeDialog(){
		$('#printViewDialog').dialog('close');
	}
	
	/**下载*/
	function downloadStuReport(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Xkkszlfx',
			data : 'active=exportReport&EXAMTYPE=' + curType + '&XNXQBM=' + curXnxq + '&COURSENAME=' + encodeURI(curKcmc) + '&TEACODE=' + curJsbh + '&TEANAME=' + encodeURI(curJsxm),
			dataType:"json",
			success : function(data){
				if(data[0].MSG == '文件生成成功'){
					//下载文件到本地
					$("#exportIframe").attr("src", '<%=request.getContextPath()%>/form/registerScoreManage/download.jsp?filePath=' + encodeURIComponent(data[0].filePath));
				}else{
					alertMsg(data[0].MSG);
				}
			}
		});
	}
</script>
</html>