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
  
  <body  class="easyui-layout">
		<div region="center" class="easyui-layout" style="background: #fafafa; width: 100%;">
			<div region="north" align="left"
				style="overflow: hidden; height: 240px; width: 800px;">
			<table  id="columnsconlist" name="columnsconlist"></table>
			</div>
			<div region="center" align="left" title="定义其他信息"
				style="overflow: hidden;">
				<table  id="columnsinfolist" name="columnsinfolist"></table>
			</div>
		</div>
  </body>
    <div id="columnsconedit" name="columnsconedit" title="视图关联编辑" class="easyui-window" style="width:500px;"  resizable=false closed=true modal=true>
    	<form id="form1" name="form1" method="POST">
			<center>
				<table class="tablestyle" style="width:100%" cellpadding="4">
					<tr>
						<td>视图1</td>
						<td align="left">
							<select
								class="easyui-combobox" name="view1" id="view1" style="width: 150px;">
							</select>
						</td>
						<td>列1</td>
						<td align="left">
							<select
								class="easyui-combobox" name="col1" id="col1" style="width: 150px;">
							</select>
						</td>
					</tr>
					<tr>
						<td>关系</td>
						<td align="left" colspan=3>
							<select
								class="easyui-combobox" name="gx" id="gx" style="width: 350px;">
								<option value="right join">左连接</option>
								<option value="inner join">内连接</option>
								<option value="left join">右连接</option>
							</select>
						</td>
					</tr>
					<tr>
						<td>视图2</td>
						<td align="left">
							<select
								class="easyui-combobox" name="view2" id="view2" style="width: 150px;">
							</select>
						</td>
						<td>列2</td>
						<td align="left">
							<select
								class="easyui-combobox" name="col2" id="col2" style="width: 150px;">
							</select>
						</td>
					</tr>
				</table>
				<input type="hidden" id="active" name="active"/>
				<input type="hidden" id="columnid" name="columnid"/>
				<input type="hidden" id="reportid" name="reportid"/>
			</center>
		</form>
  </div>
    <div id="columnconfedit" name="columnconfedit" title="视图详情编辑" class="easyui-window" style="width:600px;"  resizable=false closed=true modal=true>
    	<form id="form2" name="form2" method="POST">
				<div class="easyui-layout" style="width:100%;height:380px">
					<div region="north" align="left" style="overflow: hidden; height: 140px;">
						<center>
							<table class="tablestyle" style="width:100%" cellpadding="4">
								<tr>
									<td>列名</td>
									<td id="colname" name="colname" align="left">
										
									</td>
									<td>是否筛选</td>
									<td align="left">
										<input type="radio" style="width:15px" id="screen" name="screen" value="1" onclick="fudefault(this.value)">是
										<input type="radio" style="width:15px" id="screen" name="screen" value="0" onclick="fudefault(this.value)">否
										&nbsp;&nbsp;默认:<input id="defaluts" name="defaluts">
										<input type="hidden" id="icscreen" name="icscreen">
									</td>
								</tr>
								<tr>
									<td>排序方式</td>
									<td align="left">
										<select
											class="easyui-combobox" name="sort" id="sort" style="width: 120px;">
											<option value="001">不排序</option>
											<option value="002">升序</option>
											<option value="003">降序</option>
										</select>
									</td>
									<td>数据格式</td>
									<td align="left">
										<select
											class="easyui-combobox" name="format" id="format" style="width: 120px;">
											<option value="001">字段</option>
											<option value="002">日期</option>
											<option value="003">钱币</option>
										</select>
									</td>
								</tr>
								<tr>
									<td>是否分组</td>
									<td align="left">
										<input type="radio" style="width:15px" id="group" name="group" value="1">是
										<input type="radio" style="width:15px" id="group" name="group" value="0">否
										<input type="hidden" id="icgroup" name="icgroup">
									</td>
									<td>汇总方式</td>
									<td align="left">
										<select
											class="easyui-combobox" name="collect" id="collect" style="width: 120px;">
											<option value="001">不汇总</option>
											<option value="002">求和</option>
											<option value="003">平均</option>
											<option value="004">最大</option>
											<option value="005">最小</option>
										</select>
									</td>
								</tr>
								<tr>
									<td>是否选择</td>
									<td align="left">
										<input type="radio" style="width:15px" id="select" name="select" value="1">是
										<input type="radio" style="width:15px" id="select" name="select" value="0">否
										<input type="hidden" id="icselect" name="icselect">
									</td>
									<td>可访问角色</td>
									<td align="left">
										<select
											class="easyui-combobox" name="user" id="user" style="width: 150px;">
										</select>
										<input type="checkbox" style="width:15px" value="all" name="all" id="all" onclick="check()"/>全部
										<input type="hidden" id="auth" name="auth"/>
									</td>
								</tr>
							</table>
						</center>
					</div>
					<div class="easyui-layout" region="center" style="background: #fafafa; width: 100%;height:100%">
						<table id="chartreportList" name="chartreportList"></table>
					</div>
					<input type="hidden" id="infoactive" name="infoactive"/>
					<input type="hidden" id="infoid" name="infoid"/>
				</div>
			
			
		</form>
  </div>
  
  <div id="chartshow" name="chartshow" title="权限访问范围" class="easyui-window"  style="width:450px;" resizable=false closed=true modal=true>
		<form id="form3" name="form3" method="POST">
			<table class="tablestyle" style="width:100%" cellpadding="4">
				<tr>	
					<td>
						角色:
					</td>
					<td>
						<select class="easyui-combobox" id="icuser" name="icuser" style="width: 120px;">
						</select>
					</td>
					<td>
						值:
					</td>
					<td>
						<input id="icvalue" name="icvalue"/>
					</td>
				</tr>
			</table>
			<input type="hidden" id="active3" name="active3">
			<input type="hidden" id="icfieldname" name="icfieldname">
			<input type="hidden" id="icusers" name="icusers">
			<input type="hidden" id="icfieldid" name="icfieldid"/>
			<input type="hidden" id="icid" name="icid">
		</form>
  </div>
  <script type="text/javascript">
		var curID="";
		var rows="";
		var reportid='<%=request.getParameter("reportid")%>';
		var view=<%=PublicTools.getComboboxOptions("V_自定义报表数据对象","视图名","视图名","where 报表id='"+request.getParameter("reportid")+"'",request)%>;
		var curcolid="";
		var curinfoid="";
		var role=<%=PublicTools.getComboboxOptions("dbo.V_USER_AUTH","distinct authcode","authdesc","where 1=1",request)%>;
		var role2=<%=PublicTools.getComboboxOptions("dbo.V_USER_AUTH","distinct authcode","authdesc","where 1=1",request)%>;
		$(document).ready(function(){			
			loadGrid();
		});
		
		function check(){
			//alert($("#all:checked").val());
			if($("#all:checked").val()=="all"){
				$('#user').combobox("disable");
			}else{
				$('#user').combobox("enable");
			}
		}
		
		$('#columnsconedit').dialog({
			toolbar:[{
				text:'保存',
				iconCls:'icon-save',
				handler:function(){
					$('#form1').submit();
				}
			}]
				
		});
		var auth="";
		$('#columnconfedit').dialog({
			toolbar:[{
				text:'保存',
				iconCls:'icon-save',
				handler:function(){
					$("#icscreen").val($("#screen:checked").val());
					$("#icgroup").val($("#group:checked").val());
					$("#icselect").val($("#select:checked").val());
					if($("#all:checked").val()=="all"){
						$('#auth').val("all");
					}else{
						auth=$('#user').combobox("getValues");
						if(auth!=""){
							auth=auth.toString().replace(/,/g,";");
							$('#auth').val(";"+auth+";");
						}else{
							$('#auth').val("");
						}
					}
					$('#form2').submit();
				}
			}]
				
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
	
		$('#icuser').combobox({
			data:role2,
			valueField:'comboValue',
			textField:'comboName',
			multiple:"multiple",
			onSelect:function(record){
				//getviewcollist(record.comboValue);
			}
		});
		
		$('#view1').combobox({
			data:view,
			valueField:'comboValue',
			textField:'comboName',
			onSelect:function(record){
				getviewcollist("col1",record.comboValue);
			}
		});
			
		$('#view2').combobox({
			data:view,
			valueField:'comboValue',
			textField:'comboName',
			onSelect:function(record){
				getviewcollist("col2",record.comboValue);
			}
		});
		
		$('#form1').form({
			//定位到servlet位置的url
			url:'<%=request.getContextPath()%>/Svl_columnscon', 
			//当点击事件后触发的事件
		    onSubmit: function(data){
		    	//alert();
		    }, 
		    //当点击事件成功提交后触发的事件
		    success:function(data){
				var json = eval("("+data+")");
				$("#columnsconedit").window('close');
				loadGrid();
		    }
		});
	
		$('#form2').form({
			//定位到servlet位置的url
			url:'<%=request.getContextPath()%>/Svl_columnsinfo', 
			//当点击事件后触发的事件
		    onSubmit: function(data){
		    	//alert();
		    }, 
		    //当点击事件成功提交后触发的事件
		    success:function(data){
				var json = eval("("+data+")");
				$('#columnsinfolist').datagrid("reload");
				$("#columnconfedit").window('close');
				//loadGrid();
		    }
		});
		
		function getviewcollist(id,value){
			$.ajax({
				type: "POST",
				url: '<%=request.getContextPath()%>/Svl_columnscon',   //设置 SVL
				async:false,
				data: "active=viewcol&viewname=" + value,  // 设置模式，一般为view ，主关键字值		
				dataType: 'json',
				success: function(datas){
					//alert(id);
					$("#"+id).combobox({
						data:datas,
						valueField:'Name',
						textField:'Name'
					});
					//alert(id);
				}
			});
		}
		
		function loadGrid(){
			$('#columnsconlist').datagrid({
				title:'视图关系列表',
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
				url:'<%=request.getContextPath()%>/Svl_columnscon', 
				queryParams:{"active":"que","reportid":reportid},
				toolbar: [{
					iconCls: 'icon-add',
					text:'添加',
					handler: function(){
						doToolbar("add");
						//$("#columnsconedit").window('open');
					}
				},{
					text:'修改',
					iconCls:'icon-edit',
					handler:function(){
						doToolbar("edit");
						//$("#columnsconedit").window('open');
					}
				},{
					text:'删除',
					iconCls:'icon-cancel',
					handler:function(){
						doToolbar("cancel");
						//$("#columnsconedit").window('open');
					}
				}],
				columns:[[
					{field:'表1',title:'视图名1',width:100},
					{field:'列1',title:'列名1',width:100},
					{field:'关系',title:'关系',width:100,formatter: function(value,row,index){
						if (value=="inner join"){
							return "内连接";
						}
						if (value=="left join"){
							return "右连接";
						}
						if (value=="right join"){
							return "左连接";
						}
					}},
					{field:'表2',title:'视图名2',width:100},
					{field:'列2',title:'列名2',width:100}
				]],
				onClickRow:function(rowIndex,rowData){
					//alert();
					//curID=rowData.编号;
					curcolid=rowData.关联id;
				},
				onLoadSuccess:function(){
				}
			});
			//doToolbarAuth();
		}
		
		$('#form3').form({
			//定位到servlet位置的url
			url:'<%=request.getContextPath()%>/Svl_Userauit', 
			//当点击事件后触发的事件
		    onSubmit: function(data){
		    	//alert();
		    }, 
		    //当点击事件成功提交后触发的事件
		    success:function(data){
		    	datas=eval("("+data+")");
		    	$("#chartreportList").datagrid("reload");
		    	$("#chartshow").window("close");
		    	showMsg(datas[0].MSG);
		    }
		});
		
		function doToolbar(iToolbar) {
	  		if(iToolbar=="add"){
				$('#form1').form('clear');
				$('#view1').combobox('clear');
				$('#view2').combobox('clear');
				$('#gx').combobox('clear');
				$('#col1').combobox('clear');
				$('#col2').combobox('clear');
				$('#col1').combobox('loadData','');
				$('#col2').combobox('loadData','');
	  			$("#active").val("add");
	  			$("#reportid").val(reportid);
	  			$("#columnsconedit").window('open');
	  		}
	  		if(iToolbar=="edit"){
	  			if(curcolid!=""){
	  				rows = $('#columnsconlist').datagrid('getSelected');
	  				$('#view1').combobox('select', rows.表1);
	  				$('#view2').combobox('select', rows.表2);
	  				$('#col1').combobox('setValue', rows.列1);
	  				$('#col2').combobox('setValue', rows.列2);
	  				$('#gx').combobox('setValue', rows.关系);
	  				
	  				
	  				$("#columnid").val(curcolid);
	  				$("#reportid").val(reportid);
	  				$("#active").val("edit");
	  				
	  				$("#columnsconedit").window('open');
	  			}else{
	  				alertMsg("请选择一条数据");
	  			}
	  		}
	  		if(iToolbar=="cancel"){
	  			if(curcolid!=""){
	  				//alert();
	  				rows = $('#columnsconlist').datagrid('getSelected');
					$.ajax({
						type: "POST",
						url: '<%=request.getContextPath()%>/Svl_columnscon',   //设置 SVL
						async:false,
						data: "active=cancel&columnid=" + curcolid,  // 设置模式，一般为view ，主关键字值		
						dataType: 'json',
						success: function(datas){
							loadGrid();
						}
					});
	  			}else{
	  				alertMsg("请选择一条数据");
	  			}
	  		}
	  		if(iToolbar=="infoedit"){
	  			if(curinfoid!=""){
		  			
		  			rows = $('#columnsinfolist').datagrid('getSelected');
		  			$("#infoid").val(rows.信息id);
		  			$("#colname").html(rows.数据列);
		  			//alert(rows.筛选方式);
		  			//alert();
		  			//$("[id=screen][value=0]").attr("checked",false);
		  			
					//document.getElementById('screen').checked ='';
		  			//$(":radio[id=screen]:checked").attr("checked",false);
		  			$("[id=screen][value="+rows.筛选方式+"]").attr("checked",true);
		  			$("[id=group][value="+rows.筛选方式+"]").attr("checked",true);
		  			$("[id=select][value="+rows.是否选择+"]").attr("checked",true);
		  			if(rows.权限!=""){
		  				if(rows.权限!="all"){
				  			var jihuo=rows.权限.substring(1);
							jihuo=jihuo.substring(0,jihuo.length-1);
							//actnum=jihuo;
							$('#user').combobox('setValues', jihuo.split(";"));
						}else{
							$("#all").attr("checked","checked");
							check();
						}
					}
		  			$("#defaluts").val(rows.默认值);
		  			$("#sort").combobox('setValue', rows.排序方式);
		  			$("#format").combobox('setValue', rows.数据格式);
		  			$("#collect").combobox('setValue', rows.汇总方式);
		  			
		  			$("#infoactive").val("edit");
		  			$("#columnconfedit").window('open');
		  			colvalue(rows.信息id);
	  			}else{
	  				alertMsg("请选择一条数据");
	  			}
	  		}
	  		
	  		
	  		if(iToolbar=="addauth"){
				$('#form3').form('clear');
	  			$("#active3").val("save");
	  			
	  			$("#icfieldid").val(curinfoid);
	  			$("#chartshow").window('open');
	  		}
	  		if(iToolbar=="editauth"){
				//$('#form3').form('clear');
				if(curcauthid!=""){
					
					rows = $('#chartreportList').datagrid('getSelected');
		  			$("#active3").val("edit");
		  			$("#icfieldid").val(curinfoid);
		  			$("#icid").val(curcauthid);
		  			;
		  			var jihuo=rows.用户.substring(1);
					jihuo=jihuo.substring(0,jihuo.length-1);
					
					//actnum=jihuo;
					$('#icuser').combobox('setValues', jihuo.split(";"));
				
					$('#icvalue').val(rows.值);
					
		  			$("#chartshow").window('open');
	  			}else{
	  				alertMsg("请选择一条数据");
	  			}
	  		}
	  		if(iToolbar=="delauth"){
	  			if(curcauthid!=""){
					ConfirmMsg("你是否要删除该条记录?","chanelauth()","");
	  			}else{
	  				alertMsg("请选择一条数据");
	  			}

	  		}
  		}
		
		function chanelauth(){
			$.ajax({
				type: "POST",
				url: '<%=request.getContextPath()%>/Svl_Userauit',   //设置 SVL
				async:false,
				data: "active3=cancel&icid=" + curcauthid,  // 设置模式，一般为view ，主关键字值		
				dataType: 'json',
				success: function(datas){
					//datas=eval("("+data+")");
			    	$("#chartreportList").datagrid("reload");
			    	//$("#chartshow").window("close");
			    	showMsg(datas[0].MSG);
				}
			});
		}
		
		$('#chartshow').dialog({
			toolbar:[{
				text:'确定',
				iconCls:'icon-ok',
				handler:function(){
					auth=$('#icuser').combobox("getValues");
					$("#icusers").val(";"+auth+";");
					$("#form3").submit();
				}
			}]
				
		});
		
		$('#columnsinfolist').datagrid({
				singleSelect:true,
				checkbox:true,
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
				url:'<%=request.getContextPath()%>/Svl_columnsinfo', 
				queryParams:{"infoactive":"que","inforeportid":reportid},
				toolbar: [{
					iconCls: 'icon-edit',
					text:'编辑',
					handler: function(){
						//$("#columnsconedit").window('open');
						doToolbar("infoedit");
						
					}
				}],
				columns:[[
					{field:'数据列',title:'数据列',width:100},
					{field:'筛选方式',title:'是否筛选',width:100,formatter: function(value,row,index){
						if (value=="0"){
							return "不筛选";
						} else {
							return "筛选";
						}
					}},
					{field:'默认值',title:'默认值',width:100},
					{field:'数据格式',title:'数据格式',width:100,formatter: function(value,row,index){
						if (value=="001"){
							return "字段";
						}
						if (value=="002"){
							return "日期";
						}
						if (value=="003"){
							return "钱币";
						}
					}},
					{field:'排序方式',title:'排序方式',width:100,formatter: function(value,row,index){
						if (value=="001"){
							return "不排序";
						}
						if (value=="002"){
							return "升序";
						}
						if (value=="003"){
							return "降序";
						}
					}},
					{field:'是否分组',title:'是否分组',width:100,formatter: function(value,row,index){
						if (value=="0"){
							return "不分组";
						} else {
							return "分组";
						}
					}},
					{field:'汇总方式',title:'汇总方式',width:100,formatter: function(value,row,index){
						if (value=="001"){
							return "不汇总";
						}
						if (value=="002"){
							return "求和";
						}
						if (value=="003"){
							return "平均";
						}
						if (value=="004"){
							return "最大";
						}
						if (value=="005"){
							return "最小";
						}
					}},
					{field:'是否选择',title:'是否选择',width:100,formatter: function(value,row,index){
						if (value=="0"){
							return "不选择";
						} else {
							return "选择";
						}
					}},
					{field:'权限',title:'可访问角色',width:100,
						formatter:function (value,row,index){
							if(value=="all"){
								return "全部";
							}
							value=value.substring(1);
							value=value.substring(0,value.length-1);
							return value;
						}
					}
				]],
				onClickRow:function(rowIndex,rowData){
					curinfoid=rowData.信息id;
				},
				onLoadSuccess:function(){
				},
				onBeforeEdit:function(index,row){
					//row.editing = true;
					//updateActions(index);
				},
				onAfterEdit:function(index,row){
					//row.editing = false;
					//updateActions(index);
				},
				onCancelEdit:function(index,row){
					//row.editing = false;
					//updateActions(index);
				}
		});
		
		function setpxValues(id){

		}
		
		function fudefault(def){
			//alert(def);
			if(def=="0"){
				$("#defaluts").attr("disabled","true");
			}else{
				$("#defaluts").removeAttr("disabled");
			}
		}
		var curcauthid="";
		function colvalue(colid){
			$('#chartreportList').datagrid({
				//iconCls:'icon-edit',
				singleSelect:true,
				url: '<%=request.getContextPath()%>/Svl_Userauit',
				queryParams:{"active3":"que","icfieldid":colid},
				title:'用户访问范围',
				width: '100%',
				nowrap: false,
				fit:true,
				showFooter:true,
				striped:true, //隔行变色
				//pagination:true,
				pageSize:10,
				singleSelect:true,
				pageNumber:1,
				rownumbers:true,
				fitColumns: true,	
				toolbar: [{
					iconCls: 'icon-add',
					text:'添加',
					handler: function(){
						//$("#columnsconedit").window('open');
						doToolbar("addauth");
						
					}
				},{
					iconCls: 'icon-edit',
					text:'编辑',
					handler: function(){
						//$("#columnsconedit").window('open');
						doToolbar("editauth");
						
					}
				},{
					iconCls: 'icon-cancel',
					text:'删除',
					handler: function(){
						//$("#columnsconedit").window('open');
						doToolbar("delauth");
						
					}
				}],
				columns:[[
					{field:'用户',title:'用户',width:100,
						formatter:function (value,row,index){
							value=value.substring(1);
							value=value.substring(0,value.length-1);
							return value;
						}
					},
					{field:'值',title:'值',width:100}
				]],
				onClickRow:function(rowIndex,rowData){
					curcauthid=rowData.主键;
				},
				onLoadSuccess:function(){
					
				}
			});
		}
	</script>
</html>
