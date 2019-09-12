<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="java.util.Vector"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.src.develop.store.user.*"%>
<%
	/**
		创建人：yeq
		Create date: 2017.03.28
		功能说明：综合分析
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
	<title>综合分析</title>
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
	<div id="north" region="north" title="班级信息" style="height:91px;">
		<table>
			<tr>
				<td><a href="#" id="export" class="easyui-linkbutton" plain="true" iconcls="icon-submit" onClick="doToolbar(this.id);">批量导出</a></td>
			</tr>
		</table>
		<table id="ee" singleselect="true" width="100%" class="tablestyle">
			<tr>
				<td style="width:100px;" class="titlestyle">学年学期名称</td>
				<td>
					<select name="XNXQ_CX" id="XNXQ_CX" class="easyui-combobox" style="width:180px;">
					</select>
				</td>
				<td style="width:100px;" class="titlestyle">班级名称</td>
				<td>
					<input name="BJMC_CX" id="BJMC_CX" class="easyui-textbox" style="width:180px;"/>
				</td>
				<td class="titlestyle">班级类型</td>
				<td>
					<select name="BJLX_CX" id="BJLX_CX" class="easyui-combobox" style="width:180px;">
					</select>
				</td>
				<td style="width:150px; text-align:center;">
					<a href="#" onclick="doToolbar(this.id)" id="queList" class="easyui-linkbutton" plain="true" iconcls="icon-search">查询</a>
				</td>
			</tr>
	    </table>
	</div>
	<div region="center">
		<table id="semClassList" style="width:100%;"></table>
	</div>
	
	<!-- 统计信息 -->
	<div id="countDialog">
		<div style="width:100%; height:100%;" class="easyui-layout">
			<div id="north" region="north" style="height:34px; overflow:hidden;">
				<table>
					<tr>
						<td><a href="#" id="singleExport" class="easyui-linkbutton" plain="true" iconcls="icon-submit" onClick="doToolbar(this.id);">导出</a></td>
					</tr>
				</table>
			</div>
			<div region="center">
				<table id="countList"></table>
			</div>
		</div>
	</div>
	
	<!-- 批量导出 -->
	<div id="exportExcelDialog" style="overflow:hidden;">
		<table id="ee" singleselect="true" width="100%" class="tablestyle" margin="0px" padding="0px">
			<tr>
				<td class="titlestyle">学年学期</td>
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
				<td class="titlestyle">班级类型</td>
				<td>
					<select name="ic_bjlx" id="ic_bjlx" class="easyui-combobox" style="width:270px;">
					</select>
				</td>
			</tr>
			<tr>
				<td class="titlestyle">成绩类型</td>
				<td>
					<select name="ic_cjlx" id="ic_cjlx" class="easyui-combobox" style="width:270px;">
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
	
   	<!-- 下载excel -->
	<iframe id="exportIframe" src="" style="width:0; height:0;"></iframe>
</body>
<script type="text/javascript">
	var sAuth = '<%=sAuth%>';
	var bjlxData = [{"id":"","text":"请选择"},{"id":"xzb","text":"行政班"},{"id":"jxb","text":"教学班"},{"id":"xxb","text":"选修班"},{"id":"fcb","text":"分层班"}];
	var exportBjlxData = [{"id":"all","text":"全部"},{"id":"xzb","text":"行政班"},{"id":"jxb","text":"教学班"},{"id":"xxb","text":"选修班"},{"id":"fcb","text":"分层班"}];
	//var cjlxData = [{"id":"","text":"请选择"},{"id":"ps","text":"平时"},{"id":"qz","text":"期中"},{"id":"xxb","text":"期末"},{"id":"fcb","text":"总评"}];
	var cjlxData = [{"id":"","text":"请选择"},{"id":"qz","text":"期中"},{"id":"qm","text":"期末"}];
	var curExamType = '';
	var curXnxq = '';
	var exportXnxq = '';
	var curClassCode = '';
	var curClassName = '';
	
	$(document).ready(function(){
		loadSemClassGrid();
		initDialog();//初始化对话框
		initData();
	});
	
	/**加载 dialog控件**/
	function initDialog(){
		$('#countDialog').dialog({   
			width: 850,//宽度设置   
			height: 350,//高度设置 
			modal:true,
			closed: true,   
			cache: false, 
			draggable:true,//是否可移动dialog框设置
			//关闭事件
			onClose:function(data){
				$('#countList').datagrid('loadData',{total:0,rows:[]});
			}
		});
		
		//导出excel dialog框
		$('#exportExcelDialog').dialog({   
			title: '批量导出',   
			width: 400,//宽度设置   
			height: 216,//高度设置 
			modal:true,
			closed: true,   
			cache: false, 
			draggable:false,//是否可移动dialog框设置
			//读取事件
			onLoad:function(data){
			},
			//关闭事件
			onClose:function(data){
				$('#ic_xnxq').combobox('setValue', curXnxq);
				$('#ic_xbdm').combobox('setValue', 'all');
				$('#ic_njdm').combobox('setValue', 'all');
				$('#ic_zydm').combobox('setValue', 'all');
				$('#ic_bjlx').combobox('setValue', 'all');
				$('#ic_cjlx').combobox('setValue', '');
			}
		});
	}
	
	function initData(){
		$.ajax({
			type:"POST",
			url:'<%=request.getContextPath()%>/Svl_Zhfx',
			data:'active=initData',
			dataType:"json",
			success:function(data) {
				curXnxq = data[0].curXnxq;
				initCombobox(data[0].xnxqData, data[0].exportXbdmData, data[0].exportNjdmData, data[0].exportZydmData);
			}
		});
	}
	
	/**加载combobox控件**/
	function initCombobox(xnxqData, exportXbdmData, exportNjdmData, exportZydmData){
		$('#XNXQ_CX').combobox({
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
		
		$('#BJLX_CX').combobox({
			data:bjlxData,
			valueField:'id',
			textField:'text',
			editable:false,
			panelHeight:'auto', //combobox高度
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
					$(this).combobox('setValue', curXnxq);
				}
			}
		});
		
		$('#ic_xbdm').combobox({
			data:exportXbdmData,
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			panelHeight:'auto', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data != ''){
					//初始化combobox时赋值
					$(this).combobox('setValue', 'all');
				}
			}
		});
		
		$('#ic_njdm').combobox({
			data:exportNjdmData,
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			panelHeight:'140', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data != ''){
					//初始化combobox时赋值
					$(this).combobox('setValue', 'all');
				}
			}
		});
		
		$('#ic_zydm').combobox({
			data:exportZydmData,
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			panelHeight:'140', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data != ''){
					//初始化combobox时赋值
					$(this).combobox('setValue', 'all');
				}
			}
		});
		
		$('#ic_bjlx').combobox({
			data:exportBjlxData,
			valueField:'id',
			textField:'text',
			editable:false,
			panelHeight:'auto', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data != ''){
					//初始化combobox时赋值
					$(this).combobox('setValue', 'all');
				}
			}
		});
		
		$('#ic_cjlx').combobox({
			data:cjlxData,
			valueField:'id',
			textField:'text',
			editable:false,
			panelHeight:'auto', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data != ''){
					//初始化combobox时赋值
					$(this).combobox('setValue', '');
				}
			}
		});
	}
	
	/**加载班级列表datagrid控件，读取页面信息**/
	function loadSemClassGrid(){
		$('#semClassList').datagrid({
			url: '<%=request.getContextPath()%>/Svl_Zhfx',
			queryParams:{'active':'queSemClassList','sAuth':sAuth,'XNXQBM':$('#XNXQ_CX').combobox('getValue'),
				'CLASSNAME':encodeURI($('#BJMC_CX').textbox('getValue')),'CLASSTYPE':$('#BJLX_CX').combobox('getValue')},
			title:'学期班级信息列表',
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
				{field:'学年学期名称',title:'学年学期名称',width:fillsize(0.25),align:'center'},
				{field:'班级名称',title:'班级名称',width:fillsize(0.3),align:'center'},
				{field:'班级类型',title:'班级类型',width:fillsize(0.25),align:'center'},
				{field:'col',title:'操作',width:fillsize(0.2),align:'center',formatter:function(value,rec){
					return "<input type='button' value='[期中考试]' onclick='loadStatisticsInfo(\"qz\", \"" + rec.学年学期编码 +"\", \"" + rec.学年学期名称 +"\", \"" + rec.班级代码 + "\", \"" + rec.班级名称 + "\")' style='cursor:pointer;'>" + 
							"<input type='button' value='[期末考试]' onclick='loadStatisticsInfo(\"qm\", \"" + rec.学年学期编码 +"\", \"" + rec.学年学期名称 +"\", \"" + rec.班级代码 + "\", \"" + rec.班级名称 + "\")' style='margin-left:5px; cursor:pointer;'>";
				}}
			]],
			//加载成功后触发
			onLoadSuccess: function(data){
			}
		});
	};
	
	/**读取统计信息*/
	function loadStatisticsInfo(type, xnxqbm, xnxqmc, bjbh, bjmc){
		curExamType = type;
		exportXnxq = xnxqbm;
		curClassCode = bjbh;
		curClassName = bjmc;
	
		$('#countDialog').dialog('setTitle', xnxqmc+' '+bjmc+'单科统计表');
		
		$('#countList').datagrid({
			url: '<%=request.getContextPath()%>/Svl_Zhfx',
			queryParams:{'active':'loadStatisticsInfo','sAuth':sAuth,'XNXQBM':xnxqbm,'CLASSCODE':bjbh,'EXAMTYPE':type},
			//title:'综合分析信息列表',
			nowrap: false,
			fit:true, //自适应父节点宽度和高度
			showFooter:true,
			striped:true,
			singleSelect:true,
			rownumbers:true,
			frozenColumns:[[
				{field:'班级名称',title:'班级名称',width:100,align:'center'},
				{field:'授课教师',title:'授课教师',width:100,align:'center'},
				{field:'课程名称',title:'课程名称',width:150,align:'center'},
				{field:'成绩类型',title:'成绩类型',width:60,align:'center'},
				{field:'班级人数',title:'班级人数',width:60,align:'center'}
			]],
			columns:[[
				{field:'缺考人数',title:'缺考人数',width:60,align:'center'},
				{field:'作弊人数',title:'作弊人数',width:60,align:'center'},
				{field:'免考人数',title:'免考人数',width:60,align:'center'},
				{field:'免修人数',title:'免修人数',width:60,align:'center'},
				{field:'缓考人数',title:'缓考人数',width:60,align:'center'},
				{field:'不及格人数',title:'不及格人数',width:80,align:'center'},
				{field:'班级总分',title:'班级总分',width:60,align:'center'},
				{field:'班级平均分',title:'班级平均分',width:80,align:'center'},
				{field:'最高分',title:'最高分',width:60,align:'center'},
				{field:'最低分',title:'最低分',width:60,align:'center'},
				{field:'优秀率',title:'优秀率',width:60,align:'center'},
				{field:'及格率',title:'及格率',width:60,align:'center'},
				{field:'90分以上',title:'90分以上',width:80,align:'center'},
				{field:'80-89',title:'80-89',width:80,align:'center'},
				{field:'70-79',title:'70-79',width:80,align:'center'},
				{field:'60-69',title:'60-69',width:80,align:'center'},
				{field:'60分以下',title:'60分以下',width:80,align:'center'}
			]],
			//加载成功后触发
			onLoadSuccess: function(data){
			}
		});
		
		$('#countDialog').dialog('open');
	}
	
	/**工具栏按钮调用方法，传入按钮的id
		@id 当前按钮点击事件
	**/
	function doToolbar(id){
		//查询班级列表
		if(id == 'queList'){
			loadSemClassGrid();
		}
		
		if(id == 'export'){
			$('#exportExcelDialog').dialog('open');
		}
		
		//批量导出
		if(id == 'multipleExport'){
			if($('#ic_xnxq').combobox('getValue') == ''){
				alertMsg("请先选择学年学期");
				return;
			}
		
			if($('#ic_cjlx').combobox('getValue') == ''){
				alertMsg("请先选择成绩类型");
				return;
			}
		
			curExamType = $('#ic_cjlx').combobox('getValue');
			exportXnxq = $('#ic_xnxq').combobox('getValue');
			curClassCode = '';
			curClassName = '';
			
			exportStatisticsInfo('multiple');
		}
		
		//单班导出
		if(id == 'singleExport'){
			exportStatisticsInfo('single');
		}
	}
	
	/**导出*/
	function exportStatisticsInfo(type){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Zhfx',
			data : 'active=exportStatisticsInfo&sAuth=' + sAuth + '&TYPE=' + type + '&EXAMTYPE=' + curExamType + '&XNXQBM=' + exportXnxq + 
				'&CLASSCODE=' + curClassCode + '&CLASSNAME=' + encodeURI(curClassName) + '&XBDM=' + $('#ic_xbdm').combobox('getValue') + 
				'&NJDM=' + $('#ic_njdm').combobox('getValue') + '&ZYDM=' + $('#ic_zydm').combobox('getValue') + '&CLASSTYPE=' + $('#ic_bjlx').combobox('getValue'),
			dataType:"json",
			success : function(data){
				if(data[0].MSG == '文件生成成功'){
					//下载文件到本地
					$("#exportIframe").attr("src", '<%=request.getContextPath()%>/form/registerScoreManage/download.jsp?filePath=' + encodeURIComponent(data[0].filePath));
				}else{
					alertMsg(data[0].MSG);
				}
			}
		});
	}
</script>
</html>