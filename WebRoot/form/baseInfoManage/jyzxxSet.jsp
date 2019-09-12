<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="java.util.Vector"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.src.develop.store.user.*"%>
<%
	/**
		创建人：maowei
		Create date: 2016.11.18
		功能说明：教研组信息设置
		页面类型:列表及模块入口
		修订信息(有多次时,可有多个)
		修订日期:
		原因:
		修订人:
		修订时间:
	**/
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
  <head>
    <title>教研组信息设置</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> 
	<!-- JQuery 专用4个文件 -->
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/icon.css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/script/common/publicScript.js"></script>
    <!-- JQuery 文件导入结束 -->
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
  
  <body class="easyui-layout" style="overflow:hidden">
 	<div id="north" region="north" title="教研组信息" style="height:91px;">
 		<table>
 			<tr>
 				<td><a href="#" onclick="doToolbar(this.id)" id="new" name="new" class="easyui-linkbutton" plain="true" iconcls="icon-new">新建</a></td>
				<td><a href="#" onclick="doToolbar(this.id)" id="edit" name="edit" class="easyui-linkbutton" plain="true" iconcls="icon-edit">编辑</a></td>
				<td><a href="#" onclick="doToolbar(this.id)" id="del" name="del" class="easyui-linkbutton" plain="true" iconcls="icon-cancel">删除</a></td>
 			</tr>
 		</table>
 		<table id="ee" width="100%" class="tablestyle" >
			<tr>
				<td class="titlestyle">教研组名称</td>
				<td >
					<input id="JN_JYZMC" name="JN_JYZMC" class="easyui-textbox" style="width:180px">
				</td>
				<td class="titlestyle">学科名称</td>
				<td >
					<input id="JN_JYZXK" name="JN_JYZXK" class="easyui-textbox" style="width:180px">
				</td>
				<td class="titlestyle">教研组长</td>
				<td>
					<input id="JN_JYZZZ" name="JN_JYZZZ" class="easyui-textbox" style="width:180px">
				</td>
				<td style="width:150px;" align="center">
					<a href="#" onclick="doToolbar(this.id)" id="que" class="easyui-linkbutton" plain="true" iconcls="icon-search">查询</a>
				</td>	
			</tr>
		</table>	
	    </div>
 		 <div region="center">
			<table id = "JYZXXList" style="width:100%;">
			</table>
		</div>
		<!-- 考试等级设置 -->
	 	<div id="Dialog_JYZXXInfo" style="overflow:hidden">
	 	<form id="form1" name="form1" method="post">
			<table id = "JYZXXInfo" width="100%" class="tablestyle" >
				<tr>
					<td class="titlestyle" style="width:120px;">
						教研组代码<font style="color:red;">&nbsp;*</font>
					</td>
					<td>
						<input id="JN_JYZDM1" name="JN_JYZDM1" class="easyui-textbox" style="width:180px" required="true"/>
					</td>
				</tr>
				<tr>
					<td class="titlestyle">
						教研组名称<font style="color:red;">&nbsp;*</font>
					</td>
					<td>
						<input id="JN_JYZMC1" name="JN_JYZMC1" class="easyui-textbox" style="width:180px" required="true"/>
					</td>
				</tr>
				<tr>
					<td class="titlestyle">
						学科名称<font style="color:red;">&nbsp;*</font>
					</td>
					<td>
						<select id="JN_XKMC1" name="JN_XKMC1" class="easyui-combobox" style="width:180px" > 
						</select>
					</td>
				</tr>
				<tr>
					<td class="titlestyle">
						教研组组长<font style="color:red;">&nbsp;*</font>
					</td>
					<td>
						<select id="JN_JYZZZ1" name="JN_JYZZZ1" class="easyui-combobox" style="width:180px;">
						</select>
					</td>
				</tr>
			</table>
			 <!--用户区 ：页面隐藏变量 -->
		        <INPUT type="hidden" name="active" id="active">
		        <INPUT type="hidden" name="JN_JYZZZBH" id="JN_JYZZZBH">
		        <INPUT type="hidden" name="JN_JYZXK1" id="JN_JYZXK1">
		        <INPUT type="hidden" name="saveType" id="saveType">
		     <!--用户区 ：页面隐藏变量-结束  -->
		</form>	
	 	</div>
  	
  </body>
  <script type="text/javascript">
  	var iKeyCode="";
  	var row = '';      //行数据
  	var userCode = '<%=usercode%>';
  	var sAuth = '<%=sAuth%>';
  	var examName="";
  	var lastIndex = 0;
  	var saveType = '';
  	var curPageNumber = 1;
  	var curPageSize = 20;
  	var JYZZZ='';
  	
  	$(document).ready(function(){
  		initDialog();//初始化对话框
  		initData();//页面初始化加载数据
  		
  		//设置输入框限制长度
  		setInputMaxLen('JN_JYZDM1', 5);
  		setInputMaxLen('JN_JYZMC1', 20);
  		setInputMaxLen('JN_NJJC1', 20);
  		setInputMaxLen('JN_XKMC1', 2);
	});
	
	
	function initData(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_JyzxxSet',
			data : 'active=initData&page=' + curPageNumber + '&rows=' + curPageSize,
			dataType:"json",
			success : function(data) {
				JYZZZ = data[0].jyzzzData;
				loadGrid(data[0].jyzData);
				initCombobox(data[0].jyzzzData, data[0].jyzxkData);
			}
		});
	}
	
	function initCombobox(jyzzzData,jyzxkData){
		$('#JN_JYZZZ1').combobox({
			data:jyzzzData,
			valueField : 'comboValue',
			textField : 'comboName',
			editable:true,
			panelHeight : '140',
			multiple:true,
			onLoadSuccess : function(data) {
				if (data != "") {
					$(this).combobox('setValue', '');
				}
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
		$('#JN_XKMC1').combobox({
			data: jyzxkData,
			valueField : 'comboValue',
			textField : 'comboName',
			panelHeight : '140',
			editable:false,
			onLoadSuccess : function(data) {
				if (data != "") {
					$(this).combobox('setValue', '');
				}
			}
		});	
	}
	
  	//查询列表
	function loadGrid(jyzData){
		$('#JYZXXList').datagrid({
			data :jyzData,
			title:'教研组列表',
			nowrap: false,//当数据长度超出列宽时将会自动截取
			fit:true,//自动折叠容器的大小将填充父容器
			showFooter:true,//显示视图的页脚
			striped:true, //隔行变色
			pagination:true,//开启分页
			pageSize:curPageSize,//每页查看的记录数量
			//pageList:null,
			singleSelect:true,//开启单选模式
			pageNumber:curPageNumber,//初始的页面为第一页
			rownumbers:true,//显示行数
			fitColumns: true,//自适应
			//下面是表单中加载显示的信息
			columns:[[
				{field:'教研组编号',align:'center',title:'教研组编号',width:fillsize(0.25)},
				{field:'教研组名称',align:'center',title:'教研组名称',width:fillsize(0.25)},
				{field:'学科名称',align:'center',title:'学科名称',width:fillsize(0.25)},
				{field:'教研组组长编号',align:'center',title:'教研组组长',width:fillsize(0.25),formatter:function(value,vec){
					var teaCode = value.split(',');
					var resultName = '';
					
					for(var i=0; i<teaCode.length; i++){
						for(var j=0; j<JYZZZ.length; j++){
							if(teaCode[i]!='' && teaCode[i]==JYZZZ[j].comboValue){
								resultName += JYZZZ[j].comboName+',';
								break;
							}
						}
					}
					
					if(resultName.length > 0)
						resultName = resultName.substring(0, resultName.length-1);
						
					return resultName;
				}}
			]],
			//双击某行时触发
			onDblClickRow:function(rowIndex,rowData){
				doToolbar("edit");
			},
			//读取datagrid之前加载
			onBeforeLoad:function(){},
			//单击某行时触发
			onClickRow:function(rowIndex,rowData){
				iKeyCode = rowData.教研组编号;
				row = rowData;
			},
			//加载成功后触发
			onLoadSuccess: function(data){
				iKeyCode = '';
				row = '';
				$('#JN_JYZDM1').textbox('setValue',"");
				curPageNumber = $(this).datagrid('options').pageNumber;
				curPageSize = $(this).datagrid('options').pageSize;
			},
			error:function(data){}
		});
	}
	
	/**加载 dialog控件**/
	function initDialog(){
		$('#Dialog_JYZXXInfo').dialog({  
			width: 300,//宽度设置   
			height: 167,//高度设置 
			modal:true,
			closed: true,   
			cache: false, 
			draggable:false,//是否可移动dialog框设置
			toolbar:[{
				//保存编辑
				text:"保存",
				iconCls:'icon-save',
				handler:function(){
					//传入save值进入doToolbar方法，用于保存
					doToolbar('save');
				} 
			}],
			//打开事件
			onOpen:function(data){},
			//读取事件
			onLoad:function(data){},
			//关闭事件
			onClose:function(data){
				$('#form1').form('clear');
			}
		});
	}
	
	/*
		form1，ajax提交事件
		页面URL不会变
		页面返回为json对象
	*/
	$('#form1').form({
		url:'<%=request.getContextPath()%>/Svl_JyzxxSet',
		onSubmit: function(){
	   },
	   success:function(data){
	   //页面提交成功
   	 	var json = eval("("+data+")");
    	if(json[0].MSG=="保存成功"){
				showMsg(json[0].MSG);
				loadData();
				$('#Dialog_JYZXXInfo').dialog('close');
			}else{
				alertMsg(json[0].MSG);
			}
    	},
   		error:function(data){}
	});
	
		// 验证表单
	function doCheck(){
		if($('#JN_JYZDM1').textbox('getValue') == ""){
			alertMsg('请填写教研组代码');
			return false;
		}
		if($('#JN_JYZMC1').textbox('getValue') == ""){
			alertMsg('请填写教研组名称');
			return false;
		}
		if($('#JN_XKMC1').combobox('getValue') == ""){
			alertMsg('请选择学科');
			return false;
		}
		if($('#JN_JYZZZ1').combobox('getValue')=="" || $('#JN_JYZZZ1').combobox('getValue')==undefined){
			alertMsg('请选择教研组组长');
			return false;
		}
		return true;
	}
	
	function loadData(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_JyzxxSet',
			dataType:"json",
			data : 'active=query&page=' + curPageNumber + '&rows=' + curPageSize + 
				'&JN_JYZMC=' + $('#JN_JYZMC').val() + 
				'&JN_JYZXK=' + $('#JN_JYZXK').val() +
				'&JN_JYZZZ=' + $('#JN_JYZZZ').val() ,
			success : function(data) {
				loadGrid(data[0].jyzData);
			}
		});
	}
	
	
	//删除ajax
	function DelData(){
		$.ajax({
			type:"post",
			url:'<%=request.getContextPath()%>/Svl_JyzxxSet',
			data:"active=del&JN_JYZDM1=" + iKeyCode,
			dataType:"json",
			success: function(datas){
				if(datas[0].MSG=="删除成功"){
					showMsg(datas[0].MSG);
					loadData();
				}else{
					alertMsg(datas[0].MSG);
				}
			}
		});
	}
	
	/**工具栏按钮调用方法，传入按钮的id
		@id 当前按钮点击事件
	**/
	function doToolbar(iToolbar){
		//查询
		if(iToolbar == "que"){
			curPageNumber = 1;
			loadData();
		}	
		//判断获取参数为new，执行新建操作
		if (iToolbar=="new"){
			$('#saveType').val('new');
			$('#JN_JYZDM1').textbox('setValue','系统自动生成');
			$('#JN_JYZDM1').next("span").find("input").css('color','#AAAAAA'); 
			$('#JN_JYZDM1').textbox('readonly',true);
			$('#JN_JYZMC1').textbox('setValue','系统自动生成');
			$('#JN_JYZMC1').next("span").find("input").css('color','#AAAAAA'); 
			$('#JN_JYZMC1').textbox('readonly',true);
			$('#JN_XKMC1').combobox('setValue', '');
			$('#JN_JYZZZ1').combobox('setValue', '');
			$('#Dialog_JYZXXInfo').dialog("setTitle","新建");
			$('#Dialog_JYZXXInfo').dialog("open");
		}
		
		//判断获取参数为edit，执行编辑操作
		if(iToolbar=="edit"){
			$('#saveType').val('edit');
			if(iKeyCode == ""){
				alertMsg("请选择一行数据");
				return;
			}
			
			$('#JN_JYZDM1').textbox('readonly',true);//教研组编号控件为只读模式
			$('#JN_JYZDM1').next("span").find("input").css('color','#AAAAAA'); 
			$('#JN_JYZMC1').textbox('readonly',true);//教研组编号控件为只读模式
			$('#JN_JYZMC1').next("span").find("input").css('color','#AAAAAA');
			//获取单击行数据
            $('#JN_JYZDM1').textbox('setValue',row.教研组编号);
            $('#JN_JYZMC1').textbox('setValue',row.教研组名称);
            $('#JN_XKMC1').combobox('setValue', row.学科代码);
            $('#JN_JYZZZ1').combobox('setValues', row.教研组组长编号);
			$('#Dialog_JYZXXInfo').dialog("setTitle","编辑");
			$('#Dialog_JYZXXInfo').dialog("open");
		}
		
		//判断获取参数为del，执行删除操作
		if(iToolbar=="del"){
			if(iKeyCode == ""){
			  alertMsg("请选择一行数据");
			  return ;
			}
			
	      	$('#active').val('del');
            ConfirmMsg("删除后无法恢复，您是否确认删除?","DelData()","");
		}
		
		//判断获取参数为save，执行保存操作
		if(iToolbar=="save"){
			if(doCheck()){
				$('#JN_JYZZZBH').val($('#JN_JYZZZ1').combobox('getValues'));
				$('#JN_JYZXK1').val($('#JN_XKMC1').combobox('getText'));
				$('#active').val("save");
		    	$('#form1').submit();
			}
		}
	}
  </script>
 </html>