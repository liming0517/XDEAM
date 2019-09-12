<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="java.util.Vector"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.src.develop.store.user.*"%>
<%
	/**
		创建人：zhaixuchao
		Create date: 2017.10.12
		功能说明：查询导出登分情况信息
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
	<title>登分情况</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/icon.css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
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
<body class="easyui-layout" style="width:100%;height:100%;">
 	<%-- 遮罩层 --%>
	<div id="divPageMask" class="maskStyle">
		<div id="maskFont"></div>
	</div>
    <div id="north" region="north" title="登分情况" style="height:91px;">
    	<table id="ee" width="100%">
			<tr>
				<td>
					<a href="#" id="dfqkdc" class="easyui-linkbutton" plain="true" iconcls="icon-submit" onClick="doToolbar(this.id);">导出</a>
				</td>
			</tr>
		</table>
   		<table class="tablestyle" width="100%" border="0">
   			<tr>
				<td class="titlestyle">学年学期</td>
				<td >
					<select name="XNXQ_CX" id="XNXQ_CX" class="easyui-combobox" style="width:180px;">
					</select>
				</td>
				<td class="titlestyle">考试名称</td>
				<td >
					<input name="KSMC_CX" id="KSMC_CX" class="easyui-textbox" style="width:180px;"/>
				</td>
				<td class="titlestyle">登分情况</td>
				<td>
					<select name="DFQK_CX" id="DFQK_CX" class="easyui-combobox" style="width:180px;" panelHeight=120px;>
						<option value ="">全部</option>
						<option value ="zbwdf">整班未登分</option>
					</select>
				</td>
				<td style="width:150px;" align="center">
					<a href="#" onclick="doToolbar(this.id)" id="query" class="easyui-linkbutton" plain="true" iconcls="icon-search">查询</a>
				</td>	
			</tr>
	    </table>	    				
    </div>
	<div region="center">
		<table id="pointsList"></table>
	</div> 
	
 	<!-- 下载excel -->
	<iframe id="exportIframe" src="" style="width:0; height:0;"></iframe>
</body>
<script type="text/javascript">
	var sAuth = '<%=sAuth%>';
	var iKeyCode = '';
	var curXnxq = '';
	var pageNum = 1;   //datagrid初始当前页数
	var pageSize = 20; //datagrid初始页内行数
	
	$(document).ready(function(){
		//initDialog();//初始化dialog对话框
		$('#DFQK_CX').combobox('setValue','zbwdf');
		initData();//页面初始化加载数据
		//loadZbwdfGrid();
	});
	
	/**页面初始化加载数据**/
	function initData(){
		$.ajax({
			type:"POST",
			url:'<%=request.getContextPath()%>/Svl_Dfqk',
			data:'active=initData',
			dataType:"json",
			success:function(data) {
				initCombobox(data[0].curXnxq, data[0].xnxqData);
				loadGrid();
			}
		});
	}
	
	/**加载combobox控件
		@njdmData 年级下拉框数据
		@xnxqData 学年学期下拉框数据
	**/
	function initCombobox(curXnxq, xnxqData){
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
	}
	
	/**加载 datagrid控件，读取最外层页面信息
		@listData 列表数据
	**/
	function loadGrid(){
		$('#pointsList').datagrid({
			url: '<%=request.getContextPath()%>/Svl_Dfqk',
			queryParams:{'active':'queryRec','sAuth':sAuth,'page':pageNum ,'rows':pageSize,
				'XNXQ_CX':$('#XNXQ_CX').combobox('getValue'),'KSMC_CX':$('#KSMC_CX').textbox('getValue'),
				'DFQK_CX':$('#DFQK_CX').combobox('getValue')},
			title:'登分情况列表',
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
				{field:'学年学期名称',title:'学年学期名称',width:fillsize(0.2),align:'center'},
				{field:'考试名称',title:'考试名称',width:fillsize(0.35),align:'center'},
				{field:'行政班名称',title:'行政班名称',width:fillsize(0.1),align:'center'},
				{field:'课程名称',title:'课程名称',width:fillsize(0.1),align:'center'},
				{field:'授课教师姓名',title:'登分教师',width:fillsize(0.1),align:'center'},
				{field:'需登分人数',title:'需登分人数',width:fillsize(0.1),align:'center'},
				{field:'已登分人数',title:'已登分人数',width:fillsize(0.1),align:'center'}
			]],
			onClickRow:function(rowIndex,rowData){ 
				 iKeyCode = rowData.考试编号;
			},
			//双击某行时触发
			onDblClickRow:function(rowIndex,rowData){
			},
			//成功加载时
			onLoadSuccess:function(data){//查询成功时候把后台的查询语句赋值给变量listsql
				iKeyCode = '';
			}
		});
	}
	
	/**读取datagrid数据**/
	<%-- function loadData(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Dfqk',
			dataType:"json",
			data : 'active=queryRec&page=' + pageNum + '&rows=' + pageSize + 
				'&XNXQ_CX=' + $('#XNXQ_CX').combobox('getValue') + 
				'&KSMC_CX=' + $('#KSMC_CX').textbox('getValue') +
				'&DFQK_CX=' + $('#DFQK_CX').combobox('getValue'),
			success : function(data) {
				loadGrid(data[0].listData);
			}
		});
	} --%>
	
	/**工具栏按钮调用方法，传入按钮的id
		@id 当前按钮点击事件
	**/
	function doToolbar(id){ 
		//查询
		if(id == "query"){
			loadGrid();
		}
		//登分情况导出
		if(id == "dfqkdc"){
			$('#maskFont').html('登分情况信息导出中...');
    		$('#divPageMask').show();
			ExportExcel();
		}
	}
	
	
	//导出Excel班级学生成绩
	function ExportExcel(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Dfqk',
			data : 'active=ExportExcelRegistrationMark&sAuth=' + sAuth + '&XNXQ_CX=' + $('#XNXQ_CX').combobox('getValue') + '&KSMC_CX=' + $('#KSMC_CX').textbox('getValue') + '&DFQK_CX=' + $('#DFQK_CX').combobox('getValue') + '&DD_XNXQMC=' + $('#XNXQ_CX').combobox('getText') + '&DFQKMC=' + $('#DFQK_CX').combobox('getText'),
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