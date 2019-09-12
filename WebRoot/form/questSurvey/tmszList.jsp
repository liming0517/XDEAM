<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="java.util.Vector"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.src.develop.store.user.*"%>
<%
	/**
		创建人：翟旭超
		Create date: 2016.06.08
		功能说明：用于设置题目信息
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
	<div id="north" region="north" title="题目信息" style="height:91px;" >
		<table>
			<tr>
				<td><a href="#" id="new" class="easyui-linkbutton" plain="true" iconcls="icon-add" onClick="doToolbar(this.id);" title="">新建</a></td>
				<td><a href="#" id="edit" class="easyui-linkbutton" plain="true" iconcls="icon-edit" onClick="doToolbar(this.id);" title="">编辑</a></td>
				<td><a href="#" id="del" class="easyui-linkbutton" plain="true" iconcls="icon-cancel" onClick="doToolbar(this.id);" title="">删除</a></td>
			</tr>
		</table>
		<table id="ee" singleselect="true" width="100%" class="tablestyle">
			<tr>
				<td  class="titlestyle">题目编号</td>
				<td>
					<input name="TT_TMBH_CX" id="TT_TMBH_CX" class="easyui-textbox" style="width:180px;">
				</td>
				<td  class="titlestyle">题目内容</td>
				<td>
					<input name="TT_TMNR_CX" id="TT_TMNR_CX" class="easyui-textbox" style="width:180px;">
				</td>
				<td  class="titlestyle">题目类型</td>
				<td>
					<select name="TT_TMLX_CX" id="TT_TMLX_CX" class="easyui-combobox" style="width:180px;" panelHeight="auto" editable="false">
						<option id="" value="">请选择</option>
						<option id="" value="01">单选题</option>
						<option id="" value="02">多选题</option>
						<option id="" value="03">表单题</option>
					</select>
				</td>
				<td style="width:150px; text-align:center;">
					<a href="#" onclick="doToolbar(this.id)" id="query" class="easyui-linkbutton" plain="true" iconcls="icon-search">查询</a>
				</td>				
			</tr>
	    </table>
	</div>
	<div region="center">
		<table id = "formlist"  name= "formlist" width = "100%">
		</table>
	</div>
	
	<!-- 题目信息新建编辑 -->
	<div id="questionDialog" style="overflow:hidden;">
		<form id="form1" name="form1" method='post'>
			<table id="table1" name="table1" style="width:100%; height:100%;" class="tablestyle" >
				<tr>
					<td class="titlestyle" style="width:150px">题目编号</td>
					<td class="titlestyle" id="TMBH" ></td>
				</tr>
				<tr>
					<td class="titlestyle">题目类型</td>
					<td>
						<select name="TT_TMLX" id="TT_TMLX" class="easyui-combobox" style="width:100%" panelHeight="auto" editable="false">
							<option id="" value="" >请选择</option>
							<option id="" value="01">单选题</option>
							<option id="" value="02">多选题</option>
							<option id="" value="03">表单题</option>
						</select>
					</td>
				</tr>
				<tr>
					<td class="titlestyle">题目内容</td>
					<td>
						<textarea name="TT_TMNR" id="TT_TMNR"  style="width:100%; height:76px;" maxlength="500"></textarea>		
					</td>
				</tr>
			</table>
			 <input type="hidden" name="active" id="active">
			  <input id="TT_TMBH" name="TT_TMBH" type="hidden"> 
		</form>
	</div>
	
	<!-- 表单题设置 -->
	<div id="bdtSetDialog" style="overflow:hidden" style="width:100%; height:100%;">
		<form id="formBDSZ" name="formBDSZ" method='post'>
				<table id="tableBDSZ" name="tableBDSZ" style="width:100%; height:100%;" class="tablestyle" >
					<tr>
						<td class="titlestyle" style="width:150px;">题目编号</td>
						<td class="titlestyle" id="TT_TMBH_BDSZ"></td>
					</tr>
					<tr>
						<td class="titlestyle" style="width:150px;">题目内容</td>
						<td class="titlestyle" id="TT_TMNR_BDSZ"></td>
					</tr>
					<tr>
						<td class="titlestyle" style="width:150px;height:70px;">题目要点</td>
						<td><textarea name="TT_TMYD_BDSZ" id="TT_TMYD_BDSZ" style="width:100%;height:100%"></textarea></td>
					</tr>
					<tr style="height:20px">
						<td class="titlestyle">分值</td>
						<td><input name="TT_FZ_BDSZ" id="TT_FZ_BDSZ" class="easyui-numberbox" style="width:180px;" data-options="precision:1"></td>
					</tr>
				</table>
			<input type="hidden" name="active" id="active">
			<input id="TT_TMBH" name="TT_TMBH" type="hidden">
			<input type="hidden" name="TT_TMNR" id="TT_TMNR">
		</form>
	</div>
	
	<!-- 选择题设置 -->
	<div id="xztSetDialog">
		<div class="easyui-layout" style="width:100%; height:100%;">
			<div region="north" style="height:100px;">
				<table id="table2" name="table2" style="width:100%; height:100%;" class="tablestyle" >
					<tr>
						<td class="titlestyle" style="width:150px;">题目编号</td>
						<td class="titlestyle" id="XZT_TMBH"></td>
					</tr>
					<tr>
						<td class="titlestyle">题目类型</td>
						<td class="titlestyle" id="XZT_TMLX"></td>
					</tr>
					<tr>
						<td class="titlestyle">题目内容</td>
						<td class="titlestyle" id="XZT_TMNR"></td>
					</tr>
				</table>
			</div>
			<div region="center">
				<table id="optionList" name="optionList"></table>
			</div>
		</div>
	</div>
	
	<!-- 选择题选项设置 -->
	<div id="xztxxSetDialog">
		<form id="formXZTXX" name="formXZTXX" method='post'>
			<div region="north" style="height:100px;">
				<table id="table3" name="table3" style="width:100%; height:100%;" class="tablestyle" >
					<tr>
						<td class="titlestyle">选项</td>
						<td>
							<select name="TT_XX" id="TT_XX" class="easyui-combobox" style="width:270px" panelHeight="140px" editable="false">
								<option id="" value="" >请选择</option>
								<option id="" value="A">A</option>
								<option id="" value="B">B</option>
								<option id="" value="C">C</option>
								<option id="" value="D">D</option>
								<option id="" value="E">E</option>
								<option id="" value="F">F</option>
								<option id="" value="G">G</option>
								<option id="" value="H">H</option>
								<option id="" value="I">I</option>
								<option id="" value="J">J</option>
								<option id="" value="K">K</option>
								<option id="" value="L">L</option>
								<option id="" value="M">M</option>
								<option id="" value="N">N</option>
								<option id="" value="O">O</option>
								<option id="" value="P">P</option>
								<option id="" value="Q">Q</option>
								<option id="" value="R">R</option>
								<option id="" value="S">S</option>
								<option id="" value="T">T</option>
								<option id="" value="U">U</option>
								<option id="" value="V">V</option>
								<option id="" value="W">W</option>
								<option id="" value="X">X</option>
								<option id="" value="Y">Y</option>
								<option id="" value="Z">Z</option>
							</select>
						</td>
					</tr>
					<tr>
						<td class="titlestyle">选项内容</td>
						<td>
							<textarea name="TT_XXNR" id="TT_XXNR" style="width:100%;height:70px"></textarea>
						</td>
					</tr>
				</table>
			</div>
				<input type="hidden" name="active" id="active">
				<input id="TT_TMBH1" name="TT_TMBH1" type="hidden">
				<input id="TT_XXBH" name="TT_XXBH" type="hidden">
		</form>
	</div>
</body>
<script type="text/javascript">
	var row = '';      //行数据
	var iKeyType='';//类型
	var iKeyCode = ''; //定义主键
	
	var iKeyCode1='';//获取题目编号
	
	var ikeyXX='';//定义选项
	var ikeyXXNR='';//定义选项内容
	var ikeyXXBH='';//定义选项编号
	var pageNum = 1;   //datagrid初始当前页数
	var pageSize = 20; //datagrid初始页内行数
	var TT_TMBH_CX = '';//查询条件
	var TT_TMNR_CX = '';
	var TT_TMLX_CX = ''; 
	var USERCODE="<%=MyTools.getSessionUserCode(request)%>";
	var existOption = new Array();
	var openType = '';
	
	$(document).ready(function(){
		initDialog();//初始化对话框
		loadGrid();
		
	});
	
	/**加载 dialog控件**/
	 function initDialog(){
	 
	 //最外层界面新建编辑
		$('#questionDialog').dialog({   
			width: 500,//宽度设置   
			height: 200,//高度设置 
			resizable:true,//大小
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
					doToolbar('save');
				}
			}], 
			//打开事件
			onOpen:function(data){},
			//读取事件
			onLoad:function(data){},
			//关闭事件
			onClose:function(data){
				$('#TT_TMLX').combobox('setValue', '');
				$('#table1 #TT_TMNR').val('');
			}
		});
		//选择题设置
		$('#xztSetDialog').dialog({
			title:'单/多选题选项设置', 
			width: 500,//宽度设置   
			height: 500,//高度设置 
			resizable:true,//大小
			modal:true,
			closed: true, 
			cache: false, 
			draggable:true,//是否可移动dialog框设置
			
			//打开事件
			onOpen:function(data){},
			//读取事件
			onLoad:function(data){},
			//关闭事件
			onClose:function(data){
				$('#XZT_TMBH').html('');
				$('#XZT_TMLX').html('');
				$('#XZT_TMNR').html('');
			}
		});
		
		$('#bdtSetDialog').dialog({
			title:'表单题选项设置', 
			width: 500,//宽度设置   
			height: 250,//高度设置 
			resizable:true,//大小
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
					doToolbar('savebdt');
				}
			}], 
			//打开事件
			onOpen:function(data){},
			//读取事件
			onLoad:function(data){},
			//关闭事件
			onClose:function(data){
				$('#TT_TMBH_BDSZ').html("");
				$('#TT_TMNR_BDSZ').html("");
				$('#TT_TMYD_BDSZ').val("");
				$('#TT_FZ_BDSZ').numberbox("clear");
			}
		});
		
		$('#xztxxSetDialog').dialog({
			title:'选择题选项内容设置', 
			width: 350,//宽度设置   
			height: 168,//高度设置 
			resizable:true,//大小
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
					doToolbar('saveXZTXX');
				}
			}], 
			//打开事件
			onOpen:function(data){},
			//读取事件
			onLoad:function(data){},
			//关闭事件
			onClose:function(data){
			}
		});
	}
	
	/**加载 datagrid控件，读取页面信息
		@listData 列表数据
	**/
		function loadGrid(){
				$('#formlist').datagrid({
					url:'<%=request.getContextPath()%>/Svl_Tmsz',
					queryParams:{"active":"query",
								"TT_TMBH":encodeURI($('#TT_TMBH_CX').val()),
								"TT_TMNR":encodeURI($('#TT_TMNR_CX').val()),
								"TT_TMLX":$('#TT_TMLX_CX').combobox('getValue')
					},
					title:'题目信息列表',
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
						{field:'题目编号',title:'题目编号',width:fillsize(0.25),align:'center'},
						{field:'题目内容',title:'题目内容',width:fillsize(0.9),align:'center'},
						{field:'类型名称',title:'题目类型',width:fillsize(0.25),align:'center'},
						{field:'AA_SET',align:'center',title:'操作',width:fillsize(0.25),formatter:function(value,rows){
							var str = '';
							if(rows.题目类型 == '03')
								str = "<input type='button' value='设置' style='cursor:pointer;width:76px;' onclick='openSetDialog(\"" + rows.题目类型 + "\",\""+rows.题目编号+"\",\""+rows.类型名称+"\",\""+rows.题目内容+"\",\""+rows.题目要点+"\",\""+rows.分值+"\")'>";
							else
								str = "<input type='button' value='选项设置' style='cursor:pointer;' onclick='openSetDialog(\"" + rows.题目类型 + "\",\""+rows.题目编号+"\",\""+rows.类型名称+"\",\""+rows.题目内容+"\")'>";
								
							return str;
						}
					}
					]],
					onClickRow:function(rowIndex,rowData){ 
						iKeyCode = rowData.题目编号;
						row = rowData;
						$('#questionDialog #TT_TMBH').val(iKeyCode);
						iKeyType=rowData.题目类型;
					},
					//双击某行时触发
					onDblClickRow:function(rowIndex,rowData){
							doToolbar("edit");
					},
					//成功加载时
					onLoadSuccess:function(data){//查询成功时候把后台的查询语句赋值给变量listsql
						
						iKeyCode = "";
						iKeyType=="";
						row = '';
					}
			});
		}
		
		/**加载选择题选项 datagrid控件，选择题选项列表
		@listData 列表数据
	**/
		function loadGridXZT(){
				$('#optionList').datagrid({
					url:'<%=request.getContextPath()%>/Svl_Tmsz',
					queryParams:{"active":"queryXZT",
							"TT_TMBH":encodeURI($('#XZT_TMBH').html())
					},
					title:'选项列表',
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
					toolbar:[{
						 //新建
						text:'新建',
						iconCls:'icon-add',
						handler:function(){
							//传入save值进入doToolbar方法，用于保存
							doToolbar('saveOption');
						}
					},
					{
						//编辑
						text:'编辑',
						iconCls:'icon-edit',
						handler:function(){
							//传入save值进入doToolbar方法，用于保存
							doToolbar('editOption');
						}
					},
					{
						//删除
						text:'删除',
						iconCls:'icon-cancel',
						handler:function(){
							//传入save值进入doToolbar方法，用于保存
							doToolbar('delOption');
						} 
					}],
					//下面是表单中加载显示的信息
					columns:[[
						{field:'选项',title:'选项',width:fillsize(0.4),align:'center'
                        },
						{field:'选项内容',title:'选项内容',width:fillsize(0.6),align:'center'
						}
						
					]],
					onClickRow:function(rowIndex,rowData,field){ 
						iKeyCode = rowData.题目编号;
						row = rowData;
						ikeyXX=rowData.选项;
						ikeyXXNR=rowData.选项内容;
						ikeyXXBH=rowData.选项编号;
					},
					//双击某行时触发
					onDblClickRow:function(rowIndex,rowData){
							
					},
					//成功加载时
					onLoadSuccess:function(data){//查询成功时候把后台的查询语句赋值给变量listsql
						iKeyCode = "";
						row = '';
						var optionData = data.rows;
						existOption.length = 0;
						if(optionData!=null && optionData.length>0){
							for(var i=0; i<optionData.length; i++){
								existOption.push(optionData[i].选项);
							}
						}
					}
			});
		}
		
	/**打开题目设置对话框**/
	function openSetDialog(type, id, lxmc, tmnr, tmyd, fz){
		if(type == '03'){
			$('#bdtSetDialog #TT_TMBH').val(id);
			$('#TT_TMBH_BDSZ').html(id);
			$('#TT_TMNR_BDSZ').html(tmnr);
			$('#TT_TMYD_BDSZ').val(tmyd);
			
			$('#TT_FZ_BDSZ').textbox('setValue',fz);
			$('#bdtSetDialog').dialog('open'); 
		}else{
			$('#xztSetDialog #XZT_TMBH').html(id);
			$('#XZT_TMLX').html(lxmc);
			$('#XZT_TMNR').html(tmnr);
			loadGridXZT();
			$('#xztSetDialog').dialog('open');
		}
	}
	
	/**工具栏按钮调用方法，传入按钮的id
		@id 当前按钮点击事件
	**/
	 function doToolbar(iToolbar,rec){
	 
		if(iToolbar == "query"){
			loadGrid();
		}
		
		if (iToolbar=="new"){
			
			$('#questionDialog #TT_TMBH').val('');
			
			$('#TT_TMLX').combobox('enable',true);
			$('#questionDialog #TMBH').html('系统自动生成');
			$('#questionDialog #TMBH').css('color', '#CCCCCC');
			$('#questionDialog').dialog('setTitle','新建');
			$('#questionDialog').dialog('open');
		}
		
		if(iToolbar == "edit"){
			if(iKeyCode==""){
				alertMsg("请选择一条数据");
			}else{
				$('#TT_TMLX').combobox('disable',true);
				$('#questionDialog #TMBH').html(iKeyCode);
				
				$('#questionDialog #TMBH').css('color', '#000000');
				$('#TT_TMLX').combobox('setValue', row.题目类型);
				$('#table1 #TT_TMNR').val(row.题目内容);
				$('#questionDialog').dialog('setTitle','编辑');
				$('#questionDialog').dialog('open');
				
			}
		}
		
		if(iToolbar=="del"){
			if(iKeyCode==""){
				alertMsg("请选择一条数据");
				return;
			}else{
				if(iKeyType == '03'){
					$('#active').val("del");
					ConfirmMsg("删除后无法恢复，您是否确认删除", "Del()", "");
				}
				else{
					$('#active').val("delXZT");
					ConfirmMsg("删除后无法恢复，您是否确认删除", "DelXZT()", "");
				}
			}
		}
		if (iToolbar=="save"){
			if($('#TT_TMLX').combobox('getValue')==''){
				alertMsg("请选择题目类型");
				return;
			}
			if($('#table1 #TT_TMNR').val()==""||$('#table1 #TT_TMNR').val()==null){
				alertMsg("请填写内容");
				return;
			}
			if($('#TT_TMLX').combobox('getValue')=='03'){
				$('#active').val("save");
				$('#questionDialog #TT_TMBH').html();
				
				$('#form1').submit();
				loadGrid();
				$('#bdtSetDialog').dialog('open');
				loadGrid();				
			}else{
				$('#active').val("saveXzt");
				$('#questionDialog #TT_TMBH').html();
				$('#form1').submit();
				loadGrid();
				$('#xztSetDialog').dialog('open');
				
				loadGridXZT();
			}
			
		} 
		//选择题选项里的新建
		if (iToolbar=="saveOption"){
			openType = 'new';
			$('#formXZTXX').form('clear');
			$('#xztxxSetDialog').dialog('open');
			$('#TT_XX').combobox('setValue','请选择');
			$('#TT_XX').combobox('select','');
		}
		//选择题选项里的编辑
		if (iToolbar=="editOption"){
			openType = 'edit';
			if(iKeyCode==""){
				alertMsg("请选择一条数据");
				return;
			}
			$('#TT_XXNR').val(ikeyXXNR);
			$('#TT_XX').combobox('setValue',ikeyXX);
			$('#xztxxSetDialog').dialog('open');
		} 
		//表单题选项设置里的保存
		if(iToolbar=="savebdt"){
			$('#bdtSetDialog #TT_TMBH').val($('#TT_TMBH_BDSZ').html());
		 	$('#bdtSetDialog #active').val("save");
		 	$('#bdtSetDialog #TT_TMNR').val($('#TT_TMNR_BDSZ').html());
			if($('#bdtSetDialog #TT_TMYD_BDSZ').val()==null||$('#bdtSetDialog #TT_TMYD_BDSZ').val()==""){
				alertMsg("请填写题目要点");
				return;
			}
			if($('#bdtSetDialog #TT_FZ_BDSZ').val()==null||$('#bdtSetDialog #TT_FZ_BDSZ').val()==""){
				alertMsg("请填写题目分值");
				return;
			}
			if($('#bdtSetDialog #TT_FZ_BDSZ').val()<0){
				alertMsg("分值不能小于0");
				return;
			}
			$('#formBDSZ').submit();
			loadGrid(); 
		}
		//选择题选项保存按钮	
		if(iToolbar=="saveXZTXX"){
				$('#xztxxSetDialog #active').val("saveXztxx");
				
				$('#TT_TMBH1').val($('#XZT_TMBH').html());
				
				$('#TT_XXBH').val(ikeyXXBH);
				var str1=$('#TT_XX').combobox('getValue');
			if(openType=="new"){
					$('#TT_XXBH').val("");
					
					if(str1==""||str1=="请选择"){
						alertMsg("请选择一个选项");
					}
					else{
						
						if(existOption.length==0){
							if($('#TT_XXNR').val()==""||$('#TT_XXNR').val()==null){
								alertMsg("请填写内容");
								return;
							}
								$('#formXZTXX').submit();
								openType="";
						}
						else{
							for(var i=0;i<existOption.length;i++){
								if(str1==existOption[i]){
									alertMsg("已有"+existOption[i]+"选项");
									return;
								}
								else{
										if($('#TT_XXNR').val()==""||$('#TT_XXNR').val()==null){
											alertMsg("请填写内容");
											return;
										}
											$('#formXZTXX').submit();
											openType="";
											loadGridXZT();
											return;
									}
								}
							}
					}
			}
			if(openType=="edit"){
				var str2=ikeyXX;
				
				if(str1==""||str1=="请选择"){
					alertMsg("请选择一个选项");
					return;
				}
					
				if($('#TT_XXNR').val()==""||$('#TT_XXNR').val()==null){
					alertMsg("请填写内容");
					return;
				}
				if(str1==str2){
					$('#formXZTXX').submit();
					openType="";
				}
				else{
					for(var i=0;i<existOption.length;i++){
						if(str1==existOption[i]){
							alertMsg("已有"+existOption[i]+"选项");
							return;
						}
					}
					$('#formXZTXX').submit();
					openType="";
					loadGridXZT();
				}
			}
		}
		//选择题选项删除按钮
		if(iToolbar=="delOption"){
			if(iKeyCode==""){
				alertMsg("请选择一条数据");
				return
			}else{
				$('#TT_XXBH').val(ikeyXXBH);
				ConfirmMsg("删除后无法恢复，您是否确认删除", "DelXZTXX()", "");
				
			}
		}
		
	}
	
		//编辑保存提交
		$('#form1').form({
			url:'<%=request.getContextPath()%>/Svl_Tmsz',
			onSubmit: function(){
			},
			success:function(data){
				var json  =  eval("("+data+")");
				if(json[0].MSG == '保存成功'){
					//提示信息
					showMsg(json[0].MSG);
					loadGrid();
					if($('#active').val()=="save"){
						$('#TT_TMBH_BDSZ').html(json[0].TT_TMBH);
						$('#TT_TMNR_BDSZ').html(json[0].TT_TMNR);	
					}
					if($('#active').val()=="saveXzt"){
						$('#XZT_TMBH').html(json[0].TT_TMBH);
						if(json[0].XZT_TMLX=="01"){
							$('#XZT_TMLX').html('单选题');
						}
						if(json[0].XZT_TMLX=="02"){
							$('#XZT_TMLX').html('多选题');
						}
						$('#XZT_TMNR').html(json[0].XZT_TMNR);
					}
					$('#active').val("");
					$('#questionDialog').dialog('close');
				}else{
					alertMsg(json[0].MSG);
				}
			}
		});
	
	//用来做删除 表单题提交
  		function Del(){
  			$.ajax({
  				type:"post",
  				url:'<%=request.getContextPath()%>/Svl_Tmsz',  
  				data:"active=del&TT_TMBH="+iKeyCode,   //设置模式
  				dataType:'json',
  				success:function(datas){
  					var data=datas[0];
  						showMsg(data.MSG);
  						loadGrid();
  				},
  				error:function(){
  				
  				}
  			});
  		}
  		
  		//用来做删除选择题提交
  		function DelXZT(){
  			$.ajax({
  				type:"post",
  				url:'<%=request.getContextPath()%>/Svl_Tmsz',  
  				data:"active=delXZT&TT_TMBH="+iKeyCode,   //设置模式
  				dataType:'json',
  				success:function(datas){
  					var data=datas[0];
  						showMsg(data.MSG);
  						loadGrid();
  				},
  				error:function(){
  				
  				}
  			});
  		}
  		
  		//用来做删除选择题选项提交
  		function DelXZTXX(){
  			$.ajax({
  				type:"post",
  				url:'<%=request.getContextPath()%>/Svl_Tmsz',  
  				data:"active=delXZTXX&TT_XXBH="+$('#TT_XXBH').val(),   //设置模式
  				dataType:'json',
  				success:function(datas){
  					var data=datas[0];
  						showMsg(data.MSG);
  						$('#TT_XXBH').val("");
  						loadGridXZT();
  				},
  				error:function(){
  				
  				}
  			});
  		}
  		
  		
  		//编辑保存提交 表单题
		$('#formBDSZ').form({
			url:'<%=request.getContextPath()%>/Svl_Tmsz',
			onSubmit: function(){
			},
			success:function(data){
				var json  =  eval("("+data+")");
				if(json[0].MSG == '保存成功'){
					//提示信息
					showMsg(json[0].MSG);
					loadGrid();
					$('#active').val("");
					$('#bdtSetDialog').dialog('close');
				}else{
					alertMsg(json[0].MSG);
				}
			}
		});
		//编辑保存提交 选择题选项
		$('#formXZTXX').form({
			url:'<%=request.getContextPath()%>/Svl_Tmsz',
			onSubmit: function(){
			},
			success:function(data){
				var json  =  eval("("+data+")");
				if(json[0].MSG == '保存成功'){
					//提示信息
					showMsg(json[0].MSG);
					$('#active').val("");
					$('#xztxxSetDialog').dialog('close');
					loadGridXZT();
				}else{
					alertMsg(json[0].MSG);
				}
			}
		});
</script>
</html>