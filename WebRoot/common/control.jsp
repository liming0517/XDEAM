<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<%@ page 
language="java"
contentType="text/html; charset=GBK"
pageEncoding="GBK"
%>
<META http-equiv="Content-Type" content="text/html; charset=GBK">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">

<style media="print">.Noprint { DISPLAY: none }</style>

<style>
	.navPoint {
		FONT-SIZE: 13pt; CURSOR: hand; COLOR: black; FONT-FAMILY: "Webdings"
	}
</style>


<TITLE>control.jsp</TITLE>
<SCRIPT>

function switchSysBar(){
//alert('---');
if (switchPoint.innerText==3){
	switchPoint.innerText=4
    parent.document.all.page1.cols='0,10,*'
  //parent.document.all.left.width=0;
  // alert(parent.document.all.page1.width);
}else{
	switchPoint.innerText=3
   parent.document.all.page1.cols='160,9,*'
}}
</SCRIPT>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link href="<%=request.getContextPath()%>/css/frameSample/css/wiserunoa.css" rel="stylesheet" type="text/css">
<style type="text/css">
<!--
.STYLE1 {color: #333333}
-->
</style>
</head>
<body bgcolor='#E8E8E8' leftmargin="0" style="cursor:hand" onclick="switchSysBar()">
<br/><br/><br/><br/>
<br/><br/><br/><br/>
<br/><br/><br/><br/>
	<SPAN class="navPoint STYLE1" id=switchPoint title=关闭/打开左栏>3</SPAN>
<br/>
<br/><br/><br/>
</body>
</html>
