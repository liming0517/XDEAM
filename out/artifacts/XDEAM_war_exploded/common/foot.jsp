<%@ page 
language="java"
contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"
%>
<%@ page import="com.pantech.src.develop.Logs.*" %>
<%@ page import="com.pantech.base.common.tools.*"%>
<%@ page import="com.pantech.src.develop.store.user.*" %>
<%@ page import="com.pantech.src.develop.manage.workremind.WorkRemind" %>
<%@ page import="java.util.Vector"%>
<%@ page import="com.pantech.base.common.db.DBSource" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<%@ taglib uri="/WEB-INF/taglib/TextToPicture.tld" prefix="TToP" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- meta http-equiv="Refresh" content="60" -->
<title></title>
<link href="<%=request.getContextPath()%>/css/crm.css" rel="stylesheet" type="text/css" />
<script src="<%=request.getContextPath()%>/script/AC_RunActiveContent.js" type="text/javascript"></script>
<style type="text/css">
<!--
//modify by hades 20120309 底部显示 欢迎 XXXX(部门：xx 角色XX) 使用本系统 
-->
</style>
</head>
<%
String root = request.getContextPath();
//out.println("ǰ "+SessionListener.getCount());
//Ա
UserProfile up = new UserProfile(request,MyTools.getSessionUserCode(request));


Vector v=new Vector(); 
String sAuth ="";
//int i=0;
AuthObject auth;

//ȡԱɫ
v=up.getUserAuth() ;

TraceLog.Trace("ɫ======" + v.size());

try{
	if (v.size()>0){
	   for(int i=0;i<v.size();i++){
	   	//ɫ
	      auth=new AuthObject(MyTools.StrFiltr(v.get(i)),request);
	      if (!"".equals(auth.getAuthDesc())) {
	      	sAuth=sAuth +  MyTools.StrFiltr(  auth.getAuthDesc()) + ",";
	      }
	      auth=null;
	   }
	   TraceLog.Trace("ɫ=========" + sAuth.length());
	   if (!"".equals(sAuth)) {
	   		sAuth = sAuth.substring(0,sAuth.length()-1);
	   }
	   TraceLog.Trace("ɫ=========" + sAuth);
	}
}catch(Exception e){
	System.err.println(e.toString());
   sAuth="";
}
// ޷ȡԱϢԶת½

DBSource db = new DBSource(request);
String dep = "";
Vector vec = up.getUserDept();
for(int i=0;i<vec.size();i++){
	dep += db.GetColValue("sysDepartment","CName","where DeptCode='" + MyTools.StrFiltr(vec.get(i)) + "'") + "-";
}
dep = (dep.equals("")?"":dep.substring(0,dep.length() - 1 ));

sAuth = (sAuth.equals("")?"":sAuth);

%>

<script language="javascript">
function dosj(){
	var hei = window.parent.document.all.topFrame.height;//parent.document.all.mainFrame.height
//	alert ( parent.document.all.mainFrame.name ) ;
	window.parent.document.all.mainFrame.rows = '40,*,80';
	//alert ( parent.document.all.topFrame.height ) ;
	//for(i=0;i<hei;hei-=10){
	//	SetTimeout(function(){ window.parent.document.all.mainFrame.rows="0,*,80"; },100);
		//window.parent.document.all.topFrame.height=hei;
	//}
	
}

function reSet(){
	//window.parent.document.all.
	top.location = '<%=root%>/Logout.jsp' ;
}
//页脚首页按钮超链接地址
function reSet1(){
	top.location = '<%=root%>/form/mainframe.jsp';
}

function onSJ(){
parent.document.all.main.rows='0,0,35,*,60';
//	function(){  };

	alert("1");
}

</script>

<body background="<%=request.getContextPath()%>/images/footbg.jpg">
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="footbg">
	
  <tr>
<%
java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy年MM月dd日"); 
java.util.Date d = new java.util.Date();
%>
    <td align="center" background="<%=request.getContextPath()%>/images/footbg.jpg">
    		<a onclick="reSet1();" href="javascript:void(0);" id="1001"  class="easyui-menubutton"  menu="#sub1001" style=""   iconCls=""> <font color='red'>[首页]</font></a>
			&nbsp;&nbsp;欢迎：<%=up.getUserName()%>
			(部门：<%=dep%>)
			使用本系统！   
        	<!--角色：<%=sAuth%>)  -->
        	当前日期：<%=sdf.format(d)%>
			<span class="STYLE2"></span> <!-- 20120309 hades 不再显示邮件地址 E-Mail< %=up.getUserEmail()%> --> 
			<a href="#" onClick="reSet()"><font color='red'>[退出]</font></a>
			<!--20120309 modify byhades >span><a href="#" onClick="top.right.location='< %=request.getContextPath()%>/form/manage/WorkRemind/WorkRemind.jsp?bzmode=UnRead'"><img border="0" src="< %=request.getContextPath()%>/images/remindP2.gif" />[< % =new WorkRemind(request).getRemindCount(up.getUserCode()) %>]</a></span> -->
			<br/>Copyright &copy; 2007-<%=d.getYear()+1900 %> by 长润信息 All rights reserved.
    </td>
  </tr>
</table>
</body>
</html>
