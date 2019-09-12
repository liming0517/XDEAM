<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="java.util.Vector"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.src.develop.store.user.*"%>
<%
	/**
		创建人：yeq
		Create date: 2016.01.08
		功能说明：用于设置专业信息
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
	<title>专业信息列表</title>
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
	<div id="north" region="north" title="专业信息" style="height:91px;" >
		<table>
			<tr>
				<td><a href="#" id="new" class="easyui-linkbutton" plain="true" iconcls="icon-new" onClick="doToolbar(this.id);" title="">新建</a></td>
				<td><a href="#" id="edit" class="easyui-linkbutton" plain="true" iconcls="icon-edit" onClick="doToolbar(this.id);" title="">编辑</a></td>
				<td><a href="#" id="del" class="easyui-linkbutton" plain="true" iconcls="icon-cancel" onClick="doToolbar(this.id);" title="">删除</a></td>
			</tr>
		</table>
		<table id="ee" singleselect="true" width="100%" class="tablestyle">
			<tr>
				<td style="width:100px;" class="titlestyle">专业代码</td>
				<td>
					<input name="ZYDM_CX" id="ZYDM_CX" class="easyui-textbox" style="width:180px;"/>
				</td>
				<td style="width:100px;" class="titlestyle">专业名称</td>
				<td>
					<input name="ZYMC_CX" id="ZYMC_CX" class="easyui-textbox" style="width:180px;"/>
				</td>
				<td style="width:100px;" class="titlestyle">专业类型</td>
				<td>
					<select id="KMLX_CX" name="KMLX_CX" class="easyui-combobox" style="width:180px;" panelHeight="auto" editable="false">
						<option value="">请选择</option>
						<option value="1">文科</option>
						<option value="2">理科</option>
					</select>
				</td>
				<td style="width:150px;" align="center">
					<a href="#" onclick="doToolbar(this.id)" id="query" class="easyui-linkbutton" plain="true" iconcls="icon-search">查询</a>
				</td>				
			</tr>
	    </table>
	</div>
	<div region="center">
		<table id="majorList" style="width:100%;"></table>
	</div>
	
	<!-- 专业信息新建编辑 -->
	<div id="majorInfo" style="overflow:hidden;">
		<form id="form1" method='post'>
			<table style="width:100%;" class="tablestyle">
				<tr>
					<td class="titlestyle">专业代码</td>
					<td>
						<input name="ZYDM" id="ZYDM" class="easyui-textbox" style="width:200px;" required="true">
					</td>
				</tr>
				<tr>
					<td class="titlestyle">专业名称</td>
					<td>
						<input name="ZYMC" id="ZYMC" class="easyui-textbox" style="width:200px;" required="true">
					</td>
				</tr>
				<tr>
					<td class="titlestyle">专业类型</td>
					<td>
						<select id="KMLX" name="KMLX" class="easyui-combobox" style="width:200px;" panelHeight="auto" editable="false">
							<option value="">请选择</option>
							<option value="1">文科1</option>
							<option value="2">理科</option>
						</select>
					</td>
				</tr>
				<tr>
					<td class="titlestyle">专业组长</td>
					<td>
						<select name="ZYZZ" id="ZYZZ" class="easyui-combobox" style="width:200px;">
						</select>
					</td>
				</tr>
				<tr>
					<td class="titlestyle">学制</td>
					<td>
						<input name="XZ" id="XZ" class="easyui-numberbox" style="width:200px;" min="0" max="10" precision="1" required="true">
					</td>
				</tr>
			</table>
			
			<input type="hidden" id="active" name="active"/>
			<input type="hidden" name="JN_JYZZZBH" id="JN_JYZZZBH">
			<input type="hidden" name="JN_JYZZZBH1" id="JN_JYZZZBH1">
		</form>
	</div>
</body>
<script type="text/javascript">
	var sAuth = '<%=sAuth%>';
	var row = '';      //行数据
	var iKeyCode = ''; //定义主键
	var pageNum = 1;   //datagrid初始当前页数
	var pageSize = 20; //datagrid初始页内行数
	var ZYDM_CX = '';//查询条件
	var ZYMC_CX = '';
	var KMLX_CX = '';
	var saveType = '';
	var skjsData='';
	var queryAuth = '<%=MyTools.getProp(request, "Base.majorClassQueAuth")%>';
	
	$(document).ready(function(){
		if(sAuth.indexOf(queryAuth) > -1){
			$('#new').linkbutton('disable');
			$('#edit').linkbutton('disable');
			$('#del').linkbutton('disable');
		}
		
		initDialog();//初始化对话框
		initData();//页面初始化加载数据
	});
	
	/**页面初始化加载数据**/
	function initData(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_MajorSet',
			data : 'active=initData&page=' + pageNum + '&rows=' + pageSize,
			dataType:"json",
			success : function(data) {
				skjsData=data[0].zyzzData;
				loadGrid(data[0].listData);
				initCombobox(data[0].zyzzData);
			}
		});
	}
	//加载专业组长信息
	function initCombobox(zyzzData){
			$('#ZYZZ').combobox({
			data:zyzzData,
			valueField : 'comboValue',
			textField : 'comboName',
			editable:true,
			panelHeight : '140',
			multiple:true,
			onSelect:function(record){
	       	if(record.comboValue == ''){
	       		$(this).combobox('setValue', '');
	       	}else{
	       		var selData = $(this).combobox('getValues');
	        	if(selData.length > 1)
	        		$(this).combobox('unselect', '');
	       	}
	       },
	       onUnselect:function(){
	       	var selData = $(this).combobox('getValues');
	       	if(selData.length < 1)
	       		$(this).combobox('select', '');
	       },
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
	
	};
	/**加载 dialog控件**/
	function initDialog(){
		$('#majorInfo').dialog({   
			width: 300,//宽度设置   
			height: 193,//高度设置 
			modal:true,
			closed: true,   
			cache: false, 
			draggable:false,//是否可移动dialog框设置
			toolbar:[{
				//保存编辑
				text:'保存',
				iconCls:'icon-save',
				handler:function(){
					//传入save值进入doToolbar方法，用于保存
					doToolbar('save');
				}
			}],
			//打开事件
			onOpen:function(data){},
			//读取事件
			onLoad:function(data){},
			//关闭事件
			onClose:function(data){
				emptyDialog();
			}
		});
	}

	/**加载 datagrid控件，读取页面信息
		@listData 列表数据
	**/
	function loadGrid(listData){
		$('#majorList').datagrid({
			//url:'semesterList.json',
			data:listData,
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
				{field:'ZYDM',title:'专业代码',width:fillsize(0.25),align:'center'},
				{field:'ZYMC',title:'专业名称',width:fillsize(0.25),align:'center'},
				{field:'KMLX',title:'专业类型',width:fillsize(0.25),align:'center',
					formatter:function(value,rowData,rowIndex){
						if(value == '1') return '文科';
						else if(value == '2') return '理科';
						else return value;
				}},
				{field:'ZYZZ',title:'专业组长',width:fillsize(0.25),align:'center',formatter:function(value, rec){
					var teaCode = value.split(',');
					var resultName = '';
					
					for(var i=0; i<teaCode.length; i++){
						for(var j=0; j<skjsData.length; j++){
							if(teaCode[i]!='' && teaCode[i]==skjsData[j].comboValue){
								resultName += skjsData[j].comboName+',';
								break;
							}
						}
					}
					
					if(resultName.length > 0)
						resultName = resultName.substring(0, resultName.length-1);
						return resultName;
					
					
				}},
				
				{field:'XZ',title:'学制',width:fillsize(0.25),align:'center'}
			]],
			//双击某行时触发
			onDblClickRow:function(rowIndex,rowData){},
			//读取datagrid之前加载
			onBeforeLoad:function(){},
			//单击某行时触发
			onClickRow:function(rowIndex,rowData){
				//主键赋值
				iKeyCode = rowData.ZYDM;
				row = rowData;
			},
			//加载成功后触发
			onLoadSuccess: function(data){
				iKeyCode = '';
			}
		});
		
		$("#majorList").datagrid("getPager").pagination({
			total:listData.total,
			afterPageText: '页&nbsp;<a href="#" onclick="goEnterPage();">Go</a>&nbsp;&nbsp;&nbsp;共 {pages} 页',
			onSelectPage:function (pageNo, pageSize_1) { 
				pageNum = pageNo;
				pageSize = pageSize_1;
				loadData();
			} 
		});
	};
	
	function goEnterPage(){
		var e = jQuery.Event("keydown");//模拟一个键盘事件
		e.keyCode = 13;//keyCode=13是回车 
		$("input.pagination-num").trigger(e);//模拟页码框按下回车 
	}
	
	/**读取datagrid数据**/
	function loadData(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_MajorSet',
			data : 'active=query&page=' + pageNum + '&rows=' + pageSize + 
				'&ZYDM_CX=' + encodeURI(ZYDM_CX) + 
				'&ZYMC_CX=' + encodeURI(ZYMC_CX) +
				'&KMLX_CX=' + KMLX_CX,
			dataType:"json",
			success : function(data) {
				loadGrid(data[0].listData);
			}
		});
	}
	
	/**工具栏按钮调用方法，传入按钮的id
		@id 当前按钮点击事件
	**/
	function doToolbar(id){
		//查询
		if(id == 'query'){
			ZYDM_CX = $('#ZYDM_CX').textbox('getValue'); 
			ZYMC_CX = $('#ZYMC_CX').textbox('getValue');
			KMLX_CX = $('#KMLX_CX').combobox('getValue');
			loadData();
		}
		
		//判断获取参数为new，执行新建操作
		if(id == 'new'){
			//打开dialog
			$('#majorInfo').dialog({   
				title: '新建'
			});
			saveType = 'new';
			$('#majorInfo').dialog('open');
		}

		//判断获取参数为edit，执行编辑操作
		if(id == 'edit'){
			if(iKeyCode == ''){
				alertMsg('请选择一行数据');
				return;
			}
			
			$('#ZYDM').textbox('readonly', true);
			//打开dialog
			$('#majorInfo').dialog({   
				title: '编辑'   
			});
			
			/*  if(row!=undefined && row!=''){
				$('#form1').form('load', row);
			}   */
			
			$('#ZYDM').textbox('setValue', row.ZYDM);
			$('#ZYMC').textbox('setValue', row.ZYMC);
			$('#KMLX').combobox('setValues', row.KMLX);
			$('#XZ').numberbox('setValue', row.XZ);
			$('#ZYZZ').combobox('setValues', row.ZYZZ);
			saveType = 'edit';
			$('#majorInfo').dialog('open');
		}
		
		if(id == 'del'){
			if(iKeyCode == ''){
				alertMsg('请选择一行数据');
				return;
			}
			ConfirmMsg('是否确定要删除当前选择的专业', 'delMajor();', '');
		}
		
		//判断获取参数为save，执行保存操作
		if(id == 'save'){
			var zydm = $('#ZYDM').textbox('getValue');
			var zymc = $('#ZYMC').textbox('getValue');
			
			if(zydm== ''){
				alertMsg('请填写专业代码');
				return;
			}
			if(zydm.length > 11){
				alertMsg('专业代码长度超出11位');
				return;
			}
			if(zymc == ''){
				alertMsg('请填写专业名称');
				return;
			}
			if(zymc.length > 50){
				alertMsg('专业名称长度超出50位');
				return;
			}
			if($('#KMLX').combobox('getValue') == ''){
				alertMsg('请选择专业类型');
				return;
			}
			if($('#XZ').numberbox('getValue') == ''){
				alertMsg('请填写学制');
				return;
			}
			
			$('#active').val(saveType);
			
			$('#JN_JYZZZBH').val($('#ZYZZ').combobox('getText'));
			$('#JN_JYZZZBH1').val($('#ZYZZ').combobox('getValues'));
			$("#form1").submit();
		}
	}
	
	//删除
	function delMajor(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_MajorSet',
			data : 'active=del&ZYDM=' + iKeyCode,
			dataType:"json",
			success : function(data){
				if(data[0].MSG == '删除成功'){
					showMsg(data[0].MSG);
					loadData();
				}else{
					alertMsg(data[0].MSG);
				}
			}
		});
	}
	
	/**表单提交**/
	$('#form1').form({
		//定位到servlet位置的url
		url:'<%=request.getContextPath()%>/Svl_MajorSet',
		//当点击事件后触发的事件
		onSubmit: function(data){
			return $(this).form('validate');//验证
		}, 
		//当点击事件并成功提交后触发的事件
		success:function(data){
			var json = eval("("+data+")");
			
			if(json[0].MSG == '保存成功'){
				showMsg(json[0].MSG);
				$("#active").val('');
				
				emptyDialog();//清空并关闭dialog
				loadData();
				$('#majorInfo').dialog('close');
			}else{
				alertMsg(json[0].MSG);
			}
		}
	});
	
	/**清空Dialog中表单元素数据**/
	function emptyDialog(){
		saveType = '';
		$('#ZYDM').textbox('readonly', false);
		$('#ZYDM').textbox('setValue', '');
		$('#ZYMC').textbox('setValue', '');
		$('#KMLX').combobox('setValue', '');
		$('#XZ').numberbox('setValue', '');
		$('#ZYZZ').combobox('setValue', '');
	}
	
	//防JS注入
	/*
	function checkData(str) {
        var  entry = { "'": "&apos;", '"': '&quot;', '<': '&lt;', '>': '&gt;' };
        str = v.replace(/(['")-><&\\\/\.])/g, function ($0){
        	return entry[$0] || $0;
        });
        return str;
    }*/
</script>
</html>