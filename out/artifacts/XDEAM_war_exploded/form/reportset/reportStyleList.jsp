<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>

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
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>

	  <body class="easyui-layout">
		<div region="center" style="background: #fafafa; width: 100%;height:100%">
					<table id="reportstyleList" name="reportstyleList"></table>
		</div>
	  </body>
	  <div id="reporstyleedit" name="reporstyleedit" title="报表样式编辑" class="easyui-window" style="width:500px;"  resizable=false closed=true modal=true>
	  	<form id="form1" name="form1" method="POST">
				<center>
					<table class="tablestyle" style="width:100%" cellpadding="4">
						<tr>
							<td>样式名</td>
							<td align="left">
								<input  style="width:100%;" maxlength=50 id="reportstylename" name="reportstylename">
							</td>
							<td>描述</td>
							<td align="left">
								<input  style="width:100%;" maxlength=200 id="describe" name="describe">
							</td>
						</tr>
					</table>
					<input type="hidden" id="active" name="active"/>
					<input type="hidden" id="reportstyleid" name="reportstyleid"/>
				</center>
		</form>
  	  </div>
  	  
  	 
  	   
  <script type="text/javascript">
  	var curreportstyleid="";
  	
  	var selectview="";
  	var rows = null;
  	$(document).ready(function(){
  		loadGrid();
  	});
  	
  	$('#reporstyleedit').dialog({

	});
	
	
	$('#form1').form({
			//定位到servlet位置的url
			url:'<%=request.getContextPath()%>/Svl_reportStyleList', 
			//当点击事件后触发的事件
		    onSubmit: function(data){
		    	//alert();
		    }, 
		    //当点击事件成功提交后触发的事件
		    success:function(data){
				$("#reporstyleedit").window('close');
				showMsg("保存成功");
				loadGrid();
		    }
	});
		
	

		
  	function loadGrid(){
  		$('#reportstyleList').datagrid({
				title:'报表样式列表',
				iconCls:'icon-edit',
				singleSelect:true,
				url: '<%=request.getContextPath()%>/Svl_reportStyleList',
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
					text:'基本信息维护',
					iconCls:'icon-edit',
					handler:function(){
						doToolbar("edit");
					}
				},{
					text:'编辑',
					iconCls:'icon-edit',
					handler:function(){
						if(curreportstyleid!=""){
							location.href="<%=request.getContextPath()%>/form/reportset/reportstyleinfo.jsp?reportstyleid="+curreportstyleid;
						}else{
							alertMsg("请选择一条数据");
						}
					}
				}],
				columns:[[
					{field:'模版id',title:'模版id',width:100,hidden:true},
					{field:'模版名称',title:'模版名称',width:100},
					{field:'描述',title:'描述',width:100},
					{field:'状态',title:'状态',width:100,formatter: function(value,row,index){
						if (value=="1"){
							return "启用";
						}
						if (value=="2"){
							return "禁用";
						}
					}},
					{field:'创建时间',title:'创建时间',width:100}
				]],
				onClickRow:function(rowIndex,rowData){
					curreportstyleid=rowData.模版id;
				},
				onLoadSuccess:function(){
					
				}
			});
  	}
  	
  		
  	function doToolbar(iToolbar) {
  		if(iToolbar=="add"){
			$('#form1').form('clear');
			$('#reporstyleedit').dialog({
				toolbar:[{
					text:'确定',
					iconCls:'icon-save',
					handler:function(){
						$("#form1").submit();
					}
				}]
			});
  			$("#active").val("add");
  			$("#reporstyleedit").window('open');

  		}
  		if(iToolbar=="edit"){
  			if(curreportstyleid!=""){
  				rows = $('#reportstyleList').datagrid('getSelected');
  				
  				$("#reportstylename").val(rows.模版名称);
  				$("#describe").val(rows.描述);
  				$("#reportstyleid").val(rows.模版id);
  				
  				$("#active").val("edit");
  				$('#reporstyleedit').dialog({
						toolbar:[{
							text:'确定',
							iconCls:'icon-save',
							handler:function(){
								$("#form1").submit();
							}
						},{
								text:'禁用',
								iconCls:'icon-cancel',
								handler:function(){
									doToolbar("cancel");
								}
							},{
								text:'启用',
								iconCls:'icon-ok',
								handler:function(){
									doToolbar("qiyong");
								}
							}]
				});
  				$("#reporstyleedit").window('open');
  			}else{
  				alertMsg("请选择一条数据");
  			}
  		}
  		if(iToolbar=="cancel"){
  			if(curreportstyleid!=""){
  				rows = $('#reportstyleList').datagrid('getSelected');
				$.ajax({
					type: "POST",
					url: '<%=request.getContextPath()%>/Svl_reportStyleList',   //设置 SVL
					async:false,
					data: "active=cancel&reportstyleid=" + rows.模版id,  // 设置模式，一般为view ，主关键字值		
					dataType: 'json',
					success: function(datas){
						$("#reporstyleedit").window('close');
						loadGrid();
						showMsg("该报表样式已禁用");
					}
				});
  			}else{
  				alertMsg("请选择一条数据");
  			}
  		}
  		if(iToolbar=="qiyong"){
  			if(curreportstyleid!=""){
  				rows = $('#reportstyleList').datagrid('getSelected');
				$.ajax({
					type: "POST",
					url: '<%=request.getContextPath()%>/Svl_reportStyleList',   //设置 SVL
					async:false,
					data: "active=qiyong&reportstyleid=" + rows.模版id,  // 设置模式，一般为view ，主关键字值		
					dataType: 'json',
					success: function(datas){
						$("#reporstyleedit").window('close');
						loadGrid();
						showMsg("该报表样式已启用");
					}
				});
  			}else{
  				alertMsg("请选择一条数据");
  			}
  		}

  	}
  </script>
  
</html>
