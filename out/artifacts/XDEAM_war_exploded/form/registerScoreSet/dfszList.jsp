<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="java.util.Vector"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.src.develop.store.user.*"%>
<%
	/**
		创建人：yeq
		Create date: 2016.03.1
		功能说明：用于设置登分开放时间及教师特殊开放信息
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
	<title>学期登分信息</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/icon.css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/pubTimetable.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/publicScript.js"></script>
	<style>
		.maskStyle{
			width:100%;
			height:100%;
			overflow:hidden;
			display:none;
			background-color:#D2E0F2;
			filter:alpha(opacity=90);
			position:absolute;
			top:0px;
			left:0px;
			z-index:100;
		}
		.maskFont{
			 font-size:12px;
			 color:#2B2B2B;
			 width:150px;
			 height:100%;
			 position:absolute;
			 top:50%;
			 left:50%;
			 margin-top:-10px;
			 margin-left:-35px;
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
	<div id="north" region="north" title="学期登分信息" style="height:91px;">
		<table>
			<tr>
				<td><a href="#" id="setTime" class="easyui-linkbutton" plain="true" iconcls="icon-edit" onClick="doToolbar(this.id);">登分时间</a></td>
				<td><a href="#" id="setBkTime" class="easyui-linkbutton" plain="true" iconcls="icon-edit" onClick="doToolbar(this.id);">补考登分时间</a></td>
				<td><a href="#" id="setDbkTime" class="easyui-linkbutton" plain="true" iconcls="icon-edit" onClick="doToolbar(this.id);">大补考登分时间</a></td>
				<td><a href="#" id="setTea" class="easyui-linkbutton" plain="true" iconcls="icon-edit" onClick="doToolbar(this.id);">教师权限</a></td>
				<td><a href="#" id="resetTea" class="easyui-linkbutton" plain="true" iconcls="icon-edit" onClick="doToolbar(this.id);">教师账号重置</a></td>
			</tr>
		</table>
		<table id="ee" singleselect="true" width="100%" class="tablestyle">
			<tr>
				<td style="width:150px;" class="titlestyle">学年学期名称</td>
				<td>
					<select name="XNXQMC_CX" id="XNXQMC_CX" class="easyui-combobox" style="width:150px;">
					</select>
				</td>
				<td style="width:150px;" class="titlestyle">教学性质</td><!-- 根据当前登录人权限 -->
				<td>
					<select name="JXXZ_CX" id="JXXZ_CX" class="easyui-combobox" style="width:150px;">
					</select>
				</td>
				<td style="width:150px;" align="center">
					<a href="#" onclick="doToolbar(this.id)" id="queList" class="easyui-linkbutton" plain="true" iconcls="icon-search">查询</a>
				</td>				
			</tr>
	    </table>
	</div>
	<div region="center">
		<table id="semesterList" style="width:100%;"></table>
		<form id="form1" method='post'>
			<input type="hidden" id="active" name="active"/>
			<input type="hidden" id="CD_XNXQBM" name="CD_XNXQBM"/>
			<input type="hidden" id="CD_KSSJ" name="CD_KSSJ"/>
			<input type="hidden" id="CD_JSSJ" name="CD_JSSJ"/>
			<input type="hidden" id="CJ_JSBH" name="CJ_JSBH"/>
			<input type="hidden" id="CD_DBKXNFW" name="CD_DBKXNFW"/>
		</form>
	</div>
	
	<!-- 登分时间设置页面 -->
	<div id="setTimeDialog" style="overflow:hidden;">
		<table style="width:100%;" class="tablestyle">
			<tr>
				<td class="titlestyle">学年学期名称</td>
				<td class="titlestyle" id="xnxqmc">
				</td>
			</tr>
			<tr>
				<td class="titlestyle">登分开始日期</td>
				<td>
					<input style="width:150px;" class="easyui-datebox" id="ic_kssj" name="ic_kssj" editable="false" required="true"/>
				</td>
			</tr>
			<tr>
				<td class="titlestyle">登分结束日期</td>
				<td>
					<input style="width:150px;" class="easyui-datebox" id="ic_jssj" name="ic_jssj" editable="false" required="true"/>
				</td>
			</tr>
		</table>
	</div>
	
	<!-- 补考登分时间设置页面 -->
	<div id="setBkTimeDialog" style="overflow:hidden;">
		<table style="width:100%;" class="tablestyle">
			<tr>
				<td class="titlestyle">学年学期名称</td>
				<td class="titlestyle" id="xnxqmc_bk">
				</td>
			</tr>
			<tr>
				<td class="titlestyle">补考登分开始日期</td>
				<td>
					<input style="width:150px;" class="easyui-datebox" id="ic_kssj_bk" name="ic_kssj_bk" editable="false" required="true"/>
				</td>
			</tr>
			<tr>
				<td class="titlestyle">补考登分结束日期</td>
				<td>
					<input style="width:150px;" class="easyui-datebox" id="ic_jssj_bk" name="ic_jssj_bk" editable="false" required="true"/>
				</td>
			</tr>
		</table>
	</div>
	
	<!-- 大补考登分时间设置页面 -->
	<div id="setDbkTimeDialog" style="overflow:hidden;">
		<table style="width:100%;" class="tablestyle">
			<tr>
				<td class="titlestyle">学年学期名称</td>
				<td class="titlestyle" id="xnxqmc_dbk">
				</td>
			</tr>
			<tr>
				<td class="titlestyle">大补考登分开始日期</td>
				<td>
					<input style="width:150px;" class="easyui-datebox" id="ic_kssj_dbk" name="ic_kssj_dbk" editable="false" required="true"/>
				</td>
			</tr>
			<tr>
				<td class="titlestyle">大补考登分结束日期</td>
				<td>
					<input style="width:150px;" class="easyui-datebox" id="ic_jssj_dbk" name="ic_jssj_dbk" editable="false" required="true"/>
				</td>
			</tr>
			<tr>
				<td class="titlestyle">大补考登分学年范围</td>
				<td>
					<select name="ic_yearRange" id="ic_yearRange" class="easyui-combobox" style="width:150px;" panelHeight="auto" editable="false">
						<option value="0">全部学年</option>
						<option value="1" selected="selected">不包含当前学年</option>
						<option value="2">仅当前学年</option>
					</select>
				</td>
			</tr>
		</table>
	</div>
	
	<!-- 设置教师登分权限列表 -->
	<div id="setTeaDialog">
		<div class="easyui-layout" style="width:100%; height:100%;">
			<div region="north" style="height:32px;">
				<table id="ee" width="100%" class="tablestyle">
					<tr>
						<td width="80px" class="titlestyle">教师工号</td>
						<td width="135px">
							<input style='width:100%;' class="easyui-textbox" id='ic_teaCode' name='ic_teaCode'/>
						</td>
						<td width="80px" class="titlestyle">教师姓名</td>
						<td  width="135px">
							<input style='width:100%;' class="easyui-textbox" id='ic_teaName' name='ic_teaName'/>
						</td>	
						<td style="width:80px;" align="center">
							<a href="#" onclick="doToolbar(this.id)" id="queTeaList" class="easyui-linkbutton" plain="true" iconcls="icon-search">查询</a>
						</td>				
					</tr>
			    </table>
			</div>
			<div region="center">
				<table id="teaList" style="width:100%;"></table>
			</div>
		</div>
	</div>
	
	<!-- 教师账号重置页面 -->
	<div id="resetTeaDialog" style="overflow:hidden;">
		<%-- 遮罩层 --%>
    	<div id="mask" class="maskStyle">
    		<div class="maskFont">教师账号替换中...</div>
    	</div>
    	<div class="easyui-layout" style="width:100%; height:100%;">
			<div region="north" style="height:34px;">
				<table>
					<tr>
						<td><a href="#" id="saveResetTea" class="easyui-linkbutton" plain="true" iconcls="icon-ok" onClick="doToolbar(this.id);">确定</a></td>
					</tr>
				</table>
			</div>
			<div id="center" region="center">
				<table style="width:100%;" class="tablestyle">
					<tr>
						<td class="titlestyle" style="width:80px;">原教师账号</td>
						<td>
							<select name="beforeTea" id="beforeTea" class="easyui-combobox">
							</select>
						</td>
					</tr>
					<tr>
						<td class="titlestyle">现教师账号</td>
						<td>
							<select name="afterTea" id="afterTea" class="easyui-combobox" style="width:300px;">
							</select>
						</td>
					</tr>
				</table>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
	var sAuth = '<%=sAuth%>';
	var iKeyCode = ''; //定义主键
	var tempIndex = '';
	var xnxqmc = '';
	var pageNum = 1;   //datagrid初始当前页数
	var pageSize = 20; //datagrid初始页内行数
	var XNXQMC_CX = '';//查询条件
	var JXXZ_CX = '';
	var isLoad = true;//判断datagrid是否处于加载状态
	var curSelCode = new Array();
	
	$(document).ready(function(){
		initDialog();//初始化对话框
		initData();//页面初始化加载数据
	});
	
	/**页面初始化加载数据**/
	function initData(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Dfsz',
			data : 'active=initData&page=' + pageNum + '&rows=' + pageSize + '&sAuth=' + sAuth,
			dataType:"json",
			success : function(data) {
				loadSemesterGrid(data[0].listData);
				initCombobox(data[0].xnxqData, data[0].jxxzData);
			}
		});
	}
	
	/**加载 dialog控件**/
	function initDialog(){
		$('#setTimeDialog').dialog({
			title:'登分时间设置',
			width:300,
			height:142,
			modal:true,
			closed: true,   
			cache: false, 
			draggable:false,//是否可移动dialog框设置
			toolbar:[{
				//保存编辑
				text:'保存',
				iconCls:'icon-save',
				handler:function(){
					//传入save值进入doToolbar方法，用于保存
					doToolbar('saveTime');
				}
			}],
			//关闭事件
			onClose:function(data){
				$('#xnxqmc').html('');
				$("#ic_kssj").datebox('setValue', '');
				$("#ic_jssj").datebox('setValue', '');
			}
		});
		
		$('#setBkTimeDialog').dialog({
			title:'补考登分时间设置',
			width:300,
			height:142,
			modal:true,
			closed: true,   
			cache: false, 
			draggable:false,//是否可移动dialog框设置
			toolbar:[{
				//保存编辑
				text:'保存',
				iconCls:'icon-save',
				handler:function(){
					//传入save值进入doToolbar方法，用于保存
					doToolbar('saveBkTime');
				}
			}],
			//关闭事件
			onClose:function(data){
				$('#xnxqmc_bk').html('');
				$("#ic_kssj_bk").datebox('setValue', '');
				$("#ic_jssj_bk").datebox('setValue', '');
			}
		});
		
		$('#setDbkTimeDialog').dialog({
			title:'大补考登分时间设置',
			width:300,
			height:167,
			modal:true,
			closed: true,   
			cache: false, 
			draggable:false,//是否可移动dialog框设置
			toolbar:[{
				//保存编辑
				text:'保存',
				iconCls:'icon-save',
				handler:function(){
					//传入save值进入doToolbar方法，用于保存
					doToolbar('saveDbkTime');
				}
			}],
			//关闭事件
			onClose:function(data){
				$('#xnxqmc_dbk').html('');
				$("#ic_kssj_dbk").datebox('setValue', '');
				$("#ic_jssj_dbk").datebox('setValue', '');
				$('#ic_yearRange').combobox('setValue', '1');
			}
		});
		
		$('#setTeaDialog').dialog({   
			title: '教师登分权限列表',   
			width: 700,//宽度设置   
			height: 400,//高度设置 
			modal:true,
			closed: true,   
			cache: false, 
			draggable:true,//是否可移动dialog框设置
			toolbar:[{
				//保存编辑
				text:'保存',
				iconCls:'icon-save',
				handler:function(){
					//传入save值进入doToolbar方法，用于保存
					doToolbar('saveTea');
				}
			}],
			//读取事件
			onLoad:function(data){
			},
			//关闭事件
			onClose:function(data){
				$('#ic_teaCode').textbox('setValue', '');
				$('#ic_teaName').textbox('setValue', '');
				$('#teaList').datagrid('loadData',{total:0,rows:[]});
			}
		});
		
		$('#resetTeaDialog').dialog({
			title:'教师账号重置',
			width:400,
			height:122,
			modal:true,
			closed: true,   
			cache: false, 
			draggable:false,//是否可移动dialog框设置
			//关闭事件
			onClose:function(data){
				$('#beforeTea').combobox('setValue', '');
				$('#afterTea').combobox('disable');
				$('#afterTea').combobox('setText', '请先选择原教师账号');
			}
		});
	}
	
	/**加载combobox控件
		@xnxqData 学年学期下拉框数据
		@jxxzData 教学性质下拉框数据
		@levelData 层级下拉框数据
	**/
	function initCombobox(xnxqData, jxxzData){
		$('#XNXQMC_CX').combobox({
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
	
		$('#JXXZ_CX').combobox({
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
	}
	
	/**读取学年学期课程表datagrid数据**/
	function loadSemesterData(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Dfsz',
			data : 'active=queSemList&page=' + pageNum + '&rows=' + pageSize + '&sAuth=' + sAuth + 
				'&XNXQMC_CX=' + XNXQMC_CX + 
				'&JXXZ_CX=' + JXXZ_CX,
			dataType:"json",
			success : function(data) {
				loadSemesterGrid(data[0].listData);
			}
		});
	}
	
	/**加载学年学期课程表datagrid控件，读取页面信息
		@listData 列表数据
	**/
	function loadSemesterGrid(listData){
		$('#semesterList').datagrid({
			//url:'semesterList.json',
			data:listData,
			title:'学年学期列表',
			width:'100%',
			nowrap: false,
			fit:true, //自适应父节点宽度和高度
			showFooter:true,
			striped:true,
			pagination:true,
			pageSize:pageSize,
			pageNumber:pageNum,
			singleSelect:true,
			rownumbers:true,
			fitColumns: true,
			columns:[[
				//field为读取数据的数据名，title为显示的数据名，width宽度设置，align数字在表格中显示的位置
				{field:'CD_XNXQBM',title:'学年学期编码',width:fillsize(0.08),align:'center'},
				{field:'CD_XNXQMC',title:'学年学期名称',width:fillsize(0.12),align:'center'},
				{field:'jxxz',title:'教学性质',width:fillsize(0.05),align:'center'},
				{field:'CD_KSSJ',title:'登分开始时间',width:fillsize(0.11),align:'center'},
				{field:'CD_JSSJ',title:'登分结束时间',width:fillsize(0.11),align:'center'},
				{field:'CD_BKKSSJ',title:'补考登分开始时间',width:fillsize(0.11),align:'center'},
				{field:'CD_BKJSSJ',title:'补考登分结束时间',width:fillsize(0.1),align:'center'},
				{field:'CD_DBKKSSJ',title:'大补考登分开始时间',width:fillsize(0.1),align:'center'},
				{field:'CD_DBKJSSJ',title:'大补考登分结束时间',width:fillsize(0.1),align:'center'},
				{field:'CD_DBKXNFW',title:'大补考登分学年范围',width:fillsize(0.1),align:'center'},
				{field:'CJ_JSBH',hidden:true}
			]],
			//双击某行时触发
			onDblClickRow:function(rowIndex,rowData){},
			//读取datagrid之前加载
			onBeforeLoad:function(){},
			//单击某行时触发
			onClickRow:function(rowIndex,rowData){
				//主键赋值
				iKeyCode = rowData.CD_XNXQBM;
				tempIndex = rowIndex;
				xnxqmc = rowData.CD_XNXQMC;
				
				if(rowData.CJ_JSBH.length > 0){
					curSelCode = rowData.CJ_JSBH.split(',');
				}else{
					curSelCode.length = 0;
				}
			},
			//加载成功后触发
			onLoadSuccess: function(data){
				iKeyCode = '';
				xnxqmc = '';
				curSelCode.length = 0;
				tempIndex = '';
			}
		});
		
		$("#semesterList").datagrid("getPager").pagination({ 
			total:listData.total,
			afterPageText: '页&nbsp;<a href="#" onclick="goEnterPage();">Go</a>&nbsp;&nbsp;&nbsp;共 {pages} 页',
			onSelectPage:function (pageNo, pageSize_1) {
				pageNum = pageNo;
				pageSize = pageSize_1;
				loadSemesterData();
			} 
		});
	};
	
	function goEnterPage(){
		var e = jQuery.Event("keydown");//模拟一个键盘事件 
		e.keyCode = 13;//keyCode=13是回车 
		$("input.pagination-num").trigger(e);//模拟页码框按下回车 
	}
	
	/**工具栏按钮调用方法，传入按钮的id
		@id 当前按钮点击事件
	**/
	function doToolbar(id){
		//查询学年学期列表
		if(id == 'queList'){
			pageNum = 1;
			XNXQMC_CX = $('#XNXQMC_CX').combobox('getValue'); 
			JXXZ_CX = $('#JXXZ_CX').combobox('getValue');
			loadSemesterData();
		}
		
		if(id == 'setTime'){
			if(iKeyCode == ''){
				alertMsg('请选择一个学年学期');
				return;
			}
			
			$('#xnxqmc').html(xnxqmc);
			$('#setTimeDialog').dialog('open');
		}
		
		if(id == 'setBkTime'){
			if(iKeyCode == ''){
				alertMsg('请选择一个学年学期');
				return;
			}
			
			$('#xnxqmc_bk').html(xnxqmc);
			$('#setBkTimeDialog').dialog('open');
		}
		
		if(id == 'setDbkTime'){
			if(iKeyCode == ''){
				alertMsg('请选择一个学年学期');
				return;
			}
			
			$('#xnxqmc_dbk').html(xnxqmc);
			$('#setDbkTimeDialog').dialog('open');
		}
		
		if(id == 'saveTime'){
			var beginDate = $("#ic_kssj").datebox('getValue');
			var endDate = $("#ic_jssj").datebox('getValue');
			
			if(beginDate == ''){
				alertMsg("请选择登分开始时间");
				return;
			}
			
			if(endDate == ''){
				alertMsg("请选择登分结束时间");
				return;
			}
			
			//判断开始时间是否大于结束时间
			if(beginDate > endDate){
				alertMsg("开始时间必须在结束时间之前");
				return;
			}
			
			$('#active').val(id);
			$('#CD_XNXQBM').val(iKeyCode);
			$('#CD_KSSJ').val(beginDate);
			$('#CD_JSSJ').val(endDate);
			$("#form1").submit();
		}
		
		if(id == 'saveBkTime'){
			var beginDate = $("#ic_kssj_bk").datebox('getValue');
			var endDate = $("#ic_jssj_bk").datebox('getValue');
			
			if(beginDate == ''){
				alertMsg("请选择登分开始时间");
				return;
			}
			
			if(endDate == ''){
				alertMsg("请选择登分结束时间");
				return;
			}
			
			//判断开始时间是否大于结束时间
			if(beginDate > endDate){
				alertMsg("开始时间必须在结束时间之前");
				return;
			}
			
			$('#active').val(id);
			$('#CD_XNXQBM').val(iKeyCode);
			$('#CD_KSSJ').val(beginDate);
			$('#CD_JSSJ').val(endDate);
			$("#form1").submit();
		}
		
		if(id == 'saveDbkTime'){
			var beginDate = $("#ic_kssj_dbk").datebox('getValue');
			var endDate = $("#ic_jssj_dbk").datebox('getValue');
			
			if(beginDate == ''){
				alertMsg("请选择登分开始时间");
				return;
			}
			
			if(endDate == ''){
				alertMsg("请选择登分结束时间");
				return;
			}
			
			//判断开始时间是否大于结束时间
			if(beginDate > endDate){
				alertMsg("开始时间必须在结束时间之前");
				return;
			}
			
			$('#active').val(id);
			$('#CD_XNXQBM').val(iKeyCode);
			$('#CD_KSSJ').val(beginDate);
			$('#CD_JSSJ').val(endDate);
			$('#CD_DBKXNFW').val($('#ic_yearRange').combobox('getValue'));
			$("#form1").submit();
		}
		
		if(id == 'setTea'){
			if(iKeyCode == ''){
				alertMsg('请选择一个学年学期');
				return;
			}
			
			$('#saveTea').linkbutton('disable');
			$('#queTeaList').linkbutton('disable');
			loadTeaListData();
			$('#setTeaDialog').dialog('open');
		}
		
		if(id == 'queTeaList'){
			loadTeaListData();
		}
		
		if(id == 'saveTea'){
			$('#active').val(id);
			$('#CD_XNXQBM').val(iKeyCode);
			$('#CJ_JSBH').val(curSelCode.toString());
			$('#form1').submit();
		}
		
		if(id == 'resetTea'){
			loadBeforeTeaCombo();
			$('#resetTeaDialog').dialog('open');
		}
		
		if(id == 'saveResetTea'){
			if($('#beforeTea').combobox('getValue') == ''){
				alertMsg('请选择原教师账号');
				return;
			}
			if($('#afterTea').combobox('getValue') == ''){
				alertMsg('请选择现教师账号');
				return;
			}
			
			ConfirmMsg('是否确定要将&nbsp;<font color="red">'+$('#beforeTea').combobox('getText')+'</font>替换为&nbsp;<font color="red">'+$('#afterTea').combobox('getText')+'</font>(包括授课、登分、监考等所有相关信息)？', 'resetTea();', '');
		}
	}
	
	/**表单提交**/
	$('#form1').form({
		//定位到servlet位置的url
		url:'<%=request.getContextPath()%>/Svl_Dfsz',
		//当点击事件并成功提交后触发的事件
		success:function(data){
			var json  =  eval("("+data+")");
			
			if(json[0].MSG == '保存成功'){
				var type = $('#active').val();
				
				if(type == 'saveTime'){
					loadSemesterData();
					$('#setTimeDialog').dialog('close');
				}
				
				if(type == 'saveBkTime'){
					loadSemesterData();
					$('#setBkTimeDialog').dialog('close');
				}
				
				if(type == 'saveDbkTime'){
					loadSemesterData();
					$('#setDbkTimeDialog').dialog('close');
				}
				
				if(type == 'saveTea'){
					$('#semesterList').datagrid('updateRow', {
						index:tempIndex,
						row: {
							CJ_JSBH:$('#CJ_JSBH').val()
						}
					});
					$('#setTeaDialog').dialog('close');
				}
				
				showMsg(json[0].MSG);
			}else{
				alertMsg(json[0].MSG);
			}
		}
	});
	
	/**读取教师datagrid数据**/
	function loadTeaListData(){
		isLoad = true;
		
		$('#teaList').datagrid({
			url :'<%=request.getContextPath()%>/Svl_Dfsz',
			queryParams: {'active':'queTeaList','DFJSBH_CX':encodeURI($('#ic_teaCode').textbox('getValue')),
				'DFJSMC_CX':encodeURI($('#ic_teaName').textbox('getValue')),'CJ_JSBH':curSelCode.toString()},
			title:'',
			width:'100%',
			nowrap: false,
			fit:true, //自适应父节点宽度和高度
			showFooter:true,
			striped:true,
			pagination:true,
			pageNumber:1,
			pageSize:20,
			singleSelect:false,
			rownumbers:true,
			fitColumns: true,
			columns:[[
				//field为读取数据的数据名，title为显示的数据名，width宽度设置，align数字在表格中显示的位置
				{field:'ck',checkbox:true},
				{field:'工号',title:'工号',width:fillsize(0.45),align:'center'},
				{field:'姓名',title:'姓名',width:fillsize(0.45),align:'center'}
			]],
			onSelect:function(rowIndex,rowData){
				if(isLoad == false){
					curSelCode.push(rowData.工号);
				}
			},
			onUnselect:function(rowIndex,rowData){
				$.each(curSelCode, function(key,value){
					if(value == rowData.工号){
						curSelCode.splice(key, 1);
					}
				});
			},
			//加载成功后触发
			onLoadSuccess: function(data){
				$(".datagrid-header-check").html('&nbsp;');//隐藏全选
				//勾选已选授课教师
				if(data){
					$.each(data.rows, function(rowIndex, rowData){
						for(var i=0; i<curSelCode.length; i++){
							if(curSelCode[i] == rowData.工号){
								$('#teaList').datagrid('selectRow', rowIndex);
								break;
							}
						}
					});
				}
				
				isLoad = false;
				$('#saveTea').linkbutton('enable');
				$('#queTeaList').linkbutton('enable');
			}
		});
	}
	
	/**读取重名教师信息下拉框*/
	function loadBeforeTeaCombo(){
		$('#beforeTea').combobox({
			url:'<%=request.getContextPath()%>/Svl_Dfsz?active=loadBeforeTeaCombo',
			valueField:'comboValue',
			textField:'comboName',
			width:'300',
			panelHeight:'140', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data != ''){
					//初始化combobox时赋值
					$(this).combobox('setValue', '');
					
					$('#afterTea').combobox('disable');
					$('#afterTea').combobox('setText', '请先选择原教师账号');
				}
			},
			//下拉列表值改变事件
			onChange:function(data){
				if(data == ''){
					$('#afterTea').combobox('disable');
					$('#afterTea').combobox('setText', '请先选择原教师账号');
				}else{
					$('#afterTea').combobox({
						url:'<%=request.getContextPath()%>/Svl_Dfsz?active=loadSameNameTeaCombo&CJ_JSBH=' + data,
						valueField:'comboValue',
						textField:'comboName',
						width:'300',
						panelHeight:'140', //combobox高度
						editable:false,
						onLoadSuccess:function(data){
							if(data.length == 1){
								$('#afterTea').combobox('disable');
								$('#afterTea').combobox('setText', '没有同名教师');
							}else{
								$('#afterTea').combobox('enable');
								$(this).combobox('setValue', '');
							}
						},
						//下拉列表值改变事件
						onChange:function(data){}
					});
				}
			}
		});
	}
	
	/**重置教师账号*/
	function resetTea(){
		$('#mask').show();
	
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Dfsz',
			data : 'active=resetTea&beforeTea=' + $('#beforeTea').combobox('getValue') + '&afterTea=' + $('#afterTea').combobox('getValue'),
			dataType:"json",
			success : function(data) {
				$('#mask').hide();
				if(data[0].MSG == '替换成功'){
					$('#resetTeaDialog').dialog('close');
					showMsg(data[0].MSG);
				}else{
					alertMsg(data[0].MSG);
				}
			}
		});
	}
</script>
</html>