<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/icon.css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript"
		src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
	  <body class="easyui-layout">
		<div region="center" style="background: #fafafa; width: 100%;height:100%">
					<table id="holidaysList" name="holidaysList"></table>
		</div>
	 
	  <div id="holidaysedit" name="holidaysedit" title="调休日期编辑" class="easyui-window" style="width:500px;"  resizable=false closed=true modal=true>
	  	<form id="form1" name="form1" method="POST">
				<center>
					<table class="tablestyle" style="width:100%" cellpadding="4">
						<tr>
							<td>调休日期</td>
							<td align="left">
								<input class="easyui-datebox"  style="width:100%;" id="icleave" name="icleave">
							</td>
						</tr>
					</table>
					<input type="hidden" id="active" name="active"/>
					<input type="hidden" id="icid" name="icid"/>
				</center>
		</form>
  	  </div>
  	   </body>
  <script type="text/javascript">
  	var holidaysid="";
  	var rows = null;
  	$(document).ready(function(){
  		loadGrid();
  		//alertMsg("该模块用于定义");
  	});
  	
  	$('#holidaysedit').dialog({
			toolbar:[{
				text:'确定',
				iconCls:'icon-save',
				handler:function(){
					$("#form1").submit();
				}
			}]
	});
	
	
	$('#form1').form({
			//定位到servlet位置的url
			url:'<%=request.getContextPath()%>/Svl_Holidays', 
			//当点击事件后触发的事件
		    onSubmit: function(data){
		    	//alert();
		    }, 
		    //当点击事件成功提交后触发的事件
		    success:function(data){
		    	var datas = eval("("+data+")");
				$("#holidaysedit").window('close');
				showMsg(datas[0].MSG);
				loadGrid();
		    }
	});
		
  	function loadGrid(){
  		$('#holidaysList').datagrid({
				title:'假日调休列表',
				iconCls:'icon-edit',
				singleSelect:true,
				url: '<%=request.getContextPath()%>/Svl_Holidays',
				queryParams:{"active":"que"},
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
				toolbar: [{
					iconCls: 'icon-add',
					text:'添加',
					handler: function(){
						doToolbar("add");
					}
				},{
					text:'编辑',
					iconCls:'icon-edit',
					handler:function(){
						if(holidaysid!=""){
							doToolbar("edit");
						}else{
							alertMsg("请选择一条数据");
						}
					}
				},{
					text:'删除',
					iconCls:'icon-cancel',
					handler:function(){
						doToolbar("cancel");
					}
				}],
				columns:[[
					{field:'id',title:'id',width:100,hidden:true},
					{field:'节假日调休',title:'节假日调休',width:100},
					{field:'星期',title:'是否工作',width:100,formatter: function(value,row,index){
							if(value=="星期日"||value=="星期六"){
								return "是";
							}else{
								return "否";
							}
						}
					}
				]],
				onClickRow:function(rowIndex,rowData){
					holidaysid=rowData.Id;
				},
				onLoadSuccess:function(){
					
				}
			});
  	}
  	
  		
  	function doToolbar(iToolbar) {
  		if(iToolbar=="add"){
			$('#form1').form('clear');
  			$("#active").val("add");
  			$("#holidaysedit").window('open');
  		}
  		if(iToolbar=="edit"){
  			if(holidaysid!=""){
  				rows = $('#holidaysList').datagrid('getSelected');
  				$("#icid").val(holidaysid);
  				$("#icleave").datebox('setValue',rows.节假日调休);
  				$("#active").val("edit");
  				$("#holidaysedit").window('open');
  			}else{
  				alertMsg("请选择一条数据");
  			}
  		}
  		if(iToolbar=="cancel"){
  			if(holidaysid==""){
				alertMsg("请选择一条数据");
			}else{
				$("#active").val("del");
				$("#icid").val(holidaysid);
				ConfirmMsg("你确认要删除该记录吗?","$('#form1').submit();","");
			}
  		}

  	}
  </script>
  
</html>
