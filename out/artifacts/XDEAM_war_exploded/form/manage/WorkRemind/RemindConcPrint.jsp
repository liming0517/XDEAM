<% 
/**
文件说明
Create date: 2012.06.20
说明：
页面类型:我的联系单打印
创建人：caobei
**/
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page import="java.io.*,java.sql.*,net.sf.jasperreports.engine.*" %>
<%@ page import="com.microsoft.sqlserver.jdbc.SQLServerDriver" %>
<%@ page import="com.pantech.base.common.tools.MyTools" %>
<%@ page import="java.util.*"%>

<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="GENERATOR" content="Rational® Application Developer™ for WebSphere® Software">
</head>
<body>
<%
	String icWC_CODE = MyTools.StrFiltr(request.getParameter("icWC_CODE")); //编号
	String pKey = "icWC_CODE"; //参数名
	String pValue = icWC_CODE; //参数值
	
	File reportFile = new File(application.getRealPath("report4.jasper")); //报表编译之后的.jasper文件的存放位置
	String url = MyTools.getProp(request,"Base.PRINT_JNDI"); //从properties下的 Base.properties里获取配置JDBC的url地址
	String classPath = MyTools.getProp(request,"Base.PRINT_ClassforName"); //获取配置JDBC的类路径
	String userName = MyTools.getProp(request,"Base.PRINT_UserName"); //获取配置JDBC的用户名
	String password = MyTools.getProp(request,"Base.PRINT_Password"); //获取配置JDBC的密码
	
	Class.forName(classPath).newInstance(); //加载驱动类
	//传递报表中用到的参数值
	Map parameters = new HashMap();
	
	parameters.put(pKey,pValue);
	//连接到数据库
	Connection conn = DriverManager.getConnection(url,userName,password);
	//在控制台打印报表文件的物理路径 	
	byte[]bytes = JasperRunManager.runReportToPdf(reportFile.getPath(),parameters,conn);
	
	response.setContentType("application/pdf");
	response.setContentLength(bytes.length);
	
	//清除输出对象的冲突（因jsp本身有自己的out输出对象，如果去除下面的语句，则response.getOutputStream()得到的输出对象与原来存在的out对象冲突，会报异常）
	out.clear();
	out = pageContext.pushBody();
	
	ServletOutputStream output = response.getOutputStream();
	output.write(bytes,0,bytes.length);
	output.flush();
	output.close();
	
	try{
	    if(conn != null){
	       conn.close(); //关闭数据库连接
	    }
	}catch(Exception e){
	    e.printStackTrace();
	}
%>  
</body>
</html>