<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="com.pantech.src.develop.Logs.*" %>
<%@ page import="com.pantech.base.common.tools.MyTools" %>
<%@ taglib uri="/WEB-INF/taglib/TextToPicture.tld" prefix="TToP" %>
<%
System.out.println("hear0");
String root = request.getContextPath();
//out.println("当前在线人数： "+SessionListener.getCount());
%>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=GBK">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="<%=request.getContextPath()%>/css/crm.css" rel="stylesheet"
	type="text/css">
<LINK href="<%=request.getContextPath()%>/css/top.css" rel="stylesheet"
	type="text/css">
<LINK type="text/css" rel="stylesheet" href="<%=root%>/skins/officexp/officexp.css">
<SCRIPT type="text/javascript" src="<%=root%>/script/poslib.js"></SCRIPT>
<SCRIPT type="text/javascript" src="<%=root%>/script/scrollbutton.js"></SCRIPT>
<SCRIPT type="text/javascript" src="<%=root%>/script/menu4.js"></SCRIPT>
<SCRIPT type="text/javascript" src="<%=root%>/script/browser.js"></SCRIPT>
<!-- SCRIPT type="text/javascript" src="<%=root%>/script/mymenubar.js"></SCRIPT -->

<%
System.out.println("hear1");
out.println(MyTools.WriteMenuJS(request));
System.out.println("hear2");
%>

<SCRIPT>

function switchSysBar(){
//alert('---');
	if (switchPoint.innerText==5){
		//alert(document.wincontrol.value);
		switchPoint.innerText=6
		parent.main.rows='30,*,46'
		//alert(parent.top.document.body.scrollTop);
		window.scrollTo(0,document.body.scrollHeight)
		//parent.top.document.scrollTo(0,document.body.scrollHeight);
//		parent.top.document.body.scrollTop = 50;
	}else{
		switchPoint.innerText=5
		parent.main.rows='71,*,46'
	}
}
</SCRIPT>


</HEAD>
<body  leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">

<table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#CDCD8F" align="center" >
   <tr>
    <td align="left" class="topbg">
    	<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7,0,19,0" width="1003" height="60">
      <param name="movie" value="<%=root%>/images/top.swf" />
      <param name="quality" value="high" />
      <embed src="<%=root%>/images/top.swf" quality="high" pluginspage="http://www.macromedia.com/go/getflashplayer" type="application/x-shockwave-flash" width="1003" height="60"></embed>
    </object></td>
  </tr>
</table>
</body>
