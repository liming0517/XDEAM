<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="java.util.Vector"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.src.develop.store.user.*"%>
<%
	/**
		创建人：Zouyu
		Create date: 2017.9.7
		功能说明：学生学籍卡信息
		页面类型:列表及模块入口
		修订信息(有多次时,可有多个)
		原因:导出功能添加字段，并测试其他功能
		修订时间:
	**/
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
    <title>学生信息设置</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> 
	<!-- JQuery 专用4个文件 -->
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/icon.css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
    <!-- JQuery 文件导入结束 -->	
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
		
		.maskFont{
			 font-size:16px;
			 font-weight:bold;
			 color:#2B2B2B;
			 width:250px;
			 height:100%;
			 position:absolute;
			 top:50%;
			 left:50%;
			 margin-top:-10px;
			 margin-left:-125px;
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
    <div title="学生信息" region="north"  style="height:116px; overflow:hidden;">
    	<form method="post" id="form2" name="form2">
    		<table>
				<tr>
					<td><a href="#" onclick="doToolbar(this.id)" id="exportdialog" class="easyui-linkbutton" plain="true" iconcls="icon-submit">批量导出</a></td>
				</tr>
			</table>
			<table id="tabl1" class="tablestyle" name="tabl1" width="100%">
	   			<tr>
	   				<td class="titlestyle">学籍号</td>
			   		<td>
			   			<input class="easyui-textbox" id="JX_XJH_CX" name="JX_XJH_CX" style="width:153px;"/>
					</td>
					<td class="titlestyle">姓名</td>
			   		<td>
			   			<input class="easyui-textbox" id="JX_XM_CX" name="JX_XM_CX" style="width:153px;"/>
					</td>
	   				<td class="titlestyle">行政班名称</td>
			   		<td>
			   			<select class="easyui-combobox" id="JX_XZBDM_CX" name="JX_XZBDM_CX" style="width:153px;">
					   	</select>
					</td>
				</tr>
				<tr>
					<td class="titlestyle">系部名称</td>
			   		<td>
			   			<select class="easyui-combobox" id="JX_XBDM_CX" name="JX_XBDM_CX" style="width:153px;">
				   		</select>
					</td>
					<td class="titlestyle">专业名称</td>
			   		<td>
			   			<select class="easyui-combobox" id="JX_ZYDM_CX" name="JX_ZYDM_CX" style="width:153px;">
				   		</select>
					</td>
					<td class="titlestyle">学生状态</td>
					<td>
			   			<select class="easyui-combobox" id="JX_XSZT_CX" name="JX_XSZT_CX" style="width:153px;">
				   		</select>
					</td>
					
					<td>
						<a href="#" id="que" class="easyui-linkbutton" plain="true" iconCls="icon-search" onclick="doToolbar(this.id);">查询</a>
					</td>
					<!-- <td>&nbsp;</td> -->
	   			</tr>
	   		</table>
	   		<input type="hidden" id="active2" name="active2"/>
   			<input type="hidden" name="sxpath" id="sxpath"/>
	   	</form>
   	</div>
   	<%-- 遮罩层 --%>
   	<div region="center">
   		<table id="XSXXList" name="XSXXList"></table>
   	</div>
	 
	<!-- 批量导出 -->
	<div id="exportExcelDialog">
		<div id="divPageMask" class="maskStyle">
    		<div class="maskFont">批量学籍卡信息导出中，请稍后...</div>
    	</div>
		<div class="easyui-layout" style="width:100%; height:100%;">
			<div region="center"  style="overflow:hidden;">
				<table class="tablestyle" cellpadding="0" cellspacing="0">
					<tr>
						<td class="titlestyle">系部名称</td>
						<td>
							<select name="ic_xbdm" id="ic_xbdm" class="easyui-combobox" style="width:270px;">
							</select>
						</td>
					</tr>
					<tr>
						<td class="titlestyle">年级名称</td>
						<td>
							<select name="ic_njdm" id="ic_njdm" class="easyui-combobox" style="width:270px;">
							</select>
						</td>
					</tr>
					<tr>
						<td class="titlestyle">专业名称</td>
						<td>
							<select name="ic_zydm" id="ic_zydm" class="easyui-combobox" style="width:270px;">
							</select>
						</td>
					</tr>
					<tr>
						<td class="titlestyle">班级名称</td>
						<td>
							<select name="ic_bjmc" id="ic_bjmc" class="easyui-combobox" style="width:270px;">
							</select>
						</td>
					</tr>
					<tr>
						<td colspan="2" style="text-align:center;">
							<a id="multiplePreview" href="#" onclick="doToolbar(id);" class="easyui-linkbutton" plain="true" iconcls="icon-print">打印预览</a>
							<a id="multipleExport" href="#" onclick="doToolbar(id);" class="easyui-linkbutton" plain="true" iconcls="icon-submit">导出</a>
						</td>				
					</tr>
				</table>
			</div>
		</div>
	</div>
		<!-- 下载excel -->
	<iframe id="exportIframe" src="" style="width:0; height:0;"></iframe>
	<!-- 成绩打印预览页面 -->
	<div id="printViewDialog">
		<!--引入编辑页面用Ifram-->
		<iframe id="printViewIframe" name="printViewIframe" src='' style='width:100%; height:100%;' frameborder="0" scrolling="no"></iframe>
	</div>
	<a id="pageOfficeLink" href="#" style="display:none;"></a>
  </body>
<script type="text/javascript">
	var iKeyCode='';//学生学号
	var pofOpenType = '<%=MyTools.getProp(request, "Base.pofOpenType")%>';
	var rowInfo='';//行数据
	var curPageNumber = 1;   //datagrid初始当前页数
	var curPageSize = 20; //datagrid初始页内行数
	var pageNumber = 1;   //datagrid初始当前页数
	var pageSize = 20; //datagrid初始页内行数
	var khInfo = new Array();
	var NameInfo = new Array();
	var exportType='';
	var XSLX='';
	var initFlag = true;
	$(document).ready(function(){
		initExportData();//页面初始化加载数据
		LoadDatagrid();//初始化对话框
		loadComboboxData();//初始化combobox
		initDialog();//初始化对话框
	});
   	/**页面初始化加载数据**/
	function initExportData(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Xsxjk',
			data : 'active=initExportData',
			dataType:"json",
			success : function(data) {
				initExportCombobox(data[0].exportNjData,data[0].exportXbData,data[0].exportZyData);
			}
		});
	} 
	
	/**加载combobox控件
		@jxxzData 下拉框数据
	**/
	 function initExportCombobox(exportNjData, exportXbData, exportZyData){
		$('#ic_xbdm').combobox({
			data:exportXbData,
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			multiple:true,
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
			//下拉列表值改变事件
			onChange:function(data){
				if(initFlag == false)
					loadBJCombobox();
			}
		});
		
		$('#ic_zydm').combobox({
			data:exportZyData,
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			multiple:true,
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
			//下拉列表值改变事件
			onChange:function(data){
				if(initFlag == false)
					loadBJCombobox();
			}
		});
		
		$('#ic_njdm').combobox({
			data:exportNjData,
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			multiple:true,
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
			//下拉列表值改变事件
			onChange:function(data){
				if(initFlag == false)
					loadBJCombobox();
			}
		});
		
		initFlag = false;
		loadBJCombobox();
	}
	
	
	function loadBJCombobox(){
		$('#ic_bjmc').combobox({
			url:'<%=request.getContextPath()%>/Svl_Xsxjk?active=loadExportBjCombo&NJDM='+$('#ic_njdm').combobox('getValues') + 
			'&XBDM='+$('#ic_xbdm').combobox('getValues') + '&SSZY='+$('#ic_zydm').combobox('getValues'),
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			multiple:true,
			panelHeight:'140', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data.length == 1){
					$(this).combobox('setText', '没有可选班级');
					$(this).combobox('disable');
				}else{
					$(this).combobox('enable');
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
			}
		});
	}
	//初始化批量导出
  	function initDialog(){
  		//导出excel dialog框
		$('#exportExcelDialog').dialog({   
			title: '批量导出',   
		 	width: 348,//宽度设置   
			height: 165,//高度设置  
			modal:true,
			closed: true,   
			cache: false, 
			draggable:false,//是否可移动dialog框设置
			//读取事件
			onLoad:function(data){
			},
			//关闭事件
			onClose:function(data){
			$('#ic_xbdm').combobox('setValue','all');
			$('#ic_njdm').combobox('setValue','all');
			$('#ic_zydm').combobox('setValue','all');
			}
		});
		
		$('#printViewDialog').dialog({
			title:'打印预览',
			maximized:true,
			modal:true,
			closed: true,   
			cache: false, 
			draggable:false,//是否可移动dialog框设置
			//关闭事件
			onClose:function(data){
				$("#printViewIframe").attr('src', '');
				//删除预览文件
				delViewFile();
			}
		});
  	}
  	
  	//下拉框初始化
	function loadComboboxData(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Xsxjk',
			data : 'active=initData',
			dataType:"json",
			success : function(data) {
				initCombobox(data[0].bjData,data[0].xmcData,data[0].zymcData,data[0].xsztData);
			}
		});
	}
  	
  	//下拉框赋值
  	function initCombobox(bjData,xmcData,zymcData,xsztData){
	  		//系部
			$('#JX_XBDM_CX').combobox({
				data : xmcData,
				valueField : 'comboValue',
				textField : 'comboName',
				editable : false,
				panelHeight : 'auto',
				onLoadSuccess : function(data) {
					if (data != "") {
						$(this).combobox('setValue', '');
					}
				}
			});
  		//学生状态
		  	$('#JX_XSZT_CX').combobox({
					data : xsztData,
					valueField : 'comboValue',
					textField : 'comboName',
					panelHeight : '140',
					onLoadSuccess : function(data) {
						if (data != "") {
							$(this).combobox('setValue', '');
						}
					}
				});
  			//专业名称
		  	$('#JX_ZYDM_CX').combobox({
					data : zymcData,
					valueField : 'comboValue',
					textField : 'comboName',
					panelHeight : '140',
					onLoadSuccess : function(data) {
						if (data != "") {
							$(this).combobox('setValue', '');
						}
					}
				});
			//行政班
			$('#JX_XZBDM_CX').combobox({
				data : bjData,
				valueField : 'comboValue',
				textField : 'comboName',
				panelHeight : '140',
				onLoadSuccess : function(data) {
					if (data != "") {
						$(this).combobox('setValue', '');
					}
				}
			});
		
	 }
  	
	//主界面datagrid初始化
	function LoadDatagrid(){
		$('#XSXXList').datagrid({
			url:'<%=request.getContextPath()%>/Svl_Xsxjk',
			queryParams:{"active":"queStudent","JX_XJH_CX":encodeURI($('#JX_XJH_CX').val()),"JX_XM_CX":encodeURI($('#JX_XM_CX').val()),
				"JX_XZBDM_CX":$('#JX_XZBDM_CX').combobox('getValue'),"JX_XBDM_CX":$('#JX_XBDM_CX').combobox('getValue'),
				"JX_ZYDM_CX":$('#JX_ZYDM_CX').combobox('getValue'),"JX_XSZT":$('#JX_XSZT_CX').combobox('getValue'),"XSLX":"All"},
			//loadMsg:"学生列表加载中请稍后！",  //当远程加载数据时，现实的等待信息提示
			title:'学生列表',
			width:'100%',
			nowrap: false,
			fit:true, //自适应父节点宽度和高度
			showFooter:true,
			striped:true,
			pagination:true,
			pageSize:curPageSize,
			pageList: [20,50,100,150,200], 
			singleSelect:true,
			pageNumber:curPageNumber,
			rownumbers:true,
			fitColumns: true,
			columns:[[
				{field:'学籍号',title:'学籍号',width:fillsize(0.15),align:'center'},
				{field:'班内学号',title:'学号',width:fillsize(0.1),align:'center'},
				{field:'姓名',title:'姓名',width:fillsize(0.1),align:'center'},
				{field:'系部名称',title:'系部名称',width:fillsize(0.13),align:'center'},
				{field:'行政班名称',title:'行政班名称',width:fillsize(0.15),align:'center'},
				{field:'专业名称',title:'专业名称',width:fillsize(0.2),align:'center'},
				{field:'类别名称',title:'学生状态',width:fillsize(0.1),align:'center'},
				{field:'AA_SET',align:'center',title:'操作',width:fillsize(0.13),formatter:function(value,rec){
					return "<input type='button' value='[学籍卡]' onclick='stuInfo(\"" + rec.学号 +"\",\"" + rec.姓名 +"\",\"" + rec.行政班名称 + "\",\"" + rec.专业名称 +"\",\"" + rec.学生状态 +"\",\"" + rec.行政班代码 +"\")' id='stuInfo' style='margin-left:5px; cursor:pointer;'>";
			  }}
			]],
			//当用户点击某行时触发
			//选中某行既给iKeyCode赋值当前行学科代码
			onClickRow:function(rowIndex, rowData){
				iKeyCode = rowData.学号;
				rowInfo = rowData;
			},
			//当用户双击某行时触发
			onDblClickRow:function(rowIndex, rowData){
				doToolbar('edit');
			},
			//界面加载成功后触发
			onLoadSuccess:function(data){
				iKeyCode = "";
				rowInfo = '';
				curPageSize = $(this).datagrid('options').pageSize;
				curPageNumber = $(this).datagrid('options').pageNumber;
				
				$('#import').linkbutton('enable');
		    	$('#export').linkbutton('enable');
		    	
		    	if(data){
					$.each(data.rows, function(rowIndex, rowData){
						for(var i=0; i<khInfo.length; i++){
							if(khInfo[i] == rowData.学号){
								$('#XSXXList').datagrid('selectRow', rowIndex);
								break;
							}
						}
					});
				}
			} 
 		});
 	}
 	
	function stuInfo(stuCode, stuName, BJMC, YZYMC, stuState, bjdm){
 		if(pofOpenType == 'normal'){
			$('#printViewDialog').dialog('open');			
			$("#printViewIframe").attr("src","<%=request.getContextPath()%>/form/registerScoreManage/stuInfoView.jsp?exportType=classKcb&JX_XJH="+stuCode+"&BJDM="+bjdm+"&XSLX=All");
		}else{
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_XsxxSet',
				data : "active=loadTeaPageOfficeLink&exportType=classKcb&JX_XJH="+stuCode+"&BJBH="+bjdm+"&XM="+stuName+"&XSLX="+"All",
				dataType:"json",
				success : function(data){
					$('#pageOfficeLink').attr('href', data[0].linkStr);
					document.getElementById("pageOfficeLink").click();
				}
			});
		}
	}
		  
	/*批量打印预览*/
	function multiplePreview(){
 		if(pofOpenType == 'normal'){
			$('#printViewDialog').dialog('open');			
			$("#printViewIframe").attr("src","<%=request.getContextPath()%>/form/registerScoreManage/stuInfoView.jsp?exportType=studentInfo" + 
				"&NJDM=" + $('#ic_njdm').combobox('getValues') + "&XBDM=" + $('#ic_xbdm').combobox('getValues') + 
				"&SSZY=" + $('#ic_zydm').combobox('getValues') + "&BJBH="+$('#ic_bjmc').combobox('getValues'));
		}else{
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_XsxxSet',
				data : "active=exportScoreInfo&exportType=studentInfo"+"&NJDM="+$('#ic_njdm').combobox('getValues')+"&XBDM="+$('#ic_xbdm').combobox('getValues')+"&SSZY="+$('#ic_zydm').combobox('getValues')+"&BJBH="+$('#ic_bjmc').combobox('getValues'),
				dataType:"json",
				success : function(data){
					$('#pageOfficeLink').attr('href', data[0].linkStr);
					document.getElementById("pageOfficeLink").click();
					$('#divPageMask').hide();
					$('#exportExcelDialog').dialog('close');
				}
			});
		}
	}
	 
	/*批量导出学籍卡*/ 
	function multipleExport(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_XsxxSet',
			data : "active=exportScoreInfo2&NJDM="+$('#ic_njdm').combobox('getValues')+"&XBDM="+$('#ic_xbdm').combobox('getValues')+"&SSZY="+$('#ic_zydm').combobox('getValues')+"&BJBH="+$('#ic_bjmc').combobox('getValues'),
			dataType:"json",
			success : function(data) {
				$('#divPageMask').hide();
			
				if(data[0].MSG == '文件生成成功'){
					//下载文件到本地
					$("#exportIframe").attr("src", '<%=request.getContextPath()%>/form/scoreQuery/download.jsp?filePath=' + encodeURI(encodeURI(data[0].filePath)));
					$('#exportExcelDialog').dialog('close');
				}else{
					alertMsg(data[0].MSG);
				}
			}
		});
	}
	  
	function doToolbar(iToolbar){
		//查询
		if(iToolbar == "que"){
			curPageNumber = 1;
			khInfo.length=0;
			NameInfo.length=0;
			LoadDatagrid();
		}
		
		//批量下载
		if(iToolbar == "exportdialog"){
			$('#exportExcelDialog').dialog('open');
		}
		
		//打印预览
		if(iToolbar== 'multiplePreview'){
			multiplePreview();//批量导出方法
		}
		
		//批量导出
		if(iToolbar== "multipleExport"){
			$('#divPageMask').show();
			multipleExport();//批量导出方法
		}
	}
	
	function closeDialog(){
		$('#printViewDialog').dialog('close');
	}
  	</script>
</html>