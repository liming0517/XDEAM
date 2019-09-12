<%
//@author YH
//权限设置框架
//创建日期：2009.8.25
%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
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

<%out.print(cchd.setHtmlTop("权限设置"));%>

<frameset id="RankInfo" cols="*,40%" frameborder="NO" border="0" framespacing="0" style="overflow:hidden">
	<frame id="editarea" src="AuthDeptList.jsp" scrolling=auto frameborder=0 noresize>
	<frame id="treearea" src="RankList.jsp?selectMode=1&type=Config" scrolling=auto frameborder=0 noresize>
</frameset>

<%out.print(cchd.setBodyTop());%>

<%out.print(cchd.setHtmlBottom());%>

