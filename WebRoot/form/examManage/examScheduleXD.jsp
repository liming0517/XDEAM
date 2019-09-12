<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="java.util.Vector"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.src.develop.store.user.*"%>
<%
	/**
		创建人：yeq
		Create date: 2015.05.26
		功能说明：用于设置排课
		页面类型:列表及模块入口
		修订信息(有多次时,可有多个)
		修订日期:
		原因:
		修订人:
		修订时间:
	**/
 %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
  <head>
	<title>考试安排</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/icon.css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/pubTimetable.js"></script>
	<style>
		body{
			moz-user-select: -moz-none; 
			-moz-user-select: none; 
			-o-user-select:none; 
			-khtml-user-select:none; /* you could also put this in a class */ 
			-webkit-user-select:none;/* and add the CSS class here instead */ 
			-ms-user-select:none; 
			user-select:none;/**禁止选中文字*/ 
		}
		
		.kcbTdStyle{
			font-size:16;
			border-left:1px solid #95B8E7;
			border-bottom:1px solid #95B8E7;
			background-color:#FFFFFF;
			text-align:center;
		}
		
		.kcbTitle{
			 font-weight:bold;
			 font-size:16;
		}
		
		.titleBG{
			background-color:#EFEFEF;
		}
		
		.wpkcTdStyle{
			font-size:12;
			border-left:1px solid #95B8E7;
			border-bottom:1px solid #95B8E7;
			background-color:#FFFFFF;
			text-align:center;
		}
		
		.titleTdStyle{
			font-size:16;
			border-left:1px solid #95B8E7;
			border-bottom:1px solid #95B8E7;
			text-align:center;
		}
		
		.splitTd{
			border-bottom:4px double #95B8E7;
		}
		
		.maskStyle{
			width:100%;
			height:100%;
			overflow:hidden;
			display:none;
			background-color:#D2E0F2;
			filter:alpha(opacity=90);
			position:absolute;
			top:0px;
			left:0px;
			z-index:100;
		}
		#maskFont{
			 font-size:16px;
			 color:#2B2B2B;
			 width:200px;
			 height:100%;
			 position:absolute;
			 top:50%;
			 left:50%;
			 margin-top:-10px;
			 margin-left:-80px;
		}
		#maskFont2{
			 font-size:16px;
			 font-weight:bold;
			 color:#2B2B2B;
			 width:200px;
			 height:100%;
			 position:absolute;
			 top:50%;
			 left:50%;
			 margin-top:-10px;
			 margin-left:-80px;
		}
		#divPageMask2{background-color:#D2E0F2; filter:alpha(opacity=50);left:0px;top:0px;z-index:100;}
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
<body class="easyui-layout" onselectstart="return (event.srcElement.type=='text');">
	<div id="loadingMask" style="width:110%; height:100%; background-color:#FFFFFF; position:absolute; z-index:99999; top:0;"></div>
	<div id="divPageMask2" class="maskStyle">
			<div id="maskFont2"></div>
	</div>
	<form id="form1" method="post">
		<div id="north" region="north" title="考试安排" style="height:87px;" >
			<table>
				<tr>
					<td><a href="#" id="apkc" class="easyui-linkbutton" plain="true" iconcls="icon-new" onClick="doToolbar(this.id);" style="display:none;">排监考教师</a></td>
					<td><a href="#" id="pubConfirm" class="easyui-linkbutton" plain="true" iconcls="icon-submit" onClick="doToolbar(this.id);" style="display:none;">公共课确认</a></td>
					<td><a href="#" id="delks" class="easyui-linkbutton" plain="true" iconcls="icon-cancel" onClick="doToolbar(this.id);" style="display:none;">删除</a></td>
					<td><a href="#" id="semCourse" class="easyui-linkbutton" plain="true" iconcls="icon-add" onClick="doToolbar(this.id);" style="display:none;">添加学期课程</a></td>
					<!-- <td><a href="#" id="addZkbxx" class="easyui-linkbutton" plain="true" iconcls="icon-add" onClick="doToolbar(this.id);">添加周课表信息</a></td> -->
					<td style="height:28px;">&nbsp;</td>
				</tr>
			</table>
			<table id="ee" singleselect="true" width="100%" class="tablestyle">
				<tr>
					<td class="titlestyle">学年学期</td>
					<td>
						<select id="XNXQ" name="XNXQ" class="easyui-combobox" style="width:180px;"></select>
					</td>
					<td class="titlestyle">教学性质</td>
					<td>
						<select id="JXXZ" name="JXXZ" class="easyui-combobox" style="width:180px;"></select>
					</td>
						
				</tr>
		    </table>
		</div>
		<div region="center">
			<table id="semesterList" style="width:100%;"></table>
		</div>
		
		<input type="hidden" id="active" name="active"/>
		<input type="hidden" id="kcbInfo" name="kcbInfo"/>
		<input type="hidden" id="wpkcInfo" name="wpkcInfo"/>
		<input type="hidden" id="PK_XNXQBM" name="PK_XNXQBM"/>
		<input type="hidden" id="PK_XZBDM" name="PK_XZBDM"/>
		<input type="hidden" id="PK_SJXL" name="PK_SJXL"/>
		<input type="hidden" id="PK_KCBMXBH" name="PK_KCBMXBH"/>
		<input type="hidden" id="PK_SKJHMXBH" name="PK_SKJHMXBH"/>
		<input type="hidden" id="PT_SKJSBH" name="PT_SKJSBH"/>
		<input type="hidden" id="PT_SKJSMC" name="PT_SKJSMC"/>
		<input type="hidden" id="PT_CDBH" name="PT_CDBH"/>
		<input type="hidden" id="PT_CDMC" name="PT_CDMC"/>
		<input type="hidden" id="PT_SKZC" name="PT_SKZC"/>
		<input type="hidden" id="xqzc" name="xqzc"/>
	</form>
	
	<!-- 排课 -->
	<div id="courseArrange" style="overflow:hidden;">
		<table style="width:100%;" class="tablestyle">
			<tr>
				<td style="width:35%;" class="titlestyle">学年</td>
				<td>
					<select name="PK_XN" id="PK_XN" class="easyui-combobox" style="width:180px;">
					</select>
				</td>
			<tr>
			<tr>
				<td style="width:35%;" class="titlestyle">学期</td>
				<td>
					<select name="PK_XQ" id="PK_XQ" class="easyui-combobox" style="width:180px;" disabled="disabled">
						<option value="">请先选择学年</option>
					</select>
				</td>
			<tr>
			<tr>
				<td class="titlestyle">教学性质</td>
				<td>
					<select name="PK_JXXZ" id="PK_JXXZ" class="easyui-combobox" style="width:180px;" disabled="disabled">
						<option value="">请先选择学年学期</option>
					</select>
				</td>
			<tr>
			<tr style="">
				<td class="titlestyle">考试名称</td>
				<td>
					<select name="KC_QZQM" id="KC_QZQM" class="easyui-combobox" style="width:180px;" disabled="disabled">
						<option value="">请先选择教学性质</option>
					</select>
				</td>
			<tr>
			<tr style="">
				<td class="titlestyle">系部名称</td>
				<td>
					<select name="KC_XBDM" id="KC_XBDM" class="easyui-combobox" style="width:180px;" disabled="disabled">
						<option value="">请先选择教学性质</option>
					</select>
				</td>
			<tr>
			<tr>
				<td colspan="2" style="text-align:center;">
					<a id="start" href="#" onclick="checkSemesterExist();" class="easyui-linkbutton" plain="true" iconcls="icon-bf">开始排监考教师</a>
				</td>				
			</tr>
	    </table>
	</div>
	
	<!-- 课程表详情页面 -->
	<!-- 按考试名称查询考试信息 -->
	<div id="classTimetable" style="">
		<div class="easyui-layout" style="height:100%; width:100%;">
			<div region="west" title="班级" style="width:15%;" border="true">
					<ul id="classTree" class="easyui-tree" style="display: none">
					</ul>
			</div> 
			<div region="center" style="overflow:hidden;">
					<%-- 遮罩层 --%>
					<div id="divPageMask" class="maskStyle">
				  		<div id="maskFont">信息加载中...</div>
				   	</div>
					<div id="kcbMain" style="width:100%; height:100%;" class="easyui-layout">
<!-- 						<div id="north" region="north" title="考试安排" style="height:61px;" > -->
<!-- 							<table style="width:100%;"> -->
<!-- 								<tr> -->
<!-- 									<td style="width:340px;"> -->
<!-- 										<a href="#" id="saveKCAP" class="easyui-linkbutton" plain="true" iconcls="icon-save" onClick="doToolbar(this.id);" title="" disabled="disabled">保存</a>														 -->
<!-- 									</td> -->
<!-- 								</tr> -->
<!-- 							</table> -->
<!-- 						</div> -->
						<div region="center" id="kcbContent" style="overflow-x:hidden;">
						</div>
<!-- 						<div style="height:150px; overflow:hidden;" region="south" title="未设置课程" id="wpkcContent"> -->
<!-- 						</div> -->
				</div>
			</div>	
		</div>		
	</div>
	
	<!-- 更改监考教师 -->
	<div id="editTeaDialog">
		<div style="width:100%; height:100%;" class="easyui-layout">
			<%-- 遮罩层 --%>
	    	<div id="changeCourseMask" class="maskStyle">
	    		<div class="maskFont" style="width:100px; font-size:12px; margin-left:-50px; margin-top:-5px;">课程信息保存中...</div>
	    	</div>
			<div region="north" style="height:34px;" >
				<table>
					<tr>
						<td>
							<a href="#" id="saveJKJSInfo" class="easyui-linkbutton" plain="true" iconcls="icon-save" onClick="doToolbar(this.id);" title="">保存</a>
						</td>
					</tr>
				</table>
			</div>
			<div region="center">
				<table id="courseList" singleselect="true" style="width:100%;" class="tablestyle"></table>
			</div>
		</div>
	</div>	
	
	<!-- 补考详情页面 -->
	<div id="classBUKAOtable">
		<div style="width:100%; height:100%;" class="easyui-layout">
			<div id="north" region="north" style="height:33px;" >
				<table style="width:100%;" class="tablestyle" >
					<tr>
						<td class="titlestyle">课程名称</td>
						<td>
							<input id="BK_KCMC" name="BK_KCMC" class="easyui-textbox" style="width:180px;" />
						</td>
						<td class="titlestyle">考试形式</td>
						<td>
							<select id="BK_KSXS" name="BK_KSXS" class="easyui-combobox" style="width:180px;"></select>
						</td>	
						<td style="width:80px;" align="center">
							<a href="#" onclick="doToolbar(this.id)" id="queryBKinfo" class="easyui-linkbutton" plain="true" iconcls="icon-search">查询</a>
						</td>
					</tr>
				</table>
			</div>
			<div region="center" >
				<table id="bukaoList" style="width:100%;"></table>
			</div>
		</div>
	</div>
	
	<!-- 自动排课进度条 -->
	<div id="maskDiv">
		<div style="width:100%; height:100%; text-align:center; overflow:hidden;">
			<div style="width:100%; height:90px; margin-top:65px;">
				<div id="pkTips" style="height:24px;font-size:24px;">正在初始化课程表信息...</div>
<!-- 				<div style="height:36px; margin-top:4px;"> -->
<!-- 					<div id="pkClass">&nbsp;</div> -->
<!-- 					<div id="pkCourse">&nbsp;</div> -->
<!-- 				</div> -->
<!-- 				<div style="border:1px solid #95B8E7; width:310px; height:30px; text-align:left;"> -->
<!-- 					<div id="progressBar" style="background:#7F99BE; width:0px; height:26px; margin-top:1px; margin-left:1px;"> -->
<!-- 						<span id="percent" style="width:310px; height:26px; position:absolute; top:50%; left:50%; margin-top:23px;">0%</span> -->
<!-- 					</div> -->
<!-- 				</div> -->
			</div>
		</div>
	</div>
	
	<!-- 自动排课未全部完成提示 -->
	<div id="compPartTips">
		<div style="width:100%; height:100%;" class="easyui-layout">
			<div id="north" region="north" style="height:230px; text-align:center;">
				<div style="width:100%; height:100%; overflow-y:auto; text-align:center;">
					<div id="wpkcTips" style="width:98%; text-align:left;"></div>
				</div>
			</div>
			<div region="center" style="text-align:center;">
				<table style="width:100%; height:55px; margin-top:5px; text-align:left; font-size:12px;">
					<tr>
						<td>
							&nbsp;&nbsp;忽略下列限制条件，继续自动排课！
						</td>
					</tr>
					<tr>
						<td>
							&nbsp;&nbsp;<input id="kcjp" name="kcjp" type="checkbox"/><label for="kcjp">&nbsp;课程禁排</label>
							<input style="margin-left:20px;" id="tsgz" name="tsgz" type="checkbox"/><label for="tsgz">&nbsp;特殊规则</label>
						</td>
					</tr>
				</table>
				<table style="width:100%; border-top:1px solid #95B8E7;">
					<tr>
						<td style="text-align:center;">
							<a href="#" onclick="reCourseArrange('1');" class="easyui-linkbutton" plain="true" iconcls="icon-bf">重新排课</a>
							<a style="margin-left:10px;" href="#" onclick="reCourseArrange('2');" class="easyui-linkbutton" plain="true" iconcls="icon-bf">继续排课</a>
						</td>
					</tr>
				</table>
			</div>
		</div>
	</div>
		
	
	<!-- 更改课程信息授课教师列表 -->
	<div id="teaListDialog">
		<div class="easyui-layout" style="width:100%; height:100%;">
			<div region="north" style="height:64px;">
				<table>
					<tr>
						<td><a href="#" id="confirmTea" class="easyui-linkbutton" plain="true" iconcls="icon-ok" onClick="doToolbar(this.id);">确认</a></td>
					</tr>
				</table>
				<table id="ee" width="100%" class="tablestyle">
					<tr>
						<td width="80px" class="titlestyle">教师工号</td>
						<td width="135px">
							<input style='width:100%;' class="easyui-textbox" id='ic_teaCode' name='ic_teaCode'/>
						</td>
						<td width="80px" class="titlestyle">教师姓名</td>
						<td  width="135px">
							<input style='width:100%;' class="easyui-textbox" id='ic_teaName' name='ic_teaName'/>
						</td>	
<!-- 						<td width="80px" class="titlestyle">教师层级</td> -->
<!-- 						<td width="135px"> -->
<!-- 							<select name="ic_teaLevel" id="ic_teaLevel" class="easyui-combobox" style="width:100%" panelHeight="auto"> -->
<!-- 							</select> -->
<!-- 						</td>					 -->
						<td style="width:80px;" align="center">
							<a href="#" onclick="doToolbar(this.id)" id="queTeaList" class="easyui-linkbutton" plain="true" iconcls="icon-search">查询</a>
						</td>				
					</tr>
			    </table>
			</div>
			<div region="center">
				<table id="teaList" style="width:100%;"></table>
			</div>
		</div>
	</div>
	
	
	<!-- 排监考教师结果 -->
	<div id="msginfoDialog">
		<div id="wprk" style="width:100%;height:100%;font-size:16px;"></div>
	</div>
	
	<!-- 选中的课表课程 -->
	<table id="selectCourse" style="position:absolute; z-index:99999; display:none; font-size:12px; border:1px solid #CCCCCC; background-color:#FFE48D; text-align:center; padding-top:2px;">
		<tr><td id="selectContent"></td></tr>
	</table>
	
	<!-- 课表导出页面 -->
	<div id="exportTimetable">
		<!--引入编辑页面用Ifram-->
		<iframe id="exportTimetableiframe" name="exportTimetableiframe" src='' style='width:100%; height:100%;' frameborder="0" scrolling="no"></iframe>
	</div>
	
	<!-- 下载excel -->
  	<iframe id="exportIframe" src="" style="width:0; height:0;"></iframe>
	
	<!-- 刷新进度条页面 -->
	<!-- <iframe id="loadProgressBar" name="loadProgressBar" src="" style="width:0; height:0;" frameborder="0"></iframe> -->
