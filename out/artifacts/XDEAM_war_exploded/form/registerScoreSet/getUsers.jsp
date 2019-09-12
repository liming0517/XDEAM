<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<html>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
	<head>
		<script>
	    window.onload = function(){
	        var url = window.location.href;
	    	window.parent.parent.getUsers(url.substring(url.indexOf('users')+6));
	    }
	    </script>
	</head>
	<body>
	</body>
</html>