<!--
Author:Zedee
显示页面的在线帮助信息
-->
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<%@ page import="com.pantech.src.develop.helpinfo.HelpInfoBean" %>
<%
	String sUrl = request.getParameter("url");
	String sCont = "";
	String sTitle = "";
	if(sUrl!=null){
		sUrl = sUrl.substring(sUrl.indexOf(request.getContextPath())+request.getContextPath().length());
		//System.out.println("path=="+request.getContextPath());
		//System.out.println("url=="+sUrl);
		HelpInfoBean bHelp = new HelpInfoBean(sUrl,request);
		sCont = bHelp.getContent();
		sTitle = bHelp.getTitle();
	}
	if(sCont.equals("")){
		sCont = "该页没有相关的帮助信息!!";
	}
	if(!sTitle.equals("")){
		sTitle = "-" + sTitle;
	}
%>
<HEAD>
<%@ page 
language="java"
contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"
%>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<TITLE>在线帮助<%=sTitle%></TITLE>
</HEAD>


<script>
//使用WINDOWS自带动画
var chName;
var agent;
var Merlin;
chName = "Merlin"; 
agent = new ActiveXObject("Agent.Control.2"); 
agent.Connected = true; agent.Characters.Load(chName,chName+".acs"); 
Merlin = agent.Characters(chName); Merlin.MoveTo(800,300); Merlin.Show(); 

//Merlin.Play("Announce");
//Merlin.Speak("<//=sTitle%>模块的在线帮助"); 
//Merlin.Play("Explain"); 
//Merlin.Speak("<//=sCont%>"); 
//Merlin.Play("Idle1_1"); 

//Merlin.MoveTo(600,300); 
//Merlin.Play("Surprised"); 
//Merlin.Speak("哦对了，大家最近小心非典。多多注意哦"); 
//Merlin.Play("Idle1_2");
<%
	if(sTitle.equals("")){
%>
		Merlin.Play('Decline');Merlin.Speak('抱歉，本页暂无帮助信息'); 
<%	}else{%>
		Merlin.Play("Announce"); 
		Merlin.Speak("欢迎您使用在线帮助系统"); 
		Merlin.MoveTo(300,400);
		Merlin.Play('Read');Merlin.Speak('这里是<%=sTitle.equals("")?"":sTitle.substring(1)%>模块的在线帮助'); 
		Merlin.Play("Idle1_1");
		Merlin.MoveTo(800,400);
<%	}%>

	Merlin.Play("DoMagic2");
	Merlin.Hide();
	
//Merlin.Hide(); 

</script> 
<body bgcolor="#ffffcc">
<br>
<center>
<table width="95%">
	<tr>
		<td>
			<B><font size="2"><%=sCont%></font><B>
		</td>
	</tr>
</table>
</center>
</body> 


<!--body STYLE="background-color:transparent" background="<%=request.getContextPath()%>/images/HelpBG.GIF"-->

<!--/BODY-->
</HTML>
