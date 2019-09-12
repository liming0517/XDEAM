<!--
	Author:Zedee
	CreateDate:2005-3-15
	Function:将模板设置菜单下的各模块以模拟流程图的形式显示在页面上
-->
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page 
language="java"
contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"
%>
<%@ page import="java.util.Vector" %>
<%@ page import="com.pantech.base.common.db.DBSource" %>
<%@ page import="com.pantech.base.common.tools.MyTools" %>
<%@ page import="java.sql.SQLException" %>
<%
	String type = request.getParameter("type");
	DBSource db = new DBSource(request);
	String sql = "select Expound,Name from sysAuModule where parId = 6 order by ID";
	Vector v = new Vector();
	try{
		v = db.GetContextVector(sql);
	}catch(SQLException se){
		System.out.println(MyTools.getProp(request,session.getAttribute("language"),"[ViewProcedure_01]"));
		MyTools.GoToErrPage(request,response,MyTools.getProp(request,session.getAttribute("language"),"[ViewProcedure_01]"));
		return;
	}
%>

<TABLE width="98%" border="0" cellspacing="0" cellpadding="0"
	bgcolor="FFFFFF">
	<TBODY>
		<tr>
			<td width="1%" height="28"><img
				src="<%=request.getContextPath()%>/images/border_r1_c1.gif"
				width="16" height="28"></td>
			<td width="97%" class="tabletop">
				<p class="talbetitle"><%=MyTools.getProp(request,session.getAttribute("language"),"[ViewProcedure_02]")%></p>
			</td>
			<td width="2%"><img
				src="<%=request.getContextPath()%>/images/border_r1_c4.gif"
				width="16" height="28"></td>
		</tr>
		<tr>
			<td width="1%" class="tableleft">&nbsp;</td>
			<td width="97%" align="center">
				<%for(int i = 0;i<v.size();i+=2){%>
					<%if(type.equals(""+(i/2+1))){%>
						<img src="<%=request.getContextPath()%>/images/ExcelSetup/NFMO<%=i/2+1%>.gif"><br>
					<%}else{%>
						<img src="<%=request.getContextPath()%>/images/ExcelSetup/NF<%=i/2+1%>.gif" style="cursor: hand" onClick="javascript:window.parent.right.location='<%=request.getContextPath() + v.get(i+1)%>';"><br>
					<%}%>
					<%if(i<v.size()-2){%>
						<img src="<%=request.getContextPath()%>/images/ExcelSetup/ARROW.gif"><br>
					<%}%>
				<%}%>
			</td>
			<td width="2%" class="tableright">&nbsp;</td>
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

