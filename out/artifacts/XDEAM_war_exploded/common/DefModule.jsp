<!-- 审核查询界面 -->
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
	<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	<%@ page import="com.pantech.base.common.tools.MyTools"%>
	<%@ page import="com.pantech.base.common.tools.PublicTools"%>
    <%@ page import="com.pantech.src.develop.Logs.*" %>
    <%@ page import="com.pantech.base.common.tools.*"%>
    <%@ page import="com.pantech.src.develop.store.user.*" %>
    <%@ page import="com.pantech.src.develop.manage.workremind.WorkRemind" %>
    <%@ page import="java.util.Vector"%>
    <%@ page import="java.util.*"%>
    <%@ page import="com.pantech.base.common.db.DBSource" %>
<html>
	<head>
		<title>模块初始化定义</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
			<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/icon.css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
	</head>

	
		<body class="easyui-layout">
			<form id = "form1" name = "form1" method = "POST">
			<div region="center" style="background:#fafafa;width:100%;">
	  			<table id="deflist" name="deflist" width="100%"></table>
			</div>
				<input type="hidden" id="active" name="active" ></input>
				<input type="hidden" id="deletemoduleid" name="deletemoduleid" ></input>
			</form>
		</body>

<div id="moduleedit" name="moduleedit" title="模块初始化配置" class="easyui-window"  style="width:600px" resizable=false closed=true modal=true>
<form id="form2" name="form2" method="POST">
	<center>
	<table class="tablestyle" style="width:100%" cellpadding="4">
	<tr>
	<td>模块名称</td>
	<td>
		<select
		class="easyui-combobox" name="modulename" id="modulename" style="width: 200px;">
		</select>
	</td>
	<td>相关视图</td>
	<td>
		<select
		class="easyui-combobox" name="moduleview" id="moduleview" style="width: 200px;">
		</select>
	</td>
	</tr>
	<tr>
	<td>主键名称</td>
	<td>
		<select
			class="easyui-combobox" name="moduleviewikey" id="moduleviewikey" style="width: 200px;">
		</select>
	</td>
	<td></td>
	<td>

	</td>
	</tr>
	</table>
	</center>
		<input type="hidden" id="coactive" name="coactive" ></input>
		<input type="hidden" id="moudleid" name="moudleid"></input>
	</form>
</div>

