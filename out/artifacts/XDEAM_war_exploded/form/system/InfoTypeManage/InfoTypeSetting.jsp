<%
//页面名称：form/system/InfoTypeManage/InfoTypeSetting.jsp
//页面作用：类别信息管理，主要用于管理系统中所有用到的固定类别
//制作人员：Andrew.U
//创建时间：2006.1.11
//其他说明：重要功能，框架性模块
%>
<%@ page 
language="java"
contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"
%>
<%@ page import="com.pantech.src.develop.common.Cls_CommonHtmlData"%>
<%@ page import="com.pantech.src.develop.infotype.InfoTypeBean"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ taglib uri="/WEB-INF/taglib/util.tld" prefix="util" %>
<%
//界面类
Cls_CommonHtmlData cchd=new Cls_CommonHtmlData(request.getContextPath());

//界面数据类
InfoTypeBean itb=(InfoTypeBean)request.getAttribute("InfoType");
if(itb==null){
	itb=new InfoTypeBean(MyTools.StrFiltr(request.getParameter("TypeCode")).trim(),request);
}

//处理页面同步刷新
String needRefresh=MyTools.StrFiltr(session.getValue("needRefresh")).trim();
session.removeValue("needRefresh");
String onLoadEvent="";
if (needRefresh.equalsIgnoreCase("true")) {
   onLoadEvent="onload=doRefresh();";
}
%>

<%out.print(cchd.setHtmlTop());//页面头部分%>
<%out.print(cchd.setBodyTop(onLoadEvent));%>

<form action="<%=request.getContextPath()%>/Svl_InfoTypeManage" method="Post" name="form1">

<%out.print(cchd.setTableTop("类别信息设置 <font color=red>"+itb.getMsg()+"</font>"));%>

<table width="100%" border="0" cellspacing="1" class="tableborder" cellpadding="0">
	<tr class="trstyle">
		<td>类别代码：</td>
		<td><input type="text" size="25" maxlength="20" name="icTypeCode" value="<%=itb.getTypeCode()%>"></td>
	</tr>
	<tr class="trstyle">
		<td>类别名称：</td>
		<td><input type="text" size="25" maxlength="50" name="icTypeName" value="<%=itb.getTypeName()%>"></td>
	</tr>
	<tr class="trstyle">
		<td>类别描述：</td>
		<td><textarea rows="3" cols="40" name="icTypeDesc"><%=MyTools.convertChar(itb.getTypeDesc())%></textarea></td>
	</tr>
	<tr class="trstyle">
		<td>类别数据：</td>
		<td><input type="text" size="25" maxlength="50" name="icTypeValue" value="<%=itb.getTypeValue()%>"></td>
	</tr>
	<tr class="trstyle">
		<td>状态：</td>
		<td><util:select name="icTypeState" width="100" colNameT="类别名称" colNameV="类别数据" table="V_信息类别_类别操作" condition="where upper(父类别代码)='00005'" order="排序" selected="<%=itb.getTypeState()%>"/></td>
	</tr>
	<TR class="trstyle">
		<TD colspan="2" align="center">
			<img src="<%=request.getContextPath()%>/images/But/xz.gif" name="show" onclick="SwapDiv('')">
			<!--INPUT type="button" value="新增" name="show" onclick="SwapDiv('')"-->
			<img src="<%=request.getContextPath()%>/images/But/xg.gif" name="mod" onclick="doSubmit(this.name)">
			<!--INPUT type="button" value="修改" name="mod" onclick="doSubmit(this.name)"-->
			<img src="<%=request.getContextPath()%>/images/But/shanc.gif" name="del" onclick="doSubmit(this.name)">
			<!--INPUT type="button" value="删除" name="del" onclick="doSubmit(this.name)"-->
			<!--<img src="<%=request.getContextPath()%>/images/But/bj.gif" name="up" onclick="doSubmit(this.name)">
			input type="button" value="上移" name="up" onclick="doSubmit(this.name)"-->
			<!--<img src="<%=request.getContextPath()%>/images/But/bj.gif" name="down" onclick="doSubmit(this.name)">
			input type="button" value="下移" name="down" onclick="doSubmit(this.name)"-->
			<input type="hidden" name="ReqUrl" value="/form/system/InfoTypeManage/InfoTypeSetting.jsp">
			<input type="hidden" name="active">
			<input type="hidden" name="CurCode" value="<%=itb.getTypeCode()%>">
			<input type="hidden" name="ParCode" value="<%=itb.getParCode()%>">
			<input type="hidden" name="CurSort" value="<%=itb.getTypeSort()%>">
			<input type="hidden" name="CurLevel" value="<%=itb.getTypeLevel()%>">
		</TD>
	</TR>
</table>

<%out.print(cchd.setTableBottom());%>

<div id=NodeInfo style="display:none">
<%out.print(cchd.setTableTop("添加子类别"));%>

<table width="100%" border="0" cellspacing="1" class="tableborder" cellpadding="0">
	<tr class="trstyle">
		<td>子类别代码：</td>
		<td><input type="text" size="25" maxlength="20" name="icTypeCodeNew"></td>
	</tr>
	<tr class="trstyle">
		<td>子类别名称：</td>
		<td><input type="text" size="25" maxlength="50" name="icTypeNameNew"></td>
	</tr>
	<tr class="trstyle">
		<td>子类别描述：</td>
		<td><textarea rows="3" cols="40" name="icTypeDescNew"></textarea></td>
	</tr>
	<tr class="trstyle">
		<td>子类别数据：</td>
		<td><input type="text" size="25" maxlength="50" name="icTypeValueNew"></td>
	</tr>
	<tr class="trstyle">
		<td>状态：</td>
		<td><util:select name="icTypeStateNew" width="100" colNameT="类别名称" colNameV="类别数据" table="V_信息类别_类别操作" condition="where upper(父类别代码)='00005'" order="排序" /></td>
	</tr>
	<TR class="trstyle">
		<TD colspan="2" align="center">
			<INPUT type="button" name="add" value=" 提交 " onclick="doSubmit(this.name)">
			<INPUT type="button" name="cancel" value=" 取消 " onclick="SwapDiv('cancel')">
		</TD>
	</TR>
</table>

<%out.print(cchd.setTableBottom());%>
</div>

</form>

<%out.print(cchd.setHtmlBottom());%>


<script>
//显示新增结点信息的表格
function SwapDiv(id){
	if(id == "cancel"){ //用来隐藏表格
		NodeInfo.style.display = "none";
	}else{
		NodeInfo.style.display = "";
	}
}
//提交页面
function doSubmit(id){
	if(checkform(id)){
		document.form1.active.value=id;
		document.form1.submit();
	}
}
//页面信息检查
function checkform(id){
	return true;
}
//刷新类别树的页面
function doRefresh(){
	parent.treearea.location.reload();
}
</script>