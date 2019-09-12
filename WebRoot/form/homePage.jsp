<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Vector"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.src.develop.store.user.*"%>
<!-- 
	说明:系统首页
	日期:2016.04.11
	叶强
 -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
  <head>
	<title>上海市现代职业技术学校</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/homePageStyle.css"/>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/icon.css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
	<style type="text/css">
		body{
			/*
			background: url(../image/back.png) repeat-x ;
			background-attachment:fixed;
			background-repeat:no-repeat;
			background-size:cover;
			-moz-background-size:cover;
			-webkit-background-size:cover;
			*/
			margin:0;
			padding:0;
			overflow:hidden;
		}
		
		a:link {
			text-decoration: none;
		}
		a:visited {
			text-decoration: none;
		}
		a:hover {
			text-decoration: none;
		}
		a:active {
			text-decoration: none;
		}
		
		.divOver{
			cursor:pointer;
		}
	</style>
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
	if(v!=null && v.size()>0){
		for(int i=0; i<v.size(); i++){
			sAuth += "@"+MyTools.StrFiltr(v.get(i))+"@O";
		}
		sAuth = sAuth.substring(0, sAuth.length()-1);
	}
	// 如果无法获取人员信息，则自动跳转到登陆界面
%>
<body scroll="auto" onload="MM_preloadImages('<%=request.getContextPath()%>/images/homePageImage/zhxw_1.png','<%=request.getContextPath()%>/images/homePageImage/gsgg_1.png','<%=request.getContextPath()%>/images/homePageImage/zsxx_1.png','image/flfg_1.png','<%=request.getContextPath()%>/images/homePageImage/bszn_1.png')">
	<img src="<%=request.getContextPath()%>/images/homePageImage/back.png" style="width: 100%; height: 100%; position: absolute; top: 0pt; z-index: -99;">
	<div style="float:right; margin-right:10px; color:#ffffff; font-weight:bold;">
		<span id="ic_name"></span>
		<a href="#" style="color:#ffffff;" onclick="logout();" onmouseover="$(this).css('color', 'red');" onmouseout="$(this).css('color', '#ffffff');">[&nbsp;注销&nbsp;]</a>
	</div>
	<div class="zhuyao" align="center" >
		<div class="head">
			<div style="margin-left:0%;">
				<img src="<%=request.getContextPath()%>/images/homePageImage/logo.png"/>
			</div>
		</div>
		<div class="nr">
			<div class="h1">
				<div id="pkgl" class="a divOver" onmouseover="MM_swapImage('Image1','','<%=request.getContextPath()%>/images/homePageImage/zhxw_1.png',1)" onmouseout="MM_swapImgRestore()" onclick="enterSystem('pkgl');">
					<img src="<%=request.getContextPath()%>/images/homePageImage/zhxw.png" id="Image1"/><br/>
					<a>排课管理系统</a><br/>
					<b>Course Scheduling Management System</b>
				</div>
				<div class="b divOver" onmouseover="MM_swapImage('Image2','','<%=request.getContextPath()%>/images/homePageImage/sx_tb_2.png',1)" onmouseout="MM_swapImgRestore()" onclick="enterSystem('jxgl');">
					<img src="<%=request.getContextPath()%>/images/homePageImage/sx_tb_1.png" id="Image2"/><br/>
					<a>教学管理系统</a><br/>
					<b>Educational Management System</b>				
				</div>
				<div id="jcxxgl" class="c divOver" onmouseover="MM_swapImage('Image3','','<%=request.getContextPath()%>/images/homePageImage/gsgg_1.png',1)" onmouseout="MM_swapImgRestore()" onclick="enterSystem('jcxxgl');">
					<img src="<%=request.getContextPath()%>/images/homePageImage/gsgg.png" id="Image3"/><br/>
					<a>基础信息管理</a><br/>
					<b>Information Management</b>				
				</div>
			</div>
			<div class="h3">
				<div class="i divOver" onmouseover="MM_swapImage('Image10','','<%=request.getContextPath()%>/images/homePageImage/xxk_1.png',1)" onmouseout="MM_swapImgRestore()" onclick="enterSystem('xxkgl');">
					<img src="<%=request.getContextPath()%>/images/homePageImage/xxk.png" id="Image10"/><br/>
					<a>选修课系统</a><br/>
					<b>Elective Course System</b>				
				</div>
				<div class="j divOver" onmouseover="MM_swapImage('Image9','','<%=request.getContextPath()%>/images/homePageImage/bszn_1.png',1)" onmouseout="MM_swapImgRestore()" onclick="enterSystem('cjgl');">
				<img src="<%=request.getContextPath()%>/images/homePageImage/bszn.png" id="Image9"/><br/>
				<a>成绩管理系统</a><br/>
				<b>Result Management System</b>				
				</div>
				<div class="f divOver" onmouseover="MM_swapImage('Image11','','<%=request.getContextPath()%>/images/homePageImage/kw_1.png',1)" onmouseout="MM_swapImgRestore()" onclick="enterSystem('kwgl');">
				<img src="<%=request.getContextPath()%>/images/homePageImage/kw.png" id="Image11"/><br/>
				<a>考务管理</a><br/>
				<b>Examination Management</b>				
				</div>
			</div>
			<div class="h3">
				<div id="xtgl" class="x divOver" onmouseover="MM_swapImage('Image12','','<%=request.getContextPath()%>/images/homePageImage/xtgl_1.png',1)" onmouseout="MM_swapImgRestore()" onclick="enterSystem('xtgl');">
					<img src="<%=request.getContextPath()%>/images/homePageImage/xtgl.png" id="Image12"/><br/>
					<a>系统管理</a><br/>
					<b>Examination Management</b>				
				</div>
				<div class="y divOver" onmouseover="MM_swapImage('Image13','','<%=request.getContextPath()%>/images/homePageImage/flfg_1.png',1)" onmouseout="MM_swapImgRestore()" onclick="enterSystem('pjgl');">
					<img src="<%=request.getContextPath()%>/images/homePageImage/flfg.png" id="Image13"/><br/>
					<a>评教管理系统</a><br/>
					<b>Teaching Evaluation Management System</b>				
				</div>
				<div class="z divOver" onmouseover="MM_swapImage('Image14','','<%=request.getContextPath()%>/images/homePageImage/wk_tb_2.png',1)" onmouseout="MM_swapImgRestore()" onclick="enterSystem('wkgl');">
					<img src="<%=request.getContextPath()%>/images/homePageImage/wk_tb_1.png" id="Image14"/><br/>
					<a>外考管理系统</a><br/>
					<b>Outside The Exam Management System</b>				
				</div>
			</div>
		</div>
	</div>
	
	<!-- 成绩管理系统入口 -->
	<div id="scoreManageEnter" style="background:url('<%=request.getContextPath()%>/images/homePageImage/shi_bg.png'); position:absolute; top:0; left:0; z-index:2; display:none;">
		<table id="scoreManageTable" style="width:100%; heigth:100%; text-align:center;">
			<tr>
				<td style="height:25px;">
					<img src="<%=request.getContextPath()%>/images/homePageImage/cancel.png" style="float:right; width:16px; height:16px; margin-top:10px; margin-right:10px; cursor:pointer;" onclick="$('#scoreManageEnter').hide();">
				</td>
			</tr>
			<tr>
				<td style="height:490px;">
					<input id="scoreRegister" type="button" title="成绩登记"
						style="border:none; width:122px; height:117px; background:url('<%=request.getContextPath()%>/images/homePageImage/cjdj1.png'); cursor:pointer;"
						onmouseover="changeButtonImg(this.id, 'cjdj2');" 
						onmouseout="changeButtonImg(this.id, 'cjdj1');"
						onmousedown="changeButtonImg(this.id, 'cjdj1');"
						onmouseup="changeButtonImg(this.id, 'cjdj2');"
						onClick="enterScoreManage(this.id);"/>
					<input id="scoreManage" type="button" title="成绩管理"
						style="border:none; width:122px; height:117px; margin-left:100px; background:url('<%=request.getContextPath()%>/images/homePageImage/showQues1.png'); cursor:pointer;"
						onmouseover="changeButtonImg(this.id, 'showQues2');" 
						onmouseout="changeButtonImg(this.id, 'showQues1');"
						onmousedown="changeButtonImg(this.id, 'showQues1');"
						onmouseup="changeButtonImg(this.id, 'showQues2');"
						onClick="enterScoreManage(this.id);"/>
				</td>
			</tr>
		</table>
	</div>
	
	<form id="form1" method="post" action="<%=request.getContextPath()%>/form/mainframe.jsp">
		<input type="hidden" id="iKeyCode" name="iKeyCode"/>
	</form>
