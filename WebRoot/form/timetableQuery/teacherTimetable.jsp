<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="java.util.Vector"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.src.develop.store.user.*"%>
<%
	/**
		创建人：yeq
		Create date: 2015.06.24
		功能说明：用于查看教师课表
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
		
		.teaBzStyle{
			font-size:15;
			padding-left:10px;
		}
		
		.titleTdStyle{
			font-size:12;
			border-left:1px solid #95B8E7;
			border-bottom:1px solid #95B8E7;
			text-align:center;
		}
		
		.splitTd{
			border-bottom:4px double #95B8E7;
		}
		
		.titleBG{
			background-color:#EFEFEF;
		}
		
		.remarkStyle{
			margin-top:5px;
			margin-left:5px;
		}
		
		#divPageMask{
			background-color:#D2E0F2;
			filter:alpha(opacity=90);
			left:0px;
			top:0px;
			z-index:100;
		}
		#maskFont{
			font-size:16px;
			color:#2B2B2B;
			width:550px;
			height:50px;
			position:absolute;
			top:50%;
			left:50%;
			margin-top:-25px;
			margin-left:-275px;
			text-align:center;
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
				<td style="width:150px;" class="titlestyle">学年学期名称</td>
				<td>
					<input name="PK_XNXQMC_CX" id="PK_XNXQMC_CX" class="easyui-textbox" style="width:150px;"/>
				</td>
				<td style="width:150px;" class="titlestyle">教学性质</td><!-- 根据当前登录人权限 -->
				<td>
					<select name="PK_JXXZ_CX" id="PK_JXXZ_CX" class="easyui-combobox" panelHeight="auto" style="width:150px;">
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
	<div id="teaTimetable">
		<div style="width:100%; height:100%;" class="easyui-layout">
		<%-- 遮罩层 --%>
    	<div id="divPageMask2" class="maskStyle" style="display:none;">
    		<div id="maskFont2"></div>
    	</div>
			<div region="north" style="height:34px;">
				<table style="width:100%; border:none;">
					<tr>
						<td>
							<a href="#" id="back" class="easyui-linkbutton" plain="true" iconcls="icon-back" onClick="doToolbar(this.id);">返回</a>
							<a href="#" id="remark" class="easyui-linkbutton" plain="true" iconcls="icon-edit" onClick="doToolbar(this.id);" style="display:none;">备注设置</a>
							<a href="#" id="export" class="easyui-linkbutton" plain="true" iconcls="icon-print" onClick="doToolbar(this.id);">打印</a>
							<a href="#" id="exportAll" class="easyui-linkbutton" plain="true" iconcls="icon-print" onClick="doToolbar(this.id);" style="display:none;">打印全部</a>
							
							<!-- 翟旭超2017/10/30加 -->
							<a href="#" id="daochu" class="easyui-linkbutton" plain="true" iconcls="icon-submit" onClick="doToolbar(this.id);">导出</a>
							<a href="#" id="daochuAll" class="easyui-linkbutton" plain="true" iconcls="icon-submit" onClick="doToolbar(this.id);" style="display:none;">导出全部</a>
						</td>
						<td style="text-align:right;">
							<span id="tips" style="font-size:12; color:red; display:none;">提示：如需查看课程调整情况，请选择课表左上角详细周次。&nbsp;&nbsp;</span>
						</td>
					</tr>
				</table>
			</div>
			<div region="center">
				<div style="width:100%; height:100%;" class="easyui-layout">
					<div region="west" title="教师" style="width:200px;">
						<table width="100%" style="background:#EFEFEF; border-bottom:1px solid #95B8E7;" cellspacing="0" cellpadding="0">
							<tr>
								<td style="width:30px;" class="titlestyle">姓名</td>
								<td style="border-right:1px solid #CCCCCC;">
									<input name="teaName" id="teaName" class="easyui-textbox" style="width:80px;"/>
								</td>
								<td style="width:55px;" align="center">
									<a href="#" onclick="doToolbar(this.id)" id="queTeaTree" class="easyui-linkbutton" plain="true" iconcls="icon-search">查询</a>
								</td>				
							</tr>
					    </table>
						<ul id="teacherTree" class="easyui-tree" style="display: none">
						</ul>
					</div>    
					<div region="center" id="kcbContent" title="课程表">
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 备注设置 -->
	<div id="remarkDialog">
		<form id="form1" method="post">
			<div class="remarkStyle">
				1、本学期所授课程  理论课时_________实训课时_________。（具体可咨询教务处或专业组长）
			</div>
			<div class="remarkStyle">
				2、本学期开学日期&nbsp;<input style="width:150px;" class="easyui-datebox" id="ic_startDate" name="ic_startDate" editable="false" required="true"/>&nbsp;
				结束日期&nbsp;<input style="width:150px;" class="easyui-datebox" id="ic_endDate" name="ic_endDate" editable="false" required="true"/>
			</div>
			<div class="remarkStyle">
				3、开学第&nbsp;<input id="weekNum_1" name="weekNum_1" class="easyui-textbox" style="width:40px; text-align:center;" required="true"/>&nbsp;周交授课计划文字版、电子版各一份
			</div>
			<div class="remarkStyle">
				4、关于多媒体教室和实训室的问题请联系&nbsp;<input id="contactWay" name="contactWay" class="easyui-textbox examLength" style="width:240px;" required="true"/>&nbsp;。
			</div>
			<div class="remarkStyle">
				5、本学期&nbsp;<input id="weekNum_2" name="weekNum_2" class="easyui-textbox weekLength" style="width:90px;" required="true"/>&nbsp;周课程考试为&nbsp;<input id="exam_1" name="exam_1" class="easyui-textbox examLength" style="width:240px;" required="true"/>&nbsp;；
			</div>
			<div class="remarkStyle">
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;本学期&nbsp;<input id="weekNum_3" name="weekNum_3" class="easyui-textbox weekLength" style="width:90px;" required="true"/>&nbsp;周课程考试为&nbsp;<input id="exam_2" name="exam_2" class="easyui-textbox examLength" style="width:240px;" required="true"/>&nbsp;；
			</div>
			<div class="remarkStyle">
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;本学期&nbsp;<input id="weekNum_4" name="weekNum_4" class="easyui-textbox weekLength" style="width:90px;" required="true"/>&nbsp;周课程考试为&nbsp;<input id="exam_3" name="exam_3" class="easyui-textbox examLength" style="width:240px;" required="true"/>&nbsp;；
			</div>
			<div class="remarkStyle">
				6、如有特殊原因需变更授课教师或地点，请先在教务处填写变更申请。
			</div>
			<br/>
			<div class="remarkStyle">
				<span style="width:400px;">&nbsp;</span>年月&nbsp;<input id="yearMonth" name="yearMonth" class="easyui-textbox" style="width:80px;" required="true"/>
			</div>
			<input type="hidden" id="active" name="active"/>
			<input type="hidden" id="PK_XNXQBM" name="PK_XNXQBM"/>
			<input type="hidden" id="startDate" name="startDate"/>
			<input type="hidden" id="endDate" name="endDate"/>
		</form>
	</div>
		
	<!-- 课表导出页面 -->
	<div id="exportTimetable">
		<!--引入编辑页面用Ifram-->
		<iframe id="exportTimetableiframe" name="exportTimetableiframe" src='' style='width:100%; height:100%;' frameborder="0" scrolling="no"></iframe>
	</div>
	
	<!-- 下载excel -->
	<iframe id="exportIframe" src="" style="width:0; height:0;"></iframe>
	
	<a id="pageOfficeLink" href="#" style="display:none;"></a>
</body>
<script type="text/javascript">
	var sAuth = '<%=sAuth%>';
	var admin = '<%=MyTools.getProp(request, "Base.admin")%>';//管理员
	var jxzgxz = '<%=MyTools.getProp(request, "Base.jxzgxz")%>';//教学主管校长
	var qxjdzr = '<%=MyTools.getProp(request, "Base.qxjdzr")%>';//全校教导主任
	var qxjwgl = '<%=MyTools.getProp(request, "Base.qxjwgl")%>';//全校教务管理
	var xbjdzr = '<%=MyTools.getProp(request, "Base.xbjdzr")%>';//系部教导主任
	var xbjwgl = '<%=MyTools.getProp(request, "Base.xbjwgl")%>';//系部教务管理
	var bzr = '<%=MyTools.getProp(request, "Base.bzr")%>';//班主任
	var pofOpenType = '<%=MyTools.getProp(request, "Base.pofOpenType")%>';
	var iKeyCode = ''; //定义主键
	var pageNum = 1;   //datagrid初始当前页数
	var pageSize = 20; //datagrid初始页内行数
	var PK_XNXQMC_CX = '';//查询条件
	var PK_JXXZ_CX = '';
	var tempNodeId = '';//用于保存当前选中的教师编号
	var tempNodeText = '';
	var tempNodeTarget = '';
	var tempSemesterName = '';//用于保存当前选择自动排课的学期名称
	var mzts = 0;//每周天数
	var sw = 0;//上午
	var zw = 0;//中午
	var xw = 0;//下午
	var ws = 0;//晚上
	var zjs = 0;//总节数
	var weekNameArray = ['星期一','星期二','星期三','星期四','星期五','星期六','星期日'];
	var oddArray = new Array();//单周周次
	var evenArray = new Array();//双周周次
	var curXnxq = '';
	var loadFlag = false;
	
	$(document).ready(function(){
		initDialog();//初始化对话框
		initData();//页面初始化加载数据
		
		$('#weekNum_1').textbox('textbox').bind('keyup', function(e){
			this.value = this.value.slice(0, 1);
		});
		$('.weekLength').each(function(){
		    $(this).textbox('textbox').bind('keyup', function(e){
				this.value = this.value.slice(0, 49);
			});
    	});
    	$('.examLength').each(function(){
		    $(this).textbox('textbox').bind('keyup', function(e){
				this.value = this.value.slice(0, 199);
			});
    	});
		$('#yearMonth').textbox('textbox').bind('keyup', function(e){
			this.value = this.value.slice(0, 19);
		});
		
		if(sAuth.indexOf(admin)>-1 || sAuth.indexOf(jxzgxz)>-1 || sAuth.indexOf(qxjdzr)>-1 || sAuth.indexOf(qxjwgl)>-1 || sAuth.indexOf(xbjdzr)>-1 || sAuth.indexOf(xbjwgl)>-1 || sAuth.indexOf(bzr)>-1){
			$('#exportAll').show();
			
			$('#daochuAll').show();
		
			if(sAuth.indexOf(admin)>-1 || sAuth.indexOf(jxzgxz)>-1 || sAuth.indexOf(qxjdzr)>-1 || sAuth.indexOf(qxjwgl)>-1){
				$('#remark').show();
			}
		}
		
	});
	
	/**页面初始化加载数据**/
	function initData(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Kbcx',
			data : 'active=initData&type=teaKCB&page=' + pageNum + '&rows=' + pageSize + '&sAuth=' + sAuth,
			dataType:"json",
			success : function(data) {
				loadSemesterGrid(data[0].listData);
				initCombobox(data[0].jxxzData);
			}
		});
	}
	
	/**加载 dialog控件**/
	function initDialog(){
		$('#teaTimetable').dialog({
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
				$('#teacherTree').hide();
				$('#kcbContent').html('');
				tempNodeId = '';
				tempNodeText = '';
				$('#teaName').textbox('setValue', '');
			}
		});
		
		$('#remarkDialog').dialog({   
			title:'备注',
			width: 540,//宽度设置   
			height: 320,//高度设置 
			modal:true,
			closed: true,   
			cache: false,
			draggable:false,//是否可移动dialog框设置
			toolbar:[{
				//保存
				text:'保存',
				iconCls:'icon-save',
				handler:function(){
					doToolbar('saveRemark');
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
	**/
	function initCombobox(jxxzData){
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
	}
	
	/**读取学年学期课程表datagrid数据**/
	function loadSemesterData(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Kbcx',
			data : 'active=queKcbList&page=' + pageNum + '&rows=' + pageSize + 
				'&PK_XNXQMC_CX=' + encodeURI(PK_XNXQMC_CX) + 
				'&PK_JXXZ_CX=' + PK_JXXZ_CX,
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
				{field:'PK_XNXQBM',title:'学年学期编码',width:fillsize(0.25),align:'center'},
				{field:'PK_XNXQMC',title:'学年学期名称',width:fillsize(0.25),align:'center'},
				{field:'jxxz',title:'教学性质',width:fillsize(0.25),align:'center'},
				{field:'col4',title:'操作',width:fillsize(0.25),align:'center',
						formatter:function(value,rec){
							return "<input type='button' value='[详情]' onclick='timetableDetail(\"" + rec.PK_XNXQMC+rec.jxxz + "\",\"" + rec.PK_XNXQBM + "\",\"" + rec.PK_ZYDM + "\");' style=\"cursor:pointer;\">";
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
	function doToolbar(id){
		if(id == 'queList'){
			pageNum = 1;
			PK_XNXQMC_CX = $('#PK_XNXQMC_CX').textbox('getValue'); 
			PK_JXXZ_CX = $('#PK_JXXZ_CX').combobox('getValue');
			loadSemesterData();
		}
		
		if(id == 'back'){
			$('#teaTimetable').dialog('close');
		}
		
		//备注设置
		if(id == 'remark'){
			$('#remarkDialog').dialog('open');
		}
		
		//保存备注
		if(id == 'saveRemark'){
			$('#active').val('saveRemark');
			$('#PK_XNXQBM').val(curXnxq);
			$('#startDate').val($('#ic_startDate').datebox('getValue')==''?'':$('#ic_startDate').datebox('getValue').replace(/\-/g, '\.'));
			$('#endDate').val($('#ic_endDate').datebox('getValue')==''?'':$('#ic_endDate').datebox('getValue').replace(/\-/g, '\.'));
			$('#form1').submit();
		}
		
		//导出
		if(id == 'export'){
			if(tempNodeId == ''){
				alertMsg('请选择一个教师');
				return;
			}
			
			if(pofOpenType == 'normal'){
				$('#exportTimetable').dialog('open');
				$("#exportTimetableiframe").attr("src","<%=request.getContextPath()%>/form/timetableQuery/exportExcel.jsp?exportType=teaKcb&xnxqbm=" + curXnxq + "&code=" + tempNodeId + "&timetableName=" + encodeURI(encodeURI(tempNodeText)));
			}else{
				$.ajax({
					type : "POST",
					url : '<%=request.getContextPath()%>/Svl_Kbcx',
					data : "active=loadTeaPageOfficeLink&exportType=teaKcb&xnxqbm=" + curXnxq + "&code=" + tempNodeId + "&timetableName=" + encodeURI(encodeURI(tempNodeText)),
					dataType:"json",
					success : function(data){
						$('#pageOfficeLink').attr('href', data[0].linkStr);
						document.getElementById("pageOfficeLink").click();
					}
				});
			}
		}
		
		//导出全部
		if(id == 'exportAll'){
			if(pofOpenType == 'normal'){
				$('#exportTimetable').dialog('open');
				$("#exportTimetableiframe").attr("src","<%=request.getContextPath()%>/form/timetableQuery/exportExcel.jsp?exportType=teaKcbAll&sAuth=" + sAuth + "&xnxqbm=" + curXnxq + "&code=" + tempNodeId + "&timetableName=" + encodeURI(encodeURI(tempNodeText)));
			}else{
				$.ajax({
					type : "POST",
					url : '<%=request.getContextPath()%>/Svl_Kbcx',
					data : "active=loadTeaPageOfficeLink&exportType=teaKcbAll&sAuth=" + sAuth + "&xnxqbm=" + curXnxq + "&code=" + tempNodeId + "&timetableName=" + encodeURI(encodeURI(tempNodeText)),
					dataType:"json",
					success : function(data){
						$('#pageOfficeLink').attr('href', data[0].linkStr);
						document.getElementById("pageOfficeLink").click();
					}
				});
			}
		}
		
		if(id == 'queTeaTree'){
			loadTeaTree();
		}
		
		/* 翟旭超2017/11/23加 */
		//导出
		if(id == 'daochu'){
			if(tempNodeId == ''){
				alertMsg('请选择一个教师');
				return;
			}
			
			$('#maskFont2').html('课表信息导出中...');
    		$('#divPageMask2').show();
			ExportExceldaochu();
		}
		
		//导出全部
		if(id == 'daochuAll'){
			$('#maskFont2').html('课表信息导出中...');
    		$('#divPageMask2').show();
			ExportExceldaochuall();
		}
	}
	
	/**表单提交**/
	$('#form1').form({
		//定位到servlet位置的url
		url:'<%=request.getContextPath()%>/Svl_Kbcx',
		//当点击事件后触发的事件
		onSubmit: function(data){}, 
		//当点击事件并成功提交后触发的事件
		success:function(data){
			var json  =  eval("("+data+")");
			if(json[0].MSG == '保存成功'){
				//提示信息
				showMsg(json[0].MSG);
			}else{
				alertMsg(json[0].MSG);
			}
		}
	});
	
	/**显示班级课程表列表
		@PK_XNXQMC 学年学期名称
		@PK_XNXQBM 学年学期编码
	**/
	function timetableDetail(PK_XNXQMC, PK_XNXQBM, PK_ZYDM){
		curXnxq = PK_XNXQBM;
		$('#teaTimetable').dialog({
			title:(PK_XNXQMC+'教师课程表信息').replace(/ /g, "")
		});
		loadTeaTree();
		//初始化空白课程表
		initBlankKcb();
	}
	
	/**加载班级信息TREE
		@PK_ZYDM 专业代码
	**/
	function loadTeaTree(){
		$('#teacherTree').tree({
			checkbox: false,
			url:'<%=request.getContextPath()%>/Svl_Kbcx?active=queTeaTree&sAuth=' + sAuth + '&level=0&PK_XNXQBM=' + curXnxq + '&PK_SKJSBH=' + encodeURI($('#teaName').textbox('getValue')),
		    onClick:function(node){
		    	if(loadFlag){
		    		$('#teacherTree').tree('select', tempNodeTarget);
		    		return;
		    	}
		    
	    		//判断点击的老师是不是当前选中的老师
	    		if(node.id != tempNodeId){
	    			tempNodeId = node.id;
	    			tempNodeText = node.text;
	    			tempNodeTarget = node.target;
	    			$('#skzc').combobox('setValue', '');
	    			$('#skzc').combobox('setValue', 'all');
	    			//$('#tips').show();
	    		}
	    		$('#skzcCombo').show();
			},
			onDblClick:function(node){
				if(loadFlag){
		    		$('#teacherTree').tree('select', tempNodeTarget);
		    		return;
		    	}
			},
			onLoadSuccess:function(node, data){
				if(data.length == 0){
					$('#teacherTree').tree('insert', {data:[{'text':'没有相关教师信息'}]});
				}
				$('#teacherTree').show();
			}
		});
	};
	
	/**初始化空白课程表和未排课程表格
	**/
	function initBlankKcb(){
		//查询当前学期设置的每周天数和每天节数
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Kbcx',
			data : 'active=initBlankKCB&PK_XNXQBM=' + curXnxq,
			dataType:"json",
			success : function(data) {
				//单双周周次
				var weekArray = parseWeekArray(data[0].xqzc);
				oddArray = weekArray[0];
				evenArray = weekArray[1];
			
				var remark = data[0].remark;
				$('#ic_startDate').datebox('setValue', remark.startDate.replace(/\./g, '\-'));
				$('#ic_endDate').datebox('setValue', remark.endDate.replace(/\./g, '\-'));
				$('#weekNum_1').textbox('setValue', remark.weekNum_1);
				$('#contactWay').textbox('setValue', remark.contactWay);
				$('#weekNum_2').textbox('setValue', remark.weekNum_2);
				$('#exam_1').textbox('setValue', remark.exam_1);
				$('#weekNum_3').textbox('setValue', remark.weekNum_3);
				$('#exam_2').textbox('setValue', remark.exam_2);
				$('#weekNum_4').textbox('setValue', remark.weekNum_4);
				$('#exam_3').textbox('setValue', remark.exam_3);
				$('#yearMonth').textbox('setValue', remark.year);
			
				mzts = parseInt(data[0].mzts, 10);//每周天数
				sw = parseInt(data[0].sw, 10);//上午
				zw = parseInt(data[0].zw, 10);//中午
				xw = parseInt(data[0].xw, 10);//下午
				ws = parseInt(data[0].ws, 10);//晚上
				zjs = sw+zw+xw+ws;//总节数
				jcsj = data[0].jcsj.split(',');
				var tdWidth = 100/(mzts+1);
				var kcbContent = '<div id="divPageMask" style="position:absolute;top:0px;left:0px;width:100%;height:100%; overflow:hidden; display:none;"><div id="maskFont">课程信息加载中...<br/><br/>提示：如有老师正在排课，会影响课表显示速度，请耐心等待或稍后再试。</div></div>';
				kcbContent += '<table style="width:100%; height:100%; border-top:1px solid #95B8E7; border-right:1px solid #95B8E7;" cellspacing="0" cellpadding="0">';
				
				for(var i=-1; i<(sw+zw+xw+ws); i++){
					kcbContent += '<tr class="kcbTrStyle"';
					
					if(i == -1){
						kcbContent += ' style="height:45px;"';	
					}
					kcbContent += '>';
					
					for(var j=-1; j<mzts; j++){
						//添加左上角空单元格
						if(i==-1 && j==-1){
							kcbContent += '<td colspan="3" class="kcbTdStyle titleBG splitTd">&nbsp;'+
								'<span id="skzcCombo" style="display:none;">周次：<select name="skzc" id="skzc" class="easyui-combobox" panelHeight="auto" style="width:100px;"></select></span>' +
								'</td>';
							continue;
						}
						
						//判断添加星期数
						if(i==-1 && j>-1){
							//判断是小周还是大周版
							if(mzts < 8){
								kcbContent += '<td class="kcbTdStyle kcbTitle titleBG splitTd">' + weekNameArray[j] + '</td>';	
							}else{
								kcbContent += '<td class="kcbTdStyle kcbTitle titleBG splitTd">大周' + (j+1) + '</td>';
							}
							continue;
						}
						
						//判断添加节次数
						if(i>-1 && j==-1){
							//上午课程
							if(i<sw &&　sw>0){
								//判断第一行
								if(i == 0){
									kcbContent += '<td id="width_sw" rowspan="'+sw+'" class="titleTdStyle kcbTitle titleBG';
									//判断是否还有课
									if(i+sw < zjs){
										kcbContent += ' splitTd';
									}
									kcbContent += '">上午</td>';
								}
								
								kcbContent += '<td id="width_xh" class="titleTdStyle titleBG';
								//判断是否还有课
								if((i+1)==sw && (i+1)<zjs){
									kcbContent += ' splitTd';
								}
								kcbContent += '">' + (i+1) + '</td>';
								
								kcbContent += '<td id="width_sjd" class="titleTdStyle titleBG';
								//判断是否还有课
								if((i+1)==sw && (i+1)<zjs){
									kcbContent += ' splitTd';
								}
								kcbContent += '">';
								if(jcsj.length > i){
									kcbContent += jcsj[i];
								}else{
									kcbContent += '&nbsp;';
								}
								kcbContent += '</td>';
							}
							
							//中午课程
							if(i>=sw && i<(sw+zw) && zw>0){
								//判断第一行
								if(i == sw){
									kcbContent += '<td rowspan="'+zw+'" class="titleTdStyle kcbTitle titleBG';
									//判断是否还有课
									if(i+zw < zjs){
										kcbContent += ' splitTd';
									}
									kcbContent += '">中午</td>';
								}
								
								kcbContent += '<td class="titleTdStyle titleBG';
								//判断是否还有课
								if((i+1)==(sw+zw) && (i+1)<zjs){
									kcbContent += ' splitTd';
								}
								kcbContent += '">' + (i+1) + '</td>';
								kcbContent += '<td class="titleTdStyle titleBG';
								//判断是否还有课
								if((i+1)==(sw+zw) && (i+1)<zjs){
									kcbContent += ' splitTd';
								}
								kcbContent += '">';
								if(jcsj.length > i){
									kcbContent += jcsj[i];
								}else{
									kcbContent += '&nbsp;';
								}
								kcbContent += '</td>';
							}
							
							//下午课程
							if(i>=(sw+zw) && i<(sw+zw+xw) && xw>0){
								//判断第一行
								if(i == sw+zw){
									kcbContent += '<td rowspan="'+xw+'" class="titleTdStyle kcbTitle titleBG';
									//判断是否还有课
									if(i+xw < zjs){
										kcbContent += ' splitTd';
									}
									kcbContent += '">下午</td>';
								}
								
								kcbContent += '<td class="titleTdStyle titleBG';
								//判断是否还有课
								if((i+1)==(sw+zw+xw) && (i+1)<zjs){
									kcbContent += ' splitTd';
								}
								kcbContent += '">' + (i+1) + '</td>';
								kcbContent += '<td class="titleTdStyle titleBG';
								//判断是否还有课
								if((i+1)==(sw+zw+xw) && (i+1)<zjs){
									kcbContent += ' splitTd';
								}
								kcbContent += '">';
								if(jcsj.length > i){
									kcbContent += jcsj[i];
								}else{
									kcbContent += '&nbsp;';
								}
								kcbContent += '</td>';
							}
							
							//晚上课程
							if(i>=(sw+zw+xw) && i<(sw+zw+xw+ws) && ws>0){
								//判断第一行
								if(i == (sw+zw+xw)){
									kcbContent += '<td rowspan="'+ws+'" class="titleTdStyle titleBG kcbTitle">晚上</td>';
								}
								kcbContent += '<td class="titleTdStyle titleBG">' + (i+1) + '</td>'
											+ '<td class="titleTdStyle titleBG">';
								if(jcsj.length > i){
									kcbContent += jcsj[i];
								}else{
									kcbContent += '&nbsp;';
								}
								kcbContent += '</td>';
							}
							
							continue;
						}
						
						//拼接时间序列
						if((j+1) < 10){
							tempOrder = '0'+(j+1);
						}else{
							tempOrder = j+1;
						}
						if((i+1) < 10){
							tempOrder += '0'+(i+1);
						}else{
							tempOrder += i+1;
						}
						
						//添加普通单元格
						kcbContent += '<td id="kcb_' + tempOrder + '" class="kcbTdStyle kcbTdCentent';
						if(((i+1)==sw && zw>0) || ((i+1)==(sw+zw) && xw>0) || ((i+1)==(sw+zw+xw) && ws>0)){
							kcbContent += ' splitTd';
						}
						kcbContent += '">&nbsp;</td>';
					}
					kcbContent += '</tr>';
				}
				
				//备注
				kcbContent += '<tr><td class="teaBzStyle" colspan="'+(3+mzts)+'">' +
						'<div style="width:100%; height:135px; display:inline;">' + 
							'注：<br/>1、本学期所授课程  理论课时_________实训课时_________。（具体可咨询教务处或专业组长）<br/>' +
							'2、本学期开学日期&nbsp;' + remark.startDate.replace(/\./g, '\-') + '&nbsp;结束日期&nbsp;' + remark.endDate.replace(/\./g, '\-') + '<br/>' +
							'3、开学第&nbsp;' + remark.weekNum_1 + '&nbsp;周交授课计划文字版、电子版各一份<br/>' +
							'4、关于多媒体教室和实训室的问题请联系&nbsp;' + remark.contactWay + '&nbsp;。<br/>' +
							'5、本学期&nbsp;' + remark.weekNum_2 + '&nbsp;周课程考试为&nbsp;' + remark.exam_1 + '&nbsp;；<br/>' +
							'<div style="margin-left:22px;">本学期&nbsp;' + remark.weekNum_3 + '&nbsp;周课程考试为&nbsp;' + remark.exam_2 + '&nbsp;；<br/>' +
							'本学期&nbsp;' + remark.weekNum_4 + '&nbsp;周课程考试为&nbsp;' + remark.exam_3 + '&nbsp;；</div>' +
							'6、如有特殊原因需变更授课教师或地点，请先在教务处填写变更申请。<br/><br/>' +
							'<div style="width:100%; text-align:right;">教务处长签名 ：<span style="width:80px;">&nbsp;</span>教学校长签名：<span style="width:80px;">&nbsp;</span>' + remark.year + '<span style="width:35px;">&nbsp;</span></div>' +
						'</div>'+
					'<td></tr>';
				
				kcbContent += '</table>';
				$('#kcbContent').html(kcbContent);
				
				$('#teaTimetable').dialog('open');
				$('.kcbTdStyle').css('width', tdWidth+'%');
				$('#width_sw').css('width', '50px');
				$('#width_xh').css('width', '20px');
				$('#width_sjd').css('width', '130px');
				$('.kcbTrStyle').css('height', (parseInt($('#kcbContent').css('height').substring(0, $('#kcbContent').css('height').length-2)-1, 10)-100)/(sw+xw+ws+1)+'px');
				
				$('#skzc').combobox({
					url:'<%=request.getContextPath()%>/Svl_Kbcx?active=loadWeekCombo&PK_XNXQBM=' + curXnxq,
					valueField:'comboValue',
					textField:'comboName',
					editable:false,
					panelHeight:'140', //combobox高度
					onLoadSuccess:function(data){
						//判断data参数是否为空
						if(data != ''){
							$(this).combobox('setValue', 'all');
						}
					},
					//下拉列表值改变事件
					onChange:function(data){
						if(tempNodeId!='' && $(this).combobox('getValue')!=''){
							loadTeaKcb();
						}
					}
				});
			}
		});
	}
	
	/**读取当前教师课程表**/
	function loadTeaKcb(){
		loadFlag = true;
		document.getElementById('kcbContent').scrollTop = 0;
		$('#divPageMask').show();
		
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Kbcx',
			data : 'active=loadTeaKcb&PK_XNXQBM=' + curXnxq + '&PK_SKJSBH=' + tempNodeId + '&skzc=' + $('#skzc').combobox('getValue'),
			dataType:"json",
			success : function(data) {
				var teaKCB = data[0].kcb;//课程表明细编号,班级排课状态,时间序列,授课计划明细编号,课程名称,授课教师姓名,实际场地名称,授课周次
				fillKcb(teaKCB);//填充当前课程表单元格内的课程信息
				loadFlag = false;
			}
		});
	}
	
	/**填充当前课程表单元格内的课程信息**/
	function fillKcb(teaKCB){
		//清空课程表内容
		$('.kcbTdCentent').removeAttr('title');
		$('.kcbTdCentent').html('&nbsp;');
		$('.kcbTdCentent').css('color', '');
		var tempCourseInfo = '';
		var tempTitle = '';
		
		for(var i=0; i<teaKCB.length; i++){
			tempCourseInfo = teaKCB[i].课程名称 + '<br/>' + teaKCB[i].行政班名称;
			tempTitle = teaKCB[i].课程名称 + '\n' + teaKCB[i].行政班名称 + '\n' + teaKCB[i].授课教师姓名 + '\n' + teaKCB[i].实际场地名称;
			if($('#skzc').combobox('getValue') == 'all'){
				tempTitle += '\n' + parseWeekShow(teaKCB[i].授课周次, oddArray, evenArray);//修改授课周次显示内容
			}
			$('#kcb_' + teaKCB[i].时间序列).attr('title', tempTitle);
			$('#kcb_' + teaKCB[i].时间序列).html(tempCourseInfo);
		}
		
		$('#divPageMask').hide();
	}
	
	function closeDialog(){
		$('#exportTimetable').dialog('close');
	}
	
	
	//导出Excel班级课程表信息
	function ExportExceldaochu(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Kbcx',
			data : "active=ExportExceldaochu&exportType=teaKcb&xnxqbm=" + curXnxq + "&code=" + tempNodeId + "&timetableName=" + encodeURI(encodeURI(tempNodeText)),
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
	
	
	//导出Excel班级课程表信息
	function ExportExceldaochuall(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Kbcx',
			data : "active=ExportExceldaochu&exportType=teaKcbAll&sAuth=" + sAuth + "&xnxqbm=" + curXnxq + "&code=" + tempNodeId + "&timetableName=" + encodeURI(encodeURI(tempNodeText)),
			
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