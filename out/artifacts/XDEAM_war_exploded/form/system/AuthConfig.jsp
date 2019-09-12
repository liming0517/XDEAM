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
<%@ page import="com.pantech.src.develop.system.SystemClass"%>
<jsp:useBean class="com.pantech.base.common.tags.security.PurViewTools" id="pc" scope="page" />
<%@ taglib uri="/WEB-INF/taglib/securityTag.tld" prefix="security"%>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<link href="<%=request.getContextPath()%>/css/wiserun.css" rel="stylesheet" type="text/css">
<TITLE>角色设置</TITLE>
<%SystemClass Auth=new SystemClass(request);%>
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
}

}
/**
 * 选择一条记录后，将值赋于文本框
 */
function SetValue(prefix,i){
	//角色代号
	var a = eval("document.form1."+ prefix + i);
	//角色状态
	var b = eval("document.form1."+ prefix + (parseInt(i)+parseInt(1)));
	//角色描述
	var c = eval("document.form1."+ prefix + (parseInt(i)+parseInt(2)));

	document.form1.AuthCode.value = a.value;
	document.form1.oldAuthCode.value = a.value;

	//document.form1.AuthState.value = b.value;
	document.form1.oldAuthState.value = b.value;
	
	if(b.value == "Y" || b.value=="y"){		
		document.form1.AuthState[0].checked = true;
	}else{
		document.form1.AuthState[1].checked = true;
	}
	document.form1.AuthDesc.value = c.value;
	//document.form1.oldAuthDesc.value = c.value;
	
}


</script>
</HEAD>
<BODY>
<%
String mcode = pc.GetModuleCodeFromUrl(request);
%>
<security:securityForPage pagePermissionID="<%=mcode%>">
<P><BR>
</P>
<%
Vector v = new Vector();
v = Auth.GetAuthInfo();
System.out.println("v.size()=== "+v.size());
%>
<form action="../../WEB-INF/form/system/null" name="form1" method="post">
<DIV align="left">
<TABLE cellspacing="1">
	<TBODY align="center">
		<TR align="center">
			<TD>
			
			</TD>
		</TR>
	</TBODY>
</TABLE>
</DIV>
<TABLE width="98%" border="0" cellspacing="0" cellpadding="0"
	bgcolor="FFFFFF">
	<TBODY>
		<TR>
			<TD width="1%" height="28"><IMG
				src="<%=request.getContextPath()%>/images/border_r1_c1.gif"
				width="16" height="28"></TD>
			<TD width="97%" class="tabletop">
			<P class="talbetitle">角色信息</P>
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
									<TD>角色代号</TD>
									<TD><INPUT type="text" name="AuthCode"><INPUT type="hidden"
										name="oldAuthCode"></TD>
									<TD></TD>
									<TD><INPUT type="hidden" name="oldAuthName"></TD>
									<TD>状态：</TD>
									<TD>有效<INPUT type="radio" name="AuthState" value="Y">无效<INPUT type="radio" name="AuthState" value="N"><INPUT type="hidden"
										name="oldAuthState"></TD>
								</TR>
								<TR>
									<TD align="left" colspan="6">角色描述</TD>
								</TR>
								<TR>
									<TD align="left" colspan="6"><TEXTAREA rows="5" cols="50"
										name="AuthDesc">角色描述</TEXTAREA></TD>
								</TR>
								<TR>
									<TD colspan="6" align="center">
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
<P><BR>
</P>
<TABLE width="98%" border="0" cellspacing="0" cellpadding="0"
	bgcolor="FFFFFF">
	<TBODY>
		<TR>
			<TD width="1%" height="28"><IMG
				src="<%=request.getContextPath()%>/images/border_r1_c1.gif"
				width="16" height="28"></TD>
			<TD width="97%" class="tabletop">
			<P class="talbetitle">角色列表</P>
			</TD>
			<TD width="2%"><IMG
				src="<%=request.getContextPath()%>/images/border_r1_c4.gif"
				width="16" height="28"></TD>
		</TR>
		<TR>
			<TD class="tableleft">&nbsp;</TD>
			<TD>
			<TABLE width="100%" border="0" cellspacing="0" cellpadding="0" >
				<TBODY>
					<TR>
						<TD>

						<TABLE width="100%" cellpadding="0" cellspacing="1" class=tablestyle>
							<TBODY>
								<TR align="center" class="fieldbg">
									<TD width="45" class="tablefield">选择</TD>
									<TD width="111" class="tablefield">角色代号</TD>
									<TD width="273" class="tablefield">有效状态</TD>
									<TD width="97" class="tablefield">角色描述</TD>
								</TR>
								<%for(int i=0,j=1; i<v.size(); i++,j+=3){%>
								<TR class="tableline">
									<TD width="45"><INPUT type="radio" name="DeptItem"
										onclick="SetValue('DeptItem','<%=j%>')"></TD>
									<TD width="111"><%=((HashMap)v.get(i)).get("AUTHCODE")+"　"%><INPUT
										type="hidden" name="DeptItem<%=j%>"
										value='<%=((HashMap)v.get(i)).get("AUTHCODE")%>'></TD>
									<TD width="114"><%=((HashMap)v.get(i)).get("STATE")+"　"%><INPUT
										type="hidden" name="DeptItem<%=j+1%>"
										value='<%=((HashMap)v.get(i)).get("STATE")%>'></TD>
									<TD width="273"><%=((HashMap)v.get(i)).get("AUTHDESC")+"　"%><INPUT
										type="hidden" name="DeptItem<%=j+2%>"
										value='<%=((HashMap)v.get(i)).get("AUTHDESC")%>'></TD>
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

<input type="hidden" value="Auth" name="hModuleID">
</form>
</security:securityForPage>
</BODY>
</HTML>
