<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%
	/**
		创建人：wangzh
		Create date: 2015.06.05
		功能说明：用于设置班级固排禁排
		页面类型:列表及模块入口
		修订信息(有多次时,可有多个)
		修订日期:
		原因:
		修订人:
		修订时间:
	**/
 %>
 
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.base.common.tools.PublicTools"%>
<%@ page import="com.pantech.src.develop.Logs.*"%>
<%@ page import="com.pantech.base.common.tools.*"%>
<%@ page import="com.pantech.src.develop.store.user.*"%>
<%@ page import="com.pantech.src.develop.manage.workremind.WorkRemind"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>
<%@ page import="com.pantech.base.common.db.DBSource"%>
<%
UserProfile up = new UserProfile(request,MyTools.getSessionUserCode(request)); 
String userName =up.getUserName();
String userCode =up.getUserCode();
%>
<%
		/*
			获得角色信息
		*/
		
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
		for(int i=0;i<v.size();i++){
			sAuth += MyTools.StrFiltr(v.get(i))+",";
		}
		sAuth=sAuth.substring(0, sAuth.length()-1);
		// 如果无法获取人员信息，则自动跳转到登陆界面
%>
<html>
	<head>
		<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
		#divPageMask2{background-color:#D2E0F2; filter:alpha(opacity=50);left:0px;top:0px;z-index:100;}
	</style>
	<body class="easyui-layout">
		<%-- 遮罩层 --%>
    	<div id="divPageMask2" style="position:absolute;top:0px;left:0px;width:100%;height:100%;">
    	</div>
    	
		<div data-options="region:'north'" title="" style = "height:130px;width:100%;">
