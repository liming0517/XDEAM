<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.base.common.tools.PublicTools"%>
<%@ page import="com.pantech.src.develop.Logs.*"%>
<%@ page import="com.pantech.base.common.tools.*"%>
<%@ page import="com.pantech.src.develop.store.user.*"%>
<%@ page import="com.pantech.src.develop.manage.workremind.WorkRemind"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.*"%>
<%@ page import="com.pantech.base.common.db.DBSource"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> 
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/icon.css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
<title>审核流程定义</title>
</head>

	<body class="easyui-layout">
		<form id = "form1" name = "form1" method = "POST">
		<div region="center" style="background: #fafafa; width: 100%;height:90%">
			<table id="spaudit" name="spaudit"> </table>
		</div>
			<input type="hidden" id="active" name="active" ></input>
			<input type="hidden" id="auditids" name="auditids" ></input>
			<input type="hidden" id="deletespauditid" name="deletespauditid" ></input>
		</form>
	</body>

<div id="spaduditedit" name="spaduditedit" title="审核前置配置" class="easyui-window"  style="width:450px" resizable=false closed=true modal=true>
<form id="form2" name="form2" method="POST">
	<center>
	<table class="tablestyle" style="width:100%" cellpadding="4">
	<tr>
		<td align="center">
			所需激活编号
		</td>
		<td>
			<select
				class="easyui-combobox" name="spcode" id="spcode" style="width: 360px;">
			</select>
		</td>
	</tr>
	<tr>
		<td>业务字段</td>
		<td>
			<select
				class="easyui-combobox" name="spfield" id="spfield" style="width: 200px;">
			</select>
			<select
				class="easyui-combobox" name="term" id="term" style="width: 60px;">
			</select>
			<input id="number" name="number" style="width: 90px;"></input>
		</td>
	</tr>
	</table>
	</center>
	<input type="hidden" id="spid" name="spid" ></input>
	<input type="hidden" id="actnum" name="actnum" ></input>
	<input type="hidden" id="coactive" name="coactive" ></input>
	<input type="hidden" id="termid" name="termid" ></input>
	<input type="hidden" id="termname" name="termname" ></input>
	<input type="hidden" id="spauditid" name="spauditid" ></input>
	</form>
