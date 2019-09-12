<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.base.common.tools.PublicTools"%>
<%@ page import="com.pantech.src.develop.Logs.*"%>
<%@ page import="com.pantech.base.common.tools.*"%>
<%@ page import="com.pantech.src.develop.store.user.*"%>
<%@ page import="com.pantech.src.develop.manage.workremind.WorkRemind"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.*"%>
<%@ page import="com.pantech.base.common.db.DBSource"%>

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
			<table id="reportauthList" name="reportauthList"></table>
		</div>
	  </body>
	  <div id="reporauthedit" name="reporauthedit" title="权限关联编辑" class="easyui-window" style="width:700px;"  resizable=false closed=true modal=true>
	  	<form id="form1" name="form1" method="POST">
				<center>
					<table class="tablestyle" style="width:100%" cellpadding="4">
						<tr>
							<td>报表名称</td>
							<td align="left">
								<select
									class="easyui-combobox" name="reportid" id="reportid" style="width: 150px;">
								</select>
							</td>
							<td>访问对象</td>
							<td align="left">
								<select
									class="easyui-combobox" name="user" id="user" style="width: 150px;">
								</select>
							</td>
						</tr>
					</table>
					<input type="hidden" id="active" name="active"/>
					<input type="hidden" id="auth" name="auth"/>
					<input type="hidden" id="reportauthid" name="reportauthid"/>
				</center>
		</form>
  	  </div>
  	  
  	 
  	   
  <script type="text/javascript">
  	var curreportauthid="";
  	var reportid=<%=PublicTools.getComboboxOptions("V_自定义报表主表","报表id","报表名称","where 状态='1'",request)%>;
  	var selectview="";
  	var rows = null;
  	var role=<%=PublicTools.getComboboxOptions("dbo.V_USER_AUTH","distinct authcode","authdesc","where 1=1 union select 'all' as authcode,'全部' as authdesc order by authcode desc",request)%>;
  	$(document).ready(function(){
  		loadGrid();
  	});
  	
	$('#reportid').combobox({
		data:reportid,
		valueField:'comboValue',
		textField:'comboName',
		onSelect:function(record){
			//getviewcollist(record.comboValue);
		}
	});
	
	$('#user').combobox({
		data:role,
		valueField:'comboValue',
		textField:'comboName',
		multiple:"multiple",
		onSelect:function(record){
			//getviewcollist(record.comboValue);
		}
	});
	
	
	function getviewcollist(value){
		$.ajax({
			type: "POST",
			url: '<%=request.getContextPath()%>/Svl_reportauthList',   //设置 SVL
			async:false,
			data: "active=viewcol&viewname=" + value,  // 设置模式，一般为view ，主关键字值		
			dataType: 'json',
			success: function(datas){
				$("#field").combobox({
					data:datas,
					valueField:'comboValue',
					textField:'comboName',
					multiple:"multiple"
				});
			}
		});
	}
	var auth="";
  	$('#reporauthedit').dialog({
			toolbar:[{
				text:'确定',
				iconCls:'icon-save',
				handler:function(){
					auth=$('#user').combobox("getValues");
					if(auth!=""){
						auth=auth.toString().replace(/,/g,";");
						$('#auth').val(";"+auth+";");
					}else{
						$('#auth').val("");
					}
					$("#form1").submit();
				}
			}]
	});
	
	
	$('#form1').form({
			//定位到servlet位置的url
			url:'<%=request.getContextPath()%>/Svl_reportauthList', 
			//当点击事件后触发的事件
		    onSubmit: function(data){
		    	//alert();
		    }, 
		    //当点击事件成功提交后触发的事件
		    success:function(data){
				$("#reporauthedit").window('close');
				showMsg("保存成功");
				loadGrid();
		    }
	});
		
	

		
  	function loadGrid(){
  		$('#reportauthList').datagrid({
				title:'定义权限列表',
				iconCls:'icon-edit',
				singleSelect:true,
				url: '<%=request.getContextPath()%>/Svl_reportauthList',
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
						doToolbar("edit");
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
				}],
				columns:[[
					{field:'权限id',title:'权限id',width:100,hidden:true},
					{field:'报表id',title:'报表id',width:100,hidden:true},
					{field:'报表名称',title:'报表名称',width:100},
					{field:'访问对象',title:'访问对象',width:100,
						formatter:function (value,row,index){
							value=value.substring(1);
							value=value.substring(0,value.length-1);
							return value;
						}
					},
					{field:'状态',title:'状态',width:100,formatter: function(value,row,index){
						if (value=="1"){
							return "启用";
						}
						if (value=="2"){
							return "禁用";
						}
					}}
				]],
				onClickRow:function(rowIndex,rowData){
					curreportauthid=rowData.权限id;
				},
				onLoadSuccess:function(){
					
				}
			});
  	}
  	
  		
  	function doToolbar(iToolbar) {
  		if(iToolbar=="add"){
			$('#form1').form('clear');
			$('#field').combobox('loadData','');
  			$("#active").val("add");
  			$("#reporauthedit").window('open');

  		}
  		if(iToolbar=="edit"){
  			if(curreportauthid!=""){
  				rows = $('#reportauthList').datagrid('getSelected');
  				
  				$("#reportid").combobox('select',rows.报表id);
  				//$("#user").combobox('select',rows.访问对象);
				var jihuo=rows.访问对象.substring(1);
				jihuo=jihuo.substring(0,jihuo.length-1);
				//actnum=jihuo;
				$('#user').combobox('setValues', jihuo.split(";"));
  				$("#field").combobox('select',rows.字段id);
  				$("#reportauthid").val(rows.权限id);
  				$("#active").val("edit");
  				
  				$("#reporauthedit").window('open');
  			}else{
  				alertMsg("请选择一条数据");
  			}
  		}
  		if(iToolbar=="cancel"){
  			if(curreportauthid!=""){
  				rows = $('#reportauthList').datagrid('getSelected');
				$.ajax({
					type: "POST",
					url: '<%=request.getContextPath()%>/Svl_reportauthList',   //设置 SVL
					async:false,
					data: "active=cancel&reportauthid=" + rows.权限id,  // 设置模式，一般为view ，主关键字值		
					dataType: 'json',
					success: function(datas){
						loadGrid();
						showMsg("该报表权限已禁用");
					}
				});
  			}else{
  				alertMsg("请选择一条数据");
  			}
  		}
  		if(iToolbar=="qiyong"){
  			if(curreportauthid!=""){
  				rows = $('#reportauthList').datagrid('getSelected');
				$.ajax({
					type: "POST",
					url: '<%=request.getContextPath()%>/Svl_reportauthList',   //设置 SVL
					async:false,
					data: "active=qiyong&reportauthid=" + rows.权限id,  // 设置模式，一般为view ，主关键字值		
					dataType: 'json',
					success: function(datas){
						loadGrid();
						showMsg("该报表权限已启用");
					}
				});
  			}else{
  				alertMsg("请选择一条数据");
  			}
  		}

  	}
  </script>
  
</html>
