<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<title></title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/icon.css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/publicScript.js"></script>
</head>
<body class="easyui-layout">
	<div data-options="region:'north'" title="新建校区" style="background:#fafafa;height:93px;">
		<table>
			<tr>
				<td><a href="#" id="new" class="easyui-linkbutton" plain="true" iconcls="icon-new" onClick="doToolbar(this.id);" title="">新建</a>
				</td>
				<td><a href="#" id="edit" class="easyui-linkbutton" plain="true" iconcls="icon-edit" onClick="doToolbar(this.id);" title="">编辑</a>
				</td>
				<td><a href="#" id="del" onclick="doToolbar(this.id)" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-cancel'">删除</a>
				</td>
				<td><input id="teaName" type="text" style="display:none;"/><a href="#" id="import" onclick="doToolbar(this.id)"
					class="easyui-linkbutton"
					data-options="plain:true,iconCls:'icon-site'">导入教师</a>
				</td>
			</tr>
		</table>
		<table class="tablestyle" width="100%">
			<tr>
				<td class="titlestyle" style="width:150px;">校区名字</td>
				<td class="titlestyle">
					<select name="comboxxqmc" id="comboxxqmc" class="easyui-combobox" style="width:150px;">
					</select>
				</td>
				<td class="titlestyle" style="width:150px;">教师姓名</td>
				<td class="titlestyle">
					<input name="comboxjsmc" id="comboxjsmc" class="easyui-textbox" style="width:150px;">
				</td>
				<td  style="width:150px;" align="center">
					<a href="#" onclick="doToolbar(this.id)" id="query" class="easyui-linkbutton" plain="true" iconcls="icon-search">查询</a>
				</td>
			</tr>
		</table>
	</div>
	<div data-options="region:'center'">
		<table id='list1' class="tablestyle" width="100%"></table>
		<div id="xjzz" title="新建校区" style="overflow:hidden;">
			<form id="form1" method='post'>
				<table id="zhuanyezuzhang" class="tablestyle" style="width:100%;">
					<tr>
						<td class="titlestyle">校区</td>
						<td class="titlestyle"><input class="easyui-combobox"
							panelHeight="105" editable="false" style="width:200px;"
							id="comboxxq1">
						</td>
					</tr>
					<tr>
						<td class="titlestyle">专业名称</td>
						<td class="titlestyle"><input class="easyui-combobox"
							panelHeight="105" editable="false" style="width:200px;"
							id="comboxtyzy">
						</td>
					</tr>
					<tr>
						<td class="titlestyle">专业组长</td>
						<td class="titlestyle"><input class="easyui-combobox"
							panelHeight="105" editable="false" style="width:200px;"
							id="comboxtyzz1">
						</td>
					</tr>
					<input type="hidden" id="changedm" name="changedm" />
					<input type="hidden" id="XQDM" name="XQDM" />
					<input type="hidden" id="ZYDM" name="ZYDM" />
					<input type="hidden" id="JSDM" name="JSDM" />
					<input type="hidden" id="JSMC" name="JSMC" />
					<input type="hidden" id="xq1" name="xq1" />
					<input type="hidden" id="tyzy" name="tyzy" />
					<input type="hidden" id="tyzz1" name="tyzz1" />
					<input type="hidden" id="active" name="active" />
				</table>
			</form>
		</div>
	</div>
</body>
<script type="text/javascript">
	var row="";
	var xqcode="";
	var xqname="";
	var xqcode="";
	var zyname="";
	var zycode="";
	var jsname="";
	var jscode="";
	var iKeyCode="";
	var jsmcque="";
	
	$(document).ready(function(){
		initCombobox();
		initDialog();
		loadGrid();
	});
	
	function initCombobox(){
		$('#comboxxqmc').combobox({
			url:"<%=request.getContextPath()%>/Svl_teamleader?active=comboxxqmc",
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			onLoadSuccess:function(){
			},
			onSelect:function(){
				$("#XQDM").val($('#comboxxqmc').combobox('getValue'));
			}
		});
		
		//$('#comboxjsmc').combobox({
		//	url:"<%=request.getContextPath()%>/Svl_teamleader?active=comboxjsmc",
			//valueField:'comboValue',
		//	textField:'comboName',
		//	onLoadSuccess:function(){
		//	},
		//	onSelect:function(){
		//	$("#JSDM").val($('#comboxjsmc').combobox('getValues'));
		//	}
	//	});
		
		comboxDialog();
	}
	
	function initDialog() {
		$('#xjzz').dialog({
			width : 350,//宽度设置   
			height : 140,//高度设置 
			modal : true,
			closed : true,
			cache : false,
			draggable : false,
			toolbar : [ {
				//保存编辑
				text : '保存',
				iconCls : 'icon-save',
				handler : function() {
					//传入save值进入doToolbar方法，用于保存
					doToolbar(type);
				}
			} ],
			//打开事件
			onOpen : function(data) {
			},
			//读取事件
			onLoad : function(data) {
			},
			//关闭事件
			onClose : function(data) {
			empty();
			}
		});
	}
	
