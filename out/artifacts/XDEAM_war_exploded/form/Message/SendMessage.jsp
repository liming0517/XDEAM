<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="com.pantech.src.develop.store.user.*" %>
<HTML>
<%	
	String sIncepter = "";
	if(request.getParameter("user")!=null){
		sIncepter = request.getParameter("user");
	}
	String sTitle = "";
	if(request.getParameter("title")!=null){
		sTitle = "Re:" + request.getParameter("title");
	}
%>
<HEAD>
<%@ page 
language="java"
contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"
%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<link href="<%=request.getContextPath()%>/css/wiserunoa.css" rel="stylesheet" type="text/css">
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<TITLE>发送消息</TITLE>


<SCRIPT language="JavaScript">
	function changeInput(type){
		if(type=="0"){
			document.form1.all("MSG_INCEPTER").value = "";
			document.form1.all("MSG_INCEPTER").readOnly = true;
			document.form1.all("MSG_INCEPTER").style.background = "#EFEFEF";
		}
		else{
			document.form1.all("MSG_INCEPTER").readOnly = false;
			document.form1.all("MSG_INCEPTER").style.background = "#FFFFFF";
		}
	}
	
function checkInput(){
	if(document.form1.all("INCEPTER_TYPE").value!="0" && document.form1.all("MSG_INCEPTER").value == ""){
		alert("请输入收件人代码！");
		document.form1.all("MSG_INCEPTER").focus();
		return false;
	}
	else if(document.form1.all("MSG_TITLE").value == ""){
		alert("请输入主题！");
		document.form1.all("MSG_TITLE").focus();
		return false;		
	}
	else if(document.form1.all("MSG_MSG").value == ""){
		alert("请输入信息！");
		document.form1.all("MSG_MSG").focus();
		return false;		
	}
	return true;
}
function OpenDeptList(){
	var val = showModalDialog('<%=request.getContextPath()%>/form/system/DeptTree.jsp?returnText=1&selectMode=1&includeUser=1','','');
	if(val == "" || val == undefined){
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
	document.form1.all("MSG_INCEPTERNAME").value = iName;
	document.form1.all("MSG_INCEPTER").value = iCode;
}
</SCRIPT>
</HEAD>
<BODY>
<CENTER>
<form name="form1" action="<%=request.getContextPath()%>/Svl_message" method="POST" onSubmit="return checkInput();">
<TABLE width="78%" border="0" cellspacing="0" cellpadding="0">
	<TBODY>
		<tr>
			<td width="1%" height="28" align="right"><img
				src="<%=request.getContextPath()%>/images/border_r1_c1.gif"
				width="16" height="28"></td>
			<td width="97%" class="tabletop" colspan="3">
			<p class="talbetitle">发送消息</p>
			</td>
			<td width="2%"><img
				src="<%=request.getContextPath()%>/images/border_r1_c4.gif"
				width="16" height="28"></td>
		</tr>
		<TR>
			<td class="tableleft" width="1%" align="right">&nbsp;</td>
			<TD height="21" width="12%">收件人：</TD>
			<TD><input type="text" name="MSG_INCEPTERNAME" value="" style="cursor:hand" onClick="OpenDeptList()" readonly>
				<input type="HIDDEN" name="MSG_INCEPTER" value="<%=sIncepter%>">
				<input type="hidden" name="MSG_SENDER" value="<%=MyTools.getSessionUserCode(request)%>" /></TD>
			<!--TD height="21" width="85%" align="left" colspan="2"><SELECT name="INCEPTER_TYPE" onChange="changeInput(this.value);">
		<%
			//if(sIncepter.equals("")){
		%>
				<OPTION value="0" selected>全部用户</OPTION>
				<OPTION value="1">个人</OPTION>
		<%	//}else{%>
				<OPTION value="0">全部用户</OPTION>
				<OPTION value="1" selected>个人</OPTION>
		<%	//}%>
				<OPTION value="2">部门</OPTION>
				<OPTION value="3">角色</OPTION>
			</SELECT>
			<INPUT type="text" name="MSG_INCEPTER" size="30" VALUE="<!%=sIncepter%>" READONLY style="background-color:#EFEFEF">

			
				<font color="red">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*输入多个ID时以英文逗号分隔</font>
				<input type="hidden" name="MSG_SENDER" value="<!%=userCode%>" />
			</TD>
			<td width="2%" class="tableright">&nbsp;</td-->
		</TR>
		<TR>
			<td class="tableleft" width="1%" align="right">&nbsp;</td>
			<TD height="29" width="12%">主 &nbsp;&nbsp;题：</TD>
			<TD height="29" width="85%" colspan="2"><INPUT type="text" name="MSG_TITLE"
				size="25" value="<%=sTitle%>"></TD>
			<td width="2%" class="tableright">&nbsp;</td>
		</TR>
		<TR>
			<td class="tableleft" width="1%" align="right">&nbsp;</td>
			<TD height="29" width="12%" valign="top">信 &nbsp;&nbsp;息：</TD>
			<TD colspan="2" width="85%" height="136" valign="top"><TEXTAREA rows="10"
				cols="78" name="MSG_MSG"></TEXTAREA></TD>
			<td width="2%" class="tableright">&nbsp;</td>
		</TR>
		
		<TR>
			<td class="tableleft" width="1%" align="right">&nbsp;</td>
			<TD height="29" width="97%" colspan="3" align="center"><INPUT type="submit" name="SEND" value="发送">&nbsp;&nbsp;&nbsp;<INPUT
				type="reset" value="重置"></TD>
			<td width="2%" class="tableright">&nbsp;</td>
		</TR>
		<tr>
			<td width="1%" valign="top"><img src="<%=request.getContextPath()%>/images/border_r3_c1.gif"
				width="16" height="19"></td>
			<td width="97%" class="tablebottom" colspan="3">&nbsp;</td>
			<td width="2%" valign="top"><img src="<%=request.getContextPath()%>/images/border_r3_c4.gif"
				width="16" height="19"></td>
		</tr>
	</TBODY>
</TABLE>
</form>
</CENTER>
</BODY>
</HTML>
