<%@ page 
language="java"
contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"
%>

<%@ taglib uri="/WEB-INF/taglib/securityTag.tld" prefix="security" %>
<%@ page import = "com.pantech.base.common.config.*"%>
<%@ page import = "java.util.Vector"%>
<%@ page import="com.pantech.base.common.config.PurView"%>
<%PurView purview=new PurView(request);%>
<html>
<head>
<link href="<%=request.getContextPath()%>/css/wiserun.css" rel="stylesheet" type="text/css">
<script>

/**
 * 切换显示/隐藏
 * @author HueVan
 */
function Show(obj){
 var a = document.all[obj];
 //alert(a.style.display);
 //alert("+"+a[1].style.display+"+");
 for(var i=0; i<a.length; i++){
 	a[i].style.display = (a[i].style.display=="none"?"":"none"); 	
 }

}
/**
 * 初始化了，列表显示/隐藏<br>
 * 默认为未展开
 * @author HueVan
 */
function init(obj){
var a = document.all[obj];
 for(var i=0; i<a.length; i++){
 	a[i].style.display = "none"; 	
 }
}


</script>
</head>
<body onload = "init('auth');init('dept')">
<%
Vector v1 = purview.GetUserAuth();
Vector v2 = purview.GetUserDept();
%>
<form name="form1" action="AuthDeptList.jsp" method="post">
<TABLE border="0" width="100%" cellpadding="0" cellspacing="0">
	<TBODY style="font-size:9pt">
		<TR onclick="Show('auth')" style="cursor:hand">
			<TD align="center" bgcolor="#b5b5ff"  class="tabletop" colspan="2"><img
				src="<%=request.getContextPath()%>/images/border_r1_c1.gif"
				width="16" height="28" align="left"></TD>
			<TD align="center" bgcolor="#b5b5ff" class="tabletop" width="428">【角色列表】←点击可以收缩或者展开</TD>
			<TD align="center" bgcolor="#b5b5ff" class="tabletop"><img
				src="<%=request.getContextPath()%>/images/border_r1_c4.gif"
				width="100%" height="28"></TD>
		</TR>
		<%for (int i = 0; i < v1.size(); i++) {%>
		<TR id=auth>
			<TD valign="top" width="61">角色：</TD>
			<TD valign="top" width="111"><A href="D-CRight.jsp?PerCode=<%=v1.get(i).toString()%>&ShowType=AUTH" target = right1><%=v1.get(i).toString()%></A></TD>
			<TD colspan="2">
			</TD>
		</TR>
		<%}%>
		<TR onclick="Show('dept')" style="cursor:hand">
			<TD align="left" bgcolor="#b5b5ff" class="tabletop" colspan="2" valign="top"><IMG
				src="<%=request.getContextPath()%>/images/border_r1_c1.gif"
				width="16" height="28" align="left"></TD>
			<TD align="center" bgcolor="#b5b5ff" class="tabletop" width="428">【部门列表】←点击可以收缩或者展开</TD>
			<TD align="right" bgcolor="#b5b5ff" class="tabletop" valign="top"><IMG
				src="<%=request.getContextPath()%>/images/border_r1_c4.gif"
				width="16" height="28"></TD>
		</TR>
		<%for (int i = 0; i < v2.size(); i++) {%>
		<TR id=dept>
			<TD valign="top" width="61">部门：</TD>
			<TD valign="top" width="111"><A href="D-CRight.jsp?PerCode=<%=v2.get(i).toString()%>&ShowType=DEPT" target = right1><%=v2.get(i).toString()%></A></TD>
			<TD colspan="2">
			</TD>
		</TR>
		<%}%>
	</TBODY>
</TABLE>
</form>
</body>
</html>
