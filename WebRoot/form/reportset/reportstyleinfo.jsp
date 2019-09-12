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
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/colorpicker.css" type="text/css" />
    
	<script type="text/javascript"
		src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
	<script type="text/javascript"
		src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
  	
	<script type="text/javascript"
		src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript"
		src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
		
	
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/colorpicker.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/eye.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/utils.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/layout.js?ver=1.0.2"></script>

  </head>
  <body  class="easyui-layout">
		<div region="center" style="background: #fafafa; width: 100%;">
			<table  id="reportstyleinfoList" name="reportstyleinfoList"></table>
		</div>
  </body>
    <div id="reportstyleedit" name="reportstyleedit" title="样式编辑" class="easyui-window" style="width:500px;"  resizable=false closed=true modal=true>
    	<form id="form1" name="form1" method="POST">
			<center>
				<table class="tablestyle" style="width:100%" cellpadding="4">
					<tr>
						<td>类型</td>
						<td align="left">
							<select
								class="easyui-combobox" name="mc" id="mc" style="width: 150px;">
								<option value="title">标题</option>
								<option value="main">主信息</option>
								<option value="tabletitle">列表抬头</option>
								<option value="table">列表内容</option>
								<option value="collect">汇总</option>
							</select>
						</td>
						<td>字体</td>
						<td align="left">
							<select
								class="easyui-combobox" name="zt" id="zt" style="width: 120px;">
								<option value="宋体">宋体</option>
								<option value="楷体">楷体</option>
								<option value="隶书">隶书</option>
								<option value="黑体">黑体</option>
							</select>
							<span id="ztcss" name="ztcss">字体</span>
						</td>
					</tr>
					<tr>
						<td>字体颜色</td>
						<td align="left">
							<div id="ztyscss" name="ztyscss" style="width:150px;">
								<input id="ztys" name="ztys" onclick="zdfeibx(this.id)" style="width:120px"></input>
							</div>
						</td>
						
						<td>大小</td>
						<td align="left">
							<input id="dx" name="dx" class="easyui-numberspinner" style="width:35px;"></input>
							<span id="dxcss" name="dxcss">大小</span>
						</td>
					</tr>
					<tr>
						<td>是否倾斜</td>
						<td align="left">
							<select
								class="easyui-combobox" name="qx" id="qx" style="width:120px;">
								<option value="true">是</option>
								<option value="false">否</option>
							</select>
							<span id="qxcss" name="qxcss">倾斜</span>
						</td>
						<td>是否加粗</td>
						<td align="left">
							<select
								class="easyui-combobox" name="jc" id="jc" style="width: 100px;">
								<option value="true">是</option>
								<option value="false">否</option>
							</select>
							<span id="jccss" name="jccss">加粗</span>
						</td>
					</tr>
					<tr>
						<td>水平排列</td>
						<td align="left">
							<select
								class="easyui-combobox" name="sppl" id="sppl" style="width: 150px;">
								<option value="xlHAlignLeft">左对齐</option>
								<option value="xlHAlignCenter">居中</option>
								<option value="xlHAlignRight">右对齐</option>
							</select>
						</td>
						
						<td>垂直排列</td>
						<td align="left">
							<select
								class="easyui-combobox" name="czpl" id="czpl" style="width: 150px;">
								<option value="xlVAlignBottom">底部对齐</option>
								<option value="xlVAlignCenter">垂直居中</option>
								<option value="xlVAlignTop">顶部对齐</option>
							</select>
						</td>
					</tr>
					<tr>
						<td>边框类型</td>
						<td align="left">
							<select
								class="easyui-combobox" name="bklx" id="bklx" style="width: 150px;">
								<option value="xlAllEdges">全边框</option>
								<option value="xlBottomEdge">下边框</option>
								<option value="xlDiagonalDown">从左上角至右下角的边框</option>
								<option value="xlDiagonalUp">从左下角至右上角的边框</option>
								<option value="xlFullGrid">全边框线并且包括内部表格线</option>
								<option value="xlLeftEdge">左边框</option>
								<option value="xlNone">无边框</option>
								<option value="xlRightEdge">右边框</option>
								<option value="xlTopEdge">上边框</option>
							</select>
						</td>
						<td>边框颜色</td>
						<td align="left">
							<div id="bkyscss" name="bkyscss" style="width:150px;">
								<input id="bkys" name="bkys" onclick="zdfeibx(this.id)" style="width:120px"></input>
							</div>
						</td>
					</tr>
					<tr>
						<td>边框线形</td>
						<td align="left">
							<select
								class="easyui-combobox" name="bkxx" id="bkxx" style="width: 150px;">
								<option value="xlContinuous">实线</option>
								<option value="xlDash">虚线</option>
								<option value="xlDashDot">点划相间线</option>
								<option value="xlDashDotDot">划线后跟两个点</option>
								<option value="xlDot">点式线</option>
								<option value="xlDouble">双线</option>
								<option value="xlLineStyleNone">无线条</option>
							</select>
						</td>
						<td>边框线大小</td>
						<td align="left">
							<select
								class="easyui-combobox" name="bkxdx" id="bkxdx" style="width: 150px;">
								<option value="xlHairline">最细</option>
								<option value="xlThin">细</option>
								<option value="xlMedium">中等</option>
								<option value="xlThick">粗</option>
							</select>
						</td>
					</tr>
					<tr>
						<td>背景色</td>
						<td align="left">
							<div id="bjscss" name="bjscss" style="width:150px;">
								<input id="bjs" name="bjs" onclick="zdfeibx(this.id)" style="width:120px"></input>
							</div>
						</td>
						<td></td>
						<td></td>
					</tr>
				</table>
				<input type="hidden" id="active" name="active"/>
				<input type="hidden" id="reportstyleinfoid" name="reportstyleinfoid"/>
				<input type="hidden" id="reportstyleid" name="reportstyleid"/>
			</center>
		</form>
  </div>
  <div id="zdfeibx" name="zdfeibx" title="颜色选择" class="easyui-window"   resizable=false closed=true modal=true>
		 <div id="colorpickerHolder"></div>
  </div>
  
  <script type="text/javascript">
		var curreportstyleinfoid="";
		var rows="";
		var reportstyleid='<%=request.getParameter("reportstyleid")%>';
		var curinfoid="";
		var redid="";
		
		$(document).ready(function(){
			loadGrid();
		});
		
		$('#colorpickerHolder').ColorPicker({
			flat: true,
			onSubmit: function(hsb, hex, rgb, el) {
				$("#"+redid).val(rgb.r+","+rgb.g+","+rgb.b);
				$("#"+redid+"css").css("background-color",hex);
				$("#zdfeibx").window("close");
			}
		});
		
		$('#reportstyleedit').dialog({
			toolbar:[{
				text:'保存',
				iconCls:'icon-save',
				handler:function(){
					$('#form1').submit();
				}
			}]
				
		});
		
		$('#form1').form({
			//定位到servlet位置的url
			url:'<%=request.getContextPath()%>/Svl_reportStyleinfo', 
			//当点击事件后触发的事件
		    onSubmit: function(data){
		    	//alert();
		    }, 
		    //当点击事件成功提交后触发的事件
		    success:function(data){
				var json = eval("("+data+")");
				$("#reportstyleedit").window('close');
				loadGrid();
		    }
		});
	
		
		$('#zt').combobox({
		    onSelect: function(data){
				//alert(data.value);
				$("#ztcss").css("font-family",data.value);
				
		    }
		});
		
		$('#qx').combobox({
		    onSelect: function(data){
				if(data.value=="true"){
					$("#qxcss").css("font-style","italic");
				}else{
					$("#qxcss").css("font-style","normal");
				}
				
		    }
		});
		
		$('#jc').combobox({
		    onSelect: function(data){
				//alert(data.value);
				if(data.value=="true"){
					$("#jccss").css("font-weight","bold");
				}else{
					$("#jccss").css("font-weight","normal");
				}
				
		    }
		});
		$('#dx').numberspinner({
		    min:12,
		    value:12,
		    max:72,
		    onChange:function(newValue,oldValue){
		    	//alert(newValue);
		    	$("#dxcss").css("font-size",newValue);
		    }
		    
		});

		$('#bkxx').combobox({
		    formatter:function(row){
				return "<img src='../../images/linetype/"+row.value+".jpg'/>";
		    }
		});
		
		$('#bklx').combobox({
		    formatter:function(row){
				return "<img src='../../images/line/"+row.value+".jpg'/>"+row.text;
		    }
		});
		
		$('#bkxdx').combobox({
		    formatter:function(row){
				return "<img src='../../images/linesize/"+row.value+".jpg'/>"+row.text;
		    }
		});
		
		function loadGrid(){
			$('#reportstyleinfoList').datagrid({
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
				title:'样式详情列表',
				url: '<%=request.getContextPath()%>/Svl_reportStyleinfo',
				queryParams:{"active":"que","reportstyleid":reportstyleid},
				toolbar: [{
					iconCls: 'icon-add',
					text:'添加',
					handler: function(){
						doToolbar("add");
					}
				},{
					text:'修改',
					iconCls:'icon-edit',
					handler:function(){
						doToolbar("edit");
					}
				},{
					text:'预览',
					iconCls:'icon-edit',
					handler:function(){
						location.href="<%=request.getContextPath()%>/JSONDrawExcel/index.jsp?reportstyleid="+reportstyleid;
					}
				},{
					text:'删除',
					iconCls:'icon-cancel',
					handler:function(){
						doToolbar("cancel");
					}
				}],
				columns:[[
					{field:'类型',title:'类型',width:100,formatter: function(value,row,index){
						if (value=="title"){
							return "标题";
						}
						if (value=="main"){
							return "主信息";
						}
						if (value=="tabletitle"){
							return "列表抬头";
						}
						if (value=="table"){
							return "列表内容";
						}
						if (value=="collect"){
							return "汇总";
						}
					}},
					{field:'字体',title:'字体',width:100},
					{field:'字体颜色',title:'字体颜色',width:100},
					{field:'是否加粗',title:'是否加粗',width:100,formatter: function(value,row,index){
						if (value=="true"){
							return "是";
						}
						if (value=="false"){
							return "否";
						}
					}},
					{field:'是否倾斜',title:'是否倾斜',width:100,formatter: function(value,row,index){
						if (value=="true"){
							return "是";
						}
						if (value=="false"){
							return "否";
						}
					}},
					{field:'字体大小',title:'字体大小',width:100},
					{field:'水平排列',title:'水平排列',width:100,formatter: function(value,row,index){
						if (value=="xlHAlignLeft"){
							return "左对齐";
						}
						if (value=="xlHAlignCenter"){
							return "居中";
						}
						if (value=="xlHAlignRight"){
							return "右对齐";
						}
					}},
					{field:'垂直排列',title:'垂直排列',width:100,formatter: function(value,row,index){
						if (value=="xlVAlignBottom"){
							return "底部对齐";
						}
						if (value=="xlVAlignCenter"){
							return "垂直居中";
						}
						if (value=="xlVAlignTop"){
							return "顶部对齐";
						}
					}},
					{field:'边框类型',title:'边框类型',width:100,formatter: function(value,row,index){
						if (value=="xlAllEdges"){
							return "全边框";
						}
						if (value=="xlBottomEdge"){
							return "下边框";
						}
						if (value=="xlDiagonalDown"){
							return "从左上角至右下角的边框";
						}
						if (value=="xlDiagonalUp"){
							return "从左下角至右上角的边框";
						}
						if (value=="xlFullGrid"){
							return "全边框线并且包括内部表格线";
						}
						if (value=="xlLeftEdge"){
							return "左边框";
						}
						
						if (value=="xlNone"){
							return "无边框";
						}
						if (value=="xlRightEdge"){
							return "右边框";
						}
						if (value=="xlTopEdge"){
							return "上边框";
						}
					}},
					{field:'边框样式',title:'边框颜色',width:100},
					{field:'边框线型',title:'边框线型',width:100,formatter: function(value,row,index){
						if (value=="xlContinuous"){
							return "实线";
						}
						if (value=="xlDash"){
							return "虚线";
						}
						if (value=="xlDashDot"){
							return "点划相间线";
						}
						if (value=="xlDashDotDot"){
							return "划线后跟两个点";
						}
						if (value=="xlDot"){
							return "点式线";
						}
						if (value=="xlDouble"){
							return "双线";
						}
						
						if (value=="xlLineStyleNone"){
							return "无线条";
						}
						
					}},
					{field:'边框大小',title:'边框线大小',width:100,formatter: function(value,row,index){
						if (value=="xlHairline"){
							return "最细";
						}
						if (value=="xlThin"){
							return "细";
						}
						if (value=="xlMedium"){
							return "中等";
						}
						if (value=="xlThick"){
							return "粗";
						}	
					}},
					{field:'背景色',title:'背景色',width:100}
				]],
				onClickRow:function(rowIndex,rowData){
					curreportstyleinfoid=rowData.主键;
				},
				onLoadSuccess:function(){
				
				}
			});
		}
		
		function doToolbar(iToolbar) {
	  		if(iToolbar=="add"){
	  			/*
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
	  			*/
	  			$('#form1').form('clear');
	  			$("#active").val("add");
	  			$("#reportstyleid").val(reportstyleid);
	  			$("#reportstyleedit").window('open');
	  		}
	  		if(iToolbar=="edit"){
	  			if(curreportstyleinfoid!=""){
	  				rows = $('#reportstyleinfoList').datagrid('getSelected');
	  				$('#mc').combobox('select', rows.类型);
	  				$('#zt').combobox('select', rows.字体);
	  				$('#ztys').val(rows.字体颜色);
	  				//$('#ztyscss').ColorPickerSetColor({r:255, r:0, b:0});
	  				$('#dx').numberspinner('setValue', rows.字体大小);
	  				$('#qx').combobox('select', rows.是否倾斜);
	  				$('#jc').combobox('select', rows.是否加粗);
	  				$('#sppl').combobox('select', rows.水平排列);
	  				$('#czpl').combobox('select', rows.垂直排列);
	  				$('#bklx').combobox('select', rows.边框类型);
	  				$('#bkys').val(rows.边框样式);
	  				$('#bkxx').combobox('select', rows.边框线型);
	  				$('#bkxdx').combobox('select', rows.边框大小);
	  				$('#bjs').val(rows.背景色);
	  				//alert(curreportstyleinfoid);
	  				$("#reportstyleinfoid").val(curreportstyleinfoid);
	  				$("#reportstyleid").val(reportstyleid);
	  				$("#active").val("edit");
	  				
	  				$("#reportstyleedit").window('open');
	  			}else{
	  				alertMsg("请选择一条数据");
	  			}
	  		}
	  		if(iToolbar=="cancel"){
	  			if(curreportstyleinfoid!=""){
					$.ajax({
						type: "POST",
						url: '<%=request.getContextPath()%>/Svl_reportStyleinfo',   //设置 SVL
						async:false,
						data: "active=cancel&reportstyleinfoid=" + curreportstyleinfoid,  // 设置模式，一般为view ，主关键字值		
						dataType: 'json',
						success: function(datas){
							loadGrid();
							showMsg("删除成功");
						}
					});
					
	  			}else{
	  				alertMsg("请选择一条数据");
	  			}
	  		}
  		}

		function zdfeibx(id){
			redid=id;
			$("#zdfeibx").window('open');
		}
		
	</script>
</html>