</body>
<script type="text/javascript">
	var sAuth = '<%=sAuth%>';
	var iUSERCODE="<%=MyTools.getSessionUserCode(request)%>";
	var admin = '<%=MyTools.getProp(request, "Base.admin")%>';//管理员
	var pubTeacher = '<%=MyTools.getProp(request, "Base.pubTeacher")%>';//公共课
	var majorTeacher = '<%=MyTools.getProp(request, "Base.majorTeacher")%>';//专业课
	var examTime = '<%=MyTools.getProp(request, "Base.examTime")%>';//考试时间段
	var iKeyCode = ''; //定义主键
	var rowInfo = '';
	var pageNum = 1;   //datagrid初始当前页数
	var pageSize = 20; //datagrid初始页内行数
	var PK_XNXQMC_CX = '';//查询条件
	var PK_JXXZ_CX = '';
	var PK_ZY_CX = '';
	var PK_TJZT_CX = '';
	var cpXnxq = '';//重排课表的学年学期
	var cpZydm = '';//重排课表的专业
	var curSelXnxq = '';//用于保存当前选中的学年学期
	var curSelKszq = '';//用于保存当前选中的考试名称
	var curSelClass = '';//用于保存当前选中的班级编号
	var tempNodeTarget = '';
	var sfgqFlag = false;//用于判断当前课程表是否已过了排课截止时间
	var tempSemesterName = '';//用于保存当前选择自动排课的学期名称
	var enterColor = '';//鼠标进入前颜色
	
	var mzts = 0;//每周天数
	var sw = 0;//上午
	var zw = 0;//中午
	var xw = 0;//下午
	var ws = 0;//晚上
	var zjs = 0;//总节数
	var jcsj = '';//节次时间
	var weekNameArray = ['星期一','星期二','星期三','星期四','星期五','星期六','星期日'];
	var timeOrderArray = new Array();
	var existArray = new Array(); //用于存放已存在的课程表的学年学期
	var classKCB = '';//课程表明细编号,时间序列,授课计划明细编号,课程名称,授课教师姓名,授课教师编号,实际场地编号,实际场地名称,授课周次
	var wpkc = '';//授课计划明细编号,课程名称,授课教师姓名,场地名称,授课周次,剩余节数
	var kcbBZ = '';//课程表备注
	var tkOrder = '';//用于保存已进行过调课操作的时间序列
	var curXqzc = '';//学期总周次
	var oddArray = new Array();//单周周次
	var evenArray = new Array();//双周周次
	var stuNum = 0;//当前班级人数
	
	var curSelectId = ''; //选中单元格id
	var curTargetId = ''; //目标单元格id
	var selectArea = '';//选择的课程的区域
	var targetArea = '';//目标区域
	var enterTimes = 0;//用于判断自动排课时查询进度的次数
	var teaUsedOrderInfo = new Array();//当前班级当前学期教师授课已用时间序列信息
	var siteUsedOrderInfo = new Array();//当前班级当前学期场地已用时间序列信息
	var hbSetInfo = new Array();//当前班级合班设置信息
	var hbCourseInfo = new Array();//当前班级合班课程实际安排情况
	var selHbSetArray = new Array();//当前选中单元格课程的合班信息
	var titleArray = new Array();//用于保存手动调课时单元格title原来信息
	var updateArray = new Array();//课程表明细编号,班级排课状态,授课计划明细编号,授课教师编号,授课教师姓名,实际场地编号,实际场地名称,授课周次,授课周次详情
	var addCourseInfo = '';
	var curSelTeaCode = new Array();
	var curSelTeaName = new Array();
	var curSelSiteCode = new Array();
	var curSelSiteName = new Array();
	var curSelSkzc = '';
	var kssjd="";
	var isLoad = true;//判断datagrid是否处于加载状态
	var ei_xnxq="";//传递学年学期
	var ei_jzzs="";//传递截止周数
	var ei_zbbh="";//考试主表编号
	
	var kssjdarray =new Array();//存放考试时间段
	var kskcarray =new Array();//存放考试课程 
	var ksjsarray =new Array();//存放监考教师 
	var updateArray = new Array();//课程表明细编号,班级排课状态,授课计划明细编号,授课教师编号,授课教师姓名,实际场地编号,实际场地名称,授课周次,授课周次详情
	var titleArray = new Array();//用于保存手动调课时单元格title原来信息
	var sjdnum=5;
	var isLoad="";
	var classId="";
	var parentId="";
	var loadFlag="";
	var curSelectId = ''; //选中单元格id
	var curTargetId = ''; //目标单元格id
	var selectArea = '';//选择的课程的区域
	var targetArea = '';//目标区域
	var tempTagContent='';
	var selTitle = '';
	var selName='';
	var tagTitle = '';
	var tagName='';
	var classflag=false;
	var lastIndex=-1;//保存上次选中
	
	$(document).ready(function(){
		//判断权限
		$('#apkc').show();
		
// 		if(sAuth.indexOf(admin) > -1){
// 			$('#del').show();
// 			$('#semCourse').show();
// 			$('.zycx').hide();
// 		}
// 		if(sAuth.indexOf(pubTeacher) > -1){
// 			$('#pk').show();
// 			$('#pubConfirm').show();
// 			$('#semCourse').show();
// 			$('.zycx').hide();
// 		}
// 		if(sAuth.indexOf(majorTeacher) > -1){
// 			$('#pk').show();
// 			$('.qrcx').hide();
// 		}
	
		initDialog();//初始化对话框
		initPkDialog();
		initData();//页面初始化加载数据
		initxnxq();
		kssjd=examTime.split(",");
		$('#loadingMask').hide();
	});
	
	
	//显示考试安排
		function loadGridKCAP(){
			$('#semesterList').datagrid({
				url : '<%=request.getContextPath()%>/Svl_examSet?active=listkcap'+'&xnxq='+$('#XNXQ').combobox('getValue')+'&jxxz='+$('#JXXZ').combobox('getValue'),
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
					{field:'学年学期名称',title:'学年学期名称',width:fillsize(0.2),align:'center'},
					{field:'ex_ksmc',title:'考试名称',width:fillsize(0.2),align:'center'},
					{field:'col4',title:'操作',width:fillsize(0.2),align:'center',
						formatter:function(value,rec){
							var html="";
							//if(rec.ex_kslx=="1"){
								html="<input type='button' value='[详情]' onclick='examtableDetail(\"" + rec.学年学期名称 + "\",\"" + (rec.ex_xnxq+rec.ex_jxxz) + "\",\"" + rec.ex_ksmc + "\",\"" + rec.考场安排主表编号 + "\");' style=\"cursor:pointer;\">";
							//}else{
								//html="<input type='button' value='[导出]' onclick='ExportExcelBKAPdialog(\"" + (rec.ex_xnxq+rec.ex_jxxz) + "\",\"" + rec.考场安排主表编号 + "\",\"" + rec.ex_kslx + "\");' style=\"cursor:pointer;\">";
							//}	
							return html;		
						}
					}			
				]],
				//双击某行时触发
				onDblClickRow:function(rowIndex,rowData){},
				//读取datagrid之前加载
				onBeforeLoad:function(){},
				//单击某行时触发
				onClickRow:function(rowIndex,rowData){
					rowInfo=rowData;
				},
				//加载成功后触发
				onLoadSuccess: function(data){
					
				}
			});
			
		}
	
		function initxnxq(){
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_examSet',
				data : 'active=initData&page=' + pageNum + '&rows=' + pageSize,
				dataType:"json",
				success : function(data) {  
					xnxq = data[0].xnxqData;//获取学年学期下拉框数据
					jxxz = data[0].jxxzData;//获取教学性质下拉框数据
					KCcombobox(xnxq,jxxz);//初始化下拉框
					loadGridKCAP(); 
				}
			});
		}
	
		function KCcombobox(xnxq,jxxz){
			//加载下拉框数据
				$('#XNXQ').combobox({
					data:xnxq,
					valueField:'comboValue',
					textField:'comboName',
					editable:false,
					panelHeight:'140', //combobox高度
					onLoadSuccess:function(data){
						//判断data参数是否为空
						if(data != ''){
							//初始化combobox时赋值
							$(this).combobox('setValue', data[0].comboValue);
			
						}
					},
					//下拉列表值改变事件
					onChange:function(data){
						loadGridKCAP();
					}
				});
				
				$('#JXXZ').combobox({
					data:jxxz,
					valueField:'comboValue',
					textField:'comboName',
					editable:false,
					panelHeight:'100', //combobox高度
					onLoadSuccess:function(data){
						//判断data参数是否为空
						if(data != ''){
							//初始化combobox时赋值
							$(this).combobox('setValue', data[0].comboValue);
	
						}
					},
					//下拉列表值改变事件
					onChange:function(data){}
				});
		
			
			//考试周期combobox
			$('#KC_KSTS').combobox({
				url:"<%=request.getContextPath()%>/Svl_examSet?active=PKKSTSCombobox",
				valueField:'comboValue',
				textField:'comboName',
				editable:false,
				panelHeight:'140', //combobox高度
				onLoadSuccess:function(data){
					//判断data参数是否为空
					if(data != ''){
						//初始化combobox时赋值
						$(this).combobox('setValue', data[0].comboValue);
					}
				},
				//下拉列表值改变事件
				onChange:function(data){
					
				}
			});
		}
	
		//导出补考安排表==================================================================================
		var bkxnxq="";
		var bkzbbh="";
		var bkkslx="";
		function ExportExcelBKAPdialog(xnxqbm,kszbbh,kslx){ 
			bkxnxq=xnxqbm;
			bkzbbh=kszbbh;
			bkkslx=kslx;
			$('#qtlxBKtimeDialog').dialog("open");
		}
		
		function ExportExcelBKAP(){ 
			$('#pkTips').html('正在导出中，请稍候...');
			$('#maskDiv').dialog({
				title:'导出'
			});
			$('#maskDiv').dialog("open");
			$('#qtlxBKtimeDialog').dialog("close");
			if(bkkslx=="2"){
				$.ajax({
					type : "POST",
					url : '<%=request.getContextPath()%>/Svl_examSet',
					data : 'active=ExportExcelBKAP&GG_XNXQBM='+(bkxnxq)+'&KCAPZBBH='+bkzbbh+'&tiyutime='+encodeURI($('#ic_tiyu').textbox('getValue'))+'&qtlxtime='+encodeURI($('#ic_qtlx').textbox('getValue')),
					dataType:"json",
					success : function(data) {
						$('#divPageMaskBK').hide();
						if(data[0].MSG == '文件生成成功'){
							//下载文件到本地
							$('#maskDiv').dialog("close");
							$("#exportIframe").attr("src", '<%=request.getContextPath()%>/form/elective/fileDownload.jsp?filePath=' + encodeURI(encodeURI(data[0].filePath)));
						}else{
							alertMsg(data[0].MSG);
						}
					}
				});
			}else{
				$.ajax({
					type : "POST",
					url : '<%=request.getContextPath()%>/Svl_examSet',
					data : 'active=ExportExcelDBKAP&GG_XNXQBM='+(bkxnxq)+'&KCAPZBBH='+bkzbbh+'&tiyutime='+encodeURI($('#ic_tiyu').textbox('getValue'))+'&qtlxtime='+encodeURI($('#ic_qtlx').textbox('getValue')),
					dataType:"json",
					success : function(data) {
						$('#divPageMaskBK').hide();
						if(data[0].MSG == '文件生成成功'){
							//下载文件到本地
							$('#maskDiv').dialog("close");
							$("#exportIframe").attr("src", '<%=request.getContextPath()%>/form/elective/fileDownload.jsp?filePath=' + encodeURI(encodeURI(data[0].filePath)));
						}else{
							alertMsg(data[0].MSG);
						}
					}
				});
			}
		}
	
	
	/**自动排课
		@curXnxq 当前选择的学年学期
		@curZydm 当前选择的专业代码
		@type 排课类型(1全新排课/2继续排剩下的课)
		@tsgzFlag 特殊规则排课标识(1验证/2不验证)
		@checkTeaFlag 禁排标识(1验证/2不验证)
	*/
	function startAreaArrange(curXnxq, curQzqm,curXbdm){  	
		$('#pkTips').html('正在安排监考教师中，请稍候...');
		$('#maskDiv').dialog({
			title:'排监考教师'
		});
		$('#maskDiv').dialog('open');
		
		$('#wprk').html("");
		
		//$('#pkClass').html('&nbsp;');
		//$('#pkCourse').html('&nbsp;');
		//$('#progressBar').css('width', '99.5%');
		//$('#percent').html('100%');
		//$('#percent').css('color', 'black');
		
		
			
// 		setPkState('using');
// 		tempSemesterName = $('#PK_XN').combobox('getText') + $('#PK_XQ').combobox('getText') + $('#PK_JXXZ').combobox('getText');//保存当前选择自动排课的学期名字
		//显示遮罩层和进度条
// 		$('#courseArrange').dialog('close');
// 		
// 		enterTimes = 0;//重置查询进度次数
// 		var timer = setInterval("loadProgressBar();", 1000);
								
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_examSet',
			data : 'active=autoAreaArrange&KC_XNXQBM=' + curXnxq + '&KC_QZQM=' + curQzqm + '&KC_XBDM=' + curXbdm ,
			dataType:"json",
			success : function(data) {
						
					var timer="";
					clearInterval(timer);
					timer = setTimeout(function(){
					clearInterval(timer);
							
											//判断排课是否出错
											if(data[0].MSG=='complatePart' || data[0].MSG=='complateAll'){
												loadSemesterData();
												//$('#divPageMask2').hide();
												$('#progressBar').css('width', '99.5%');
												$('#percent').html('100%');
												$('#pkTips').html('自动排监考教师已完成');
												$('#pkClass').html('&nbsp;');
												$('#pkCourse').html('&nbsp;');
												
												setTimeout(function(){
													clearInterval(timer);
													$('#maskDiv').dialog('close');
													cpXnxq = data.xnxq; //当前排课的学期
													cpZydm = curZydm; //当前排课的专业
													
													if(data.MSG=='complatePart'){
														var content = '';
														var wpkcInfo = data.wpkc;
														var tempMajor = '';
														var tempClass = '';
														
														//拼接未排课程信息
														content = '<span style="height:30px; line-height:30px; color:blue; font-size:14px;">课表未能完全排完，以下是未排课程信息：</span><br/>';
														for(var i=0; i<wpkcInfo.length; i++){
															if(tempMajor != wpkcInfo[i].专业){
																tempMajor = wpkcInfo[i].专业;
																content += '<br/><b>专业：' + tempMajor +'</b><br/>';
															}
															
															if(tempClass != wpkcInfo[i].班级){
																tempClass = wpkcInfo[i].班级;
																content += '<br/>班级：' + tempClass +'<br/>';
																content += '<span style="width:35px;">课程:</span>';
															}else{
																content += '<span style="width:35px;">&nbsp;</span>';
															}
															
															content += '<span onmouseover="$(this).css(\'color\', \'blue\');" onmouseout="$(this).css(\'color\', \'black\');"><span style="width:150px;">' + wpkcInfo[i].课程名称 + '</span>';
															content += '<span style="width:110px;">课时总数：' + (parseInt(wpkcInfo[i].已排节数, 10)+parseInt(wpkcInfo[i].未排节数, 10)) + '节</span>';
															content += '<span style="width:70px;">已排：' + wpkcInfo[i].已排节数 + '节</span>';
															content += '未排：' + wpkcInfo[i].未排节数 + '节</span><br/>';
														}
														
														$('#wpkcTips').html(content);
													
														//完成部分排课后，显示未排课程信息
														$('#compPartTips').dialog('open');
													}else{
														//完成所有排课后，显示课表
														//timetableDetail(tempSemesterName, cpXnxq, cpZydm);
														examtableDetail(tempSemesterName, cpXnxq, cpZydm);
													}
												}, 500);
											}else{ 
												var msginfo=data[0].MSG;
												if(data[0].MSG2!=""){
													msginfo+="<br/><br/>有未排监考教师："+data[0].MSG2;
												}
												if(data[0].MSG3!=""){
													msginfo+="<br/><br/>监考教师都不是该班的任课教师："+data[0].MSG3;
												}
												
												if(data[0].MSG2==""&&data[0].MSG3==""){
													alertMsg(msginfo);
												}else{
													$('#msginfoDialog').dialog('open');
													$('#wprk').html(msginfo);
												}
												
												
												$('#maskDiv').dialog('close');
											}
										}, 500);
									}
			});	
	}
	
	/**显示班级考试列表
		@PK_XNXQMC 学年学期名称
		@PK_XNXQBM 学年学期编码
	**/
	function examtableDetail(PK_XNXQMC, PK_XNXQBM, ex_ksmc, PK_KSZBBH){ 
		var useFlag = false;
		var msgStr = '';
		lastIndex=-1;
		
		$('#classTimetable').dialog({
			title:ex_ksmc
		});
		$('#classTimetable').dialog("open");
		ei_xnxq=PK_XNXQBM;
		ei_zbbh=PK_KSZBBH;
		//加载班级信息TREE
		loadClassTree(PK_XNXQBM, PK_KSZBBH);
		
		//初始化空白课程表
		initBlankKCAP(PK_XNXQBM, PK_KSZBBH, useFlag, msgStr);
		
	}
	
	/**显示班级考试列表
		@PK_XNXQMC 学年学期名称
		@PK_XNXQBM 学年学期编码
	**/
	var pkxnxqmc="";
	var pkxnxnbm="";
	var pkkslx="";
	var pkkszq="";
	
	function ksxsCombobox(PK_KSLX){
		$('#BK_KSXS').combobox({
					url:"<%=request.getContextPath()%>/Svl_examSet?active=BKKSXSCombobox&PK_KSLX="+PK_KSLX,
					valueField:'comboValue',
					textField:'comboName',
					editable:false,
					panelHeight:'140', //combobox高度
					onLoadSuccess:function(data){
						//判断data参数是否为空
						if(data != ''){
							//初始化combobox时赋值
							$(this).combobox('setValue', data[0].comboValue);
			
						}
					},
					//下拉列表值改变事件
					onChange:function(data){
	
					}
				});	
	}
	
	function examBUKAODetail(PK_XNXQMC, PK_XNXQBM, PK_KSLX, PK_KSZQ){ 
		pkxnxqmc=PK_XNXQMC;
		pkxnxnbm=PK_XNXQBM;
		pkkslx=PK_KSLX;
		pkkszq=PK_KSZQ;
		
		ksxsCombobox(PK_KSLX);
		
		$('#classBUKAOtable').dialog({
			title:(PK_XNXQMC+'考试信息').replace(/ /g, "")
		});
		$('#classBUKAOtable').dialog("open");
		BKINFOdatagrid(PK_XNXQMC, PK_XNXQBM, PK_KSLX, PK_KSZQ);
	}
	
	function BKINFOdatagrid(PK_XNXQMC, PK_XNXQBM, PK_KSLX, PK_KSZQ){
		$('#bukaoList').datagrid({
				url : '<%=request.getContextPath()%>/Svl_examSet?active=queryBUKAOinfo&XNXQ=' + encodeURIComponent(PK_XNXQMC) + '&ex_kslx=' + PK_KSLX + '&QZQM=' + PK_KSZQ+'&BK_KCMC='+encodeURIComponent($('#BK_KCMC').textbox('getValue'))+'&BK_KSXS='+encodeURIComponent($('#BK_KSXS').combobox('getValue')),
				title:'',
				width:'100%',
				nowrap: false,
				fit:true, //自适应父节点宽度和高度
				showFooter:true,
				striped:true,
				pagination:true,
				pageSize:pageSize,
				singleSelect:true,
				pageNumber:pageNum,
				rownumbers:true,
				fitColumns: true,
				columns:[[
					//field为读取数据的数据名，title为显示的数据名，width宽度设置，align数字在表格中显示的位置
					{field:'学年学期名称',title:'学年学期名称',width:fillsize(0.2),align:'center'},
					{field:'课程名称',title:'课程名称',width:fillsize(0.4),align:'center'},
					{field:'学生人数',title:'学生人数',width:fillsize(0.1),align:'center'},
					{field:'试卷类型',title:'考试形式',width:fillsize(0.1),align:'center'},
					{field:'行政班名称',title:'专业',width:fillsize(0.4),align:'center'},
					{field:'时间序列',title:'考试时间',width:fillsize(0.4),align:'center',
						formatter:function(value,rec){
							var sjxl=rec.时间序列.split("#");
							var ksrq=rec.MSG.split(",");
							return ksrq[sjxl[0]]+"  "+kssjd[sjxl[1]];
						}
					}		
				]],
				//双击某行时触发
				onDblClickRow:function(rowIndex,rowData){},
				//读取datagrid之前加载
				onBeforeLoad:function(){},
				//单击某行时触发
				onClickRow:function(rowIndex,rowData){
					rowInfo=rowData;
				},
				//加载成功后触发
				onLoadSuccess: function(data){
					
				}
		});
	}
	
	/**加载班级信息TREE
		@PK_XNXQBM 学年学期编码
		@PK_ZYDM 专业代码
		@useFlag 允许排课判断参数
	**/
	function loadClassTree2(PK_XNXQBM, PK_ZYDM, useFlag){ 
		$('#classTree').tree({
			checkbox: false,
			url:'<%=request.getContextPath()%>/Svl_examSet?active=queMajorTree&level=0&sAuth=' + sAuth + '&GG_XNXQBM=' + PK_XNXQBM + '&zydm=' + PK_ZYDM,
		    onClick:function(node){
		    	//判断点击的是不是班级
		    	if($('#classTree').tree('getParent', node.target) != null){ 
		    		
		    		//判断点击的班级是不是当前选中的班级
		    		if(node.id != curSelClass){
		    			$('#divPageMask').show();
		    			curSelClass = node.id;
		    			tempNodeTarget = node.target;
		    			addCourseInfo = '';
		    			loadClassKCAP(PK_XNXQBM, curSelClass);
		    		}
		    	}else{
					$('#classTree').tree('select', tempNodeTarget);
					
		    	}
			},
			onDblClick:function(node){
				//判断点击的是不是班级
		    	if($('#classTree').tree('getParent', node.target) == null){
		    		$('#classTree').tree('select', tempNodeTarget);
		    	}
			},
		    onBeforeExpand:function(node,param){
			  	$('#classTree').tree('options').url="<%=request.getContextPath()%>/Svl_examSet?active=queMajorTree&level=1&GG_XNXQBM=" + PK_XNXQBM + "&parentCode="+node.id;
			},
			onLoadSuccess:function(node, data){
				$("#classTree").show();
			}
		});
	};
	
	/**读取当前班级课程表和未排课程表格
		@PK_XNXQBM 学年学期编码
		@classCode 班级编号
	**/
	function loadClassKCAP(PK_XNXQBM, PK_KSZBBH, classId){
		loadFlag = true;
		updateArray.length = 0;//清空课表更新数组
		document.getElementById('kcbContent').scrollTop = 0;
		
		//清空课程表内容
		$('.kcbTdCentent').removeAttr('title');
		$('.kcbTdCentent').html('&nbsp;');
		$('.kcbTdCentent').css('color', '');
		//清空未排课程表格内容
		$('.wpkcTdCentent').removeAttr('title');
		$('.wpkcTdCentent').html('&nbsp;');
		
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_examSet',
			data : 'active=loadClassKCAP&sAuth=' + sAuth + '&XNXQ=' + PK_XNXQBM + '&KCAPZBBH=' + PK_KSZBBH + '&XZBDM=' + classId,
			dataType:"json",
			success : function(data) {
				classKCB = data[0].kcb;//课程表明细编号,班级排课状态,时间序列,授课计划明细编号,授课教师编号,课程名称,授课教师姓名,实际场地编号,实际场地名称,授课周次,授课周次详情
				wpkc = data[0].wpkc;//授课计划明细编号,课程名称,授课教师姓名,场地要求,场地名称,授课周次,剩余节数
									
				fillKcb(PK_XNXQBM, classId);//填充当前课程表单元格内的课程信息
				//fillWpkc();//填充未排课程表格
				$('#divPageMask').hide();
				loadFlag = false;
			}
		});
	}
	
	/**填充当前课程表单元格内的课程信息
		@PK_XNXQBM 学年续期编码
		@classCode 班级编号
	**/
	function fillKCAP(PK_XNXQBM, classCode){
		var tempContent = '';
		var tempTitle = '';
		
		for(var i=0; i<classKCB.length; i++){
			tempTitle = '';
			if(classKCB[i].考场安排明细编号 != ''){ 
				var chgsjxl="0"+(parseInt(classKCB[i].时间序列.split("#")[0])+1)+"0"+(parseInt(classKCB[i].时间序列.split("#")[1])+1);
				
				tempTitle = classKCB[i].课程名称 + '\n' + classKCB[i].监考教师姓名;
				if(classKCB[i].场地名称 != ''){
					tempTitle += '\n' + classKCB[i].场地名称;
				}
				//tempTitle += '\n' + parseWeekShow(classKCB[i].授课周次详情, oddArray, evenArray);//修改授课周次显示内容
				
				//判断如果当前单元的状态是固排或者课程类型不符，禁止操作该单元格。
				
				tempContent = '<font color="blue">' + classKCB[i].课程名称 + '<br/>' + classKCB[i].监考教师姓名 + '<br/>' + classKCB[i].场地名称 + '</font>';
				tempContent += '<input type="hidden" id="changeFlag_'+chgsjxl+'" value="1"/>';//用于判断可否手动调整(0不可/1可/2空白/3添加课程)
				
				tempContent += '<input type="hidden" id="skjhmxbh_'+chgsjxl+'" value="'+classKCB[i].考场安排明细编号+'"/>';//授课计划明细编号
				tempContent += '<input type="hidden" id="skjsbh_'+chgsjxl+'" value="'+classKCB[i].监考教师编号+'"/>';//授课教师编号
				tempContent += '<input type="hidden" id="skjsmc_'+chgsjxl+'" value="'+classKCB[i].监考教师姓名+'"/>';//授课教师编号
				//tempContent += '<input type="hidden" id="kclx_'+classKCB[i].时间序列+'" value="'+classKCB[i].课程类型+'"/>';//课程类型
				tempContent += '<input type="hidden" id="cdbh_'+chgsjxl+'" value="'+classKCB[i].场地要求+'"/>';//场地编号
				tempContent += '<input type="hidden" id="cdmc_'+chgsjxl+'" value="'+classKCB[i].场地名称+'"/>';//场地编号
				//tempContent += '<input type="hidden" id="kczc_'+classKCB[i].时间序列+'" value="'+classKCB[i].授课周次+'"/>';//课程周次
				//tempContent += '<input type="hidden" id="kczcxq_'+classKCB[i].时间序列+'" value="'+classKCB[i].授课周次详情+'"/>';//课程周次详情
				
				$('#kcb_' + chgsjxl).attr('title', tempTitle);
				$('#kcb_' + chgsjxl).html(tempContent);
			}
			
			$('#kcb_' + chgsjxl).attr('name', classKCB[i].考场安排明细编号);
		}
	}
	
	
	/**初始化空白课程表和未排课程表格
		@PK_XNXQBM 学年学期编码
		@PK_ZYDM 专业代码
		@useFlag 排课功能是否正在被使用判断
		@msgStr 提示信息
	**/
	var oddArray = new Array();//单周周次
	var evenArray = new Array();//双周周次
	var timeOrderArray = new Array();
	var curXqzc="";
	function initBlankKCAP(PK_XNXQBM, PK_KSZBBH, useFlag, msgStr){ 
		//查询当前学期设置的每周天数和每天节数
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_examSet',
			data : 'active=initBlankKCAP&GG_XNXQBM=' + PK_XNXQBM + '&PK_KSZBBH=' + PK_KSZBBH,
			dataType:"json",
			success : function(data) { 
				
				//$('#kcbMain').layout('panel', 'south').panel('options').height = (2+1)*30 + 'px';
		
				sw = parseInt(data[0].sw, 10);//上午
				zw = parseInt(data[0].zw, 10);//中午
				xw = parseInt(data[0].xw, 10);//下午
				ws = parseInt(data[0].ws, 10);//晚上
				zjs = sw+zw+xw+ws;
				jcsj = data[0].jcsj.split(',');
				curXqzc = data[0].xqzc;//学期总周次
				
				//单双周周次
				//var weekArray = parseWeekArray(curXqzc);
				//oddArray = weekArray[0];
				//evenArray = weekArray[1];
				
				//生成周次调整对话框内容
				var dialogHeight = 99;
				dialogHeight += parseInt(curXqzc/3, 10)*30;
				if(curXqzc%3 > 0){
					dialogHeight += 30;
				}
									
				//生成全部时间序列数组
				var ksts=(data[0].ksrq+"").split(","); 
				for(var i=1; i<ksts.length+1; i++){
					for(var j=1; j<zjs+1; j++){
						timeOrderArray.push((i<10?'0'+i:''+i) + (j<10?'0'+j:''+j));
						timeOrderArray.push('');
					}
				}
				
				mzts = ksts.length;//每周天数
				//授课计划未排课程所需单元格数量
				var maxNum = data[0].maxNum;
				if(parseInt(maxNum, 10)%(mzts+1) == 0){
					maxNum = parseInt(parseInt(maxNum, 10)/(mzts+1), 10);
				}else{
					maxNum = parseInt(parseInt(maxNum, 10)/(mzts+1), 10) + 1;
				}
				if(maxNum < 2){
					maxNum = 2;
				}
							
				var tdWidth = 100/(ksts.length+1);
				var tempNum = 0;
				var kcbContent = '<table style="width:100%; height:100%; border-top:1px solid #95B8E7; border-right:1px solid #95B8E7;" cellspacing="0" cellpadding="0">';
				var wpkcContet = '<table style="width:100%; height:100%; border-top:1px solid #95B8E7; border-right:1px solid #95B8E7;" cellspacing="0" cellpadding="0">';
				
				for(var i=-1; i<zjs; i++){
					kcbContent += '<tr class="kcbTrStyle" onmouseout="window.event? window.event.cancelBubble = true : e.stopPropagation();"';
					
					//空白未排课程表格
					if((i+1) <= maxNum){
						wpkcContet += '<tr class="wpkcTrStyle" onmouseout="window.event? window.event.cancelBubble = true : e.stopPropagation();">';
					}
					
					if(i == -1){
						kcbContent += ' style="height:45px;"';	
					}
					kcbContent += '>';
	
					
					for(var j=-1; j<ksts.length; j++){ 
						//空白未排课程表格
						if((i+2) <= maxNum){
							wpkcContet += '<td id="wpkc_' + tempNum + '" class="wpkcTdStyle wpkcTdCentent" onmouseover="tdMouseOver(this.id);" onmouseout="tdMouseOut(this.id);" onmousedown="selectKcbCourse(this.id, \'wpkc\');">&nbsp;</td>';
						}
						tempNum++;
						
						//添加左上角空单元格
						if(i==-1 && j==-1){
							kcbContent += '<td colspan="3" class="kcbTdStyle titleBG splitTd" onmouseout="window.event? window.event.cancelBubble = true : e.stopPropagation();">&nbsp;</td>';
							continue;
						}
						
						//判断添加星期数
						
						if(i==-1 && j>-1){
							//判断是小周还是大周版
							//if(mzts < 8){
								kcbContent += '<td class="kcbTdStyle kcbTitle titleBG splitTd" onmouseout="window.event? window.event.cancelBubble = true : e.stopPropagation();">' + ksts[j] + '</td>';	
							//}else{
							//	kcbContent += '<td class="kcbTdStyle kcbTitle titleBG splitTd" onmouseout="window.event? window.event.cancelBubble = true : e.stopPropagation();">大周' + (j+1) + '</td>';
							//}
							continue;
						}
						
						//判断添加节次数
						if(i>-1 && j==-1){
							//上午课程
							if(i<sw &&　sw>0){
								//判断第一行
								if(i == 0){
									kcbContent += '<td id="width_sw" rowspan="'+sw+'" class="titleTdStyle kcbTitle titleBG';
									//判断是否还有课
									if(i+sw < zjs){
										kcbContent += ' splitTd';
									}
									kcbContent += '"></td>';
								}
								
								kcbContent += '<td id="width_xh" class="titleTdStyle titleBG';
								//判断是否还有课
								if((i+1)==sw && (i+1)<zjs){
									kcbContent += ' splitTd';
								}
								kcbContent += '">' + (i+1) + '</td>';
								
								kcbContent += '<td id="width_sjd" class="titleTdStyle titleBG';
								//判断是否还有课
								if((i+1)==sw && (i+1)<zjs){
									kcbContent += ' splitTd';
								}
								kcbContent += '">';
								if(jcsj.length > i){
									kcbContent += jcsj[i];
								}else{
									kcbContent += '&nbsp;';
								}
								kcbContent += '</td>';
							}
							
							//中午课程
							if(i>=sw && i<(sw+zw) && zw>0){
								//判断第一行
								if(i == sw){
									kcbContent += '<td rowspan="'+zw+'" class="titleTdStyle kcbTitle titleBG';
									//判断是否还有课
									if(i+zw < zjs){
										kcbContent += ' splitTd';
									}
									kcbContent += '">中午</td>';
								}
								
								kcbContent += '<td class="titleTdStyle titleBG';
								//判断是否还有课
								if((i+1)==(sw+zw) && (i+1)<zjs){
									kcbContent += ' splitTd';
								}
								kcbContent += '">' + (i+1) + '</td>';
								kcbContent += '<td class="titleTdStyle titleBG';
								//判断是否还有课
								if((i+1)==(sw+zw) && (i+1)<zjs){
									kcbContent += ' splitTd';
								}
								kcbContent += '">';
								if(jcsj.length > i){
									kcbContent += jcsj[i];
								}else{
									kcbContent += '&nbsp;';
								}
								kcbContent += '</td>';
							}
							
							//下午课程
							if(i>=(sw+zw) && i<(sw+zw+xw) && xw>0){
								//判断第一行
								if(i == sw+zw){
									kcbContent += '<td rowspan="'+xw+'" class="titleTdStyle kcbTitle titleBG';
									//判断是否还有课
									if(i+xw < zjs){
										kcbContent += ' splitTd';
									}
									kcbContent += '"></td>';
								}
								
								kcbContent += '<td class="titleTdStyle titleBG';
								//判断是否还有课
								if((i+1)==(sw+zw+xw) && (i+1)<zjs){
									kcbContent += ' splitTd';
								}
								kcbContent += '">' + (i+1) + '</td>';
								kcbContent += '<td class="titleTdStyle titleBG';
								//判断是否还有课
								if((i+1)==(sw+zw+xw) && (i+1)<zjs){
									kcbContent += ' splitTd';
								}
								kcbContent += '">';
								if(jcsj.length > i){
									kcbContent += jcsj[i];
								}else{
									kcbContent += '&nbsp;';
								}
								kcbContent += '</td>';
							}
							
							//晚上课程
							if(i>=(sw+zw+xw) && i<(sw+zw+xw+ws) && ws>0){
								//判断第一行
								if(i == (sw+zw+xw)){
									kcbContent += '<td rowspan="'+ws+'" class="titleTdStyle titleBG kcbTitle">晚上</td>';
								}
								kcbContent += '<td class="titleTdStyle titleBG">' + (i+1) + '</td>'
											+ '<td class="titleTdStyle titleBG">';
								if(jcsj.length > i){
									kcbContent += jcsj[i];
								}else{
									kcbContent += '&nbsp;';
								}
								kcbContent += '</td>';
							}
							
							continue;
						}
						
						//拼接时间序列
						if((j+1) < 10){
							tempOrder = '0'+(j+1);
						}else{
							tempOrder = j+1;
						}
						if((i+1) < 10){
							tempOrder += '0'+(i+1);
						}else{
							tempOrder += i+1;
						}
					
						//添加普通单元格
						kcbContent += '<td id="kcb_' + tempOrder + '" name="kcbsell" class="kcbTdStyle kcbTdCentent';
						if(((i+1)==sw && zw>0) || ((i+1)==(sw+zw) && xw>0) || ((i+1)==(sw+zw+xw) && ws>0)){
							kcbContent += ' splitTd';
						}
						kcbContent += '" onmouseover="tdMouseOver(this.id);" onmouseout="tdMouseOut(this.id);" ' + 
									'onmousedown="selectKcbCourse(this.id, \'kcb\');" ondblclick="changeTeacher(this.id);">&nbsp;';
						kcbContent += '<input type="hidden" id="kcdm_'+tempOrder+'" value=""/></td>';//课程代码 
					}
					kcbContent += '</tr>';
					wpkcContet += '</tr>';
				}
				
				kcbContent += '</table>';
				wpkcContet += '</table>';
				var mask = '<div id="timetableMask" style="position:absolute; top:0; z-index:99999; width:100%; height:100%; background:url(0);"></div>';
				$('#kcbContent').html(kcbContent);
				mask = '<div id="wpkcMask" style="position:absolute; top:0; z-index:99999; width:100%; height:100%; background:url(0);"></div>';
				//$('#wpkcContent').html(wpkcContet);
				
				$('#classTimetable').dialog('open');
				$('.kcbTdStyle').css('width', tdWidth+'%');
				$('.wpkcTdStyle').css('width', tdWidth+'%');
				$('#width_sw').css('width', '50px');
				$('#width_xh').css('width', '20px');
				$('#width_sjd').css('width', '130px');
				$('.wpkcTdStyle').css('height', '30px');
				$('.kcbTrStyle').css('height', parseInt($('#kcbContent').css('height').substring(0, $('#kcbContent').css('height').length-2)-1, 10)/(sw+xw+ws+1)+'px');
				$('.wpkcTrStyle').css('height', 100/sw+'%');
				
				//当前课程表是否已过了排课截止时间
				sfgqFlag = data[0].sfgq=='1'?true:false;
				
				//判断排课功能是否正在被使用
				if(useFlag == true){
					alertMsg(msgStr);
				}
			}
		});
	}
	
	//编辑监考教师 
	var sjxl="";
	var ori_kcmc="";
	var ori_jsbh="";
	var ori_jsxm="";
	var selJKJSBH="";
	var selJKJSXM="";
	var oldJKJSBH="";
	var oldJKJSXM=""; 
	var newJKJSBH="";
	var newJKJSXM=""; 
	var teacellnum="";
	var teanum=0;
	var jkjsarray = new Array();
	var chgteaarray = new Array();
	var vect=null;
	function changeTeacher(orderid){
		//ei_xnxq,ei_zbbh,orderid,classId
		sjxl=orderid.substring(4,orderid.length);
		teanum=0;
		for(var i=0; i<classKCB.length; i++){
			//alert(classKCB[i].时间序列+"|"+classKCB[i].课程名称+"|"+classKCB[i].监考教师姓名);
			if(classKCB[i].时间序列==sjxl){
				ori_kcmc=classKCB[i].课程名称;
				ori_jsbh=classKCB[i].监考教师编号;
				ori_jsxm=classKCB[i].监考教师姓名;
				var teacherid="";
				var teacherxm="";
				
				teanum=classKCB[i].监考教师人数;
				var ypteanum=0;
				if(classKCB[i].监考教师编号.indexOf("，")>-1){
					//teanum=classKCB[i].监考教师编号.split("，").length;
					teacherid=classKCB[i].监考教师编号.split("，");
					teacherxm=classKCB[i].监考教师姓名.split("，");
					ypteanum=teacherid.length;
				}else{
					//teanum=1;
					teacherid=classKCB[i].监考教师编号;
					teacherxm=classKCB[i].监考教师姓名;
					ypteanum=1;
				}
				
				var content="";
				content+='<tr><td style="width:25%;" align="center"><span style="width:100%;" >考试课程</span></td><td style="width:25%;" colspan='+teanum+' align="center"><span style="width:100%;" >监考教师</span></td></tr>';
				
				content+='<tr>';
				content+='<td style="width:25%;" align="center"><span id="KCBH_0" style="width:100%; cursor:pointer;" onclick="" >'+classKCB[i].课程名称+'</span></td>';
				if(teanum>1){
					for(var j=0;j<teanum;j++){ 
						if(j<ypteanum){
							if(classKCB[i].监考教师编号.indexOf("，")>-1){
								content+='<td style="width:25%;"><input id="JSBH_'+ j +'" name="'+ teacherid[j] +'" value="'+ teacherxm[j] +'" style="width:100%; cursor:pointer;" onclick="openTeaList(this.id,'+j+');" readonly="readonly"/></td>';
							}else{
								content+='<td style="width:25%;"><input id="JSBH_'+ j +'" name="'+ teacherid +'" value="'+ teacherxm +'" style="width:100%; cursor:pointer;" onclick="openTeaList(this.id,'+j+');" readonly="readonly"/></td>';
							}	
						}else{
							content+='<td style="width:25%;"><input id="JSBH_'+ j +'" name="" value="" style="width:100%; cursor:pointer;" onclick="openTeaList(this.id,'+j+');" readonly="readonly"/></td>';
						}	
					}
				}else{
					content+='<td style="width:25%;"><input id="JSBH_'+ j +'" name="'+ teacherid +'" value="'+ teacherxm +'" style="width:100%; cursor:pointer;" onclick="openTeaList(this.id,'+"0"+');" readonly="readonly"/></td>';
				}				
				content+='</tr>';
				
				content+='<tr><td style="width:25%;" colspan='+(parseInt(teanum)+1)+' align="left"><span style="width:100%;color:red;font-size:14px;padding-left:10px;" >注：第一位监考教师默认选择该班级的任课教师 </span></td></tr>';
				
				$('#courseList').html(content);
				
				$('#editTeaDialog').dialog({   
					width: (parseInt(teanum)+1)*200
				});
				$('#editTeaDialog').dialog("open");
				
				//获取临时监考教师信息
// 				$.ajax({
// 					type : "POST",
//					url : '< %=request.getContextPath()%>/Svl_examSet',
// 					data : 'active=getVecTea&KCAPZBBH='+ei_zbbh,
// 					dataType:"json",
// 					success : function(data) {
// 						vect=data;
// 					}
// 				});
			}
		}
	}
	
	/**打开教师列表
		@param id 当前选中的教师input id
	**/
	function openTeaList(id,num){ 
		$('#' + id).blur();
		selJKJSBH = document.getElementById(id).name;   
		selJKJSXM = document.getElementById(id).value;
		teacellnum=num;
		oldJKJSBH=selJKJSBH;
		oldJKJSXM=selJKJSXM;
		
		jkjsarray.length=0;
		var obj = document.getElementsByTagName("input");
		for(var i=0;i<obj.length;i++){
			if(obj[i].id.indexOf("JSBH_")>-1){
				jkjsarray.push(obj[i].name);
				jkjsarray.push(obj[i].value);
			}
		}
		
		loadTeaListData();
		$('#teaListDialog').dialog('open');
	}
	
	/**读取教师datagrid数据**/
	function loadTeaListData(){ 	
		isLoad = true;
		$('#teaList').datagrid({
			url :'<%=request.getContextPath()%>/Svl_examSet',
			queryParams: {'active':'queTeaList','JKJSBH':selJKJSBH,'JKJSXM':encodeURI(selJKJSXM),'XZBDM':classId,'KCAPZBBH':ei_zbbh,'XNXQ':ei_xnxq,'chgteaarray':encodeURI(chgteaarray),
				'SJXL':sjxl,'KCMC':encodeURI(ori_kcmc),'num':teacellnum,'jkjsarray':encodeURI(jkjsarray),'ic_teaCode':$('#ic_teaCode').textbox('getValue'),'ic_teaName':encodeURI($('#ic_teaName').textbox('getValue'))
			},
			title:'',
			width:'100%',
			nowrap: false,
			fit:true, //自适应父节点宽度和高度
			showFooter:true,
			striped:true,
			pagination:true,
			pageNumber:1,
			pageSize:20,
			singleSelect:true,
			rownumbers:true,
			fitColumns: true,
			columns:[[
				//field为读取数据的数据名，title为显示的数据名，width宽度设置，align数字在表格中显示的位置
				{field:'ck',checkbox:true},
				{field:'工号',title:'工号',width:fillsize(0.5),align:'center'},
				{field:'姓名',title:'姓名',width:fillsize(0.5),align:'center'}
				//{field:'层级',title:'层级',width:fillsize(0.3),align:'center'}
			]],
			onSelect:function(rowIndex,rowData){
				if(isLoad == false){
					newJKJSBH=rowData.工号;
					newJKJSXM=rowData.姓名;
				}
			},
			onUnselect:function(rowIndex,rowData){
				newJKJSBH="";
				newJKJSXM="";
			},
			//加载成功后触发
			onLoadSuccess: function(data){
				$(".datagrid-header-check").html('&nbsp;');//隐藏全选
				//勾选已选授课教师
				if(data){
					$.each(data.rows, function(rowIndex, rowData){
						if(selJKJSBH == rowData.工号){
							$('#teaList').datagrid('selectRow', rowIndex);
						}
					});
				}
				
				isLoad = false;
				$('#confirmTea').linkbutton('enable');
				$('#queTeaList').linkbutton('enable');
			}
		});
	}
	

	/**页面初始化加载数据**/
	function initData(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_examSet',
			data : 'active=initData&sAuth=' + sAuth + '&page=' + pageNum + '&rows=' + pageSize,
			dataType:"json",
			success : function(data) {
				//loadSemesterGrid(data[0].listData, data[0].existInfo);
				initCombobox(data[0].jxxzData, data[0].xnData);
			}
		});
	}
	
	/**加载 dialog控件**/
	function initDialog(){
		$('#compPartTips').dialog({   
			title:'自动排课信息',
			width: 500,//宽度设置   
			height: 360,//高度设置 
			modal:true,
			closed: true,   
			cache: false, 
			draggable:false,//是否可移动dialog框设置
			//关闭事件
			onClose:function(data){
				$('#wpkcTips').html('');
				$("[name='kcjp']").removeAttr("checked");//取消选中
				$("[name='tsgz']").removeAttr("checked");
				setPkState('noUse');
			}
		});
		
		$('#maskDiv').dialog({   
			title:'排监考教师',
			width: 450,//宽度设置   
			height: 200,//高度设置 
			modal:true,
			closed: true,   
			cache: false,
			closable: false,
			draggable:false,//是否可移动dialog框设置
			//打开事件
			onOpen:function(data){},
			//读取事件
			onLoad:function(data){},
			//关闭事件
			onClose:function(data){
			}
		});
		
		$('#classTimetable').dialog({
			maximized:true,
			modal:true,
			closed: true,   
			cache: false, 
			draggable:false,//是否可移动dialog框设置
			toolbar:[{
				text:'返回',
				iconCls:'icon-back',
				handler:function(){
					$('#classTimetable').dialog('close');
				}
			}],
			onBeforeClose:function(){
				if(updateArray.length > 0){
					ConfirmMsg('修改的课程表还未保存，是否需要保存？', 'saveCloseKcb();', 'closeKcb();');
					return false;
				}
			},
			//关闭事件
			onClose:function(data){
				$('#kcbContent').html('');
				$('#wpkcContent').html('');
				curSelClass = '';
	    		updateArray.length = 0;
	    		var zcctCheckbox = document.getElementById('zcct');
	    		zcctCheckbox.checked = false;
	    		$('#zcctTips').hide();
	    		$('#tips').hide();
	    		$('#save').linkbutton('disable');
				$('#remark').linkbutton('disable');
				$('#classCourse').linkbutton('disable');
				$('#stuCourse').linkbutton('disable');
				//$('#timetableMask').show();
				//$('#wpkcMask').show();
				
				setPkState('noUse');
			}
		});
		
		$('#classBUKAOtable').dialog({
			maximized:true,
			modal:true,
			closed: true,   
			cache: false, 
			draggable:false,//是否可移动dialog框设置
			//关闭事件
			onClose:function(data){

			}
		});
		
		$('#remarkDialog').dialog({   
			title:'备注',
			width: 500,//宽度设置   
			height: 270,//高度设置 
			modal:true,
			closed: true,   
			cache: false,
			draggable:false,//是否可移动dialog框设置
			//打开事件
			onOpen:function(data){},
			//读取事件
			onLoad:function(data){},
			//关闭事件
			onClose:function(data){
			}
		});
		
		$('#changeCourseInfoDialog').dialog({   
			title:'更改考试信息',
			width: 700,//宽度设置   
			height: 270,//高度设置 
			modal:true,
			closed: true,   
			cache: false,
			draggable:false,//是否可移动dialog框设置
			//打开事件
			onOpen:function(data){},
			//读取事件
			onLoad:function(data){},
			//关闭事件
			onClose:function(data){
			}
		});
		
		$('#teaListDialog').dialog({   
			title: '授课教师列表',   
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
				$('#ic_teaCode').textbox('setValue', '');
				$('#ic_teaName').textbox('setValue', '');
				$('#teaList').datagrid('loadData',{total:0,rows:[]});
				newJKJSBH="";
				newJKJSXM="";
			}
		});
		
		$('#siteListDialog').dialog({   
			title: '教室列表',   
			width: 700,//宽度设置   
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
				$('#ic_siteCode').textbox('setValue', '');
				$('#ic_siteName').textbox('setValue', '');
				$('#ic_siteType').combobox('setValue', '');
				$('#siteList').datagrid('loadData',{total:0,rows:[]});
			}
		});
		
		$('#addCourseDialog').dialog({   
			width: 850,//宽度设置   
			height: 450,//高度设置 
			modal:true,
			closed: true,   
			cache: false,
			draggable:false,//是否可移动dialog框设置
			//打开事件
			onOpen:function(data){},
			//读取事件
			onLoad:function(data){}
		});
		
		$('#qtlxBKtimeDialog').dialog({   
			title:'请输入体育课和其它考试类型的补考时间',
			width: 600,//宽度设置   
			height: 188,//高度设置 
			modal:true,
			closed: true,   
			cache: false,
			draggable:false,//是否可移动dialog框设置
			//打开事件
			onOpen:function(data){},
			//读取事件
			onLoad:function(data){}
		});
		
		$('#exportTimetable').dialog({
				title:'导出预览',
				maximized:true,
				modal:true,
				closed: true,   
				cache: false, 
				draggable:false,//是否可移动dialog框设置
				//打开事件
				onOpen:function(data){},
				//读取事件
				onLoad:function(data){},
				//关闭事件
				onClose:function(data){
					$("#exportTimetableiframe").attr('src', '');
				}
		});
		
		$('#editTeaDialog').dialog({
			title:'编辑监考教师',
			width: 600,//宽度设置   
			height: 146,//高度设置 
			modal:true,
			closed: true,   
			cache: false, 
			draggable:false,//是否可移动dialog框设置
			//打开事件
			onOpen:function(data){},
			//读取事件
			onLoad:function(data){},
			//关闭事件
			onClose:function(data){
				vect=null;
			}
		});
		
		$('#msginfoDialog').dialog({
			title:'排监考教师结果',
			width: 300,//宽度设置   
			height: 300,//高度设置 
			modal:true,
			closed: true,   
			cache: false, 
			draggable:false,//是否可移动dialog框设置
			//打开事件
			onOpen:function(data){},
			//读取事件
			onLoad:function(data){},
			//关闭事件
			onClose:function(data){
				
			}
		});
		
	}
	
	function initPkDialog(){
		var pkDialogHeight = 0;
		if(sAuth.indexOf(admin)>-1 || sAuth.indexOf(pubTeacher)>-1)
			pkDialogHeight = 168;
		if(sAuth.indexOf(majorTeacher) > -1){
			pkDialogHeight = 168;
			$('#pk_major').show();
		}
		
		$('#courseArrange').dialog({
			title: '排监考教师',
			width: 300,//宽度设置   
			height: pkDialogHeight,//高度设置 
			modal:true,
			closed: true,
			cache: false, 
			draggable:false,//是否可移动dialog框设置
			//关闭事件
			onClose:function(data){
				$('#PK_XN').combobox('setValue', '');
				$('#PK_XQ').combobox('setText', '请先选择学年');
				$('#PK_XQ').combobox('disable');
				$('#PK_JXXZ').combobox('setText', '请先选择学年学期');
				$('#PK_JXXZ').combobox('disable');
				$('#PK_ZYDM').combobox('setValue', '');
			}
		});
	}
	
	/**关闭课表时保存修改的课程信息**/
	function saveCloseKcb(){
		$('#active').val('updateKCB');
		$('#kcbInfo').val(updateArray);
		var wpkcInfo = new Array();
		//解析未排课程json
		for(var i=0; i<wpkc.length; i++){
			wpkcInfo.push(wpkc[i].授课计划明细编号);
			wpkcInfo.push(wpkc[i].剩余节数);
		}
		$('#wpkcInfo').val(wpkcInfo);
		$("#form1").submit();
		updateArray.length = 0;
		$('#classTimetable').dialog('close');
	}
	
	/**关闭课表时不保存修改的课程信息**/
	function closeKcb(){
		updateArray.length = 0;
		$('#classTimetable').dialog('close');
	}
	
	/**加载combobox控件
		@jxxzData 教学性质下拉框数据
		@xnxqData 学年学期下拉框数据
		@zydmData 专业下拉框数据
		@teaLevel 教师层级下拉框数据
	**/
	function initCombobox(jxxzData, xnData, zydmData, levelData, siteTypeData){
		$('#PK_JXXZ_CX').combobox({
			data:jxxzData,
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
			onChange:function(data){}
		});
		
		$('#PK_XN').combobox({
			data:xnData,
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			panelHeight:'140', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data.length == 1){
					$('#PK_XN').combobox('setText', '没有可排课学年');
					$('#PK_XN').combobox('disable');
				}else{
					$('#PK_XN').combobox('enable');
					//初始化combobox时赋值
					$(this).combobox('setValue', '');
				}
			},
			//下拉列表值改变事件
			onChange:function(data){
				if(data == ''){
					$('#PK_XQ').combobox('setText', '请先选择学年');
					$('#PK_XQ').combobox('disable');
					$('#PK_JXXZ').combobox('setText', '请先选择学年学期');
					$('#PK_JXXZ').combobox('disable');
					$('#KC_QZQM').combobox('setText', '请先选择教学性质');
					$('#KC_QZQM').combobox('disable');
				}else{
					$('#PK_JXXZ').combobox('setText', '请先选择学年学期');
					$('#PK_JXXZ').combobox('disable');
					
					//读取学期下拉框
					$('#PK_XQ').combobox({
						url:'<%=request.getContextPath()%>/Svl_Pksz?active=queXqCombo&PK_XN=' + $('#PK_XN').combobox('getValue'),
						valueField:'comboValue',
						textField:'comboName',
						editable:false,
						panelHeight:'140', //combobox高度
						onLoadSuccess:function(data){
							//判断data参数是否为空
							if(data.length == 1){
								$('#PK_XQ').combobox('setText', '没有可选学期');
								$('#PK_XQ').combobox('disable');
							}else{
								$('#PK_XQ').combobox('enable');
								//初始化combobox时赋值
								$(this).combobox('setValue', '');
							}
						},
						//下拉列表值改变事件
						onChange:function(data){
							if(data == ''){
								$('#PK_JXXZ').combobox('setText', '请先选择学年学期');
								$('#PK_JXXZ').combobox('disable');
							}else{
								//读取教学性质下拉框
								$('#PK_JXXZ').combobox({
									url:'<%=request.getContextPath()%>/Svl_Pksz?active=queJxxzCombo&PK_XN=' + $('#PK_XN').combobox('getValue') + '&PK_XQ=' + $('#PK_XQ').combobox('getValue'),
									valueField:'comboValue',
									textField:'comboName',
									editable:false,
									panelHeight:'140', //combobox高度
									onLoadSuccess:function(data){
										//判断data参数是否为空
										if(data.length == 1){
											$('#PK_JXXZ').combobox('setText', '没有可选教学性质');
											$('#PK_JXXZ').combobox('disable');
										}else{
											$('#PK_JXXZ').combobox('enable');
											//初始化combobox时赋值
											$(this).combobox('setValue', '');
										}
									},
									//下拉列表值改变事件
									onChange:function(data){
										//考试名称combobox
										$('#KC_QZQM').combobox({
											url:"<%=request.getContextPath()%>/Svl_examSet?active=PKQZQMCombobox"+"&xnxq="+($('#PK_XN').combobox('getValue')+$('#PK_XQ').combobox('getValue')+$('#PK_JXXZ').combobox('getValue')),
											valueField:'comboValue',
											textField:'comboName',
											editable:false,
											panelHeight:'140', //combobox高度
											onLoadSuccess:function(data){
												//判断data参数是否为空
												if(data.length == 0){
													$('#KC_QZQM').combobox('setText', '没有可选的考试');
													$('#KC_QZQM').combobox('disable');
												}else{
													$('#KC_QZQM').combobox('enable');
													//初始化combobox时赋值
													$(this).combobox('setValue', data[0].comboValue);
												}	
											},
											//下拉列表值改变事件
											onChange:function(data){
												
											}
										});
										
										//系部名称combobox
										$('#KC_XBDM').combobox({
											url:"<%=request.getContextPath()%>/Svl_examSet?active=PKXBDMCombobox"+"&xnxq="+($('#PK_XN').combobox('getValue')+$('#PK_XQ').combobox('getValue')+$('#PK_JXXZ').combobox('getValue')),
											valueField:'comboValue',
											textField:'comboName',
											editable:false,
											panelHeight:'140', //combobox高度
											onLoadSuccess:function(data){
												$('#KC_XBDM').combobox('enable');
												$(this).combobox('setValue', data[0].comboValue);
													
											},
											//下拉列表值改变事件
											onChange:function(data){
												
											}
										});
										
									}
								});
							}
						}
					});
				}
			}
		});
		
		$('#PK_ZY_CX').combobox({
			data:zydmData,
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
			onChange:function(data){}
		});
		
		$('#PK_ZYDM').combobox({
			data:zydmData,
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
			onChange:function(data){}
		});
		
		$('#ic_teaLevel').combobox({
			data:levelData,
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
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
		
		$('#ic_siteType').combobox({
			data:siteTypeData,
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
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
		
		//查询学年学期combobox
// 		$('#CX_XNXQMC').combobox({
// 				url:"< %=request.getContextPath()%>/Svl_examSet?active=CX_XNXQMCCombobox",
// 				valueField:'comboValue',
// 				textField:'comboName',
// 				editable:false,
// 				panelHeight:'140', //combobox高度
// 				onLoadSuccess:function(data){
					//判断data参数是否为空
// 					if(data != ''){
						//初始化combobox时赋值
// 						$(this).combobox('setValue', data[0].comboValue);
// 					}
// 				},
				//下拉列表值改变事件
// 				onChange:function(data){
// 					loadGridKCAP(data);
// 				}
// 		});
		
		//查询考试周期combobox
		$('#CX_QZQM').combobox({
				url:"<%=request.getContextPath()%>/Svl_examSet?active=CXQZQMCombobox",
				valueField:'comboValue',
				textField:'comboName',
				editable:false,
				panelHeight:'140', //combobox高度
				onLoadSuccess:function(data){
					//判断data参数是否为空
					if(data != ''){
						//初始化combobox时赋值
						$(this).combobox('setValue', data[0].comboValue);
					}
				},
				//下拉列表值改变事件
				onChange:function(data){
					
				}
		});
	}
	
	/**读取学年学期课程表datagrid数据**/
	function loadSemesterData(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Pksz',
			data : 'active=queSemesterList&sAuth=' + sAuth + '&page=' + pageNum + '&rows=' + pageSize + 
				'&PK_XNXQMC_CX=' + encodeURI(PK_XNXQMC_CX) + 
				'&PK_JXXZ_CX=' + PK_JXXZ_CX +
				'&PK_ZY_CX=' + PK_ZY_CX +
				'&PK_TJZT_CX=' + PK_TJZT_CX,
			dataType:"json",
			success : function(data) {
				loadSemesterGrid(data[0].listData, data[0].existInfo);
			}
		});
	}
	
	/**加载学年学期课程表datagrid控件，读取页面信息
		@listData 列表数据
		@existInfo 已存在的课表信息
	**/
	function loadSemesterGrid(listData, existInfo){
		var cols = new Array();
		if(sAuth.indexOf(admin)>-1 || sAuth.indexOf(pubTeacher)>-1){
			cols.push({field:'PK_XNXQBM',title:'学年学期编码',width:fillsize(0.2),align:'center'});
			cols.push({field:'PK_XNXQMC',title:'学年学期名称',width:fillsize(0.2),align:'center'});
			cols.push({field:'jxxz',title:'教学性质',width:fillsize(0.2),align:'center'});
			cols.push({field:'PK_TJZT',title:'确认状态',width:fillsize(0.2),align:'center',
				formatter:function(value,rec){
					if(value == '0')
						return '未确认';
					else
						return '已确认';
				}
			});
			cols.push({field:'col4',title:'操作',width:fillsize(0.2),align:'center',
						formatter:function(value,rec){
							return "<input type='button' value='[详情]' onclick='timetableDetail(\"" + rec.PK_XNXQMC+rec.jxxz + "\",\"" + rec.PK_XNXQBM + "\",\"" + rec.PK_ZYDM + "\");' style=\"cursor:pointer;\">";
				}});
		}else{
			cols.push({field:'PK_XNXQBM',title:'学年学期编码',width:fillsize(0.2),align:'center'});
			cols.push({field:'PK_XNXQMC',title:'学年学期名称',width:fillsize(0.2),align:'center'});
			cols.push({field:'jxxz',title:'教学性质',width:fillsize(0.2),align:'center'});
			cols.push({field:'PK_ZYMC',title:'专业',width:fillsize(0.2),align:'center'});
			cols.push({field:'col4',title:'操作',width:fillsize(0.2),align:'center',
						formatter:function(value,rec){
							return "<input type='button' value='[详情]' onclick='timetableDetail(\"" + rec.PK_XNXQMC+rec.jxxz + "\",\"" + rec.PK_XNXQBM + "\",\"" + rec.PK_ZYDM + "\");' style=\"cursor:pointer;\">";
				}});
		}
	
		$('#semesterList').datagrid({
			//url:'semesterList.json',
			data:listData,
			title:'学年学期课程表信息列表',
			width:'100%',
			nowrap: false,
			fit:true, //自适应父节点宽度和高度
			showFooter:true,
			striped:true,
			pagination:true,
			pageSize:pageSize,
			singleSelect:true,
			pageNumber:pageNum,
			rownumbers:true,
			fitColumns: true,
			columns:[cols],
			//双击某行时触发
			onDblClickRow:function(rowIndex,rowData){},
			//读取datagrid之前加载
			onBeforeLoad:function(){},
			//单击某行时触发
			onClickRow:function(rowIndex,rowData){
				//主键赋值
				iKeyCode = rowData.PK_XNXQBM;
				rowInfo = rowData;
			},
			//加载成功后触发
			onLoadSuccess: function(data){
				iKeyCode = '';
				rowInfo = '';
				
				//var tempData = listData.rows;
				//for(var i=0; i<tempData.length; i++){
					//existArray.push(tempData[i].PK_XNXQBM, tempData[i].PK_ZYDM, tempData[i].PK_TJZT);
				//}
				if(existInfo != ''){
					existArray = existInfo.split(',');
				}else{
					existArray.length = 0;
				}
			}
		});
		
		$("#semesterList").datagrid("getPager").pagination({ 
			total:listData.total,
			afterPageText: '页&nbsp;<a href="#" onclick="goEnterPage();">Go</a>&nbsp;&nbsp;&nbsp;共 {pages} 页',
			onSelectPage:function (pageNo, pageSize_1) {
				pageNum = pageNo;
				pageSize = pageSize_1;
				loadSemesterData();
			} 
		});
	};
	
	function goEnterPage(){
		//pageNum = $('.pagination-num').val();
		//pageSize = $('.pagination-page-list').val();
        //loadSemesterData();
        var e = jQuery.Event("keydown");//模拟一个键盘事件 
		e.keyCode = 13;//keyCode=13是回车 
		$("input.pagination-num").trigger(e);//模拟页码框按下回车 
	}
	
	/**工具栏按钮调用方法，传入按钮的id
		@id 当前按钮点击事件
	**/
	var teachid="";
	var teachname="";
	var roomid="";
	var roomname="";
	function doToolbar(id, PK_XNXQBM, nodeId){
		//查询学年学期列表
		if(id == 'queSemester'){
			pageNum = 1;
			PK_XNXQMC_CX = $('#PK_XNXQMC_CX').textbox('getValue'); 
			PK_JXXZ_CX = $('#PK_JXXZ_CX').combobox('getValue');
			PK_ZY_CX = $('#PK_ZY_CX').combobox('getValue');
			PK_TJZT_CX = $('#PK_TJZT_CX').combobox('getValue');
			//loadGridKCAP();
		}
		
		//排课
		if(id == 'apkc'){
			$('#courseArrange').dialog('open');
		}
		
		//公共课确认
		if(id == 'pubConfirm'){
			if(rowInfo == ''){
				alertMsg('请选择一条数据');
				return;
			}
			
			if(rowInfo.PK_TJZT == '0'){
				ConfirmMsg('确认后当前选择的学年学期的公共课将无法再自动排课，是否确定要确认&nbsp;<font color="red">' + rowInfo.PK_XNXQMC.replace(' ', '')+rowInfo.jxxz + '</font>&nbsp;的公共课信息？', 'pubCourseConfirm()', '');
			}else{
				alertMsg('当前选择的学年学期已确认了公共课');
			}
		}
		
		//删除当前学期课表
		if(id == 'del'){ 
			if(rowInfo == ''){
				alertMsg('请选择一条数据');
				return;
			}
			
			ConfirmMsg('当前选择的课程表删除后无法恢复（包括添加课程信息及调课信息），是否确定删除&nbsp;<font color="red">' + rowInfo.PK_XNXQMC.replace(' ', '')+rowInfo.jxxz + '</font>&nbsp;的课程表？', 'delTimetable()', '');
		}
		
		//保存当前班级课程表
		if(id == 'save'){
			if(updateArray.length > 0){
				$('#active').val('updateKCB');
				$('#kcbInfo').val(updateArray);
				
				var wpkcInfo = new Array();
				//解析未排课程json
				for(var i=0; i<wpkc.length; i++){
					wpkcInfo.push(wpkc[i].授课计划明细编号);
					wpkcInfo.push(wpkc[i].剩余节数);
				}
				
				$('#wpkcInfo').val(wpkcInfo);
				$("#form1").submit();
				
				if(PK_XNXQBM != undefined){
					loadClassKcb(PK_XNXQBM, nodeId);
				}
				updateArray.length = 0;
			}
		}
		
		//备注
		if(id == 'remark'){
			if(curSelClass != ''){
				$('#PK_BZ').val(kcbBZ);
				$('#remarkDialog').dialog('open');
			}else{
				alertMsg('请选择一个班级');
			}
		}
		
		//保存备注
		if(id == 'saveRemark'){
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_Pksz',
				data : 'active=saveRemark&PK_XNXQBM=' + curSelXnxq + '&PK_XZBDM=' + curSelClass + '&PK_BZ=' + encodeURI(encodeURI($('#PK_BZ').val())),
				dataType:"json",
				success : function(data) {
					if(data[0].MSG == '保存成功'){
						kcbBZ = $('#PK_BZ').val();
						$('#remarkDialog').dialog('close');
						showMsg(data[0].MSG);
					}else{
						alertMsg(data[0].MSG);
					}
				}
			});
		}
		
		//学期课程
		if(id == 'semCourse'){
			$('#addCourseDialog').dialog({   
				width: 850,//宽度设置   
				height: 500,//高度设置 
				title:'添加学期课程',
				//关闭事件
				onClose:function(data){
					$('#addCourseFrame').attr('src', '');
				}
			});
			$('#addCourseFrame').attr('src', '<%=request.getContextPath()%>/form/courseManage/xqkc.jsp');
			$('#addCourseDialog').dialog('open');
		}
		
		//整班课程
		if(id == 'classCourse'){
			$('#addCourseDialog').dialog({   
				width: 850,//宽度设置   
				height: 450,//高度设置 
				title:'添加整班课程',
				//关闭事件
				onClose:function(data){
					$('#addCourseFrame').attr('src', '');
					queOtherInfo("queAddCourseInfo", curSelXnxq, curSelClass);
				}
			});
			$('#addCourseFrame').attr('src', '<%=request.getContextPath()%>/form/courseManage/zbkc.jsp');
			$('#addCourseDialog').dialog('open');
		}
		
		//个人课程
		if(id == 'stuCourse'){
			$('#addCourseDialog').dialog({   
				width: 900,//宽度设置   
				height: 450,//高度设置
				title:'添加个人课程',
				//关闭事件
				onClose:function(data){
					$('#addCourseFrame').attr('src', '');
				}
			});
			$('#addCourseFrame').attr('src', '<%=request.getContextPath()%>/form/courseManage/grkc.jsp');
			$('#addCourseDialog').dialog('open');
		}
		
		//确认更改周次
		if(id == 'confirmWeek'){
			var selWeek = '';
			$('.allNum').each(function(){
				if($(this).val() == '1'){
					selWeek += $(this).attr('name')+'#';
				}
			});
			
			if(selWeek.length == 0){
				alertMsg('请至少选择一个周次');
				return;
			}
			selWeek = selWeek.substring(0, selWeek.length-1);
			var curSelWeekArray = parseSkzc(selWeek);
			
			//修改周次为正确格式
			var weekName = '第';
			var weekCode = '';
			var flag = true;
			var oddFlag = true;
			var evenFlag = true;
			
			if(oddArray.length == curSelWeekArray.length){
				for(var i=0; i<oddArray.length; i++){
					if(oddArray[i] != curSelWeekArray[i]){
						oddFlag = false;
						break;
					}
				}
			}else{
				oddFlag = false;
			}
			
			if(evenArray.length == curSelWeekArray.length){
				for(var i=0; i<evenArray.length; i++){
					if(evenArray[i] != curSelWeekArray[i]){
						evenFlag = false;
						break;
					}
				}
			}else{
				evenFlag = false;
			}
			
			if(oddFlag == true){
				weekName = "单周";
				for(var i=0; i<curSelWeekArray.length; i++){
					weekCode += curSelWeekArray[i]+'#';
				}
				weekCode = weekCode.substring(0, weekCode.length-1);
			}else if(evenFlag == true){
				weekName = "双周";
				for(var i=0; i<curSelWeekArray.length; i++){
					weekCode += curSelWeekArray[i]+'#';
				}
				weekCode = weekCode.substring(0, weekCode.length-1);
			}else{
				for(var i=0; i<curSelWeekArray.length; i++){
					weekName += curSelWeekArray[i]+',';
					weekCode += curSelWeekArray[i]+'#';
					
					if(i < curSelWeekArray.length-1){
						if(parseInt(curSelWeekArray[i], 10)+1 != curSelWeekArray[i+1]){
							flag = false;
						}
					}
				}
				weekName = weekName.substring(0, weekName.length-1)+'周';
				weekCode = weekCode.substring(0, weekCode.length-1);
				
				if(flag==true && curSelWeekArray.length>1){
					weekName = curSelWeekArray[0]+'-'+curSelWeekArray[curSelWeekArray.length-1]+'周';
					weekCode = weekName.substring(0, weekName.length-1);
				}
			}
			
			$('#SKZCMC_' + curSelId).val(weekName);
			$('#SKZC_' + curSelId).val(weekCode);
			$('#weekNumDialog').dialog('close');
		}
		
		//查询授课教师列表
		if(id == 'queTeaList'){
			$('#confirmTea').linkbutton('disable');
			$('#' + id).linkbutton('disable');
			loadTeaListData();
		}
		
		//更改教师确认
		if(id == 'confirmTea'){
			if(newJKJSBH!=""){
				document.getElementById("JSBH_"+teacellnum).name=newJKJSBH;
				document.getElementById("JSBH_"+teacellnum).value=newJKJSXM;
				selJKJSBH=newJKJSBH;
				selJKJSXM=newJKJSXM;
				
				//alert(oldJKJSBH+"|"+oldJKJSXM);
				//alert(selJKJSBH+"|"+selJKJSXM);
				
				chgteaarray.length=0;
				
				chgteaarray.push(sjxl);
				chgteaarray.push(ori_jsbh);
				chgteaarray.push(ori_jsxm);
				
				var obj = document.getElementsByTagName("input");
				var newbh="";
				var newname="";
				for(var i=0;i<obj.length;i++){
					if(obj[i].id.indexOf("JSBH_")>-1){
						newbh+=obj[i].name+"，";
						newname+=obj[i].value+"，";
					}
				}
				newbh=newbh.substring(0,newbh.length-1);
				newname=newname.substring(0,newname.length-1);
				chgteaarray.push(newbh);
				chgteaarray.push(newname);
						
			}
			$('#teaListDialog').dialog('close');
		}
		
		//保存更改后的课程信息		
		if(id == 'saveJKJSInfo'){
			$('#changeCourseMask').show();		
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_examSet',
				data : 'active=saveJKJSInfo&KCAPZBBH='+ei_zbbh+'&XZBDM='+classId+'&KCMC='+encodeURI(ori_kcmc)+'&chgteaarray='+chgteaarray,
				dataType:"json",
				success : function(data) {
					$('#changeCourseMask').hide();
					$('#editTeaDialog').dialog('close');
					showMsg(data[0].MSG);
					loadClassKCAP(ei_xnxq, ei_zbbh, classId);
				}
			});
		}
		
		//查询授课教师列表
		if(id == 'queSiteList'){
			$('#confirmSite').linkbutton('disable');
			$('#' + id).linkbutton('disable');
			loadSiteListData();
		}
		
		//更改场地确认
		if(id == 'confirmSite'){ 
			if(curSelSiteCode.length == 0){
				alertMsg('请至少选择一个授课场地');
				return;
			}
			$('#CDBH_' + curSelId).val(curSelSiteCode);
			$('#CDMC_' + curSelId).val(curSelSiteName);
			roomid=$('#CDBH_' + curSelId).val();
			roomname=$('#CDMC_' + curSelId).val();
			$('#siteListDialog').dialog('close');
		}
		
		//导出考试安排表
		if(id == 'export'){ 
			//$('#exportTimetable').dialog('open');
			//$("#exportTimetableiframe").attr("src","<%=request.getContextPath()%>/form/timetableQuery/exportExamSchedule.jsp?xnxqbm="+ rowInfo.学年学期编码 +"&kszq="+ rowInfo.考试周期 );
			$('#maskFont').html('考试安排表导出中...');
    		$('#divPageMask').show();
			ExportExcelKSAPB();
		}
		
		//导出试卷标签
		if(id == 'exportTag'){ 
			//$('#exportTimetable').dialog('open');
			//$("#exportTimetableiframe").attr("src","<%=request.getContextPath()%>/form/timetableQuery/exportExamScheduleTag.jsp?xnxqbm="+ rowInfo.学年学期编码 +"&kszq="+ rowInfo.考试周期 );
			$('#maskFont').html('试卷标签导出中...');
    		$('#divPageMask').show();
			ExportExcelSJBQ();
		}
		
		if(id=="queryBKinfo"){
			BKINFOdatagrid(pkxnxqmc,pkxnxnbm,pkkslx,pkkszq);
		}
		//添加周课表信息
		/*
		if(id == 'addZkbxx'){
			$.ajax({
				type : "POST",
				url : '<-%=request.getContextPath()%>/Svl_Pksz',
				data : 'active=addZkbxx',
				dataType:"json",
				success : function(data) {
				}
			});
		}
		*/
	}
	
	//导出考试安排表==================================================================================
	function ExportExcelKSAPB(){ 
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_examSet',
				data : 'active=ExportExcelKSAPB&GG_XNXQBM='+curSelXnxq+'&QZQM='+curSelKszq,
				dataType:"json",
				success : function(data) {
					$('#divPageMask').hide(); 
					if(data[0].MSG == '文件生成成功'){
						//下载文件到本地
						$("#exportIframe").attr("src", '<%=request.getContextPath()%>/form/elective/fileDownload.jsp?filePath=' + encodeURI(encodeURI(data[0].filePath)));
					}else{
						alertMsg(data[0].MSG);
					}
				}
			});
	}
	
	//导出试卷标签
	function ExportExcelSJBQ(){
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_examSet',
				data : 'active=ExportExcelSJBQ&GG_XNXQBM='+curSelXnxq+'&QZQM='+curSelKszq,
				dataType:"json",
				success : function(data) {
					$('#divPageMask').hide(); 
					if(data[0].MSG == '文件生成成功'){
						//下载文件到本地
						$("#exportIframe").attr("src", '<%=request.getContextPath()%>/form/elective/fileDownload.jsp?filePath=' + encodeURI(encodeURI(data[0].filePath)));
					}else{
						alertMsg(data[0].MSG);
					}
				}
			});
	}
	
	
	
	/**更改课程信息
		@id 单元格id
	**/
	var courseInfoId = new Array();
	var curTimeOrder = '';
	var curSelId = '';
	function changeCourseInfo(id){
		//判断单元格有内容
		var changeFlag = $('#changeFlag_' + id.substring(id.indexOf('_')+1)).val();
		if($('#'+id).html()!='&nbsp;' && changeFlag!='2' && changeFlag!='3'){
			curTimeOrder = id.substring(id.indexOf('_')+1);
			
			//判断排课权限
			var kcbIndex = -1;
			for(var c=0;c<classKCB.length;c++){
				if(curTimeOrder==classKCB[c].时间序列){
					kcbIndex=c;
				}
			}
			
			var content = '';
			var tempContent = '';
			var rowNum = 0;
			var totalNum = 0;
			
			//var kcStr = $('#kcb_' + curTimeOrder).attr('title').split('\n\n')[1].split('\n');
	
			var skjhmxbhArray = $('#skjhmxbh_' + curTimeOrder).val().split('｜');
			var kcmcArray = classKCB[kcbIndex].课程名称.split('｜');
			//var kclxArray = $('#kclx_' + curTimeOrder).val().split('｜');
			var skjsbhArray = $('#skjsbh_' + curTimeOrder).val().split('｜');	
			var tempSkjsbhArray = '';
			var skjsmcArray = classKCB[kcbIndex].监考教师姓名.replace(/\+/g, '\,').split('｜');
			var tempSkjsmcArray = '';
			var cdbhArray = $('#cdbh_' + curTimeOrder).val().split('｜');
			var tempCdbhArray = '';
			var cdmcArray = classKCB[kcbIndex].场地名称.replace(/\+/g, '\,').split('｜');
			var tempCdmcArray = '';
			//var skzcArray = $('#kczcxq_' + curTimeOrder).val().split('｜');
		
			
			var tempCourseInfoId = new Array();
			//courseInfoId.length = 0;
		
			for(var i=0; i<kcmcArray.length; i++){
			
				tempSkjsbhArray = skjsbhArray[i].split('&');
				tempSkjsmcArray = skjsmcArray[i].split('&');
				tempCdbhArray = cdbhArray[i].split('&');
				tempCdmcArray = cdmcArray[i].split('&');
				
				tempContent = '';
				rowNum = 0;
				tempCourseInfoId = new Array();
		
				for(var j=0; j<tempSkjsbhArray.length; j++){ 
					rowNum++;
					totalNum++;
						
					if(j > 0){
						tempContent += '<tr>';
					}
					tempCourseInfoId.push(i+'_'+j);
					
					//授课周次
					
					//授课教师 
					tempContent += '<td style="width:22%;"'; 
					if(sAuth.indexOf(admin) < 0){
						tempContent += 'class="titlestyle">'+tempSkjsmcArray[j];
					}else{
						tempContent += '><input id="SKJSMC_' + i+'_'+j + '" value="'+tempSkjsmcArray[j]+'" style="width:100%; cursor:pointer;" onclick="openTeaList(this.id);" readonly="readonly"/>';
					}
					tempContent += '</td>';
					
					//授课场地		
					tempContent += '<td style="width:22%;"';
					if((sAuth.indexOf(pubTeacher)>-1&&kclxArray[i]=='02') || (sAuth.indexOf(majorTeacher)>-1&&kclxArray[i]=='01')){
						tempContent += 'class="titlestyle">'+tempCdbhArray[j];
					}else{
						tempContent += '><input id="CDBH_' + i+'_'+j + '" value="'+tempCdbhArray[j]+'" style="width:100%; cursor:pointer;" onclick="openSiteList(this.id);" readonly="readonly"/>';
					}
					tempContent += '</td></tr>' +
								'<input type="hidden" id="SKJHMXBH_' + i+'_'+j + '" value="' + skjhmxbhArray[i] + '"/>' +
								'<input type="hidden" id="SKJSBH_' + i+'_'+j + '" value="' + tempSkjsbhArray[j] + '"/>' +
								'<input type="hidden" id="CDBH_' + i+'_'+j + '" value="' + tempCdbhArray[j] + '"/>' +
								'<input type="hidden" id="CDMC_' + i+'_'+j + '" value="' + tempCdmcArray[j] + '"/>';
					
					teachid=tempSkjsbhArray[j];
					teachname=tempSkjsmcArray[j];
					roomid=tempCdbhArray[j];
					roomname=tempCdmcArray[j];
				}
				content += '<tr><td style="width:30%;" rowspan="'+rowNum+'" class="titlestyle">' + kcmcArray[i] + '</td>' + tempContent;
				//courseInfoId.push(tempCourseInfoId);
				
			}
			$('#courseList').html(content);
			$('#changeCourseInfoDialog').dialog({
				height:70+27*totalNum-(totalNum-1)
			});
			
			$('#changeCourseInfoDialog').dialog('open');
		}
	}
	
	/**打开周次选择对话框
	**/
	function openWeekList(id){
		$('#' + id).blur();
		curSelId = id.substring(id.indexOf('_')+1);
		curSelSkzc = $('#SKZC_' + curSelId).val();
		
		//查询周次可用情况
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Pksz',
			data : 'active=checkWeekUsable&PT_XZBDM=' + curSelClass + '&PT_SJXL=' + curTimeOrder + 
				'&PT_SKJSBH=' + $('#SKJSBH_' + curSelId).val().replace(/\+/g, ',') + 
				'&PT_CDBH=' + $('#CDBH_' + curSelId).val().replace(/\+/g, ',') + "&xqzc=" + curXqzc,
			dataType:"json",
			success : function(data) {
				var tipsArray = new Array();
				var tipsContent = '';
			
				//选中当前授课周次
				var skzcArray = parseSkzc(curSelSkzc);
				for(var i=0; i<skzcArray.length; i++){
					$('#week_' + skzcArray[i]).css('background-color', '#E7FECB');
					$('#weekNumFlag_' + skzcArray[i]).val('1');
				}
				
				//验证选择的周次是否已被使用
				var tempWeekArray = new Array();
				var usedWeekArray = new Array();
				for(var i=0; i<courseInfoId.length; i++){
					for(var j=0; j<courseInfoId[i].length; j++){
						if(curSelId != (i+'_'+j)){
							tempWeekArray = parseSkzc($('#SKZC_' + i+'_'+j).val());
							
							for(var k=0; k<tempWeekArray.length; k++){
								$('#week_' + tempWeekArray[k]).css('background-color', '#AAAAAA');
								$('#week_' + tempWeekArray[k]).attr('title', '本班该周次已安排其他课程');
								$('#week_' + tempWeekArray[k]).css('cursor', '');
								$('#weekNumFlag_' + tempWeekArray[k]).val('2');
								
								usedWeekArray.push(tempWeekArray[k]);
							}
						}
					}
				}
				
				//添加教师场地不可用提示
				for(var i=1; i<parseInt(curXqzc, 10)+1; i++){
					if($('#weekNumFlag_' + i).val() != '2'){
						for(var j=0; j<data.length; j++){
							if(data[j].weekNum == i){
								$('#week_' + i).css('background-color', '#AAAAAA');
								tipsContent = '';
								tipsArray = data[j].tips.split(',');
								
								for(var k=0; k<tipsArray.length; k++){
									tipsContent += tipsArray[k]+'\n';
								}
								
								$('#week_' + i).attr('title', tipsContent.substring(0, tipsContent.length-2));
								$('#week_' + i).css('cursor', '');
								$('#weekNumFlag_' + i).val('2');
							}
						}
					}
				}
				
				$('#weekNumDialog').dialog('open');
			}
		});
	}
	
	/**选择周次**/
	function selWeekNum(id){
		var curNum = '';
		var tempObj = '';
	
		if(id == 'week_all'){
			$('.weekNumTd').each(function(){
				curNum = $(this).attr('id').substring(5);
				tempObj = $('#weekNumFlag_' + curNum);
				
				if(tempObj.val()!='1' && tempObj.val()!='2'){
					$(this).css('background-color', '#E7FECB');
					tempObj.val('1');
				}
			});
		}else{
			var numArray = new Array();
			
			if(id == 'week_odd')
				numArray = ['1', '0'];
			if(id == 'week_even')
				numArray = ['0', '1'];
			
			$('.weekNumTd').each(function(){
				curNum = $(this).attr('id').substring(5);
				tempObj = $('#weekNumFlag_' + curNum);
				
				if(curNum%2==numArray[0] && tempObj.val()!='1' && tempObj.val()!='2'){
					$(this).css('background-color', '#E7FECB');
					tempObj.val('1');
				}else if(curNum%2==numArray[1] && tempObj.val()!='2'){
					$(this).css('background-color', '#FFFFFF');
					tempObj.val('0');
				}
			});
		}
	}
	
	function formatterData(selData){
		var result = '';
	
		for(var i=0; i<selData.length; i++){
			result += selData[i]+'+';
		}
		
		if(result.length > 1)
			result = result.substring(0, result.length-1);
		
		return result;
	}
	
	/**打开场地列表
		@param id 当前选中的场地input id
	**/
	function openSiteList(id){  
		$('#confirmSite').linkbutton('disable');
		$('#queSiteList').linkbutton('disable');
		$('#' + id).blur();
		curSelId = id.substring(id.indexOf('_')+1);
		curSelSiteCode = $('#CDBH_' + curSelId).val().split(',');
		curSelSiteName = $('#CDMC_' + curSelId).val().split(',');
		
		loadSiteListData();
		$('#siteListDialog').dialog('open');
	}
	
	/**读取场地datagrid数据**/
	function loadSiteListData(){
		isLoad = true;
		
		$('#siteList').datagrid({
			url :'<%=request.getContextPath()%>/Svl_Pksz',
			queryParams: {'active':'queSiteList','CDBH_CX':$('#ic_siteCode').textbox('getValue'),
				'CDMC_CX':$('#ic_siteName').textbox('getValue'),'CDLX_CX':$('#ic_siteType').textbox('getValue'),
				'PT_XNXQBM':curSelXnxq,'PK_KCBMXBH':$('#kcb_' + curTimeOrder).attr('name'),
				'PK_SKJHMXBH':$('#SKJHMXBH_' + curSelId).val(),'PT_SKZC':'',
				'PT_CDBH':curSelSiteCode,'PT_SJXL':curTimeOrder,'curXqzc':''},
			title:'',
			width:'100%',
			nowrap: false,
			fit:true, //自适应父节点宽度和高度
			showFooter:true,
			striped:true,
			pagination:true,
			pageNumber:1,
			pageSize:20,
			singleSelect:false,
			rownumbers:true,
			fitColumns: true,
			columns:[[
				//field为读取数据的数据名，title为显示的数据名，width宽度设置，align数字在表格中显示的位置
				{field:'ck',checkbox:true},
				{field:'教室编号',title:'教室编号',width:fillsize(0.25),align:'center'},
				{field:'教室名称',title:'教室名称',width:fillsize(0.25),align:'center'},
				{field:'教室容量',title:'教室容量',width:fillsize(0.25),align:'center'},
				{field:'教室类型',title:'教室类型',width:fillsize(0.25),align:'center'}
			]],
			onSelect:function(rowIndex,rowData){
				if(isLoad == false){
					curSelSiteCode.push(rowData.教室编号);
					curSelSiteName.push(rowData.教室名称);
				}
			},
			onUnselect:function(rowIndex,rowData){
				$.each(curSelSiteCode, function(key,value){
					if(value == rowData.教室编号){
						curSelSiteCode.splice(key, 1);
						curSelSiteName.splice(key, 1);
					}
				});
			},
			//加载成功后触发
			onLoadSuccess: function(data){
				$(".datagrid-header-check").html('&nbsp;');//隐藏全选
				//勾选已选授课教师
				if(data){
					$.each(data.rows, function(rowIndex, rowData){
						
							if(curSelSiteCode == rowData.教室编号){
								$('#siteList').datagrid('selectRow', rowIndex);
							}
						
					});
				}
				
				isLoad = false;
				$('#confirmSite').linkbutton('enable');
				$('#queSiteList').linkbutton('enable');
			}
		});
	}
	
	/**初始化场地combotree
		@param comboData 下拉树数据
	**/
	/*
	function loadSiteComboTree(comboData){
		var tempArray = '';
		
		for(var i=0; i<siteComboValue.length; i++){
			tempArray = siteComboValue[i];
			
			for(var j=0; j<tempArray.length; j+=2){
				//读取可用教室下拉框
				$('#ic_CDBH_' + tempArray[j]).combotree({
					data:comboData,
					multiple:true,
		            valueField: 'id',
		            textField: 'text',
		            //required: true,
		            editable: false,
		            onlyLeafCheck:true,
		            //cascadeCheck:false,
		            onLoadSuccess:function(node, data){
						$('#ic_CDBH_' + tempArray[j]).combotree('tree').tree("collapseAll");
						$('#ic_CDBH_' + tempArray[j]).combotree('setValues', tempArray[j+1].split('+'));
						//展开所有选中的节点
						var selArray = $('#ic_CDBH_'  + tempArray[j]).combotree('tree').tree('getChecked');
	            		for(var i=0; i<selArray.length; i++){
	            			$('#ic_CDBH_' + tempArray[j]).combotree('tree').tree('expandTo', selArray[i].target);
	            		}
		            },
		            //下拉列表值选择事件
					onCheck:function(node, checked){
					}
				});
			}
		}
		
		$('.disableCombo').combobox('disable');
	}
	*/
	
	/**表单提交**/
	$('#form1').form({
		//定位到servlet位置的url
		url:'<%=request.getContextPath()%>/Svl_Pksz',
		//当点击事件后触发的事件
		onSubmit: function(data){}, 
		//当点击事件并成功提交后触发的事件
		success:function(data){
			var json  =  eval("("+data+")");
			if(json[0].MSG == '保存成功'){
				if($('#active').val() == 'updateKCB'){
					updateArray.length = 0;
				}else{
					var tempTeaInfo = parseUsedInfo(json[0].teaUsedOrder);
					var teaListArray = json[0].teaList.split(',');
					var keepFlag = false;
					
					//更新教师已用信息
					for(var i=0; i<teaUsedOrderInfo.length; i+=5){
						for(var j=0; j<tempTeaInfo.length; j+=5){
							if(teaUsedOrderInfo[i] == tempTeaInfo[j]){
								teaUsedOrderInfo[i+4] = tempTeaInfo[j+4];
								tempTeaInfo.splice(j, 5);
							}
						}
						
						//判断当前遍历的教师已用信息是否还需要保留
						keepFlag = false;
						for(var j=0; j<teaListArray.length; j++){
							if(teaUsedOrderInfo[i] == teaListArray[j]){
								keepFlag = true;
								break;
							}
						}
						if(keepFlag == false){
							teaUsedOrderInfo.splice(i, 5);
							i-=5;
						}
					}
					if(tempTeaInfo.length > 0){
						for(var i=0; i<tempTeaInfo.length; i+=5){
							teaUsedOrderInfo.push(tempTeaInfo[i]);
							teaUsedOrderInfo.push(tempTeaInfo[i+1]);
							teaUsedOrderInfo.push(tempTeaInfo[i+2]);
							teaUsedOrderInfo.push(tempTeaInfo[i+3]);
							teaUsedOrderInfo.push(tempTeaInfo[i+4]);
						}
					}
					
					var teaCode = json[0].teaCode.replace(/\&amp;/g, '&');
					var teaName = json[0].teaName.replace(/\&amp;/g, '&');
					var siteCode = json[0].siteCode.replace(/\&amp;/g, '&');
					var siteName = json[0].siteName.replace(/\&amp;/g, '&');
					var weekAll = json[0].weekAll.replace(/\&amp;/g, '&');
					var weekDetail = json[0].weekDetail.replace(/\&amp;/g, '&');
					var tempTitleArray = $('#kcb_' + curTimeOrder).attr('title').split('\n');
					var tempContent = '';
					var tempTitle = tempTitleArray[0];
					var tempNum = 1;
					
					var kcbContent = $('#kcb_' + curTimeOrder).html().split('<BR>');
					var courseInfo = kcbContent[1].substring(kcbContent[1].indexOf('<INPUT'));
					tempContent = kcbContent[0] + '<br/>' + teaName;
					if(kcbContent[0].indexOf('<FONT') > -1)
						tempContent += '</font>';
					tempContent += courseInfo;
					
					if(tempTitle == '固排课程'){
						for(var i=1; i<3; i++){
							tempTitle += '\n'+tempTitleArray[i];
							tempNum++;
						}
					}
					tempTitle += '\n'+teaName+'\n'+siteName+'\n'+parseWeekShow(weekDetail, oddArray, evenArray);
					
					for(var i=(tempNum+3); i<tempTitleArray.length; i++){
						tempTitle += '\n'+tempTitleArray[i];
					}
					
					$('#kcb_' + curTimeOrder).html(tempContent);
					$('#kcb_' + curTimeOrder).attr('title', tempTitle);
					$('#skjsbh_' + curTimeOrder).val(teaCode);
					$('#cdbh_' + curTimeOrder).val(siteCode);
					$('#kczc_' + curTimeOrder).val(weekAll);
					$('#kczcxq_' + curTimeOrder).val(weekDetail);
					
					//遍历需要更新的课表信息中是否有当前单元格信息，有的话更新课程信息
					var code = $('#kcb_' + curTimeOrder).attr('name');
					for(var i=0; i<updateArray.length; i+=9){
						if(updateArray[i] == code){
							updateArray[i+3] = teaCode;
							updateArray[i+4] = teaName;
							updateArray[i+5] = siteCode;
							updateArray[i+6] = siteName;
							updateArray[i+7] = weekAll;
							updateArray[i+8] = weekDetail;
							break;
						}
					}
					
					//更新当前课表中的相关场地信息
					for(var i=0; i<classKCB.length; i++){
						if(classKCB[i].课程表明细编号 == code){
							classKCB[i].授课教师编号 = teaCode;
							classKCB[i].授课教师姓名 = teaName;
							classKCB[i].实际场地编号 = siteCode;
							classKCB[i].实际场地名称 = siteName;
							classKCB[i].授课周次 = weekAll;
							classKCB[i].授课周次详情 = weekDetail;
						}
					}
					
					$('#changeCourseMask').hide();
					$('#changeCourseInfoDialog').dialog('close');
				}
				
				//提示信息
				showMsg(json[0].MSG);
			}else{
				alertMsg(json[0].MSG);
			}
		}
	});
	
	/**检查学年学期考试是否存在**/
	var sfypks="0";
	function checkSemesterExist(){
		//判断是否选择了学年学期
		if($('#PK_XN').combobox('getValue')=='' || $('#PK_XQ').combobox('getValue')=='' || $('#PK_JXXZ').combobox('getValue') == ''){
			alertMsg('请选择学年学期及教学性质');
			return;
		}
		if($('#KC_XBDM').combobox('getValue')==''){
			alertMsg('请选择系部');
			return;
		}
		
		$.ajax({
				type: "POST",
				url: '<%=request.getContextPath()%>/Svl_examSet',
				data: "active=searchKSLX&ex_kszbbh=" + $('#KC_QZQM').combobox('getValue')+"&ex_xbdm=" + $('#KC_XBDM').combobox('getValue'),
				dataType: 'json',
				success: function(datas){
		            var data = datas[0];
					//if(data.MSG=="1"){//考试类型
						var curXnxq = $('#PK_XN').combobox('getValue') + $('#PK_XQ').combobox('getValue') + $('#PK_JXXZ').combobox('getValue');
						if(data.MSG2=="1"){//已排过考试
							ConfirmMsg('当前选择的系部已按排过监考教师，是否确定重排？', 'startAreaArrange("' + curXnxq + '","' + $('#KC_QZQM').combobox('getValue') + '","' + $('#KC_XBDM').combobox('getValue') + '" )');
						}else{  
							startAreaArrange(curXnxq, $('#KC_QZQM').combobox('getValue'),$('#KC_XBDM').combobox('getValue'));
						}
// 					}else{
// 						$('#qtlxBKtimeDialog').dialog("open");
// 					}
				}
		});
	
// 		if(flag){
// 			ConfirmMsg('当前选择的学年学期已排过课表，是否确定重排？', 'startCourseArrange("' + curXnxq + '","' + $('#PK_ZYDM').combobox('getValue') + '", "1", "1", "1")', '');
// 		}else{
// 			startCourseArrange(curXnxq, $('#PK_ZYDM').combobox('getValue'), '1', '1', '1');
// 		}
	}
	
	/**公共课确认*/
	function pubCourseConfirm(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Pksz',
			data : 'active=pubCourseConfirm&PK_XNXQBM=' + rowInfo.PK_XNXQBM,
			dataType:"json",
			success : function(data) {
				if(data[0].MSG == '保存成功'){
					loadSemesterData();
					showMsg(data[0].MSG);
				}else{
					alertMsg(data[0].MSG);
				}
			}
		});
	}
	
	/**删除课程表*/
	function delTimetable(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Pksz',
			data : 'active=delTimetable&PK_XNXQBM=' + rowInfo.PK_XNXQBM,
			dataType:"json",
			success : function(data) {
				if(data[0].MSG == '删除成功'){
					loadSemesterData();
					showMsg(data[0].MSG);
				}else{
					alertMsg(data[0].MSG);
				}
			}
		});
	}
	
	/**重新自动排课
		@type 排课类型(1全新排课/2继续排剩下的课)
	*/
	function reCourseArrange(type){
		var curXnxq = cpXnxq;
		var curZydm = cpZydm;
		var tsgzFlag = '1';
		var checkTeaFlag = '1';
		
		if($('#tsgz').is(':checked')){
			tsgzFlag = '2';
		}
		if($('#kcjp').is(':checked')){
			checkTeaFlag = '2';
		}
		
		$('#compPartTips').dialog('close');
		startCourseArrange(curXnxq, curZydm, type, tsgzFlag, checkTeaFlag);
	}
	
	/**自动排课
		@curXnxq 当前选择的学年学期
		@curZydm 当前选择的专业代码
		@type 排课类型(1全新排课/2继续排剩下的课)
		@tsgzFlag 特殊规则排课标识(1验证/2不验证)
		@checkTeaFlag 禁排标识(1验证/2不验证)
	*/
	function startCourseArrange(curXnxq, curZydm, type, tsgzFlag, checkTeaFlag){
		$('#pkTips').html('正在初始化课程表信息...');
		$('#pkClass').html('&nbsp;');
		$('#pkCourse').html('&nbsp;');
		$('#progressBar').css('width', '0');
		$('#percent').html('0%');
		$('#percent').css('color', 'black');
	
		//检查是否有老师正在排课
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Pksz',
			data : 'active=checkPkState',
			async: false,
			dataType:"json",
			success : function(data) {
				//判断有教师正在排课
				if(data[0].pkState == 'using'){
					var msg = data[0].pkTeaName + '老师正在使用排课功能<br/>已用时间:'+data[0].pkTime;
					//msg += '<br/>联系方式:'+data[0].pkTeaTel;
					msg += '<br/><br/>为保证排课的正确性，您目前只能查看课程表，请稍后再进行排课操作！';
					alertMsg(msg);
				}else{

					$.ajax({
						type : "POST",
						url : '<%=request.getContextPath()%>/Svl_Pksz',
						data : 'active=checkCourseArrange&sAuth='+sAuth+'&PK_XNXQBM=' + curXnxq + '&PK_ZYDM=' + curZydm + 
							'&type=' + type + '&tsgzFlag=' + tsgzFlag + '&checkTeaFlag=' + checkTeaFlag,
						dataType:"json",
						success : function(data) { 
							if(data[0].MSG=="没有教室冲突"){
								setPkState('using');
								tempSemesterName = $('#PK_XN').combobox('getText') + $('#PK_XQ').combobox('getText') + $('#PK_JXXZ').combobox('getText');//保存当前选择自动排课的学期名字
								//显示遮罩层和进度条
								$('#courseArrange').dialog('close');
								$('#maskDiv').dialog('open');
								enterTimes = 0;//重置查询进度次数
								var timer = setInterval("loadProgressBar();", 1000);
								
								$.ajax({
									type : "POST",
									url : '<%=request.getContextPath()%>/Svl_Pksz',
									data : 'active=autoCourseArrange&sAuth='+sAuth+'&PK_XNXQBM=' + curXnxq + '&PK_ZYDM=' + curZydm + 
										'&type=' + type + '&tsgzFlag=' + tsgzFlag + '&checkTeaFlag=' + checkTeaFlag,
									dataType:"json",
									success : function(data) {
										clearInterval(timer);
										timer = setTimeout(function(){
											clearInterval(timer);
											
											//判断排课是否出错
											if(data.MSG=='complatePart' || data.MSG=='complateAll'){
												loadSemesterData();
												$('#progressBar').css('width', '99.5%');
												$('#percent').html('100%');
												$('#pkTips').html('自动排课已完成');
												$('#pkClass').html('&nbsp;');
												$('#pkCourse').html('&nbsp;');
												
												setTimeout(function(){
													clearInterval(timer);
													$('#maskDiv').dialog('close');
													cpXnxq = data.xnxq; //当前排课的学期
													cpZydm = curZydm; //当前排课的专业
													
													if(data.MSG=='complatePart'){
														var content = '';
														var wpkcInfo = data.wpkc;
														var tempMajor = '';
														var tempClass = '';
														
														//拼接未排课程信息
														content = '<span style="height:30px; line-height:30px; color:blue; font-size:14px;">课表未能完全排完，以下是未排课程信息：</span><br/>';
														for(var i=0; i<wpkcInfo.length; i++){
															if(tempMajor != wpkcInfo[i].专业){
																tempMajor = wpkcInfo[i].专业;
																content += '<br/><b>专业：' + tempMajor +'</b><br/>';
															}
															
															if(tempClass != wpkcInfo[i].班级){
																tempClass = wpkcInfo[i].班级;
																content += '<br/>班级：' + tempClass +'<br/>';
																content += '<span style="width:35px;">课程:</span>';
															}else{
																content += '<span style="width:35px;">&nbsp;</span>';
															}
															
															content += '<span onmouseover="$(this).css(\'color\', \'blue\');" onmouseout="$(this).css(\'color\', \'black\');"><span style="width:150px;">' + wpkcInfo[i].课程名称 + '</span>';
															content += '<span style="width:110px;">课时总数：' + (parseInt(wpkcInfo[i].已排节数, 10)+parseInt(wpkcInfo[i].未排节数, 10)) + '节</span>';
															content += '<span style="width:70px;">已排：' + wpkcInfo[i].已排节数 + '节</span>';
															content += '未排：' + wpkcInfo[i].未排节数 + '节</span><br/>';
														}
														
														$('#wpkcTips').html(content);
													
														//完成部分排课后，显示未排课程信息
														$('#compPartTips').dialog('open');
													}else{
														//完成所有排课后，显示课表
														timetableDetail(tempSemesterName, cpXnxq, cpZydm);
													}
												}, 500);
											}else{
												alertMsg(data.MSG);
												$('#maskDiv').dialog('close');
											}
										}, 500);
									}
								});
							}else{
								alertMsg(data[0].MSG);
								
							}	
						}
					});

				}
			}
		});
	}
	
	/**定时查询进度条进度**/
	function loadProgressBar(){
		enterTimes++;
		var firstEnter = 'yes';
		/*
		if(enterTimes > 1){
			$('#loadProgressBar').attr('src', '<-%=request.getContextPath()%>/form/courseManage/loadProgressBar.jsp?firstEnter=yes');
		}else{
			$('#loadProgressBar').attr('src', '<-%=request.getContextPath()%>/form/courseManage/loadProgressBar.jsp?firstEnter=no');
		}
		*/
		if(enterTimes > 1){
			firstEnter = 'no';
		}
		
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Pksz',
			data : 'active=loadProgressBar&firstEnter=' + firstEnter,
			dataType:"json",
			success : function(data) {
				var progress = data[0].progress;
				var tips = data[0].tips;
				var className = data[0].className;
				var courseName = data[0].courseName;
				$('#progressBar').css('width', parseInt(285*progress/100, 10)+'px');
				$('#percent').html(progress + '%');
				$('#pkTips').html(tips);
				$('#pkClass').html(className);
				$('#pkCourse').html(courseName);
				if(parseInt(progress, 10) > 60){
					$('#percent').css('color', 'white');
				}
			}
		});
	}
	
	/**显示班级课程表列表
		@PK_XNXQMC 学年学期名称
		@PK_XNXQBM 学年学期编码
	**/
	function timetableDetail(PK_XNXQMC, PK_XNXQBM, PK_ZYDM){ 
		var useFlag = false;
		var msgStr = '';
		
		$('#classTimetable').dialog({
			title:(PK_XNXQMC+'班级课程表信息').replace(/ /g, "")
		});
		curSelXnxq = PK_XNXQBM;
		
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Pksz',
			data : 'active=checkPkState',
			dataType:"json",
			success : function(data) { 
				//判断有教师正在排课(暂不用)
				if(data[0].pkState == 'no'){
				//if(data[0].pkState == 'using'){
					useFlag = true;
				}
				loadClassTree(PK_XNXQBM, PK_ZYDM, useFlag);
				//判断有教师正在排课(暂不用)
				if(data[0].pkState == 'no'){
				//if(data[0].pkState == 'using'){
					var time = countTime(data[0].pkTime);
					msgStr = data[0].pkTeaName + '老师正在使用排课功能<br/>已用时间:'+time;
					//msgStr += '<br/>联系方式:'+data[0].pkTeaTel;
					msgStr += '<br/><br/>为保证排课的正确性，您目前只能查看课程表，请稍后再进行排课操作！';
				}else{
					setPkState('using');
				}
				//初始化空白课程表
				initBlankKcb(PK_XNXQBM, PK_ZYDM, useFlag, msgStr);
			}
		});
	}
	
	function loadClassTree(PK_XNXQBM, PK_KSZBBH){
		$('#classTree').tree({
			checkbox: false,
			url:'<%=request.getContextPath()%>/Svl_examSet?active=queryTree&level=0'+'&AUTH='+sAuth+'&parentId='+'&iUSERCODE='+iUSERCODE+'&XNXQ='+PK_XNXQBM,
			onClick:function(node){ 
				//判断点击的是不是班级
		    	if($('#classTree').tree('getParent', node.target) != null){
		    		if(lastIndex!=node.id){
						classId=node.id;
						lastIndex=node.id;
						classflag=true; 	
						//判断修改的课表是否保存过
			    		//if(updateArray.length > 0){
			    		//	ConfirmMsg('修改的课程表还未保存，是否需要保存？', 'doToolbar("save", ' + PK_XNXQBM + ', ' + curSelClass + ')', 'loadClassKcb(' + PK_XNXQBM + ', ' + curSelClass + ');');
			    		//}else{
			    			loadClassKCAP(PK_XNXQBM, PK_KSZBBH, classId);
			    			$('#saveKCAP').linkbutton('enable');
			    		//}
					}
					parentId=node.id;
		    	}else{
		    		classflag=false;
		    	}					
			},
//				onBeforeLoad:function(row,param){     //分层显示treegrid
//					$('#classTree').tree('options').url='< %=request.getContextPath()%>/Svl_Skjh?active=queryTree&AUTH='+sAuth+'&parentId='+'&iUSERCODE='+iUSERCODE+'&GS_XNXQBM='+(xnxqVal+jxxzVal);
//				},
			onBeforeExpand:function(node,param){
		  		$('#classTree').tree('options').url='<%=request.getContextPath()%>/Svl_Skjh?active=queryTree&level=1'+'&AUTH='+sAuth+'&parentId='+node.id+'&iUSERCODE='+iUSERCODE+'&GS_XNXQBM='+PK_XNXQBM;
			},
			onLoadSuccess:function(data){
				$('#classTree').show();	
		    }
		});
	}
	
	/**读取当前班级课程表和未排课程表格
		@PK_XNXQBM 学年学期编码
		@classCode 班级编号
	**/
	function loadClassKCAP(PK_XNXQBM, PK_KSZBBH, classId){
		loadFlag = true;
		updateArray.length = 0;//清空课表更新数组
		document.getElementById('kcbContent').scrollTop = 0;
		
		//清空课程表内容
		$('.kcbTdCentent').removeAttr('title');
		$('.kcbTdCentent').html('&nbsp;');
		$('.kcbTdCentent').css('color', '');
		//清空未排课程表格内容
		$('.wpkcTdCentent').removeAttr('title');
		$('.wpkcTdCentent').html('&nbsp;');
		
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_examSet',
			data : 'active=loadClassKCAP&sAuth=' + sAuth + '&XNXQ=' + PK_XNXQBM + '&KCAPZBBH=' + PK_KSZBBH + '&XZBDM=' + classId,
			dataType:"json",
			success : function(data) {
				classKCB = data[0].kcb;//课程表明细编号,班级排课状态,时间序列,授课计划明细编号,授课教师编号,课程名称,授课教师姓名,实际场地编号,实际场地名称,授课周次,授课周次详情
				//wpkc = data[0].wpkc;//授课计划明细编号,课程名称,授课教师姓名,场地要求,场地名称,授课周次,剩余节数
								
				fillKcb(PK_XNXQBM, classId);//填充当前课程表单元格内的课程信息
				//fillWpkc();//填充未排课程表格
				$('#divPageMask').hide();
				loadFlag = false;
			}
		});
	}
	
	/**初始化空白课程表和未排课程表格
		@PK_XNXQBM 学年学期编码
		@PK_ZYDM 专业代码
		@useFlag 排课功能是否正在被使用判断
		@msgStr 提示信息
	**/
	function initBlankKcb(PK_XNXQBM, PK_ZYDM, useFlag, msgStr){
		//查询当前学期设置的每周天数和每天节数
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Pksz',
			data : 'active=initBlankKCB&PK_XNXQBM=' + PK_XNXQBM + '&PK_ZYDM=' + PK_ZYDM,
			dataType:"json",
			success : function(data) {
				mzts = parseInt(data[0].mzts, 10);//每周天数
				//授课计划未排课程所需单元格数量
				var maxNum = data[0].maxNum;
				if(parseInt(maxNum, 10)%(mzts+1) == 0){
					maxNum = parseInt(parseInt(maxNum, 10)/(mzts+1), 10);
				}else{
					maxNum = parseInt(parseInt(maxNum, 10)/(mzts+1), 10) + 1;
				}
				if(maxNum < 2){
					maxNum = 2;
				}
				$('#kcbMain').layout('panel', 'south').panel('options').height = (maxNum+1)*30 + 'px';
				
				sw = parseInt(data[0].sw, 10);//上午
				zw = parseInt(data[0].zw, 10);//中午
				xw = parseInt(data[0].xw, 10);//下午
				ws = parseInt(data[0].ws, 10);//晚上
				zjs = sw+zw+xw+ws;
				jcsj = data[0].jcsj.split(',');
				curXqzc = data[0].xqzc;//学期总周次
				//单双周周次
				var weekArray = parseWeekArray(curXqzc);
				oddArray = weekArray[0];
				evenArray = weekArray[1];
				
				//生成周次调整对话框内容
				var dialogHeight = 99;
				dialogHeight += parseInt(curXqzc/3, 10)*30;
				if(curXqzc%3 > 0){
					dialogHeight += 30;
				}
				$('#weekNumDialog').dialog({   
					title: '授课周次',   
					width: 400,
					height: dialogHeight,
					modal: true,
					closed: true,   
					cache: false, 
					draggable: true,//是否可移动dialog框设置
					//读取事件
					onLoad:function(data){
					},
					//关闭事件
					onClose:function(data){
						$('.weekNumTd').css('background-color', '#FFFFFF');
						$('.weekNumTd').removeAttr('title');
						$('.weekNumTd').css('cursor', 'pointer');
						$('.allNum').val('0');
					}
				});
				
				var contentStr = '';
				contentStr = '<tr style="background-color:#EFEFEF;"><td style="width:33.3%; height:30px; border-bottom:1px solid #95B8E7;">' + 
						'<input id="week_all" type="button" value="全选" style="cursor:pointer;" onclick="selWeekNum(this.id);"></td>' +
					'<td style="width:33.3%; border-bottom:1px solid #95B8E7;">' +
						'<input id="week_odd" type="button" value="单周" style="cursor:pointer;" onclick="selWeekNum(this.id);"></td>' +
					'<td style="border-bottom:1px solid #95B8E7;">' +
						'<input id="week_even" type="button" value="双周" style="cursor:pointer;" onclick="selWeekNum(this.id);"></td></tr>';
				
				for(var i=1; i<parseInt(curXqzc, 10)+1; i++){
					if(i%3 == 1){
						contentStr += '<tr>';
					}
					
					contentStr += '<td id="week_' + i + '" class="weekNumTd" ' +
						'style="height:30px; border-left:1px solid #95B8E7; border-bottom:1px solid #95B8E7; cursor:pointer;" ' + 
						'>'+i+'<input type="hidden" id="weekNumFlag_' + i + '" name="'+i+'" class="allNum" value="0"></td>'; //0未选中：1选中：2不可选
					
					if(i%3 == 0){
						contentStr += '</tr>';
					}
				}
				if(curXqzc%3 != 0){
					for(var i=0; i<3-curXqzc%3; i++){
						contentStr += '<td style="width:130px; height:30px; border-left:1px solid #95B8E7; border-bottom:1px solid #95B8E7;">&nbsp;</td>';
					}
					contentStr += '</tr>';
				}
				$('#weekList').html(contentStr);
				
				$('.weekNumTd').bind('mouseover', function(){
					var curNum = $(this).attr('id').substring(5);
					if($('#weekNumFlag_' + curNum).val() == '2'){
						return;
					}
				
					enterColor = $(this).css('background-color');
					$(this).css('background-color', '#FFE48D');
				}).bind('mouseout', function(){
					var curNum = $(this).attr('id').substring(5);
					if($('#weekNumFlag_' + curNum).val() == '2'){
						return;
					}
				
					$(this).css('background-color', enterColor);
					//阻止冒泡事件
					window.event? window.event.cancelBubble = true : e.stopPropagation();
				}).bind('click', function(){
					var curNum = $(this).attr('id').substring(5);
					if($('#weekNumFlag_' + curNum).val() == '2'){
						return;
					}
					
					if($('#weekNumFlag_' + curNum).val() == '0'){
						enterColor = '#E7FECB';
						$('#weekNumFlag_' + curNum).val('1');
					}else{
						enterColor = '#FFFFFF';
						$('#weekNumFlag_' + curNum).val('0');
					}
				});
				
				//生成全部时间序列数组
				for(var i=1; i<mzts+1; i++){
					for(var j=1; j<zjs+1; j++){
						timeOrderArray.push((i<10?'0'+i:''+i) + (j<10?'0'+j:''+j));
						timeOrderArray.push('');
					}
				}
				
				var tdWidth = 100/(mzts+1);
				var tempNum = 0;
				var kcbContent = '<table style="width:100%; height:100%; border-top:1px solid #95B8E7; border-right:1px solid #95B8E7;" cellspacing="0" cellpadding="0">';
				var wpkcContet = '<table style="width:100%; height:100%; border-top:1px solid #95B8E7; border-right:1px solid #95B8E7;" cellspacing="0" cellpadding="0">';
				
				for(var i=-1; i<zjs; i++){
					kcbContent += '<tr class="kcbTrStyle" onmouseout="window.event? window.event.cancelBubble = true : e.stopPropagation();"';
					
					//空白未排课程表格
					if((i+1) <= maxNum){
						wpkcContet += '<tr class="wpkcTrStyle" onmouseout="window.event? window.event.cancelBubble = true : e.stopPropagation();">';
					}
					
					if(i == -1){
						kcbContent += ' style="height:45px;"';	
					}
					kcbContent += '>';
					
					for(var j=-1; j<mzts; j++){
						//空白未排课程表格
						if((i+2) <= maxNum){
							wpkcContet += '<td id="wpkc_' + tempNum + '" class="wpkcTdStyle wpkcTdCentent" onmouseover="tdMouseOver(this.id);" onmouseout="tdMouseOut(this.id);" onmousedown="selectKcbCourse(this.id, \'wpkc\');">&nbsp;</td>';
						}
						tempNum++;
						
						//添加左上角空单元格
						if(i==-1 && j==-1){
							kcbContent += '<td colspan="3" class="kcbTdStyle titleBG splitTd" onmouseout="window.event? window.event.cancelBubble = true : e.stopPropagation();">&nbsp;</td>';
							continue;
						}
						
						//判断添加星期数
						if(i==-1 && j>-1){
							//判断是小周还是大周版
							if(mzts < 8){
								kcbContent += '<td class="kcbTdStyle kcbTitle titleBG splitTd" onmouseout="window.event? window.event.cancelBubble = true : e.stopPropagation();">' + weekNameArray[j] + '</td>';	
							}else{
								kcbContent += '<td class="kcbTdStyle kcbTitle titleBG splitTd" onmouseout="window.event? window.event.cancelBubble = true : e.stopPropagation();">大周' + (j+1) + '</td>';
							}
							continue;
						}
						
						//判断添加节次数
						if(i>-1 && j==-1){
							//上午课程
							if(i<sw &&　sw>0){
								//判断第一行
								if(i == 0){
									kcbContent += '<td id="width_sw" rowspan="'+sw+'" class="titleTdStyle kcbTitle titleBG';
									//判断是否还有课
									if(i+sw < zjs){
										kcbContent += ' splitTd';
									}
									kcbContent += '">上午</td>';
								}
								
								kcbContent += '<td id="width_xh" class="titleTdStyle titleBG';
								//判断是否还有课
								if((i+1)==sw && (i+1)<zjs){
									kcbContent += ' splitTd';
								}
								kcbContent += '">' + (i+1) + '</td>';
								
								kcbContent += '<td id="width_sjd" class="titleTdStyle titleBG';
								//判断是否还有课
								if((i+1)==sw && (i+1)<zjs){
									kcbContent += ' splitTd';
								}
								kcbContent += '">';
								if(jcsj.length > i){
									kcbContent += jcsj[i];
								}else{
									kcbContent += '&nbsp;';
								}
								kcbContent += '</td>';
							}
							
							//中午课程
							if(i>=sw && i<(sw+zw) && zw>0){
								//判断第一行
								if(i == sw){
									kcbContent += '<td rowspan="'+zw+'" class="titleTdStyle kcbTitle titleBG';
									//判断是否还有课
									if(i+zw < zjs){
										kcbContent += ' splitTd';
									}
									kcbContent += '">中午</td>';
								}
								
								kcbContent += '<td class="titleTdStyle titleBG';
								//判断是否还有课
								if((i+1)==(sw+zw) && (i+1)<zjs){
									kcbContent += ' splitTd';
								}
								kcbContent += '">' + (i+1) + '</td>';
								kcbContent += '<td class="titleTdStyle titleBG';
								//判断是否还有课
								if((i+1)==(sw+zw) && (i+1)<zjs){
									kcbContent += ' splitTd';
								}
								kcbContent += '">';
								if(jcsj.length > i){
									kcbContent += jcsj[i];
								}else{
									kcbContent += '&nbsp;';
								}
								kcbContent += '</td>';
							}
							
							//下午课程
							if(i>=(sw+zw) && i<(sw+zw+xw) && xw>0){
								//判断第一行
								if(i == sw+zw){
									kcbContent += '<td rowspan="'+xw+'" class="titleTdStyle kcbTitle titleBG';
									//判断是否还有课
									if(i+xw < zjs){
										kcbContent += ' splitTd';
									}
									kcbContent += '">下午</td>';
								}
								
								kcbContent += '<td class="titleTdStyle titleBG';
								//判断是否还有课
								if((i+1)==(sw+zw+xw) && (i+1)<zjs){
									kcbContent += ' splitTd';
								}
								kcbContent += '">' + (i+1) + '</td>';
								kcbContent += '<td class="titleTdStyle titleBG';
								//判断是否还有课
								if((i+1)==(sw+zw+xw) && (i+1)<zjs){
									kcbContent += ' splitTd';
								}
								kcbContent += '">';
								if(jcsj.length > i){
									kcbContent += jcsj[i];
								}else{
									kcbContent += '&nbsp;';
								}
								kcbContent += '</td>';
							}
							
							//晚上课程
							if(i>=(sw+zw+xw) && i<(sw+zw+xw+ws) && ws>0){
								//判断第一行
								if(i == (sw+zw+xw)){
									kcbContent += '<td rowspan="'+ws+'" class="titleTdStyle titleBG kcbTitle">晚上</td>';
								}
								kcbContent += '<td class="titleTdStyle titleBG">' + (i+1) + '</td>'
											+ '<td class="titleTdStyle titleBG">';
								if(jcsj.length > i){
									kcbContent += jcsj[i];
								}else{
									kcbContent += '&nbsp;';
								}
								kcbContent += '</td>';
							}
							
							continue;
						}
						
						//拼接时间序列
						if((j+1) < 10){
							tempOrder = '0'+(j+1);
						}else{
							tempOrder = j+1;
						}
						if((i+1) < 10){
							tempOrder += '0'+(i+1);
						}else{
							tempOrder += i+1;
						}
						
						//添加普通单元格
						kcbContent += '<td id="kcb_' + tempOrder + '" class="kcbTdStyle kcbTdCentent';
						if(((i+1)==sw && zw>0) || ((i+1)==(sw+zw) && xw>0) || ((i+1)==(sw+zw+xw) && ws>0)){
							kcbContent += ' splitTd';
						}
						kcbContent += '" onmouseover="tdMouseOver(this.id);" onmouseout="tdMouseOut(this.id);" ' + 
									'onmousedown="selectKcbCourse(this.id, \'kcb\');" ondblclick="changeCourseInfo(this.id);">&nbsp;</td>';
					}
					kcbContent += '</tr>';
					wpkcContet += '</tr>';
				}
				
				kcbContent += '</table>';
				wpkcContet += '</table>';
				//var mask = '<div id="timetableMask" style="position:absolute; top:0; z-index:99999; width:100%; height:100%; background:url(0);"></div>';
				$('#kcbContent').html(kcbContent);
				//mask = '<div id="wpkcMask" style="position:absolute; top:0; z-index:99999; width:100%; height:100%; background:url(0);"></div>';
				$('#wpkcContent').html(wpkcContet);
				
				$('#classTimetable').dialog('open');
				$('.kcbTdStyle').css('width', tdWidth+'%');
				$('.wpkcTdStyle').css('width', tdWidth+'%');
				$('#width_sw').css('width', '50px');
				$('#width_xh').css('width', '20px');
				$('#width_sjd').css('width', '130px');
				$('.wpkcTdStyle').css('height', '30px');
				$('.kcbTrStyle').css('height', parseInt($('#kcbContent').css('height').substring(0, $('#kcbContent').css('height').length-2)-1, 10)/(sw+xw+ws+1)+'px');
				$('.wpkcTrStyle').css('height', 100/sw+'%');
				
				//当前课程表是否已过了排课截止时间
				sfgqFlag = data[0].sfgq=='1'?true:false;
				
				//判断排课功能是否正在被使用
				if(useFlag == true){
					alertMsg(msgStr);
				}
			}
		});
	}
	
	/**课程表及未排课程单元格鼠标移入事件
		@id 当前td的id
	**/
	function tdMouseOver(tdID){
		//判断当前课程表是否已过了排课截止时间
		if(curSelClass=='' || (sAuth.indexOf(admin)<0 && sfgqFlag)){
			return;
		}
		curTargetId = tdID;
		enterColor = $('#' + tdID).css('background-color');
		$('#' + tdID).css('background-color', '#FFE48D');
	}
	
	/**课程表及未排课程单元格鼠标移出事件
		@tdID 单元格id
	**/
	function tdMouseOut(tdID){
		//判断当前课程表是否已过了排课截止时间
		if(curSelClass=='' || (sAuth.indexOf(admin)<0 && sfgqFlag)){
			return;
		}
		curTargetId = '';
		//判断当前不是选中课程状态  或  选中课程状态下当前移出的单元格不是选择的单元格
		if(selectArea=='' || tdID!=curSelectId){
			$('#' + tdID).css('background-color', enterColor);
		}
		//阻止冒泡事件
		window.event? window.event.cancelBubble = true : e.stopPropagation();
	}
	
	/**读取当前班级课程表和未排课程表格
		@PK_XNXQBM 学年学期编码
		@classCode 班级编号
	**/
	function loadClassKcb(PK_XNXQBM, classCode){
		$('#timetableMask').show();//课表遮罩层
		updateArray.length = 0;//清空课表更新数组
		document.getElementById('kcbContent').scrollTop = 0;
		
		//清空课程表内容
		$('.kcbTdCentent').removeAttr('title');
		$('.kcbTdCentent').html('&nbsp;');
		$('.kcbTdCentent').css('color', '');
		//清空未排课程表格内容
		$('.wpkcTdCentent').removeAttr('title');
		$('.wpkcTdCentent').html('&nbsp;');
		
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Pksz',
			data : 'active=loadClassKcb&sAuth=' + sAuth + '&PK_XNXQBM=' + PK_XNXQBM + '&PK_XZBDM=' + classCode,
			dataType:"json",
			success : function(data) {
				classKCB = data[0].kcb;//课程表明细编号,班级排课状态,时间序列,授课计划明细编号,授课教师编号,课程名称,授课教师姓名,实际场地编号,实际场地名称,授课周次,授课周次详情
				wpkc = data[0].wpkc;//授课计划明细编号,课程名称,授课教师姓名,场地要求,场地名称,授课周次,剩余节数
				kcbBZ = data[0].bz;//课程表备注
				stuNum = data[0].stuNum;//班级人数
				tkOrder = data[0].tkOrder;//已经进行过调课操作的时间序列
				
				fillKcb(PK_XNXQBM, classCode);//填充当前课程表单元格内的课程信息
				fillWpkc();//填充未排课程表格
				$('#divPageMask').hide();
			}
		});
	}
	
	/**查询其他信息（教师/教室已用时间信息,添加课程信息）
		@active
		@PK_XNXQBM 学年学期编码
		@classCode 行政班代码
	**/
	function queOtherInfo(active, PK_XNXQBM, classCode){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Pksz',
			data : 'active=' + active + '&PK_XNXQBM=' + PK_XNXQBM + '&PK_XZBDM=' + classCode,
			async: false,
			dataType:"json",
			success : function(data) {
				if(active == 'queAddCourseInfo'){
					//清空课表中原来数据
					if(addCourseInfo != ''){
						for(var i=0; i<addCourseInfo.length; i++){
							$('#kcb_' + addCourseInfo[i].timeOrder).html('&nbsp;');
						}
					}
					addCourseInfo = data[0].addCourseInfo;
					for(var i=0; i<addCourseInfo.length; i++){
						var tempTitle = addCourseInfo[i].type + '\n\n' + addCourseInfo[i].course + '\n' + addCourseInfo[i].tea + '\n' + addCourseInfo[i].site;
						$('#kcb_' + addCourseInfo[i].timeOrder).html(addCourseInfo[i].course + '</br>' + addCourseInfo[i].tea + '<input type="hidden" id="changeFlag_' + addCourseInfo[i].timeOrder + '" value="3"/>');
						$('#kcb_' + addCourseInfo[i].timeOrder).attr('title', tempTitle);
					}
				}
				teaUsedOrderInfo = parseUsedInfo(data[0].teaUsedOrder);
				siteUsedOrderInfo = parseUsedInfo(data[0].siteUsedOrder);
				
				var tempHbInfo = data[0].hbSet;
				hbSetInfo.length = 0;
				for(var i=0; i<tempHbInfo.length; i++){
					hbSetInfo.push(tempHbInfo[i].合班授课编号.split('+'));
					hbSetInfo.push(tempHbInfo[i].合班班级名称.split('+'));
					hbSetInfo.push(tempHbInfo[i].总人数);
				}
				tempHbInfo = data[0].hbInfo;
				hbCourseInfo.length = 0;
				for(var i=0; i<tempHbInfo.length; i++){
					hbCourseInfo.push(tempHbInfo[i].时间序列);
					hbCourseInfo.push(tempHbInfo[i].合班授课计划编号.split('+'));
					hbCourseInfo.push(tempHbInfo[i].合班课程名称);
					hbCourseInfo.push(tempHbInfo[i].合班班级名称);
					hbCourseInfo.push(tempHbInfo[i].实际场地编号);
					hbCourseInfo.push(tempHbInfo[i].实际场地名称);
				}
				
				//为合班课程添加提示信息
				var tempTitle = '';
				var tempSkjhbhArray = '';
				var tempHbCodeArray = '';
				var tempHbSet = '';
				for(var i=0; i<hbCourseInfo.length; i+=6){
					//判断单元格是否有内容
					if($('#changeFlag_' + hbCourseInfo[i]).val()==0 || $('#changeFlag_' + hbCourseInfo[i]).val()==1){
						tempSkjhbhArray = $('#skjhmxbh_' + hbCourseInfo[i]).val().split('｜');
						tempHbCodeArray = hbCourseInfo[i+1];
						
						//判断当前单元格内课程是否为合班课程
						for(var j=0; j<tempSkjhbhArray.length; j++){
							for(var k=0; k<hbSetInfo.length; k+=3){
								tempHbSet = hbSetInfo[k].toString();
								if(tempHbSet.indexOf(tempSkjhbhArray[j])>-1 && tempHbSet.indexOf(tempHbCodeArray[0])>-1){
									tempTitle = $('#kcb_' + hbCourseInfo[i]).attr('title');
									if(tempTitle.indexOf('固排课程')<0 && tempTitle.indexOf('\n\n')>-1){
										tempTitle += '\n';
									}else{
										tempTitle += '\n\n';
									}
									$('#kcb_' + hbCourseInfo[i]).attr('title', tempTitle + '该节'+hbCourseInfo[i+2]+'为合班课程（'+hbCourseInfo[i+3]+'）');
								}
							}
						}
					}
				}
				$('#timetableMask').hide();
			}
		});	
	}
	
	/**填充当前课程表单元格内的课程信息
		@PK_XNXQBM 学年续期编码
		@classCode 班级编号
	**/
	function fillKcb(PK_XNXQBM, classCode){
		var tempContent = '';
		var tempTitle = '';
		
		for(var i=0; i<classKCB.length; i++){
			var tempContent = '';
			var tempTitle = '';
			//判断节数是否大于0
			if(classKCB[i].节数 > 0){
				tempTitle = '';
				tempContent = classKCB[i].课程名称 +'<br /><br />'+ classKCB[i].监考教师姓名;
				tempTitle = classKCB[i].课程名称 +'\n'+ classKCB[i].监考教师姓名;
				
				$('#kcb_' + classKCB[i].时间序列).attr('title', tempTitle);
				tempContent += '<input type="hidden" id="kcdm_'+classKCB[i].时间序列+'" value="'+classKCB[i].课程代码+'"/>';//课程代码 
				$('#kcb_' + classKCB[i].时间序列).html(tempContent);
			}
		}
	}
	
	/**填充未排课程表格**/
	function fillWpkc(){
		var tempContent = '';
		var tempTitle = '';
		var num = 0;
		
		for(var i=0; i<wpkc.length; i++){
			//判断剩余节数是否大于0
			if(wpkc[i].剩余节数 > 0){
				tempContent = wpkc[i].课程名称 + '<br/>' + wpkc[i].授课教师姓名;
				tempTitle = wpkc[i].课程名称 + '\n' + wpkc[i].授课教师姓名;
				tempTitle += '\n' + parseWeekShow(wpkc[i].授课周次详情, oddArray, evenArray);//修改授课周次显示内容
				tempTitle += '\n剩余节数:' + wpkc[i].剩余节数;
				tempContent += '<input type="hidden" id="wpkcjsbh_'+num+'" value="'+wpkc[i].授课教师编号+'"/>';
				tempContent += '<input type="hidden" id="wpkccdbh_'+num+'" value="'+wpkc[i].场地要求+'"/>';
				tempContent += '<input type="hidden" id="wpkccdmc_'+num+'" value="'+wpkc[i].场地名称+'"/>';
				tempContent += '<input type="hidden" id="wpkczc_'+num+'" value="'+wpkc[i].授课周次+'"/>';
				tempContent += '<input type="hidden" id="wpkczcxq_'+num+'" value="'+wpkc[i].授课周次详情+'"/>';
				$('#wpkc_' + num).attr('name', wpkc[i].授课计划明细编号);
				$('#wpkc_' + num).attr('title', tempTitle);
				$('#wpkc_' + num).html(tempContent);
				num++;
			}
		}
	}
	
	/**选中当前的课程表课程单元格
		@tdID 单元格id
		@area 选中的单元格所在的区域
	**/
	function selectKcbCourse(tdID, area){
				
		var e = window.event || arguments.callee.caller.arguments[0];
		//判断是不是左键点击
		if(e.button!=2 && e.button!=3){
			if(area == 'kcb'){
				
			}
		
			var changeFlag = $('#changeFlag_' + tdID.substring(tdID.indexOf('_')+1,tdID.length)).val();//用于判断可否手动调整(0不可/1可/2空白/3添加课程)
			//判断在课表区域选择的单元格是否可以手动调整
			if($('#'+tdID).html()=='&nbsp;' || (area=='kcb' && (changeFlag==undefined || changeFlag=='0' || changeFlag=='2' || changeFlag=='3'))){
				//管理员可调整固排课程
				if(sAuth.indexOf(admin)<0 || (sAuth.indexOf(admin)>-1 && changeFlag!='0')){
					return;
				}
			}
			
			$('#' + tdID).css('background-color', '#FFE48D');
			selectArea = area;
			curSelectId = tdID;
		
			//检查并标识课程在课表中可排和不可排的位置
			//checkKcbArea(area);
				
			var mouseX = e.clientX;
			var mouseY = e.clientY;
			$('#selectCourse').css('left', mouseX+5);
			$('#selectCourse').css('top', mouseY+5);
			
			var curDivWidth = $('#' + tdID).css('width');
			var curDivHeight = $('#' + tdID).css('height');
			$('#selectCourse').css('width', curDivWidth);
			$('#selectCourse').css('height', curDivHeight);
			$('#selectContent').html($('#' + tdID).html());
			$('#selectCourse').show();
	
			bindBodyMouseEvent();		
		}
	}
	
	/**检查并标识课程在课表中可排和不可排的位置
		@area 选择的区域
	**/
	function checkKcbArea(area){
		var order = curSelectId.substring(curSelectId.indexOf('_')+1);
		var selSkjhbhArray = '';//授课计划编号
		var selTeaCodeArray = '';//选择的教师编号
		var tagSkjhbhArray = '';
		var tagTeaCodeArray = '';
		var selSiteCodeArray = '';//场地编号
		var tagSiteCodeArray = '';
		var selKczcArray = '';//课程周次
		var tagKczcArray = '';
		var tempOrder = '';
		var tempHbInfoArray = new Array();
		var tagHbSetArray = new Array();//当前目标单元格课程的合班信息
		var selHbFlag = false;
		titleArray.length = 0;
		
		if(area == 'kcb'){
			selSkjhbhArray = parseInfoToArray($('#skjhmxbh_' + order).val());
			selTeaCodeArray = parseInfoToArray($('#skjsbh_' + order).val());
			selSiteCodeArray = parseInfoToArray($('#cdbh_' + order).val());
			selKczcArray = parseInfoToArray($('#kczcxq_' + order).val());
		}else{
			selSkjhbhArray = parseInfoToArray($('#wpkc_' + order).attr('name'));
			selTeaCodeArray = parseInfoToArray($('#wpkcjsbh_' + order).val());
			selSiteCodeArray = parseInfoToArray($('#wpkccdbh_' + order).val());
			selKczcArray = parseInfoToArray($('#wpkczcxq_' + order).val());
		}
		
		//检查选中单元格的课程是否有合班设置
		selHbSetArray = checkHbSetInfo(selSkjhbhArray);
		
		//遍历所有单元格
		for(var i=1; i<mzts+1; i++){
			for(var j=1; j<zjs+1; j++){
				tempHbInfoArray.length = 0;
				tempOrder = (i<10?'0'+i:''+i) + (j<10?'0'+j:''+j);
				selHbFlag = false;
				
				//判断如果是当前选中的单元格不做操作
				if(tempOrder == order){
					continue;
				}
				
				$('#kcb_' + tempOrder).css('background-color', '#E7FECB');//更改可调整课程背景色
				
				//判断是否为固排和禁排
				if(sAuth.indexOf(admin)<0 && $('#changeFlag_' + tempOrder).val()=='0'){
					$('#kcb_' + tempOrder).css('background-color', '#FFFFFF');//更改可调整课程背景色
					continue;
				}
				
				//判断是否为添加课程
				if($('#changeFlag_' + tempOrder).val() == '3'){
					//保留单元格原title信息
					keepTitleInfo(tempOrder);
					$('#changeFlag_' + tempOrder).val('0');
					$('#kcb_' + tempOrder).attr('title', '添加课程不可调整');
					$('#kcb_' + tempOrder).css('background-color', '#FFFFFF');//更改可调整课程背景色
					continue;
				}
				
				//判断在课程表中的课程内容完全相同，不需要更换
				//if((selectArea=='kcb' && $('#skjsbh_' + order).val()==$('#skjsbh_' + tempOrder).val() && $('#cdbh_' + order).val()==$('#cdbh_' + tempOrder).val() && $('#kczcxq_' + order).val()==$('#kczcxq_' + tempOrder).val()) 
					//|| (selectArea=='wpkc' && $('#wpkcjsbh_' + order).val()==$('#skjsbh_' + tempOrder).val() && $('#wpkczcxq_' + order).val()==$('#kczcxq_' + tempOrder).val())){
				if((selectArea=='kcb' && $('#skjhmxbh_' + order).val()==$('#skjhmxbh_' + tempOrder).val() && $('#cdbh_' + order).val()==$('#cdbh_' + tempOrder).val()) 
					|| (selectArea=='wpkc' && $('#wpkc_' + order).attr('name')==$('#skjhmxbh_' + tempOrder).val())){
					//保留单元格原title信息
					keepTitleInfo(tempOrder);
					$('#changeFlag_' + tempOrder).val('0');
					$('#kcb_' + tempOrder).attr('title', '相同课程无需调整');
					$('#kcb_' + tempOrder).css('background-color', '#FFFFFF');//更改可调整课程背景色
					continue;
				}
				
				//判断选中单元格的课程是否有合班设置
				if(selHbSetArray.length > 0){
					tempHbInfoArray = judgeHbCourse(tempOrder, selHbSetArray);
					if(tempHbInfoArray[0].length > 0){
						//保留单元格原title信息
						keepTitleInfo(tempOrder);
						$('#kcb_' + tempOrder).attr('title', tempHbInfoArray[2]);
						$('#kcb_' + tempOrder).css('background-color', '#BFEFFF');//更改可合班课程背景色
						selHbFlag = true;
					}
				}
				
				//判断选中单元格的课程授课教师的授课时间和周次是否可用
				if(judgeOrder('tea', 'sel', order, tempOrder, selSkjhbhArray, selTeaCodeArray, selKczcArray, tempHbInfoArray, selHbFlag) == true){
					continue;
				}
				
				//判断选择单元格区域
				//如果是选择的课程表区域，需要判断选中的课程教室和目标位置的教师和教室
				if(selectArea == 'kcb'){
					//判断选中的课程所用教室的授课时间和周次是否可用
					if(judgeOrder('site', 'sel', order, tempOrder, selSkjhbhArray, selSiteCodeArray, selKczcArray, tempHbInfoArray, selHbFlag) == true){
						continue;
					}
					
					//判断当前遍历的时间序列是否有课程,有课程的话获取课程信息
					if($('#changeFlag_' + tempOrder).val()=='1' || (sAuth.indexOf(admin)>-1&&$('#changeFlag_' + tempOrder).val()=='0')){
						tagSkjhbhArray = parseInfoToArray($('#skjhmxbh_' + tempOrder).val());//授课计划明细编号
						tagTeaCodeArray = parseInfoToArray($('#skjsbh_' + tempOrder).val());//教师编号
						tagSiteCodeArray = parseInfoToArray($('#cdbh_' + tempOrder).val());//场地编号
						tagKczcArray = parseInfoToArray($('#kczcxq_' + tempOrder).val());//课程周次
						
						//检查目标单元格的课程是否有合班设置
						tagHbSetArray = checkHbSetInfo(tagSkjhbhArray);
						//判断目标单元格的课程是否为可合班课程
						if(tagHbSetArray.length > 0){
							tempHbInfoArray = judgeHbCourse(order, tagHbSetArray);
							//判断选择的单元格与目标单元格是否为合班课程（优先提示），如果不是，需判断目标单元格到选择单元格是否为合班课程
							if(tempHbInfoArray[0].length>0 && selHbFlag==false){
								//保留单元格原title信息
								keepTitleInfo(tempOrder);
								
								var tempTitle = '';
								//判断是小周还是大周版
								if(mzts < 8){
									tempTitle = weekNameArray[parseInt(order.substring(0, 2), 10)-1];	
								}else{
									tempTitle = '大周' + parseInt(order.substring(0, 2), 10);
								}
								tempTitle += ' 第' + parseInt(order.substring(2), 10) + '节 ';
								$('#kcb_' + tempOrder).attr('title', tempTitle+tempHbInfoArray[2]);
								$('#kcb_' + tempOrder).css('background-color', '#BFEFFF');//更改可合班课程背景色
							}
						}
						
						//判断目标单元格的课程授课教师的授课时间和周次
						if(judgeOrder('tea', 'tag', order, tempOrder, tagSkjhbhArray, tagTeaCodeArray, tagKczcArray, tempHbInfoArray, selHbFlag) == true){
							continue;
						}
						
						//判断目标单元格的课程使用教室的授课时间和周次
						if(judgeOrder('site', 'tag', order, tempOrder, tagSkjhbhArray, tagSiteCodeArray, tagKczcArray, tempHbInfoArray, selHbFlag) == true){
							continue;
						}
					}
				}else{
					//选择的是未排课程区域，如果有场地要求，判断选中的课程所用教室的授课时间和周次是否可用
					if(selSiteCodeArray.length > 0){
						if(judgeOrder('site', 'sel', order, tempOrder, selSkjhbhArray, selSiteCodeArray, selKczcArray, tempHbInfoArray, selHbFlag) == true){
							continue;
						}
					}
				}
			}
		}
	}
	
	/**保留单元格原title信息
		@order 目标单元格的时间序列
	**/
	function keepTitleInfo(order){
		//判断数组中是否已有当前单元格title信息（只保存title原信息，不保存提示信息）
		for(var i=0; i<titleArray.length; i+=3){
			if(titleArray[i+2] == order){
				return;				
			}
		}
		titleArray.push(order);
		titleArray.push($('#kcb_' + order).attr('title')==undefined?'':$('#kcb_' + order).attr('title'));
		titleArray.push($('#changeFlag_' + order).val());
	}
	
	/**检查单元格的课程是否有合班设置
		@skjhbhArray 单元格中内容的授课计划明细编号
		@return 当前单元格合班设置信息
	**/
	function checkHbSetInfo(skjhbhArray){
		var resultHbArray = new Array();
		var setSkjhbhArray = new Array();
		var hbSetFlag = false;//判断是否设置了合班
		var hbCodeArray = new Array();
		
		//遍历当前单元格所有授课计划
		for(var i=0; i<skjhbhArray.length; i++){
			hbSetFlag = false;
			
			for(var j=0; j<hbSetInfo.length; j+=3){
				hbCodeArray = new Array();
				setSkjhbhArray = hbSetInfo[j];
				
				for(var k=0; k<setSkjhbhArray.length; k++){
					if(skjhbhArray[i] == setSkjhbhArray[k]){
						hbSetFlag = true;
					}else{
						hbCodeArray.push(setSkjhbhArray[k]);
					}
				}
				
				if(hbSetFlag == true){
					resultHbArray.push(hbCodeArray);
					resultHbArray.push(hbSetInfo[j+2]);//总人数
					break;
				}
			}
		}
		
		return resultHbArray;
	}
	
	/**判断课程是否可合班
		@order 目标单元格的时间序列
		@hbSetArray 合班设置信息
		@return 与当前课程合班的课程信息
	**/
	function judgeHbCourse(order, hbSetArray){
		var resultArray = new Array();
		var codes = '';
		var tempCodes = '';
		var resultCodes = '';
		var tips = '';
		var resultTips = '';
		var tempSkjhbhArray = new Array();
		var hbCodeArray = new Array();
		
		for(var i=0; i<hbSetArray.length; i+=2){
			hbCodeArray = hbSetArray[i];
			tempCodes = '';
			tips = '';
			
			for(var j=0; j<hbCodeArray.length; j++){
				for(var a=0; a<hbCourseInfo.length; a+=6){
					//判断时间序列是否相同
					if(hbCourseInfo[a] == order){
						tempSkjhbhArray = hbCourseInfo[a+1];
						
						for(var b=0; b<tempSkjhbhArray.length; b++){
							if(hbCodeArray[j] == tempSkjhbhArray[b]){
								codes += tempSkjhbhArray[b]+',';
								tempCodes += tempSkjhbhArray[b]+',';
								tips = hbCourseInfo[a+2]+'可与'+hbCourseInfo[a+3]+'合班上课\n';
							}
						}
					}
				}
			}
			tempCodes = tempCodes.substring(0, tempCodes.length-1);
			if(tempCodes.length > 0){
				resultCodes += tempCodes+'+';
			}
			resultTips += tips;
		}
		
		codes = codes.substring(0, codes.length-1);
		resultCodes = resultCodes.substring(0, resultCodes.length-1);
		resultTips = resultTips.substring(0, resultTips.length-1);
		resultArray.push(resultCodes);
		resultArray.push(codes);
		resultArray.push(resultTips);
		return resultArray;
	}
	
	/**判断选中单元格课程（授课教师，场地）是否允许被安排在某一时间序列（不判断合班课程）
		@judgeType 判断的编号类型
		@type 判断是选择的单元格还是目标单元格
		@selOrder 选中单元格的时间序列
		@tagOrder 目标单元格的时间序列
		@skjhbhArray 当前选择的授课计划明细编号
		@codeArray 当前选择的课程教师/教室编号
		@kczcArray 课程周次
		@HbInfoArray 合班的信息
		@selHbFlag 用于判断是否合班
	**/
	function judgeOrder(judgeType, type, selOrder, tagOrder, skjhbhArray, codeArray, kczcArray, hbInfoArray, selHbFlag){
		var allUsedOrderInfo = '';//所有已用信息
		var selKczc = '';//当前选中的课程授课周次
		var tempUsedOrderArray = new Array();
		var usedCourseInfo = '';
		var usedKczc = '';
		var timeOrder = '';
		var tempCodeArray = '';
		
		var hbCodeArray = new Array();
		//判断是否有合班信息
		if(hbInfoArray.length > 0){
			hbCodeArray = hbInfoArray[1].split(',');
		}
		
		//判断类型使用不同参数
		if(judgeType == 'tea'){
			allUsedOrderInfo = teaUsedOrderInfo;
		}else if(judgeType == 'site'){
			allUsedOrderInfo = siteUsedOrderInfo;
		}
		
		if(type == 'sel'){
			timeOrder = tagOrder;
		}else{
			timeOrder = selOrder;
		}
		
		//获取该时间序列的授课周次在数组中的位置
		var indexNum = ((parseInt(timeOrder.substring(0, 2), 10)-1)*zjs*2 + parseInt(timeOrder.substring(2), 10)*2) - 1;
		var flag = false;
		var specialCodeArray = '<%=MyTools.getProp(request, "Base.specialCode")%>'.split(",");//教室特殊类型代码
		var specialFlag = false;
		
		//遍历所有需要检查的教师/教室编号
		for(var i=0; i<codeArray.length; i++){
			tempCodeArray = codeArray[i].split('+');
			
			for(var j=0; j<tempCodeArray.length; j++){
				//遍历所有的已用时间序列信息
				for(var k=0; k<allUsedOrderInfo.length; k+=5){
					//判断是否当前教师/教室
					if(tempCodeArray[j] == allUsedOrderInfo[k]){
						//判断如果是特殊类型（允许多个班级同时使用）的教室不需要判断是否可用
						specialFlag = false;
						for(var z=0; z<specialCodeArray.length; z++){
							if(specialCodeArray[z] == allUsedOrderInfo[k+2]){
								specialFlag = true;
								break;
							}
						}
						
						if(specialFlag == false){
							selKczc = parseSkzc(kczcArray[i]);
							tempUsedOrderArray = allUsedOrderInfo[k+4];//当前教师/教室的课程安排情况
							usedCourseInfo = tempUsedOrderArray[indexNum];//当前需要判断的时间序列已用信息
							
							//遍历当前时间序列的已用信息
							for(var a=0; a<usedCourseInfo.length; a+=5){
								//如果是合班课程的话不判断授课信息冲突情况
								for(var m=0; m<hbCodeArray.length; m++){
									if(hbCodeArray[m] == usedCourseInfo[a]){
										flag = true;
										break;
									}
								}
								
								if(flag == true){
									flag = false;
									continue;
								}
								
								usedKczc = usedCourseInfo[a+4];
								//对比授课周次是否冲突
								for(var m=0; m<usedKczc.length; m++){
									for(var n=0; n<selKczc.length; n++){
										if(usedKczc[m] == selKczc[n]){
											var tempTitle = '';
											if(type == 'tag'){
												//判断是小周还是大周版
												if(mzts < 8){
													tempTitle += weekNameArray[parseInt(timeOrder.substring(0, 2), 10)-1];	
												}else{
													tempTitle += '大周' + parseInt(timeOrder.substring(0, 2), 10);
												}
												tempTitle += ' 第' + parseInt(timeOrder.substring(2), 10) + '节 ';
											}else{
												tempTitle += '此位置 ';
											}
											tempTitle += usedCourseInfo[a+1];
											if(judgeType == 'tea'){
												tempTitle += '老师 已安排为';
											}else if(judgeType == 'site'){
												tempTitle += ' 已安排';
											}
											tempTitle += usedCourseInfo[a+3] + '上' + usedCourseInfo[a+2] + '课';
											
											//如果检查的是目标单元格，需要修改改变显示title的单元格编号
											if(type == 'tag'){
												timeOrder = tagOrder;
											}
											//判断如果是合班课程，说明已保存title课程信息
											if(selHbFlag == false){
												//保留单元格原title信息
												keepTitleInfo(timeOrder);
											}
											$('#changeFlag_' + timeOrder).val('0');
											$('#kcb_' + timeOrder).attr('title', tempTitle);
											$('#kcb_' + timeOrder).css('background-color', '#FFFFFF');//更改可调整课程背景色
											return true;
										}
									}
								}
							}
						}
					}
				}
			}
		}
		
		return false;
	}
	
	/**body添加鼠标事件**/
	function bindBodyMouseEvent(){
		//鼠标离开body范围,清空选中课程表课程相关信息
		$('body').mouseleave(function(){
	  		resetKcb();//重置课程表信息及解除鼠标事件
	  		curSelectId = '';
	  		curTargetId = '';
	  		selectArea = '';
	  		targetArea = '';
		}).mousemove(function(){//设置选中课程跟随鼠标移动
			var e = window.event || arguments.callee.caller.arguments[0];
			var mouseX = e.clientX;
			var mouseY = e.clientY;
			
			$('#selectCourse').css('left', mouseX+5);
			$('#selectCourse').css('top', mouseY+5);
		}).mouseup(function(){//设置放开鼠标左键事件
			//判断目标单元格是否可以调课
			//选择的单元格与目标单元格是否为同一个单元格
			if(curTargetId=='' || $('#changeFlag_' + curTargetId.substring(curTargetId.indexOf('_')+1)).val()=='0' || curTargetId==curSelectId){
				curSelectId = '';
		  		curTargetId = '';
		  		selectArea = '';
		  		targetArea = '';
		  		//重置课程表信息及解除鼠标事件
				resetKcb();
				return;
			}
			
			//重置课程表信息及解除鼠标事件
			resetKcb();
			
			//判断目标单元格是否有过调课操作
			if(tkOrder.indexOf(curTargetId.substring(curTargetId.indexOf('_')+1)) > -1){
				alertMsg("目标位置已进行过调课操作，无法手工排课！");
				return;
			}
			
			var selContent = '';
			var tagContent = '';
			var tempContent = '';
			var selTitle = '';
			var tagTitle = '';
			var pjType = '';
			var tempTagContent = '';
			var selOrder = curSelectId.substring(curSelectId.indexOf('_')+1);
			var tagOrder = curTargetId.substring(curTargetId.indexOf('_')+1);
			//判断目标单元格是否为固排或禁排
			if(curTargetId != ''){
				targetArea = curTargetId.substring(0, curTargetId.indexOf('_'));
			}
			var changeType = selectArea+'to'+targetArea;
			
			var zcctCheckbox = document.getElementById('zcct');
			
			//判断是否为课程表区域内两节课互换
			if(changeType == 'kcbtokcb'){
				var tempArray = new Array();
				var selTips = '';
				var tagTips = '';
				
				//判断选中的课程设置了合班，获取合班详细信息
				if(selHbSetArray.length > 0){
					tempArray = getHbDetailInfo(tagOrder, selHbSetArray);
					var selSkjhbhArray = tempArray[0].split('｜');
					var selCdbhArray = tempArray[1].split('｜');
					var selCdmcArray = tempArray[2].split('｜');
					selTips = tempArray[3];
					
					if(selTips.length > 0){
						//更新合班课程场地信息
						changeHbSite(selOrder, selSkjhbhArray, selCdbhArray, selCdmcArray);
					}
				}
				
				//判断目标单元格有安排课程
				if($('#changeFlag_' + tagOrder).val() == '1'){
					//判断目标的课程设置了合班，获取合班详细信息
					var tagSkjhbhArray = parseInfoToArray($('#skjhmxbh_' + tagOrder).val());//授课计划明细编号
					var tagHbSetArray = checkHbSetInfo(tagSkjhbhArray);
					//判断目标单元格的课程是否为可合班课程
					if(tagHbSetArray.length > 0){
						tempArray = getHbDetailInfo(selOrder, tagHbSetArray);
						var tagSkjhbhArray = tempArray[0].split('｜');
						var tagCdbhArray = tempArray[1].split('｜');
						var tagCdmcArray = tempArray[2].split('｜');
						tagTips = tempArray[3];
						
						if(tagTips.length > 0){
							//更新合班课程场地信息
							changeHbSite(tagOrder, tagSkjhbhArray, tagCdbhArray, tagCdmcArray);
						}
					}
				}
				
				//判断两个单元格内的课程是否可以课程拼接
				var pjFlag = false;//用于判断是否可以拼接
				var selZC = $('#kczc_' + selOrder).val();
				var tagZC = $('#kczc_' + tagOrder).val();
				
				//判断目标单元格是否有授课周次信息
				if(tagZC!=undefined && tagZC.length>0){
					var selZcStart = '';
					var selZcEnd = '';
					var tagZcStart = '';
					var tagZcEnd = '';
					var beforeInfo = '';
					var afterInfo = '';
					
					//获取选择单元格和目标单元格中的课程信息
					tempNum = (parseInt(selOrder.substring(0, 2), 10)-1)*zjs + parseInt(selOrder.substring(2), 10) - 1;
					selContent = classKCB[tempNum];
					tempNum = (parseInt(tagOrder.substring(0, 2), 10)-1)*zjs + parseInt(tagOrder.substring(2), 10) - 1;
					tagContent = classKCB[tempNum];
					
					//判断授课周次的类型一致的情况下检查可否拼接
					if(selZC.indexOf('-')>-1 && tagZC.indexOf('-')>-1){
						//获取周次起始位置和结束位置
						selZcStart = selZC.substring(0, selZC.indexOf('-'));
						selZcEnd = selZC.substring(selZC.lastIndexOf('-')+1);
						tagZcStart = tagZC.substring(0, tagZC.indexOf('-'));
						tagZcEnd = tagZC.substring(tagZC.lastIndexOf('-')+1);
						
						//判断周次可否拼接
						if(parseInt(selZcEnd, 10)+1==tagZcStart || parseInt(selZcStart, 10)-1==tagZcEnd){
							//判断是拼接在前还是后
							if(parseInt(selZcEnd, 10)+1 == tagZcStart){
								beforeInfo = selContent;
								afterInfo = tagContent;
								pjType = 'before';
							}else{
								beforeInfo = tagContent;
								afterInfo = selContent;
								pjType = 'after';
							}
							pjFlag = true;
						}
					}
					if((selZC=='odd'&&tagZC=='even') || (selZC=='even'&&tagZC=='odd')){
						//判断是拼接在前还是后
						if(selZC=='odd' && tagZC=='even'){
							beforeInfo = selContent;
							afterInfo = tagContent;
							pjType = 'before';
						}else{
							beforeInfo = tagContent;
							afterInfo = selContent;
							pjType = 'after';
						}
						pjFlag = true;
					}
					if((selZC.length==1||selZC.indexOf('#')>-1) && (tagZC.length==1||tagZC.indexOf('#')>-1)){
						var selArray = selZC.replace(/\｜/g, '#').split('#');
						var tagArray = tagZC.replace(/\｜/g, '#').split('#');
						var flag = false;
						
						for(var i=0; i<selArray.length; i++){
							for(var j=0; j<tagArray.length; j++){
								if(selArray[i] == tagArray[j]){
									flag = true;
									break;
								}
							}
							if(flag == true){
								break;
							}
						}
						
						if(flag == false){
							//判断是拼接在前还是后
							if(selArray[0] < tagArray[0]){
								beforeInfo = selContent;
								afterInfo = tagContent;
								pjType = 'before';
							}else{
								beforeInfo = tagContent;
								afterInfo = selContent;
								pjType = 'after';
							}
							pjFlag = true;
						}
					}
					
					//判断是否忽略课程周次冲突
					if(pjFlag==false && zcctCheckbox.checked){
						pjFlag = true;
						beforeInfo = tagContent;
						afterInfo = selContent;
						pjType = 'after';
					}
					
					if(pjFlag == true){
						//清空选择单元格的课程信息
						$('#' + curSelectId).html('&nbsp;<input type="hidden" id="changeFlag_' + selOrder + '" value="2"/>');
						$('#' + curSelectId).removeAttr('title');
					
						//添加目标单元格课程信息
						tempContent = beforeInfo.课程名称 + '｜' + afterInfo.课程名称 + '<br/>' + beforeInfo.授课教师姓名 + '｜' + afterInfo.授课教师姓名;
						tempContent += '<input type="hidden" id="changeFlag_' + tagOrder + '" value="1"/>';
						tempContent += '<input type="hidden" id="skjhmxbh_' + tagOrder + '" value="' + beforeInfo.授课计划明细编号 + '｜' + afterInfo.授课计划明细编号 +'"/>';
						tempContent += '<input type="hidden" id="kclx_' + tagOrder + '" value="' + beforeInfo.课程类型 + '｜' + afterInfo.课程类型 +'"/>';
						tempContent += '<input type="hidden" id="skjsbh_' + tagOrder + '" value="' + beforeInfo.授课教师编号 + '｜' + afterInfo.授课教师编号 + '"/>';
						tempContent += '<input type="hidden" id="cdbh_' + tagOrder + '" value="' + beforeInfo.实际场地编号 + '｜' + afterInfo.实际场地编号 + '"/>';
						tempContent += '<input type="hidden" id="kczc_' + tagOrder + '" value="' + beforeInfo.授课周次 + '｜' + afterInfo.授课周次 + '"/>';
						tempContent += '<input type="hidden" id="kczcxq_' + tagOrder + '" value="' + beforeInfo.授课周次详情 + '｜' + afterInfo.授课周次详情 + '"/>';
						$('#' + curTargetId).html(tempContent);
						selTitle = beforeInfo.课程名称 + '｜' + afterInfo.课程名称 + '\n' + beforeInfo.授课教师姓名 + '｜' + afterInfo.授课教师姓名 + '\n' + beforeInfo.实际场地名称 + '｜' + afterInfo.实际场地名称;
						selTitle += '\n' + parseWeekShow(beforeInfo.授课周次详情 + '｜' + afterInfo.授课周次详情, oddArray, evenArray);//修改授课周次显示内容
						
						//判断如果原来的课程为合班课程，保留原来的合班提示信息
						tagTitle = $('#' + curTargetId).attr('title');
						if(tagTitle.indexOf('\n\n') > -1){
							tagTitle = tagTitle.substring(tagTitle.indexOf('\n\n'));
						}else{
							tagTitle = '';
						}
						selTitle += tagTitle;
						
						//判断目标单元格是否有合班提示
						if(selTips.length > 0){
							if(tagTitle.length > 0){
								selTitle += '\n';
							}else{
								selTitle += '\n\n';
							}
							selTitle += selTips;
						}
						$('#' + curTargetId).attr('title', selTitle);
					}
				}
				
				//判断不可拼接的情况下，直接交换两个单元格中的课程信息
				if(pjFlag == false){
					tempContent = $('#' + curSelectId).html().substring(0, $('#' + curSelectId).html().toUpperCase().indexOf('<INPUT'));
					tempContent += '<input type="hidden" id="changeFlag_' + tagOrder + '" value="1"/>';
					tempContent += '<input type="hidden" id="skjhmxbh_' + tagOrder + '" value="' + $('#skjhmxbh_' + selOrder).val() +'"/>';
					tempContent += '<input type="hidden" id="kclx_' + tagOrder + '" value="' + $('#kclx_' + selOrder).val() +'"/>';
					tempContent += '<input type="hidden" id="skjsbh_' + tagOrder + '" value="' + $('#skjsbh_' + selOrder).val() + '"/>';
					tempContent += '<input type="hidden" id="cdbh_' + tagOrder + '" value="' + $('#cdbh_' + selOrder).val() + '"/>';
					tempContent += '<input type="hidden" id="kczc_' + tagOrder + '" value="' + $('#kczc_' + selOrder).val() + '"/>';
					tempContent += '<input type="hidden" id="kczcxq_' + tagOrder + '" value="' + $('#kczcxq_' + selOrder).val() + '"/>';
					
					//替换title属性
					selTitle = $('#' + curSelectId).attr('title');
					//判断目标单元格内是否有课程
					if($('#changeFlag_' + tagOrder).val() == '2'){
						tempTagContent = '&nbsp;';
						tempTagContent += '<input type="hidden" id="changeFlag_' + selOrder + '" value="2"/>';
						$('#' + curSelectId).removeAttr('title');
					}else{
						tempTagContent = $('#' + curTargetId).html().substring(0, $('#' + curTargetId).html().toUpperCase().indexOf('<INPUT'));
						tempTagContent += '<input type="hidden" id="changeFlag_' + selOrder + '" value="1"/>';
						tempTagContent += '<input type="hidden" id="skjhmxbh_' + selOrder + '" value="' + $('#skjhmxbh_' + tagOrder).val() +'"/>';
						tempTagContent += '<input type="hidden" id="kclx_' + selOrder + '" value="' + $('#kclx_' + tagOrder).val() +'"/>';
						tempTagContent += '<input type="hidden" id="skjsbh_' + selOrder + '" value="' + $('#skjsbh_' + tagOrder).val() + '"/>';
						tempTagContent += '<input type="hidden" id="cdbh_' + selOrder + '" value="' + $('#cdbh_' + tagOrder).val() + '"/>';
						tempTagContent += '<input type="hidden" id="kczc_' + selOrder + '" value="' + $('#kczc_' + tagOrder).val() + '"/>';
						tempTagContent += '<input type="hidden" id="kczcxq_' + selOrder + '" value="' + $('#kczcxq_' + tagOrder).val() + '"/>';
						
						tagTitle = $('#' + curTargetId).attr('title');
						
						var gpFlag = false;
						if(tagTitle.indexOf('固排课程') > -1){
							gpFlag = true;
							tagTitle = tagTitle.substring(6);
						}
						if(tagTitle.indexOf('\n\n') > -1){
							tagTitle = tagTitle.substring(0, tagTitle.indexOf('\n\n'));
						}
						//判断目标单元格是否为合班
						if(tagTips.length > 0){
							tagTitle += '\n\n'+tagTips;
						}
						if(gpFlag == true){
							tagTitle = '固排课程\n\n' + tagTitle;
						}
						$('#' + curSelectId).attr('title', tagTitle);
					}
					$('#' + curTargetId).html(tempContent);
					$('#' + curSelectId).html(tempTagContent);
					
					var gpFlag = false;
					if(selTitle.indexOf('固排课程') > -1){
						gpFlag = true;
						selTitle = selTitle.substring(6);
					}
					if(selTitle.indexOf('\n\n') > -1){
						selTitle = selTitle.substring(0, selTitle.indexOf('\n\n'));
					}
					//判断目标单元格是否为合班
					if(selTips.length > 0){
						selTitle += '\n\n'+selTips;
					}
					if(gpFlag == true){
						selTitle = '固排课程\n\n' + selTitle;
					}
					$('#' + curTargetId).attr('title', selTitle);
				}
				//更新课程表数据
				updateKcb(changeType, pjType);
			}
			
			//判断是将课程表区域的内容拖拽到未排课程区域
			if(changeType == 'kcbtowpkc'){
				//清空课程表单元格内容
				$('#' + curSelectId).removeAttr('title');
				$('#' + curSelectId).html('&nbsp;<input type="hidden" id="changeFlag_' + selOrder + '" value="2"/>');
				var tempArray = updateKcb(changeType);
				updateWpkc(changeType, tempArray);
			}
			
			//判断是否将未排课程拖拽到课程表
			if(changeType == 'wpkctokcb'){
				var tempWpkcCdbh = '';
				var tempCdbh = '';
				var tempWpkcCdmc = '';
				var tempCdmc = '';
				var siteCodeArray = $('#wpkccdbh_' + selOrder).val().split('&');
				var siteNameArray = $('#wpkccdmc_' + selOrder).val().split('&');
				var skzcArray = $('#wpkczcxq_' + selOrder).val().split('&');
				var tempStuNum = stuNum;//学生人数
				//var selHbFlag = false;
				//var hbCourseName = '';
				//var hbClassName = '';
				var tips = '';
				
				for(var i=0; i<siteCodeArray.length; i++){
					tempCdbh = '';
					tempCdmc = '';
					
					//判断选中的课程设置了合班
					if(selHbSetArray.length > 0){
						var tempArray = getHbDetailInfo(tagOrder, selHbSetArray);
						
						tips = tempArray[3];
						//判断选中的课程在目标单元格是否为合班课程,是的话获取合班详细信息。
						if(tips.length > 0){
							tempCdbh = tempArray[1]+'&';
							tempCdmc = tempArray[2]+'&';
						}
						tempStuNum = selHbSetArray[1];//合班班级总人数
					}
					
					//自动分配教室
					if(tempCdbh == ''){
						//判断如果没有场地要求或者只指定了类型的话，随机分配场地
						if(siteCodeArray[i]!='' && siteCodeArray[i].length>2){
							tempCdbh = siteCodeArray[i]+'&';
							tempCdmc = siteNameArray[i]+'&';
						}else{
							var tempCourseInfo = '';
							var tempSkzc = '';
							var tempNum = 0;
							var selSkzc = parseSkzc(skzcArray[i]);
							
							var flag = true;
							//获取该时间序列的授课周次在数组中的位置
							var indexNum = ((parseInt(tagOrder.substring(0, 2), 10)-1)*zjs*2 + parseInt(tagOrder.substring(2), 10)*2) - 1;
							
							//遍历场地信息,查询人数容量核实的教室
							for(var a=0; a<siteUsedOrderInfo.length; a+=5){
								//如果有指定场地类型的话，判断场地类型是否匹配
								if(siteCodeArray[i]!='' && siteCodeArray[i]!=siteUsedOrderInfo[a+2]){
									continue;
								}
								tempNum = parseInt(siteUsedOrderInfo[a+3], 10);
								if(tempNum >= tempStuNum){//判断如果人数符合要求，检查其他信息
									tempCourseInfo = siteUsedOrderInfo[a+4][indexNum];
									
									//判断该时间序列是否安排有其他课程
									if(tempCourseInfo.length > 0){
										tempSkzc = tempCourseInfo[4];
										//判断周次是否有冲突
										for(var m=0; m<tempSkzc.length; m++){
											for(var n=0; n<selSkzc.length; n++){
												if(tempSkzc[m] == selSkzc[n]){
													flag = false;
													break;
												}
											}
											if(flag == false){
												break;
											}
										}
										
										if(flag == true){
											tempCdbh = siteUsedOrderInfo[a]+'&';
											tempCdmc = siteUsedOrderInfo[a+1]+'&';
											break;
										}else{
											flag == true;
											continue;
										}
									}else{
										tempCdbh = siteUsedOrderInfo[a]+'&';
										tempCdmc = siteUsedOrderInfo[a+1]+'&';
										break;
									}
								}
							}
						}
					}
					
					//判断是否有可用场地
					if(tempCdbh == ''){
						tempWpkcCdbh += '无可用教室&';
						tempWpkcCdmc += '无可用教室&';
					}else{
						tempWpkcCdbh += tempCdbh;
						tempWpkcCdmc += tempCdmc;
					}
				}
				tempWpkcCdbh = tempWpkcCdbh.substring(0, tempWpkcCdbh.length-1);
				tempWpkcCdmc = tempWpkcCdmc.substring(0, tempWpkcCdmc.length-1);
				
				//判断两个单元格内的课程是否可以课程拼接
				var pjFlag = false;//用于判断是否可以拼接
				var selZC = $('#wpkczc_' + selOrder).val();
				var tagZC = $('#kczc_' + tagOrder).val();
				//判断目标单元格是否为空单元格
				if(tagZC!=undefined && tagZC.length>0){
					var selZcStart = '';
					var selZcEnd = '';
					var tagZcStart = '';
					var tagZcEnd = '';
					var beforeInfo = '';
					var afterInfo = '';
					
					//获取选择单元格和目标单元格中的课程信息
					tempNum = $('#' + curSelectId).attr('name');
					for(var i=0; i<wpkc.length; i++){
						if(wpkc[i].授课计划明细编号 == tempNum){
							selContent = wpkc[i];
						}
					}
					tempNum = (parseInt(tagOrder.substring(0, 2), 10)-1)*zjs + parseInt(tagOrder.substring(2), 10) - 1;
					tagContent = classKCB[tempNum];
					
					//判断授课周次的类型一致的情况下检查可否拼接
					if(selZC.indexOf('-')>-1 && tagZC.indexOf('-')>-1){
						//获取周次起始位置和结束位置
						selZcStart = selZC.substring(0, selZC.indexOf('-'));
						selZcEnd = selZC.substring(selZC.lastIndexOf('-')+1);
						tagZcStart = tagZC.substring(0, tagZC.indexOf('-'));
						tagZcEnd = tagZC.substring(tagZC.lastIndexOf('-')+1);
						
						//判断周次可否拼接
						if(parseInt(selZcEnd, 10)+1==tagZcStart || parseInt(selZcStart, 10)-1==tagZcEnd){
							//判断是拼接在前还是后
							if(parseInt(selZcEnd, 10)+1 == tagZcStart){
								beforeInfo = selContent;
								afterInfo = tagContent;
								pjType = 'before';
							}else{
								beforeInfo = tagContent;
								afterInfo = selContent;
								pjType = 'after';
							}
							pjFlag = true;
						}
					}
					if((selZC=='odd'&&tagZC=='even') || (selZC=='even'&&tagZC=='odd')){
						//判断是拼接在前还是后
						if(selZC=='odd' && tagZC=='even'){
							beforeInfo = selContent;
							afterInfo = tagContent;
							pjType = 'before';
						}else{
							beforeInfo = tagContent;
							afterInfo = selContent;
							pjType = 'after';
						}
						pjFlag = true;
					}
					if((selZC.length==1||selZC.indexOf('#')>-1) && (tagZC.length==1||tagZC.indexOf('#')>-1)){
						var selArray = selZC.replace(/\｜/g, '#').split('#');
						var tagArray = tagZC.replace(/\｜/g, '#').split('#');
						var flag = false;
						
						for(var i=0; i<selArray.length; i++){
							for(var j=0; j<tagArray.length; j++){
								if(selArray[i] == tagArray[j]){
									flag = true;
									break;
								}
							}
							if(flag == true){
								break;
							}
						}
						
						if(flag == false){
							//判断是拼接在前还是后
							if(selArray[0] < tagArray[0]){
								beforeInfo = selContent;
								afterInfo = tagContent;
								pjType = 'before';
							}else{
								beforeInfo = tagContent;
								afterInfo = selContent;
								pjType = 'after';
							}
							pjFlag = true;
						}
					}
					
					//判断是否忽略课程周次冲突
					if(pjFlag==false && zcctCheckbox.checked){
						pjFlag = true;
						beforeInfo = tagContent;
						afterInfo = selContent;
						pjType = 'after';
					}
					
					if(pjFlag == true){
						//添加目标单元格课程信息
						tempContent = beforeInfo.课程名称 + '｜' + afterInfo.课程名称 + '<br/>' + beforeInfo.授课教师姓名 + '｜' + afterInfo.授课教师姓名;
						selTitle = beforeInfo.课程名称 + '｜' + afterInfo.课程名称 + '\n' + beforeInfo.授课教师姓名 + '｜' + afterInfo.授课教师姓名 ;
						tempContent += '<input type="hidden" id="changeFlag_' + tagOrder + '" value="1"/>';
						tempContent += '<input type="hidden" id="skjhmxbh_' + tagOrder + '" value="' + beforeInfo.授课计划明细编号 + '｜' + afterInfo.授课计划明细编号 +'"/>';
						tempContent += '<input type="hidden" id="kclx_' + tagOrder + '" value="' + beforeInfo.课程类型 + '｜' + afterInfo.课程类型 +'"/>';
						tempContent += '<input type="hidden" id="skjsbh_' + tagOrder + '" value="' + beforeInfo.授课教师编号 + '｜' + afterInfo.授课教师编号 + '"/>';
						if(pjType == 'before'){
							tempContent += '<input type="hidden" id="cdbh_' + tagOrder + '" value="' + tempWpkcCdbh + '｜' + afterInfo.实际场地编号 + '"/>';
							selTitle += '\n' + tempWpkcCdmc + '｜' + afterInfo.实际场地名称;
						}else{
							tempContent += '<input type="hidden" id="cdbh_' + tagOrder + '" value="' + beforeInfo.实际场地编号 + '｜' + tempWpkcCdbh + '"/>';
							selTitle += '\n' + beforeInfo.实际场地名称 + '｜' + tempWpkcCdmc;
						}
						tempContent += '<input type="hidden" id="kczc_' + tagOrder + '" value="' + beforeInfo.授课周次 + '｜' + afterInfo.授课周次 + '"/>';
						tempContent += '<input type="hidden" id="kczcxq_' + tagOrder + '" value="' + beforeInfo.授课周次详情 + '｜' + afterInfo.授课周次详情 + '"/>';
						selTitle += '\n' + parseWeekShow(beforeInfo.授课周次详情 + '｜' + afterInfo.授课周次详情, oddArray, evenArray);//修改授课周次显示内容
						
						//判断如果原来的课程为合班课程，保留原来的合班提示信息
						tagTitle = $('#' + curTargetId).attr('title');
						if(tagTitle.indexOf('\n\n') > -1){
							tagTitle = tagTitle.substring(tagTitle.indexOf('\n\n'));
						}else{
							tagTitle = '';
						}
						selTitle += tagTitle;
						
						//判断是否合班课程
						if(tips.length > 0){
							if(tagTitle.length > 0){
								selTitle += '\n';
							}else{
								selTitle += '\n\n';
							}
							selTitle += tips;
						}
						$('#' + curTargetId).html(tempContent);
						$('#' + curTargetId).attr('title', selTitle);
					}
				}
						
				//判断不可拼接的情况下，直接交换两个单元格中的课程信息
				if(pjFlag == false){
					tempTagContent = $('#' + curSelectId).html().substring(0, $('#' + curSelectId).html().toUpperCase().indexOf('<INPUT')).replace(/\&amp;/g, '\&');
					selTitle = tempTagContent.replace(/\<BR>/g, '\n');
					tempTagContent += '<input type="hidden" id="changeFlag_' + tagOrder + '" value="1"/>';
					tempTagContent += '<input type="hidden" id="skjhmxbh_' + tagOrder + '" value="' + $('#wpkc_' + selOrder).attr('name') +'"/>';
					tempTagContent += '<input type="hidden" id="kclx_' + tagOrder + '" value="';
					if(sAuth.indexOf(pubTeacher) > -1)
						tempTagContent += '01';
					if(sAuth.indexOf(majorTeacher) > -1)
						tempTagContent += '02';
					tempTagContent += '"/>';
					tempTagContent += '<input type="hidden" id="skjsbh_' + tagOrder + '" value="' + $('#wpkcjsbh_' + selOrder).val() + '"/>';
					tempTagContent += '<input type="hidden" id="cdbh_' + tagOrder + '" value="' + tempWpkcCdbh + '"/>';
					tempTagContent += '<input type="hidden" id="kczc_' + tagOrder + '" value="' + $('#wpkczc_' + selOrder).val() + '"/>';
					tempTagContent += '<input type="hidden" id="kczcxq_' + tagOrder + '" value="' + $('#wpkczcxq_' + selOrder).val() + '"/>';
					
					if(tempWpkcCdmc != ''){
						selTitle += '\n' + tempWpkcCdmc;
					}
					selTitle += '\n' + parseWeekShow($('#wpkczcxq_' + selOrder).val(), oddArray, evenArray);//修改授课周次显示内容
					
					//判断是否合班课程
					if(tips.length > 0){
						selTitle += tips;
					}
					$('#' + curTargetId).html(tempTagContent);
					$('#' + curTargetId).attr('title', selTitle);
				}
						
				var tempArray = updateKcb(changeType, pjType, tempWpkcCdbh, tempWpkcCdmc);
				updateWpkc(changeType, tempArray);
			}
			
			selectArea = '';
		});
	}
	
	/**获取合班信息
		@order 目标单元格时间序列
		@hbSetInfoArray 合班设置信息
	**/
	function getHbDetailInfo(order, hbSetInfoArray){
		var resultArray = new Array();
		var hbFlag = false;
		var skjhbh = '';
		var siteCode = '';
		var siteName = '';
		var tips = '';
		
		var tempHbInfoArray = judgeHbCourse(order, hbSetInfoArray);
		if(tempHbInfoArray[0].length > 0){
			var hbCodeArray = tempHbInfoArray[0].split('+');
			var tempHbCodeArray = new Array();
			
			for(var i=0; i<hbCodeArray.length; i++){
				hbFlag = false;
				tempHbCodeArray = hbCodeArray[i].split(',');
				
				for(var j=0; j<tempHbCodeArray.length&&hbFlag==false; j++){
					//查询合班课程详细信息
					for(var k=0; k<hbCourseInfo.length&&hbFlag==false; k+=6){
						//判断已排课的课时序列与目标单元格是否相同
						if(hbCourseInfo[k] == order){
							var tempArray = hbCourseInfo[k+1];//合班授课计划编号
							
							for(var a=0; a<tempArray.length&&hbFlag==false; a++){
								if(tempHbCodeArray[j] == tempArray[a]){
									skjhbh += tempArray[a]+'｜';
									siteCode += hbCourseInfo[k+4]+'｜';
									siteName += hbCourseInfo[k+5]+'｜';
									tips += '该节'+hbCourseInfo[k+2]+'为合班课程（'+hbCourseInfo[k+3]+'）'+'\n';
									hbFlag = true;
								}
							}
						}
					}
				}
			}
		}
		
		skjhbh = skjhbh.substring(0, skjhbh.length-1);
		siteCode = siteCode.substring(0, siteCode.length-1);
		siteName = siteName.substring(0, siteName.length-1);
		tips = tips.substring(0, tips.length-1);
		resultArray.push(skjhbh);
		resultArray.push(siteCode);
		resultArray.push(siteName);
		resultArray.push(tips);
		return resultArray;
	}
	
	/**更新合班课程场地信息（移动合班课程时候，如果场地不一致，更新选中单元格课程场地信息）
		@order 时间序列
		@skjhbhArray 授课计划明细编号
		@cdbhArray 实际场地编号
		@cdmcArray 实际场地名称
	**/
	function changeHbSite(order, skjhbhArray, cdbhArray, cdmcArray){
		var kcbCode = $('#kcb_' + order).attr('name');
		var tempSiteCode = '';
		var tempSiteName = '';
		var titleArray = new Array();
		var tempTitle = '';
		var flag = false;
		var hbSetSkjhbhArray = '';
		var hbSetSkjhbh = '';
		
		//更新课表中课程和隐藏域中的场地信息
		for(var i=0; i<classKCB.length; i++){
			if(classKCB[i].课程表明细编号 == kcbCode){
				var kcbSkjhbhArray = classKCB[i].授课计划明细编号.split('｜');
				var kcbCdbhArray = classKCB[i].实际场地编号.split('｜');
				var kcbCdmcArray = classKCB[i].实际场地名称.split('｜');
				
				for(var j=0; j<skjhbhArray.length; j++){
					flag = false;
					
					for(var k=0; k<kcbSkjhbhArray.length&&flag==false; k++){
						for(var a=0; a<hbSetInfo.length&&flag==false; a+=3){
							hbSetSkjhbhArray = hbSetInfo[a];
							hbSetSkjhbh = '';
							
							for(var b=0; b<hbSetSkjhbhArray.length; b++){
								hbSetSkjhbh += hbSetSkjhbhArray[b]+',';
							}
							//判断是否为合班课程
							if(hbSetSkjhbh.indexOf(skjhbhArray[j])>-1 && hbSetSkjhbh.indexOf(kcbSkjhbhArray[j])>-1){
								tempSiteCode += cdbhArray[j]+'｜';
								tempSiteName += cdmcArray[j]+'｜';
								flag = true;
							}
						}
					}
					if(flag == false){
						tempSiteCode += kcbCdbhArray[k]+'｜';
						tempSiteName += kcbCdmcArray[k]+'｜';
					}
				}
				tempSiteCode = tempSiteCode.substring(0, tempSiteCode.length-1);
				tempSiteName = tempSiteName.substring(0, tempSiteName.length-1);
				
				classKCB[i].实际场地编号 = tempSiteCode;
				classKCB[i].实际场地名称 = tempSiteName;
				$('#cdbh_' + order).val(tempSiteCode);
				titleArray = $('#kcb_' + order).attr('title').split('\n');
				for(var j=0; j<titleArray.length; j++){
					if(j == 2){
						tempTitle += tempSiteName;
					}else{
						tempTitle += titleArray[j];
					}
					tempTitle += '\n';
				}
				tempTitle = tempTitle.substring(0, tempTitle.length-1);
				$('#kcb_' + order).attr('title', tempTitle);
				
				break;
			}
		}
	}
	
	/**重置课程表信息**/
	function resetKcb(){
		$('#selectContent').html('');
		$('#selectCourse').hide();
		enterColor = '#FFFFFF';
		$('.kcbTdStyle').css('background-color', '#FFFFFF');
		$('.titleBG').css('background-color', '#EFEFEF');
	  	$('.wpkcTdStyle').css('background-color', '#FFFFFF');
	  	//$('#' + curTargetId).css('background-color', '#FFE48D');
	  	
	  	//重置title信息
	  	for(var i=0; i<titleArray.length; i+=3){
	  		$('#changeFlag_' + titleArray[i]).val(titleArray[i+2]);
	  		$('#kcb_' + titleArray[i]).attr('title', titleArray[i+1]);
	  	}
	  	
	  	//解除鼠标事件绑定
		$('body').unbind('mouseup');
		$('body').unbind('mousemove');
		$('body').unbind('mouseleave');
	}
	
	/**更新课程表数据
		@type 调整课程表的方式
		@pjType 是否拼接
		@wpkcCdbh 未排课程随机到的场地编号
		@wpkcCdmc 未排课程随机到的场地名称
	**/
	function updateKcb(type, pjType, wpkcCdbh, wpkcCdmc){
		var tempSelectArray = new Array();//选择的单元格的数据
		var tempTargetArray = new Array();//目标的单元格的数据
		var tempWpkcCdbh = '';
		var tempWpkcCdmc = '';
		var existFlag = false;
		
		//课程表中课程对调
		if(type == 'kcbtokcb'){
			tempSelectArray.push($('#' + curSelectId).attr('name'));
			tempTargetArray.push($('#' + curTargetId).attr('name'));
			
			//遍历课程表，获取选择的单元格数据和目标单元格数据
			for(var i=0; i<classKCB.length; i++){
				//classKCB内容格式：课程表明细编号,班级排课状态,时间序列,授课计划明细编号,课程名称,授课教师姓名,授课教师编号,实际场地编号,实际场地名称,授课周次
				if(classKCB[i].课程表明细编号 == tempSelectArray[0]){
					tempSelectArray.push(classKCB[i].班级排课状态);
					tempSelectArray.push(classKCB[i].授课计划明细编号);
					tempSelectArray.push(classKCB[i].课程名称);
					tempSelectArray.push(classKCB[i].授课教师姓名);
					tempSelectArray.push(classKCB[i].授课教师编号);
					tempSelectArray.push(classKCB[i].实际场地编号);
					tempSelectArray.push(classKCB[i].实际场地名称);
					tempSelectArray.push(classKCB[i].授课周次);
					tempSelectArray.push(classKCB[i].授课周次详情);
					
					if(tempTargetArray.length > 1){//判断如果选择的单元格数据和目标单元格数据都已取到，结束循环
						break;
					}
				}
				
				if(classKCB[i].课程表明细编号 == tempTargetArray[0]){
					tempTargetArray.push(classKCB[i].班级排课状态);
					tempTargetArray.push(classKCB[i].授课计划明细编号);
					tempTargetArray.push(classKCB[i].课程名称);
					tempTargetArray.push(classKCB[i].授课教师姓名);
					tempTargetArray.push(classKCB[i].授课教师编号);
					tempTargetArray.push(classKCB[i].实际场地编号);
					tempTargetArray.push(classKCB[i].实际场地名称);
					tempTargetArray.push(classKCB[i].授课周次);
					tempTargetArray.push(classKCB[i].授课周次详情);
					
					if(tempSelectArray.length > 1){//判断如果选择的单元格数据和目标单元格数据都已取到，结束循环
						break;
					}
				}
			}
			
			//遍历课程表，更新数据
			for(var i=0; i<classKCB.length; i++){
				if(classKCB[i].课程表明细编号==tempSelectArray[0] || classKCB[i].课程表明细编号 == tempTargetArray[0]){
					//判断更新选择的单元格
					if(classKCB[i].课程表明细编号 == tempSelectArray[0]){
						//判断是否为拼接课程
						if(pjType == ''){
							classKCB[i].班级排课状态 = tempTargetArray[1];
							classKCB[i].授课计划明细编号 = tempTargetArray[2];
							classKCB[i].课程名称 = tempTargetArray[3];
							classKCB[i].授课教师姓名 = tempTargetArray[4];
							classKCB[i].授课教师编号 = tempTargetArray[5];
							classKCB[i].实际场地编号 = tempTargetArray[6];
							classKCB[i].实际场地名称 = tempTargetArray[7];
							classKCB[i].授课周次 = tempTargetArray[8];
							classKCB[i].授课周次详情 = tempTargetArray[9];
						}else{
							classKCB[i].班级排课状态 = '';
							classKCB[i].授课计划明细编号 = '';
							classKCB[i].课程名称 = '';
							classKCB[i].授课教师姓名 = '';
							classKCB[i].授课教师编号 = '';
							classKCB[i].实际场地编号 = '';
							classKCB[i].实际场地名称 = '';
							classKCB[i].授课周次 = '';
							classKCB[i].授课周次详情 = '';
						}
					}
					
					//判断更新目标单元格
					if(classKCB[i].课程表明细编号 == tempTargetArray[0]){
						//判断是否为拼接课程
						if(pjType == ''){
							classKCB[i].班级排课状态 = tempSelectArray[1];
							classKCB[i].授课计划明细编号 = tempSelectArray[2];
							classKCB[i].课程名称 = tempSelectArray[3];
							classKCB[i].授课教师姓名 = tempSelectArray[4];
							classKCB[i].授课教师编号 = tempSelectArray[5];
							classKCB[i].实际场地编号 = tempSelectArray[6];
							classKCB[i].实际场地名称 = tempSelectArray[7];
							classKCB[i].授课周次 = tempSelectArray[8];
							classKCB[i].授课周次详情 = tempSelectArray[9];
						}else{
							var beforeInfo = '';
							var afterInfo = '';
							if(pjType == 'before'){
								beforeInfo = tempSelectArray;
								afterInfo = tempTargetArray;
							}else{
								beforeInfo = tempTargetArray;
								afterInfo = tempSelectArray;
							}
							
							if(beforeInfo[1]=='2' || afterInfo[1]=='2'){
								classKCB[i].班级排课状态 = '2';
							}else{
								classKCB[i].班级排课状态 = beforeInfo[1];
							}
							classKCB[i].授课计划明细编号 = beforeInfo[2] + '｜' + afterInfo[2];
							classKCB[i].课程名称 = beforeInfo[3] + '｜' + afterInfo[3];
							classKCB[i].授课教师姓名 = beforeInfo[4] + '｜' + afterInfo[4];
							classKCB[i].授课教师编号 = beforeInfo[5] + '｜' + afterInfo[5];
							classKCB[i].实际场地编号 = beforeInfo[6] + '｜' + afterInfo[6];
							classKCB[i].实际场地名称 = beforeInfo[7] + '｜' + afterInfo[7];
							classKCB[i].授课周次 = beforeInfo[8] + '｜' + afterInfo[8];
							classKCB[i].授课周次详情 = beforeInfo[9] + '｜' + afterInfo[9];
						}
					}
					
					existFlag = false;
					//遍历需要更新的课表信息中是否有当前单元格信息，有的话更新，没有的话添加
					for(var k=0; k<updateArray.length; k+=9){
						if(updateArray[k] == classKCB[i].课程表明细编号){
							updateArray[k+1] = classKCB[i].班级排课状态;
							updateArray[k+2] = classKCB[i].授课计划明细编号;
							updateArray[k+3] = classKCB[i].授课教师编号;
							updateArray[k+4] = classKCB[i].授课教师姓名;
							updateArray[k+5] = classKCB[i].实际场地编号;
							updateArray[k+6] = classKCB[i].实际场地名称;
							updateArray[k+7] = classKCB[i].授课周次;
							updateArray[k+8] = classKCB[i].授课周次详情;
							existFlag = true;
							break;
						}
					}
					//判断是否需要添加
					if(existFlag == false){
						updateArray.push(classKCB[i].课程表明细编号);
						updateArray.push(classKCB[i].班级排课状态);
						updateArray.push(classKCB[i].授课计划明细编号);
						updateArray.push(classKCB[i].授课教师编号);
						updateArray.push(classKCB[i].授课教师姓名);
						updateArray.push(classKCB[i].实际场地编号);
						updateArray.push(classKCB[i].实际场地名称);
						updateArray.push(classKCB[i].授课周次);
						updateArray.push(classKCB[i].授课周次详情);
					}
				}
			}
		}
		
		//将课程表中内容拖拽到未排课程表格中
		if(type == 'kcbtowpkc'){
			tempSelectArray.push($('#' + curSelectId).attr('name'));
			
			//遍历课程表，更新当前选择的单元格内容
			for(var i=0; i<classKCB.length; i++){
				if(classKCB[i].课程表明细编号 == tempSelectArray[0]){
					tempSelectArray.push(classKCB[i].授课计划明细编号);
					classKCB[i].班级排课状态 = '';
					classKCB[i].授课计划明细编号 = '';
					classKCB[i].课程名称 = '';
					classKCB[i].授课教师姓名 = '';
					classKCB[i].授课教师编号 = '';
					classKCB[i].实际场地编号 = '';
					classKCB[i].实际场地名称 = '';
					classKCB[i].授课周次 = '';
					classKCB[i].授课周次详情 = '';
					
					//更新保存时需要更新的数据
					for(var k=0; k<updateArray.length; k+=9){
						if(updateArray[k] == tempSelectArray[0]){
							updateArray[k+1] = "";
							updateArray[k+2] = "";
							updateArray[k+3] = "";
							updateArray[k+4] = "";
							updateArray[k+5] = "";
							updateArray[k+6] = "";
							updateArray[k+7] = "";
							updateArray[k+8] = "";
							existFlag = true;
							break;
						}
					}
					//判断是否需要添加
					if(existFlag == false){
						updateArray.push(tempSelectArray[0]);
						updateArray.push("");
						updateArray.push("");
						updateArray.push("");
						updateArray.push("");
						updateArray.push("");
						updateArray.push("");
						updateArray.push("");
						updateArray.push("");
					}
					return tempSelectArray;
				}
			}
		}
		
		//将未排课程安排到课程表中
		if(type == 'wpkctokcb'){
			tempSelectArray.push($('#' + curSelectId).attr('name'));//未排课程信息数组
			tempTargetArray.push($('#' + curTargetId).attr('name'));//课程表课程信息数组
			
			//遍历课程表，更新当前选择的单元格内容
			for(var i=0; i<classKCB.length; i++){
				if(classKCB[i].课程表明细编号 == tempTargetArray[0]){
					//判断课程表中的目标单元格是否有课程
					if(classKCB[i].授课计划明细编号 != ''){
						//有的话，判断如果为不可拼接，需要将原来课程表单元格中的信息放入未排课程中
						if(pjType == ''){
							tempTargetArray.push(classKCB[i].班级排课状态);
							tempTargetArray.push(classKCB[i].授课计划明细编号);
							tempTargetArray.push(classKCB[i].课程名称);
							tempTargetArray.push(classKCB[i].授课教师姓名);
							tempTargetArray.push(classKCB[i].授课教师编号);
							tempTargetArray.push(classKCB[i].实际场地编号);
							tempTargetArray.push(classKCB[i].实际场地名称);
							tempTargetArray.push(classKCB[i].授课周次);
							tempTargetArray.push(classKCB[i].授课周次详情);
						}
					}
					
					//遍历未排课程信息
					for(var j=0; j<wpkc.length; j++){
						if(tempSelectArray[0] == wpkc[j].授课计划明细编号){
							//判断是否为拼接课程
							if(pjType == ''){
								classKCB[i].班级排课状态 = '4';
								classKCB[i].授课计划明细编号 = wpkc[j].授课计划明细编号;
								classKCB[i].课程名称 = wpkc[j].课程名称;
								classKCB[i].授课教师姓名 = wpkc[j].授课教师姓名;
								classKCB[i].授课教师编号 = wpkc[j].授课教师编号;
								classKCB[i].实际场地编号 = wpkcCdbh;
								classKCB[i].实际场地名称 = wpkcCdmc;
								classKCB[i].授课周次 = wpkc[j].授课周次;
								classKCB[i].授课周次详情 = wpkc[j].授课周次详情;
							}else{
								var beforeInfo = '';
								var afterInfo = '';
								if(pjType == 'before'){
									beforeInfo = wpkc[j];
									afterInfo = classKCB[i];
								}else{
									beforeInfo = classKCB[i];
									afterInfo = wpkc[j];
								}
								
								if(beforeInfo.班级排课状态=='2' || afterInfo.班级排课状态=='2'){
									classKCB[i].班级排课状态 = '2';
								}else{
									classKCB[i].班级排课状态 = '4';
								}
								classKCB[i].授课计划明细编号 = beforeInfo.授课计划明细编号 + '｜' + afterInfo.授课计划明细编号;
								classKCB[i].课程名称 = beforeInfo.课程名称 + '｜' + afterInfo.课程名称;
								classKCB[i].授课教师姓名 = beforeInfo.授课教师姓名 + '｜' + afterInfo.授课教师姓名;
								classKCB[i].授课教师编号 = beforeInfo.授课教师编号 + '｜' + afterInfo.授课教师编号;
								if(pjType == 'before'){
									classKCB[i].实际场地编号 = tempWpkcCdbh + '｜' + afterInfo.实际场地编号;
									classKCB[i].实际场地名称 = tempWpkcCdmc + '｜' + afterInfo.实际场地名称;
								}else{
									classKCB[i].实际场地编号 = beforeInfo.实际场地编号 + '｜' + wpkcCdbh;
									classKCB[i].实际场地名称 = beforeInfo.实际场地名称 + '｜' + wpkcCdmc;
								}
								classKCB[i].授课周次 = beforeInfo.授课周次 + '｜' + afterInfo.授课周次;
								classKCB[i].授课周次详情 = beforeInfo.授课周次详情 + '｜' + afterInfo.授课周次详情;
							}
							break;
						}
					}
					
					//更新保存时需要更新的数据
					for(var k=0; k<updateArray.length; k+=9){
						if(updateArray[k] == classKCB[i].课程表明细编号){
							updateArray[k+1] = classKCB[i].班级排课状态;
							updateArray[k+2] = classKCB[i].授课计划明细编号;
							updateArray[k+3] = classKCB[i].授课教师编号;
							updateArray[k+4] = classKCB[i].授课教师姓名;
							updateArray[k+5] = classKCB[i].实际场地编号;
							updateArray[k+6] = classKCB[i].实际场地名称;
							updateArray[k+7] = classKCB[i].授课周次;
							updateArray[k+8] = classKCB[i].授课周次详情;
							existFlag = true;
							break;
						}
					}
					//判断是否需要添加
					if(existFlag == false){
						updateArray.push(classKCB[i].课程表明细编号);
						updateArray.push(classKCB[i].班级排课状态);
						updateArray.push(classKCB[i].授课计划明细编号);
						updateArray.push(classKCB[i].授课教师编号);
						updateArray.push(classKCB[i].授课教师姓名);
						updateArray.push(classKCB[i].实际场地编号);
						updateArray.push(classKCB[i].实际场地名称);
						updateArray.push(classKCB[i].授课周次);
						updateArray.push(classKCB[i].授课周次详情);
					}
					
					return tempTargetArray;
				}
			}
		}
	}
	
	/**更新未排课程数据
		@type 调整课程表的方式
		@skjhmxbh 授课计划明细编号
	**/
	function updateWpkc(type, tempArray){
		//判断将未排课程放入课程表中
		if(type == 'wpkctokcb'){
			//更新未排课程信息
			for(var i=0; i<wpkc.length; i++){
				if(wpkc[i].授课计划明细编号 == $('#' + curSelectId).attr('name')){
					wpkc[i].剩余节数 = parseInt(wpkc[i].剩余节数, 10)-1;
					break;
				}
			}
		}
		
		//判断课程表目标单元格中是否有课程，有的话，需要放入未排课程中
		if(tempArray.length > 1){
			var skjhArray =parseInfoToArray(tempArray[1]);//授课计划明细编号
		
			for(var i=0; i<skjhArray.length; i++){
				//遍历未排课程数据
				//wpkc内容格式：授课计划明细编号,课程名称,授课教师姓名,授课教师编号,场地要求,场地名称,授课周次,剩余节数
				for(var j=0; j<wpkc.length; j++){
					if(wpkc[j].授课计划明细编号 == skjhArray[i]){
						wpkc[j].剩余节数 = parseInt(wpkc[j].剩余节数, 10)+1;
					}
				}
			}
		}
		
		//清空未排课程表格内容
		$('.wpkcTdCentent').removeAttr('title');
		$('.wpkcTdCentent').html('&nbsp;');
		fillWpkc();//填充未排课程表格
	}
	
	/**解析已用时间序列数据
		@usedInfo 原始数据
	**/
	function parseUsedInfo(usedInfo){
		var resultArray = new Array();
		var usedOrderArray = new Array();
		var tempUsedInfo = '';
		var tempOrderInfo = '';
		var indexNum = '';
		
		for(var i=0; i<usedInfo.length; i++){
			resultArray.push(usedInfo[i].code);
			resultArray.push(usedInfo[i].name);
			resultArray.push(usedInfo[i].type);
			resultArray.push(usedInfo[i].num);
			tempUsedInfo = usedInfo[i].usedOrder.split(',');
			usedOrderArray = timeOrderArray.slice(0);//复制完整时间序列
			
			if(tempUsedInfo != ''){
				for(var j=0; j<tempUsedInfo.length; j++){
					tempOrderInfo = tempUsedInfo[j].split(':');
					indexNum = ((parseInt(tempOrderInfo[0].substring(0, 2), 10)-1)*zjs*2 + parseInt(tempOrderInfo[0].substring(2), 10)*2) - 1;
					//alert(usedInfo[i].code+":"+tempOrderInfo[1]);
					usedOrderArray[indexNum] = parseCourseInfo(usedInfo[i].code, tempOrderInfo[1]);
				}
			}
			
			resultArray.push(usedOrderArray);
		}
		return resultArray;
	}
	
	<%--
	//课程表右键事件
	//课程表区域鼠标右键菜单设置
	$('#kcbMenu').menu({
		onClick:function(item){
			if(item.text == "教室"){
				//alert(222);
			}
	    }
	});
	//document.oncontextmenu = kcbContextMenu;
	function kcbContextMenu(){
		var e = event || window.event;
		if(kcbFlag == true){
		    if($('#'+e.srcElement.id).html()!='&nbsp;' && e.srcElement.tagName=="TD"){
		    	$('#kcbMenu').menu('show', {
	                left: e.clientX,
	                top: e.clientY
	            });
		    	//window.alert("您单击了右键");
		    }
	    	return false;
	    }
	}
	--%>
	
	/**设置排课状态**/
	function setPkState(state){
		$.ajax({
			type : "POST",
			async:true,
			url : '<%=request.getContextPath()%>/Svl_Pksz',
			data : 'active=setPkState&pkState=' + state,
			dataType:"json",
			success : function(data) {
			}
		});
	}
	
	//页面刷新或关闭调用
	$(window).unload(function(){
		setPkState('noUse');
	});
	
	$(window).bind('beforeunload',function(){
		if(navigator.userAgent.indexOf("Chrome") > -1){
			setPkState('noUse');
		}
	});
	
	/**计算时间差**/
	/*
	function countTime(pkTime){
		var resultTime = '';
		var myDate = new Date();
		var curTime = myDate.getTime();//获取当前时间
		var time = curTime - pkTime;
		var days = Math.floor(time/(24*3600*1000));//计算出相差天数
		var leave1 = time%(24*3600*1000);//计算天数后剩余的毫秒数
		var hours = Math.floor(leave1/(3600*1000));//计算出小时数
		var leave2 = leave1%(3600*1000);//计算小时数后剩余的毫秒数
		var minutes = Math.floor(leave2/(60*1000));//计算相差分钟数
		var leave3 = leave2%(60*1000);//计算分钟数后剩余的毫秒数
		var seconds = Math.round(leave3/1000);//计算相差秒数
		
		if(days > 0){
			resultTime += '&nbsp;'+days+"天";
		}
		if(hours > 0){
			resultTime += '&nbsp;'+hours+'小时';
		}
		if(minutes > 0){
			resultTime += "&nbsp;"+minutes+"分钟";
		}else{
			resultTime = "&nbsp;"+seconds+" 秒";
		}
		
		return resultTime;
	}
	*/
</script>
</html>