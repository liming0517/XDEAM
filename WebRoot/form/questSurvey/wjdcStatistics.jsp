<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="java.util.Vector"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.src.develop.store.user.*"%>
<%
	/**
		创建人：yeq
		Create date: 2015.11.20
		功能说明：问卷调查统计
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
	<title>问卷调查统计</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/icon.css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/publicScript.js"></script>
  </head>
  <style>
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
		height:160px;
		background-color:#FBF3CF;
	}
	
	#titlePic{
		width:100%;
		position:relative;
		top:150px;
	}
	
	#titleDiv{
		position:absolute;
		top:10px;
		left:50%;
		margin-left:-225px;
		width:450px;
	}
	
	#wjdcPic{
		width:180px;
		float:left;
	}
	
	#biPic{
		width:120px;
		position:absolute;
		top:65px;
		left:360px;
		z-index:999;
	}
	
	#titleContent{
		width:400px;
		height:96px;
		position:absolute;
		top:190px;
		left:50%;
		z-index:998;
		margin-left:-200px;
	}
	
	#dcbtPic{
		width:100%;
		position:absolute;
		top:0px;
		left:50%;
		margin-left:-200px;
		z-index:-999;
	}
	
	#wjTitle{
		width:100%;
		font-size:20px;
		margin-top:40px;
	}
	
	#wjdcTable{
		width:809px;
		position:relative;
		top:142px;
		text-align:center;
		margin-left:auto; 
		margin-right:auto;
	}
	
	#juantou{
		width:100%;
		height:16px;
		background:url("<%=request.getContextPath()%>/images/wjdc/wen-juantou.png")
	}
	
	#wjdcBackground{
		width:100%;
		background-image:url("<%=request.getContextPath()%>/images/wjdc/wen-juanbei.png");
	}
	
	#wjdcContent{
		width:750px;
		text-align:left;
		margin-left:auto; 
		margin-right:auto;
	}
	
	.pageButton{
		float:right;
		height:52px;
		margin-right:10px;
		cursor:pointer;
		text-align:center;
		display:None;
	}
	
	#buttonFont{
		width:150px; 
		background:url('<%=request.getContextPath()%>/images/wjdc/wen-anniucenter.png');
		font-size:20px;
		color:white;
	}
	
	#juanwei{
		width:100%;
		height:13px;
		background:url("<%=request.getContextPath()%>/images/wjdc/wen-juanwei.png")
	}
	
	.contentTable{
		margin-top:15px;
		margin-left:22px;
		border-top:1px solid black;
		border-right:1px solid black;
		text-align:center;
	}
	
	.tdBorder{
		border-left:1px solid black;
		border-bottom:1px solid black;
	}
	
	.tdBackColor{
		background-color:#CCFFFF;
	}
	
	.tipsDiv{
		margin-top:20px;
		margin-left:22px;
		margin-bottom:5px;
		color:red;
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
			<div id="logout" class="topButton"><a href="#" class="fontColor" onclick="logout();" onmouseover="$(this).css('color', 'red');" onmouseout="$(this).css('color', '#FDAD78');">[ 注销 ]</a></div>
			<div id="home" class="topButton"><a href="#" class="fontColor" onclick="goHomePage();" onmouseover="$(this).css('color', 'red');" onmouseout="$(this).css('color', '#FDAD78');">[ 首页 ]</a></div>
			<div id="userInfo" class="topButton fontColor">欢迎 <span id="ic_userName"></span>登录本系统</div>
		</div>
	</div>
	
	<div id="titleBackground">
		<img id="titlePic" src="<%=request.getContextPath()%>/images/wjdc/wen-xian.png">
	</div>
	<div id="titleDiv">
		<img id="wjdcPic" src="<%=request.getContextPath()%>/images/wjdc/wen-ming.png">
		<img id="biPic" src="<%=request.getContextPath()%>/images/wjdc/wen-bi.png">
		<div id="titleContent">
			<img id="dcbtPic" src="<%=request.getContextPath()%>/images/wjdc/wen-huangkuang.png">
			<div id="wjTitle">
			</div>
		</div>
	</div>
	<table id="wjdcTable" cellpadding="0" cellspacing="0">
		<tr id="juantou">
			<td></td>
		</tr>
		<tr id="wjdcBackground">
			<td>
				<div id="wjdcContent">
					<div style="height:400px; text-align:center;">
						<img src="<%=request.getContextPath()%>/images/loading_3.gif" style="margin-top:150px;"/>
					</div>
				</div>
				<br/></br>
				<table id="exportButton" class="pageButton" cellpadding="0" cellspacing="0" onclick="exportExcel();">
					<tr>
						<td style="width:6px; background:url('<%=request.getContextPath()%>/images/wjdc/wen-anniuleft.png');"></td>
						<td id="buttonFont">导&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;出</td>
						<td style="width:7px; background:url('<%=request.getContextPath()%>/images/wjdc/wen-anniuright.png');"></td>
					</tr>
				</table>
				<!-- 遮罩层 -->
				<div id="mask" style="background:url(0); width:100%; height:100%; position:absolute; top:0; display:none;"></div>
			</td>
		</tr>
		<tr id="juanwei"><td></td></tr>
		<tr><td style="height:60px;">&nbsp;</td></tr>
	</table>
	
	<!-- 导出excel -->
	<iframe id="exportIframe" src="" style="width:0; height:0;"></iframe>
</body>
<script src="<%=request.getContextPath()%>/script/ECharts/echarts-all.js"></script>
<script type="text/javascript">
	var auth = '<%=sAuth%>';
	var userCode = '<%=usercode%>';
	var userName = '<%=userName%>';
	var teaAuth = '<%=MyTools.getProp(request, "Base.wjdcTea")%>';
	var stuAuth = '<%=MyTools.getProp(request, "Base.wjdcStu")%>';
	var iKeyCode = '<%=request.getParameter("iKeyCode")%>';
	var imageData = new Array();
	
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
	
		fillContent();
	});
	
	function fillContent(){
		$.ajax({
			type : "POST",
			async:true,
			url : '<%=request.getContextPath()%>/Svl_wjdc',
			data : 'active=loadStatistics&WW_WJBH=' + iKeyCode + "&sAuth=" + auth,
			dataType:"json",
			success : function(data) {
				$('#wjTitle').html(data.title + '统计信息');
				var allData = data.data;
				var tableData = allData.tableData;
				var resultContent = '';
				
				//有图表
				if(data.wjType == '01'){
					var chartData = allData.chartData;
					var optionsData = '';
					
					for(var i=0; i<tableData.length; i++){
						resultContent += '<span style="width:18px; display:inline-block; margin-top:40px; font-weight:bold; color:#FBF3CF; background-color:#FD7A7A; text-align:center;">'+(i+1)+'</span>&nbsp;';
						resultContent += tableData[i].title+'(总投票人数：'+tableData[i].totalStuNum+'人)';
						resultContent += '<table class="contentTable" cellpadding="0" cellspacing="0">' +
							'<tr><td class="tdBorder tdBackColor" style="width:200px;">选项</td><td class="tdBorder tdBackColor" style="width:100px;">投票数</td><td class="tdBorder tdBackColor" style="width:100px;">百分比</td></tr>';
						optionsData = tableData[i].options;
						
						for(var j=0; j<optionsData.length; j++){
							resultContent += '<tr><td class="tdBorder tdBackColor" style="text-align:left; padding-left:5px;">' + optionsData[j].option + '</td>' +
								'<td class="tdBorder">' + optionsData[j].stuNum + '</td>' +
								'<td class="tdBorder">' + optionsData[j].percent + '</td></tr>';
						}
						resultContent += '</table><div class="tipsDiv">提示：单选题显示饼图；多选题显示柱状图；</div>';
						resultContent += '<div id="chart_' + i + '" style="height:400px;"></div>';
						if(i < tableData.length-1){
							resultContent += '<img src="<%=request.getContextPath()%>/images/wjdc/wen-fengexian.png">';
						}
					}
					$('#wjdcContent').html(resultContent);
					
					//生成图表
					for(var i=0; i<chartData.length; i++){
						showChart('chart_'+i, chartData[i]);
					}
				}
				
				if(data.wjType == '02'){
					resultContent = '<table class="contentTable" cellpadding="0" cellspacing="0">' + 
								'<tr style="height:40px;"><td class="tdBorder" style="width:40px;">序号</td>' + 
								'<td class="tdBorder" style="width:200px;">课程</td>' + 
								'<td class="tdBorder" style="width:100px;">教师</td>' +
								'<td class="tdBorder" style="width:100px;">系别</td>' +
								'<td class="tdBorder" style="width:200px;">班级</td>' +
								'<td class="tdBorder">平均分</td></tr>';
					for(var i=0; i<tableData.length; i++){
						resultContent += '<tr style="height:40px;"><td class="tdBorder">' + tableData[i].id + '</td>' + 
								'<td class="tdBorder">' + tableData[i].courseName + '</td>' + 
								'<td class="tdBorder">' + tableData[i].teaName + '</td>' +
								'<td class="tdBorder">' + tableData[i].majorName + '</td>' +
								'<td class="tdBorder">' + tableData[i].className + '</td>' +
								'<td class="tdBorder">' + tableData[i].score + '</td></tr>';
					}
					
					if(tableData.length < 8){
						for(var i=0; i<8-tableData.length; i++){
							resultContent += '<tr style="height:40px;"><td class="tdBorder" style="width:60px;">&nbsp;</td>' + 
								'<td class="tdBorder">&nbsp;</td>' + 
								'<td class="tdBorder">&nbsp;</td>' +
								'<td class="tdBorder">&nbsp;</td>' +
								'<td class="tdBorder">&nbsp;</td>' +
								'<td class="tdBorder">&nbsp;</td></tr>';
						}
					}
					
					resultContent += '</table>';
					$('#wjdcContent').html(resultContent);
				}
				
				if(data.wjType == '03'){
					resultContent = '<table class="contentTable" cellpadding="0" cellspacing="0">' + 
								'<tr style="height:40px;"><td class="tdBorder" style="width:40px;">序号</td>' + 
								'<td class="tdBorder" style="width:150px;">辅导员姓名</td>' + 
								'<td class="tdBorder" style="width:415px;">班级名称</td>' +
								'<td class="tdBorder" style="width:80px;">平均分</td></tr>';
					for(var i=0; i<tableData.length; i++){
						resultContent += '<tr style="height:40px;"><td class="tdBorder">' + tableData[i].id + '</td>' + 
								'<td class="tdBorder">' + tableData[i].name + '</td>' + 
								'<td class="tdBorder">' + tableData[i].className + '</td>' +
								'<td class="tdBorder">' + tableData[i].score + '</td></tr>';
					}
					
					if(tableData.length < 5){
						for(var i=0; i<5-tableData.length; i++){
							resultContent += '<tr style="height:40px;"><td class="tdBorder" style="width:60px;">&nbsp;</td>' + 
								'<td class="tdBorder">&nbsp;</td>' + 
								'<td class="tdBorder">&nbsp;</td>' +
								'<td class="tdBorder">&nbsp;</td></tr>';
						}
					}
					
					resultContent += '</table>';
					$('#wjdcContent').html(resultContent);
				}
				
				$('#exportButton').show();
			}
		});
	}
	
	/**生成图表*/
	function showChart(id, data){
		var chartDiv = echarts.init(document.getElementById(id));
		chartDiv.showLoading({text: '正在努力的读取数据中...'});  
		chartDiv.setOption(data);
		chartDiv.hideLoading();
		var imageObj = chartDiv.getDataURL('png');
		if(imageObj.toString().length > 0){
			imageData.push(base_encode(imageObj.toString().split(',')[1]));
		}
	}
	
	/**导出*/
	function exportExcel(){
		$('#mask').show();
		$.ajax({
			type : "POST",
			async:true,
			url : '<%=request.getContextPath()%>/Svl_wjdc',
			data : 'active=exportToExcel&sAuth=' + auth + '&WW_WJBH=' + iKeyCode + '&imageData=' + imageData,
			dataType:"json",
			success : function(data) {
				if(data != null){
					if(data[0].MSG == '文件生成成功'){
						//下载文件到本地
						$("#exportIframe").attr("src", '<%=request.getContextPath()%>/form/questSurvey/download.jsp?filePath=' + encodeURIComponent(data[0].PATH));
					}else{
						alert(data[0].Msg);
					}
					$('#mask').hide();
				}
			}
		});
	}
	
	/**base64特殊编码转换*/
	function base_encode(str) {
		/*
        var codeArray  = ["/", "+", "="];
        var regArray = ["_a", "_b", "_c"];
        for(var i=0; i<codeArray.length; i++){
       		str = str.replace(/\codeArray[i]/g, regArray[i]);
        }
       	*/
        str = str.replace(/\//g, '_a');
        str = str.replace(/\+/g, '_b');
       	
        return str;
	}
	
	//首页
	function goHomePage(){
		window.location = '<%=request.getContextPath()%>/form/questSurvey/wjdcList.jsp';
	}
	
	//登出
	function logout(){
		window.location = '<%=request.getContextPath()%>/pjLogout.jsp';
	}
</script>
</html>