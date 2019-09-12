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
	<title>教学班信息列表</title>
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
	<div id="north" region="north" title="教学班信息" style="height:91px; overflow:hidden;" >
		<table>
			<tr>
				<td><a href="#" id="new" class="easyui-linkbutton" plain="true" iconcls="icon-new" onClick="doToolbar(this.id);" title="">新建</a></td>
				<td><a href="#" id="edit" class="easyui-linkbutton" plain="true" iconcls="icon-edit" onClick="doToolbar(this.id);" title="">编辑</a></td>
				<td><a href="#" id="del" class="easyui-linkbutton" plain="true" iconcls="icon-cancel" onClick="doToolbar(this.id);" title="">删除</a></td>
			</tr>
		</table>
		<table id="ee" singleselect="true" width="100%" class="tablestyle">
			<tr>
				<td style="width:90px;" class="titlestyle">年级名称</td>
				<td>
					<select name="NJDM_CX" id="NJDM_CX" class="easyui-combobox" style="width:170px;">
					</select>
				</td>
				<td style="width:90px;" class="titlestyle">所属系部</td>
				<td>
					<select name="XBDM_CX" id="XBDM_CX" class="easyui-combobox" style="width:170px;">
					</select>
				</td>
				<td style="width:90px;" class="titlestyle">所属专业</td>
				<td>
					<select name="SSZY_CX" id="SSZY_CX" class="easyui-combobox" style="width:170px;">
					</select>
				</td>
			
			<!-- <td style="width:150px;" class="titlestyle">教学班编号</td>
				<td>
					<input name="BJBH_CX" id="BJBH_CX" class="easyui-textbox" style="width:180px;"/>
				</td> -->
				<td style="width:90px;" class="titlestyle">教学班名称</td>
				<td>
					<input name="BJMC_CX" id="BJMC_CX" class="easyui-textbox" style="width:170px;"/>
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
					<td class="titlestyle">教学班名称</td>
					<td><input name="BJMC" id="BJMC" class="easyui-textbox" style="width:200px;" min="0"  maxlength="50" >
					 </td> 
					<!--  <td class="titlestyle" id="BJMC"> 
					</td> -->
				</tr>
				<tr>
					<td class="titlestyle">教学班类型</td>
					<td>
						<select name="BJLX" id="BJLX" class="easyui-combobox" panelHeight="auto" style="width:200px;">
						</select>
					</td>
				</tr>
				<tr>
					<td class="titlestyle">年级名称</td>
					<td>
						<select name="NJDM" id="NJDM" class="easyui-combobox" panelHeight="auto" style="width:200px;">
						</select>
					</td>
				</tr>
				<tr>
					<td class="titlestyle">所属系部</td>
					<td>
						<select name="XBDM" id="XBDM" class="easyui-combobox" panelHeight="auto" style="width:200px;">
						</select>
					</td>
				</tr>
				<tr>
					<td class="titlestyle">所属专业</td>
					<td>
						<select name="SSZY" id="SSZY" class="easyui-combobox" panelHeight="auto" style="width:200px;">
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
					<td class="titlestyle">班级教室</td>
					<td onclick="openroom();">
						<select name="JSMC" id="JSMC" class="easyui-textbox" panelHeight="auto" style="width:200px;" readonly="readonly" >
						</select>
					</td>
				</tr>
				<tr>
					<td class="titlestyle">班主任</td>
					<td onClick="opentea();">
						<input name="BZR" id="BZR" class="easyui-textbox" style="width:200px;" min="0" max="1000" precision="0" readonly="readonly" >
					</td>
				</tr>
				<tr>
					<td class="titlestyle">教学班简称</td>
					<td>
						<input name="BJJC" id="BJJC" class="easyui-textbox" style="width:200px;" min="0" max="1000" precision="0">
					</td>
				</tr>
			</table>
			
			<input type="hidden" id="active" name="active"/>
			<input type="hidden" id="BJBH" name="BJBH"/>
			<input type="hidden" id="BZRGH" name="BZRGH"/>
			<input type="hidden" id="JSBH" name="JSBH"/>
		</form>
	</div>
	
	<div id="classTeaSetDialog" style="overflow:hidden;">
		<div style="width:100%; height:100%;" class="easyui-layout">
			<div region="north" style="height:32px; overflow:hidden;">
				<table width="100%" class="tablestyle">
					<tr>
						<td width="12%" class="titlestyle">教师工号</td>
						<td  width="16%">
							<input style='width:100%' class='easyui-validate' id='ic_teaId' name='ic_teaId'/>
						</td>
						<td width="12%" class="titlestyle">教师姓名</td>
						<td  width="16%">
							<input style='width:100%' class='easyui-validate' id='ic_teaName' name='ic_teaName'/>
						</td>
						<td align="center" style='width:12%'>
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
	
	<!-- 教室dialog -->
	<div id="room" style="overflow:hidden;">
			<div >
				<table id="roomtable" class = "tablestyle" width="566px" >
					<tr>
						<td>
							<a href="#" class="easyui-linkbutton" id="submit5" name="submit5" iconCls="icon-submit" plain="true" onClick="doToolbar(this.id)">确定</a>
						</td>
					</tr>

					<tr>
