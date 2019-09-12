<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/css/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/css/themes/icon.css">
<script language="JavaScript"
	src="<%=request.getContextPath()%>/script/VerifyForm.js"></script>
<script language="JavaScript"
	src="<%=request.getContextPath()%>/script/meizzDate.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
<title>短信规则设定</title>
</head>
<body class="easyui-layout">
	<div region="center" style="background: #fafafa; width: 100%;height:100%">
		<table id="rulesList" name="rulesList"></table>
	</div>

<div id="rulesedit" name="rulesedit" title="短信规则编辑" class="easyui-window" style="width:800px;"  resizable=false closed=true modal=true>
	<form id="form1" name="form1" method="POST">
			<center>
				<table class="tablestyle" style="width:100%" cellpadding="4">
					<tr>
						<td align="left" width="60px">事务描述：</td>
						<td align="left"><input id="icdescribe" name="icdescribe"  style="width:100%" ></input></td>
						<td align="left" width="60px">生效时间：</td>
						<td align="left"><input class="easyui-datebox" id="ictime" name="ictime"  style="width:100%" required="required"></input></td>
						<td align="left" width="60px">事务类型：</td>
						<td align="left">
							<select class="easyui-combobox" id="ictype" name="ictype"  style="width:100%" required="required">
								<option value="gzr">工作日</option>
								<option value="zrr">自然日</option>
							</select>
						</td>
					</tr>
					<tr>
						<td align="left" width="60px">周期：</td>
						<td align="left">每
							<select
								class="easyui-combobox" name="zqtype1" id="zqtype1" style="width: 50px;" required="required">
								<option value="yue">月</option>
								<option value="ge">隔</option>
							</select>
							<input id="zqtime" name="zqtime" class="easyui-numberspinner" style="width:120px;" required="required"></input>
							<select
								class="easyui-combobox" name="zqtype2" id="zqtype2" style="width: 50px;" required="required">
								<option value="tian">天</option>
								<option value="fen">分钟</option>
							</select>
						</td>
						<td align="left" width="60px">提醒时间：</td>
						<td align="left"><input class="easyui-timespinner" id="icremtime" name="icremtime"  style="width:100%" required="required"></input></td>
						<td align="left" width="60px">任务期限：</td>
						<td align="left"><input id="icdeadline" name="icdeadline" class="easyui-numberspinner"  style="width:100%" required="required"></input></td>
					</tr>
					<tr>
						<td align="left" width="60px">提醒对象：</td>
						<td align="left" colspan="5">
							<select
								class="easyui-combobox" name="type" id="type" style="width: 50px;" required="required">
								<option value="qb">全部</option>
								<option value="js">角色</option>
								<option value="bm">部门</option>
								<option value="ry">人员</option>
							</select>
							
							<select
								class="easyui-combogrid" name="name" id="name" style="width: 230px;" required="required">
							</select>
						</td>
					</tr>
					<tr>
						<td align="left" width="60px">短信内容：</td>
						<td align="left" colspan="5"><textarea id="iccontent" name="iccontent" onfocus="focues();" rows="5" style="width:100%" required="required"></textarea></td>
					</tr>
				</table>
			</center>
			<input type="hidden" id="active" name="active"/>
			<input type="hidden" id="icname" name="icname"/>
			<input type="hidden" id="icuuid" name="icuuid"/>
	</form>
