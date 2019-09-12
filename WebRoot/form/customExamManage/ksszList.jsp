<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="java.util.Vector"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.src.develop.store.user.*"%>
<%
	/**
		创建人：zhaixuchao
		Create date: 2017-06-20
		功能说明：考试设置
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
	<title>考试设置</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> 
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/icon.css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/publicScript.js"></script>
	<style>
		.maskStyle{
			width:100%;
			height:100%;
			overflow:hidden;
			display:none;
			background-color:#D2E0F2;
			filter:alpha(opacity=80);
			position:absolute;
			top:0px;
			left:0px;
			z-index:100;
		}
		#maskFontPL{
			 font-size:16px;
			 font-weight:bold;
			 color:#2B2B2B;
			 width:200px;
			 height:100%;
			 position:absolute;
			 top:50%;
			 left:50%;
			 margin-top:-10px;
			 margin-left:-80px;
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
    <div id="north"  region="north" title="考试设置" style="height:91px;" >
		<table>
			<tr>
				<td><a href="#" id="new" class="easyui-linkbutton" plain="true" iconcls="icon-add" onClick="doToolbar(this.id);" title="">新建</a></td>
				<td><a href="#" id="edit" class="easyui-linkbutton" plain="true" iconcls="icon-edit" onClick="doToolbar(this.id);" title="">编辑</a></td>
				<td><a href="#" id="del" class="easyui-linkbutton" plain="true" iconcls="icon-cancel" onClick="doToolbar(this.id);" title="">删除</a></td>
				<td><a href="#" id="yksz" class="easyui-linkbutton" plain="true" iconcls="icon-tip" onClick="doToolbar(this.id);" title="">月考设置</a></td>
			</tr>
		</table>
		<table id="ee" singleselect="true" width="100%" class="tablestyle">
			<tr>
				<td  class="titlestyle">学年</td>
				<td>
					<select name="KS_XN_CX" id="KS_XN_CX" class="easyui-combobox" style="width:150px;">
					</select>
				</td>
				<td class="titlestyle">学期</td>
				<td>
					<select name="KS_XQ_CX" id="KS_XQ_CX" class="easyui-combobox" style="width:150px;">
					</select>
				</td>
				<td class="titlestyle">考试名称</td>
				<td>
					<input name="KS_KSMC_CX" id="KS_KSMC_CX" style="width:150px;" class="easyui-textbox"></input>		
				</td>
				<td class="titlestyle">考试类别</td>
				<td>
					<select name="KS_KSLB_CX" id="KS_KSLB_CX" class="easyui-combobox" style="width:150px;">
					</select>
				</td>
   				<td class="titlestyle" style="width:150px; text-align:center;">
					<a href="#" onclick="doToolbar(this.id)" id="search" class="easyui-linkbutton" iconcls="icon-search" plain="true">查询</a>
				</td>		
			</tr>
	    </table>
	</div>
	<div region="center">
		<table id="formlist" name="formlist" width="100%">
		</table>
	</div>
	
	<!-- 考试新建编辑 -->
	<div id="examInfoDialog" style="background-color:#EFEFEF;overflow:hidden;">
		<form id="form1" name="form1" method='post'>
			<table class="tablestyle">
				<tr>
					<td class="titlestyle" style="width:90px;">
						学年学期<!-- <font style="color:red;">&nbsp;*</font> -->
					</td>
					<td>
						<select name="ZK_XNXQBM" id="ZK_XNXQBM" class="easyui-combobox" style="width:190px;">
						</select>
					</td>
				</tr>
				<tr>
					<td class="titlestyle">
						考试名称<!-- <font style="color:red;">&nbsp;*</font> -->
					</td>
					<td>
						<input name="ZK_KSMC" id="ZK_KSMC" class="easyui-textbox" style="width:190px;"/>
					</td>
				</tr>
				<tr>
					<td class="titlestyle">
						考试类别<!-- <font style="color:red;">&nbsp;*</font> -->
					</td>
					<td>
						<select name="ZK_KSLBXJ" id="ZK_KSLBXJ" class="easyui-combobox" style="width:190px;">
						</select>
					</td>
				</tr>
				<tr>
					<td class="titlestyle">
						学生类别<!-- <font style="color:red;">&nbsp;*</font> -->
					</td>
					<td>
						<select name="ZK_ZSLB" id="ZK_ZSLB" class="easyui-combobox" style="width:190px;" multiple="true" disabled="true">
						</select>
					</td>
				</tr>
				<tr id="djkxslbTR" style="display:none;">
					<td class="titlestyle">
						等级考学生类别<!-- <font style="color:red;">&nbsp;*</font> -->
					</td>
					<td style="height: 100%">
						<select name="ZK_DJKXSLBXJ" id="ZK_DJKXSLBXJ" class="easyui-combobox" style="width:190px;" multiple="true" >
						</select>
					</td>
				</tr>
				<tr>
				<td class="titlestyle">登分开始日期</td>
					<td>
						<input style="width:190px;" class="easyui-datebox" id="ZK_DFKSSJ" name="ZK_DFKSSJ" editable="false"/>
					</td>
				</tr>
				<tr>
					<td class="titlestyle">登分结束日期</td>
					<td>
						<input style="width:190px;" class="easyui-datebox" id="ZK_DFJSSJ" name="ZK_DFJSSJ" editable="false"/>
					</td>
				</tr>
			</table>
			
			<input type="hidden" id="active" name="active"/>
			<input type="hidden" id="ZK_KSBH" name="ZK_KSBH"/>
			<input type="hidden" id="ZZZBH" name="ZZZBH"/><!-- 自动增长编号 -->
			<input type="hidden" id="ZT" name="ZT"/><!-- 状态 -->
			
			<input type="hidden" id="YKSZ_BH" name="YKSZ_BH"/><!-- 月考编号  -->
			<input type="hidden" id="YKSZ_KSMC" name="YKSZ_KSMC"/><!-- 月考考试名称  -->
			<input type="hidden" id="YKSZ_BJ" name="YKSZ_BJ"/><!-- 月考班级  -->
			<input type="hidden" id="YKSZ_KCMC" name="YKSZ_KCMC"/><!-- 月考课程名称  -->
			<input type="hidden" id="YKSZ_CJSJ" name="YKSZ_CJSJ"/><!-- 月考创建时间  -->
			
			<input type="hidden" id="ZK_BJBH" name="ZK_BJBH"/><!-- 考试设置班级编号 -->
			<input type="hidden" id="xkInfo" name="xkInfo"/><!-- 考试设置修改的学科 -->
			
			<input type="hidden" id="plnjInfo" name="plnjInfo"/><!-- 考试学科批量设置保存里的年级 -->
			<input type="hidden" id="plzyInfo" name="plzyInfo"/><!-- 考试学科批量设置保存里的专业 -->
			<input type="hidden" id="plkcInfo" name="plkcInfo"/><!-- 考试学科批量设置保存里的课程 -->
			
			<input type="hidden" id="ZK_ZSLBXJ" name="ZK_ZSLBXJ"/><!-- 招生类别 -->
			<input type="hidden" id="ZK_DJKXSLB" name="ZK_DJKXSLB"/><!-- 等级考学生类别 -->
		</form>
	</div>
	
	<!-- 月考设置dialog -->
	<div id="ykszSetDialog">
		<div class="easyui-layout" style="width:100%; height:100%;">
			<div region="north" style="overflow:hidden;width:100%; height:64px;">
				<table>
					<tr>
						<td><a href="#" id="saveOptionyk" class="easyui-linkbutton" plain="true" iconcls="icon-add" onClick="doToolbar(this.id);" title="">新建</a></td>
						<td><a href="#" id="editOptionyk" class="easyui-linkbutton" plain="true" iconcls="icon-edit" onClick="doToolbar(this.id);" title="">编辑</a></td>
						<td><a href="#" id="delOptionyk" class="easyui-linkbutton" plain="true" iconcls="icon-cancel" onClick="doToolbar(this.id);" title="">删除</a></td>
					</tr>
				</table>
				<table id="ee2" class="tablestyle">
					<tr>
						<td  class="titlestyle" style="width:100px">考试名称</td>
						<td style="width:365px">
							<input name="YK_KSMC" id="YK_KSMC" class="easyui-textbox" style="width:180px;">
						</td>
						<td style="width:100px;" align="center">
							<a href="#" onclick="doToolbar(this.id)" id="chaxun" class="easyui-linkbutton" plain="true" align="right" iconcls="icon-search">查询</a>
						</td>				
					</tr>
			    </table>
			</div>
			<div region="center">
				<table id = "optionList_YK"  name= "optionList_YK"  width = "100%">
				</table>
			</div>
		</div>
	</div>
	
	<!-- 月考信息新建编辑 -->
	<div id="monthexamDialog" style="overflow:hidden;">
		<table class="tablestyle" >
			<tr>
				<td class="titlestyle" style="width:90px;">
					考试名称<!-- <font style="color:red;">&nbsp;*</font> -->
				</td>
				<td>
					<input name="YKBJ_KSMC" id="YKBJ_KSMC" class="easyui-textbox" style="width:190px;"/>
				</td>
			</tr>
			<tr>
				<td class="titlestyle">
					年级<!-- <font style="color:red;">&nbsp;*</font> -->
				</td>
				<td>
					<select name="YKBJ_BJ" id="YKBJ_BJ" class="easyui-combobox" style="width:190px;" multiple="true">
					</select>
				</td>
			</tr>
			<tr>
				<td class="titlestyle">
					课程名称<!-- <font style="color:red;">&nbsp;*</font> -->
				</td>
				<td>
					<select name="YKBJ_KCMC" id="YKBJ_KCMC" class="easyui-combobox" style="width:190px;" multiple="true">
					</select>
				</td>
			</tr>
			
			<tr>
				<td class="titlestyle">
					等级考学生类别<!-- <font style="color:red;">&nbsp;*</font> -->
				</td>
				<td>
					<select name="ZK_DJKXSLBYK" id="ZK_DJKXSLBYK" class="easyui-combobox" style="width:190px;" multiple="true" >
					</select>
				</td>
			</tr>
			<tr>
				<td class="titlestyle">
					每月创建时间<!-- <font style="color:red;">&nbsp;*</font> -->
				</td>
				<td>
					<input name="YKBJ_MYCJSJ" id="YKBJ_MYCJSJ" class="easyui-numberbox" style="width:190px;" max="31"/>
				</td>
			</tr>
			<tr>
				<td colspan="2" style="text-align:center;">如果当月没有31号 , 默认为当月最后一天</td>
			</tr>
		</table>
	</div>
	
	
	<!-- 考试学科设置 dialog -->
	<div id="xkSetDialog" style="overflow:hidden;">
		<%-- 遮罩层 --%>
    	<div id="divPageMask" class="maskStyle">
    		<div id="maskFont"></div>
    	</div>
		<div id="cc" class="easyui-layout" style="width:100%; height:100%;">
			<div id="north" region="north" style="height:36px; overflow:hidden;">
				<table style="height: 20px;">
					<tr>
						<td><a href="#" onclick="doToolbar(this.id);" id="saveXK" class="easyui-linkbutton" plain="true" iconCls="icon-save">保存</a></td>
						<td><a href="#" onclick="doToolbar(this.id);" id="saveXKPLSZ" class="easyui-linkbutton" plain="true" iconCls="icon-edit">批量设置</a></td>
					</tr>
				</table>
			</div> 
			<div id="west" region="west" style="width:200px;" title="班级列表">
				<div id="tree"> 
					<ul id="classTree" class="easyui-tree">
					</ul>
				</div>
			</div>   
			<div region="center" title="学科列表">
				<div id="mask" style="width:100%; height:100%; position:absolute; top:0; z-index:99; background:url(0);" onclick="alertMsg('请先选择班级');"></div>
				<table id="subjectList"  name="subjectList"></table>
			</div> 
		</div>
	</div>
	
	<!-- 考试学科批量设置dialog -->
	<div id="xkplDialog">
	<%-- 遮罩层 --%>
    	<div id="divPageMaskPL" class="maskStyle">
    		<div id="maskFontPL"></div>
    	</div>
		<table  class="tablestyle">
			<tr>
				<td class="titlestyle" style="width:80px;">年级</td>
				<td>
					<select name="PL_NJ" id="PL_NJ" class="easyui-combobox" style="width:180px;" panelHeight="auto" multiple="true" editable="false">
					</select>
				</td>
			</tr>
			<tr>
				<td class="titlestyle">系部</td>
				<td>
					<select name="PL_ZY" id="PL_ZY" class="easyui-combobox" style="width:180px;" panelHeight="auto" multiple="true" editable="false">
					</select>
				</td>
			</tr>
			<tr>
				<td class="titlestyle">课程名称</td>
				<td>
					<select name="PL_KCMC" id="PL_KCMC" class="easyui-combobox" style="width:180px;" panelHeight="auto" multiple="true" editable="false">
					</select>
				</td>
			</tr>
		</table>
	</div>
  </body>
  <script type="text/javascript">
  
  var nianji=[{"comboName":"全部","comboValue":"all"},{"comboName":"一年级","comboValue":"1"},{"comboName":"二年级","comboValue":"2"},{"comboName":"三年级","comboValue":"3"}];
  	var sAuth = '<%=sAuth%>';
  	var authType = '<%=MyTools.StrFiltr(request.getParameter("authType"))%>';
  	var USERCODE="<%=MyTools.getSessionUserCode(request)%>";
	var pageNum = 1;   //datagrid初始当前页数
	var pageSize = 20; //datagrid初始页内行数
	var curXn = '';
	var curXq = '';
	var saveType = '';
	var iKeyCode = ''; //定义主键
	var row = '';      //行数据
	var ykiKeyCode='';//月考主键
	var ykrow='';//月考行数据
	
	var curSelClass = '';
  	var cpBJDM='';
  	var cpBJMC='';
  	
  	var yksaveType='';
  	
  	var lastIndex = 0;
  	
  	var zslbsum = '';
  	
  	var zslbdm = '';
  	
  	//var djkxslbdm='';//等级考学生类别代码
	
	
	$(document).ready(function(){
		initDialog();//初始化对话框
		initData();
		
		//设置输入框限制长度
		setInputMaxLen('ZK_KSMC', 100);
		
		setInputMaxLen('YKBJ_KSMC', 100);
	});
	
	
	/**页面初始化加载数据**/
	function initData(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Kssz',
			data : 'active=initData&page=' + pageNum + '&rows=' + pageSize,
			dataType:"json",
			success : function(data) {
				if(data[0].curXnxq != ''){
					curXn = data[0].curXnxq.substring(0, 4);
					curXq = data[0].curXnxq.substring(4, 5);
				}
				zslbsum = data[0].zslbsum;
				initCombobox(data[0].xnData, data[0].xqData, data[0].lbData, data[0].xnxqData, data[0].zslbData);
				loadGrid(data[0].listData);
				loadSubjectList();
			}
		});
	}
	
	/**加载 dialog控件**/
	function initDialog(){
		$('#examInfoDialog').dialog({   
			width: 310,//宽度设置   
			height: 243,//高度设置  119
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
			}
		});
		
		//月考设置dialog
		$('#ykszSetDialog').dialog({   
			title:'月考考试设置',
			width: 596,//宽度设置   
			height: 500,//高度设置 
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
			}
		});
		
		//月考新建编辑dialog
		$('#monthexamDialog').dialog({   
			width: 310,//宽度设置   
			height: 218,//高度设置 
			modal:true,
			closed: true,   
			cache: false, 
			draggable:true,//是否可移动dialog框设置
			toolbar:[{
				//保存编辑
				text:"保存",
				iconCls:'icon-save',
				handler:function(){
					//传入save值进入doToolbar方法，用于保存
					doToolbar('saveYKXX');
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
		//考试学科dialog
		$('#xkSetDialog').dialog({   
			width: 800,//宽度设置   
			height: 500,//高度设置 
			modal:true,
			closed: true,   
			cache: false, 
			draggable:false,//是否可移动dialog框设置
			//打开事件
			onOpen:function(data){},
			//读取事件
			onLoad:function(data){},
			//关闭事件
			onClose:function(data){
				$('#west').html('<ul id="classTree" class="easyui-tree"></ul>');
				$('#subjectList').datagrid('reload');
				curSelClass = '';
				$('#mask').show();
			}
		});
		//考试学科批量设置
		$('#xkplDialog').dialog({   
			title:'批量设置',
			width: 290,//宽度设置   
			height: 143,//高度设置 
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
					doToolbar('saveXKPL');
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
	
	/**加载 datagrid控件，读取最外层页面信息
		@listData 列表数据
	**/
	function loadGrid(listData){
		$('#formlist').datagrid({
			data:listData,
			title:'考试列表',
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
				{field:'学年学期名称',title:'学年学期',width:fillsize(0.2),align:'center'},
				{field:'考试名称',title:'考试名称',width:fillsize(0.35),align:'center'},
				{field:'类别名称',title:'考试类别',width:fillsize(0.1),align:'center'},
				{field:'登分开始时间',title:'登分开始时间',width:fillsize(0.1),align:'center'},
				{field:'登分结束时间',title:'登分结束时间',width:fillsize(0.1),align:'center'},
				{field:'AA_SET',align:'center',title:'操作',width:fillsize(0.15),formatter:function(value,rec){
					var str = '';
					str = "<input type='button' value='[学科设置]' onclick='openSubjectList(\"" + rec.学年学期编码   + "\",\"" + rec.招生类别 +"\",\"" + rec.等级考学生类别 +"\",\"" + rec.考试名称 + "\")' id='openSubjectList' style='cursor:pointer;'>";
					return str;
				}}
			]],
			onClickRow:function(rowIndex,rowData){ 
				iKeyCode = rowData.考试编号;
				row = rowData;
			},
			//双击某行时触发
			onDblClickRow:function(rowIndex,rowData){
				doToolbar("edit");
			},
			//成功加载时
			onLoadSuccess:function(data){//查询成功时候把后台的查询语句赋值给变量listsql
				iKeyCode = "";
				row = ''; 
			}
		});
	}
	
	/**加载combobox控件
		@jxxzData 下拉框数据
	**/
	function initCombobox(xnData, xqData, lbData, xnxqData, zslbData){
		$('#KS_XN_CX').combobox({
			data:xnData,
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			panelHeight:'140', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data != ''){
					if(curXn != ''){
						$(this).combobox('setValue', curXn);
					}else{
						//初始化combobox时赋值
						$(this).combobox('setValue', '');
					}
				}
			},
			//下拉列表值改变事件
			onChange:function(data){}
		});
		
		$('#KS_XQ_CX').combobox({
			data:xqData,
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			panelHeight:'auto', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data != ''){
					if(curXq != ''){
						$(this).combobox('setValue', curXq);
					}else{
						//初始化combobox时赋值
						$(this).combobox('setValue', '');
					}
				}
			},
			//下拉列表值改变事件
			onChange:function(data){}
		});
		
		$('#KS_KSLB_CX').combobox({
			data:lbData,
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			panelHeight:'auto', //combobox高度
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
		
		$('#ZK_XNXQBM').combobox({
			data:xnxqData,
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			panelHeight:'140px', //combobox高度
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
		
		
		$('#ZK_KSLBXJ').combobox({
			data:lbData,
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			panelHeight:'auto', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data != ''){
					//初始化combobox时赋值
					$(this).combobox('setValue', '');
				}
			},
			//下拉列表值改变事件
			onChange:function(data){
				if(data != ''){
					if(data == '03'){
						$('#ZK_ZSLB').combobox('enable');
						$('#djkxslbTR').css('display','none');
					}else{
						if(data == '01'){
							$('#djkxslbTR').css('display','block');
							$('#ZK_DJKXSLBXJ').combobox('setValues','all');
						}else{
							$('#djkxslbTR').css('display','none');
						}
						
						$('#ZK_ZSLB').combobox('setValue','all');
						$('#ZK_ZSLB').combobox('disable');
					}
				}
			}
		});
		
		//等级考学生类别
		$('#ZK_DJKXSLBXJ').combobox({
			data:zslbData,
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			panelHeight:'auto', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data != ''){
					//初始化combobox时赋值
					$(this).combobox('setValue', 'all');
				}
			},
			//下拉列表值改变事件
			onChange:function(data){},
			
			onSelect: function (row) {
				 if($(this).combobox('getValues')!='all'){
				 	$(this).combobox('unselect', 'all');
				 }
				 if(row.comboValue=='all'){
				 	$(this).combobox('setValues', 'all');
				 }
			},
			onUnselect: function (row) {
				if($(this).combobox('getValues')==''){
					$(this).combobox('setValues', 'all');
				}
			}
		});
		
		//等级考学生类别(月考)
		$('#ZK_DJKXSLBYK').combobox({
			data:zslbData,
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			panelHeight:'auto', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data != ''){
					//初始化combobox时赋值
					$(this).combobox('setValue', 'all');
				}
			},
			//下拉列表值改变事件
			onChange:function(data){},
			
			onSelect: function (row) {
				 if($(this).combobox('getValues')!='all'){
				 	$(this).combobox('unselect', 'all');
				 }
				 if(row.comboValue=='all'){
				 	$(this).combobox('setValues', 'all');
				 }
			},
			onUnselect: function (row) {
				if($(this).combobox('getValues')==''){
					$(this).combobox('setValues', 'all');
				}
			}
		});
		
		$('#ZK_ZSLB').combobox({
			data:zslbData,
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			panelHeight:'140', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data != ''){
					//初始化combobox时赋值
					$(this).combobox('setValue', 'all');
				}
			},
			//下拉列表值改变事件
			onChange:function(data){},
			
			onSelect: function (row) {
				 if($(this).combobox('getValues')!='all'){
				 	$(this).combobox('unselect', 'all');
				 }
				 if(row.comboValue=='all'){
				 	$(this).combobox('setValues', 'all');
				 }
			},
			onUnselect: function (row) {
				if($(this).combobox('getValues')==''){
					$(this).combobox('setValues', 'all');
				}
			}
		});
	}
	
	/**读取datagrid数据**/
	function loadData(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Kssz',
			dataType:"json",
			data : 'active=queryRec&page=' + pageNum + '&rows=' + pageSize + 
				'&ZK_XN=' + encodeURI($('#KS_XN_CX').combobox('getValue')) + 
				'&ZK_XQ=' + encodeURI($('#KS_XQ_CX').combobox('getValue')) +
				'&ZK_KSMC=' + encodeURI($('#KS_KSMC_CX').textbox('getValue'))+
				'&ZK_KSLB=' + encodeURI($('#KS_KSLB_CX').combobox('getValue')),
			success : function(data) {
				loadGrid(data[0].listData);
			}
		});
	}
	
	function doToolbar(iToolbar){
		//查询
		if(iToolbar == "search"){
			loadData();
		}
		
		//判断获取参数为new，执行新建操作
		if(iToolbar == "new"){
			$('#ZK_XNXQBM').combobox('setValue','');
			$('#ZK_KSMC').textbox('setValue','');
			$('#ZK_KSLBXJ').combobox('setValue','');
			$('#ZK_ZSLB').combobox('setValues','all');
			$('#ZK_DJKXSLBXJ').combobox('setValues','all');
			$('#ZK_DFKSSJ').datebox('setValue', '');
			$('#ZK_DFJSSJ').datebox('setValue', '');
			saveType = iToolbar;
			
			//打开dialog
			$('#examInfoDialog').dialog({   
				title: "新建"
			});
			$('#examInfoDialog').dialog('open');
		}
		
		//判断获取参数为edit，执行编辑操作
		if(iToolbar == "edit"){
			if(iKeyCode == ''){
				alertMsg('请选择一行数据');
				return;
			}
			//打开dialog
			$('#examInfoDialog').dialog({   
				title: '编辑'   
			});
			
			saveType = iToolbar;
			$('#ZK_XNXQBM').combobox('setValue',row.学年学期编码);
			$('#ZK_KSMC').textbox('setValue',row.考试名称);
			$('#ZK_KSLBXJ').combobox('setValue',row.类别编号);
			if(row.类别编号=='01'){
				$('#djkxslbTR').css('display','block');
				$('#ZK_DJKXSLBXJ').combobox('setValues',row.等级考学生类别);
			}else{
				$('#djkxslbTR').css('display','none');
			}
			$('#ZK_ZSLB').combobox('setValues',row.招生类别);
			$('#ZK_DFKSSJ').datebox('setValue', row.登分开始时间);
			$('#ZK_DFJSSJ').datebox('setValue', row.登分结束时间);
			$('#examInfoDialog').dialog('open');
		}
		
		//判断获取参数为save，执行保存操作
		if(iToolbar == "save"){
			if($('#ZK_XNXQBM').combobox('getValue') == ''){
				alertMsg('请选择学年学期');
				return;
			}
			if($('#ZK_KSMC').textbox('getValue') == ''){
				alertMsg('请输入考试名称');
				return;
			}
			if($('#ZK_KSLBXJ').combobox('getValue') == ''){
				alertMsg('请选择考试类别');
				return;
			}
			if($('#ZK_DFKSSJ').datebox('getValue') == ''){
				alertMsg('请选择登分开始时间');
				return;
			}
			if($('#ZK_DFJSSJ').datebox('getValue') == ''){
				alertMsg('请选择登分结束时间');
				return;
			}
			
			if(saveType == 'new'){
				$('#ZK_KSBH').val('');
			}else{
				$('#ZK_KSBH').val(iKeyCode);
			}
			
			if($('#ZK_KSLBXJ').combobox('getValue')!='01'){
				$('#ZK_DJKXSLBXJ').combobox('setValues','null');
			}
			
			$('#ZK_ZSLBXJ').val($('#ZK_ZSLB').combobox('getValues'));
			$('#ZK_DJKXSLB').val($('#ZK_DJKXSLBXJ').combobox('getValues'));
			$('#active').val(iToolbar);
			$("#form1").submit();
		}
		//删除
		if(iToolbar == "del"){
			if(iKeyCode == ''){
				alertMsg('请选择一行数据');
				return;
			}
			ConfirmMsg('当前选择的考试删除后无法恢复<font color="red">（包括相关考试设置及成绩信息）</font>，是否确定要删除？', 'delExam();', '');
		}
		
		//月考设置
		if(iToolbar == "yksz"){
			loadGridYKKS();
			YKBJcombobox();
			YKKCMCcombobox();
			$('#ykszSetDialog').dialog('open');
		}
		//月考查询
		if(iToolbar == "chaxun"){
			loadGridYKKS();
		}
		
		//月考新建
		if(iToolbar == "saveOptionyk"){
			yksaveType='saveOptionyk';
			//打开dialog
			$('#YKBJ_KSMC').textbox('setValue','');
			$('#YKBJ_MYCJSJ').numberbox('setValue','');
			$('#monthexamDialog').dialog({   
				title: "新建"
			});
			YKBJcombobox();
			YKKCMCcombobox();
			$('#ZK_DJKXSLBYK').combobox('setValues','all');
			$('#YKBJ_KCMC').combobox('setValues','0000000111,0000000112,0000000113');
			$('#monthexamDialog').dialog('open');
		}
		
		//月考编辑
		if(iToolbar == "editOptionyk"){
			yksaveType='editOptionyk';
			//打开dialog
			$('#YKBJ_KSMC').textbox('setValue',ykrow.考试名称);
			$('#YKBJ_MYCJSJ').numberbox('setValue',ykrow.每月创建日期);
			if(ykiKeyCode == ''){
				alertMsg('请选择一行数据');
				return;
			}
			$('#monthexamDialog').dialog({   
				title: "编辑"
			});
			/* YKBJcombobox();
			YKKCMCcombobox(); */
			$('#ZK_DJKXSLBYK').combobox('setValues',ykrow.等级考学生类别);
			$('#YKBJ_BJ').combobox('setValues',ykrow.年级.split(','));
			$('#YKBJ_KCMC').combobox('setValues',ykrow.课程代码.split(','));
			$('#monthexamDialog').dialog('open');
		}
		
		//月考保存
		if(iToolbar == "saveYKXX"){
			if($('#YKBJ_KSMC').textbox('getValue') == ''){
				alertMsg('请输入考试名称');
				return;
			}
			if($('#YKBJ_BJ').combobox('getValues') == ''){
				alertMsg('请选择年级');
				return;
			}
			if($('#YKBJ_KCMC').combobox('getValues') == ''){
				alertMsg('请选择课程名称');
				return;
			}
			if($('#YKBJ_MYCJSJ').numberbox('getValue') == ''){
				alertMsg('请选择每月创建时间');
				return;
			}
			
			if(yksaveType=='saveOptionyk'){
				$('#YKSZ_BH').val('');
			}else{
				$('#YKSZ_BH').val(ykiKeyCode);
			}
			$('#YKSZ_KSMC').val($('#YKBJ_KSMC').textbox('getValue'));
			$('#YKSZ_BJ').val($('#YKBJ_BJ').combobox('getValues'));
			$('#YKSZ_KCMC').val($('#YKBJ_KCMC').combobox('getValues'));
			$('#YKSZ_CJSJ').val($('#YKBJ_MYCJSJ').numberbox('getValue'));
			
			$('#ZK_DJKXSLB').val($('#ZK_DJKXSLBYK').combobox('getValues'));
			$('#active').val(iToolbar);
			$("#form1").submit();
		}
		
		//删除月考信息
		if(iToolbar == "delOptionyk"){
			if(ykiKeyCode == ''){
				alertMsg('请选择一行数据');
				return;
			}
			ConfirmMsg('是否要删除此条月考信息？', 'delYKExam();', '');
		}
		
		//保存已选学科信息
		if(iToolbar == "saveXK"){
			if(curSelClass != ''){
				$('#subjectList').datagrid('endEdit', lastIndex);
				var xkData = $('#subjectList').datagrid('getSelections');
				var selXkInfo = '';
				for(var i=0; i<xkData.length; i++){
					selXkInfo += xkData[i].课程号+'｜';
				}
				if(selXkInfo.length > 0){
					selXkInfo = selXkInfo.substring(0, selXkInfo.length-1); 
				}
				$('#active').val(iToolbar);
				$('#ZK_KSBH').val(iKeyCode);
				$('#ZK_BJBH').val(curSelClass);
				
				$('#ZK_ZSLBXJ').val(zslbdm);
				
				$('#xkInfo').val(selXkInfo);
				$("#form1").submit();
			}
		}
		
		//考试学科批量设置
		if(iToolbar == "saveXKPLSZ"){
			XKPLXNcombobox();
			$('#xkplDialog').dialog('open');
		}
		
		//考试学科批量设置保存
		if(iToolbar == "saveXKPL"){
		
			$('#maskFontPL').html('学科批量设置中...');
    		$('#divPageMaskPL').show();
    		
			if($('#PL_NJ').combobox('getValues') == ''){
				alertMsg('请选择年级');
				return;
			}
			$('#ZK_KSBH').val(iKeyCode);
			$('#plnjInfo').val($('#PL_NJ').combobox('getValues'));
			$('#plzyInfo').val($('#PL_ZY').combobox('getValues'));
			$('#plkcInfo').val($('#PL_KCMC').combobox('getValues'));
			$('#ZK_ZSLBXJ').val(zslbdm);
			$('#active').val(iToolbar);
			$("#form1").submit();
		}
	}
	
	/**表单提交**/
	$('#form1').form({
		url:'<%=request.getContextPath()%>/Svl_Kssz',
	    onSubmit: function(){
	    },
	    success:function(data){
	    	var json  =  eval("("+data+")");
	    	if($('#active').val() == 'save'){
				if(json[0].MSG == '保存成功'){
					//提示信息
					showMsg(json[0].MSG);
					loadData();
					$('#examInfoDialog').dialog('close');
				}else{
					alertMsg(json[0].MSG);
				}
			}
			
			if($('#active').val() == 'ModYKRec'){
				if(json[0].MSG == '修改成功'){
					//提示信息
					showMsg(json[0].MSG);
					loadGridYKKS();
					$('#monthexamDialog').dialog('close');
				}else{
					alertMsg(json[0].MSG);
				}
			}
			
			if($('#active').val() == 'saveYKXX'){
				if(json[0].MSG == '保存成功'){
					//提示信息
					showMsg(json[0].MSG);
					loadGridYKKS();
					$('#monthexamDialog').dialog('close');
				}else{
					alertMsg(json[0].MSG);
				}
			}
			
			if($('#active').val() == 'saveXK'){
				if(json[0].MSG == '保存成功'){
					//提示信息
					showMsg(json[0].MSG);
					loadGridYKKS();
				}else{
					alertMsg(json[0].MSG);
				}
			}
			
			if($('#active').val() == 'saveXKPL'){
				if(json[0].MSG == '保存成功'){
					//提示信息
					$('#divPageMaskPL').hide();
					showMsg(json[0].MSG);
					loadGridYKKS();
					$('#xkplDialog').dialog('close');
				}else{
					alertMsg(json[0].MSG);
				}
			}
	    }
	});
	
	
	//删除
	function delExam(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Kssz',
			data : 'active=del&ZK_KSBH=' + iKeyCode,
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
	
	function loadGridYKKS(){
		$('#optionList_YK').datagrid({
			url:'<%=request.getContextPath()%>/Svl_Kssz',
			queryParams:{"active":"queryykksRec",
						"YK_KSMC":encodeURI($('#YK_KSMC').textbox('getValue'))
			},
			title:'月考考试列表',
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
				{field:'考试名称',title:'考试名称',width:fillsize(0.5),align:'center'},
				{field:'每月创建日期',title:'每月创建时间',width:fillsize(0.3),align:'center'},
				{field:'AA_SET',align:'center',title:'操作',width:fillsize(0.2),formatter:function(value,rec){
					var str = '';
					if(rec.状态 == '1'){
						str = "<input type='button' value='[停用]' onclick='openScoreList(\"" + rec.编号 + "\",\"" + rec.考试名称   + "\",\"" + rec.班级代码   + "\",\"" + rec.课程代码   + "\",\"" + rec.每月创建时间 + "\",\"" + rec.状态 + "\")' id='openScoreList' style='cursor:pointer;'>";
					}else{
						str = "<input type='button' value='[启用]' onclick='openScoreList(\"" + rec.编号 + "\",\"" + rec.考试名称   + "\",\"" + rec.班级代码   + "\",\"" + rec.课程代码   + "\",\"" + rec.每月创建时间 + "\",\"" + rec.状态 + "\")' id='openScoreList' style='cursor:pointer;'>";
					}
					return str;
				}}
			]],
			onClickRow:function(rowIndex,rowData){ 
			
				ykiKeyCode = rowData.编号;
	 			ykrow=rowData;
			},
			//双击某行时触发
			onDblClickRow:function(rowIndex,rowData){
			},
			//成功加载时
			onLoadSuccess:function(data){//查询成功时候把后台的查询语句赋值给变量listsql
						
				ykiKeyCode = '';
	 			ykrow = '';
			}
		});
	}
	
	/**点击启用或停用**/
	function openScoreList(bianhao, ksmc, bjdm, kcdm, mycjsj, zt){
		$('#active').val("ModYKRec");
		$('#ZZZBH').val(bianhao);
		$('#ZT').val(zt);
		$('#form1').submit();
	}
	
	//用来绑定月考里班级combobox
  	function YKBJcombobox(){
  		$('#YKBJ_BJ').combobox({
			//url:'<%=request.getContextPath()%>/Svl_Kssz?active=loadBJCombo',
			data:nianji,
			valueField:'comboValue',
			textField:'comboName',
			editable:true,
			selectOnNavigation:true,
			panelHeight:'140',
			onLoadSuccess:function(data){
				if(data!=""){
					$(this).combobox('setValue','all');
				}
			},
			onSelect: function (row) {
				 if($(this).combobox('getValues')!='all'){
				 	$(this).combobox('unselect', 'all');
				 }
				 if(row.comboValue=='all'){
				 	$(this).combobox('setValues', 'all');
				 }
			},
			onUnselect: function (row) {
				if($(this).combobox('getValues')==''){
					$(this).combobox('setValues', 'all');
				}
			} 
		});
  	}
  	
  	//用来绑定月考里课程名称combobox
  	function YKKCMCcombobox(){
  		$('#YKBJ_KCMC').combobox({
			url:'<%=request.getContextPath()%>/Svl_Kssz?active=loadKCMCCombo',
			valueField:'comboValue',
			textField:'comboName',
			editable:true,
			selectOnNavigation:true,
			panelHeight:'140',
			onLoadSuccess:function(data){
				if(data!=""){
					//$(this).combobox('setValues','all');
					$('#YKBJ_KCMC').combobox('setValues','0000000111,0000000112,0000000113');
				}
			},
			onSelect: function (row) {
				 if($(this).combobox('getValues')!='all'){
				 	$(this).combobox('unselect', 'all');
				 }
				 if(row.comboValue=='all'){
				 	$(this).combobox('setValues', 'all');
				 }
			},
			onUnselect: function (row) {
				if($(this).combobox('getValues')==''){
					$(this).combobox('setValues', 'all');
				}
			}
		});
  	}
  	
  	//删除月考信息
	function delYKExam(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Kssz',
			data : 'active=delYKRec&YKSZ_BH=' + ykiKeyCode,
			dataType:"json",
			success : function(data){
				if(data[0].MSG == '删除成功'){
					showMsg(data[0].MSG);
					loadGridYKKS();
				}else{
					alertMsg(data[0].MSG);
				}
			}
		});
	}
	
	/**打开学科设置对话框**/
	function openSubjectList(xnxq, zslb, djkxslb, ksmc){
		$('#xnxq').val(xnxq);
		zslbdm = zslb;
		
		//djkxslbdm=djkxslb;
		
		loadClassTree(xnxq);
		$("#xkSetDialog").dialog('setTitle',ksmc + ' 考试学科设置');
		$("#xkSetDialog").dialog('open');
	}
	
	/**读取班级TREE数据**/
	function loadClassTree(xnxq){
		$('#classTree').tree({
			url:'<%=request.getContextPath()%>/Svl_Kssz?active=loadClassTree&ZK_XNXQBM=' + xnxq,
		    onClick:function(node){
		    	if(node.id != curSelClass){
		    		$('#subjectList').datagrid('reload');
		    		curSelClass = node.id;
		    		cpBJDM = node.id;
		    		cpBJMC = node.text;
		    		$('#mask').hide();
		    	}
		    },
			onDblClick:function(node){},
			onLoadSuccess:function(node, data){
				curSelClass = '';
				cpBJDM = '';
				cpBJMC = '';
				$("#classTree").show();
			}
		});
	}
	
	/**读取当前班级已选考试学科信息**/
	function loadClassSelSubject(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Kssz',
			data : 'active=loadClassSelSubject&ZK_KSBH=' + iKeyCode + '&ZK_BJBH=' + curSelClass + '&ZK_XNXQBM=' + row.学年学期编码  ,
			dataType:"json",
			success : function(data) {
				for(var i=0; i<data.length; i++){
					if(data[i].state != '2'){
						//更新datagrid数据
						$('#subjectList').datagrid('updateRow',{
							index: i,
							row: data
						});
						if(data[i].state == '1'){
							$('#subjectList').datagrid('selectRow', i);
						}
					}
				}
			}
		});
	}
	
	/**读取科目列表**/
	var IsCheckFlag = true;//用于判断如果点击的是可编辑单元格不做选中或取消选中操作
	function loadSubjectList(){
		$("#subjectList").datagrid({
			url: '<%=request.getContextPath()%>/Svl_Kssz',
			queryParams:{"active":"querySubjectList"},
			width: 300,//宽度dialog设置了 datagrid可以不用设置
			nowrap: false,
			fit:true,
			showFooter:true,
			striped:true, //隔行变色
			singleSelect:false,
			//rownumbers:true,//行号
			fitColumns: true,
			columns:[[
				{field:'ck',checkbox:true},
				{field:'课程名称',title:'课程名称',width:fillsize(0.4),align:'center'},
			]],
			onClickRow:function(rowIndex, rowData){
				$(this).datagrid('endEdit', lastIndex);
				$(this).datagrid('beginEdit', rowIndex);
				lastIndex = rowIndex;
			},
			onClickCell: function (rowIndex, field, value) {
				if(field == '课程名称'){
					IsCheckFlag = true;
				}else{
					IsCheckFlag = false;
				}
			},
			/*
			onSelect: function (rowIndex, rowData) {
				if (!IsCheckFlag) {
					IsCheckFlag = true;
					$(this).datagrid("unselectRow", rowIndex);
				}
			},
			*/
			onUnselect: function (rowIndex, rowData) {
				if (!IsCheckFlag) {
					IsCheckFlag = true;
					$(this).datagrid("selectRow", rowIndex);
				}
			},
			onLoadSuccess: function(data){
				$('#subjectList').datagrid('selectRow', 0);
				$('#subjectList').datagrid('unselectRow', 0);
			
				if(curSelClass != ''){
					loadClassSelSubject();
				}
			}
		});
	}
	
	//考试学科批量设置按钮里的学年
	function XKPLXNcombobox(){
		$('#PL_NJ').combobox({
				url:'<%=request.getContextPath()%>/Svl_Kssz?active=loadXKPLCombo', 
				valueField:'comboValue',
				textField:'comboName',
				editable:false,
				panelHeight:'100',
				onLoadSuccess:function(data){
					if(data!=""){
						$(this).combobox('setValues','');
					}
				},
				onChange:function(data){
					if(data==''){
						$('#PL_ZY').combobox('setText', '请先选择年级');
						$('#PL_ZY').combobox('disable');
						
						$('#PL_KCMC').combobox('setText', '请先选择年级');
						$('#PL_KCMC').combobox('disable');
					}
					else{
						XKPLZYcombobox();
						loadXKPLKCCombo();
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
	}
	
	//考试学科批量设置按钮里的专业
	function XKPLZYcombobox(){
		//读取学生姓名下拉框
		$('#PL_ZY').combobox({
			url:'<%=request.getContextPath()%>/Svl_Kssz?active=loadXKPLZYCombo&PL_XN=' + $('#PL_NJ').combobox('getValues'),
			valueField:'comboValue',
			textField:'comboName',
			editable:true,
			panelHeight:'140', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data.length == 1){
										
					$('#PL_ZY').combobox('setText', '没有可选的专业');
					$('#PL_ZY').combobox('disable');
					
					$('#PL_KCMC').combobox('setText', '没有可选的专业');
					$('#PL_KCMC').combobox('disable');
				}else{
					$('#PL_ZY').combobox('enable');
					//初始化combobox时赋值
					$(this).combobox('setValues', 'all');
				}
			},
			//下拉列表值改变事件
			onChange:function(data){
				loadXKPLKCCombo();
			},
			onSelect: function (row) {
			 	if($(this).combobox('getValues')!='all'){
			 		$(this).combobox('unselect', 'all');
			 	}
			 	if(row.comboValue=='all'){
			 		$(this).combobox('setValues', 'all');
			 	}
			},
			onUnselect: function (row) {
				if($(this).combobox('getValues')==''){
					$(this).combobox('setValues', 'all');
				}
			}
		});
	}
	
	
	//考试学科批量设置按钮里的课程
	function loadXKPLKCCombo(){
		//读取学生姓名下拉框
		$('#PL_KCMC').combobox({
			//url:'<%=request.getContextPath()%>/Svl_Kssz?active=loadXKPLKCCombo&PL_XN=' + $('#PL_NJ').combobox('getValues') + '&PL_ZY=' + $('#PL_ZY').combobox('getValues'),
			url:'<%=request.getContextPath()%>/Svl_Kssz?active=loadXKPLKCCombo',
			valueField:'comboValue',
			textField:'comboName',
			editable:true,
			panelHeight:'140', //combobox高度
			onLoadSuccess:function(data){
				if(data != ''){
				//初始化combobox时赋值
					$(this).combobox('setValue', 'all');
				}
				//判断data参数是否为空
				if(data.length == 1){
										
					/* $('#PL_KCMC').combobox('setText', '没有可选的课程');
					$('#PL_KCMC').combobox('disable'); */
					$(this).combobox('setValues', 'all');
				}else{
					$('#PL_KCMC').combobox('enable');
					//初始化combobox时赋值
					$(this).combobox('setValues', 'all');
				}
			},
			//下拉列表值改变事件
			onChange:function(data){
				
			},
			onSelect: function (row) {
			 	if($(this).combobox('getValues')!='all'){
			 		$(this).combobox('unselect', 'all');
			 	}
			 	if(row.comboValue=='all'){
			 		$(this).combobox('setValues', 'all');
			 	}
			},
			onUnselect: function (row) {
				if($(this).combobox('getValues')==''){
					$(this).combobox('setValues', 'all');
				}
			}
		});
	}
	
	
  </script>