<!-- 						<td width="20%" align="center">校区</td> -->
<!-- 						<td width="30%" align="center"><select id="school" name="" class="easyui-combobox" style="width:98%" panelHeight="auto"></select></td> -->
						<td width="40%" align="center">教室类型</td>
						<td width="60%" align="center"><select id="clstype" name="clstype" class="easyui-combobox" style="width:70%" panelHeight="auto"></select></td>
					</tr>

				</table>
			</div>
			<div>
				<form id='fm5' method='post' style="margin: 0px;">
					<table id="clstable" class = "tablestyle" width="686px">
					</table>
				</form>
			</div>
	</div>
	
	<!-- 学生名单 -->
	<div id="xsmdDialog">
		<table>
			<tr>
				<td><a href="#" id="addStu" class="easyui-linkbutton" plain="true" iconcls="icon-add" onClick="doToolbar(this.id);" title="">添加</a></td>
				<td><a href="#" id="change" class="easyui-linkbutton" plain="true" iconcls="icon-edit" onClick="doToolbar(this.id);" title="">换班</a></td>
				<td><a href="#" id="delmore" class="easyui-linkbutton" plain="true" iconcls="icon-cancel" onClick="doToolbar(this.id);" title="">删除</a></td>
			</tr>
		</table>
		<table id="xsmdList" style="width:100%;">
		</table>
	</div>
	 
 	 <!-- 学生名单新增 -->
 	 <div id="addStuDialog">
		<div class="easyui-layout" style="width:100%; height:100%;">
			<div region="north" style="height:34px;">
				<table>
					<tr>
						<td><a href="#" id="saveAddStu" class="easyui-linkbutton" plain="true" iconcls="icon-save" onClick="doToolbar(this.id);">保存</a></td>
					</tr>
				</table>
			</div>
			<div id="center" region="center">
				<ul id="classStuTree" class="easyui-tree">
				</ul>
			</div>
		</div>
	</div>
	
 	<!-- 学生换班 -->
 	<div id="xsmdhb" style="overflow:hidden;">
 		<table id="xsmd_hb" style="width:100%;height:100%" class="tablestyle">
			<!-- <tr>
			<td class="titlestyle">现教学班编号</td>
			<td>
				<input name="xsmd_xjxbbh" id="xsmd_xjxbbh" class="easyui-textbox" style="width:200px;" >
			</td>
			</tr> 
			<tr> -->
			<tr>
			<td class="titlestyle">现教学班名称</td>
			<td style="width:200px;" class="titlestyle" id="xsmd_xjxbmc"></td>
			</tr> 
				
			<tr>
				<td class="titlestyle">现年级</td>
				<td style="width:200px;" class="titlestyle"   id="xsmd_xnj"></td>
			</tr> 
			
			<tr>
				<td class="titlestyle">现系部</td>
				<td  class="titlestyle" id="xsmd_xxb"  style="width:200px;" ></td>
			</tr> 
			
			<tr>
				<td class="titlestyle">现专业</td>
				<td class="titlestyle" id="xsmd_xzy"  style="width:200px;"></td>
			</tr> 
			
			<tr>
				<td class="titlestyle">可换的教学班名称</td>
				<td>
					<select name="xsmd_hbhjxbmc" id="xsmd_hbhjxbmc" class="easyui-combobox" style="width:200px;">
					</select>
				</td>
			</tr>
		</table>
 	</div> 
