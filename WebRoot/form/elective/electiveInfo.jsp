<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%
	/**
		创建人：yeq
		Create date: 2015.05.14
		功能说明：用于设置学年学期
		页面类型:列表及模块入口
		修订信息(有多次时,可有多个)
		修订日期:
		原因:
		修订人:
		修订时间:
	**/
 %>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%> 
<html>
  <head>
	<title>选修信息查询</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/icon.css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
  </head>
  <style>
		/*对角线矩形分别对应border-top和border-left两个属性来设定*/
		.d1{border-top:50px threedlightshadow solid;border-left:122px windowframe solid;width:0;height:0;position:relative;}
		.s1,.s2{display:block;width:40px;height:22px;}
		.s1{position:absolute;top:-40px;left:-55px;}
		.s2{position:absolute;bottom:1px;right:60px}
		
		#xlHead,#xlDetail,#xlTime{
			width:100%;
			border-right:1px solid gray;
		}
		#xlHead,#xlTime{
			width:99.9%;
			border-top:1px solid gray;
		}
		
		#xlHead td, #xlDetail td{
			border-left:1px solid gray;
			border-bottom:1px solid gray;
			text-align:center;
		}
		
		#xlTime td, #xlTime th{
			border-left:1px solid gray;
			border-bottom:1px solid gray;
			text-align:center;
			empty-cells:show;
		}
		
		.titleFont{
			font-weight:bold;
		}
		.tdWidth{
			width:12%;
		}
  </style>
<body  class="easyui-layout">
	<div data-options="region:'north'" title="" style="background:#fafafa;height:53px;">
			<table  class = "tablestyle" width = "100%">
				<tr>
					<td class="titlestyle" width="10%" >学年学期</td>
					<td width="20%">
						<select id="SC_XNXQ" name="SC_XNXQ" class="easyui-combobox" style="width:240px;"></select>
					</td>
					<td class="titlestyle" width="10%" >选修班名称</td>
					<td width="20%">
						<select id="SC_XXBM" name="SC_XXBM" class="easyui-textbox" style="width:240px;"></select>
					</td>
					<td align="center" style="width:10%" rowspan=2>
						<a href="#" class="easyui-linkbutton" id="searchSC" name="searchSC" iconCls="icon-search" plain="true" onClick="doToolbar(this.id)">查询</a>
					</td>
				</tr>
				<tr>
					<td class="titlestyle" width="10%">学号</td>
					<td width="20%">
						<input id="SC_XH" name="SC_XH" class="easyui-textbox" style="width:240px;"/>
					</td>
					<td class="titlestyle" width="10%">姓名</td>
					<td width="20%">
						<select id="SC_XM" name="SC_XM" class="easyui-textbox" style="width:240px;"></select>
					</td>
						
				</tr>
			</table>
		</div>
		<div data-options="region:'center'">
			<table id='list' width="100%" >
			</table>
		</div>
	
		<div id="showDialog" style="width:734px;height:336px;overflow:hidden;">
			<iframe id="iframe" name="iframe" width="100%" height="100%"></iframe>
		</div>	
	
