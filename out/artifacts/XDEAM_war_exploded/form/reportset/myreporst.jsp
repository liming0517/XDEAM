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
  	<meta name="renderer" content="webkit"> 
  	<meta name="renderer" content="ie-comp">
  	<meta name="renderer" content="ie-stand">
    <link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css"
		href="<%=request.getContextPath()%>/themes/icon.css">
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
				<table id="userreportList" name="userreportList"></table>
		</div>
	  
	  <div id="userreporedit" name="userreporedit" title="我的报表编辑" class="easyui-window" style="width:500px;"  resizable=false closed=true modal=true>
	  	<form id="form1" name="form1" method="POST">
				<center>
					<table class="tablestyle" style="width:100%" cellpadding="4">
						<tr>
							<td>名称</td>
							<td align="left">
								<input  style="width:100%;" maxlength=50 id="userreport" name="userreport">
							</td>
							<td>类型</td>
							<td align="left">
							<select
								class="easyui-combobox" name="template" id="template" style="width: 150px;">
							</select>
							</td>
							<td>描述</td>
							<td align="left">
								<input  style="width:100%;" maxlength=200 id="describe" name="describe"/>
							</td>
						</tr>
					</table>
					<input type="hidden" id="active" name="active"/>
					<input type="hidden" id="userreportid" name="userreportid"/>
				</center>
		</form>
  	  </div>
  	  <div id="hisreport" name="hisreport" title="历史报表列表" class="easyui-window" style="width:800px;height:500px"  resizable=false closed=true modal=true>
		<table id="hisreportList" name="hisreportList"></table>
  	  </div>
  	  
