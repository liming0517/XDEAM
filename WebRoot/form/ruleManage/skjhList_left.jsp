<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%
	/**
		创建人：wangzh
		Create date: 2015.06.02
		功能说明：用于设置授课计划
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
<%@ page import="com.pantech.src.develop.store.user.*"%>
<%@page import="com.pantech.base.common.tools.PublicTools"%>
<%@page import="java.net.URLEncoder" %>

<%
UserProfile up = new UserProfile(request,MyTools.getSessionUserCode(request)); 
String userName =up.getUserName();
String userCode =up.getUserCode();
%>

<html>
	<head>
		<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/icon.css">
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery-1.8.0.min.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/pubTimetable.js"></script>
	</head>
	
	<body class="easyui-layout" >
		<div data-options="region:'north'" title="" style = "height:61px;width:100%;">
			<table  class = "tablestyle" width = "100%">
				<tr>
					<td class="titlestyle">学年学期</td>
					<td>
						<select id="XNXQ" name="XNXQ" class="easyui-combobox" style="width:150px;"></select>
					</td>
					<td>
						<a href="#" onclick="doToolbar(this.id);" id="que"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-search'">查询</a>
					</td>
				</tr>
				<tr>
					<td class="titlestyle">教学性质</td>
					<td>
						<select id="JXXZ" name="JXXZ" class="easyui-combobox" style="width:150px;"></select>
					</td>
					<td>
						<a href="#" onclick="doToolbar(this.id);" id="import"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-site'">导入</a>
					</td>
				</tr>
			</table>			
		</div>
		<div data-options="region:'center'" id='assistance'>
			<ul id="classTree" class="easyui-tree" style="display: none">
			</ul>
		</div>
		
		<input type="hidden" id="active" name="active" />
		<div id="showDialog" style="width:250px;height:190px;">
			<iframe id="iframe" name="iframe" width="100%" height="100%"></iframe>
		</div>		
	</body>

	<script type="text/javascript">
		var iUSERCODE="<%=MyTools.getSessionUserCode(request)%>";
		var classId = "";//课程号
		var xqbh=""; //学期编号
		var weeks="";//周次
		var parentId = "";//父节点
		var xnxq_cx = '';//查询条件
		var jxxz_cx = '';
		var xnxq = "";//学年学期下拉框数据
		var jxxz = "";//教学性质下拉框数据
		var xnxqVal = "";//当前学年学期数据
		var jxxzVal = "";//当前教学性质数据
		var pageNum = 1;   //datagrid初始当前页数
		var pageSize = 20; //datagrid初始页内行数
		var lastIndex="-1";//上次选中的行
		var tempNodeTarget="";
		
		$(document).ready(function(){
			$('#importfile').hide();
			initData();//页面初始化加载数据
		});
		
		//页面初始化加载数据
		function initData(){
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_Skjh',
				data : 'active=initData&page=' + pageNum + '&rows=' + pageSize+'&iUSERCODE='+iUSERCODE,
				dataType:"json",
				success : function(data) {
					xnxq = data[0].xnxqData;//获取学年学期下拉框数据
					jxxz = data[0].jxxzData;//获取教学性质下拉框数据
					initCombobox(xnxq,jxxz);//初始化下拉框
					loadData("","");
				}
			});
		}
		
		function treegrid(){
			$('#classTree').tree({
				checkbox: false,
				url:'<%=request.getContextPath()%>/Svl_Skjh?active=queryTree&level=0&parentId=""'+'&iUSERCODE='+iUSERCODE+'&GS_XNXQBM='+(xnxqVal+jxxzVal),
				onClick:function(node){
					if(lastIndex!=node.id){
						classId=node.id;
						lastIndex=node.id;
						xqbh=xnxq_cx+jxxz_cx;
						window.parent.window.frames["right"].initGridData(classId,xqbh);//点击后刷新right页面取值结果	
					}					
				},
// 				onBeforeLoad:function(row,param){     //分层显示treegrid
// 				},
				onBeforeExpand:function(node,param){
			  		$('#classTree').tree('options').url='<%=request.getContextPath()%>/Svl_Skjh?active=queryTree&level=1&parentId='+node.id+'&iUSERCODE='+iUSERCODE+'&GS_XNXQBM='+(xnxqVal+jxxzVal);
				},
				onLoadSuccess:function(data){
					$('#classTree').show();
					xnxq_cx = $('#XNXQ').combobox('getValue');
					jxxz_cx = $('#JXXZ').combobox('getValue');
					xqbh=xnxq_cx+jxxz_cx;
			    }
			});
		}
			
		//查询后台数据
		function loadData(xnxq_cx,jxxz_cx){
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_Skjh',
				data : 'active=query&page=' + pageNum + '&rows=' + pageSize + '&iUSERCODE='+iUSERCODE+
					'&JXXZ=' + jxxz_cx + '&XNXQ=' + xnxq_cx + '&termid='+(xnxq_cx+jxxz_cx),
				dataType:"json",
				asyn:false,
				success : function(data) {
					weeks=data[0].MSG;
				}
			});
		}
		
		//加载下拉框数据
		function initCombobox(xnxq,jxxz){
			$('#XNXQ').combobox({
				data:xnxq,
				valueField:'comboValue',
				textField:'comboName',
				editable:false,
				panelHeight:'140', //combobox高度
				onLoadSuccess:function(data){
					//判断data参数是否为空
					if(data != ''){
						//初始化combobox时赋值
						$(this).combobox('setValue', data[0].comboValue);
						xnxqVal=data[0].comboValue;
						treegrid();//加载特殊规则列表
					}
				},
				//下拉列表值改变事件
				onChange:function(data){}
			});
			
			$('#JXXZ').combobox({
				data:jxxz,
				valueField:'comboValue',
				textField:'comboName',
				editable:false,
				panelHeight:'140', //combobox高度
				onLoadSuccess:function(data){
					//判断data参数是否为空
					if(data != ''){
						//初始化combobox时赋值
						$(this).combobox('setValue', data[0].comboValue);
						jxxzVal=data[0].comboValue;
					}
				},
				//下拉列表值改变事件
				onChange:function(data){}
			});
		}
		
		//工具按钮
		function doToolbar(id){
			//查询
			if(id == 'que'){
				xnxqVal = $('#XNXQ').combobox('getValue');
				jxxzVal = $('#JXXZ').combobox('getValue');
				//$('#List').treegrid("unselectRow", lastIndex);	
				classId="";
				lastIndex="";
				treegrid();
			}
			//导入
			if(id == 'import'){
				var url=""; 
	   			url=encodeURI(url); //用了2次encodeURI 
				showDialog("<%=request.getContextPath()%>/form/ruleManage/uploadImport.jsp?"+url,"批量导入培养计划");
			}

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
		
		function show1() {
			$.messager.alert('提示',"请添加文件!");
		}
		function show2() {
			$.messager.alert('提示',"很抱歉!只能上传Excel类型的文件!");
		}
		function show3() {
			$.messager.alert('提示',"请选择要导入的学期!");
		}
		
		//打开teacher编辑窗口
		/* function openImport(){
			$('#importfile').show();
			$('#importfile').dialog({   
				title: '导入',   
				width: 250,//宽度设置   
				height: 91,//高度设置 
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
			$('#importfile').dialog("open");
		}

		//上传导入excel表格
		function doForm(){
			$('#form1').submit();	
		}
		
		$('#form1').form({
			//定位到servlet位置的url
			url:'/ISS/Svl_Import',
			//当点击事件后触发的事件
		    onSubmit: function(data){ 
		    }, 
		    //当点击事件并成功提交后触发的事件
		    success:function(data){
		     	var json = eval("("+data+")");

					
			}
		}); */
		
		
	</script>
</html>