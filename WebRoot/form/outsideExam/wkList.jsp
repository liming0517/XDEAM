<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%
	/**
		创建人：wangzh
		Create date: 2015.06.02
		功能说明：用于设置授课计划
		页面类型:列表及模块入口
		修订信息(有多次时,可有多个)
		修订日期: 2015.07.21
		原因:
		修订人: lupengfei
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
<%@page import="java.net.URLEncoder" %>
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
		sAuth = MyTools.StrFiltr(v.get(0));
		// 如果无法获取人员信息，则自动跳转到登陆界面
%>
<html>
	<head>
		<title>开班设置</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/icon.css">
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/publicScript.js"></script>
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/form/css/uploadify.css">
		<script type="text/javascript" src="<%=request.getContextPath()%>/form/File/jquery.uploadify-v2.1.4/swfobject.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/form/File/jquery.uploadify-v2.1.4/jquery.uploadify.v2.1.4.min.js"></script>
		<style type="text/css">
			#divPageMask2{background-color:#D2E0F2; filter:alpha(opacity=90);left:0px;top:0px;z-index:100;}
			#divPageMask3{background-color:#D2E0F2; filter:alpha(opacity=90);left:0px;top:0px;z-index:100;}
			#win td{height:40px;}
			
			#maskFont{
				 font-size:16px;
				 color:#2B2B2B;
				 width:200px;
				 height:100%;
				 position:absolute;
				 top:50%;
				 left:50%;
				 margin-top:-10px;
				 margin-left:-150px;
				 font-weight:bold;
			}
		</style>
	</head>
	<body class="easyui-layout" >
		<%-- 遮罩层 --%>
    	<div id="divPageMask2" style="position:absolute;top:0px;left:0px;width:100%;height:100%;">
    		<div id="maskFont">文件上传中，请稍后...</div>
    	</div>		
		
		<div data-options="region:'north'" title="" style="background:#fafafa;height:64px;">
			<table  class = "" width = "100%">
				<tr>
					<td>
						<a id="newOutsideExam"  onclick="doToolbar(this.id)" class="easyui-linkbutton" href="javascript:void(0);" data-options="plain:true,iconCls:'icon-new'">新建</a>
						<a id="editOutsideExam" onclick="doToolbar(this.id)" class="easyui-linkbutton" href="javascript:void(0);" data-options="plain:true,iconCls:'icon-edit'">编辑</a>
						<a id="delOutsideExam" onclick="doToolbar(this.id)" class="easyui-linkbutton" href="javascript:void(0);" data-options="plain:true,iconCls:'icon-cancel'">删除</a>
						<a id="uploadAllstuPhoto" onclick="doToolbar(this.id)" class="easyui-linkbutton" href="javascript:void(0);" data-options="plain:true,iconCls:'icon-site'">上传学生照片</a>
					</td>
				</tr>
			</table>
			<table  class = "tablestyle" width = "100%">
				<tr>
					<td class="titlestyle" width="20%" >外考科目名称</td>
					<td >
						<input id="SC_KSMC" name="SC_KSMC" class="easyui-textbox" style="width:200px;"/>
					</td>
					<td align="center" style="width:12%">
						<a href="#" class="easyui-linkbutton" id="searchSC" name="searchSC" iconCls="icon-search" plain="true" onClick="doToolbar(this.id)">查询</a>
					</td>	
				</tr>
			</table>
		</div>
		<div data-options="region:'center'">
			<table id='list' width="100%" >
			</table>
		</div>
		
		<div id="win" title="编辑" style="width:100%;height:100%;overflow-x:hidden;">
			<div class="easyui-layout" style="width:100%; height:100%;">
			<form id='fm' method='post' style="margin:0px;">
				<table  class = "tablestyle" width = "100%">
					<tr>
						<td class="titlestyle" width="20%">外考科目名称</td>
						<td width="30%">
							<input name="WK_KSMC" id="WK_KSMC" class="easyui-textbox" style="width:250px;" required="true"/>
						</td>
						
					</tr>
					<tr>
						<td class="titlestyle" >报名开始时间</td>
						<td >
							<input id="WK_BMKSSJ" name="WK_BMKSSJ" class="easyui-datebox" editable="false" required="true" style="width:250px;" />
						</td>
					</tr>
					<tr>
						<td class="titlestyle" >报名结束时间</td>
						<td >
							<input name="WK_BMJSSJ" id="WK_BMJSSJ" class="easyui-datebox" editable="false" required="true" style="width:250px;" />	
						</td>
					</tr>
					<tr>
						<td class="titlestyle" >考试日期</td>
						<td >
							<input name="WK_KSRQ" id="WK_KSRQ" class="easyui-datebox" editable="false" required="true" style="width:250px;" />
						</td>
					</tr>
					<tr>
						<td class="titlestyle" >报名费用</td>
						<td >
							<input name="WK_BMFY" id="WK_BMFY" class="easyui-numberspinner" style="width:250px;" min="0" precision="2" editable="true" style="width:250px;">
						</td>
					</tr>
					<tr >
						<td class="titlestyle">备注</td>
						<td colspan=3>
							<textarea name="WK_BEIZHU" id="WK_BEIZHU" style="width:370px;height:87px;" onClick="">
							</textarea>
						</td>
					</tr>
				</table>
				
				<!-- 隐藏属性,传参用 -->
				<input type="hidden" id="active" name="active" />
				<input type="hidden" id="iUSERCODE" name="iUSERCODE" />
				<input type="hidden" id="WK_KSBH" name="WK_KSBH" />
				<input type="hidden" id="WK_BZ" name="WK_BZ" />
			</form>
			</div>
		</div>
		
		<div id="tsc" title="编辑" style="overflow-x:hidden;">
			<form id='fm2' method='post' style="margin: 0px;">
				<table id="tsctable" class = "tablestyle" width="566px">
														
				</table>
				<!-- 隐藏属性,传参用 -->
<!-- 				<input type="hidden" id="active" name="active" /> -->

			</form>
		</div>
		
		<div id="teacher" title="编辑" style="">
			<div style="height:30px;">
				<table width="566px" class="tablestyle">
				<tr>
					<a href="#" class="easyui-linkbutton" id="submit3" name="submit3" iconCls="icon-submit" plain="true" onClick="doToolbar(this.id)">确定</a>
				</tr>
				<tr>
					<td width="12%" class="titlestyle">教师工号</td>
					<td  width="16%">
						<input style='width:100%' class='easyui-validate' id='ic_teaId' name='ic_teaId'/>
					</td>
					<td width="12%" class="titlestyle">教师姓名</td>
					<td  width="16%">
						<input style='width:100%' class='easyui-validate' id='ic_teaName' name='ic_teaName'/>
					</td>	
					<td width="12%" class="titlestyle">教师类别</td>
					<td width="16%"><select name="ic_teaLevel" id="ic_teaLevel" class="easyui-combobox" style="width:100%" panelHeight="auto">
					</select></td>							
					<td align="center" style='width:12%'>
						<a href="#" class="easyui-linkbutton" id="search" name="search" iconCls="icon-search" plain="true" onClick="doToolbar(this.id)">查询</a>
					</td>					
				</tr>
				</table>
			</div>
			<div style="">
				<form id='fm3' method='post' style="margin: 0px;">
					<table id="teatable" class = "tablestyle" width="466px">
					</table>
				</form>
			</div>
		</div>
		
		<div id="week" title="编辑">
			<form id='fm4' method='post' style="margin: 0px;">
				<table id="weektable" class = "tablestyle" width="286px">
					<tr>
						<a href="#" class="easyui-linkbutton" id="submit4" name="submit4" iconCls="icon-submit" plain="true" onClick="doToolbar(this.id)">确定</a>
					</tr>
					
					<tr>
						<td width="33%" align="center"><input type="checkbox" id="custom" name="" onclick="weekcheck1();" /> 自定义</td>
						<td width="33%" align="center"><input type="checkbox" id="singleweek" name="" onclick="weekcheck2();" /> 单周</td>
						<td width="33%" align="center"><input type="checkbox" id="doubleweek" name="" onclick="weekcheck3();" /> 双周</td>
					</tr>
					
					<tr>
						<td width="33%" align="center" id="weekn1" style="cursor:pointer;" onmouseover="weekmouseover(this.id);" onmouseout="weekmouseout(this.id);" onclick="chooseweek(this.id);">1</td>
						<td width="33%" align="center" id="weekn2" style="cursor:pointer;" onmouseover="weekmouseover(this.id);" onmouseout="weekmouseout(this.id);" onclick="chooseweek(this.id);">2</td>
						<td width="33%" align="center" id="weekn3" style="cursor:pointer;" onmouseover="weekmouseover(this.id);" onmouseout="weekmouseout(this.id);" onclick="chooseweek(this.id);">3</td>
					</tr>
					
				</table>
				<!-- 隐藏属性,传参用 -->
<!-- 				<input type="hidden" id="active" name="active" /> -->

			</form>
		</div>
		
		<div id="room" title="编辑">
			<div >
				<table id="roomtable" class = "tablestyle" width="566px" >
					<tr>
						<a href="#" class="easyui-linkbutton" id="submit5" name="submit5" iconCls="icon-submit" plain="true" onClick="doToolbar(this.id)">确定</a>
					</tr>
					
					<tr>
						<td width="25%" align="center"><input type="checkbox" id="chooseroom" name="" onclick="chooseRoom();" /> 指定教室</td>
						<td width="25%"></td><td width="25%"></td><td width="25%"></td>
					</tr>
					<tr>
						<td width="25%" align="center">校区</td>
						<td width="25%" align="center"><select id="school" name="" class="easyui-combobox" style="width:90%" panelHeight="auto"></select></td>
						<td width="25%" align="center">教学楼</td>
						<td width="25%" align="center"><select id="house" name="" class="easyui-combobox" style="width:90%" panelHeight="auto"></select></td>
					</tr>
					<tr>
						<td width="25%" align="center">教室类型</td>
						<td width="25%" align="center"><select id="clstype" name="clstype" class="easyui-combobox" style="width:90%" panelHeight="auto"></select></td>
						
					</tr>
				</table>
			</div>
			<div >
					
				<form id='fm5' method='post' style="margin: 0px;">
					
					<table id="clstable" class = "tablestyle" width="686px">
										
					</table>
				</form>
			</div>
		</div>
		
		<!-- 专业 -->
		<div id="ic_zymc" style="width:786px;height:91px;float:left;" >
			<div style="width:100%;height:464px;">
				<div style="border-left:1px solid #95B8E7;" >
				<table >
					<tr>
						<td ><a href="#" id="addMajor" class="easyui-linkbutton" plain="true" iconcls="icon-submit" onClick="doToolbar(this.id);" >确定</a></td>	
					</tr>
				</table>
				<table  class = "tablestyle" style="width:99.9%;">
					<tr>
						<td class="titlestyle" style="width:16%">专业代码</td>
						<td style="width:25%">
							<input id="XX_ZYDM" name="XX_ZYDM" class="easyui-textbox" style="width:150px;"/>
						</td>
						<td class="titlestyle" style="width:16%">专业名称</td>
						<td style="width:25%">
							<input id="XX_ZYMC" name="XX_ZYMC" class="easyui-textbox" style="width:150px;"/>
						</td>
						<td align="center" style="width:16%">
							<a href="#" class="easyui-linkbutton" id="searchMAJOR" name="searchMAJOR" iconCls="icon-search" plain="true" onClick="doToolbar(this.id)">查询</a>
						</td>	
					</tr>
				</table>
				</div>
				<div style="width:100%;height:402px;">
					<table id="majorList" style="width:100%;"></table>
				</div>
			</div>
		</div>
		
		
			<div id="stuPhoto" title="" style="" align="center">
				<div class="easyui-layout" style="width:100%; height:100%;">
				<form id="form1" name="form1" method="POST" action="<%=request.getContextPath()%>/Svl_Import" enctype="multipart/form-data">
					<table  class="tablestyle">
						<tr>
							<td align="left">
								<a href="#"	onclick="doToolbar(this.id)" id="uploadRar" class="easyui-linkbutton" plain="true" iconcls="icon-save" style="display:inline-block;  position:relative; overflow:hidden;" >上传</a>
							</td>
						</tr>
						<tr>
							<td align="center">
						      	<input type="file" name="browse" id="browse" width="100%" style="" />
							</td>
						</tr>
					</table>	
					<input type="hidden" id="path" name="path" />	
				</form>	
				</div>		
			</div>
		
		
	</body>
	<script type="text/javascript">

		var iUSERCODE="<%=MyTools.getSessionUserCode(request)%>";   //获得用户登录code
		var sAuth="<%=sAuth%>";  //角色权限
		var iKeyCode = "";//授课计划明细编号
		var pageNum = 1;   //datagrid初始当前页数
		var pageSize = 20; //datagrid初始页内行数
		var xkmc = "";//学科名称下拉框数据
		var rkjs = "";//任课教师下拉框数据
		var ksxs = "";//考试形式下拉框数据
		var kcmc = "";//课程名称
		var jsxm = "";//教师姓名
		//var classId1 = window.parent.document.frames["left"].classId;
		//var xqbh1 = window.parent.document.frames["left"].xqbh;
		//var weeks1 = window.parent.document.frames["left"].weeks;
		var weeks1="";
		var xnxqVal1 = "";
		var jxxzVal1 = "";
		var skjsbh = "";
		var xkdm = "";
		var lastIndex = -1;//使datagrid选中行取消，以便下次选择从新开始
		var saveType = "";//判断打开窗口的操作（new or edit）
		var roomnum=0;//校区建筑物数量
		var tscnum=0;//授课信息计数
		var weeksel=0;//选择周次模式
		var teainfoidarray =new Array();//存放选择的任课教师编号
		var teainfoarray =new Array();//存放选择的任课教师姓名
		//var weekArray = new Array(weeks1);//保存当前窗口第几周是否被选中
		//var weekAllArray = new Array(weeks1);//保存所有窗口周次是否已经被选择
		var clsinfoidarray =new Array();//存放选择的场地编号
		var clsinfoarray =new Array();//存放选择的场地名称
		//var linkArray = new Array(weeks1);//判断周次是否连续
		var inputid="";//点击开的input框id
		var teaidarray =new Array();//存放教师编号
		var teaarray =new Array();//存放教师
		var wekidarray =new Array();//存放周次编号
		var wekarray =new Array();//存放周次
		var clsidarray =new Array();//存放场地编号
		var clsarray =new Array();//存放场地
		var roomsel=0;//选择教室模式
		var courseid="";//学科编号
		var coursename="";//学科名称
		var teacherbh="";//授课教师编号
		var teacherxm="";//授课教师姓名
		var teachweek="";//授课周次
		var teachinfo="";//授课周次详情
		var spaceyq="";//场地要求
		var spacemc="";//场地名称
		var kclx="";//课程类型
		var savexqbh="";//保存传递过来的学期编码
		var paike="0";//排课状态
		var majorbh="";//可报名专业编号
		var skjhzbbh="";//授课计划主表编号
		var ostype=0;//浏览器类型
		
		$(document).ready(function(){
			loadGrid();
			loadDialog();
			initGridData();//页面初始化加载数据
			$('#tsc').hide();
			$('#teacher').hide();
			$('#week').hide();
			$('#room').hide();
			$('#ic_zymc').hide();
			//checkChoose();			
			initCombobox();
			$('#divPageMask2').hide();
		});
				
		//页面初始化加载数据
		function initGridData(){ 
			
			teainfoidarray.splice(0,clsinfoidarray.length);
			teainfoarray.splice(0,clsinfoarray.length);
			clsinfoidarray.splice(0,clsinfoidarray.length);
			clsinfoarray.splice(0,clsinfoarray.length);
						
			//true未打开,false打开
			if($("#win").parent().is(":hidden")){
				
			}else{	
				if($("#tsc").parent().is(":hidden")){
					
				}else{
					if($("#teacher").parent().is(":hidden")){
				
					}else{
						$('#teacher').dialog("close");			
					}
					if($("#week").parent().is(":hidden")){
				
					}else{
						$('#week').dialog("close");			
					}
					if($("#room").parent().is(":hidden")){
				
					}else{
						$('#room').dialog("close");			
					}
					$('#tsc').dialog("close");			
				}	
				$('#win').dialog("close");	
			}
				
			var ex=getOs();
			if(ex=="MSIE"){ 
				if(navigator.appName == "Microsoft Internet Explorer" && navigator.appVersion.match(/7./i)=="7."){ 
					ostype=1;
				}else if(navigator.appName == "Microsoft Internet Explorer" && navigator.appVersion.match(/8./i)=="8."){ 
					ostype=1;
				}else if(navigator.appName == "Microsoft Internet Explorer" && navigator.appVersion.match(/6./i)=="6."){ 
					ostype=1;
				}else{
					ostype=2;
				} 
			}else if(!!window.ActiveXObject || "ActiveXObject" in window){
				ostype=3;
			}else{
				ostype=2;
			}			
			loadEx();
		}
		
		//判断浏览器类型
		function getOs(){  
			   if(navigator.userAgent.indexOf("MSIE")>0) {  
			        return "MSIE";  
			   }  
			   if(isFirefox=navigator.userAgent.indexOf("Firefox")>0){  
				    $('#squarediv').css('height','33');
				    $('#navigation').css('width','95.6%');
				    $('#line').css('margin-top','0');
				    $('#tt').css('margin-top','0');
				    return "Firefox";  
			   }  
			   if(isSafari=navigator.userAgent.indexOf("Safari")>0) {  
			        return "Safari";  
			   }   
			   if(isCamino=navigator.userAgent.indexOf("Camino")>0){  
			        return "Camino";  
			   }  
			   if(isMozilla=navigator.userAgent.indexOf("Gecko/")>0){  
			        return "Gecko";  
			   }      
		}
		
		//检查当前时间是否超过允许排课时间
		function checkpaike(){ 
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_Skjh',
				data : 'active=checkpaike&GS_XNXQBM=' + savexqbh,
				async : false,
				dataType:"json",
				success : function(data) {
					paike=data[0].MSG;
				}
			});
		}
		
		
		function loadGrid(){
			$('#list').datagrid({
				url: '<%=request.getContextPath()%>/Svl_outsideExam?active=loadOutsideExam&SC_KSMC='+encodeURI(encodeURI($('#SC_KSMC').textbox('getValue'))),
				loadMsg : "信息加载中请稍后!",//载入时信息
				width : '100%',//宽度
				nowrap:false,
				showFooter:true,
				rownumbers: true,
				animate:true,
				striped : true,//隔行变色
				pageSize : pageSize,//每页记录数
				singleSelect : true,//单选模式
				pageNumber : pageNum,//当前页码
				pagination:true,
				fit:true,
				fitColumns: true,//设置边距
				columns:[[
					{field:'WK_KSBH',title:'外考考试编号',width:fillsize(0.15),align:'center'},
					{field:'WK_KSMC',title:'外考考试名称',width:fillsize(0.2),align:'center'},
					{field:'WK_BMKSSJ',title:'报名开始时间',width:fillsize(0.1),align:'center'},
					{field:'WK_BMJSSJ',title:'报名结束时间',width:fillsize(0.1),align:'center'},
					{field:'WK_KSRQ',title:'考试日期',width:fillsize(0.1),align:'center'},			
					{field:'WK_BMFY',title:'报名费用',width:fillsize(0.1),align:'center'},
					{field:'WK_BEIZHU',title:'备注',width:fillsize(0.3),align:'center'}
				]],
				onClickRow:function(rowIndex, rowData){
					row=rowData;
					iKeyCode=rowData.WK_KSBH;
					$('#WK_KSBH').val(iKeyCode);
				},
				onLoadSuccess: function(data){
					
				},
				onLoadError:function(none){
					
				}
			});
			
// 			$("#list").datagrid("getPager").pagination({ 
// 				total:listData.total, 
// 				onSelectPage:function (pageNo, pageSize_1) { 
// 					pageNum = pageNo;
// 					pageSize = pageSize_1;
// 					loadData();
// 				} 
// 			});
		}
		
		//加载下拉框数据
		function initCombobox(){		
			$('#SC_XNXQ').combobox({
				url:"<%=request.getContextPath()%>/Svl_CourseSet?active=XNXQElecCombobox",
				valueField:'comboValue',
				textField:'comboName',
				editable:false,
				panelHeight:'140', //combobox高度
				onLoadSuccess:function(data){
					//判断data参数是否为空
					if(data != ''){
						//初始化combobox时赋值
						$('#SC_XNXQ').combobox('setValue', data[0].comboValue);
						loadGrid();
					}
				},
				//下拉列表值改变事件
				onChange:function(data){}
			});
			
			$('#XX_KCDM').combobox({
				url:"<%=request.getContextPath()%>/Svl_CourseSet?active=XX_KCDMCombobox",
				valueField:'comboValue',
				textField:'comboName',
				editable:false,
				panelHeight:'140', //combobox高度
				onLoadSuccess:function(data){
					//判断data参数是否为空
					if(data != ''){
						//初始化combobox时赋值
						$(this).combobox('setValue', '');
					}
				},
				//下拉列表值改变事件
				onChange:function(data){ 
					
				}
			});
			
			$('#XX_KCLX').combobox({
				url:"<%=request.getContextPath()%>/Svl_CourseSet?active=XX_KCLXCombobox",
				valueField:'comboValue',
				textField:'comboName',
				editable:false,
				panelHeight:'140', //combobox高度
				onLoadSuccess:function(data){
					//判断data参数是否为空
					if(data != ''){
						//初始化combobox时赋值
						$(this).combobox('setValue', '');
					}
				},
				//下拉列表值改变事件
				onChange:function(data){
	
				}
			});
			
			$('#XX_XNXQ').combobox({
				url:"<%=request.getContextPath()%>/Svl_CourseSet?active=XX_XNXQCombobox",
				valueField:'comboValue',
				textField:'comboName',
				editable:false,
				panelHeight:'140', //combobox高度
				onLoadSuccess:function(data){
					//判断data参数是否为空
					if(data != ''){
						//初始化combobox时赋值
						$('#XX_XNXQ').combobox('setValue', data[0].comboValue);
					
					}
				},
				//下拉列表值改变事件
				onChange:function(data){
					$.ajax({
						type: "POST",
						url: '<%=request.getContextPath()%>/Svl_CourseSet',
						data: "active=getweeknum&XNXQBM=" + data ,
						dataType: 'json',
						success: function(data){
							weeks1=data[0].MSG;	
						}
					});
				}
			});
			
			$('#XX_SKRQ').combobox({
				url:"<%=request.getContextPath()%>/Svl_CourseSet?active=XX_SKRQCombobox",
				valueField:'comboValue',
				textField:'comboName',
				editable:false,
				panelHeight:'140', //combobox高度
				onLoadSuccess:function(data){
					//判断data参数是否为空
					if(data != ''){
						//初始化combobox时赋值
						$('#XX_SKRQ').combobox('setValue', data[0].comboValue);
						
					}
				},
				//下拉列表值改变事件
				onChange:function(data){}
			});
			
			$('#XX_SKSJ').combobox({
				url:"<%=request.getContextPath()%>/Svl_CourseSet?active=XX_SKSJCombobox",
				valueField:'comboValue',
				textField:'comboName',
				editable:false,
				multiple:true,
				panelHeight:'140', //combobox高度
				onLoadSuccess:function(data){
					//判断data参数是否为空
					if(data != ''){
						//初始化combobox时赋值
						$('#XX_SKSJ').combobox('setValue', data[0].comboValue);
					}
				},
				//下拉列表值改变事件
				onChange:function(data){}
			});
		}
		
		//工具按钮
		function doToolbar(iToolbar){
			if(iToolbar == "newOutsideExam"){
				saveType = 'newOutsideExam';
				$("#active").val(iToolbar);
				$('#win').dialog({   
					title: '新建外考科目'
				});
				$('#win').dialog("open");
				$.parser.parse(('#win'));
				$('#WK_KSMC').textbox("setValue","");
				$('#WK_BMKSSJ').datebox("setValue","");
				$('#WK_BMJSSJ').datebox("setValue","");
				$('#WK_KSRQ').datebox("setValue","");	
				$('#WK_BMFY').numberbox("setValue","");
				$('#WK_BEIZHU').val("");
			}
			if(iToolbar == "editOutsideExam"){
				saveType = 'editOutsideExam';
				$("#active").val(iToolbar);
				if(iKeyCode==""){
					alertMsg("请选择一行数据");
					return;
				}else{
					$('#win').dialog({   
						title: '编辑外考科目'
					});
					$('#win').dialog("open");
					if(row!=undefined && row!=''){
						$('#fm').form('load', row);
				
					}		

				}
			}
			//保存外考考试
			if(iToolbar == "saveClass"){
				var bmkssj = $("#WK_BMKSSJ").datebox('getValue');
				var bmjssj = $("#WK_BMJSSJ").datebox('getValue');
				var ksrq = $("#WK_KSRQ").datebox('getValue');

				if($('#WK_KSMC').val()==""){
					alertMsg("请输入外考科目名称");
					return;
				}				
				if(bmkssj==""){
					alertMsg("请输入报名开始时间");
					return;
				}
				if(bmjssj==""){
					alertMsg("请输入报名节数时间");
					return;
				}
				if(ksrq==""){
					alertMsg("请输入考试日期");
					return;
				}
				if($('#WK_BMFY').val()==""){
					alertMsg("请输入报名费用");
					return;
				}
				//判断开始时间是否大于结束时间
				if(bmkssj > bmjssj){
					alertMsg("报名开始时间必须在报名结束时间之前");
					return;
				}
			
				if(bmjssj > ksrq){
					alertMsg("报名日期必须在考试日期之前");
					return;
				}
				$('#WK_BZ').val($('#WK_BEIZHU').text());
				$('#fm').submit();

			}
			//添加一行授课信息
			if(iToolbar == "add"){
				tscnum++;
				var html='';
				html='<tr>'+
						'<td width="12%" class="titlestyle">任课老师</td>'+
						'<td width="12%" onclick="opentea(\'HS_RKLS'+tscnum+'\');">'+
							'<input name="HS_RKLS" id="HS_RKLS'+tscnum+'" class="easyui-textbox" style="width:100px;" readonly="true" />'+
						'</td>'+
						'<td width="12%" class="titlestyle">授课周次</td>'+
						'<td width="12%" onclick="openweek(\'HS_SKZC'+tscnum+'\');">'+
							'<input name="HS_SKZC" id="HS_SKZC'+tscnum+'" class="easyui-textbox" style="width:100px;" readonly="true" />'+
						'</td>'+
						'<td width="12%" class="titlestyle">场地要求</td>'+
						'<td width="12%" onclick="openroom(\'HS_CDYQ'+tscnum+'\');">'+
							'<input name="HS_CDYQ" id="HS_CDYQ'+tscnum+'" class="easyui-textbox" style="width:100px;" readonly="true" />'+
						'</td>'+
						'<td style="width:40px;" align="center">'+
							
						'</td>'+
					'</tr>';
				$("#tsctable").append(html);
				//$.parser.parse(('#tsctable'));
			}
			if(iToolbar == "delOutsideExam"){
				if(iKeyCode==""){
					alertMsg("请选择一行数据");
					return;
				}else{
					$.ajax({
						type: "POST",
						url: '<%=request.getContextPath()%>/Svl_outsideExam',
						data: "active=checkExamStu&WK_KSBH=" + iKeyCode,
						dataType: 'json',
						success: function(datas){
							if(datas[0].MSG=="0"){
								ConfirmMsg("是否确认删除这门考试？", "delExam()","");
							}else{
								ConfirmMsg(datas[0].MSG, "delExam()","");
							}			
						}
					});
				}
			}
			if(iToolbar == "delall"){
					classId1 = window.parent.document.frames["left"].classId;
					xqbh1 = window.parent.document.frames["left"].xqbh;
					$.ajax({
						type: "POST",
						url: '<%=request.getContextPath()%>/Svl_Skjh',
						data: "active=checkDelallSKJH&GS_XNXQBM=" + xqbh1 + "&GS_XZBDM=" + classId1,
						dataType: 'json',
						success: function(datas){
				            var data = datas[0];
							if(data.MSG=="0"){//没有排课信息
								ConfirmMsg("本次操作将删除当前学期该专业同一届所有班级的授课计划，是否确定？", "delallskjh("+xqbh1+","+classId1+")","");
							}else{
								alertMsg("本学期授课计划已经完成了排课，无法删除！");
							}
						}
					});	
			}
			if(iToolbar == "submit3"){//教师
				var teas="";	
				var html="";
				for (var i = 0;i < teainfoidarray.length;i++) {
					if(html==""){
						if(teainfoidarray[i]=='000'&&teainfoarray[i]=='请选择'){
						
						}else{
							teas+=teainfoidarray[i];
							html+=teainfoarray[i];
						}
					}else{
						if(teainfoidarray[i]=='000'&&teainfoarray[i]=='请选择'){
						
						}else{
							teas+="+"+teainfoidarray[i];
							html+="+"+teainfoarray[i];
						}			
					}
				}
				teaidarray[(inputid.substring(7,inputid.length)-1)]=teas;//保存
				teaarray[(inputid.substring(7,inputid.length)-1)]=html;//保存
				$('#XX_JSBH').val(teas);
				$('#'+inputid).val(html);
				teainfoidarray.splice(0,teainfoidarray.length);
				teainfoarray.splice(0,teainfoarray.length);
				$('#teacher').dialog("close");

			}
			if(iToolbar == "submit4"){//周次
				var html="";
				if(weeksel==1){//自定义				
					var html="";
					var link=0;
					var tag=0;
					var num2=0;//上一个数字
					for (var i = 0;i < weekArray.length;i++) {			
						if(weekArray[i]==1){	
						    linkArray[link]=(i+1);
						    link++;
						}							
					}
					for(var j=0;j<link-1;j++){
						if((linkArray[(j+1)]-linkArray[j]==1)){
							tag=1;
						}else{
							tag=2;	
							break;		
						}
					}
					if(link==1){
						tag=3;
					}
					if(tag==1){
						html=linkArray[0]+"-"+linkArray[(link-1)];
					}else if(tag==2){
						for (var k=0;k<link;k++) {			
							if(k==0){
						    	html+=linkArray[k];
						    }else{
						    	html+="#"+linkArray[k];
						    }											
						}
					}else if(tag==3){
						html=linkArray[0];
					}else{
						html="";
					}
					
					//saveweeks();
					wekidarray[(inputid.substring(7,inputid.length)-1)]=(html);//保存
					wekarray[(inputid.substring(7,inputid.length)-1)]=(html);//保存
				}else if(weeksel==2){//单周
					html="单周";
					var weekmax=0;
					if(weeks1%2==0){
						weekmax=weeks1-1;
					}else{
						weekmax=weeks1;
					}
					//saveweeks();
					wekidarray[(inputid.substring(7,inputid.length)-1)]=("odd");//保存
					wekarray[(inputid.substring(7,inputid.length)-1)]=("odd");//保存
				}else if(weeksel==3){//双周
					html="双周";
					var weekmax=0;
					if(weeks1%2==0){
						weekmax=weeks1;
					}else{
						weekmax=weeks1-1;
					}
					//saveweeks();
					wekidarray[(inputid.substring(7,inputid.length)-1)]=("even");//保存
					wekarray[(inputid.substring(7,inputid.length)-1)]=("even");//保存
				}else if(weeksel==4){//前9周
					html="1-9";
					wekidarray[(inputid.substring(7,inputid.length)-1)]=("1-9");//保存
					wekarray[(inputid.substring(7,inputid.length)-1)]=("1-9");//保存
				}else if(weeksel==5){//前14周
					html="1-14";
					wekidarray[(inputid.substring(7,inputid.length)-1)]=("1-14");//保存
					wekarray[(inputid.substring(7,inputid.length)-1)]=("1-14");//保存
				}else if(weeksel==6){//后14周
					html="5-18";
					wekidarray[(inputid.substring(7,inputid.length)-1)]=("5-18");//保存
					wekarray[(inputid.substring(7,inputid.length)-1)]=("5-18");//保存
				}else if(weeksel==7){//后9周
					html="10-18";
					wekidarray[(inputid.substring(7,inputid.length)-1)]=("10-18");//保存
					wekarray[(inputid.substring(7,inputid.length)-1)]=("10-18");//保存
				}else if(weeksel==8){//前9周
					html="1-18";
					wekidarray[(inputid.substring(7,inputid.length)-1)]=("1-18");//保存
					wekarray[(inputid.substring(7,inputid.length)-1)]=("1-18");//保存
				}
				
				//alert(inputid+"|"+wekarray[(inputid.substring(7,inputid.length)-1)]);
				$('#'+inputid).val(html);

				$('#week').dialog("close");
			}
			if(iToolbar == "submit5"){//场地			
				var roms="";
				var html="";
				if(roomsel==1){//只选类型	
					if($('#normalroom').val()+$('#meidaroom').val()<1){
						alertMsg("教室数量至少为1");
						return;
					}	
					if(!$('#normalroom').val()==""){	
						for(var j=0;j<$('#normalroom').val();j++){
							if(j==0){
								html+="普通教室";
								roms+="5";
							}else{
								html+="+普通教室";
								roms+="+5";
							}
						}
					}
					if(!$('#meidaroom').val()==""){	
						if($('#normalroom').val()==""||$('#normalroom').val()==0||$('#meidaroom').val()==0){
							html+="";
							roms+="";
						}else{
							html+="+";
							roms+="+";
						}
						for(var j=0;j<$('#meidaroom').val();j++){
							if(j==0){
								html+="多媒体教室";
								roms+="1";
							}else{
								html+="+多媒体教室";
								roms+="+1";
							}
						}					
					}
					clsidarray[(inputid.substring(7,inputid.length)-1)]=(roms);//保存
					clsarray[(inputid.substring(7,inputid.length)-1)]=(html);//保存
				}else if(roomsel==2){//指定教室
					//var rObj = document.getElementsByName("roomall");		
					var roms="";
					var html="";
					for (var i = 0;i < clsinfoidarray.length;i++) {
						if(html==""){
							roms+=clsinfoidarray[i];
							html+=clsinfoarray[i];
						}else{
							roms+="+"+clsinfoidarray[i];
							html+="+"+clsinfoarray[i];
						}
					}
	
					clsidarray[(inputid.substring(7,inputid.length)-1)]=(roms);//保存
					clsarray[(inputid.substring(7,inputid.length)-1)]=(html);//保存
					clsinfoidarray.splice(0,clsinfoidarray.length);
					clsinfoarray.splice(0,clsinfoarray.length);
				}else{
				
				}
				$('#XX_CDYQ').val(roms);
				$('#'+inputid).val(html);

				$('#room').dialog("close");
			}
			if(iToolbar == "submit2"){//保存所有
				var rObj=document.getElementsByName("HS_RKLS");
				var sObj=document.getElementsByName("HS_SKZC");
				var cObj=document.getElementsByName("HS_CDYQ");
				var rkls="";
				var skzc="";
				var cdyq="";
				
				var checkObj=0;
				for (var i=0;i<rObj.length;i++) {
					if($('#'+rObj[i].id).val()==""){
						checkObj=1;
					}else{
						if(i==0){
							rkls+=$('#'+rObj[i].id).val();
						}else{
							rkls+="&"+$('#'+rObj[i].id).val();
						}
					}
				}
				for (var j=0;j<sObj.length;j++) {
					if($('#'+sObj[j].id).val()==""){
						checkObj=1;
					}else{
						if(j==0){
							skzc+=$('#'+sObj[j].id).val();
						}else{
							skzc+="&"+$('#'+sObj[j].id).val();
						}
					}
				}
				for (var k=0;k<cObj.length;k++) {
					if($('#'+cObj[k].id).val()==""){
						checkObj=1;
					}else{
						if(k==0){
							cdyq+=$('#'+cObj[k].id).val();
						}else{
							cdyq+="&"+$('#'+cObj[k].id).val();
						}
					}
				}
				if(checkObj==1){
					alertMsg("任课老师,授课周次,场地要求都不能为空");
				}else{//保存提交
					
					$('#XX_SKJS').textbox('setValue',rkls);
					$('#XX_SKZC').textbox('setValue',skzc);
					$('#XX_CDMC').textbox('setValue',cdyq);
					$('#tsc').dialog("close");
				}
				
				//saveweeks();
			}
			if(iToolbar == "searchSC"){
				loadGrid();
			}
			if(iToolbar == "searchMAJOR"){
				loadGridMajor();
			}
			//可报名专业
			if(iToolbar == "addMajor"){
				var rObj = document.getElementsByName("zybh");
				var majorid="";
				var majorname="";
				for (var i = 0;i < rObj.length;i ++) {
					var checkbox = document.getElementById(rObj[i].id);
					if(checkbox.checked){
						majorid+=rObj[i].id+",";
						majorname+=rObj[i].value+",";
					}					
				}
				majorid=majorid.substring(0,majorid.length-1);
				majorname=majorname.substring(0,majorname.length-1);
				$('#XX_ZYBH').val(majorid);
				$('#XX_ZYNA').val(majorname);
				$('#ic_zymc').dialog("close");
			}
			//上传学生照片,打开dialog 
			if(iToolbar == "uploadAllstuPhoto"){
				$("#stuPhoto").dialog("open");
			}
			//上传学生照片 
			if(iToolbar == "uploadRar"){
				var filename=$('#browse').val();
				if(filename==''||filename==null){//未选择文件
						alertMsg('请选择文件!',0) ;
						//alert('请选择文件');
						return;
				}
				$('#path').val(escape(filename));
				$("#stuPhoto").dialog("close");
				$('#divPageMask2').show();
				$("#form1").attr("enctype","multipart/form-data");
				$("#form1").attr("encoding", "multipart/form-data");
				$("#form1").submit();
			}
		}
		
		//删除考试项目
		function delExam(){
			$.ajax({
				type: "POST",
				url: '<%=request.getContextPath()%>/Svl_outsideExam',
				data: "active=delOutsideExam&WK_KSBH=" + iKeyCode,
				dataType: 'json',
				success: function(datas){
					showMsg(datas[0].MSG);
					loadGrid();
				}
			});
		}
		
		//根据改变的授课计划删除已设置的固排与合班
		function editCD(){
			$.ajax({
				type: "POST",
				url: '<%=request.getContextPath()%>/Svl_Skjh',
				data: "active=editCD&iKeyCode=" + iKeyCode,
				dataType: 'json',
				success: function(datas){
		            var data = datas[0];
					if(data.MSG=="删除成功"){
						check();
					}	
				}
			});
		}
		
		function delrow(obj){
				var delId=obj.id.substring(3,obj.id.length)-1;//删除的编号
				
				teaidarray[delId]="";
				teaarray[delId]="";
				teainfoidarray[delId]="";
				teainfoarray[delId]="";
				wekidarray[delId]="";
				wekarray[delId]="";
				clsidarray[delId]="";
				clsarray[delId]="";
				clsinfoidarray[delId]="";
				clsinfoarray[delId]="";
				
				//删除按钮所在的tr对象
				var tr=obj.parentNode.parentNode;
				var tbody=tr.parentNode;
				tbody.removeChild(tr);
				
				
		}
		
		//删除授课计划
		function delskjh(){
			$.ajax({
				type: "POST",
				url: '<%=request.getContextPath()%>/Svl_Skjh',
				data: "active=deleteskjh&iKeyCode=" + iKeyCode,
				dataType: 'json',
				success: function(datas){
		            var data = datas[0];
					initGridData(classId1,xqbh1);	
					showMsg(data.MSG);	
				}
			});
		}
		
		//删除所有授课计划
		function delallskjh(xqbh1,classId1){
			$.ajax({
				type: "POST",
				url: '<%=request.getContextPath()%>/Svl_Skjh',
				data: "active=deleteAllskjh&GS_XNXQBM=" + xqbh1 + "&GS_XZBDM=" + classId1,
				dataType: 'json',
				success: function(datas){
		            var data = datas[0];
					initGridData(classId1,xqbh1);	
					showMsg(data.MSG);	
				}
			});
		}
		
		//保存所有最小和最大周次
		function saveweeks(){
				//取最小和最大周次
				var min="";
				var max="";
				var sObj=document.getElementsByName("HS_SKZC");
				for(var k=0;k<sObj.length;k++){
					var skzcinfo=$('#'+sObj[k].id).val();
					if(skzcinfo=="单周"){
						var weekmax=0;
						if(weeks1%2==0){
							weekmax=weeks1-1;
						}else{
							weekmax=weeks1;
						}
						wekidarray[k]=("1|"+weekmax);//保存
					}else if(skzcinfo=="双周"){
						var weekmax=0;
						if(weeks1%2==0){
							weekmax=weeks1;
						}else{
							weekmax=weeks1-1;
						}
						wekidarray[k]=("2|"+weekmax);//保存
					}else if(skzcinfo.indexOf("#")>-1){
						var skzcs=skzcinfo.split("#");
						wekidarray[k]=(skzcs[0]+"|"+skzcs[skzcs.length-1]);//保存
					}else if(skzcinfo.indexOf("-")>-1){
						var skzcs=skzcinfo.split("-");
						wekidarray[k]=(skzcs[0]+"|"+skzcs[1]);//保存
					}else{
						wekidarray[k]=(skzcinfo+"|"+skzcinfo);//保存
					}
				}
		}
		
		//检查教师授课是否超过上限
		function check(){ 
			
			skjsbh="";
			for(var i=0;i<teaidarray.length;i++){
				if(i==0){
					skjsbh+=teaidarray[i];
				}else{
					skjsbh+="|"+teaidarray[i];
				}
			}	
			skjsbhxm="";
			for(var i=0;i<teaarray.length;i++){
				if(i==0){
					skjsbhxm+=teaarray[i];
				}else{
					skjsbhxm+="|"+teaarray[i];
				}
			}

			if(saveType=="new"){
				xkdm=$('#GS_KCMC').combobox('getValue');
			}else{
				xkdm=$('#GS_KCMC').val();
			}
			
			$.ajax({
			   type: "POST",
			   url: '<%=request.getContextPath()%>/Svl_Skjh',
			   data: "active=check&XNXQ=" + xnxqVal1 + '&JXXZ=' + jxxzVal1 + '&SKJSBH=' + skjsbh + '&SKJSBHXM=' + skjsbhxm + '&XKDM=' + xkdm,
			   dataType: 'json',
			   success: function(datas){
                    var data = datas[0];
					if(data.JSKCS=="true"){
						$('#fm').submit();
					}else{
						//alertMsg("该任课教师的最大同时任教课程数为"+data.SKSL+"门,已达上限");
						var teachersid=data.MSG;
						var teacherid=teachersid.substring(0,teachersid.length-1);
						teacherid=teacherid.split(",");
						var teachername="";
						for(var i=0;i<teacherid.length;i++){
							if(teachername.indexOf(teacherid[i])==-1){
								if(i==0){
									teachername+=teacherid[i];
								}else{
									teachername+=","+teacherid[i];
								}
							}
						}
						alertMsg("任课教师"+teachername+"的最大同时任教课程数已达上限");
					}
			   }
			});
		}
		
		//fm提交事件
		$('#fm').form({
			url:'<%=request.getContextPath()%>/Svl_outsideExam',
			onSubmit:function(){
			
			},
			//提交成功
			success:function(datas){
				var json = eval("("+datas+")");
				showMsg(json[0].MSG); 
				$('#win').dialog("close");
				loadGrid();	
			}
		});
		
		/**表单提交**/
		$('#form1').form({
			//定位到servlet位置的url
			url:'<%=request.getContextPath()%>/Svl_outsideExam?active=uploadAllstuPhoto&path='+$('#path').val(),
			//当点击事件后触发的事件
			onSubmit: function(data){
				return $(this).form('validate');//验证
			}, 
			//当点击事件并成功提交后触发的事件
			success:function(data){ 
				$("#form1").attr("enctype","application/x-www-form-urlencoded");
				$("#form1").attr("encoding", "application/x-www-form-urlencoded");
				var json = eval("("+data+")");
				if(json[0].MSG == '上传失败'){
					$('#divPageMask2').hide();
					alertMsg(json[0].MSG);
				}else{
					$('#divPageMask2').hide();
					alertMsg(json[0].MSG);
				}
			}
		});
		
		//dialog窗口
		function loadDialog() {
			$('#win').dialog({
				width: 600,//宽度设置   
				height: 357,//高度设置
				modal : true,
				closed : true,
				toolbar:[{
					text:'保存',
					iconCls:'icon-save',
					handler:function(){
						doToolbar('saveClass');
					}
				}],
				onOpen : function(data) {
					$('#win').find('table').css('display', 'block');
				},
				onLoad : function(data) {
				},
				onClose : function(data) {
					emptyDialog();//初始化信息
					saveType = '';
				}
			});
			
			$('#tsc').dialog({   
				title: '任课老师  授课周次  场地要求',   
				width: 580,//宽度设置   
				height: 280,//高度设置 
				modal:true,
				closed: true,   
				cache: false, 
				draggable:true,//是否可移动dialog框设置
				//读取事件
				onLoad:function(data){
					var kw=document.getElementById("HS_RKLS1");
					kw.focus();					
				},
				//关闭事件
				onClose:function(data){
					tscnum=0;
				}
			});
			
			$('#teacher').dialog({   
				title: '任课老师',   
				width: 580,//宽度设置   
				height: 400,//高度设置 
				modal:true,
				closed: true,   
				cache: false, 
				draggable:true,//是否可移动dialog框设置
				//读取事件
				onLoad:function(data){
					
				},
				//关闭事件
				onClose:function(data){
					teainfoidarray.splice(0,teainfoidarray.length);
					teainfoarray.splice(0,teainfoarray.length);
				}
			});
			
			$('#week').dialog({   
				title: '授课周次',   
				width: 300,//宽度设置   
				height: 313,//高度设置 
				modal:true,
				closed: true,   
				cache: false, 
				draggable:true,//是否可移动dialog框设置
				//读取事件
				onLoad:function(data){},
				//关闭事件
				onClose:function(data){
					
				}
			});
			
			$('#room').dialog({   
				title: '场地要求',   
				width: 580,//宽度设置   
				height: 450,//高度设置 
				modal:true,
				closed: true,   
				cache: false, 
				draggable:true,//是否可移动dialog框设置
				//读取事件
				onLoad:function(data){},
				//关闭事件
				onClose:function(data){
					clsinfoidarray.splice(0,teainfoidarray.length);
					clsinfoarray.splice(0,teainfoarray.length);
				}
			});
			
			$('#ic_zymc').dialog({   
				title: '专业',   
				width: 800,//宽度设置   
				height: 500,//高度设置 
				modal:true,
				closed: true,   
				cache: false, 
				draggable:true,//是否可移动dialog框设置
				//读取事件
				onLoad:function(data){
					
				},
				//关闭事件
				onClose:function(data){
					
				}
			});

			
		}
		
		function loadEx(){
			$('#stuPhoto').dialog({   
				title: '批量上传学生照片',   
				width: 244,//宽度设置   
				height: 91,//高度设置
				modal:true,
				closed: true,   
				cache: false, 
				draggable:true,//是否可移动dialog框设置
				//读取事件
				onLoad:function(data){
					
				},
				//关闭事件
				onClose:function(data){
					
				}
			});	
		}
		
		//初始化信息
		function emptyDialog(){
	
		}
		
		//打开tsc编辑窗口
		function opentsc(){
			document.getElementById("win").focus(); 
			if($('#XX_XNXQ').combobox('getValue')==""){
				alertMsg("请先选择学年学期");
				return;
			}
			//初始化fm2
			$("#fm2").html('');
			var html='';
				html='<table id="tsctable" class = "tablestyle" width="686px">'+
						'<tr>'+
							'<a href="#" class="easyui-linkbutton" id="submit2" name="submit2" iconCls="icon-submit" plain="true" onClick="doToolbar(this.id)">确定</a>'+
							
						'</tr>'+
					'</table>';
			$("#fm2").html(html);
			$.parser.parse(('#fm2'));
			
			if(teaidarray.length==0){
				doToolbar("add");
			}else{
				for(var i=0;i<teaidarray.length;i++){
					if(teaidarray[i]!=""){
						doToolbar("add");
					}else{
						tscnum++;
					}	
				}
				
				//var rObj=document.getElementsByName("HS_RKLS");
				//var sObj=document.getElementsByName("HS_SKZC");
				//var cObj=document.getElementsByName("HS_CDYQ");
				
				for(var i=0;i<teaidarray.length;i++){
					var teachall="";
					var wekchall="";
					var classall="";
					//alert(teaarray[i]+"|"+wekarray[i]+"|"+clsarray[i]);
					if(wekarray[i]=="odd"){
						wekchall="单周";
					}else if(wekarray[i]=="even"){
						wekchall="双周";
					}else{
						wekchall=wekarray[i];
					}
					if(teaarray[i]!=""){
						$('#HS_RKLS'+(i+1)).val(teaarray[i]);
						$('#HS_SKZC'+(i+1)).val(wekchall);
						$('#HS_CDYQ'+(i+1)).val(clsarray[i]);
					}
				}
			}
			
			
			$('#tsc').show();
			
			$('#tsc').dialog("open");
			
		}
		
		//打开teacher编辑窗口
		function opentea(id){
			inputid=id;
			document.getElementById("tsc").focus(); 
			$('#ic_teaId').val("");
			$('#ic_teaName').val("");
			$('#ic_teaLevel').combobox("setValue","");
			var teabh=teaidarray[(inputid.substring(7,inputid.length)-1)];		
			loadGridTea(teabh);
			
			if($('#'+inputid).val()!=""&&teaidarray[(inputid.substring(7,inputid.length)-1)]!=""){
				var teaid=teaidarray[(inputid.substring(7,inputid.length)-1)].split("+");
				var teana=teaarray[(inputid.substring(7,inputid.length)-1)].split("+");
				for(var i=0;i<teaid.length;i++){
					teainfoidarray.push(teaid[i]);
					teainfoarray.push(teana[i]);				
				}
		 	}else{
		 		teainfoidarray.splice(0,teainfoidarray.length);
				teainfoarray.splice(0,teainfoarray.length);
		 	}
		 	
			JSLBHCombobox();
			$('#teacher').show();
			
			$('#teacher').dialog("open");		
		}
		
		function loadGridTea(teabh){
			$('#teatable').datagrid({
				url: '<%=request.getContextPath()%>/Svl_Skjh',
	 			queryParams: {"active":"openTeacher","teaId":$('#ic_teaId').val(),"teaName":$('#ic_teaName').val(),"teaLevel":$('#ic_teaLevel').combobox('getText'),"teacharr":teabh},
				loadMsg : "信息加载中请稍后!",//载入时信息
				width:566,
				height:307,
				rownumbers: true,
				animate:true,
				striped : true,//隔行变色
				pageSize : pageSize,//每页记录数
				singleSelect : true,//单选模式
				pageNumber : pageNum,//当前页码
				pagination:true,
				fit:false,
				fitColumns: true,//设置边距
				columns:[[
					{field:'1',title:'选择',width:50,align:'center',
						formatter:function(value,rec){
							var gh='<input type="checkbox" id="'+rec.工号+'" name="teacb" hidename="'+rec.姓名+'"  onclick="editTeaInfo(this.id);" />';
							return gh;
						}
					},
					{field:'工号',title:'工号',width:100,align:'center'},
					{field:'姓名',title:'姓名',width:100,align:'center'},
					{field:'层级名称',title:'教师类别',width:100,align:'center'}
					
				]],
				onClickRow:function(rowIndex, rowData){
					$('#teatable').datagrid("unselectRow", rowIndex);
				},
				onLoadSuccess: function(data){
					
					
					if($('#'+inputid).val()!=""&&teaidarray[(inputid.substring(7,inputid.length)-1)]!=""){
						var teaid=teaidarray[(inputid.substring(7,inputid.length)-1)].split("+");

						for(var i=0;i<teaid.length;i++){
							$('#'+teaid[i]).attr("checked","checked");
						}
						for(var i=0;i<teainfoidarray.length;i++){
							$('#'+teainfoidarray[i]).attr("checked","checked");
						}
		 			}
				},
				onLoadError:function(none){
					
				}
			});
		}
		
		//加载下拉框数据
		function JSLBHCombobox(){
			$('#ic_teaLevel').combobox({
				url:"<%=request.getContextPath()%>/Svl_queryState?active=JSLBHCombobox",
				valueField:'comboValue',
				textField:'comboName',
				editable:false,
				panelHeight:'140', //combobox高度
				onLoadSuccess:function(data){
					
				},
				//下拉列表值改变事件
				onChange:function(data){ 
					
				}
			});
		}	
		
		//保存选择的任课教师信息
		function editTeaInfo(id){
			var checkbox = document.getElementById(id);
			if(checkbox.checked){//勾选
				teainfoidarray.push(checkbox.id);
				teainfoarray.push(checkbox.hidename);
			}else{//不勾选
				for(var i=0;i<teainfoidarray.length;i++){
					if(checkbox.id==teainfoidarray[i]){
						teainfoidarray.splice(i,1);
						teainfoarray.splice(i,1);
					}
				}
			}
		}
		
		//打开week编辑窗口
		function openweek(id){ 
			var weekArray = new Array(weeks1);//保存当前窗口第几周是否被选中
			var weekAllArray = new Array(weeks1);//保存所有窗口周次是否已经被选择
			inputid=id;
			document.getElementById("tsc").focus(); 
			
			var html1="";
			html1=  '<tr>'+
						'<a href="#" class="easyui-linkbutton" id="submit4" name="submit4" iconCls="icon-submit" plain="true" onClick="doToolbar(this.id)">确定</a>'+
				    '</tr>'+
				    '<tr>'+
						'<td width="33%" align="center"><input type="checkbox" id="custom" name="" onclick="weekcheck1();" />自定义</td>'+
						'<td width="33%" align="center"><input type="checkbox" id="singleweek" name="" onclick="weekcheck2();" /> 单周</td>'+
						'<td width="33%" align="center"><input type="checkbox" id="doubleweek" name="" onclick="weekcheck3();" /> 双周</td>'+
					'</tr>'+
					'<tr>'+
						'<td width="33%" align="center"><input type="checkbox" id="firstnine" name="" onclick="weekcheck4();" /> 01-09</td>'+
						'<td width="33%" align="center"><input type="checkbox" id="firstfourteen" name="" onclick="weekcheck5();" /> 01-14</td>'+
						'<td width="33%" align="center"><input type="checkbox" id="lastfourteen" name="" onclick="weekcheck6();" /> 05-18</td>'+
					'</tr>'+
					'<tr>'+
						'<td width="33%" align="center"><input type="checkbox" id="lastnine" name="" onclick="weekcheck7();" /> 10-18</td>'+
						'<td width="33%" align="center"><input type="checkbox" id="allweek" name="" onclick="weekcheck8();" /> 01-18</td>'+
						'<td width="33%" align="center"> </td>'+
					'</tr>';
			var html2="";
			for(var i=1;i<=weeks1;i=i+3){
				if((i+1)>weeks1){
					html2+= '<tr>'+
							'<td width="33%" align="center" id="weekn'+i+'" style="cursor:pointer;" onmouseover="weekmouseover(this.id);" onmouseout="weekmouseout(this.id);" onclick="chooseweek(this.id);">'+i+'</td>'+
							'<td width="33%" align="center" ></td>'+
							'<td width="33%" align="center" ></td>'+
						'</tr>';
				}else if((i+2)>weeks1){
					html2+= '<tr>'+
							'<td width="33%" align="center" id="weekn'+i+'" style="cursor:pointer;" onmouseover="weekmouseover(this.id);" onmouseout="weekmouseout(this.id);" onclick="chooseweek(this.id);">'+i+'</td>'+
							'<td width="33%" align="center" id="weekn'+(i+1)+'" style="cursor:pointer;" onmouseover="weekmouseover(this.id);" onmouseout="weekmouseout(this.id);" onclick="chooseweek(this.id);">'+(i+1)+'</td>'+
							'<td width="33%" align="center" ></td>'+
						'</tr>';
				}else{
					html2+= '<tr>'+
							'<td width="33%" align="center" id="weekn'+i+'" style="cursor:pointer;" onmouseover="weekmouseover(this.id);" onmouseout="weekmouseout(this.id);" onclick="chooseweek(this.id);">'+i+'</td>'+
							'<td width="33%" align="center" id="weekn'+(i+1)+'" style="cursor:pointer;" onmouseover="weekmouseover(this.id);" onmouseout="weekmouseout(this.id);" onclick="chooseweek(this.id);">'+(i+1)+'</td>'+
							'<td width="33%" align="center" id="weekn'+(i+2)+'" style="cursor:pointer;" onmouseover="weekmouseover(this.id);" onmouseout="weekmouseout(this.id);" onclick="chooseweek(this.id);">'+(i+2)+'</td>'+
						'</tr>';
				}
				
			}
			$("#weektable").html(html1+html2);
			$.parser.parse(('#weektable'));
			
			$('#week').show();
			
			//初始化
			weeksel=0;
			//weekArray.splice(0,weekArray.length);  //清空选择
			$('#custom').attr("checked",false);
			$('#singleweek').attr("checked",false);
			$('#doubleweek').attr("checked",false);
			for(var i=1;i<=weeks1;i++){
				$('#weekn'+i).css('background','#efefef');	
				weekArray[(i-1)]=0;
			}
			$('#week').dialog("open");
			
			//alert("wekarray[i]:"+wekarray[inputid.substring(7,inputid.length)-1]);
			if($('#'+inputid).val()!=""){
				if(wekarray[(inputid.substring(7,inputid.length)-1)]=="odd"){
					$('#singleweek').attr("checked","checked");
					weekcheck2();
				}else if(wekarray[(inputid.substring(7,inputid.length)-1)]=="even"){
					$('#doubleweek').attr("checked","checked");
					weekcheck3();
				}else{
					var weeknum=wekarray[(inputid.substring(7,inputid.length)-1)]+"";
					var weeknums="";
					$('#custom').attr("checked","checked");	
					weeksel=1;
					//weekcheck1();
					if(weeknum.indexOf("#")>-1){//不连续
						weeknums=weeknum.split("#");
						for(var i=0;i<weeknums.length;i++){
							$('#weekn'+weeknums[i]).css('background','#E46BA1');
							weekArray[(weeknums[i]-1)]=1;						
						}
					}else if(weeknum.indexOf("-")>-1){//连续
						weeknums=weeknum.split("-");
						for(var i=parseInt(weeknums[0]);i<=parseInt(weeknums[1]);i++){
							$('#weekn'+i).css('background','#E46BA1');
							weekArray[(i-1)]=1;		
						}
					}else{
						$('#weekn'+weeknum).css('background','#E46BA1');
						weekArray[(weeknum-1)]=1;
					}
				}
 			}
 			
 			//清空weekAllArray重新赋值
 			for(var i=0;i<weeks1;i++){
				weekAllArray[i]=0;
			}
			
 			//取所有周次的值，不允许重复
 			for(var i=1;i<=tscnum;i++){
 				if(i!=(inputid.substring(7,inputid.length))){
 					if($('#HS_SKZC'+i).val()!=""){
 						//alert($('#HS_SKZC'+i).val());
	 					if($('#HS_SKZC'+i).val()=="单周"){
	 						$('#singleweek').attr("disabled","disabled");
	 						$('#firstnine').attr("disabled","disabled");
	 						$('#firstfourteen').attr("disabled","disabled");
	 						$('#lastfourteen').attr("disabled","disabled");
	 						$('#lastnine').attr("disabled","disabled");
	 						$('#allweek').attr("disabled","disabled");
	 						if(tscnum>2){
	 							$('#doubleweek').attr("disabled","disabled");
	 						}
	    					for(var j=0;j<weeks1;j++){
		  						if(j%2==0){//单数
	  								weekAllArray[j]=1;
	  							}
	    					}
	 					}else if($('#HS_SKZC'+i).val()=="双周"){
	 						$('#doubleweek').attr("disabled","disabled");
	 						$('#firstnine').attr("disabled","disabled");
	 						$('#firstfourteen').attr("disabled","disabled");
	 						$('#lastfourteen').attr("disabled","disabled");
	 						$('#lastnine').attr("disabled","disabled");
	 						$('#allweek').attr("disabled","disabled");
	 						if(tscnum>2){
	 							$('#singleweek').attr("disabled","disabled");
	 						}
	 						for(var j=0;j<weeks1;j++){
	 							if(j%2!=0){//双数
	 								weekAllArray[j]=1;
	 							}
	 						}
	 					}else if($('#HS_SKZC'+i).val()!=""&&($('#HS_SKZC'+i).val().indexOf("#")>-1||$('#HS_SKZC'+i).val().indexOf("-")>-1)){
	 						var skzc=$('#HS_SKZC'+i).val();
	 						var skzcnum="";
	 						if(skzc.indexOf("#")>-1){//不连续
								skzcnum=skzc.split("#");
								for(var j=0;j<skzcnum.length;j++){
									weekAllArray[(skzcnum[j]-1)]=1;
									if(skzcnum[j]%2==0){
										$('#doubleweek').attr("disabled","disabled");
									}else if(skzcnum[j]%2==1){
										$('#singleweek').attr("disabled","disabled");
									}	
									if(1<=parseInt(skzcnum[j])&&parseInt(skzcnum[j])<5){//1-4
										$('#firstnine').attr("disabled","disabled");
			 							$('#firstfourteen').attr("disabled","disabled");
			 							$('#allweek').attr("disabled","disabled");
									}else if(5<=parseInt(skzcnum[j])&&parseInt(skzcnum[j])<10){//5-9
										$('#firstnine').attr("disabled","disabled");
			 							$('#firstfourteen').attr("disabled","disabled");
			 							$('#lastfourteen').attr("disabled","disabled");
			 							$('#allweek').attr("disabled","disabled");
									}else if(10<=parseInt(skzcnum[j])&&parseInt(skzcnum[j])<15){//10-14
			 							$('#firstfourteen').attr("disabled","disabled");
			 							$('#lastfourteen').attr("disabled","disabled");
			 							$('#lastnine').attr("disabled","disabled");
			 							$('#allweek').attr("disabled","disabled");
									}else if(15<=parseInt(skzcnum[j])&&parseInt(skzcnum[j])<19){//15-18
			 							$('#lastfourteen').attr("disabled","disabled");
			 							$('#lastnine').attr("disabled","disabled");
			 							$('#allweek').attr("disabled","disabled");
									}else if(19<=parseInt(skzcnum[j])){//18-总周数
		
									}					
								}
							}else if(skzc.indexOf("-")>-1){//连续
								skzcnum=skzc.split("-");
								for(var j=parseInt(skzcnum[0]);j<=parseInt(skzcnum[1]);j++){
									weekAllArray[(j-1)]=1;	
								}
								$('#singleweek').attr("disabled","disabled");
								$('#doubleweek').attr("disabled","disabled");
								if(1<=parseInt(skzcnum[0])&&parseInt(skzcnum[0])<5){//1-4
									$('#firstnine').attr("disabled","disabled");
		 							$('#firstfourteen').attr("disabled","disabled");
		 							$('#allweek').attr("disabled","disabled");
								}else if(5<=parseInt(skzcnum[0])&&parseInt(skzcnum[0])<10){//5-9
									$('#firstnine').attr("disabled","disabled");
		 							$('#firstfourteen').attr("disabled","disabled");
		 							$('#lastfourteen').attr("disabled","disabled");
		 							$('#allweek').attr("disabled","disabled");
								}else if(10<=parseInt(skzcnum[0])&&parseInt(skzcnum[0])<15){//10-14
		 							$('#firstfourteen').attr("disabled","disabled");
		 							$('#lastfourteen').attr("disabled","disabled");
		 							$('#lastnine').attr("disabled","disabled");
		 							$('#allweek').attr("disabled","disabled");
								}else if(15<=parseInt(skzcnum[0])&&parseInt(skzcnum[0])<19){//15-18
		 							$('#lastfourteen').attr("disabled","disabled");
		 							$('#lastnine').attr("disabled","disabled");
		 							$('#allweek').attr("disabled","disabled");
								}else if(19<=parseInt(skzcnum[0])){//18-总周数
	
								}
							}		
	 					}else{
							var skzc=$('#HS_SKZC'+i).val();
							weekAllArray[(skzc-1)]=1;	
							if(skzc%2==0){
								$('#doubleweek').attr("disabled","disabled");
							}else if(skzc%2==1){
								$('#singleweek').attr("disabled","disabled");
							}
							if(1<=parseInt(skzc)&&parseInt(skzc)<5){//1-4
								$('#firstnine').attr("disabled","disabled");
	 							$('#firstfourteen').attr("disabled","disabled");
	 							$('#allweek').attr("disabled","disabled");
							}else if(5<=parseInt(skzc)&&parseInt(skzc)<10){//5-9
								$('#firstnine').attr("disabled","disabled");
	 							$('#firstfourteen').attr("disabled","disabled");
	 							$('#lastfourteen').attr("disabled","disabled");
	 							$('#allweek').attr("disabled","disabled");
							}else if(10<=parseInt(skzc)&&parseInt(skzc)<15){//10-14
	 							$('#firstfourteen').attr("disabled","disabled");
	 							$('#lastfourteen').attr("disabled","disabled");
	 							$('#lastnine').attr("disabled","disabled");
	 							$('#allweek').attr("disabled","disabled");
							}else if(15<=parseInt(skzc)&&parseInt(skzc)<19){//15-18
	 							$('#lastfourteen').attr("disabled","disabled");
	 							$('#lastnine').attr("disabled","disabled");
	 							$('#allweek').attr("disabled","disabled");
							}else if(19<=parseInt(skzc)){//18-总周数

							}
						}
					}
 				}
			}
			
			//var ii="";
			//for(var i=0;i<weeks1;i++){
			//	ii+="|"+(i+1)+":"+weekAllArray[i];
			//}
			//alert(ii);
		}
		
		//自定义，单周，双周选择
		function weekcheck1(){
			var checkbox = document.getElementById('custom');
			if(checkbox.checked){
				//$('#custom').attr("checked",false);
				$('#singleweek').attr("checked",false);
				$('#doubleweek').attr("checked",false);
				$('#firstnine').attr("checked",false);
				$('#firstfourteen').attr("checked",false);
				$('#lastfourteen').attr("checked",false);
				$('#lastnine').attr("checked",false);
				$('#allweek').attr("checked",false);
				
				weeksel=1;
				for(var i=1;i<=weeks1;i++){
					$('#weekn'+i).css('background','#efefef');	
				}
			}else{
				weeksel=0;
				for(var i=1;i<=weeks1;i++){
					$('#weekn'+i).css('background','#efefef');
				}
			}		
		}
		function weekcheck2(){
			var checkbox = document.getElementById('singleweek');
			if(checkbox.checked){
				$('#custom').attr("checked",false);
				//$('#singleweek').attr("checked",false);
				$('#doubleweek').attr("checked",false);
				$('#firstnine').attr("checked",false);
				$('#firstfourteen').attr("checked",false);
				$('#lastfourteen').attr("checked",false);
				$('#lastnine').attr("checked",false);
				$('#allweek').attr("checked",false);
				weeksel=2;
				for(var i=1;i<=weeks1;i++){
					if(i%2==0){
						$('#weekn'+i).css('background','#efefef');
					}else{
						$('#weekn'+i).css('background','#E46BA1');
					}	
				}
			}else{
				weeksel=0;
				for(var i=1;i<=weeks1;i++){
					$('#weekn'+i).css('background','#efefef');	
				}
			}
		}
		function weekcheck3(){
			var checkbox = document.getElementById('doubleweek');
			if(checkbox.checked){
				$('#custom').attr("checked",false);
				$('#singleweek').attr("checked",false);
				//$('#doubleweek').attr("checked",false);
				$('#firstnine').attr("checked",false);
				$('#firstfourteen').attr("checked",false);
				$('#lastfourteen').attr("checked",false);
				$('#lastnine').attr("checked",false);
				$('#allweek').attr("checked",false);
				weeksel=3;
				for(var i=1;i<=weeks1;i++){
					if(i%2==0){
						$('#weekn'+i).css('background','#E46BA1');
					}else{
						$('#weekn'+i).css('background','#efefef');
					}	
				}
			}else{
				weeksel=0;
				for(var i=1;i<=weeks1;i++){
					$('#weekn'+i).css('background','#efefef');	
				}
			}
		}
		function weekcheck4(){
			var checkbox = document.getElementById('firstnine');
			if(checkbox.checked){
				$('#custom').attr("checked",false);
				$('#singleweek').attr("checked",false);
				$('#doubleweek').attr("checked",false);
				//$('#firstnine').attr("checked",false);
				$('#firstfourteen').attr("checked",false);
				$('#lastfourteen').attr("checked",false);
				$('#lastnine').attr("checked",false);
				$('#allweek').attr("checked",false);
				weeksel=4;
				for(var i=1;i<=9;i++){
					$('#weekn'+i).css('background','#E46BA1');
				}
				for(var i=10;i<=weeks1;i++){
					$('#weekn'+i).css('background','#efefef');
				}
			}else{
				weeksel=0;
				for(var i=1;i<=weeks1;i++){
					$('#weekn'+i).css('background','#efefef');	
				}
			}
		}
		function weekcheck5(){
			var checkbox = document.getElementById('firstfourteen');
			if(checkbox.checked){
				$('#custom').attr("checked",false);
				$('#singleweek').attr("checked",false);
				$('#doubleweek').attr("checked",false);
				$('#firstnine').attr("checked",false);
				//$('#firstfourteen').attr("checked",false);
				$('#lastfourteen').attr("checked",false);
				$('#lastnine').attr("checked",false);
				$('#allweek').attr("checked",false);
				weeksel=5;
				for(var i=1;i<=14;i++){
					$('#weekn'+i).css('background','#E46BA1');
				}
				for(var i=15;i<=weeks1;i++){
					$('#weekn'+i).css('background','#efefef');
				}
			}else{
				weeksel=0;
				for(var i=1;i<=weeks1;i++){
					$('#weekn'+i).css('background','#efefef');	
				}
			}
		}
		function weekcheck6(){
			var checkbox = document.getElementById('lastfourteen');
			if(checkbox.checked){
				$('#custom').attr("checked",false);
				$('#singleweek').attr("checked",false);
				$('#doubleweek').attr("checked",false);
				$('#firstnine').attr("checked",false);
				$('#firstfourteen').attr("checked",false);
				//$('#lastfourteen').attr("checked",false);
				$('#lastnine').attr("checked",false);
				$('#allweek').attr("checked",false);
				weeksel=6;
				for(var i=5;i<=18;i++){
					$('#weekn'+i).css('background','#E46BA1');
				}
				for(var i=1;i<=4;i++){
					$('#weekn'+i).css('background','#efefef');
				}
				for(var i=19;i<=weeks1;i++){
					$('#weekn'+i).css('background','#efefef');
				}
			}else{
				weeksel=0;
				for(var i=1;i<=weeks1;i++){
					$('#weekn'+i).css('background','#efefef');	
				}
			}
		}
		function weekcheck7(){
			var checkbox = document.getElementById('lastnine');
			if(checkbox.checked){
				$('#custom').attr("checked",false);
				$('#singleweek').attr("checked",false);
				$('#doubleweek').attr("checked",false);
				$('#firstnine').attr("checked",false);
				$('#firstfourteen').attr("checked",false);
				$('#lastfourteen').attr("checked",false);
				//$('#lastnine').attr("checked",false);
				$('#allweek').attr("checked",false);
				weeksel=7;
				for(var i=10;i<=18;i++){
					$('#weekn'+i).css('background','#E46BA1');
				}
				for(var i=1;i<=9;i++){
					$('#weekn'+i).css('background','#efefef');
				}
				for(var i=19;i<=weeks1;i++){
					$('#weekn'+i).css('background','#efefef');
				}
			}else{
				weeksel=0;
				for(var i=1;i<=weeks1;i++){
					$('#weekn'+i).css('background','#efefef');	
				}
			}
		}
		function weekcheck8(){
			var checkbox = document.getElementById('allweek');
			if(checkbox.checked){
				$('#custom').attr("checked",false);
				$('#singleweek').attr("checked",false);
				$('#doubleweek').attr("checked",false);
				$('#firstnine').attr("checked",false);
				$('#firstfourteen').attr("checked",false);
				$('#lastfourteen').attr("checked",false);
				$('#lastnine').attr("checked",false);
				//$('#allweek').attr("checked",false);
				weeksel=8;
				for(var i=1;i<=18;i++){
					$('#weekn'+i).css('background','#E46BA1');
				}
				for(var i=19;i<=weeks1;i++){
					$('#weekn'+i).css('background','#efefef');
				}
			}else{
				weeksel=0;
				for(var i=1;i<=weeks1;i++){
					$('#weekn'+i).css('background','#efefef');	
				}
			}
		}
		
		
		
		//选择周次
		function chooseweek(id){
			if(weeksel==1){
				if(weekAllArray[id.substring(5,id.length)-1]==0){
					if(weekArray[(id.substring(5,id.length)-1)]==0){
						$('#'+id).css('background','#E46BA1');
						weekArray[(id.substring(5,id.length)-1)]=1;
					}else{
						$('#'+id).css('background','#efefef');
						weekArray[(id.substring(5,id.length)-1)]=0;
					}
				}	
			}
		}
		function weekmouseover(id){
			if(weeksel==1){
				if(weekAllArray[id.substring(5,id.length)-1]==0){
					if(weekArray[(id.substring(5,id.length)-1)]==0){
						$('#'+id).css('background','#E46BA1');
					}
				}
			}
		}
		function weekmouseout(id){
			if(weeksel==1){
				if(weekAllArray[id.substring(5,id.length)-1]==0){
					if(weekArray[(id.substring(5,id.length)-1)]==0){
						$('#'+id).css('background','#efefef');
					}	
				}
			}
		}
		
		//打开room编辑窗口
		function openroom(id){
			inputid=id;
			document.getElementById("tsc").focus(); 
			
			//初始化
			$('#chooseroom').attr("checked",false);
			$('#choosetype').attr("checked",false);
			$('#normalroom').numberbox('setValue','');
			$('#meidaroom').numberbox('setValue','');
			$('#normalroom').numberbox({ disabled: true });
			$('#meidaroom').numberbox({ disabled: true });
			$('#school').combobox('setValue','');
			$('#house').combobox('setValue','');
			$('#clstype').combobox('setValue','');
			
			var rombh=clsidarray[(inputid.substring(7,inputid.length)-1)];
			loadGridCls(rombh);
            schoolCombobox();
			houseCombobox();
			classtypeCombobox();
			$('#chooseroom').attr("checked","checked");
			chooseRoom();	
					//填充数据
					if($('#'+inputid).val()!=""&&clsidarray[(inputid.substring(7,inputid.length)-1)]!=""){
						var clsid=clsidarray[(inputid.substring(7,inputid.length)-1)].split("+");
						var clsna=clsarray[(inputid.substring(7,inputid.length)-1)].split("+");
						for(var i=0;i<clsid.length;i++){
							clsinfoidarray.push(clsid[i]);
							clsinfoarray.push(clsna[i]);				
						}
				 	}
					
					//alert("clsarray[i]:"+clsidarray[inputid.substring(7,inputid.length)-1]);
					if($('#'+inputid).val()!=""){
						var clsidname=clsidarray[(inputid.substring(7,inputid.length)-1)];
						if(clsidname.indexOf("+")>-1){
							var clsid=clsidname.split("+");
							if(clsid[0]=="5"||clsid[0]=="1"){//只选类型
								$('#choosetype').attr("checked","checked");
								chooseType();							
								var normalrooms=0;
								var mediarooms=0;
								for(var j=0;j<clsid.length;j++){
									if(clsid[j]=="5"){
										normalrooms++;
									}else{
										mediarooms++;
									}
								}
								$('#normalroom').numberbox('setValue',normalrooms);
								$('#meidaroom').numberbox('setValue',mediarooms);
							}else{//指定教室
								$('#chooseroom').attr("checked","checked");
								chooseRoom();
								var rObj=document.getElementsByName("roomall");
								for(var i=0;i<clsid.length;i++){
									var rObj=document.getElementsByName("roomall");
									for (var j = 0;j < rObj.length;j++) {
										var rObjid=rObj[j].hidename.split("|");
										if(rObjid[0]==clsid[i]){
											$('#room'+j).attr("checked","checked");
										}
									}	
								}
							}
						}else{
							if(clsidname=="5"||clsidname=="1"){//只选类型
								$('#choosetype').attr("checked","checked");
								chooseType();							
								var normalrooms=0;
								var mediarooms=0;
								if(clsidname=="5"){
									normalrooms++;
								}else{
									mediarooms++;
								}
								$('#normalroom').numberbox('setValue',normalrooms);
								$('#meidaroom').numberbox('setValue',mediarooms);
							}else{//指定教室
								$('#chooseroom').attr("checked","checked");
								chooseRoom();
								var rObj=document.getElementsByName("roomall");
								for (var j = 0;j < rObj.length;j++) {
									var rObjid=rObj[j].hidename.split("|");
									if(rObjid[0]==clsidname){
										$('#room'+j).attr("checked","checked");
									}
								}	
							}
						}
 					}else{//教室为空
 						
						clsinfoidarray.splice(0,clsinfoidarray.length);
						clsinfoarray.splice(0,clsinfoarray.length);
 					}		
			
			$('#room').show();
			
			$('#room').dialog("open");
		}
		
		function loadGridCls(rombh){
			$('#clstable').datagrid({
				url: '<%=request.getContextPath()%>/Svl_Skjh',
	 			queryParams: {"active":"openRoom","selschool":$('#school').combobox('getValue'),"selhouse":$('#house').combobox('getValue'),"seltype":$('#clstype').combobox('getValue'),"roomarr":rombh},
				loadMsg : "信息加载中请稍后!",//载入时信息
				width:566,
				height:311,
				rownumbers: true,
				animate:true,
				striped : true,//隔行变色
				pageSize : pageSize,//每页记录数
				singleSelect : true,//单选模式
				pageNumber : pageNum,//当前页码
				pagination:false,
				fit:false,
				fitColumns: true,//设置边距
				columns:[[
					{field:'1',title:'选择',width:30,align:'center',
						formatter:function(value,rec){
							var gh='<input type="checkbox" id="'+rec.教室编号+'" name="clscb" hidename="'+rec.教室名称+'" onclick="editClsInfo(this.id);" />';
							return gh;
						}
					},
					{field:'校区名称',title:'校区',width:80,align:'center'},
					{field:'建筑物名称',title:'教学楼',width:80,align:'center'},
					{field:'名称',title:'教室类型',width:80,align:'center'},
					{field:'教室名称',title:'教室',width:80,align:'center'}
				]],
				onClickRow:function(rowIndex, rowData){
					$('#clstable').datagrid("unselectRow", rowIndex);
				},
				onLoadSuccess: function(data){
					var rObj = document.getElementsByName("clscb");
					var checkbox = document.getElementById('chooseRoom');
					for (var i = 0;i < rObj.length;i ++) {
						if(checkbox.checked){
							$('#'+rObj[i].id).attr("disabled",false);
						}else{
							$('#'+rObj[i].id).attr("disabled",true);
						}					
					}
			   	
					if($('#'+inputid).val()!=""&&clsidarray[(inputid.substring(7,inputid.length)-1)]!=""){
						var clsid=clsidarray[(inputid.substring(7,inputid.length)-1)].split("+");
						for(var i=0;i<clsid.length;i++){
							$('#'+clsid[i]).attr("checked","checked");
						}
						for(var i=0;i<clsinfoidarray.length;i++){
							$('#'+clsinfoidarray[i]).attr("checked","checked");
						}
		 			}
				},
				onLoadError:function(none){
					
				}
			});
		}
		
		//保存选择的任课教师信息
		function editClsInfo(id){
			var checkbox = document.getElementById(id);
			if(checkbox.checked){//勾选
				clsinfoidarray.push(checkbox.id);
				clsinfoarray.push(checkbox.hidename);
			}else{//不勾选
				for(var i=0;i<clsinfoidarray.length;i++){
					if(checkbox.id==clsinfoidarray[i]){
						clsinfoidarray.splice(i,1);
						clsinfoarray.splice(i,1);
					}
				}
			}
		}
		
		//只选类型，指定教室
		function chooseType(){
			var rObj = document.getElementsByName("clscb");
			for (var i = 0;i < rObj.length;i ++) {
				$('#'+rObj[i].id).attr("disabled",true);				
			}
			clsinfoidarray.splice(0,clsinfoidarray.length);
			clsinfoarray.splice(0,clsinfoarray.length);
			var checkbox = document.getElementById('choosetype');
			if(checkbox.checked){
				roomsel=1;
				$('#chooseroom').attr("checked",false);
				$('#normalroom').numberbox({ disabled: false });
				$('#meidaroom').numberbox({ disabled: false });
				$('#school').combobox("disable");
				$('#house').combobox("disable");
				$('#clstype').combobox("disable");
				for(var i=0;i<roomnum;i++){
					$('#room'+i).attr("disabled","disabled");
				}
				$('#rooms').attr("disabled","disabled");
			}else{
				roomsel=0;
				$('#normalroom').numberbox('setValue','');
				$('#meidaroom').numberbox('setValue','');
				$('#normalroom').numberbox({ disabled: true });
				$('#meidaroom').numberbox({ disabled: true });				
			}
		}
		//指定教室
		function chooseRoom(){
			var checkbox = document.getElementById('chooseroom');
			if(checkbox.checked){
				roomsel=2;
				$('#normalroom').numberbox('setValue','');
				$('#meidaroom').numberbox('setValue','');
				$('#normalroom').numberbox({ disabled: true });
				$('#meidaroom').numberbox({ disabled: true });
				$('#choosetype').attr("checked",false);
				$('#school').combobox("enable");
				$('#house').combobox("enable");
				$('#clstype').combobox("enable");
				var rObj = document.getElementsByName("clscb");
				for (var i = 0;i < rObj.length;i ++) {
					$('#'+rObj[i].id).attr("disabled",false);				
				}
			}else{
				roomsel=0;
				$('#school').combobox("disable");
				$('#house').combobox("disable");
				$('#clstype').combobox("disable");
				var rObj = document.getElementsByName("clscb");
				for (var i = 0;i < rObj.length;i ++) {
					$('#'+rObj[i].id).attr("disabled",true);				
				}
				clsinfoidarray.splice(0,clsinfoidarray.length);
				clsinfoarray.splice(0,clsinfoarray.length);
			}
		}
		
		//打开专业选择框
		function openmajor(){
			$('#ic_zymc').show();
			$('#ic_zymc').dialog("open");
			loadGridMajor();
		}
		
		//显示专业
		function loadGridMajor(){
			$('#majorList').datagrid({
				url : '<%=request.getContextPath()%>/Svl_CourseSet?active=loadGridMajor&ZYDM='+$('#XX_ZYDM').textbox('getValue')+'&ZYMC='+encodeURI(encodeURI($('#XX_ZYMC').textbox('getValue'))+'&majorarr='+majorbh),
				title:'',
				width:'100%',
				nowrap: false,
				fit:true, //自适应父节点宽度和高度
				showFooter:true,
				striped:true,
				pagination:false,
				pageSize:pageSize,
				singleSelect:true,
				pageNumber:pageNum,
				rownumbers:true,
				fitColumns: true,
				columns:[[
					//field为读取数据的数据名，title为显示的数据名，width宽度设置，align数字在表格中显示的位置
					{field:'sele',title:'',width:fillsize(0.1),align:'center',
						formatter:function(value,rec,row){
							var sfksbox='<input type="checkbox" id="'+rec.专业代码+'" name="zybh" value="'+rec.专业名称+'" onclick="" />';
							//examinfoidarray.push(rec.授课计划明细编号);							
							return sfksbox;
						}
					},
					{field:'专业名称',title:'专业名称',width:fillsize(0.2),align:'center'}
				]],
				//双击某行时触发
				onDblClickRow:function(rowIndex,rowData){},
				//读取datagrid之前加载
				onBeforeLoad:function(){},
				//单击某行时触发
				onClickRow:function(rowIndex,rowData){
					
				},
				//加载成功后触发
				onLoadSuccess: function(data){
					var majorid=majorbh.substring(0,majorbh.length-1).split(",");
					for(var i=0;i<majorid.length;i++){
							$('#'+majorid[i]).attr("checked","checked");
					}
				}
			});
		};
		
		//加载下拉框数据
		function schoolCombobox(){
			$('#school').combobox({
				url:"<%=request.getContextPath()%>/Svl_Skjh?active=schoolCombobox",
				valueField:'comboValue',
				textField:'comboName',
				editable:false,
				panelHeight:'140', //combobox高度
				onLoadSuccess:function(data){
					//判断data参数是否为空
					if(data != ''){
						//初始化combobox时赋值
						$(this).combobox('setValue', '');
					}
					if(roomsel!=2){
						$('#school').combobox("disable");
					}
							
				},
				//下拉列表值改变事件
				onChange:function(data){
					loadGridCls();
				}
			});
		}
		
		//加载下拉框数据
		function houseCombobox(){
			$('#house').combobox({
				url:"<%=request.getContextPath()%>/Svl_Skjh?active=houseCombobox",
				valueField:'comboValue',
				textField:'comboName',
				editable:false,
				panelHeight:'140', //combobox高度
				onLoadSuccess:function(data){
					//判断data参数是否为空
					if(data != ''){
						//初始化combobox时赋值
						$(this).combobox('setValue', '');
					}
					if(roomsel!=2){
						$('#house').combobox("disable");
					}
					
				},
				//下拉列表值改变事件
				onChange:function(data){ 
					loadGridCls();
				}
			});
		}
		
		//加载下拉框数据
		function classtypeCombobox(){
			$('#clstype').combobox({
				url:"<%=request.getContextPath()%>/Svl_Skjh?active=classtypeCombobox",
				valueField:'comboValue',
				textField:'comboName',
				editable:false,
				panelHeight:'140', //combobox高度
				onLoadSuccess:function(data){
					//判断data参数是否为空
					if(data != ''){
						//初始化combobox时赋值
						$(this).combobox('setValue', '');
					}
					if(roomsel!=2){
						$('#clstype').combobox("disable");
					}
							
				},
				//下拉列表值改变事件
				onChange:function(data){
					loadGridCls();
				}
			});
		}
		
		//全选
		function chooseAll(){
			var checkbox = document.getElementById('rooms');
			if(checkbox.checked){
				$("[name='roomall']").attr("checked",'true');//全选
				
			}else{
				$("[name='roomall']").removeAttr("checked");//取消全选
				
			}	
		}
		
		//查询是否全选中,保存选中的教室
		function checkChoose(){
			var choosetag=0;
			var rObj = document.getElementsByName("roomall");
			for (var i = 0;i < rObj.length;i ++) {
				if(rObj[i].checked){
					choosetag++;
				}
			}
			if(choosetag==rObj.length){
				$("#rooms").attr("checked",true);
			}else{
				$("#rooms").attr("checked",false);
			}
		}
		
		
		//ArrayList
		function ArrayList(){
			 private:
			 this.buffer=new Array();
			 var args=ArrayList.arguments;
			 if(args.length>0) this.buffer=args[0];
			 this.length=this.buffer.length;
			
			
			 function ListIterator(table,len){
			
			        this.table=table;
			  this.len=len;                          
			        this.index=0;
			  
			  this.hasNext=hasNext;
			  function hasNext() {
			   return this.index< this.len;
			        }
			
			        this.next=next;
			  function next() { 
			   if(!this.hasNext())
			    throw "No such Element!";
			      return this.table[this.index++];
			        }
			    }
			 
			 public:
			 this.hashCode=hashCode;
			 function hashCode(){
			  var h=0;
			  for(var i=0;i<this.lengh;i++)
			   h+=this.buffer[i].hashCode();
			  return h;
			 }
			 
			 this.size=size;
			 function size(){
			  return this.length;
			 }
			
			 
			 this.clear=clear;
			 function clear(){
			  this.length=0;
			 }
			
			 
			 this.isEmpty=isEmpty;
			 function isEmpty(){
			  return this.length==0;
			 }
			 
			 
			 this.toArray=toArray;
			 function toArray(){
			  var copy=new Array();
			  for(var i=0;i<this.length;i++){
			   copy[i]=this.buffer[i];
			  }
			  return copy;
			 }
			
			 this.get=get;
			 function get(index){
			  if(index>=0 && index<this.length)
			   return this.buffer[index];
			  return null;
			 }
			
			 
			 this.remove=remove;
			 function remove(param){
			  var index=0;
			  
			  if(isNaN(param)){
			   index=this.indexOf(param);
			  }
			  else index=param;
			  
			  
			  if(index>=0 && index<this.length){
			   for(var i=index;i<this.length-1;i++)
			    this.buffer[i]=this.buffer[i+1];
			   this.length-=1;
			   return true;
			  }
			  else return false;
			 }
			 
			 this.add=add;
			 function add(){
			  var args=add.arguments;
			  if(args.length==1){
			   this.buffer[this.length++]=args[0];
			   return true;
			  }
			  else if(args.length==2){
			   var index=args[0];
			   var obj=args[1];
			   if(index>=0 && index<=this.length){
			    for(var i=this.length;i>index;i--)
			     this.buffer[i]=this.buffer[i-1];
			     this.buffer[i]=obj;
			    this.length+=1;
			    return true;
			   }
			  }
			  return false;
			 }
			
			 this.indexOf=indexOf;
			 function indexOf(obj){
			  for(var i=0;i<this.length;i++){
			   if(this.buffer[i].equals(obj)) return i;
			  }
			  return -1;
			 }
			
			 
			 this.lastIndexOf=lastIndexOf;
			 function lastIndexOf(obj){
			  for(var i=this.length-1;i>=0;i--){
			   if(this.buffer[i].equals(obj)) return i;
			  }
			  return -1;
			 }
			
			 this.contains=contains;
			 function contains(obj){
			  return this.indexOf(obj)!=-1;
			 }
			
			 this.equals=equals;
			 function equals(obj){
			  if(this.size()!=obj.size()) return false;
			  for(var i=0;i<this.length;i++){
			   if(!obj.contains(this.buffer[i])) return false;
			  }
			  return true;
			 }
			
			
			 this.addAll=addAll;
			 function addAll(list){
			  var mod=false;
			  for(var it=list.iterator();it.hasNext();){
			   var v=it.next();
			   if(this.add(v)) mod=true;
			  }
			  return mod;  
			 }
			 
			 this.containsAll=containsAll;
			 function containsAll(list){
			  for(var i=0;i<list.size();i++){
			   if(!this.contains(list.get(i))) return false;
			  }
			  return true;
			 }
			
			 this.removeAll=removeAll;
			 function removeAll(list){
			  for(var i=0;i<list.size();i++){
			   this.remove(this.indexOf(list.get(i)));
			  }
			 }
			 
			 
			 this.retainAll=retainAll;
			 function retainAll(list){
			  for(var i=this.length-1;i>=0;i--){
			   if(!list.contains(this.buffer[i])){
			    this.remove(i);
			   }
			  }
			 }
			
			 this.subList=subList;
			 function subList(begin,end){
			  if(begin<0) begin=0;
			  if(end>this.length) end=this.length;
			  var newsize=end-begin;
			  var newbuffer=new Array();
			  for(var i=0;i<newsize;i++){
			   newbuffer[i]=this.buffer[begin+i];
			  }
			  return new ArrayList(newbuffer);
			 }
			 
			 this.set=set;
			 function set(index,obj){
			  if(index>=0 && index<this.length){
			   temp=this.buffer[index];
			   this.buffer[index]=obj;
			   return temp;
			  }
			 }
			
			 this.iterator=iterator;
			 function iterator(){
			  return new ListIterator(this.buffer,this.length);
			 }
			 
		} 
		
	</script>
</html>