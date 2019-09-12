<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<%@ page 
language="java"
contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"
%>
<jsp:useBean class="com.pantech.base.common.tags.security.PurViewTools" id="pc" scope="page" />
<%@ taglib uri="/WEB-INF/taglib/securityTag.tld" prefix="security"%>
<%@ page import="java.util.HashMap,java.util.Vector"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.src.develop.system.SystemClass"%>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<link href="<%=request.getContextPath()%>/css/wiserun.css" rel="stylesheet" type="text/css">
<TITLE>用户设置</TITLE>
<%SystemClass User=new SystemClass(request);%>
<script>
function doSubmit(type){
var o = eval(document.form1);
if(type == "add"){ //添加
	o.action = "<%=request.getContextPath()%>/Svl_System?type=add";
	o.submit();
}
else if(type == "mod"){//修改
	o.action = "<%=request.getContextPath()%>/Svl_System?type=mod";
	o.submit();
}
else if(type == "del"){//删除
	o.action = "<%=request.getContextPath()%>/Svl_System?type=del";
	o.submit();
}else if(type == "IniPass"){
if(confirm('进行此操作后\n对应人员的密码将被重新初始化成11111111(8个1) !','确定','取消')){
	o.action = "<%=request.getContextPath()%>/Svl_System?type=IniPass";
	o.submit();
	}
}

}

/**
 * 选择一条记录后，将值赋于文本框
 */
function SetValue(prefix,i){
	//用户代码
	var a = eval("document.form1."+ prefix + i);
	//用户名称
	var b = eval("document.form1."+ prefix + (parseInt(i)+parseInt(1)));
	//邮件
	var yj = eval("document.form1."+ prefix + "E" + i);
	//有效标志
	var c = eval("document.form1."+ prefix + (parseInt(i)+parseInt(2)));
	
	//alert(document.form1.UserItem1.value);

	
	document.form1.UserCode.value = a.value;
	document.form1.oldUserCode.value = a.value;
	
	document.form1.UserName.value = b.value;
	
	document.form1.Email.value = yj.value;
		
	document.form1.oldUserName.value = b.value;
	
	//alert(c.value);
	if(c.value == "Y" || c.value=="y"){		
		document.form1.UserState[0].checked = true;
	}else{
		document.form1.UserState[1].checked = true;
	}
	//document.form1.UserState.value = c.value;
	document.form1.oldUserState.value = c.value;
	
}

/**
 * 打开指定的路径
 */
function OpenWin(url){
	window.open(url,'','width=400,height=650,toolbar=no,menubar=no,scrollbars=yes, resizable=no,location=no, status=no');
	
}


</script>
</HEAD>
<%
Vector v = new Vector();
v = User.GetUserInfo();
System.out.println("v.size()=== "+v.size());
%>
<BODY>
<%
String mcode = pc.GetModuleCodeFromUrl(request);
%>
<security:securityForPage pagePermissionID="<%=mcode%>">
<form  name="form1" method="POST">

