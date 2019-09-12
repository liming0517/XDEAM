<%
/**
* @Description 批量导入试题 
* @date 2015-04-09
* @author lupengfei 
* @version V1.0  
*/
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.pantech.base.common.tools.MyTools"%>
<%@page import="com.pantech.src.develop.store.user.*"%>
<%@page import="com.pantech.base.common.tools.PublicTools"%>
<%@page import="java.net.URLEncoder" %>
<%@page import="java.net.URLDecoder"%>
<% String importType = URLDecoder.decode(request.getParameter("importType"),"utf-8");
   String xnxq = MyTools.StrFiltr(request.getParameter("xnxq"));
   String qzqm = MyTools.StrFiltr(request.getParameter("qzqm"));
   String xnxqtext = URLDecoder.decode(request.getParameter("xnxqtext"),"utf-8");
   String qzqmtext = URLDecoder.decode(request.getParameter("qzqmtext"),"utf-8");
 %>
<html>
	<head>
		<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/icon.css">
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
	</head>
<form id="form1" name="form1" method="POST"
		action="<%=request.getContextPath()%>/Svl_Import" enctype="multipart/form-data">
	<body class="easyui-layout">
	<div id="importfile" title="" style="">
			<table width="336px" class="tablestyle">
				<tr>
					<td colspan=2 align="left">
					<a href="#"
						onclick="doToolbar(this.id)" id="upload" class="easyui-linkbutton" plain="true" iconcls="icon-save">导入数据</a></td>
				</tr>
				<tr>
					<td class="titlestyle" width="25%">学年学期</td>
					<td class="titlestyle" width="75%" id="JK_XNXQ" name="JK_XNXQ"></td>
				</tr>
				<tr>
					<td class="titlestyle">考试周期</td>
					<td class="titlestyle" id="JK_QZQM" name="JK_QZQM"></td>
				</tr>
				<tr>
					<td colspan=2 ><input type="file" name="excel1" id="excel1" width="100%">
					</td>
				</tr>
				
			</table>
	</div>

	<input type="hidden" id="ic_importType" name="ic_importType" />
	<input type="hidden" id="ic_xnxq" name="ic_xnxq" />
	<input type="hidden" id="ic_qzqm" name="ic_qzqm" />
	<input type="hidden" id="active" name="active" />
	</body>
	</form>
	
<script language="JavaScript" type="text/JavaScript">
	var choose0=0;
	var term="";
	var importType="<%=importType%>";
	var xnxq="<%=xnxq%>";
	var qzqm="<%=qzqm%>";
	var xnxqtext="<%=xnxqtext%>";
	var qzqmtext="<%=qzqmtext%>";
	var pageNum = 1;   //datagrid初始当前页数
	var pageSize = 20; //datagrid初始页内行数
	var qzqmzq=""; 
			
	//页面初始化
	$(document).ready(function(){
		$('#ic_importType').val(importType);
		$('#ic_xnxq').val(xnxq);
		qzqmzq=qzqmtext.replace("周", "");
		$('#ic_qzqm').val(qzqmzq);
		$('#JK_XNXQ').html(xnxqtext);
		$('#JK_QZQM').html(qzqmtext);
	});
	
		
	//工具栏操作
	function doToolbar(iToolbar){
	
		if(iToolbar == "upload"){
			var excel1 = $('#excel1').val();
			var excelsuffix=excel1.substring(excel1.lastIndexOf("."),excel1.length);

			if(excel1==""){
				//alertMsg("请添加文件!");
				parent.show1();		
				return;
			}
			if(excelsuffix!=".xls" && excelsuffix!=".xlsx"){  
           		parent.show2();		
           		return;
        	} 

			$("#active").val("importExamInfo");
			
			doForm();
			
		}
	}
	
	//form提交
	function doForm(){
		$('#form1').submit();	
	}
	
	$('#form1').form({
		//定位到servlet位置的url
		url:'<%=request.getContextPath()%>/Svl_Import',
		//当点击事件后触发的事件
	    onSubmit: function(data){ 
	    }, 
	    //当点击事件并成功提交后触发的事件
	    success:function(data){
	     	var json = eval("("+data+")");
   		    parent.showMSG(json[0].MSG);		
		}
	});
	
	//选择学期
	function cs0(){
		if(choose0==1){
			$('#choose0').attr("checked",false);
			$('#choose1').attr("checked",false);
			$('#choose2').attr("checked",false);
			$('#choose3').attr("checked",false);
			$('#choose4').attr("checked",false);
			$('#choose5').attr("checked",false);
			$('#choose6').attr("checked",false);
			choose0=0;
			term="";
			$("#ic_term").val(term);
		}else{
			$('#choose0').attr("checked","checked");
			$('#choose1').attr("checked","checked");
			$('#choose2').attr("checked","checked");
			$('#choose3').attr("checked","checked");
			$('#choose4').attr("checked","checked");
			$('#choose5').attr("checked","checked");
			$('#choose6').attr("checked","checked");
			choose0=1;
			term="0,1,2,3,4,5";
			$("#ic_term").val(term);
		}
	}
	
	function checkall(){
		var checkbox="";
		term="";
		for(var i=0;i<6;i++){
			checkbox= document.getElementById("choose"+(i+1));
			if(checkbox.checked){//勾选
				term+=i+",";
			}
		}
		term=term.substring(0, term.length-1);
		if(term=="0,1,2,3,4,5"){
			$('#choose0').attr("checked","checked");
		}else{
			$('#choose0').attr("checked",false);
		}
		$("#ic_term").val(term);
	}
	
	
</script>
</html>
