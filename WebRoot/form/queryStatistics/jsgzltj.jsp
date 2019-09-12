<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.base.common.tools.PublicTools"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.*"%>
<%@ page import="com.pantech.base.common.db.DBSource"%>

<!--
	createTime:2015-6-25
	createUser:刘金璋
	description:教师工作量统计
 -->
<html>
<head>
<title>教师工作量统计</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- JQuery 专用4个文件 -->
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/icon.css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
</head>
<body>
	<div id="aa" class="easyui-layout" data-options="fit:true">
		<div data-options="region:'north',title:'教师工作量统计',split:false,minimizable:false"
			style="height:91px;">
			<table width="100%">
				<tr>
					<td ><a href="#" onclick="doToolbar(this.id);" id="export"
						class="easyui-linkbutton"
						data-options="iconCls:'icon-submit',plain:true">导出</a></td>		
				</tr>
			</table>
			<table class="tablestyle" width="100%">
				<tr>
					<td class="titlestyle">学年学期</td>
					<td><select id="XNXQ" name="XNXQ" class="easyui-combobox" style="width:200px;"></select></td>
					<td class="titlestyle">教师编号</td>
					<td><input id="teaId" name="teaId" class="easyui-textbox" style="width:200px;"></input></td>
					<td class="titlestyle">教师姓名</td>
					<td><input id="teaName" name="teaName" class="easyui-textbox" style="width:200px;"></input></td>		
					<td ><a href="#" onclick="doToolbar(this.id);" id="search"
						class="easyui-linkbutton"
						data-options="iconCls:'icon-search',plain:true">查询</a></td>
				</tr>			
			</table>
		</div>
		<div id="index"
			data-options="region:'center',split:false,border:false">
			<table class="easyui-datagrid" id="List"></table>
		</div>
	</div>
	
	<!-- 课表导出页面 -->
	<div id="exportTimetable">
		<!--引入编辑页面用Ifram-->
		<iframe id="exportTimetableiframe" name="exportTimetableiframe" src='' style='width:100%; height:100%;' frameborder="0" scrolling="no"></iframe>
	</div>
		
	<a id="pageOfficeLink" href="#" style="display:none;"></a>
	
