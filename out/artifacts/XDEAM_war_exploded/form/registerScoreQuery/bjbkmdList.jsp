<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="java.util.Vector"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.src.develop.store.user.*"%>
<%
	/**
		创建人：zouyu
		Create date: 2017.05.04
		功能说明:整班学生成绩查询
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
	<title>班级信息列表</title>
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
		#File1{
			height:26px;
			position:absolute;
			z-index:99;
			top:0;
			left:-170;
			opacity: 0; 
			filter: "alpha(opacity=0)"; 
			filter: alpha(opacity=0); 
			-moz-opacity: 0;
			cursor:pointer;
		}
		
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
		.maskStyle2{
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
			 width:250px;
			 height:100%;
			 position:absolute;
			 top:50%;
			 left:50%;
			 margin-top:-10px;
			 margin-left:-100px;
		}
		#maskFont2{
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
<body  class="easyui-layout">
	<div id="north" region="north" title="班级信息" style="height:115px; overflow:hidden;" >
		<table>
			<tr>
				<td><a href="#" id="export" class="easyui-linkbutton" plain="true" iconcls="icon-submit" onClick="doToolbar(this.id);">批量导出</a></td>
			</tr>
		</table>
		<table id="ee" singleselect="true" width="100%" class="tablestyle">
			<tr>
				<td style="width:130px;" class="titlestyle">年级名称</td>
				<td>
					<select name="NJDM_CX" id="NJDM_CX" class="easyui-combobox" style="width:180px;">
					</select>
				</td>
				<td style="width:130px;" class="titlestyle">所属系部</td>
				<td>
					<select name="XBDM_CX" id="XBDM_CX" class="easyui-combobox" style="width:180px;">
					</select>
				</td>
				<td style="width:130px;" class="titlestyle">所属专业</td>
				<td>
					<select name="SSZY_CX" id="SSZY_CX" class="easyui-combobox" style="width:180px;">
					</select>
				</td>
				<td  style="text-align:center;">
					<a href="#" onclick="doToolbar(this.id)" align="center" id="query" class="easyui-linkbutton" plain="true" iconcls="icon-search">查询</a>
				</td>
			</tr>
			<tr>
				<td style="width:130px;" class="titlestyle">学年</td>
				<td>
					<select name="XN_CX" id="XN_CX" class="easyui-combobox" style="width:180px;">
					</select>
				</td>
				<td style="width:130px;" class="titlestyle">班级编号</td>
				<td>
					<input name="BJBH_CX" id="BJBH_CX" class="easyui-textbox" style="width:180px;"/>
				</td>
				<td style="width:130px;" class="titlestyle">班级名称</td>
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
	
	<!-- 补考成绩信息 -->
	<div id="scoreInfoDialog" style="overflow:hidden;">
	 <div id="divPageMask2" class="maskStyle2">
    		<div id="maskFont2">补考信息导出中，请稍后...</div>
    	</div>
    	<div style="width:100%; height:100%;" class="easyui-layout">
			 <div  region="north" style="width:100%; height:32px;">
					<table id="ee" singleselect="true" width="100%"  class="tablestyle">
					<tr>
						<td  class="titlestyle">学号</td>
						<td>
							<select name="ic_xh" id="ic_xh" class="easyui-textbox" style="width:200px">
							</select>
						</td>
						<td  class="titlestyle">课程名称</td>
						<td>
							<select name="ic_kcmc" id="ic_kcmc" class="easyui-combobox" style="width:200px">
							</select>
						</td>
						<td  style="text-align:center;">
							<a href="#" onclick="doToolbar(this.id)" id="queList" class="easyui-linkbutton" plain="true" iconcls="icon-search" >查询</a>
						</td>
					</tr>
			    </table>
	    	</div>
		    <div region="center" style="width:100%;">
		   		 <table id="scoreList" style="width:100%;"></table>
		    </div>
	    </div>
	</div>
	
	<!-- 批量导出 -->
	<div id="exportExcelDialog" style="overflow:hidden;">
		<div id="divPageMask" class="maskStyle">
    		<div id="maskFont">补考信息导出中，请稍后...</div>
    	</div>
		<table id="ee" singleselect="true" width="100%" class="tablestyle" margin="0px" padding="0px">
			<tr>
				<td class="titlestyle">学年</td>
				<td>
					<select name="ic_xn" id="ic_xn" class="easyui-combobox" style="width:270px;">
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
					<a id="multipleExport" href="#" onclick="doToolbar(id);" class="easyui-linkbutton" plain="true" iconcls="icon-submit">批量导出</a>
				</td>				
			</tr>
		</table>
	</div>
	
	<!-- 下载excel -->
	<iframe id="exportIframe" src="" style="width:0; height:0;"></iframe>
</body>
<script type="text/javascript">
	var sAuth = '<%=sAuth%>';
	//sAuth = '@05@';
	var row = '';      //行数据
	var pageNum = 1;   //datagrid初始当前页数
	var pageSize = 20; //datagrid初始页内行数
	var lData = '';//补考成绩
	var xh = '';//搜索学号
	var kcmc = '';//搜索的课程名
	var ic_Bjbh = '';//班级编号
	var ic_Xn='';//学年
	var ic_Xbdm='';//系部代码
	var initFlag = true;
	
	$(document).ready(function(){
		initDialog();//初始化对话框
		loadGrid();
		initData();//页面初始化加载数据
	});
	
	/**页面初始化加载数据**/
	function initData(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Bjbkmd',
			data : 'active=initData&page=' + pageNum + '&rows=' + pageSize + '&sAuth=' + sAuth,
			dataType:"json",
			success : function(data) {
				initCombobox(data[0].njdmData, data[0].xbdmData, data[0].sszyData, data[0].xnData, data[0].exportNjData, data[0].exportXbData, data[0].exportZyData);
			}
		});
	}
	
	/**加载combobox控件
		@jxxzData 下拉框数据
	**/
	function initCombobox(njdmData, xbdmData, sszyData, xnData,exportNjData,exportXbData,exportZyData){
		//年级名称下拉框
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
		
		//所属系部下拉框
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
		
		//所属专业下拉框
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
		
		 
		//学年下拉框
		$('#XN_CX').combobox({
			data:xnData,
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
		//导出学年下拉框
		$('#ic_xn').combobox({
			data:xnData,
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
				if(initFlag == false){
					loadBJCombobox();//加载班级下拉框信息
				}
			}
		});
		//导出专业代码下拉框
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
				if(initFlag == false){
					loadBJCombobox();//加载班级下拉框信息
				}
			}
		});
		//导出系部下拉框
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
				if(initFlag == false){
					loadBJCombobox();//加载班级下拉框信息
				}
			}
		});
		//导出年级下拉框子
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
				if(initFlag == false){
					loadBJCombobox();//加载班级下拉框信息
				}
			}
		});
		
		initFlag = false;
		loadBJCombobox();//加载班级下拉框信息
	}
	
	//加载班级下拉框信息请求
	function loadBJCombobox(){
	$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Bjbkmd',
			data : 'active=loadExportBjCombo&nj='+$('#ic_njdm').combobox('getValues')+
						'&xb='+$('#ic_xbdm').combobox('getValues')+'&zy='+$('#ic_zydm').combobox('getValues')+'&xn='+$('#ic_xn').combobox('getValue')+'&sAuth=' + sAuth,
			dataType:"json",
			success : function(data){
				bjData = data[0].bjData;
				loadExportBj(bjData);
			}
		});
	}
	//班级下拉框信息
	function loadExportBj(bjData){
		$('#ic_bjmc').combobox({
			data:bjData,
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
		//补考信息dialog
		$('#scoreInfoDialog').dialog({
			width: 750,//宽度设置   
			height:370,
			modal:true,
			closed: true,   
			cache: false, 
			draggable:false,//是否可移动dialog框设置
			toolbar:[{
				text:"导出",
				iconCls:'icon-submit',
				handler:function(){
					doToolbar('singleExport');
				}
			}
			
			],
			//关闭事件
			onClose:function(data){
			
				$('#scoreList').html('');
				$('#scoreList').datagrid('loadData',{total:0,rows:[]});
				xh='';//清空学号
				xkmc='';//清空课程名称
				$('#ic_kcmc').combobox('setValue','');
				$('#ic_xh').textbox('setValue','')
			}
		});
		
		//导出excel dialog框
		$('#exportExcelDialog').dialog({   
			title: '批量导出',   
			width: 400,//宽度设置   
			height: 191,//高度设置 
			modal:true,
			closed: true,   
			cache: false, 
			draggable:false,//是否可移动dialog框设置
			//读取事件
			onLoad:function(data){
			},
			//关闭事件
			onClose:function(data){
				$('#ic_xn').combobox('setValue', '');
				$('#ic_xbdm').combobox('setValue', 'all');
				$('#ic_njdm').combobox('setValue', 'all');
				$('#ic_zydm').combobox('setValue', 'all');
				$('#ic_bjmc').combobox('setValue', 'all');
				
			}
		});
	}

	/**加载 datagrid控件，读取页面信息
		@listData 列表数据
	**/
	function loadGrid(){
		//班级信息
		$('#classList').datagrid({
			url:'<%=request.getContextPath()%>/Svl_Bjbkmd',
			queryParams:{'active':'query','page':pageNum,'rows':pageSize,'sAuth':sAuth,'XN':$('#XN_CX').combobox('getValue'),
				'BJBH':encodeURI($('#BJBH_CX').textbox('getValue')),'BJMC':encodeURI($('#BJMC_CX').textbox('getValue')),
				'NJDM':$('#NJDM_CX').combobox('getValue'),'XBDM':$('#XBDM_CX').combobox('getValue'),'SSZY':$('#SSZY_CX').combobox('getValue')},
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
				{field:'学年学期名称',title:'学年学期名称',hidden:true},
				{field:'学年',title:'学年',width:fillsize(0.08),align:'center'},
				{field:'班级代码',title:'班级代码',width:fillsize(0.08),align:'center'},
				{field:'班级名称',title:'班级名称',width:fillsize(0.15),align:'center'},
				{field:'年级代码',title:'年级代码',hidden:true},
				{field:'年级名称',title:'年级名称',width:fillsize(0.08),align:'center'},
				{field:'系部代码',title:'系部代码',hidden:true},
				{field:'系部名称',title:'所属系部',width:fillsize(0.1),align:'center'},
				{field:'专业代码',title:'专业代码',hidden:true},
				{field:'专业名称',title:'所属专业',width:fillsize(0.15),align:'center'},
				{field:'col',title:'操作',width:fillsize(0.08),align:'center',formatter:function(value,rec){
					return "<input type='button' value='[查看]' onclick='initStuScoreInfo(\"" + rec.班级代码 + "\", \"" + rec.班级名称 + "\", \"" + rec.学年 + "\", \"" + rec.系部代码 + "\");' style=\"cursor:pointer;\">";
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
	
	//导出单个班级人员成绩信息==================================================================================
	function exportSingle(bjbh,xnxqdm,xb,nj,zy,xnxqmc,xn){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Bjbkmd',
			data : 'active=exportSingle&BJBH='+bjbh+'&XNXQBM='+xnxqdm+'&XBDM='+xb+'&NJDM='+nj+'&SSZY='+zy+'&XNXQMC='+xnxqmc+'&XN='+xn,
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
	//导出班级人员成绩信息==================================================================================
	function exportStatisticsInfo(){
		$('#divPageMask').show();
	
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Bjbkmd',
			data : 'active=exportScoreInfo&NJDM='+$('#ic_njdm').combobox('getValues')+
					'&XBDM='+$('#ic_xbdm').combobox('getValues')+
					'&SSZY='+$('#ic_zydm').combobox('getValues')+
					'&BJBH='+$('#ic_bjmc').combobox('getValues')+
					'&XN='+$('#ic_xn').combobox('getValue')+
					'&sAuth=' + sAuth,
			dataType:"json",
			success : function(data) {
				if(data[0].MSG == '文件生成成功'){
				//下载文件到本地
				$("#exportIframe").attr("src", '<%=request.getContextPath()%>/form/registerScoreManage/download.jsp?filePath=' + encodeURIComponent(data[0].filePath));
					$('#exportExcelDialog').dialog('close');
					}else{
					alertMsg(data[0].MSG);
				}
				$('#divPageMask').hide();
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
		//补考名单查询
		if(id == 'queList'){
			/* xh=$('#ic_xh').textbox('getValue');
			kcmc=$('#ic_kcmc').combobox('getValue');  */
			loadBk();//补考信息
		}
		//返回
		if(id=='back'){
			$('#scoreInfoDialog').dialog('close');
		}
		//单个补考学成信息导出
		if(id == 'singleExport'){
			$('#divPageMask2').show();
    		exportSingle(row.班级代码,row.学年学期编码,row.系部代码,row.年级代码,row.专业代码,row.学年学期名称,row.学年);
		}
		
		//批量导出按钮
		if(id == 'export'){
			$('#exportExcelDialog').dialog('open');
		}
		//批量导出
		if(id == 'multipleExport'){
			if($('#ic_xn').combobox('getValue') == ''){
				alertMsg("请选择学年");
				return;
			}
			
			if($('#ic_bjmc').combobox('getValue')==undefined || $('#ic_bjmc').combobox('getValue')==''){
				alertMsg("没有可导出的补考名单信息");
				return;
			}
			
			exportStatisticsInfo();
		}
	}
	
	//加载学科拉下框
	function loadKc(kcData){
		$('#ic_kcmc').combobox({
			data:kcData,
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
	}
	
	/**初始化补考学生成绩信息页面数据*/
	function initStuScoreInfo(bjbh, bjmc,xn,xbdm){
		$('#scoreInfoDialog').dialog('setTitle', bjmc+' '+xn+'学年 学生补考成绩信息');
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Bjbkmd',
			data : 'active=loadKcCombo&sAuth=' + sAuth +'&XBDM='+xbdm+'&BJBH=' + bjbh+'&XN='+xn,
			dataType:"json",
			success : function(data){
				loadKc(data[0].kcData);
			}
		});
		ic_Bjbh=bjbh;
		ic_Xn=xn;
		ic_Xbdm=xbdm;
		loadBk(); 
		$('#scoreInfoDialog').dialog('open'); 
	}
	
	/**初始化补考学生成绩信息页面数据请求*/
	function loadBk(){
	  $.ajax({ 
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Bjbkmd',
			data : 'active=initStuScoreInfo&sAuth=' + sAuth + '&BJBH=' + ic_Bjbh+'&XN='+ic_Xn+'&XH=' + $('#ic_xh').textbox('getValue')+'&KCMC='+$('#ic_kcmc').combobox('getValue')+'&XBDM=' + ic_Xbdm,
			dataType:"json",
			success : function(data) {
				$('#scoreList').datagrid({
					data:data[0].llData,
					nowrap: false,
					fit:true, //自适应父节点宽度和高度
					showFooter:true,
					striped:true,
					singleSelect:true,
					rownumbers:true,
					fitColumns: true,
					columns:[[
						{field:'XJH',title:'学籍号',width:fillsize(0.2),align:'center'},
						{field:'XM',title:'姓名',width:fillsize(0.15),align:'center'},
						{field:'XK',title:'课程名称',width:fillsize(0.5),align:'center'},
						{field:'CJ',title:'学年总评',width:fillsize(0.15),align:'center'}
			 		 ]],
					//双击某行时触发
					onDblClickRow:function(rowIndex,rowData){},
					//读取datagrid之前加载
					onBeforeLoad:function(){},
					//单击某行时触发
					onClickRow:function(rowIndex, rowData){},
					//加载成功后触发
					onLoadSuccess: function(data){
					}	
				});
			}
		}); 
	}	
</script>
</html>