<div id="chartedit" name="chartedit" title="图表编辑" class="easyui-window"  style="width:800px;" resizable=false closed=true modal=true>
	<form id="form2" name="form2" method="POST">
		<div class="easyui-layout" style="width:100%;height:380px">
			<div region="east" style="width:300px;">
				<div id="tt" class="easyui-tabs" style="height:375px">
					<div title="图表列表">
						<table id="chartreportList" name="chartreportList"></table>
					</div>
					<div title="历史图表">
						<table id="chartreporthisList" name="chartreporthisList"></table>
					</div>
				</div>
			</div>
			<div class="easyui-layout" region="center" style="background: #fafafa; width: 100%;height:100%">
				<div region="north" align="left" style="overflow: hidden; height: 30px;">
					<a href="#"
					 	onclick="doToolbar(this.id);"
						id="savejt" name="savejt" class="easyui-linkbutton" plain="true"
						iconCls="icon-save">保存图表</a>
					<a href="#"
					 	onclick="doToolbar(this.id);"
						id="edit" name="edit" class="easyui-linkbutton" plain="true"
						iconCls="icon-edit">筛选</a>
					<a href="#"
					 	onclick="doToolbar(this.id);"
						id="zhuomian" name="edit" class="easyui-linkbutton" plain="true"
						iconCls="icon-edit">移至桌面</a>
				</div>
				<div region="center" style="background: #fafafa; width: 100%;">
					<iframe id="chartiframe" name="chartiframe" src="<%=request.getContextPath()%>/form/reportset/Chart.jsp" style="width:99%;height:98%"></iframe>
				</div>
			</div>
		</div>
	</form>
  </div>
  <div id="chartgl" name="chartgl" title="图表筛选" class="easyui-window"  style="width:600px;" resizable=false closed=true modal=true>
		<div region="center" id="showchartgl" name="showchartgl" style="background: #fafafa;">
			
		</div>
  </div>
  <div id="chartshow" name="chartshow" title="图表显示" class="easyui-window"  style="width:300px;" resizable=false closed=true modal=true>
		<form id="form3" name="form3" method="POST">
			<table class="tablestyle" style="width:100%" cellpadding="4">
				<tr>
					<td>
						图表名称:
					</td>
					<td>
						<input id="icchartname" name="icchartname">
					</td>
				</tr>
			</table>
			<input type="hidden" id="active3" name="active3">
			<input type="hidden" id="iccharttype" name="iccharttype">
			<input type="hidden" id="icchartuserreportid" name="icchartuserreportid"/>
			<input type="hidden" id="icchartxml" name="icchartxml">
		</form>
		
    </div>
    <div id="chartzhuomian" name="chartzhuomian" title="桌面图表"  class="easyui-window"  style="width:280px;height:150px" resizable=false closed=true modal=true>
		<div id='charttihuanname' name='charttihuanname'>
		
		</div>
  	</div>
  </body>	
  <script type="text/javascript">
  	var curuserreportid="";
  	var rows = null;
  	var chartxml="";
  	var charttype="";
  	var chartsql="";
  	var view=<%=PublicTools.getComboboxOptions("V_用户定义报表主表","用户报表id","名称","where 状态='1' and 用户编号='all' and 报表id in (select 报表id from V_用户定义报表主表)",request)%>;
  	var address="";
  	var chartname="";
  	var tiaojian="";
  	var userreportname="";
  	$(document).ready(function(){
  		loadGrid();
  	});
  	
  	$.ajax({
		type: "POST",
		url: '<%=request.getContextPath()%>/Svl_Chart',   //设置 SVL
		async:false,
		data: "active2=xianshi",  // 设置模式，一般为view ，主关键字值		
		dataType: 'json',
		success: function(datas){
			//alert(datas[0].div);
			$('#charttihuanname').html(datas[0].div);
			
		}
  	});
  	
  	$('#userreporedit').dialog({
			toolbar:[{
				text:'下一步',
				iconCls:'icon-undo',
				handler:function(){
					$("#form1").submit();
				}
			}]
	});
	
	$('#template').combobox({
		data:view,
		valueField:'comboValue',
		textField:'comboName'
	});
		
	$('#chartgl').dialog({
		toolbar:[{
			text:'确定',
			iconCls:'icon-ok',
			handler:function(){
				//alert();
				doToolbar("quedin");
			}
		}]
			
	});
	function hisreportList(userreportid){
		$('#hisreportList').datagrid({
				//iconCls:'icon-edit',
				singleSelect:true,
				url: '<%=request.getContextPath()%>/Svl_userreportList',
				queryParams:{"active":"quehis","userreportid":userreportid},
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
					text:'查看',
					handler: function(){
						if(address!=""){
							location.href="<%=request.getContextPath()%>/JSONDrawExcel/HisReportExcel.jsp?address="+address;
						}else{
							alertMsg("请选择一条数据");
						}
					}
				}],
				columns:[[
					{field:'名称',title:'名称',width:100},
					{field:'用户编号',title:'用户编号',width:100},
					{field:'创建时间',title:'创建时间',width:100}
				]],
				onClickRow:function(rowIndex,rowData){
					address=rowData.地址;
				},
				onLoadSuccess:function(data){
					//var datas = eval("("+data+")");
					//alert(data);
					$("#hisreport").window("open");
				}
			});
		}
		$('#chartshow').dialog({
			toolbar:[{
				text:'确定',
				iconCls:'icon-ok',
				handler:function(){
					$("#form3").submit();
				}
			}]
				
		});
		
		$('#form3').form({
			//定位到servlet位置的url
			url:'<%=request.getContextPath()%>/Svl_HisChart', 
			//当点击事件后触发的事件
		    onSubmit: function(data){
		    	//alert();
		    }, 
		    //当点击事件成功提交后触发的事件
		    success:function(data){
		    	datas=eval("("+data+")");
		    	$("#chartreporthisList").datagrid("reload");
		    	showMsg(datas[0].MSG);
		    	$("#chartshow").window("close");
		    }
		});
		
	$('#form1').form({
			//定位到servlet位置的url
			url:'<%=request.getContextPath()%>/Svl_userreportList', 
			//当点击事件后触发的事件
		    onSubmit: function(data){
		    	//alert();
		    }, 
		    //当点击事件成功提交后触发的事件
		    success:function(data){
				//alert();
				var json = eval("("+data+")");
				$("#userreporedit").window('close');
				curuserreportid=json[0].ID;
				location.href="<%=request.getContextPath()%>/form/reportset/reportuserinfo.jsp?userreportid="+curuserreportid+"&userreportname="+$("#userreport").val();
		    }
	});

	
 	var chartid="";
	//var chartname="";
  	function loadGrid(){
  		$('#userreportList').datagrid({
				title:'我的报表',
				iconCls:'icon-edit',
				singleSelect:true,
				url: '<%=request.getContextPath()%>/Svl_userreportList',
				queryParams:{"active":"quemyreport"},
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
						if(curuserreportid!=""){
							location.href="<%=request.getContextPath()%>/form/reportset/reportuserinfo.jsp?userreportid="+curuserreportid+"&userreportname="+userreportname;
						}else{
							alertMsg("请选择一条数据");
						}
					}
				},{
					text:'预览',
					iconCls:'icon-ok',
					handler:function(){
						if(curuserreportid!=""){
							location.href="<%=request.getContextPath()%>/JSONDrawExcel/ReportExcel.jsp?userreportid="+curuserreportid;
						}else{
							alertMsg("请选择一条数据");
						}
						
					}
				},{
					text:'图表',
					iconCls:'icon-ok',
					handler:function(){
						if(curuserreportid!=""){
							$('#chartreportList').datagrid({
							//iconCls:'icon-edit',
							singleSelect:true,
							url: '<%=request.getContextPath()%>/Svl_Chart',
							queryParams:{"active2":"que","chartuserreportid":curuserreportid},
							title:'图表列表',
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
							columns:[[
								{field:'图表名称',title:'图表名称',width:100},
								{field:'图表类型',title:'图表类型',width:100,formatter: function(value,row,index){
									if (value=="001"){
										return "单维柱状图";
									}
									if (value=="002"){
										return "饼图";
									}
									if (value=="003"){
										return "折线图";
									}
									if (value=="004"){
										return "多维柱状图";
									}
								}},
								{field:'创建时间',title:'创建时间',width:100}
							]],
							onDblClickRow:function(rowIndex,rowData){
								$("#savejt").linkbutton('enable');
								$("#edit").linkbutton('enable');
								$("#zhuomian").linkbutton('enable');
								$("#chartiframe").show();
								chartxml=rowData.图表输出;
								charttype=rowData.图表类型;
								chartsql=rowData.查询语句;
								chartname=rowData.图表名称;
								chartid=rowData.图表id;
								window.frames["chartiframe"].chartsqls(chartsql);
								window.frames["chartiframe"].charttypes(charttype);
								window.frames["chartiframe"].chartsxs("");
								if(charttype=="001"){
									window.frames["chartiframe"].Column3D(chartxml);
								}
								if(charttype=="002"){
									window.frames["chartiframe"].Pie3D(chartxml);
								}
								if(charttype=="003"){
									window.frames["chartiframe"].Line2D(chartxml);
								}
								if(charttype=="004"){
									window.frames["chartiframe"].MSColumn2D(chartxml);
								}
								window.frames["chartiframe"].doToolbar("ok");
							},
							onLoadSuccess:function(){
								
							}
						});
						$('#chartreporthisList').datagrid({
							//iconCls:'icon-edit',
							singleSelect:true,
							url: '<%=request.getContextPath()%>/Svl_HisChart',
							queryParams:{"active3":"que","icchartuserreportid":curuserreportid},
							title:'历史图表',
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
							columns:[[
								{field:'图表名称',title:'图表名称',width:100},
								{field:'图表类型',title:'图表类型',width:100,formatter: function(value,row,index){
									if (value=="001"){
										return "单维柱状图";
									}
									if (value=="002"){
										return "饼图";
									}
									if (value=="003"){
										return "折线图";
									}
									if (value=="004"){
										return "多维柱状图";
									}
								}}
							]],
							onDblClickRow:function(rowIndex,rowData){
								$("#savejt").linkbutton('disable');
								$("#edit").linkbutton('disable');
								$("#zhuomian").linkbutton('disable');
								$("#chartiframe").show();
								chartxml=rowData.图表输出;
								charttype=rowData.图表类型;
								chartsql=rowData.查询语句;
								chartname=rowData.图表名称;
								chartid=rowData.图表id;
								//window.frames["chartiframe"].chartsqls(chartsql);
								//window.frames["chartiframe"].charttypes(charttype);
								//window.frames["chartiframe"].chartsxs("");
								if(charttype=="001"){
									window.frames["chartiframe"].Column3D(chartxml);
								}
								if(charttype=="002"){
									window.frames["chartiframe"].Pie3D(chartxml);
								}
								if(charttype=="003"){
									window.frames["chartiframe"].Line2D(chartxml);
								}
								if(charttype=="004"){
									window.frames["chartiframe"].MSColumn2D(chartxml);
								}
								//window.frames["chartiframe"].doToolbar("ok");
							},
							onLoadSuccess:function(){
								
							}
						});
					    $.ajax({
							type: "POST",
							url: '<%=request.getContextPath()%>/Svl_userreportinfo',   //设置 SVL
							async:false,
							data: "active=quesxcol&userreportid=" + curuserreportid,  // 设置模式，一般为view ，主关键字值		
							dataType: 'json',
							success: function(datas){
								//alert(datas[0].div);
								$('#showchartgl').html(datas[0].div);
								$(".easyui-combobox").combobox();
							}
						});
						$("#chartedit").window("open");
						}else{
							alertMsg("请选择一条数据");
						}
					}
				}],
				columns:[[
					{field:'用户报表id',title:'用户报表id',width:100,hidden:true},
					{field:'报表id',title:'报表id',width:100,hidden:true},
					{field:'样式id',title:'样式id',width:100,hidden:true},
					{field:'名称',title:'名称',width:100},
					{field:'描述',title:'描述',width:100},
					{field:'创建时间',title:'创建时间',width:100},
					{field:'操作',title:'操作',width:30,
						formatter:function(value,row,index){
							var btn="";
							btn="<input type='button' onclick='hisreportList(\""+row.用户报表id+"\")' value='历史报表'>";
							return btn;
						}
					}
				]],
				onClickRow:function(rowIndex,rowData){
					curuserreportid=rowData.用户报表id;
					userreportname=rowData.名称;
				},
				onLoadSuccess:function(){

				}
			});
  	}
  			
  	function doToolbar(iToolbar) {
  		if(iToolbar=="add"){
			$('#form1').form('clear');
  			$("#active").val("addmyreport");
  			$("#template").combobox('enable');
  			$("#userreporedit").window('open');
  		}
  		if(iToolbar=="zhuomian"){
  			$("#chartzhuomian").window('open');
  		}
 		if(iToolbar=="edit"){
  			$("#chartgl").window("open");
  		}
	  	if(iToolbar=="savejt"){
	  		//alert();
 			$("#icchartuserreportid").val(curuserreportid);
			$("#icchartxml").val(chartxml);
			$("#icchartname").val(chartname);
			$("#iccharttype").val(charttype);
			$("#active3").val("save");
			
			$("#chartshow").window("open");
	  	}
	  	if(iToolbar=="quedin"){
	  			//alert();
			for(var i=0;i<$("#jishu").html();i++){
					//
					tiaojian="";
					if($("#inp"+i).val()!=""){
						tiaojian+=" and "+$("#hid"+i).val();
						//tiaojian+="='";
						//tiaojian+=$("#inp"+i).val()+"'";
						//alert();
						//alert($("#com2").combobox('getValue'));
						//alert(document.all.com0.value)
						//alert($("#com0").combobox('getValue'));
						if($("#com"+i).combobox('getValue')=="001"){
							tiaojian+="='";
							tiaojian+=$("#inp"+i).val()+"'";
						}
						if($("#com"+i).combobox('getValue')=="002"){
							//alert(tiaojian);
							tiaojian+=" like ';K;";
							tiaojian+=$("#inp"+i).val()+";K;'";
						}
						if($("#com"+i).combobox('getValue')=="003"){
							tiaojian+=">'";
							tiaojian+=$("#inp"+i).val()+"'";
						}
						if($("#com"+i).combobox('getValue')=="004"){
							tiaojian+=">='";
							tiaojian+=$("#inp"+i).val()+"'";
						}
						if($("#com"+i).combobox('getValue')=="005"){
							tiaojian+="='";
							tiaojian+=$("#inp"+i).val()+"'";
						}
						if($("#com"+i).combobox('getValue')=="006"){
							tiaojian+="<='";
							tiaojian+=$("#inp"+i).val()+"'";
						}
						if($("#com"+i).combobox('getValue')=="007"){
							tiaojian+="<'";
							tiaojian+=$("#inp"+i).val()+"'";
						}
						
					}
				}
				//$("#active2").val("sx");
				//$("#chartsql").val(chartsql);
				//$("#chartsx").val(tiaojian);
				$("#chartgl").window('close');
				window.frames["chartiframe"].chartsqls(chartsql);
				//alert(tiaojian);
				window.frames["chartiframe"].chartsxs(tiaojian);
				window.frames["chartiframe"].doToolbar("ok");
				chartxml=window.frames["chartiframe"].getchartxml();
				//$("#form2").submit();
	  		}
  	}
  	
  	function hqnr(id,type){
  		//alert(chartid);
	  	$.ajax({
			type: "POST",
			url: '<%=request.getContextPath()%>/Svl_Chart',   //设置 SVL
			async:false,
			data: "active2=tihuan&id="+id+"&chartid="+chartid+"&chartname="+chartname+"&charttype="+charttype,  // 设置模式，一般为view ，主关键字值		
			dataType: 'json',
			success: function(datas){
				showMsg("替换成功");
				$("#chartzhuomian").window("close");
			}
	  	});
  	}
  </script>
  
</html>
