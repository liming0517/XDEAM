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
<META http-equiv="Content-Style-Type" content="text/css">
<link href="<%=request.getContextPath()%>/css/wiserun.css" rel="stylesheet" type="text/css">
<TITLE>权限设置</TITLE>
<script>
/*
 * 提交
 */
function doSubmit(){
	var o = eval("document.form1");
	
	var bx = o.PerCode.length;
	var n = 0;
	for(var i=0;i<bx; i++){
		if(o.PerCode[i].checked == true){
			n++;
		}
	}
	if( n==0 ){alert("请至少选择一项！^^");return;}
	
	o.action = "<%=request.getContextPath()%>/Svl_SetPerCode";
	//alert(o.action);
	o.submit();
}

function delChoose(){
	var elArr = document.form1.elements; // 将表单中的所有元素放入数组

	for(var i = 0; i < elArr.length; i++){
		with(elArr[i]){              // 对于表单中的每一个元素...
			if(elArr[i].type=="checkbox"){
				elArr[i].checked = false;
			}
		} 
	}
}
</script>
</HEAD>
<%
SystemClass SC = new SystemClass(request);
//传递过来的用户代码
String userCode = request.getParameter("UserCode");
//角色信息
Vector authV = SC.GetAuthInfo();
//部门信息
Vector deptV = SC.GetDeptInfo();
%>


<BODY>
<form name="form1" action="" method="Post">
<TABLE width="98%" border="0" cellspacing="0" cellpadding="0"
	bgcolor="FFFFFF">
	<TBODY>
		<TR>
			<TD width="1%" height="28"><IMG
				src="<%=request.getContextPath()%>/images/border_r1_c1.gif"
				width="16" height="28"></TD>
			<TD width="97%" class="tabletop">
			<P class="talbetitle" align="center">权限代码列表</P>
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
						<TABLE border="0" width="100%" cellpadding="1" cellspacing="1">
							<TBODY>
								<TR class="fieldbg">
									<TD>角色</TD>
									<TD>中文描述</TD>
									<TD >部门</TD>
									<TD >中文描述</TD>
								</TR>
								<TR>
									<TD valign="top" colspan="2">
									<TABLE border="0" width="100%" cellpadding="1" cellspacing="1">
										<TBODY>
										<%for(int i=0; i<authV.size(); i++){
											String perCode = MyTools.StrFiltr(((HashMap)authV.get(i)).get("AUTHCODE"));
										%>
											<TR>
												<TD><input type="checkbox" name="PerCode" value="<%=((HashMap)authV.get(i)).get("AUTHCODE")%>" <%=SC.PerCodeState(perCode,userCode)%>><%=perCode%></TD>
												<TD><%=((HashMap)authV.get(i)).get("AUTHDESC")%></TD>
											</TR>
											<%}%>
										</TBODY>
									</TABLE>
									</TD>
									<TD valign="top" colspan="2">
									<TABLE border="0" width="100%" cellpadding="1" cellspacing="1">
										<TBODY>
										<%for(int j=0; j<deptV.size(); j++){
											String perCode = MyTools.StrFiltr(((HashMap)deptV.get(j)).get("DEPTCODE"));
										%>
											<TR>
												<TD><INPUT type="checkbox" name="PerCode" value="<%=((HashMap)deptV.get(j)).get("DEPTCODE")%>" <%=SC.PerCodeState(perCode,userCode)%>><%=perCode%></TD>
												<TD><%=((HashMap)deptV.get(j)).get("CNAME")%></TD>
											</TR>
											<%}%>
										</TBODY>
									</TABLE>
									</TD></TR>
								<TR>
									<TD colspan="3"><input type="button" value="确定" onclick="doSubmit()"><input type="reset" value="重新选择"><INPUT
										type="button" value="取消选定" onClick="delChoose();"></TD>
									<TD></TD>
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
<input type="hidden" name="UserCode" value="<%=userCode%>">
</form>
</BODY>
</HTML>
