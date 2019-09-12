<!--
Author:Zedee
CreateDate:20050631
Function:代码自动化-设置界面
-->
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<%@ page 
language="java"
contentType="text/html; charset=utf-8"
pageEncoding="utf-8"
%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.base.common.autocode.Cls_DBStruct"%>
<%@ page import="com.pantech.base.common.autocode.Cls_DBStructAnalyze"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.HashMap"%>
<%
	//用户表/视图ID
	String id = MyTools.StrFiltr(request.getParameter("id"));
	//得到表结构
	Cls_DBStruct cf = new Cls_DBStruct(request);
	String table = cf.GetTableNameByID(id);
	Vector v = new Vector();
	Cls_DBStructAnalyze cda = new Cls_DBStructAnalyze(request);
	v = cda.GetTableStruct(table);
	HashMap hm = new HashMap();
	//返回的操作消息
  	String msg = "";
	if(request.getAttribute("MSG")!=null){
		msg = (String)request.getAttribute("MSG");
		request.removeAttribute("MSG");
	}
	//页面URL
	String url = "";
	if(request.getAttribute("URL")!=null){
		url = (String)request.getAttribute("URL");
	}else{
		url = request.getRequestURL().toString();
	}
	//取得表结构VECTOR
	Vector vStructs ; //= cf.GetDBStruct();
	
	//获取视图SQL语句 hades
	String sViewSql="";//= cda.GetViewSql(table);
	
%>
<META http-equiv="Content-Type" content="text/html; charset=utf-8">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<link href="<%=request.getContextPath()%>/css/wiserunoa.css" rel="stylesheet" type="text/css">
<TITLE>DBStructAnalyze.jsp</TITLE>
<script language="JavaScript"
	src="<%=request.getContextPath()%>/script/VerifyForm.js"></script>
<SCRIPT language="JavaScript">
function doSubmit(form){
	/*if(validateInput(form)){
		//if(validateForm(form)){
			if(confirm("若文件已存在，重新生成时将覆盖原有文件！是否生成代码？")){	
				return validateForm(form);
			}else{
				return false;
			}			
		//}else{
		//	return false;
		//}
	}else{
		return false;
	}*/
	return true;
}
function validateInput(form){
	if(form.TABLENAME.value==""){
		alert("请从左侧列表中选择视图");
		return false;
	}
	if(form.PACKAGE.value==""){
		alert("请输入SERVLET/BEAN所属包名");
		form.PACKAGE.focus();
		return false;
	}
	if(form.FOLDER.value==""){
		alert("请输入JSP文件所属目录名");
		form.FOLDER.focus();
		return false;
	}
	if(form.FUNCDESCEN.value==""){
		alert("请输入英文功能描述");
		form.FUNCDESCEN.focus();
		return false;
	}
	if(form.FUNCDESCCN.value==""){
		alert("请输入中文功能描述");
		form.FUNCDESCCN.focus();
		return false;
	}
	if(form.FIELDNAME!=null){
		if(form.FIELDNAME.length!="undefined"){
			for(var i = 0;i<form.FIELDNAME.length;i++){
				if(form.FIELDNAME[i].value==""){
					alert("请输入视图字段对应的英文名,或通过视图结构SQL语句生成");
					form.FIELDNAME[i].focus();
					return false;
				}
			}
		}else{
			if(form.FIELDNAME.value==""){
				alert("请输入视图字段对应的英文名,或通过视图结构SQL语句生成");
				form.FIELDNAME.focus();
				return false;
			}
		}
	}
	if(form.FUNCTION.value!="2"){
		if(form.PRIMARYKEY.length!="undefined"){
			for(var i = 0;i<form.PRIMARYKEY.length;i++){
				if(form.PRIMARYKEY[i].checked){
					return true;
				}
			}		
			alert("请选择作为主键的视图字段");
			return false;
		}else{
			if(!form.PRIMARYKEY.checked){
				alert("请选择作为主键的视图字段");
				form.PRIMARYKEY.focus();
				return false;
			}
		}		
	}
	var flag = false;
	if(form.FUNCTION.value!="1"){
		if(form.QUERYCON.length!="undefined"){
			for(var i = 0;i<form.QUERYCON.length;i++){
				if(form.QUERYCON[i].checked){
					flag = true;
					break;
				}
			}
			if(!flag){
				alert("请选择作为查询条件的视图字段");
				return false;
			}else{
				flag = false;
				for(var i = 0;i<form.LISTFIELD.length;i++){
					if(form.LISTFIELD[i].checked){
						return true;
					}
				}
				alert("请选择在查询列表中显示的视图字段");
				return false;
			}
		}else{
			if(!form.QUERYCON.checked){
				alert("请选择作为查询条件的视图字段");
				return false;
			}
			if(!form.LISTFIELD.checked){
				alert("请选择在查询列表中显示的视图字段");
				return false;
			}
		}		
	}
	return true;
}
function openList(){
	var url;
	var arr=new Array();

	url="<%=request.getContextPath()%>/common/autocode/DBStructList.jsp?type=U";
	window.open(url,"usertable","Width=300px;Height=600px;status=yes");
	/*var returnVal = showModalDialog(url,"type","dialogWidth:300px;dialogHeight:400px");
	if (returnVal !=null){
		arr = splitStr(returnVal,";");
		if(arr.length>0){
			document.form1.TYPECODE.value = arr[0];
			document.form1.TYPENAME.value = arr[1];
		}
		
	}*/
}

