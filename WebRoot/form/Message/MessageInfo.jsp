<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import ="java.util.*"%>
<%@ page import="com.pantech.src.develop.message.*"%>
<%@ page import="com.pantech.src.develop.store.user.*" %>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<HTML>
<%
	/**
	////UserProfile up = (UserProfile)request.getSession().getAttribute("UserProfile");
	//////String userCode = "";
	////if(up!=null){
		//userCode = up.getUserCode();	
	//}
	**/
	String userCode =MyTools.getSessionUserCode(request);
	Svl_message mes = new Svl_message();
	Vector vList = mes.QueryMessage(request.getParameter("MessageCode"),userCode,request);
	mes.UpdateMsgStat(request.getParameter("MessageCode"),userCode,request);
%>
<HEAD>
<%@ page 
language="java"
contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"
%>
<link href="<%=request.getContextPath()%>/css/wiserunoa.css" rel="stylesheet" type="text/css">
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<TITLE>我的消息</TITLE>
</HEAD>
<BODY>
<center>
<%
	if(vList.size()>=3){
%>

<TABLE width="78%" border="0" cellspacing="0" cellpadding="0">
	<TBODY>
		<tr>
			<td width="1%" height="28" align="right"><img
				src="<%=request.getContextPath()%>/images/border_r1_c1.gif"
				width="16" height="28"></td>
			<td width="97%" class="tabletop">
				<p class="talbetitle">阅读消息</p>
			</td>
			<td width="2%"><img
				src="<%=request.getContextPath()%>/images/border_r1_c4.gif"
				width="16" height="28"></td>
		</tr>
		<tr>
			<td class="tableleft" width="1%" align="right">&nbsp;</td>
			<td width="97%" align="center">
			<table width="100%">
				<tbody>
					<TR>
						<TD width="50">发件人：</TD>
						<TD>
							<%=vList.elementAt(0).toString()%>
						</TD>
					</TR>
					<TR>
						<TD>主 &nbsp;&nbsp;题：</TD>
						<TD>
							<%=vList.elementAt(1).toString()%>
						</TD>
					</TR>
					<TR>
						<TD valign="top">信 &nbsp;&nbsp;息：</TD>
						<TD>
						<%
							String msgInfo = vList.elementAt(2).toString();
						%>
							<%=msgInfo%>
						</TD>
					</TR>				
				</tbody>
			</table>
			</td>
			<td width="2%" class="tableright">&nbsp;</td>

		<tr>
			<td class="tableleft" width="1%" align="right">&nbsp;</td>
			<td>&nbsp;</td>
			<td width="2%" class="tableright">&nbsp;</td>
		</tr>
		<tr>
			<td class="tableleft" width="1%" align="right">&nbsp;</td>
			<td align="center">
				<A href="./SendMessage.jsp?user=<%=vList.elementAt(0).toString()%>&title=<%=vList.elementAt(1).toString()%>">回复消息</A>
				<A href="./QueryMessage.jsp">返回</A>
			</td>
			<td width="2%" class="tableright">&nbsp;</td>
		</tr>
		<tr>
			<td width="1%" valign="top"><img src="<%=request.getContextPath()%>/images/border_r3_c1.gif"
				width="16" height="19"></td>
			<td width="97%" class="tablebottom">&nbsp;</td>
			<td width="2%" valign="top"><img src="<%=request.getContextPath()%>/images/border_r3_c4.gif"
				width="16" height="19"></td>
		</tr>
	</TBODY>
</TABLE>
<%
	}
%>

</center>
</BODY>
</HTML>
