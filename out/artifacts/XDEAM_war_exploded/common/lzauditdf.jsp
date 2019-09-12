<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>   
    
    <title></title>
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
  
  <body>
   	<table id="auditdf" name="auditdf"></table>
   	<div id='fkqr' name='fkqr' title='是否反馈' class="easyui-window" style="width:300px;height:150px"  resizable=false closed=true modal=true>
    	<div class="easyui-layout" data-options="fit:true">
    		<div data-options="region:'center'" style="padding:10px;">
    			是否接受反馈？
    		</div>
    		<div data-options="region:'south',border:false" style="text-align:right;padding:5px 0 0;">
                <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:void(0)" onclick="onsubmits('Y')">接受</a>
                <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" onclick="onsubmits('N')">拒绝</a>
            </div>
    	</div>
    </div>
  </body>
  <script type="text/javascript">
  	//alert();
  	var ywzj='<%=request.getParameter("ywzj")%>';
  	//alert();
  	$(document).ready(function(){
  		
  		loadGrid();
  	});
  	
  	function loadGrid(){
  		$('#auditdf').datagrid({
				title:'公文流转人员',
				iconCls:'icon-edit',
				width: '100%',
				nowrap: false,
				fit:true,
				showFooter:true,
				striped:true, //隔行变色
				pagination:true,
				pageSize:50,
				singleSelect:false,
				pageNumber:1,
				rownumbers:true,
				fitColumns: true,
				url: '<%=request.getContextPath()%>/Svl_AuditConduct1',
				queryParams:{"active":"queuser","ywzj":ywzj},
				//pagePosition:pos,
				toolbar:[
				{
					text:'添加',
					iconCls:'icon-add',
					handler:function(){
						$('#fkqr').window('open');
						//alert(row);
					}
				}],
				columns:[[
					{field:'用户编号',title:'用户编号',width:100,hidden:true},
					{field:'用户名称',title:'用户名称',width:100}
				]],
				onClickRow:function(rowIndex,rowData){
					//curAuditID=rowData.审核编号;
				},
				onLoadSuccess:function(){
				}
			});
  	}
  	
  	 function onsubmits(sffk){
			var row="";
			var rows = $('#auditdf').datagrid('getSelections');
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
				data: "active=addgwxf&row=" + row+"&ywzj="+ywzj+"&sffk="+sffk,  // 设置模式，一般为view ，主关键字值		
				dataType: 'json',
				success: function(datas){
					window.opener.location.reload(); 
					window.close();
				}
			});
 	 }
  </script>
</html>
