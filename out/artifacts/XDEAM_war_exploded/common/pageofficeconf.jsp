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
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/css/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/css/themes/icon.css">
<script type="text/javascript"
	src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
<title>模版配置</title>
</head>
	
	<body class="easyui-layout">
		<form id="form1" name="form1" method="POST">
			<div region="north" align="left" title="模版配置"
				style="overflow: hidden; height: 50px; width: 800px;">
				<a href="#"
					 onclick="doToolbar(this.id);"
					id="add" name="add" class="easyui-linkbutton" plain="true"
					iconCls="icon-add">添加文件</a>	
				<a href="#"
					 onclick="doToolbar(this.id);"
					id="edit" name="edit" class="easyui-linkbutton" plain="true"
					iconCls="icon-edit">编辑</a>
				<a href="#"
					 onclick="doToolbar(this.id);"
					id="edittemp" name="edittemp" class="easyui-linkbutton" plain="true"
					iconCls="icon-edit">编辑模版</a>
				<a href="#"
					 onclick="doToolbar(this.id);"
					id="enables" name="enables" class="easyui-linkbutton" plain="true"
					iconCls="icon-cancel">启用</a>
				<a href="#"
					 onclick="doToolbar(this.id);"
					id="disables" name="disables" class="easyui-linkbutton" plain="true"
					iconCls="icon-cancel">禁用</a>
				<a href="#"
					 onclick="doToolbar(this.id);"
					id="audit" name="audit" class="easyui-linkbutton" plain="true"
					iconCls="icon-cancel">审核配置</a>
			</div>
			<div region="center" style="background: #fafafa; width: 100%;height:90%">
				<table id="audit" name="audit"></table>
				<input type="hidden" id="active" name="active" ></input>
				<input type="hidden" id="delid" name="delid" ></input>
				<input type="hidden" id="edition" name="edition" ></input>
			</div>
		</form>
	</body>
	<div id="aduditedit" name="aduditedit" title="部门负责人配置(一级审核)" class="easyui-window"  style="width:700px" resizable=false closed=true modal=true>
		<form id="form2" name="form2" method="POST">
			<center>
				<table class="tablestyle" style="width:100%" cellpadding="4">
					<tr>
						<td>标题</td>
						<td>
							<input type='text'/>
						</td>
						<td>文档类型</td>
						<td>
							<select
									class="easyui-combobox" name="one" id="one" style="width: 200px;">
							</select>
						</td>
					</tr>
					<tr>
						<td>章节号</td>
						<td>
							<input type='text'/>
						</td>
						<td>模版使用部门</td>
						<td>
							<select
									class="easyui-combobox" name="one" id="one" style="width: 200px;">
							</select>
						</td>
					</tr>
					<tr>

						<td>模版审核人员</td>
						<td colspan=3>
							<select
									class="easyui-combobox" name="one" id="one" style="width: 200px;">
							</select>
						</td>
					</tr>
					<tr>
						<td align="right" colspan=4>
							<a href="#"
								 onclick="doToolbar(this.id);"
								id="addbtn" name="addbtn" class="easyui-linkbutton" plain="false">添加模版(生成数据和空模版文件)</a>
								<a href="#"
								 onclick="doToolbar(this.id);"
								id="in" name="in" class="easyui-linkbutton" plain="false">导入模版(生成数据)</a>	
								<a href="#"
								 onclick="doToolbar(this.id);"
								id="save" name="save" class="easyui-linkbutton" plain="false">保存</a>		
						</td>
					</tr>
					<tr>
						<td colspan=4>隐藏区域</td>
					</tr>
					<tr>
						<td colspan=4>
							模版编号(CR-SF-7.2.1-01-20120605-001-RDC)<br/>
							模版地址
						</td>
					</tr>
				</table>
			</center>
			<input type="hidden" id="orgid" name="orgid" ></input>
			<input type="hidden" id="cactive" name="cactive" ></input>
		</form>
	</div>
	
	<script type="text/javascript">
		var org='';
		
		var people='';
		
		var two='';
		var three='';
		var curorgid="";
		var rows = null;
		$('#two').combobox({
			data:eval(people),
			valueField:'comboValue',
			textField:'comboName'
		});
		
		$('#three').combobox({
			data:eval(people),
			valueField:'comboValue',
			textField:'comboName'
		});
		
		$('#org').combobox({
			data:eval(org),
			valueField:'comboValue',
			textField:'comboName',
			onSelect:function(record){
				getfieldlist();
			}
		});
		
		$('#aduditedit').dialog({
				/*
				toolbar:[{
					text:'保存',
					iconCls:'icon-save',
					handler:function(){
						$('#orgid').val($('#org').combobox('getValue'));
						$('#form2').submit();
					}
				}]
				*/
		});
		
		
		$('#form1').form({
			//定位到servlet位置的url
			url: '<%=request.getContextPath()%>/Svl_PaggofficeConf',
			//当点击事件后触发的事件
		    onSubmit: function(data){
		    	//alert($('#modeid').combobox('getValue'));
		    }, 
		    //当点击事件成功提交后触发的事件
		    success:function(data){
		    	//alert("success");
		    	var datas = eval("("+data+")");
		    	//showMsg(datas[0].MSG);
		    	location.reload();
		    	//loadGrid();
		    },
		    onLoadError:function(){
		    	//alert("error");
		    }
		});
		
		$('#form2').form({
			url: '<%=request.getContextPath()%>/Svl_PaggofficeConf',
			//当点击事件后触发的事件
		    onSubmit: function(data){
		    	//alert(active);
		    }, 
		    //当点击事件成功提交后触发的事件
		    success:function(data){
		   		var datas = eval("("+data+")");
		    	$("#aduditedit").window("close");
		    	doToolbar("save");
		    }
		});
		
		$(document).ready(function(){
			if(two!=""){
				$('#two').combobox('setValue', two);
			}
			if(three!=""){
				$('#three').combobox('setValue', three);
			}
			loadGrid();
		});
		
		function loadGrid(){
			$('#audit').datagrid({
				title:'审核列表',
				iconCls:'icon-edit',
				singleSelect:true,
				idField:'itemid',
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
				url: '<%=request.getContextPath()%>/Svl_PaggofficeConf',
				queryParams:{"active":"que"},
				columns:[[
					{field:'模版编号(隐藏)',title:'模版编号(隐藏,拼接)',width:100},
					{field:'模版名称',title:'模版名称',width:100},
					{field:'所属部门',title:'所属部门',width:100},
					{field:'部门号(隐藏)',title:'路径(隐藏)',width:100},
					{field:'一级审核人(隐藏)',title:'状态(隐藏)',width:100},
					{field:'模版序号(隐藏)',title:'模版序号(隐藏,自动)',width:100},
					{field:'创建时间(隐藏)',title:'创建时间(隐藏,自动)',width:100}
				]],
				onClickRow:function(rowIndex,rowData){
					curorgid=rowData.部门号;
				},
				onLoadSuccess:function(){
				}
			});
			//doToolbarAuth();
		}
		
		function doToolbar(id){
			if(id=="add"){
				$('#org').combobox('clear');
				$('#one').combobox('loadData','');
				$('#form2').form('clear');
				$('#cactive').val('savedf');
				$("#aduditedit").window('open');
			}
			if(id=="edit"){
				if(curorgid==""){
					alertMsg("请选择一条数据");
				}else{
					rows = $('#audit').datagrid('getSelected');
					$('#org').combobox('setValue', rows.部门号);
					$('#org').combobox('setText', rows.部门名称);
					//$('#orgid').val(rows.部门号);
					$('#org').combobox('disable');
					$("#cactive").val("edit");
					getfieldlist();
					$('#one').combobox('setValue', rows.一级审核人);
					$("#aduditedit").window('open');
				}
			}
			if(id=="save"){
				$("#active").val("save");	
				//alert($("#active").val());
				$('#form1').submit();
			}
			if(id=="cancel"){
				if(curorgid==""){
					alertMsg("请选择一条数据");
				}else{
					//$('#audit').datagrid('endEdit', lastIndex);
					$("#active").val("del");
					//alert(curAuditID);
					$("#delid").val(curorgid);
					ConfirmMsg("你确认要删除该记录吗?","$('#form1').submit();","");
				}
			}
		}
		
		function getfieldlist(){
			$.ajax({
				type: "POST",
				url: '<%=request.getContextPath()%>/Svl_PaggofficeConf',   //设置 SVL
				async:false,
				data: "active=org&orgid=" + $('#org').combobox("getValue"),  // 设置模式，一般为view ，主关键字值		
				dataType: 'json',
				success: function(datas){
					$('#one').combobox({
							data:datas,
							valueField:'comboValue',
							textField:'comboName'
					});
				}
			});
		}
		
	</script>
</html>