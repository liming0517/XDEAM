<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="java.util.Vector"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.src.develop.store.user.*"%>
<%
	/**
		创建人：yeq
		Create date: 2017.04.13
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
	<div id="north" region="north" title="班级信息" style="height:84px; overflow:hidden;" >
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
				<td style="width:150px;" class="titlestyle">班级编号</td>
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
		<table id="loadMask" style="width:100%; height:100%; text-align:center; display:none;">
			<tr><td><img src="<%=request.getContextPath()%>/images/loading_3.gif"></td></tr>
		</table>
		<div style="width:100%; height:100%;" class="easyui-layout">
			<div region="north" align="left" style="width:100%; height:80px; overflow:hidden;">
				<table id="ee" width="100%" class="tablestyle" singleselect="true">
					<tr>
						<td class="titlestyle" style="width:85px;">纳入总分学科</td>
						<td colspan="3">
							<table style="width:100%; height:75px; border:0;" cellpadding="0" cellspacing="0">
								<tr>
									<td style="width:60px; border-top:0; border-left:0; border-bottom:0; text-align:center;">
										<input class="examSub" type="checkbox" id="selAllExamSub" onclick="doToolbar(this.id);" style="cursor:pointer;"><label for="selAllExamSub" style="padding-left:3px; cursor:pointer;">全选</label>
										<input id="selOtherExamSub" style="width:50px; margin-top:10px; cursor:pointer;" type="button" value="反选" onclick="doToolbar(this.id);"/>
									</td>
									<td id="examSubList" style="border:0;">&nbsp;</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
		    </div>
		    <div region="center">
		    	<table id="scoreList" style="width:100%;"></table>
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
	var curExamType = '';
	var curXnxqbm = '';
	var curXnxqmc = '';
	var curClassCode = '';
	var curClassName = '';
	var curCountSub = '';
	var wzcjShowArray = '';
	
	$(document).ready(function(){
		initDialog();//初始化对话框
		initData();//页面初始化加载数据
		loadGrid();
	});
	
	/**页面初始化加载数据**/
	function initData(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Zbcjcx',
			data : 'active=initData&sAuth=' + sAuth,
			dataType:"json",
			success : function(data) {
				wzcjShowArray = data[0].wzcjShowData;
				initCombobox(data[0].njdmData, data[0].xbdmData, data[0].sszyData, data[0].xnxqData);
			}
		});
	}
	
	/**加载combobox控件
		@jxxzData 下拉框数据
	**/
	function initCombobox(njdmData, xbdmData, sszyData, xnxqData){
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

		$('#NJDM').combobox({
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
		
		$('#XBDM').combobox({
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

		$('#SSZY').combobox({
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
			}],
			//关闭事件
			onClose:function(data){
				$('#scoreList').datagrid('loadData',{total:0,rows:[]});
				$('#examSubList').html('&nbsp;');
				curExamType = '';
				curXnxqbm = '';
				curXnxqmc = '';
				curClassCode = '';
				curClassName = '';
				curCountSub = '';
				$('#selAllExamSub').prop('checked', false);
			}
		});
	}

	/**加载 datagrid控件，读取页面信息
		@listData 列表数据
	**/
	function loadGrid(){
		$('#classList').datagrid({
			url: '<%=request.getContextPath()%>/Svl_Zbcjcx',
			queryParams:{'active':'query','sAuth':sAuth,'XNXQBM':$('#XNXQBM_CX').combobox('getValue'),
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
				//field为读取数据的数据名，title为显示的数据名，width宽度设置，align数字在表格中显示的位置
				{field:'学年学期编码',title:'学年学期编码',hidden:true},
				{field:'学年学期名称',title:'学年学期名称',width:fillsize(0.1),align:'center'},
				{field:'班级代码',title:'班级代码',width:fillsize(0.1),align:'center'},
				{field:'班级名称',title:'班级名称',width:fillsize(0.2),align:'center'},
				{field:'年级代码',title:'年级代码',hidden:true},
				{field:'年级名称',title:'年级名称',width:fillsize(0.1),align:'center'},
				{field:'系部代码',title:'系部代码',hidden:true},
				{field:'系部名称',title:'所属系部',width:fillsize(0.1),align:'center'},
				{field:'专业代码',title:'专业代码',hidden:true},
				{field:'专业名称',title:'所属专业',width:fillsize(0.2),align:'center'},
				{field:'col',title:'操作',width:fillsize(0.2),align:'center',formatter:function(value,rec){
					return "<input type='button' value='[期中考试]' onclick='initStuScoreInfo(\"qz\", \"" + rec.学年学期编码 +"\", \"" + rec.学年学期名称 +"\", \"" + rec.班级代码 + "\", \"" + rec.班级名称 + "\")' style='cursor:pointer;'>" + 
						"<input type='button' value='[期末考试]' onclick='initStuScoreInfo(\"qm\", \"" + rec.学年学期编码 +"\", \"" + rec.学年学期名称 +"\", \"" + rec.班级代码 + "\", \"" + rec.班级名称 + "\")' style='margin-left:5px; cursor:pointer;'>";
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
	
	/**工具栏按钮调用方法，传入按钮的id
		@id 当前按钮点击事件
	**/
	function doToolbar(id){
		//查询
		if(id == 'query'){
			loadGrid();
		}
		
		//全选考试学科
		if(id == "selAllExamSub"){
			var isSel = $('#' + id).is(':checked');
			$("input[name='examSub']").prop('checked', isSel);
			this.doToolbar('selExamSub');
		}
		
		//反选
		if(id == "selOtherExamSub"){
			var examSubArray = new Array();
			$("input[name='examSub']").each(function(){
				$(this).prop('checked', !$(this).prop('checked'));
				
				if($(this).prop('checked')){
					examSubArray.push($(this).val());
				}
			});
			
			if(examSubArray.length == $('input[name="examSub"]').length)
				$('#selAllExamSub').prop('checked', true);
			else
				$('#selAllExamSub').prop('checked', false);
		}
		
		//选中考试学科
		if(id == 'selExamSub'){
			var examSubArray = new Array();
			$('input[name="examSub"]:checked').each(function(){ 
				examSubArray.push($(this).val()); 
			});
			if(examSubArray.length == $('input[name="examSub"]').length)
				$('#selAllExamSub').prop('checked', true);
			else
				$('#selAllExamSub').prop('checked', false);
		}
		
		//计算总分
		if(id == 'count'){
			var examSubArray = new Array();
			$('input[name="examSub"]:checked').each(function(){ 
				examSubArray.push($(this).val()); 
			});
			curCountSub = examSubArray.toString();
			loadStuScoreInfo();
		}
		
		//导出
		if(id == 'export'){
			exportScoreInfo();
		}
	}
	
	/**初始化学生成绩信息页面数据*/
	function initStuScoreInfo(type, xnxqbm, xnxqmc, bjbh, bjmc){
		$('#loadMask').show();
		curExamType = type;
		curXnxqbm = xnxqbm;
		curXnxqmc = xnxqmc;
		curClassCode = bjbh;
		curClassName = bjmc;
	
		$('#scoreInfoDialog').dialog('setTitle', bjmc+' '+xnxqmc+' 学生' + (type=='qz'?'期中':'期末') + '考试成绩信息');
		
		$.ajax({ 
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Zbcjcx',
			data : 'active=initStuScoreInfo&sAuth=' + sAuth + '&EXAMTYPE=' + type + '&XNXQBM=' + xnxqbm + '&BJBH=' + bjbh,
			dataType:"json",
			success : function(data) {
				createExamSubInfo(data[0].examSubData);
				//默认全选
				$("#selAllExamSub").prop('checked', true);
				$("input[name='examSub']").prop('checked', true);
				var examSubArray = new Array();
				$('input[name="examSub"]:checked').each(function(){ 
					examSubArray.push($(this).val()); 
				});
				curCountSub = examSubArray.toString();
				
				createScoreList(data[0].colData, data[0].listData);
			}
		});
		
		$('#scoreInfoDialog').dialog('open');
	}
	
	/**读取学生成绩列表*/
	function loadStuScoreInfo(){
		$('#loadMask').show();
		$.ajax({ 
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Zbcjcx',
			data : 'active=loadStuScoreInfo&sAuth=' + sAuth + '&EXAMTYPE=' + curExamType + '&XNXQBM=' + curXnxqbm + '&BJBH=' + curClassCode + "&countSub=" + curCountSub,
			dataType:"json",
			success : function(data) {
				createScoreList(data[0].colData, data[0].listData);
			}
		});
	}
	
	/**生成学生成绩列表*/
	function createScoreList(colData, listData){
		var cols = new Array();
		for(var i=0; i<colData.length; i++){
			cols.push({field:colData[i].colField,title:colData[i].colName,width:'100px',align:'center',
				formatter:function(value,rec){
					return parseScoreShow(value);
				}
			});
		}
	
		$('#scoreList').datagrid({
			data:listData,
			//title:'综合分析信息列表',
			nowrap: false,
			fit:true, //自适应父节点宽度和高度
			showFooter:true,
			striped:true,
			singleSelect:true,
			rownumbers:true,
			toolbar:[{
		        text: '计算总分',
		        iconCls: 'icon-collection_edit',
		        handler: function(){
			        doToolbar('count');
		        }
		    },{
		        text: '导出',
		        iconCls: 'icon-submit',
		        handler: function(){
			        doToolbar('export');
		        }
		    }],
			frozenColumns:[[
				{field:'XH',title:'学号',width:60,align:'center'},
				{field:'XM',title:'姓名',width:100,align:'center'},
				{field:'XJH',title:'学籍号',width:150,align:'center'},
				{field:'ZF',title:'总分',width:60,align:'center'},
				{field:'RANK',title:'班级排名',width:60,align:'center'},
				{field:'bjgNum',title:'不及格科目数',width:90,align:'center'},
				{field:'bjgContent',title:'不及格科目详情',width:150,align:'center'}
			]],
			columns:[cols]
		});
		
		$('#loadMask').hide();
	}
	
	/**生成考试学科信息*/
	function createExamSubInfo(examSubInfo){
		var tempContent = '';
		var content_1 = '';
		var content_2 = '';
		var content_3 = '';
		var content_4 = '';
		var content_5 = '';
		var content_6 = '';
		
		for(var i=0; i<examSubInfo.length; i++){
			for(var j=0; j<1; j++){
				tempContent = '<span style="width:100%; margin-top:3px; margin-bottom:3px;"><input class="examSub" id="' + examSubInfo[i].code + '" name="examSub" type="checkbox" value="' + examSubInfo[i].code + '" style="cursor:pointer;" onclick="doToolbar(\'selExamSub\');"/>' + 
					'<label for="' + examSubInfo[i].code + '" style="padding-left:3px; cursor:pointer;">'+ examSubInfo[i].subName  + '</label></span>';
			
				if(i%6 == 0)
					content_1 += tempContent;
				else if(i%6 == 1)
					content_2 += tempContent;
				else if(i%6 == 2)
					content_3 += tempContent;
				else if(i%6 == 3)
					content_4 += tempContent;
				else if(i%6 == 4)
					content_5 += tempContent;
				else if(i%6 == 5)
					content_6 += tempContent;
			}
		}
		
		if(content_1 == '')	content_1 = '&nbsp;';
		if(content_2 == '')	content_2 = '&nbsp;';
		if(content_3 == '')	content_3 = '&nbsp;';
		if(content_4 == '')	content_4 = '&nbsp;';
		if(content_5 == '')	content_5 = '&nbsp;';
		if(content_6 == '')	content_6 = '&nbsp;';
			
		if(examSubInfo.length > 18){
			tempContent = '<div style="width:100%; height:100%; margin-left:20px; overflow:auto;">';
		}else{
			tempContent = '<div style="width:100%;  margin-left:20px; overflow:auto;">';
		}
		tempContent += '<table style="width:100%; border:0;" cellpadding="0" cellspacing="0"><tr>' +
			'<td style="border:0; width:16.5%; vertical-align:top;">' + content_1 + '</td>' + 
			'<td style="border:0; width:16.5%; vertical-align:top;">' + content_2 + '</td>' + 
			'<td style="border:0; width:16.5%; vertical-align:top;">' + content_3 + '</td>' + 
			'<td style="border:0; width:16.5%; vertical-align:top;">' + content_4 + '</td>' + 
			'<td style="border:0; width:16.5%; vertical-align:top;">' + content_5 + '</td>' + 
			'<td style="border:0; width:17.5%; vertical-align:top;">' + content_6 + '</td>' + 
			'</tr></table></div>';
		$('#examSubList').html(tempContent);
	}
	
	function parseScoreShow(scoreStr){
		var result = scoreStr;
		
		for(var i=0; i<wzcjShowArray.length; i++){
			if(scoreStr == wzcjShowArray[i].id){
				result = wzcjShowArray[i].text;
				break;
			}
		}
		
		if(scoreStr!='' && ((scoreStr>=0 && scoreStr<60) || scoreStr==-2 || scoreStr==-3 || scoreStr==-4 || scoreStr==-10 || scoreStr==-12)){
			result = '<font color="red">'+result+'</font>';
		}
		
		return result;
	}
	
	/**导出*/
	function exportScoreInfo(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Zbcjcx',
			data : 'active=exportScoreInfo&sAuth=' + sAuth + '&EXAMTYPE=' + curExamType + 
				'&XNXQBM=' + curXnxqbm + '&XNXQMC=' + encodeURI(curXnxqmc) + 
				'&BJBH=' + curClassCode + '&BJMC=' + encodeURI(curClassName) + '&countSub=' + curCountSub,
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