<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="java.util.Vector"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.src.develop.store.user.*"%>
<%
	/**
		创建人：yeq
		Create date: 2016.08.08
		功能说明：查询绩点统计信息
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
	<title>绩点统计信息</title>
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
	<div id="north" region="north" title="" style="height:65px;">
		<table>
			<tr>
				<td><a href="#" id="export" class="easyui-linkbutton" plain="true" iconcls="icon-submit" onClick="doToolbar(this.id);">导出</a></td>
			</tr>
		</table>
		<table id="ee" singleselect="true" width="100%" class="tablestyle">
			<tr>
				<td style="width:150px;" class="titlestyle">学年</td>
				<td>
					<select name="ic_xn" id="ic_xn" class="easyui-combobox" style="width:150px;">
					</select>
				</td>
				<td style="width:150px;" class="titlestyle">专业名称</td>
				<td>
					<input name="ic_zymc" id="ic_zymc" style="width:150px;"/>
				</td>
				<td style="width:150px;" align="center">
					<div id="queJdMajorList" style="width:56px; height:26px; border:1px solid #EFEFEF; background-color:#EFEFEF; cursor:pointer;" onclick="doToolbar(this.id)" 
						onmouseenter="$(this).css('border','1px solid #B7D2FF'); $(this).css('background-color','#E9F1FF');" 
						onmouseleave="$(this).css('border','1px solid #EFEFEF'); $(this).css('background-color','#EFEFEF');">
						<table style="width:100%; height:100%; border:0;" cellpadding="0" cellspacing="0">
							<tr>
								<td style="border:0;"><img src="<%=request.getContextPath()%>/css/themes/icons/search.png"/></td>
								<td style="border:0;">查询</td>
							</tr>
						</table>
					</div>
					<!-- <a href="#" onclick="doToolbar(this.id)" id="queJdList" class="easyui-linkbutton" plain="true" iconcls="icon-search">查询</a> -->
				</td>
			</tr>
	    </table>
	</div>
	<div region="center">
		<table id="majorList"></table>
	</div>
	
	<!-- 绩点信息对话框 -->
	<div id="jdInfoDialog">
		<div class="easyui-layout" style="width:100%; height:100%;">
			<div region="north" style="height:34px;">
				<table id="ee" width="100%" class="tablestyle">
					<tr>
						<td style="width:80px;" class="titlestyle">学号</td>
						<td>
							<input id='ic_xh' name='ic_xh' class="easyui-textbox" style="width:140px;"/>
						</td>
						<td style="width:80px;" class="titlestyle">姓名</td>
						<td>
							<input id='ic_xm' name='ic_xm' class="easyui-textbox" style="width:140px;"/>
						</td>
						<td style="width:80px;" class="titlestyle">班级</td>
						<td>
							<input id='ic_bjmc' name='ic_bjmc' class="easyui-textbox" style="width:140px;"/>
						</td>
						<td style="width:80px;" align="center">
							<!-- <a href="#" onclick="doToolbar(this.id)" id="queJdInfoList" class="easyui-linkbutton" plain="true" iconcls="icon-search">查询</a> -->
							<div id="queJdInfoList" onclick="doToolbar(this.id)" style="width:55px; height:26px; 
								border:1px solid #EFEFEF; background-color:#EFEFEF; cursor:pointer; line-height:26px;"
								onmouseenter="$(this).css('border','1px solid #B7D2FF'); $(this).css('background-color','#E9F1FF');" 
								onmouseleave="$(this).css('border','1px solid #EFEFEF'); $(this).css('background-color','#EFEFEF');">
								<img src="<%=request.getContextPath()%>/css/themes/icons/search.png" style="float:left; margin-top:5px; margin-left:3px;"/>查询
							</div>
						</td>
					</tr>
			    </table>
			</div>
			<div region="center">
				<table id="jdInfoList"></table>
			</div>
		</div>
	</div>
	
	<!-- 下载excel -->
	<iframe id="exportIframe" src="" style="width:0; height:0;"></iframe>
</body>
<script type="text/javascript">
	var curXnxq = '<%=MyTools.StrFiltr(request.getParameter("curXnxq"))%>';
	var curXn = '';
	var curSelXn = '';
	var curSelZy = '';

	$(document).ready(function(){
		if(curXnxq.substring(4) == '1'){
			curXn = parseInt(curXnxq.substring(0, 4))-1;
		}else{
			curXn = curXnxq.substring(0, 4);
		}
		initDialog();//初始化对话框
		initCombobox();//页面初始化加载数据
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
				
				loadJdMajorGrid();
			}
		});
	}
	
	/**加载 dialog控件**/
	function initDialog(){
		$('#jdInfoDialog').dialog({   
			title: '绩点统计信息',   
			width: 700,//宽度设置   
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
				$('#ic_xh').textbox('setValue', '');
				$('#ic_xm').textbox('setValue', '');
				$('#ic_bjmc').textbox('setValue', '');
				$('#jdInfoList').datagrid('loadData',{total:0,rows:[]});
			}
		});
	}
	
	/**加载班级列表datagrid控件，读取页面信息**/
	function loadJdMajorGrid(){
		$('#majorList').datagrid({
			url: '<%=request.getContextPath()%>/Svl_Cjcx',
			queryParams:{'active':'queJdMajorList','XNXQ':$('#ic_xn').combobox('getValue'),
				'ZYMC':encodeURI($('#ic_zymc').val())},
			title:'专业信息列表',
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
				{field:'学年',title:'学年',width:fillsize(0.18),align:'center'},
				{field:'专业代码',title:'专业代码',width:fillsize(0.18),align:'center'},
				{field:'专业名称',title:'专业名称',width:fillsize(0.5),align:'center'},
				{field:'col4',title:'操作',width:fillsize(0.14),align:'center',
					formatter:function(value,rec){
						return	"<input type='button' value='[详情]' onclick='openJdInfo(\"" + rec.学年 + "\",\"" + rec.专业代码 + "\",\"" + rec.专业名称 + "\");' style=\"width:60px; cursor:pointer;\"/>"
							/*"<input type='button' value='[导出]' onclick='exportJdInfo(\"" + rec.学年学期编码 + "\");' style=\"margin-left:5px; width:60px; cursor:pointer;\"/>";*/
				}}
			]],
			//加载成功后触发
			onLoadSuccess: function(data){
				iKeyCode = '';
			}
		});
	};
	
	/**打开绩点信息详情*/
	function openJdInfo(xn, majorCode, majorName){
		curSelXn = xn;
		curSelZy = majorCode;
	
		loadJdInfoGrid();
		$('#jdInfoDialog').dialog('setTitle', xn + '学年' + majorName + ' 绩点统计信息');
		$('#jdInfoDialog').dialog('open');
	}
	
	/**加载绩点信息列表datagrid控件，读取页面信息**/
	function loadJdInfoGrid(){
		$('#jdInfoList').datagrid({
			url: '<%=request.getContextPath()%>/Svl_Cjcx',
			queryParams:{'active':'queJdInfoList','XNXQ':curSelXn,'ZYMC':curSelZy,'STUCODE':encodeURI($('#ic_xh').textbox('getValue')),
						'STUNAME':encodeURI($('#ic_xm').textbox('getValue')),'BJMC':encodeURI($('#ic_bjmc').textbox('getValue'))},
			title:'绩点统计信息列表',
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
			//a.学号,a.姓名,d.行政班名称,c.课程名称,a.总评 as 成绩
				//field为读取数据的数据名，title为显示的数据名，width宽度设置，align数字在表格中显示的位置
				{field:'学号',title:'学号',width:fillsize(0.13),align:'center'},
				{field:'姓名',title:'姓名',width:fillsize(0.13),align:'center'},
				{field:'平均绩点',title:'平均绩点',width:fillsize(0.13),align:'center'},
				{field:'不及格科目数',title:'不及格科目数',width:fillsize(0.15),align:'center'},
				{field:'体育',title:'体育',width:fillsize(0.13),align:'center'},
				{field:'体育新标准',title:'体育新标准',width:fillsize(0.13),align:'center'},
				{field:'行政班简称',title:'行政班名称',width:fillsize(0.2),align:'center'}
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
		//查询专业列表
		if(id == 'queJdMajorList'){
			loadJdMajorGrid();
		}
		
		//查询绩点统计列表
		if(id == 'queJdInfoList'){
			loadJdInfoGrid();
		}
		
		//导出
		if(id == 'export'){
			var xn = $('#ic_xn').combobox('getValue');
				
			window.parent.$('#maskFont').html('文件生成中，请稍后...');
			window.parent.$('#divPageMask').show();
			
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_Cjcx',
				data : 'active=jdtjExport&XNXQ=' + xn,
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