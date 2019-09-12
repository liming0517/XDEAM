<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="java.util.Vector"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.src.develop.store.user.*"%>
<%
	/**
		创建人：yeq
		Create date: 2016.01.08
		功能说明：用于设置班级信息
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
	<title>班级信息列表</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/icon.css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
 	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/publicScript.js"></script>
  </head>
  <style>
  </style>
 <%
	/*
		获得角色信息
	 */
	UserProfile up = new UserProfile(request,
			MyTools.getSessionUserCode(request));
	String userName = up.getUserName();
	if (userName == null) {
		userName = "";
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
<body  class="easyui-layout">
	<div id="north" region="north" title="班级信息" style="height:116px; overflow:hidden;" >
		<table>
			<tr>
				<td><a href="#" id="new" class="easyui-linkbutton" plain="true" iconcls="icon-new" onClick="doToolbar(this.id);" title="">新建</a></td>
				<td><a href="#" id="edit" class="easyui-linkbutton" plain="true" iconcls="icon-edit" onClick="doToolbar(this.id);" title="">编辑</a></td>
				<td><a href="#" id="del" class="easyui-linkbutton" plain="true" iconcls="icon-cancel" onClick="doToolbar(this.id);" title="">删除</a></td>
			</tr>
		</table>
		<table id="ee" singleselect="true" width="100%" class="tablestyle">
			<tr>
				<td style="width:150px;" class="titlestyle">年级名称</td>
				<td>
					<select name="NJDM_CX" id="NJDM_CX" class="easyui-combobox" style="width:180px;">
					</select>
				</td>
				<td style="width:150px;" class="titlestyle">所属系部</td>
				<td>
					<select name="XBDM_CX" id="XBDM_CX" class="easyui-combobox" style="width:180px;">
					</select>
				</td>
				<td style="width:150px;" class="titlestyle">所属专业</td>
				<td>
					<select name="SSZY_CX" id="SSZY_CX" class="easyui-combobox" style="width:180px;">
					</select>
				</td>
			</tr>
			<tr>
				<td style="width:150px;" class="titlestyle">班级编号</td>
				<td>
					<input name="BJBH_CX" id="BJBH_CX" class="easyui-textbox" style="width:180px;"/>
				</td>
				<td style="width:150px;" class="titlestyle">班级名称</td>
				<td>
					<input name="BJMC_CX" id="BJMC_CX" class="easyui-textbox" style="width:180px;"/>
				</td>
				<td colspan="2">
					<a href="#" onclick="doToolbar(this.id)" id="query" class="easyui-linkbutton" plain="true" iconcls="icon-search">查询</a>
				</td>				
			</tr>
	    </table>
	</div>
	<div region="center">
		<table id="classList" style="width:100%;"></table>
	</div>
	
	<!-- 班级信息新建编辑 -->
	<div id="classInfo" style="overflow:hidden;">
		<form id="form1" method='post'>
			<table style="width:100%;" class="tablestyle">
				<!-- 
				<tr>
					<td class="titlestyle">班级编号</td>
					<td>
						<input name="BJBH" id="BJBH" class="easyui-textbox" style="width:200px;" required="true">
					</td>
				</tr>
				 -->
				<tr>
					<td class="titlestyle">行政班名称</td>
					<td class="titlestyle" id="BJMC">
					</td>
				</tr>
				<tr>
					<td class="titlestyle">年级名称</td>
					<td>
						<select name="NJDM" id="NJDM" class="easyui-combobox" style="width:200px;">
						</select>
					</td>
				</tr>
				<tr>
					<td class="titlestyle">所属系部</td>
					<td>
						<select name="XBDM" id="XBDM" class="easyui-combobox" style="width:200px;">
						</select>
					</td>
				</tr>
				<tr>
					<td class="titlestyle">所属专业</td>
					<td>
						<select name="SSZY" id="SSZY" class="easyui-combobox" style="width:200px;">
						</select>
					</td>
				</tr>
				<!-- 
				<tr>
					<td class="titlestyle">总人数</td>
					<td>
						<input name="ZRS" id="ZRS" class="easyui-numberbox" style="width:200px;" min="0" max="1000" precision="0">
					</td>
				</tr>
				 -->
				<tr>
					<td class="titlestyle">班主任</td>
					<td onClick="opentea();">
						<input name="BZR" id="BZR" class="easyui-textbox" style="width:200px;" min="0" max="1000" precision="0" >
					</td>
				</tr>
				<tr>
					<td class="titlestyle">班级简称</td>
					<td>
						<input name="BJJC" id="BJJC" class="easyui-textbox" style="width:200px;" min="0" max="1000" precision="0">
					</td>
				</tr>
				<tr>
					<td class="titlestyle">班级教室</td>
					<td>
						<select name="BJJS" id="BJJS" class="easyui-combobox" style="width:200px;">
						</select>
					</td>
				</tr>
			</table>
			
			<input type="hidden" id="active" name="active"/>
			<input type="hidden" id="BJBH" name="BJBH"/>
			<input type="hidden" id="BZRGH" name="BZRGH"/>
			<input type="hidden" id="XBDM1" name="XBDM1"/>
			
			<!-- 2017/12/11翟旭超加 -->
			<input type="hidden" id="addName" name="addName"/>
		</form>
	</div>
	
	<div id="classTeaSetDialog" style="overflow:hidden;">
		<div style="width:100%; height:100%;" class="easyui-layout">
			<div region="north" style="height:32px; overflow:hidden;">
				<table width="100%" class="tablestyle">
					<tr>
						<td style="width:80px;" class="titlestyle">教师工号</td>
						<td style="width:150px;">
							<input style='width:100%' class='easyui-validate' id='ic_teaId' name='ic_teaId'/>
						</td>
						<td style="width:80px;" class="titlestyle">教师姓名</td>
						<td>
							<input style='width:100%' class='easyui-validate' id='ic_teaName' name='ic_teaName'/>
						</td>	
						<td style="width:80px; text-align:center;">
							<a href="#" class="easyui-linkbutton" id="search" name="search" iconCls="icon-search" plain="true" onClick="doToolbar(this.id)">查询</a>
						</td>					
					</tr>
				</table>
			</div>
			<div region="center">
				<table id="teaList" style="width:100%;">
				</table>
			</div>
		</div>
	</div>
	
	<!-- 学生名单 -->
	<div id="xsmdDialog">
		<table id="xsmdList" style="width:100%;"></table>
 	</div> 
 	
 	<!-- 2017/12/11翟旭超加 -->
	<div id="test"  class="easyui-window" style="width:695px;height:350px;">
		<iframe id="xiafaxiada" name="xiafaxiada" src='http://180.166.123.122:8087/HRM/form/param/TeaTree1.jsp?u=1' style="width:680px;height:310px;"  frameborder="0"></iframe>
	</div>
</body>
<script type="text/javascript">
	var sAuth = '<%=sAuth%>';
	var row = '';      //行数据
	var iKeyCode = ''; //定义主键
	var pageNum = 1;   //datagrid初始当前页数
	var pageSize = 20; //datagrid初始页内行数
	var NJDM_CX = '';//查询条件
	var XBDM_CX = '';
	var SSZY_CX = '';
	var BJBH_CX = '';
	var BJMC_CX = '';
	var saveType = '';
	var teaidarray =new Array();//存放教师编号
	var teaarray =new Array();//存放教师
	var teainfoidarray =new Array();//存放选择的任课教师编号
	var teainfoarray =new Array();//存放选择的任课教师姓名
	var queryAuth = '<%=MyTools.getProp(request, "Base.majorClassQueAuth")%>';
	var isLoad="";
	var teabh="";//传入查询的教师编号
	var bzrgh="";//班主任工号
	var bzrxm="";//班主任姓名
	var tempNjdm = '';
	var tempXbdm = '';
	var tempJsbh = '';
	
	$(document).ready(function(){
		if(sAuth.indexOf(queryAuth) > -1){
			$('#new').linkbutton('disable');
			$('#edit').linkbutton('disable');
			$('#del').linkbutton('disable');
		}
		
		initDialog();//初始化对话框
		initData();//页面初始化加载数据
		
		//2017/12/11翟旭超加
		$('#test').window('close');//初始化关闭人员窗口
	});
	
	/**页面初始化加载数据**/
	function initData(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_ClassSet',
			data : 'active=initData&page=' + pageNum + '&rows=' + pageSize,
			dataType:"json",
			success : function(data) {
				loadGrid(data[0].listData);
				initCombobox(data[0].njdmData, data[0].xbdmData, data[0].sszyData);
			}
		});
	}
	
	/**加载combobox控件
		@jxxzData 下拉框数据
	**/
	function initCombobox(njdmData, xbdmData, sszyData){
		$('#NJDM_CX').combobox({
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

		$('#NJDM').combobox({
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
		
		$('#XBDM_CX').combobox({
			data:xbdmData,
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
		
		$('#XBDM').combobox({
			data:xbdmData,
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
				$('#BJJS').combobox('setText', '请先选择系部');
				$('#BJJS').combobox('disable');
			},
			onChange:function(record){
				if(record == ''){
					$('#BJJS').combobox('setText', '请先选择系部');
					$('#BJJS').combobox('disable');
				}else{
					$('#BJJS').combobox('enable');
					$('#BJJS').combobox({
						url:'<%=request.getContextPath()%>/Svl_ClassSet?active=loadClassroomCombo&XBDM=' + record + '&BJBH=' + $('#BJBH').val(),
						valueField:'comboValue',
						textField:'comboName',
						width:'200',
						panelHeight:'140', //combobox高度
						onLoadSuccess:function(data){
							//判断data参数是否为空
							if(data != ''){
								//初始化combobox时赋值
								if(tempJsbh == ''){
									$(this).combobox('setValue', '');
								}else{
									$(this).combobox('setValue', tempJsbh);
								}
							}
							//判断data参数是否为空
							if(data.length == 1){
								$(this).combobox('setText', '没有可选教室');
								$(this).combobox('disable');
							}else{
								$(this).combobox('enable');
							}
						}
					});
				}
			}
		});
		
		$('#SSZY_CX').combobox({
			data:sszyData,
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

		$('#SSZY').combobox({
			data:sszyData,
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
	
	/**加载 dialog控件**/
	function initDialog(){
		$('#classInfo').dialog({   
			width: 300,//宽度设置   
			height: 242,//高度设置 
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
					doToolbar('save');
				}
			}],
			onClose:function(data){
				emptyDialog();
			}
		});
		
		$('#classTeaSetDialog').dialog({   
			title: '班主任设置',   
			width: 580,//宽度设置   
			height: 400,//高度设置 
			modal:true,
			closed: true,   
			cache: false,
			toolbar:[{
				text:'确定',
				iconCls:'icon-submit',
				handler:function(){
					//传入save值进入doToolbar方法，用于保存
					doToolbar('saveClassTea');
				}
			}],
			draggable:true,//是否可移动dialog框设置
			//读取事件
			onLoad:function(data){
			},
			//关闭事件
			onClose:function(data){
			}
		});
		
		$('#xsmdDialog').dialog({
			title: '学生名单',
			width: 750,//宽度设置   
			height: 450,//高度设置 
			modal:true,
			closed: true,   
			cache: false, 
			draggable:true,//是否可移动dialog框设置
			//打开事件
			onOpen:function(data){
			},
			//读取事件
			onLoad:function(data){
			},
			//关闭事件
			onClose:function(data){
				$('#xsmdList').datagrid('loadData',{total:0,rows:[]});
			}
		});
	}

	/**加载 datagrid控件，读取页面信息
		@listData 列表数据
	**/
	function loadGrid(listData){
		$('#classList').datagrid({
			data:listData,
			title:'',
			width:'100%',
			nowrap: false,
			fit:true, //自适应父节点宽度和高度
			showFooter:true,
			striped:true,
			pagination:true,
			pageSize:pageSize,
			singleSelect:true,
			pageNumber:pageNum,
			rownumbers:true,
			fitColumns: true,
			columns:[[
				//field为读取数据的数据名，title为显示的数据名，width宽度设置，align数字在表格中显示的位置
				{field:'BJBH',title:'行政班编号',width:fillsize(0.1),align:'center'},
				{field:'BJMC',title:'行政班名称',width:fillsize(0.12),align:'center'},
				{field:'NJDM',title:'年级代码',hidden:true},
				{field:'NJMC',title:'年级名称',width:fillsize(0.08),align:'center'},
				{field:'XBDM',title:'系部代码',hidden:true},
				{field:'XBMC',title:'所属系部',width:fillsize(0.1),align:'center'},
				{field:'SSZY',title:'所属专业',hidden:true},
				{field:'ZYMC',title:'所属专业',width:fillsize(0.15),align:'center'},
				{field:'ZRS',title:'总人数',width:fillsize(0.05),align:'center'},
				{field:'BZR',title:'班主任',width:fillsize(0.08),align:'center'},
				{field:'BJJC',title:'班级简称',width:fillsize(0.08),align:'center'},
				{field:'JSBH',title:'教室编号',hidden:true},
				{field:'JSMC',title:'班级教室',width:fillsize(0.14),align:'center'},
				{field:'col',title:'操作',width:fillsize(0.1),align:'center',formatter:function(value,rec){
					return "<input type='button' value='[学生名单]' onclick='loadStuList(\"" + rec.BJBH +"\")' style='cursor:pointer;'>";
				}}
			]],
			onClickRow:function(rowIndex,rowData){
				//主键赋值
				iKeyCode = rowData.BJBH;
				row = rowData;
				$('#BJBH').val(rowData.BJBH);
				$('#BZRGH').val(rowData.BZRGH);
			},
			//加载成功后触发
			onLoadSuccess: function(data){
				iKeyCode = '';
			}
		});
		
		$("#classList").datagrid("getPager").pagination({
			total:listData.total,
			afterPageText: '页&nbsp;<a href="#" onclick="goEnterPage();">Go</a>&nbsp;&nbsp;&nbsp;共 {pages} 页',
			onSelectPage:function (pageNo, pageSize_1) { 
				pageNum = pageNo;
				pageSize = pageSize_1;
				loadData();
			} 
		});
	};
	
	function goEnterPage(){
		var e = jQuery.Event("keydown");//模拟一个键盘事件
		e.keyCode = 13;//keyCode=13是回车 
		$("input.pagination-num").trigger(e);//模拟页码框按下回车 
	}
	
	/**读取datagrid数据**/
	function loadData(){
		$.ajax({
			type:"POST",
			url:'<%=request.getContextPath()%>/Svl_ClassSet',
			data:'active=query&page=' + pageNum + '&rows=' + pageSize + '&BJBH_CX=' + encodeURI(BJBH_CX) + '&BJMC_CX=' + encodeURI(BJMC_CX) + 
				'&NJDM_CX=' + NJDM_CX + '&XBDM_CX=' + XBDM_CX + '&SSZY_CX=' + SSZY_CX,
			dataType:"json",
			success:function(data) {
				loadGrid(data[0].listData);
			}
		});
	}
	
	//查询学生名单
  	function loadStuList(classCode){
  		$('#xsmdDialog').dialog('open');
  	
  		$('#xsmdList').datagrid({
 			url:'<%=request.getContextPath()%>/Svl_ClassSet',
			queryParams:{"active":"loadStuList","BJBH":classCode},
			title:'',
			width:'100%',
			nowrap: false,
			fit:true, //自适应父节点宽度和高度
			showFooter:true,
			striped:true,
			//pagination:true,
			//pageSize:50,
			singleSelect:true,
			//pageNumber:1,
			rownumbers:true,
			fitColumns: true,
			columns:[[
				{field:'学籍号',title:'学籍号',width:fillsize(0.2),align:'center'},
				{field:'班内学号',title:'学号',width:fillsize(0.2),align:'center'},
				{field:'姓名',title:'姓名',width:fillsize(0.2),align:'center'},
				{field:'专业名称',title:'专业名称',width:fillsize(0.2),align:'center'},
				{field:'招生类别',title:'招生类别',width:fillsize(0.2),align:'center'}
			]]
  		});
  	}
	
	/**工具栏按钮调用方法，传入按钮的id
		@id 当前按钮点击事件
	**/
	function doToolbar(id){
		//查询
		if(id == 'query'){
			NJDM_CX = $('#NJDM_CX').combobox('getValue');
			XBDM_CX = $('#XBDM_CX').combobox('getValue');
			SSZY_CX = $('#SSZY_CX').combobox('getValue');
			BJBH_CX = $('#BJBH_CX').textbox('getValue'); 
			BJMC_CX = $('#BJMC_CX').textbox('getValue');
			loadData();
		}
		
		if(id == 'search'){
			var teaid=teainfoidarray[0];
			loadGridTea(teaid);
		}
		
		//判断获取参数为new，执行新建操作
		if(id == 'new'){
			//打开dialog
			$('#classInfo').dialog({   
				title: '新建班级信息'
			});
			saveType = 'new';
			$('#classInfo').dialog('open');
			openWindow("");
		}

		//判断获取参数为edit，执行编辑操作
		if(id == 'edit'){
			if(iKeyCode == ''){
				alertMsg('请选择一行数据');
				return;
			}
			tempNjdm = row.NJDM;
			tempXbdm = row.XBDM;
			teabh = row.BZRGH;
			tempJsbh = row.JSBH;
			teainfoidarray.push(row.BZRGH);
			teainfoarray.push(row.BZR);
			
			//$('#BJBH').textbox('readonly', true);
			//$('#NJDM').combobox('disable');
			//$('#XBDM').combobox('disable');
			//$('#SSZY').combobox('disable');
						
			//打开dialog
			$('#classInfo').dialog({   
				title: '编辑班级信息'   
			});
			if(row!=undefined && row!=''){
				$('#XBDM').combobox('setValue', tempXbdm);
				$('#form1').form('load', row);
				$('#BJMC').html(row.BJMC);
			}
			saveType = 'edit';
			$('#classInfo').dialog('open');
		}
		
		if(id == 'del'){
			if(iKeyCode == ''){
				alertMsg('请选择一行数据');
				return;
			}
			ConfirmMsg('是否确定要删除当前选择的班级', 'delClass();', '');
		}
		
		if(id == "saveClassTea"){//教师
			var teas="";	
			var html="";
			
			for (var i = 0;i < teainfoidarray.length;i++) {
				if(html==""){
					if((teainfoidarray[i]=='000'&&teainfoarray[i]=='请选择')||teainfoidarray[i]==''||(teainfoidarray[i]+'')=='undefined'){
					
					}else{
						teas+=teainfoidarray[i];
						html+=teainfoarray[i];
					}
				}else{
					if((teainfoidarray[i]=='000'&&teainfoarray[i]=='请选择')||teainfoidarray[i]==''||(teainfoidarray[i]+'')=='undefined'){
						
					}else{
						teas+="+"+teainfoidarray[i];
						html+="+"+teainfoarray[i];
					}			
				}
			}
			$('#BZRGH').val(teas);
			$('#BZR').textbox('setValue', html);
			$('#classTeaSetDialog').dialog("close");
		}
		//判断获取参数为save，执行保存操作
		if(id == 'save'){
			//var bjbh = $('#BJBH').textbox('getValue');
			//var bjmc = $('#BJMC').textbox('getValue');
			
			/*
			if(bjbh == ''){
				alertMsg('请填写班级编号');
				return;
			}
			if(bjbh.length > 10){
				alertMsg('班级编号长度超出10位');
				return;
			}
			
			if(bjmc == ''){
				alertMsg('请填写班级名称');
				return;
			}
			if(bjmc.length > 50){
				alertMsg('班级名称长度超出50位');
				return;
			}
			*/
			if($('#NJDM').combobox('getValue') == ''){
				alertMsg('请选择年级');
				return;
			}
			if($('#XBDM').combobox('getValue') == ''){
				alertMsg('请选择系部');
				return;
			}
			if($('#SSZY').combobox('getValue') == ''){
				alertMsg('请选择所属专业');
				return;
			}
			if($('#BZR').textbox('getValue').indexOf("+")>-1){
				alertMsg('班主任只能选一人');
				return;
			}
			$('#XBDM1').val($('#XBDM').combobox('getValue'));
			$('#active').val(saveType);
			
			if(saveType=='edit' && parseInt(row.num)>0 && ($('#NJDM').combobox('getValue')!=tempNjdm || $('#XBDM').combobox('getValue')!=tempXbdm)){
				alertMsg('当前班级已设置过授课计划，无法修改年级代码和所属系部!');
			}else{
				$("#form1").submit();
			}
		}
	}
	
	//删除
	function delClass(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_ClassSet',
			data : 'active=del&BJBH=' + iKeyCode,
			dataType:"json",
			success : function(data){
				if(data[0].MSG == '删除成功'){
					showMsg(data[0].MSG);
					loadData();
				}else{
					alertMsg(data[0].MSG);
				}
			}
		});
	}
	
	/**表单提交**/
	$('#form1').form({
		//定位到servlet位置的url
		url:'<%=request.getContextPath()%>/Svl_ClassSet',
		//当点击事件后触发的事件
		onSubmit: function(data){
			return $(this).form('validate');//验证
		}, 
		//当点击事件并成功提交后触发的事件
		success:function(data){
			var json = eval("("+data+")");
			
			if(json[0].MSG == '保存成功'){
				showMsg(json[0].MSG);
				$("#active").val('');
				
				emptyDialog();//清空并关闭dialog
				loadData();
				$('#classInfo').dialog('close');
			}else{
				alertMsg(json[0].MSG);
			}
		}
	});
	
	/**清空Dialog中表单元素数据**/
	function emptyDialog(){
		saveType = '';
		tempNjdm = '';
		tempXbdm = '';
		teabh = '';
		tempJsbh = '';
		teainfoidarray.splice(0,teainfoidarray.length);
		teainfoarray.splice(0,teainfoarray.length);
		
		//$('#BJBH').textbox('readonly', false);
		//$('#BJBH').textbox('setValue', '');
		$('#BJMC').html('');
		$('#NJDM').combobox('enable');
		$('#NJDM').combobox('setValue', '');
		$('#XBDM').combobox('enable');
		$('#XBDM').combobox('setValue', '');
		$('#SSZY').combobox('enable');
		$('#SSZY').combobox('setValue', '');
		//$('#ZRS').numberbox('setValue', '');
		$('#BZR').textbox('setValue', '');
		$('#BJJC').textbox('setValue', '');
	}
	
	//打开teacher编辑窗口
	function opentea(){
		$('#ic_teaId').val("");
		$('#ic_teaName').val("");
		
		teabh = teainfoidarray[0];
		loadGridTea();
		//2017/12/11翟旭超注释
		//$('#classTeaSetDialog').dialog("open");		
		//2017/12/11翟旭超加
		var teaCode = $('#BZRGH').val();
		showOpen(teaCode);
	}
	
	function loadGridTea(){
		isLoad = true;
		
		$('#teaList').datagrid({
			url: '<%=request.getContextPath()%>/Svl_ClassSet',
 			queryParams: {"active":"openTeacher","teaId":$('#ic_teaId').val(),"teaName":$('#ic_teaName').val(),"teacharr":teabh},
			loadMsg : "信息加载中请稍后!",//载入时信息
			title:'',
			width:'100%',
			nowrap: false,
			fit:true, //自适应父节点宽度和高度
			showFooter:true,
			striped:true,
			pagination:true,
			pageSize:20,
			singleSelect:true,
			pageNumber:1,
			rownumbers:true,
			fitColumns: true,
			columns:[[
				{field:'ckt',checkbox:true},
				{field:'工号',title:'工号',width:fillsize(0.45),align:'center'},
				{field:'姓名',title:'姓名',width:fillsize(0.45),align:'center'}
				
			]],
			onSelect:function(rowIndex,rowData){
				if(isLoad == false){
					teainfoidarray.splice(0,teainfoidarray.length);
					teainfoarray.splice(0,teainfoarray.length);
					teainfoidarray.push(rowData.工号);
					teainfoarray.push(rowData.姓名);
					teabh=rowData.工号;
				}
			},
			onUnselect:function(rowIndex,rowData){
				$.each(teainfoidarray, function(key,value){
					if(value == rowData.工号){
						teainfoidarray.splice(key, 1);
						teainfoarray.splice(key, 1);
					}
				});
			},
			onLoadSuccess: function(data){
				$(".datagrid-header-check").html('&nbsp;');//隐藏全选
				//勾选已选授课教师
				if(data){
					if(teabh!=undefined){
						var selteaid=teabh.split("+");
						$.each(data.rows, function(rowIndex, rowData){
							for(var i=0; i<selteaid.length; i++){
								if(selteaid[i] == rowData.工号){
									$('#teaList').datagrid('selectRow', rowIndex);
								}
							}
						});
					}
				}
	 			isLoad = false;
			}
		});
	}
	
	//保存选择的班主任信息
	function editTeaInfo(id,name){  
		var checkbox = document.getElementById(id);
		if(checkbox.checked){//勾选
			teainfoidarray.push(id);
			teainfoarray.push(name);
		}else{//不勾选
			for(var i=0;i<teainfoidarray.length;i++){
				if(checkbox.id==teainfoidarray[i]){
					teainfoidarray.splice(i,1);
					teainfoarray.splice(i,1);
				}
			}
		}
	}
	
	//2017/12/7翟旭超加  begin
	//打开一个人员选择窗口
		function showOpen(teaCode){
			$('#test').window('open');//打开一个window
			openWindow(teaCode);
		}
		// 设置窗口属性
		function openWindow(teaCode){
			$('#test').window({
					title: '人员列表',
					modal: true,
					shadow: false,
					closed: false,
					maximizable:false,
					minimizable:false,
					onOpen:function(){
						//$("#xiafaxiada").attr("src",'http://180.166.123.122:8087/HRM/form/param/TeaTree1.jsp?u='+encodeURIComponent('http://180.166.123.122:8087/SCOA1//getUsers.jsp')+'&s='+teaCode);
						$("#xiafaxiada").attr("src",'http://180.166.123.122:8087/HRM/form/param/TeaTree1.jsp?u='+encodeURIComponent('http://192.168.111.26:8080/XDEAM/form/registerScoreSet//getUsers.jsp')+'&s='+teaCode);
					}
			});
		}
		//关闭人员选择窗口
		function closeWin(){
			$('#test').window('close');
		}
		
		function getUsers(data){
			var text = decodeURI(data).split(',');
			var ids = [];
			var names = [];
			for(var i=0;i<text.length;i++){
				ids.push(text[i].split(':')[0]);
				names.push(text[i].split(':')[1]);
			}
			$('#addName').val(ids.join(','));
			$('#ic_target').val(names.join(','));
			//AddTea(ids,names);
			
			if(text.length>1){
				alertMsg('只能选择一个班主任');
				return;
			}else{
				$('#BZRGH').val(ids);
				$('#BZR').textbox('setValue', "");
				
				$('#BZR').textbox('setValue', names);
				closeWin();
			}
		}
  	//2017/12/7翟旭超加  end
</script>
</html>