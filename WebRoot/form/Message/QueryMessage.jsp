<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page 
language="java"
contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"
%>
<%@ page import ="java.util.*"%>
<%@ page import="com.pantech.src.develop.message.*"%>
<%@ page import="com.pantech.src.develop.store.user.*" %>
<%@ page import ="com.pantech.base.common.tools.MyTools" %>
<HTML>
<%
	/**
	UserProfile up = (UserProfile)request.getSession().getAttribute("UserProfile");
	String userCode = "";
	if(up!=null){
		userCode = up.getUserCode();	
	}
	**/
	String userCode=MyTools.getSessionUserCode(request);
	Svl_message mes = new Svl_message();
	String sType = "";
	if(request.getParameter("MSG_TYPE")!=null){
		sType = request.getParameter("MSG_TYPE");
		if(!mes.RemoveMsg(request.getParameter("DEL_MSG_CODE"),userCode,request)){
%>
<SCRIPT language="JavaScript">
			alert("消息未成功删除！");
</SCRIPT>
<%
		}
	}
	else{
		sType = request.getParameter("Type");
	}
	String sAction = request.getParameter("Action");
	if(sType==null){
		sType = "unread";
	}
	else if(sType.equals("2")){
		sType = "read";
	}
	else{
		sType = "unread";
	}
	Vector vList = mes.QueryMessage(sType,userCode,request);
%>
<HEAD>

<link href="<%=request.getContextPath()%>/css/wiserunoa.css" rel="stylesheet" type="text/css">
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<TITLE>我的消息</TITLE>
<SCRIPT language="JavaScript">

function js_check_all(form1){
	var i;
	var max = form1.elements.length;
	for(i=1;i<max;i++){
		if(form1.elements[i].type == "checkbox"){
			form1.elements[i].checked = form1.all("checkall").checked;
		}
	}
}

function js_get_delList(form1){
	var i;
	var max = form1.elements.length;
	for(i=1;i<max;i++){
		if(form1.elements[i].type == "checkbox"){
			if(form1.elements[i].checked){
				form1.all("DEL_MSG_CODE").value += form1.elements[i].name + ","; 
			}
		}
	}
	if(form1.all("DEL_MSG_CODE").value==""){
		alert("请选中要删除的消息！");
		return false;
	}
	return true;
}


</SCRIPT>
</HEAD>
<BODY>
<center>
<FORM name="form1" method="POST" onSubmit="return js_get_delList(this);">

<TABLE width="78%" border="0" cellspacing="0" cellpadding="0">
	<TBODY>
		<tr>
			<td width="1%" height="28" align="right"><img
				src="<%=request.getContextPath()%>/images/border_r1_c1.gif"
				width="16" height="28"></td>
			<td width="97%" class="tabletop" colspan="4">
			<p class="talbetitle">收件箱</p>
			</td>
			<td width="2%"><img
				src="<%=request.getContextPath()%>/images/border_r1_c4.gif"
				width="16" height="28"></td>
		</tr>
		<TR>
			<td class="tableleft" width="1%" align="right">&nbsp;</td>
			<TD align="left" width="189" >主题</TD>
			<TD align="left" width="180" >发件人</TD>
			<TD align="center" width="181">发送时间</TD>
			<TD align="center" width="10">
				<input type="checkbox" name="allbox" id="checkall" title="全选／取消选择" onclick="js_check_all(this.form)" />
			</TD>
			<td width="2%" class="tableright">&nbsp;</td>
		</TR>
<%
	for(int i = 0;i<vList.size();i+=4){
%>		
		<TR>
			<td class="tableleft" width="1%" align="right">&nbsp;</td>
			<TD align="left" width="189"><a href="MessageInfo.jsp?MessageCode=<%=MyTools.StrFiltr(vList.elementAt(i))%>" onClick="parent.online.location.reload();"><%=vList.elementAt(i+1).toString()%></a></TD>
			<TD align="left" width="180" ><%=MyTools.StrFiltr(vList.elementAt(i+3))%></TD>
			<TD align="center" width="181"><%=MyTools.StrFiltr(vList.elementAt(i+2)).substring(0)%></TD>
			<TD align="center" width="10"><input type="checkbox" name="<%=MyTools.StrFiltr(vList.elementAt(i))  %>" /></TD>
			<td width="2%" class="tableright">&nbsp;</td>
		</TR>
<%
	}
%>
		<TR>
			<td class="tableleft" width="1%" align="right">&nbsp;</td>
			<TD colspan="4" align="center">
				<%
					if(sType.equals("unread")){
				%>
					未读消息
					<A href="./QueryMessage.jsp?Type=2">已读消息</A>
				<%
					}else{
				%>
					<A href="./QueryMessage.jsp?Type=1">未读消息</A>
					已读消息
				<%}%>
					<input type="submit" value="删除消息" name="DEL" />
					<input type="hidden" value="" name="MSG_TYPE" />
					<input type="hidden" value="" name="DEL_MSG_CODE" />	
			</TD>
			<td width="2%" class="tableright">&nbsp;</td>
		</TR>
		<tr>
			<td width="1%" valign="top"><img src="<%=request.getContextPath()%>/images/border_r3_c1.gif"
				width="16" height="19"></td>
			<td width="97%" class="tablebottom" colspan="4">&nbsp;</td>
			<td width="2%" valign="top"><img src="<%=request.getContextPath()%>/images/border_r3_c4.gif"
				width="16" height="19"></td>
		</tr>
	</TBODY>
</TABLE>

<SCRIPT language="JavaScript">
	document.form1.all("MSG_TYPE").value = <%=sType.equals("unread")?1:2%>
</SCRIPT>
</form>
</center>
</BODY>
</HTML>
