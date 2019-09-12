<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'sysEchelonsInfo.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
		<script type = "text/javascript" src = "<%= request.getContextPath()%>/script/common/clientScript.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery-1.8.0.min.js"></script>
		<script type = "text/javascript" src = "<%= request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
	<script charset="UTF-8" src="<%=request.getContextPath()%>/script/JQueryUI/kindeditor.js"></script>
	<link rel = "stylesheet" type = "text/css" href = "<%= request.getContextPath()%>/css/JQueryUI/themes/default/easyui.css">
	<link rel = "stylesheet" type = "text/css" href = "<%= request.getContextPath()%>/css/JQueryUI/themes/icon.css"> 
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
	<link href="<%=request.getContextPath()%>/css/wiserun.css" rel="stylesheet" type="text/css">

  </head>
  
  <body>
    <form id="form1" name="form1" method="post">
 		<div name="层级编辑" id="cengjibianji">
		<table width="400px" border="0" cellspacing="0" cellpadding="0" bgcolor="FFFFFF">
				<TR>
				<TD width="1%" height="28"><IMG src="<%=request.getContextPath()%>/images/border_r1_c1.gif" width="16" height="28"></TD>
				<TD width="97%" class="tabletop"><P class="talbetitle">层级编辑</TD>
				<TD width="2%"><IMG src="<%=request.getContextPath()%>/images/border_r1_c4.gif"	width="16" height="28"></TD>
    			</TR>
			</table>
						<div style="width:398px;border:1px solid #F00">
			<table width="100%" cellspacing="5" cellpadding="5">
				<tr>
					<td>
					<input type="button" value="保存" onclick="doFrom()">
					</td>
					<td>
					<input type="button" value="返回" onclick="doToolbar()"/>
					</td>
				</tr>
				<tr>
					<td>
					层级编号
					</td>
					<td>
					<input id="EcheCode">
					</td>
				</tr>
				<tr>
					<td>
					层级名称
					</td>
					<td>
					<input id="EcheName">
					</td>	
				</tr>
				<tr>
					<td>
					状态
					</td>
					<td>
						<select id="state" name="State"  style="width:100px">
							<option value="1" >有效</option>
							<option value="0" >无效</option>
						</select>
					</td>	
				</tr>
			</table>	
			</div>
	</div>
	<input type="hidden" id="active" name="active" value="add">
	</form>
  </body>
<script type="text/javascript">

//初始化界面
var EcheCode="<%=request.getParameter("EcheCode")%>";

$(document).ready(function(){
	var active="<%=request.getParameter("btn")%>";
	//判定是否是编辑或新建
	if(active=="bj"){
	$("#EcheCode").val(EcheCode.substr(1));
	$("#EcheName").val("<%=request.getParameter("EcheName")%>");
	$("#active").val("queryupdate");
	}
	
});
function doToolbar(){
window.location.href="sysAuthEche.jsp";
}
//工具按钮
function doFrom(){
	$.ajax({
		   type: "POST",
		   url: "<%=request.getContextPath()%>/Svl_EchelonsManage",
		   data:"active="+$("#active").val()+"&EcheName="+$("#EcheName").val()+"&EcheCode="+$("#EcheCode").val()+"&state="+$("#state").val()+"&EcheCode1=<%=request.getParameter("EcheCode")%>",												
		   dataType: 'json',
		   success: function(datas){
		   		var	data=datas[0];
		   		if(data.MSG=="层级已存在"){
		   		showMsg(data.MSG);
		   		}else 
		   		if(data.MSG=="保存成功"){
		   		alertMsg(data.MSG);
		   		window.location.href="sysAuthEche.jsp";
		   		}else if(data.MSG=="保存失败"){
		   		showMsg(data.MSG);
		   		}else if(data.MSG=="编辑成功"){
		   		alertMsg(data.MSG);
		   		window.location.href="sysAuthEche.jsp";
		   		}
		   			
		  
		   	}
		   });
	
}
</script>
</html>
