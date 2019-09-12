<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
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
<title>审核跳转</title>
</head>
<body class="easyui-layout">
	<div region="center">
		<table width="100%" class="tablestyle"> 
			<tr>
				<td>转至：</td>
				<td>				
					<select
						class="easyui-combobox" name="skip" id="skip" style="width: 150px;">
					</select>
				</td>
				<td>
					<a href="#"
					 onclick="enter();"
					id="initial" name="initial" class="easyui-linkbutton" plain="false"
					iconCls="icon-save">确定</a>
				</td>
			</tr>
		</table> 
	</div>
</body>
<script type="text/javascript">
	//alert(audituserid);
	var audituserid=<%=PublicTools.getComboboxOptions("V_审核流程记录表","编号","审核名称","where 业务主键='"+MyTools.StrFiltr(request.getParameter("oprateID"))+"'",request)%>;
	var oprateID='<%=MyTools.StrFiltr(request.getParameter("oprateID"))%>';
	$('#skip').combobox({
			data:audituserid,
			valueField:'comboValue',
			textField:'comboName',
			onSelect:function(record){

			}
	});
	
	function enter(){
		var skip=$('#skip').combobox('getValue');
		$.ajax({
			type: "POST",
			url: '<%=request.getContextPath()%>/Svl_Auditskip',
			async:false,
			//必要的参数
			data: "active=skip&skip=" + skip + "&oprateID=" +oprateID,
			success: function(result){ 
				window.close();
				window.opener.location.reload();
				//hideaudit();
				//parent.$('#adudittables2').window('close');
			}
		});
	}
</script>
