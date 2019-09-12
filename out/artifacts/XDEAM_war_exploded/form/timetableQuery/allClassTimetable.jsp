<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="java.util.Vector"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.src.develop.store.user.*"%>
<%
	/**
		创建人：yeq
		Create date: 2015.06.24
		功能说明：用于查看全部班级课表
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
	<title>课程表信息</title>
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
		.kcbTable{
			border-top:1px solid #95B8E7;
			float:left;
		}
		
		.kcbTrStyle{
			height:55px;
		}
		
		.kcbTdStyle{
			font-size:12;
			border-left:1px solid #95B8E7;
			border-bottom:1px solid #95B8E7;
			text-align:center;
		}
		
		.kcbTitle{
			 font-weight:bold;
			 font-size:14;
		}
		
		.splitTd{
			border-bottom:4px double #95B8E7;
		}
		
		.titleBG{
			background-color:#EFEFEF;
		}
		
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
		
		#maskFont2{
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
		
		#uploadDiv{
			position:relative;
			width:56px;
			height:26px;
			border:1px solid #ffffff;
			background-color:#ffffff;
			cursor:pointer;
			overflow:hidden;
		}
	
		#File1{
			height:24px;
			position:absolute;
			z-index:99;
			top:0;
			left:-170;
			opacity: 0; 
			filter: "alpha(opacity=0)"; 
			filter: alpha(opacity=0); 
			-moz-opacity: 0;
			cursor:pointer;
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
	<div id="north" region="north" title="课程表信息" style="height:59px;" >
		<table id="ee" singleselect="true" width="100%" class="tablestyle">
			<tr>
				<td style="width:100px;" class="titlestyle">学年学期名称</td>
				<td>
					<input name="PK_XNXQMC_CX" id="PK_XNXQMC_CX" class="easyui-textbox" style="width:200px;"/>
				</td>
				<td style="width:100px;" class="titlestyle">教学性质</td><!-- 根据当前登录人权限 -->
				<td>
					<select name="PK_JXXZ_CX" id="PK_JXXZ_CX" class="easyui-combobox" panelHeight="auto" style="width:200px;">
					</select>
				</td>
				<td style="width:100px;" class="titlestyle">系部名称</td>
				<td>
					<select name="PK_XB_CX" id="PK_XB_CX" class="easyui-combobox" panelHeight="auto" style="width:200px;">
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
	</div>
	
	<!-- 课程表详情页面 -->
	<div id="classTimetable" style="overflow:hidden;">
		<div id="divPageMask2" class="maskStyle" style="display:none;">
    		<div id="maskFont2"></div>
    	</div>
		<div class="easyui-layout" style="width:100%; height:100%;">
			<div region="center" style="overflow:hidden;">
				<div id="kcbContent">
					<table id="mask" style="width:100%; height:100%; text-align:center;">
						<tr><td><img src="<%=request.getContextPath()%>/images/loading_3.gif"/></td></tr>
					</table>
				</div>
			</div>
		</div>
		<!-- 课表导出页面 -->
		<div id="exportTimetable">
			<!--引入编辑页面用Ifram-->
			<iframe id="exportTimetableiframe" name="exportTimetableiframe" src='' style='width:100%; height:100%;' frameborder="0" scrolling="no"></iframe>
		</div>
	</div>
	<%-- <div id="classTimetable">
	遮罩层
    	<div id="divPageMask2" class="maskStyle" style="display:none;">
    		<div id="maskFont2"></div>
    	</div>
		<div id="kcbContent">
			<table id="mask" style="width:100%; height:100%; text-align:center;">
				<tr><td><img src="<%=request.getContextPath()%>/images/loading_3.gif"/></td></tr>
			</table>
		</div>
		
		<!-- 课表导出页面 -->
		<div id="exportTimetable">
			<!--引入编辑页面用Ifram-->
			<iframe id="exportTimetableiframe" name="exportTimetableiframe" src='' style='width:100%; height:100%;' frameborder="0" scrolling="no"></iframe>
		</div>
	</div> --%>
	
	<!-- 下载excel -->
	<iframe id="exportIframe" src="" style="width:0; height:0;"></iframe>
	
	<a id="pageOfficeLink" href="#" style="display:none;"></a>
</body>
<script type="text/javascript">
	var sAuth = '<%=sAuth%>';
	var iKeyCode = ''; //定义主键
	var pageNum = 1;   //datagrid初始当前页数
	var pageSize = 20; //datagrid初始页内行数
	var PK_XNXQMC_CX = '';//查询条件
	var PK_JXXZ_CX = '';
	var PK_XB_CX = '';
	var maxWidth = 260;
	var maxHeight = 55;
	var fontNum = 20;
	var curXnxq = '';
	var curXbdm = '';
	var curXbmc = '';
	var kcbWidth = 0;
	var kcbHeight = 0;
	var oddArray = new Array();//单周周次
	var evenArray = new Array();//双周周次
	var pofOpenType = '<%=MyTools.getProp(request, "Base.pofOpenType")%>';
	
	$(document).ready(function(){
		initDialog();//初始化对话框
		initData();//页面初始化加载数据
		
		kcbWidth = parseInt($('#classTimetable').css('width').substring(0, $('#classTimetable').css('width').length-2), 10)-30;
	});
	
	/**页面初始化加载数据**/
	function initData(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Kbcx',
			data : 'active=initData&type=allClassKCB&page=' + pageNum + '&rows=' + pageSize + '&sAuth=' + sAuth,
			dataType:"json",
			success : function(data) {
				loadSemesterGrid(data[0].listData);
				initCombobox(data[0].jxxzData, data[0].xbdmData);
			}
		});
	}
	
	/**加载 dialog控件**/
	function initDialog(){
		$('#classTimetable').dialog({
			maximized:true,
			modal:true,
			closed: true,   
			cache: false, 
			draggable:false,//是否可移动dialog框设置
			toolbar:[{
				text:'返回',
				iconCls:'icon-back',
				handler:function(){
					$('#classTimetable').dialog('close');
				}
			},{
				text:'打印',
				iconCls:'icon-print',
				handler:function(){
					doToolbar('export');
				}
			},{
				//2017/11/23翟旭超改
				text:'导出',
				iconCls:'icon-submit',
				handler:function(){
					doToolbar('daochu');
				}
			}
			
			
			],
			//打开事件
			onOpen:function(data){
				kcbHeight = parseInt($('#classTimetable').css('height').substring(0, $('#classTimetable').css('height').length-2), 10)-60;
			},
			//读取事件
			onLoad:function(data){},
			//关闭事件
			onClose:function(data){
				$('#kcbContent').css('width', kcbWidth);
				$('#kcbContent').css('height', kcbHeight);
				$('#kcbContent').html('<table style="width:100%; height:100%; text-align:center;"><tr><td><img src="<%=request.getContextPath()%>/images/loading_3.gif"/></td></tr></table>');
			}
		});
		
		$('#exportTimetable').dialog({
			title:'打印预览',
			maximized:true,
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
				$("#exportTimetableiframe").attr('src', '');
			}
		});
	}
	
	/**加载combobox控件
		@jxxzData 教学性质下拉框数据
		@zydmData 专业下拉框数据
	**/
	function initCombobox(jxxzData, xbdmData){
		$('#PK_JXXZ_CX').combobox({
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
		
		$('#PK_XB_CX').combobox({
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
			},
			//下拉列表值改变事件
			onChange:function(data){}
		});
	}
	
	/**读取学年学期课程表datagrid数据**/
	function loadSemesterData(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Kbcx',
			data : 'active=queAllClassKcbList&page=' + pageNum + '&rows=' + pageSize + '&sAuth=' + sAuth + 
				'&PK_XNXQMC_CX=' + encodeURI(PK_XNXQMC_CX) + 
				'&PK_JXXZ_CX=' + PK_JXXZ_CX +
				'&PK_ZY_CX=' + PK_XB_CX,
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
			title:'学年学期课程表信息列表',
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
				{field:'PK_XNXQBM',title:'学年学期编码',width:fillsize(0.2),align:'center'},
				{field:'PK_XNXQMC',title:'学年学期名称',width:fillsize(0.2),align:'center'},
				{field:'jxxz',title:'教学性质',width:fillsize(0.2),align:'center'},
				{field:'PK_XBMC',title:'系部名称',width:fillsize(0.2),align:'center'},
				{field:'col4',title:'操作',width:fillsize(0.2),align:'center',
						formatter:function(value,rec){
							return "<input type='button' value='[详情]' onclick='loadAllClassKcb(\"" + rec.PK_XNXQMC + "\",\"" + rec.PK_XNXQBM + "\",\"" + rec.PK_XBDM + "\",\"" + rec.PK_XBMC + "\");' style=\"cursor:pointer;\">";
				}}
			]],
			//双击某行时触发
			onDblClickRow:function(rowIndex,rowData){},
			//读取datagrid之前加载
			onBeforeLoad:function(){},
			//单击某行时触发
			onClickRow:function(rowIndex,rowData){
				//主键赋值
				iKeyCode = rowData.PK_XNXQBM;
			},
			//加载成功后触发
			onLoadSuccess: function(data){
				iKeyCode = '';
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
	function doToolbar(id, PK_XNXQBM, nodeId){
		if(id == 'queList'){
			pageNum = 1;
			PK_XNXQMC_CX = $('#PK_XNXQMC_CX').textbox('getValue'); 
			PK_JXXZ_CX = $('#PK_JXXZ_CX').combobox('getValue');
			PK_XB_CX = $('#PK_XB_CX').combobox('getValue');
			loadSemesterData();
		}
		
		if(id == 'export'){
			if(pofOpenType == 'normal'){
				$('#exportTimetable').dialog('open');
				$("#exportTimetableiframe").attr("src","<%=request.getContextPath()%>/form/timetableQuery/exportExcel.jsp?exportType=allClassKcb&xnxqbm=" + curXnxq + "&code=" + curXbdm + "&timetableName=" + encodeURI(encodeURI(curXbmc)));
			}else{
				$.ajax({
					type : "POST",
					url : '<%=request.getContextPath()%>/Svl_Kbcx',
					data : "active=loadAllClassPageOfficeLink&exportType=allClassKcb&xnxqbm=" + curXnxq + "&code=" + curXbdm + "&timetableName=" + encodeURI(encodeURI(curXbmc)),
					dataType:"json",
					success : function(data){
						$('#pageOfficeLink').attr('href', data[0].linkStr);
						document.getElementById("pageOfficeLink").click();
					}
				});
			}
		}
		
		if(id == 'daochu'){
			
			$('#maskFont2').html('课表信息导出中...');
    		$('#divPageMask2').show();
			ExportExceldaochu();
		}
	}
	
	/**读取当前专业的所有班级课表
		@PK_XNXQBM 学年学期编码
		@PK_XBDM 系部代码
	*/
	function loadAllClassKcb(PK_XNXQMC, PK_XNXQBM, PK_XBDM, PK_XBMC){
		$('#classTimetable').dialog('open');
	
		curXnxq = PK_XNXQBM;
		curXbdm = PK_XBDM;
		curXbmc = PK_XBMC;
		//maxWidth = 200;
		//maxHeight = 55;
		
		$('#classTimetable').dialog({
			title:(PK_XNXQMC+'&nbsp;&nbsp;&nbsp;'+PK_XBMC+'班级课程表信息').replace(/ /g, "")
		});
		
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Kbcx',
			data : 'active=loadAllClassKcb&PK_XNXQBM=' + PK_XNXQBM + '&PK_ZYDM=' + PK_XBDM,
			dataType:"json",
			success : function(data) {
				fillKcb(data.semesterInfo, data.allClassKcb);
			}
		});
	}
	
	/**
		@semesterInfo 学期相关信息
		@allClassKcb 当前专业所有班级的课表
	*/
	function fillKcb(semesterInfo, allClassKcb){
		//单双周周次
		var weekArray = parseWeekArray(semesterInfo.学期周次);
		oddArray = weekArray[0];
		evenArray = weekArray[1];
	
		var mzts = parseInt(semesterInfo.每周天数, 10);
		var swjs = parseInt(semesterInfo.上午节数, 10);
		var zwjs = parseInt(semesterInfo.中午节数, 10);
		var xwjs = parseInt(semesterInfo.下午节数, 10);
		var wsjs = parseInt(semesterInfo.晚上节数, 10);
		var zjs = swjs + zwjs + xwjs + wsjs; //总节数
		var timeArray = semesterInfo.节次时间.split(',');
		var numArray = ['一','二','三','四','五','六','七','八','九','十'];
		var content = '';
		var curTdContent = '';
		
		//标题
		content = '<table class="kcbTable" cellspacing="0" cellpadding="0">' + 
					'<tr style="height:30px;">' + 
						'<td class="kcbTdStyle titleBG splitTd" colspan="3" style="width:140px; height:69px;"><img src="<%=request.getContextPath()%>/images/timetable/classTitle.png"/></td>' +
					'</tr>';
		for(var i=0; i<mzts; i++){
			content += '<tr class="kcbTrStyle">' + 
						'<td class="kcbTdStyle titleBG splitTd" rowspan="' + zjs + '" style="width:18px;">' + numArray[i] + '</td>' + 
						'<td class="kcbTdStyle titleBG" style="width:25px;">1</td>' + 
						'<td class="kcbTdStyle titleBG" style="width:90px;">' + timeArray[0] + '</td>' + 
					'</tr>';
			for(var j=1; j<zjs; j++){
				content += '<tr class="kcbTrStyle">' + 
								'<td class="kcbTdStyle titleBG';
				if(j+1==swjs || j+1==swjs+zwjs || j+1==swjs+zwjs+xwjs || j+1==zjs){
					content += ' splitTd';
				}
				
				content += '">' + (j+1) + '</td>' +
								'<td class="kcbTdStyle titleBG';
				if(j+1==swjs || j+1==swjs+zwjs || j+1==swjs+zwjs+xwjs || j+1==zjs){
					content += ' splitTd';
				} 
				content += '">' + timeArray[j] + '</td>' + 
							'</tr>';
			}
		}
		
		content += '<tr class="kcbTrStyle"><td class="kcbTdStyle titleBG bzTd" colspan="3" style="height:100px;">备注</td></tr></table>';
		
		var tempClass = '';
		var tempKcb = '';
		var tempCode = '';
		var tempTea = '';
		var tempSite = '';
		var tempSkzc = '';
		var rowNum = 0;
		var tempNum = 0;
		var tempCourse = "";
		var tempIndex = 0;
		
		for(var i=0; i<allClassKcb.length; i++){
			tempClass = allClassKcb[i];
			tempKcb = tempClass.课表信息;
			//班级课表
			content += '<table class="kcbTable" cellspacing="0" cellpadding="0"';
			if(i == allClassKcb.length-1){
				content += ' style="border-right:1px solid #95B8E7;"';
			}
			
			//判断课程表最大宽度
			/*
			if(tempClass.班级名称.length*20 > maxWidth){
				maxWidth = tempClass.班级名称.length*20;
			}
			*/
			content += '>' + 
						'<tr style="height:35px;">' + 
							'<td class="kcbTdStyle classNameTd titleBG" style="font-weight:bold; font-size:14;">' + tempClass.班级名称 + '</td>' +
						'</tr>' +
						'<tr style="height:18px;">' + 
							'<td class="kcbTdStyle titleBG">' + tempClass.班级人数 + '</td>' +
						'</tr>' +
						'<tr style="height:20px;">' + 
							'<td class="kcbTdStyle titleBG splitTd">' + (tempClass.教室名称==''?'&nbsp;':tempClass.教室名称) + '</td>' +
						'</tr>';
			tempCode = null;
			tempTea = null;
			tempSite = null;
			tempSkzc = null;
			
			for(var j=0; j<tempKcb.length; j++){
				if(tempCode!=tempKcb[j].授课计划明细编号 || tempTea!=tempKcb[j].授课教师姓名 || tempSite!=tempKcb[j].实际场地名称 || tempSkzc!=tempKcb[j].授课周次){
					if(j > 0){
						content += '" name="' + rowNum + '"';
						if(tempKcb[j-1].授课计划明细编号 == ''){
							content += '>&nbsp;';
						}else{
							curTdContent = parseCourseInfo(tempKcb[j-1].课程名称, tempKcb[j-1].授课教师姓名, tempKcb[j-1].授课周次, tempKcb[j-1].实际场地名称);
							content += ' title="' + curTdContent[0] + '">';
							
							tempCourse = curTdContent[1];
							tempIndex = 3*rowNum;
							if(tempIndex < tempCourse.length){
								tempIndex--;
							}
							
							for(var k=0; k<tempIndex; k++){
								content += tempCourse[k]+'<br/>';
								
								if(k+1 == tempCourse.length){
									break;
								}
							}
							content = content.substring(0, content.length-5);
							
							if(tempIndex < tempCourse.length){
								content += '<br/>……';
							}
						}
						content += '</td></tr>';
					}
					
					tempCode = tempKcb[j].授课计划明细编号;
					tempTea = tempKcb[j].授课教师姓名;
					tempSite = tempKcb[j].实际场地名称;
					tempSkzc = tempKcb[j].授课周次;
					rowNum = 1;
					content += '<tr class="kcbTrStyle"><td class="kcbTdStyle kcbHeight';
				}else{
					rowNum++;
				}
				
				//判断是否为上午中午下午晚上的最后一个单元格
				tempNum = parseInt((tempKcb[j].时间序列).substring(2), 10);
				if(tempNum==swjs || tempNum==swjs+zwjs || tempNum==swjs+zwjs+xwjs || tempNum==zjs){
					content += ' splitTd';
					tempCode = null;
					tempTea = null;
					tempSite = null;
					tempSkzc = null;
				}
			}
			
			content += '" name="' + rowNum + '"';
			if(tempKcb[tempKcb.length-1].授课计划明细编号 == ''){
				content += '>&nbsp;';
			}else{
				curTdContent = parseCourseInfo(tempKcb[tempKcb.length-1].课程名称, tempKcb[tempKcb.length-1].授课教师姓名, tempKcb[tempKcb.length-1].授课周次, tempKcb[tempKcb.length-1].实际场地名称);
				content += ' title="' + curTdContent[0] + '">';
				
				tempCourse = curTdContent[1];
				tempIndex = 3*rowNum;
				if(tempIndex < tempCourse.length){
					tempIndex--;
				}
				
				for(var k=0; k<tempIndex; k++){
					content += tempCourse[k]+'<br/>';
					
					if(k+1 == tempCourse.length){
						break;
					}
				}
				content = content.substring(0, content.length-5);
				
				if(tempIndex < tempCourse.length){
					content += '<br/>……';
				}
			}
			content += '</td></tr>';
			content += '<tr><td class="kcbTdStyle classNameTd bzTd" style="height:100px; text-align:left;">' + (tempClass.备注==''?'&nbsp;':tempClass.备注.replace(/\n/g, '<br/>')) + '</td></tr></table>';
		}
		
		$('#kcbContent').html(content);
		$('.kcbTrStyle').css('height', maxHeight + 'px');
		$('.kcbHeight').each(function(){
			$(this).css('height', $(this).attr('name')*maxHeight+'px');
		});
		$('#kcbContent').css('width', 160+maxWidth*allClassKcb.length + 'px');
		$('.classNameTd').css('width', maxWidth + 'px');
		
		//备注高度
		var curBzHeight = '';
		var maxBzHeight = '';
		$('.bzTd').each(function(){
			curBzHeight = parseFloat($(this).css('height').substring(0, $(this).css('height').length-2));
			if(curBzHeight > maxBzHeight){
				maxBzHeight = curBzHeight;
			}
		});
		$('.bzTd').css('height', maxBzHeight);
		var width1 = $(".layout-panel-center").width()-16;
		var height1 =  $(".layout-panel-center").height()-10;
		FixTable("kcbContent",width1,height1);
	
	}
	/**锁定表头和列
		@param TableID 要锁定的Table的ID
		@param width 显示的宽度
		@param height 显示的高度
	*/
	function FixTable(TableID, width, height) {
		//创建框架
		if($("#" + TableID + "_tableLayout").length != 0) {
		    $("#" + TableID + "_tableLayout").before($("#" + TableID));
		    $("#" + TableID + "_tableLayout").empty();
		}else{
			$("#" + TableID).after("<div id='" + TableID + "_tableLayout' style='overflow:hidden; height:" + height + "; width:" + width + "px;'></div>");
		}
		
		$('<div id="' + TableID + '_tableFix"></div>' + 
			'<div id="' + TableID + '_tableHead"></div>' + 
			'<div id="' + TableID + '_tableColumn"></div>' + 
			'<div id="' + TableID + '_tableData"></div>').appendTo("#" + TableID + "_tableLayout");
		var oldtable = $("#" + TableID);
		var tableFixClone = oldtable.clone(true);
		tableFixClone.attr("id", TableID + "_tableFixClone");
		$("#" + TableID + "_tableFix").append(tableFixClone);
		var tableHeadClone = oldtable.clone(true);
		tableHeadClone.attr("id", TableID + "_tableHeadClone");
		$("#" + TableID + "_tableHead").append(tableHeadClone);
		var tableColumnClone = oldtable.clone(true);
		tableColumnClone.attr("id", TableID + "_tableColumnClone");
		$("#" + TableID + "_tableColumn").append(tableColumnClone);
		$("#" + TableID + "_tableData").append(oldtable);
		$("#" + TableID + "_tableLayout table").each(function(){
			$(this).css("margin", "0");
		});
		
		//计算tableFix，tableHead的高度
		var HeadHeight = 78;
		$("#" + TableID + "_tableHead").css("height", HeadHeight);
		$("#" + TableID + "_tableFix").css("height", HeadHeight);
		
		//计算tableFix，tableColumn的宽度
		var ColumnsWidth = 143;
		$("#" + TableID + "_tableColumn").css("width", ColumnsWidth);
		$("#" + TableID + "_tableFix").css("width", ColumnsWidth);
		
		//为tableHead和tableColumn添加联动的滚动条事件
		$("#" + TableID + "_tableData").scroll(function () {
		    $("#" + TableID + "_tableHead").scrollLeft($("#" + TableID + "_tableData").scrollLeft());
		    $("#" + TableID + "_tableColumn").scrollTop($("#" + TableID + "_tableData").scrollTop());
		});
		$("#" + TableID + "_tableFix").css({"overflow":"hidden", "position":"relative", "z-index":"50"});
		if (myBrowser() == "IE"){
			$("#" + TableID + "_tableColumn").css({"overflow":"hidden", "height":height-15, "position":"relative", "z-index":"40"});
			$("#" + TableID + "_tableData").css({"overflow":"auto", "width":width+3, "height":height+2, "position":"relative", "z-index":"35"});
			$("#" + TableID + "_tableHead").css({"overflow":"hidden", "width":width-14, "position":"relative", "z-index":"45"});
		}else{
			$("#" + TableID + "_tableHead").css({"overflow":"hidden", "width":width-19, "position":"relative", "z-index":"45"});
			$("#" + TableID + "_tableColumn").css({"overflow":"hidden", "height":height-21, "position":"relative", "z-index":"40"});
			$("#" + TableID + "_tableData").css({"overflow":"auto", "width":width+2, "height":height, "position":"relative", "z-index":"35"});
		}
		
		$("#" + TableID + "_tableFix").offset($("#" + TableID + "_tableLayout").offset());
		$("#" + TableID + "_tableHead").offset($("#" + TableID + "_tableLayout").offset());
		$("#" + TableID + "_tableColumn").offset($("#" + TableID + "_tableLayout").offset());
		$("#" + TableID + "_tableData").offset($("#" + TableID + "_tableLayout").offset());
	}	
	/**判断浏览器*/
	function myBrowser(){
	    var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串
	    var isOpera = userAgent.indexOf("Opera") > -1; //判断是否Opera浏览器
	    var isIE = userAgent.indexOf("compatible") > -1 && userAgent.indexOf("MSIE") > -1 && !isOpera; //判断是否IE浏览器
	    var isFF = userAgent.indexOf("Firefox") > -1; //判断是否Firefox浏览器
	    var isSafari = userAgent.indexOf("Safari") > -1; //判断是否Safari浏览器
	    var isChrome =userAgent.indexOf("Chrome") >-1;//判断是否Chrome浏览器
		    if (isIE) {
		        return "IE";
		     }//isIE end
		    if (isFF) {
		        return "FF";
		    }
		    if (isOpera) {
		        return "Opera";
		    }
		     if (isChrome) {
		        return "Chrome";
		    }
		}
	/**解析课程信息格式**/
	function parseCourseInfo(courseName, teaName, weekNum, siteName){
		var resultArray = new Array();
		var titleStr = '';
		var contentArray = new Array();
		var tempStr = '';
		
		//判断是单门课程还是拼接的课程
		if(courseName.indexOf('｜') > -1){
			var tempCourseName = courseName.split('｜');
			var tempTeaName = teaName.split('｜');
			var tempWeekNum = weekNum.split('｜');
			var tempSiteName = siteName.split('｜');
			
			/*
			if(tempCourseName.length > 1){
				if(tempCourseName.length > maxHeight/55){
					maxHeight = 13 * tempCourseName.length;
				}
			}
			*/
			
			for(var i=0; i<tempCourseName.length; i++){
				titleStr += tempCourseName[i] + '\n' + tempTeaName[i] + '\n' + parseWeekShow(tempWeekNum[i], oddArray, evenArray) + '\n' + tempSiteName[i];
				
				//判断如果内容过长用省略号代替多余内容
				tempStr = tempCourseName[i];
				if(tempStr.length > fontNum){
					tempStr = tempStr.substring(0, fontNum)+'···';
				}
				contentArray.push(tempStr);
				
				if(weekNum==""){
					tempStr = "" ;
				}else{
					tempStr = tempTeaName[i] + '（' + parseWeekShow(tempWeekNum[i], oddArray, evenArray) + '）';
				}
				
				if(tempStr.length > fontNum){
					tempStr = tempStr.substring(0, fontNum)+'···';
				}
				contentArray.push(tempStr);
				
				
				tempStr = tempSiteName[i];
				if(tempStr.length > fontNum){
					tempStr = tempStr.substring(0, fontNum)+'···';
				}
				contentArray.push(tempStr);
				
				if(i < tempCourseName.length-1){
					titleStr += '\n\n';
				}
				
				//判断课程表最大宽度
				/*
				if(tempCourseName[i].length*fontWidth > maxWidth){
					maxWidth = tempCourseName[i].length*fontWidth;
				}
				if(tempTeaName[i].length*fontWidth+parseWeekShow(tempWeekNum[i]).length*fontWidth > maxWidth){
					maxWidth = tempTeaName[i].length*fontWidth+parseWeekShow(tempWeekNum[i]).length*fontWidth;
				}
				if(tempSiteName[i].length*fontWidth > maxWidth){
					maxWidth = tempSiteName[i].length*fontWidth;
				}
				*/
			}
		}else{
			titleStr = courseName + '\n' + teaName + '\n' + parseWeekShow(weekNum, oddArray, evenArray) + '\n' + siteName;
			
			//判断如果内容过长用省略号代替多余内容
			if(courseName.length > fontNum){
				courseName = courseName.substring(0, fontNum)+'···';
			}
			contentArray.push(courseName);
			
			if(weekNum==""){
				tempStr = "" ;
			}else{
				tempStr = teaName + '（' + parseWeekShow(weekNum, oddArray, evenArray) + '）';
			}
			
			if(tempStr.length > fontNum){
				tempStr = tempStr.substring(0, fontNum)+'···';
			}
			contentArray.push(tempStr);
			
			if(siteName.length > fontNum){
				siteName = siteName.substring(0, fontNum)+'···';
			}
			contentArray.push(siteName);
		
			//判断课程表最大宽度
			/*
			if(courseName.length*fontWidth > maxWidth){
				maxWidth = courseName.length*fontWidth;
			}
			if(teaName.length*fontWidth+parseWeekShow(weekNum).length*fontWidth > maxWidth){
				maxWidth = teaName.length*fontWidth+parseWeekShow(weekNum).length*fontWidth;
			}
			if(siteName.length*fontWidth > maxWidth){
				maxWidth = siteName.length*fontWidth;
			}
			*/
		}
		
		resultArray.push(titleStr);
		resultArray.push(contentArray);
		return resultArray;
	}
	
	function closeDialog(){
		$('#exportTimetable').dialog('close');
	}
	
	//导出Excel班级课程表信息
	function ExportExceldaochu(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Kbcx',
			data : "active=ExportExceldaochu&exportType=allClassKcb&xnxqbm=" + curXnxq + "&code=" + curXbdm + "&timetableName=" + encodeURI(encodeURI(curXbmc)),
			
			dataType:"json",
			success : function(data) {
				$('#divPageMask2').hide();
				if(data[0].MSG == '文件生成成功'){
					//下载文件到本地
					//$("#exportIframe").attr("src", '<%=request.getContextPath()%>/form/questSurvey/download.jsp?filePath=' + encodeURI(encodeURI(json[0].filePath)));
					$("#exportIframe").attr("src", '<%=request.getContextPath()%>/form/elective/fileDownload.jsp?filePath=' + encodeURI(encodeURI(data[0].filePath)));
				}else{
					alertMsg(data[0].MSG);
				}
			}
		});
	}
</script>
</html>