function loadGrid(){
	$('#list1').datagrid({
		url:'<%=request.getContextPath()%>/Svl_teamleader',
		queryParams:{"active":"query","queryxq":encodeURI($('#comboxxqmc').combobox('getValue')),"queryjs":encodeURI(jsmcque)},
		loadMsg : "信息加载中请稍后!",//载入时信息
		width:'100%',
		nowrap: false,
		fit:true, //自适应父节点宽度和高度
		striped:true,
		singleSelect:true,
		rownumbers:true,
		fitColumns: true,
		columns:[[
			{field:'XQMC',title:'校区名称',width:120},
			{field:'ZYDM',title:'专业代码',width:120},
			{field:'ZYMC',title:'专业名称',width:120},
			{field:'XM',title:'专业组长',width:120}
			]],	
		onClickRow:function(rowIndex, rowData){
				row = rowData;
				xqname=row.XQMC;
				xqcode=row.XQDM;
				zyname=row.ZYMC;
				zycode=row.ZYDM;
				jsname=row.XM;
				jscode=row.JSDM;
				iKeyCode=rowIndex;
				$('#xq1').val(xqcode);
				$('#tyzy').val(zycode);
				$('#tyzz1').val(jscode);
		},
		onLoadSuccess: function(){
			//empty();
		},
		onLoadError:function(){				
		}
	});		
}
function doToolbar(itoolbar){
	if(itoolbar=="new"){
		$('#xjzz').dialog({title: '新建专业组长'});
		$('#xjzz').dialog("open");
		type="savezz";
		comboxDialog();
	}
	if(itoolbar=="savezz"){
		if($("#XQDM").val()==""||$("#ZYDM").val()==""||$("#JSDM").val()==""){
			alertMsg("未完成选择");
			return;
		}
		var jsmc=$('#comboxtyzz1').combobox('getText');
		$("#JSMC").val(jsmc);
		$("#active").val("savezuzhang");
		$("#form1").submit();
	}
	if(itoolbar=="edit"){
		if(row == ""){
				//提示信息
				alertMsg('请选择一行数据');
				return;
			}else{
				saveType = 'edit';
				//打开dialog
				$('#xjzz').dialog({title: '编辑专业组长'});
				$('#xjzz').dialog("open");
				$('#comboxxq1').combobox('setValue',xqcode);
				$('#comboxtyzy').combobox('setValue',zycode);
				$('#comboxxq1').combobox('disable');
				$('#comboxtyzy').combobox('disable');
				$('#comboxtyzz1').combobox('setValues',jscode);
				$('#XQDM').val(xqcode);
				$('#ZYDM').val(zycode);
				$('#JSDM').val(jscode);
			}

		type="updatezz";
	}
	if(itoolbar=="updatezz"){
		 if($("#XQDM").val()==""){
			alertMsg("校区不能为空");
			return;
		}
		var jsmc=$('#comboxtyzz1').combobox('getText');
		$("#JSMC").val(jsmc);
		$('#active').val("updatezz");
		$('#changedm').val(xqcode);
		$('#form1').submit();
	
	}
	if(itoolbar=="query"){
		jsmcque=$("#comboxjsmc").textbox('getValue');
		loadGrid();
	}
	if(itoolbar=="del"){
		$('#XQDM').val(xqcode);
		$('#ZYDM').val(zycode);
		$('#JSDM').val(jscode);
		
		if($("#XQDM").val()==""||$('#ZYDM').val(zycode)==""||$('#JSDM').val(jscode)==""){
			alertMsg("请选择一行 ");
			return;
		}
		$("#active").val("del");
		$('#form1').submit();
		
	}
	if(itoolbar=="import"){
		$('#teaName').show();
		if(!$('#teaName').val()==""){
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_teamleader',
				data : 'active=importTeacher&teaName=' + $('#teaName').val(),
				dataType:"json",
				success : function(data) {
					if(data[0].MSG=="导入成功"){
						showMsg(data[0].MSG);
					}else{
						alertMsg(data[0].MSG);
					}
					
				}
			});
		}
	}
}
$('#form1').form({
	url:'<%=request.getContextPath()%>/Svl_teamleader', 
	success:function(data){		
		var	data1=eval(data);
		if(data1[0].MSG=="保存成功"){
			$('#xjzz').dialog("close");
			loadGrid();
		}else if(data1[0].MSG=="编辑成功"){
			$('#xjzz').dialog("close");
			loadGrid();
		}else if(data1[0].MSG=="删除成功"){
			loadGrid();
		}
		showMsg(data1[0].MSG);	
		iKeyCode='';
		row='';
	}

});

function comboxDialog(){
	$('#comboxxq1').combobox({
		url:"<%=request.getContextPath()%>/Svl_teamleader?active=comboxxqmc",
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
			$("#XQDM").val($('#comboxxq1').combobox('getValue'));
		}
	});
	
	$('#comboxtyzy').combobox({
		url:"<%=request.getContextPath()%>/Svl_teamleader?active=comboxzy",
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
			$("#ZYDM").val($('#comboxtyzy').combobox('getValue'));
		}
	});

	$('#comboxtyzz1').combobox({
		url:"<%=request.getContextPath()%>/Svl_teamleader?active=comboxjsmc",
		valueField:'comboValue',
		textField:'comboName',
		editable:true,
		multiple:true,
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
			$("#JSDM").val($('#comboxtyzz1').combobox('getValues'));
		},
		onSelect: function (row) {
			 	if($(this).combobox('getValues')!=''){
			 		$(this).combobox('unselect', '');
			 	}
			 	if(row.comboValue==''){
			 		$(this).combobox('setValues', '');
			 	}
			},
			onUnselect: function (row) {
				if($(this).combobox('getValues')==''){
					$(this).combobox('setValues', '');
				}
			}
	});		
		
}
function empty(){
				$('#XQDM').val("");
				$('#ZYDM').val("");
				$('#JSDM').val("");
				$('#JSMC').val("");
}
</script>
</html>
