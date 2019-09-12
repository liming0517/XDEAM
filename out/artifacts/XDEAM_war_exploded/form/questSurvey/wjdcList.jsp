<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="java.util.Vector"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.src.develop.store.user.*"%>
<%
	/**
		创建人：yeq
		Create date: 2015.12.02
		功能说明：问卷调查列表
		页面类型:
		修订信息(有多次时,可有多个)
		修订日期:
		原因:
		修订人:
		修订时间:
	**/
 %>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
  <head>
	<title>首页</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/icon.css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/publicScript.js"></script>
  </head>
  <style>
  	@font-face {
		font-family:'myfont';
		src:url('<%=request.getContextPath()%>/css/font/AdobeKaitiStd-Regular.eot'); /* IE9 Compat Modes */        
		src:url('<%=request.getContextPath()%>/css/font/AdobeKaitiStd-Regular.eot?#iefix') format('embedded-opentype'), /* IE6-IE8 */
			url('<%=request.getContextPath()%>/css/font/AdobeKaitiStd-Regular.woff') format('woff'), /* Modern Browsers */
			url('<%=request.getContextPath()%>/css/font/AdobeKaitiStd-Regular.ttf')  format('truetype'), /* Safari, Android, iOS */
			url('<%=request.getContextPath()%>/css/font/AdobeKaitiStd-Regular.svg#AdobeKaitiStd-Regular') format('svg'); /* Legacy iOS */
		font-weight:normal;
		font-style:normal; 
	} 
  
	body{
		margin:0;
		padding:0;
		background-color:#FDAD78;
		text-align:center;
	}
	
	.fontColor{
		color:#FDAD78;
	}
	
	.top{
		width:100%;
		position:absolute;
		top:0;
	}
	.topButton{
		float:right;
		margin-right:10px;
	}
	
	#userInfo{
		margin-top:1px;	
	}
	
	#titleBackground{
		width:100%;
		height:226px;
		background:url("<%=request.getContextPath()%>/images/wjdc/up_back.png");
	}
	
	.wjdcTable{
		width:802px;
		height:128px;
		margin-top:20px;
		margin-left:auto;
		margin-right:auto; 
		cursor:pointer;
		text-align:left;
		border:2px solid #FDAD78;
	}
	
	.wjdbBackgourd_0{
		background:url("<%=request.getContextPath()%>/images/wjdc/gpen_back.png");
	}
	.wjdbBackgourd_1{
		background:url("<%=request.getContextPath()%>/images/wjdc/rpan_back.png");
	}
	.wjdbBackgourd_2{
		background:url("<%=request.getContextPath()%>/images/wjdc/bpan_back.png");
	}
	.wjdcContent{
		width:580px;
		text-align:left;
	}
	
	#wjdcTable{
		width:809px;
		position:relative;
		top:40px;
		text-align:center;
		margin-left:auto; 
		margin-right:auto;
	}
	#juantou{
		height:16px;
		background:url("<%=request.getContextPath()%>/images/wjdc/wen-juantou.png")
	}
	#wjdcBackground{
		font-weight:bold;
		font-size:25px;
		height:150px;
		background-image:url("<%=request.getContextPath()%>/images/wjdc/wen-juanbei.png");
	}
	#juanwei{
		height:13px;
		background:url("<%=request.getContextPath()%>/images/wjdc/wen-juanwei.png")
	}
  </style>
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
<body>
	<div class="top">
		<div class="top_bar">
			<div id="logout" class="topButton">
				<!-- <a href="#" class="fontColor" onclick="backHome();" onmouseover="$(this).css('color', 'red');" onmouseout="$(this).css('color', '#FDAD78');">[&nbsp;首页&nbsp;]</a> -->
				<a href="#" class="fontColor" id="enterManager" onclick="enterManager();" onmouseover="$(this).css('color', 'red');" onmouseout="$(this).css('color', '#FDAD78');" style="display:none;">[ 后台管理 ]</a>
				<a href="#" class="fontColor" id="logout" onclick="logout();" onmouseover="$(this).css('color', 'red');" onmouseout="$(this).css('color', '#FDAD78');">[ 注销 ]</a>
			</div>
			<div id="userInfo" class="topButton fontColor">欢迎 <span id="ic_userName"></span>登录本系统</div>
		</div>
	</div>

	<div style="width:100%; height:100%;">
		<table style="width:100%; text-align:center;" cellpadding="0" cellspacing="0">
			<tr id="titleBackground">
				<td>
					<img src="<%=request.getContextPath()%>/images/wjdc/logo.png">
				</td>
			</tr>
			<tr>
				<td>
					<div style="width:800px; margin-top:18px; margin-bottom:-10px; text-align:left; margin-left:auto; margin-right:auto;">
						<select id="ic_xnxq">
						</select>
					</div>
				</td>
			</tr>
			<tr>
				<td id="listContent">
				</td>
			</tr>
			<tr style="height:60px;">
				<td></td>
			</tr>
		</table>
	</div>
	
	<form id="form1" name='form1' method="post">
		<input id="iKeyCode" name="iKeyCode" type="hidden"/>
	</form>
