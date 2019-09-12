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
<LINK href="<%=request.getContextPath()%>/css/crm.css" rel="stylesheet" type="text/css">
<LINK href="<%=request.getContextPath()%>/css/top.css" rel="stylesheet" type="text/css">
<script src="<%=request.getContextPath()%>/script/AC_RunActiveContent.js" type="text/javascript"></script>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="Refresh" content="60000">
<title>online</title>
<style type="text/css">
<!--
.STYLE1 {
	color: #FFCC00;
	font-weight: bold;
}

-->
</style>
</head> 
<%
String root = request.getContextPath();
//out.println("当前在线人数： "+SessionListener.getCount());
//人员对象
UserProfile up = new UserProfile(request,MyTools.getSessionUserCode(request));


Vector v=new Vector(); 
String sAuth ="";
//int i=0;
AuthObject auth;

//获取人员所属角色
v=up.getUserAuth() ;

TraceLog.Trace("角色长度======" + v.size());

try{
	if (v.size()>0){
	   for(int i=0;i<v.size();i++){
	   	//角色对象
	      auth=new AuthObject(MyTools.StrFiltr(v.get(i)),request);
	      if (!"".equals(auth.getAuthDesc())) {
	      	sAuth=sAuth +  MyTools.StrFiltr(  auth.getAuthDesc()) + ",";
	      }
	      auth=null;
	   }
	   TraceLog.Trace("所属角色长度=========" + sAuth.length());
	   if (!"".equals(sAuth)) {
	   		sAuth = sAuth.substring(0,sAuth.length()-1);
	   }
	   TraceLog.Trace("所属角色=========" + sAuth);
	}
}catch(Exception e){
	System.err.println(e.toString());
   sAuth="";
}
// 如果无法获取人员信息，则自动跳转到登陆界面

DBSource db = new DBSource(request);
String dep = "";
Vector vec = up.getUserDept();
for(int i=0;i<vec.size();i++){
	dep += db.GetColValue("sysDepartment","CName","where DeptCode='" + MyTools.StrFiltr(vec.get(i)) + "'") + "-";
}
dep = (dep.equals("")?"无":dep.substring(0,dep.length() - 1 ));

sAuth = (sAuth.equals("")?"无":sAuth);

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

function onSJ(){
parent.document.all.main.rows='0,0,35,*,60';
//	function(){  };

	alert("1");
}

</script>

<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <!--td align="center" class="dhbg"><img src="<%=request.getContextPath()%>/images/11.gif" width="121" height="35" /></td-->
    	<td align="center" class="dhbg" >
    		当前用户：<%=up.getUserName()%> 
        	部门：<%=dep%> 
        	角色：<%=sAuth%>
			<span class="STYLE2">E-Mail：<%=up.getUserEmail()%></span> 
			<a href="#" onClick="reSet()">[注销]</a>
			<span style="fontsize:10"><a href="#" onClick="top.right.location='<%=request.getContextPath()%>/form/manage/WorkRemind/WorkRemind.jsp?bzmode=UnRead'"><img border="0" src="<%=request.getContextPath()%>/images/remindP2.gif" width=75px height=20px /><font size=3>[<%=new WorkRemind(request).getRemindCount(up.getUserCode()) %>]</font></a></span>
		</td>
      </tr>
</table>
    
</body>
</html>
