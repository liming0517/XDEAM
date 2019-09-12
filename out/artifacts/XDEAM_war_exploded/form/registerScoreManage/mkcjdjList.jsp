<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="java.util.Vector"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.src.develop.store.user.*"%>
<%
	/**
		创建人：yeq
		Create date: 2017.04.20
		功能说明：用于成绩登记
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
	<title>免考成绩登记</title>
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
		#maskFont{
			 font-size:16px;
			 color:#2B2B2B;
			 width:200px;
			 height:100%;
			 position:absolute;
			 top:50%;
			 left:50%;
			 margin-top:-10px;
			 margin-left:-80px;
		}
		
		.kmInfoTable{
			width:100%;
			height:209px;
			border-top:1px solid #CCCCCC;
			border-left:1px solid #CCCCCC;
			background-color:#EFEFEF;
		}
	
		.kmInfoTd{
			border-right:1px solid #CCCCCC;
			border-bottom:1px solid #CCCCCC;
			font-size:12;
			padding-left:6px;
		}
		
		.zpInfoTable{
			width:100%;
			height:100%;
			text-align:center;
		}
		
		.zpInfoTd{
			font-size:12;
			border-right:1px solid #CCCCCC;
			border-bottom:1px solid #CCCCCC;
		}
		
		#stuList{
			width:100%;
			text-align:center;
			border-left:1px solid #95B8E7;
			border-top:1px solid #95B8E7;
			display:none;
		}
		
		.stuListTr{
			height:32px;
		}
		
		.stuListTd{
			font-size:16;
			border-right:1px solid #95B8E7;
			border-bottom:1px solid #95B8E7;
		}
		
		.stuListTitle{
			font-weight:bold;
		}
		
		.scoreSpan{
			font-size:16;
			width:99%;
			height:24px;
			padding-top:5px;
			display:block;
		}
		
		.scoreInput{
			height:28px;
			font-size:16;
			text-align:center;
			display:none;
		}
		
		.scoreCombo{
			display:none;
		}
		
		.configInfo{
			height:27px;
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
<body id="layoutBody" class="easyui-layout" style="margin:0; padding:0; overflow:hidden;">
	<div id="mask" style="width:110%; height:100%; background-color:#ffffff; position:absolute; top:0; z-index:999;"></div>
	
	<div region="north" title="免考成绩登记" style="height:84px; overflow:hidden;">
		<table id="queryCondition" singleselect="true" width="100%" class="tablestyle">
			<tr>
				<td style="width:150px;" class="titlestyle">学年学期名称</td>
				<td>
					<select name="XNXQMC_CX" id="XNXQMC_CX" class="easyui-combobox" style="width:145px;">
					</select>
				</td>
				<td style="width:150px;" class="titlestyle">教学性质</td><!-- 根据当前登录人权限 -->
				<td>
					<select name="JXXZ_CX" id="JXXZ_CX" class="easyui-combobox" style="width:145px;">
					</select>
				</td>
				<td style="width:150px;" class="titlestyle zycx">专业名称</td>
				<td>
					<input name="ZYMC_CX" id="ZYMC_CX" class="easyui-textbox" style="width:145px;"/>
				</td>
				<td style="width:150px;" align="center">
					<a href="#" onclick="doToolbar(this.id)" id="queList" class="easyui-linkbutton" plain="true" iconcls="icon-search">查询</a>
				</td>
			</tr>
			<tr>
				<td style="width:150px;" class="titlestyle">年级名称</td><!-- 根据当前登录人权限 -->
				<td>
					<select name="NJDM_CX" id="NJDM_CX" class="easyui-combobox" style="width:145px;">
					</select>
				</td>
				<td style="width:150px;" class="titlestyle zycx">课程名称</td>
				<td>
					<input name="KCMC_CX" id="KCMC_CX" class="easyui-textbox" style="width:145px;"/>
				</td>
				<td style="width:150px;" class="titlestyle zycx">课程类型</td>
				<td>
					<select name="KCLX_CX" id="KCLX_CX" class="easyui-combobox" panelHeight="auto" style="width:145px;" editable="false">
						<option value="">请选择</option>
						<option value="1">普通课程</option>
						<option value="2">添加课程</option>
						<option value="3">选修课程</option>
						<option value="4">分层课程</option>
					</select>
				</td>
				<td>
				</td>
			</tr>
	    </table>
	</div>
	<div region="center">
		<table id="subjectList" style="width:100%;"></table>
	</div>
	
	<!-- 成绩登记 -->
	<div id="cjdjDialog" style=" overflow:hidden;">
		<div style="width:100%; height:100%;" class="easyui-layout">
			<%-- 遮罩层 --%>
	    	<div id="divPageMask" class="maskStyle">
	    		<div id="maskFont">学生成绩保存中...</div>
	    	</div>
	    	
			<div region="west" style="width:440px; overflow:hidden;">
				<div id="scoreInfo" style="width:100%; height:100%;" class="easyui-layout">
					<div region="north" title="科目信息" style="height:262px; background-color:#EFEFEF; overflow:hidden;">
					    <div id="scoreViewDiv">
							<table class="kmInfoTable" cellspacing="0" cellpadding="0">
								<tr class="configInfo">
									<td style="width:100px;" class="kmInfoTd">学年学期名称</td>
									<td class="kmInfoTd" id="xqmc">&nbsp;</td>
								</tr>
								<tr class="configInfo">
									<td class="kmInfoTd">专业名称</td>
									<td class="kmInfoTd" id="zymc">&nbsp;</td>
								</tr>
								<tr class="configInfo">
									<td class="kmInfoTd">课程名称</td>
									<td class="kmInfoTd" id="kcmc">&nbsp;</td>
								</tr>
								<tr class="configInfo">
									<td class="kmInfoTd">考试类型</td>
									<td class="kmInfoTd" id="kslxmc">&nbsp;</td>
								</tr>
								<tr class="configInfo">
									<td class="kmInfoTd" id="sxcjTitle">实训成绩</td>
									<td class="kmInfoTd" id="sxmc">&nbsp;</td>
								</tr>
								<tr class="configInfo">
									<td class="kmInfoTd">成绩类型</td>
									<td class="kmInfoTd" id="cjlxmc">&nbsp;</td>
								</tr>
								<tr>
									<td class="kmInfoTd" style="height:70px;">总评计算比例</td>
									<td style="border-right:1px solid #CCCCCC; border-bottom:1px solid #CCCCCC;">
										<table id="zpblShow" class="zpInfoTable" cellspacing="0" cellpadding="0">
											<tr><td>&nbsp;</td></tr>
										</table>
									</td>
								</tr>
						    </table>
					    </div>
				    </div>
				    <div region="center" title="班级课程列表">
				    	<ul id="classTree" class="easyui-tree" style="display:none">
						</ul>
					</div>
				</div>
			</div>    
			<div region="center" style="overflow:hidden;">
				<div id="kcbMain" style="width:100%; height:100%;" class="easyui-layout">
					<div region="north" title="学生成绩列表" style="height:61px; overflow:hidden;">
						<form method="post" id="form2" name="form2">
							<table>
								<tr>
									<td><a href="#" id="saveStuScore" class="easyui-linkbutton" plain="true" iconcls="icon-save" onClick="doToolbar(this.id);" style="display:none;">成绩保存</a></td>
									<td><a href="#" id="dmsm" class="easyui-linkbutton" plain="true" iconcls="icon-assets" onClick="doToolbar(this.id);">成绩代码说明</a></td>
								</tr>
							</table>
					   	</form>
					</div>
					<div region="center" id="stuContent">
						<table id="loadingTable" style="width:100%; height:100%; text-align:center; display:none;">
							<tr><td><img src="<%=request.getContextPath()%>/images/loading_3.gif"></td></tr>
						</table>
						<table id="stuList" cellspacing="0" cellpadding="0">
						</table>
					</div>
				</div>
			</div>
		</div>
		
		<form id="form1" method="post">
			<input type="hidden" id="active" name="active"/>
			<input type="hidden" id="CX_ID" name="CX_ID"/>
			<input type="hidden" id="CK_KSLX" name="CK_KSLX"/>
			<input type="hidden" id="CK_ZPBLXX" name="CK_ZPBLXX"/>
			<input type="hidden" id="CK_PSBL" name="CK_PSBL"/>
			<input type="hidden" id="CK_QZBL" name="CK_QZBL"/>
			<input type="hidden" id="CK_SXBL" name="CK_SXBL"/>
			<input type="hidden" id="CK_QMBL" name="CK_QMBL"/>
			<input type="hidden" id="CK_CJLX" name="CK_CJLX"/>
			<input type="hidden" id="CK_SX" name="CK_SX"/>
			<input type="hidden" id="CX_XGBH" name="CX_XGBH"/>
			<input type="hidden" id="updateInfo" name="updateInfo"/>
		</form>
	</div>
	
	<!-- 文字成绩代码说明 -->
	<div id="dmsmDialog" style="overflow:hidden;">
		<table class="tablestyle" style="width:100%; height:100%; text-align:center;" cellspacing="0" cellpadding="0">
			<tr><td style="width:50%; font-weight:bold;">代码</td><td style="width:50%; font-weight:bold;">名称</td></tr>
			<tr><td>-1</td><td>免修</td></tr>
			<tr><td>-2</td><td>作弊</td></tr>
			<tr><td>-3</td><td>取消资格</td></tr>
			<tr><td>-4</td><td>缺考</td></tr>
			<tr><td>-5</td><td>缓考</td></tr>
			<tr><td>-17</td><td>免考</td></tr>
		</table>
	</div>
</body>
<script type="text/javascript">
	var sAuth = '<%=sAuth%>';
	var iKeyCode = '';
	var curSelCode = '';//当前选择的班级课程编号
	//var sportFlag = false;//用于判断选中的是否为体育相关课程
	var preSelCode = '';
	var comboOption = '';
	var curSelStu = '';//当前选择的学生编号
	var tempCjArray = new Array();//当前选中的学生未改动前的成绩
	var tempWidth = '';//控件宽度
	var initComboArray = new Array();//用于存放已经初始化combobox的学生编号
	var updateInfo = new Array();//用于保存更新的成绩信息
	var timer = '';
	var selFlag = false;
	var stuCodeArray = new Array();
	var stuIndex = 0;
	var closeDialogFlag = false;
	var changeClassFlag = false;
	var cjdjFlag = false;//用于判断当前时间或者权限是否允许登记成绩
	var admin = '<%=MyTools.getProp(request, "Base.admin")%>';//管理员
	var wzcjShowArray = '';
	var curClickCell = '';
	var titleArray = new Array();
	var zeroConfirmFlag = false;
	var clickFlag = true;
	var bjgScoreArray = new Array();
	var mkPosArray = new Array();//用于保存登分前免考成绩的位置
	
	$(document).ready(function(){
		$('#mask').hide();
		
		initDialog();//初始化对话框
		loadCourseGrid();
		initComboData();//页面初始化加载数据
	});
	
	/**重置设置区域内容**/
	function initConfigArea(){
		$('.zpblxxFont').css('color', '#000000');
		$("input[type='radio']").removeAttr('checked');
		$('#sxcjTitle').html('实训成绩');
		$('#kslxmc').html('&nbsp;');
		$('#sxmc').html('&nbsp;');
		$('#cjlxmc').html('&nbsp;');
		$('#zpblShow').html('<tr><td>&nbsp;</td></tr>');
	}
	
	/**页面初始化加载数据**/
	function initComboData(){
		$.ajax({
			type:"POST",
			url:'<%=request.getContextPath()%>/Svl_Xqcjdj',
			data:'active=loadComboData',
			dataType:"json",
			success:function(data) {
				comboOption = data[0].wzcjData;
				wzcjShowArray = data[0].wzcjShowData;
				initCombobox(data[0].xnxqData, data[0].jxxzData, data[0].njdmData);
			}
		});
	}
	
	/**加载combobox控件
		@xnxqData 学年学期下拉框数据
		@jxxzData 教学性质下拉框数据
		@njdmData 年级名称下拉框数据
	**/
	function initCombobox(xnxqData, jxxzData, njdmData){
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
		
		$('#tjfs').combobox({
			data:[{"comboName":"按期末统计","comboValue":"1"},{"comboName":"按总评统计","comboValue":"2"}],
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			panelHeight:'auto', //combobox高度
			onLoadSuccess:function(data){
			},
			//下拉列表值改变事件
			onSelect:function(record){
				if(record.comboValue != undefined){
					$('#kftjMask').show();
					loadKftj(record.comboValue);
				}
			}
		});
	}
	
	/**加载 dialog控件**/
	function initDialog(){
		$('#cjdjDialog').dialog({
			title:'成绩信息',
			maximized:true,
			modal:true,
			closed: true,   
			cache: false, 
			draggable:true,//是否可移动dialog框设置
			toolbar:[{
				text:'返回',
				iconCls:'icon-back',
				handler:function(){
					$('#cjdjDialog').dialog('close');
				}
			}],
			//读取事件
			onLoad:function(data){
			},
			onBeforeClose:function(){
				if(closeDialogFlag == false){
					//保存之前修改的成绩
					updateScoreArray();
				}
				
				//判断是否有需要保存的数据
    			if(updateInfo.length > 0){
    				closeDialogFlag = true;
    				//提示保存成绩
    				ConfirmMsg('有登记的成绩未保存，是否需要保存？', 'saveStuScore("saveStuScore");', 'closeDfDialog();');
    				return false;
    			}
	    	},
			//关闭事件
			onClose:function(data){
				clearInterval(timer);//结束自动保存成绩定时器
				$('#loadingTable').hide();
				$('#stuList').hide();
				$('#stuList').html('');
				
				$('#saveStuScore').hide();
				
				curSelCode = '';
				preSelCode = '';
				curSelStu = '';
				closeDialogFlag = false;
				
				initConfigArea();
				
				mkPosArray.length = 0;
			}
		});
		
		$('#dmsmDialog').dialog({   
			title: '文字成绩代码说明',
			width: 300,//宽度设置   
			height: 275,//高度设置 
			modal:true,
			closed: true,   
			cache: false, 
			draggable:true//是否可移动dialog框设置
		});
	}
	
	/**关闭登分窗口**/
	function closeDfDialog(){
		updateInfo.length = 0;
		$('#cjdjDialog').dialog('close');
	}
	
	/**加载课程列表datagrid控件，读取页面信息**/
	function loadCourseGrid(){
		$('#subjectList').datagrid({
			url: '<%=request.getContextPath()%>/Svl_Xqcjdj',
			queryParams:{'active':'queMkSubjectList','sAuth':sAuth,'XNXQMC_CX':$('#XNXQMC_CX').combobox('getValue'),
				'JXXZ_CX':$('#JXXZ_CX').combobox('getValue'),'ZYMC_CX':encodeURI($('#ZYMC_CX').textbox('getValue')),
				'NJDM_CX':$('#NJDM_CX').combobox('getValue'),'KCMC_CX':encodeURI($('#KCMC_CX').textbox('getValue')),
				'KCLX_CX':$('#KCLX_CX').combobox('getValue')},
			title:'科目信息列表',
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
				{field:'科目编号',hidden:true},
				{field:'学年学期编码',hidden:true},
				{field:'学年学期名称',title:'学年学期名称',width:fillsize(0.15),align:'center'},
				{field:'教学性质',title:'教学性质',width:fillsize(0.1),align:'center'},
				{field:'专业名称',title:'专业名称',width:fillsize(0.22),align:'center'},
				{field:'年级名称',title:'年级名称',width:fillsize(0.1),align:'center'},
				{field:'课程名称',title:'课程名称',width:fillsize(0.23),align:'center'},
				{field:'课程类型',title:'课程类型',width:fillsize(0.1),align:'center'},
				//{field:'登分时间',hidden:true},
				//{field:'登分权限',hidden:true},
				{field:'col4',title:'操作',width:fillsize(0.1),align:'center',
					formatter:function(value,rec){
						var button = "<input type='button' ";
						//判断如果在登分时间内并没有锁定或者开放了教师登分权限或者是管理员权限并没有打印锁定
						/*20170110yeq修改查询是否可登分相关代码
						if((rec.登分时间=='true' && rec.打印锁定=='true') || (sAuth.indexOf(admin)>-1 && rec.打印锁定=='true') || rec.登分权限=='true')
							button += "value='[成绩登记]' onclick='openCjdjDialog(\"" + rec.科目编号 + "\", \"set\");' style=\"cursor:pointer;\">";
						else
							button += "value='[成绩查询]' onclick='openCjdjDialog(\"" + rec.科目编号 + "\", \"view\");' style=\"cursor:pointer;\">";
						*/
						button += "value='[成绩登记]' onclick='openCjdjDialog(\"" + rec.科目编号 + "\");' style=\"cursor:pointer;\">";
						return button;
				}}
			]],
			//双击某行时触发
			onDblClickRow:function(rowIndex,rowData){},
			//读取datagrid之前加载
			onBeforeLoad:function(){},
			//单击某行时触发
			onClickRow:function(rowIndex,rowData){
				iKeyCode = rowData.科目编号;
				
				/*
				if(rowData.课程名称.indexOf('体育') > -1){
					sportFlag = true;
				}else{
					sportFlag = false;
				}
				*/
			},
			//加载成功后触发
			onLoadSuccess: function(data){
				iKeyCode = '';
			}
		});
	};
	
	/**打开成绩登记对话框**/
	function openCjdjDialog(kmbh){
		if(clickFlag == false){
			return;
		}
		clickFlag = false;
		
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Xqcjdj',
			data : 'active=loadMkSubjectInfo&CX_ID=' + kmbh + '&sAuth=' + sAuth,
			dataType:"json",
			success : function(data) {
				var subjectInfo = data[0];
				
				$('#xqmc').html(subjectInfo.xnxqmc==''?'&nbsp;':subjectInfo.xnxqmc);
				$('#zymc').html(subjectInfo.zymc==''?'&nbsp;':subjectInfo.zymc);
				$('#kcmc').html(subjectInfo.kcmc==''?'&nbsp;':subjectInfo.kcmc);
				$('#cjdjDialog').dialog('open');
				loadClassTree(subjectInfo.classData);
				clickFlag = true;
			}
		});
	}
	
	/**查询当前课程是否可登分状态*/
	function loadLockState(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Xqcjdj',
			data : 'active=loadLockState&CX_XGBH=' + curSelCode,
			dataType:"json",
			success : function(data) {
				var subjectInfo = data[0];
				
				if(sAuth.indexOf(admin) < 0){
					if((subjectInfo.dfsj=='true' && subjectInfo.dysd=='true') || subjectInfo.dfqx=='true')
						type = 'set';
					else
						type = 'view';
				}else{
					if(subjectInfo.dysd == 'true')
						type = 'set';
					else
						type = 'view';
				}
				
				if(type == 'set'){
					cjdjFlag = true;
					
					//显示按钮
					$('#saveStuScore').show();
				}else{
					cjdjFlag = false;
					
					//按钮隐藏
					$('#saveStuScore').hide();
				}
				
				$('#scoreViewDiv').show();
				
				//读取登分配置信息
	    		loadDfConfig(curSelCode);
			}
		});
	}
	
	/**生成总评比例显示内容
		@kslx 考试类型
		@zpblxx 总评比例选项
		@psbl 平时比例
		@qzbl 期中比例
		@qmbl 期末比例
	**/
	function createZpblShow(kslx, zpblxx, sx, psbl, qzbl, sxbl, qmbl){
		var htmlContent = '';
		
		if(zpblxx == '4'){
			htmlContent = '<tr><td class="zpInfoTd" style="border-bottom:0;">手动输入<td></tr>';
		}else if(zpblxx == '5'){
			htmlContent = '<tr><td class="zpInfoTd" style="border-bottom:0;">仅输总评<td></tr>';
		}else{
			htmlContent = '<tr><td class="zpInfoTd">';
			if(kslx == '1'){
				htmlContent += '平时';
			}else{
				htmlContent += '阶段一';
			}
			htmlContent += '</td><td class="zpInfoTd">';
			if(kslx == '1'){
				htmlContent += '期中';
			}else{
				htmlContent += '阶段二';
			}
			htmlContent += '</td>';
			
			if(sx == '1'){
				htmlContent += '<td class="zpInfoTd">';
				if(kslx == '1'){
					htmlContent += '实训';
				}else{
					htmlContent += '阶段三';
				}
				htmlContent += '</td>';
			}
			
			htmlContent += '<td class="zpInfoTd">';
			if(kslx == '1'){
				htmlContent += '期末';
			}else{
				if(sx == '1'){
					htmlContent += '阶段四';
				}else{
					htmlContent += '阶段三';
				}
			}
			htmlContent += '</td></tr>' +
						'<tr><td class="zpInfoTd" style="border-bottom:0;">' + (psbl!=''?psbl+'%':'&nbsp;') + '</td>' +
						'<td class="zpInfoTd" style="border-bottom:0;">' + (qzbl!=''?qzbl+'%':'&nbsp;') + '</td>';
						
			if(sx == '1')
				htmlContent += '<td class="zpInfoTd" style="border-bottom:0;">' + (sxbl!=''?sxbl+'%':'&nbsp;') + '</td>';
				
			htmlContent += '<td class="zpInfoTd" style="border-bottom:0;">' + (qmbl!=''?qmbl+'%':'&nbsp;') + '</td></tr>';
		}
		
		return htmlContent;
	}
	
	/**加载班级信息TREE
		@classData
	**/
	function loadClassTree(classData){
		$('#classTree').tree({
			checkbox: false,
			data:classData,
		    onClick:function(node){
	    		//判断点击的班级是不是当前选中的班级
	    		if(node.id != curSelCode){
	    			curSelCode = node.id;
	    			
	    			//保存之前修改的成绩
					updateScoreArray();
	    			
	    			//判断是否有需要保存的数据
	    			if(updateInfo.length > 0){
	    				changeClassFlag = true;
	    				
	    				//提示保存成绩
	    				ConfirmMsg('有登记的成绩未保存，是否需要保存？', 'saveStuScore("saveStuScore");', 'changeClassCourse();');
	    			}else{
	    				preSelCode = curSelCode;
	    				
	    				//读取当前锁定状态
	    				loadLockState();
	    			}
	    		}
			},
			onLoadSuccess:function(node, data){
				$("#classTree").show();
			}
		});
	};
	
	/**读取登分配置**/
	function loadDfConfig(code){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Xqcjdj',
			data : 'active=loadDfConfig&CX_XGBH=' + code,
			dataType:"json",
			success : function(data) {
				var subjectInfo = data[0];
		
				initConfigArea();
				$('#CK_KSLX').val(subjectInfo.kslx);
				$('#CK_ZPBLXX').val(subjectInfo.zpblxx);
				$('#CK_CJLX').val(subjectInfo.cjlx);
				$('#CK_SX').val(subjectInfo.sx);
				$('#CK_PSBL').val(subjectInfo.psbl);
				$('#CK_QZBL').val(subjectInfo.qzbl);
				$('#CK_SXBL').val(subjectInfo.sxbl);
				$('#CK_QMBL').val(subjectInfo.qmbl);
				$('#kslxmc').html(subjectInfo.kslxmc);
					
				if(subjectInfo.kslx == '1'){
					$('#sxcjTitle').html('实训成绩');
					$('#sxmc').html(subjectInfo.sxmc);
				}else{
					$('#sxcjTitle').html('阶段数量');
					if(subjectInfo.sx == '1'){
						$('#sxmc').html('四阶段');
					}else{
						$('#sxmc').html('三阶段');
					}
				}
				
				$('#cjlxmc').html(subjectInfo.cjlxmc);
				$('#zpblShow').html(createZpblShow(subjectInfo.kslx, subjectInfo.zpblxx, subjectInfo.sx, subjectInfo.psbl, subjectInfo.qzbl, subjectInfo.sxbl, subjectInfo.qmbl));
				
				//读取当前选择班级课程的学生列表
		    	loadStuList(curSelCode);
			}
		});
	}
	
	//切换班级课程
	function changeClassCourse(){
		updateInfo.length = 0;
		//读取当前锁定状态
	    loadLockState();
	}
	
	/**读取当前选择班级课程的学生列表
		@xgbh 相关编号
	**/
	function loadStuList(xgbh){
		clearInterval(timer);//结束自动保存成绩定时器
		$('#loadingTable').show();
		$('#stuList').hide();
	    $('#stuList').html('');
	    selFlag = false;
	    initComboArray.length = 0;
	    curSelStu = '';
	    
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Xqcjdj',
			data : 'active=loadMkStuList&CX_XGBH=' + xgbh,
			dataType:"json",
			success : function(data) {
				var listContent = '';
				var stuCode = '';
				var stuClassNum = '';
				var stuName = '';
				var stuState = '';
				var ps = '';
				var qz = '';
				var sx = '';
				var qm = '';
				var zp = '';
				var kslx = $('#CK_KSLX').val();
				var haveSx = $('#CK_SX').val();
				var cjlx = $('#CK_CJLX').val();
				var zpblxx = $('#CK_ZPBLXX').val();
				var titleWidth = '15%';
				
				//title = ['Ps','Qz','Sx','Qm','Zp'];
				titleArray.length = 0;
				if(zpblxx == '5'){
					titleArray.push('Zp');
				}else{
					titleArray.push('Ps');
					titleArray.push('Qz');
					if(haveSx == '1'){
						titleArray.push('Sx');
					}
					titleArray.push('Qm');
					if(zpblxx == '5'){
						titleArray.push('Zp');
					}
				}
				
				listContent = '<tr class="stuListTr">' + 
							'<td class="stuListTd stuListTitle" style="width:10%;">学号</td>' +
							'<td class="stuListTd stuListTitle" style="width:10%;">姓名</td>' +
							'<td class="stuListTd stuListTitle" style="width:20%;">学籍号</td>' +
							'<td class="stuListTd stuListTitle titleWidth">';
				if(kslx == '1'){
					listContent += '平时';
				}else{
					listContent += '阶段一';
				}
				listContent += '</td>' +
							'<td class="stuListTd stuListTitle titleWidth">';
				if(kslx == '1'){
					listContent += '期中';
				}else{
					listContent += '阶段二';
				}
				listContent += '</td>';		
							
				if(haveSx == '1'){
					titleWidth = '12%';
					listContent += '<td class="stuListTd stuListTitle titleWidth">';
					if(kslx == '1'){
						listContent += '实训';
					}else{
						listContent += '阶段三';
					}
					listContent += '</td>';
				}
				listContent += '<td class="stuListTd stuListTitle titleWidth">';
				if(kslx == '1'){
						listContent += '期末';
				}else{
					if(haveSx == '1'){
						listContent += '阶段四';
					}else{
						listContent += '阶段三';
					}
				}
				listContent += '</td>' +
								'<td class="stuListTd stuListTitle titleWidth">总评</td>' +
							'</tr>';
				$('#stuList').append(listContent);
				$('.titleWidth').css('width', titleWidth);		
				
				stuCodeArray.length = 0;
				bjgScoreArray.length = 0;
				
				//判断是否有学生数据
				if(data.length > 0){
					for(var i=0; i<data.length; i++){
						stuClassNum = data[i].stuClassNum;
						stuCode = data[i].stuCode;
						stuCodeArray.push(stuCode);
						stuName = data[i].stuName;
						stuState = data[i].stuState;
						ps = data[i].ps;
						qz = data[i].qz;
						sx = data[i].sx;
						qm = data[i].qm;
						zp = data[i].zp;
						
						listContent = '<tr id="tr_' + stuCode + '" class="stuListTr stuScoreInfo" onmouseenter="enterStuRow(this.id);" onmouseleave="leaveStuRow(this.id);" onclick="clickStuRow(this.id)">' +
							'<td class="stuListTd" style="font-size:16;" onclick="clickCell(\'\');">' + stuClassNum + '</td>' +
							'<td class="stuListTd" style="font-size:16;" onclick="clickCell(\'\');">' + stuName + '</td>' +
							'<td class="stuListTd" style="font-size:16;" onclick="clickCell(\'\');">' + data[i].xjh + '<input type="hidden" id="stuState_' + stuCode + '" value="' + stuState + '"></td>' +
							'<td class="stuListTd" onclick="clickCell($(this).find(\'span\').attr(\'id\'));">';
								//判断总评比例选项是否为仅输总评
								if(zpblxx == '5'){
									listContent += '&nbsp;';
								}else if(stuState == '2'){
									listContent += '免修';
								}else{
									listContent += '<span id="spanPs_' + stuCode + '" class="span_' + stuCode + ' scoreSpan">' + parseScoreShow('init', 'spanPs_' + stuCode, cjlx, zpblxx, ps, 'ps') + '</span>';
									
									if(cjdjFlag == true){
										listContent += '<input id="inputPs_' + stuCode + '" class="input_' + stuCode + ' scoreInput" value="' + ps + '"/>' +
											'<div class="combo_' + stuCode + ' scoreCombo"><select id="comboPs_' + stuCode + '" class="easyui-combobox"></select></div>';
									}
								}
						listContent += '</td>' +
							'<td class="stuListTd" onclick="clickCell($(this).find(\'span\').attr(\'id\'));">';
								//判断总评比例选项是否为仅输总评
								if(zpblxx == '5'){
									listContent += '&nbsp;';
								}else if(stuState == '2'){
									listContent += '免修';
								}else{
									listContent += '<span id="spanQz_' + stuCode + '" class="span_' + stuCode + ' scoreSpan">' + parseScoreShow('init', 'spanQz_' + stuCode, cjlx, zpblxx, qz, 'qz') + '</span>';
									
									if(cjdjFlag == true){
										listContent += '<input id="inputQz_' + stuCode + '" class="input_' + stuCode + ' scoreInput" value="' + qz + '"/>' +
											'<div class="combo_' + stuCode + ' scoreCombo"><select id="comboQz_' + stuCode + '" class="easyui-combobox"></select></div>';
									}
								}
						listContent += '</td>';
						//判断是否有实训
						if(haveSx == '1'){
							listContent += '<td class="stuListTd" onclick="clickCell($(this).find(\'span\').attr(\'id\'));">';
								//判断总评比例选项是否为仅输总评
								if(zpblxx == '5'){
									listContent += '&nbsp;';
								}else if(stuState == '2'){
									listContent += '免修';
								}else{
									listContent += '<span id="spanSx_' + stuCode + '" class="span_' + stuCode + ' scoreSpan">' + parseScoreShow('init', 'spanSx_' + stuCode, cjlx, zpblxx, sx, 'sx') + '</span>';
									
									if(cjdjFlag == true){
										listContent += '<input id="inputSx_' + stuCode + '" class="input_' + stuCode + ' scoreInput" value="' + sx + '"/>' +
											'<div class="combo_' + stuCode + ' scoreCombo"><select id="comboSx_' + stuCode + '" class="easyui-combobox"></select></div>';
									}
								}
							listContent += '</td>';
						}
						listContent += '<td class="stuListTd" onclick="clickCell($(this).find(\'span\').attr(\'id\'));">';
							//判断总评比例选项是否为仅输总评或学生状态为免修
							if(zpblxx == '5'){
								listContent += '&nbsp;';
							}else if(stuState == '2'){
								listContent += '免修';
							}else{
								listContent += '<span id="spanQm_' + stuCode + '" class="span_' + stuCode + ' scoreSpan">' + parseScoreShow('init', 'spanQm_' + stuCode, cjlx, zpblxx, qm, 'qm') + '</span>';
								
								if(cjdjFlag == true){
									listContent += '<input id="inputQm_' + stuCode + '" class="input_' + stuCode + ' scoreInput" value="' + qm + '"/>' +
										'<div class="combo_' + stuCode + ' scoreCombo"><select id="comboQm_' + stuCode + '" class="easyui-combobox"></select></div>';
								}
							}
						listContent += '</td>' +
									'<td class="stuListTd" onclick="clickCell($(this).find(\'span\').attr(\'id\'));">';
								if(stuState == '2'){
									listContent += '免修';
								}else{
									listContent += '<span id="spanZp_' + stuCode + '" class="';
									
									//判断总评比例是否手动输入或仅输总评
									if(zpblxx == '4' || zpblxx=='5'){
										listContent += 'span_' + stuCode + ' ';
									}
									listContent += 'scoreSpan">' + parseScoreShow('init', 'spanZp_' + stuCode, cjlx, zpblxx, zp, 'zp') + '</span>';
									
									if(cjdjFlag == true){
										if(zpblxx=='4' || zpblxx=='5'){
											listContent += '<input id="inputZp_' + stuCode + '" class="input_' + stuCode + ' scoreInput" value="' + zp + '"/>' +
															'<div class="combo_' + stuCode + ' scoreCombo"><select id="comboZp_' + stuCode + '" class="easyui-combobox"></select></div>';
										}
									}
								}
						listContent += '</td></tr>';
						$('#stuList').append(listContent);
					}
					//不及格红色标注
					for(var i=0; i<bjgScoreArray.length; i++){
						$('#' + bjgScoreArray[i]).css('color', '#FF0000');
					}
					$('#stuList').show();
					
					tempWidth = $('.scoreSpan').eq(0).css('width');
					tempWidth = tempWidth.substring(0, tempWidth.length-2);
					if(tempWidth < 80){
						tempWidth = '80';
					}else{
						tempWidth = tempWidth-4;
					}
					$('.scoreInput').css('width', tempWidth + 'px');
					
					$('.scoreInput').bind('keydown', function(e){
						//ie火狐兼容
						//e = e || window.event;
						//var curKey = e.which || e.keyCode;
						e = (e) ? e : ((window.event) ? window.event : "");
	            		var curKey = e.keyCode ? e.keyCode : (e.which ? e.which : e.charCode);
						var curValue = $(this).val();
						
						//backspace/delete/tab
						if(curKey==8 || curKey==9 || curKey==46) return true;
						//上下左右键
						if(curKey==37 || curKey==38 || curKey==39 || curKey==40) return true;
						
						if(curValue == '0') return false;
						
						//禁用shift+数字键
				        if(e.shiftKey && (curKey>=48 && curKey<=57 || curKey==189)) return false;
				        
						//判断如果已经输入过-
						if(curValue == '-'){
							//减号
							if(curKey==109 || curKey==189) return false;
							//0
							if(curKey==48 || curKey==96) return false;
							
							//判断如果是期末和总评输入框，禁止输入-5
							//if(($(this).attr('id').indexOf('Qm')>-1 || $(this).attr('id').indexOf('Zp')>-1) && (curKey==53 || curKey==101)) return false;
						}
						//数字
						if(curKey>=48 && curKey<=57) return true;
						//小数字键盘
						if(curKey>=96 && curKey<=105) return true;
						//减号
						if(curValue=='' && (curKey==109 || curKey==189)) return true;
						
						return false;
					}).bind('blur',function(){
						var score = $(this).val();
						var stuCode = $(this).attr('id').substring(5);
						
						//判断如果输入的成绩是否符合规范
						if(parseInt(score)<0 && (score!='-2' && score!='-3' && score!='-4' && score!='-5' && score!='-17')){
							score = '';
							$(this).val('');
							//判断如果在非期末或总评栏输入，直接清空
							/*
							if(($(this).attr('id').indexOf('Qm')<0 && $(this).attr('id').indexOf('Zp')<0)){
								score = '';
								$(this).val('');
							}else{
								//判断如果不是体育课，所有输入框禁止输入-1，如果是体育课，期末和总评输入框允许输入-1
								if(sportFlag == false){
									score = '';
									$('#inputQm_' + stuCode.substring(3)).val('');
									$('#inputZp_' + stuCode.substring(3)).val('');
									
									if(zpblxx=='5' || zpblxx=='6'){
										if(zpblxx == '6'){
											$(this).parent().prev().html('&nbsp;');
										}else{
											$('#spanQm_' + stuCode.substring(3)).html('');
										}
										$('#spanZp_' + stuCode.substring(3)).html('');
									}else{
										$('#spanQm_' + stuCode.substring(3)).html('');
										$('#spanZp_' + stuCode.substring(3)).html('-');
									}
								}
							}
							*/
						}
						
						if(score != ''){
							//判断是否为数字
							if(isNaN(score)){
								score = '';
								alertMsg('请输入正确数字');
								$(this).val(score);
							}
							
							if(score.substring(score.length-2) == '.0'){
								score = score.substring(0, score.length-2);
								$(this).val(score);
							}
							
							if(score=='0' && zeroConfirmFlag==false){
								zeroConfirmFlag = true;
								$('#span' + stuCode).html(score);
								ConfirmMsg('确定当前输入的成绩是<span style="width:15px; color:red; text-align:center;">0</span>分吗？', 'zeroConfirmFlag=false;', 'cancelEnterZero("'+$(this).attr('id')+'","' + stuCode + '");');
								return;
							}
							
							/*
							//平时/期中/实训输入-4自动转为0
							if(stuCode.indexOf('Qm')<0 && stuCode.indexOf('Zp')<0 && score=='-4'){
								score = '0';
								$(this).val(score);
							}
							*/
						}
						if(parseInt(score) < 0){
							for(var i=0; i<wzcjShowArray.length; i++){
								if(score == wzcjShowArray[i].id){
									score = wzcjShowArray[i].text;
								}
							}
						}
						/*
						if(stuCode.indexOf('Zp')>-1 && score=='-4'){
							score = '缺考';
						}
						*/
						//不及格红色标注
						if(score!='' && ((score>-1&&score<60) || score=='作弊' || score=='取消资格' || score=='缺考' || score=='不及格' || score=='不合格')){
							$('#span' + stuCode).css('color', '#FF0000');
						}else{
							$('#span' + stuCode).css('color', '#000000');
						}
						$('#span' + stuCode).html(score);
					}).bind('keyup', function(e){
						//ie火狐兼容
						//e = e || window.event;
						//var curKey = e.which || e.keyCode;
						e = (e) ? e : ((window.event) ? window.event : "");
	            		var curKey = e.keyCode ? e.keyCode : (e.which ? e.which : e.charCode);
						var score = $(this).val();
						var stuCode = $(this).attr('id').substring(8);
						
						//应对中文输入法输入负号时，键值为229被屏蔽的情况。
						if(score=='' && curKey=='189'){
							$(this).val('-');
						}
						
						if(parseInt(score) > 100){
							score = '100';
							$(this).val(score);
						}
						if(parseInt(score) < -17){
							score = '-17';
							$(this).val(score);
						}
						
						//判断是否为自动计算总评
						if($('#CK_ZPBLXX').val()!='4' && $('#CK_ZPBLXX').val()!='5'){
							//判断是否为数字
							if(!isNaN(score)){
								//计算总评
								$('#spanZp_' + stuCode).html(countZp(stuCode));
							}
						}
					});
					
					//自动保存成绩定时器
					timer = setInterval(function(){
						saveStuScore('saveStuScoreTimer');
					}, 120*1000);
				}else{
					listContent = '<tr><td ';
					if(haveSx == '1'){
						listContent += 'colspan="7"';
					}else{
						listContent += 'colspan="6"';
					}
					listContent += ' class="stuListTd stuListTitle" style="height:100px; color:#8D8583;">没有相关学生信息</td></tr>';
					$('#stuList').append(listContent);
					$('#stuList').show();
				}
				$('#loadingTable').hide();
			}
		});
	}
	
	/**取消输入的0分**/
	function cancelEnterZero(id, stuCode){
		$('#span' + stuCode).html('');
		$('#' + id).val('');
		$('#' + id).focus();
		zeroConfirmFlag = false;
	}
	
	/**进入学生行**/
	function enterStuRow(id){
		var stuCode = id.substring(3);
		
		if(stuCode != curSelStu){
			$('#' + id).css('background-color', '#EAF2FF');
		}
	}
	
	/**离开学生行**/
	function leaveStuRow(id){
		var stuCode = id.substring(3);
		
		if(stuCode != curSelStu){
			$('#' + id).css('background-color', '#ffffff');
		}
	}
	
	/**获取当前点击单元格的id**/
	function clickCell(id){
		if(id!=undefined && id!='')
			curClickCell = id.substring(4);
		else
			curClickCell = '';
	}
	
	/**点击学生成绩行，进入成绩输入状态**/
	function clickStuRow(id){
		//判断是否可登分状态
		if(cjdjFlag == false) return;
	
		selFlag = true;
		var stuCode = id.substring(3);
		var cjlx = $('#CK_CJLX').val();
		
		if(stuCode != curSelStu){
			if(curSelStu != ''){
				//保存之前修改的成绩
				updateScoreArray();
			}
			
			//判断如果是文字成绩
			if($('#stuState_' + stuCode).val()!='2' && cjlx=='2'){
				//初始化文字成绩下拉框
				initWzcjCombo(stuCode);
			}
			
			//判断是否有选择过学生行
			if(curSelStu != ''){
				$('#tr_' + curSelStu).css('background-color', '#ffffff');
				
				if($('#stuState_' + curSelStu).val() != '2'){
					if(cjlx == '1'){
						$('.input_' + curSelStu).hide();
					}else{
						$('.combo_' + curSelStu).hide();
					}
					$('.span_' + curSelStu).show();
				}
			}
			
			$('#' + id).css('background-color', '#FFE48D');
			curSelStu = stuCode;
			//暂存修改前成绩信息
			saveBeforeScore(curSelStu);
			
			for(var i=0; i<stuCodeArray.length; i++){
				if(curSelStu == stuCodeArray[i]){
					stuIndex = i;
					break;
				}
			}
			
			if($('#stuState_' + stuCode).val() != '2'){
				if(cjlx == '1'){
					$('.input_' + curSelStu).each(function(){
						var tempID =  $(this).attr('id').substring(5);
						
						if($(this).val()=='-17' || mkPosArray.indexOf(tempID)>-1){
							$('#span' + tempID).hide();
							$(this).show();
							mkPosArray.push(tempID);
						}
					});
					
					if(document.getElementById('input' + curClickCell) != null && $('#input' + curClickCell).css('display')!='none')
						document.getElementById('input' + curClickCell).focus();
						
					setCursorPosition('input' + curClickCell);
				}else{
					$('.combo_' + curSelStu).show();
					/*
					$('.combo_' + curSelStu).each(function(){
						var id = $(this).children("select").attr('id');
						
						if((id.indexOf('Qm')>-1 || id.indexOf('Zp')>-1) && $('#' + id).combobox('getValue')=='-5'){
							$('#span' + id.substring(5)).show();
						}else{
							$(this).show();
						}
					});
					*/
				}
			}
		}
	}
	
	//移动光标到文本最后
	function setCursorPosition(id){
		var inputObj = document.getElementById(id);
		if(inputObj != null){
			var num = inputObj.value.length;
		
			if(inputObj.selectionStart!=null && inputObj.selectionStart!=undefined){//非IE浏览器
				inputObj.selectionStart = num;
				inputObj.selectionEnd = num;
			}else{//IE
				var range = inputObj.createTextRange();
				range.move("character", num);
				range.select();
			}
		}
	}
	
	/**暂存修改前成绩信息
		@code 学生编号
	**/
	function saveBeforeScore(code){
		if(code == '') return;
	
		tempCjArray.length = 0;
		var cjlx = $('#CK_CJLX').val();
		var zpblxx = $('#CK_ZPBLXX').val();
		var stuState = $('#stuState_' + code).val();
		
		if(cjlx == '1'){
			//判断总评选项是否为仅输总评或者学生状态为免修
			if(zpblxx=='5' || stuState=='2'){
				for(var i=0; i<4; i++){
					tempCjArray.push('');
				}
			}else{
				tempCjArray.push($('#inputPs_' + code).val());
				tempCjArray.push($('#inputQz_' + code).val());
				
				//判断是否有实训
				if($('#CK_SX').val() == '1')
					tempCjArray.push($('#inputSx_' + code).val());
				else
					tempCjArray.push('');
				tempCjArray.push($('#inputQm_' + code).val());
			}
			
			//判断总评是否手动输入或仅输总评
			if(zpblxx=='4' || zpblxx=='5')
				tempCjArray.push($('#inputZp_' + code).val());
			else
				tempCjArray.push(parseScoreValue(cjlx, $('#spanZp_' + code).html()));
		}else if(cjlx == '2'){
			//判断总评选项是否为仅输总评或者学生状态为免修
			if(zpblxx=='5' || stuState=='2'){
				for(var i=0; i<4; i++){
					tempCjArray.push('');
				}
			}else{
				tempCjArray.push($('#comboPs_' + code).combobox('getValue'));
				tempCjArray.push($('#comboQz_' + code).combobox('getValue'));
				
				//判断是否有实训
				if($('#CK_SX').val() == '1')
					tempCjArray.push($('#comboSx_' + code).combobox('getValue'));
				else
					tempCjArray.push('');
				tempCjArray.push($('#comboQm_' + code).combobox('getValue'));
			}
			
			tempCjArray.push($('#comboZp_' + code).combobox('getValue'));
		}
	}
	
	/**初始化文字成绩下拉框
		@code 学生编号
	**/
	function initWzcjCombo(code){
		//检查当前遍历的学生的下拉框控件是否初始化
		var initFlag = false;
		//判断当前行combobox是否已经初始化
		for(var i=0; i<initComboArray.length; i++){
			if(code == initComboArray[i]){
				initFlag = true;
				break;
			}
		}
		if(initFlag == false){
			initComboArray.push(code);
			
			$('.combo_' + code).find('select').each(function(){
				$(this).combobox({
					data:comboOption,
					width:tempWidth,
					valueField:'comboValue',
					textField:'comboName',
					editable:false,
					panelHeight:'140', //combobox高度
					onLoadSuccess:function(data){
						var tempValue = $('#input' + $(this).attr('id').substring(5)).val();
						//初始化combobox时赋值
						$(this).combobox('setValue', tempValue);
					},
					onSelect:function(record){
						var stuCode = $(this).attr('id').substring(5);
						
						$('#input' + stuCode).val(record.comboValue);
						if(record.comboValue == ''){
							$('#span' + stuCode).html('');
						}else{
							$('#span' + stuCode).html(judgeFail('span' + stuCode , record.comboName));
						}
					}
	            });
			});
		}
	}
    
	/**工具栏按钮调用方法，传入按钮的id
		@id 当前按钮点击事件
	**/
	function doToolbar(id){
		//查询课程列表
		if(id == 'queList'){
			loadCourseGrid();
		}
		
		//代码说明
		if(id == 'dmsm'){
			$('#dmsmDialog').dialog('open');
		}
		
		//保存学生成绩
		if(id == 'saveStuScore'){
			saveStuScore(id);
		}
	}
	
	/**填充初始内容*/
	function fillInitContent(){
		$('#kkbj').html('&nbsp;');
		$('#zrs').html('&nbsp;');
		$('#ydfrs').html('&nbsp;');
		$('#wdfrs').html('&nbsp;');
		$('#mxrs').html('&nbsp;');
		$('#qkrs').html('&nbsp;');
		$('#hkrs').html('&nbsp;');
	
		var content = '<tr>' + 
				'<td class="kmInfoTd" colspan="2">分数范围</td>' + 
				'<td class="kmInfoTd" style="width:25%;">人数</td>' + 
				'<td class="kmInfoTd" style="width:25%;">占总人数百分比</td>' + 
			'</tr>' + 
			'<tr>' + 
				'<td class="kmInfoTd" style="width:25%;">90&nbsp;--&nbsp;100</td>' + 
				'<td class="kmInfoTd" style="width:25%;">优</td>' + 
				'<td class="kmInfoTd">&nbsp;</td>' + 
				'<td class="kmInfoTd">&nbsp;</td>' + 
			'</tr>' + 
			'<tr>' + 
				'<td class="kmInfoTd">80&nbsp;--&nbsp;89</td>' +
				'<td class="kmInfoTd">良</td>' +
				'<td class="kmInfoTd">&nbsp;</td>' + 
				'<td class="kmInfoTd">&nbsp;</td>' + 
			'</tr>' + 
			'<tr>' + 
				'<td class="kmInfoTd">70&nbsp;--&nbsp;79</td>' +
				'<td class="kmInfoTd">中</td>' +
				'<td class="kmInfoTd">&nbsp;</td>' + 
				'<td class="kmInfoTd">&nbsp;</td>' + 
			'</tr>' + 
			'<tr>' + 
				'<td class="kmInfoTd">60&nbsp;--&nbsp;69</td>' +
				'<td class="kmInfoTd">及格</td>' +
				'<td class="kmInfoTd">&nbsp;</td>' + 
				'<td class="kmInfoTd">&nbsp;</td>' + 
			'</tr>' + 
			'<tr>' + 
				'<td class="kmInfoTd">&lt;&nbsp;60</td>' +
				'<td class="kmInfoTd">不及格</td>' +
				'<td class="kmInfoTd">&nbsp;</td>' + 
				'<td class="kmInfoTd">&nbsp;</td>' + 
			'</tr>' +
			'<tr>' + 
				'<td class="kmInfoTd">其中</td>' +
				'<td class="kmInfoTd">50&nbsp;--&nbsp;59</td>' +
				'<td class="kmInfoTd">&nbsp;</td>' + 
				'<td class="kmInfoTd">&nbsp;</td>' + 
			'</tr>' +
			'<tr>' + 
				'<td class="kmInfoTd">其中</td>' +
				'<td class="kmInfoTd">1&nbsp;--&nbsp;49</td>' +
				'<td class="kmInfoTd">&nbsp;</td>' + 
				'<td class="kmInfoTd">&nbsp;</td>' + 
			'</tr>' +
			'<tr>' + 
				'<td colspan="2" class="kmInfoTd">平均分</td>' + 
				'<td colspan="2" class="kmInfoTd">&nbsp;</td>' + 
			'</tr>';
		$('#kftjTable').html(content);
	}
	
	/**表单提交**/
	$('#form1').form({
		//定位到servlet位置的url
		url:'<%=request.getContextPath()%>/Svl_Xqcjdj',
		//当点击事件后触发的事件
		onSubmit: function(data){}, 
		//当点击事件并成功提交后触发的事件
		success:function(data){
			var json  =  eval("("+data+")");
			$('#divPageMask').hide();
			
			if(json[0].MSG == '保存成功'){
				var saveType = $('#active').val();
				updateInfo.length = 0;
				
				//保存登分设置
				if(saveType == 'saveDfsz'){
					showMsg(json[0].MSG);
					
					if(curSelCode != ''){
						//读取当前选择班级课程的学生列表
		    			loadStuList(curSelCode);
					}
				}
				
				//保存成绩
				if(saveType == 'saveStuScore'){
					saveBeforeScore(curSelStu);
					
					//判断是否关闭成绩登记对话框触发的保存
					if(closeDialogFlag == true){
						$('#cjdjDialog').dialog('close');
					}
					
					//判断是否切换班级课程触发的保存
					if(changeClassFlag == true){
						changeClassFlag = false;
						
						//读取当前锁定状态
	    				loadLockState();
					}
					
					showMsg(json[0].MSG);
				}
				
				//定时保存成绩
				/*
				if(saveType == 'saveStuScoreTimer'){
					updateInfo.length = 0;
				}
				*/
			}else{
				alertMsg(json[0].MSG);
			}
		}
	});
	
	/**保存之前修改的成绩**/
	function updateScoreArray(){
		//判断是否已选择过学生并且不是免修学生
		if(curSelStu!='' && $('#stuState_' + curSelStu).val()!='2'){
			var curCjArray = new Array();
			var cjlx = $('#CK_CJLX').val();
			var zpblxx = $('#CK_ZPBLXX').val();
			var existFlag = false;
			
			//获取当前学生成绩信息
			if(cjlx == '1'){
				//判断总评比例选项是否为仅输总评
				if(zpblxx != '5'){
					curCjArray.push($('#inputPs_' + curSelStu).val());
					curCjArray.push($('#inputQz_' + curSelStu).val());
					
					//判断是否有实训
					if($('#CK_SX').val() == '1')
						curCjArray.push($('#inputSx_' + curSelStu).val());
					else
						curCjArray.push('');
						
					curCjArray.push($('#inputQm_' + curSelStu).val());
				}else{
					for(var i=0; i<4; i++){
						curCjArray.push('');
					}
				}
				
				//判断总评是否手动输入或仅输总评
				if(zpblxx=='4' || zpblxx=='5')
					curCjArray.push($('#inputZp_' + curSelStu).val());
				else
					curCjArray.push(parseScoreValue(cjlx, $('#spanZp_' + curSelStu).html()));
			}else if(cjlx == '2'){
				//判断总评比例选项是否为仅输总评
				if(zpblxx != '5'){
					curCjArray.push($('#comboPs_' + curSelStu).combobox('getValue'));
					curCjArray.push($('#comboQz_' + curSelStu).combobox('getValue'));
					
					//判断是否有实训
					if($('#CK_SX').val() == '1')
						curCjArray.push($('#comboSx_' + curSelStu).combobox('getValue'));
					else
						curCjArray.push('');
						
					curCjArray.push($('#comboQm_' + curSelStu).combobox('getValue'));
				}else{
					for(var i=0; i<4; i++){
						curCjArray.push('');
					}
				}
				
				//判断总评是否手动输入或仅输总评
				if(zpblxx=='4' || zpblxx=='5')
					curCjArray.push($('#comboZp_' + curSelStu).combobox('getValue'));
				else
					curCjArray.push(parseScoreValue(cjlx, $('#spanZp_' + curSelStu).html()));
			}
			
			//判断是否修改过成绩
			if(tempCjArray[0]!=curCjArray[0] || tempCjArray[1]!=curCjArray[1] || tempCjArray[2]!=curCjArray[2] || tempCjArray[3]!=curCjArray[3] || tempCjArray[4]!=curCjArray[4]){
				for(var i=0; i<updateInfo.length; i+=6){
					if(updateInfo[i] == curSelStu){
						updateInfo[i+1] = curCjArray[0];
						updateInfo[i+2] = curCjArray[1];
						updateInfo[i+3] = curCjArray[2];
						updateInfo[i+4] = curCjArray[3];
						updateInfo[i+5] = curCjArray[4];
						existFlag = true;
						break;
					}
				}
				if(existFlag == false){
					updateInfo.push(curSelStu);
					updateInfo.push(curCjArray[0]);
					updateInfo.push(curCjArray[1]);
					updateInfo.push(curCjArray[2]);
					updateInfo.push(curCjArray[3]);
					updateInfo.push(curCjArray[4]);
				}
			}
		}
	}
	
	/**保存更新的成绩**/
	function saveStuScore(type){
		//判断是否为手动保存
		if(type != 'saveStuScoreTimer'){
			$('#divPageMask').show();
		}
	
		//保存之前修改的成绩
		updateScoreArray();
		
		//20170110yeq为防止个别学生成绩信息丢失，保存所有学生成绩（无论是否修改过）
		if(type == 'saveStuScore'){
			updateInfo.length = 0;
			var tempStuCode = '';
			var cjlx = $('#CK_CJLX').val();
			var zpblxx = $('#CK_ZPBLXX').val();
			
			//判断如果是成绩类型是文字类型并且没有初始化过下拉框,说明未改动过成绩,不做真实保存操作
			if(cjlx=='2' && initComboArray.length==0){
				$('#divPageMask').hide();
				showMsg('保存成功');
				return;
			}
			
			$('.stuListTr').each(function(index){
				if(index > 0){
					tempStuCode = $(this).attr('id').substring(3);
					updateInfo.push(tempStuCode);
					
					//获取当前学生成绩信息
					if(cjlx == '1'){
						//判断总评比例选项是否为仅输总评
						if(zpblxx != '5'){
							updateInfo.push($('#inputPs_' + tempStuCode).val());
							updateInfo.push($('#inputQz_' + tempStuCode).val());
							
							//判断是否有实训
							if($('#CK_SX').val() == '1')
								updateInfo.push($('#inputSx_' + tempStuCode).val());
							else
								updateInfo.push('');
								
							updateInfo.push($('#inputQm_' + tempStuCode).val());
						}else{
							for(var i=0; i<4; i++){
								updateInfo.push('');
							}
						}
						
						//判断总评是否手动输入或仅输总评
						if(zpblxx=='4' || zpblxx=='5')
							updateInfo.push($('#inputZp_' + tempStuCode).val());
						else
							updateInfo.push(parseScoreValue(cjlx, $('#spanZp_' + tempStuCode).html()));
					}else if(cjlx == '2'){
						//初始化下拉框
						initWzcjCombo(tempStuCode);
					
						//判断总评比例选项是否为仅输总评
						if(zpblxx != '5'){
							updateInfo.push($('#comboPs_' + tempStuCode).combobox('getValue'));
							updateInfo.push($('#comboQz_' + tempStuCode).combobox('getValue'));
							
							//判断是否有实训
							if($('#CK_SX').val() == '1')
								updateInfo.push($('#comboSx_' + tempStuCode).combobox('getValue'));
							else
								updateInfo.push('');
								
							updateInfo.push($('#comboQm_' + tempStuCode).combobox('getValue'));
						}else{
							for(var i=0; i<4; i++){
								updateInfo.push('');
							}
						}
						
						//判断总评是否手动输入或仅输总评
						if(zpblxx=='4' || zpblxx=='5')
							updateInfo.push($('#comboZp_' + tempStuCode).combobox('getValue'));
						else
							updateInfo.push(parseScoreValue(cjlx, $('#spanZp_' + curSelStu).html()));
					}
				}
			});
		}
		
		if(updateInfo.length > 0){
			if(type != 'saveStuScoreTimer'){
				$('#divPageMask').show();
			}
			$('#active').val(type);
			if(changeClassFlag == true){
				$('#CX_XGBH').val(preSelCode);
			}else{
				$('#CX_XGBH').val(curSelCode);
			}
			
			$('#updateInfo').val(updateInfo.toString());
			$('#form1').submit();
		}else{
			if(curSelCode!='' && type=='saveStuScore'){
				showMsg('保存成功');
			}
			$('#divPageMask').hide();
		}
	}
	
	/**解析文字成绩显示**/
	function parseScoreShow(type, id, cjlx, zpblxx, score, scoreType){
		var scoreStr = '';
		
		if(cjlx == '2'){
			if(score != ''){
				for(var i=0; i<wzcjShowArray.length; i++){
					if(score == wzcjShowArray[i].id){
						scoreStr = wzcjShowArray[i].text;
					}
				}
			}
		}else{
			if(scoreType=='zp' && zpblxx!='4' && zpblxx!='5' && score==''){
				scoreStr = '-';
			}else{
				if(parseInt(score) < 0){
					for(var i=0; i<wzcjShowArray.length; i++){
						if(score == wzcjShowArray[i].id){
							scoreStr = wzcjShowArray[i].text;
						}
					}
				}else{
					scoreStr = score;
				}
			}
		}
			
		//不及格红色标注
		if(type == 'init'){
			if(scoreStr!='' && ((scoreStr>-1&&scoreStr<60) || scoreStr=='作弊' || scoreStr=='取消资格' || scoreStr=='缺考' || scoreStr=='不及格' || scoreStr=='不合格')){
				bjgScoreArray.push(id);
			}
		}else{
			if(scoreStr!='' && ((scoreStr>-1&&scoreStr<60) || scoreStr=='作弊' || scoreStr=='取消资格' || scoreStr=='缺考' || scoreStr=='不及格' || scoreStr=='不合格')){
				$('#' + id).css('color', '#FF0000');
			}else{
				$('#' + id).css('color', '#000000');
			}
		}
			
		return scoreStr;
	}
	
	/**解析文字成绩值**/
	function parseScoreValue(cjlx, scoreStr){
		var score = '';
		
		if(cjlx == '2'){
			for(var i=0; i<wzcjShowArray.length; i++){
				if(scoreStr == wzcjShowArray[i].text){
					score = wzcjShowArray[i].id;
				}
			}
		}else{
			if(scoreStr == '-'){
				score = '';
			}else{
				for(var i=0; i<wzcjShowArray.length; i++){
					if(scoreStr == wzcjShowArray[i].text){
						score = wzcjShowArray[i].id;
					}
				}
				
				if(score == '')
					score = scoreStr;
			}
		}
		
		return score;
	}
	
	/**计算总评
		@code
	**/
	function countZp(stuCode){
		var cjlx = $('#CK_CJLX').val();
		var psbl = $('#CK_PSBL').val();
		var qzbl = $('#CK_QZBL').val();
		var sxbl = $('#CK_SXBL').val();
		var qmbl = $('#CK_QMBL').val();
		var psScore = '';
		var qzScore = '';
		var sxScore = '';
		var qmScore = '';
		var zpScore = '-';
		$('#spanZp_' + stuCode).css('color', '#000000');
		
		if(cjlx == '1'){
			if(stuCode != ''){
				psScore = $('#inputPs_' + stuCode).val();
				qzScore = $('#inputQz_' + stuCode).val();
				sxScore = $('#inputSx_' + stuCode).val();
				qmScore = $('#inputQm_' + stuCode).val();
			}else{
				psScore = $('#pldf_ps').val();
				qzScore = $('#pldf_qz').val();
				sxScore = $('#pldf_sx').val();
				qmScore = $('#pldf_qm').val();
			}
		}
		
		//特殊代码成绩转换
		if(psScore==-2 || psScore==-3 || psScore==-4) psScore = 0;
		if(qzScore==-2 || qzScore==-3 || qzScore==-4) qzScore = 0;
		if(sxScore==-2 || sxScore==-3 || sxScore==-4) sxScore = 0;
		if(qmScore==-2 || qmScore==-3 || qmScore==-4) qmScore = 0;
		
		//判断是否需要计算总评,如果没有填写分数或有成绩代码,不计算总评
		if(psbl!='' && ((psScore=='' && psScore!=0) || psScore<0)) return zpScore;
		if(qzbl!='' && ((qzScore=='' && qzScore!=0) || qzScore<0)) return zpScore;
		if(sxbl!='' && ((sxScore=='' && sxScore!=0) || sxScore<0)) return zpScore;
		if(qmbl!='' && ((qmScore=='' && qmScore!=0) || qmScore<0)) return zpScore;
		
		zpScore = 0;
		if(psbl != '') zpScore += psScore*psbl/100;
		if(qzbl != '') zpScore += qzScore*qzbl/100;
		if(sxbl != '') zpScore += sxScore*sxbl/100;
		if(qmbl != '') zpScore += qmScore*qmbl/100;
		
		if(!isNaN(zpScore)){
			zpScore = Math.round(zpScore);
			
			if(zpScore!=''&&zpScore>-1&&zpScore<60){
				$('#spanZp_' + stuCode).css('color', '#FF0000');
			}
		}
		
		return zpScore;
	}
	
	//上下键选中行
	document.onkeydown=selectRow;
	function selectRow(e){
		var flag = true;
		
		//判断是否选中了行
		if(selFlag == false)
			flag = false;
		
		//ie火狐兼容
		//e = e || window.event; 
		//var curKey = e.which || e.keyCode;
		e = (e) ? e : ((window.event) ? window.event : "");
		var curKey = e.keyCode ? e.keyCode : (e.which ? e.which : e.charCode);
		
		//判断如果需要做零分判断的话，不允许操作
		if (curKey==37 || curKey==38 || curKey==39 || curKey == 40){
			if($('#input' + curClickCell).val() == '0'){
				$('#input' + curClickCell).blur();
				flag = false;
			}
		}
		
		if(flag){
			if (curKey == 37){//左
				if(curSelStu != ''){
					if(curClickCell != ''){
						var title = curClickCell.substring(0, curClickCell.indexOf('_'));
						var targetTitle = '';
						
						for(var i=0; i<titleArray.length; i++){
							if(title==titleArray[i] && i>0){
								targetTitle = titleArray[i-1];
							}
						}
						
						if(targetTitle != ''){
							$('#input' + targetTitle + curClickCell.substring(curClickCell.indexOf('_'))).focus();
							curClickCell = targetTitle + curClickCell.substring(curClickCell.indexOf('_'));
							setCursorPosition('input' + curClickCell);
						}
					}
				}
				return false;
			}
			if (curKey == 38){//上
				if(stuIndex > 0){
					if(curClickCell != '')
						curClickCell = curClickCell.substring(0, curClickCell.indexOf('_')+1)+stuCodeArray[stuIndex-1];
					clickStuRow('tr_' + stuCodeArray[stuIndex-1]);
				}
				return false;
			}
			if (curKey == 39){//右
				if(curSelStu != ''){
					if(curClickCell != ''){
						var title = curClickCell.substring(0, curClickCell.indexOf('_'));
						var targetTitle = '';
						
						for(var i=0; i<titleArray.length; i++){
							if(title==titleArray[i] && i<titleArray.length-1){
								targetTitle = titleArray[i+1];
							}
						}
						
						if(targetTitle != ''){
							$('#input' + targetTitle + curClickCell.substring(curClickCell.indexOf('_'))).focus();
							curClickCell = targetTitle + curClickCell.substring(curClickCell.indexOf('_'));
							setCursorPosition('input' + curClickCell);
						}
					}
				}
				return false;
			}
			if (curKey == 40){//下 
				if(stuIndex < stuCodeArray.length-1){
					if(curClickCell != '')
						curClickCell = curClickCell.substring(0, curClickCell.indexOf('_')+1)+stuCodeArray[stuIndex+1];
					clickStuRow('tr_' + stuCodeArray[stuIndex+1]);
				}
				return false;
			}
		}		
	}
	
	function judgeFail(id, score){
		if(score=='作弊' || score=='取消资格' || score=='缺考' || score=='不及格' || score=='不合格'){
			$('#' + id).css('color', '#FF0000');
		}else{
			$('#' + id).css('color', '#000000');
		}
		
		return score;
	}
</script>
</html>