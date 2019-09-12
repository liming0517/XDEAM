<!--
Author:KotLiu
CreateDate:2005/04/06
Function:在线帮助设置界面
-->
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<%@ page
language="java"
contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"
%>
<%@ page import="com.pantech.src.develop.helpinfo.HelpInfoBean"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="java.util.ArrayList"%>
<%
	ArrayList arr=(ArrayList)request.getAttribute("HelpInfo");
	HelpInfoBean hib;
	int para=-1;
	String name="";
	String state="";
	if(arr!=null){
		hib=(HelpInfoBean)arr.get(0);
		state=(String)arr.get(1);
	}else{
		para=MyTools.parseInt(request.getParameter("id"));
		name=MyTools.StrFiltr(request.getParameter("name"));
		hib=new HelpInfoBean(para,name,request);
	}
%>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<link href="<%=request.getContextPath()%>/css/wiserunoa.css" rel="stylesheet" type="text/css">
<TITLE>HelpList.jsp</TITLE>
<Script language="javascript">
	function dosubmit(val){
		document.all.icContent.value=editFrame.idEditbox.document.body.innerHTML;
		if(document.all.icContextPath.value==""){
			alert("请先选择右边的菜单！");
			return false;
		}
		document.all.active.value=val;
		document.form1.submit();
	}
	function resizeFrame(){
		var mainHeight = document.body.offsetHeight-160;
		document.getElementById("editFrame").style.height=""+ mainHeight +"px";
	}
	function loadInfo(){
		if(editFrame.idEditbox){
			editFrame.idEditbox.document.body.innerHTML=document.all.icContent.value;
		}
	}
</script>
</HEAD>
<BODY leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" class="contentbg" onload='resizeFrame();loadInfo();'>
<form action="<%=request.getContextPath()%>/Svl_HelpInfo" name="form1" method="post">
<table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="FFFFFF">
	<tr>
		<td width="1%"><img src="<%=request.getContextPath()%>/images/border_r1_c1.gif" width="16" height="28" class="menu2"></td>
		<td width="96%" class="tabletop"><p class="talbetitle">在线帮助设置</p></td>
		<td width="3%"><img src="<%=request.getContextPath()%>/images/border_r1_c4.gif" width="16" height="28"></td>
	</tr>
	<tr>
		<td class="tableleft"></td>
		<td>
			<TABLE border="0" width="100%" cellspacing="1" cellpadding="1" class="tableborder">
				<tr class="trstyle">
					<td>页面名称</td>
					<td><input type="text" name="icMenuName" value="<%=hib.getMenuName()%>"><%=state%></td>
				</tr>
				<tr class="trstyle">
					<td>标题</td>
					<td><input type="text" name="icTitle" value="<%=hib.getTitle()%>"></td>
				</tr>
				<tr class="trstyle">
					<td valign="top">内容</td>
					<td>
						<textarea rows="6" cols="50" name="icContent" style="display:none"><%=hib.getContent()%></textarea>
						<IFRAME ID="editFrame" src="<%=request.getContextPath()%>/base/config/HelpInfoEdit.jsp" frameborder="0" scrolling="no" height="310" width="100%"></IFRAME>
					</td>
					
				</tr>
				<tr class="trstyle">
					<td colspan="2" align="center">
						<input type="button" value=" 确定 " onclick="dosubmit('save');">
						<input type="button" value=" 删除 " onclick="dosubmit('delete')">
						<input type="hidden" name="active" value="">
						<input type="hidden" name="icContextPath" value="<%=hib.getContextPath()%>">
						<input type="hidden" name="ReqUrl" value="<%=request.getContextPath()%>/base/config/HelpList.jsp">
					</td>
				</tr>
			</table>
		</td>
		<td class="tableright"></td>
	</tr>
	<tr>
		<td valign="top"><img src="<%=request.getContextPath()%>/images/border_r3_c1.gif" width="16" height="19"></td>
		<td class="tablebottom">&nbsp;</td>
		<td valign="top"><img src="<%=request.getContextPath()%>/images/border_r3_c4.gif" width="16" height="19"></td>
	</tr>
</table>
</form>
</BODY>
</HTML>
