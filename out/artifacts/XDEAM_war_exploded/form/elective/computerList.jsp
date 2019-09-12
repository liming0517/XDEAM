<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%
	/**
		创建人：wangzh
		Create date: 2015.06.02
		功能说明：用于设置授课计划
		页面类型:列表及模块入口
		修订信息(有多次时,可有多个)
		修订日期: 2016.09.21
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
		<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/icon.css">
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
		<style type="text/css">
			#divPageMask2{background-color:#D2E0F2; filter:alpha(opacity=90);left:0px;top:0px;z-index:100;}
			#divPageMask3{background-color:#D2E0F2; filter:alpha(opacity=90);left:0px;top:0px;z-index:100;}
			#win td{height:30px;}
		</style>
	</head>
	<body class="easyui-layout" >
		<%-- 遮罩层 --%>
<!--     	<div id="divPageMask2" style="position:absolute;top:0px;left:0px;width:100%;height:100%;"> -->
<!--     	</div>		 -->
		
		<div data-options="region:'north'" title="" style="background:#fafafa;height:64px;">
			<table  class = "" width = "100%">
				<tr>
					<td>
						<a id="newComputer"  onclick="doToolbar(this.id)" class="easyui-linkbutton" href="javascript:void(0);" data-options="plain:true,iconCls:'icon-new'">新建</a>
						<a id="editComputer" onclick="doToolbar(this.id)" class="easyui-linkbutton" href="javascript:void(0);" data-options="plain:true,iconCls:'icon-edit'">编辑</a>
						<a id="deleteComputer" onclick="doToolbar(this.id)" class="easyui-linkbutton" href="javascript:void(0);" data-options="plain:true,iconCls:'icon-cancel'">删除</a>
					</td>
				</tr>
			</table>
			<table  class = "tablestyle" width = "100%">
				<tr>
					<td class="titlestyle">学年学期</td>
					<td >
						<select id="SC_XNXQ" name="SC_XNXQ" class="easyui-combobox" style="width:200px;"></select>
					</td>
					<td class="titlestyle">课程名称</td>
					<td >
						<input id="SC_KCMC" name="SC_KCMC" class="easyui-textbox" style="width:200px;"/>
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
		
		<div id="win" title="编辑" style="width:800px;height:200px;">
			<form id='fm' method='post' style="margin: 0px;">
				<table class = "tablestyle" width="800px" >
					<tr >
						<a href="#" class="easyui-linkbutton" id="saveClass" name="saveClass" iconCls="icon-save" plain="true" onClick="doToolbar(this.id)">保存</a>
					</tr>
					
					<tr>
						<td class="titlestyle" width="20%" >课程或考试名称</td>
						<td width="30%">
							<input name="XX_KCDM" id="XX_KCDM" class="easyui-textbox" style="width:230px;" />
							
						</td>
						<td class="titlestyle" width="20%" >学年学期</td>
						<td width="30%">
							<select id="XX_XNXQ" name="XX_XNXQ" class="easyui-combobox"  style="width:230px;"></select>
						</td>
					</tr>

					<tr>
						<td class="titlestyle" >上课日期</td>
						<td >
							<select name="XX_SKRQ" id="XX_SKRQ" class="easyui-combobox" panelHeight="auto" style="width:230px;">
							</select>
						</td>
						<td class="titlestyle" >上课时间（节数）</td>
						<td >
							<select name="XX_SKSJ" id="XX_SKSJ" class="easyui-combobox" panelHeight="auto" style="width:230px;" data-options="multiple:true" >
							</select>
						</td>
					</tr>
					<tr>
						<td class="titlestyle">班级</td>
						<td >
							<input name="XX_BJMC" id="XX_BJMC" class="easyui-textbox" style="width:230px;"/>
						</td>
						<td class="titlestyle" >总人数</td>
						<td >
							<input name="XX_BJRS" id="XX_BJRS" class="easyui-numberspinner" style="width:230px;" min="0" increment="1" precision="0" editable="true" />	
						</td>
					</tr>
					<tr>
						<td class="titlestyle" >学分</td>
						<td >
							<select name="XX_XUEF" id="XX_XUEF" class="easyui-textbox" panelHeight="auto" style="width:230px;">
							</select>
						</td>
						<td class="titlestyle" >总课时</td>
						<td >
							<select name="XX_ZOKS" id="XX_ZOKS" class="easyui-textbox" panelHeight="auto" style="width:230px;">
							</select>
						</td>
					</tr>
					<tr>
						<td class="titlestyle">任课教师</td>
						<td >
							<input name="XX_SKJS" id="XX_SKJS" class="easyui-textbox" style="width:230px;"/>
						</td>
						<td class="titlestyle">场地名称</td>
						<td onclick="openroom();">
							<input name="XX_CDMC" id="XX_CDMC" class="easyui-textbox" style="width:230px;" readonly="true"/>
						</td>
					</tr>
					<tr>
						<td class="titlestyle">授课周次</td>
						<td onclick="openweek();">
							<input name="XX_SKZC" id="XX_SKZC" class="easyui-textbox" style="width:230px;" readonly="true"/>
						</td>
						<td class="titlestyle" ></td>
						<td >
							
						</td>
					</tr>	

				</table>
				
				<!-- 隐藏属性,传参用 -->
				<input type="hidden" id="active" name="active" />
				<input type="hidden" id="XX_KCMC" name="XX_KCMC" /><!-- 课程代码 -->
				<input type="hidden" id="XX_JSBH" name="XX_JSBH" /><!-- 教师编号 -->
				<input type="hidden" id="XX_CDYQ" name="XX_CDYQ" /><!-- 场地要求 -->
				<input type="hidden" id="XX_ZYBH" name="XX_ZYBH" /><!-- 可选专业 -->
				<input type="hidden" id="XX_SJXL" name="XX_SJXL" /><!-- 时间序列 -->
				<input type="hidden" id="XX_WEEK" name="XX_WEEK" /><!-- 学期总周数 -->
				<input type="hidden" id="XX_XXKZBBH" name="XX_XXKZBBH" /><!-- 授课计划主表编号 -->
				<input type="hidden" id="XX_XXKMXBH" name="XX_XXKMXBH" /><!-- 授课计划明细编号  -->
				<input type="hidden" id="iUSERCODE" name="iUSERCODE" />
			</form>
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
			<div style="height:56px;">
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
						<a href="#" class="easyui-linkbutton" id="searchTea" name="searchTea" iconCls="icon-search" plain="true" onClick="doToolbar(this.id)">查询</a>
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
				<table id="weektable" class = "tablestyle" width="386px">
					<tr>
						<a href="#" class="easyui-linkbutton" id="submit4" name="submit4" iconCls="icon-submit" plain="true" onClick="doToolbar(this.id)">确定</a>
					</tr>					
				</table>
				<!-- 隐藏属性,传参用 -->
<!-- 				<input type="hidden" id="active" name="active" /> -->

			</form>
		</div>
		
		<div id="room" style="overflow:hidden;">
			<div >
				<table id="roomtable" class = "tablestyle" width="566px" >
					<tr>
						<a href="#" class="easyui-linkbutton" id="submit5" name="submit5" iconCls="icon-submit" plain="true" onClick="doToolbar(this.id)">确定</a>
					</tr>
<!-- 					<tr> -->
<!-- 						<td width="25%" align="center"><input type="checkbox" id="choosetype" name="" onclick="chooseType();" /> 只选类型</td> -->
<!-- 						<td width="25%"></td><td width="25%"></td><td width="25%"></td> -->
<!-- 					</tr> -->
<!-- 					<tr> -->
<!-- 						<td width="25%" align="center">普通教室</td> -->
<!-- 						<td width="25%" align="center"><input class="easyui-numberbox" id="normalroom" name="" max="10" /></td> -->
<!-- 						<td width="25%" align="center">多媒体教室</td> -->
<!-- 						<td width="25%" align="center"><input class="easyui-numberbox" id="meidaroom" name="" max="10" /></td> -->
<!-- 					</tr> -->
<!-- 					<tr> -->
<!-- 						<td width="25%" align="center"><input type="checkbox" id="chooseroom" name="" onclick="chooseRoom();" /> 指定教室</td> -->
<!-- 						<td width="25%"></td><td width="25%"></td><td width="25%"></td> -->
<!-- 					</tr> -->
					<tr>
<!-- 						<td width="20%" align="center">校区</td> -->
<!-- 						<td width="30%" align="center"><select id="school" name="" class="easyui-combobox" style="width:98%" panelHeight="auto"></select></td> -->
						<td width="40%" align="center">教室类型</td>
						<td width="60%" align="center"><select id="clstype" name="clstype" class="easyui-combobox" style="width:70%" panelHeight="auto"></select></td>
					</tr>
<!-- 					<tr> -->
<!-- 						<td width="25%" align="center">教学楼</td> -->
<!-- 						<td width="25%" align="center"><select id="house" name="" class="easyui-combobox" style="width:90%" panelHeight="auto"></select></td>				 -->
<!-- 					</tr> -->
				</table>
			</div>
			<div>
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
		
		<!-- 选课时间 -->
		<div id="elcTime" title="选课时间">
			<div style="height:30px;" >
				<table >
					<tr>
						<td ><a href="#" id="saveTime" class="easyui-linkbutton" plain="true" iconcls="icon-save" onClick="doToolbar(this.id);" title="">保存</a></td>	
					</tr>
				</table>
			</div>
			<div >
				<table id="electiveTime" style="width:100%;" class="tablestyle">
					<tr>
							<td class="titlestyle">学年学期编码</td>
							<td>
								<select name="GX_XQ" id="GX_XQ" class="easyui-combobox" panelHeight="auto" editable="false" style="width:150px;"  required="true">
									
								</select>
							</td>
							<td class="titlestyle">选课开始日期</td>
							<td>
								<input style="width:150px;" class="easyui-datebox" id="GX_XQKSSJ" name="GX_XQKSSJ" editable="false" required="true"/>
							</td>
							<td class="titlestyle">选课结束日期</td>
							<td>
								<input style="width:150px;" class="easyui-datebox" id="GX_XQJSSJ" name="GX_XQJSSJ" editable="false" required="true"/>
							</td>
					</tr>
					<tr>
						<td class="titlestyle" colspan=2></td>
							
						<td class="titlestyle">选课开始时间</td>
							<td>
								<input style="width:150px;" class="easyui-textbox" id="GX_XQKSXS" name="GX_XQKSXS"  required="true"/> 
						</td>
						<td class="titlestyle">选课结束时间</td>
						<td>
								<input style="width:150px;" class="easyui-textbox" id="GX_XQJSXS" name="GX_XQJSXS"  required="true"/> 
						</td>
					</tr>
				</table>
			</div>
		</div>		
		
		<div id="bxstudent" title="编辑" style="width:100%;">
			<div style="height:30px;">
				<table width="100%" class="tablestyle">
				<tr>
					<a href="#" class="easyui-linkbutton" id="bxadd" name="bxadd" iconCls="icon-add" plain="true" onClick="doToolbar(this.id)">添加</a>
					<a href="#" class="easyui-linkbutton" id="bxdel" name="bxdel" iconCls="icon-cancel" plain="true" onClick="doToolbar(this.id)">删除</a>
				</tr>
				<tr>
					<td width="10%" class="titlestyle">学年学期</td>
					<td  width="40%">
						<input style='width:200px;' class='easyui-combobox' id='ic_xnxq' name='ic_xnxq'/>
					</td>
											
					<td align="center" style='width:12%'>
						<a href="#" class="easyui-linkbutton" id="searchBXStu" name="searchBXStu" iconCls="icon-search" plain="true" onClick="doToolbar(this.id)">查询</a>
					</td>					
				</tr>
				</table>
			</div>
			<div style="">
				<table id="bxstutable" class = "tablestyle" width="100%"></table>
			</div>	
		</div>
		
		<div id="student" title="编辑" style="width:100%;">
			<div style="height:30px;">
				<table width="100%" class="tablestyle">
				<tr>
					<a href="#" class="easyui-linkbutton" id="addBXStu" name="addBXStu" iconCls="icon-submit" plain="true" onClick="doToolbar(this.id)">确定</a>
				</tr>
				<tr>
					<td width="10%" class="titlestyle">学号</td>
					<td  width="20%">
						<input style='width:160px;' class='easyui-textbox' id='ic_stuId' name='ic_stuId'/>
					</td>
					<td width="10%" class="titlestyle">姓名</td>
					<td  width="20%">
						<input style='width:160px;' class='easyui-textbox' id='ic_stuName' name='ic_stuName'/>
					</td>							
					<td align="center" style='width:12%'>
						<a href="#" class="easyui-linkbutton" id="searchStu" name="searchStu" iconCls="icon-search" plain="true" onClick="doToolbar(this.id)">查询</a>
					</td>					
				</tr>
				</table>
			</div>
			<div style="">
				<table id="stutable" class = "tablestyle" width="100%"></table>
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

		var weeks="";
		var weekall="";//总周次
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
		var weekArray="";//保存当前窗口第几周是否被选中
		var weekAllArray="";//保存所有窗口周次是否已经被选择
		var clsinfoidarray =new Array();//存放选择的场地编号
		var clsinfoarray =new Array();//存放选择的场地名称
		var linkArray="";//判断周次是否连续
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
		var classmc="";//班级名称
		var kclx="";//课程类型
		var savexqbh="";//保存传递过来的学期编码
		var paike="0";//排课状态
		var majorbh="";//可报名专业编号
		var skjhzbbh="";//授课计划主表编号
		var addbxstuarray=new Array();//添加补选学生编号
		var delbxstuarray=new Array();//删除补选学生编号
		var teabh="";//取input框内的教师编号
		var rombh="";//取input框内的教室编号
		var rommc="";
		
		$(document).ready(function(){
			
			loadDialog();
			initGridData();//页面初始化加载数据
			$('#tsc').hide();
			$('#teacher').hide();
			$('#week').hide();
			$('#room').hide();
			$('#ic_zymc').hide();
			//checkChoose();			
			initCombobox();
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
				url: '<%=request.getContextPath()%>/Svl_CourseSet?active=loadComputer&SC_XNXQ='+$('#SC_XNXQ').textbox('getValue')+'&SC_KCMC='+encodeURI($('#SC_KCMC').textbox('getValue')),
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
					{field:'机房明细编号',title:'机房明细编号',width:fillsize(0.2),align:'center'},
					{field:'XX_XNXQ',title:'学年学期编码',width:fillsize(0.2),align:'center',
						formatter:function(value,rec){
							var xnxqbm=rec.XX_XNXQ;
							var xnxq=xnxqbm.substring(0,4)+"学年第"+xnxqbm.substring(4,5)+"学期";
							return xnxq;
						}
					},
					{field:'XX_KCMC',title:'课程或考试名称',width:fillsize(0.2),align:'center'},
					{field:'班级名称',title:'班级名称',width:fillsize(0.2),align:'center'},
					{field:'XX_SKJS',title:'使用教师',width:fillsize(0.15),align:'center'},
					{field:'XX_CDMC',title:'使用教室',width:fillsize(0.1),align:'center'},			
					{field:'XX_SKZC',title:'授课周次',width:fillsize(0.1),align:'center',
						formatter:function(value,rec){
							var skzcxq2=rec.XX_SKZC;
							skzcxq2=skzcxq2.replace(new RegExp('odd','gm'),'单周');
							skzcxq2=skzcxq2.replace(new RegExp('even','gm'),'双周');
							return skzcxq2;
						}
					},	
					{field:'XX_SJXL',title:'使用时间',width:fillsize(0.15),align:'center',
						formatter:function(value,rec){
							if(rec.XX_SJXL==""||rec.XX_SJXL==undefined){
								return "";
							}
							var sksj=rec.XX_SJXL;
							var sksj2=sksj.split(",");
							var week=sksj2[0].substring(0,2);
							if(week=="01"){
								week="周一";
							}else if(week=="02"){
								week="周二";
							}else if(week=="03"){
								week="周三";
							}else if(week=="04"){
								week="周四";
							}else if(week=="05"){
								week="周五";
							}else if(week=="06"){
								week="周六";
							}else if(week=="07"){
								week="周日";
							}
							var apm=parseInt(sksj2[0].substring(2,4));
							var num="";
									
											
							if(apm<5){
								apm="上午";
							}else if(5<=apm&&apm<=6){
								apm="中午";
							}else if(6<apm&&apm<11){
								apm="下午";
							}else if(10<apm&&apm<13){
								apm="晚上";
							}else{
							
							}
							
							if(parseInt(sksj2[sksj2.length-1].substring(2,4))==0){
								num=parseInt(sksj2[0].substring(2,4));
							}else{
								num=parseInt(sksj2[0].substring(2,4))+"-"+parseInt(sksj2[sksj2.length-1].substring(2,4));
							}
							
// 							if(sksj2.length==1){
// 								num=parseInt(sksj.substring(2,4));
// 							}
							return week+apm+"第"+num+"节课";
						}
					},			
					{field:'XX_BJRS',title:'人数',width:fillsize(0.1),align:'center'}				
				]],
				onClickRow:function(rowIndex, rowData){
					row=rowData;
					iKeyCode=rowData.机房明细编号;
					teacherbh=rowData.XX_JSBH;//授课教师编号
					teacherxm=rowData.XX_SKJS;//授课教师姓名
					teachweek=rowData.XX_SKZC;//授课周次
					teachinfo=rowData.XX_SKZC;//授课周次详情
					spaceyq=rowData.XX_CDYQ;//场地要求
					spacemc=rowData.XX_CDMC;//场地名称
					classmc=rowData.班级名称;//班级名称
					majorbh=rowData.XX_ZYBH;//可报名专业编号
					coursename=rowData.XX_KCMC;//课程考试名称
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
				panelHeight:'100', //combobox高度
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
							weeks=data[0].MSG;
							weekall=data[0].MSG2;//总周次		
						}
					});
				}
			});
			
			$('#XX_SKRQ').combobox({
				url:"<%=request.getContextPath()%>/Svl_CourseSet?active=CP_SKRQCombobox",
				valueField:'comboValue',
				textField:'comboName',
				editable:false,
				panelHeight:'100', //combobox高度
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
				url:"<%=request.getContextPath()%>/Svl_CourseSet?active=CP_SKSJCombobox",
				valueField:'comboValue',
				textField:'comboName',
				editable:false,
				multiple:true,
				panelHeight:'100', //combobox高度
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
			
			$('#GX_XQ').combobox({
				url:"<%=request.getContextPath()%>/Svl_CourseSet?active=xnxqCombobox",
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
			
			$('#ic_xnxq').combobox({
				url:"<%=request.getContextPath()%>/Svl_CourseSet?active=xnxqCombobox",
				valueField:'comboValue',
				textField:'comboName',
				editable:false,
				panelHeight:'140', //combobox高度
				onLoadSuccess:function(data){
					if(data != ''){
						//初始化combobox时赋值
						$('#ic_xnxq').combobox('setValue', data[0].comboValue);
					}
				},
				//下拉列表值改变事件
				onChange:function(data){ 
					
				}
			});
		}
		
		function electiveTime(){
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_CourseSet',
				data : 'active=electiveTime',
				dataType:"json",
				success : function(data) {
					$('#GX_XQ').combobox('setValue', data[0].XNXQBM);
					$('#GX_XQKSSJ').datebox('setValue', data[0].KSXKSJ);
					$('#GX_XQJSSJ').datebox('setValue', data[0].JSXKSJ);
					$('#GX_XQKSXS').textbox('setValue', data[0].KSXKXS);
					$('#GX_XQJSXS').textbox('setValue', data[0].JSXKXS);
				}
			});
		}
		
		//删除学生
		function delbxstudent(){
			$.ajax({
					type : "POST",
					url : '<%=request.getContextPath()%>/Svl_CourseSet',
					data : 'active=delbxstudent&delbxstuarray='+delbxstuarray+'&XX_XNXQ='+$('#ic_xnxq').combobox('getValue'),
					dataType:"json",
					success : function(data){
						if(data[0].MSG=="删除成功"){
							showMsg(data[0].MSG);
							loadGridBX();
							$('#student').dialog('close');
							delbxstuarray.splice(0,delbxstuarray.length);
						}else{
							alertMsg(data[0].MSG);
						}
					}
				});
		}
		
		//工具按钮
		function doToolbar(iToolbar){
			if(iToolbar == "newComputer"){
				saveType = 'newComputer';
				$("#active").val(iToolbar);
				$('#win').dialog({   
					title: '课程或考试信息'
				});
				$('#win').dialog("open");
				$.parser.parse(('#win'));
				$('#XX_SKJS').textbox("setValue","");
				$('#XX_SKZC').textbox("setValue","");
				$('#XX_CDMC').textbox("setValue","");
						
				teaidarray.splice(0,teaarray.length);
				teaarray.splice(0,teaarray.length);
				wekidarray.splice(0,wekarray.length);
				wekarray.splice(0,wekarray.length);
				clsidarray.splice(0,clsarray.length);	
				clsarray.splice(0,clsarray.length);	
				clsidinfoarray.splice(0,clsinfoarray.length);	
				clsinfoarray.splice(0,clsinfoarray.length);	
				roomsel=0;
			}
			if(iToolbar == "editComputer"){
				saveType = 'editComputer';
				$("#active").val(iToolbar);
				roomsel=0;
				if(iKeyCode==""){
					alertMsg("请选择一行数据");
					return;
				}else{ 
					$('#win').dialog({   
						title: '课程或考试信息'
					});
					$('#win').dialog("open");
					if(row!=undefined && row!=''){ 
						$('#fm').form('load', row);
				
						var skzcweek=row.XX_SKZC;
						skzcweek=skzcweek.replace("odd","单周").replace("even","双周");
						$('#XX_SKZC').textbox("setValue",skzcweek);
						$('#XX_XUEF').textbox("setValue",row.XX_XUEF);
						$('#XX_ZOKS').textbox("setValue",row.XX_ZOKS);
						$('#XX_ZYNA').val(row.XX_ZYNA);
						var sjxl=row.XX_SJXL;
						var skrq=sjxl.substring(0,2);
						$('#XX_SKRQ').combobox('setValue',skrq);
						var sksj=","+sjxl;
						sksj=sksj.replace(new RegExp(','+skrq,'gm'),',');
						sksj=sksj.substring(1,sksj.length);
						$('#XX_SKSJ').combobox('setValues',sksj.split(','));
					}
					
					$('#XKBH').html(courseid);
					$('#XX_KCDM').textbox('setValue',coursename);//课程名称
					$('#XX_SKJS').val(teacherxm);//授课教师姓名
					$('#XX_SKZC').val(teachinfo);//授课周次
					$('#XX_CDMC').textbox('setValue',spacemc);//场地要求
					$('#XX_BJMC').textbox('setValue',classmc);//班级名称
					$('#XX_XXKMXBH').val(iKeyCode);//选修课明细编号

					teaidarray.splice(0,teaidarray.length);
					teaarray.splice(0,teaarray.length);
					wekidarray.splice(0,wekidarray.length);
					wekarray.splice(0,wekarray.length);
					clsidarray.splice(0,clsidarray.length);	
					clsarray.splice(0,clsarray.length);	
					clsinfoidarray.splice(0,clsinfoidarray.length);	
					clsinfoarray.splice(0,clsinfoarray.length);	

					$.ajax({
						type: "POST",
						url: '<%=request.getContextPath()%>/Svl_CourseSet',
						data: "active=getweeknum&XNXQBM=" + $('#XX_XNXQ').combobox('getValue') ,
						dataType: 'json',
						success: function(data){ 
							weeks=data[0].MSG;	
							weekall=data[0].MSG2;//总周次		
						}
					});
				}
			}
			//保存开班信息
			if(iToolbar == "saveClass"){
				if($('#XX_KCDM').textbox('getValue')==""){
					alertMsg("请输入课程名称");
					return;
				}	
				
				if($('#XX_XNXQ').combobox('getValue')==""){
					alertMsg("请选择学年学期");
					return;
				}	
				
				if($('#XX_SKSJ').combobox('getValue')==undefined){
					alertMsg("请选择上课时间");
					return;
				}	
				
				if($('#XX_SKJS').val()==""){
					alertMsg("请输入教师");
					return;
				}
				if($('#XX_CDMC').val()==""){
					alertMsg("请输入场地名称");
					return;
				}
				if($('#XX_SKZC').val()==""){
					alertMsg("请选择授课周次");
					return;
				}
				$('#XX_KCMC').val($('#XX_KCDM').textbox('getValue'));
				$('#XX_SJXL').val($('#XX_SKSJ').combobox('getText'));
				$('#XX_WEEK').val(weekall);
				$('#fm').submit();

			}
			//选课时间设置
			if(iToolbar == "electiveTime"){
				electiveTime();
				$('#elcTime').dialog("open");
			}
			//保存选课时间
			if(iToolbar == 'saveTime'){
				var xnxqbm=$("#GX_XQ").combobox('getValue');
				var beginDate = $("#GX_XQKSSJ").datebox('getValue');
				var endData = $("#GX_XQJSSJ").datebox('getValue');
				var beginHour= $("#GX_XQKSXS").textbox('getValue');
				var endHour = $("#GX_XQJSXS").textbox('getValue');
				
				//判断开始时间是否大于结束时间
				if(beginDate > endData){
					alertMsg("开始日期必须在结束日期之前");
					return;
				}
				
				if(beginHour > endHour){
					alertMsg("开始时间必须在结束时间之前");
					return;
				}
				
				$.ajax({
					type : "POST",
					url : '<%=request.getContextPath()%>/Svl_CourseSet',
					data : 'active=saveTime&XNXQBM='+xnxqbm+'&KSXKSJ='+beginDate+'&JSXKSJ='+endData+'&KSXKXS='+beginHour+'&JSXKXS='+endHour,
					dataType:"json",
					success : function(data) {
						$('#elcTime').dialog("close");
						showMsg(data[0].MSG);
					}
				});
				
			}
			//补选名单设置
			if(iToolbar == "byElection"){
				$('#bxstudent').dialog("open");
				loadGridBX();
			}
			//添加补选学生
			if(iToolbar == "bxadd"){
				$('#student').dialog("open");
				loadGridStu();
			}
			//删除补选学生
			if(iToolbar == "bxdel"){
				if(delbxstuarray.length==0){
					alertMsg("请选择学生");
					return;
				}
				ConfirmMsg('是否确定要删除所选学生', 'delbxstudent();', '');
			}
			//添加补选学生
			if(iToolbar == "addBXStu"){
				$.ajax({
					type : "POST",
					url : '<%=request.getContextPath()%>/Svl_CourseSet',
					data : 'active=addBXStu&addbxstuarray='+addbxstuarray+'&XX_XNXQ='+$('#ic_xnxq').combobox('getValue'),
					dataType:"json",
					success : function(data){
						if(data[0].MSG=="添加成功"){
							showMsg(data[0].MSG);
							loadGridBX();
							$('#student').dialog('close');
						}else{
							alertMsg(data[0].MSG);
						}
					}
				});
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
			if(iToolbar == "deleteComputer"){
				if(iKeyCode==""){
					alertMsg("请选择一行数据");
					return;
				}else{
					ConfirmMsg("是否确认删除该选修课？", "delelc();","");
						
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
					var link=0;
					var tag=0;
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
					
					wekidarray[0]=(html);//保存
					wekarray[0]=(html);//保存
				}else if(weeksel==0){
					var obj = document.getElementsByName("selweek");
					for(var i=0;i<obj.length;i++){
						var checkbox=document.getElementById(obj[i].id);
						if(checkbox.checked){//选中
							if(obj[i].id=="singleweek"){
								html="单周";
								var weekmax=0;
								if(weeks%2==0){
									weekmax=weeks-1;
								}else{
									weekmax=weeks;
								}
								wekidarray[0]=("odd");//保存
								wekarray[0]=("odd");//保存
							}else if(obj[i].id=="doubleweek"){
								html="双周";
								var weekmax=0;
								if(weeks%2==0){
									weekmax=weeks;
								}else{
									weekmax=weeks-1;
								}
								wekidarray[0]=("even");//保存
								wekarray[0]=("even");//保存
							}else{
								html=obj[i].value;
								wekidarray[0]=obj[i].value;//保存
								wekarray[0]=obj[i].value;//保存	
							}
						}
					}
				}
				//alert(inputid+"|"+wekarray[(inputid.substring(7,inputid.length)-1)]);
	
				$('#XX_SKZC').textbox('setValue',html);

				$('#week').dialog("close");
			}
			if(iToolbar == "submit5"){//场地			
				var roms="";
				var html="";
				rombh="";
				rommc="";
				
				for (var i = 0;i < clsinfoidarray.length;i++) {
					if(html==""){
						roms+=clsinfoidarray[i];
						html+=clsinfoarray[i];
					}else{
						roms+="+"+clsinfoidarray[i];
						html+="+"+clsinfoarray[i];
					}
				}
	
				clsidarray[0]=(roms);//保存
				clsarray[0]=(html);//保存
				clsinfoidarray.splice(0,clsinfoidarray.length);
				clsinfoarray.splice(0,clsinfoarray.length);
				
				$('#XX_CDYQ').val(roms);
				$('#XX_CDMC').textbox('setValue',html);
				rommc=html;
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
			if(iToolbar == "searchTea"){
				loadGridTea(teabh);
			}
			if(iToolbar == "searchSC"){
				loadGrid();
			}
			if(iToolbar == "searchMAJOR"){
				loadGridMajor();
			}
			if(iToolbar == "searchStu"){
				loadGridStu();
			}
			if(iToolbar == "searchBXStu"){
				loadGridBX();
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
			
		}
		
		//显示补选学生
		function loadGridBX(){
			$('#bxstutable').datagrid({
					url : '<%=request.getContextPath()%>/Svl_CourseSet?active=loadGridBX&XX_XNXQ='+$('#ic_xnxq').combobox('getValue'),
					loadMsg : "信息加载中请稍后!",//载入时信息
					width:886,
					height:407,
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
								var xh='<input type="checkbox" id="'+rec.学号+'" name="stucb" onclick="bxStuInfo(this.id);" />';
								return xh;
							}
						},
						{field:'学号',title:'学号',width:100,align:'center'},
						{field:'姓名',title:'姓名',width:100,align:'center'},
						{field:'行政班名称',title:'行政班名称',width:100,align:'center'}	
					]],
					onClickRow:function(rowIndex, rowData){
						$('#bxstutable').datagrid("unselectRow", rowIndex);
					},
					onLoadSuccess: function(data){	
						
					},
					onLoadError:function(none){
						
					}
			});
		};
		
		//显示所有学生
		function loadGridStu(){
			$('#stutable').datagrid({
					url : '<%=request.getContextPath()%>/Svl_CourseSet?active=loadGridStu&stuid='+$('#ic_stuId').textbox('getValue')+'&stuname='+encodeURI(encodeURI($('#ic_stuName').textbox('getValue')))+'&XX_XNXQ='+$('#ic_xnxq').combobox('getValue'),
					loadMsg : "信息加载中请稍后!",//载入时信息
					width:886,
					height:407,
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
								var xh='<input type="checkbox" id="'+rec.学号+'" name="stucb" onclick="addBXStu(this.id);" />';
								return xh;
							}
						},
						{field:'学号',title:'学号',width:100,align:'center'},
						{field:'姓名',title:'姓名',width:100,align:'center'},
						{field:'行政班名称',title:'行政班名称',width:100,align:'center'}	
					]],
					onClickRow:function(rowIndex, rowData){
						$('#stutable').datagrid("unselectRow", rowIndex);
					},
					onLoadSuccess: function(data){	
						$('#'+selStuid).attr("checked","checked");
					},
					onLoadError:function(none){
						
					}
			});
		};
		
		//选中学生
		function addBXStu(id){
			var checkbox = document.getElementById(id);
			if(checkbox.checked){//勾选
				addbxstuarray.push(id);
			}else{//不勾选
				for(var i=0;i<addbxstuarray.length;i++){
					if(checkbox.id==addbxstuarray[i]){
						addbxstuarray.splice(i,1);
					}
				}
			}
		}
		
		//选中要删除的学生
		function bxStuInfo(id){
			var checkbox = document.getElementById(id);
			if(checkbox.checked){//勾选
				delbxstuarray.push(id);
			}else{//不勾选
				for(var i=0;i<delbxstuarray.length;i++){
					if(checkbox.id==delbxstuarray[i]){
						delbxstuarray.splice(i,1);
					}
				}
			}
		}
		
		//删除选修课
		function delelc(){
			$.ajax({
				type: "POST",
				url: '<%=request.getContextPath()%>/Svl_CourseSet',
				data: "active=deleteComputer&skjhmxbh=" + iKeyCode,
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
						if(weeks%2==0){
							weekmax=weeks-1;
						}else{
							weekmax=weeks;
						}
						wekidarray[k]=("1|"+weekmax);//保存
					}else if(skzcinfo=="双周"){
						var weekmax=0;
						if(weeks%2==0){
							weekmax=weeks;
						}else{
							weekmax=weeks-1;
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
			url:'<%=request.getContextPath()%>/Svl_CourseSet',
			onSubmit:function(){
			
			},
			//提交成功
			success:function(datas){
				var json = eval("("+datas+")");
				if(json[0].MSG=="新建成功"||json[0].MSG=="编辑成功"){
					showMsg(json[0].MSG); 
					$('#win').dialog("close");
					loadGrid();	
				}else{
					alertMsg(json[0].MSG2);
				}
			}
		});
		
		//dialog窗口
		function loadDialog() {
			$('#win').dialog({
				width: 814,//宽度设置   
				height: 243,//高度设置
				modal : true,
				closed : true,
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
				width: 400,//宽度设置   
				height: 263,//高度设置 
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
				height: 425,//高度设置 
				modal:true,
				closed: true,   
				cache: false, 
				draggable:true,//是否可移动dialog框设置
				//读取事件
				onLoad:function(data){},
				//关闭事件
				onClose:function(data){
					clsinfoidarray.splice(0,clsinfoarray.length);
					clsinfoarray.splice(0,clsinfoarray.length);
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
			
			$('#elcTime').dialog({   
				title: '选课时间设置',   
				width: 800,//宽度设置   
				height: 119,//高度设置 
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
			
			$('#student').dialog({   
				title: '请选择补选学生',   
				width: 900,//宽度设置   
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
					$('#ic_stuId').textbox('setValue','');
					$('#ic_stuName').textbox('setValue','');
					addbxstuarray.splice(0,addbxstuarray.length);
				}
			});
			
			$('#bxstudent').dialog({   
				title: '请选择补选学生',   
				width: 900,//宽度设置   
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
					//doToolbar("stuinfo");
				}
			});
		}
		
		//初始化信息
		function emptyDialog(){
			$('#XX_KCDM').textbox('setValue', '');
			$('#XX_BJMC').textbox('setValue', '');
			$('#XKBH').html('');
			$('#XX_SKJS').textbox('setValue', '');
			$('#XX_CDMC').textbox('setValue', '');
			$('#XX_SKZC').textbox('setValue', '');
			$('#XX_XUEF').textbox('setValue', '');
			$('#XX_ZOKS').textbox('setValue', '');
			$('#XX_ZYNA').val('');
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
			teabh=teaidarray[(inputid.substring(7,inputid.length)-1)];		
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
		function openweek(){
			if($('#XX_XNXQ').combobox('getValue')==""){
				alertMsg("请先选择学年学期");
				return;
			}
			weekArray = new Array(weekall);//保存当前窗口第几周是否被选中
			weekAllArray = new Array(parseInt(weekall));//保存所有窗口周次是否已经被选择
			linkArray = new Array(parseInt(weekall));//判断周次是否连续
			
			var html1="";
			if(weeks==17){
				html1=  '<tr>'+
						'<a href="#" class="easyui-linkbutton" id="submit4" name="submit4" iconCls="icon-submit" plain="true" onClick="doToolbar(this.id)">确定</a>'+
					    '</tr>'+
					    '<tr>'+
							'<td width="25%" align="center"><input type="checkbox" id="custom" name="selweek" value="custom" onclick="weekcheck(this.id,this.value);" />自定义</td>'+
							'<td width="25%" align="center"><input type="checkbox" id="singleweek" name="selweek" value="singleweek" onclick="weekcheck(this.id,this.value);" /> 单周</td>'+
							'<td width="25%" align="center"><input type="checkbox" id="doubleweek" name="selweek" value="doubleweek" onclick="weekcheck(this.id,this.value);" /> 双周</td>'+
							'<td width="25%" align="center"> </td>'+
						'</tr>'+
						'<tr>'+
							'<td width="25%" align="center"><input type="checkbox" id="w0109" name="selweek" value="1-9" onclick="weekcheck(this.id,this.value);" /> 01-09</td>'+
							'<td width="25%" align="center"><input type="checkbox" id="w0209" name="selweek" value="2-9" onclick="weekcheck(this.id,this.value);" /> 02-09</td>'+
							'<td width="25%" align="center"><input type="checkbox" id="w0114" name="selweek" value="1-14" onclick="weekcheck(this.id,this.value);" /> 01-14</td>'+
							'<td width="25%" align="center"><input type="checkbox" id="w0214" name="selweek" value="2-14" onclick="weekcheck(this.id,this.value);" /> 02-14</td>'+
						'</tr>'+
						'<tr>'+
							'<td width="25%" align="center"><input type="checkbox" id="w0517" name="selweek" value="5-17" onclick="weekcheck(this.id,this.value);" /> 05-17</td>'+
							'<td width="25%" align="center"><input type="checkbox" id="w1017" name="selweek" value="10-17" onclick="weekcheck(this.id,this.value);" /> 10-17</td>'+
							'<td width="25%" align="center"><input type="checkbox" id="w0117" name="selweek" value="1-17" onclick="weekcheck(this.id,this.value);" /> 01-17</td>'+
							'<td width="25%" align="center"><input type="checkbox" id="w0217" name="selweek" value="2-17" onclick="weekcheck(this.id,this.value);" /> 02-17</td>'+
						'</tr>';
			}else if(weeks==19){
				html1=  '<tr>'+
						'<a href="#" class="easyui-linkbutton" id="submit4" name="submit4" iconCls="icon-submit" plain="true" onClick="doToolbar(this.id)">确定</a>'+
					    '</tr>'+
					    '<tr>'+
							'<td width="25%" align="center"><input type="checkbox" id="custom" name="selweek" value="custom" onclick="weekcheck(this.id,this.value);" />自定义</td>'+
							'<td width="25%" align="center"><input type="checkbox" id="singleweek" name="selweek" value="singleweek" onclick="weekcheck(this.id,this.value);" /> 单周</td>'+
							'<td width="25%" align="center"><input type="checkbox" id="doubleweek" name="selweek" value="doubleweek" onclick="weekcheck(this.id,this.value);" /> 双周</td>'+
							'<td width="25%" align="center"> </td>'+
						'</tr>'+
						'<tr>'+
							'<td width="25%" align="center"><input type="checkbox" id="w0109" name="selweek" value="1-9" onclick="weekcheck(this.id,this.value);" /> 01-09</td>'+
							'<td width="25%" align="center"><input type="checkbox" id="w0209" name="selweek" value="2-9" onclick="weekcheck(this.id,this.value);" /> 02-09</td>'+
							'<td width="25%" align="center"><input type="checkbox" id="w0114" name="selweek" value="1-14" onclick="weekcheck(this.id,this.value);" /> 01-14</td>'+
							'<td width="25%" align="center"><input type="checkbox" id="w0214" name="selweek" value="2-14" onclick="weekcheck(this.id,this.value);" /> 02-14</td>'+
						'</tr>'+
						'<tr>'+
							'<td width="25%" align="center"><input type="checkbox" id="w0519" name="selweek" value="5-19" onclick="weekcheck(this.id,this.value);" /> 05-19</td>'+
							'<td width="25%" align="center"><input type="checkbox" id="w1019" name="selweek" value="10-19" onclick="weekcheck(this.id,this.value);" /> 10-19</td>'+
							'<td width="25%" align="center"><input type="checkbox" id="w0119" name="selweek" value="1-19" onclick="weekcheck(this.id,this.value);" /> 01-19</td>'+
							'<td width="25%" align="center"><input type="checkbox" id="w0219" name="selweek" value="2-19" onclick="weekcheck(this.id,this.value);" /> 02-19</td>'+
						'</tr>';
			}else{
				html1=  '<tr>'+
						'<a href="#" class="easyui-linkbutton" id="submit4" name="submit4" iconCls="icon-submit" plain="true" onClick="doToolbar(this.id)">确定</a>'+
				    '</tr>'+
				    '<tr>'+
						'<td width="25%" align="center"><input type="checkbox" id="custom" name="selweek" value="custom" onclick="weekcheck(this.id,this.value);" />自定义</td>'+
						'<td width="25%" align="center"><input type="checkbox" id="singleweek" name="selweek" value="singleweek" onclick="weekcheck(this.id,this.value);" /> 单周</td>'+
						'<td width="25%" align="center"><input type="checkbox" id="doubleweek" name="selweek" value="doubleweek" onclick="weekcheck(this.id,this.value);" /> 双周</td>'+
						'<td width="25%" align="center"> </td>'+
					'</tr>'+
					'<tr>'+
						'<td width="25%" align="center"><input type="checkbox" id="w0109" name="selweek" value="1-9" onclick="weekcheck(this.id,this.value);" /> 01-09</td>'+
						'<td width="25%" align="center"><input type="checkbox" id="w0209" name="selweek" value="2-9" onclick="weekcheck(this.id,this.value);" /> 02-09</td>'+
						'<td width="25%" align="center"><input type="checkbox" id="w0114" name="selweek" value="1-14" onclick="weekcheck(this.id,this.value);" /> 01-14</td>'+
						'<td width="25%" align="center"><input type="checkbox" id="w0214" name="selweek" value="2-14" onclick="weekcheck(this.id,this.value);" /> 02-14</td>'+
					'</tr>'+
					'<tr>'+
						'<td width="25%" align="center"><input type="checkbox" id="w0518" name="selweek" value="5-18" onclick="weekcheck(this.id,this.value);" /> 05-18</td>'+
						'<td width="25%" align="center"><input type="checkbox" id="w1018" name="selweek" value="10-18" onclick="weekcheck(this.id,this.value);" /> 10-18</td>'+
						'<td width="25%" align="center"><input type="checkbox" id="w0118" name="selweek" value="1-18" onclick="weekcheck(this.id,this.value);" /> 01-18</td>'+
						'<td width="25%" align="center"><input type="checkbox" id="w0218" name="selweek" value="2-18" onclick="weekcheck(this.id,this.value);" /> 02-18</td>'+
					'</tr>';
			}
			
					
			var html2="";
			for(var i=1;i<=weekall;i=i+4){
				if((i+1)>weekall){
					html2+= '<tr>'+
							'<td width="25%" align="center" id="weekn'+i+'" style="cursor:pointer;" onmouseover="weekmouseover(this.id);" onmouseout="weekmouseout(this.id);" onclick="chooseweek(this.id);">'+i+'</td>'+
							'<td width="25%" align="center" ></td>'+
							'<td width="25%" align="center" ></td>'+
							'<td width="25%" align="center" ></td>'+
						'</tr>';
				}else if((i+2)>weekall){
					html2+= '<tr>'+
							'<td width="25%" align="center" id="weekn'+i+'" style="cursor:pointer;" onmouseover="weekmouseover(this.id);" onmouseout="weekmouseout(this.id);" onclick="chooseweek(this.id);">'+i+'</td>'+
							'<td width="25%" align="center" id="weekn'+(i+1)+'" style="cursor:pointer;" onmouseover="weekmouseover(this.id);" onmouseout="weekmouseout(this.id);" onclick="chooseweek(this.id);">'+(i+1)+'</td>'+
							'<td width="25%" align="center" ></td>'+
							'<td width="25%" align="center" ></td>'+
						'</tr>';
				}else if((i+3)>weekall){
					html2+= '<tr>'+
							'<td width="25%" align="center" id="weekn'+i+'" style="cursor:pointer;" onmouseover="weekmouseover(this.id);" onmouseout="weekmouseout(this.id);" onclick="chooseweek(this.id);">'+i+'</td>'+
							'<td width="25%" align="center" id="weekn'+(i+1)+'" style="cursor:pointer;" onmouseover="weekmouseover(this.id);" onmouseout="weekmouseout(this.id);" onclick="chooseweek(this.id);">'+(i+1)+'</td>'+
							'<td width="25%" align="center" id="weekn'+(i+2)+'" style="cursor:pointer;" onmouseover="weekmouseover(this.id);" onmouseout="weekmouseout(this.id);" onclick="chooseweek(this.id);">'+(i+2)+'</td>'+
							'<td width="25%" align="center" ></td>'+
						'</tr>';
				}else{
					html2+= '<tr>'+
							'<td width="25%" align="center" id="weekn'+i+'" style="cursor:pointer;" onmouseover="weekmouseover(this.id);" onmouseout="weekmouseout(this.id);" onclick="chooseweek(this.id);">'+i+'</td>'+
							'<td width="25%" align="center" id="weekn'+(i+1)+'" style="cursor:pointer;" onmouseover="weekmouseover(this.id);" onmouseout="weekmouseout(this.id);" onclick="chooseweek(this.id);">'+(i+1)+'</td>'+
							'<td width="25%" align="center" id="weekn'+(i+2)+'" style="cursor:pointer;" onmouseover="weekmouseover(this.id);" onmouseout="weekmouseout(this.id);" onclick="chooseweek(this.id);">'+(i+2)+'</td>'+
							'<td width="25%" align="center" id="weekn'+(i+3)+'" style="cursor:pointer;" onmouseover="weekmouseover(this.id);" onmouseout="weekmouseout(this.id);" onclick="chooseweek(this.id);">'+(i+3)+'</td>'+
						'</tr>';
				}
				
			}
			$("#weektable").html(html1+html2);
			$.parser.parse(('#weektable'));
	
			$('#week').show();
			
			//初始化
			weeksel=0;
			//weekArray.splice(0,weekArray.length);  //清空选择
			var obj = document.getElementsByName("selweek");
			for(var i=0;i<obj.length;i++){
				$('#'+obj[i].id).attr("checked",false);
			}	
			for(var i=1;i<weekall;i++){
				$('#weekn'+i).css('background','#efefef');	
				weekArray[(i-1)]=0;
			}
			$('#week').dialog("open");

			//alert("wekarray[i]:"+wekarray[inputid.substring(7,inputid.length)-1]);
			if($('#XX_SKZC').val()!=""){
				if(wekarray[0]=="odd"){
					$('#singleweek').attr("checked","checked");
					weekcheck("singleweek","singleweek");
				}else if(wekarray[0]=="even"){
					$('#doubleweek').attr("checked","checked");
					weekcheck("doubleweek","doubleweek");
				}else{
					var weeknum=wekarray[0]+"";
					var weeknums="";
					weeksel=1;
					if(weeknum.indexOf("#")>-1){//不连续
						$('#custom').attr("checked","checked");							
						weeknums=weeknum.split("#");
						for(var i=0;i<weeknums.length;i++){
							$('#weekn'+weeknums[i]).css('background','#E46BA1');
							weekArray[(weeknums[i]-1)]=1;						
						}
					}else if(weeknum.indexOf("-")>-1){//连续
						for(var j=0;j<obj.length;j++){
							if(obj[j].value==weeknum){//weeknum等于1-9这类选择
								$('#'+obj[j].id).attr("checked","checked");	
								weeksel=0;
								weeknums=weeknum.split("-");
								for(var i=parseInt(weeknums[0]);i<=parseInt(weeknums[1]);i++){
									$('#weekn'+i).css('background','#E46BA1');
									weekArray[(i-1)]=1;		
								}
							}
						}
						if(weeksel!=0){
							$('#custom').attr("checked","checked");	
							weeksel=1;
							weeknums=weeknum.split("-");
							for(var i=parseInt(weeknums[0]);i<=parseInt(weeknums[1]);i++){
								$('#weekn'+i).css('background','#E46BA1');
								weekArray[(i-1)]=1;		
							}
						}
					}else{
						$('#custom').attr("checked","checked");	
						$('#weekn'+weeknum).css('background','#E46BA1');
						weekArray[(weeknum-1)]=1;
					}
				}
 			}
 			
 			//清空weekAllArray重新赋值
 			for(var i=0;i<weekall;i++){
				weekAllArray[i]=0;
			}
			
		}
		
		//自定义，单周，双周选择
		function weekcheck(skzsid,skzsvalue){
			var checkbox=document.getElementById(skzsid);
			if(checkbox.checked){//选中
				if(skzsid=="custom"){
					weeksel=1;
					for(var i=1;i<=weekall;i++){
						$('#weekn'+i).css('background','#efefef');	
						weekArray[(i-1)]=0;
					}
				}else if(skzsid=="singleweek"){
					weeksel=0;
					for(var i=1;i<=weeks;i++){
						if(i%2==0){
							$('#weekn'+i).css('background','#efefef');
							weekArray[(i-1)]=0;
						}else{
							$('#weekn'+i).css('background','#E46BA1');
							weekArray[(i-1)]=1;
						}	
					}
				}else if(skzsid=="doubleweek"){
					weeksel=0;
					for(var i=1;i<=weeks;i++){
						if(i%2==0){
							$('#weekn'+i).css('background','#E46BA1');
							weekArray[(i-1)]=1;
						}else{
							$('#weekn'+i).css('background','#efefef');
							weekArray[(i-1)]=0;
						}	
					}
				}else{//选择的周次范围
					weeksel=0;
					for(var i=1;i<parseInt(skzsvalue.split("-")[0]);i++){
						$('#weekn'+i).css('background','#efefef');
						weekArray[(i-1)]=0;
					}
					for(var i=parseInt(skzsvalue.split("-")[0]);i<=parseInt(skzsvalue.split("-")[1]);i++){
						$('#weekn'+i).css('background','#E46BA1');
						weekArray[(i-1)]=1;
					}
					for(var i=parseInt(skzsvalue.split("-")[1])+1;i<=weekall;i++){
						$('#weekn'+i).css('background','#efefef');
						weekArray[(i-1)]=0;
					}
				}
			}else{//取消选中
				weeksel=0;
				for(var i=1;i<=weekall;i++){
					$('#weekn'+i).css('background','#efefef');	
				}
			}
			var obj = document.getElementsByName("selweek");
			for(var i=0;i<obj.length;i++){
				if(obj[i].id!=skzsid){
					$('#'+obj[i].id).attr("checked",false);
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
		function openroom(){ 
			//初始化
			//$('#chooseroom').attr("checked",false);
			$('#choosetype').attr("checked",false);
			$('#normalroom').numberbox('setValue','');
			$('#meidaroom').numberbox('setValue','');
			$('#normalroom').numberbox({ disabled: true });
			$('#meidaroom').numberbox({ disabled: true });
			$('#school').combobox('setValue','');
			$('#house').combobox('setValue','');
			$('#clstype').combobox('setValue','');
			
			//填充数据			
			if(saveType=="newComputer"){
				rommc="";
			}else{
				rommc=$('#XX_CDMC').textbox('getValue');
				if(rombh!=undefined){
					var selromid=rombh.split("+");
					var selrommc=rommc.split("+");
					for(var i=0; i<selrommc.length; i++){
						clsinfoidarray.push(selromid[i]);
						clsinfoarray.push(selrommc[i]);				
					}
				}
			}
			
			loadGridCls();
			classtypeCombobox();
			
            //schoolCombobox();
			//houseCombobox();
			
			//$('#chooseroom').attr("checked","checked");
			chooseRoom();
			
			$('#room').show();
			
			$('#room').dialog("open");
		}
		
		function loadGridCls(){ 
			isLoad = true;
			$('#clstable').datagrid({
				url: '<%=request.getContextPath()%>/Svl_Skjh',
	 			queryParams: {"active":"openComRoom","seltype":$('#clstype').combobox('getValue'),"roomarr":rommc},
				loadMsg : "信息加载中请稍后!",//载入时信息
				width:566,
				height:336,
				rownumbers: true,
				animate:true,
				striped : true,//隔行变色
				pageSize : pageSize,//每页记录数
				singleSelect : false,//单选模式
				pageNumber : pageNum,//当前页码
				pagination:false,
				fit:false,
				fitColumns: true,//设置边距
				columns:[[
					{field:'ckr',checkbox:true},
					{field:'教室编号',title:'教室编号',width:60,align:'center'},
					{field:'教室名称',title:'教室名称',width:80,align:'center'},
					{field:'名称',title:'教室类型',width:60,align:'center'},
					{field:'实际容量',title:'教室容量（人）',width:60,align:'center'}
				]],
				onSelect:function(rowIndex,rowData){
					if(isLoad == false){
						var same=0;	
						$.each(clsinfoarray, function(key,value){
							if(value == rowData.教室名称){
								same=1;
							}
						});
						if(same==0){
							clsinfoidarray.push(rowData.教室编号);
							clsinfoarray.push(rowData.教室名称);	
							rommc+="+"+rowData.教室名称;		
						}
					}
				},
				onUnselect:function(rowIndex,rowData){
					$.each(clsinfoarray, function(key,value){
						if(value == rowData.教室名称){
							clsinfoidarray.splice(key, 1);
							clsinfoarray.splice(key, 1);
						}
					});
					rommc="";
					for(var i=0;i<clsinfoarray.length;i++){
						rommc+=clsinfoarray[i]+"+";
					}
					if(rommc!=""){
						rommc=rommc.substring(0,rommc.length-1);
					}	
				},
				onLoadSuccess: function(data){
					$(".datagrid-header-check").html('&nbsp;');//隐藏全选
					//勾选已选教室
					if(data){
						if(rommc!=undefined){ 
							var selrommc=rommc.split("+");
							$.each(data.rows, function(rowIndex, rowData){
								for(var i=0; i<selrommc.length; i++){
									if(selrommc[i] == rowData.教室名称){
										$('#clstable').datagrid('selectRow', rowIndex);
									}
								}
							});
						}
					}
												
					isLoad = false;
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
				//$('#chooseroom').attr("checked",false);
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
					//loadGridCls();
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
					//loadGridCls();
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
				asy:false,
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