</body>
<script language =javascript >
	var sAuth = '<%=sAuth%>';
	var admin = '<%=MyTools.getProp(request, "Base.admin")%>';//管理员
	var jxzgxz = '<%=MyTools.getProp(request, "Base.jxzgxz")%>';
	var qxjdzr = '<%=MyTools.getProp(request, "Base.qxjdzr")%>';
	var xbjdzr = '<%=MyTools.getProp(request, "Base.xbjdzr")%>';
	var qxjwgl = '<%=MyTools.getProp(request, "Base.qxjwgl")%>';
	var xbjwgl = '<%=MyTools.getProp(request, "Base.xbjwgl")%>';
	var bzr = '<%=MyTools.getProp(request, "Base.bzr")%>';
	var win = "";//windonw.open返回值
	var userName = '<%=userName%>';
	var systemId = '';
	
	$(document).ready(function(){
		$('#ic_name').html('欢迎&nbsp;' + userName);
	});
	
	function enterSystem(id){
		systemId = '336';
		if(id == 'xtgl') systemId+='01';
		else if(id == 'jxgl') systemId+='02';
		else if(id == 'pkgl') systemId+='03';
		else if(id == 'pjgl') systemId+='04';
		else if(id == 'kwgl') systemId+='05';
		else if(id == 'cjgl') systemId+='06';
		else if(id == 'xxkgl') systemId+='07';
		else if(id == 'wkgl') systemId+='08';
		else if(id == 'jcxxgl') systemId+='09';
		systemId += '8791';
			
		if(id != ''){
			if(id=='cjgl'){
				$('#scoreManageEnter').css('top', $('#pkgl').offset().top);
				$('#scoreManageEnter').css('left', $('#pkgl').offset().left);
				$('#scoreManageEnter').css('width', $('#jcxxgl').offset().left+$('#jcxxgl').width()-$('#pkgl').offset().left);
				$('#scoreManageEnter').css('height', $('#xtgl').offset().top+$('#xtgl').height()-$('#pkgl').offset().top);
				$('#scoreManageTable').css('width', '100%');
				$('#scoreManageTable').css('height', '100%');
				
				$('#scoreManageEnter').show();
			}else if(id == 'pjgl'){
				if(!window.win || win.closed){
					win = window.open('<%=request.getContextPath()%>/form/questSurvey/wjdcList.jsp', '', 'left=0,top=0,width='+ screen.availWidth +',height='+ screen.availHeight + ',resizable=yes,location=yes,menubar=yes,titlebar=yes,status=yes,scrollbars=yes');
				}else{
					alertMsg("您已打开了评教管理系统");
				}
			}else{
				$('#iKeyCode').val(encodeURI(systemId));
				$('#form1').submit();
			}
		}else{
			alert("网站建设中...");
		}
	}

	function enterScoreManage(type){
		delRepeatCourse();
	
		if(type == 'scoreRegister'){
			if(!window.win || win.closed){
				win = window.open('<%=request.getContextPath()%>/form/registerScoreManage/teaXqcjdjList.jsp', '', 'left=0,top=0,width='+ screen.availWidth +',height='+ screen.availHeight + ',resizable=yes,location=yes,menubar=yes,titlebar=yes,status=yes,scrollbars=no');
			}else{
				alertMsg("您已打开了成绩管理系统");
			}
		}else if(type == 'scoreManage'){
			//判断权限
			/*
			if(sAuth.indexOf(admin)<0 && sAuth.indexOf(jxzgxz)<0 && sAuth.indexOf(qxjdzr)<0 && sAuth.indexOf(xbjdzr)<0 && sAuth.indexOf(qxjwgl)<0 && sAuth.indexOf(xbjwgl)<0 && sAuth.indexOf(bzr)<0){
				alertMsg('当前教师权限无法访问');
				return;
			}
			*/
			$('#iKeyCode').val(encodeURI(systemId));
			$('#form1').submit();
		}
	}

	function delRepeatCourse(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Kmsz',
			data : 'active=delRepeatCourse',
			dataType:"json",
			success : function(data) {
				
			}
		});
	}

	function MM_preloadImages(){ //v3.0
		var d = document;
		
		if(d.images){
			if(!d.MM_p)
				d.MM_p=new Array();
				
			var i,j=d.MM_p.length,a=MM_preloadImages.arguments;
		
			for(i=0; i<a.length; i++){
				if(a[i].indexOf("#")!=0){
					d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];
				}
			}
		}
	}
	
	//替换按钮图片
	function changeButtonImg(id, imgName){
		$('#' + id).css('background', 'url("<%=request.getContextPath()%>/images/homePageImage/' + imgName + '.png")');
	}
	
	function MM_swapImgRestore() { //v3.0
	  var i,x,a=document.MM_sr;
	  
	  for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
	}
	
	function MM_findObj(n, d) { //v4.01
	  var p,i,x;
	  
	  if(!d) d=document;
	  if((p=n.indexOf("?"))>0&&parent.frames.length) {
	    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);
		}
	  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
	  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
	  if(!x && d.getElementById) x=d.getElementById(n); return x;
	}
	
	function MM_swapImage() { //v3.0
	  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
	   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
	}
	
	//登出
	function logout(){
		window.location = "<%=request.getContextPath()%>/Logout.jsp";
	}
</script>
</html>