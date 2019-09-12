<!--
Author:Zedee
CreateDate:20050631
Function:代码自动化-视图列表
-->
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<%@ page 
language="java"
contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"
%>
<%@ page import="java.util.Vector"%>
<%@ page import="com.pantech.base.common.autocode.Cls_DBStruct"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%String type = MyTools.StrFiltr(request.getParameter("type"));
Vector vTable = new Vector();
Vector vView = new Vector();
Cls_DBStruct cf = new Cls_DBStruct(request);
//	if(type.equalsIgnoreCase("U")){
//		vTable = cf.GetTableList("U");
//	}else if(type.equalsIgnoreCase("V")){
vView = cf.GetTableList("V");
//	}
%>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<link href="<%=request.getContextPath()%>/css/wiserunoa.css" rel="stylesheet" type="text/css">
<TITLE>数据库对象列表</TITLE>
<SCRIPT language="JavaScript">
/*
function Show(obj){
	var a = document.all[obj];
	for(var i=0; i<a.length; i++){
		a[i].style.display = (a[i].style.display=="none"?"":"none"); 	
	}
}

function init(obj){
	var a = document.all[obj];
	for(var i=0; i<a.length; i++){
		a[i].style.display = "none"; 	
	}
}
*/
function refreshInfo(id){
	parent.editarea.location.href = "<%=request.getContextPath()%>/common/autocode/DBStructAnalyze.jsp?id=" + id;
}

function loadInfo(name){
	opener.document.form1.USERTABLE.value = name;
	window.close();
}
</SCRIPT>
</HEAD>
<BODY leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" class="contentbg">
<table width="100%" cellspacing="0" cellpadding="0">
	<tr>
		<td class="ets_normal_top" align="center">视图列表</td>
	</tr>
	<tr>
		<td align="center" width="100%">
		<TABLE class="tablestyle" cellspacing="1" cellpadding="0" width="100%" id="Result">
			<TBODY>
				
				<%if (vView.size() == 0) {%>
				<TR class="tableline">
					<TD colspan=9>没有记录．．．．</TD>
				</TR>
				<%} else {
    //循环显示本页记录
    for (int i = 0; i < vView.size(); i+=2) {%>
				<TR style="cursor: hand" onmouseover="this.style.background='#E5FFDE'" onmouseout="this.style.background='#FFFFFF'" bgColor="#FFFFFF">
					<td><INPUT type="radio" name="VIEW" onClick="parent.editarea.location.href='<%=request.getContextPath()%>/common/autocode/DBStructAnalyze.jsp?id=<%=MyTools.StrFiltr((String)vView.get(i))%>'"><%=MyTools.StrFiltr((String)vView.get(i+1))%></td>

				</TR>
				<%}
}%>
			</TBODY>
		</TABLE>
		</TD>
	</TR>
</table>


</BODY>
</HTML>