function GetFieldName(){
	//alert(document.all.SQL.value);
	var sql = document.all.SQL.value;
	var arr = new Array();
	var iPos = 0;//" AS"的位置
	var fPos = 0;//表字段名的位置
	var lPos = 1;//分隔符","的位置
	
	sql = sql.replace(/\r\n/g," ");
	var res = "";//表字段名
	var field = "";//视图字段名
	while(lPos > 0){
		iPos = sql.toUpperCase().indexOf(" AS ");
		fPos = sql.lastIndexOf(".",iPos) + 1;//[tablename].[fieldname]
		if(fPos <= 0){
			fPos = sql.lastIndexOf(" ",iPos - 1);// [fieldname]
		}
		lPos = sql.indexOf(",",iPos);
		if(lPos < 0){
			lPos = sql.toUpperCase().indexOf(" FROM ");
		}
		
		field = sql.substring(iPos + 4,lPos);
		res = sql.substring(fPos,iPos);
		while (res.indexOf(" ")>=0){
			res = res.substring(res.indexOf(" ") + 1);
		}

		sql = sql.substring(lPos);
		for(var j=0;j<document.all.FIELDS.length;j++){
			if(document.all.FIELDS[j].value==field || "\"" + document.all.FIELDS[j].value + "\""==  field ){
				document.all.FIELDNAME[j].value=res;
				break;
			}
		}
	}
}

