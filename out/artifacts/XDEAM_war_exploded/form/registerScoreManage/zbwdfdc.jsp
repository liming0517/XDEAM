<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="java.util.Vector"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.src.develop.store.user.*"%>
<%
	/**
		创建人：Maowei
		Create date: 2017.5.10
		功能说明：查询导出整班未登分信息
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
	<title>整班未登分统计信息</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/icon.css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
	<style>
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
<body style="margin:0;">
	<div class="easyui-layout" style="width:100%; height:100%;">
		<div id="north" region="north" title="" style="height:66px; overflow:hidden;">
			<table>
				<tr>
					<td>
						<div id="exportClassInfo" onclick="doToolbar(this.id)" style="width:55px; height:26px; font-size:12;
							border:1px solid #ffffff; background-color:#FFFFFF; cursor:pointer; line-height:26px;"
							onmouseenter="$(this).css('border','1px solid #B7D2FF'); $(this).css('background-color','#E9F1FF');" 
							onmouseleave="$(this).css('border','1px solid #ffffff'); $(this).css('background-color','#FFFFFF');">
							<img src="<%=request.getContextPath()%>/css/themes/icons/plan_go.png" style="float:left; margin-top:5px; margin-left:3px;"/>
							&nbsp;&nbsp;导出
						</div>
						<!-- <a href="#" id="exportClassInfo" class="easyui-linkbutton" plain="true" iconcls="icon-submit" onClick="doToolbar(this.id);">导出</a> -->
					</td>
				</tr>
			</table>
			<table id="ee" singleselect="true" width="100%" class="tablestyle">
				<tr>
					<td style="width:150px;" class="titlestyle">学年学期</td>
					<td>
						<select name="ic_xnxq" id="ic_xnxq" class="easyui-combobox" style="width:200px;">
						</select>
					</td>
					<td style="width:150px;" align="center">
						<div id="queZbList" onclick="doToolbar(this.id)" style="width:55px; height:26px; 
							border:1px solid #EFEFEF; background-color:#EFEFEF; cursor:pointer; line-height:26px;"
							onmouseenter="$(this).css('border','1px solid #B7D2FF'); $(this).css('background-color','#E9F1FF');" 
							onmouseleave="$(this).css('border','1px solid #EFEFEF'); $(this).css('background-color','#EFEFEF');">
							<img src="<%=request.getContextPath()%>/css/themes/icons/search.png" style="float:left; margin-top:5px; margin-left:3px;"/>
							查询
						</div>
					</td>
				</tr>
		    </table>
		</div>
		<div region="center">
			<table id="zbwdfInfoList"></table>
		</div>
	</div>
	
	<!-- 整班未登分导出dialog -->
	<div id="zbwdfInfoDialog" style="overflow:hidden;">
		<table style="width:100%;" class="tablestyle">
			<tr>
				<td style="width:35%;" class="titlestyle">起始学年学期</td>
				<td>
					<select name="exportClass_qsxnxq" id="exportClass_qsxnxq" class="easyui-combobox exportCombo">
					</select>
				</td>
			</tr>
			<tr>
				<td style="width:35%;" class="titlestyle">结束学年学期</td>
				<td>
					<select name="exportClass_jsxnxq" id="exportClass_jsxnxq" class="easyui-combobox exportCombo">
					</select>
				</td>
			</tr>
			<tr>
				<td style="width:35%;" class="titlestyle">年级</td>
				<td>
					<select name="exportClass_nj" id="exportClass_nj" class="easyui-combobox exportCombo">
					</select>
				</td>
			</tr>
			<tr>
				<td style="width:35%;" class="titlestyle">系部</td>
				<td>
					<select name="exportClass_xb" id="exportClass_xb" class="easyui-combobox exportCombo" >
					</select>
				</td>
			</tr>
			<tr>
				<td style="width:35%;" class="titlestyle">专业</td>
				<td>
					<select name="exportClass_zy" id="exportClass_zy" class="easyui-combobox exportCombo" >
					</select>
				</td>
			</tr>
			<tr>
				<td colspan="2" style="text-align:center;">
					<a id="zbwdfExport" href="#" onclick="doToolbar(id);" class="easyui-linkbutton" plain="true" iconcls="icon-submit">导出</a>
				</td>				
			</tr>
	    </table>
	</div> 
	
	<!-- 下载excel -->
	<iframe id="exportIframe" src="" style="width:0; height:0;"></iframe>
</body>
<script type="text/javascript">
	var iKeyCode = '';
	var page=1;
	var rows=20;
	
	$(document).ready(function(){
		initDialog();//初始化dialog对话框
		initData();//页面初始化加载数据
		loadZbwdfGrid();
	});
	
	function initData(){
		$.ajax({
			type:"POST",
			url:'<%=request.getContextPath()%>/Svl_Cjcx',
			data:'active=initData',
			dataType:"json",
			success:function(data) {
				LoadCombobox(data[0].exportXnxqData, data[0].exportXbData, data[0].exportNjData, data[0].exportZyData);
				
			$('#ic_xnxq').combobox({
				url:'<%=request.getContextPath()%>/Svl_Cjcx?active=loadzbXnxQCombo', 
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
				onChange:function(data){
					
				}
				});
			}
		});
		
	}
	function LoadCombobox(exportXnxqData,exportXbData,exportNjData,exportZyData){
		//选择导出的起始学年学期combobox
		$('#exportClass_qsxnxq').combobox({
			data:exportXnxqData,
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			width:'250',
			panelHeight:'140', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data != ''){
					//初始化combobox时赋值
					$(this).combobox('setValue', 'all');
				}
			}
		});
		
		//选择导出的结束学年学期combobox
		$('#exportClass_jsxnxq').combobox({
			data:exportXnxqData,
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			width:'250',
			panelHeight:'140', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data != ''){
					//初始化combobox时赋值
					$(this).combobox('setValue', 'all');
				}
			}
		});
		
		//选择导出的年级combobox
		$('#exportClass_nj').combobox({
			data:exportNjData,
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			multiple:true,
			width:'250',
			panelHeight:'140', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data != ''){
					//初始化combobox时赋值
					$(this).combobox('setValue', 'all');
				}
			},
			onSelect:function(data){
				var obj = $(this).combobox('getValues');
				
				if(obj.length > 1){
					$(this).combobox('unselect', 'all');
				}
				
				if(data.comboValue == 'all'){
					$(this).combobox('clear');
					$(this).combobox('setValue', 'all');
				}
			},
			onUnselect:function(data){
				var obj = $(this).combobox('getValues');
				
				if(obj.length < 1){
					$(this).combobox('select', 'all');
				}
			},
			onChange:function(data){
			}
		});	
		
		//选择导出的系部combobox
		$('#exportClass_xb').combobox({
			data:exportXbData,
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			multiple:true,
			width:'250',
			panelHeight:'140', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data != ''){
					//初始化combobox时赋值
					$(this).combobox('setValue', 'all');
				}
			},
			onSelect:function(data){
				var obj = $(this).combobox('getValues');
				
				if(obj.length > 1){
					$(this).combobox('unselect', 'all');
				}
				
				if(data.comboValue == 'all'){
					$(this).combobox('clear');
					$(this).combobox('setValue', 'all');
				}
			},
			onUnselect:function(data){
				var obj = $(this).combobox('getValues');
				
				if(obj.length < 1){
					$(this).combobox('select', 'all');
				}
			},
			onChange:function(data){
			}
		});
		
		//选择导出的专业combobox
		$('#exportClass_zy').combobox({
			data:exportZyData,
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			multiple:true,
			width:'250',
			panelHeight:'140', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data != ''){
					//初始化combobox时赋值
					$(this).combobox('setValue', 'all');
				}
			},
			onSelect:function(data){
				var obj = $(this).combobox('getValues');
				
				if(obj.length > 1){
					$(this).combobox('unselect', 'all');
				}
				
				if(data.comboValue == 'all'){
					$(this).combobox('clear');
					$(this).combobox('setValue', 'all');
				}
			},
			onUnselect:function(data){
				var obj = $(this).combobox('getValues');
				
				if(obj.length < 1){
					$(this).combobox('select', 'all');
				}
			},
			onChange:function(data){
			}
		});
	}
	
	//整班未登分导出液初始化
	function initDialog(){
		$('#zbwdfInfoDialog').dialog({   
			title: '整班未登分信息导出',   
			width: 450,//宽度设置   
			height: 191,//高度设置 
			modal:true,
			closed: true,   
			cache: false, 
			draggable:true,//是否可移动dialog框设置
			//关闭事件
			onClose:function(data){
				$('.exportCombo').combobox('clear');
				$('.exportCombo').combobox('setValue', 'all');
			}
		});
	}
	
	//加载整班未登分详细信息
	function loadZbwdfGrid(){
		$('#zbwdfInfoList').datagrid({
			url: '<%=request.getContextPath()%>/Svl_Cjcx',
			queryParams:{'active':'queZbwdfList','XNXQ':$('#ic_xnxq').combobox('getValue')},
			title:'整班未登分信息列表',
			width:'100%',
			nowrap: false,
			fit:true, //自适应父节点宽度和高度
			showFooter:true,
			striped:true,
			pagination:true,
			pageSize:rows,
			pageNumber:page,
			singleSelect:true,
			rownumbers:true,
			fitColumns: true,
			columns:[[
				//field为读取数据的数据名，title为显示的数据名，width宽度设置，align数字在表格中显示的位置
				{field:'学年学期名称',title:'学年学期名称',width:fillsize(0.23),align:'center'},
				{field:'行政班名称',title:'行政班名称',width:fillsize(0.22),align:'center'},
				{field:'课程名称',title:'课程名称',width:fillsize(0.2),align:'center'},
				{field:'登分教师姓名',title:'登分教师',width:fillsize(0.15),align:'center'},
				{field:'需登分人数',title:'需登分人数',width:fillsize(0.1),align:'center'},
				{field:'已登分人数',title:'已登分人数',width:fillsize(0.1),align:'center'}
				
			]],
			//加载成功后触发
			onLoadSuccess: function(data){
				iKeyCode = '';
			}
		});
	};
	
	//导出提交
	function exportZBInfo(){
		window.parent.$('#maskFont').html('文件生成中，请稍后...');
		window.parent.$('#divPageMask').show();
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Cjcx',
			data : 'active=zbwdfExport&export_qsxnxq='
					+$('#exportClass_qsxnxq').combobox('getValue').toString()+'&export_jsxqxq='
					+$('#exportClass_jsxnxq').combobox('getValue').toString()+'&export_nj='
					+$('#exportClass_nj').combobox('getValues').toString()+'&export_xb='
					+$('#exportClass_xb').combobox('getValues').toString()+'&export_zy='
					+$('#exportClass_zy').combobox('getValues').toString(),
			dataType:"json",
			success : function(data){
				if(data[0].MSG == '成绩文件生成成功'){
					//下载文件到本地
					$("#exportIframe").attr("src", '<%=request.getContextPath()%>/form/registerScoreManage/download.jsp?filePath=' + encodeURIComponent(data[0].filePath));
					$('#exportDialog').dialog('close');
				}else{
					alertMsg(data[0].MSG);
				}
				window.parent.$('#divPageMask').hide();
			}
		});
	}
	
	function doToolbar(id){
		//查询整班未登分
		if(id == 'queZbList'){
			loadZbwdfGrid();
		}
		//打开整班未登分导出页面
		if(id == 'exportClassInfo'){
			$('#zbwdfInfoDialog').dialog('open');
		}
		//导出
		if(id == 'zbwdfExport'){
			exportZBInfo();
		} 
	}
</script>
</html>