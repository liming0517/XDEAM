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
<TITLE>权限代码列表</TITLE>
<%SystemClass DC=new SystemClass(request);%>
<script>
function doSubmit(type){
var o = eval(document.form1);
//从右下框架处获得的权限代码
var perCode = document.form1.hPerCode.value;

	if(document.form1.PerCode.value=="" && document.all.ownDept.checked==false && document.all.ownCompany.checked==false ){
	  alert("请输入数据！");
	  return;
	}
if(type == "add"){ //添加新的权限范围	
	//alert(deptCode);
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
	//区域代码
	var a = eval("document.form1."+ prefix + i);
	//
	//var b = eval("parent.rightdown.document.form1."+ prefix + (parseInt(i)+parseInt(1)));
	//区域名称
	//var c = eval("parent.rightdown.document.form1."+ prefix + (parseInt(i)+parseInt(1)));
	//alert(document.form1.UserItem1.value);
	document.form1.PerCode.value = a.value;
	document.form1.oldPerCode.value = a.value;
	
	//document.form1.AreaName.value = b.value;
	//document.form1.oldAreaName.value = b.value;
	
	//alert(c.value);
	//if(c.value == "Y" || c.value=="y"){		
	//	document.form1.AreaState[0].checked = true;
	//}else{
	//	document.form1.AreaState[1].checked = true;
	//}
	//document.form1.UserState.value = c.value;
	//document.form1.oldAreaState.value = c.value;
	
}
</script>
</HEAD>
<BODY class="contentbg">
<%
String perCode = request.getParameter("PerCode");
//部门还是角色？
String showType = request.getParameter("ShowType");
Vector v = DC.GetSubInfo(perCode);

//200501208 hades 允许设定对本部门，全公司范围的数据操作权
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
			<P class="talbetitle">[<%=DC.ShowText(showType)%>]信息</P>
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
						<TABLE border="0" width="100%" cellspacing="1" cellpadding="1" class="tableborder">
							<TBODY>
								<TR class="trstyle">
									<TD><%=DC.ShowText(showType)+MyTools.StrFiltr(perCode)%>可访问的数据范围</TD>
									<TD><INPUT type="text" name="PerCode"> <INPUT type="hidden" 	name="oldPerCode"></TD>
								</TR>
								<TR class="trstyle">
									<TD>（请输入角色代码或部门代码）</TD>
									
									<TD>
									   <INPUT type="checkbox" name="ownDept" value="dept">本人部门
   									   <INPUT type="checkbox" name="ownCompany" value="company">全公司
									</TD>
								</TR>
								<TR class="trstyle">
									<TD colspan="2" align="center">
									<TABLE border="0" width="70%">
										<TBODY>
											<TR>
												<TD colspan="3" align="center"><INPUT type="button"
													value="添加" onclick="doSubmit('add')"> <INPUT
													type="button" value="删除"
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
<BR>
<TABLE width="98%" border="0" cellspacing="0" cellpadding="0"
	bgcolor="FFFFFF">
	<TBODY>
		<TR>
			<TD width="1%" height="28"><IMG
				src="<%=request.getContextPath()%>/images/border_r1_c1.gif"
				width="16" height="28"></TD>
			<TD width="97%" class="tabletop">
			<P class="talbetitle" align="center"><%=DC.ShowText(showType)+MyTools.StrFiltr(perCode)%>可访问的数据范围列表</P>
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
						<table width="100%" border="0" cellspacing="1" cellpadding="0" class="tableborder">
							<TBODY>
								<TR class="trstyle">
									<TD class="tablefield" align="center">操作</TD>
									<TD class="tablefield" align="center">权限代码</TD>									
								</TR>
								<%for(int i=0,j=1; i<v.size(); i++,j+=3){%>
								<TR class="tableline">
									<TD width="45"><INPUT type="radio" name="UserItem"
										onclick="SetValue('UserItem','<%=j%>')"></TD>
									<TD width="111">
									<%
									String type = MyTools.ObjFiltr(((HashMap)v.get(i)).get("DATAPERCODE"));
									if(type.equals("*")){
									%>
										本部门
								<%  }else if(type.equals("**")){%>
										全公司
								<%	}else{%>
										<%=((HashMap)v.get(i)).get("DATAPERCODE")+"　"%> 
								<%	}%>
										<INPUT type="hidden" name="UserItem<%=j%>" value='<%=((HashMap)v.get(i)).get("DATAPERCODE")%>'>
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
<input type="hidden" value="<%=perCode%>" name="hPerCode">
<input type="hidden" value="PerData" name="hModuleID">
</form>
</BODY>
</HTML>
