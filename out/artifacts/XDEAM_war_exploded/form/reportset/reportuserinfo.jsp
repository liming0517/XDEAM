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
<%@ page import="com.pantech.base.common.tools.Base64Util"%>

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
  		<div region="east"  class="easyui-layout" title="数据源" style="width:300px;">
			<div region="center" id="showreport" name="showreport" style="background: #fafafa;">
				<!-- 动态加载 -->
				
			</div>
  		</div>
   		<div region="center" title="我的报表-<%=request.getParameter("userreportname") %><font color='red'>(编辑特定格式的报表)</font>" style="background: #fafafa;">
			<table  id="baoxiaolist" name="baoxiaolist"></table>
			<form id="form1" name="form1" method="POST">
				<input type="hidden" id="active" name="active"/>
				<input type="hidden" id="userreportid" name="userreportid"/>
				<input type="hidden" id="jsonlist" name="jsonlist"/>
			</form>
		</div>
		
  </body>
  <div id="chenfeibxedit" name="chenfeibxedit" title="自定义名称" class="easyui-window" style="width:250px;"  resizable=false closed=true modal=true>
			<center>
				<table class="tablestyle" style="width:100%" cellpadding="4">
					<tr>
						<td>名称</td>
						<td align="left">
							<input id="zdy" name="zdy">
						</td>
					</tr>
				</table>
			</center>
  </div>
  
    <div id="reportmergeedit" name="reportmergeedit" title="跨行编辑" class="easyui-window"  style="width:350px;" resizable=false closed=true modal=true>
			<center>
				<table class="tablestyle" style="width:100%" cellpadding="4">
					<tr>
						<td>跨行</td>
						<td align="left">
							<input class="easyui-numberspinner" id="rows" name="rows" data-options="min:1,max:3,value:1">
						</td>
						<td>跨列</td>
						<td align="left">
							<input class="easyui-numberspinner" id="cols" name="cols" data-options="min:1,max:26,value:1">
						</td>
					</tr>
				</table>
				<input type="hidden" id="startrow" name="startrow">
				<input type="hidden" id="startcol" name="startcol">
			</center>
  </div>
  
  <div id="reportrem" name="reportrem" class="easyui-menu" style="width:120px;">
  	<div class="easyui-tooltip" title="添加一行" position="right">添加</div>
    <div class="easyui-tooltip" title="清空单元格内的内容" position="right">删除</div>
	<div class="easyui-tooltip" title="将若干单元格合并成一格" position="right">合并</div>
  </div>

  <div id="chartedit" name="chartedit" title="图表编辑" class="easyui-window" style="width:800px;" resizable=false closed=true modal=true>
	<form id="form2" name="form2" method="POST">
		<div class="easyui-layout" style="width:100%;height:350px">
			<div region="north" align="left"
			style="overflow: hidden; height: 73px;">
				<center>
					<table class="tablestyle" style="width:100%" cellpadding="4">
						<tr>
							<td>图表名称</td>
							<td align="left">
								<input id="chartname" name="chartname" style="width: 180px;">
							</td>
							<td>图表类型</td>
							<td align="left">
								<select
									class="easyui-combobox" name="type" id="type" style="width: 180px;">
									<option value="001">单维柱状图</option>
									<option value="002">饼图</option>
									<option value="003">折线图</option>
									<option value="004">多维柱状图</option>
								</select>
							</td>
						</tr>
						<tr>
							<td>X(分组数据)</td>
							<td align="left">
								<select
									class="easyui-combobox" name="chartx" id="chartx" style="width: 180px;">
								</select>
							</td>
							<td>Y(统计数据)</td>
							<td align="left">
								<select
									class="easyui-combobox" name="charty" id="charty" style="width: 180px;">
								</select>
							</td>
						</tr>
					</table>
					<input type="hidden" id="active2" name="active2">
					<input type="hidden" id="chartuserreportid" name="chartuserreportid"/>
					<input type="hidden" id="chartxml" name="chartxml">
					<input type="hidden" id="chartsql" name="chartsql">
					<input type="hidden" id="chartsx" name="chartsx">
				</center>
			</div>
			<div region="east" style="width:280px;">
				<div id="tt" class="easyui-tabs" style="height:276px">
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
				</div>
				<div region="center" style="background: #fafafa; width: 100%;">
					<iframe id="chartiframe" name="chartiframe" src="<%=request.getContextPath()%>/form/reportset/Chart.jsp" style="width:99%;height:100%"></iframe>
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
  <script type="text/javascript">
  		var curfield="";
  		var jsonvalue;
  		var jsonlist=[];
  		var mokuai="";
  		var lie="";
  		var hang="";
  		var lastindex="";
  		var type="";
  		var list=new Array();
  		var curindex="";
  		var curycbs="";
  		var curfile="";
  		var chartname="";
  		var chart=<%=PublicTools.getComboboxOptions("dbo.V_自定义报表数据对象 a join dbo.V_自定义报表信息 b on a.对象id=b.对象id","a.视图名+'.'+b.数据列","b.数据列","where b.是否选择='1' and b.报表id=(select 报表id from dbo.V_用户定义报表主表 where 用户报表id='"+request.getParameter("userreportid")+"')",request)%>;
  		var chart1=<%=PublicTools.getComboboxOptions("dbo.V_自定义报表数据对象 a join dbo.V_自定义报表信息 b on a.对象id=b.对象id","a.视图名+'.'+b.数据列","b.数据列","where b.是否选择='1' and b.报表id=(select 报表id from dbo.V_用户定义报表主表 where 用户报表id='"+request.getParameter("userreportid")+"')",request)%>;
  		var tiaojian="";
  		var charttype="";
  		var chartxml="";
  		var chartsql="";
  		list.push("A");
  		list.push("B");
  		list.push("C");
  		list.push("D");
  		list.push("E");
  		list.push("F");
  		
  		list.push("G");
  		list.push("H");
  		list.push("I");
  		list.push("J");
  		list.push("K");
  		list.push("L");
  		
  		
  		list.push("M");
  		list.push("N");
  		list.push("O");
  		list.push("P");
  		list.push("Q");
  		list.push("R");
  		
  		list.push("S");
  		list.push("T");
  		list.push("U");
  		list.push("V");
  		list.push("W");
  		list.push("X");
  		
  		list.push("Y");
  		list.push("Z");
  		
  		var userreportid='<%=request.getParameter("userreportid")%>';
		$('#chartx').combobox({
			data:chart1,
			valueField:'comboValue',
			textField:'comboName'
		});
			
		$('#charty').combobox({
			data:chart,
			valueField:'comboValue',
			textField:'comboName'
		});
				
  		if (!Array.prototype.indexOf)
		{
		  Array.prototype.indexOf = function(elt /*, from*/)
		  {
		    var len = this.length >>> 0;
		
		    var from = Number(arguments[1]) || 0;
		    from = (from < 0)
		         ? Math.ceil(from)
		         : Math.floor(from);
		    if (from < 0)
		      from += len;
		
		    for (; from < len; from++)
		    {
		      if (from in this &&
		          this[from] === elt)
		        return from;
		    }
		    return -1;
		  };
		}

  		$('#reportrem').menu({
		    onClick:function(item){
		    	//alert(item.text);
		    	if(item.text=="删除"){
			        var data="{"+curfield+":''}";
					var datas=eval("("+data+")");
			        $('#baoxiaolist').datagrid('updateRow',{
						index: curindex,
						row:datas
					});
					//alert(jsonlist.length);
			        for(var i=0;i<jsonlist.length;i++){
			        	//alert(jsonlist[i].X+"=============="+curfield);
			        	//alert(jsonlist[i].Y+"=============="+curindex);
	        			if(jsonlist[i].X==curfield&&jsonlist[i].Y==curindex){
	        				//alert(i);
	        				jsonlist.splice(i,1);
	        			}
	        		}
				}
				if(item.text=="添加"){
					//alert(endEditing());
					if (endEditing()&&(curycbs=="main"||curycbs=="list")){
							
							 $('#baoxiaolist').datagrid('insertRow',{
								index: curindex+1,
								row:{
									隐藏标识:curycbs
								}
							});
							for(var i=0;i<jsonlist.length;i++){
								if(jsonlist[i].Y>curindex){
									//alert(jsonlist[i].value);
									jsonlist[i].Y=parseInt(jsonlist[i].Y)+1;
									//alert(jsonlist[i].Y);
								}
							}

           			 	}
				}
				if(item.text=="合并"){
					$("#startrow").val(curindex);
					$("#startcol").val(curfield);
					//alert(curfield);
					$("#reportmergeedit").window('open');
				}
		    }
		});
		
        
	
  		$.extend($.fn.datagrid.methods, {
            editCell: function(jq,param){
               return jq.each(function(){
                    var opts = $(this).datagrid('options');
                    var fields = $(this).datagrid('getColumnFields',true).concat($(this).datagrid('getColumnFields'));
                    for(var i=0; i<fields.length; i++){
                        var col = $(this).datagrid('getColumnOption', fields[i]);
                        col.editor1 = col.editor;
                        if (fields[i] != param.field){
                            col.editor = null;
                        }
                    }
                    $(this).datagrid('beginEdit', param.index);
                    for(var i=0; i<fields.length; i++){
                        var col = $(this).datagrid('getColumnOption', fields[i]);
                        col.editor = col.editor1;
                   }
                });
            }
        });

  		var editIndex = undefined;
	    function endEditing(){
	            if (editIndex == undefined){return true}
	            if ($('#baoxiaolist').datagrid('validateRow', editIndex)){
	                $('#baoxiaolist').datagrid('endEdit', editIndex);
	                editIndex = undefined;
	                return true;
	            } else {
	                return false;
	            }
        }
  		
  		
  		$(document).ready(function(){	
  			$("#savejt").linkbutton('disable');
			$("#edit").linkbutton('disable');
			$("#chartiframe").hide();
  			showreport();
  			$(".easyui-tooltip").tooltip();
		});
		
  		function showchartgl(){
  			$.ajax({
				type: "POST",
				url: '<%=request.getContextPath()%>/Svl_userreportinfo',   //设置 SVL
				async:false,
				data: "active=quesxcol&userreportid=" + userreportid,  // 设置模式，一般为view ，主关键字值		
				dataType: 'json',
				success: function(datas){
					//alert(datas[0].div);
					$('#showchartgl').html(datas[0].div);
					$(".easyui-combobox").combobox();
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
		    	$("#chartshow").window("close");
		    	showMsg(datas[0].MSG);
		    }
		});
		
		$('#chenfeibxedit').dialog({
			toolbar:[{
				text:'保存',
				iconCls:'icon-save',
				handler:function(){
					var zdy=$("#zdy").val();
					var data="{"+lie+":'"+zdy+"'}";
					var datas=eval("("+data+")");
					
					$('#baoxiaolist').datagrid('updateRow',{
						index: hang,
						row:datas
					});
					$("#chenfeibxedit").window('close');
				}
			}]
				
		});
		
		$('#chartedit').dialog({
			toolbar:[{
				text:'保存并预览',
				iconCls:'icon-save',
				handler:function(){
					$("#active2").val("create");
					$("#chartsx").val("");
					$("#chartuserreportid").val(userreportid);
					$("#form2").submit();
				}
			}]
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
		
		$('#reportmergeedit').dialog({
			toolbar:[{
				text:'确定',
				iconCls:'icon-save',
				handler:function(){
					var x=$('#startcol').val();
					var y=parseInt($('#startrow').val());
					var kx=$('#cols').val();
					var ky=parseInt($('#rows').val()); 
					var ex=list[list.indexOf(x)+parseInt(kx)-1];
					//alert(ky);
					var ey=y+ky-1;
					//alert(ey);
					//alert(x);
					$('#baoxiaolist').datagrid('mergeCells',{
	                    field: x,
	                    index: y,
	                    rowspan: ky,
	                    colspan:kx
	                });
	                
	                for(var i=0;i<jsonlist.length;i++){
						if(jsonlist[i].X==x&&jsonlist[i].Y==y){
							//alert(jsonlist[i].value);
							//alert(jsonlist[i].ex);
							jsonlist[i].ex=ex;
							jsonlist[i].ey=ey;
							
							break;
							
						}
					}
	                
			        for(var i=0;i<jsonlist.length;i++){
			        	//alert(list.indexOf(jsonlist[i].X));
			        	//alert(list.indexOf(x));
			        	//alert(jsonlist[i].Y);
			        	//alert(y);
			        	//alert(list.indexOf(jsonlist[i].ex));
			        	//alert(jsonlist[i].ey);
	        			if(list.indexOf(jsonlist[i].X)>=list.indexOf(x)&&parseInt(jsonlist[i].Y)>=parseInt(y)
	        					&&list.indexOf(jsonlist[i].ex)<=list.indexOf(ex)&&parseInt(jsonlist[i].ey)<=parseInt(ey)){
	        				if(x==jsonlist[i].X&&y==jsonlist[i].Y){
	        					continue;
	        				}
	        				jsonlist.splice(i,1);
	        				
	        			}
	        		}
			        
	                $("#reportmergeedit").window('close');
				}
			}]
		});
		
		$('#chartreportList').datagrid({
			//iconCls:'icon-edit',
			singleSelect:true,
			url: '<%=request.getContextPath()%>/Svl_Chart',
			queryParams:{"active2":"que","chartuserreportid":userreportid},
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
				}}
			]],
			onDblClickRow:function(rowIndex,rowData){
				$("#savejt").linkbutton('enable');
				$("#edit").linkbutton('enable');
				$("#chartiframe").show();
				chartxml=rowData.图表输出;
				charttype=rowData.图表类型;
				chartsql=rowData.查询语句;
				chartname=rowData.图表名称;
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
			queryParams:{"active3":"que","icchartuserreportid":userreportid},
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
				}},
				{field:'创建时间',title:'创建时间',width:100}
			]],
			onDblClickRow:function(rowIndex,rowData){
				$("#savejt").linkbutton('disable');
				$("#edit").linkbutton('disable');
				$("#chartiframe").show();
				chartxml=rowData.图表输出;
				charttype=rowData.图表类型;
				chartsql=rowData.查询语句;
				chartname=rowData.图表名称;
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
		
		function showreport(){
			$.ajax({
					type: "POST",
					url: '<%=request.getContextPath()%>/Svl_userreportinfo',   //设置 SVL
					async:false,
					data: "active=quecol&userreportid=" + userreportid,  // 设置模式，一般为view ，主关键字值		
					dataType: 'json',
					success: function(datas){
						$('#showreport').html(datas[0].div);
						loadGrid();
						loadjson();
						showchartgl();
					}
			});
		}
		
		function gridmergeCells(){
			for(var i=0;i<jsonlist.length;i++){
				//alert(parseInt(parseInt(jsonlist[i].ey)-parseInt(jsonlist[i].Y))+1);
				//alert();
				$('#baoxiaolist').datagrid('mergeCells',{
	                   field: jsonlist[i].X,
	                   index: parseInt(jsonlist[i].Y),
	                   rowspan: parseInt(parseInt(jsonlist[i].ey)-parseInt(jsonlist[i].Y))+1,
	                   colspan:list.indexOf(jsonlist[i].ex)-list.indexOf(jsonlist[i].X)+1
	               });
			}
		}
		
		function loadjson(){
			$.ajax({
				type: "POST",
				url: '<%=request.getContextPath()%>/Svl_userreportinfo',   //设置 SVL
				async:false,
				data: "active=que&userreportid=" + userreportid,  // 设置模式，一般为view ，主关键字值		
				dataType: 'json',
				success: function(datas){
					//var json= eval("("+datas[0].div+")");
					//alert(datas[0].divs);
					if(datas[0].div!=""){
						$('#baoxiaolist').datagrid("loadData",datas[0].div);
						jsonlist=datas[0].jsonval;
						gridmergeCells();
						//alert(jsonlist[0]);
						//alert(jsonlist[3].ex);
					}else{
						$('#baoxiaolist').datagrid({
							url: './baoxiaodatas.json'
						});
					}
					//$('#showreport').html(datas[0].div);
					//loadGrid();
				}
			});
		}
		
		$('#form1').form({
			//定位到servlet位置的url
			url:'<%=request.getContextPath()%>/Svl_userreportinfo', 
			//当点击事件后触发的事件
		    onSubmit: function(data){
		    	//alert();
		    }, 
		    //当点击事件成功提交后触发的事件
		    success:function(data){
				location.href="<%=request.getContextPath()%>/JSONDrawExcel/ReportExcel.jsp?userreportid="+userreportid;
		    }
		});
		
		
		
		function loadGrid(){
			$('#baoxiaolist').datagrid({
				singleSelect:true,
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
				
				rowStyler: function(index,row){
					if (row.隐藏标识=="TITLE"||row.隐藏标识=="MAIN"||row.隐藏标识=="LIST"||row.隐藏标识=="COLLECT"){
						$('#baoxiaolist').datagrid('mergeCells',{
		                    field: "标识",
		                    index: index,
		                    colspan:27
	                	});
						return 'background-color:#6293BB;color:#fff;';
					}
				},
				toolbar: [{
					text:'保存并预览',
					iconCls:'icon-save',
					handler:function(){
						//alert($('#baoxiaolist').datagrid("getData").rows.A);
						$("#active").val("add");
						$("#userreportid").val(userreportid);
						$("#jsonlist").val(json2str(jsonlist));
						//alert($("#jsonlist").val());
						$("#form1").submit();
					}
				},{
					iconCls: 'icon-edit',
					text:'图形报表',
					handler: function(){
						//document.getElementById("charttr").style.display = "none";
						//$("#divchart").hide();
						$("form2").form('clear');
						$("#chartedit").window('open');
						$("#chartedit").window('center');
					}
				}],
				columns:[[
					{field:'隐藏标识',title:'隐藏标识',width:100,hidden:true},
					{field:'标识',title:'标识',width:100},
					{field:'A',title:'A',width:100,editor:'text',
						formatter: function(value,row,index){
						if(value!=undefined){
							if(value.substring(0,2)=="V_"){
								return value.substring(value.indexOf(".")+1,value.length);
								}else{
									return value;
								}
							}
						}
					},
					{field:'B',title:'B',width:100,editor:'text',
						formatter: function(value,row,index){
						if(value!=undefined){
							if(value.substring(0,2)=="V_"){
								return value.substring(value.indexOf(".")+1,value.length);
								}else{
									return value;
								}
							}
						}},
					{field:'C',title:'C',width:100,editor:'text',
						formatter: function(value,row,index){
						if(value!=undefined){
							if(value.substring(0,2)=="V_"){
								return value.substring(value.indexOf(".")+1,value.length);
								}else{
									return value;
								}
							}
						}},
					{field:'D',title:'D',width:100,editor:'text',
						formatter: function(value,row,index){
						if(value!=undefined){
							if(value.substring(0,2)=="V_"){
								return value.substring(value.indexOf(".")+1,value.length);
								}else{
									return value;
								}
							}
						}},
					{field:'E',title:'E',width:100,editor:'text',
						formatter: function(value,row,index){
						if(value!=undefined){
							if(value.substring(0,2)=="V_"){
								return value.substring(value.indexOf(".")+1,value.length);
								}else{
									return value;
								}
							}
						}},
					
					{field:'F',title:'F',width:100,editor:'text',
						formatter: function(value,row,index){
						if(value!=undefined){
							if(value.substring(0,2)=="V_"){
								return value.substring(value.indexOf(".")+1,value.length);
								}else{
									return value;
								}
							}
						}},
					{field:'G',title:'G',width:100,editor:'text',
						formatter: function(value,row,index){
						if(value!=undefined){
							if(value.substring(0,2)=="V_"){
								return value.substring(value.indexOf(".")+1,value.length);
								}else{
									return value;
								}
							}
						}},
					{field:'H',title:'H',width:100,editor:'text',
						formatter: function(value,row,index){
						if(value!=undefined){
							if(value.substring(0,2)=="V_"){
								return value.substring(value.indexOf(".")+1,value.length);
								}else{
									return value;
								}
							}
						}},
					{field:'I',title:'I',width:100,editor:'text',
						formatter: function(value,row,index){
						if(value!=undefined){
							if(value.substring(0,2)=="V_"){
								return value.substring(value.indexOf(".")+1,value.length);
								}else{
									return value;
								}
							}
						}},
					{field:'J',title:'J',width:100,editor:'text',
						formatter: function(value,row,index){
						if(value!=undefined){
							if(value.substring(0,2)=="V_"){
								return value.substring(value.indexOf(".")+1,value.length);
								}else{
									return value;
								}
							}
						}},
					
					{field:'K',title:'K',width:100,editor:'text',
						formatter: function(value,row,index){
						if(value!=undefined){
							if(value.substring(0,2)=="V_"){
								return value.substring(value.indexOf(".")+1,value.length);
								}else{
									return value;
								}
							}
						}},
					{field:'L',title:'L',width:100,editor:'text',
						formatter: function(value,row,index){
						if(value!=undefined){
							if(value.substring(0,2)=="V_"){
								return value.substring(value.indexOf(".")+1,value.length);
								}else{
									return value;
								}
							}
						}},
					{field:'M',title:'M',width:100,editor:'text',
						formatter: function(value,row,index){
						if(value!=undefined){
							if(value.substring(0,2)=="V_"){
								return value.substring(value.indexOf(".")+1,value.length);
								}else{
									return value;
								}
							}
						}},
					{field:'N',title:'N',width:100,editor:'text',
						formatter: function(value,row,index){
						if(value!=undefined){
							if(value.substring(0,2)=="V_"){
								return value.substring(value.indexOf(".")+1,value.length);
								}else{
									return value;
								}
							}
						}},
					{field:'O',title:'O',width:100,editor:'text',
						formatter: function(value,row,index){
						if(value!=undefined){
							if(value.substring(0,2)=="V_"){
								return value.substring(value.indexOf(".")+1,value.length);
								}else{
									return value;
								}
							}
						}},
					
					{field:'P',title:'P',width:100,editor:'text',
						formatter: function(value,row,index){
						if(value!=undefined){
							if(value.substring(0,2)=="V_"){
								return value.substring(value.indexOf(".")+1,value.length);
								}else{
									return value;
								}
							}
						}},
					{field:'Q',title:'Q',width:100,editor:'text',
						formatter: function(value,row,index){
						if(value!=undefined){
							if(value.substring(0,2)=="V_"){
								return value.substring(value.indexOf(".")+1,value.length);
								}else{
									return value;
								}
							}
						}},
					{field:'R',title:'R',width:100,editor:'text',
						formatter: function(value,row,index){
						if(value!=undefined){
							if(value.substring(0,2)=="V_"){
								return value.substring(value.indexOf(".")+1,value.length);
								}else{
									return value;
								}
							}
						}},
					{field:'S',title:'S',width:100,editor:'text',
						formatter: function(value,row,index){
						if(value!=undefined){
							if(value.substring(0,2)=="V_"){
								return value.substring(value.indexOf(".")+1,value.length);
								}else{
									return value;
								}
							}
						}},
					{field:'T',title:'T',width:100,editor:'text',
						formatter: function(value,row,index){
						if(value!=undefined){
							if(value.substring(0,2)=="V_"){
								return value.substring(value.indexOf(".")+1,value.length);
								}else{
									return value;
								}
							}
						}},
					
					{field:'U',title:'U',width:100,editor:'text',
						formatter: function(value,row,index){
						if(value!=undefined){
							if(value.substring(0,2)=="V_"){
								return value.substring(value.indexOf(".")+1,value.length);
								}else{
									return value;
								}
							}
						}},
					{field:'V',title:'V',width:100,editor:'text',
						formatter: function(value,row,index){
						if(value!=undefined){
							if(value.substring(0,2)=="V_"){
								return value.substring(value.indexOf(".")+1,value.length);
								}else{
									return value;
								}
							}
						}},
					{field:'W',title:'W',width:100,editor:'text',
						formatter: function(value,row,index){
						if(value!=undefined){
							if(value.substring(0,2)=="V_"){
								return value.substring(value.indexOf(".")+1,value.length);
								}else{
									return value;
								}
							}
						}},
					{field:'X',title:'X',width:100,editor:'text',
						formatter: function(value,row,index){
						if(value!=undefined){
							if(value.substring(0,2)=="V_"){
								return value.substring(value.indexOf(".")+1,value.length);
								}else{
									return value;
								}
							}
						}},
					{field:'Y',title:'Y',width:100,editor:'text',
						formatter: function(value,row,index){
						if(value!=undefined){
							if(value.substring(0,2)=="V_"){
								return value.substring(value.indexOf(".")+1,value.length);
								}else{
									return value;
								}
							}
						}},
					{field:'Z',title:'Z',width:100,editor:'text',
						formatter: function(value,row,index){
						if(value!=undefined){
							if(value.substring(0,2)=="V_"){
								return value.substring(value.indexOf(".")+1,value.length);
								}else{
									return value;
								}
							}
						}}
				]],
				onClickCell: function(index,field,value){
					endEditing();
					var ycbs=$(this).datagrid('getRows')[index].隐藏标识;
					curycbs=ycbs;
					if(ycbs!='LIST'&&ycbs!='TITLE'&&ycbs!='MAIN'&&ycbs!='COLLECT'){
						if(type==ycbs&&mokuai!=""){
							var data="{"+field+":'"+mokuai+"'}";
							var datas=eval("("+data+")");
							if(ycbs=="main"){
								if(list.indexOf(field)-1>-1){
									$('#baoxiaolist').datagrid('updateRow',{
										index: index,
										row:datas
									});
									//
									PDJson(field,index,mokuai,"sj",ycbs);
									mokuai=mokuai.substring(mokuai.indexOf(".")+1,mokuai.length);
									data="{"+list[list.indexOf(field)-1]+":'"+mokuai+"'}";
									datas=eval("("+data+")");
									$('#baoxiaolist').datagrid('updateRow',{
										index: index,
										row:datas
									});
									//
									PDJson(list[list.indexOf(field)-1],index,mokuai,"zd",ycbs);
								}else{
									data="{"+list[list.indexOf(field)+1]+":'"+mokuai+"'}";
									datas=eval("("+data+")");
									$('#baoxiaolist').datagrid('updateRow',{
										index: index,
										row:datas
									});
									//
									PDJson(list[list.indexOf(field)+1],index,mokuai,"sj",ycbs);
									mokuai=mokuai.substring(mokuai.indexOf(".")+1,mokuai.length);
									data="{"+list[list.indexOf(field)]+":'"+mokuai+"'}";
									datas=eval("("+data+")");
									$('#baoxiaolist').datagrid('updateRow',{
										index: index,
										row:datas
									});
									//
									PDJson(field,index,mokuai,"zd",ycbs);
								}
							}
							
							if(ycbs=="list"){
								if($(this).datagrid('getRows')[index-1].隐藏标识=="list"){
									$('#baoxiaolist').datagrid('updateRow',{
										index: index,
										row:datas
									});
									//
									PDJson(field,index,mokuai,"sj",ycbs);
									mokuai=mokuai.substring(mokuai.indexOf(".")+1,mokuai.length);
									data="{"+field+":'"+mokuai+"'}";
									datas=eval("("+data+")");
									$('#baoxiaolist').datagrid('updateRow',{
										index: index-1,
										row:datas
									});
									//
									PDJson(field,index-1,mokuai,"zd",ycbs);
								}else{
									$('#baoxiaolist').datagrid('updateRow',{
										index: index+1,
										row:datas
									});
									//
									PDJson(field,index+1,mokuai,"sj",ycbs);
									mokuai=mokuai.substring(mokuai.indexOf(".")+1,mokuai.length);
									data="{"+field+":'"+mokuai+"'}";
									datas=eval("("+data+")");
									$('#baoxiaolist').datagrid('updateRow',{
										index: index,
										row:datas
									});
									//
									PDJson(field,index,mokuai,"zd",ycbs);
								}
							}
							
							if(ycbs=="collect"){
								if(list.indexOf(field)-1>-1){
									$('#baoxiaolist').datagrid('updateRow',{
										index: index,
										row:datas
									});
									//
									PDJson(field,index,mokuai,"sj",ycbs);
									mokuai=mokuai.substring(mokuai.indexOf(".")+1,mokuai.length);
									data="{"+list[list.indexOf(field)-1]+":'"+mokuai+"'}";
									datas=eval("("+data+")");
									$('#baoxiaolist').datagrid('updateRow',{
										index: index,
										row:datas
									});
									//
									PDJson(list[list.indexOf(field)-1],index,mokuai,"zd",ycbs);
								}else{
									data="{"+list[list.indexOf(field)+1]+":'"+mokuai+"'}";
									datas=eval("("+data+")");
									
									$('#baoxiaolist').datagrid('updateRow',{
										index: index,
										row:datas
									});
									//
									PDJson(list[list.indexOf(field)+1],index,mokuai,"sj",ycbs);
									mokuai=mokuai.substring(mokuai.indexOf(".")+1,mokuai.length);
									data="{"+list[list.indexOf(field)]+":'"+mokuai+"'}";
									datas=eval("("+data+")");
									$('#baoxiaolist').datagrid('updateRow',{
										index: index,
										row:datas
									});
									//
									PDJson(field,index,mokuai,"zd",ycbs);
								}
							}
							mokuai="";
						}
						gridmergeCells();
					}
				},
				onRowContextMenu:function(e, rowIndex, rowData,field){
					if(rowData.隐藏标识!="LIST"&&rowData.隐藏标识!="TITLE"&&rowData.隐藏标识!="MAIN"&&rowData.隐藏标识!="COLLECT"){
						curfield=field;
						//alert(field);
						curindex=rowIndex;
						curycbs=rowData.隐藏标识;
						e.preventDefault();
						
						$('#reportrem').menu('show', {
		                    left: e.pageX,
		                    top: e.pageY
		                });
	                }
				},
				onClickRow:function(rowIndex, rowData){
					curindex=rowIndex;
				},
				onDblClickCell: function(index,field,value){
					//alert(field);
					for(var i=0;i<jsonlist.length;i++){
	        			if(jsonlist[i].X==field&&jsonlist[i].Y==index&&jsonlist[i].type=="sj"){
	        				return;
	        			}
	        		}
        		
					if (endEditing()){
						var ycbs=$(this).datagrid('getRows')[index].隐藏标识;
						curfile=field;
						if(ycbs!='LIST'&&ycbs!='TITLE'&&ycbs!='MAIN'&&ycbs!='COLLECT'){
							$(this).datagrid('selectRow', index).datagrid('editCell', {index:index,field:field});
							
						}
						editIndex = index;
           			}
				},
				onAfterEdit:function(rowIndex, rowData, changes){
					if(curfile!="标识"){
						PDJson(curfile,rowIndex,rowData[curfile],"zd",rowData.隐藏标识);
					}
				},
				onLoadSuccess:function(){
					//loadjson();
				}
			});
			//doToolbarAuth();
		}
        var lastid="";
		function hqnr(id,type){
			//alert($("#inplist1").val());
			this.type=type;
			mokuai=$("#inp"+type+id.substring(4,id.length)).val();
			$("#"+id).css("background","#FBEC88");
			$("#"+lastid).css("background","#efefef");
			lastid=id;
		}
		
		       
		
		function PDJson(x,y,value,type,qy){
        	if(jsonlist==""){
				jsonvalue={"X":x,"Y":y,"value":value,"type":type,"qy":qy,"ex":x,"ey":y};
				jsonlist[0]=jsonvalue;				
        	}else{
        		for(var i=0;i<jsonlist.length;i++){
        			if(jsonlist[i].X==x&&jsonlist[i].Y==y){
        				jsonlist[i].value=value;
        				jsonlist[i].type=type;
        				//alert();
        				//alert(jsonlist[i].value);
        				return;
        			}
        		}
        		//alert();
        		jsonvalue={"X":x,"Y":y,"value":value,"type":type,"qy":qy,"ex":x,"ey":y};
        		jsonlist[jsonlist.length]=jsonvalue;
        	}
        	//alert(jsonlist[0].ex);
        }
         
        
		//修复
		(function ($) {
            $.extend($.fn.datagrid.defaults.view, {
                insertRow: function (target, index, row) {
                    var state = $.data(target, 'datagrid');
                    var opts = state.options;
                    var dc = state.dc;
                    var data = state.data;
 
                    if (index == undefined || index == null) index = data.rows.length;
                    if (index > data.rows.length) index = data.rows.length;
 
                    for (var i = data.rows.length - 1; i >= index; i--) {
                        var tr2 = opts.finder.getTr(target, i, 'body', 2);
                        tr2.attr('datagrid-row-index', i + 1);
                        tr2.attr('id', state.rowIdPrefix + '-2-' + (i + 1));
                        var tr1 = opts.finder.getTr(target, i, 'body', 1);
                        tr1.attr('datagrid-row-index', i + 1);
                        tr1.attr('id', state.rowIdPrefix + '-1-' + (i + 1));
                        if (opts.rownumbers) {
                            tr1.find('div.datagrid-cell-rownumber').html(i + 2);
                        }
                    }

                    var fields1 = $(target).datagrid('getColumnFields', true);
                    var fields2 = $(target).datagrid('getColumnFields', false);
                    var rowId1 = state.rowIdPrefix + '-1-' + index;
                    var rowId2 = state.rowIdPrefix + '-2-' + index;
                    var tr1 = '<tr id="' + rowId1 + '" class="datagrid-row" datagrid-row-index="' + index + '">' + this.renderRow.call(this, target, fields1, true, index, row) + '</tr>';
                    var tr2 = '<tr id="' + rowId2 + '" class="datagrid-row" datagrid-row-index="' + index + '">' + this.renderRow.call(this, target, fields2, false, index, row) + '</tr>';
                    if (index >= data.rows.length) {	// append new row
                        if (data.rows.length) {	// not empty
                            opts.finder.getTr(target, '', 'last', 1).after(tr1);
                            opts.finder.getTr(target, '', 'last', 2).after(tr2);
                        } else {
                            dc.body1.html('<table cellspacing="0" cellpadding="0" border="0"><tbody>' + tr1 + '</tbody></table>');
                            dc.body2.html('<table cellspacing="0" cellpadding="0" border="0"><tbody>' + tr2 + '</tbody></table>');
                        }
                    } else {	// insert new row
                        opts.finder.getTr(target, index + 1, 'body', 1).before(tr1);
                        opts.finder.getTr(target, index + 1, 'body', 2).before(tr2);
                    }
 
                    data.total += 1;
                    data.rows.splice(index, 0, row);
 
                    this.refreshRow.call(this, target, index);
                },
 
                deleteRow: function (target, index) {
                    var state = $.data(target, 'datagrid');
                    var opts = state.options;
                    var data = state.data;
 
                    opts.finder.getTr(target, index).remove();
                    for (var i = index + 1; i < data.rows.length; i++) {
                        var tr2 = opts.finder.getTr(target, i, 'body', 2);
                        tr2.attr('datagrid-row-index', i - 1);
                        tr2.attr('id', state.rowIdPrefix + '-2-' + (i - 1));
                        var tr1 = opts.finder.getTr(target, i, 'body', 1);
                        tr1.attr('id', state.rowIdPrefix + '-1-' + (i - 1));
                        tr1.attr('datagrid-row-index', i - 1);
                        if (opts.rownumbers) {
                            tr1.find('div.datagrid-cell-rownumber').html(i);
                        }
                    }
 
                    data.total -= 1;
                    data.rows.splice(index, 1);
                },
                onCellContextMenu:function(target, index, row){
                
                }
            });
        })(jQuery);

		function doToolbar(id){
			if(id=="ok"){
				$("#active2").val("create");
				
				//alert(userreportid);
				$("#chartuserreportid").val(userreportid);
				$("#form2").submit();
			}
			if(id=="quedin"){
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
	  		if(id=="edit"){
	  			$("#chartgl").window("open");
	  		}
	  		if(id=="savejt"){
	  				$("#icchartuserreportid").val(userreportid);
					$("#icchartxml").val(chartxml);
					$("#icchartname").val(chartname);
					$("#iccharttype").val(charttype);
					$("#active3").val("save");
					$("#chartshow").window("open");
	  		}
		}
		
		$('#form2').form({
			//定位到servlet位置的url
			url:'<%=request.getContextPath()%>/Svl_Chart', 
			//当点击事件后触发的事件
		    onSubmit: function(data){
		    	//alert();
		    }, 
		    //当点击事件成功提交后触发的事件
		    success:function(data){
		    	//alert();
		    	//alert(data);
				if($("#active2").val()=="create"){
					$("#savejt").linkbutton('enable');
					$("#edit").linkbutton('enable');
					$("#chartiframe").show();
					var json = eval("("+data+")");
					var datas=json[0].chart;
					chartsql=json[0].chartsql;
					
					datas=datas.replace(/-/g,">");
					datas=datas.replace(/\+/g,"<");
					datas=datas.replace(/\"/g,"'");
					chartsql=chartsql.replace(/\+z\+/g,"<");
					chartsql=chartsql.replace(/-y-/g,">");
					
					//chartsql=chartsql.replace(/\"/g,"'");
					//data=data.replace(/+/g,"<");
					//data=data.replace(/-/g,">");
					//				
					
					$("#chartxml").val(datas);
					$("#chartsql").val(chartsql);
					
					chartxml=datas;
					charttype=$("#type").combobox('getValue');
					chartname=$("#chartname").val();
					window.frames["chartiframe"].chartsqls(chartsql);
					window.frames["chartiframe"].charttypes(charttype);
					window.frames["chartiframe"].chartsxs("");
					if(charttype=="001"){
						//alert(datas);	
						window.frames["chartiframe"].Column3D(datas);
					}
					if(charttype=="002"){
						window.frames["chartiframe"].Pie3D(datas);
					}
					if(charttype=="003"){
						window.frames["chartiframe"].Line2D(datas);
					}
					if(charttype=="004"){
						window.frames["chartiframe"].MSColumn2D(datas);
					}
					$("#active2").val("save");
					$("#form2").submit();
				}
				if($("#active2").val()=="save"){
					showMsg("保存成功");
					$("#chartreportList").datagrid("reload");
					//$("#chartedit").window('close');
				}
		    }
		});
  </script>
  
</html>