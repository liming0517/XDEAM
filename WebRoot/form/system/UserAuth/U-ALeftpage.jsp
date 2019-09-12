<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<%@ page 
language="java"
contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"
%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.src.develop.system.SystemClass"%>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<link href="<%=request.getContextPath()%>/css/wiserun.css" rel="stylesheet" type="text/css">
<TITLE>角色列表</TITLE>
</HEAD>
<BODY>

<%
SystemClass Auth=new SystemClass(request);
Vector v = new Vector();
//接收指定的角色代码如果参数存在则获取指定的角色类表，如果没有则获取所有角色列表
String Authlist=MyTools.StrFiltr(request.getParameter("AuthList"));
Vector velist=new Vector();
if(Authlist.length()>1){
	velist=MyTools.StringToVector(Authlist,",");
	v = Auth.GetAuthInfo(velist);
}else{
	v = Auth.GetAuthInfo();
}
System.out.println("v.size()=== "+v.size());
%>
<TABLE width="98%" border="0" cellspacing="0" cellpadding="0" bgcolor="FFFFFF">
	<TBODY>
		<tr>
			<td width="1%" height="28"><img
				src="<%=request.getContextPath()%>/images/border_r1_c1.gif"
				width="16" height="28"></td>
			<td width="97%" class="tabletop">
			<p class="talbetitle" align="center">WISERUN角色列表</p>
			</td>
			<td width="2%"><img
				src="<%=request.getContextPath()%>/images/border_r1_c4.gif"
				width="16" height="28"></td>
		</tr>
		<tr>
			<td class="tableleft">&nbsp;</td>
			<td>
			<table width="100%" border="0" cellspacing="1" cellpadding="0">
				<tr>
					<td>

					<TABLE class="tablestyle" width="100%" cellspacing="1" cellpadding="0">
						<TBODY>
							<TR align="center" class="fieldbg">
								<TD class="tablefield" align="center" width="119">角色代码</TD>
								<TD class="tablefield" align="center" width="106">中文名称</TD>
								<TD class="tablefield" align="center" width="120">有效状态</TD>
							</TR>
							<%for(int i=0,j=1; i<v.size(); i++,j+=4){%>
							<TR class="tableline">
								<TD width="119"><a href="U-ARightDown.jsp?AuthCode=<%=((HashMap)v.get(i)).get("AUTHCODE")%>" target="rightdown"><%=((HashMap)v.get(i)).get("AUTHCODE")+"　"%></a><INPUT
									type="hidden" name="DeptItem<%=j%>"
									value='<%=((HashMap)v.get(i)).get("AUTHCODE")%>'></TD>
								<TD width="106"><%=((HashMap)v.get(i)).get("AUTHDESC")+"　"%><INPUT
									type="hidden" name="DeptItem<%=j+1%>"
									value='<%=((HashMap)v.get(i)).get("AUTHDESC")%>'></TD>
								<TD width="120"><%=((HashMap)v.get(i)).get("STATE")+"　"%><INPUT
									type="hidden" name="DeptItem<%=j+2%>"
									value='<%=((HashMap)v.get(i)).get("STATE")%>'></TD>
							</TR>
							<%}%>
						</TBODY>
					</TABLE>
					</td>
				</tr>
			</table>

			</td>
			<td class="tableright">&nbsp;</td>
		</tr>
		<tr>
			<td valign="top"><img src="<%=request.getContextPath()%>/images/border_r3_c1.gif"
				width="16" height="19"></td>
			<td class="tablebottom">&nbsp;</td>
			<td valign="top"><img src="<%=request.getContextPath()%>/images/border_r3_c4.gif"
				width="16" height="19"></td>
		</tr>
	</TBODY>
</TABLE>

</BODY>
</HTML>
