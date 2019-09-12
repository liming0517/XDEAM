<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="java.util.Vector"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.src.develop.store.user.*"%>
<%
	/**
		创建人：yeq
		Create date: 2016.07.21
		功能说明：查询导出补考信息
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
	<title>补考统计信息</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/icon.css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/publicScript.js"></script>
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
				<td style="width:150px;" class="titlestyle">学年学期</td>
				<td>
					<select name="ic_xnxq" id="ic_xnxq" class="easyui-combobox" style="width:200px;">
					</select>
				</td>
				<td style="width:150px;" align="center">
					<div id="queXnxqList" onclick="doToolbar(this.id)" style="width:55px; height:26px; 
						border:1px solid #EFEFEF; background-color:#EFEFEF; cursor:pointer; line-height:26px;"
						onmouseenter="$(this).css('border','1px solid #B7D2FF'); $(this).css('background-color','#E9F1FF');" 
						onmouseleave="$(this).css('border','1px solid #EFEFEF'); $(this).css('background-color','#EFEFEF');">
						<img src="<%=request.getContextPath()%>/css/themes/icons/search.png" style="float:left; margin-top:5px; margin-left:3px;"/>
						查询
					</div>
					<!-- <a href="#" onclick="doToolbar(this.id)" id="queXnxqList" class="easyui-linkbutton" plain="true" iconcls="icon-search">查询</a> -->
				</td>
			</tr>
	    </table>
	</div>
	<div region="center">
		<table id="xnxqList"></table>
	</div>
	
	<!-- 补考信息对话框 -->
	<div id="bkInfoDialog">
		<div class="easyui-layout" style="width:100%; height:100%;">
			<div region="north" style="height:58px; overflow:hidden;">
				<table id="ee" width="100%" class="tablestyle">
					<tr>
						<td style="width:80px;" class="titlestyle">学号</td>
						<td>
							<input id='ic_xh' name='ic_xh' class="easyui-textbox" style="width:155px;"/>
						</td>
						<td style="width:80px;" class="titlestyle">姓名</td>
						<td>
							<input id='ic_xm' name='ic_xm' class="easyui-textbox" style="width:155px;"/>
						</td>
						<td style="width:80px;" class="titlestyle">整班未登分</td>
						<td>
							<select name="ic_zbwdf" id="ic_zbwdf" class="easyui-combobox" style="width:155px;" panelHeight="auto" editable="false">
								<option value="include">包含</option>
								<option value="exclude" selected="selected">不包含</option>
							</select>
						</td>
					</tr>
					<tr>
						<td class="titlestyle">班级名称</td>
						<td>
							<input id='ic_bjmc' name='ic_bjmc' class="easyui-textbox" style="width:155px;"/>
						</td>
						<td class="titlestyle">课程名称</td>
						<td>
							<input id='ic_kcmc' name='ic_kcmc' class="easyui-textbox" style="width:155px;"/>
						</td>					
						<td colspan="2" align="center">
							<div id="queBkInfoList" onclick="doToolbar(this.id)" style="width:55px; height:26px; 
								border:1px solid #EFEFEF; background-color:#EFEFEF; cursor:pointer; line-height:26px;"
								onmouseenter="$(this).css('border','1px solid #B7D2FF'); $(this).css('background-color','#E9F1FF');" 
								onmouseleave="$(this).css('border','1px solid #EFEFEF'); $(this).css('background-color','#EFEFEF');">
								<img src="<%=request.getContextPath()%>/css/themes/icons/search.png" style="float:left; margin-top:5px; margin-left:3px;"/>
								查询
							</div>
							<!-- <a href="#" onclick="doToolbar(this.id)" id="queBkInfoList" class="easyui-linkbutton" plain="true" iconcls="icon-search">查询</a> -->
						</td>			
					</tr>
			    </table>
			</div>
			<div region="center">
				<table id="bkInfoList"></table>
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
	
	<!-- 下载excel -->
	<iframe id="exportIframe" src="" style="width:0; height:0;"></iframe>
</body>
<script type="text/javascript">
	var iKeyCode = '';

	$(document).ready(function(){
		initDialog();//初始化对话框
		initData();//页面初始化加载数据
		loadXnxqGrid();
	});
	
	/**页面初始化加载数据**/
	function initData(){
		$.ajax({
			type:"POST",
			url:'<%=request.getContextPath()%>/Svl_Cjcx',
			data:'active=initBkCombo',
			dataType:"json",
			success:function(data) {
				initCombobox(data[0].xnxqData, data[0].exportNjData, data[0].exportZyData);
			}
		});
	}
	
	/**加载combobox控件*/
	function initCombobox(xnxqData, exportNjData, exportZyData){
		$('#ic_xnxq').combobox({
			data:xnxqData,
			//url:'<-%=request.getContextPath()%>/Svl_Cjcx?active=loadXnxqCombo', 
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
		$('#bkInfoDialog').dialog({   
			title: '补考信息',   
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
				$('#bkInfoList').datagrid('loadData',{total:0,rows:[]});
			}
		});
		
		$('#exportDialog').dialog({   
			title: '补考名册导出',   
			width: 350,//宽度设置   
			height: 172,//高度设置 
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
			}
		});
	}
	
	/**加载班级列表datagrid控件，读取页面信息**/
	function loadXnxqGrid(){
		$('#xnxqList').datagrid({
			url: '<%=request.getContextPath()%>/Svl_Cjcx',
			queryParams:{'active':'queXnxqList','XNXQ':$('#ic_xnxq').combobox('getValue')},
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
				{field:'学年学期编码',title:'学年学期编码',width:fillsize(0.4),align:'center'},
				{field:'学年学期名称',title:'学年学期名称',width:fillsize(0.4),align:'center'},
				{field:'col4',title:'操作',width:fillsize(0.2),align:'center',
					formatter:function(value,rec){
						return	"<input type='button' value='[查看]' onclick='openBkInfo(\"" + rec.学年学期编码 + "\",\"" + rec.学年学期名称 + "\");' style=\"width:60px; cursor:pointer;\"/>" + 
							"<input type='button' value='[导出]' onclick='openExportDialog(\"" + rec.学年学期编码 + "\");' style=\"margin-left:5px; width:60px; cursor:pointer;\"/>";
				}}
			]],
			//加载成功后触发
			onLoadSuccess: function(data){
				iKeyCode = '';
			}
		});
	};
	
	/**加载补考信息列表datagrid控件，读取页面信息**/
	function loadBkInfoGrid(){
		$('#bkInfoList').datagrid({
			url: '<%=request.getContextPath()%>/Svl_Cjcx',
			queryParams:{'active':'queBkInfoList','XNXQ':iKeyCode,'STUCODE':encodeURI($('#ic_xh').textbox('getValue')),'STUNAME':encodeURI($('#ic_xm').textbox('getValue')),
						'BJMC':encodeURI($('#ic_bjmc').textbox('getValue')),'KCMC':encodeURI($('#ic_kcmc').textbox('getValue')),'zbwdfFlag':$('#ic_zbwdf').combobox('getValue')},
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
			//a.学号,a.姓名,d.行政班名称,c.课程名称,a.总评 as 成绩
				//field为读取数据的数据名，title为显示的数据名，width宽度设置，align数字在表格中显示的位置
				{field:'学号',title:'学号',width:fillsize(0.1),align:'center'},
				{field:'姓名',title:'姓名',width:fillsize(0.1),align:'center'},
				{field:'行政班名称',title:'班级名称',width:fillsize(0.3),align:'center'},
				{field:'课程名称',title:'课程名称',width:fillsize(0.4),align:'center'},
				{field:'成绩',title:'成绩',width:fillsize(0.1),align:'center'}
			]],
			//加载成功后触发
			onLoadSuccess: function(data){
			}
		});
	};
	
	/**打开补考信息详情*/
	function openBkInfo(semCode, semName){
		iKeyCode = semCode;
		
		loadBkInfoGrid();
		$('#bkInfoDialog').dialog('setTitle', semName + ' 补考信息');
		$('#bkInfoDialog').dialog('open');
	}
	
	function openExportDialog(semCode){
		iKeyCode = semCode;
		$('#exportDialog').dialog('open');
	}
	
	/**补考名册导出*/
	function exportBkInfo(){
		window.parent.$('#maskFont').html('文件生成中，请稍后...');
		window.parent.$('#divPageMask').show();
		
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Cjcx',
			data : 'active=bkmdExport&XNXQ=' + iKeyCode + '&NJDM=' + $('#export_nj').combobox('getValues').toString() + 
				'&ZYMC=' + $('#export_zy').combobox('getValues').toString() + '&BJMC=' + $('#export_bj').combobox('getValues').toString() + 
				'&zbwdfFlag=' + $('#export_zbwdf').combobox('getValue'),
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
		if(id == 'queXnxqList'){
			loadXnxqGrid();
		}
		
		//查询补考信息列表
		if(id == 'queBkInfoList'){
			loadBkInfoGrid();
		}
		
		if(id == 'export'){
			exportBkInfo();
		}
	}
</script>
</html>