</div>
<script type="text/javascript">
		var lastIndex=-1;
		var auditid="<%= request.getParameter("auditid")%>";
		var curindex="";
		var field="";
		var termbianhao="";
		var termname="";
		var curspid="";
		var rows = null;
		var term = new Array(
		    {termid:'001',termname:'大于'},
		    {termid:'002',termname:'大于等于'},
		    {termid:'003',termname:'等于'},
		    {termid:'004',termname:'小于等于'},
		    {termid:'005',termname:'小于'}
		);
		
		var act=<%=PublicTools.getComboboxOptions("V_审核标签配置","distinct 步骤","步骤","where 模块编号='"+MyTools.StrFiltr(request.getParameter("modeid"))+"' and 步骤!='"+MyTools.StrFiltr(request.getParameter("bz"))+"' and 版本号='"+MyTools.StrFiltr(request.getParameter("edition"))+"'",request)%>;
		var actnum="";
		//var fieldlist = window.opener.fieldlist;//获取父页面值
		var fieldlist = window.dialogArguments.fieldlist;
		$(document).ready(function(){
			loadGrid();
			$('#spaduditedit').window('center');
		});
		
		$('#spaduditedit').dialog({
				toolbar:[{
					text:'保存',
					iconCls:'icon-save',
					handler:function(){
						//alert(actnum);				
						if($('#term').combobox('getValue')=="001"){
							$('#termid').val(">");
						}else if($('#term').combobox('getValue')=="002"){
							$('#termid').val(">=");
						}else if($('#term').combobox('getValue')=="003"){
							$('#termid').val("=");
						}else if($('#term').combobox('getValue')=="004"){
							$('#termid').val("<=");
						}else if($('#term').combobox('getValue')=="005"){
							$('#termid').val("<");
						}
						actnum=$('#spcode').combobox("getValues");
						actnum=actnum.toString().replace(/,/g,";");
						$('#actnum').val(";"+actnum+";");
						$('#termname').val($('#term').combobox('getText'));
						$('#form2').submit();
					}
				}]
		});
		
		$('#spcode').combobox({
			data:act,
			multiple:"multiple",
			valueField:'comboValue',
			textField:'comboName'
		});
		
		
		$('#spfield').combobox({
			data:fieldlist,
			valueField:'Name',
			textField:'Name'
		});
		
		
		$('#term').combobox({
			data:term,
			valueField:'termid',
			textField:'termname'
		});
		
		$("#number").numberbox({precision:2});
		$('#form1').form({
			//定位到servlet位置的url
			url: '<%=request.getContextPath()%>/Svl_Auditconfig',
			//当点击事件后触发的事件
		    onSubmit: function(data){
		    	//alert($('#modeid').combobox('getValue'));
		    }, 
		    //当点击事件成功提交后触发的事件
		    success:function(data){
		    	var datas = eval("("+data+")");
		    	//showMsg(datas[0].MSG);
		    	loadGrid();
		    },
		    onLoadError:function(){
		    	//alert("error");
		    }
		});
		
		
		$('#form2').form({
			url: '<%=request.getContextPath()%>/Svl_Auditedit',
			//当点击事件后触发的事件
		    onSubmit: function(data){
		    }, 
		    success:function(data){
		    	$("#spaduditedit").window('close');
				loadGrid();
		    }
		});
		
		function loadGrid(){
			$('#spaudit').datagrid({
				title:'特殊条件列表',
				iconCls:'icon-edit',
				singleSelect:true,
				width: '100%',
				nowrap: false,
				fit:true,
				showFooter:true,
				striped:true, //隔行变色
				pagination:true,
				pageSize:10,
				singleSelect:true,
				pageNumber:1,
				rownumbers:true,
				fitColumns: true,
				url: '<%=request.getContextPath()%>/Svl_Auditconfig',
				queryParams:{"active":"queauditsp","auditids":auditid},
				toolbar:[
					{
						text:'添加',
						iconCls:'icon-add',
						handler:function(){
							doToolbar("add");
						}
					},
					{
						text:'编辑',
						iconCls:'icon-ok',
						handler:function(){
							doToolbar("edit");
						}
					},
					{
						text:'删除',
						iconCls:'icon-cancel',
						handler:function(){
							$('#spaudit').datagrid('endEdit', lastIndex);
							doToolbar("cancel");
							lastIndex=-1;
						}
					}
				],
				columns:[[
					{field:'特殊编号',title:'特殊编号',rowspan:2,width:100,hidden:true},
					{field:'随机',title:'随机',width:100,editor:'text',rowspan:2,hidden:true},
					{title:'必要条件',width:100},
					{title:'特殊条件',colspan:4,width:400}],[
					{field:'所需激活编号',title:'所需激活编号',width:100,
						formatter:function (value,row,index){
							value=value.substring(1);
							value=value.substring(0,value.length-1);
							return value;
						}
					},
					{field:'特殊字段',title:'业务字段',width:100},
					{field:'条件编号',title:'条件编号',width:100,hidden:true},
					{field:'条件',title:'业务属性',width:100},
					{field:'数值',title:'数值',width:100}
				]],
				onClickRow:function(rowIndex,rowData){
					curspid=rowData.特殊编号;
				},
				onLoadSuccess:function(){
					//待定
					curspid="";
				}
			});
		}
		
		function doToolbar(id){
			if(id=="add"){
				//actnum="";
				$('#spcode').combobox('clear');
				$('#spfield').combobox('clear');
				$('#term').combobox('clear');
				$('#form2').form('clear');
				$("#spauditid").val(auditid);
				$("#coactive").val("spadd");
				$("#spaduditedit").window('open');
			}
			if(id=="edit"){
				if(curspid==""){
					alertMsg("请选择一条数据");
				}else{
					rows = $('#spaudit').datagrid('getSelected');
					
					$("#spid").val(curspid);
					$("#coactive").val("spedit");
					var jihuo=rows.所需激活编号.substring(1);
					jihuo=jihuo.substring(0,jihuo.length-1);
					//actnum=jihuo;
					$('#spcode').combobox('setValues', jihuo.split(";"));
					$('#actnum').val(rows.所需激活编号);
					if(rows.条件编号==">"){
						$('#term').combobox('setValue', "001");
					}else if(rows.条件编号==">="){
						$('#term').combobox('setValue', "002");
					}else if(rows.条件编号=="="){
						$('#term').combobox('setValue', "003");
					}else if(rows.条件编号=="<="){
						$('#term').combobox('setValue', "004");
					}else if(rows.条件编号=="<"){
						$('#term').combobox('setValue', "005");
					}
					$('#spfield').combobox('setValue', rows.特殊字段);
					$("#number").val(rows.数值);
					$("#spaduditedit").window('open');
				}
			}
			if(id=="cancel"){
				if(curspid==""){
					alertMsg("请选择一条数据");
				}else{
					$('#spaudit').datagrid('endEdit', lastIndex);
					$("#active").val("deletespaudit");
					$("#deletespauditid").val(curspid);
					ConfirmMsg("你确认要删除该记录吗?","$('#form1').submit();","");
				}
			}
		}
		
		function deleterow(){
			$('#spaudit').datagrid('deleteRow',curindex);
		}
	</script>
</html>