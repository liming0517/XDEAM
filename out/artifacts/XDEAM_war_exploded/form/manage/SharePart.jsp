<!-- 
编写人员：YH
页面作用：附件模块(SharePart.jsp)
创建日期：2008-08-28
 -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!-- page 包含 -->
<%@ page import="com.pantech.src.develop.common.Cls_CommonHtmlData"%>
<%@ page import="com.pantech.src.develop.store.user.*" %>
<%@ page import="com.pantech.base.common.tools.MyTools" %>
<%@ page import="com.pantech.base.common.db.DBSource" %>
<%@ page import="java.util.Date" %>

<%@ taglib uri="/WEB-INF/taglib/util.tld" prefix="util"%>
<%@ taglib uri="/WEB-INF/taglib/securityTag.tld" prefix="security"%>

<!-- 预处理 -->
<%
	//页面通用HTML代码输出类定义
	Cls_CommonHtmlData cchd=new Cls_CommonHtmlData(request.getContextPath());
	String msg = "";
	if(request.getAttribute("MSG")!=null){
		msg = (String)request.getAttribute("MSG");
		request.removeAttribute("MSG");
	}
	//记录JSP地址
	String url = "";
	if(request.getAttribute("URL")!=null){
		url = (String)request.getAttribute("URL");
	}else{
		url = request.getRequestURL().toString();
	}
	
	//模式：编辑edit 详细view
	String part=MyTools.StrFiltr(request.getParameter("part"));
	String type=MyTools.StrFiltr(request.getParameter("type"));
	String sButtonTitle="";
	if (type.equalsIgnoreCase("edit")){
	   sButtonTitle="上传/浏览";
	}else{
	   sButtonTitle="浏览";
	}
	
	//获取传递过来的值
	String CM_CUSCODE = MyTools.StrFiltr(request.getParameter("ctcode"));
	
	String PJ_PJNO = MyTools.StrFiltr(request.getParameter("pjcode"));
	
	//初始化DBSourse
//	需要查找文件是否缺
	DBSource db = new DBSource(request);
%>

<!-- jsp:useBean class="com.pantech.base.common.tags.security.PurViewTools" id="pc" scope="page" / -->
<%//String mcode = pc.GetModuleCodeFromUrl(request);%>


<!-- JS块 -->
<script language="JavaScript"
	src="<%=request.getContextPath()%>/script/VerifyForm.js"></script>
<script language="JavaScript"
	src="<%=request.getContextPath()%>/script/meizzDate.js"></script>
<script language="JavaScript">

function dosubmit(val){
	if(val=="que"){
		form1.active.value = val;
		form1.submit();
	}else if(val=="add"){
		location.href="<%=request.getContextPath()%>/form/frame.jsp?pjcode=&ctcode=";
	}
}

function doPrint(){
		document.form1.target = "_blank";
		document.form1.action = "<%=request.getContextPath()%>/form/Functions/PrintControl.jsp";
		document.form1.submit();
		document.form1.target = "";
		document.form1.action = "<%=request.getContextPath()%>/Svl_SalesQuery";
}

function doit(pjtype,filedecsc,filetype){
//<%=type%>
	var str = "<%=request.getContextPath()%>/form/File/FileList.jsp" +
				"?type=<%=type%>&pjcode=<%=MyTools.StrFiltr(PJ_PJNO)%>&pjtype=" + pjtype + "&realid=&filedesc=" + filedecsc + "&filetype=" + filetype + "&Refreah=T";
	window.open(str,'','state=no,width=800,height=600,resizable=yes');
}

</script>
<%out.print(cchd.setHtmlTop("相关附件"));//页面头部分%>
<style type="text/css">
<!--
.STYLE1 {
	font-size: 16px;
	font-weight:bold;
	}
-->
</style>

<body>
<form action="<%=request.getContextPath()%>/Svl_SalesQuery" method="Post" name="form1">

<table width="100%" border="0" align="center" cellpadding="0" cellspacing="1" class="tableborder">

	<tr>
		<td align="center" class="bg">
			<span>
				<IMG src="<%=request.getContextPath()%>/images/load1.gif" width="20" height="15">&nbsp;&nbsp;&nbsp;&nbsp;
				<% if (!PJ_PJNO.equalsIgnoreCase("")){  %>
					<a href="#" class="STYLE1" onClick="doit('<%=part%>','<%=part%>','附件');">
					  <%=sButtonTitle%>					 
					</a>
			 		<%if(!db.getResultFromDB("select count(*) from V_销售管理_对应附件 where 附件描述='"+ part +"' and 项目号='" + PJ_PJNO + "'")) {out.print("<font color=\"red\">（缺）</font>");}%>
			    <%}else { 
			         out.print("<font color=\"red\">（请先保存记录，然后上传附件！）</font>");
			    } %>
			</span>
		</td>
	</tr>


</table>
<%out.print(cchd.setTableBottom());%>
</form>
<%out.print(cchd.setHtmlBottom());//页面尾部分%>
