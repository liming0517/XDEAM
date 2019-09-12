<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%
	/**
		创建人：lupengfei
		Create date: 2016.08.18
		功能说明：用于设置授课计划
		页面类型:列表及模块入口
		修订信息(有多次时,可有多个)
		修订日期:
		原因:
		修订人:
		修订时间:
	**/
 %>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/publicScript.js"></script>
		<style type="text/css">
			#divPageMask2{background-color:#D2E0F2; filter:alpha(opacity=90);left:0px;top:0px;z-index:100;}
			.maskStyle{
				width:100%;
				height:100%;
				overflow:hidden;
				display:none;
				background-color:#D2E0F2;
				filter:alpha(opacity=80);
				position:absolute;
				top:0px;
				left:0px;
				z-index:100;
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
		</style>
	</head>
	
	<body class="easyui-layout" onkeydown="myKeyDown()">
		<%-- 遮罩层 --%>
    	<div id="divPageMask2" style="position:absolute;top:0px;left:0px;width:100%;height:100%;">
    	</div>		
		<div id="divPageMask4" class="maskStyle">
			<div id="maskFont2"></div>
		</div>
		<div data-options="region:'west'" title="" style = "width:28%;">
			<div class="easyui-layout" fit="true">
				<div region="north" split="true" border="false" style="height:64px;"> 
					<table  class = "tablestyle" width = "100%">
						<tr>
							<td class="titlestyle">学年学期</td>
							<td>
								<select id="XNXQ" name="XNXQ" class="easyui-combobox" style="width:150px;"></select>
							</td>
							<td>
								<a id="download" onclick="doToolbar(this.id)" class="easyui-linkbutton" href="TeachingPlan.xls" data-options="plain:true,iconCls:'icon-download'">模板下载</a>
							</td>
						</tr>
						<tr>
							<td class="titlestyle">教学性质</td>
							<td>
								<select id="JXXZ" name="JXXZ" class="easyui-combobox" style="width:150px;"></select>
							</td>
							<td>
								<a href="#" onclick="doToolbar(this.id);" id="import"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-site'">导入</a>
							</td>
						</tr>
					</table>
				</div> 
				<div region="center" border="false"> 
					<div id="classTree" class="easyui-tree" style="width:100%;">
					</div>
				</div> 
			</div> 	
			<div id="showDialog" style="width:280px; height:245px; overflow:hidden;">
				<iframe id="iframe" name="iframe" width="100%" height="100%"></iframe>
			</div>		
		</div>
		
		<div data-options="region:'center'" style="width:100%;height:100%;">
			<div  title="" style="background:#fafafa;height:30px;">
				<table  class = "tablestyle" width = "100%">
					<tr>
						<td>
							<a id="new"  onclick="doToolbar(this.id)" class="easyui-linkbutton" href="javascript:void(0);" data-options="plain:true,iconCls:'icon-new'">新建</a>
							<a id="edit" onclick="doToolbar(this.id)" class="easyui-linkbutton" href="javascript:void(0);" data-options="plain:true,iconCls:'icon-edit'">编辑</a>
<!-- 							<a id="specialEdit" onclick="doToolbar(this.id)" class="easyui-linkbutton" href="javascript:void(0);" data-options="plain:true,iconCls:'icon-edit'">特殊内容编辑</a> -->
							<a id="delete" onclick="doToolbar(this.id)" class="easyui-linkbutton" href="javascript:void(0);" data-options="plain:true,iconCls:'icon-no'">删除</a>
<!-- 							<a id="delall" onclick="doToolbar(this.id)" class="easyui-linkbutton" href="javascript:void(0);" data-options="plain:true,iconCls:'icon-no'">全部删除</a> -->
	<!-- 						<a id="copy" onclick="doToolbar(this.id)" class="easyui-linkbutton" href="javascript:void(0);" data-options="plain:true,iconCls:'icon-begin'">复制</a> -->
						</td>
					</tr>
				</table>
			</div>
			<div id="grid" style="width:100%;">
				<table id='list' width="100%" >
				</table>
			</div>
		</div>
		
		<div id="win" style="overflow:hidden;">
			<div class="easyui-layout" style="width:100%; height:100%;">
			<form id='fm' method='post' style="">
			 	<div region="center" >
				<table class = "tablestyle" width="100%">
					<tr>
						<td colspan=2>
						<a href="#" class="easyui-linkbutton" id="save" name="save" iconCls="icon-save" plain="true" onClick="doToolbar(this.id)">保存</a>
						</td>
					</tr>
					<tr>
						<td class="titlestyle" id="GS_KCLXN">课程类型<span style="color:red;"> * </span></td>
						<td id="CH_KCLX">
							<select name="GS_KCLX" id="GS_KCLX" class="easyui-combobox" panelHeight="auto" style="width:250px;">
							</select>				
						</td>
					</tr>
					<tr>
						<td class="titlestyle">学科编号</td>
						<td id="XKBH">
						</td>
					</tr>
					<tr>
						<td class="titlestyle">学科名称<span style="color:red;"> * </span></td>
						<td id="CH_KCDM">							
							<select name="GS_KCMC" id="GS_KCMC" class="easyui-combobox" panelHeight="auto" style="width:250px;" >
							</select>
						</td>
					</tr>
					<tr>
						<td class="titlestyle">任课教师<span style="color:red;"> * </span></td>
						<td onclick="opentsc();">
							<input name="GS_SKJSXM" id="GS_SKJSXM" class="easyui-textbox" style="width:250px;" readonly="true"/>
						</td>
					</tr>
					<tr>
						<td class="titlestyle">节数<span style="color:red;"> * </span></td>
						<td>						
							<input name="GS_JS" id="GS_JS" class="easyui-numberspinner" style="width:250px;" min="0" increment="1" precision="0" editable="true"  />
						</td>
					</tr>
					<tr>
						<td class="titlestyle">连节<span style="color:red;"> * </span></td>
						<td>						
							<input name="GS_LJ" id="GS_LJ" class="easyui-numberspinner" style="width:250px;" min="1" max="4" increment="1" precision="0" editable="true" />
						</td>
					</tr>
					<tr>
						<td class="titlestyle">连次<span style="color:red;"> * </span></td>
						<td>
							<input name="GS_LC" id="GS_LC" class="easyui-numberspinner" style="width:250px;" min="0" increment="1" precision="0" editable="true" />
						</td>
					</tr>
					<tr>
						<td class="titlestyle">授课周次<span style="color:red;"> * </span></td>
						<td onclick="opentsc();">					
							<input name="GS_SKZC" id="GS_SKZC" class="easyui-textbox" style="width:250px;" readonly="true"/>
						</td>
					</tr>	
					<tr>
						<td class="titlestyle">场地要求<span style="color:red;"> * </span></td>
						<td onclick="opentsc();">	
							<input name="GS_CDYQ" id="GS_CDYQ" class="easyui-textbox" style="width:250px;" readonly="true"/>
						</td>
					</tr>
					<tr>
						<td class="titlestyle" >考试形式<span style="color:red;"> * </span></td>
						<td id="CH_KSXS">			
							<select name="GS_KSXS" id="GS_KSXS" class="easyui-combobox" panelHeight="auto" style="width:250px;">
							</select>
						</td>
					</tr>
					<tr>
						<td class="titlestyle">学分<span style="color:red;"> * </span></td>
						<td>				
							<input name="GS_XF" id="GS_XF" class="easyui-textbox" style="width:250px;" />
						</td>
					</tr>
					<tr>
						<td class="titlestyle">总课时<span style="color:red;"> * </span></td>
						<td>						
							<input name="GS_ZKS" id="GS_ZKS" class="easyui-textbox" style="width:250px;" />
						</td>
					</tr>
					<tr>
						<td class="titlestyle" id="GS_KCLXF">是否考试<span style="color:red;"> * </span></td>
						<td id="CH_KCLXF">				
							<select name="GS_SFKS" id="GS_SFKS" class="easyui-combobox">
							</select>
						</td>
					</tr>
<!-- 					<tr> -->
<!-- 						<td class="titlestyle">学年学期</td> -->
<!-- 						<td> -->
<!-- 							<select id="GS_XNXQ2" name="GS_XNXQ2" class="easyui-combobox" panelHeight="auto" style="width:250px;"></select> -->
<!-- 						</td> -->
<!-- 					</tr> -->
				</table>
				
				<!-- 隐藏属性,传参用 -->
				<input type="hidden" id="active" name="active" />
				<input type="hidden" id="useType" name="useType" />
				<input type="hidden" id="GS_SKJHMXBH" name="GS_SKJHMXBH" /><!-- 授课计划明细编号 -->
				<input type="hidden" id="GS_XZBDM" name="GS_XZBDM" />
				<input type="hidden" id="GS_XNXQBM" name="GS_XNXQBM" />
				<input type="hidden" id="GS_KCDM" name="GS_KCDM" />
				<input type="hidden" id="GS_KCMCDM" name="GS_KCMCDM" />
				<input type="hidden" id="GS_SKJSBH" name="GS_SKJSBH" /><!-- 授课教师编号 -->
				<input type="hidden" id="GS_CDMC" name="GS_CDMC" /><!-- 场地名称（存的场地要求）；GS_CDYQ存的场地名称 -->
				<input type="hidden" id="GS_SKZCXQ" name="GS_SKZCXQ" /><!-- 授课周次详情（存的授课周次）；GS_SKZC存的授课周次详情 -->
				<input type="hidden" id="iUSERCODE" name="iUSERCODE" />
				</div>
			</form>
			</div>
		</div>
		
		<div id="tsc" style="overflow-x:hidden;">
			<form id='fm2' method='post' style="margin: 0px;">
				<table id="tsctable" class = "tablestyle" width="566px">
				</table>
				<!-- 隐藏属性,传参用 -->
<!-- 				<input type="hidden" id="active" name="active" /> -->
			</form>
		</div>
		
		<div id="teacher" style="overflow:hidden;">
			<div class="easyui-layout" style="width:100%; height:100%;">
			<div id="teainput" style="height:56px;">
				<table width="666px" class="tablestyle">
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
<!-- 					<td width="12%" class="titlestyle">教师类别</td> -->
<!-- 					<td width="16%"><select name="ic_teaLevel" id="ic_teaLevel" class="easyui-combobox" style="width:100%" panelHeight="auto"> -->
<!-- 					</select></td>							 -->
					<td align="center" style='width:12%'>
						<a href="#" class="easyui-linkbutton" id="search" name="search" iconCls="icon-search" plain="true" onClick="doToolbar(this.id)">查询</a>
					</td>					
				</tr>
				</table>
			</div>
			<div style="">
				<table id="teatable" class = "tablestyle" width="566px">
				</table>
			</div>
			</div>
		</div>
		
		<div id="week" style="overflow-x:hidden;">
			<div class="easyui-layout" style="width:100%; height:100%;">
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
		</div>
		
		<div id="room" style="overflow:hidden;">
			<div class="easyui-layout" style="width:100%; height:100%;">
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
		</div>
		
		<!-- 下载excel -->
		<iframe id="exportIframe" src="" style="width:0; height:0;"></iframe>
		
		<!-- 2017/12/7翟旭超加 -->
	<div id="test"  class="easyui-window" style="width:695px;height:350px;">
		<iframe id="xiafaxiada" name="xiafaxiada" src='http://180.166.123.122:8087/HRM/form/param/TeaTree1.jsp?u=1' style="width:680px;height:310px;"  frameborder="0"></iframe>
	</div>
</body>
	<script type="text/javascript">
		var iUSERCODE="<%=MyTools.getSessionUserCode(request)%>";
		var classId = "";//课程号
		var xqbh=""; //学期编号
		var weeks="";//上课周次
		var weekall="";//总周次
		var parentId = "";//父节点
		var xnxq = "";//学年学期下拉框数据
		var jxxz = "";//教学性质下拉框数据
		var xnxqVal = "";//当前学年学期数据
		var jxxzVal = "";//当前教学性质数据
		var pageNum = 1;   //datagrid初始当前页数
		var pageSize = 20; //datagrid初始页内行数
		var tempNodeTarget="";

		var sAuth="<%=sAuth%>";  //角色权限
		var iKeyCode = "";//授课计划明细编号
		var xkmc = "";//学科名称下拉框数据
		var rkjs = "";//任课教师下拉框数据
		var ksxs = "";//考试形式下拉框数据
		var kcmc = "";//课程名称
		var jsxm = "";//教师姓名

		var skjsbh = "";
		var xkdm = "";
		var lastIndex = -1;//使datagrid选中行取消，以便下次选择从新开始
		var saveType = "";//判断打开窗口的操作（new or edit）
		var roomnum=0;//校区建筑物数量
		var tscnum=0;//授课信息计数
		var weeksel=0;//选择周次模式
		var teainfoidarray =new Array();//存放的任课教师编号
		var teainfoarray =new Array();//存放选择的任课教师姓名
		var clsinfoidarray =new Array();//存放选择的场地编号
		var clsinfoarray =new Array();//存放选择的场地名称
		var weekArray="";//保存当前窗口第几周是否被选中
		var weekAllArray="";//保存所有窗口周次是否已经被选择
		var linkArray="";//判断周次是否连续
		var inputid="";//点击开的input框id
		var teaidarray =new Array();//存放教师编号
		var teaarray =new Array();//存放教师
		var wekidarray =new Array();//存放周次编号
		var wekarray =new Array();//存放周次
		var clsidarray =new Array();//存放场地编号
		var clsarray =new Array();//存放场地
		var roomsel=2;//选择教室模式
		var courseid="";//学科编号
		var coursename="";//学科名称
		var teacherbh="";//授课教师编号
		var teacherxm="";//授课教师姓名
		var teachweek="";//授课周次
		var teachinfo="";//授课周次详情
		var spaceyq="";//场地要求
		var spacemc="";//场地名称
		var kclx="";//课程类型
		var xuefen="";//学分
		var zongks="";//总课时
		var savexqbh="";//保存传递过来的学期编码
		var paike="0";//排课状态
		var js="";//节数
		var lj="";//连节
		var lc="";//连次
		var sfks="";//是否考试
		var xnxqgrid="";//学年学期
		var teabh="";//选中教师
		var rombh="";//取input框内的教室编号
		var isLoad = true;//判断datagrid是否处于加载状态
		var clsroomlen=-1;
		
		var ids = [];
		var names = [];
		var aa="";
		
		$(document).ready(function(){
			var OsObject = getOs();			//alert("您的浏览器类型为:"+getOs());
			loadXQWEEK();//初始化下拉框	
			loadDialog();
			checkChoose();	
			
			$('#tsc').hide();
			$('#teacher').hide();
			$('#week').hide();
			$('#room').hide();
			
			//2017/12/7翟旭超加
			$('#test').window('close');//初始化关闭人员窗口
			
			/*
			if(sAuth.indexOf("01")<0 && sAuth.indexOf("J5")<0){
				$('#delete').hide();
				$('#delall').hide();
			}
			*/
			
			if(getOs()=="MSIE"){
				if(navigator.appName == "Microsoft Internet Explorer" && (navigator.appVersion.match(/8./i)=="8."||navigator.appVersion.match(/7./i)=="7.")){ 
					
				}else if(navigator.appName == "Microsoft Internet Explorer" && (navigator.appVersion.match(/9./i)=="9."||navigator.appVersion.match(/10./i)=="10."||navigator.appVersion.match(/11./i)=="11.")){ 
					
				}else{
					
				} 
			}else if(getOs()=="Firefox"){
							
			}else if(getOs()=="Safari"){
							
			}else{
							
			}
			
			//设置百分比微调器值改变时，datagrid也随之改变-王孟梅-2016年7月16日17:01:29  
			$('#GS_JS').numberspinner({  
    			"onChange": function changePer(newValue,oldValue){		
    				var xf=newValue;
					var skzctext=$('#GS_SKZC').textbox('getValue');
					if(skzctext==""){
					
					}else{
						//计算周数
						var zs=0;
						var skzctext2=skzctext.split("&");
						for(var s=0;s<skzctext2.length;s++){
							if(skzctext2[s].indexOf("#")>-1){
								var skzcnum=skzctext2[s].split("#");
								zs=skzcnum.length;
							}else if(skzctext2[s].indexOf("-")>-1){
								var skzc2=skzctext2[s];
								var skzcnum=skzc2.split("-");
								zs=skzcnum[1]-skzcnum[0]+1;
							}else{
								zs+=1;
							}
						}
						var zks=newValue*zs;
						$('#GS_XF').textbox('setValue',xf);
						$('#GS_ZKS').textbox('setValue',zks);
					}
				}
			});
		});  
			
		//获取学年学期combobox，学期周数
		function loadXQWEEK(){
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_Skjh',
				data : 'active=initData&page=' + pageNum + '&rows=' + pageSize+'&iUSERCODE='+iUSERCODE,
				dataType:"json",
				success : function(data) {
					xnxq = data[0].xnxqData;//获取学年学期下拉框数据
					jxxz = data[0].jxxzData;//获取教学性质下拉框数据
					xnxqCombobox(xnxq,jxxz);
					loadData("","");
					loadSFKSCombo();
				}
			});
		}
		
		//加载下拉框数据
		function xnxqCombobox(xnxq,jxxz){
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
						xnxqVal=data[0].comboValue;
					}
				},
				//下拉列表值改变事件
				onChange:function(data){
					doToolbar("que");
				}
			});
			
			$('#JXXZ').combobox({
				data:jxxz,
				valueField:'comboValue',
				textField:'comboName',
				editable:false,
				panelHeight:'140', //combobox高度
				onLoadSuccess:function(data){
					//判断data参数是否为空
					if(data != ''){
						//初始化combobox时赋值
						$(this).combobox('setValue', data[0].comboValue);
						jxxzVal=data[0].comboValue;
					}
				},
				//下拉列表值改变事件
				onChange:function(data){}
			});
			
		}
		
		//查询本学期实际上课周数
		function loadData(xnxqVal,jxxzVal){
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_Skjh',
				data : 'active=query&page=' + pageNum + '&rows=' + pageSize + '&iUSERCODE='+iUSERCODE+
					'&JXXZ=' + jxxzVal + '&XNXQ=' + xnxqVal + '&termid='+(xnxqVal+jxxzVal),
				dataType:"json",
				asyn:false,
				success : function(data) {
					weeks=data[0].MSG;//上课周次
					weekall=data[0].MSG2;//总周次
					initData(weeks,weekall);//页面初始化加载数据
					treegrid();//加载特殊规则列表
				}
			});
		}
		
		//页面初始化加载数据
		function initData(weeks,weekall){
			weekArray = new Array(weekall);//保存当前窗口第几周是否被选中
			weekAllArray = new Array(weekall);//保存所有窗口周次是否已经被选择
			linkArray = new Array(weekall);//判断周次是否连续
			for(var i=0;i<weekall;i++){
				weekArray[i]=0;
				weekAllArray[i]=0;
			}
			var gridheight=document.body.clientHeight-32;
			$('#grid').attr('style','width:100%;height:'+gridheight+'px');
			initGridData("",xnxqVal+jxxzVal);//页面初始化加载数据
		}
		
		function treegrid(){
			$('#classTree').tree({
				checkbox: false,
				url:'<%=request.getContextPath()%>/Svl_Skjh?active=queryTree&level=0'+'&AUTH='+sAuth+'&parentId='+'&iUSERCODE='+iUSERCODE+'&GS_XNXQBM='+(xnxqVal+jxxzVal),
				onClick:function(node){
					//判断点击的是不是班级
			    	if($('#classTree').tree('getParent', node.target) != null){
			    		if(lastIndex!=node.id){
							classId=node.id;
							lastIndex=node.id;
							xqbh=xnxqVal+jxxzVal;
							initGridData(classId,xqbh);//点击后刷新right页面取值结果	
						}
						parentId=node.id;
			    	}else{
			    		initGridData("",xnxqVal+jxxzVal);//页面初始化加载数据
			    		parentId="";
			    		lastIndex="";
			    	}					
				},
//  				onBeforeLoad:function(row,param){     //分层显示treegrid
// 					$('#classTree').tree('options').url='< %=request.getContextPath()%>/Svl_Skjh?active=queryTree&AUTH='+sAuth+'&parentId='+'&iUSERCODE='+iUSERCODE+'&GS_XNXQBM='+(xnxqVal+jxxzVal);
//  				},
				onBeforeExpand:function(node,param){
			  		$('#classTree').tree('options').url='<%=request.getContextPath()%>/Svl_Skjh?active=queryTree&level=1'+'&AUTH='+sAuth+'&parentId='+node.id+'&iUSERCODE='+iUSERCODE+'&GS_XNXQBM='+(xnxqVal+jxxzVal);
				},
				onLoadSuccess:function(data){
					$('#classTree').show();
					xnxqVal = $('#XNXQ').combobox('getValue');
					jxxzVal = $('#JXXZ').combobox('getValue');
					
			    }
			});
		}
			
		
		//显示对话操作框
		function showDialog(src, title) {
				$('#iframe').attr("src", src);
				$('#showDialog').dialog({
					title:title
	//				title:'对话框',//对话框的标题
	//				collapsible:true,//定义是否显示可折叠按钮
	//				minimizable:true,//定义是否显示最小化按钮
	//				maximizable:true//定义是否显示最大化按钮
	//				resizable:true,	//定义对话框是否可编辑大小
				});
		}
		
		function showMSG(msg) {
			$('#showDialog').dialog("close");
			alertMsg(msg);	
		}
		
		function show1() {
			$.messager.alert('提示',"请添加文件!");
		}
		function show2() {
			$.messager.alert('提示',"很抱歉!只能导入Excel类型的文件!");
		}
		function show3() {
			$.messager.alert('提示',"请选择要导入的学期!");
		}
		function show4() {
			$.messager.alert('提示',"请选择要导入的专业!");
		}
		
		//打开teacher编辑窗口
		/* function openImport(){
			$('#importfile').show();
			$('#importfile').dialog({   
				title: '导入',   
				width: 250,//宽度设置   
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
			$('#importfile').dialog("open");
		}

		//上传导入excel表格
		function doForm(){
			$('#form1').submit();	
		}
		
		$('#form1').form({
			//定位到servlet位置的url
			url:'/ISS/Svl_Import',
			//当点击事件后触发的事件
		    onSubmit: function(data){ 
		    }, 
		    //当点击事件并成功提交后触发的事件
		    success:function(data){
		     	var json = eval("("+data+")");

					
			}
		}); */

		
		function myKeyDown(){
			var k=window.event.keyCode;
			if (8 == k){
				event.keyCode=0;//取消按键操作
			}
		}		
		
		//判断浏览器类型
		function getOs(){  
			   
			   if(navigator.userAgent.indexOf("MSIE")>0) {  
			        return "MSIE";  
			   }  
			   if(isFirefox=navigator.userAgent.indexOf("Firefox")>0){  
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
		
		//页面初始化加载数据
		function initGridData(classId,xqbh){ 
			savexqbh=xqbh;
			$("#GS_XNXQBM").val(savexqbh);	
			teainfoidarray.splice(0,clsinfoidarray.length);
			teainfoarray.splice(0,clsinfoarray.length);
			clsinfoidarray.splice(0,clsinfoidarray.length);
			clsinfoarray.splice(0,clsinfoarray.length);
			checkpaike();
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_Skjh',
				data : 'active=initGridData&page=' + pageNum + '&rows=' + pageSize + '&XZBDM=' + classId + '&GS_XNXQBM=' + xqbh,
				dataType:"json",
				success : function(data) {
					xkmc = data[0].xkmcData;//获取学科名称下拉框数据
					kslx = data[0].ksxsData;//获取考试形式下拉框数据
					loadGrid(data[0].listData);//加载特殊规则列表
					initCombobox(xkmc,kslx);//初始化下拉框
				}
			});
			
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
		
		
		function loadGrid(listData){
			$('#list').datagrid({
				data:listData,
				loadMsg : "信息加载中请稍后!",//载入时信息
				width : '100%',//宽度
				height : '100%',//高度
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
					{field:'课程名称',title:'学科名称',width:fillsize(0.15)},
					{field:'GS_SKJSXM',title:'任课教师',width:fillsize(0.12)},
					{field:'GS_JS',title:'节数',width:fillsize(0.05)},
					{field:'GS_LJ',title:'连节',width:fillsize(0.05)},
					{field:'GS_LC',title:'连次',width:fillsize(0.05)},
					{field:'GS_CDYQ',title:'场地名称',width:fillsize(0.16)},
					{field:'GS_SKZC',title:'授课周次',width:fillsize(0.08),
						formatter:function(value,rec){
							var skzcxq2=rec.GS_SKZC;
							skzcxq2=skzcxq2.replace(new RegExp('odd','gm'),'单周');
							skzcxq2=skzcxq2.replace(new RegExp('even','gm'),'双周');
							return skzcxq2;
						}
					},
					{field:'GS_KSXS',title:'考试形式',width:fillsize(0.08)},
					{field:'学分',title:'学分',width:fillsize(0.05)},
					{field:'总课时',title:'总课时',width:fillsize(0.05)}
				]],
				onClickRow:function(rowIndex, rowData){
					lastIndex = rowIndex;
					iKeyCode = rowData.授课计划明细编号;
					iKeyCodeZb = rowData.授课计划主表编号;
					row=rowData;
					courseid=row.GS_KCMC;//课程代码
					coursename=row.课程名称;//课程名称
					ksxs=rowData.考试形式编号;//考试形式
					if(ksxs==""||ksxs==undefined){
						ksxs=0;
					}
					teacherbh=rowData.GS_SKJSBH;//授课教师编号
					teacherxm=rowData.GS_SKJSXM;//授课教师姓名
					teachweek=rowData.GS_SKZCXQ;//授课周次
					teachinfo=rowData.GS_SKZC;//授课周次详情
					spaceyq=rowData.GS_CDMC;//场地要求
					spacemc=rowData.GS_CDYQ;//场地名称
					kclx=rowData.GS_KCLX;//课程类型
					xuefen=rowData.学分;//学分
					zongks=rowData.总课时;//总课时
					js=rowData.GS_JS;//节数
					lj=rowData.GS_LJ;//连节
					lc=rowData.GS_LC;//连次
					sfks=rowData.是否考试;//是否考试
					xnxqgrid=rowData.学年学期编码;//学年学期编码
					$("#GS_SKZCXQ").val(teachweek);
				},
				onLoadSuccess: function(data){
					//判断是否可以编辑授课计划
					if(parentId==""){
						$('#new').linkbutton('disable');
						$('#edit').linkbutton('disable');
						$('#delete').linkbutton('disable');
						$('#delall').linkbutton('disable');
					}else{
						if(paike=="0"){
							$('#new').linkbutton('enable');
							$('#edit').linkbutton('enable');
							$('#delete').linkbutton('enable');
							if(classId==""){
								$('#delall').linkbutton('disable');
							}else{
								$('#delall').linkbutton('enable');
							}
						}else{
							$('#new').linkbutton('disable');
							$('#edit').linkbutton('enable');
							$('#delete').linkbutton('disable');
							$('#delall').linkbutton('disable');
						}
					}
					//iKeyCode="";
					$('#divPageMask2').hide();
				},
				onLoadError:function(none){
					
				}
			});
			