<TABLE width="98%" border="0" cellspacing="0" cellpadding="0"
	bgcolor="FFFFFF"> 
	<TBODY>
		<TR>
			<TD width="1%" height="28"><IMG
				src="<%=request.getContextPath()%>/images/border_r1_c1.gif"
				width="16" height="28"></TD>
			<TD width="97%" class="tabletop">
			<P class="talbetitle">用户信息<font color=red><%=MyTools.StrFiltr(request.getParameter("MSG"))%></font></P>
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
									<TD>用户代码：</TD>
									<TD><INPUT type="text" name="UserCode" maxLength="10"><INPUT type="hidden"
										name="oldUserCode"></TD>
									<TD>用户名：</TD>
									<TD><INPUT type="text" name="UserName"><INPUT type="hidden"
										name="oldUserName"></TD>
									<TD>手机号：</TD>
									<TD><INPUT type="text" name="phone">
								</tr>
								<tr>
									<TD>邮箱地址：</TD>
									<TD><INPUT type="text" name="Email"><INPUT type="hidden"
										name="oldEmail"></TD>
									<TD>密码(不可修改)：</TD>
									<TD>	
									   <INPUT type="password" name="password" maxlength="10" >
									</TD>
									<TD>状态：</TD>
									<TD>有效<input type=radio name="UserState" value="Y" Checked>无效<input type=radio name="UserState" value="N"><INPUT type="hidden"
										name="oldUserState"></TD>
								</TR>
								<TR>
									<TD align="left" colspan="8"></TD>
								</TR>
								<TR>
									<TD colspan="8" align="center">
									<TABLE border="0" width="70%">
										<TBODY>
											<TR>
												<TD colspan="3" align="center"><INPUT type="button" value="添加"
													onclick="doSubmit('add')"> <INPUT type="button" value="修改"
													onclick="doSubmit('mod')"> <INPUT type="button" value="删除"
													onclick="doSubmit('del')"></TD>
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
<P align="center">
<input type="button" value="转到人员-部门设置" onclick="window.location.href='<%=request.getContextPath()%>/form/system/UserDept/UserDeptConfig.jsp'">
<input type="button" value="转到人员-角色设置" onclick="window.location.href='<%=request.getContextPath()%>/form/system/UserAuth/UserAuthConfig.jsp'">
</P>



<TABLE width="98%" border="0" cellspacing="0" cellpadding="0"
	bgcolor="FFFFFF">
	<TBODY>
		<TR>
			<TD width="1%" height="28"><IMG
				src="<%=request.getContextPath()%>/images/border_r1_c1.gif"
				width="16" height="28"></TD>
			<TD width="97%" class="tabletop">
			<P class="talbetitle" align="center">用户信息列表</P>
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
									<TD class="tablefield" align="center">操作</TD>
									<TD class="tablefield" align="center">用户代码</TD>
									<TD class="tablefield" align="center">用户名</TD>
									<TD class="tablefield" align="center">邮箱地址</TD>
									<TD class="tablefield" align="center">状态</TD>
									<TD class="tablefield" align="center">详细</TD>
								</TR>
							<%for(int i=0,j=1; i<v.size(); i++,j+=3){%>
								<TR class="tableline">
									<TD width="45" align="center"><INPUT type="radio" name="UserItem" onclick="SetValue('UserItem','<%=j%>')"></TD>
									<TD width="111">
										<%=MyTools.ObjFiltr(((HashMap)v.get(i)).get("USERCODE"))+"　"%>
										<INPUT type="hidden" name="UserItem<%=j%>" value="<%=MyTools.ObjFiltr(((HashMap)v.get(i)).get("USERCODE"))%>">
									</TD>
									<TD width="90">
										<%=MyTools.ObjFiltr(((HashMap)v.get(i)).get("USERNAME"))+"　"%>
										<INPUT type="hidden" name="UserItem<%=j+1%>" value="<%=MyTools.ObjFiltr(((HashMap)v.get(i)).get("USERNAME"))%>">
									</TD>
									<TD width="97">
										<%=MyTools.ObjFiltr(((HashMap)v.get(i)).get("EMAIL"))+"　"%>
										<INPUT type="hidden" name="UserItemE<%=j%>" value="<%=MyTools.ObjFiltr(((HashMap)v.get(i)).get("EMAIL"))%>">
									</TD>
									<TD width="37">
										<%=MyTools.ObjFiltr(((HashMap)v.get(i)).get("STATE"))+"　"%>
										<INPUT type="hidden" name="UserItem<%=j+2%>" value="<%=MyTools.ObjFiltr(((HashMap)v.get(i)).get("STATE"))%>">
									</TD>
									<TD width="67">
										<input type="button" value="设置权限" onclick="OpenWin('PerCodeList.jsp?UserCode=<%=MyTools.fixSql(MyTools.StrFiltr(((HashMap)v.get(i)).get("USERCODE")))%>')">
										<input type="button" value="密码重置" onclick="SetValue('UserItem','<%=j%>');doSubmit('IniPass');">
									</TD>
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
<input type="hidden" value="User" name="hModuleID">
</form></security:securityForPage>
</BODY>
</HTML>
