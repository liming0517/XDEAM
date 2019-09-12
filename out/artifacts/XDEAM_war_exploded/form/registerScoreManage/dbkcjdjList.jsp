<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="java.util.Vector"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.src.develop.store.user.*"%>
<%
	/**
		创建人：yeq
		Create date: 2016.11.03
		功能说明：用于大补考成绩登记
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
	<title>大补考成绩登记</title>
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
		
		.pointFont{
			margin-left:1px;
			margin-right:1px;
			color:red;
		}
		
		.zpInfoWidth{
			width:65px;
		}
		
		.configInfo{
			height:27px;
		}
		
		.pldfInput{
			height:28px;
			font-size:16;
			text-align:center;
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
	
	<div region="north" title="大补考科目信息" style="height:59px;">
		<table id="queryCondition" singleselect="true" width="100%" class="tablestyle">
			<tr>
				<td style="width:150px;" class="titlestyle">大补考学年</td>
				<td>
					<select name="XN_CX" id="XN_CX" class="easyui-combobox" style="width:145px;">
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
				<td style="width:150px;" align="center">
					<a href="#" onclick="doToolbar(this.id)" id="queList" class="easyui-linkbutton" plain="true" iconcls="icon-search">查询</a>
				</td>
			</tr>
	    </table>
	</div>
	<div region="center">
		<table id="subjectList" style="width:100%;"></table>
	</div>
	
	<!-- 成绩登记 -->
	<div id="cjdjDialog" style="overflow:hidden;">
		<%-- 遮罩层 --%>
    	<div id="divPageMask" class="maskStyle">
    		<div id="maskFont">学生成绩保存中...</div>
    	</div>
    	
		<div id="kcbMain" style="width:100%; height:100%;" class="easyui-layout">
			<div region="north" title="学生成绩列表" style="height:61px;" >
				<table style="width:100%;">
					<tr>
						<td>
							<a href="#" id="saveStuScore" class="easyui-linkbutton" plain="true" iconcls="icon-save" onClick="doToolbar(this.id);">成绩保存</a>
							<a href="#" id="pldf" class="easyui-linkbutton" plain="true" iconcls="icon-edit" onClick="doToolbar(this.id);">批量登分</a>
							<a href="#" id="dmsm" class="easyui-linkbutton" plain="true" iconcls="icon-assets" onClick="doToolbar(this.id);">成绩代码说明</a>
						</td>
					</tr>
				</table>
			</div>
			<div region="center" id="stuContent">
				<table id="stuListMask" style="width:100%; height:100%; text-align:center; display:none;">
					<tr><td><img src="<%=request.getContextPath()%>/images/loading_3.gif"/></td></tr>
				</table>
				<table id="stuList" cellspacing="0" cellpadding="0">
				</table>
			</div>
		</div>
		
		<form id="form1" method="post">
			<input type="hidden" id="active" name="active"/>
			<input type="hidden" id="updateInfo" name="updateInfo"/>
		</form>
	</div>
	
	<!-- 批量登分对话框 -->
	<div id="pldfDialog">
		<div class="easyui-layout" style="width:100%; height:100%;">
			<div region="north" style="height:34px;">
				<table>
					<tr>
						<td><a href="#" id="confirm" class="easyui-linkbutton" plain="true" iconcls="icon-ok" onClick="doToolbar(this.id);">确认</a></td>
					</tr>
				</table>
			</div>
			<div region="center">
				<table id="dfStuList" style="width:100%;"></table>
			</div>
			<div region="south" title="成绩登记" style="height:90px; overflow:hidden;">
				<table id="dfTable" style="width:100%; text-align:center;" cellspacing="0" cellpadding="0">
				</table>
			</div>
		</div>
	</div>
	
	<!-- 文字成绩代码说明 -->
	<div id="dmsmDialog">
		<table class="tablestyle" style="width:100%; height:100%; text-align:center;" cellspacing="0" cellpadding="0">
			<tr><td style="width:50%; font-weight:bold;">代码</td><td style="width:50%; font-weight:bold;">名称</td></tr>
			<!-- 
			<tr><td>-1</td><td>免修</td></tr>
			<tr><td>-2</td><td>作弊</td></tr>
			<tr><td>-3</td><td>取消资格</td></tr>
			<tr><td>-4</td><td>缺考</td></tr>
			<tr><td>-5</td><td>缓考</td></tr>
			<tr><td>-6</td><td>优</td></tr>
			<tr><td>-7</td><td>良</td></tr>
			<tr><td>-8</td><td>中</td></tr>
			<tr><td>-9</td><td>及格</td></tr>
			<tr><td>-10</td><td>不及格</td></tr>
			<tr><td>-11</td><td>合格</td></tr>
			<tr><td>-12</td><td>不合格</td></tr>
			<tr><td>-13</td><td>补及</td></tr>
			<tr><td>-15</td><td>达标</td></tr>
			<tr><td>-16</td><td>重考</td></tr>
			 -->
			<tr><td>-4</td><td>缺考</td></tr>
			<tr><td>-9</td><td>及格</td></tr>
			<tr><td>-10</td><td>不及格</td></tr>
		</table>
	</div>
</body>
<script type="text/javascript">
	var sAuth = '<%=sAuth%>';
	var curSelCode = '';//当前选择学生成绩的相关编号
	var curSelStu = '';//当前选择的学生编号
	var tempCjArray = new Array();//当前选中的学生未改动前的成绩
	var tempWidth = '';//控件宽度
	var updateInfo = new Array();//用于保存更新的成绩信息
	var timer = '';
	var selFlag = false;
	var stuCodeArray = new Array();
	var stuIndex = 0;
	var closeDialogFlag = false;
	var cjdjFlag = true;//用于判断当前时间或者权限是否允许登记成绩
	var admin = '<%=MyTools.getProp(request, "Base.admin")%>';//管理员
	var wzcjShowArray = '';
	var curClickCell = '';
	var curXn = '';
	var comboOption = '';
	var initComboArray = new Array();//用于存放已经初始化combobox的学生编号
	var cjlx = '2';
	var bjgScoreArray = new Array();
	
	$(document).ready(function(){
		$('#mask').hide();
		initComboData();//页面初始化加载数据
		initDialog();//初始化对话框
	});
	
	/**页面初始化加载数据**/
	function initComboData(){
		$.ajax({
			type:"POST",
			url:'<%=request.getContextPath()%>/Svl_Dbkcjdj',
			data:'active=loadComboData',
			dataType:"json",
			success:function(data) {
				curXn = data[0].curXn;
				comboOption = data[0].wzcjData;
				wzcjShowArray = data[0].wzcjShowData;
				initCombobox(data[0].xnData);
			}
		});
	}
	
	/**加载combobox控件
		@xnData 学年下拉框数据
	**/
	function initCombobox(xnData){
		$('#XN_CX').combobox({
			data:xnData,
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
				loadCourseGrid();
			},
			//下拉列表值改变事件
			onChange:function(data){}
		});
	}
	
	/**加载 dialog控件**/
	function initDialog(){
		$('#cjdjDialog').dialog({
			title:'大补考登分',
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
				$('#stuList').hide();
				$('#stuList').html('');
				curSelCode = '';
				curSelStu = '';
				closeDialogFlag = false;
			}
		});
		
		$('#pldfDialog').dialog({   
			title: '批量登分',   
			width: 300,//宽度设置   
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
				$('#dfStuList').datagrid('unselectAll');
				$('#dfTable').html('');
			}
		});
		
		$('#dmsmDialog').dialog({   
			title: '文字成绩代码说明',
			width: 300,//宽度设置   
			height: 160,//高度设置 
			modal:true,
			closed: true,   
			cache: false, 
			draggable:true,//是否可移动dialog框设置
			//读取事件
			onLoad:function(data){
			},
			//关闭事件
			onClose:function(data){
			}
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
			url: '<%=request.getContextPath()%>/Svl_Dbkcjdj',
			queryParams:{'active':'queSubjectList','sAuth':sAuth,'XN_CX':$('#XN_CX').combobox('getValue'),
				'KCMC_CX':encodeURI($('#KCMC_CX').textbox('getValue')),'KCLX_CX':$('#KCLX_CX').combobox('getValue')},
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
				{field:'大补考学年',title:'大补考学年',width:fillsize(0.15),align:'center'},
				/*{field:'专业名称',title:'专业名称',width:fillsize(0.3),align:'center'},*/
				{field:'课程名称',title:'课程名称',width:fillsize(0.55),align:'center'},
				{field:'课程类型',title:'课程类型',width:fillsize(0.2),align:'center'},
				{field:'登分时间',hidden:true},
				{field:'col4',title:'操作',width:fillsize(0.1),align:'center',
					formatter:function(value,rec){
						var button = "<input type='button' ";
						
						//判断如果在登分时间内或者是管理员权限
						if(rec.登分时间=='true' || sAuth.indexOf(admin)>-1)
							button += "value='[成绩登记]' onclick='openCjdjDialog(\"" + rec.大补考学年 + "\",\"" + rec.课程名称 + "\", \"set\");' style=\"cursor:pointer;\">";
						else
							button += "value='[成绩查询]' onclick='openCjdjDialog(\"" + rec.大补考学年 + "\",\"" + rec.课程名称 + "\", \"view\");' style=\"cursor:pointer;\">";
							
						return button;
				}}
			]],
			//双击某行时触发
			onDblClickRow:function(rowIndex,rowData){},
			//读取datagrid之前加载
			onBeforeLoad:function(){},
			//单击某行时触发
			onClickRow:function(rowIndex,rowData){
			},
			//加载成功后触发
			onLoadSuccess: function(data){
			}
		});
	};
	
	/**打开成绩登记对话框**/
	function openCjdjDialog(dbkxn, kcmc, type){
		if(type == 'set'){
			cjdjFlag = true;
			$('#saveStuScore').show();
			$('#pldf').show();
		}else{
			cjdjFlag = false;
			$('#saveStuScore').hide();
			$('#pldf').hide();
		}
			
		//读取当前选择班级课程的学生列表
		loadStuList(dbkxn, kcmc);
		$('#cjdjDialog').dialog('open');   	
	}
	
	/**读取当前选择班级课程的学生列表
		@dbkxn 大补考学年
		@kcmc 课程名称
	**/
	function loadStuList(dbkxn, kcmc){
		clearInterval(timer);//结束自动保存成绩定时器
		$('#stuList').hide();
		$('#stuListMask').show();
	    $('#stuList').html('');
	    selFlag = false;
	    curSelCode = '';
	    curSelStu = '';
	    initComboArray.length = 0;
	
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Dbkcjdj',
			data : 'active=loadStuList&sAuth=' + sAuth + '&DBKXN=' + dbkxn + '&KCMC=' + encodeURI(kcmc),
			dataType:"json",
			success : function(data) {
				var listContent = '';
				var xgbh = '';
				var num = '';
				var stuCode = '';
				var stuName = '';
				var bjmc = '';
				var xnxq = '';
				var kcmc = '';
				var dbk = '';
				var titleWidth = '12%';
				var stuDataJson = '';
				
				listContent = '<tr class="stuListTr">' + 
								'<td class="stuListTd stuListTitle" style="width:50px;">&nbsp;</td>' +
								'<td class="stuListTd stuListTitle titleWidth">姓名</td>' +
								'<td class="stuListTd stuListTitle titleWidth">学号</td>' +
								'<td class="stuListTd stuListTitle" style="width:24%;">班级名称</td>' +
								'<td class="stuListTd stuListTitle titleWidth">学年学期</td>' +
								'<td class="stuListTd stuListTitle" style="width:24%;">课程名称</td>' +
								'<td class="stuListTd stuListTitle titleWidth">大补考成绩</td>';
							'</tr>';
				$('#stuList').append(listContent);
				$('.titleWidth').css('width', titleWidth);		
				
				stuCodeArray.length = 0;
				bjgScoreArray.length = 0;
				
				for(var i=0; i<data.length; i++){
					xgbh = data[i].xgbh;
					num = data[i].num;
					stuName = data[i].stuName;
					stuCode = data[i].stuCode;
					stuCodeArray.push(stuCode);
					bjmc = data[i].className;
					xnxq = data[i].xnxq;
					kcmc = data[i].kcmc;
					dbk = data[i].dbk;
					
					stuDataJson += '{"code":"' + xgbh + '","stuCode":"'+stuCode+'","stuName":"'+stuName+'"},';
					
					listContent = '<tr id="tr_' + stuCode + '" class="stuListTr stuScoreInfo" onmouseenter="enterStuRow(this.id);" onmouseleave="leaveStuRow(this.id);" onclick="clickStuRow(this.id)">' +
									'<td class="stuListTd" style="font-size:16;" onclick="clickCell(\'\');">' + num + '</td>' +
									'<td class="stuListTd" style="font-size:16;" onclick="clickCell(\'\');">' + stuName + '</td>' +
									'<td class="stuListTd" style="font-size:16;" onclick="clickCell(\'\');">' + stuCode + '</td>' +
									'<td class="stuListTd" style="font-size:16;" onclick="clickCell(\'\');">' + bjmc + '</td>' +
									'<td class="stuListTd" style="font-size:16;" onclick="clickCell(\'\');">' + xnxq + '</td>' +
									'<td class="stuListTd" style="font-size:16;" onclick="clickCell(\'\');">' + kcmc + '</td>';
					//大补考
					listContent += '<td class="stuListTd" onclick="clickCell($(this).find(\'span\').attr(\'id\'));">';
					listContent += '<span id="spanDbk_' + stuCode + '" class="span_' + stuCode + ' scoreSpan">' + parseScoreShow('init', 'spanDbk_' + stuCode, cjlx, dbk, 'dbk') + '</span>';
					if(cjdjFlag == true){
						listContent += '<input id="inputDbk_' + stuCode + '" class="input_' + stuCode + ' scoreInput" value="' + dbk + '"/>' +
							'<div class="combo_' + stuCode + ' scoreCombo"><select id="comboDbk_' + stuCode + '" class="easyui-combobox"></select></div>' +
							'<input type="hidden" id="xgbh_' + stuCode + '" value="' + xgbh + '"/>';
					}
					listContent += '</td></tr>';
								
					$('#stuList').append(listContent);
				}
				//不及格红色标注
				for(var i=0; i<bjgScoreArray.length; i++){
					$('#' + bjgScoreArray[i]).css('color', '#FF0000');
				}
				
				$('#stuListMask').hide();
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
					}
					
					//数字
					if(curKey>=48 && curKey<=57) return true;
					//小数字键盘
					if(curKey>=96 && curKey<=105) return true;
					//减号
					if(curValue=='' && (curKey==109 || curKey==189)) return true;
					
					return false;
				}).bind('blur',function(){
					var id = $(this).attr('id');
					var score = $(this).val();
					var stuCode = $(this).attr('id').substring(5);
					
					if(score != ''){
						if(score!='-4' && score!='-10' && score!='-13' && score!='-15'){
							$.messager.alert('提示','补考成绩只允许输入相关成绩代码（可查看成绩代码说明），请重新输入！','',function(){
								score = '';
								$('#' + id).val('');
								$('#' + id).focus();
							});
						}else{
							for(var i=0; i<wzcjShowArray.length; i++){
								if(score == wzcjShowArray[i].id){
									score = wzcjShowArray[i].text;
								}
							}
						}
					}
					$('#span' + stuCode).html(score);
					
					/*
					if(score == '-1'){
						score = '';
						$(this).val(score);
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
						
						if(score == '0'){
							$('#span' + stuCode).html(score);
							ConfirmMsg('确定当前输入的成绩是<span style="width:15px; color:red; text-align:center;">0</span>分吗？', '', 'cancelEnterZero("'+$(this).attr('id')+'","' + stuCode + '");');
							return;
						}
					}
					if(parseInt(score) < 0){
						for(var i=0; i<wzcjShowArray.length; i++){
							if(score == wzcjShowArray[i].id){
								score = wzcjShowArray[i].text;
							}
						}
					}
					
					$('#span' + stuCode).html(score);
					*/
				}).bind('keyup', function(e){
					//ie火狐兼容
					//e = e || window.event;
					//var curKey = e.which || e.keyCode;
					e = (e) ? e : ((window.event) ? window.event : "");
            		var curKey = e.keyCode ? e.keyCode : (e.which ? e.which : e.charCode);
					var score = $(this).val();
					
					//应对中文输入法输入负号时，键值为229被屏蔽的情况。
					if(score=='' && curKey=='189'){
						$(this).val('-');
					}
					
					if(score > 100){
						score = '100';
						$(this).val(score);
					}
					if(score < -16){
						score = '-16';
						$(this).val(score);
					}
				});
				
				//自动保存成绩定时器
				timer = setInterval(function(){
					saveStuScore('saveStuScoreTimer');
				}, 120*1000);
				
				//批量登分学生列表
				if(stuDataJson.length > 1){
					stuDataJson = '['+stuDataJson.substring(0, stuDataJson.length-1)+']';
				}else{
					stuDataJson = '[]';
				}
				stuDataJson = eval("("+stuDataJson+")");
				loadPldfStuList(stuDataJson);
			}
		});
	}
	
	/**取消输入的0分**/
	function cancelEnterZero(id, stuCode){
		$('#span' + stuCode).html('');
		$('#' + id).val('');
		$('#' + id).focus();
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
		
		if(stuCode != curSelStu){
			if(curSelStu != ''){
				//保存之前修改的成绩
				updateScoreArray();
			}
			
			//判断如果是文字成绩
			if(cjlx == '2'){
				//初始化文字成绩下拉框
				initWzcjCombo(stuCode);
			}
			
			//判断是否有选择过学生行
			if(curSelStu != ''){
				$('#tr_' + curSelStu).css('background-color', '#ffffff');
				if(cjlx == '1'){
					$('.input_' + curSelStu).hide();
				}else{
					$('.combo_' + curSelStu).hide();
				}
				$('.span_' + curSelStu).show();
			}
			
			$('#' + id).css('background-color', '#FFE48D');
			curSelCode = $('#xgbh_' + stuCode).val();
			curSelStu = stuCode;
			
			//暂存修改前成绩信息
			saveBeforeScore(curSelStu);
			
			//查询当前选中学生的行号
			for(var i=0; i<stuCodeArray.length; i++){
				if(curSelStu == stuCodeArray[i]){
					stuIndex = i;
					break;
				}
			}
			
			$('.span_' + curSelStu).hide();
			if(cjlx == '1'){
				$('.input_' + curSelStu).show();
				
				if(document.getElementById('input' + curClickCell) != null)
					document.getElementById('input' + curClickCell).focus();
					
				setCursorPosition('input' + curClickCell);
			}else{
				$('.combo_' + curSelStu).show();
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
		tempCjArray.length = 0;
		
		if(cjlx == '1'){
			tempCjArray.push($('#inputDbk_' + code).val());
		}else if(cjlx == '2'){
			tempCjArray.push($('#comboDbk_' + code).combobox('getValue'));
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
		
		//批量登分
		if(id == 'pldf'){
			var content = '';
			var titleWidth = '100%';
			var comboWidth = '280px';
			
			//生成登分控件
			content = '<tr class="stuListTr">' + 
					'<td class="stuListTd stuListTitle dfTitleWidth">大补考</td</tr>';
			$('#dfTable').append(content);
			$('.dfTitleWidth').css('width', titleWidth);
			
			if(cjlx == '1'){//数字成绩
				content = '<tr>' + 
						'<td class="stuListTd"><input id="pldf_dbk" class="pldfInput"/></td><tr>';
				$('#dfTable').append(content);
				
				$('.pldfInput').css('width', '100%');
				$('.pldfInput').bind('keydown', function(e){
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
					}
					
					//数字
					if(curKey>=48 && curKey<=57) return true;
					//小数字键盘
					if(curKey>=96 && curKey<=105) return true;
					//减号
					if(curValue=='' && (curKey==109 || curKey==189)) return true;
					
					return false;
				}).bind('keyup', function(e){
					//ie火狐兼容
					//e = e || window.event;
					//var curKey = e.which || e.keyCode;
					e = (e) ? e : ((window.event) ? window.event : "");
            		var curKey = e.keyCode ? e.keyCode : (e.which ? e.which : e.charCode);
					var score = $(this).val();
					
					//应对中文输入法输入负号时，键值为229被屏蔽的情况。
					if(score=='' && curKey=='189'){
						$(this).val('-');
					}
					
					if(score > 100){
						score = '100';
						$(this).val(score);
					}
					if(score < -16){
						score = '-16';
						$(this).val(score);
					}
				}).bind('blur',function(){
					var id = $(this).attr('id');
					var score = $(this).val();
					
					if(score != ''){
						if(score!='-4' && score!='-9' && score!='-10'){
							$.messager.alert('提示','补考成绩只允许输入相关成绩代码（可查看成绩代码说明），请重新输入！','',function(){
								score = '';
								$('#' + id).val('');
								$('#' + id).focus();
							});
						}
					}
				});
			}else if(cjlx == '2'){//文字成绩
					content = '<tr>' + 
							'<td class="stuListTd"><select id="pldf_dbk" class="easyui-combobox pldfCombo"></select></td></tr>';
				$('#dfTable').append(content);
				
				$('.pldfCombo').combobox({
					data:comboOption,
					width:comboWidth,
					height:28,
					valueField:'comboValue',
					textField:'comboName',
					editable:false,
					panelHeight:'140', //combobox高度
					onLoadSuccess:function(data){
						$(this).combobox('setValue', '');
					}
	            });
			}
			
			//生成登分控件
			content = '<tr class="stuListTr"><td class="stuListTd stuListTitle dfTitleWidth">大补考</td</tr>' + 
					'<tr><td class="stuListTd"><input id="pldf_dbk" class="pldfInput"/></td></tr>';
			$('#dfTable').append(content);
			$('.dfTitleWidth').css('width', titleWidth);
			
			$('.pldfInput').css('width', '100%');
			$('.pldfInput').bind('keydown', function(e){
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
				}
				
				//数字
				if(curKey>=48 && curKey<=57) return true;
				//小数字键盘
				if(curKey>=96 && curKey<=105) return true;
				//减号
				if(curValue=='' && (curKey==109 || curKey==189)) return true;
				
				return false;
			}).bind('keyup', function(e){
				//ie火狐兼容
				//e = e || window.event;
				//var curKey = e.which || e.keyCode;
				e = (e) ? e : ((window.event) ? window.event : "");
           		var curKey = e.keyCode ? e.keyCode : (e.which ? e.which : e.charCode);
				var score = $(this).val();
				
				//应对中文输入法输入负号时，键值为229被屏蔽的情况。
				if(score=='' && curKey=='189'){
					$(this).val('-');
				}
				
				if(score > 100){
					score = '100';
					$(this).val(score);
				}
				if(score < -16){
					score = '-16';
					$(this).val(score);
				}
			}).bind('blur',function(){
				var id = $(this).attr('id');
				var score = $(this).val();
				
				if(score != ''){
					if(score!='-4' && score!='-10' && score!='-13' && score!='-15'){
						$.messager.alert('提示','补考成绩只允许输入相关成绩代码（可查看成绩代码说明），请重新输入！','',function(){
							score = '';
							$('#' + id).val('');
							$('#' + id).focus();
						});
					}
				}
				
				/*
				if(score == '-1'){
					score = '';
					$(this).val(score);
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
				}
				*/
			});
			
			$('#pldfDialog').dialog('open');
		}
		
		//确认批量登分
		if(id == 'confirm'){
			var stuArray = $('#dfStuList').datagrid('getSelections');
			
			if(stuArray.length == 0){
				alertMsg('请至少选择一名学生');
				return;
			}
			
			var tempCode = '';
			var tempStuCode = '';
			var tempSelStu = '';
			var dbkScore = '';
			
			if(cjlx == '1'){
				dbkScore = $('#pldf_dbk').val();
				if(isNaN(dbkScore)){
					alertMsg('大补考成绩请输入正确数字');
					return;
				}
			}else if(cjlx == '2'){
				dbkScore = $('#pldf_dbk').combobox('getValue');
			}
			
			for(var i=0; i<stuArray.length; i++){
				tempCode = stuArray[i].code;
				tempStuCode = stuArray[i].stuCode;
				
				if(cjlx == '2'){
					//初始化文字成绩下拉框
					initWzcjCombo(tempStuCode);
				}
				
				//暂存修改前成绩信息
				saveBeforeScore(tempStuCode);
				
				//显示框
				$('#spanDbk_' + tempStuCode).html(parseScoreShow('pldf', 'spanDbk_' + tempStuCode, cjlx, dbkScore, 'dbk'));
				//input输入框
				$('#inputDbk_' + tempStuCode).val(dbkScore);
				
				if(cjlx == '1'){
					//显示框
					$('#spanDbk_' + tempStuCode).html(parseScoreShow('pldf', 'spanDbk_' + tempStuCode, cjlx, dbkScore, 'bk'));
					//input输入框
					$('#inputDbk_' + tempStuCode).val(dbkScore);
				}else if(cjlx == '2'){
					$('#comboDbk_' + tempStuCode).combobox('setValue', dbkScore);
					//input输入框
					$('#inputDbk_' + tempStuCode).val(dbkScore);
					//显示框
					if(dbkScore == '')
						$('#spanDbk_' + tempStuCode).html('');
					else
						$('#spanDbk_' + tempStuCode).html(judgeFail('spanDbk_' + tempStuCode, $('#comboDbk_' + tempStuCode).combobox('getText')));
				}
				
				tempSelCode = curSelCode;
				tempSelStu = curSelStu;
				curSelCode = tempCode;
				curSelStu = tempStuCode;
				
				//保存之前修改的成绩
				updateScoreArray();
				
				curSelCode = tempSelCode;
				curSelStu = tempSelStu;
			}
			
			$('#pldfDialog').dialog('close');
		}
	}
	
	/**表单提交**/
	$('#form1').form({
		//定位到servlet位置的url
		url:'<%=request.getContextPath()%>/Svl_Dbkcjdj',
		//当点击事件后触发的事件
		onSubmit: function(data){}, 
		//当点击事件并成功提交后触发的事件
		success:function(data){
			var json  =  eval("("+data+")");
			if(json[0].MSG == '保存成功'){
				$('#divPageMask').hide();
				updateInfo.length = 0;
				
				saveBeforeScore(curSelStu);
				//判断是否关闭成绩登记对话框触发的保存
				if(closeDialogFlag == true){
					$('#cjdjDialog').dialog('close');
				}
				
				showMsg(json[0].MSG);
			}else{
				alertMsg(json[0].MSG);
			}
		}
	});
	
	/**保存之前修改的成绩**/
	function updateScoreArray(){
		//判断是否已选择过学生
		if(curSelStu != ''){
			var score = '';
			
			if(cjlx == '1'){
				score = $('#inputDbk_' + curSelStu).val();
			}else{
				score = $('#comboDbk_' + curSelStu).combobox('getValue');
			}
			
			//判断当前成绩是否符合要求
			if(score=='' || score=='-4' || score=='-9' || score=='-10'){
				var curCjArray = new Array();
				var existFlag = false;
			
				//获取当前学生成绩信息
				curCjArray.push(score);
				
				//判断是否修改过成绩
				if(tempCjArray[0] != curCjArray[0]){
					for(var i=0; i<updateInfo.length; i+=3){
						if(updateInfo[i]==curSelStu && updateInfo[i+1]==curSelCode){
							updateInfo[i+2] = curCjArray[0];
							existFlag = true;
							break;
						}
					}
					if(existFlag == false){
						updateInfo.push(curSelStu);
						updateInfo.push(curSelCode);
						updateInfo.push(curCjArray[0]);
					}
				}
			}
		}
	}
	
	/**批量登分学生列表**/
	function loadPldfStuList(stuData){
		$('#dfStuList').datagrid({
			data:stuData,
			title:'',
			width:'100%',
			nowrap: false,
			fit:true, //自适应父节点宽度和高度
			showFooter:true,
			striped:true,
			singleSelect:false,
			rownumbers:true,
			fitColumns: true,
			columns:[[
				//field为读取数据的数据名，title为显示的数据名，width宽度设置，align数字在表格中显示的位置
				{field:'ck',checkbox:true},
				{field:'code',hidden:true},
				{field:'stuCode',title:'学号',width:fillsize(0.5),align:'center'},
				{field:'stuName',title:'姓名',width:fillsize(0.5),align:'center'}
			]],
			//加载成功后触发
			onLoadSuccess: function(data){
			}
		});
	}
	
	/**保存更新的成绩**/
	function saveStuScore(type){
		//保存之前修改的成绩
		updateScoreArray();
		
		if(updateInfo.length > 0){
			if(type != 'saveStuScoreTimer'){
				$('#divPageMask').show();
			}
			$('#active').val(type);
			
			$('#updateInfo').val(updateInfo.toString());
			$('#form1').submit();
		}else{
			if(curSelCode!='' && type=='saveStuScore'){
				showMsg('保存成功');
			}
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
							$('#span' + stuCode).html(judgeFail('span' + stuCode, record.comboName));
						}
					}
	            });
			});
		}
	}
	
	/**解析文字成绩显示**/
	function parseScoreShow(type, id, cjlx, score, scoreType){
		var scoreStr = '';
		
		if(cjlx == '2'){
			if(score != ''){
				if(score == '-5'){
					scoreStr = '缓考';
				}else{
					for(var i=0; i<comboOption.length; i++){
						if(score == comboOption[i].comboValue){
							scoreStr = comboOption[i].comboName;
							break;
						}
					}
				}
			}
			
			if(scoreStr == '')
				scoreStr = score;
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
		if (curKey==38 || curKey == 40){
			if($('#input' + curClickCell).val() == '0'){
				$('#input' + curClickCell).blur();
				flag = false;
			}
		}
		
		if(flag){
			if (curKey == 38){//上
				if(stuIndex > 0){
					if(curClickCell != '')
						curClickCell = curClickCell.substring(0, curClickCell.indexOf('_')+1)+stuCodeArray[stuIndex-1];
					clickStuRow('tr_' + stuCodeArray[stuIndex-1]);
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