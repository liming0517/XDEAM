<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="java.util.Vector"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.src.develop.store.user.*"%>
<%
	/**
		创建人：ZhaiXuChao
		Create date: 2017.07.27
		功能说明：成绩统计
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
	<title>成绩统计</title>
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
<body class="easyui-layout">
    <div id="north" region="north" title="成绩统计" style="height:59px;">
   		<table class="tablestyle" width="100%" border="0">
   			<tr>
				<td class="titlestyle">学年学期名称</td>
				<td >
					<select name="XNXQ_CX" id="XNXQ_CX" class="easyui-combobox" style="width:180px;">
					</select>
				</td>
				<td class="titlestyle">考试名称</td>
				<td >
					<input name="KSMC_CX" id="KSMC_CX" class="easyui-textbox" style="width:180px;"/>
				</td>
				<td class="titlestyle">考试类别</td>
				<td>
					<select name="KSLB_CX" id="KSLB_CX" class="easyui-combobox" style="width:180px;">
					</select>
				</td>
				<td class="titlestyle">学科</td>
				<td >
					<select name="XK_CX" id="XK_CX" class="easyui-combobox" style="width:180px;">
					</select>
				</td>
				<td style="width:150px;" align="center">
					<a href="#" onclick="doToolbar(this.id)" id="query" class="easyui-linkbutton" plain="true" iconcls="icon-search">查询</a>
				</td>	
			</tr>
	    </table>	    				
    </div>
	<div region="center">
		<table id="classList"></table>
	</div> 
	
	<!--成绩统计详情列表 -->
	<!-- <div id="statisticsListDialog">
		<div class="easyui-layout" style="width:100%; height:100%;">
			<div region="north" style="overflow:hidden;width:100%; height:64px;">
				<table>
					<tr>
						<td><a href="#" onclick="doToolbar(this.id);" id="export" class="easyui-linkbutton" plain="true" iconCls="icon-submit" disabled="true">导出</a></td>
					</tr>
				</table>
			</div>
			<div region="center">
				<table id="sourceList" style="width:100%;"></table>
			</div>
		</div>
	</div> -->
	
	<!-- 成绩统计详情列表-->
	<div id="statisticsListDialog" style="overflow:hidden;">
		<%-- 遮罩层 --%>
    	<div id="divPageMask" class="maskStyle">
    		<div id="maskFont"></div>
    	</div>
 		<div style="width:100%; height:100%;" class="easyui-layout">
 			<div region="north" style="height:34px;" style="overflow:hidden;">
 				<table>
					<tr>
						<td><a href="#" onclick="doToolbar(this.id);" id="export" class="easyui-linkbutton" plain="true" iconCls="icon-submit">导出</a></td>
					</tr>
				</table>
			</div>
			<div region="center">
				<table id="sourceList" style="width:100%; height:100%;"></table>
			</div>
 		</div>
	</div>
	
 	<!-- 下载excel -->
	<iframe id="exportIframe" src="" style="width:0; height:0;"></iframe>
</body>
<script type="text/javascript">
	var sAuth = '<%=sAuth%>';
	var authType = '<%=MyTools.StrFiltr(request.getParameter("authType"))%>';
  	var USERCODE="<%=MyTools.getSessionUserCode(request)%>";
	var pageNum = 1;   //datagrid初始当前页数
	var pageSize = 20; //datagrid初始页内行数
	
	var kcdm_dc="";//课程代码导出
	var kcmc_dc="";
	var xnxqbm_dc="";
	var xnxqmc_dc="";
	var ksbh_dc="";
	var ksmc_dc="";
	
	$(document).ready(function(){
		initData();
		initDialog();//初始化对话框
		
	});
	
	/**页面初始化加载数据**/
	function initData(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Cjtj',
			data : 'active=initData&page=' + pageNum + '&rows=' + pageSize + '&sAuth=' + sAuth,
			dataType:"json",
			success : function(data) {
				initCombobox(data[0].curXnxq, data[0].kslbData, data[0].xnxqData, data[0].kcData);
				loadGrid(data[0].listData);
			}
		});
	}
	
	/**加载 dialog控件**/
	function initDialog(){
		$('#statisticsListDialog').dialog({   
			width: 800,//宽度设置   
			height: 600,//高度设置 
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
			}
		});
	}
	
	/**加载combobox控件
		@jxxzData 下拉框数据
	**/
	function initCombobox(curXnxq, kslbData, xnxqData, kcData){
		$('#XNXQ_CX').combobox({
			data:xnxqData,
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			panelHeight:'140', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data != ''){
					if(curXnxq != ''){
						$(this).combobox('setValue', curXnxq);
					}else{
						//初始化combobox时赋值
						$(this).combobox('setValue', '');
					}
				}
			},
			//下拉列表值改变事件
			onChange:function(data){}
		});
		
		$('#KSLB_CX').combobox({
			data:kslbData,
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
		
		$('#XK_CX').combobox({
			data:kcData,
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
	
	/**读取datagrid数据**/
	function loadData(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Cjtj',
			dataType:"json",
			data : 'active=queryRec&page=' + pageNum + '&rows=' + pageSize + 
				'&CJ_XNXQBM=' + encodeURI($('#XNXQ_CX').combobox('getValue')) + 
				'&CJ_KSMC=' + encodeURI($('#KSMC_CX').textbox('getValue')) +
				'&CJ_KCDM=' + encodeURI($('#XK_CX').textbox('getValue'))+
				'&CJ_KSLB=' + encodeURI($('#KSLB_CX').combobox('getValue'))+
				'&sAuth=' + sAuth,
			success : function(data) {
				loadGrid(data[0].listData);
			}
		});
	}
	
	/**加载 datagrid控件，读取最外层页面信息
		@listData 列表数据
	**/
	function loadGrid(listData){
		$('#classList').datagrid({
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
				{field:'学年学期名称',title:'学年学期名称',width:fillsize(0.3),align:'center'},
				{field:'考试名称',title:'考试名称',width:fillsize(0.7),align:'center'},
				{field:'类别名称',title:'考试类别',width:fillsize(0.2),align:'center'},
				{field:'课程名称',title:'学科',width:fillsize(0.2),align:'center'},
				{field:'AA_SET',align:'center',title:'操作',width:fillsize(0.2),formatter:function(value,rec){
					var str = '';
					str = "<input type='button' value='[查看]' onclick='openSubjectList(\"" + rec.课程代码 + "\",\"" + rec.课程名称 + "\",\"" + rec.学年学期编码 + "\",\"" + rec.学年学期名称+ "\",\"" + rec.考试编号+ "\",\"" + rec.考试名称  + "\")' id='openSubjectList' style='cursor:pointer;'>";
					return str;
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
	
	
	/**加载 datagrid控件，读取最外层页面信息
		@listData 列表数据
	**/
	function loadGridCJTJ(kcdm, kcmc, xnxqbm, xnxqmc, ksbh, ksmc){
			$('#sourceList').datagrid({
				url:'<%=request.getContextPath()%>/Svl_Cjtj',
				queryParams:{"active":"sourceStatisticsRec",
							"CJ_KSBH":encodeURI(ksbh),
							"CJ_KCDM":encodeURI(kcdm),
							"sAuth":sAuth
				},
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
					{field:'行政班名称',title:'班级',width:fillsize(0.7),align:'center'},
					{field:'人数',title:'考试人数',width:fillsize(0.4),align:'center'},
					{field:'优秀',title:'80-100(人数)',width:fillsize(0.3),align:'center'},
					{field:'合格',title:'60-79(人数)',width:fillsize(0.3),align:'center'},
					{field:'不合格',title:'0-59(人数)',width:fillsize(0.3),align:'center'},
					{field:'平均分',title:'平均分',width:fillsize(0.3),align:'center'},
					{field:'优秀率',title:'优秀率',width:fillsize(0.3),align:'center'},
					{field:'合格率',title:'合格率',width:fillsize(0.3),align:'center'}
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
	
	/**打开成绩信息详情对话框**/
	function openSubjectList(kcdm, kcmc, xnxqbm, xnxqmc, ksbh, ksmc){
		kcdm_dc=kcdm;
		kcmc_dc=kcmc;
		xnxqbm_dc=xnxqbm;
		xnxqmc_dc=xnxqmc;
		ksbh_dc=ksbh;
		ksmc_dc=ksmc;
	
	
		loadGridCJTJ(kcdm, kcmc, xnxqbm, xnxqmc, ksbh, ksmc);
		$("#statisticsListDialog").dialog('setTitle',ksmc + ' ' + kcmc + ' 成绩统计');
		$("#statisticsListDialog").dialog('open');
	}
	
	function doToolbar(iToolbar){
		//查询
		if(iToolbar == "query"){
			loadData();
		}
		//导出
		if(iToolbar == 'export'){
			$('#maskFont').html('成绩信息导出中...');
    		$('#divPageMask').show();
			ExportExcel();
		}
	}
	
	//导出学生考试统计信息
	function ExportExcel(){
	
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Cjtj',
			data : 'active=ExportExcel&DC_KCDM=' + kcdm_dc + '&DC_KCMC=' + kcmc_dc+'&DC_XNXQBM=' + xnxqbm_dc + '&DC_KSXKBH=' + xnxqmc_dc + '&DC_KSBH=' + ksbh_dc + '&DC_KSMC=' + ksmc_dc + '&sAuth=' + sAuth,
			dataType:"json",
			success : function(data) {
				$('#divPageMask').hide();
				if(data[0].MSG == '文件生成成功'){
					//下载文件到本地
					$("#exportIframe").attr("src", '<%=request.getContextPath()%>/form/scoreQuery/download.jsp?filePath=' + encodeURI(encodeURI(data[0].filePath)));
				}else{
					alertMsg(data[0].MSG);
				}
			}
		});
	}
	
</script>
</html>