</html>
<script type = "text/JavaScript">
	var lastIndex = -1;
	var modeneirong="";
	var moduleid="";
	var modulename="";
	var curmoduleid="";
	<%System.out.println("aaaaaaaaaaaaaaaaaaaaa");%>
	var view=<%=PublicTools.getComboboxOptions("sysobjects","distinct name","name","where type='V'",request)%>;
	var viewname="";
	var viewfield="";
	var rows = null;
	//页面初始化
	$(document).ready(function(){
		//alert();
		loadGrid(); 
		loadmodeneirong();
	});
	
	function loadmodeneirong(){
		//alert();
		modeneirong=<%=PublicTools.getComboboxOptions("V_Node_AuthModule","distinct 模块编号","节点名","where 1=1",request)%>;		
		for(var i=0;i<modeneirong.length;i++){
			modeneirong[i].comboValue=modeneirong[i].comboValue.replace(/[.]/g,"_");//将"."转换为"_"方便combox编译
		}
		
		$('#modulename').combobox({
			data:modeneirong,
			valueField:'comboValue',
			textField:'comboName',
			onSelect:function(record){
				moduleid=record.comboValue;
				modulename=record.comboName;
			}
							
		});
	}
	//alert(modeneirong);

	
	$('#moduleview').combobox({
		//data:modeneirong,
		valueField:'comboValue',
		textField:'comboName',
		data:view,
		onSelect:function(record){
			viewname=record.comboName;
			//alert(viewname);
			$.ajax({
				type: "POST",
				url: '<%=request.getContextPath()%>/Svl_Auditconfig',   //设置 SVL
				async:false,
				data: "active=viewfield&viewname="+encodeURI(viewname),		
				dataType: 'json',
				success: function(datas){
					$('#moduleviewikey').combobox({
						data:datas,
						valueField:'Name',
						textField:'Name',
						onSelect:function(record){
							viewfield=record.Name;
						}
					}); 
					
				}
			});
		}
							
	});
	
	
	//获取表格中的数据（传入所有查询参数）
	function loadGrid() {
		//alert();
		$(function() {
			$('#deflist').datagrid({
				url: '<%=request.getContextPath()%>/Svl_Auditconfig',
				queryParams:{"active":"quedef"},
				title: '模块初始化定义列表',
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
				toolbar:[
					{
						text:'添加',
						iconCls:'icon-add',
						handler:function(){
								$('#modeid').val('');
								$('#modulename').combobox('enable');
								$('#modulename').combobox('clear');
								$('#moduleview').combobox('clear');
								$('#moduleviewikey').combobox('loadData','');
								$('#form2').form('clear');
								$("#coactive").val("savedefmod");
								$("#moduleedit").window('open');
						}
					},
					{
						text:'编辑',
						iconCls:'icon-edit',
						handler:function(){
							//doToolbar("save");
							if(curmoduleid==""){
								alertMsg("请选择一条数据");
							}else{
								rows = $('#deflist').datagrid('getSelected');
								$('#modulename').combobox('disable');
								$('#modulename').combobox('setValue', rows.模块编号);
								$("#moduleid").val(rows.模块编号);
								$('#moduleview').combobox('select', rows.相关视图);
								$('#moduleviewikey').combobox('setValue', rows.主键名称);
								$("#coactive").val("editdefmod");
								$("#moduleedit").window('open');								
							}
						}
					},
					{
						text:'删除',
						iconCls:'icon-cancel',
						handler:function(){
							//$('#deflist').datagrid('endEdit', lastIndex);
							doToolbar("cancel");
							lastIndex=-1;
						}
					},					
					{
						text:'移植',
						iconCls:'icon-send',
						handler:function(){
							doToolbar("tran");
						}
					}
				],
				columns:[[
					{field:'模块编号',title:'模块编号',width:100,hidden:true},
					{field:'模块名称',title:'模块名称',width:100},
					{field:'相关视图',title:'相关视图',width:100},
					{field:'主键名称',title:'主键名称',width:100}
				]],
				onClickRow:function(rowIndex,rowData){
					curmoduleid=rowData.模块编号;
				}
			});
		});
	}
	
	/*
	function insert(){
			$('#deflist').datagrid('endEdit', lastIndex);
			var row = $('#deflist').datagrid('getSelected');
			var indexs=0;
			if (row){
				indexs = $('#deflist').datagrid('getRowIndex', row);
			} else {
				indexs = 0;
			}
			$('#deflist').datagrid('insertRow', {
				index: 0,
				row:{
						//审核编号: auditid
				}
			});
			$('#deflist').datagrid('selectRow',indexs);
			$('#deflist').datagrid('beginEdit',indexs);
			lastIndex = indexs;
	}
	*/	
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
		    	showMsg(datas[0].MSG);
		    	loadGrid();
		    },
		    onLoadError:function(){
		    	//alert("error");
		    }
	});
		
	$('#form2').form({
		//定位到servlet位置的url
		url: '<%=request.getContextPath()%>/Svl_Auditedit',
		//当点击事件后触发的事件
	    onSubmit: function(data){
	    	//alert($('#modeid').combobox('getValue'));
	    }, 
	    //当点击事件成功提交后触发的事件
	    success:function(data){
	    	//alert("success");
	    	var datas = eval("("+data+")");
	    	$("#moduleedit").window('close');
	    	showMsg(datas[0].MSG);
	    	loadGrid();
	    },
	    onLoadError:function(){
	    	//alert("error");
	    }
	});
	
	$('#moduleedit').dialog({
			toolbar:[{
				text:'保存',
				iconCls:'icon-save',
				handler:function(){
					//alert('save')
					//$('#IDname').val($('#auditname').combobox('getText'));
					//alert($("#IDname").val());
					$('#form2').submit();
				}
			}]
	});
		
	function doToolbar(id){
		if(id=="cancel"){
			if(curmoduleid==""){
				alertMsg("请选择一条数据");
			}else{
				$('#deflist').datagrid('endEdit', lastIndex);
				$("#active").val("deletedefmod");
				//alert(curspid);
				$("#deletemoduleid").val(curmoduleid);
				curmoduleid="";
				ConfirmMsg("你确认要删除该记录吗?","$('#form1').submit();","");
			}
		}
		if(id=="tran"){
			if(curmoduleid==""){
				alertMsg("请选择一条数据");
			}else{
				rows = $('#deflist').datagrid('getSelected');
				$.ajax({
					type: "POST",
					url: '<%=request.getContextPath()%>/Svl_datatran',   //设置 SVL
					async:false,
					data: "tablename="+encodeURI(rows.相关视图)+"&ikeyname="+encodeURI(rows.主键名称),
					dataType: 'json',
					success:function(data){
				    	$('#deflist').datagrid('endEdit', lastIndex);
				    	if(data[0].MSG=="Success"){
		    				showMsg("数据移植成功");
		    			}else{
		    				showMsg("数据移植失败");
		    			}
					}
				});
			}
		}
	}
</script>