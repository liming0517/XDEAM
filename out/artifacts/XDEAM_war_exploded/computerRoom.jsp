<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.base.common.tools.PublicTools"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.*"%>
<%@ page import="com.pantech.base.common.db.DBSource"%>

<!--
	createTime:2015-12-10
	createUser:lupengfei
	description:教室课表
-->
<html>
<head>
<title>楼层课表</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- JQuery 专用4个文件 -->
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/icon.css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
</head>

<style>
		#xlTime{
			width:100%;
			border-right:1px solid #99bbe8;
		}
		#xlTime{
			width:99.9%;
			border-top:1px solid #99bbe8;
		}
		#xlTime td{
			width:300px;
			border-left:1px solid #99bbe8;
			border-bottom:1px solid #99bbe8;
			text-align:center;
			empty-cells:show;
			font-size:11;
		}
		#xlTime th{
			width:300px;
			border-left:1px solid #99bbe8;
			border-bottom:1px solid #99bbe8;
			text-align:center;
			empty-cells:show;
		}
		#divPageMask2{background-color:#D2E0F2; filter:alpha(opacity=100);left:0px;top:0px;z-index:100;}
	
	</style>

<body style="width:100%;height:567px;" >
	<center>
	
	<div id="mainroom" style="width:1300px;height:720px;background-image:url('<%= request.getContextPath()%>/images/qsczp/beijing10.png');background-repeat: no-repeat;" >
		<div id="showtime" style="width:1200px;height:50px;margin-top:25px;font-size:36px;font-weight:600;" align="right">
	
		</div>
		<div id="index"	style="width:1200px;height:560px;">
			<table id="xlTime" cellspacing="0" cellpadding="0" style="height:74%;border-collapse:collapse;"> 
					<tbody id="content"> 
							 
					</tbody> 
			</table>
			<%-- 遮罩层 --%>
<!-- 	    	<div id="divPageMask2" style="position:absolute;top:0px;left:0px;width:100%;height:100%;" align="center"> -->
<!-- 	    		<div style="font-size:20px;margin-top:200px;">信息加载中请稍后!</div> -->
<!-- 	    	</div> -->
		</div>

	</div>

	</center>
