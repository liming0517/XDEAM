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

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> 
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
  </head>
  
  <body>
    <table id="auditxf" name="auditxf"></table>
  </body>
  <div id="gwfkedit" name="gwfkedit" title="公文反馈" class="easyui-window"  style="width:450px" resizable=true closed=true modal=true>
		<center>
			<textarea rows="8" id='gwfk' name='gwfk' style='width:100%'></textarea>
		</center>
  </div>
  <script type="text/javascript">
  	var ywzj='<%=MyTools.StrFiltr(request.getParameter("ywzj"))%>';
  	var tablename='V_报表信息主表';
  	var ikeycodename='模版id';
  	var xfid="";
  	//var sffk='';
 	$(document).ready(function(){
		loadGrid();
	});
 	
 	$('#gwfkedit').dialog({
		toolbar:[{
			text:'保存',
			iconCls:'icon-save',
			handler:function(){
	 			$.ajax({
					type: "POST",
					url: '<%=request.getContextPath()%>/Svl_AuditConduct1',   //设置 SVL
					async:false,
					data: "active=gwfk&xfid="+xfid+"&gwfk="+$('#gwfk').val(),  // 设置模式，一般为view ，主关键字值		
					dataType: 'json',
					success: function(datas){
						$('#gwfkedit').window('close');
						loadGrid();
					}
				});
			}
		}]
	});
 	
	
 	function loadGrid(){
		$('#auditxf').datagrid({
			title:'公文下发执行',
			iconCls:'icon-edit',
			singleSelect:true,
			width: '100%',
			nowrap: false,
			fit:true,
			showFooter:true,
			striped:true, //隔行变色
			pagination:true,
			pageSize:10,
			singleSelect:false,
			pageNumber:1,
			rownumbers:true,
			fitColumns: true,
			url: '<%=request.getContextPath()%>/Svl_AuditConduct1',
			queryParams:{"active":"gwxflist","tablename":tablename,"ikeycodename":ikeycodename},
			toolbar:[
				{
					text:'详情',
					iconCls:'icon-ok',
					handler:function(){
						
					}
				}
			],
			columns:[[
				{field:'公文ID',title:'公文ID',width:100,hidden:true},
				{field:'名称',title:'公文名称',width:100},
				{field:'状态',title:'状态',width:100,formatter: function(value,row,index){
						if(value=='0'){
							return '待执行';
						}	
						if(value=='1'){
							return '执行中';
						}
						if(value=='2'){
							return '已反馈';
						}
					}
				},
				{field:'操作',title:'操作',width:100,formatter: function(value,row,index){
						var btn="";
						if(row.状态=='0'){
							btn="<input type='button' onclick='showsp(\""+row.状态+"\",\""+row.下发ID+"\")' value='执行'>";
						}	
						if(row.状态=='1'){
							btn="<input type='button' onclick='showsp(\""+row.状态+"\",\""+row.下发ID+"\")' value='反馈'>";
						}
						if(row.状态=='2'){
							btn="<input type='button' onclick='showsp(\""+row.状态+"\",\""+row.反馈内容+"\")' value='详情'>";
						}
						return 	btn;
					}	
				}
			]],
			onClickRow:function(rowIndex,rowData){
				
			},
			onLoadSuccess:function(){

			}
		});
	}
 	
 	 function onsubmits(sffk){
			var row="";
			var rows = $('#auditxf').datagrid('getSelections');
			//alert(rows[1]);
            for(var i=0; i<rows.length; i++){
            	if(i!=0){
            		row += ";";
            	}
                row += rows[i].用户编号;
            }
			$.ajax({
				type: "POST",
				url: '<%=request.getContextPath()%>/Svl_AuditConduct1',   //设置 SVL
				async:false,
				data: "active=upuser&row=" + row+"&ywzj="+ywzj+"&sffk="+sffk,  // 设置模式，一般为view ，主关键字值		
				dataType: 'json',
				success: function(datas){
					window.opener.location.reload(); 
					window.close();
				}
			});
 	 }

 	 function showsp(id,xfid){
 		 if(id=='0'){
 			$.ajax({
				type: "POST",
				url: '<%=request.getContextPath()%>/Svl_AuditConduct1',   //设置 SVL
				async:false,
				data: "active=dozx&xfid="+xfid,  // 设置模式，一般为view ，主关键字值		
				dataType: 'json',
				success: function(datas){
					loadGrid();
				}
			});
 		 }
 		 if(id=='1'){
 			 this.xfid=xfid;
 			 $('#gwfkedit').window('open');
 		 }
 		 if(id=='2'){
 			$('#gwfk').val(xfid);
 		 	$('#gwfkedit').dialog({
 				toolbar:[{
 					text:'保存',
 					iconCls:'icon-save',
 					disabled:'true',
 					handler:function(){

 					}
 				}]
 			});
 		 	$('#gwfkedit').window('open');
 		 }
 	 }
 	 
  </script>
</html>
