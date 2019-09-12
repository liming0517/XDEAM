<%
//@author liuyi
//类别设置框架
//创建日期：2005.5.20
%>

<%@ page 
language="java"
contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"
%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.src.develop.common.Cls_CommonHtmlData"%>
<%@ taglib uri="/WEB-INF/taglib/securityTag.tld" prefix="security"%>
<jsp:useBean class="com.pantech.base.common.tags.security.PurViewTools" id="pc" scope="page" />
<%String mcode = pc.GetModuleCodeFromUrl(request);%>


<%
	//界面
	Cls_CommonHtmlData cchd=new Cls_CommonHtmlData(request.getContextPath());
	//根节点
	String rootType=MyTools.StrFiltr(request.getParameter("rootType"));
%>

<%out.print(cchd.setHtmlTop("类别设置"));%>

<frameset id="TypeInfo" cols="*,40%" frameborder="NO" border="0" framespacing="0" style="overflow:hidden">
	<frame id="editarea" src="InfoTypeSetting.jsp" scrolling=auto frameborder=0 noresize>
	<frame id="treearea" src="<%=request.getContextPath()%>/form/system/TreeList.jsp?rootType=<%=rootType%>&type=TypeConfig" scrolling=auto frameborder=0 noresize>
</frameset>

<%out.print(cchd.setBodyTop());%>

<%out.print(cchd.setHtmlBottom());%>

