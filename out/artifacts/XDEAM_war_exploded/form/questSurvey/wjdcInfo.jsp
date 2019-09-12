<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="java.util.Vector"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.src.develop.store.user.*"%>
<%
	/**
		创建人：yeq
		Create date: 2015.11.18
		功能说明：问卷调查
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
	<title>问卷调查</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
		font-size:18px;
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
		display:none;
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
		width:100%;
		border-top:2px solid #000000;
		border-left:2px solid #000000;
		border-right:1px solid #000000;
		border-bottom:1px solid #000000;
		text-align:center;
	}
	
	.contentTr{
		height:35px;
		line-height:35px;
	}
	
	.contentTd{
		border-right:1px solid #000000;
		border-bottom:1px solid #000000;
	}
	
	.contentInput{
		width:90%;
		text-align:center;
		border:0;
		background:white;
		font-size:16px;
	}
	
	.contentTr_3{
		height:30px;
		line-height:18px;
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
				<br/><br/>
				<!-- 
				<table id="closeButton" class="pageButton" cellpadding="0" cellspacing="0" onclick="window.close();">
					<tr>
						<td style="width:6px; background:url('<-%=request.getContextPath()%>/images/wjdc/wen-anniuleft.png');"></td>
						<td id="buttonFont">离&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;开</td>
						<td style="width:7px; background:url('<-%=request.getContextPath()%>/images/wjdc/wen-anniuright.png');"></td>
					</tr>
				</table>
				 -->
				<table id="submitButton" class="pageButton" cellpadding="0" cellspacing="0" onclick="doSubmit();">
					<tr>
						<td style="width:6px; background:url('<%=request.getContextPath()%>/images/wjdc/wen-anniuleft.png');"></td>
						<td id="buttonFont">提 交 问 卷</td>
						<td style="width:7px; background:url('<%=request.getContextPath()%>/images/wjdc/wen-anniuright.png');"></td>
					</tr>
				</table>
			</td>
		</tr>
		<tr id="juanwei"><td></td></tr>
		<tr><td style="height:60px;">&nbsp;</td></tr>
	</table>
	
	<form id="form1" method="post">
		<input type="hidden" id="active" name="active"/>
		<input type="hidden" id="WW_WJBH" name="WW_WJBH"/>
		<input type="hidden" id="WW_WJLX" name="WW_WJLX"/>
		<input type="hidden" id="result" name="result"/>
	</form>
</body>
<script type="text/javascript">
	var auth = '<%=sAuth%>';
	var userCode = '<%=usercode%>';
	var userName = '<%=userName%>';
	var teaAuth = '<%=MyTools.getProp(request, "Base.wjdcTea")%>';
	var stuAuth = '<%=MyTools.getProp(request, "Base.wjdcStu")%>';
	var iKeyCode = '<%=request.getParameter("iKeyCode")%>';
	var allQuestion = '';
	var wjType = '';
	var doFlag = '0';
	var limit = '0';
	var data = '';
	var optionTitleArray = ['A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'];
	var optionIndex = 0;
	
	var tempScore = 0;
	var totalScore = 0;
	
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
	
		loadWjdc(); //读取问卷调查
	});
	
	/**读取问卷调查**/
	function loadWjdc(){
		$.ajax({
			type : "POST",
			async:true,
			url : '<%=request.getContextPath()%>/Svl_wjdc',
			data : 'active=loadWjdc&WW_WJBH=' + iKeyCode,
			dataType:"json",
			success : function(data) {
				$('#wjTitle').html(data.title);
				if(data.doFlag=='1' || data.doFlag=='2'){
					$('#submitButton').hide();
				}else{
					$('#submitButton').show();
				}
				limit = data.limit;
				fillContent(data);
			}
		});
	}
	
	function fillContent(data){
		wjType = data.type;
		allQuestion = data.allQuestion;
		doFlag = data.doFlag;
		
		var optionData = '';
		var resultContent = '';
		var lineFlag = false;
		var optionLength = 0;
		var scoreRange = new Array();
		
		//遮罩层
		resultContent = '<div id="mask" style="position:absolute; top:0; z-index:99999; background:url(0); display:none;"></div>';
		
		//判断调查问卷类型
		if(wjType == '01'){
			for(var i=0; i<allQuestion.length; i++){
				optionData = allQuestion[i].allOptions;
				optionLength = 0;
				
				//获取选项长度，判断是否要换行
				for(var j=0; j<optionData.length; j++){
					optionLength += 2+optionData[j].option.length;
				}
				if(optionLength > 30){
					lineFlag = true;
				}else{
					lineFlag = false;
				}
				optionIndex = 0;
				
				resultContent += '<span style="width:18px; display:inline-block; margin-top:40px; font-weight:bold; color:#FBF3CF; background-color:#FD7A7A; text-align:center;">'+(i+1)+'</span>&nbsp;';
				resultContent += allQuestion[i].question+'<br/><div style="position:relative; top:5px; left:20px; margin-bottom:40px;">';
				
				//判断题目类型（1单选/2多选）
				if(allQuestion[i].quesType == '01'){
					for(var j=0; j<optionData.length; j++){
						resultContent += '<span style="margin-right:30px;">' +
							'<input id="ans_'+i+'_'+optionTitleArray[optionIndex]+'" name="ques_'+i+'" type="radio" value="'+optionTitleArray[optionIndex]+'"';
						if(doFlag=='1' && allQuestion[i].answer==optionTitleArray[optionIndex]){
							resultContent += 'checked';
						}
						resultContent += ' style="cursor:pointer;">' +
								'<label for="ans_'+i+'_'+optionTitleArray[optionIndex]+'" style="cursor:pointer;">' +
									optionData[j].option +
								'</label>' +
							'</span>';
						if(lineFlag == true){
							resultContent += '<br/>';
						}
						optionIndex++;
					}
				}else if(allQuestion[i].quesType == '02'){
					for(var j=0; j<optionData.length; j++){
						resultContent += '<span style="margin-right:30px;">' +
							'<input id="ans_'+i+'_'+optionTitleArray[optionIndex]+'" name="ques_'+i+'" type="checkbox" value="'+optionTitleArray[optionIndex]+'"';
						if(doFlag=='1' && allQuestion[i].answer.indexOf(optionTitleArray[optionIndex])>-1){
							resultContent += 'checked';
						}
						resultContent += ' style="cursor:pointer;">' +
								'<label for="ans_'+i+'_'+optionTitleArray[optionIndex]+'" style="cursor:pointer;">' +
									optionData[j].option +
								'</label>' +
							'</span>';
						if(lineFlag == true){
							resultContent += '<br/>';
						}
						optionIndex++;
					}
				}
				resultContent += '</div>';
				
				if(i < allQuestion.length-1){
					resultContent += '<img src="<%=request.getContextPath()%>/images/wjdc/wen-fengexian.png">';
				}
			}
			$('#wjdcContent').html(resultContent);
		}
		
		if(wjType == '02'){
			var tempData = '[{option:"很满意",score:"91-100"},'+
						'{option:"较满意",score:"81-90"},'+
						'{option:"基本满意",score:"71-80"},'+
						'{option:"尚可",score:"61-70"},'+
						'{option:"不满意",score:"50-60"}]';
			var title = eval("("+tempData+")");//转换为json对象 
		
			resultContent += '<div style="width:100%; position:relative; left:8px;">' +
				'<div style="float:left; width:250px">学生姓名：' + data.stuName + '</div>' +
				'<div style="float:left; width:250px">系别：' + data.majorName + '</div>' +
				'<div style="float:left; width:250px">班级：' + data.className + '</div></div>';
			resultContent += '<table class="contentTable" cellpadding="0" cellspacing="0">' + 
				'<tr class="contentTr_3">' + 
					'<td class="contentTd" style="width:80px;">教师姓名</td>' +
					'<td class="contentTd" style="width:150px;">课程</td>';
			for(var i=0; i<title.length; i++){
				resultContent += '<td class="contentTd" style="width:70px;">' + title[i].option + '<br/>(' + title[i].score + ')' + '</td>';
			}
			resultContent += '<td class="contentTd">意见栏</td></tr>';
			
			for(var i=0; i<allQuestion.length; i++){
				resultContent += '<tr class="contentTr_3">' + 
					'<td class="contentTd">' + allQuestion[i].teaName + '</td>' + 
					'<td class="contentTd">' + allQuestion[i].courseName + '</td>';
				for(var j=0; j<title.length; j++){
					scoreRange = title[j].score.split('-');
					resultContent += '<td class="contentTd"><input id="score_'+i+'_'+j+'" name="' + title[j].score + '" class="contentInput scoreInput ' + allQuestion[i].teaCode + ' ' + allQuestion[i].teaName + ' ' + allQuestion[i].courseCode + '"';
					//判断是否填充答案
					if(allQuestion[i].score!='' && parseInt(allQuestion[i].score, 10)>=parseInt(scoreRange[0], 10) && parseInt(allQuestion[i].score, 10)<=parseInt(scoreRange[1], 10)){
						resultContent += ' value="' + allQuestion[i].score + '"';
					}
					
					resultContent += '></td>';
				}
				resultContent += '<td id="opinionTd_' + i + '" class="contentTd" onclick="enterOpinion(this.id);">' + 
								'<div id="opinionText_' + i + '" style="width:165px; word-break:break-all;">';
				if(allQuestion[i].suggest != ''){
					resultContent += allQuestion[i].suggest;
				}else{
					resultContent += '&nbsp';
				}			
				resultContent += '</div>' + 
								'<input id="opinion_' + i + '" class="contentInput" style="width:165px; display:none;" onblur="leaveOpinion(this.id);"/>' + 
								'</td></tr>';
			}
			
			if(allQuestion.length < 8){
				for(var i=allQuestion.length; i<8; i++ ){
					resultContent += '<tr class="contentTr_3">';
					for(var j=0; j<3+title.length; j++){
						resultContent += '<td class="contentTd">&nbsp;</td>';
					}
					resultContent += '</tr>';
				}
			}
			resultContent += '</table>';
			$('#wjdcContent').html(resultContent);
			
			$('.scoreInput').bind('keydown', function(e){
				//ie火狐兼容
				e = e || window.event;
				var curKey = e.which || e.keyCode;
				//var realkey = String.fromCharCode(e.which);
				//var reg = /^[0-9]*$/;
				//数字
				if(curKey>=48 && curKey<=57) return true;
				//小数字键盘
				if(curKey>=96 && curKey<=105) return true; 
				//backspace/delete/tab
				if(curKey==8 || curKey==9 || curKey==46) return true;
				
				return false;
			}).bind('blur',function(){
				var curId = $(this).attr('id');
				checkScoreRange(curId);
				if(limit == '0'){
					checkUniqueScore(curId);
				}
			});
		}
		
		if(wjType == '03'){
			var date = data.date.split('-');
		
			resultContent += '<div style="width:100%; text-align:center;">辅导员姓名：<span style="width:100px; border-bottom:1px solid #000000; display:inline-block;">' + data.teaName + '</span>&nbsp;&nbsp;' +
				'专业：<span style="width:190px; border-bottom:1px solid #000000; display:inline-block;">' + data.majorName + '</span>&nbsp;&nbsp;' +
				'测评时间：<span style="width:50px; border-bottom:1px solid #000000; display:inline-block;">' + date[0] + '</span>年' +
				'<span style="width:50px; border-bottom:1px solid #000000; display:inline-block;">' + date[1] + '</span>月' + 
				'<span style="width:50px; border-bottom:1px solid #000000; display:inline-block;">' + date[2] + '</span>日</div><br/>';
			resultContent += '<table class="contentTable" cellpadding="0" cellspacing="0">' + 
				'<tr class="contentTr">' + 
					'<td class="contentTd" style="width:80px;">序号</td>' +
					'<td class="contentTd">测评内容</td>' +
					'<td class="contentTd">观测点</td>' +
					'<td class="contentTd" style="width:60px;">分值</td>' +
					'<td class="contentTd" style="width:60px;">得分</td>' +
				'</tr>';
			
			for(var i=0; i<allQuestion.length; i++){
				resultContent += '<tr class="contentTr">' + 
								'<td class="contentTd">' + (i+1) + '</td>' +
								'<td class="contentTd" style="text-align:left; padding-left:10px; padding-right:10px;">' + allQuestion[i].question + '</td>' +
								'<td class="contentTd" style="text-align:left; padding-left:10px; padding-right:10px;">' + allQuestion[i].point + '</td>' +
								'<td id="max_' + i + '" class="contentTd">' + allQuestion[i].score + '</td>' +
								'<td id="inputTd_' + i + '" class="contentTd" ';
				if(doFlag == '1'){
					resultContent += '>' + allQuestion[i].answer;
					totalScore += parseFloat(allQuestion[i].answer);
				}else{
					resultContent += 'onclick="focusInput(this.id);"><input id="score_' + i + '" name="0-' + allQuestion[i].score + '" class="contentInput scoreInput">';
				}
				resultContent += '</td>' +
								'</tr>';
			}
			
			resultContent += '<tr class="contentTr">' +
								'<td class="contentTd">满意度<br/>测评</td>' +
								'<td id="" class="contentTd" colspan="2">' +
									'<span style="margin-right:30px; cursor:pointer;"><input id="manyidu_1" name="manyidu" class="manyidu" type="radio" value="1" style="cursor:pointer;"/><label for="manyidu_1" style="cursor:pointer;">非常满意</label></span>' +
									'<span style="margin-right:30px; cursor:pointer;"><input id="manyidu_2" name="manyidu" class="manyidu" type="radio" value="2" style="cursor:pointer;"/><label for="manyidu_2" style="cursor:pointer;">满意</label></span>' +
									'<span style="margin-right:30px; cursor:pointer;"><input id="manyidu_3" name="manyidu" class="manyidu" type="radio" value="3" style="cursor:pointer;"/><label for="manyidu_3" style="cursor:pointer;">不满意</label></span>' +
								'</td>' +
								'<td class="contentTd">合计</td>' +
								'<td id="totalScore" class="contentTd">';
			if(totalScore != ''){
				resultContent += totalScore;
			}else{
				resultContent += '&nbsp;';
			}
			resultContent += '</td>' +
							'</tr>' +
						'</table>';
			$('#wjdcContent').html(resultContent);
			
			//满意度赋值
			if(doFlag=='1' && data.myd!=''){
				$("input[name='manyidu'][value=" + data.myd + "]").attr("checked",true);
			}
			
			$('.scoreInput').bind('keydown', function(e){
				//ie火狐兼容
				e = e || window.event;
				var curKey = e.which || e.keyCode;
				
				//禁用shift+数字键
			    if(e.shiftKey && curKey>=48 && curKey<=57) return false;
				//数字
				if(curKey>=48 && curKey<=57) return true;
				//小数字键盘
				if(curKey>=96 && curKey<=105) return true;
				//点
				if(curKey==110 || curKey==190) return true;
				//backspace/delete/tab
				if(curKey==8 || curKey==9 || curKey==46) return true;
				//左右键
				if(curKey==37 || curKey==39) return true;
				
				return false;
			}).bind('focus', function(){
				tempScore = $(this).val();
			}).bind('blur',function(){
				var curId = $(this).attr('id');
				checkScoreRange(curId);
				countTotalScore(curId);
			});
		}
		
		if(doFlag=='1' || doFlag=='2'){
			$('#mask').show();
			$('#mask').css('width', $('#wjdcTable').css('width'));
			$('#mask').css('height', $('#wjdcTable').css('height'));
		}
	}
	
	//点击意见栏，切换输入框
	function enterOpinion(id){
		var tempIndex = id.substring(id.indexOf('_'));
		$('#opinionText' + tempIndex).hide();
		$('#opinion' + tempIndex).show();
		$('#opinion' + tempIndex).focus();
	}
	
	//离开意见栏，切换文本显示
	function leaveOpinion(id){
		var tempIndex = id.substring(id.indexOf('_'));
		var str = $('#' + id).val();
		$('#' + id).hide();
		$('#opinionText' + tempIndex).html(str==''?'&nbsp;':str);
		$('#opinionText' + tempIndex).show();
	}
	
	function focusInput(id){
		$('#' + id).children().focus();
	}
	
	//检查填写的分数是否在允许范围内
	function checkScoreRange(id){
		var curScore =  $('#' + id).val();
		var range = $('#' + id).attr('name').split('-');//取值范围
		
		if(curScore != ''){
			curScore = parseFloat($('#' + id).val());
			if(isNaN(curScore)){
				curScore = '';
			}else{
				if(curScore < parseInt(range[0], 10)) curScore = range[0];
				if(curScore > parseInt(range[1], 10)) curScore = range[1];
			}
		}
		
		$('#' + id).val(curScore);
	}
	
	//检查同课程教师打分唯一性
	function checkUniqueScore(id){
		if($('#' + id).val() != ''){
			var courseCode = $('#' + id).attr('class').split(' ')[4];
			
			$('.' + courseCode).each(function(){
				if($(this).attr('id') != id){
					$(this).val('');
				}
			});
		}
	}
	
	/**验证数字*/
	/*
	function checkNum(id){
		var result = true;
		var reg = /^(?:[1-9][0-9]*(?:\.[0-9]+)?|0\.(?!0+$)[0-9]+)$/;//正浮点数
		var score = parseFloat($('#'+id).val());
		
		if(reg.test(score)){
			//判断数字是否超过最大分值
			if(score > parseFloat($('#max' + id.substring(id.indexOf('_'))).html())){
				result = false;
				$('#'+id).val(tempScore);
			}
		}else{
			result = false;
			$('#'+id).val(tempScore);
			//alertMsg('请输入正确的数字');
		}
		return result;
	}
	*/
	
	/**计算总分*/
	function countTotalScore(id){
		var temp = $('#'+id).val();
		totalScore -= tempScore;
		
		if(temp == '') temp = 0;
		totalScore += parseFloat(temp);
		
		if(totalScore == 0)		
			$('#totalScore').html('&nbsp;');
		else
			$('#totalScore').html(totalScore);
	}
	
	function doSubmit(){
		$('#mask').show();
		$('#submitButton').hide();
		var result = '';
		var tempValue= '';
		
		//判断问卷类型
		if(wjType == '01'){
			for(var i=0; i<allQuestion.length; i++){
				//单选题
				if(allQuestion[i].quesType == '01'){
					tempValue = $("input[name='ques_" + i + "']:checked").val();
					
					if(tempValue == undefined){
						alert("请完成所有题目后重新提交");
						return;
					}else{
						result += allQuestion[i].code+":"+tempValue+"｜｜";
					}
				}
				
				//多选题
				if(allQuestion[i].quesType == '02'){
					tempValue = '';
					
					$("input[name='ques_" + i + "']:checked").each(function(){    
						tempValue += $(this).val()+'+';   
					});
					
					if(tempValue == ''){
						alert("请完成所有题目后重新提交");
						return;
					}else{
						tempValue = tempValue.substring(0, tempValue.length-1);
						result += allQuestion[i].code+":"+tempValue+"｜｜";
					}
				}
			}
		}else if(wjType == '02'){
			var courseArray = new Array();
			var flag = true;
			var teaCode = '';
			var opinionStr = '';
			var scoreFlag = false;
			
			//获取所有课程编号
			for(var i=0; i<allQuestion.length; i++){
				flag = true;
				
				for(var j=0; j<courseArray.length; j++){
					if(courseArray[j] == allQuestion[i].courseCode){
						flag = false;
						break;
					}
				}
				
				if(flag){
					courseArray.push(allQuestion[i].courseCode);
				}
			}
			
			for(var i=0; i<courseArray.length; i++){
				scoreFlag = false;
			
				$('.' + courseArray[i]).each(function(){
					if($(this).val() != ''){
						teaCode = $(this).attr('class').split(' ')[2];
						teaName = $(this).attr('class').split(' ')[3];
						opinionStr = $('#opinion_' + $(this).attr('id').split('_')[1]).val().replace(/\｜/g, '|');
						
						result += courseArray[i]+'＼'+teaCode+'＼'+teaName+'＼'+$(this).val()+'＼'+opinionStr+'｜｜';
						scoreFlag = true;
						return false;
					}
				});
				//判断是否当前课程是否有评分
				if(scoreFlag == false){
					alert("请为所有授课老师评分后重新提交");
					return;
				}
			}
		}else if(wjType == '03'){
			for(var i=0; i<allQuestion.length; i++){
				tempValue  = $('#score_' + i).val();
				
				if(tempValue == ''){
					alert("请完成所有评测后重新提交");
					return;
				}
				
				result += allQuestion[i].code+":"+tempValue+"｜｜";
			}
			
			tempValue = $("input[name='manyidu']:checked").val();
			if(tempValue == undefined){
				alert("请选择满意度后重新提交");
				return;
			}
			result += ":"+tempValue+"｜｜";
		}
		
		if(result.length > 0){
			result = result.substring(0, result.length-2);
		}
		$('#result').val(encodeURI(result).replace(/\+/g,'%2B'));
		$('#active').val('saveResult');
		$('#WW_WJBH').val(iKeyCode);
		$('#WW_WJLX').val(wjType);
		$('#form1').submit();
	}
	
	$('#form1').form({
		//定位到servlet位置的url
		url:'<%=request.getContextPath()%>/Svl_wjdc',
		//当点击事件后触发的事件
		onSubmit: function(data){}, 
		//当点击事件并成功提交后触发的事件
		success:function(data){
			var json  =  eval("("+data+")");
			if(json[0].MSG == '提交成功'){
				window.location = '<%=request.getContextPath()%>/form/questSurvey/wjdcList.jsp';
			}else{
				$('#mask').hide();
				alert(json[0].MSG);
			}
		}
	});
	
	//首页
	function goHomePage(){
		window.location = '<%=request.getContextPath()%>/form/questSurvey/wjdcList.jsp';
	}
	
	//登出
	function logout(){
		window.location = "<%=request.getContextPath()%>/pjLogout.jsp";
	}
</script>
</html>