<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="java.util.Vector"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.src.develop.store.user.*"%>
<%@ page import="com.pantech.base.common.tools.PublicTools"%>
<%
	/**
		创建人：翟旭超
		Create date: 2016.07.13
		功能说明：用于设置问卷结果查询
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
	<title>问卷信息列表</title>
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
	UserProfile up = new UserProfile(request,MyTools.getSessionUserCode(request));
	String userName =up.getUserName();
	if(userName==null){
		userName="";
	}
	String root = request.getContextPath();
	//out.println("当前在线人数： "+SessionListener.getCount());	
	Vector v = new Vector(); 
	String usercode = "";
	usercode = MyTools.getSessionUserCode(request);
	String sAuth = "";
	//int i=0;
	AuthObject auth;
	//获取人员所属角色
	v = up.getUserAuth();
	//up.setUserAuth();
	if(v!=null){
		for(int i=0; i<v.size(); i++){
			if(i == v.size()-1){
				sAuth += MyTools.StrFiltr(v.get(i));
			}else{
				sAuth += MyTools.StrFiltr(v.get(i))+"O";
			}
		}
	}
	// 如果无法获取人员信息，则自动跳转到登陆界面
%>
<body class="easyui-layout">
	<div id="north"  region="north" title="问卷结果" style="height:91px;" >
		<table>
			<tr>
				<td><a href="#" id="reset" class="easyui-linkbutton" plain="true" iconcls="icon-reload" onClick="doToolbar(this.id);" title="">评教重置</a></td>
			</tr>
		</table>
		<table id="ee" singleselect="true" width="100%" class="tablestyle">
			<tr>
				<td  class="titlestyle">问卷名称</td>
				<td>
					<input name="WJ_BT_CX" id="WJ_BT_CX" class="easyui-textbox" style="width:150px;">
				</td>
				<td class="titlestyle">学年学期名称</td>
				<td>
					<select name="WJ_XNXQBM_CX" id="WJ_XNXQBM_CX" class="easyui-combobox" style="width:150px;">
					</select>
				</td>
				<td class="titlestyle">
  					开始时间
  				</td>
  				<td>
   					<input id="WJ_KSSJ_CX" name="WJ_KSSJ_CX" class="easyui-datebox" style="width:150px;" type="text">
   				</td>
   				<td class="titlestyle">
  					结束时间
  				</td>
  				<td>
					<input id="WJ_JSSJ_CX" name="WJ_JSSJ_CX" class="easyui-datebox" style="width:150px;" type="text">
   				</td>
   				<td  class="titlestyle" style="width:150px; text-align:center;">
					<a href="#" onclick="doToolbar(this.id)" id="search" class="easyui-linkbutton" iconcls="icon-search" plain="true">查询</a>
				</td>		
			</tr>
	    </table>
	</div>
	<div region="center">
		<table id="formlist"  name="formlist" width="100%">
		</table>
	</div>
	
	<!-- 查看结果dialog -->
	<div id="chakandialog">
		<div class="easyui-layout" style="width:100%; height:100%;">
			<div region="north" style="height:32px;">
			<table id="table2" singleselect="true" width="100%" class="tablestyle">
				<tr>
					<td style="width:50px;" class="titlestyle">年级</td>
					<td>
						<select name="WJ_NJ_CX" id="WJ_NJ_CX" class="easyui-combobox" style="width:150px;">
						
						</select>
					</td>
					<td class="titlestyle" style="width:150px;" >班级名称</td>
					<td>
						<input name="WJ_BJMC_CX" id="WJ_BJMC_CX" class="easyui-textbox" style="width:150px;"></input>
					</td>
					<td class="titlestyle" style="width:150px;">完成情况</td>
					<td>
						<select name="WJ_WCQK_CX" id="WJ_WCQK_CX" class="easyui-combobox" style="width:150px;" editable="false" panelHeight="120px">
								<option id="" value="" >全部</option>
								<option id="" value="1">已完成</option>
								<option id="" value="2">未完成</option>
						</select>
					</td>
	   				<td class="titlestyle" style="width:150px; text-align:center;">
						<a href="#" onclick="doToolbar(this.id)" id="cha" class="easyui-linkbutton" iconcls="icon-search" plain="true">查询</a>
					</td>		
				</tr>
		    </table>
		</div>
		<div region="center">
			<table id="classCountList"  name="classCountList" width="100%">
			</table>
			
			<input id="WJ_WJBH_CK" name="WJ_WJBH_CK" type="hidden">
			<input id="WJ_XNXQBH_CK" name="WJ_XNXQBH_CK" type="hidden">
			<input id="WJ_XZBDM" name="WJ_XZBDM" type="hidden">
		</div>
		</div>
	</div>
	
	<!-- 查询班级详情 -->
	<div id="CxbjxqDialog" >
		<div class="easyui-layout" style="width:100%; height:100%;" >
			<div region="north" style="height:60px;">
				<table id="table3" name="table3" style="width:100%; height:100%;" class="tablestyle" >
					<tr>
						<td class="titlestyle" style="width:100px;">行政班代码</td>
						<td class="titlestyle" id="WJ_XZBDM_XQ"></td>
					</tr>
					<tr>
						<td class="titlestyle" style="width:60px;">行政班名称</td>
						<td class="titlestyle" id="WJ_XZBMC_XQ"></td>
					</tr>
				</table>
			</div>
			<div region="center">
				<table id="classDetailInfo" style="width:100%;"></table>
			</div>
		</div>
	</div>
	
	<!-- 重置班级或学生的问卷信息 -->
	<div id="CzbjxswjDialog" >
		<div class="easyui-layout" style="width:100%; height:100%;" >
			<!-- <div region="north" style="height:60px;"> -->
				<table id="cztable" name="cztable" style="width:100%;" class="tablestyle" >
					<tr>
						<td class="titlestyle">学年学期名称</td>
						<td>
							<select name="CZ_XNXQMC" id="CZ_XNXQMC" style="width:205px;" class="easyui-combobox" panelHeight="auto" editable="false">
									
							</select>
						</td>
					</tr>
					<tr>
						<td class="titlestyle">问卷名称</td>
						<td>
							<select name="CZ_WJMC" id="CZ_WJMC" style="width:205px;" class="easyui-combobox" panelHeight="auto" editable="false">
									
							</select>
						</td>
					</tr>
					<tr>
						<td class="titlestyle">班级</td>
						<td>
							<select name="CZ_BJ" id="CZ_BJ" style="width:205px;" class="easyui-combobox" panelHeight="auto" editable="false" multiple="true">
									
							</select>
						</td>
					</tr>
					<tr>
						<td class="titlestyle">学生姓名</td>
						<td>
							<select name="CZ_XSXM" id="CZ_XSXM" style="width:205px;" class="easyui-combobox" panelHeight="auto" editable="false" multiple="true">
									
							</select>
						</td>
					</tr>
					<tr>
						<td colspan="2" align="center"><a href="#" id="resetbjxs" class="easyui-linkbutton" plain="true" iconcls="icon-reload" onClick="doToolbar(this.id);" title="">重置</a></td>
					</tr>
				</table>
			<!-- </div> -->
		</div>
	</div>
</body>
<script type="text/javascript">
	var row = '';      //行数据
	var pageNum = 1;   //datagrid初始当前页数
	var pageSize = 20; //datagrid初始页内行数
	var WJ_BT_CX = '';//查询条件
	var WJ_XNXQBM_CX = '';
	var WJ_KSSJ_CX = ''; 
	var WJ_JSSJ_CX = '';
	var curWjbh = '';
	var curWjlx = '';
	
	$(document).ready(function(){
		initDialog();//初始化对话框
		loadGrid();
		initCombobox();
	});
	/**加载 datagrid控件，读取页面信息
		@listData 列表数据
	**/
	function loadGrid(){
		$('#formlist').datagrid({
			url:'<%=request.getContextPath()%>/Svl_Wjjgcx',
			queryParams:{"active":"query",
						"WJ_BT":encodeURI($('#WJ_BT_CX').val()),
						"WJ_XNXQBM_CX":encodeURI($('#WJ_XNXQBM_CX').combobox('getValue')),
						"WJ_KSSJ_CX":encodeURI($('#WJ_KSSJ_CX').datebox('getValue')),
						"WJ_JSSJ_CX":encodeURI($('#WJ_JSSJ_CX').datebox('getValue'))
			},
			title:'问卷信息列表',
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
			
			//下面是表单中加载显示的信息
			columns:[[
				{field:'标题',title:'问卷名称',width:fillsize(0.5),align:'center'},
				{field:'学年学期名称',title:'学年学期名称',width:fillsize(0.15),align:'center'},
				{field:'开始时间',title:'开始时间',width:fillsize(0.1),align:'center'},
				{field:'结束时间',title:'结束时间',width:fillsize(0.1),align:'center'},
				{field:'AA_SET',align:'center',title:'操作',width:fillsize(0.1),formatter:function(value,rows){
					var str = '';
				    str = "<input type='button' value='查看结果' style='cursor:pointer;width:70px;' onclick='openCountInfo(\"" + rows.问卷编号 + "\",\""+rows.问卷类型+"\",\""+rows.学年学期编码+"\")'>";
				    return str;
				}
			}
			]],
			onClickRow:function(rowIndex,rowData){ 
			},
			//双击某行时触发
			onDblClickRow:function(rowIndex,rowData){
			},
			//成功加载时
			onLoadSuccess:function(data){//查询成功时候把后台的查询语句赋值给变量listsql
			}
		});
	}
		
	/**加载 查看结果dialog控件，读取页面信息
		@listData 列表数据
	**/
	function loadCountInfo(){
		$('#classCountList').datagrid({
			url:'<%=request.getContextPath()%>/Svl_Wjjgcx',
			queryParams:{"active":"queryCKJG",
						"WJ_NJ_CX":encodeURI($('#WJ_NJ_CX').combobox('getValue')),
						"WJ_BJMC_CX":encodeURI($('#WJ_BJMC_CX').val()),
						"WJ_WCQK_CX":encodeURI($('#WJ_WCQK_CX').combobox('getValue')),
						"WJ_XNXQBM_CX":encodeURI($('#WJ_XNXQBH_CK').val()),
						"WJ_WJBH":curWjbh,"WJ_WJLX":curWjlx
			},
			title:'完成情况列表',
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
			
			//下面是表单中加载显示的信息
			columns:[[
				{field:'行政班代码',title:'行政班代码',width:fillsize(0.4),align:'center'},
				{field:'行政班名称',title:'行政班名称',width:fillsize(0.8),align:'center'},
				{field:'总人数',title:'总人数',width:fillsize(0.3),align:'center'},
				{field:'已完成',title:'已完成',width:fillsize(0.3),align:'center'},
				{field:'未完成',title:'未完成',width:fillsize(0.3),align:'center'},
				{field:'AA_SET',align:'center',title:'操作',width:fillsize(0.3),formatter:function(value,rows){
					var str = '';
				   	str = "<input type='button' value='详情' style='cursor:pointer;width:60px;' onclick='openSetDialog1(\"" + rows.行政班代码 + "\",\""+rows.行政班名称+"\",\""+rows.总人数+"\",\""+rows.已完成+"\",\""+rows.未完成+"\",\""+rows.年级代码+"\",\""+rows.完成情况+"\")'>";

					return str;
				}
			}
			]],
			onClickRow:function(rowIndex,rowData){ 
			},
			//双击某行时触发
			onDblClickRow:function(rowIndex,rowData){
			},
			//成功加载时
			onLoadSuccess:function(data){//查询成功时候把后台的查询语句赋值给变量listsql
			}
		});
	}
		
	/**加载 datagrid控件，读取页面信息
		班级详情
	**/
	function loadGridBJXQ(){
	$('#classDetailInfo').datagrid({
			url:'<%=request.getContextPath()%>/Svl_Wjjgcx',
			queryParams:{"active":"queryOne","WJ_WJBH":curWjbh,"WJ_WJLX":curWjlx,
					"WJ_XZBDM":encodeURI($('#WJ_XZBDM').val())
			},
			title:'详情列表',
			width:'100%',
			nowrap: false,
			fit:true, //自适应父节点宽度和高度
			showFooter:true,
			striped:true,
			singleSelect:true,
			rownumbers:true,
			fitColumns: true,
			//下面是表单中加载显示的信息
			columns:[[
				{field:'学号',title:'学号',width:fillsize(0.2),align:'center'},
				{field:'姓名',title:'姓名',width:fillsize(0.2),align:'center'},
				{field:'完成情况',title:'完成情况',width:fillsize(0.2),align:'center',formatter:function(value,row,index){
					if(value=='未完成')
						return "<font color='red'>未完成</font>";
					else
						return "<font color='black'>已完成</font>";
				}}
			]],
			onClickRow:function(rowIndex,rowData){ 
			},
			//双击某行时触发
			onDblClickRow:function(rowIndex,rowData){
			},
			//成功加载时
			onLoadSuccess:function(data){//查询成功时候把后台的查询语句赋值给变量listsql
			}
		});
	}
		
	/**打开结果统计信息**/
	function openCountInfo(code, type, xnxqbm){
		curWjbh = code;
		curWjlx = type;
	
		$('#WJ_XNXQBH_CK').val(xnxqbm);
		$('#WJ_NJ_CX').combobox({
			url:'<%=request.getContextPath()%>/Svl_Wjjgcx?active=loadNjCombo&WJ_XNXQBM=' + xnxqbm,
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			selectOnNavigation:true,
			panelHeight:'100',
			onLoadSuccess:function(data){
				if(data!=""){
					$(this).combobox('setValue','');
				}
			} 
		});
		$('#WJ_BJMC_CX').val(''); 
		$('#WJ_WCQK_CX').combobox('setValue', '');
		loadCountInfo();
		$('#chakandialog').dialog('open'); 
	}
	
	function openSetDialog1(xzbdm,xzbmc,zrs,ywc,wwc,njdm,wcqk){
		//详情按钮
		$('#WJ_XZBDM').val(xzbdm);
		$('#WJ_XZBDM_XQ').html(xzbdm);
		$('#WJ_XZBMC_XQ').html(xzbmc);
		loadGridBJXQ();
		$('#CxbjxqDialog').dialog('open');
	}
	
	/**加载 dialog控件**/
	 function initDialog(){
	 	//界面上点击新建编辑的界面
		$('#chakandialog').dialog({   
			width: 800,//宽度设置   
			height: 600,//高度设置 
			resizable:true,//大小
			modal:true,
			closed: true, 
			cache: false, 
			title:'查看结果',
			draggable:true,//是否可移动dialog框设置
			onClose:function(data){
				$('#classCountList').datagrid('loadData',{total:0,rows:[]});
			}
		});
		
		//查看班级详情dialog
		$('#CxbjxqDialog').dialog({   
			width: 600,//宽度设置   
			height: 500,//高度设置 
			resizable:true,//大小
			modal:true,
			closed: true, 
			cache: false, 
			title:'查看详情',
			draggable:true,//是否可移动dialog框设置
			//打开事件
			onOpen:function(data){},
			//读取事件
			onLoad:function(data){},
			//关闭事件
			onClose:function(data){
				$('#classDetailInfo').datagrid('loadData',{total:0,rows:[]});
			}
		});
		
		//重置班级或学生问卷信息dialog
		$('#CzbjxswjDialog').dialog({   
			width: 310,//宽度设置   
			height: 165,//高度设置 
			resizable:false,//大小
			modal:true,
			closed: true, 
			cache: false, 
			title:'重置',
			draggable:true,//是否可移动dialog框设置
			//打开事件
			onOpen:function(data){},
			//读取事件
			onLoad:function(data){},
			//关闭事件
			onClose:function(data){
			}
		});
	}
	
		
	/**工具栏按钮调用方法，传入按钮的id
		@id 当前按钮点击事件
	**/
	 function doToolbar(iToolbar){
		if(iToolbar == "search"){
			loadGrid();
		}
		if(iToolbar == "cha"){
			loadCountInfo();
		}
		if(iToolbar == "reset"){
			$('#CzbjxswjDialog').dialog('open'); 
			$('#CZ_XNXQMC').combobox('setValue',curXnXq);
			$('#CZ_WJMC').combobox('setValue','');
			
			$('#CZ_BJ').combobox('setText', '请先选择问卷名称');
			$('#CZ_BJ').combobox('disable');
						
			$('#CZ_XSXM').combobox('setText', '请先选择问卷名称');
			$('#CZ_XSXM').combobox('disable'); 
		}
		
		if(iToolbar == "resetbjxs"){
			if($('#CZ_BJ').combobox('getValue')==undefined || $('#CZ_XSXM').combobox('getValue')==undefined){
				alertMsg('请选择需要重置的班级或学生');
				return;
			}
		
			ConfirmMsg("是否确认重置 "+$('#CZ_XNXQMC').combobox('getText')+" "+$('#CZ_WJMC').combobox('getText')+" 的评教信息？", "DelWJXX()", "");
		}
	}
	
	function initCombobox(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Wjjgcx',
			data : 'active=loadCurXnxq',
			dataType:"json",
			success : function(data) {
				if(data[0].curXnxq != ''){
					curXnXq = data[0].curXnxq;
				}
				XNXQMCcombobox(curXnXq);
			}
		});
	
		//学年学期名称
		$('#ee #WJ_XNXQBM_CX').combobox({
				url:'<%=request.getContextPath()%>/Svl_Wjsz?active=XNXQMCcombobox', 
				valueField:'comboValue',
				textField:'comboName',
				editable:false,
				panelHeight:'100',
				onLoadSuccess:function(data){
					if(data!=""){
						$(this).combobox('setValue','');
					}
				}
		});
		XNXQMCcombobox();
	}
	
	//查询重置按钮里的学年学期
	function XNXQMCcombobox(curXnXq){
		$('#CZ_XNXQMC').combobox({
				url:'<%=request.getContextPath()%>/Svl_Wjsz?active=XNXQMCcombobox', 
				valueField:'comboValue',
				textField:'comboName',
				editable:false,
				panelHeight:'100',
				onLoadSuccess:function(data){
					if(data!=""){
						$(this).combobox('setValue',curXnXq);
					}
				},
				onChange:function(data){
					if(data==''){
						$('#CZ_WJMC').combobox('setText', '请先选择学年学期');
						$('#CZ_WJMC').combobox('disable');
						
						$('#CZ_BJ').combobox('setText', '请先选择学年学期');
						$('#CZ_BJ').combobox('disable');
						
						$('#CZ_XSXM').combobox('setText', '请先选择学年学期');
						$('#CZ_XSXM').combobox('disable');
						
						$('#resetbjxs').linkbutton('disable'); 
					}else{
						loadCZWJMCCombo();
					}
				}
		});
	}
	
	//查询重置按钮里的问卷
	function loadCZWJMCCombo(){
		$('#CZ_WJMC').combobox({
			url:'<%=request.getContextPath()%>/Svl_Wjjgcx?active=loadCZWJMCCombo&WJ_XNXQBM=' + $('#CZ_XNXQMC').combobox('getValue'),
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			panelHeight:'140', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data.length == 1){
					$('#CZ_WJMC').combobox('setText', '没有可选的问卷');
					$('#CZ_WJMC').combobox('disable');
							
					$('#CZ_BJ').combobox('setText', '没有可选的问卷');
					$('#CZ_BJ').combobox('disable');
							
					$('#CZ_XSXM').combobox('setText', '没有可选的问卷');
					$('#CZ_XSXM').combobox('disable');
				}else{
					$('#CZ_WJMC').combobox('enable');
					//初始化combobox时赋值
					$(this).combobox('setValue', '');
					
					$('#CZ_BJ').combobox('setText', '请先选择问卷名称');
					$('#CZ_BJ').combobox('disable');
					
					$('#CZ_XSXM').combobox('setText', '请先选择问卷名称');
					$('#CZ_XSXM').combobox('disable'); 
					$('#resetbjxs').linkbutton('disable');
				}
			},
			//下拉列表值改变事件
			onChange:function(data){
				if(data==''){
					$('#CZ_BJ').combobox('setText', '请先选择问卷名称');
					$('#CZ_BJ').combobox('disable');
					
					$('#CZ_XSXM').combobox('setText', '请先选择问卷名称');
					$('#CZ_XSXM').combobox('disable');
				}else{
					loadCZBJCombo();
				}
			}
		});
	}
	
	//查询重置按钮里的班级
	function loadCZBJCombo(){
	$('#resetbjxs').linkbutton('enable');
		//读取班级下拉框
		$('#CZ_BJ').combobox({
				url:'<%=request.getContextPath()%>/Svl_Wjjgcx?active=loadCZBJCombo&WJ_XNXQBM=' + $('#CZ_XNXQMC').combobox('getValue') + '&WJ_WJBH=' + $('#CZ_WJMC').combobox('getValue'),
				valueField:'comboValue',
				textField:'comboName',
				editable:true,
				panelHeight:'140', //combobox高度
				onLoadSuccess:function(data){
					//判断data参数是否为空
					if(data.length == 1){
						$('#CZ_BJ').combobox('setText', '没有可选的班级');
						$('#CZ_BJ').combobox('disable');
											
						$('#CZ_XSXM').combobox('setText', '没有可选的学生');
						$('#CZ_XSXM').combobox('disable');
					}else{
						$('#CZ_BJ').combobox('enable');
						//初始化combobox时赋值
						$(this).combobox('setValue', '');
					}
				},
				//下拉列表值改变事件
				onChange:function(data){
						loadBJXSCombo();
				},
				onSelect: function (row) {
				 	if($(this).combobox('getValues')!=''){
				 		$(this).combobox('unselect', '');
				 	}
				 	if(row.comboValue==''){
				 		$(this).combobox('setValues', '');
				 	}
				},
				onUnselect: function (row) {
					if($(this).combobox('getValues')==''){
						$(this).combobox('setValues', '');
					}
				}
				
			});
	}
	
	function loadBJXSCombo(){
		//读取学生姓名下拉框
		$('#CZ_XSXM').combobox({
			url:'<%=request.getContextPath()%>/Svl_Wjjgcx?active=loadBJXSCombo&WJ_XNXQBM=' + $('#CZ_XNXQMC').combobox('getValue') + '&WJ_WJBH=' + $('#CZ_WJMC').combobox('getValue') + '&WJ_XZBDM=' + $('#CZ_BJ').combobox('getValues'),
			valueField:'comboValue',
			textField:'comboName',
			editable:true,
			panelHeight:'140', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data.length == 1){
										
					$('#CZ_XSXM').combobox('setText', '没有可选的学生');
					$('#CZ_XSXM').combobox('disable');
				}else{
					$('#CZ_XSXM').combobox('enable');
					//初始化combobox时赋值
					$(this).combobox('setValue', '');
				}
			},
			//下拉列表值改变事件
			onChange:function(data){
				
			},
			onSelect: function (row) {
			 	if($(this).combobox('getValues')!=''){
			 		$(this).combobox('unselect', '');
			 	}
			 	if(row.comboValue==''){
			 		$(this).combobox('setValues', '');
			 	}
			},
			onUnselect: function (row) {
				if($(this).combobox('getValues')==''){
					$(this).combobox('setValues', '');
				}
			}
		});
	}
	
	//重置指定数据的问卷信息
  	function DelWJXX(){
  		$.ajax({
			type:"post",
			url:'<%=request.getContextPath()%>/Svl_Wjjgcx',  
			data:"active=delCZWJXX&WJ_XNXQBM="+$('#CZ_XNXQMC').combobox('getValue')+"&WJ_WJBH="+$('#CZ_WJMC').combobox('getValue')+ '&WJ_XZBDM=' + $('#CZ_BJ').combobox('getValues') + '&CZ_XSXM=' + $('#CZ_XSXM').combobox('getValues'),//设置模式   
			dataType:'json',
			success:function(datas){
				var data = datas[0];
				if(data.MSG == '重置成功'){
					showMsg(data.MSG);
				}else{
					alertMsg(data.MSG);
				}
			}
  		});
  	}
	</script>
</html>