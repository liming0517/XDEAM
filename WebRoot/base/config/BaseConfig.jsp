<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<%@ page 
language="java"
contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"
%>
<jsp:useBean id="gp" class="com.pantech.base.common.config.GetProp" scope="page" />
<%@ taglib uri="/WEB-INF/taglib/securityTag.tld" prefix="security" %>
<%
if("y".equalsIgnoreCase(request.getParameter("message"))){
	out.println("<script>alert('修改成功！');</script>");
}
%>

<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<link href="<%=request.getContextPath()%>/css/wiserunoa.css" rel="stylesheet" type="text/css">
<TITLE>BaseConfig.jsp</TITLE>
</HEAD>
<BODY>

<script>
function doSubmit(butName){
  	document.form1.hAction.value = butName;
  	document.form1.submit();
}

/**
 * 控制按钮的enable状态
 */
function Set(name){
if(document.form1.debug.checked){
	document.form1.debugBut.disabled = false;
}else{
	document.form1.debugBut.disabled = true;
}
	
if(document.form1.trace.checked){
	document.form1.traceBut.disabled = false;
}else{
	document.form1.traceBut.disabled = true;
}

}
</script>
<jsp:useBean class="com.pantech.base.common.tags.security.PurViewTools" id="pc" scope="page" />
<%
String mcode = pc.GetModuleCodeFromUrl(request);
%>
<security:securityForPage pagePermissionID="<%=mcode%>">
	<BR>
	<BR>
	<form action="<%=request.getContextPath()%>/Svl_BaseConfig" name="form1" method="post">
	<TABLE width="98%" border="0" cellspacing="0" cellpadding="0"
		bgcolor="FFFFFF">
		<TBODY>
			<TR>
				<TD width="1%" height="28"><IMG
					src="<%=request.getContextPath()%>/images/border_r1_c1.gif"
					width="16" height="28"></TD>
				<TD width="97%" class="tabletop">
				<P class="talbetitle">环境变量设置</P>
				</TD>
				<TD width="2%"><IMG
					src="<%=request.getContextPath()%>/images/border_r1_c4.gif"
					width="16" height="28"></TD>
			</TR>
			<TR>
				<TD class="tableleft">&nbsp;</TD>
				<TD>
				<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
					<TBODY>
						<TR>
							<TD align="center">
							<TABLE border="0" width="80%" class="tablestyle" cellpadding="1" cellspacing="1">
								<TBODY>
									<TR class="fieldbg">
										<TD width="161" align="center">选项</TD>
										<TD colspan="2" align="center">设置</TD>
										<TD width="136" align="center">操作</TD>
									</TR>
									<TR class="tableline">
										<TD width="161">Debug开关</TD>
										<TD width="113"><INPUT type="checkbox" name="debug" value=""
											onclick="Set(this.name)">取消/打开</TD>
										<TD width="393"><%=gp.getProp("","Base.isDebug")%></TD>
										<TD width="136"><INPUT type="button" name="debugBut"
											value="修改" onclick="doSubmit(this.name)" disabled></TD>
									</TR>
									<TR class="tableline">
										<TD width="161">Trace开关</TD>
										<TD width="113"><INPUT type="checkbox" name="trace" value=""
											onclick="Set(this.name)">取消/打开</TD>
										<TD width="393"><%=gp.getProp("","Trace.isPrint")%></TD>
										<TD width="136"><INPUT type="button" name="traceBut"
											value="修改" onclick="doSubmit(this.name)" disabled></TD>
									</TR>
									<TR class="fieldbg">
										<TD colspan="4" align="center">页面显示相关</TD>
									</TR>
									<TR  class="tableline">
										<TD width="161">每页显示记录数</TD>
										<TD colspan="2"><INPUT type="text" name="PageSize"
											value='<%=gp.getProp("","Base.pageSize")%>'></TD>
										<TD width="136"><INPUT type="button" name="pageBut" value="修改"
											onclick="doSubmit(this.name)"></TD>
									</TR>
								</TBODY>
							</TABLE>
							</TD>
						</TR>
					</TBODY>
				</TABLE>

				</TD>
				<TD class="tableright">&nbsp;</TD>
			</TR>
			<TR>
				<TD valign="top"><IMG src="<%=request.getContextPath()%>/images/border_r3_c1.gif"></TD>
				<TD class="tablebottom">&nbsp;</TD>
				<TD valign="top"><IMG src="<%=request.getContextPath()%>/images/border_r3_c4.gif"></TD>
			</TR>
		</TBODY>
	</TABLE>
<!-- 隐藏文本域 -->
<input type="hidden" name="hAction" value="">
</form>
</security:securityForPage>
</BODY>
</HTML>
