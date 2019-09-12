<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'aaa.jsp' starting page</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> 
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
   		<form action="UploadToolbar" id="form3" name="form3" method="POST" enctype="multipart/form-data">
			<center>
				<table class="tablestyle" style="width:100%" cellpadding="4">
					<tr>
						<td>模版</td>
						<td>
							<input type='file' id='file1' name='file1' style="width: 280px;"/>
							<br/>
							<input type="text" id="fileurl" name="fileurl"></input>
							<input type="submit" value="提交"><br>
						</td>
					</tr>
				</table>
			</center>
			
		</form>
  </body>
</html>
