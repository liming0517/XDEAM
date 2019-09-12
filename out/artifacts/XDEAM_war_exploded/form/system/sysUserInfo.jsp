<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<%@ page 
language="java"
contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"
%>
<%@ page import="com.pantech.src.develop.store.user.*" %>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<!--
//人员信息数据
//CREATE BY HADES
//模块编号 
-->
<%@ taglib uri="/WEB-INF/taglib/securityTag.tld" prefix="security" %>
<%@ taglib uri="/WEB-INF/taglib/util.tld" prefix="util" %>

<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">

<!--CSS文件,包含了风格信息-->
<link href="<%=request.getContextPath()%>/css/wiserun.css" rel="stylesheet" type="text/css">
<!--/CSS文件-->
<TITLE><%//=GetProp.getProp("","htmlTitle") %></TITLE>
</HEAD>
<BODY>
<!--权限安全判定-->
<!--security:securityForPage pagePermissionID="M1.7.4.2"-->

<!--判定当前工作流任务是否可以开始-->
<%
//通用数据初始化
//////////////////////////
String modId="M1.7.4.2";  //注意，这一步必须要有，否则工作流无法获取正确的模块编号

%>
<%
//UserProfile up = (UserProfile)request.getSession().getAttribute("UserProfile");
UserProfile up =new UserProfile(request,MyTools.getSessionUserCode(request));
%>


<FORM action="" method="post" name="form1">


<TABLE width="98%" border="0" cellspacing="0" cellpadding="0" >
	<TBODY>
		<!--头部,只能改标题-->
		<tr class="fieldbg">
			<td width="1%" height="28"><img
				src="<%=request.getContextPath()%>/images/border_r1_c1.gif"
				width="16" height="28"></td>
			<td width="97%" class="tabletop">
			<p class="talbetitle"><%=MyTools.getProp(request,"[用户信息]")%> <font color=red></font></p>
			</td>
			<td width="2%"><img
				src="<%=request.getContextPath()%>/images/border_r1_c4.gif"
				width="16" height="28"></td>
		</tr>
		<!--/头部-->
		<!--表体,实际数据-->
		<tr>
			<td class="tableleft">&nbsp;</td>
			<td>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
			    <!--申请编号-->
				<tr>
					<td>
					<TABLE class=tablestyle border="1" width="100%">
						<TBODY>
							<TR class="fieldbg">
								<TD>欢迎您：</TD>
								<TD><%=up.getUserName()%>
								
								</TD>
							</TR>
						</TBODY>
					</TABLE>
					</td>
				</tr>
			    <!--详细描述-->
				<tr>
					<td>
					    <%=up.printProp()%>
					</td>
				</tr>
				
			
				
				
				
			</table>

			</td>
			<td class="tableright">&nbsp;</td>
		</tr>
		<!--/表体-->
		<!--尾部,-->
		<tr>
			<td valign="top"><img src="<%=request.getContextPath()%>/images/border_r3_c1.gif"
				width="16" height="19"></td>
			<td class="tablebottom">&nbsp;</td>
			<td valign="top"><img src="<%=request.getContextPath()%>/images/border_r3_c4.gif"
				width="16" height="19"></td>
		</tr>
		<!--/尾部,-->
	</TBODY>
</TABLE>
</FORM>
<!--/security:securityForPage-->
<!--/权限安全判定-->
</BODY>

</HTML>
