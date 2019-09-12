<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="java.util.Vector"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.src.develop.store.user.*"%>
<%
	/**
		创建人：yeq
		Create date: 2016.07.21
		功能说明：查询导出补考信息
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
	<title>补考统计信息</title>
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
	<div id="north" region="north" title="" style="height:60px;">
		<table>
			<tr>
				<td><a href="#" id="export" class="easyui-linkbutton" plain="true" iconcls="icon-submit" onClick="doToolbar(this.id);">导出</a></td>
			</tr>
		</table>
		<table id="ee" singleselect="true" width="100%" class="tablestyle">
			<tr>
				<td style="width:150px;" class="titlestyle">学年</td>
				<td>
					<select name="ic_xn" id="ic_xn" class="easyui-combobox" style="width:200px;">
					</select>
				</td>
			</tr>
	    </table>
	</div>
	<div region="center">
		<table id="ljmdList"></table>
	</div>
	
	<!-- 不及格科目信息对话框 -->
	<div id="detailDialog">
		<div class="easyui-layout" style="width:100%; height:100%;">
			<div region="north" style="height:27px;">
				<table style="width:100%;" class="tablestyle">
					<tr>
						<td class="titlestyle" style="width:80px; font-weight:bold;">学号</td>
						<td id="xhContent" class="titlestyle">&nbsp;</td>
						<td class="titlestyle" style="width:80px; font-weight:bold;">姓名</td>
						<td id="xmContent" class="titlestyle">&nbsp;</td>
						<td class="titlestyle" style="width:80px; font-weight:bold;">行政班名称</td>
						<td id="bjContent" class="titlestyle">&nbsp;</td>
					</tr>
				</table>
			</div>
			<div region="center">
				<table id="detailList"></table>
			</div>
		</div>
	</div>
	
	<!-- 下载excel -->
	<iframe id="exportIframe" src="" style="width:0; height:0;"></iframe>
</body>
<script type="text/javascript">
	var curXn = '<%=MyTools.StrFiltr(request.getParameter("curXn"))%>';
	var type = '';

	$(document).ready(function(){
		$('#export').linkbutton('disable');
		initCombobox();
		initDialog();//初始化对话框
	});
	
	/**加载combobox控件*/
	function initCombobox(){
		$('#ic_xn').combobox({
			url:'<%=request.getContextPath()%>/Svl_Cjcx?active=loadXnCombo',
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			panelHeight:'140', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data != ''){
					//初始化combobox时赋值
					$(this).combobox('setValue', curXn);
				}
			},
			//下拉列表值改变事件
			onChange:function(data){
				$('#export').linkbutton('disable');
				if(data >= curXn){
					type = 'now';
				}else{
					type = 'his';
				}
				loadLjmdGrid();
			}
		});
	}
	
	/**加载 dialog控件**/
	function initDialog(){
		$('#detailDialog').dialog({   
			title: '不及格科目信息',   
			width: 750,//宽度设置   
			height: 500,//高度设置 
			modal:true,
			closed: true,   
			cache: false, 
			draggable:true,//是否可移动dialog框设置
			//读取事件
			onLoad:function(data){
			},
			//关闭事件
			onClose:function(data){
				$('#xhContent').html('');
				$('#xmContent').html('');
				$('#bjContent').html('');
				$('#detailList').datagrid('loadData',{total:0,rows:[]});
			}
		});
	}
	
	/**加载班级列表datagrid控件，读取页面信息**/
	function loadLjmdGrid(){
		$('#ljmdList').datagrid({
			url: '<%=request.getContextPath()%>/Svl_Cjcx',
			queryParams:{'active':'queLjmdList','type':type,'XNXQ':$('#ic_xn').combobox('getValue')},
			title:'留级信息列表',
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
				{field:'学号',title:'学号',width:fillsize(0.1),align:'center'},
				{field:'姓名',title:'姓名',width:fillsize(0.1),align:'center'},
				{field:'系别',title:'系别',width:fillsize(0.2),align:'center'},
				{field:'行政班名称',title:'行政班名称',width:fillsize(0.4),align:'center'},
				{field:'不及格科目数',title:'不及格科目数',width:fillsize(0.1),align:'center'},
				{field:'col4',title:'操作',width:fillsize(0.1),align:'center',
					formatter:function(value,rec){
						return	"<input type='button' value='[详情]' onclick='openDetail(\"" + rec.学号 + "\",\"" + rec.姓名 + "\",\"" + rec.行政班名称 + "\");' style=\"width:60px; cursor:pointer;\"/>"; 
				}}
			]],
			//加载成功后触发
			onLoadSuccess: function(data){
				iKeyCode = '';
				$('#export').linkbutton('enable');
			}
		});
	};
	
	/**加载补考信息列表datagrid控件，读取页面信息**/
	function loadDetailGrid(stuCode){
		$('#detailList').datagrid({
			url: '<%=request.getContextPath()%>/Svl_Cjcx',
			queryParams:{'active':'queBjgDetialList','type':type,'XNXQ':$('#ic_xn').combobox('getValue'),'STUCODE':stuCode},
			title:'',
			width:'100%',
			nowrap: false,
			fit:true, //自适应父节点宽度和高度
			showFooter:true,
			striped:true,
			//pagination:true,
			//pageSize:20,
			//pageNumber:1,
			singleSelect:true,
			rownumbers:true,
			fitColumns: true,
			columns:[[
			//a.学号,a.姓名,d.行政班名称,c.课程名称,a.总评 as 成绩
				//field为读取数据的数据名，title为显示的数据名，width宽度设置，align数字在表格中显示的位置
				{field:'课程名称',title:'课程名称',width:fillsize(0.3),align:'center'},
				{field:'学年学期',title:'学年学期',width:fillsize(0.15),align:'center'},
				{field:'课程类型',title:'课程类型',width:fillsize(0.15),align:'center'},
				{field:'学分',title:'学分',width:fillsize(0.08),align:'center'},
				{field:'总评',title:'总评',width:fillsize(0.08),align:'center'},
				{field:'重修1',title:'重修1',width:fillsize(0.08),align:'center'},
				{field:'重修2',title:'重修1',width:fillsize(0.08),align:'center'},
				{field:'补考',title:'补考',width:fillsize(0.08),align:'center'},
				{field:'大补考',title:'大补考',width:fillsize(0.08),align:'center'}
			]],
			//加载成功后触发
			onLoadSuccess: function(data){
			}
		});
	};
	
	/**不及格科目详情*/
	function openDetail(stuCode, stuName, className){
		$('#xhContent').html(stuCode);
		$('#xmContent').html(stuName);
		$('#bjContent').html(className);
		loadDetailGrid(stuCode);
		$('#detailDialog').dialog('open');
	}
	
	/**工具栏按钮调用方法，传入按钮的id
		@id 当前按钮点击事件
	**/
	function doToolbar(id){ 
		//导出
		if(id == 'export'){
			window.parent.$('#maskFont').html('文件生成中，请稍后...');
			window.parent.$('#divPageMask').show();
			
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_Cjcx',
				data : 'active=ljmdExport&type=' + type + '&XNXQ=' + $('#ic_xn').combobox('getValue'),
				dataType:"json",
				success : function(data){
					if(data[0].MSG == '文件生成成功'){
						//下载文件到本地
						$("#exportIframe").attr("src", '<%=request.getContextPath()%>/form/registerScoreManage/download.jsp?filePath=' + encodeURIComponent(data[0].filePath));
					}else{
						alertMsg(data[0].MSG);
					}
					window.parent.$('#divPageMask').hide();
				}
			});
		}
	}
</script>
</html>