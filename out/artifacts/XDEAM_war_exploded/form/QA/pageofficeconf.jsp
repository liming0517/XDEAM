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
					iconCls="icon-add">添加</a>	
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
					id="uptemp" name="uptemp" class="easyui-linkbutton" plain="true"
					iconCls="icon-redo">导入模版</a>
				<a href="#"
					 onclick="doToolbar(this.id);"
					id="qiyong" name="qiyong" class="easyui-linkbutton" plain="true"
					iconCls="icon-ok">启用</a>
				<a href="#"
					 onclick="doToolbar(this.id);"
					id="jinyong" name="jinyong" class="easyui-linkbutton" plain="true"
					iconCls="icon-cancel">禁用</a>
				<a href="#"
					 onclick="doToolbar(this.id);"
					id="shenhe" name="shenhe" class="easyui-linkbutton" plain="true"
					iconCls="icon-undo">审核模版</a>
			</div>
			<div region="center" style="background: #fafafa; width: 100%;height:90%">
				<table id="page" name="page"></table>
				<input type="hidden" id="active" name="active" ></input>
				<input type="hidden" id="delid" name="delid" ></input>
			</div>
		</form>
	</body>
	<div id="pageedit" name="pageedit" title="模版配置" class="easyui-window"  style="width:700px" resizable=false closed=true modal=true>
		<form id="form2" name="form2" method="POST">
			<center>
				<table class="tablestyle" style="width:100%" cellpadding="4">
					<tr>
						<td>标题</td>
						<td>
							<input type='text' id='title' name='title' style="width: 200px;"/>
						</td>
						<td>ISO标识</td>
						<td>
							<input type='text' id='iso' name='iso' style="width: 200px;"/>
						</td>
					</tr>
					<tr>
						<td>模版使用部门</td>
						<td>
							<select
									class="easyui-combobox" name="dep" id="dep" style="width: 200px;">
							</select>
							<input type="checkbox" id='all' name='all' style="width: 20px;" onclick="alls();">全部
						</td>
						<td>发起部门</td>
						<td>
							<select
									class="easyui-combobox" name="fqdep" id="fqdep" style="width: 200px;">
							</select>
						</td>
					</tr>
				</table>
			</center>
			<input type="hidden" id="ikeycode" name="ikeycode" ></input>
			<input type="hidden" id="depid" name="depid" ></input>
			<input type="hidden" id="depname" name="depname" ></input>
			<input type="hidden" id="cactive" name="cactive" ></input>
		</form>
	</div>
	<div id="upload" name="upload" title="模版上传" class="easyui-window"  style="width:700px" resizable=false closed=true modal=true>
		<form  id="form3" name="form3" method="POST" enctype="multipart/form-data">
			<center>
				<table class="tablestyle" style="width:100%" cellpadding="4">
					<tr>
						<td>模版</td>
						<td>
							<input type='file' id='file1' name='file1' style="width: 280px;" accept="application/msword"/>
						</td>
					</tr>
				</table>
			</center>
			<input type="hidden" id="fileurl" name="fileurl" ></input>
		</form>
	</div>
	<script type="text/javascript">
		var dep=<%=PublicTools.getComboboxOptions("sysDepartment","distinct DeptCode","CName","where 1=1",request)%>;
		
		var curid="";
		var rows = null;
		$('#dep').combobox({
			data:eval(dep),
			multiple:"multiple",
			valueField:'comboValue',
			textField:'comboName'
		});
		
		$('#fqdep').combobox({
			data:eval(dep),
			valueField:'comboValue',
			textField:'comboName'
		});
		
		$('#pageedit').dialog({
			toolbar:[{
				text:'保存',
				iconCls:'icon-save',
				handler:function(){
					$('#depid').val(";"+$('#dep').combobox('getValues').toString().replace(/,/g,";")+";");
					$('#depname').val($('#dep').combobox('getText'));
					if($("#all").attr("checked")=="checked"){
						$('#depid').val("all");
						$('#depname').val("全部");
					}
				
					$('#form2').submit();
				}
			}]
				
		});
		
		$('#upload').dialog({
			toolbar:[{
				text:'导入',
				iconCls:'icon-save',
				handler:function(){
					$('#form3').submit();
				}
			}]
				
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
		    	showMsg(datas[0].MSG);
		    	//location.reload();
		    	loadGrid();
		    },
		    onLoadError:function(){
		    	//alert("error");
		    }
		});
		
		$('#form2').form({
			url: '<%=request.getContextPath()%>/Svl_PaggofficeConf',
			//当点击事件后触发的事件
			async:false,
		    onSubmit: function(data){
		    	//alert(active);
		    }, 
		    //当点击事件成功提交后触发的事件
		    success:function(data){
		    	//alert(111);
		   		var datas = eval("("+data+")");
		    	$("#pageedit").window("close");
		    	
		    	//location.href="./word.jsp?url="+datas[0].url;
		    	showMsg(datas[0].MSG);
		    	loadGrid();
		    }
		});
		
		$('#form3').form({
			url: '<%=request.getContextPath()%>/UploadToolbar',
			//当点击事件后触发的事件
			async:false,
		    onSubmit: function(data){
		    	//alert(active);
		    }, 
		    //当点击事件成功提交后触发的事件
		    success:function(data){
		   		var datas = eval("("+data+")");
		    	$("#upload").window("close");
		    	location.href="./word.jsp?code="+curid+"&tablename="+encodeURI("V_模版配置表")+"&filed="+encodeURI("模版编号");
		    }
		});
		
		$(document).ready(function(){
			//$("#add").attr("disabled",true);
			loadGrid();
			$("#edit").linkbutton('disable');
			$("#edittemp").linkbutton('disable');
			$("#uptemp").linkbutton('disable');
			$("#submit").linkbutton('disable');
			$("#qiyong").linkbutton('disable');
			$("#jinyong").linkbutton('disable');
			$("#shenhe").linkbutton('disable');
		});
		
		function loadGrid(){
			$('#page').treegrid({
				title:'模版配置列表',
				iconCls:'icon-edit',
				singleSelect:true,
				idField: 'id',
                treeField: 'id',
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
				viewsortcols:[false,'vertical',false],
				url: '<%=request.getContextPath()%>/Svl_PaggofficeConf',
				queryParams:{"active":"que"},
				columns:[[
					{field:'id',title:'发起部门',width:100,formatter:function(value,row,index){
							//alert();
							if(row._parentId!=undefined){
								return row.创建用户;
							}else{
								return value;
							}
						}
					},
					{field:'模板名称',title:'模板名称',width:100},
					{field:'模版使用部门名称',title:'所属部门',width:100},
					{field:'状态',title:'模版状态',width:100,formatter:function(value,row,index){
							if(value=="0"){
								return "编辑";
							}
							if(value=="1"){
								return "提交";
							}
							if(value=="2"){
								return "驳回";
							}
							if(value=="3"){
								return "启用";
							}
							if(value=="4"){
								return "禁用";
							}
						}
					}
				]],
				onClickRow:function(){
					
					rowData = $('#page').treegrid('getSelected');
					curid=rowData.模版编号;
					if(rowData.状态==""){
						$("#edit").linkbutton('disable');
						$("#edittemp").linkbutton('disable');
						$("#uptemp").linkbutton('disable');
						$("#submit").linkbutton('disable');
						$("#qiyong").linkbutton('disable');
						$("#jinyong").linkbutton('disable');
						$("#shenhe").linkbutton('disable');
					}
					if(rowData.状态=="0"){
						//$('#btn').linkbutton('disable');
						$("#edit").linkbutton('enable');
						$("#edittemp").linkbutton('enable');
						$("#uptemp").linkbutton('enable');
						$("#submit").linkbutton('enable');
						$("#qiyong").linkbutton('disable');
						$("#jinyong").linkbutton('disable');
						$("#shenhe").linkbutton('disable');
						//alert();
						//$("#qiyong").linkbutton('enable');
					}
					if(rowData.状态=="1"){
						$("#edit").linkbutton('disable');
						$("#edittemp").linkbutton('disable');
						$("#uptemp").linkbutton('disable');
						$("#submit").linkbutton('disable');
						$("#qiyong").linkbutton('disable');
						$("#jinyong").linkbutton('disable');
						$("#shenhe").linkbutton('enable');
					}
					if(rowData.状态=="2"){
						$("#edit").linkbutton('enable');
						$("#edittemp").linkbutton('enable');
						$("#uptemp").linkbutton('enable');
						$("#submit").linkbutton('enable');
						$("#qiyong").linkbutton('disable');
						$("#jinyong").linkbutton('disable');
						$("#shenhe").linkbutton('disable');
					}
					if(rowData.状态=="3"){
						$("#edit").linkbutton('disable');
						$("#edittemp").linkbutton('disable');
						$("#uptemp").linkbutton('disable');
						$("#submit").linkbutton('disable');
						$("#qiyong").linkbutton('disable');
						$("#jinyong").linkbutton('enable');
						$("#shenhe").linkbutton('disable');
					}
					if(rowData.状态=="4"){
						$("#edit").linkbutton('disable');
						$("#edittemp").linkbutton('disable');
						$("#uptemp").linkbutton('disable');
						$("#submit").linkbutton('disable');
						$("#qiyong").linkbutton('enable');
						$("#jinyong").linkbutton('disable');
						$("#shenhe").linkbutton('disable');
					}
					//alert(rowData.模版状态);
				},
				onLoadSuccess:function(){
				}
			});
		}
		
		function doToolbar(id){
			if(id=="add"){
				$('#dep').combobox('clear');
				$('#form2').form('clear');
				$("#cactive").val("save");
				$('#dep').combobox('enable');
				$('#fqdep').combobox('enable');
				$("#iso").removeAttr("disabled");
				$("#pageedit").window('open');
			}
			if(id=="edit"){
				if(curid==""){
					alertMsg("请选择一条数据");
				}else{
					rows = $('#page').treegrid('getSelected');
					$('#fqdep').combobox('setValue', rows.发起部门);
					$('#fqdep').combobox('disable');
					$("#cactive").val("edit");
					$("#iso").val(rows.模版前缀);
					$("#title").val(rows.模板名称);
					$("#ikeycode").val(rows.模版编号);
					$("#iso").attr("disabled","true");
					if(rows.模版使用部门编号=="all"){
						$('#dep').combobox('clear');
						$('#dep').combobox('disable');
						$("#all").attr("checked","checked");
					}else{
						$('#dep').combobox('enable');
						var jihuo=rows.模版使用部门编号.substring(1);
						jihuo=jihuo.substring(0,jihuo.length-1);
						//actnum=jihuo;
						$('#dep').combobox('setValues', jihuo.split(";"));
						$("#all").removeAttr("checked");
						
					}
					//$('#iso').attr('disable',true);
					$("#pageedit").window('open');
				}
			}
			if(id=="edittemp"){
				if(curid==""){
					alertMsg("请选择一条数据");
				}else{
					rows = $('#page').treegrid('getSelected');
					location.href="./word.jsp?code="+curid+"&tablename="+encodeURI("V_模版配置表")+"&filed="+encodeURI("模版编号");
				}
			}
			
			if(id=="uptemp"){
				if(curid==""){
					alertMsg("请选择一条数据");
				}else{
					rows = $('#page').treegrid('getSelected');
					$("#fileurl").val(rows.路径);
					$("#upload").window('open');
				}
				//
				//location.href="./word.jsp?url="+rows.路径;
			}
			
			if(id=="submit"){
				if(curid==""){
					alertMsg("请选择一条数据");
				}else{
					$("#active").val('submit');
					$("#delid").val(curid);
					ConfirmMsg("提交后无法更改，您是否确认更提交?","$('#form1').submit();","");
				}
			}
			if(id=="cancel"){
				if(curid==""){
					alertMsg("请选择一条数据");
				}else{
					$("#active").val("del");
					$("#delid").val(curid);
					ConfirmMsg("你确认要删除该记录吗?","$('#form1').submit();","");
				}
			}
			if(id=="shenhe"){
				if(curid==""){
					alertMsg("请选择一条数据");
				}else{
					rows = $('#page').treegrid('getSelected');
					location.href="./auditword.jsp?code="+curid+"&tablename="+encodeURI("V_模版配置表")+"&filed="+encodeURI("模版编号");
				}
			}
			
			if(id=="qiyong"){
				if(curid==""){
					alertMsg("请选择一条数据");
				}else{
					//$('#audit').datagrid('endEdit', lastIndex);
					$("#active").val("qiyong");
					//alert(curAuditID);
					$("#delid").val(curid);
					$('#form1').submit();
				}
			}
			if(id=="jinyong"){
				if(curid==""){
					alertMsg("请选择一条数据");
				}else{
					//$('#audit').datagrid('endEdit', lastIndex);
					$("#active").val("jinyong");
					//alert(curAuditID);
					$("#delid").val(curid);
					$('#form1').submit();
				}
			}
			
		}
		
		function alls(){
			if($("#all").attr("checked")=="checked"){
				$('#dep').combobox('disable');
			}else{
				$('#dep').combobox('enable');
			}
		}
	</script>
</html>