</div>
</body>
<script type="text/javascript">
	var lastIndex=-1;
	var uuid="";
	//var jsonvalue;
	var jsonlist=new Array();
  		
	$(document).ready(function(){	
  		loadGrid();
  	});
  	
  	if (!Array.prototype.indexOf) Array.prototype.indexOf = function(item, i) {  
	  i || (i = 0);  
		  var length = this.length;  
		  if (i < 0) i = length + i;  
		  for (; i < length; i++)  
		    if (this[i] === item) return i;  
		  return -1;  
	}; 
  	$('#form1').form({
			//定位到servlet位置的url
			url: '<%=request.getContextPath()%>/Svl_Rules',
			//当点击事件后触发的事件
		    onSubmit: function(data){
		    	//alert($('#modeid').combobox('getValue'));
		    	return $(this).form('validate');
		    }, 
		    //当点击事件成功提交后触发的事件
		    success:function(data){
		    	//alert("success");
		    	var datas = eval("("+data+")");
		    	//alert(modeid);
		    	$("#rulesedit").window("close");
		    	showMsg(datas[0].MSG);
		    	loadGrid();
		    },
		    onLoadError:function(){
		    	//alert("error");
		    }
	});
		
 	$('#rulesedit').dialog({
		toolbar:[{
			text:'保存',
			iconCls:'icon-save',
			handler:function(){
				//alert($('#name').combobox('getValues'));
				//alert(jsonlist.toString());
				$('#icname').val(jsonlist.toString());
				$('#form1').submit();
			}
		}]
	});
		
	$('#icdeadline').numberspinner({
	    min: 0,
	    max: 50
	});
					
	$('#zqtype1').combobox({
		onSelect:function(record){
				
				$('#zqtime').numberspinner("enable");
				if($('#zqtype1').combobox('getValue')=="yue"){
					$("#zqtype2").combobox('disable');
					$("#zqtype2").combobox('setText','日');
					$('#zqtime').numberspinner({
					    min: 1,
					    max: 31
					});
				}else if($('#zqtype1').combobox('getValue')=="ge"){
					$("#zqtype2").combobox('enable');
					$('#zqtime').numberspinner({
					    min: 1,
					    max: 1440
					});
				}
			}
	});
		
	$('#type').combobox({
		onSelect:function(record){
				if($('#type').combobox('getValue')=="qb"){
					jsonlist=[];
					$("#name").combogrid('setValue','all');
					$("#name").combogrid('disable');
				}else if($('#type').combobox('getValue')=="ry"){
					jsonlist=[];
					$("#name").combogrid('enable');
					$("#name").combogrid('clear');
					$('#name').combogrid({
						url: '<%=request.getContextPath()%>/Svl_Rules',
						idField:'员工编号',
            			textField:'姓名',
						queryParams:{"active":"query"},
						width: '230px',
						nowrap: false,
						fit:true,
						showFooter:true,
						striped:true, //隔行变色
						pageSize:10,
						singleSelect:true,
						pageNumber:1,
						rownumbers:true,
						fitColumns: true,
						mode:'remote',
						multiple: true,	
						columns:[[
							{field:'ck',checkbox:true},
							{field:'员工编号',title:'员工编号',width:100,hidden:true},
							{field:'姓名',title:'姓名',width:100}
						]],
						onSelect:function(rowIndex, rowData){
							//alert(rowData.员工编号);
							jsonlist.push(rowData.员工编号);
						},
						onUnselect:function(rowIndex, rowData){
							jsonlist.splice(jsonlist.indexOf(rowData.员工编号),1);
						},
						onSelectAll:function(rows){
							
							for(var i=0;i<rows.length;i++){
								
								jsonlist.push(rows[i].员工编号);	
							}
							//alert(jsonlist);
						},
						onUnselectAll:function(rows){
							jsonlist=[];
						}
					});
				}else if($('#type').combobox('getValue')=="js"){
					jsonlist=[];
					$("#name").combogrid('enable');
					$("#name").combogrid('clear');
					$('#name').combogrid({
						url: '<%=request.getContextPath()%>/Svl_Rules',
						idField:'角色编号',
            			textField:'角色名称',
						queryParams:{"active":"quejs"},
						width: '230px',
						nowrap: false,
						fit:true,
						showFooter:true,
						striped:true, //隔行变色
						pageSize:10,
						singleSelect:true,
						pageNumber:1,
						rownumbers:true,
						fitColumns: true,
						multiple: true,	
						mode:'remote',
						columns:[[
							{field:'ck',checkbox:true},
							{field:'角色编号',title:'角色编号',width:100,hidden:true},
							{field:'角色名称',title:'角色名称',width:100}
						]],
						onSelect:function(rowIndex, rowData){
							jsonlist.push(rowData.角色编号);
						},
						onUnselect:function(rowIndex, rowData){
							jsonlist.splice(jsonlist.indexOf(rowData.角色编号),1);
						},
						onSelectAll:function(rows){
							
							for(var i=0;i<rows.length;i++){
								
								jsonlist.push(rows[i].角色编号);	
							}
							//alert(jsonlist);
						},
						onUnselectAll:function(rows){
							jsonlist=[];
						}
					});
				}else if($('#type').combobox('getValue')=="bm"){
					jsonlist=[];
					$("#name").combogrid('enable');
					$("#name").combogrid('clear');
					$('#name').combogrid({
						url: '<%=request.getContextPath()%>/Svl_Rules',
						idField:'部门编号',
            			textField:'部门名称',
						queryParams:{"active":"quebm"},
						width: '230px',
						nowrap: false,
						fit:true,
						showFooter:true,
						striped:true, //隔行变色
						pageSize:10,
						singleSelect:true,
						pageNumber:1,
						rownumbers:true,
						fitColumns: true,
						multiple: true,	
						mode:'remote',
						columns:[[
							{field:'ck',checkbox:true},
							{field:'部门编号',title:'部门编号',width:100,hidden:true},
							{field:'部门名称',title:'部门名称',width:100}
						]],
						onSelect:function(rowIndex, rowData){
							jsonlist.push(rowData.部门编号);
							//alert(rowData.角色编号);
							//uuid=rowData.规则Id;
						},
						onUnselect:function(rowIndex, rowData){
							jsonlist.splice(jsonlist.indexOf(rowData.部门编号),1);
							//alert(rowData.角色编号);
							//alert(record);
							//uuid=rowData.规则Id;
						},
						onSelectAll:function(rows){
							
							for(var i=0;i<rows.length;i++){
								
								jsonlist.push(rows[i].部门编号);	
							}
							//alert(jsonlist);
						},
						onUnselectAll:function(rows){
							jsonlist=[];
						}
					});
				}
			}
	});
	
  	function loadGrid(){
  		$('#rulesList').datagrid({
				title:'短信规则列表',
				iconCls:'icon-edit',
				singleSelect:true,
				url: '<%=request.getContextPath()%>/Svl_Rules',
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
							doToolbar("cancel");
							lastIndex=-1;
						}
					}
				],
				columns:[[
					{field:'事务描述',title:'事务描述',width:100},
					{field:'生效时间',title:'生效时间',width:100},
					{field:'提醒时间',title:'提醒时间',width:100},
					{field:'事务类型',title:'事务类型',width:100,formatter: function(value,row,index){
							if(value=="gzr"){
								return "工作日";
							}
							if(value=="zrr"){
								return "自然日";
							}
						}
					},
					{field:'周期',title:'周期',width:100,formatter: function(value,row,index){
						var values=value.split(";");
						var str="每";
						for(var i=0;i<values.length;i++){
							if(values[i]=="yue"){
								str+="月";
							}
							else if(values[i]=="ge"){
								str+="隔";
							}
							else if(values[i]=="tian"){
								str+="天";
							}
							else if(values[i]=="fen"){
								str+="分钟";
							}
							else if(values[i]=="hao"){
								str+="日";
							}
							else{
								str+=values[i];
							}
						}
						return str;
					}},
					{field:'任务期限',title:'任务期限',width:100},
					{field:'对象类型',title:'对象类型',width:100,formatter: function(value,row,index){
							if(value=="js"){
								return "角色";
							}
							if(value=="ry"){
								return "人员";
							}
							if(value=="bm"){
								return "部门";
							}
							if(value=="qb"){
								return "全部";
							}
					}},
					{field:'提醒对象',title:'提醒对象',width:100,formatter: function(value,row,index){
							if(row.对象类型=="qb"){
								return "所有员工";
							}
							return value;
					}},
					{field:'短信内容',title:'短信内容',width:100}
				]],
				onClickRow:function(rowIndex,rowData){
					uuid=rowData.规则Id;
				},

				onLoadSuccess:function(){
					
				}
			});
  	}
  	
  	function doToolbar(id){
		if(id=="add"){
			$('#form1').form('clear');
			$('#zqtime').numberspinner("disable");
			$("#type").combobox('clear');
			$("#name").combogrid('clear');
			$("#name").combogrid('disable');
			
			$("#active").val("add");
			$("#rulesedit").window("open");
		}
		if(id=="edit"){
			if(uuid==""){
				alertMsg("请选择一条数据");
			}else{
				rows = $('#rulesList').datagrid('getSelected');
				$("#active").val("edit");
  				$("#icuuid").val(rows.规则Id);
  				$("#icremtime").timespinner('setValue', rows.提醒时间); 
  				$("#type").combobox('select',rows.对象类型);
  				if(rows.对象类型=="qb"){
  					$("#name").combogrid('setValue','all');
  				}else{
  					$("#name").combogrid('setValues',rows.提醒对象.split(","));
  				}
  				//var zq=rows.周期.split(";");
  				//alert(zq.length);
  				if(rows.周期!=""){
	  				$("#zqtype1").combobox('select', rows.周期.split(";")[0]); 
	  				$("#zqtime").numberspinner('setValue', rows.周期.split(";")[1]); 
	  				if(rows.周期.split(";")[0]=="ge"){
	  					$("#zqtype2").combobox('setValue', rows.周期.split(";")[2]); 
	  				}
				}
  				$("#ictime").datebox('setValue',rows.生效时间);
  				$("#icdescribe").val(rows.事务描述);
  				$("#iccontent").val(rows.短信内容);
  				$("#icdeadline").numberspinner('setValue',rows.任务期限);
  				$("#icremperson").val(rows.提醒对象);
  				$("#ictype").val(rows.事务类型);
  				$("#rulesedit").window("open");
			}
		}
		if(id=="cancel"){
			if(uuid==""){
				alertMsg("请选择一条数据");
			}else{
				$("#active").val("del");
				$("#icuuid").val(uuid);
				ConfirmMsg("你确认要删除该记录吗?","$('#form1').submit();","");
			}
		}
	}
		
  	function showPerson(){
  		$("#showperson").window("open");
  	}
  	
	function OpenUserList(){
		var val = showModalDialog('<%=request.getContextPath()%>/form/system/DeptTree.jsp?returnText=1&selectMode=1&includeUser=1','','');
		if(val == "" || val == undefined){
			document.getElementById("icremperson").value = '';
			return;
		}
		var arr = val.split(",");
		var iName="";
		var iCode="";
		for(var i=0;i<arr.length;i++){
			var arr1=arr[i].split(";");
			iName=iName+arr1[1]+",";
			iCode=iCode+arr1[0]+",";
		}
		iName=iName.substring(0,iName.length-1);
		iCode=iCode.substring(0,iCode.length-1);
		document.getElementById("icremperson").value = iCode;
	}
	
	function focues(){
		//alert();
		if($("#iccontent").val()==""){
			$("#iccontent").val("请在"+$("#zqtime").numberspinner('getValue')+"日前完成"+$("#icdescribe").val());
		}
	}
</script>
</html>