// 			$("#list").datagrid("getPager").pagination({ 
// 				total:listData.total, 
// 				onSelectPage:function (pageNo, pageSize_1) { 
// 					pageNum = pageNo;
// 					pageSize = pageSize_1;
// 					loadData(listData);
// 				} 
// 			});
		}
		
		//加载下拉框数据
		function initCombobox(xkmc,kslx){
			//加载下拉框数据
			$('#GS_KSXS').combobox({
				data:kslx,
				valueField:'comboValue',
				textField:'comboName',
				editable:false,
				panelHeight:'140', //combobox高度
				onLoadSuccess:function(data){
					//判断data参数是否为空
					if(data != ''){
						//初始化combobox时赋值
						$(this).combobox('setValue', '0');
					}
				}
			});	
			
// 			$('#GS_SKJSXM').combobox({
// 				data:rkjs,
// 				valueField:'comboValue',
// 				textField:'comboName',
// 				editable:false,
// 				panelHeight:'140', //combobox高度
// 				onLoadSuccess:function(data){
					//判断data参数是否为空
// 					if(data != ''){
						//初始化combobox时赋值
// 						$(this).combobox('setValue', '');
// 					}
// 				},
				//下拉列表值改变事件
// 				onChange:function(data){
				
// 				}
// 			});
		}
		
		//工具按钮
		function doToolbar(iToolbar){
			//查询
			if(iToolbar == 'que'){
				xnxqVal = $('#XNXQ').combobox('getValue');
				jxxzVal = $('#JXXZ').combobox('getValue');
				savexqbh=xnxqVal+jxxzVal;
				$('#List').treegrid("unselectRow", lastIndex);	
				classId="";
				lastIndex="";
				treegrid();
				initGridData("",xnxqVal+jxxzVal);//页面初始化加载数据
			}
			//导入
			if(iToolbar == 'import'){
				var url=""; 
	   			url=encodeURI(url); //用了2次encodeURI 
				showDialog("<%=request.getContextPath()%>/form/ruleManage/uploadImport.jsp?weeks="+weeks,"批量导入培养计划");
			}
			//新建
			if(iToolbar == "new"){ 
				saveType = 'new';
				$('#win').dialog({   
					title: '新建授课计划信息'
				});
				$('#win').dialog("open");
				$.parser.parse(('#win'));
		
				$('#CH_KCDM').html('<select name="GS_KCMC" id="GS_KCMC" class="easyui-combobox" panelHeight="auto" style="width:250px;" ></select>');
				$.parser.parse(('#CH_KCDM'));
				initCombobox(xkmc,kslx);
				
				emptyDialog();
				$('#GS_KCLX').combobox("enable");//课程类型			
			}
			if(iToolbar == "edit"){
				if(iKeyCode==""){
					alertMsg("请选择一行数据");
					return;
				}else{
					$('#win').dialog({   
						title: '编辑授课计划信息'
					});
					$('#win').dialog("open");
	
					if(row!=undefined && row!=''){
						$('#fm').form('load', row);
						$('#XKBH').html(row.GS_KCMC);
						var skzcweek=row.GS_SKZC;
						skzcweek=skzcweek.replace("odd","单周").replace("even","双周");
						$('#GS_SKZC').textbox("setValue",skzcweek);
					}
					
					$('#XKBH').html(courseid);
					$('#GS_KCMCDM').val(courseid);//保存的课程代码
					
					$('#CH_KCDM').html('<input name="GS_KCMC" id="GS_KCMC" class="easyui-textbox" style="width:250px;"/>');
					$('#GS_KCMC').val(coursename);//课程名称
					$.parser.parse(('#CH_KCDM'));
					
					$('#GS_SKJSXM').val(teacherxm);//授课教师姓名
					$('#GS_SKZC').val(teachinfo);//授课周次
					$('#GS_CDYQ').val(spacemc);//场地要求
					$('#GS_KCLX').combobox('setValue',kclx);//课程类型
				
					$('#GS_KSXS').combobox('setValue',ksxs);//考试形式	
					$('#GS_XF').textbox('setValue',xuefen);//学分
					$('#GS_ZKS').textbox('setValue',zongks);//总课时	
					$('#GS_SFKS').combobox('setValue',sfks);//是否考试
					
					teaidarray.splice(0,teaarray.length);
					teaarray.splice(0,teaarray.length);
					wekidarray.splice(0,wekarray.length);
					wekarray.splice(0,wekarray.length);
					clsidarray.splice(0,clsarray.length);	
					clsarray.splice(0,clsarray.length);	
					
					var info1=teacherbh.split("&");
					var info2=teacherxm.split("&");
					var info3=teachinfo.split("&");
					var info4=teachinfo.split("&");
					var info5=spaceyq.split("&");
					var info6=spacemc.split("&");
					for(var i=0;i<info1.length;i++){
						teaidarray[i]=info1[i];//授课教师编号
						teaarray[i]=info2[i];//授课教师姓名
						wekidarray[i]=info3[i];//授课周次
						wekarray[i]=info4[i];//授课周次详情
						clsidarray[i]=info5[i];//场地要求
						clsarray[i]=info6[i];//场地名称
					}
					
					$("#GS_SKJSBH").val(teacherbh);//授课教师编号
					$("#GS_SKZCXQ").val(teachweek);//授课周次
					$("#GS_CDMC").val(spaceyq);//场地要求
					
					if(paike=="0"){
						saveType = "edit";
						//允许修改项
						$('#GS_KCMC').textbox("enable");//课程名称
						$('#GS_SKJSXM').textbox("enable");//授课教师姓名
						$('#GS_SKZC').textbox("enable");//授课周次
						$('#GS_CDYQ').textbox("enable");//场地要求
						$('#GS_KCLX').combobox("disable");//课程类型				
						$('#GS_JS').numberspinner("enable");//节数
						$('#GS_LJ').numberspinner("enable");//连节
						$('#GS_LC').numberspinner("enable");;//连次
						$('#GS_KSXS').combobox("enable");//考试形式
						$('#GS_SFKS').combobox("enable");//是否考试
					}else{
						$('#GS_KCMC').textbox("disable");//课程名称
						$('#GS_SKJSXM').textbox("disable");//授课教师姓名
						$('#GS_SKZC').textbox("disable");//授课周次
						$('#GS_CDYQ').textbox("disable");//场地要求
						$('#GS_KCLX').combobox("disable");//课程类型				
						$('#GS_JS').numberspinner("disable");//节数
						$('#GS_LJ').numberspinner("disable");//连节
						$('#GS_LC').numberspinner("disable");;//连次
					}
					
				}
			}
			//保存授课计划
			if(iToolbar == "save"){		
				$("#GS_XZBDM").val(classId);
				$("#GS_XNXQBM").val(savexqbh);
				if(saveType == "new"){
					$("#GS_KCDM").val($('#GS_KCMC').combobox('getText'));
					$("#useType").val("new");
				}else if(saveType == "edit"){
					$("#GS_KCDM").val($('#GS_KCMC').val());
					$("#useType").val("edit");
				}else{
					$("#GS_KCDM").val($('#GS_KCMC').val());
					$("#useType").val("specialEdit");
				}
			
				//判断公共课是否确认，如已确认不允许修改公共课授课计划
				if(kclx=="01"&&saveType!="specialEdit"){
					$.ajax({
					   type: "POST",
					   url: '<%=request.getContextPath()%>/Svl_Skjh',
					   data: "active=checkGGK&GS_XNXQBM=" + savexqbh ,
					   dataType: 'json',
					   success: function(datas){
		                    var data = datas[0];
							if(datas[0].MSG=="0"){//公共课未确认
								$("#active").val(iToolbar);
								if($('#GS_KCLX').combobox('getValue')=="00"){
									alertMsg("请选择课程类型");
									return;
								}
								if(saveType == 'new'){
									$('#GS_SKJHMXBH').val('');//主键
									if($('#GS_KCMC').combobox('getValue')==undefined || $('#GS_KCMC').combobox('getValue')==''){
										alertMsg("请选择学科名称");
										return;
									}
								}else{
									$('#GS_SKJHMXBH').val(iKeyCode);//主键
									if($('#GS_KCMC').val()==""){
										alertMsg("请输入学科名称");
										return;
									}				
								}
											
								if($('#GS_SKJSXM').val()==""||$('#GS_SKJSXM').val()=="请选择"){
									alertMsg("请选择任课教师");
									return;
								}
								if($('#GS_LJ').val()==0){
									alertMsg("连节至少为1");
									return;
								}
								if($('#GS_LJ').val()>4){
									alertMsg("连节最大为4");
									return;
								}
											
								if(parseInt($('#GS_LJ').val())>parseInt($('#GS_JS').val())){
									alertMsg("连节不能大于节数");
									return;
								}
								if(parseInt($('#GS_LC').val())>(parseInt($('#GS_JS').val())/(parseInt($('#GS_LJ').val())))){
									alertMsg("连次数量过多");
									return;
								}
								if($('#GS_KSXS').combobox('getValue')=="0"||$('#GS_KSXS').combobox('getValue')==""){
									alertMsg("请选择考试形式");
									return;
								}
								if($('#GS_XF').textbox('getValue')==""){
									alertMsg("请填写学分");
									return;
								}
								if($('#GS_XF').textbox('getValue')=="0"){
									alertMsg("学分不能为0");
									return;
								}		
								if(saveType=="new"){
									check();
								}else{
									$.ajax({
										type: "POST",
									   	url: '<%=request.getContextPath()%>/Svl_Skjh',
										data: "active=checkChangeSKJH&GS_SKJHMXBH=" + iKeyCode ,
										dataType: 'json',
										success: function(datas){
									    	var data = datas[0];
											if(datas[0].MSG=="0"){
												check();
											}else{	
												var editKCMC=$('#GS_KCMC').val();
												var editJSXM=$('#GS_SKJSXM').val();
												var editSKZC=$('#GS_SKZC').val();
												var editCDMC=$('#GS_CDYQ').val();	
												var editKCLX=$('#GS_KCLX').combobox('getValue');
												var editJS=$('#GS_JS').val();
												var editLJ=$('#GS_LJ').val();
												var editLC=$('#GS_LC').val();
												//授课计划修改不影响排课表
												if(editKCMC==coursename&&editJSXM==teacherxm&&editSKZC==teachinfo&editCDMC==spacemc&&editKCLX==kclx&&editJS==js&&editLJ==lj&&editLC==lc){
													check();
												}else{//授课计划修改影响排课表,删除相关信息
													ConfirmMsg("保存后将删除该授课计划已设置的固排信息与合班信息，需要重新配置，是否继续？", "editCD();","");
												}
											}				
										}
									});			
								}
							}else{
								alertMsg("公共课已确认，不能修改公共课授课计划");
								return;
							}	
					   }
					});
				}else{			
							
					$("#active").val(iToolbar);
					if($('#GS_KCLX').combobox('getValue')=="00"){
						alertMsg("请选择课程类型");
						return;
					}
					if(saveType == 'new'){
						$('#GS_SKJHMXBH').val('');//主键
						if($('#GS_KCMC').combobox('getValue')==undefined || $('#GS_KCMC').combobox('getValue')==''){
							alertMsg("请选择学科名称");
							return;
						}
					}else{
						$('#GS_SKJHMXBH').val(iKeyCode);//主键
						if($('#GS_KCMC').val()==""){
							alertMsg("请输入学科名称");
							return;
						}				
					}
								
					if($('#GS_SKJSXM').val()==""||$('#GS_SKJSXM').val()=="请选择"){
						alertMsg("请选择任课教师");
						return;
					}
					if($('#GS_LJ').val()==0){
						alertMsg("连节至少为1");
						return;
					}
					if($('#GS_LJ').val()>4){
						alertMsg("连节最大为4");
						return;
					}
								
					if(parseInt($('#GS_LJ').val())>parseInt($('#GS_JS').val())){
						alertMsg("连节不能大于节数");
						return;
					}
					if(parseInt($('#GS_LC').val())>(parseInt($('#GS_JS').val())/(parseInt($('#GS_LJ').val())))){
						alertMsg("连次数量过多");
						return;
					}
					if($('#GS_KSXS').combobox('getValue')=="0"||$('#GS_KSXS').combobox('getValue')==""){
						alertMsg("请选择考试形式");
						return;
					}
					if($('#GS_XF').textbox('getValue')==""){
						alertMsg("请填写学分");
						return;
					}
					if($('#GS_XF').textbox('getValue')=="0"){
						alertMsg("学分不能为0");
						return;
					}		
					if(saveType=="new"){
						check();
					}else{
						$.ajax({
							type: "POST",
						   	url: '<%=request.getContextPath()%>/Svl_Skjh',
							data: "active=checkChangeSKJH&GS_SKJHMXBH=" + iKeyCode ,
							dataType: 'json',
							success: function(datas){
						    	var data = datas[0];
								if(datas[0].MSG=="0"){
									check();
								}else{	
									var editKCMC=$('#GS_KCMC').val();
									var editJSXM=$('#GS_SKJSXM').val();
									var editSKZC=$('#GS_SKZC').val();
									var editCDMC=$('#GS_CDYQ').val();	
									var editKCLX=$('#GS_KCLX').combobox('getValue');
									var editJS=$('#GS_JS').val();
									var editLJ=$('#GS_LJ').val();
									var editLC=$('#GS_LC').val();
									//授课计划修改不影响排课表
									if(editKCMC==coursename&&editJSXM==teacherxm&&editSKZC==teachinfo&editCDMC==spacemc&&editKCLX==kclx&&editJS==js&&editLJ==lj&&editLC==lc){
										check();
									}else{//授课计划修改影响排课表,删除相关信息
										ConfirmMsg("保存后将删除该授课计划已设置的固排信息与合班信息，需要重新配置，是否继续？", "editCD();","");
									}
								}				
							}
						});			
					}
				}	
			}
			//添加一行授课信息
			if(iToolbar == "add"){
				tscnum++;
				var html='';
				aa="";
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
							'<a href="#" class="easyui-linkbutton" id="del'+tscnum+'" name="del" iconCls="icon-cancel" plain="true" onClick="delrow(this)">删除</a>'+
						'</td>'+
					'</tr>';
				$("#tsctable").append(html);
				//$.parser.parse(('#tsctable'));
			}
			if(iToolbar == "delete"){
				if(iKeyCode==""){
					alertMsg("请选择一行数据");
					return;
				}else{
					$.ajax({
						type: "POST",
						url: '<%=request.getContextPath()%>/Svl_Skjh',
						data: "active=checkDelSKJH&iKeyCode=" + iKeyCode,
						dataType: 'json',
						success: function(datas){
				            var data = datas[0];
							if(data.MSG=="0"){//没有排课信息
								ConfirmMsg("是否确定删除？", "delskjh()","");
							}else{
								ConfirmMsg("该操作会删除授课计划及相关的固排和排课信息，是否确定删除？", "delskjh()","");
							}
// 							else{
// 								alertMsg("该授课计划已经完成了排课，无法删除！");
// 							}
						}
					});	
				}
			}
			if(iToolbar == "delall"){
					$.ajax({
						type: "POST",
						url: '<%=request.getContextPath()%>/Svl_Skjh',
						data: "active=checkDelallSKJH&GS_XNXQBM=" + savexqbh + "&GS_XZBDM=" + classId,
						dataType: 'json',
						success: function(datas){
				            var data = datas[0];
							if(data.MSG=="0"){//没有排课信息
								ConfirmMsg("本次操作将删除当前学期该专业同一届所有班级的授课计划，固排和排课信息，是否确定？", "delallskjh("+savexqbh+","+classId+")","");
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
						if((teainfoidarray[i]=='000'&&teainfoarray[i]=='请选择')||teainfoidarray[i]==''||(teainfoidarray[i]+'')=='undefined'){
						
						}else{
							teas+=teainfoidarray[i];
							html+=teainfoarray[i];
						}
					}else{
						if((teainfoidarray[i]=='000'&&teainfoarray[i]=='请选择')||teainfoidarray[i]==''||(teainfoidarray[i]+'')=='undefined'){
							
						}else{
							teas+="+"+teainfoidarray[i];
							html+="+"+teainfoarray[i];
						}			
					}
				}
				teaidarray[(inputid.substring(7,inputid.length)-1)]=teas;//保存
				teaarray[(inputid.substring(7,inputid.length)-1)]=html;//保存
				
				$('#'+inputid).val(html);
				//teainfoidarray.splice(0,teainfoidarray.length);
				//teainfoarray.splice(0,teainfoarray.length);

				teainfoidarray=new Array();
				teainfoarray=new Array();

				$('#teacher').dialog("close");

			}
			if(iToolbar == "submit4"){//周次
				var html="";
				//if(weeksel==1){//自定义				
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
					
					wekidarray[(inputid.substring(7,inputid.length)-1)]=(html);//保存
					wekarray[(inputid.substring(7,inputid.length)-1)]=(html);//保存
				//}
// 				else if(weeksel==0){
// 					var obj = document.getElementsByName("selweek");
// 					for(var i=0;i<obj.length;i++){
// 						var checkbox=document.getElementById(obj[i].id);
// 						if(checkbox.checked){//选中
// 							if(obj[i].id=="singleweek"){
// 								html="单周";
// 								var weekmax=0;
// 								if(weeks%2==0){
// 									weekmax=weeks-1;
// 								}else{
// 									weekmax=weeks;
// 								}
// 								wekidarray[(inputid.substring(7,inputid.length)-1)]=("odd");//保存
// 								wekarray[(inputid.substring(7,inputid.length)-1)]=("odd");//保存
// 							}else if(obj[i].id=="doubleweek"){
// 								html="双周";
// 								var weekmax=0;
// 								if(weeks%2==0){
// 									weekmax=weeks;
// 								}else{
// 									weekmax=weeks-1;
// 								}
// 								wekidarray[(inputid.substring(7,inputid.length)-1)]=("even");//保存
// 								wekarray[(inputid.substring(7,inputid.length)-1)]=("even");//保存
// 							}else{
// 								html=obj[i].value;
// 								wekidarray[(inputid.substring(7,inputid.length)-1)]=obj[i].value;//保存
// 								wekarray[(inputid.substring(7,inputid.length)-1)]=obj[i].value;//保存	
// 							}
// 						}
// 					}
// 				}
				
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
							if(clsinfoidarray[i]==''||(clsinfoidarray[i]+'')=='undefined'){
						
							}else{
								roms+=clsinfoidarray[i];
								html+=clsinfoarray[i];
							}	
						}else{
							if(clsinfoidarray[i]==''||(clsinfoidarray[i]+'')=='undefined'){
						
							}else{
								roms+="+"+clsinfoidarray[i];
							    html+="+"+clsinfoarray[i];
							}
						}
					}
	
					clsidarray[(inputid.substring(7,inputid.length)-1)]=(roms);//保存
					clsarray[(inputid.substring(7,inputid.length)-1)]=(html);//保存
					clsinfoidarray.splice(0,clsinfoidarray.length);
					clsinfoarray.splice(0,clsinfoarray.length);
				}else{
				
				}
				
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
					if(($('#'+rObj[i].id).val()+'')==""){
						checkObj=1;
					}else{
						rkls+=$('#'+rObj[i].id).val()+"&";
					}
				}
				for (var j=0;j<sObj.length;j++) {
					if(($('#'+sObj[j].id).val()+'')==""){
						checkObj=1;
					}else{
						skzc+=$('#'+sObj[j].id).val()+"&";
					}
				}
				for (var k=0;k<cObj.length;k++) {
					if(($('#'+cObj[k].id).val()+'')==""){
						checkObj=1;
					}else{
						cdyq+=$('#'+cObj[k].id).val()+"&";
					}
				}
				rkls=rkls.substring(0,rkls.length-1);
				skzc=skzc.substring(0,skzc.length-1);
				cdyq=cdyq.substring(0,cdyq.length-1);
				if(checkObj==1){
					alertMsg("任课老师,授课周次,场地要求都不能为空");
				}else{//保存提交
					//取idarray编号
					var rkls2="";
					var skzc2="";
					var cdyq2="";
					var cdtag=0;
					var checklag=0;
								
					for(var i=0;i<teaidarray.length;i++){ 
						if((teaidarray[i]+'')!=""){
							rkls2+=teaidarray[i]+"&";
						}
					}	
					rkls2=rkls2.substring(0,rkls2.length-1);			
					//没改动过授课教师保存，取隐藏域的授课教师编号
					if(rkls2==""){
									
					}else{
						$("#GS_SKJSBH").val(rkls2);//授课教师编号
					}				 
						
					var checkweek = new Array(weekall);
					for(var i=0;i<weekall;i++){
						checkweek[i]=0;
					}
					
					for(var i=0;i<wekidarray.length;i++){ 
						skzc3="";
						if(wekidarray[i]=="odd"){
							for(var j=0;j<weekall;j++){
								if(j%2==0){
									checkweek[j]=1;
								}
							}
						}else if(wekidarray[i]=="even"){
							for(var j=0;j<weekall;j++){
								if(j%2!=0){
									checkweek[j]=1;
								}
							}
						}else if((wekidarray[i]+"").indexOf("#")>-1){
							var weeknum=wekidarray[i].split("#");
							for(var j=0;j<weeknum.length;j++){
								checkweek[parseInt(weeknum[j])-1]=1;
							}
						}else if((wekidarray[i]+"").indexOf("-")>-1){  
							var weeknum=wekidarray[i].split("-");
							for(var j=parseInt(weeknum[0]);j<=parseInt(weeknum[1]);j++){
								checkweek[j-1]=1;
							}
						}else if(wekidarray[i]==""){
							checklag=1;
						}else{
							checkweek[parseInt(wekidarray[i]-1)]=1;
						}				
					} 
					if(checklag==0){
						var min=0;
						var max=0;
						var tag=1;
						for(var k=0;k<weekall;k++){
							if(checkweek[k]==1){
								if(tag==1){
									min=k;
									max=k;
									tag=2;
								}else{
									max=k;
								}
							}
						}
						if(min==max){
							skzc2=(min+1);
						}else{
							for(var r=min;r<max;r++){
								if(checkweek[r]==1){//连续
									tag=3;
								}else{
									tag=4;
									break;
								}
							}
							if(tag==3){
								skzc2=(min+1)+"-"+(max+1);
							}else{
								for(var r=0;r<weekall;r++){
									if(checkweek[r]==1){
										skzc2+=(r+1)+"#";
									}				
								}
								skzc2=skzc2.substring(0,skzc2.length-1);
							}
						}
						checkweek.splice(0, checkweek.length);
					}
						
					//没改动过周次保存，取隐藏域的授课周次
					if(skzc2==""){
									
					}else{
						$("#GS_SKZCXQ").val(skzc2);				//授课周次详情（存的授课周次）；GS_SKZC存的授课周次详情
					}		
				
					//$("#GS_SKZC").val(	//授课周次详情 
				
					for(var k=0;k<clsidarray.length;k++){
						if((clsidarray[k]+'')!=""){
							cdyq2+=clsidarray[k]+"&";						
						}
					}
					cdyq2=cdyq2.substring(0,cdyq2.length-1);
					//没改动过场地要求保存，取隐藏域的场地要求
					if(cdyq2==""){
									
					}else{
						$("#GS_CDMC").val(cdyq2);					//场地名称（存的场地要求）；GS_CDYQ存的场地名称 
					}
										
					//$("#GS_CDYQ").val(	//场地名称 
					
					$('#GS_SKJSXM').textbox('setValue',rkls);
					$('#GS_SKZC').textbox('setValue',skzc);
					$('#GS_CDYQ').textbox('setValue',cdyq);
					$('#tsc').dialog("close");
				}
				
				//保存到teaidarray，teaarray
				
			}
			if(iToolbar == "search"){//场地
				loadGridTea();
			}
			if(iToolbar == "download"){//模板下载
				//var dlpath="D:/exportExcel/xj/2016电子商务专业(模板).xls"; 
				//$("#exportIframe").attr("src", '< %=request.getContextPath()%>/form/ruleManage/templetDownload.jsp?filePath=' + encodeURI(encodeURI(dlpath)));
			}
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
					initGridData(classId,savexqbh);	
					showMsg(data.MSG);	
				}
			});
		}
		
		//删除所有授课计划
		function delallskjh(savexqbh,classId){
			$.ajax({
				type: "POST",
				url: '<%=request.getContextPath()%>/Svl_Skjh',
				data: "active=deleteAllskjh&GS_XNXQBM=" + savexqbh + "&GS_XZBDM=" + classId,
				dataType: 'json',
				success: function(datas){
		            var data = datas[0];
					initGridData(classId,savexqbh);	
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
			skjsbhxm="";
			
			for(var i=0;i<teaidarray.length;i++){
				if(i==0){
					skjsbh+=teaidarray[i];
				}else{
					skjsbh+="|"+teaidarray[i];
				}
			}	
			
			for(var i=0;i<teaarray.length;i++){
				if(i==0){
					skjsbhxm+=teaarray[i];
				}else{
					skjsbhxm+="|"+teaarray[i];
				}
			}
			
			if(skjsbh==""){ 
				skjsbh=$("#GS_SKJSBH").val();
				skjsbhxm=$("#GS_SKJSXM").val()
			}
			
			if(saveType=="new"){
				xkdm=$('#GS_KCMC').combobox('getValue');
			}else{
				xkdm=$('#GS_KCMC').val();
			}
			var yteaid=teacherbh.replace("+",",");
			var yteaname=teacherxm.replace("+",",");
	
			$.ajax({
			   type: "POST",
			   url: '<%=request.getContextPath()%>/Svl_Skjh',
			   data: "active=check&XNXQ=" + xnxqVal + '&JXXZ=' + jxxzVal + '&SKJSBH=' + skjsbh + '&SKJSBHXM=' + skjsbhxm + '&XKDM=' + xkdm + '&teacherbh=' + yteaid + '&teacherxm=' + yteaname + '&saveType=' + saveType,
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
						if(data.SKSL==""){
							alertMsg("任课教师 "+teachername+" 的最大同时任教课程数未生成，请联系管理员!");
						}else{
							alertMsg("任课教师 "+teachername+" 的最大同时任教课程数已达上限");
						}
					}
			   }
			});
		}
		
		//fm提交事件
		$('#fm').form({
			url:'<%=request.getContextPath()%>/Svl_Skjh?iUSERCODE='+iUSERCODE+'&courseid='+courseid,
			onSubmit:function(){
			
			},
			//提交成功
			success:function(datas){
				if($('#active').val()=="save"){ 
					var json = eval("("+datas+")");
					showMsg(json[0].msg); 
					initGridData(classId,savexqbh);
					$('#win').dialog("close");
					
				}
			}
		});
		
		//dialog窗口
		function loadDialog() {
			$('#win').dialog({
				width : 342,
				height: 393,
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
					teabh="";
					rombh="";
					teainfoidarray.splice(0,teainfoidarray.length);
					teainfoarray.splice(0,teainfoarray.length);
					clsinfoidarray.splice(0,clsinfoidarray.length);
					clsinfoarray.splice(0,clsinfoarray.length);
				}
			});
			
			$('#tsc').dialog({   
				title: '任课老师  授课周次  场地要求',   
				width: 660,//宽度设置   
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
				width: 680,//宽度设置   
				height: 438,//高度设置 
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
				height: 290,//高度设置 
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
				height: 500,//高度设置 
				modal:true,
				closed: true,   
				cache: false, 
				draggable:true,//是否可移动dialog框设置
				//读取事件
				onLoad:function(data){},
				//关闭事件
				onClose:function(data){
					clsinfoidarray.splice(0,clsinfoidarray.length);
					clsinfoarray.splice(0,clsinfoarray.length);
				}
			});
		}
		
		//初始化信息
		function emptyDialog(){
			$('#XKBH').html('');
			if(saveType=="new"){
				$('#GS_KCMC').combobox('setValue', '');
			}else{
				$('#GS_KCMC').textbox('setValue', '');
			}
			kclx="";
			iKeyCode="";
			$('#list').treegrid("unselectRow", lastIndex);
				
				$('#GS_SKJSXM').textbox("setValue","");
				$('#GS_SKZC').textbox("setValue","");
				$('#GS_CDYQ').textbox("setValue","");
					
				$('#GS_JS').textbox('setValue', '');
				$('#GS_LJ').textbox('setValue', '');
				$('#GS_LC').textbox('setValue', '');			
				$('#GS_XF').textbox('setValue','');//学分
				$('#GS_ZKS').textbox('setValue','');//总课时	
				$('#GS_SFKS').combobox('setValue','1');
				//$('#GS_XNXQ2').combobox('setValue',xnxqVal);
				
				teaidarray.splice(0,teaarray.length);
				teaarray.splice(0,teaarray.length);
				wekidarray.splice(0,wekarray.length);
				wekarray.splice(0,wekarray.length);
				clsidarray.splice(0,clsarray.length);	
				clsarray.splice(0,clsarray.length);	
				//roomsel=0;

				//允许修改项
				$('#GS_KCMC').combobox('setValue','请先选择课程类型');
				$('#GS_KCMC').combobox("disable");//课程名称
				$('#GS_SKJSXM').textbox("disable");//授课教师姓名
				$('#GS_SKZC').textbox("disable");//授课周次
				$('#GS_CDYQ').textbox("disable");//场地要求	
						
				$('#GS_JS').numberspinner("disable");//节数
				$('#GS_LJ').numberspinner("disable");//连节
				$('#GS_LC').numberspinner("disable");;//连次
				$('#GS_KSXS').combobox("disable");//考试形式
				$('#GS_SFKS').combobox("disable");//是否考试
				$('#GS_XUEF').textbox("disable");//学分
				$('#GS_ZOKS').textbox("disable");//总课时
		}
		
		//打开tsc编辑窗口
		function opentsc(){  
			//特殊内容编辑，不打开窗口
			if(paike!="0"){
				return;
			}
			
			//执行
			document.getElementById("win").focus(); 
					
			//初始化fm2
			$("#fm2").html('');
			var html='';
				html='<table id="tsctable" class = "tablestyle" width="686px">'+
						'<tr>'+
							'<a href="#" class="easyui-linkbutton" id="submit2" name="submit2" iconCls="icon-submit" plain="true" onClick="doToolbar(this.id)">确定</a>'+
							'<a href="#" class="easyui-linkbutton" id="add" name="add" iconCls="icon-add" plain="true" onClick="doToolbar(this.id)">添加</a>'+
						'</tr>'+
					'</table>';
			$("#fm2").html(html);
			$.parser.parse(('#fm2'));
			
			
			//saveType="enter";
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
			//$('#ic_teaLevel').combobox("setValue","");
			
// 			$.ajax({
// 			   type: "POST",
// 			   url: '< %=request.getContextPath()%>/Svl_Skjh',
// 			   data: "active=openTeacher",
// 			   dataType: 'json',
// 			   success: function(datas){
                    
// 					$("#fm3").html('');
				
// 					var html1="";
// 						html1='<table id="teatable" class = "tablestyle" width="286px" >'+
// 							'<tr>'+
// 								'<a href="#" class="easyui-linkbutton" id="submit3" name="submit3" iconCls="icon-submit" plain="true" onClick="doToolbar(this.id)">确定</a>'+
// 							'</tr>'+
// 							'<tr>'+
// 								'<td width="20%" align="center"></td>'+
// 								'<td width="40%" align="center">工号</td>'+
// 								'<td width="40%" align="center">姓名</td>'+
// 							'</tr>';
// 					var html2="";	
// 					for(var i=0;i<datas.length;i++){
// 						html2+='<tr>'+
// 								'<td width="20%" align="center"><input type="checkbox" id="'+datas[i].工号+'" name="teacb" hidename="'+datas[i].姓名+'" /></td>'+
// 								'<td width="40%" align="center"><span>'+datas[i].工号+'</span></td>'+
// 								'<td width="40%" align="center"><span>'+datas[i].姓名+'</span></td>'+
// 							  '</tr>';
// 					}
// 					var html3='</table>';
// 					$("#fm3").html(html1+html2+html3);
// 					$.parser.parse(('#fm3'));
					
					
// 					if($('#'+inputid).val()!=""&&teaidarray[(inputid.substring(7,inputid.length)-1)]!=""){
// 						var teaid=teaidarray[(inputid.substring(7,inputid.length)-1)].split("+");
// 						for(var i=0;i<teaid.length;i++){
// 							$('#'+teaid[i]).attr("checked","checked");
// 						}
//  					}
// 			   }
// 			});

			teabh=teaidarray[(inputid.substring(7,inputid.length)-1)];
			//loadGridTea();
			if(aa==""){
				
			}else{
				teabh="";
				teabh+="+"+aa;
			}
			if(typeof(teabh)=="undefined"){
				teabh="";
			}
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
			//JSLBHCombobox();
			//$('#teacher').show();
// 			if(getOs()=="MSIE"){
// 				if(navigator.appName == "Microsoft Internet Explorer" && (navigator.appVersion.match(/8./i)=="8."||navigator.appVersion.match(/7./i)=="7.")){ 
// 					$('#teainput').attr('style','height:36px;');
// 				}else if(navigator.appName == "Microsoft Internet Explorer" && (navigator.appVersion.match(/9./i)=="9."||navigator.appVersion.match(/10./i)=="10."||navigator.appVersion.match(/11./i)=="11.")){ 
// 					$('#teainput').attr('style','height:56px;');	
// 				}else{
// 					$('#teainput').attr('style','height:36px;');	
// 				} 
// 			}
			//$('#teacher').dialog("open");		
			var teabhgai=teabh.replace(/\+/g,",");
			showOpen(teabhgai);
			teainfoidarray.splice(0,teainfoidarray.length);
			teainfoarray.splice(0,teainfoarray.length);
		}
		
		function loadGridTea(){ 
			isLoad = true;
			$('#teatable').datagrid({
				url: '<%=request.getContextPath()%>/Svl_Skjh',
	 			queryParams: {"active":"openTeacher","teaId":$('#ic_teaId').val(),"teaName":$('#ic_teaName').val(),"teaLevel":"","teacharr":teabh},
				loadMsg : "信息加载中请稍后!",//载入时信息
				width:666,
				height:346,
				rownumbers: true,
				animate:true,
				striped : true,//隔行变色
				pageSize : pageSize,//每页记录数
				singleSelect : false,//单选模式
				pageNumber : pageNum,//当前页码
				pagination:true,
				fit:false,
				fitColumns: true,//设置边距
				columns:[[
					{field:'ckt',checkbox:true},
					{field:'工号',title:'工号',width:100,align:'center'},
					{field:'姓名',title:'姓名',width:100,align:'center'}
					//{field:'层级名称',title:'教师类别',width:100,align:'center'}			
				]],
				onSelect:function(rowIndex,rowData){
					if(isLoad == false){
						var same=0;
						$.each(teainfoidarray, function(key,value){
							if(value == rowData.工号){
								same=1;
							}
						}); 
						if(same==0){
							teainfoidarray.push(rowData.工号);
							teainfoarray.push(rowData.姓名);
							teabh+="+"+rowData.工号;		
						}			
					} 
				},
				onUnselect:function(rowIndex,rowData){
					$.each(teainfoidarray, function(key,value){
						if(value == rowData.工号){
							teainfoidarray.splice(key, 1);
							teainfoarray.splice(key, 1);
						}
					}); 
					teabh="";
					for(var i=0;i<teainfoidarray.length;i++){
						teabh+=teainfoidarray[i]+"+";
					}
					if(teabh!=""){
						teabh=teabh.substring(0,teabh.length-1);
					}
				},
				onLoadSuccess: function(data){
					$(".datagrid-header-check").html('&nbsp;');//隐藏全选
					//勾选已选授课教师
					if(data){
						if(teabh!=undefined){
							var selteaid=teabh.split("+");
							$.each(data.rows, function(rowIndex, rowData){
								for(var i=0; i<selteaid.length; i++){
									if(selteaid[i] == rowData.工号){
										$('#teatable').datagrid('selectRow', rowIndex);
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
		
		function loadSFKSCombo(){
			$('#GS_SFKS').combobox({
				url:"<%=request.getContextPath()%>/Svl_Skjh?active=loadSFKSCombo",
				valueField:'comboValue',
				textField:'comboName',
				editable:false,
				width:'250',
				panelHeight:'auto', //combobox高度
				onLoadSuccess:function(data){
					
				},
				//下拉列表值改变事件
				onSelect:function(data){ 
						if(data.comboValue==2){
							if(saveType=="new"){
								alertMsg("是否考试为否,不会生成相关学生成绩数据");
							}else{
								alertMsg("是否考试为否,将会删除相关学生成绩数据");
							}						
						}
					
				}
			});
			
			$('#GS_KCLX').combobox({
				url:"<%=request.getContextPath()%>/Svl_Skjh?active=loadKCLXCombo",
				valueField:'comboValue',
				textField:'comboName',
				editable:false,
				panelHeight:'auto', //combobox高度
				onLoadSuccess:function(data){
					if(data != ''){
						//初始化combobox时赋值
						$(this).combobox('setValue', '00');
					}
				},
				//下拉列表值改变事件
				onSelect:function(data){  
					$('#XKBH').html(""); 			
					if(data.comboValue!='00'){
						$('#GS_KCMC').combobox({
							url:"<%=request.getContextPath()%>/Svl_Skjh?active=loadKCMCCombo"+"&GS_KCLX="+data.comboValue+"&GS_XZBDM="+classId,
							valueField:'comboValue',
							textField:'comboName',
							editable:true,
							panelHeight:'140', //combobox高度
							onLoadSuccess:function(data){
								//判断data参数是否为空
								if(data != ''){
									//初始化combobox时赋值
									$(this).combobox('setValue', '');
									$('#GS_SKJSXM').textbox("disable");//授课教师姓名
									$('#GS_SKZC').textbox("disable");//授课周次
									$('#GS_CDYQ').textbox("disable");//场地要求			
									$('#GS_JS').numberspinner("disable");//节数
									$('#GS_LJ').numberspinner("disable");//连节
									$('#GS_LC').numberspinner("disable");;//连次
									$('#GS_KSXS').combobox("disable");//考试形式
									$('#GS_SFKS').combobox("disable");//是否考试
									$('#GS_XUEF').textbox("disable");//学分
									$('#GS_ZOKS').textbox("disable");//总课时
								}
							},
							//下拉列表值改变事件
							onChange:function(data){
								if($('#GS_KCMC').combobox('getValue')!=undefined && ($('#GS_KCMC').combobox('getValue')==""||$('#GS_KCMC').combobox('getValue').length==10)){
									$('#XKBH').html($(this).combobox('getValue'));
									$('#GS_KCMCDM').val($(this).combobox('getValue'));
									if($('#GS_KCMC').combobox('getValue')==""){//请选择
										$('#GS_SKJSXM').textbox("disable");//授课教师姓名
										$('#GS_SKZC').textbox("disable");//授课周次
										$('#GS_CDYQ').textbox("disable");//场地要求			
										$('#GS_JS').numberspinner("disable");//节数
										$('#GS_LJ').numberspinner("disable");//连节
										$('#GS_LC').numberspinner("disable");;//连次
										$('#GS_KSXS').combobox("disable");//考试形式
										$('#GS_SFKS').combobox("disable");//是否考试
										$('#GS_XUEF').textbox("disable");//学分
										$('#GS_ZOKS').textbox("disable");//总课时
									}else{
										$('#GS_SKJSXM').textbox("enable");//授课教师姓名
										$('#GS_SKZC').textbox("enable");//授课周次
										$('#GS_CDYQ').textbox("enable");//场地要求			
										$('#GS_JS').numberspinner("enable");//节数
										$('#GS_LJ').numberspinner("enable");//连节
										$('#GS_LC').numberspinner("enable");;//连次
										$('#GS_KSXS').combobox("enable");//考试形式
										$('#GS_SFKS').combobox("enable");//是否考试
										$('#GS_XUEF').textbox("enable");//学分
										$('#GS_ZOKS').textbox("enable");//总课时
									}
									
								}else{
									$('#XKBH').html("");
									$('#GS_KCMCDM').val("");
								}	
									
							}
						});
					}else{ 
						if(saveType=="new"){ 
							$('#XKBH').html("");
							$('#GS_KCMCDM').val("");
							$('#GS_KCMC').combobox('setValue','请先选择课程类型');
							$('#GS_KCMC').combobox("disable");
							emptyDialog();
						}else{
							$('#XKBH').html("");
							$('#GS_KCMCDM').val("");
							$('#GS_KCMC').textbox('setValue','');
							//$('#GS_KCMC').combobox('setValue','');
							//$('#GS_KCMC').combobox("disable");
						}
					}
					
				},
				//下拉列表值改变事件
				onChange:function(data){
				
				}
			});
		}
		
		//保存选择的任课教师信息
		function editTeaInfo(id,name){ 
			
			var checkbox = document.getElementById(id);
			if(checkbox.checked){//勾选
				teainfoidarray.push(id);
				teainfoarray.push(name);
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
			inputid=id;
			document.getElementById("tsc").focus(); 

			var html1="";
			if(weeks==17){
				html1=  '<tr>'+
						'<a href="#" class="easyui-linkbutton" id="submit4" name="submit4" iconCls="icon-submit" plain="true" onClick="doToolbar(this.id)">确定</a>'+
					    '</tr>'+
					    '<tr>'+
							'<td width="25%" align="center"><input type="checkbox" id="custom" name="selweek" value="custom" onclick="weekcheck(this.id,this.value);" /><label for="custom">自定义</label></td>'+
							'<td width="25%" align="center"><input type="checkbox" id="singleweek" name="selweek" value="singleweek" onclick="weekcheck(this.id,this.value);" /><label for="singleweek"> 单周</label></td>'+
							'<td width="25%" align="center"><input type="checkbox" id="doubleweek" name="selweek" value="doubleweek" onclick="weekcheck(this.id,this.value);" /><label for="doubleweek"> 双周</label></td>'+
							'<td width="25%" align="center"> </td>'+
						'</tr>'+
						'<tr>'+
							'<td width="25%" align="center"><input type="checkbox" id="w0109" name="selweek" value="1-9" onclick="weekcheck(this.id,this.value);" /> <label for="w0109">01-09</label></td>'+
							'<td width="25%" align="center"><input type="checkbox" id="w0209" name="selweek" value="2-9" onclick="weekcheck(this.id,this.value);" /> <label for="w0209">02-09</label></td>'+
							'<td width="25%" align="center"><input type="checkbox" id="w0114" name="selweek" value="1-14" onclick="weekcheck(this.id,this.value);" /> <label for="w0114">01-14</label></td>'+
							'<td width="25%" align="center"><input type="checkbox" id="w0214" name="selweek" value="2-14" onclick="weekcheck(this.id,this.value);" /> <label for="w0214">02-14</label></td>'+
						'</tr>'+
						'<tr>'+
							'<td width="25%" align="center"><input type="checkbox" id="w0517" name="selweek" value="5-17" onclick="weekcheck(this.id,this.value);" /> <label for="w0517">05-17</label></td>'+
							'<td width="25%" align="center"><input type="checkbox" id="w1017" name="selweek" value="10-17" onclick="weekcheck(this.id,this.value);" /> <label for="w1017">10-17</label></td>'+
							'<td width="25%" align="center"><input type="checkbox" id="w0117" name="selweek" value="1-17" onclick="weekcheck(this.id,this.value);" /> <label for="w0117">01-17</label></td>'+
							'<td width="25%" align="center"><input type="checkbox" id="w0217" name="selweek" value="2-17" onclick="weekcheck(this.id,this.value);" /> <label for="w0217">02-17</label></td>'+
						'</tr>';
			}else if(weeks==19){
				html1=  '<tr>'+
						'<a href="#" class="easyui-linkbutton" id="submit4" name="submit4" iconCls="icon-submit" plain="true" onClick="doToolbar(this.id)">确定</a>'+
					    '</tr>'+
					    '<tr>'+
							'<td width="25%" align="center"><input type="checkbox" id="custom" name="selweek" value="custom" onclick="weekcheck(this.id,this.value);" /><label for="custom">自定义</label></td>'+
							'<td width="25%" align="center"><input type="checkbox" id="singleweek" name="selweek" value="singleweek" onclick="weekcheck(this.id,this.value);" /><label for="singleweek"> 单周</label></td>'+
							'<td width="25%" align="center"><input type="checkbox" id="doubleweek" name="selweek" value="doubleweek" onclick="weekcheck(this.id,this.value);" /><label for="doubleweek"> 双周</label></td>'+
							'<td width="25%" align="center"><input type="checkbox" id="w0105" name="selweek" value="1-5" onclick="weekcheck(this.id,this.value);" /> <label for="w0105">01-05</label></td>'+
						'</tr>'+
						'<tr>'+
							'<td width="25%" align="center"><input type="checkbox" id="w0110" name="selweek" value="1-10" onclick="weekcheck(this.id,this.value);" /> <label for="w0110">01-10</label></td>'+
							'<td width="25%" align="center"><input type="checkbox" id="w0210" name="selweek" value="2-10" onclick="weekcheck(this.id,this.value);" /> <label for="w0210">02-10</label></td>'+
							'<td width="25%" align="center"><input type="checkbox" id="w0115" name="selweek" value="1-15" onclick="weekcheck(this.id,this.value);" /> <label for="w0115">01-15</label></td>'+
							'<td width="25%" align="center"><input type="checkbox" id="w0215" name="selweek" value="2-15" onclick="weekcheck(this.id,this.value);" /> <label for="w0215">02-15</label></td>'+
						'</tr>'+
						'<tr>'+
							'<td width="25%" align="center"><input type="checkbox" id="w0619" name="selweek" value="6-19" onclick="weekcheck(this.id,this.value);" /> <label for="w0619">06-19</label></td>'+
							'<td width="25%" align="center"><input type="checkbox" id="w1119" name="selweek" value="11-19" onclick="weekcheck(this.id,this.value);" /> <label for="w1119">11-19</label></td>'+
							'<td width="25%" align="center"><input type="checkbox" id="w0119" name="selweek" value="1-19" onclick="weekcheck(this.id,this.value);" /> <label for="w0119">01-19</label></td>'+
							'<td width="25%" align="center"><input type="checkbox" id="w0219" name="selweek" value="2-19" onclick="weekcheck(this.id,this.value);" /> <label for="w0219">02-19</label></td>'+
						'</tr>';
			}else{
				html1=  '<tr>'+
						'<a href="#" class="easyui-linkbutton" id="submit4" name="submit4" iconCls="icon-submit" plain="true" onClick="doToolbar(this.id)">确定</a>'+
				    '</tr>'+
				    '<tr>'+
						'<td width="25%" align="center"><input type="checkbox" id="custom" name="selweek" value="custom" onclick="weekcheck(this.id,this.value);" /><label for="custom">自定义</label></td>'+
						'<td width="25%" align="center"><input type="checkbox" id="singleweek" name="selweek" value="singleweek" onclick="weekcheck(this.id,this.value);" /><label for="singleweek"> 单周</label></td>'+
						'<td width="25%" align="center"><input type="checkbox" id="doubleweek" name="selweek" value="doubleweek" onclick="weekcheck(this.id,this.value);" /><label for="doubleweek"> 双周</label></td>'+
						'<td width="25%" align="center"> </td>'+
					'</tr>'+
					'<tr>'+
						'<td width="25%" align="center"><input type="checkbox" id="w0109" name="selweek" value="1-9" onclick="weekcheck(this.id,this.value);" /> <label for="w0109">01-09</label></td>'+
						'<td width="25%" align="center"><input type="checkbox" id="w0209" name="selweek" value="2-9" onclick="weekcheck(this.id,this.value);" /> <label for="w0209">02-09</label></td>'+
						'<td width="25%" align="center"><input type="checkbox" id="w0114" name="selweek" value="1-14" onclick="weekcheck(this.id,this.value);" /> <label for="w0114">01-14</label></td>'+
						'<td width="25%" align="center"><input type="checkbox" id="w0214" name="selweek" value="2-14" onclick="weekcheck(this.id,this.value);" /> <label for="w0214">02-14</label></td>'+
					'</tr>'+
					'<tr>'+
						'<td width="25%" align="center"><input type="checkbox" id="w0518" name="selweek" value="5-18" onclick="weekcheck(this.id,this.value);" /> <label for="w0518">05-18</label></td>'+
						'<td width="25%" align="center"><input type="checkbox" id="w1018" name="selweek" value="10-18" onclick="weekcheck(this.id,this.value);" /> <label for="w1018">10-18</label></td>'+
						'<td width="25%" align="center"><input type="checkbox" id="w0118" name="selweek" value="1-18" onclick="weekcheck(this.id,this.value);" /> <label for="w0118">01-18</label></td>'+
						'<td width="25%" align="center"><input type="checkbox" id="w0218" name="selweek" value="2-18" onclick="weekcheck(this.id,this.value);" /> <label for="w0218">02-18</label></td>'+
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
			
			$('#week').dialog({
				height:63+(3+Math.ceil(weekall/4))*25
			});
			$('#week').show();
			
			//初始化
			weeksel=0;
			//weekArray.splice(0,weekArray.length);  //清空选择
			var obj = document.getElementsByName("selweek");
			for(var i=0;i<obj.length;i++){
				$('#'+obj[i].id).attr("checked",false);
			}	
			for(var i=1;i<=weekall;i++){
				$('#weekn'+i).css('background','#efefef');	
				weekArray[(i-1)]=0;
			}
			$('#week').dialog("open");
			
			//alert("wekarray[i]:"+wekarray[inputid.substring(7,inputid.length)-1]);
			if($('#'+inputid).val()!=""){
				if(wekarray[(inputid.substring(7,inputid.length)-1)]=="odd"){
					$('#singleweek').attr("checked","checked");
					weekcheck("singleweek","singleweek");
				}else if(wekarray[(inputid.substring(7,inputid.length)-1)]=="even"){
					$('#doubleweek').attr("checked","checked");
					weekcheck("doubleweek","doubleweek");
				}else{
					var weeknum=wekarray[(inputid.substring(7,inputid.length)-1)]+"";
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
 			}else{//空
				$('#custom').attr("checked","checked");		
			}
 			
 			//清空weekAllArray重新赋值
 			for(var i=0;i<weekall;i++){
				weekAllArray[i]=0;
				linkArray[i]=0;
			}

 			//取所有周次的值，不允许重复
 			for(var i=1;i<=tscnum;i++){
 				if(i!=(inputid.substring(7,inputid.length))){
 					if($('#HS_SKZC'+i).val()!=""){
 						//alert($('#HS_SKZC'+i).val());
	 					if($('#HS_SKZC'+i).val()=="单周"){
	 						for(var j=0;j<obj.length;j++){
	 							if(obj[j].id!="custom"&&obj[j].id!="doubleweek"){
	 								$('#'+obj[j].id).attr("disabled","disabled");
	 							}	
							}
	    					for(var j=0;j<weeks;j++){
		  						if(j%2==0){//单数
	  								weekAllArray[j]=1;
	  							}
	    					}
	 					}else if($('#HS_SKZC'+i).val()=="双周"){
	 						for(var j=0;j<obj.length;j++){
	 							if(obj[j].id!="custom"&&obj[j].id!="singleweek"){
	 								$('#'+obj[j].id).attr("disabled","disabled");
	 							}	
							}
	 						for(var j=0;j<weeks;j++){
	 							if(j%2!=0){//双数
	 								weekAllArray[j]=1;
	 							}
	 						}
	 					}else if($('#HS_SKZC'+i).val().indexOf("#")>-1){//不连续
	 						var skzcnum=$('#HS_SKZC'+i).val().split("#");
							for(var j=0;j<skzcnum.length;j++){
								weekAllArray[(skzcnum[j]-1)]=1;
								if(skzcnum[j]%2==0){
									$("#doubleweek").attr("disabled","disabled");
								}else if(skzcnum[j]%2==1){
									$("#singleweek").attr("disabled","disabled");
								}
								for(var k=0;k<obj.length;k++){
									var weekarea=obj[k].value.split("-");
			 						if(parseInt(weekarea[0])<=skzcnum[j]&&skzcnum[j]<=parseInt(weekarea[1])){
			 							$('#'+obj[k].id).attr("disabled","disabled");
			 						}		
								}					
							}		
	 					}else if($('#HS_SKZC'+i).val().indexOf("-")>-1){
	 						var skzcnum=$('#HS_SKZC'+i).val().split("-");
							for(var j=parseInt(skzcnum[0]);j<=parseInt(skzcnum[1]);j++){
								weekAllArray[(j-1)]=1;	
							}
							$("#singleweek").attr("disabled","disabled");
							$("#doubleweek").attr("disabled","disabled");
							for(var k=0;k<obj.length;k++){
								var weekarea=obj[k].value.split("-");
								for(var l=parseInt(skzcnum[0]);l<=parseInt(skzcnum[1]);l++){
									if(parseInt(weekarea[0])<=l&&l<=parseInt(weekarea[1])){
				 						$('#'+obj[k].id).attr("disabled","disabled");
				 					}
								}
							}					
	 					}else{
							var skzc=$('#HS_SKZC'+i).val();
							weekAllArray[(skzc-1)]=1;	
							if(skzc%2==0){
								$("#doubleweek").attr("disabled","disabled");
							}else if(skzc%2==1){
								$("#singleweek").attr("disabled","disabled");
							}
							for(var k=0;k<obj.length;k++){
								var weekarea=obj[k].value.split("-");
								if(parseInt(weekarea[0])<=skzc&&skzc<=parseInt(weekarea[1])){
				 					$('#'+obj[k].id).attr("disabled","disabled");
				 				}
							}	
						}
					}
 				}
			}
		}
		
		//自定义，单周，双周，周范围选择
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
					weekArray[(i-1)]=0;	
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
			//if(weeksel==1){
				if(weekAllArray[id.substring(5,id.length)-1]==0){
					if(weekArray[(id.substring(5,id.length)-1)]==0){
						$('#'+id).css('background','#E46BA1');
						weekArray[(id.substring(5,id.length)-1)]=1;
					}else{
						$('#'+id).css('background','#efefef');
						weekArray[(id.substring(5,id.length)-1)]=0;
					}
				}	
			//}
		}
		function weekmouseover(id){
			//if(weeksel==1){
				if(weekAllArray[id.substring(5,id.length)-1]==0){
					if(weekArray[(id.substring(5,id.length)-1)]==0){
							$('#'+id).css('background','#E46BA1');
					}
				}
			//}
		}
		function weekmouseout(id){
			//if(weeksel==1){
				if(weekAllArray[id.substring(5,id.length)-1]==0){
					if(weekArray[(id.substring(5,id.length)-1)]==0){
						$('#'+id).css('background','#efefef');
					}	
				}
			//}
		}
		
		//打开room编辑窗口
		var rombh="";
		var rommc="";
		function openroom(id){
			inputid=id;
			document.getElementById("tsc").focus(); 
			
			//初始化
			//$('#chooseroom').attr("checked",false);
			//$('#choosetype').attr("checked",false);
			//$('#normalroom').numberbox('setValue','');
			//$('#meidaroom').numberbox('setValue','');
			//$('#normalroom').numberbox({ disabled: true });
			//$('#meidaroom').numberbox({ disabled: true });
			$('#school').combobox('setValue','');
			//$('#house').combobox('setValue','');
			$('#clstype').combobox('setValue','');
			
			//填充数据
			rombh=clsidarray[(inputid.substring(7,inputid.length)-1)]; 
			rommc=clsarray[(inputid.substring(7,inputid.length)-1)];
			if(rombh!=undefined){
				var selromid=rombh.split("+");
				var selrommc=rommc.split("+");
				for(var i=0; i<selromid.length; i++){  
					clsinfoidarray.push(selromid[i]);
					clsinfoarray.push(selrommc[i]);				
				}
			}
			loadGridCls();
            schoolCombobox();
			//houseCombobox();
			classtypeCombobox();
				
					
// 					if($('#'+inputid).val()!=""&&clsidarray[(inputid.substring(7,inputid.length)-1)]!=""){
// 						var clsid=clsidarray[(inputid.substring(7,inputid.length)-1)].split("+");
// 						var clsna=clsarray[(inputid.substring(7,inputid.length)-1)].split("+");
// 						for(var i=0;i<clsid.length;i++){
// 							clsinfoidarray.push(clsid[i]);
// 							clsinfoarray.push(clsna[i]);				
// 						}
// 				 	}
					
					//alert("clsarray[i]:"+clsidarray[inputid.substring(7,inputid.length)-1]);
					if($('#'+inputid).val()!=""){
						var clsidname=clsidarray[(inputid.substring(7,inputid.length)-1)];
						if(clsidname.indexOf("+")>-1){
							var clsid=clsidname.split("+");
// 							if(clsid[0]=="5"||clsid[0]=="1"){//只选类型
// 								$('#choosetype').attr("checked","checked");
								//chooseType();
// 								var checkbox = document.getElementById('choosetype');
// 								if(checkbox.checked){
// 									roomsel=1;
// 									$('#chooseroom').attr("checked",false);
// 									$('#normalroom').numberbox({ disabled: false });
// 									$('#meidaroom').numberbox({ disabled: false });
// 									$('#school').combobox("disable");
// 									$('#house').combobox("disable");
// 									$('#clstype').combobox("disable");
// 									for(var i=0;i<roomnum;i++){
// 										$('#room'+i).attr("disabled","disabled");
// 									}
// 									$('#rooms').attr("disabled","disabled");
// 								}else{
// 									roomsel=0;
// 									$('#normalroom').numberbox('setValue','');
// 									$('#meidaroom').numberbox('setValue','');
// 									$('#normalroom').numberbox({ disabled: true });
// 									$('#meidaroom').numberbox({ disabled: true });				
// 								}							
// 								var normalrooms=0;
// 								var mediarooms=0;
// 								for(var j=0;j<clsid.length;j++){
// 									if(clsid[j]=="5"){
// 										normalrooms++;
// 									}else{
// 										mediarooms++;
// 									}
// 								}
// 								$('#normalroom').numberbox('setValue',normalrooms);
// 								$('#meidaroom').numberbox('setValue',mediarooms);
// 							}else{
								//指定教室
								//$('#chooseroom').attr("checked","checked");
								//chooseRoom();
								//var checkbox = document.getElementById('chooseroom');
								//if(checkbox.checked){
									roomsel=2;
									//$('#normalroom').numberbox('setValue','');
									//$('#meidaroom').numberbox('setValue','');
									//$('#normalroom').numberbox({ disabled: true });
									//$('#meidaroom').numberbox({ disabled: true });
									//$('#choosetype').attr("checked",false);
									$('#school').combobox("enable");
									//$('#house').combobox("enable");
									$('#clstype').combobox("enable");
// 								}else{
// 									roomsel=0;
// 									$('#school').combobox("disable");
// 									$('#house').combobox("disable");
// 									$('#clstype').combobox("disable");	
// 								}
							//}
						}else{
// 							if(clsidname=="5"||clsidname=="1"){//只选类型
// 								$('#choosetype').attr("checked","checked");
								//chooseType();		
// 								var checkbox = document.getElementById('choosetype');
// 								if(checkbox.checked){
// 									roomsel=1;
// 									$('#chooseroom').attr("checked",false);
// 									$('#normalroom').numberbox({ disabled: false });
// 									$('#meidaroom').numberbox({ disabled: false });
// 									$('#school').combobox("disable");
// 									$('#house').combobox("disable");
// 									$('#clstype').combobox("disable");
// 									for(var i=0;i<roomnum;i++){
// 										$('#room'+i).attr("disabled","disabled");
// 									}
// 									$('#rooms').attr("disabled","disabled");
// 								}else{
// 									roomsel=0;
// 									$('#normalroom').numberbox('setValue','');
// 									$('#meidaroom').numberbox('setValue','');
// 									$('#normalroom').numberbox({ disabled: true });
// 									$('#meidaroom').numberbox({ disabled: true });				
// 								}					
// 								var normalrooms=0;
// 								var mediarooms=0;
// 								if(clsidname=="5"){
// 									normalrooms++;
// 								}else{
// 									mediarooms++;
// 								}
// 								$('#normalroom').numberbox('setValue',normalrooms);
// 								$('#meidaroom').numberbox('setValue',mediarooms);
// 							}else{
								//指定教室
								//$('#chooseroom').attr("checked","checked");
								//chooseRoom();
								//var checkbox = document.getElementById('chooseroom');
								//if(checkbox.checked){
									roomsel=2;
									//$('#normalroom').numberbox('setValue','');
									//$('#meidaroom').numberbox('setValue','');
									//$('#normalroom').numberbox({ disabled: true });
									//$('#meidaroom').numberbox({ disabled: true });
									//$('#choosetype').attr("checked",false);
									$('#school').combobox("enable");
									//$('#house').combobox("enable");
									$('#clstype').combobox("enable");
// 								}else{
// 									roomsel=0;
// 									$('#school').combobox("disable");
// 									$('#house').combobox("disable");
// 									$('#clstype').combobox("disable");	
// 								}
							//}
						}
 					}else{//教室为空
 						//$('#school').combobox("disable");
						//$('#house').combobox("disable");
						//$('#clstype').combobox("disable");
						clsinfoidarray.splice(0,clsinfoidarray.length);
						clsinfoarray.splice(0,clsinfoarray.length); 
 					}		
				
			$('#room').show();		
			$('#room').dialog("open");
		}
		
		function loadGridCls(){
			isLoad = true;
			$('#clstable').datagrid({
				url: '<%=request.getContextPath()%>/Svl_Skjh',
	 			queryParams: {"active":"openRoom","seltype":$('#clstype').combobox('getValue'),"roomarr":rombh,"classId":classId},
				loadMsg : "信息加载中请稍后!",//载入时信息
				width:566,
				height:412,
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
					if(isLoad == false&&roomsel==2){
						var same=0;	
						$.each(clsinfoidarray, function(key,value){
							if(value == rowData.教室编号){
								same=1;
							}
						});
						if(same==0){
							clsinfoidarray.push(rowData.教室编号);
							clsinfoarray.push(rowData.教室名称);	
							rombh+="+"+rowData.教室编号;		
						}
					}
				},
				onUnselect:function(rowIndex,rowData){
					if(roomsel==2){
						$.each(clsinfoidarray, function(key,value){
							if(value == rowData.教室编号){
								clsinfoidarray.splice(key, 1);
								clsinfoarray.splice(key, 1);
							}
						});
						rombh="";
						for(var i=0;i<clsinfoidarray.length;i++){
							rombh+=clsinfoidarray[i]+"+";
						}
						if(rombh!=""){
							rombh=rombh.substring(0,rombh.length-1);
						}
					}
				},
				onLoadSuccess: function(data){
					$(".datagrid-header-check").html('&nbsp;');//隐藏全选
					//勾选已选教室
					if(data){
						if(rombh!=undefined){
							var selromid=rombh.split("+");
							$.each(data.rows, function(rowIndex, rowData){
								for(var i=0; i<selromid.length; i++){
									if(selromid[i] == rowData.教室编号){
										$('#clstable').datagrid('selectRow', rowIndex);
									}
								}
							});
						}
					}
// 					clsroomlen=data.rows.length;	
// 					if(roomsel!=2){
// 						for (var i = 0; i < clsroomlen; i++) { 
// 							$('#clstable').datagrid('unselectRow', i);
// 						}
// 					}
												
					isLoad = false;
				},
				onLoadError:function(none){
					
				}
			});
		}
		
		//保存选择的任课教师信息
		function editClsInfo(id,room){
			var checkbox = document.getElementById(id);
			if(checkbox.checked){//勾选
				clsinfoidarray.push(id);
				clsinfoarray.push(room);
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
			for (var i = 0; i < clsroomlen; i++) {
				$("input[name='ckr']")[i].disabled = true;
				$('#clstable').datagrid('unselectRow', i);
			}
			clsinfoidarray.splice(0,clsinfoidarray.length);
			clsinfoarray.splice(0,clsinfoarray.length);
			var checkbox = document.getElementById('choosetype');
			if(checkbox.checked){
				//roomsel=1;
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
				//roomsel=0;
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
				//roomsel=2;
				$('#normalroom').numberbox('setValue','');
				$('#meidaroom').numberbox('setValue','');
				$('#normalroom').numberbox({ disabled: true });
				$('#meidaroom').numberbox({ disabled: true });
				$('#choosetype').attr("checked",false);
				$('#school').combobox("enable");
				$('#house').combobox("enable");
				$('#clstype').combobox("enable");
				for (var i = 0; i < clsroomlen; i++) {
					$('#clstable').datagrid('unselectRow', i);
					$("input[name='ckr']")[i].disabled = false;
				}
				clsinfoidarray.splice(0, clsinfoidarray.length);
				clsinfoarray.splice(0, clsinfoarray.length);
			}else{
				//roomsel=0;
				$('#school').combobox("disable");
				$('#house').combobox("disable");
				$('#clstype').combobox("disable");
				for (var i = 0; i < clsroomlen; i++) {
					$("input[name='ckr']")[i].disabled = true;
					$('#clstable').datagrid('unselectRow', i);
				}
				clsinfoidarray.splice(0,clsinfoidarray.length);
				clsinfoarray.splice(0,clsinfoarray.length);
			}
		}
		
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
						//$('#school').combobox("disable");
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
						//$('#clstype').combobox("disable");
					}
							
				},
				//下拉列表值改变事件
				onChange:function(data){ 
					for(var i=0;i<clsinfoidarray.length;i++){
						rombh+=clsinfoidarray[i]+"+";
					}
					rombh=rombh.substring(0,rombh.length-1);
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
		
	
	//处理键盘事件
	//禁止后退键（Backspace）密码或单行、多行文本框除外
	function banBackSpace(e){
	    var ev = e || window.event;//获取event对象
	    var obj = ev.target || ev.srcElement;//获取事件源
	    var t = obj.type || obj.getAttribute('type');//获取事件源类型
	    
	    //获取作为判断条件的事件类型
	    var vReadOnly = obj.getAttribute('readonly');
	    var vEnabled = obj.getAttribute('enabled');
	
	    //处理null值情况
	    vReadOnly = (vReadOnly == null)?false:vReadOnly;
	    vEnabled = (vEnabled == null)?true:vEnabled;
	    
	    //当敲Backspace键时，事件源类型为密码或单行、多行文本的，
	    //并且readonly属性为true或enabled属性为false的，则退格键失效
	    var flag1=(ev.keyCode == 8 && (t=="password"|| t=="text"|| t=="textarea") && (vReadOnly==true|| vEnabled!=true))?true:false;
	
	    //当敲Backspace键时，事件源类型非密码或单行、多行文本的，则退格键失效
	    var flag2=(ev.keyCode == 8 && t != "password"&& t != "text"&& t != "textarea")?true:false;
	
	    //判断
	    if(flag2)
	        return false;
	
	    if(flag1)
	        return false;
	}
	
	//禁止后退键(作用于Firefox、Opera)
	document.onkeypress=banBackSpace;
	//禁止后退键 (作用于IE、Chrome)
	document.onkeydown=banBackSpace;
	
	//2017/12/7翟旭超加  begin
	//打开一个人员选择窗口
		function showOpen(teaCode){
			$('#test').window('open');//打开一个window
			openWindow(teaCode);
		}
		// 设置窗口属性
		function openWindow(teaCode){
			$('#test').window({
					title: '人员列表',
					modal: true,
					shadow: false,
					closed: false,
					maximizable:false,
					minimizable:false,
					onOpen:function(){
						//$("#xiafaxiada").attr("src",'http://180.166.123.122:8087/HRM/form/param/TeaTree1.jsp?u='+encodeURIComponent('http://180.166.123.122:8087/SCOA1//getUsers.jsp')+'&s='+teaCode);
						$("#xiafaxiada").attr("src",'http://180.166.123.122:8087/HRM/form/param/TeaTree1.jsp?u='+encodeURIComponent('http://192.168.111.26:8080/XDEAM/form/registerScoreSet//getUsers.jsp')+'&s='+teaCode);
					}
			});
		}
		//关闭人员选择窗口
		function closeWin(){
			$('#test').window('close');
		}
		
		function getUsers(data){
			var text = decodeURI(data).split(',');
			ids = [];
			names = [];
			aa="";
			var bb="";
			teainfoidarray.length=0;
			teainfoarray.length=0;
			for(var i=0;i<text.length;i++){
				ids.push(text[i].split(':')[0]);
				names.push(text[i].split(':')[1]);
				
				teainfoidarray.push(text[i].split(':')[0]);
				teainfoarray.push(text[i].split(':')[1]);
				aa+=text[i].split(':')[0]+"+";
				bb+=text[i].split(':')[1]+"+";
			}
			aa=aa.substring(0,aa.length-1);
			bb=bb.substring(0,bb.length-1);
			//$('#addName').val(ids.join(','));
			//$('#ic_target').val(names.join(','));
			teaidarray[(inputid.substring(7,inputid.length)-1)]="";
			teaarray[(inputid.substring(7,inputid.length)-1)]="";
			
			if(bb=="undefined"){
				bb="";
			}else{
			}
			teaidarray[(inputid.substring(7,inputid.length)-1)]=aa;
			teaarray[(inputid.substring(7,inputid.length)-1)]=bb;
			
			
			teabh+="+"+aa;	
			teabh=teabh.substring(0,teabh.length-1);
			closeWin();
			$('#'+inputid).val(bb);
		}
  	//2017/12/7翟旭超加  end
		
	</script>
</html>
