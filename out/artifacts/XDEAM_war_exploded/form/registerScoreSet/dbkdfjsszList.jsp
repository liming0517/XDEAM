<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="java.util.Vector"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.src.develop.store.user.*"%>
<%
	/**
		创建人：yeq
		Create date: 2016.08.03
		功能说明：用于设置补考登分教师信息
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
	<title>补考课程登分信息</title>
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
	<div id="north" region="north" title="大补考课程登分信息" style="height:59px;">
		<table id="ee" singleselect="true" width="100%" class="tablestyle">
			<tr>
				<td style="width:150px;" class="titlestyle">大补考学年</td>
				<td>
					<select name="DBKXN_CX" id="DBKXN_CX" class="easyui-combobox" style="width:150px;">
					</select>
				</td>
				<td style="width:150px;" class="titlestyle zycx">专业名称</td>
				<td>
					<input name="ZYMC_CX" id="ZYMC_CX" class="easyui-textbox" style="width:150px;"/>
				</td>
				<td style="width:150px;" class="titlestyle zycx">课程名称</td>
				<td>
					<input name="KCMC_CX" id="KCMC_CX" class="easyui-textbox" style="width:150px;"/>
				</td>
				<td style="width:150px;" class="titlestyle zycx">课程类型</td>
				<td>
					<select name="KCLX_CX" id="KCLX_CX" class="easyui-combobox" panelHeight="auto" style="width:150px;" editable="false">
						<option value="">请选择</option>
						<option value="1">普通课程</option>
						<option value="2">添加课程</option>
						<option value="3">选修课程</option>
						<option value="4">分层课程</option>
					</select>
				</td>
				<td style="width:150px;" align="center">
					<a href="#" onclick="doToolbar(this.id)" id="queList" class="easyui-linkbutton" plain="true" iconcls="icon-search">查询</a>
				</td>
			</tr>
	    </table>
	</div>
	<div region="center">
		<table id="courseList" style="width:100%;"></table>
		<form id="form1" method="post">
			<input type="hidden" id="active" name="active"/>
			<input type="hidden" id="CD_ID" name="CD_ID"/>
			<input type="hidden" id="CD_DFJSBH" name="CD_DFJSBH"/>
			<input type="hidden" id="CD_DFJSXM" name="CD_DFJSXM"/>
		</form>
	</div>
	
	<!-- 登分教师列表 -->
	<div id="teaListDialog">
		<div class="easyui-layout" style="width:100%; height:100%;">
			<div region="north" style="height:64px;">
				<table>
					<tr>
						<td><a href="#" id="saveTea" class="easyui-linkbutton" plain="true" iconcls="icon-save" onClick="doToolbar(this.id);">保存</a></td>
					</tr>
				</table>
				<table id="ee" width="100%" class="tablestyle">
					<tr>
						<td width="80px" class="titlestyle">教师工号</td>
						<td width="135px">
							<input style='width:100%;' class="easyui-textbox" id='ic_teaCode' name='ic_teaCode'/>
						</td>
						<td width="80px" class="titlestyle">教师姓名</td>
						<td  width="135px">
							<input style='width:100%;' class="easyui-textbox" id='ic_teaName' name='ic_teaName'/>
						</td>	
						<td style="width:80px;" align="center">
							<a href="#" onclick="doToolbar(this.id)" id="queTeaList" class="easyui-linkbutton" plain="true" iconcls="icon-search">查询</a>
						</td>				
					</tr>
			    </table>
			</div>
			<div region="center">
				<table id="teaList" style="width:100%;"></table>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
	var sAuth = '<%=sAuth%>';
	var tempIndex = '';//行号
	var curSelCode = new Array();
	var curSelName = new Array();
	var isLoad = true;//判断datagrid是否处于加载状态
	var curXn = '';
	
	$(document).ready(function(){
		initDialog();//初始化对话框
		initComboData();//页面初始化加载数据
	});
	
	/**页面初始化加载数据**/
	function initComboData(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Dbkdfjssz',
			data : 'active=initComboData&sAuth=' + sAuth,
			dataType:"json",
			success : function(data) {
				curXn = data[0].curXn;
				initCombobox(data[0].xnData);
			}
		});
	}
	
	/**加载 dialog控件**/
	function initDialog(){
		$('#teaListDialog').dialog({   
			title: '登分教师列表',   
			width: 700,//宽度设置   
			height: 400,//高度设置 
			modal:true,
			closed: true,   
			cache: false, 
			draggable:true,//是否可移动dialog框设置
			//读取事件
			onLoad:function(data){
			},
			//关闭事件
			onClose:function(data){
				$('#ic_teaCode').textbox('setValue', '');
				$('#ic_teaName').textbox('setValue', '');
				$('#teaList').datagrid('loadData',{total:0,rows:[]});
			}
		});
	}
	
	/**加载combobox控件
		@xnxqData 学年学期下拉框数据
	**/
	function initCombobox(xnxqData){
		$('#DBKXN_CX').combobox({
			data:xnxqData,
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			panelHeight:'140', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data != ''){
					//初始化combobox时赋值
					$(this).combobox('setValue', curXn);
				}
				loadCourseGrid();
			},
			//下拉列表值改变事件
			onChange:function(data){}
		});
	}
	
	/**加载课程列表datagrid控件，读取页面信息**/
	function loadCourseGrid(){
		$('#courseList').datagrid({
			url: '<%=request.getContextPath()%>/Svl_Dbkdfjssz',
			queryParams:{'active':'queCourseList','sAuth':sAuth,'DBKXN_CX':$('#DBKXN_CX').combobox('getValue'),
				'ZYMC_CX':encodeURI($('#ZYMC_CX').textbox('getValue')),'KCMC_CX':encodeURI($('#KCMC_CX').textbox('getValue')),
				'KCLX_CX':$('#KCLX_CX').combobox('getValue')},
			title:'课程登分信息列表',
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
				{field:'编号',hidden:true},
				{field:'大补考学年',title:'大补考学年',width:fillsize(0.1),align:'center'},
				{field:'专业名称',title:'专业名称',width:fillsize(0.15),align:'center'},
				{field:'课程名称',title:'课程名称',width:fillsize(0.25),align:'center'},
				{field:'课程类型',title:'课程类型',width:fillsize(0.1),align:'center'},
				{field:'登分教师编号',hidden:true},
				{field:'登分教师姓名',hidden:true},
				{field:'col4',title:'操作',width:fillsize(0.1),align:'center',
						formatter:function(value,rec){
							return "<input type='button' value='[登分教师]' onclick='openDfjsList(\"" + rec.编号 + "\",\"" + rec.登分教师编号 + "\",\"" + rec.登分教师姓名 + "\");' style=\"cursor:pointer;\">";
				}}
			]],
			//双击某行时触发
			onDblClickRow:function(rowIndex,rowData){},
			//读取datagrid之前加载
			onBeforeLoad:function(){},
			//单击某行时触发
			onClickRow:function(rowIndex,rowData){
				tempIndex = rowIndex;
			},
			//加载成功后触发
			onLoadSuccess: function(data){
				tempIndex = '';
			}
		});
	};
	
	/**打开登分教师列表**/
	function openDfjsList(code, teaCode, teaName){
		if(teaCode != ''){
			curSelCode = teaCode.split(',');
			curSelName = teaName.split(',');
		}else{
			curSelCode.length = 0;
			curSelName.length = 0;
		}
		$('#saveTea').linkbutton('disable');
		$('#queTeaList').linkbutton('disable');
		$('#CD_ID').val(code);
		
		loadTeaListData();
		$('#teaListDialog').dialog('open');
	}
	
	/**工具栏按钮调用方法，传入按钮的id
		@id 当前按钮点击事件
	**/
	function doToolbar(id){
		//查询课程列表
		if(id == 'queList'){
			loadCourseGrid();
		}
		
		//查询登分教师列表
		if(id == 'queTeaList'){
			$('#saveTea').linkbutton('disable');
			$('#' + id).linkbutton('disable');
			loadTeaListData();
		}
		
		//保存登分教师
		if(id == 'saveTea'){
			if(curSelCode.length == 0){
				alertMsg('请至少选择一名登分教师');
				return;
			}
			$('#active').val(id);
			$('#CD_DFJSBH').val(curSelCode.toString());
			$('#CD_DFJSXM').val(curSelName.toString());
			
			$('#form1').submit();
		}
	}
	
	/**表单提交**/
	$('#form1').form({
		//定位到servlet位置的url
		url:'<%=request.getContextPath()%>/Svl_Dbkdfjssz',
		//当点击事件后触发的事件
		onSubmit: function(data){}, 
		//当点击事件并成功提交后触发的事件
		success:function(data){
			var json  =  eval("("+data+")");
			if(json[0].MSG == '保存成功'){
				showMsg(json[0].MSG);
				
				$('#courseList').datagrid('updateRow', {
					index:tempIndex,
					row: {
						登分教师编号:$('#CD_DFJSBH').val(),
						登分教师姓名:$('#CD_DFJSXM').val()
					}
				});
				$('#teaListDialog').dialog('close');
			}else{
				alertMsg(json[0].MSG);
			}
		}
	});
	
	/**读取教师datagrid数据**/
	function loadTeaListData(){
		isLoad = true;
		
		$('#teaList').datagrid({
			url :'<%=request.getContextPath()%>/Svl_Dbkdfjssz',
			queryParams: {'active':'queTeaList','DFJSBH_CX':encodeURI($('#ic_teaCode').textbox('getValue')),
				'DFJSMC_CX':encodeURI($('#ic_teaName').textbox('getValue')),'CD_DFJSBH':curSelCode.toString()},
			title:'',
			width:'100%',
			nowrap: false,
			fit:true, //自适应父节点宽度和高度
			showFooter:true,
			striped:true,
			pagination:true,
			pageNumber:1,
			pageSize:20,
			singleSelect:false,
			rownumbers:true,
			fitColumns: true,
			columns:[[
				//field为读取数据的数据名，title为显示的数据名，width宽度设置，align数字在表格中显示的位置
				{field:'ck',checkbox:true},
				{field:'工号',title:'工号',width:fillsize(0.45),align:'center'},
				{field:'姓名',title:'姓名',width:fillsize(0.45),align:'center'}
			]],
			onSelect:function(rowIndex,rowData){
				if(isLoad == false){
					curSelCode.push(rowData.工号);
					curSelName.push(rowData.姓名);
				}
			},
			onUnselect:function(rowIndex,rowData){
				$.each(curSelCode, function(key,value){
					if(value == rowData.工号){
						curSelCode.splice(key, 1);
						curSelName.splice(key, 1);
					}
				});
			},
			//加载成功后触发
			onLoadSuccess: function(data){
				$(".datagrid-header-check").html('&nbsp;');//隐藏全选
				//勾选已选授课教师
				if(data){
					$.each(data.rows, function(rowIndex, rowData){
						for(var i=0; i<curSelCode.length; i++){
							if(curSelCode[i] == rowData.工号){
								$('#teaList').datagrid('selectRow', rowIndex);
								break;
							}
						}
					});
				}
				isLoad = false;
				$('#saveTea').linkbutton('enable');
				$('#queTeaList').linkbutton('enable');
			}
		});
	}
</script>
</html>