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
<title>当前流程通过条件</title>
</head>


	<body class="easyui-layout">
		<form id = "form1" name = "form1" method = "POST">
		<div region="center" style="background: #fafafa; width: 100%;height:90%">
			<table id="tgaudit" name="tgaudit" Style="width:300px;height:400px"> </table>
		<input type="hidden" id="active" name="active" ></input>
		<input type="hidden" id="auditids" name="auditids" ></input>
		<input type="hidden" id="deletetgauditid" name="deletetgauditid" ></input>
		</div>
		</form>
	</body>

<div id="tgaduditedit" name="tgaduditedit" title="审核通过配置" class="easyui-window"  style="width:400px" resizable=false closed=true modal=true>
<form id="form2" name="form2" method="POST">
	<center>
	<table class="tablestyle" style="width:100%" cellpadding="4">
	<tr>
		<td align="center">
			审核通过所需人员
		</td>
		<td>
			<select
				class="easyui-combobox" name="user" id="user" style="width: 200px;">
			</select>
		</td>
	</tr>
	</table>
	</center>
	<input type="hidden" id="tgid" name="tgid"></input>
	<input type="hidden" id="coactive" name="coactive" ></input>
	<input type="hidden" id="userid" name="userid" ></input>
	<input type="hidden" id="username" name="username" ></input>
	<input type="hidden" id="spauditid" name="spauditid" ></input>
	</form>
</div>

<script type="text/javascript">
		var lastIndex=-1;
		var auditid="<%= request.getParameter("auditid")%>";
		var buzhou="<%= request.getParameter("bz")%>";
		var modeid="<%= request.getParameter("modeid")%>";
		var curindex="";
		var field="";
		var renyuanbianhao="";
		var renyuanname="";
		var curtgid="";
		var rows = null;
		var user=<%=PublicTools.getComboboxOptions("V_审核标签配置","编号","角色名称+人员名称+字段名称","where 模块编号='"+MyTools.StrFiltr(request.getParameter("modeid"))+"'and 步骤='"+MyTools.StrFiltr(request.getParameter("bz"))+"' and 审核编号!='"+request.getParameter("auditid")+"' and 版本号='"+MyTools.StrFiltr(request.getParameter("edition"))+"'",request)%>;
		//var strM = "javascript is a good script language";
 		//alert("javascript is a good script language<br/>"+strM.replace(/(javascript)\s*(is)/g,"$1 $2 fun. it $2"));
 
		$(document).ready(function(){
			loadGrid();
			$('#tgaduditedit').window('center');
		});
		
		
		$('#tgaduditedit').dialog({
				toolbar:[{
					text:'保存',
					iconCls:'icon-save',
					handler:function(){			
						renyuanname=$("#user").combobox("getText");
						renyuanname=renyuanname.replace(/,/g,";");
						renyuanbianhao=$("#user").combobox("getValues");
						renyuanbianhao=renyuanbianhao.toString().replace(/,/g,";");
						$('#username').val(renyuanname);
						$('#userid').val(renyuanbianhao);
						$('#form2').submit();
					}
				}]
		});
		
		$('#user').combobox({
			data:user,
			multiple:"multiple",
			valueField:'comboValue',
			textField:'comboName'
		});
		
		$('#form1').form({
			//定位到servlet位置的url
			url: '<%=request.getContextPath()%>/Svl_Auditconfig',
			//当点击事件后触发的事件
		    onSubmit: function(data){
		    	//alert($('#modeid').combobox('getValue'));
		    }, 
		    //当点击事件成功提交后触发的事件
		    success:function(data){
		    	//alert("success");
		    	var datas = eval("("+data+")");
		    	//modeid=datas[0].MODEID;
		    	//wayid=datas[0].WAYID;
		    	//repeatid=datas[0].REPEATID;
		    	//alert(modeid);
		    	showMsg(datas[0].MSG);
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
		    	$("#tgaduditedit").window('close');
				loadGrid();
		    }
		});
		
		function loadGrid(){
			$('#tgaudit').datagrid({
				title:'当前流程通过条件列表',
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
				queryParams:{"active":"queaudittg","auditids":auditid},
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
							$('#tgaudit').datagrid('endEdit', lastIndex);
							doToolbar("cancel");
							lastIndex=-1;
						}
					}
				],
				columns:[[
					{field:'通过编号',title:'通过编号',width:100,hidden:true},
					{field:'编号',title:'编号',width:100,hidden:true},
					{field:'通过人员编号',title:'通过人员编号',width:100,hidden:true},
					{field:'随机',title:'随机',width:100,editor:'text',hidden:true},
					{field:'通过人员名称',title:'审核通过所需人员',width:100}
				]],
				onClickRow:function(rowIndex,rowData){
					curtgid=rowData.通过编号;
				},
				onLoadSuccess:function(){
					//待定
					curtgid=="";
				}
			});
		}
		
		
		function doToolbar(id){
			if(id=="add"){
				renyuanbianhao="";
				renyuanname="";
				$('#user').combobox('clear');
				$('#form2').form('clear');
				$("#spauditid").val(auditid);
				$("#coactive").val("tgadd");
				$("#tgaduditedit").window('open');
			}
			if(id=="edit"){
				if(curtgid==""){
					alertMsg("请选择一条数据");
				}else{
					rows = $('#tgaudit').datagrid('getSelected');
					$("#coactive").val("tgedit");
					var users=rows.通过人员编号;
					//renyuanbianhao=rows.通过人员编号;
					//renyuanname=rows.通过人员名称;
					$('#user').combobox('setValues', users.split(";"));
					$("#spauditid").val(auditid);
					$('#userid').val(rows.通过人员编号);
					$('#username').val(rows.通过人员名称);
					$("#tgid").val(rows.通过编号);
					$("#tgaduditedit").window('open');
				}
			}
			if(id=="cancel"){
				if(curtgid==""){
					alertMsg("请选择一条数据");
				}else{
					$('#tgaudit').datagrid('endEdit', lastIndex);
					$("#active").val("deletetgaudit");
					$("#deletetgauditid").val(curtgid);
					ConfirmMsg("你确认要删除该记录吗?","$('#form1').submit();","");
				}
			}
		}
		
		function deleterow(){
			$('#tgaudit').datagrid('deleteRow',curindex);
		}
	</script>
</html>