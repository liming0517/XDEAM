<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="java.util.Vector"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.src.develop.store.user.*"%>
<%
	/**
		创建人：zouyu
		Create date: 2017.05.04
		功能说明:学期总评查询
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
	<title>学期总评查询</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/icon.css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
 	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/publicScript.js"></script>
  </head>
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
			 margin-left:-100px;
		}
		
		#scoreInfo{
			width:100%;
			height:100%;
			overflow:hidden;
			display:none;
		}
		
		.scoreTd{
			height:30px;
			border-top:1px solid #95B8E7;
			border-left:1px solid #95B8E7;
		}
		.titleBG{
			font-weight:bold;
			background-color:#EFEFEF;
		}
		.borderBottom{
			border-bottom:3px double #95B8E7;
		}
		
		.borderRight{
			border-right:3px double #95B8E7;
		}
		
		.codeWidth{
			width:40px;
		}
		
		.nameWidth{
			width:100px;
		}
		
		.scoreWidth{
			width:45px;
		}
  </style>
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
	<div id="north" region="north" title="班级信息" style="height:116px; overflow:hidden;" >
		<table>
			<tr>
				<td><a href="#" id="export" class="easyui-linkbutton" plain="true" iconcls="icon-submit" onClick="doToolbar(this.id);">批量导出</a></td>
			</tr>
		</table>
		<table id="ee" singleselect="true" width="100%" class="tablestyle">
			<tr>
				<td style="width:150px;" class="titlestyle">年级名称</td>
				<td>
					<select name="NJDM_CX" id="NJDM_CX" class="easyui-combobox" style="width:180px;">
					</select>
				</td>
				<td style="width:150px;" class="titlestyle">所属系部</td>
				<td>
					<select name="XBDM_CX" id="XBDM_CX" class="easyui-combobox" style="width:180px;">
					</select>
				</td>
				<td style="width:150px;" class="titlestyle">所属专业</td>
				<td>
					<select name="SSZY_CX" id="SSZY_CX" class="easyui-combobox" style="width:180px;">
					</select>
				</td>
				<td style="text-align:center;">
					<a href="#" onclick="doToolbar(this.id)" id="query" class="easyui-linkbutton" plain="true" iconcls="icon-search">查询</a>
				</td>
			</tr>
			<tr>
				<td style="width:100px;" class="titlestyle">学年学期名称</td>
				<td>
					<select name="XNXQBM_CX" id="XNXQBM_CX" class="easyui-combobox" style="width:180px;">
					</select>
				</td>
				<td style="width:150px;" class="titlestyle">班级代码</td>
				<td>
					<input name="BJBH_CX" id="BJBH_CX" class="easyui-textbox" style="width:180px;"/>
				</td>
				<td style="width:150px;" class="titlestyle">班级名称</td>
				<td>
					<input name="BJMC_CX" id="BJMC_CX" class="easyui-textbox" style="width:180px;"/>
				</td>
				<td>&nbsp;</td>
			</tr>
	    </table>
	</div>
	<div region="center">
		<table id="classList" style="width:100%;"></table>
	</div>
	
	<!-- 成绩信息 -->
	<div id="scoreInfoDialog" style="overflow:hidden;">
		<div id="divPageMask2" class="maskStyle">
    		<div class="maskFont">成绩信息导出中，请稍后...</div>
    	</div>
		<div style="width:100%; height:100%;" class="easyui-layout">
			<div region="center" id="mainDiv" style="overflow:hidden;">
				<table id="loadMask" style="width:100%; height:100%; text-align:center; display:none;">
					<tr><td><img src="<%=request.getContextPath()%>/images/loading_3.gif"></td></tr>
				</table>
				<table id="tips" style="width:100%; height:100%; color:#8D8583; text-align:center; display:none;">
					<tr><td><h1>没有相关成绩信息</h1></td></tr>
				</table>
				<div id="scoreInfo">
				</div>
			</div>
		</div>
	</div>
	
	<!-- 批量导出 -->
	<div id="exportExcelDialog">
		<div id="divPageMask" class="maskStyle">
    		<div class="maskFont">成绩信息导出中，请稍后...</div>
    	</div>
		<div class="easyui-layout" style="width:100%; height:100%;">
			<div region="center"  style="overflow:hidden;">
				<table class="tablestyle" cellpadding="0" cellspacing="0">
					<tr>
						<td class="titlestyle" style="width:100px;">学年学期编号</td>
						<td>
							<select name="ic_xnxq" id="ic_xnxq" class="easyui-combobox" style="width:270px;">
							</select>
						</td>
					</tr>
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
							<a id="multipleExport" href="#" onclick="doToolbar(id);" class="easyui-linkbutton" plain="true" iconcls="icon-submit">导出</a>
						</td>				
					</tr>
				</table>
			</div>
		</div>
	</div>
	
	<!-- 下载excel -->
	<iframe id="exportIframe" src="" style="width:0; height:0;"></iframe>
</body>
<script type="text/javascript">
	var sAuth = '<%=sAuth%>';
	var row = '';      //行数据
	var pageNum = 1;   //datagrid初始当前页数
	var pageSize = 20; //datagrid初始页内行数
	//var wzcjShowArray = '';
	var initFlag = true;
	var listWidth = '';
	var listHeight = '';
	//var schoolName = '<-%=MyTools.getProp(request, "Base.schoolName")%>';
	
	$(document).ready(function(){
		//获取成绩列表可显示的宽度高度
		listWidth = $('#mainDiv').css('width');
		listWidth = listWidth.substring(0, listWidth.length-2)-10;
		listHeight = $('#mainDiv').css('height');
		listHeight = listHeight.substring(0, listHeight.length-2)-68;
	
		loadGrid();
		initDialog();//初始化对话框
		initData();//页面初始化加载数据
	});
	
	/**页面初始化加载数据**/
	function initData(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Xqzpcx',
			data : 'active=initData&page=' + pageNum + '&rows=' + pageSize + '&sAuth=' + sAuth,
			dataType:"json",
			success : function(data) {
				//wzcjShowArray = data[0].wzcjShowData;
				//loadGrid(data[0].listData);
				initCombobox(data[0].njdmData, data[0].xbdmData, data[0].sszyData, data[0].xnxqData, data[0].exportNjData, data[0].exportXbData, data[0].exportZyData);
			}
		});
	}
	
	/**加载combobox控件
		@jxxzData 下拉框数据
	**/
	function initCombobox(njdmData, xbdmData, sszyData, xnxqData, exportNjData, exportXbData, exportZyData){
		$('#NJDM_CX').combobox({
			data:njdmData,
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

		$('#XBDM_CX').combobox({
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
			}
		});
		
		$('#SSZY_CX').combobox({
			data:sszyData,
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

		$('#XNXQBM_CX').combobox({
			data:xnxqData,
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
			}
		});
		
		$('#ic_xnxq').combobox({
			data:xnxqData,
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
			onChange:function(data){
				if(initFlag == false)
					loadBJCombobox();
			}
		});
		
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
			url:'<%=request.getContextPath()%>/Svl_Xqzpcx?active=loadExportBjCombo&sAuth=' + sAuth +'&XNXQBM='+$('#ic_xnxq').combobox('getValue') + 
				'&NJDM='+$('#ic_njdm').combobox('getValues') + '&XBDM='+$('#ic_xbdm').combobox('getValues') + '&SSZY='+$('#ic_zydm').combobox('getValues'),
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
	
	/**加载 dialog控件**/
	function initDialog(){
		$('#scoreInfoDialog').dialog({
			maximized:true,
			modal:true,
			closed: true,   
			cache: false, 
			draggable:false,//是否可移动dialog框设置
			toolbar:[{
				text:"返回",
				iconCls:'icon-back',
				handler:function(){
					$('#scoreInfoDialog').dialog('close');
				}
			},{
				text:"导出",
				iconCls:'icon-submit',
				handler:function(){
					doToolbar('singleExport');
				}
			}
			
			],
			//关闭事件
			onClose:function(data){
				$('#scoreInfo').html('');
				$('#scoreInfo').hide();
			}
		});
		
		//导出excel dialog框
		$('#exportExcelDialog').dialog({   
			title: '批量导出',   
			width: 400,//宽度设置   
			height: 190,//高度设置 
			modal:true,
			closed: true,   
			cache: false, 
			draggable:false,//是否可移动dialog框设置
			//读取事件
			onLoad:function(data){
			},
			//关闭事件
			onClose:function(data){
				$('#ic_xnxq').combobox('setValue', '');
				$('#ic_xbdm').combobox('setValue', 'all');
				$('#ic_njdm').combobox('setValue', 'all');
				$('#ic_zydm').combobox('setValue', 'all');
			}
		});
	}

	/**加载 datagrid控件，读取页面信息
		@listData 列表数据
	**/
	function loadGrid(){
		$('#classList').datagrid({
			url:'<%=request.getContextPath()%>/Svl_Xqzpcx',
			queryParams:{'active':'query','page':pageNum,'rows':pageSize,'sAuth':sAuth,'XNXQBM':$('#XNXQBM_CX').combobox('getValue'),
				'BJBH':encodeURI($('#BJBH_CX').textbox('getValue')),'BJMC':encodeURI($('#BJMC_CX').textbox('getValue')),'NJDM':$('#NJDM_CX').combobox('getValue'),
				'XBDM':$('#XBDM_CX').combobox('getValue'),'SSZY':$('#SSZY_CX').combobox('getValue')},
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
				{field:'学年学期名称',title:'学年学期名称',width:fillsize(0.15),align:'center'},
				{field:'班级代码',title:'班级代码',width:fillsize(0.08),align:'center'},
				{field:'班级名称',title:'班级名称',width:fillsize(0.15),align:'center'},
				{field:'年级代码',title:'年级代码',hidden:true},
				{field:'年级名称',title:'年级名称',width:fillsize(0.08),align:'center'},
				{field:'系部代码',title:'系部代码',hidden:true},
				{field:'系部名称',title:'所属系部',width:fillsize(0.1),align:'center'},
				{field:'专业代码',title:'专业代码',hidden:true},
				{field:'专业名称',title:'所属专业',width:fillsize(0.15),align:'center'},
				{field:'col',title:'操作',width:fillsize(0.08),align:'center',formatter:function(value, rec){
					return "<input type='button' value='[查看]' onclick='loadStuScoreInfo(\"" + rec.学年学期编码 +"\", \"" + rec.学年学期名称 +"\", \"" + rec.班级代码 + "\", \"" + rec.班级名称 + "\", \"" + rec.年级代码 + "\", \"" + rec.系部代码 + "\", \"" + rec.专业代码 + "\");' style=\"cursor:pointer;\">";
				}}
			]],
			onClickRow:function(rowIndex,rowData){
				row = rowData;
			},
			//加载成功后触发
			onLoadSuccess: function(data){
				row = '';
			}
		});
	};
	
	/**导出单个班级学生学期总评成绩信息*/
	function exportSingle(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Xqzpcx',
			data : 'active=exportSingle&sAuth=' + sAuth + '&BJBH=' + row.班级代码 + '&BJMC=' + encodeURI(row.班级名称) + '&XNXQBM=' + row.学年学期编码 + '&XBDM=' + row.系部代码 + 
				'&NJDM=' + row.年级代码 + '&SSZY=' + row.专业代码 + '&XNXQMC=' + encodeURI(row.学年学期名称),
			dataType:"json",
			success : function(data) {
				$('#divPageMask2').hide();
				if(data[0].MSG == '文件生成成功'){
					//下载文件到本地
					$("#exportIframe").attr("src", '<%=request.getContextPath()%>/form/registerScoreManage/download.jsp?filePath=' + encodeURIComponent(data[0].filePath));
				}else{
					alertMsg(data[0].MSG);
				}
			}
		});
	}
	
	/**批量导出班级学生学期总评成绩信息*/
	function exportStatisticsInfo(xnxqmc){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Xqzpcx',
			data : 'active=exportScoreInfo&sAuth=' + sAuth + '&NJDM=' + $('#ic_njdm').combobox('getValues') + 
				'&XBDM=' + $('#ic_xbdm').combobox('getValues') + '&SSZY=' + $('#ic_zydm').combobox('getValues') + 
				'&XNXQBM=' + $('#ic_xnxq').combobox('getValue') + '&XNXQMC=' + $('#ic_xnxq').combobox('getText') +
				'&BJBH=' + $('#ic_bjmc').combobox('getValues'),
			dataType:"json",
			success : function(data) {
				$('#divPageMask').hide();
			
				if(data[0].MSG == '文件生成成功'){
					//下载文件到本地
					$("#exportIframe").attr("src", '<%=request.getContextPath()%>/form/registerScoreManage/download.jsp?filePath=' + encodeURIComponent(data[0].filePath));
					$('#exportExcelDialog').dialog('close');
				}else{
					alertMsg(data[0].MSG);
				}
			}
		});
	}
		
	/**工具栏按钮调用方法，传入按钮的id
		@id 当前按钮点击事件
	**/
	function doToolbar(id){
		//查询
		if(id == 'query'){
			loadGrid();
		}
		
		if(id == 'singleExport'){
    		$('#divPageMask2').show();
    		exportSingle();
		}
		
		//批量导出
		if(id == 'export'){
			$('#exportExcelDialog').dialog('open');
		}
		
		//批量导出
		if(id == 'multipleExport'){
			if($('#ic_xnxq').combobox('getValue') == ''){
				alertMsg("请选择学年学期");
				return;
			}
			if($('#ic_bjmc').combobox('getValue')==undefined || $('#ic_bjmc').combobox('getValue')==''){
				alertMsg("没有可导出的学期总评成绩信息");
				return;
			}
			$('#divPageMask').show();
			exportStatisticsInfo();
		}
	}
	
	/**初始化学生成绩信息页面数据*/
	function loadStuScoreInfo(xnxqbm, xnxqmc, bjbh, bjmc,njdm,xbdm,zydm){
		$('#loadMask').show();
		$('#scoreInfoDialog').dialog('setTitle', xnxqmc.replace(' ','')+' '+bjmc+' 学生考试成绩信息');
		$('#scoreInfoDialog').dialog('open');
		
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Xqzpcx',
			data : 'active=loadStuScoreInfo&sAuth=' + sAuth + '&XNXQBM=' + xnxqbm + '&BJBH=' + bjbh+ '&NJDM=' + njdm+ '&XBDM=' + xbdm+ '&SSZY=' + zydm,
			dataType:"json",
			success : function(data) {
				var scoreContent = '';
				var subData = data[0].subData;
				var scoreData = data[0].scoreData;
				var tempScoreData = '';
				var tempScore = '';
			
				if(data[0].MSG == '读取成功'){
					$('#scoreInfo').html('<table id="scoreList" style="border-top:1px solid #95B8E7; border-left:1px solid #95B8E7; border-right:3px solid #95B8E7; border-bottom:3px solid #95B8E7; font-size:12; text-align:center;" cellpadding="0" cellspacing="0"></table>');
					//标题栏
					scoreContent = '<thead><tr><th rowspan="2" class="scoreTd titleBG borderBottom codeWidth">学号</th>' +
								'<th rowspan="2" class="scoreTd titleBG borderBottom borderRight nameWidth">姓名</th>';
					for(var i=0; i<subData.length; i++){
						scoreContent += '<th colspan="5" class="scoreTd titleBG';
						if(i < subData.length-1)
							scoreContent += ' borderRight';
// 						scoreContent += '" style="width:180px; word-wrap:break-word;word-break:break-all;">' + subData[i].colName + '</th>';
						scoreContent += '" style="width:225px; word-wrap:break-word;word-break:break-all;">' + subData[i].colName + '</th>';
					}
					scoreContent +='</tr><tr>';
					for(var i=0; i<subData.length; i++){
						scoreContent += '<th class="scoreTd titleBG borderBottom">平时</th>' + 
									'<th class="scoreTd titleBG borderBottom">期中</th>' + 
									'<th class="scoreTd titleBG borderBottom">期末</th>' + 
									'<th class="scoreTd titleBG borderBottom">加分</th>' + 
									'<th class="scoreTd titleBG borderBottom';
						if(i < subData.length-1)		
							scoreContent += ' borderRight';  
						scoreContent += '">总评</th>';
					}
					scoreContent += '</tr></thead><tbody>';
					
					//表格内容
					for(var i=0; i<scoreData.length; i++){
						scoreContent +='<tr class="score_' + scoreData[i].XJH + '" onmouseover="$(\'.\'+$(this).attr(\'class\')).css(\'background-color\',\'#FFE48D\');" onmouseout="$(\'.\'+$(this).attr(\'class\')).css(\'background-color\',\'#ffffff\');">' + 
							'<td class="scoreTd titleBG">' + scoreData[i].XH + '</td>' + 
							'<td class="scoreTd titleBG borderRight">' + scoreData[i].XM + '</td>';
						tempScoreData = scoreData[i].SCORE;
						
						for(var j=0; j<tempScoreData.length; j++){
							//平时
							tempScore = tempScoreData[j].PS;
							scoreContent += '<td class="scoreTd"'; 
							if(tempScore!='' && (parseInt(tempScore, 10)<60 || tempScore=='缺考' || tempScore=='作弊' || tempScore=='取消资格'))
								scoreContent += ' style="color:red;"';
							if(tempScore == '取消资格')
								tempScore = '取消<br/>资格';
							scoreContent += '>' + (tempScore==''?'&nbsp;':tempScore) + '</td>';
							
							//期中
							tempScore = tempScoreData[j].QZ;
							scoreContent += '<td class="scoreTd"'; 
							if(tempScore!='' && (parseInt(tempScore, 10)<60 || tempScore=='缺考' || tempScore=='作弊' || tempScore=='取消资格'))
								scoreContent += ' style="color:red;"';
							if(tempScore == '取消资格')
								tempScore = '取消<br/>资格';
							scoreContent += '>' + (tempScore==''?'&nbsp;':tempScore) + '</td>';
							
							//期末
							tempScore = tempScoreData[j].QM;
							scoreContent += '<td class="scoreTd"'; 
							if(tempScore!='' && (parseInt(tempScore, 10)<60 || tempScore=='缺考' || tempScore=='作弊' || tempScore=='取消资格'))
								scoreContent += ' style="color:red;"';
							if(tempScore == '取消资格')
								tempScore = '取消<br/>资格';
							scoreContent += '>' + (tempScore==''?'&nbsp;':tempScore) + '</td>';
							
							//加分
							tempScore = tempScoreData[j].JF;
							scoreContent += '<td class="scoreTd">' + (tempScore==''?'&nbsp;':tempScore) + '</td>';
							
							//总评
							tempScore = tempScoreData[j].ZP;
							scoreContent += '<td class="scoreTd';
							if(j < tempScoreData.length-1)
								scoreContent += ' borderRight';
							scoreContent += '"';
							if(tempScore!='' && (parseInt(tempScore, 10)<60 || tempScore=='缺考' || tempScore=='作弊' || tempScore=='取消资格'))
								scoreContent += ' style="color:red;"';
							if(tempScore == '取消资格')
								tempScore = '取消<br/>资格';
							scoreContent += '>' + (tempScore==''?'&nbsp;':tempScore) + '</td>';
						}
						scoreContent +='</tr>';
					}
					scoreContent += '</tbody>';
					$('#tips').hide();
// 					$('#scoreList').css('width', 150+180*(tempScoreData.length)+'px');
					$('#scoreList').css('width', 150+225*(tempScoreData.length)+'px');
					$('#scoreList').html(scoreContent);
					$('#scoreInfo').show();
					FixTable('scoreList', 2, listWidth, listHeight);
				}else{
					$('#tips').show();
				}
				$('#loadMask').hide();
			}
		});
	}
	
	/**锁定表头和列
		@param TableID 要锁定的Table的ID
		@param FixColumnNumber 要锁定列的个数
		@param width 显示的宽度
		@param height 显示的高度
	*/
	function FixTable(TableID, FixColumnNumber, width, height) {
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
    	var HeadHeight = $("#" + TableID + "_tableHead thead").height();
    	HeadHeight += 6;
    	$("#" + TableID + "_tableHead").css("height", HeadHeight);
    	$("#" + TableID + "_tableFix").css("height", HeadHeight);
 
 		//计算tableFix，tableColumn的宽度
    	var ColumnsWidth = 0;
    	var ColumnsNumber = 0;
	    $("#" + TableID + "_tableColumn tr:last td:lt(" + FixColumnNumber + ")").each(function () {
	        ColumnsWidth += $(this).outerWidth(true);
	        ColumnsNumber++;
	    });
	    ColumnsWidth += 6;
	    /*
	    if ($.browser.msie) {
	        switch ($.browser.version) {
	            case "7.0":
	                if (ColumnsNumber >= 3) ColumnsWidth--;
	                break;
	            case "8.0":
	                if (ColumnsNumber >= 2) ColumnsWidth--;
	                break;
	        }
	    }
	    */
	    $("#" + TableID + "_tableColumn").css("width", ColumnsWidth);
	    $("#" + TableID + "_tableFix").css("width", ColumnsWidth);
 
	 	//为tableHead和tableColumn添加联动的滚动条事件
	    $("#" + TableID + "_tableData").scroll(function () {
	        $("#" + TableID + "_tableHead").scrollLeft($("#" + TableID + "_tableData").scrollLeft());
	        $("#" + TableID + "_tableColumn").scrollTop($("#" + TableID + "_tableData").scrollTop());
	    });
	    
	    $("#" + TableID + "_tableFix").css({ "overflow":"hidden", "position":"relative", "z-index":"50"});
	    $("#" + TableID + "_tableHead").css({ "overflow":"hidden", "width":width-17, "position":"relative", "z-index":"45"});
	    $("#" + TableID + "_tableColumn").css({"overflow":"hidden", "height":height-17, "position": "relative", "z-index":"40"});
	    $("#" + TableID + "_tableData").css({"overflow":"auto", "width":width, "height":height, "position":"relative", "z-index":"35"});
	    
	    //为较小的table修正样式
	    if($("#" + TableID + "_tableHead").width() > $("#" + TableID + "_tableFix table").width()){
	        $("#" + TableID + "_tableHead").css("width", $("#" + TableID + "_tableFix table").width());
	        $("#" + TableID + "_tableData").css("width", $("#" + TableID + "_tableFix table").width() + 21);
	    }
	    if($("#" + TableID + "_tableColumn").height() > $("#" + TableID + "_tableColumn table").height()){
	        $("#" + TableID + "_tableColumn").css("height", $("#" + TableID + "_tableColumn table").height());
	        $("#" + TableID + "_tableData").css("height", $("#" + TableID + "_tableColumn table").height() + 21);
	    }
	    
	    $("#" + TableID + "_tableFix").offset($("#" + TableID + "_tableLayout").offset());
	    $("#" + TableID + "_tableHead").offset($("#" + TableID + "_tableLayout").offset());
	    $("#" + TableID + "_tableColumn").offset($("#" + TableID + "_tableLayout").offset());
	    $("#" + TableID + "_tableData").offset($("#" + TableID + "_tableLayout").offset());
	    
	    $('#' + TableID + '_tableData').css('width', width);
	    $('#' + TableID + '_tableData').css('height', height);
	}
	
	/**生成学生成绩列表*/
	/*
	function createScoreList(colData, listData, titleLegth, xnxqmc, bjbh){
		var tempContent = '';
		tempContent = '<table style="width:150%; height:10%;" align="center" cellpadding="1" cellspacing="0">' + 
				'<tr><td style="border:1px solid black;" align="center" colspan="'+titleLegth+'">'+schoolName+xnxqmc+bjbh+'成绩表</td><tr><tr><td style="border:1px solid black;" align="center" rowspan="2">学<br/>号</td><td align="center"  style="border:1px solid black;width:50px" rowspan="2">姓<br/>名</td>'; 
		for(var i=0; i<colData.length; i++){
			tempContent += '<td align="center" colspan="4" style="border:1px solid black;width:120px">'+colData[i].colName+'</td>';
		}
		tempContent +='</tr>';
		tempContent +='<tr>';
		for(var i=0; i<colData.length; i++){
			tempContent += '<td style="border:1px solid black;width:40px" align="center">平<br/>时</td><td style="border:1px solid black;width:40px" align="center">期<br/>中</td><td style="border:1px solid black;width:40px" align="center">期<br/>末</td><td style="border:1px solid black;width:40px" align="center">总<br/>评</td>';
		}
		tempContent += '</tr>';
		
		for(var i=0; i<listData.length; i++){
				tempContent +='<tr><td align="center"  style="border:1px solid black;width:40px">'+listData[i].XH+'</td>';
				tempContent +='<td align="center"  style="border:1px solid black;width:40px">'+listData[i].XM+'</td>';
			for(var j=0; j<listData[i].SCORE.length; j++){
				if(listData[i].SCORE[j].PS!='' && ((listData[i].SCORE[j].PS>=0 && listData[i].SCORE[j].PS<60))){
					tempContent += '<td align="center"  style="border:1px solid black;width:40px;height:30px"><font color="red">'+listData[i].SCORE[j].PS+'</font></td>';
					}else{
					tempContent += '<td align="center"  style="border:1px solid black;width:40px;height:30px">'+listData[i].SCORE[j].PS+'</td>';
				}
				
				if(listData[i].SCORE[j].QZ!='' && ((listData[i].SCORE[j].QZ>=0 && listData[i].SCORE[j].QZ<60))){
					tempContent += '<td align="center"  style="border:1px solid black;width:40px;height:30px"><font color="red">'+listData[i].SCORE[j].QZ+'</font></td>';
					}else{
					tempContent += '<td align="center"  style="border:1px solid black;width:40px;height:30px">'+listData[i].SCORE[j].QZ+'</td>';
				}
				if(listData[i].SCORE[j].QM!='' && ((listData[i].SCORE[j].QM>=0 && listData[i].SCORE[j].QM<60))){
					tempContent += '<td align="center"  style="border:1px solid black;width:40px;height:30px"><font color="red">'+listData[i].SCORE[j].QM+'</font></td>';
					}else{
					tempContent += '<td align="center"  style="border:1px solid black;width:40px;height:30px">'+listData[i].SCORE[j].QM+'</td>';
				}
				if(listData[i].SCORE[j].ZP!='' && ((listData[i].SCORE[j].ZP>=0 && listData[i].SCORE[j].ZP<60))){
					tempContent += '<td align="center"  style="border:1px solid black;width:40px;height:30px"><font color="red">'+listData[i].SCORE[j].ZP+'</font></td>';
					}else{
					tempContent += '<td align="center"  style="border:1px solid black;width:40px;height:30px">'+listData[i].SCORE[j].ZP+'</td>';
				}
				
				}
				tempContent +='</tr>';
		}
		tempContent +='</table>';
		$('#scoreInfo').html(tempContent);
		$('#scoreInfo').show();
	}
	*/
</script>
</html>