</body>
<script type="text/javascript">
	var sAuth = '<%=sAuth%>';
	var row = '';      //行数据
	var Sturow='';//学生行数据
	var iKeyCode = ''; //定义主键
	var iStuCode='';//学生名单主键
	var pageNum = 1;   //datagrid初始当前页数
	var pageSize = 20; //datagrid初始页内行数
	var NJDM_CX = '';//查询条件
	var XBDM_CX = '';
	var SSZY_CX = '';
	//var BJBH_CX = '';
	var BJMC_CX = '';
	var saveType = '';
	var bdtARRAY = new Array();//多选
	var xsxh=new Array();//学生学号
	var teaidarray =new Array();//存放教师编号
	var teaarray =new Array();//存放教师
	var teainfoidarray =new Array();//存放选择的任课教师编号
	var teainfoarray =new Array();//存放选择的任课教师姓名
	var clsinfoidarray =new Array();//存放选择的场地编号
	var clsinfoarray =new Array();//存放选择的场地名称
	var queryAuth = '<%=MyTools.getProp(request, "Base.majorClassQueAuth")%>';
	var isLoad="";
	var teabh="";//传入查询的教师编号
	var bzrgh="";//班主任工号
	var curNjdm = '';//当前年级代码
	var curXbdm = '';//当前系部代码
	var jxblx="";//教学班类型
	var rombh="";
	var rommc="";
	var jsbh="";//教室编号
	var jsmc="";//教室名称
	
	$(document).ready(function(){
		if(sAuth.indexOf(queryAuth) > -1){
			$('#new').linkbutton('disable');
			$('#edit').linkbutton('disable');
			$('#del').linkbutton('disable');
		}
		
		initDialog();//初始化对话框
		initData();//页面初始化加载数据
		});
	
	/**页面初始化加载数据**/
	function initData(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_JxbSet',
			data : 'active=initData&page=' + pageNum + '&rows=' + pageSize,
			dataType:"json",
			success : function(data) {
				loadGrid(data[0].listData);
				initCombobox(data[0].njdmData, data[0].xbdmData, data[0].sszyData, data[0].bjlxData );
			}
		});
	}
	
	/**加载combobox控件
		@jxxzData 下拉框数据
	**/
	function init(classCode,njdm,xbdm){
			$('#xsmd_hbhjxbmc').combobox({
			url:'<%=request.getContextPath()%>/Svl_JxbSet?active=xjxbmcombobox&BJBH='+classCode+'&NJDM='+njdm+'&XBDM='+xbdm+'&BJLX='+jxblx,
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
	};
	function initCombobox(njdmData, xbdmData, sszyData, bjlxData){
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

		$('#BJLX').combobox({
			data:bjlxData,
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
	
	//加载下拉框数据
	function classtypeCombobox(){
			$('#clstype').combobox({
				url:"<%=request.getContextPath()%>/Svl_Skjh?active=classtypeCombobox",
				valueField:'comboValue',
				textField:'comboName',
				editable:false,
				asy:false,
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
					loadGridCls(rombh);
				}
			});
	}
	
	/**加载 dialog控件**/
	function initDialog(){
		$('#xsmdhb').dialog({
			width: 350,//宽度设置   
			height: 250,//高度设置 
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
					doToolbar('saveChange');
				}
			}],
			onClose:function(data){
				emptyDialog();
			}
	});
	/*学生名单增加*/
	$('#addStuDialog').dialog({   
			title: '增加学生列表',   
			width: 600,//宽度设置   
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
				$('#center').html('<ul id="classStuTree" class="easyui-tree"></ul>');
			}
		});
		$('#classInfo').dialog({ 
			width: 300,//宽度设置   
			height: 268,//高度设置 
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
			width: 650,//宽度设置   
			height: 339,//高度设置 
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
			bdtARRAY.length = 0;
			loadData();
				//$('#xsmdList').datagrid('loadData',{total:0,rows:[]});
			}
		});
		
		$('#room').dialog({   
				title: '场地要求',   
				width: 580,//宽度设置   
				height: 425,//高度设置 
				modal:true,
				closed: true,   
				cache: false, 
				draggable:true,//是否可移动dialog框设置
				//读取事件
				onLoad:function(data){},
				//关闭事件
				onClose:function(data){
					clsinfoidarray.splice(0,clsinfoidarray.length);
					clsinfoarray.splice(0,clsinfoarray.length);
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
				{field:'BJBH',title:'教学班编号',width:fillsize(0.12),align:'center'},
				{field:'BJMC',title:'教学班名称',width:fillsize(0.15),align:'center'},
				{field:'BJLX',title:'教学班类型',width:fillsize(0.10),align:'center',
					formatter:function(value,rec){
						var jxblx="";
						if(rec.BJLX=="1"){
							jxblx="普通教学班";
						}else{
							jxblx="高复班";
						}
						return jxblx;	
					}
				},
				{field:'NJDM',title:'年级代码',hidden:true},
				{field:'NJMC',title:'年级名称',width:fillsize(0.1),align:'center'},
				{field:'XBDM',title:'系部代码',hidden:true},
				{field:'XBMC',title:'所属系部',width:fillsize(0.1),align:'center'},
				{field:'SSZY',title:'所属专业',hidden:true},
				{field:'ZYMC',title:'所属专业',width:fillsize(0.15),align:'center'},
				{field:'ZRS',title:'总人数',width:fillsize(0.08),align:'center'},
				{field:'JSMC',title:'班级教室',width:fillsize(0.15),align:'center'},
				{field:'BZR',title:'班主任',width:fillsize(0.08),align:'center'},
				{field:'BJJC',title:'教学班简称',width:fillsize(0.09),align:'center'},
				{field:'col',title:'操作',width:fillsize(0.12),align:'center',formatter:function(value,rec){
					return "<input type='button' value='[学生名单]' onclick='loadStuList(\"" + rec.BJBH +"\",\"" + rec.NJDM +"\",\"" + rec.XBDM +"\")' style='cursor:pointer;'>";
				}}
			]],
			onClickRow:function(rowIndex,rowData){
				//主键赋值
				iKeyCode = rowData.BJBH;
				row = rowData;
				jxblx=rowData.BJLX;
				jsbh=rowData.JSBH;
				jsmc=rowData.JSMC;
				$('#BZRGH').val(rowData.BZRGH);
			},
			//当用户双击某行时触发
			onDblClickRow:function(rowIndex, rowData){
				doToolbar('edit');
			},
			//加载成功后触发
			onLoadSuccess: function(data){
				iKeyCode = '';
				curNjdm = '';
				curXbdm = '';
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
	
	/**读取datagrid数据**///+ '&BJBH_CX=' + encodeURI(BJBH_CX) 
	function loadData(){
		$.ajax({
			type:"POST",
			url:'<%=request.getContextPath()%>/Svl_JxbSet',
			data:'active=query&page=' + pageNum + '&rows=' + pageSize + '&BJMC_CX=' + encodeURI(BJMC_CX) + 
				'&NJDM_CX=' + NJDM_CX + '&XBDM_CX=' + XBDM_CX + '&SSZY_CX=' + SSZY_CX,
			dataType:"json",
			success:function(data) {
				loadGrid(data[0].listData);
			}
		});
	}
	
	//查询学生名单
  	function loadStuList(classCode, njdm, xbdm){
  		curNjdm = njdm;
  		curXbdm = xbdm;
  		$('#xsmdDialog').dialog('open');
  		init(classCode,njdm,xbdm);
  		$('#xsmdList').datagrid({
  			url:'<%=request.getContextPath()%>/Svl_JxbSet',
			queryParams:{"active":"loadStuList","BJBH":classCode},
			title:'',
			width:'100%',
			nowrap: false,
			fit:true, //自适应父节点宽度和高度
			showFooter:true,
			striped:true,
			pagination:true,
			pageSize:50,
			singleSelect:false,
			pageNumber:1,
			rownumbers:true,
			fitColumns: true,
			columns:[[
				{field:'ckt',checkbox:true},
				{field:'学号',title:'学号',width:fillsize(0.25),align:'center'},
				{field:'姓名',title:'姓名',width:fillsize(0.25),align:'center'},
				{field:'专业名称',title:'专业名称',width:fillsize(0.25),align:'center'},
				{field:'招生类别',title:'招生类别',width:fillsize(0.25),align:'center'}
			]],
			onSelect:function(rowIndex,rowData){
				bdtARRAY.push(rowData.学号);
			},
			//取消勾选时触发
			onUnselect:function(rowIndex,rowData){
				 $.each(bdtARRAY, function(key,value){
					if(value == rowData.学号){
						bdtARRAY.splice(key, 1);
					}
				}); 
			},
			//选择全部时触发
			onSelectAll:function(rows){
				 bdtARRAY.length = 0;
				for(var i=0; i<rows.length; i++){
					bdtARRAY.push(rows[i].学号);
				} 
			},
			//取消选择全部时触发
			onUnselectAll:function(rows){
				bdtARRAY.length = 0;
			},
			onClickRow:function(rowIndex,rowData){
				//主键赋值
				iStuCode = rowData.BJBH;
				Sturow = rowData;
			},
			//加载成功后触发
			onLoadSuccess: function(data){
				iStuCode = '';
			}
		});
  	}
  	
 	/**学生名单新增**/
	function addXSXZ(stuInfo){
		$.ajax({
			type:"POST",
			url:'<%=request.getContextPath()%>/Svl_JxbSet',
			data:'active=add&XSMDXZXH='+stuInfo+'&xjbbd='+row.BJBH,  
			dataType:"json",
			success:function(data) {
				if(data[0].MSG == '增加成功'){
					showMsg(data[0].MSG);
					$('#addStuDialog').dialog('close');
					loadStuList(row.BJBH, curNjdm, curXbdm);
				}else{
					showMsg(data[0].MSG);
				}
			}
		}); 
	}
	
	/**学生名单换班**/
	function changeStudent(bdtARRAY){
		$.ajax({
			type:"POST",
			url:'<%=request.getContextPath()%>/Svl_JxbSet',
			 data:'active=change&BJBH='+row.BJBH +
			 '&HBH='+$('#xsmd_hbhjxbmc').textbox('getValue')+
			'&bdtARRAY='+bdtARRAY,  
			dataType:"json",
			success:function(data) {
			if(data[0].MSG == '换班成功'){
					showMsg(data[0].MSG);
					$('#xsmdhb').dialog('close');
					loadStuList(row.BJBH, curNjdm, curXbdm);
					}else{
					showMsg(data[0].MSG);
				}
				
			}
		});
	}
	/**加载班级学生信息TREE**/
	var checkZyFlag = false;//用于判断是否勾选专业
	var childNum = 0;
	var loadNum = 0;
	var checkBjFlag = false;//用于判断是否勾选班级
	var checkObj = '';
	function loadClassStuTree(){
		$('#classStuTree').tree({
			checkbox: true,
			url:'<%=request.getContextPath()%>/Svl_JxbSet?active=queClassStuTree&sAuth=' + sAuth + '&NJDM=' + curNjdm + '&XBDM=' + curXbdm+'&BJLX='+jxblx,
			onCheck:function(node){
			var checkType = '';
				loadNum = 0;
				childNum = 0;
				
				if(node.id.indexOf('zy') > -1)
					checkType = 'zy';
				else if(node.id.indexOf('bj') > -1)
					checkType = 'bj';
				else 
					checkType = 'xs';
				if(node.checked==false && checkType=='zy'){
					checkZyFlag = true;
					checkObj = node.target;
					$(this).tree('expand', checkObj);
					
					var childrenObj = $('#classStuTree').tree('getChildren', node.target);
					if(childrenObj.length > 0){
						loadNum++;
						childNum = 1;
						
						for(var i=0; i<childrenObj.length; i++){
				    		if(childrenObj[i].id.substring(0, 2)=='bj' && childrenObj[i].state=='closed'){
								$(this).tree('expand', childrenObj[i].target);
								childNum++;
							}
		    			}
		    			//childNum = childrenObj.length+1;
					}
				}
				if(node.checked==false && checkType=='bj'){
					if(node.state == 'closed'){
						checkBjFlag = true;
						checkObj = node.target;
						$(this).tree('expand', checkObj);
					}
				}
			},
		    onBeforeExpand:function(node){
		   $('#classStuTree').tree('options').url="<%=request.getContextPath()%>/Svl_JxbSet?active=queClassStuTree&classCode=" + iKeyCode + "&parentCode=" + node.id;
			},
			onLoadSuccess:function(node, data){
				if(checkZyFlag == true){
					loadNum++;
					
					if(loadNum == 1){
						childNum = 1;
						var childrenObj = $('#classStuTree').tree('getChildren', node.target);
						for(var i=0; i<childrenObj.length; i++){
							if(childrenObj[i].id.substring(0, 2)=='bj' && childrenObj[i].state=='closed'){
								$(this).tree('expand', childrenObj[i].target);
								childNum++;
							}
		    			}
		    			//childNum = childrenObj.length+1;
					}
					
					if(loadNum == childNum){
						$(this).tree('check', checkObj);
						checkObj = '';
						checkZyFlag = false;
					}
				}
				
				if(checkBjFlag == true){
					$(this).tree('check', checkObj);
					checkObj = '';
					checkBjFlag = false;
				}
			}
		});
	};
	
	/**工具栏按钮调用方法，传入按钮的id
		@id 当前按钮点击事件
	**/
	function doToolbar(id){
		//新增学生
		if(id == 'addStu'){
			loadClassStuTree();
			$('#addStuDialog').dialog('open');
			document.getElementById('center').scrollTop = 0;
		}
		if(id == 'saveAddStu'){
			var totalData = $('#classStuTree').tree('getChecked');
			var stuInfo = '';
			
			for(var i=0; i<totalData.length; i++){
				if(totalData[i].id.indexOf('-') < 0){
					stuInfo += totalData[i].id+','+totalData[i].text+',';
				}
			}
			if(stuInfo.length == 0){
				alertMsg('请至少选择一名学生');
				return;
			}
			
			stuInfo = stuInfo.substring(0, stuInfo.length-1);
			addXSXZ(stuInfo);
		}
	//学生换班
	if(id=='change'){
	if(bdtARRAY == ''){
				alertMsg('请选择一行数据');
				return;
			}
		$('#xsmdhb').dialog({   
				title: '换班'
			});
			//打开dialog
			$('#xsmdhb').dialog('open');
			$('#stuCode').html(row.BJMC);
			$('#xsmd_xjxbmc').html(row.BJMC);
			$('#xsmd_xnj').html(row.NJDM);
			$('#xsmd_xxb').html(row.XBMC);
			$('#xsmd_xzy').html(row.ZYMC);
	}
	if(id=='saveChange'){
		changeStudent(bdtARRAY);
	}
	//查询
		if(id == 'query'){
			NJDM_CX = $('#NJDM_CX').combobox('getValue');
			XBDM_CX = $('#XBDM_CX').combobox('getValue');
			SSZY_CX = $('#SSZY_CX').combobox('getValue');
			//BJBH_CX = $('#BJBH_CX').textbox('getValue'); 
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
			$('#BJMC').val('');
			$('#classInfo').dialog('open');
			
		}

		//判断获取参数为edit，执行编辑操作
		if(id == 'edit'){
			if(iKeyCode == ''){
				alertMsg('请选择一行数据');
				return;
			}
			
			teabh=row.BZRGH;
			teainfoidarray.push(row.BZRGH);
			teainfoarray.push(row.BZR);
			
			//$('#BJBH').textbox('readonly', true);
			$('#NJDM').combobox('disable');
			
			// $('#SSZY').combobox('disable'); 
						
			//打开dialog
			$('#classInfo').dialog({   
				title: '编辑班级信息'   
			});
			if(row!=undefined && row!=''){
				$('#form1').form('load', row);
			}
			$('#JSMC').textbox('setValue',jsmc);
			saveType = 'edit';
			$('#classInfo').dialog('open');
		}
		
		if(id == 'del'){
			if(iKeyCode == ''){
				alertMsg('请选择一行数据');
				return;
			}
			ConfirmMsg('是否确定要删除当前选择的班级,并且该班级的学生也会被删除', 'delClass();', '');
		}
		if(id == 'delmore'){
			if(bdtARRAY == ''){
			alertMsg('请选择一行数据');
				return;
			}
			ConfirmMsg('是否确定要删除当前选择的学生', 'delStu(bdtARRAY);', '');
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

			if($('#BJMC').textbox('getValue') == ''){
				alertMsg('请输入班级名称');
				return;
			}
			if($('#BJLX').combobox('getValue') == ''){
				alertMsg('请选择教学班类型');
				return;
			}
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
			if($('#JSMC').textbox('getValue') == ''){
				alertMsg('请选择班级教室');
				return;
			}
			
			$('#active').val(saveType);
			$("#form1").submit();
		}
		
		if(id == "submit5"){//场地			
				var roms="";
				var html="";

				for (var i = 0;i < clsinfoidarray.length;i++) {
					if(html==""){
						roms+=clsinfoidarray[i];
						html+=clsinfoarray[i];
					}else{
						roms+="+"+clsinfoidarray[i];
						html+="+"+clsinfoarray[i];
					}
				}
	
				clsinfoidarray.splice(0,clsinfoidarray.length);
				clsinfoarray.splice(0,clsinfoarray.length);
				
				jsbh=roms;
				jsmc=html;
				$('#JSBH').val(roms);
				$('#JSMC').textbox('setValue',html);

				$('#room').dialog("close");
		}
	}
	
	//删除教室
	function delClass(){
	$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_JxbSet',
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
	//删除学生
	function delStu(bdtARRAY){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_JxbSet',
			data : 'active=delmore&bHarray=' + bdtARRAY+'&jxbbh='+row.BJBH,
			dataType:"json",
			success : function(data){
				if(data[0].MSG == '删除成功'){
					showMsg(data[0].MSG);
					loadStuList(row.BJBH, curNjdm, curXbdm);
					bdtARRAY.length=0;
				}else{
					alertMsg(data[0].MSG);
				}
			}
		});
	}
	
	/**表单提交**/
	$('#form1').form({
		//定位到servlet位置的url
		url:'<%=request.getContextPath()%>/Svl_JxbSet',
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
		teabh="";
		teainfoidarray.splice(0,teainfoidarray.length);
		teainfoarray.splice(0,teainfoarray.length);
		
		//$('#BJBH').textbox('readonly', false);
		//$('#BJBH').textbox('setValue', '');
		$('#BJMC').textbox('setValue','');
		$('#NJDM').combobox('enable');
		$('#NJDM').combobox('setValue', '');
		$('#XBDM').combobox('enable');
		$('#XBDM').combobox('setValue', '');
		$('#SSZY').combobox('enable');
		$('#SSZY').combobox('setValue', '');
		$('#BZR').textbox('setValue', '');
		$('#BJJC').textbox('setValue', '');
		$('#BJMC_CX').textbox('setValue', '');
		$('#BJLX').combobox('setValue', '');
		$('#JSMC').textbox('setValue','');
	}
	
	//打开teacher编辑窗口
	function opentea(){
		$('#ic_teaId').val("");
		$('#ic_teaName').val("");
		
		teabh = teainfoidarray[0];
		loadGridTea();
		$('#classTeaSetDialog').dialog("open");		
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
	
	//保存选择的任课教师信息
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
	
	//打开room编辑窗口
	var rommc="";
	function openroom(){   	
			//初始化
			//$('#chooseroom').attr("checked",false);
			//$('#choosetype').attr("checked",false);
			//$('#normalroom').numberbox('setValue','');
			//$('#meidaroom').numberbox('setValue','');
			//$('#normalroom').numberbox({ disabled: true });
			//$('#meidaroom').numberbox({ disabled: true });
			//$('#school').combobox('setValue','');
			//$('#house').combobox('setValue','');
			$('#clstype').combobox('setValue','');

			//填充数据
			rombh=jsbh; 
			rommc=jsmc;
			if(rombh!=undefined){
				clsinfoidarray.push(rombh);
				clsinfoarray.push(rommc);				
			}   
			loadGridCls(rombh);
            //schoolCombobox();
			//houseCombobox();
			classtypeCombobox();
	
			$('#room').show();		
			$('#room').dialog("open");
	}
	
	
	function loadGridCls(rombh){  
			isLoad = true;
			$('#clstable').datagrid({
				url: '<%=request.getContextPath()%>/Svl_Skjh',
	 			queryParams: {"active":"openRoomXX","seltype":$('#clstype').combobox('getValue'),"XBDM":$('#XBDM').combobox('getValue'),"roomarr":rombh},
				loadMsg : "信息加载中请稍后!",//载入时信息
				width:566,
				height:336,
				rownumbers: true,
				animate:true,
				striped : true,//隔行变色
				pageSize : pageSize,//每页记录数
				singleSelect : true,//单选模式
				pageNumber : pageNum,//当前页码
				pagination:false,
				fit:false,
				fitColumns: true,//设置边距
				columns:[[
					{field:'ckr',checkbox:true},
					{field:'教室编号',title:'教室编号',width:60,align:'center'},
					{field:'教室名称',title:'教室名称',width:80,align:'center'},
					{field:'名称',title:'教室类型',width:60,align:'center'},
					{field:'实际容量',title:'教室容量（人）',width:60,align:'center'}
				]],
				onSelect:function(rowIndex,rowData){ 
					if(isLoad == false){
						clsinfoidarray.splice(0, clsinfoidarray.length);
						clsinfoarray.splice(0, clsinfoarray.length);
						clsinfoidarray.push(rowData.教室编号);
						clsinfoarray.push(rowData.教室名称);
					}
				},
				onUnselect:function(rowIndex,rowData){
						$.each(clsinfoidarray, function(key,value){
							if(value == rowData.教室编号){
								clsinfoidarray.splice(key, 1);
								clsinfoarray.splice(key, 1);
							}
						});
				},
				onClickRow: function(rowIndex, rowData){
			        //加载完毕后获取所有的checkbox遍历
			        $("input[name='ckr']").each(function(index, el){
			            //如果当前的复选框不可选，则不让其选中
			            if (el.disabled == true) {
			                $('#clstable').datagrid('unselectRow', index-2);
			            }
			        })
			    },
				onLoadSuccess: function(data){
					$(".datagrid-header-check").html('&nbsp;');//隐藏全选
					//勾选已选教室
					if(data){
						if(rombh!=undefined){
							var selromid=rombh.split("+");
							$.each(data.rows, function(rowIndex, rowData){
								for(var i=0; i<selromid.length; i++){
									if(selromid[i] == rowData.教室编号){
										$('#clstable').datagrid('selectRow', rowIndex);
									}
								}
							});
						}
					}
// 					clsroomlen=data.rows.length;	
// 					if(roomsel!=2){
// 						for (var i = 0; i < clsroomlen; i++) { 
// 							$('#clstable').datagrid('unselectRow', i);
// 						}
// 					}
												
					isLoad = false;
				},
				onLoadError:function(none){
					
				}
			});
	}
		
	//保存选择的任课教师信息
	function editClsInfo(id){
			var checkbox = document.getElementById(id);
			if(checkbox.checked){//勾选
				clsinfoidarray.push(checkbox.id);
				clsinfoarray.push(checkbox.hidename);
			}else{//不勾选
				for(var i=0;i<clsinfoidarray.length;i++){
					if(checkbox.id==clsinfoidarray[i]){
						clsinfoidarray.splice(i,1);
						clsinfoarray.splice(i,1);
					}
				}
			}
	}
	
</script>
</html>