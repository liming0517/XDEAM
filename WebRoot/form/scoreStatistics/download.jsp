<%@ page contentType="text/html;charset=utf-8" import="com.jspsmart.upload.*" %><%
		//String fileCode = request.getParameter("fileCode");
		// 新建一个SmartUpload对象
	String fileCode="d:/补考安排表.xls";
	SmartUpload su = new SmartUpload("UTF-8");
		// 初始化
	su.initialize(pageContext);
		// 设定contentDisposition为null以禁止浏览器自动打开文件，
		//保证点击链接后是下载文件。若不设定，则下载的文件扩展名为
		//doc时，浏览器将自动用word打开它。扩展名为pdf时，
		//浏览器将用acrobat打开。
	su.setContentDisposition(null);
		// 下载文件
	su.downloadFile(fileCode);
%>