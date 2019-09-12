<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="java.util.Vector"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.src.develop.store.user.*"%>
<%
	/**
		创建人：yeq
		Create date: 2016.10.13
		功能说明：查询导出大补考信息
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
	<title>大补考统计信息</title>
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
<body class="easyui-layout">
	<div id="north" region="north" title="" style="height:33px; overflow:hidden;">
		<table id="ee" singleselect="true" width="100%" class="tablestyle">
			<tr>
				<td style="width:150px;" class="titlestyle">大补考学年</td>
				<td>
					<select name="ic_xn" id="ic_xn" class="easyui-combobox" style="width:200px;">
					</select>
				</td>
				<td style="width:150px;" align="center">
					<div id="queXnList" onclick="doToolbar(this.id)" style="width:55px; height:26px; 
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
		<table id="xnxqList"></table>
	</div>
	
	<!-- 补考信息对话框 -->
	<div id="dbkInfoDialog">
		<div class="easyui-layout" style="width:100%; height:100%;">
			<div region="north" style="height:59px; overflow:hidden;">
				<table id="ee" width="100%" class="tablestyle">
					<tr>
						<td style="width:75px;" class="titlestyle">学号</td>
						<td>
							<input id='ic_xh' name='ic_xh' class="easyui-textbox" style="width:120px;"/>
						</td>
						<td style="width:75px;" class="titlestyle">姓名</td>
						<td>
							<input id='ic_xm' name='ic_xm' class="easyui-textbox" style="width:120px;"/>
						</td>
						<td style="width:75px;" class="titlestyle">整班未登分</td>
						<td>
							<select name="ic_zbwdf" id="ic_zbwdf" class="easyui-combobox" style="width:120px;" panelHeight="auto" editable="false">
								<option value="include">包含</option>
								<option value="exclude" selected="selected">不包含</option>
							</select>
						</td>
						<td style="width:65px;" align="center">
							<div id="queDbkInfoList" onclick="doToolbar(this.id)" style="width:55px; height:26px; 
								border:1px solid #EFEFEF; background-color:#EFEFEF; cursor:pointer; line-height:26px;"
								onmouseenter="$(this).css('border','1px solid #B7D2FF'); $(this).css('background-color','#E9F1FF');" 
								onmouseleave="$(this).css('border','1px solid #EFEFEF'); $(this).css('background-color','#EFEFEF');">
								<img src="<%=request.getContextPath()%>/css/themes/icons/search.png" style="float:left; margin-top:5px; margin-left:3px;"/>
								查询
							</div>
						</td>	
					</tr>
					<tr>
						<td class="titlestyle">班级名称</td>
						<td>
							<input id='ic_bjmc' name='ic_bjmc' class="easyui-textbox" style="width:120px;"/>
						</td>
						<td class="titlestyle">课程名称</td>
						<td>
							<input id='ic_kcmc' name='ic_kcmc' class="easyui-textbox" style="width:120px;"/>
						</td>
						<td class="titlestyle">学年范围</td>
						<td>
							<select name="ic_yearRange" id="ic_yearRange" class="easyui-combobox" style="width:120px;" panelHeight="auto" editable="false">
								<option value="all">全部</option>
								<option value="other" selected="selected">不包含当前学年</option>
								<option value="current">仅当前学年</option>
							</select>
						</td>
						<td>&nbsp;</td>			
					</tr>
			    </table>
			</div>
			<div region="center">
				<table id="dbkInfoList"></table>
			</div>
		</div>
	</div>
	
	<!-- 导出 -->
	<div id="exportDialog" style="overflow:hidden;">
		<table style="width:100%;" class="tablestyle">
			<tr>
				<td style="width:35%;" class="titlestyle">年级</td>
				<td>
					<select name="export_nj" id="export_nj" class="easyui-combobox exportCombo">
					</select>
				</td>
			<tr>
			<tr>
				<td class="titlestyle">专业</td>
				<td>
					<select name="export_zy" id="export_zy" class="easyui-combobox exportCombo">
					</select>
				</td>
			<tr>
			<tr>
				<td class="titlestyle">班级</td>
				<td>
					<select name="export_bj" id="export_bj" class="easyui-combobox exportCombo" disabled="disabled">
						<option value="">请先选择年级</option>
					</select>
				</td>
			<tr>
			<tr>
				<td class="titlestyle">整班未登分</td>
				<td>
					<select name="export_zbwdf" id="export_zbwdf" class="easyui-combobox" style="width:250px;" panelHeight="auto" editable="false">
						<option value="include">包含</option>
						<option value="exclude" selected="selected">不包含</option>
					</select>
				</td>
			<tr>
			<tr>
				<td class="titlestyle">学年范围</td>
				<td>
					<select name="export_yearRange" id="export_yearRange" class="easyui-combobox" style="width:250px;" panelHeight="auto" editable="false">
						<option value="all">全部</option>
						<option value="other" selected="selected">不包含当前学年</option>
						<option value="current">仅当前学年</option>
					</select>
				</td>
			<tr>
			<tr>
				<td colspan="2" align="center">
					<div id="export" onclick="doToolbar(this.id)" style="width:55px; height:26px; 
						border:1px solid #EFEFEF; background-color:#EFEFEF; cursor:pointer; line-height:26px;"
						onmouseenter="$(this).css('border','1px solid #B7D2FF'); $(this).css('background-color','#E9F1FF');" 
						onmouseleave="$(this).css('border','1px solid #EFEFEF'); $(this).css('background-color','#EFEFEF');">
						<img src="<%=request.getContextPath()%>/css/themes/icons/plan_go.png" style="float:left; margin-top:5px; margin-left:3px;"/>
						导出
					</div>
					<!-- <a id="export" href="#" onclick="doToolbar(id);" class="easyui-linkbutton" plain="true" iconcls="icon-submit">导出</a> -->
				</td>				
			</tr>
	    </table>
	</div>
	
	<!-- 分卷信息对话框 -->
	<div id="subMainInfoDialog">
		<div class="easyui-layout" style="width:100%; height:100%;">
			<div region="north" style="height:34px; overflow:hidden;">
				<table id="ee" width="100%" class="tablestyle">
					<tr>
						<td class="titlestyle">学科名称</td>
						<td>
							<input id='ic_fjkcmc' name='ic_fjkcmc' class="easyui-textbox" style="width:155px;"/>
						</td>	
						<td style="width:80px;" align="center">
							<div id="queFjInfoList" onclick="doToolbar(this.id)" style="width:55px; height:26px; 
								border:1px solid #EFEFEF; background-color:#EFEFEF; cursor:pointer; line-height:26px;"
								onmouseenter="$(this).css('border','1px solid #B7D2FF'); $(this).css('background-color','#E9F1FF');" 
								onmouseleave="$(this).css('border','1px solid #EFEFEF'); $(this).css('background-color','#EFEFEF');">
								<img src="<%=request.getContextPath()%>/css/themes/icons/search.png" style="float:left; margin-top:5px; margin-left:3px;"/>查询
							</div>
						</td>
					</tr>
			    </table>
			</div>
			<div region="center">
				<table id="subInfoList"></table>
			</div>
		</div>
	</div>
	
	<!-- 分卷学科新建编辑 -->
	<div id="fjxkInfo" style="overflow:hidden;">
		<form id="form1" method='post'>
			<table style="width:100%;" class="tablestyle">
				<tr>
					<td class="titlestyle" style="width:80px;">学科名称</td>
					<td>
						<select name="subName" id="subName" class="easyui-combobox">
						</select>
					</td>
				</tr>
			</table>
			
			<input type="hidden" id="active" name="active"/>
			<input type="hidden" id="mainCode" name="mainCode"/>
			<input type="hidden" id="XNXQ" name="XNXQ"/>
			<input type="hidden" id="examType" name="examType"/>
			<input type="hidden" id="detailCode" name="detailCode"/>
			<input type="hidden" id="majorCode" name="majorCode"/>
			<input type="hidden" id="majorName" name="majorName"/>
		</form>
	</div>
	
	<!-- 分卷明细信息对话框 -->
	<div id="subDetailDialog">
		<div class="easyui-layout" style="width:100%; height:100%;">
			<div region="center">
				<table id="subDetailList"></table>
			</div>
		</div>
	</div>
	
	<!-- 分卷学科明细新建编辑 -->
	<div id="subDetailSetDialog" style="overflow:hidden;">
		<table style="width:100%;" class="tablestyle">
			<tr>
				<td class="titlestyle" style="width:80px;">专业名称</td>
				<td>
					<select name="subMajor" id="subMajor" class="easyui-combobox">
					</select>
				</td>
			</tr>
		</table>
	</div>
	
	<!-- 下载excel -->
	<iframe id="exportIframe" src="" style="width:0; height:0;"></iframe>
</body>
<script type="text/javascript">
	var iKeyCode = '';
	var mainCode = '';
	var curSelSub = '';
	var detailCode = '';
	var curSelMajor = '';

	$(document).ready(function(){
		initDialog();//初始化对话框
		initData();//页面初始化加载数据
		loadXnGrid();
	});
	
	/**页面初始化加载数据**/
	function initData(){
		$.ajax({
			type:"POST",
			url:'<%=request.getContextPath()%>/Svl_Cjcx',
			data:'active=initBkCombo',
			dataType:"json",
			success:function(data) {
				initCombobox(data[0].exportNjData, data[0].exportZyData);
			}
		});
	}
	
	/**加载combobox控件*/
	function initCombobox(exportNjData, exportZyData){
		$('#ic_xn').combobox({
			url:'<%=request.getContextPath()%>/Svl_Cjcx?active=loadYearCombo', 
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
		
		$('#export_nj').combobox({
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
				$('#export_bj').combobox({
					url:'<%=request.getContextPath()%>/Svl_Cjcx?active=loadExportBjCombo&exportNj=' + $('#export_nj').combobox('getValues').toString() + 
						'&exportZy=' + $('#export_zy').combobox('getValues').toString(),
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
						//判断data参数是否为空
						if(data.length == 1){
							$(this).combobox('setText', '没有可选班级');
							$(this).combobox('disable');
						}else{
							$(this).combobox('enable');
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
		});
		
		$('#export_zy').combobox({
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
				$('#export_bj').combobox({
					url:'<%=request.getContextPath()%>/Svl_Cjcx?active=loadExportBjCombo&exportNj=' + $('#export_nj').combobox('getValues').toString() + 
						'&exportZy=' + $('#export_zy').combobox('getValues').toString(),
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
						//判断data参数是否为空
						if(data.length == 1){
							$(this).combobox('setText', '没有可选班级');
							$(this).combobox('disable');
						}else{
							$(this).combobox('enable');
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
		});
	}
	
	/**加载 dialog控件**/
	function initDialog(){
		$('#dbkInfoDialog').dialog({   
			width: 730,//宽度设置   
			height: 500,//高度设置 
			modal:true,
			closed: true,   
			cache: false, 
			draggable:true,//是否可移动dialog框设置
			//读取事件
			onLoad:function(data){
			},
			//关闭事件
			onClose:function(data){
				$('#ic_xh').textbox('setValue', '');
				$('#ic_xm').textbox('setValue', '');
				$('#ic_bjmc').textbox('setValue', '');
				$('#ic_kcmc').textbox('setValue', '');
				$('#ic_zbwdf').combobox('setValue', 'exclude');
				$('#ic_yearRange').combobox('setValue', 'other');
				$('#dbkInfoList').datagrid('loadData',{total:0,rows:[]});
			}
		});
		
		$('#subMainInfoDialog').dialog({   
			width: 700,//宽度设置   
			height: 500,//高度设置 
			modal:true,
			closed: true,   
			cache: false, 
			draggable:true,//是否可移动dialog框设置
			toolbar:[{
				text:'添加',
				iconCls:'icon-add',
				handler:function(){
					doToolbar('addSub');
				}
			},{
				text:'编辑',
				iconCls:'icon-edit',
				handler:function(){
					doToolbar('editSub');
				}
			},{
				text:'删除',
				iconCls:'icon-cancel',
				handler:function(){
					doToolbar('delSub');
				}
			}],
			//读取事件
			onLoad:function(data){
			},
			//关闭事件
			onClose:function(data){
				$('#ic_fjkcmc').val('');
				$('#subInfoList').datagrid('loadData',{total:0,rows:[]});
			}
		});
		
		$('#fjxkInfo').dialog({
			width: 400,//宽度设置   
			height: 93,//高度设置 
			modal:true,
			closed: true,   
			cache: false, 
			draggable:true,//是否可移动dialog框设置
			toolbar:[{
				text:'保存',
				iconCls:'icon-save',
				handler:function(){
					doToolbar('saveSub');
				}
			}],
			//读取事件
			onLoad:function(data){
			},
			//关闭事件
			onClose:function(data){
			}
		});
		
		$('#subDetailDialog').dialog({   
			width: 700,//宽度设置   
			height: 500,//高度设置 
			modal:true,
			closed: true,   
			cache: false, 
			draggable:true,//是否可移动dialog框设置
			toolbar:[{
				text:'添加',
				iconCls:'icon-add',
				handler:function(){
					doToolbar('addSubDetail');
				}
			},{
				text:'编辑',
				iconCls:'icon-edit',
				handler:function(){
					doToolbar('editSubDetail');
				}
			},{
				text:'删除',
				iconCls:'icon-cancel',
				handler:function(){
					doToolbar('delSubDetail');
				}
			}],
			//读取事件
			onLoad:function(data){
			},
			//关闭事件
			onClose:function(data){
				$('#subDetailList').datagrid('loadData',{total:0,rows:[]});
			}
		});
		
		$('#subDetailSetDialog').dialog({
			width: 400,//宽度设置   
			height: 93,//高度设置 
			modal:true,
			closed: true,   
			cache: false, 
			draggable:true,//是否可移动dialog框设置
			toolbar:[{
				text:'保存',
				iconCls:'icon-save',
				handler:function(){
					doToolbar('saveSubDetail');
				}
			}],
			//读取事件
			onLoad:function(data){
			},
			//关闭事件
			onClose:function(data){
			}
		});
		
		$('#exportDialog').dialog({   
			title: '大补考名册导出',   
			width: 350,//宽度设置   
			height: 198,//高度设置 
			modal:true,
			closed: true,   
			cache: false, 
			draggable:true,//是否可移动dialog框设置
			//读取事件
			onLoad:function(data){
			},
			//关闭事件
			onClose:function(data){
				$('.exportCombo').combobox('clear');
				$('.exportCombo').combobox('setValue', 'all');
				$('#export_zbwdf').combobox('setValue', 'exclude');
				$('#export_yearRange').combobox('setValue', 'other');
			}
		});
	}
	
	/**加载datagrid控件，读取页面信息**/
	function loadXnGrid(){
		$('#xnxqList').datagrid({
			url: '<%=request.getContextPath()%>/Svl_Cjcx',
			queryParams:{'active':'queXnList','XNXQ':$('#ic_xn').combobox('getValue')},
			title:'学年信息列表',
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
				{field:'学年名称',title:'大补考学年',width:fillsize(0.65),align:'center'},
				{field:'col4',title:'操作',width:fillsize(0.35),align:'center',
					formatter:function(value,rec){
						return	"<input type='button' value='[查看]' onclick='openDbkInfo(\"" + rec.学年 + "\",\"" + rec.学年名称 + "\");' style=\"width:60px; cursor:pointer;\"/>" + 
							"<input type='button' value='[分卷设置]' onclick='openFjInfo(\"" + rec.学年 + "\",\"" + rec.学年名称 + "\");' style=\"margin-left:5px; width:80px; cursor:pointer;\"/>" +
							"<input type='button' value='[导出]' onclick='openExportDialog(\"" + rec.学年 + "\");' style=\"margin-left:5px; width:60px; cursor:pointer;\"/>";
				}}
			]],
			//加载成功后触发
			onLoadSuccess: function(data){
				iKeyCode = '';
			}
		});
	};
	
	/**加载补考信息列表datagrid控件，读取页面信息**/
	function loadDbkInfoGrid(){
		$('#dbkInfoList').datagrid({
			url: '<%=request.getContextPath()%>/Svl_Cjcx',
			queryParams:{'active':'queDbkInfoList','XNXQ':iKeyCode,'STUCODE':encodeURI($('#ic_xh').textbox('getValue')),'STUNAME':encodeURI($('#ic_xm').textbox('getValue')),
						'BJMC':encodeURI($('#ic_bjmc').textbox('getValue')),'KCMC':encodeURI($('#ic_kcmc').textbox('getValue')),
						'zbwdfFlag':$('#ic_zbwdf').combobox('getValue'),'yearRange':$('#ic_yearRange').combobox('getValue')},
			title:'学期信息列表',
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
				{field:'学年学期',title:'学年学期',width:fillsize(0.1),align:'center'},
				{field:'学号',title:'学号',width:fillsize(0.1),align:'center'},
				{field:'姓名',title:'姓名',width:fillsize(0.1),align:'center'},
				{field:'行政班名称',title:'班级名称',width:fillsize(0.25),align:'center'},
				{field:'课程名称',title:'课程名称',width:fillsize(0.35),align:'center'},
				{field:'成绩',title:'成绩',width:fillsize(0.1),align:'center'}
			]],
			//加载成功后触发
			onLoadSuccess: function(data){
			}
		});
	};
	
	/**打开补考信息详情*/
	function openDbkInfo(semCode, semName){
		iKeyCode = semCode;
		
		loadDbkInfoGrid();
		$('#dbkInfoDialog').dialog('setTitle', semName + ' 大补考信息');
		$('#dbkInfoDialog').dialog('open');
	}
	
	/**打开分卷信息详情*/
	function openFjInfo(semCode, semName){
		iKeyCode = semCode;
		
		loadFjInfoGrid();
		$('#subMainInfoDialog').dialog('setTitle', semName + '大补考分卷学科信息');
		$('#subMainInfoDialog').dialog('open');
	}
	
	function openExportDialog(semCode){
		iKeyCode = semCode;
		$('#exportDialog').dialog('open');
	}
	
	/**补考名册导出*/
	function exportDbkInfo(){
		window.parent.$('#maskFont').html('文件生成中，请稍后...');
		window.parent.$('#divPageMask').show();
		
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Cjcx',
			data : 'active=dbkmdExport&XNXQ=' + iKeyCode + '&NJDM=' + $('#export_nj').combobox('getValues').toString() + 
				'&ZYMC=' + $('#export_zy').combobox('getValues').toString() + '&BJMC=' + $('#export_bj').combobox('getValues').toString() + 
				'&zbwdfFlag=' + $('#export_zbwdf').combobox('getValue') + '&yearRange=' + $('#export_yearRange').combobox('getValue'),
			dataType:"json",
			success : function(data){
				if(data[0].MSG == '文件生成成功'){
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
	
	/**工具栏按钮调用方法，传入按钮的id
		@id 当前按钮点击事件
	**/
	function doToolbar(id){ 
		//查询学年学期列表
		if(id == 'queXnList'){
			loadXnGrid();
		}
		
		//查询补考信息列表
		if(id == 'queDbkInfoList'){
			loadDbkInfoGrid();
		}
		
		if(id == 'export'){
			exportDbkInfo();
		}
		
		//查询分卷科目信息
		if(id == 'queFjInfoList'){
			loadFjInfoGrid();
		}
		
		//添加分卷学科
		if(id == 'addSub'){
			$('#mainCode').val('');
			loadSubCombo('');
			$('#fjxkInfo').dialog('setTitle','新建');
			$('#fjxkInfo').dialog('open');
		}
		
		//编辑分卷学科
		if(id == 'editSub'){
			if(mainCode == ''){
				alertMsg('请选择一条分卷学科记录');
				return;
			}
		
			$('#mainCode').val(mainCode);
			loadSubCombo(curSelSub);
			$('#fjxkInfo').dialog('setTitle','编辑');
			$('#fjxkInfo').dialog('open');
		}
		
		//保存分卷学科
		if(id == 'saveSub'){
			if($('#subName').combobox('getValue') == ''){
				alertMsg('请选择学科');
				return;
			}
			
			$('#XNXQ').val(iKeyCode);
			$('#examType').val('3');
			$('#active').val(id);
			$('#form1').submit();
		}
		
		//删除分卷学科
		if(id == 'delSub'){
			if(mainCode == ''){
				alertMsg('请选择一条分卷学科记录');
				return;
			}
			
			ConfirmMsg('确定要删除当前选中的分卷学科吗？', 'delSub();', '');
		}
		
		//添加分卷学科明细信息
		if(id == 'addSubDetail'){
			$('#detailCode').val('');
			loadMajorCombo('');
			$('#subDetailSetDialog').dialog('setTitle','新建');
			$('#subDetailSetDialog').dialog('open');
		}
		
		//编辑分卷学科
		if(id == 'editSubDetail'){
			if(detailCode == ''){
				alertMsg('请选择一条分卷学科明细记录');
				return;
			}
			
			$('#detailCode').val(detailCode);
			loadMajorCombo(curSelMajor);
			$('#subDetailSetDialog').dialog('setTitle','编辑');
			$('#subDetailSetDialog').dialog('open');
		}
		
		//保存分卷学科详情
		if(id == 'saveSubDetail'){
			if($('#subMajor').combobox('getValues') == ''){
				alertMsg('请选择专业名称');
				return;
			}
			
			$('#mainCode').val(mainCode);
			$('#majorCode').val($('#subMajor').combobox('getValues').toString());
			$('#majorName').val($('#subMajor').combobox('getText').toString());
			$('#active').val(id);
			$('#form1').submit();
		}
		
		//删除分卷学科
		if(id == 'delSubDetail'){
			if(detailCode == ''){
				alertMsg('请选择一条分卷学科明细记录');
				return;
			}
			
			ConfirmMsg('确定要删除当前选中的分卷学科明细信息吗？', 'delSubDetail();', '');
		}
	}
	
	/**加载补考信息列表datagrid控件，读取页面信息**/
	function loadFjInfoGrid(){
		$('#subInfoList').datagrid({
			url: '<%=request.getContextPath()%>/Svl_Cjcx',
			queryParams:{'active':'queSubInfoList','examType':'3','XNXQ':iKeyCode,'KCMC':encodeURI($('#ic_fjkcmc').textbox('getValue'))},
			title:'学科信息列表',
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
				{field:'主表编号',hidden:true},
				{field:'课程名称',title:'学科名称',width:fillsize(0.8),align:'center'},
				{field:'col4',title:'操作',width:fillsize(0.2),align:'center',
						formatter:function(value,rec){
							return "<input type='button' value='[明细设置]' onclick='openSubDetail(\"" + rec.主表编号 + "\");' style=\"cursor:pointer;\">";
				}}
			]],
			//单击某行时触发
			onClickRow:function(rowIndex,rowData){
				mainCode = rowData.主表编号;
				curSelSub = rowData.课程名称;
			},
			//加载成功后触发
			onLoadSuccess: function(data){
				mainCode = '';
				curSelSub = '';
			}
		});
	};
	
	/**读取可添加的学科下拉框数据*/
	function loadSubCombo(subCode){
		$('#subName').combobox({
			url:'<%=request.getContextPath()%>/Svl_Cjcx?active=loadSubCombo&XNXQ=' + iKeyCode + '&KCMC=' + $('#mainCode').val(),
			valueField:'comboValue',
			textField:'comboName',
			width:'280',
			panelHeight:'140', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data != ''){
					//初始化combobox时赋值
					$(this).combobox('setValue', subCode);
				}
			}
		});
	}
	
	/**表单提交**/
	$('#form1').form({
		//定位到servlet位置的url
		url:'<%=request.getContextPath()%>/Svl_Cjcx',
		//当点击事件后触发的事件
		onSubmit: function(data){}, 
		//当点击事件并成功提交后触发的事件
		success:function(data){
			var json  =  eval("("+data+")");
			
			if(json[0].MSG == '保存成功'){
				showMsg(json[0].MSG);
				
				if($('#active').val() == 'saveSub'){
					$('#fjxkInfo').dialog('close');
					loadFjInfoGrid();
				}else{
					$('#subDetailSetDialog').dialog('close');
					loadFjmxInfoGrid();
				}
			}else{
				alertMsg(json[0].MSG);
			}
		}
	});
	
	/**删除分卷学科*/
	function delSub(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Cjcx',
			data : 'active=delSub&iKeyCode=' + mainCode,
			dataType:"json",
			success : function(data){
				if(data[0].MSG == '删除成功'){
					showMsg(data[0].MSG);
					loadFjInfoGrid();
				}else{
					alertMsg(data[0].MSG);
				}
			}
		});
	}
	
	/**打开分卷学科明细对话框*/
	function openSubDetail(code){
		mainCode = code;
		
		loadFjmxInfoGrid();
		$('#subDetailDialog').dialog('setTitle', '分卷学科详细信息');
		$('#subDetailDialog').dialog('open');
	}
	
	/**加载补考信息列表datagrid控件，读取页面信息**/
	function loadFjmxInfoGrid(){
		$('#subDetailList').datagrid({
			url: '<%=request.getContextPath()%>/Svl_Cjcx',
			queryParams:{'active':'queSubDetailList','iKeyCode':mainCode},
			title:'分卷明细信息列表',
			width:'100%',
			nowrap: false,
			fit:true, //自适应父节点宽度和高度
			showFooter:true,
			striped:true,
			singleSelect:true,
			rownumbers:true,
			fitColumns: true,
			columns:[[
				{field:'明细编号',hidden:true},
				{field:'分卷名称',title:'分卷名称',width:fillsize(0.2),align:'center'},
				{field:'专业代码',hidden:true},
				{field:'专业名称',title:'专业名称',width:fillsize(0.8),align:'center'}
			]],
			//单击某行时触发
			onClickRow:function(rowIndex,rowData){
				detailCode = rowData.明细编号;
				curSelMajor = rowData.专业代码;
			},
			//加载成功后触发
			onLoadSuccess: function(data){
				detailCode = '';
				curSelMajor = '';
			}
		});
	};
	
	/**读取可添加的专业下拉框数据*/
	function loadMajorCombo(majorCode){
		$('#subMajor').combobox({
			url:'<%=request.getContextPath()%>/Svl_Cjcx?active=loadMajorCombo&iKeyCode=' + mainCode + '&ZYMC=' + detailCode,
			valueField:'comboValue',
			textField:'comboName',
			width:'280',
			multiple:true,
			panelHeight:'140', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data != ''){
					//初始化combobox时赋值
					$(this).combobox('setValues', majorCode.split(','));
				}
			},
			onSelect:function(data){
				var obj = $(this).combobox('getValues');
				
				if(obj.length > 1){
					$(this).combobox('unselect', '');
				}
				
				if(data.comboValue == ''){
					$(this).combobox('clear');
					$(this).combobox('setValue', '');
				}
			},
			onUnselect:function(data){
				var obj = $(this).combobox('getValues');
				
				if(obj.length < 1){
					$(this).combobox('select', '');
				}
			}
		});
	}
	
	/**删除分卷学科明细信息*/
	function delSubDetail(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Cjcx',
			data : 'active=delSubDetail&iKeyCode=' + detailCode,
			dataType:"json",
			success : function(data){
				if(data[0].MSG == '删除成功'){
					showMsg(data[0].MSG);
					loadFjmxInfoGrid();
				}else{
					alertMsg(data[0].MSG);
				}
			}
		});
	}
</script>
</html>