<!-- 			<table  class = "tablestyle" width = "100%"> -->
<!-- 				<tr> -->
<!-- 					<td> -->
<!-- 						<a href="#" onclick="doToolbar(this.id);" id="import"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-site'">选修课导入</a> -->
<!-- 					</td> -->
<!-- 				</tr> -->
<!-- 			</table> -->
			<table id='list' style="width:100%;height:20%;">
			
			</table>
		</div>
		<div data-options="region:'center'">
			
			<div id="setTime" style="overflow-x:hidden;">
				<form id='fm' method='post' style="margin: 0px;padding-top: 5px;">
					<table id="xlTime" cellspacing="0" cellpadding="0" style="height:74%;border-collapse:collapse;"> 
						<tbody id="content"> 
							 
						</tbody> 
					</table>
					<input type="hidden" id="active" name="active"/>
					<input type="hidden" id="GG_XZBDM" name="GG_XZBDM"/>
					<input type="hidden" id="GG_XNXQBM" name="GG_XNXQBM"/>
					<input type="hidden" id="GG_SJXL" name="GG_SJXL"/>
					<input type="hidden" id="KCJS" name="KCJS"/>
					<input type="hidden" id="SJXL" name="SJXL"/>
					<input type="hidden" id="CDYQ" name="CDYQ"/>
					<input type="hidden" id="CDMC" name="CDMC"/>
					<input type="hidden" id="SAVETYPE" name="SAVETYPE"/>
					<input type="hidden" id="GG_SKJHMXBH" name="GG_SKJHMXBH"/>
					<input type="hidden" id="JSBH" name="JSBH"/>
					<input type="hidden" id="JSXM" name="JSXM"/>
				</form>
			</div>
		</div>
		
		<!-- 选修课导入 -->
		<div id="xxkImport" style="overflow:hidden;">
			<form id="form1" method='post'>
				<table style="width:100%;" class="tablestyle">
					<tr>
						<td class="titlestyle">学年学期</td>
						<td>
							<select name="IM_XNXQ" id="IM_XNXQ" class="easyui-combobox" panelHeight="auto" editable="false" style="width:150px;"  required="true">
							</select>
						</td>
					</tr>
					<tr>
						<td class="titlestyle">教学性质</td>
						<td>
							<select name="IM_JXXZ" id="IM_JXXZ" class="easyui-combobox" panelHeight="auto" editable="false" style="width:150px;">
							</select>
						</td>
					</tr>
					<tr>
						<td colspan="2" align="center">
							<input id="Select" name="Select" class="easyui-textbox" type='button' style="text-align:center;width:80px;" value='[附件选择]'  onclick='selectfile()'/>
						</td>
					</tr>
					<tr>
						<td colspan="2" align="center">
							<input id="Upload" name="Upload" class="easyui-textbox" type='button' style="text-align:center;width:80px;" value='[上传]'  onclick='show()'/>
						</td>
					</tr>
				</table>
				<input type="hidden" id="active" name="active"/>
			</form>
		</div>
	</body>
	<script type="text/javascript">
		var iUSERCODE="<%=MyTools.getSessionUserCode(request)%>";   //获得用户登录code
		var sAuth="<%=sAuth%>";  //角色权限
		var iKeyCode = "";//授课计划明细编号
		var pageNum = 1;   //datagrid初始当前页数
		var pageSize = 20; //datagrid初始页内行数
		//var xkmc = "";//学科名称下拉框数据
		//var rkjs = "";//任课教师下拉框数据
		var kcmc = "";//课程名称
		var jsbh = "";//教师编号
		var jsxm = "";//教师姓名
		var skzcxq = "";//授课周次详情
		var cdyq = "";//场地要求
		var cdmc = "";//场地名称
		var zjs = "";//总节数
		var gjs = "";//固排已排节数
		var lj = "";//连节
		var lc = "";//连次
		var lcs= "";//已排连次次数
		var aod= "";//判断添加或删除
		var checkrec="";
		var delinfo = "";
		var classdm = "";//行政班代码
		var classmc = "";//行政班名称
		var classId1 = window.parent.document.frames["left"].classId;//实际是场地要求
		var className = window.parent.document.frames["left"].className;//实际是场地名称
		var xnxqVal1 = window.parent.document.frames["left"].xnxqVal;
		var jxxzVal1 = window.parent.document.frames["left"].jxxzVal;
		var weeks1="";
		var lastIndex = -1;
		var savexqbh1="";//保存传递过来的学期编码
		var savexqbh2="";//保存传递过来的学期编码
		var paike="0";//排课状态
		var gpjs=0;
		var idValconTrans="";
		var kclx=""; //课程类型
		//var saveType = "";//判断打开窗口的操作（new or edit）
		
		
		$(document).ready(function(){ 
			initDialog();
			initGridData(classId1,className,xnxqVal1,jxxzVal1,"1");//页面初始化加载数据
			
		});
		
		//页面初始化加载数据
		function initGridData(classId,className,xnxqVal,jxxzVal,n){
			savexqbh1=xnxqVal;
			savexqbh2=jxxzVal;
			checkpaike();
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_Cdgpjp',
				data : 'active=initGridData&page=' + pageNum + '&rows=' + pageSize + '&JSBH=' + classId + '&JSXM=' + className + '&XNXQ=' + xnxqVal + '&JXXZ=' + jxxzVal+'&iUSERCODE='+iUSERCODE+'&sAuth='+sAuth,
				dataType:"json",
				success : function(data) {
					//xkmc = data[0].xkmcData;//获取学科名称下拉框数据
					//rkjs = data[0].rkjsData;//获取任课教师下拉框数据					
					loadTime(data[0].timeData,data[0].gpjpData);
					
					loadGrid(data[0].listData);//加载特殊规则列表
					
					//initCombobox(xkmc,rkjs);//初始化下拉框
					if(n=="1"){
						iKeyCode = "";
						$('#list').datagrid("unselectRow", lastIndex);
					}else if(iKeyCode!=""){
						checkTeaCls(jsxm,skzcxq,weeks1,cdyq,iKeyCode,xnxqVal1+jxxzVal1,classId1,classdm,jsbh);
						queryjinpai(xnxqVal1,jxxzVal1,iKeyCode);
					}
				}
			});
		}
		
		//检查当前时间是否超过允许排课时间
		function checkpaike(){ 
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_Skjh',
				data : 'active=checkpaike&GS_XNXQBM=' + (savexqbh1+savexqbh2),
				async : false,
				dataType:"json",
				success : function(data) {
					paike=data[0].MSG;
				}
			});
		}
		
		function loadGrid(listData){
			$('#list').datagrid({
				data:listData,
				loadMsg : "信息加载中请稍后!",//载入时信息
				rownumbers: true,
				animate:true,
				striped : true,//隔行变色
				pageSize : pageSize,//每页记录数
				singleSelect : true,//单选模式
				pageNumber : pageNum,//当前页码
				pagination:false,
				fit:true,
				fitColumns: true,//设置边距
				columns:[[
					{field:'课程名称',title:'科目名称',width:100},
					{field:'行政班名称',title:'班级名称',width:100},
					{field:'授课教师姓名',title:'任课教师',width:100,
						formatter:function(value,rec){
							var skzcxq2=rec.授课教师姓名;
							skzcxq2=skzcxq2.replace(new RegExp('@','gm'),'&');
							return skzcxq2;
						}
					},
					{field:'固排已排节数',title:'已排节数',width:50},
					{field:'未排节数',title:'未排节数',width:50},
					{field:'授课周次详情',title:'授课周次',width:50,
						formatter:function(value,rec){
							var skzcxq2=rec.授课周次详情;
							skzcxq2=skzcxq2.replace(new RegExp('odd','gm'),'单周');
							skzcxq2=skzcxq2.replace(new RegExp('even','gm'),'双周');
							skzcxq2=skzcxq2.replace(new RegExp('@','gm'),'&');
							return skzcxq2;
						}
					}
				]],
				onClickRow:function(rowIndex, rowData){
					lastIndex = rowIndex;
					iKeyCode = rowData.授课计划明细编号;
					//iKeyCodeZb = rowData.授课计划主表编号;
					kcmc = rowData.课程名称;
					jsbh = rowData.授课教师编号;
					jsxm = rowData.授课教师姓名;
					classdm=rowData.行政班代码;
					classmc=rowData.行政班名称;
					lj = rowData.连节;
					lc = rowData.连次;
					cdyq=rowData.场地要求;
					cdmc=rowData.场地名称;
					skzcxq = rowData.授课周次详情;
					cdyq=cdyq.replace(new RegExp('@','gm'),'&');
					cdmc=cdmc.replace(new RegExp('@','gm'),'&');
					skzcxq=skzcxq.replace(new RegExp('@','gm'),'&');

					//row=rowData;
					classId1 = window.parent.document.frames["left"].classId;//实际是场地编号
					xnxqVal1 = window.parent.document.frames["left"].xnxqVal;
					jxxzVal1 = window.parent.document.frames["left"].jxxzVal;
					weeks1 = window.parent.document.frames["left"].weeks;	
					queryjinpai(xnxqVal1,jxxzVal1,iKeyCode);
					checkTeaCls(jsxm,skzcxq,weeks1,cdyq,iKeyCode,xnxqVal1+jxxzVal1,classId1,classdm,jsbh);
					
				},
				onLoadSuccess: function(data){
					//parentId=window.parent.document.frames["left"].parentId;
					//if(parentId==""){
					//	$('#new').linkbutton('disable');
					//	$('#edit').linkbutton('disable');
					//}else{
					//	$('#new').linkbutton('enable');
					//	$('#edit').linkbutton('enable');
					//}
					//iKeyCode="";
					$('#list').datagrid("selectRow", lastIndex);
					if(data.total == 0){
						$('#save').linkbutton('disable');
					}else{
						$('#save').linkbutton('enable');
					}
					$('#divPageMask2').hide();
				},
				onLoadError:function(none){
					
				}
			});
			
			$("#list").datagrid("getPager").pagination({ 
				total:listData.total, 
				onSelectPage:function (pageNo, pageSize_1) { 
					pageNum = pageNo;
					pageSize = pageSize_1;
					loadData();
				} 
			});
		}
		
		//查询是否禁排	
		function queryjinpai(xnxq,jxxz,skjh){			
			$.ajax({
					type:'post',
					url:"<%=request.getContextPath()%>/Svl_Jsgpjp",
					data:"active=loadjinpai&GG_XNXQBM="+(xnxq+jxxz)+"&GG_SKJHMXBH="+skjh,
					dataType:'json',
					asy:false,
					success:function(data){
						if(data[0].loadjinpai.length==0){
							for(var i=0;i<data[0].loadallsjxl.length;i++){
								if($('#'+data[0].loadallsjxl[i].时间序列).html()=='禁排'){
									$('#'+data[0].loadallsjxl[i].时间序列).html('');
								}		
							}
						}else{
							for(var i=0;i<data[0].loadjinpai.length;i++){
								$('#'+data[0].loadjinpai[i].时间序列).html('禁排');
							}
						}
					}
			});
		}
		
		//检查可以排的格子
		function checkTeaCls(jsxm,skzcxq,weeks1,cdyq,iKeyCode,termid,cdyqId,classdm,jsbh){		
					$.ajax({
						type:'post',
						url:"<%=request.getContextPath()%>/Svl_Cdgpjp",
						data:"active=checkTeaCls&jsxm="+encodeURIComponent(jsxm)+"&skzcxq="+encodeURIComponent(skzcxq)+"&weeks="+encodeURIComponent(weeks1)+"&cdyq="+encodeURIComponent(cdyq)+"&iKeyCode="+iKeyCode+"&termid="+termid+"&cdyqId="+cdyqId+"&classdm="+classdm+"&jsbh="+jsbh,
						dataType:'json',
						asy:false,
						success:function(datas){
							//alert(datas[0].msg);
							var times=datas[0].msg;
							var time=times.split(",");
							var tab=document.getElementById("content");
				    		var rows=tab.rows;
				    		for(var i=1;i<rows.length;i++){
					        	for(var j=1;j<rows[i].cells.length;j++){
					        		$('#' + rows[i].cells[j].id).css('background-color', '#E7FECB');	
					        	}
					        }
							for(var i=0;i<time.length;i++){
								$('#' + time[i]).css('background-color', '#FFFFFF');	
							}
// 							for(var i=1;i<6;i++){
// 								for(var j=1;j<rows.length;j++){
// 									if(rows[j].cells[0].innerHTML.substring(0,2)=="晚上"){
// 										if(j<10){
// 											$('#0'+i+'0'+j).css('background-color', '#FFFFFF');
// 										}else{
// 											$('#0'+i+j).css('background-color', '#FFFFFF');
// 										}
// 									}
// 								}
// 							}
							$('#divPageMask2').hide();
						}
					});				
		}
		
		function loadTime(timeData,gpjpData){
			var className1 = window.parent.document.frames["left"].className;
			var count = 1;
			var idNum = "";
			if(timeData != ""){
				$('#setTime').show();
				var html='';
				html += '<tr align="center" style="height:20px;">'+
							'<th>'+
								className1 +
							'</th>'+
							'<th>'+
								'星期一' +
							'</th>'+
							'<th>'+
								'星期二' +
							'</th>'+
							'<th>'+
								'星期三' +
							'</th>'+
							'<th>'+
								'星期四' +
							'</th>'+
							'<th>'+
								'星期五' +
							'</th>'+
						'</tr>';
				
				for(var i=0;i<timeData.length;i++){
					idNum = count+i;
					if(idNum < 10){
						idNum = ("0"+idNum);
					}
					html += '<tr align="center" style="height:45px;">'+
							'<th class="jc">'+
								timeData[i].节次 +
							'</th>'+
							'<td id="01'+idNum+'" class="pkxx" title="">'+
							'</td>'+
							'<td id="02'+idNum+'" class="pkxx" title="">'+
							'</td>'+
							'<td id="03'+idNum+'" class="pkxx" title="">'+
							'</td>'+
							'<td id="04'+idNum+'" class="pkxx" title="">'+
							'</td>'+
							'<td id="05'+idNum+'" class="pkxx" title="">'+
							'</td>'+
						'</tr>';
				}
				$('#content').html(html);
				$.parser.parse(('#content'));
				if(gpjpData.length > 0){
					for(var j=0;j<gpjpData.length;j++){
						if(gpjpData[j].类型==2){
							$('#'+gpjpData[j].时间序列+'').html(gpjpData[j].课程名称+"<br />"+gpjpData[j].行政班名称);
							var pkxx=gpjpData[j].课程名称;
							var bjxx=gpjpData[j].行政班名称;
							var cdxx=gpjpData[j].预设场地名称;
							pkxx=pkxx.replace(new RegExp('&amp;','gm'),'&');
							bjxx=bjxx.replace(new RegExp('&amp;','gm'),'&');
							cdxx=cdxx.replace(new RegExp('&amp;','gm'),'&');
							$('#'+gpjpData[j].时间序列+'').attr('title',(pkxx+"\r\n"+bjxx+"\r\n"+cdxx+"\r\n"+gpjpData[j].授课周次));
						}
						
						//if(gpjpData[j].类型==3){
						//	$('#'+gpjpData[j].时间序列+'').html('禁排');
						//}
					}
				}else{
					
				}
				editTable();
			}else{
				$('#setTime').hide();
			}
		}
		
		function editTable(){
			$("#content td").click(function(){ 
				if(paike==1){
					return;
				}
				var idVal = $(this).attr("id");
				var fir = idVal.substr(0, 2);
				var sec = idVal.substr(2, 1);
				var thi = idVal.substr(3, 1);
				var kcjsn=$('#'+idVal).html();
				
				if(iKeyCode==""){
					alertMsg("请先选择上方列表的“科目名称”以及“任课教师”");
					return;
				}
							
				//alert($('#'+idVal).attr("id"));
				kcjsn=kcjsn.replace(new RegExp('&amp;','gm'),'&');
				//检查可以是添加还是删除，及可以排的课数量
				$.ajax({
					type:'post',
					url:"<%=request.getContextPath()%>/Svl_Cdgpjp",
					data:"active=checkRec&SKJHMXBH="+iKeyCode+"&idVal="+idVal,
					dataType:'json',
					asy:false,
					success:function(datas){
						checkrec=datas[0].msg;
						lcs=datas[0].lcs;
						aod=datas[0].aod;
						if((kcmc+classmc)==""){
							alertMsg("请先选择上方列表的“科目名称”以及“任课教师”");
							return
						}else{
							//alert($('#'+idVal).html()+"|"+(kcmc+jsxm));
							if(jsxm=="请选择"){
								alertMsg("教师不能为请选择，请修改授课计划！");
								return;
							}
							var idValcon="";
							if(aod==1){//删除
								delinfo = $('#'+idVal).html();
								idValcon="'"+idVal+"'";
								delRec(idValcon);
								if(checkrec!="minus"){
									$('#'+idVal).html('');
								}
							}else{//添加
								if($('#'+idVal).css("background-color")!="#ffffff"&&$('#'+idVal).css("background-color")!="rgb(255, 255, 255)"&&$('#'+idVal).css("background-color")!="transparent"){//背景不是白色
																								
									if(lj>1&&((lc-lcs)>0||lc==0)){
										gpjs=0;
										var idValcon="";
										if(checkrec!="no"){
											var kpjs=parseInt(checkrec);//可以排的节数
											if(kpjs>lj){
												kpjs=lj;
											}
											
											for(var i=0;i<kpjs;i++){
													if(1==2){
														//$('#'+fir+(parseInt(thi)+i)+'').html(kcmc+jsxm);
														for(var i=0;i<kpjs;i++){
															$('#'+fir+sec+(parseInt(thi)+i)+'').html("");
														}
														gpjs=0;
														idValcon="";
														break;
													}else{
														if((parseInt(thi)+i)>9){
															if($('#'+fir+(parseInt(thi)+i)+'').css("background-color")!="#ffffff"&&$('#'+idVal).css("background-color")!="rgb(255, 255, 255)"&&$('#'+idVal).css("background-color")!="transparent"){
															
																idValcon+="'"+fir+(parseInt(thi)+i)+"',";
																gpjs++;
															}else{
																
																gpjs=0;
																idValcon="";
																break;
															}
														}else{
															if($('#'+fir+sec+(parseInt(thi)+i)+'').css("background-color")!="#ffffff"&&$('#'+idVal).css("background-color")!="rgb(255, 255, 255)"&&$('#'+idVal).css("background-color")!="transparent"){
															
																idValcon+="'"+fir+sec+(parseInt(thi)+i)+"',";
																gpjs++;
															}else{
																
																gpjs=0;
																idValcon="";
																break;
															}
														}
													}				
											}
											idValcon=idValcon.substring(0, idValcon.length-1);
										}
										if(gpjs==0){
										
										}else{
											for(var i=0;i<kpjs;i++){
												if((parseInt(thi)+i)>9){
													$('#'+fir+(parseInt(thi)+i)+'').html(kcmc+jsxm);
												}else{
													$('#'+fir+sec+(parseInt(thi)+i)+'').html(kcmc+jsxm);
												}
											}
											//addRec(gpjs,idValcon,kcmc+classmc);
											savepb(idValcon,"add");
											idValconTrans=idValcon;
										}								
									}else{	
										if(1==2){
												
										}else{
												if(checkrec!="no"){
													$('#'+idVal).html(kcmc+classmc);	
													//$('#'+idVal).attr('title','aa'); 	
												}	
												idValcon="'"+idVal+"'";
												//addRec("1",idValcon,kcmc+classmc);
												gpjs=1;
												savepb(idValcon,"add");
												idValconTrans=idValcon;
										}
												
									}
								}
							}	
						}				
					}
				});	
			}); 
		}
		
		window.onload = function(){
			//去掉默认的contextmenu事件，否则会和右键事件同时出现。
			document.oncontextmenu = function(e){
				return false;
			};
			document.getElementById("content").onmousedown = function(){
				if(paike==1){
					return;
				}
				
				if(iKeyCode==""){
					return;
				}
				
				if(event.button ==2){
					var className1 = window.parent.document.frames["left"].className;
					var td = event.srcElement;
					var td1 = ($(td).html()).substr(0, 2);
					if($(td).html()=="星期一"||$(td).html()=="星期二"||$(td).html()=="星期三"||$(td).html()=="星期四"||$(td).html()=="星期五"||$(td).html()==className1||td1=="上午"||td1=="下午"||td1=="晚上"){
						return;
					}else{
						var idValcon="'"+td.id+"'";
						if($(td).html()==""){
							$(td).html("禁排");
							savepb(idValcon,"addjin");
						}else{
							if($(td).html()=="禁排"){
								$(td).html('');
								savepb(idValcon,"deljin");
							}else{
								delinfo = $(td).html();
								delRec(idValcon);
								$(td).html('');
							}
						}
					}
				}
			};
		};
		
		function addRec(gpjs,idValcon,kcjs){
			$.ajax({
				type:'post',
				url:"<%=request.getContextPath()%>/Svl_Cdgpjp",
				data:"active=add&SKJHMXBH="+iKeyCode+"&gpjs="+gpjs,
				dataType:'json',
				asy:false,
				success:function(datas){
// 					if(datas[0].msg=="1"){
// 						savepb(idValcon,"add");
// 					}
				}
			});
		}
		
		function delRec(idValcon){
			xnxqVal1=window.parent.document.frames["left"].xnxqVal;
			jxxzVal1=window.parent.document.frames["left"].jxxzVal;
			classId1=window.parent.document.frames["left"].classId;
			delinfo=delinfo.replace(new RegExp('&amp;','gm'),'&');
			var url="active=del&QCXX=" + encodeURIComponent(delinfo) + '&XNXQ=' + xnxqVal1 + '&JXXZ=' + jxxzVal1 + '&XZBDM=' + classId1 + '&SKJHMXBH=' + iKeyCode + '&idValcon='+idValcon;
			$.ajax({
				type:'post',
				url:"<%=request.getContextPath()%>/Svl_Cdgpjp",
				data:url,//"active=del&QCXX=" + delinfo + '&XNXQ=' + xnxqVal1 + '&JXXZ=' + jxxzVal1 + '&XZBDM=' + classId1 + '&SKJHMXBH=' + iKeyCode,
				dataType:'json',
				asy:false,
				success:function(datas){
					if(datas[0].msg=="1"){
						savepb(idValcon,"del");
					}
				}
			});
		}
		
		//工具按钮
		function doToolbar(iToolbar){
			if(iToolbar == "import"){
				$('#xxkImport').dialog('open');
			}
			if(iToolbar == "savexxk"){
				
			}
		}
		
		function savepb(idValcon,savetype){
			xnxqVal1=window.parent.document.frames["left"].xnxqVal;
			jxxzVal1=window.parent.document.frames["left"].jxxzVal;
			classId1=window.parent.document.frames["left"].classId;
			className=window.parent.document.frames["left"].className;
			
			$("#active").val("save");
			$('#GG_XZBDM').val(classdm);
			$('#JSBH').val(classId1);
			$('#JSXM').val(className);
			$('#GG_XNXQBM').val(xnxqVal1+jxxzVal1);
			$('#GG_SKJHMXBH').val(iKeyCode);
			$('#divPageMask2').show();
			
			//取所有content值，保存不为空的
		    var tab=document.getElementById("content");
    		var rows=tab.rows;
    		var b="";
    		for(var i=1;i<rows.length;i++){
	        	for(var j=1;j<rows[i].cells.length;j++){
	        		if((rows[i].cells[j].innerHTML)!=""){
	        			b+=rows[i].cells[j].id+",";
	        		}
	        	}
	        }
	        b=b.substring(0,b.length-1);
		    $('#GG_SJXL').val(b);
		    
		    $('#SJXL').val(idValcon);
		    $('#CDYQ').val(cdyq);
		    $('#CDMC').val(cdmc);
		    $('#SAVETYPE').val(savetype);
		    
		    var c = $('.pkxx');
		    var d="";
			for(var i=0;i<c.length;i++){
				d+=$(c[i]).html()+",";
		    } 
		    d=d.substring(0,d.length-1);
		  	$('#KCJS').val(d);
		  	
			$('#fm').submit();
		}
		
		//fm提交事件
		$('#fm').form({
			url:'<%=request.getContextPath()%>/Svl_Cdgpjp',
			onSubmit:function(){
				
			},
			//提交成功
			success:function(datas){
				if($('#active').val()=="save"){
					var json = eval("("+datas+")");
					if(json[0].msg=="没有可以使用的教室"){
						alertMsg(json[0].msg);
					}else{
						if($('#SAVETYPE').val()=="add"){
							addRec(idValconTrans);
						}
					}
					//showMsg(json[0].msg);
					initGridData(classId1,className,savexqbh1,savexqbh2,"2");
				}
			}
		});
		
		function initDialog(){
			$('#xxkImport').dialog({   
				title: '选修课导入',
				width: 350,//宽度设置   
				height: 170,//高度设置 
				modal:true,
				closed: true,   
				cache: false, 
				draggable:false,//是否可移动dialog框设置
				toolbar:[{
					//保存编辑
					text:'保存',
					iconCls:'icon-save',
					handler:function(){
						//传入save值进入doToolbar方法，用于保存
						doToolbar('savexxk');
					}
				}],
				//打开事件
				onOpen:function(data){},
				//读取事件
				onLoad:function(data){},
				//关闭事件
				onClose:function(data){
					emptyDialog();
				}
			});
		}
	</script>
</html>