</body>
<script type="text/javascript">
	var systemId = "<%=MyTools.StrFiltr(request.getParameter("id"))%>";
	var auth = '<%=sAuth%>';
	var userCode = '<%=usercode%>';
	var userName = '<%=userName%>';
	var admin = '<%=MyTools.getProp(request, "Base.admin")%>';
	var teaAuth = '<%=MyTools.getProp(request, "Base.wjdcTea")%>';
	var stuAuth = '<%=MyTools.getProp(request, "Base.wjdcStu")%>';
	var iKeyCode = '';
	var win = "";//windonw.open返回值
	var curXnxq = "";

	$(document).ready(function(){
		//判断是否已登录
		if(userCode != ''){
			var tempStr = '';
			if(auth.indexOf(teaAuth) > -1){
				tempStr = ' 老师';
			}else if(auth.indexOf(stuAuth) > -1){
				tempStr = ' 同学';
			}else{
				tempStr = ' ';
			}
			$('#ic_userName').html(userName + tempStr);
		}else{
			$('.top').hide();
		}
		
		if(auth.indexOf(admin) > -1){
			$('#enterManager').show();
		}
		
		initData();
	});
	
	/**页面初始化加载数据**/
	function initData(){
		$.ajax({
			type:"POST",
			url:'<%=request.getContextPath()%>/Svl_wjdc',
			data:'active=initData',
			dataType:"json",
			success:function(data) {
				curXnxq = data[0].curXnxq;
				initCombobox(data[0].xnxqData);
			}
		});
	}
	
	/**初始化下拉框*/
	function initCombobox(xnxqData){
		$('#ic_xnxq').combobox({
			data:xnxqData,
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			width:'180',
			panelHeight:'140', //combobox高度
			onChange:function(data){
				loadWjdcList(); //读取问卷调查列表
			},
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data != ''){
					//初始化combobox时赋值
					$(this).combobox('setValue', curXnxq);
				}
				
				//loadWjdcList(); //读取问卷调查列表
			}
		});
	}
	
	/**读取调查问卷列表*/
	function loadWjdcList(){
		$.ajax({
			type : "POST",
			async:true,
			url : '<%=request.getContextPath()%>/Svl_wjdc',
			data : 'active=loadWjdcList&sAuth=' + auth + '&WW_XNXQBM=' + $('#ic_xnxq').combobox('getValue'),
			dataType:"json",
			success : function(data) {
				fillList(data);
			}
		});
	}
	
	/**填充调查问卷列表*/
	function fillList(listData){
		var contentStr = '';
		var backIndex = 0;
		
		//判断是否有可查看的调查问卷
		if(listData.length > 0){
			for(var i=0; i<listData.length; i++){
				contentStr += '<table id="' + listData[i].code + '" name="' + listData[i].state + '" class="wjdcTable wjdbBackgourd_'+backIndex+'" cellpadding="0" cellspacing="0">' + 
							'<tr><td style="width:110px;">&nbsp;</td>';
				backIndex++;
				
				if(backIndex == 3){
					backIndex = 0;
				}
				contentStr += '<td class="wjdcContent">问卷调查名称:&nbsp;' + listData[i].title + '<br/><br/>调查问卷时间:&nbsp;' + listData[i].date + '</td>';
				contentStr += '<td>';
				
				if(listData[i].state == '0') contentStr += '<img id="' + listData[i].code + '_pic" src="<%=request.getContextPath()%>/images/wjdc/wks.png"/>';
				if(listData[i].state == '1') contentStr += '<img id="' + listData[i].code + '_pic" src="<%=request.getContextPath()%>/images/wjdc/jxz.png"/>';
				if(listData[i].state == '2') contentStr += '<img id="' + listData[i].code + '_pic" src="<%=request.getContextPath()%>/images/wjdc/ywc.png"/>';
				if(listData[i].state == '3') contentStr += '<img id="' + listData[i].code + '_pic" src="<%=request.getContextPath()%>/images/wjdc/ygq.png"/>';
				
				contentStr += '</td></tr></table>';
			}
		}else{
			contentStr += '<table id="wjdcTable" cellpadding="0" cellspacing="0">' +
					'<tr id="juantou"><td></td></tr>' +
					'<tr id="wjdcBackground" class="fontColor"><td>没有可查看的调查问卷</td></tr>' +
					'<tr id="juanwei"><td></td></tr>' +
					'<tr><td style="height:60px;">&nbsp;</td></tr></table>';
		}
		
		$('#listContent').html(contentStr);
		
		$('.wjdcTable').bind('click', function(){
			iKeyCode = $(this).attr('id');
			
			//判断学生打开调查问卷，老师打开调查问卷统计
			if(auth.indexOf(stuAuth) > -1){
				$('#form1').attr('action', '<%=request.getContextPath()%>/form/questSurvey/wjdcInfo.jsp');
				$('#iKeyCode').val(iKeyCode);
				$('#form1').submit();
			}
			if(auth.indexOf(admin)>-1 || auth.indexOf(teaAuth)>-1){
				$('#form1').attr('action', '<%=request.getContextPath()%>/form/questSurvey/wjdcStatistics.jsp');
				$('#iKeyCode').val(iKeyCode);
				$('#form1').submit();
			}
		}).bind('mouseover', function(){
			$(this).css('border-color', '#51DB47');
		}).bind('mouseout', function(){
			$(this).css('border-color', '#FDAD78');
		});
	}
	
	//返回首页
	/*
	function backHome(){
		location.href='<-%=request.getContextPath()%>/form/homePage.jsp';
	}
	*/
	
	//登出
	function logout(){
		//addSkjhInfo();
		
		if(window.opener != undefined){
			window.opener.location = "<%=request.getContextPath()%>/Logout.jsp";
			window.close();
		}else{
			window.location = "<%=request.getContextPath()%>/pjLogout.jsp";
		}
	}
	
	//进入后台管理
	function enterManager(){
		$('#form1').attr('action', '<%=request.getContextPath()%>/form/mainframe.jsp');
		$('#iKeyCode').val('336048791');
		$('#form1').submit();
		/*
		if(!window.win || win.closed){
			win = window.open('<-%=request.getContextPath()%>/form/mainframe.jsp?type=4444', '', 'left=0,top=0,width='+ screen.availWidth +',height='+ screen.availHeight + ',resizable=yes,location=yes,menubar=yes,titlebar=yes,status=yes,scrollbars=no');
		}else{
			alertMsg("您已打开了评教管理页面");
		}
		*/
	}
	
	//添加授课计划数据
	<%-- function addSkjhInfo(){
		$.ajax({
			type : "POST",
			async:true,
			url : '<%=request.getContextPath()%>/Svl_wjdc',
			data : 'active=addSkjh',
			dataType:"json",
			success : function(data) {
			}
		});
	} --%>
</script>
</html>