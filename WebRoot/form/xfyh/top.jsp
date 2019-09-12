<!DOCTYPE html>
<%@ page import="java.util.Vector"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.src.develop.store.user.*"%>
<%-- <%@ page import="com.pantech.base.common.db.DBSource"%> --%>

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<html>
  <head>  
    
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no"/>
    <meta charset="utf-8">
    <title>学分银行头部</title>
    <link rel="shortcut icon" href="<%=request.getContextPath()%>/images/xfyh/MCS.ico" />
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/icon.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css"/>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/bootstrap.css"/>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/jquery-1.7.2.min.js"></script>
    <script src="<%=request.getContextPath()%>/script/bootstrap.min.js"></script>

  </head>
 <%
	/*
		获得角色信息
	*/
	
	UserProfile up = new UserProfile(request,MyTools.getSessionUserCode(request));
	String userName =up.getUserName();
	if(userName==null){
		userName="";
	}
	String root = request.getContextPath();
	//out.println("当前在线人数： "+SessionListener.getCount());
	//人员对象		
	Vector v = new Vector(); 
	String usercode = "";
	usercode = MyTools.getSessionUserCode(request);
	String sAuth = "";
	//int i=0;
	AuthObject auth;
	//获取人员所属角色
	v = up.getUserAuth();
	//up.setUserAuth();
	if(v!=null){
		for(int i=0; i<v.size(); i++){
			if(i == v.size()-1){
				sAuth += MyTools.StrFiltr(v.get(i));
			}else{
				sAuth += MyTools.StrFiltr(v.get(i))+"O";
			}
		}
	}
	
	
	// 如果无法获取人员信息，则自动跳转到登陆界面
	%>
<body id="top" >
    <header >
     <div class="container">
        <div class="row" >
       
        
            <div class="col-lg-2 col-md-2 col-sm-3 col-xs-6 logo">
                <a  href="#"><img class="pic" src="<%=request.getContextPath()%>/images/xfyh/logo3.png" style="height:50px;margin-top:5%"/></a>
            </div>
            <div class="col-lg-8 col-md-8 col-sm-6"></div>
            <div class="col-lg-2 col-md-2 col-sm-3 col-xs-6 login text-center" style="line-height:65px;padding:0;font-size:90%">
                <b>登录人：</b><span id="usercode" ><%=userName%></span>
                <a href="<%=request.getContextPath()%>/XFYHLogin.jsp" target="_parent">[退出]</a><br/>
               
            </div>
        </div>
       </div>
    </header>
    <script type="text/javascript">
// 	var userCode = '< %=usercode%>';
//  	var userName = '< %=userName%>';
//     var dep = '< %=dep%>';
//     $(document).ready(function(){
    	//dep=dep.substring(dep.lastIndexOf("-")+1, dep.length);
// 		$("#usercode").html(userName);
// 		$("#userdep").html("部&nbsp;&nbsp;&nbsp;门：" + dep);
// 	});
    </script>
</body>
</html>
