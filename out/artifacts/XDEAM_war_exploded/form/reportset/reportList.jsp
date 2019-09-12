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

	  <body >
	  <div class="easyui-layout" style="width:100%;height:100%">
		<div region="center" style="background: #fafafa; width: 100%;height:100%">
					<table id="reportList" name="reportList"></table>
		</div>
	   </div>
	  <div id="reporedit" name="reporedit" title="数据源编辑" class="easyui-window" style="width:500px;"  resizable=false closed=true modal=true>
	  	<form id="form1" name="form1" method="POST">
				<center>
					<table class="tablestyle" style="width:100%" cellpadding="4">
						<tr>
							<td>报表名</td>
							<td align="left">
								<input  style="width:100%;" maxlength=50 id="reportname" name="reportname">
							</td>
							<td>描述</td>
							<td align="left">
								<input  style="width:100%;" maxlength=200 id="describe" name="describe">
							</td>
						</tr>
					</table>
					<input type="hidden" id="active" name="active"/>
					<input type="hidden" id="reportid" name="reportid"/>
				</center>

		</form>
  	  </div>
  	  
  	  <div id="objreport" name="objreport" title="视图选择列表" class="easyui-window" style="width:800px;height:500px"  resizable=false closed=true modal=true>
				<table id="objreportList" name="objreportList"></table>
  	  </div>
  	 
  	  <div id="showview" name="showview" title="可选视图列表" class="easyui-window" style="width:600px;height:300px"  resizable=false closed=true modal=true>
			<table id="viewList" name="viewList"></table>
  	  </div>
  	   
  	  <div id="objreporedit" name="objreporedit" title="视图选择编辑" class="easyui-window" style="width:500px;"  resizable=false closed=true modal=true>
	  	<form id="form2" name="form2" method="POST">
			<center>
				<table class="tablestyle" style="width:100%" cellpadding="4">
					<tr>
						<td>视图名</td>
						<td align="left">
							<input  style="width:100%;" maxlength=50 id="viewname" name="viewname" onclick="showviewlist()">
						</td>
						<td>类型</td>
						<td align="left">
							<select
								class="easyui-combobox" name="type" id="type" style="width: 150px;">
								<option value="001">主视图</option>
								<option value="002">辅视图</option>
							</select>
						</td>
					</tr>
					<tr>
						<td>描述</td>
						<td align="left">
							<input  style="width:100%;" maxlength=200 id="describes" name="describes">
						</td>
						<td></td>
						<td></td>
					</tr>
				</table>
				<input type="hidden" id="objactive" name="objactive"/>
				<input type="hidden" id="objid" name="objid"/>
				<input type="hidden" id="objreportid" name="objreportid"/>
			</center>
		</form>
  	  </div>
  	  </body>
  <script type="text/javascript">
  	var curreportid="";
  	var curobjid="";
  	var selectview="";
  	var rows = null;
  	$(document).ready(function(){
  		
  		loadGrid();
  	});
  	

	$('#reporedit').dialog({

	});
	
	$('#objreporedit').dialog({
			toolbar:[{
				text:'确定',
				iconCls:'icon-save',
				handler:function(){
					$("#form2").submit();
					curobjid="";
				}
			}]
	});
	
	$('#form1').form({
			//定位到servlet位置的url
			url:'<%=request.getContextPath()%>/Svl_reportList', 
			//当点击事件后触发的事件
		    onSubmit: function(data){
		    	//alert();
		    }, 
		    //当点击事件成功提交后触发的事件
		    success:function(data){
				//alert();
				var json = eval("("+data+")");
				$("#reporedit").window('close');
				curreportid=json[0].ID;
				if($("#active").val()=="add"){
					objGrid();
				}else{
					curreportid="";
				}
				loadGrid();
		    }
	});
		
	$('#form2').form({
			//定位到servlet位置的url
			url:'<%=request.getContextPath()%>/Svl_objreportList', 
			//当点击事件后触发的事件
		    onSubmit: function(data){
		    	//alert();
		    }, 
		    //当点击事件成功提交后触发的事件
		    success:function(data){
				$("#objreporedit").window('close');
				$('#objreportList').datagrid("reload");
		    }
	});
	
 	$('#viewList').datagrid({
			iconCls:'icon-edit',
			singleSelect:true,
			url: '<%=request.getContextPath()%>/Svl_objreportList',
			queryParams:{"objactive":"queviewlist"},
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
				iconCls:'icon-ok',
				text:'确认',
				handler: function(){
					if(selectview!=""){
						$("#viewname").val(selectview);
						$("#showview").window('close');
					}else{
						alertMsg("请选择一条记录");
					}
					selectview="";
				}
			}],
			columns:[[
				{field:'视图名称',title:'视图名称',width:100}
			]],
			onClickRow:function(rowIndex,rowData){
				selectview=rowData.视图名称;
			},
			onLoadSuccess:function(){
			}
	});
		
  	function loadGrid(){
  		$('#reportList').datagrid({
				title:'数据源列表',
				iconCls:'icon-edit',
				singleSelect:true,
				url: '<%=request.getContextPath()%>/Svl_reportList',
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
						if(curreportid!=""){
							location.href="<%=request.getContextPath()%>/form/reportset/reportinfo.jsp?reportid="+curreportid;
						}else{
							alertMsg("请选择一条数据");
						}
					}
				}],
				columns:[[
					{field:'报表id',title:'报表id',width:100,hidden:true},
					{field:'报表名称',title:'报表名称',width:100},
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
					curreportid=rowData.报表id;
				},
				onLoadSuccess:function(){
					
				}
			});
  	}
  	
  	function objGrid(){
  		$('#objreportList').datagrid({
				iconCls:'icon-edit',
				singleSelect:true,
				url: '<%=request.getContextPath()%>/Svl_objreportList',
				queryParams:{"objactive":"que","objreportid":curreportid},
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
						doToolbar("objadd");
					}
				},{
					text:'修改',
					iconCls:'icon-edit',
					handler:function(){
						doToolbar("objedit");
					}
				},{
					text:'删除',
					iconCls:'icon-cancel',
					handler:function(){
						doToolbar("objcancel");
					}
				}],
				columns:[[
					{field:'视图名',title:'视图名',width:100},
					{field:'类型',title:'类型',width:100,formatter: function(value,row,index){
						if (value=="001"){
							return "主视图";
						}
						if (value=="002"){
							return "辅视图";
						}

					}},
					{field:'描述',title:'描述',width:100}
				]],
				onClickRow:function(rowIndex,rowData){
					curobjid=rowData.对象id;
				},
				onLoadSuccess:function(){
					$("#objreport").window('open');
				}
			});
  	}
  	
  	function showviewlist(){
  		$("#showview").window('open');
  	}
  	
	function objcancel(){
			//alert();
		rows = $('#objreportList').datagrid('getSelected');
		$.ajax({
			type: "POST",
			url: '<%=request.getContextPath()%>/Svl_objreportList',   //设置 SVL
			async:false,
			data: "objactive=cancel&objid=" + rows.对象id,  // 设置模式，一般为view ，主关键字值		
			dataType: 'json',
			success: function(datas){
				$('#objreportList').datagrid("reload");
				showMsg("删除成功");
			}
		});
	}
  		
  	function doToolbar(iToolbar) {
  		if(iToolbar=="add"){
			$('#form1').form('clear');
		  	$('#reporedit').dialog({
				toolbar:[{
					text:'确定',
					iconCls:'icon-save',
					handler:function(){
						$("#form1").submit();
					}
				}]
			});
  			$("#active").val("add");
  			$("#reporedit").window('open');

  		}
  		if(iToolbar=="edit"){
  			if(curreportid!=""){
  				rows = $('#reportList').datagrid('getSelected');
  				
  				$("#reportname").val(rows.报表名称);
  				$("#describe").val(rows.描述);
  				$("#reportid").val(rows.报表id);
  				
  				$("#active").val("edit");
  				$('#reporedit').dialog({
						toolbar:[{
							text:'确定',
							iconCls:'icon-save',
							handler:function(){
								$("#form1").submit();
							}
						},{
								text:'数据源管理',
								iconCls:'icon-edit',
								handler:function(){
									doToolbar("objlist");
								}
							},{
								text:'启用',
								iconCls:'icon-ok',
								handler:function(){
									doToolbar("qiyong");
								}
							},{
								text:'禁用',
								iconCls:'icon-cancel',
								handler:function(){
									doToolbar("cancel");
								}
							}]
				});
  				$("#reporedit").window('open');
  			}else{
  				alertMsg("请选择一条数据");
  			}
  		}
  		if(iToolbar=="cancel"){
  			if(curreportid!=""){
  				rows = $('#reportList').datagrid('getSelected');
				$.ajax({
					type: "POST",
					url: '<%=request.getContextPath()%>/Svl_reportList',   //设置 SVL
					async:false,
					data: "active=cancel&reportid=" + rows.报表id,  // 设置模式，一般为view ，主关键字值		
					dataType: 'json',
					success: function(datas){
						$("#reporedit").window('close');
						loadGrid();
						showMsg("该报表已禁用");
					}
				});
  			}else{
  				alertMsg("请选择一条数据");
  			}
  		}
  		if(iToolbar=="qiyong"){
  			if(curreportid!=""){
  				rows = $('#reportList').datagrid('getSelected');
				$.ajax({
					type: "POST",
					url: '<%=request.getContextPath()%>/Svl_reportList',   //设置 SVL
					async:false,
					data: "active=qiyong&reportid=" + rows.报表id,  // 设置模式，一般为view ，主关键字值		
					dataType: 'json',
					success: function(datas){
						$("#reporedit").window('close');
						loadGrid();
						showMsg("该报表已启用");
					}
				});
  			}else{
  				alertMsg("请选择一条数据");
  			}
  		}
  		if(iToolbar=="objlist"){
  			if(curreportid!=""){
  				objGrid();
  				curobjid="";
  			}else{
  				alertMsg("请选择一条数据");
  			}
  		}
  		if(iToolbar=="objadd"){
  			$('#form2').form('clear');
  			$('#type').combobox('clear');
  			$("#objreportid").val(curreportid);
  			$("#objactive").val("add");
  			$("#objreporedit").window('open');
  		}
  		if(iToolbar=="objedit"){
  			if(curobjid!=""){
	  			
	  			rows = $('#objreportList').datagrid('getSelected');
	  			
	  			$("#viewname").val(rows.视图名);
	  			$("#type").combobox('setValue', rows.类型);
  				$("#describes").val(rows.描述);
  				$("#objid").val(rows.对象id);
	  			$("#objreportid").val(rows.报表id);
	  			
	  			$("#objactive").val("edit");
	  			$("#objreporedit").window('open');
  			}else{
  				alertMsg("请选择一条数据");
  			}
  		}
  		if(iToolbar=="objcancel"){
  			if(curreportid!=""){
				ConfirmMsg("你确认要删除该记录吗?","objcancel()","");
				//alert();
  			}else{
  				alertMsg("请选择一条数据");
  			}
  		}
  		

  	}
  </script>
  
</html>