<%if(!msg.equals("")){%>
	alert("<%=msg%>");
<%}%>
</SCRIPT>
</HEAD>
<BODY>
<FORM name="form1" method="post" action="<%=request.getContextPath()%>/Svl_AutoCode" onSubmit="return doSubmit(this);">
<table width="100%" border="0" cellspacing="0" cellpadding="0"
	bgcolor="FFFFFF">
	<tr>
		<td width="1%" height="28"><img
			src="<%=request.getContextPath()%>/images/border_r1_c1.gif"
			width="16" height="28"></td>
		<td width="97%" class="tabletop" height="28">
		<p class="talbetitle">代码生成设置&nbsp;&nbsp;&nbsp;&nbsp;</p>
		</td>
		<td width="2%"><img
			src="<%=request.getContextPath()%>/images/border_r1_c4.gif"
			width="16" height="28"></td>
	</tr>
	<tr>
		<td class="tableleft">&nbsp;</td>
		<td>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td>
				<TABLE width="100%" cellpadding="0" cellspacing="1" class="tableborder">
					<TBODY>
						<tr class="trstyle">
							<td>视图名:</td>
							<td colspan="3">
								<%=table%>
								<input type="hidden" name="TABLENAME" value="<%=table%>">
								<input type="hidden" name="ReqUrl" value="<%=url%>">
							</td>
							<!--td>对应用户表：</td>
							<td>
								<INPUT type="text" name="USERTABLE" size="20" readonly style="cursor:hand" title="点击选择用户表" onClick="openList();">
								<INPUT type="button" value="提取字段" onClick="changeFields()">
							</td-->
						</tr>
						<tr class="trstyle">
							<td>生成代码功能类型:</td>
							<td colspan="3"><SELECT name="FUNCTION">
								<OPTION value="0" selected>编辑及查询</OPTION>
								<OPTION value="1">只有编辑</OPTION>
								<OPTION value="2">只有查询</OPTION>
							</SELECT></td>
						</tr>
						<tr class="trstyle">
							<td>Servlet/Bean所属包名:</td>
							<td><INPUT type="text" name="PACKAGE" size="30" validator="folderPat" cnName="功能描述(英文)"></td>
							<td>JSP所属目录名:</td>
							<td><INPUT type="text" name="FOLDER" size="30" validator="folderPat" cnName="功能描述(英文)"></td>
						</tr>
						<tr class="trstyle">
							<td>功能描述(英文):</td>
							<td><INPUT type="text" name="FUNCDESCEN" size="20" validator="charPat" cnName="功能描述(英文)"></td>
							<td>功能描述(中文):</td>
							<td><INPUT type="text" name="FUNCDESCCN" size="20" validator="chinesePat" cnName="功能描述(中文)"></td>
						</tr>
						<tr class="trstyle">
							<td colspan="4"><INPUT type="checkbox" name="PERMISSION">模块需要权限控制</td>
						</tr>
						<tr class="trstyle">
							<td valign="top">视图结构SQL语句：</td>
							<td colspan="3" align="left">
								<TEXTAREA rows="8" cols="80" name="SQL"><%=sViewSql%></TEXTAREA>
							</td>
						</tr>
						<tr class="trstyle">
							<TD colspan="4" align="center">
								<INPUT type="button" value="提取表字段名" onClick="GetFieldName()">
								<INPUT type="submit" value="生成代码">
							</TD>
						</tr>
					</tBody>
				</table>
				</td>
			</tr>
		</table>
		</td>
		<td class="tableright">&nbsp;</td>
	</tr>
	<tr>
		<td valign="top"><img
			src="<%=request.getContextPath()%>/images/border_r3_c1.gif"
			width="16" height="19"></td>
		<td class="tablebottom" height="19">&nbsp;</td>
		<td valign="top"><img
			src="<%=request.getContextPath()%>/images/border_r3_c4.gif"
			width="16" height="19"></td>
	</tr>
</table>
<TABLE class="tablestyle" cellspacing="1" cellpadding="0" width="100%">
	<TBODY>
		<TR class="fieldbg">
			<TD width="15%" align="center">视图字段名</TD>
			<TD width="15%" align="center">对应表字段名</TD>
			<TD width="18%" align="center">字段类型</TD>
			<TD width="5%" align="center">长度</TD>
			<TD width="12%" align="center">默认值</TD>
			<TD width="10%" align="center">作为主关键字</TD>
			<TD width="10%" align="center">作为查询条件</TD>
			<TD width="10%" align="center">在列表中显示</TD>
		</TR>		
	<%
		for(int i = 0;i<v.size();i++){
			hm = (HashMap)v.get(i);
	%>
		<TR style="cursor: hand" onmouseover="this.style.background='#E5FFDE'" onmouseout="this.style.background='#FFFFFF'" bgColor="#FFFFFF">
			<TD>
			<%if(hm.get("主键").toString().equalsIgnoreCase("Y")){%>
				<font color="red">
			<%}%>
					<%=hm.get("字段名")%>
			<%if(hm.get("主键").toString().equalsIgnoreCase("Y")){%>
				</font>
			<%}%>	
				<INPUT type="hidden" name="FIELDS" size="20" value="<%=hm.get("字段名")%>">			
			</TD>
			<TD><INPUT type="text" name="FIELDNAME" size="20">
			<!--div id = "field">
				<SELECT name="FIELDNAME" style="width:100%"></SELECT>
			</div-->
			</TD>
			<TD><%=hm.get("类型")%></TD>			
			<TD align="right"><%=hm.get("占用字节数")%></TD>
			<TD align="center"><%=hm.get("默认值")%></TD>
			<TD align="center"><INPUT type="checkbox" value="<%=hm.get("字段名")%>" name="PRIMARYKEY" <%=MyTools.WriteChecked(hm.get("主键").toString(),"Y")%>></TD>
			<TD align="center"><INPUT type="checkbox" value="<%=hm.get("字段名")%>" name="QUERYCON" checked></TD>
			<TD align="center"><INPUT type="checkbox" value="<%=hm.get("字段名")%>" name="LISTFIELD" checked></TD>
		</TR>			
	<%	
		}
	%>	
	</TBODY>
</TABLE>
</FORM>
</BODY>
</HTML>
