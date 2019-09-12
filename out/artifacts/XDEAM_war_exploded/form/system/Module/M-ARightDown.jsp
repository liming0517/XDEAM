<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<%@ page 
language="java"
contentType="text/html; charset=GBK"
pageEncoding="GBK"
%>
<%@ page import="java.util.HashMap,java.util.Vector"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<jsp:useBean id="Area" class="com.pantech.src.develop.system.SystemClass" scope="page"/>
<META http-equiv="Content-Type" content="text/html; charset=GBK">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<META http-equiv="Content-Type" content="text/html; charset=GB18030">
<META HTTP-EQUIV="expires" CONTENT="Wed, 26 Feb 1978 08:21:57 GMT">
<META http-equiv="Content-Style-Type" content="text/css">
<link href="<%=request.getContextPath()%>/css/wiserunoa.css" rel="stylesheet" type="text/css">
<TITLE>角色人员列表</TITLE>
<script>
function doSubmit(type){
var o = eval(document.form1);
var moduleCode = document.form1.hModuleCode.value;

	if(o.AreaCode.value==""){
	  alert("区域代码不得为空！");
	  return;
	}

if(type == "add"){ //为该角色添加新的人员	
	//alert(deptCode);
	o.action = "<%=request.getContextPath()%>/Svl_System?type=add&ModuleCode="+moduleCode;
	o.submit();
}
else if(type == "mod"){//修改
	o.action = "<%=request.getContextPath()%>/Svl_System?type=mod&ModuleCode="+moduleCode;
	o.submit();
}
else if(type == "del"){//删除
	o.action = "<%=request.getContextPath()%>/Svl_System?type=del&ModuleCode="+moduleCode;
	o.submit();
}

}

/**
 * 选择一条记录后，将值赋于文本框
 */
function SetValue(prefix,i){
	//区域代码
	var a = eval("document.form1."+ prefix + i);
	//
	var b = eval("document.form1."+ prefix + (parseInt(i)+parseInt(1)));
	//区域名称
	var c = eval("document.form1."+ prefix + (parseInt(i)+parseInt(1)));
	//alert(document.form1.UserItem1.value);
	document.form1.AreaCode.value = a.value;
	document.form1.oldAreaCode.value = a.value;
	
	document.form1.AreaName.value = b.value;
	document.form1.oldAreaName.value = b.value;
	
	//alert(c.value);
	if(c.value == "Y" || c.value=="y"){		
		document.form1.AreaState[0].checked = true;
	}else{
		document.form1.AreaState[1].checked = true;
	}
	//document.form1.UserState.value = c.value;
	document.form1.oldAreaState.value = c.value;
	
}
</script>
</HEAD>
<BODY>
<%
String moduleCode = request.getParameter("ModuleCode");
Vector v = Area.GetAreaInfo(moduleCode);
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
			<P class="talbetitle">区域信息</P>
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
									<TD>区域代码</TD>
									<TD><INPUT type="text" name="AreaCode">
									<INPUT type="hidden" name="oldAreaCode"></TD>
									<TD>区域名称：</TD>
									<TD><INPUT type="text" name="AreaName">
									<INPUT type="hidden" name="oldAreaName"></TD>
									<TD>状态：</TD>
									<TD>有效<INPUT type="radio" name="AreaState" value="Y">无效<INPUT
										type="radio" name="AreaState" value="N"><INPUT type="hidden"
										name="oldAreaState"></TD>
								</TR>
								<TR>
									<TD align="left" colspan="6"></TD>
								</TR>
								<TR>
									<TD colspan="6" align="center">
									<TABLE border="0" width="70%">
										<TBODY>
											<TR>
												<TD colspan="3" align="center">
												<INPUT type="button" value="添加" onclick="doSubmit('add')">
												<INPUT type="button" value="修改" onclick="doSubmit('mod')"> 
												<INPUT type="button" value="删除" onclick="doSubmit('del')"></TD>
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
<input type="hidden" value="ModuleArea" name="hModuleID">

<br><br>
<TABLE width="98%" border="0" cellspacing="0" cellpadding="0"
	bgcolor="FFFFFF">
	<TBODY>
		<TR>
			<TD width="1%" height="28"><IMG
				src="<%=request.getContextPath()%>/images/border_r1_c1.gif"
				width="16" height="28"></TD>
			<TD width="97%" class="tabletop">
			<P class="talbetitle" align="center"><%=MyTools.StrFiltr(moduleCode)%>模块下属区域列表</P>
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
									<TD class="tablefield" align="center">区域代码</TD>
									<TD class="tablefield" align="center">区域名称</TD>									
								</TR>
								<%for(int i=0,j=1; i<v.size(); i++,j+=3){%>
								<TR class="tableline">
									<TD width="45"><INPUT type="radio" name="UserItem"
										onclick="SetValue('UserItem','<%=j%>')"></TD>
									<TD width="111"><%=((HashMap)v.get(i)).get("MODAREACODE")+"　"%> <INPUT
										type="hidden" name="UserItem<%=j%>"
										value='<%=((HashMap)v.get(i)).get("MODAREACODE")%>'></TD>
									<TD width="97"><%=((HashMap)v.get(i)).get("AREANAME")+"　"%> <INPUT
										type="hidden" name="UserItem<%=j+1%>"
										value='<%=((HashMap)v.get(i)).get("AREANAME")%>'></TD>								
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
<input type="hidden" value="<%=moduleCode%>" name="hModuleCode">
</form>
</BODY>
</HTML>
