
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="org.jasig.cas.client.validation.Assertion"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
	<HEAD>
		<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<%
			String refreshTag = "";//用于刷新页面的标签
			String anchorTag = "";//<a>标签
			String ssoLogin = request.getSession().getServletContext().getInitParameter("ssoLogin");//SSO方式登录

			if (ssoLogin != null && !"".equals(ssoLogin) && "true".equals(ssoLogin)) {//SSO单点退出
				//Object object = request.getSession().getAttribute("_const_cas_assertion_");
				//if (object != null) {//SSO的CAS用户名存在
					//request.getSession().invalidate();
					refreshTag = "<meta http-equiv='refresh' content='3; url=http://192.168.168.213:8080/CAS/logout?service=" + basePath + "ssoProcessPage'>";
					anchorTag = "<a href='http://192.168.168.213:8080/CAS/logout?service=" + basePath + "ssoProcessPage'>" + "如果您的浏览器没有自动跳转，请点击这里</a>";
					//request.getSession().removeAttribute("_const_cas_assertion_");//退出时，清除SSO单点登录的用户 信息 
					
				//}
			} else {//正常或其他方式登陆
				refreshTag = "<meta http-equiv='refresh' content='3; url=" + basePath + "'>";//注销用户
				anchorTag = "<a href='"+ basePath +"'>" + "如果您的浏览器没有自动跳转，请点击这里</a>";  
				request.getSession().setAttribute("UserProfile",null);
			}
			out.print(refreshTag);
		%>
		<TITLE>注销</TITLE>
		<!-- JQuery文件引用 开始 
		<link rel="stylesheet" type="text/css" href="css/JQueryUI/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="css/JQueryUI/themes/icon.css">
		<script type="text/javascript" src="script/JQueryUI/jquery-1.8.0.min.js">
</script>
		<script type="text/javascript" src="script/JQueryUI/jquery.easyui.min.js">
</script>
		<script type="text/javascript" src="script/JQueryUI/locale/easyui-lang-zh_CN.js">
</script>-->
		<!-- JQuery文件引用 结束-->
	
	</HEAD>
	<BODY >
		系统已注销，三秒后跳转到登陆页
		<%=anchorTag%>
		<div id="content" name="content"  style="display:none">
		<iframe src="http://192.168.168.213:8080/SSO/logoutall.jsp"></iframe>
		</div>
	</BODY>
</HTML>