</body>
<script type="text/javascript">
	var iUSERCODE="<%=MyTools.getSessionUserCode(request)%>";
	var row = '';      //行数据
	var iKeyCode = ''; //定义主键
	var lastIndex = -1;//使datagrid选中行取消，以便下次选择从新开始
	var pageNum = 1;   //datagrid初始当前页数
	var pageSize = 20; //datagrid初始页内行数
	var GX_XNXQMC_CX = '';//查询条件
	var GX_JXXZ_CX = '';
	
	var jxxz = "";     //教学性质下拉框数据
	var saveType = "";     //判断打开窗口的操作（new or edit）
	
	var tempHolidayArray = ''; //用于临时保存当前选中学期节假日

	
	$(document).ready(function(){
		initCombobox();
		loadGrid();
	});
	
	/**页面初始化加载数据**/
	function loadGrid(){
		$('#list').datagrid({
			url : '<%=request.getContextPath()%>/Svl_CourseSet?active=showSelectionAll&XX_XNXQ='+$('#SC_XNXQ').combobox('getValue')+'&xxbm='+encodeURI(encodeURI($('#SC_XXBM').textbox('getValue')))+'&xh='+$('#SC_XH').textbox('getValue')+'&xm='+encodeURI(encodeURI($('#SC_XM').textbox('getValue'))),
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
				{field:'学号',title:'学号',width:fillsize(0.1),align:'center'},
				{field:'姓名',title:'姓名',width:fillsize(0.1),align:'center'},
				{field:'学年学期编码',title:'学年学期编码',width:fillsize(0.15),align:'center',
					formatter:function(value,rec){
						var xq=rec.学年学期编码;
						xq=xq.substring(0,4)+"年第"+xq.substring(4,5)+"学期";
						return xq;
					}
				},
				{field:'选修班名称',title:'选修班名称',width:fillsize(0.25),align:'center'},
				{field:'授课教师姓名',title:'授课教师姓名',width:fillsize(0.1),align:'center'},
				{field:'场地名称',title:'场地名称',width:fillsize(0.1),align:'center'},
				{field:'授课周次',title:'授课周次',width:fillsize(0.1),align:'center'},
				{field:'上课时间',title:'上课时间',width:fillsize(0.2),align:'center',
					formatter:function(value,rec){
							if(rec.上课时间==""||rec.上课时间==undefined){
								return "";
							}
							var sksj=rec.上课时间;
							var sksj2=sksj.split(",");
							var week=sksj2[0].substring(0,2);
							if(week=="01"){
								week="周一";
							}else if(week=="02"){
								week="周二";
							}else if(week=="03"){
								week="周三";
							}else if(week=="04"){
								week="周四";
							}else if(week=="05"){
								week="周五";
							}else{
							
							}
							var apm=parseInt(sksj2[0].substring(2,4),10);
							var num="";
							if(apm<5){
								apm="上午";
								if(sksj.indexOf(",")>-1){
									num=parseInt(sksj2[0].substring(2,4),10)+"-"+parseInt(sksj2[sksj2.length-1].substring(2,4),10);
								}else{
									num=parseInt(sksj.substring(2,4),10);
								}
							}else if(apm>6){
								apm="下午";
								if(sksj.indexOf(",")>-1){
									num=parseInt(sksj2[0].substring(2,4),10)+"-"+parseInt(sksj2[sksj2.length-1].substring(2,4),10);
								}else{
									num=parseInt(sksj.substring(2,4),10);
								}
							}
							return week+apm+"第"+num+"节课";
					}
				},
				{field:'info',title:'详情',width:fillsize(0.12),align:'center',
					formatter:function(value,rec){
						var button="";
						button='<input id="'+rec.学号+'" type="button" value="[个人信息]" style="cursor:pointer;display:block;" onclick="showStuInfo(this.id);" />';
						return button;
					}
				}	
			]],
			//双击某行时触发
			onDblClickRow:function(rowIndex,rowData){},
			//读取datagrid之前加载
			onBeforeLoad:function(){},
			//单击某行时触发
			onClickRow:function(rowIndex,rowData){
				$('#tsctable').datagrid("unselectRow", rowIndex);
			},
			//加载成功后触发
			onLoadSuccess: function(data){
				iKeyCode = '';
			}
		});

	};
	
	
	
	function electiveTime(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_CourseSet',
			data : 'active=electiveTime',
			dataType:"json",
			success : function(data) {
				$('#GX_XQ').combobox('setValue', data[0].XNXQBM);
				$('#GX_XQKSSJ').datebox('setValue', data[0].KSXKSJ);
				$('#GX_XQJSSJ').datebox('setValue', data[0].JSXKSJ);
				$('#GX_XQKSXS').textbox('setValue', data[0].KSXKXS);
				$('#GX_XQJSXS').textbox('setValue', data[0].JSXKXS);
			}
		});
	}
	
	/**加载combobox控件
		@jxxzData 下拉框数据
	**/
	function initCombobox(){ 
			$('#SC_XNXQ').combobox({
				url:"<%=request.getContextPath()%>/Svl_CourseSet?active=XNXQElecCombobox",
				valueField:'comboValue',
				textField:'comboName',
				editable:false,
				panelHeight:'140', //combobox高度
				onLoadSuccess:function(data){
					
				},
				//下拉列表值改变事件
				onChange:function(data){ 
					
				}
			});
	}

	
	
	/**工具栏按钮调用方法，传入按钮的id
		@id 当前按钮点击事件
	**/
	function doToolbar(id){
		//查询
		if(id == 'searchSC'){
			loadGrid();
		}
	}
	
	function showStuInfo(stuId){
		showDialog("<%=request.getContextPath()%>/form/outsideExam/studentInfo.jsp?stuId="+stuId,"学生信息");
	}
	
	//显示对话操作框
	function showDialog(src, title) {
			$(function(){
				$('#iframe').attr("src",src);
				$('#showDialog').dialog({
					title:title
	//				title:'对话框',//对话框的标题
	//				collapsible:true,//定义是否显示可折叠按钮
	//				minimizable:true,//定义是否显示最小化按钮
	//				maximizable:true//定义是否显示最大化按钮
	//				resizable:true,	//定义对话框是否可编辑大小
				});
			});
	}
		
	function showMSG(msg) {
		$('#showDialog').dialog("close");
		alertMsg(msg);	
	}
	
</script>
</html>