</body>
<script type="text/javascript">
	var iUSERCODE="<%=MyTools.getSessionUserCode(request)%>";
	var pageNum = 1;   //datagrid初始当前页数
	var pageSize = 20; //datagrid初始页内行数
	var sjd=-1;//时间段
	var tag=0;//是否读取过课表
	var time =null;
	
	$(document).ready(function(){
		
		disptime();
		loadComputerRoom();
		checktime();
		
	});
	
	//检查当前时间
	function checktime(){ 
		if(time.toLocaleTimeString().substring(0,5)=="11:15"||time.toLocaleTimeString().substring(0,5)=="12:30"||time.toLocaleTimeString().substring(0,5)=="17:00"||time.toLocaleTimeString().substring(0,4)=="6:00"){
			loadComputerRoom();
		}
		//1分钟检查一次时间
		var timer = setTimeout("checktime()",60000); 
	}

	
	//显示时间
	function disptime(){ 
		time = new Date(); //获得当前时间
		//获得年、月、日
		var year = time.getYear();
		var month = time.getMonth()+1;
		var day = time.getDate(); 
		var weeks; 
		if(new Date().getDay()==0){
			weeks="星期日";
		}
		if(new Date().getDay()==1){
			weeks="星期一";
		} 
		if(new Date().getDay()==2){
			weeks="星期二";
		}
		if(new Date().getDay()==3){
			weeks="星期三";
		} 
		if(new Date().getDay()==4){
			weeks="星期四";
		} 
		if(new Date().getDay()==5){
			weeks="星期五";
		} 
		if(new Date().getDay()==6){
			weeks="星期六";
		} 
		
		//获得小时、分钟、秒
		var hour = time.getHours(); 
		var minute = time.getMinutes(); 
		var second = time.getSeconds(); 
		var apm="AM"; //默认显示上午: AM
		//按12小时制显示
		if(hour>12){ 
			hour=hour-12; apm="PM" ; 
		} 
		//如果分钟只有1位，补0显示
		if(minute < 10){
		 	minute="0"+minute;
		}
		//如果秒数只有1位，补0显示
		if(second < 10){
		  second="0"+second; 
		}
		 
		/*设置文本框的内容为当前时间*/ 
		$('#showtime').html(year+"年"+month+"月"+day+"日  "+weeks+"  "+hour+":"+minute+":"+second+" "+apm); 
		/*设置定时器每隔1秒（1000毫秒），调用函数disptime()执行，刷新时钟显示*/ 
		var myTime = setTimeout("disptime()",1000); 
	} 

	
	function loadComputerRoom(){
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_Skjh',
				data : 'active=loadComputerRoom',
				dataType:"json",
				loadMsg : "",//载入时信息
				success : function(data) {
					//alert(data[0].行政班名称);	
					//if(data[0].VEC!="5"){
					//	alertMsg(data[0].VEC);
					//}else{
						var classroom=data[0].MSG;
						var timedata=data[0].MSG2;
						var rooms=classroom.split(",");
						sjd=timedata.substring(timedata.indexOf("#")+1,timedata.length);
						var times=timedata.substring(0,timedata.indexOf("#")).split(",");
					
						var count = -1;
						if(sjd==1){
							count=1;
						}else if(sjd==2){
							count=5;
						}else if(sjd==3){
							count=7;
						}else if(sjd==4){
							count=11;
						}
						var idNum = "";
						var html="";
							html += '<tr align="center" style="height:40px;">'+
										'<th >'+
			// 								className1 +
										'</th>';
						for(var k=0;k<9;k++){
							var rom=rooms[k];
							if(rom=="0000"){
								rom="操场";
							}else if(rom=="0001"){
								rom="其它";
							}else if(rom=="0002"){
								rom="校外";
							}
							html +='<th >'+
										rom +
								   '</th>';
						}
						html +='<th>'+
								'</th>';
						html +='<th>'+
								'</th>';
						html +='<th>'+
								'</th>';
						html+='</tr>';
						var html2="";
							
						for(var i=0;i<times.length;i++){
								idNum = count+i;
								if(idNum < 10){
									idNum = ("0"+idNum);
								}
								html2+= '<tr align="center" style="height:45px;">'+
										'<th class="jc">'+
			 								times[i] +
										'</th>';
								for(var k=0;k<9;k++){
									html2+= '<td id="'+rooms[k]+idNum+'" class="pkxx" title="">'+
											'</td>';
								}	
								html2+= '<td id="" class="pkxx" title="">'+
											'</td>';
								html2+= '<td id="" class="pkxx" title="">'+
											'</td>';	
								html2+= '<td id="" class="pkxx" title="">'+
											'</td>';					
								html2+=	'</tr>';
						}
						
						var html3="";
							html3 += '<tr align="center" style="height:40px;">'+
										'<th>'+
			// 								className1 +
										'</th>';
						for(var k=9;k<rooms.length;k++){
							var rom=rooms[k];
							if(rom=="0000"){
								rom="操场";
							}else if(rom=="0001"){
								rom="其它";
							}else if(rom=="0002"){
								rom="校外";
							}
							html3 +='<th>'+
										rom +
								   '</th>';
						}
						html3+='</tr>';
						var html4="";
							
						for(var i=0;i<times.length;i++){
								idNum = count+i;
								if(idNum < 10){
									idNum = ("0"+idNum);
								}
								html4+= '<tr align="center" style="height:45px;">'+
										'<th class="jc">'+
			 								times[i] +
										'</th>';
								for(var k=9;k<rooms.length;k++){
									html4+= '<td id="'+rooms[k]+idNum+'" class="pkxx" title="">'+
											'</td>';
								}							
								html4+=	'</tr>';
						}
						
						$('#content').html(html+html2+html3+html4);
						$.parser.parse(('#content')); 
						
						for(var j=0;j<data.length;j++){
								for(var k=0;k<rooms.length;k++){
									if(data.length!=1){
										if(data[j].实际场地编号.indexOf(rooms[k])>-1){ 
											$("#content td[id='"+rooms[k]+data[j].时间序列.substring(2,4)+"']").html(data[j].行政班名称+"<br />"+data[j].课程名称+"<br />"+data[j].授课教师姓名);
											var clxx=data[j].行政班名称;
											var pkxx=data[j].课程名称;
											var jsxx=data[j].授课教师姓名;
											var cdxx=data[j].实际场地名称;
											pkxx=pkxx.replace(new RegExp('&amp;','gm'),'&');
											jsxx=jsxx.replace(new RegExp('&amp;','gm'),'&');
											cdxx=cdxx.replace(new RegExp('&amp;','gm'),'&');
											$("#content td[id='"+rooms[k]+data[j].时间序列.substring(2,4)+"']").attr('title',(clxx+"\r\n"+pkxx+"\r\n"+jsxx));
										}
									}
								}
						}
						//$('#divPageMask2').hide();
					//}
				}
			});
		}
				
		

</script>
</html>