</body>
<script type="text/javascript">
	var iUSERCODE="<%=MyTools.getSessionUserCode(request)%>";
	var pofOpenType = '<%=MyTools.getProp(request, "Base.pofOpenType")%>';
	var pageNum = 1;   //datagrid初始当前页数
	var pageSize = 20; //datagrid初始页内行数
	var xnxqVal="";
	var tempxnxq="";
	var tempjslb="";
	var tempjsbh="";
	var tempjsxm="";
	
	$(document).ready(function(){
		JSLBHCombobox();
		initDialog();
		
	});
	
	//判断浏览器类型
	function getOs(){  
		if(navigator.userAgent.indexOf("MSIE")>0) {  
			return "MSIE";  
		}  
		if(isFirefox=navigator.userAgent.indexOf("Firefox")>0){  
			return "Safari";  
		}  
		if(isSafari=navigator.userAgent.indexOf("Safari")>0||navigator.appVersion.indexOf("Safari")!= -1) {  
			return "Safari";  
		}      
	}

	function loadGrid(){
		$('#List').datagrid({
				url:'<%=request.getContextPath()%>/Svl_queryState',
				queryParams:{"active":"teagzl","XNXQBM":$('#XNXQ').combobox('getValue'),"TEAID":$('#teaId').val(),"TEANAME":$('#teaName').val()},
				loadMsg : "信息加载中请稍侯!",
				fit:true,
				width:'100%',
				nowrap:false,
				striped:true,
				pageSize:20,
				pageNumber:1,
				showFooter:true,
				rownumbers:true,
				singleSelect:true,
				pagination:true,
				fitColumns:true,
				columns:[[
					{field:'学年学期编码',title:'学年学期',width:fillsize(0.1),align:'left',
						formatter:function(value,rec){
							var xnxqbm=rec.学年学期编码;
							var xnxq=xnxqbm.substring(0,4)+"学年第"+xnxqbm.substring(4,5)+"学期";
							return xnxq;
						}
					},
					{field:'授课教师编号',title:'教师工号',width:fillsize(0.08),align:'left'},
					{field:'授课教师姓名',title:'教师姓名',width:fillsize(0.08),align:'left'},
// 					{field:'层级名称',title:'教师类别',width:fillsize(0.1),align:'left'},
					{field:'课程名称',title:'课程名称',width:fillsize(0.1),align:'left'},
					{field:'行政班名称',title:'班级名称',width:fillsize(0.12),align:'left'},
					{field:'授课周次详情',title:'授课周次',width:fillsize(0.08),align:'left'},
					{field:'每周节数',title:'周课时',width:fillsize(0.08),align:'left',
						formatter:function(value,rec){
							var result="";
							if(rec.授课教师编号==""){
								result="";
							}else{
								result=rec.每周节数;
							}
							return result;
						}
					},
					{field:'总人数',title:'班级人数',width:fillsize(0.06),align:'left'},
					{field:'系数',title:'班级系数',width:fillsize(0.06),align:'left'},
					{field:'授课周数',title:'授课周数',width:fillsize(0.08),align:'left',
						formatter:function(value,rec){
							var zs=0; 
							zs=rec.授课周数-rec.假日周次;
							
							if(rec.授课教师编号==""){
								zs="";
							}
							return zs;
						}
					},
					{field:'总课时',title:'课程课时',width:fillsize(0.08),align:'left',
						formatter:function(value,rec){ 
							var zs=0;
							zs=rec.授课周数-rec.假日周次;							
						
							var zks=0;				
							zks=(rec.每周节数*zs).toFixed(1);
								
							if(rec.授课教师编号==""){
								zks="";
							}						
							return zks;
						}
					},
					{field:'周平均课时',title:'周平均课时',width:fillsize(0.08),align:'left',
						formatter:function(value,rec){
							var ks=0;
							var zks=0;
							
							var zs=0;
							zs=rec.授课周数-rec.假日周次;
							
							
							var zzs=0;
							zks=(rec.每周节数*zs).toFixed(1);
							zzs=rec.MSG;
							
							ks=(zks/zzs).toFixed(2);
							if(rec.授课教师编号==""){
								ks="";
							}
							return ks;
						}
					}
					//{field:'费用',title:'费用',width:fillsize(0.06),align:'left'}
				]],
				onClickRow:function(rowIndex,rowData){
				},
				//双击某行时触发
				onDblClickRow:function(rowIndex,rowData){
				},
				//成功加载时
				onLoadSuccess : function(data) {
					tempxnxq=$('#XNXQ').combobox('getValue');
// 					tempjslb=$('#JSLBH').combobox('getText');
					tempjsbh=$('#teaId').val();
					tempjsxm=$('#teaName').val();
				}
			});					
		}
		
		//加载下拉框数据
		function JSLBHCombobox(){
// 			$('#JSLBH').combobox({
// 				url:"< %=request.getContextPath()%>/Svl_queryState?active=JSLBHCombobox",
// 				valueField:'comboValue',
// 				textField:'comboName',
// 				editable:false,
// 				panelHeight:'140', //combobox高度
// 				onLoadSuccess:function(data){
					
// 				},
				//下拉列表值改变事件
// 				onChange:function(data){ 
					
// 				}
// 			});
			
			$('#XNXQ').combobox({
				url:"<%=request.getContextPath()%>/Svl_queryState?active=XNXQCombobox",
				valueField:'comboValue',
				textField:'comboName',
				editable:false,
				panelHeight:'140', //combobox高度
				onLoadSuccess:function(data){
					//判断data参数是否为空
					if(data != ''){
						//初始化combobox时赋值
						$(this).combobox('setValue', data[0].comboValue);
						xnxqVal=$('#XNXQ').combobox('getValue');
						loadGrid();
					}
				},
				//下拉列表值改变事件
				onChange:function(data){}
			});
		}		
		
		//工具按钮
		function doToolbar(iToolbar){
			if(iToolbar=="search"){
				loadGrid();
			}
			
			if(iToolbar=="export"){	
				var browerType="";	
				if(getOs()=="MSIE"){
					browerType="ie";		
				}else{
					browerType="Safari";			
				}
					
				if(pofOpenType == 'normal'){ 
					$('#exportTimetable').dialog('open');
					$("#exportTimetableiframe").attr("src","<%=request.getContextPath()%>/form/timetableQuery/exportWorkLoad.jsp?exportType=workLoad&XNXQ=" + encodeURI(encodeURI(tempxnxq)) + "&TEAID=" + encodeURI(encodeURI(tempjsbh)) + "&TEANAME=" + encodeURI(encodeURI(tempjsxm)) );
				}else{  
					$.ajax({
						type : "POST",
						url : '<%=request.getContextPath()%>/Svl_Kbcx',
						data : "active=loadWorkPageOfficeLink&exportType=workLoad&XNXQ=" + encodeURI(encodeURI(tempxnxq)) + "&TEAID=" + encodeURI(encodeURI(tempjsbh)) + "&TEANAME=" + encodeURI(encodeURI(tempjsxm)) ,
						dataType:"json",
						success : function(data){ 
							$('#pageOfficeLink').attr('href', data[0].linkStr);
							document.getElementById("pageOfficeLink").click();
						}
					});
				}
			}	
		}
		
		/**加载 dialog控件**/
		function initDialog(){
			$('#exportTimetable').dialog({
				title:'导出预览',
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
		
		function closeDialog(){
			$('#exportTimetable').dialog('close');
		}
		
</script>
</html>