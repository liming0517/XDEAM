<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<%@ page 
language="java"
contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"
%>
<%@ page import="java.util.HashMap,java.util.Vector"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.src.develop.system.SystemClass"%>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<META http-equiv="Content-Type" content="text/html; charset=GB18030">
<META HTTP-EQUIV="expires" CONTENT="Wed, 26 Feb 1978 08:21:57 GMT">
<META http-equiv="Content-Style-Type" content="text/css">
<link href="<%=request.getContextPath()%>/css/wiserun.css" rel="stylesheet" type="text/css">
<TITLE><%=MyTools.getProp(request,session.getAttribute("language"),"[U-DRightDown_01]")%></TITLE>
<script>
function doSubmit(type){
var o = eval(document.form1);
var deptCode = document.form1.hDeptCode.value;
if(type == "add"){ //为该部门添加新的人员	
	//alert(deptCode);
	o.action = "<%=request.getContextPath()%>/Svl_System?type=add&DeptCode="+deptCode;
	o.submit();
}
else if(type == "mod"){//修改
	o.action = "<%=request.getContextPath()%>/Svl_System?type=mod&DeptCode="+deptCode;
	o.submit();
}
else if(type == "del"){//删除
	o.action = "<%=request.getContextPath()%>/Svl_System?type=del&DeptCode="+deptCode;
	//alert(o.action);
	o.submit();
}

}

/**
 * 选择一条记录后，将值赋于文本框
 */
function SetValue(prefix,i){
	//用户代码
	var a = eval("document.form1."+ prefix + i);
	//用户名称
	//var b = eval("parent.rightdown.document.form1."+ prefix + (parseInt(i)+parseInt(1)));
	//有效标志
	var c = eval("document.form1."+ prefix + (parseInt(i)+parseInt(2)));
	//alert(document.form1.UserItem1.value);
	document.form1.UserCode.value = a.value;
	document.form1.oldUserCode.value = a.value;
	

	
}
</script>
</HEAD>
<BODY>
<%SystemClass User=new SystemClass(request);%>
<%
String deptCode = request.getParameter("DeptCode");
Vector v = User.GetUserInfo(deptCode);
%>
<form name="form1" action="" method="POST">
<TABLE width="98%" border="0" cellspacing="0" cellpadding="0"
	bgcolor="FFFFFF">
	<TBODY>
		<TR>
			<TD width="1%" height="28"><IMG
				src="<%=request.getContextPath()%>/images/border_r1_c1.gif"
				width="16" height="28"></TD>
			<TD width="97%" class="tabletop">
			<P class="talbetitle"><%=MyTools.getProp(request,session.getAttribute("language"),"[U-DRightDown_02]")%></P>
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
						<TD>
						<TABLE border="0" width="100%" cellspacing="1" cellpadding="1">
							<TBODY>
								<TR>
									<TD><%=MyTools.getProp(request,session.getAttribute("language"),"[U-DRightDown_03]")%></TD>
									<TD><INPUT type="text" name="UserCode"><INPUT type="hidden"
										name="oldUserCode"></TD>
									<TD></TD>
									<TD></TD>
									<TD></TD>
									<TD></TD>
								</TR>
								<TR>
									<TD align="left" colspan="6"></TD>
								</TR>
								<TR>
									<TD colspan="6" align="center">
									<TABLE border="0" width="70%">
										<TBODY>
											<TR>
												<TD colspan="3" align="center"><INPUT type="button"
													value="<%=MyTools.getProp(request,session.getAttribute("language"),"[U-DRightDown_04]")%>" onclick="doSubmit('add')"> <INPUT type="button"
													value="<%=MyTools.getProp(request,session.getAttribute("language"),"[U-DRightDown_05]")%>" onclick="doSubmit('del')"></TD>
											</TR>
										</TBODY>
									</TABLE>
									</TD>
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
			<TD valign="top"><IMG src="<%=request.getContextPath()%>/images/border_r3_c1.gif"
				width="16" height="19"></TD>
			<TD class="tablebottom">&nbsp;</TD>
			<TD valign="top"><IMG src="<%=request.getContextPath()%>/images/border_r3_c4.gif"
				width="16" height="19"></TD>
		</TR>
	</TBODY>
</TABLE>
<input type="hidden" value="UserDept" name="hModuleID">


<br>
<!-- <center><input type="button" value="< %=MyTools.getProp(request,session.getAttribute("language"),"[U-DRightDown_06]")%>" onclick="window.parent.location.href='< %=request.getContextPath()%>/form/system/UserConfig.jsp'"></center> -->
<br><br>
<TABLE width="98%" border="0" cellspacing="0" cellpadding="0"
	bgcolor="FFFFFF">
	<TBODY>
		<TR>
			<TD width="1%" height="28"><IMG
				src="<%=request.getContextPath()%>/images/border_r1_c1.gif"
				width="16" height="28"></TD>
			<TD width="97%" class="tabletop">
			<P class="talbetitle" align="center"><%=MyTools.StrFiltr(deptCode)%><%=MyTools.getProp(request,session.getAttribute("language"),"[U-DRightDown_07]")%></P>
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
						<TD>
						<TABLE border="0" width="100%" cellpadding="1" class="tablestyle">
							<TBODY>
								<TR class="fieldbg">
									<TD class="tablefield" align="center"><%=MyTools.getProp(request,session.getAttribute("language"),"[U-DRightDown_08]")%></TD>
									<TD class="tablefield" align="center"><%=MyTools.getProp(request,session.getAttribute("language"),"[U-DRightDown_09]")%></TD>
									<TD class="tablefield" align="center"><%=MyTools.getProp(request,session.getAttribute("language"),"[U-DRightDown_10]")%></TD>
									<TD class="tablefield" align="center"><%=MyTools.getProp(request,session.getAttribute("language"),"[U-DRightDown_11]")%></TD>
								</TR>
								<%for(int i=0,j=1; i<v.size(); i++,j+=3){%>
								<TR class="tableline">
									<TD width="45"><INPUT type="radio" name="UserItem"
										onclick="SetValue('UserItem','<%=j%>')"></TD>
									<TD width="111"><%=((HashMap)v.get(i)).get("USERCODE")+"　"%> <INPUT
										type="hidden" name="UserItem<%=j%>"
										value='<%=((HashMap)v.get(i)).get("USERCODE")%>'></TD>
									<TD width="97"><%=((HashMap)v.get(i)).get("USERNAME")+"　"%> <INPUT
										type="hidden" name="UserItem<%=j+1%>"
										value='<%=((HashMap)v.get(i)).get("USERNAME")%>'></TD>
									<TD width="97"><%=((HashMap)v.get(i)).get("STATE")+"　"%> <INPUT
										type="hidden" name="UserItem<%=j+2%>"
										value='<%=((HashMap)v.get(i)).get("STATE")%>'></TD>
								</TR>
								<%}%>
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
			<TD valign="top"><IMG src="<%=request.getContextPath()%>/images/border_r3_c1.gif"
				width="16" height="19"></TD>
			<TD class="tablebottom">&nbsp;</TD>
			<TD valign="top"><IMG src="<%=request.getContextPath()%>/images/border_r3_c4.gif"
				width="16" height="19"></TD>
		</TR>
	</TBODY>
</TABLE>
<input type="hidden" value="<%=deptCode%>" name="hDeptCode">
</form>
</BODY>
</HTML>
