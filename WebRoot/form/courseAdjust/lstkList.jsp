<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="java.util.Vector"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.src.develop.store.user.*"%>
<%
	/**
		创建人：yeq
		Create date: 2015.08.21
		功能说明：长期调课
		页面类型:列表及模块入口
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
	<title>调课信息</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/icon.css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/pubTimetable.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/publicScript.js"></script>
	<style>
		.inputWidth{
			width:182px;
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
<body class="easyui-layout">
	<div id="north" region="north" title="调课信息" style="height:116px;" >
		<table>
			<tr>
				<td><a href="#" id="new" class="easyui-linkbutton" plain="true" iconcls="icon-new" onClick="doToolbar(this.id);" title="">新建</a></td>
				<td><a href="#" id="edit" class="easyui-linkbutton" plain="true" iconcls="icon-edit" onClick="doToolbar(this.id);" title="">编辑</a></td>
				<td><a href="#" id="audit" class="easyui-linkbutton" plain="true" iconcls="icon-audit" onClick="doToolbar(this.id);" title="" style="display:none;">审核</a></td>
			</tr>
		</table>
		<table id="ee" singleselect="true" width="100%" class="tablestyle">
			<tr>
				<td style="width:150px;" class="titlestyle">学年学期名称</td>
				<td>
					<input name="TT_XNXQMC_CX" id="TT_XNXQMC_CX" class="easyui-textbox inputWidth"/>
				</td>
				<td style="width:150px;" class="titlestyle">教学性质</td><!-- 根据当前登录人权限 -->
				<td>
					<select name="TT_JXXZ_CX" id="TT_JXXZ_CX" class="easyui-combobox inputWidth" panelHeight="auto">
					</select>
				</td>
				<td style="width:150px;" class="titlestyle">系部名称</td>
				<td>
					<select name="TT_XBMC_CX" id="TT_XBMC_CX" class="easyui-combobox inputWidth">
					</select>
				</td>
				<td style="width:150px;" align="center">
					<a href="#" onclick="doToolbar(this.id)" id="queRequestNote" class="easyui-linkbutton" plain="true" iconcls="icon-search">查询</a>
				</td>				
			</tr>
			<tr>
				<td style="width:150px;" class="titlestyle">课程名称</td>
				<td>
					<input name="TT_KCMC_CX" id="TT_KCMC_CX" class="easyui-textbox inputWidth"/>
				</td>
				<td style="width:150px;" class="titlestyle">班级名称</td>
				<td>
					<input name="TT_BJMC_CX" id="TT_BJMC_CX" class="easyui-textbox inputWidth"/>
				</td>
				<td style="width:150px;" class="titlestyle">申请类型</td>
				<td>
					<select name="TT_SQLX_CX" id="TT_SQLX_CX" class="easyui-combobox inputWidth" panelHeight="auto" editable="false">
						<option value="">请选择</option>
						<option value="1">调课</option>
						<option value="2">停课</option>
						<option value="3">补课</option>
					</select>
				</td>
				<td style="width:150px;" align="center">
				</td>				
			</tr>
	    </table>
	</div>
	<div region="center">
		<table id="requestNoteList" style="width:100%;"></table>
	</div>
	
	<!-- 调课页面 -->
	<div id="tkInfo">
		<div style="width:100%; height:100%;" class="easyui-layout">
			<div region="north" style="height:34px;" >
				<table>
					<tr>
						<td><a href="#" id="save" class="easyui-linkbutton" plain="true" iconcls="icon-save" onClick="doToolbar(this.id);" title="">保存</a></td>
						<td><a href="#" id="submit" class="easyui-linkbutton" plain="true" iconcls="icon-submit" onClick="doToolbar(this.id);" title="">提交</a></td>
						<td><a href="#" id="pass" class="easyui-linkbutton" plain="true" iconcls="icon-ok" onClick="doToolbar(this.id);" title="">通过</a></td>
						<td><a href="#" id="reject" class="easyui-linkbutton" plain="true" iconcls="icon-cancel" onClick="doToolbar(this.id);" title="">驳回</a></td>
					</tr>
				</table>
			</div>
			<div region="center" style="background:#EFEFEF; overflow:hidden;">
				<form id="form1" method='post' style="width:100%; height:100%;">
					<div id="mask" style="position:absolute; top:0; left:0; z-index:99999; width:100%; height:100%; background:url(0); display:none;"></div>
					<table style="width:100%;" class="tablestyle">
						<tr>
							<td style="width:100px;" class="titlestyle">教学性质</td>
							<td style="width:210px;">
								<select name="TT_JXXZ" id="TT_JXXZ" class="easyui-combobox inputWidth">
								</select>
							</td>
							<td style="width:100px;" class="titlestyle">系部名称</td>
							<td>
								<select name="TT_XBDM" id="TT_XBDM" class="easyui-combobox inputWidth" disabled="disabled">
									<option value="">请先选择教学性质</option>
								</select>
							</td>
							<!-- 
							<td style="width:100px;" class="titlestyle">专业名称</td>
							<td>
								<select name="TT_ZYDM" id="TT_ZYDM" class="easyui-combobox inputWidth" disabled="disabled">
									<option value="">请先选择教学性质</option>
								</select>
							</td>
							 -->
						</tr>
						<tr>
							<td class="titlestyle">班级名称</td>
							<td>
								<select name="TT_BJBH" id="TT_BJBH" class="easyui-combobox inputWidth" disabled="disabled">
									<option value="">请先选择系部</option>
								</select>
							</td>
							<td class="titlestyle">课程名称</td>
							<td>
								<select name="TT_KCBH" id="TT_KCBH" class="easyui-combobox inputWidth" disabled="disabled">
									<option value="">请先选择班级</option>
								</select>
							</td>
						</tr>
						<tr>
							<td class="titlestyle">申请类型</td>
							<td colspan="3">
								<select name="TT_SQLX" id="TT_SQLX" class="easyui-combobox inputWidth" disabled="disabled">
									<option value="">请先选择课程信息</option>
								</select>
							</td>
						</tr>
					</table>
					<!-- 调课表格 -->
					<table id="tiaokeTable" style="width:100%; display:none;" class="tablestyle">
						<tr>
							<td style="width:100px;" class="titlestyle"><div style="width:96px;">变更理由</div></td>
							<td colspan="3">
								<textarea id="TT_BGLY_C" name="TT_BGLY_C" style="width:509px;" rows="3" maxlength="500"></textarea>
							</td>
						</tr>
						<tr>
							<td class="titlestyle">原计划授课时间</td>
							<td style="width:210px;">
								<span style="margin-left:2px; margin-right:2px;">周数</span>
								<select name="TT_YJHSKZC_C" id="TT_YJHSKZC_C" class="easyui-combobox" panelHeight="auto" style="width:150px;">
								</select>
								<div style="margin-top:3px;">
									<span style="margin-left:2px; margin-right:2px;">星期</span>
									<select name="TT_YJHDAY_C" id="TT_YJHDAY_C" class="easyui-combobox" panelHeight="auto" style="width:50px;">
									</select>
									<span style="margin-left:2px; margin-right:2px;">第</span>
									<select name="TT_YJHJS_C" id="TT_YJHJS_C" class="easyui-combobox" panelHeight="auto" style="width:75px;">
									</select>
									<span style="margin-left:2px;">节</span>
								</div>
							</td>
							<td style="width:100px;" class="titlestyle">调整后授课时间</td>
							<td>
								<span style="margin-left:2px; margin-right:2px;">周数</span>
								<select name="TT_TZHSKZC_C" id="TT_TZHSKZC_C" class="easyui-combobox" panelHeight="auto" style="width:150px;">
								</select>
								<div style="margin-top:3px;">
									<span style="margin-left:2px; margin-right:2px;">星期</span>
									<select name="TT_TZHDAY_C" id="TT_TZHDAY_C" class="easyui-combobox" panelHeight="auto" style="width:50px;">
									</select>
									<span style="margin-left:2px; margin-right:2px;">第</span>
									<select name="TT_TZHJS_C" id="TT_TZHJS_C" class="easyui-combobox" panelHeight="auto" style="width:75px;">
									</select>
									<span style="margin-left:2px;">节</span>
								</div>
							</td>
						</tr>
						<tr>
							<td colspan="2" class="titlestyle">调整授课教师</td>
							<td colspan="2" class="titlestyle">调整授课地点</td>
						</tr>
						<tr>
							<td colspan="2">
								<div id="teaList" class="tablestyle" style="width:100%; height:85px; overflow:auto;">
								</div>
							</td>
							<td colspan="2">
								<div id="siteList" class="tablestyle" style="width:100%; height:85px; overflow:auto;">
								</div>
							</td>
						</tr>
						<tr>
							<td class="titlestyle">其他说明</td>
							<td colspan="3">
								<textarea id="TT_QTSM" name="TT_QTSM" style="width:509px;" rows="3" maxlength="500"></textarea>
							</td>
						</tr>
					</table>
					<!-- 停课表格 -->
					<table id="tingkeTable" style="width:100%; display:none;" class="tablestyle">
						<tr>
							<td style="width:100px;" class="titlestyle">变更理由</td>
							<td colspan="3">
								<textarea id="TT_BGLY_S" name="TT_BGLY_S" style="width:509px;" rows="3" maxlength="500"></textarea>
							</td>
						</tr>
						<tr>
							<td style="width:100px;" class="titlestyle">原计划授课教师</td>
							<td id="SKJS_S" class="titlestyle">
							</td>
						</tr>
						<tr>
							<td style="width:100px;" class="titlestyle">原计划授课地点</td>
							<td id="SKDD_S" class="titlestyle">
							</td>
						</tr>
						<tr>
							<td class="titlestyle">原计划任课时间</td>
							<td colspan="3" class="titlestyle">
								<span style="margin-right:2px;">周数</span>
								<select name="TT_YJHSKZC_S" id="TT_YJHSKZC_S" class="easyui-combobox" panelHeight="auto" style="width:150px;">
								</select>
								<span style="margin-left:2px; margin-right:2px;">星期</span>
								<select name="TT_YJHDAY_S" id="TT_YJHDAY_S" class="easyui-combobox" panelHeight="auto" style="width:50px;">
								</select>
								<span style="margin-left:2px; margin-right:2px;">第</span>
								<select name="TT_YJHJS_S" id="TT_YJHJS_S" class="easyui-combobox" panelHeight="auto" style="width:75px;">
								</select>
								<span style="margin-left:2px;">节</span>
							</td>
						</tr>
					</table>
					<!-- 补课表格 -->
					<table id="bukeTable" style="width:100%; display:none;" class="tablestyle">
						<tr>
							<td style="width:100px;" class="titlestyle">变更理由</td>
							<td colspan="3">
								<textarea id="TT_BGLY_B" name="TT_BGLY_B" style="width:509px;" rows="3" maxlength="500"></textarea>
							</td>
						</tr>
						<tr>
							<td style="width:100px;" class="titlestyle">授课教师</td>
							<td class="titlestyle">
								<select name="SKJS_B" id="SKJS_B" class="easyui-combobox" style="width:250px;">
								</select>
							</td>
						</tr>
						<tr>
							<td style="width:100px;" class="titlestyle">补课地点</td>
							<td class="titlestyle">
								<select name="SKDD_B" id="SKDD_B" class="easyui-combobox" style="width:250px;">
								</select>
							</td>
						</tr>
						<tr>
							<td class="titlestyle">补课时间</td>
							<td colspan="3" class="titlestyle">
								<span style="margin-left:2px; margin-right:2px;">周数</span>
								<select name="TT_TZHSKZC_B" id="TT_TZHSKZC_B" class="easyui-combobox" panelHeight="auto" style="width:216px;">
								</select>
								<span style="margin-left:2px; margin-right:2px;">星期</span>
								<select name="TT_TZHDAY_B" id="TT_TZHDAY_B" class="easyui-combobox" panelHeight="auto" style="width:50px;">
								</select>
								<span style="margin-left:2px; margin-right:2px;">第</span>
								<select name="TT_TZHJS_B" id="TT_TZHJS_B" class="easyui-combobox" panelHeight="auto" style="width:75px;">
								</select>
								<span style="margin-left:2px;">节</span>
							</td>
						</tr>
					</table>
					
					<input type="hidden" id="active" name="active"/>
					<input type="hidden" id="TT_ID" name="TT_ID"/><!-- 编号 -->
					<input type="hidden" id="TT_XNXQBM" name="TT_XNXQBM"/><!-- 学年学期编码 -->
					<input type="hidden" id="TT_SXLX" name="TT_SXLX"/><!-- 时限类型 -->
					<input type="hidden" id="TT_SKJHMXBH" name="TT_SKJHMXBH"/><!-- 授课计划明细编号 -->
					<input type="hidden" id="TT_BGLY" name="TT_BGLY"/><!-- 变更理由 -->
					<input type="hidden" id="TT_YJHSKZC" name="TT_YJHSKZC"/><!-- 授课周次 -->
					<input type="hidden" id="TT_TZHSKZC" name="TT_TZHSKZC"/>
					<input type="hidden" id="TT_YJHXQ" name="TT_YJHXQ"/><!-- 时间序列 -->
					<input type="hidden" id="TT_YJHSJXL" name="TT_YJHSJXL"/>
					<input type="hidden" id="TT_TZHXQ" name="TT_TZHXQ"/>
					<input type="hidden" id="TT_TZHSJXL" name="TT_TZHSJXL"/>
					<input type="hidden" id="TT_YJHSKJSBH" name="TT_YJHSKJSBH"/><!-- 授课教师 -->
					<input type="hidden" id="TT_YJHSKJSMC" name="TT_YJHSKJSMC"/>
					<input type="hidden" id="TT_TZHSKJSBH" name="TT_TZHSKJSBH"/>
					<input type="hidden" id="TT_TZHSKJSMC" name="TT_TZHSKJSMC"/>
					<input type="hidden" id="TT_YJHCDBH" name="TT_YJHCDBH"/><!-- 授课地点 -->
					<input type="hidden" id="TT_YJHCDMC" name="TT_YJHCDMC"/>
					<input type="hidden" id="TT_TZHCDBH" name="TT_TZHCDBH"/>
					<input type="hidden" id="TT_TZHCDMC" name="TT_TZHCDMC"/>
					
					<input type="hidden" id="TT_BJ" name="TT_BJ"/>
					<input type="hidden" id="TT_KC" name="TT_KC"/>
					<input type="hidden" id="TT_LX" name="TT_LX"/>
				</form>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
	var sAuth = '<%=sAuth%>';
	var admin = '<%=MyTools.getProp(request, "Base.admin")%>';//管理员
	var jxzgxz = '<%=MyTools.getProp(request, "Base.jxzgxz")%>';//教学主管校长
	var qxjdzr = '<%=MyTools.getProp(request, "Base.qxjdzr")%>';//全校教导主任
	var qxjwgl = '<%=MyTools.getProp(request, "Base.qxjwgl")%>';//全校教务管理
	var xbjdzr = '<%=MyTools.getProp(request, "Base.xbjdzr")%>';//系部教导主任
	var xbjwgl = '<%=MyTools.getProp(request, "Base.xbjwgl")%>';//系部教务管理
	var iKeyCode = ''; //定义主键
	var curData = '';//临时行数据
	var pageNum = 1;   //datagrid初始当前页数
	var pageSize = 20; //datagrid初始页内行数
	var TT_XNXQMC_CX = '';//查询条件
	var TT_JXXZ_CX = '';
	var TT_XBMC_CX = '';
	var TT_KCMC_CX = '';
	var TT_BJMC_CX = '';
	var TT_SQLX_CX = '';
	var openMode = '';
	var tkState = '';
	var sqlxData = [{"comboName":"请选择","comboValue":""},{"comboName":"调课","comboValue":"1"},{"comboName":"停课","comboValue":"2"},{"comboName":"补课","comboValue":"3"}];
	var curXnxq = '';
	var tempXnxq = '';
	var yjhjsbhArray = '';
	var yjhjsmcArray = '';
	var yjhcdbhArray = '';
	var yjhcdmcArray = '';
	
	var teaComboData = '';
	var siteComboData = '';
	var submitFlag = false;
	
	$(document).ready(function(){
		initDialog();//初始化对话框
		initData();//页面初始化加载数据
		
		if(sAuth.indexOf(admin)>-1 || sAuth.indexOf(jxzgxz)>-1 || sAuth.indexOf(qxjdzr)>-1 || sAuth.indexOf(qxjwgl)>-1 || sAuth.indexOf(xbjdzr)>-1 || sAuth.indexOf(xbjwgl)>-1){
			$('#audit').show();
		}
	});
	
	/**页面初始化加载数据**/
	function initData(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Tkgl',
			data : 'active=initData&page=' + pageNum + '&rows=' + pageSize + '&sAuth=' + sAuth + '&TT_SXLX=1',
			dataType:"json",
			success : function(data) {
				curXnxq = data[0].curXnxq;
				listData = data[0].listData;
				loadRequestNoteGrid(data[0].listData);
				initCombobox(data[0].jxxzData, data[0].xbdmData);
				
				teaComboData = data[0].teaComboData;
				//siteComboData = data[0].siteComboData;
			}
		});
	}
	
	/**加载 dialog控件**/
	function initDialog(){
		$('#tkInfo').dialog({
			title:'课程调整申请',
			width:665,
			height:425,
			modal:true,
			closed:true,   
			cache:false, 
			draggable:false,//是否可移动dialog框设置
			//打开事件
			onOpen:function(data){},
			//读取事件
			onLoad:function(data){},
			onBeforeClose:function(){
				$('#TT_JXXZ').combobox('setValue', '');
				$('#mask').hide();
			},
			//关闭事件
			onClose:function(data){
				$('#form1 input[type=hidden]').val('');
				$('#TT_BGLY_C').val('');
				$('#TT_BGLY_S').val('');
				$('#TT_BGLY_B').val('');
				$('#save').show();
				$('#submit').show();
				$('#pass').show();
				$('#reject').show();
				$('#save').linkbutton('enable');
				$('#submit').linkbutton('enable');
				$('#pass').linkbutton('enable');
				$('#reject').linkbutton('enable');
				$('#TT_JXXZ').combobox('enable');
			}
		});
	}
	
	/**加载combobox控件
		@jxxzData 教学性质下拉框数据
	**/
	function initCombobox(jxxzData, xbdmData){
		$('#TT_JXXZ_CX').combobox({
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
		
		$('#TT_XBMC_CX').combobox({
			data:xbdmData,
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
		
		$('#TT_JXXZ').combobox({
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
			onChange:function(data){
				$('#tiaokeTable').hide();
				$('#tingkeTable').hide();
				$('#bukeTable').hide();
			
				if(data == ''){
					$('#TT_XBDM').combobox('setText', '请先选择教学性质');
					$('#TT_XBDM').combobox('disable');
					$('#TT_BJBH').combobox('setText', '请先选择系部');
					$('#TT_BJBH').combobox('disable');
					$('#TT_KCBH').combobox('setText', '请先选择班级');
					$('#TT_KCBH').combobox('disable');
					$('#TT_SQLX').combobox('setValue', '');
					$('#TT_SQLX').combobox('setText', '请先选择课程信息');
					$('#TT_SQLX').combobox('disable');
				}else{
					$('#TT_BJBH').combobox('setText', '请先选择系部');
					$('#TT_BJBH').combobox('disable');
					$('#TT_KCBH').combobox('setText', '请先选择班级');
					$('#TT_KCBH').combobox('disable');
					$('#TT_SQLX').combobox('setValue', '');
					$('#TT_SQLX').combobox('setText', '请先选择课程信息');
					$('#TT_SQLX').combobox('disable');
					
					//读取可调整的周次，星期等信息
					loadWeekInfo();
					
					$('#TT_XBDM').combobox({
						url:'<%=request.getContextPath()%>/Svl_Tkgl?active=loadCurDeptCombo&sAuth=' + sAuth + '&TT_XNXQBM=' + tempXnxq + $('#TT_JXXZ').combobox('getValue'),
						valueField:'comboValue',
						textField:'comboName',
						editable:false,
						panelHeight:'140', //combobox高度
						onLoadSuccess:function(data){
							//判断data参数是否为空
							if(data.length == 1){
								$(this).combobox('setText', '没有可选系部');
								$(this).combobox('disable');
							}else{
								$(this).combobox('enable');
								
								if(curData!='' && openMode!='new'){
									$(this).combobox('setValue', curData.系部代码);
									if(openMode=='edit' && curData.审核状态=='0') $(this).combobox('disable');
								}else{
									//初始化combobox时赋值
									$(this).combobox('setValue', '');
								}
							}
						},
						//下拉列表值改变事件
						onChange:function(data){
							$('#tiaokeTable').hide();
							$('#tingkeTable').hide();
							$('#bukeTable').hide();
							
							if(data == ''){
								$('#TT_BJBH').combobox('setText', '请先选择系部');
								$('#TT_BJBH').combobox('disable');
								$('#TT_KCBH').combobox('setText', '请先选择班级');
								$('#TT_KCBH').combobox('disable');
								$('#TT_SQLX').combobox('setValue', '');
								$('#TT_SQLX').combobox('setText', '请先选择课程信息');
								$('#TT_SQLX').combobox('disable');
							}else{
								$('#TT_KCBH').combobox('setText', '请先选择班级');
								$('#TT_KCBH').combobox('disable');
								$('#TT_SQLX').combobox('setValue', '');
								$('#TT_SQLX').combobox('setText', '请先选择课程信息');
								$('#TT_SQLX').combobox('disable');
								
								//读取班级下拉框
								$('#TT_BJBH').combobox({
									url:'<%=request.getContextPath()%>/Svl_Tkgl?active=loadClassCombo&sAuth=' + sAuth + '&TT_XNXQBM=' + tempXnxq + $('#TT_JXXZ').combobox('getValue') + '&TT_ZYDM=' + $('#TT_XBDM').combobox('getValue'),
									valueField:'comboValue',
									textField:'comboName',
									editable:false,
									panelHeight:'140', //combobox高度
									onLoadSuccess:function(data){
										//判断data参数是否为空
										if(data.length == 1){
											$(this).combobox('setText', '没有可选班级');
											$(this).combobox('disable');
										}else{
											$(this).combobox('enable');
											
											if(curData!='' && openMode!='new'){
												$(this).combobox('setValue', curData.班级编号);
												if(openMode=='edit' && curData.审核状态=='0') $(this).combobox('disable');
											}else{
												//初始化combobox时赋值
												$(this).combobox('setValue', '');
											}
											
											//读取班级所属系部相关教室信息
											$.ajax({
												type : "POST",
												url : '<%=request.getContextPath()%>/Svl_Tkgl',
												data : 'active=loadDeptSiteInfo&TT_ZYDM=' + $('#TT_XBDM').combobox('getValue'),
												dataType:"json",
												success : function(data) {
													siteComboData = data[0].siteComboData;
													
													//读取补课教师和地点下拉框数据
													loadBkTeaAndSiteCombobox();
												}
											});
										}
									},
									//下拉列表值改变事件
									onChange:function(data){
										$('#tiaokeTable').hide();
										$('#tingkeTable').hide();
										$('#bukeTable').hide();
									
										if(data == ''){
											$('#TT_KCBH').combobox('setText', '请先选择班级');
											$('#TT_KCBH').combobox('disable');
											$('#TT_SQLX').combobox('setValue', '');
											$('#TT_SQLX').combobox('setText', '请先选择课程信息');
											$('#TT_SQLX').combobox('disable');
										}else{
											$('#TT_SQLX').combobox('setValue', '');
											$('#TT_SQLX').combobox('setText', '请先选择课程信息');
											$('#TT_SQLX').combobox('disable');
										
											//读取课程下拉框
											$('#TT_KCBH').combobox({
												url:'<%=request.getContextPath()%>/Svl_Tkgl?active=loadCourseCombo&sAuth=' + sAuth + '&TT_XNXQBM=' + tempXnxq + $('#TT_JXXZ').combobox('getValue') + '&TT_ZYDM=' + $('#TT_XBDM').combobox('getValue') + '&TT_BJ=' + $('#TT_BJBH').combobox('getValue'),
												valueField:'comboValue',
												textField:'comboName',
												editable:false,
												panelHeight:'140', //combobox高度
												onLoadSuccess:function(data){
													//判断data参数是否为空
													if(data.length == 1){
														$(this).combobox('setText', '没有可选课程');
														$(this).combobox('disable');
													}else{
														$(this).combobox('enable');
														
														if(curData!='' && openMode!='new'){
															$(this).combobox('setValue', curData.授课计划明细编号);
															if(openMode=='edit' && curData.审核状态=='0') $(this).combobox('disable');
														}else{
															//初始化combobox时赋值
															$(this).combobox('setValue', '');
														}
													}
												},
												//下拉列表值改变事件
												onChange:function(data){
													$('#tiaokeTable').hide();
													$('#tingkeTable').hide();
													$('#bukeTable').hide();
												
													if(data == ''){
														$('#TT_SQLX').combobox('setValue', '');
														$('#TT_SQLX').combobox('setText', '请先选择课程信息');
														$('#TT_SQLX').combobox('disable');
													}else{
														//读取当前课程的周次信息
														loadSkzcInfo();
			
														$('#TT_SQLX').combobox({
															data:sqlxData,
															valueField:'comboValue',
															textField:'comboName',
															editable:false,
															panelHeight:'auto', //combobox高度
															onLoadSuccess:function(data){
																//判断data参数是否为空
																if(data != ''){
																	$(this).combobox('enable');
																
																	if(curData!='' && openMode!='new'){
																		$(this).combobox('setValue', curData.申请类型);
																		if(openMode=='edit' && curData.审核状态=='0') $(this).combobox('disable');
																	}else{
																		//初始化combobox时赋值
																		$(this).combobox('setValue', '');
																	}
																}
															},
															//下拉列表值改变事件
															onChange:function(data){
																$('#tiaokeTable').hide();
																$('#tingkeTable').hide();
																$('#bukeTable').hide();
																
																if(data == '1'){
																	$('#tiaokeTable').show();
																}
																if(data == '2'){
																	$('#tingkeTable').show();
																}
																if(data == '3'){
																	$('#bukeTable').show();
																}
																
																if(curData == ''){
																	//重置调整后时间信息
																	var data = $('#TT_TZHSKZC_C').combobox('getData');
																	$('#TT_TZHSKZC_C').combobox('setValue', data[0].comboValue);
																	$('#TT_TZHSKZC_B').combobox('setValue', data[0].comboValue);
																	data = $('#TT_TZHDAY_C').combobox('getData');
																	$('#TT_TZHDAY_C').combobox('setValue', data[0].comboValue);
																	$('#TT_TZHDAY_B').combobox('setValue', data[0].comboValue);
																	data = $('#TT_TZHJS_C').combobox('getData');
																	$('#TT_TZHJS_C').combobox('setValue', data[0].comboValue);
																	$('#TT_TZHJS_B').combobox('setValue', data[0].comboValue);
																}
															}
														});
													}
												}
											});
										}
									}
								});
							}
						}
					});
				}
			}
		});
	}
	
	//读取当前课程的周次信息
	function loadSkzcInfo(){
		$('#teaList').html('');
		$('#siteList').html('');
		
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Tkgl',
			data : 'active=loadSkzcInfo&TT_ID=' + iKeyCode + '&TT_SHZT=' + tkState + '&TT_SKJHMXBH=' + $('#TT_KCBH').combobox('getValue'),
			dataType:"json",
			success : function(data) {
				$('#TT_SKJHMXBH').val($('#TT_KCBH').combobox('getValue'));
				
				/**调课表格中的下拉框*/
				$('#TT_YJHSKZC_C').combobox({
					data:data[0].weekNum,
					valueField:'comboValue',
					textField:'comboName',
					editable:false,
					panelHeight:'140', //combobox高度
					onLoadSuccess:function(data){
						if(data != ''){
							if(curData!='' && openMode!='new'){
								if(curData.申请类型 == '1'){
									$(this).combobox('setValue', curData.原计划授课周次);
								}else{
									$(this).combobox('setValue', data[0].comboValue);
								}
							}else{
								$(this).combobox('setValue', data[0].comboValue);
							}
						}
					},
					//下拉列表值改变事件
					onChange:function(data){
						$('#TT_YJHDAY_C').combobox({
							url:'<%=request.getContextPath()%>/Svl_Tkgl?active=loadWeekday' + 
								'&TT_ID=' + iKeyCode + '&TT_SHZT=' + tkState + 
								'&TT_SKJHMXBH=' + $('#TT_KCBH').combobox('getValue') + 
								'&weekNum=' + $('#TT_YJHSKZC_C').combobox('getValues'),
							valueField:'comboValue',
							textField:'comboName',
							editable:false,
							panelHeight:'140', //combobox高度
							onLoadSuccess:function(data){
								if(data != ''){
									if(curData!='' && openMode!='new'){
										if(curData.申请类型 == '1'){
											$(this).combobox('setValue', curData.原计划星期);
										}else{
											$(this).combobox('setValue', data[0].comboValue);
										}
									}else{
										$(this).combobox('setValue', data[0].comboValue);
									}
								}
							},
							//下拉列表值改变事件
							onChange:function(data){
								$('#TT_YJHJS_C').combobox({
									url:'<%=request.getContextPath()%>/Svl_Tkgl?active=loadOrder' + 
										'&TT_ID=' + iKeyCode + '&TT_SHZT=' + tkState + 
										'&TT_SKJHMXBH=' + $('#TT_KCBH').combobox('getValue') + 
										'&weekNum=' + $('#TT_YJHSKZC_C').combobox('getValues') + 
										'&weekDay=' + $('#TT_YJHDAY_C').combobox('getValue'),
									valueField:'comboValue',
									textField:'comboName',
									multiple:true,
									editable:false,
									panelHeight:'140', //combobox高度
									onLoadSuccess:function(data){
										if(data != ''){
											if(curData!='' && openMode!='new'){
												if(curData.申请类型 == '1'){
													$(this).combobox('setValues', curData.原计划时间序列.split(','));
												}else{
													$(this).combobox('setValue', data[0].comboValue);
												}
											}else{
												$(this).combobox('setValue', data[0].comboValue);
											}
										}
									},
									onUnselect:function(record){
										if($(this).combobox('getValues') == ''){
											var data = $(this).combobox('getData');
											$(this).combobox('setValue', data[0].comboValue);
										}
									},
									//下拉列表值改变事件
									onChange:function(data){
										//读取教师和场地信息
										$.ajax({
											type : "POST",
											url : '<%=request.getContextPath()%>/Svl_Tkgl',
											data : 'active=loadTeaAndSiteInfo' +
												'&TT_ID=' + iKeyCode + '&TT_SHZT=' + tkState +  
												'&TT_SKJHMXBH=' + $('#TT_KCBH').combobox('getValue') + 
												'&weekNum=' + $('#TT_YJHSKZC_C').combobox('getValues') + 
												'&weekDay=' + $('#TT_YJHDAY_C').combobox('getValue') + 
												'&order=' + $('#TT_YJHJS_C').combobox('getValues'),
											dataType:"json",
											success : function(data) {
												if(data[0].sameFlag == 'true'){
													createTeaAndSiteInfo(data[0].teaCode, data[0].teaName, data[0].siteCode, data[0].siteName);
													$('#SKJS_S').html(data[0].teaName);
													$('#SKJS_B').combobox('setValues', data[0].teaCode.split(','));
													/*
													$('#SKJS_B').combotree('setValues', data[0].teaCode.split(','));
													//展开所有选中的节点
													$('#SKJS_B').combotree('tree').tree("collapseAll");
													var selArray = $('#SKJS_B').combotree('tree').tree('getChecked');
													for(var i=0; i<selArray.length; i++){
														$('#SKJS_B').combotree('tree').tree('expandTo', selArray[i].target);
													}
													*/
													
													$('#SKDD_S').html(data[0].siteName);
													$('#SKDD_B').combobox('setValues', data[0].siteCode.split(','));
													/*
													$('#SKDD_B').combotree('setValues', data[0].siteCode.split(','));
													//展开所有选中的节点
													$('#SKDD_B').combotree('tree').tree("collapseAll");
													selArray = $('#SKDD_B').combotree('tree').tree('getChecked');
													for(var i=0; i<selArray.length; i++){
														$('#SKDD_B').combotree('tree').tree('expandTo', selArray[i].target);
													}
													*/
												}else{
													yjhjsbhArray = data[0].teaCode;
													yjhjsmcArray = data[0].teaName;
													yjhcdbhArray = data[0].siteCode;
													yjhcdmcArray = data[0].siteName;
													$('#teaList').html('<span style="width:100%; margin-top:5px; text-align:center;">当前选择的课程时间的授课教师不同，无法统一设置。</span>');
													$('#SKJS_S').html(data[0].teaName);
													$('#SKJS_B').combobox('setValues', data[0].teaCode.split(','));
													//$('#SKJS_B').combotree('setValues', data[0].teaCode.split(','));
													$('#siteList').html('<span style="width:100%; margin-top:5px; text-align:center;">当前选择的课程时间的授课地点不同，无法统一设置。</span>');
													$('#SKDD_S').html(data[0].siteName);
													$('#SKDD_B').combobox('setValues', data[0].siteCode.split(','));
													//$('#SKDD_B').combotree('setValues', data[0].siteCode.split(','));
												}
											}
										});
									}
								});
							}
						});
					}
				});
				
				/**停课表格中的下拉框*/
				$('#TT_YJHSKZC_S').combobox({
					data:data[0].weekNum,
					valueField:'comboValue',
					textField:'comboName',
					editable:false,
					panelHeight:'140', //combobox高度
					onLoadSuccess:function(data){
						if(data != ''){
							if(curData!='' && openMode!='new'){
								if(curData.申请类型 == '2'){
									$(this).combobox('setValues', curData.原计划授课周次.split(','));
								}else{
									$(this).combobox('setValue', data[0].comboValue);
								}
							}else{
								$(this).combobox('setValue', data[0].comboValue);
							}
						}
					},
					//下拉列表值改变事件
					onChange:function(data){
						$('#TT_YJHDAY_S').combobox({
							url:'<%=request.getContextPath()%>/Svl_Tkgl?active=loadWeekday' + 
								'&TT_SKJHMXBH=' + $('#TT_KCBH').combobox('getValue') + 
								'&weekNum=' + $('#TT_YJHSKZC_S').combobox('getValues'),
							valueField:'comboValue',
							textField:'comboName',
							editable:false,
							panelHeight:'140', //combobox高度
							onLoadSuccess:function(data){
								if(data != ''){
									if(curData!='' && openMode!='new'){
										if(curData.申请类型 == '2'){
											$(this).combobox('setValue', curData.原计划星期.split(','));
										}else{
											$(this).combobox('setValue', data[0].comboValue);
										}
									}else{
										$(this).combobox('setValue', data[0].comboValue);
									}
								}
							},
							//下拉列表值改变事件
							onChange:function(data){
								$('#TT_YJHJS_S').combobox({
									url:'<%=request.getContextPath()%>/Svl_Tkgl?active=loadOrder' + 
										'&TT_SKJHMXBH=' + $('#TT_KCBH').combobox('getValue') + 
										'&weekNum=' + $('#TT_YJHSKZC_S').combobox('getValues') + 
										'&weekDay=' + $('#TT_YJHDAY_S').combobox('getValue'),
									valueField:'comboValue',
									textField:'comboName',
									multiple:true,
									editable:false,
									panelHeight:'140', //combobox高度
									onLoadSuccess:function(data){
										if(data != ''){
											if(curData!='' && openMode!='new'){
												if(curData.申请类型 == '2'){
													$(this).combobox('setValues', curData.原计划时间序列.split(','));
												}else{
													$(this).combobox('setValue', data[0].comboValue);
												}
											}else{
												$(this).combobox('setValue', data[0].comboValue);
											}
										}
									},
									onUnselect:function(record){
										if($(this).combobox('getValues') == ''){
											var data = $(this).combobox('getData');
											$(this).combobox('setValue', data[0].comboValue);
										}
									},
									//下拉列表值改变事件
									onChange:function(data){
										//读取教师和场地信息
										$.ajax({
											type : "POST",
											url : '<%=request.getContextPath()%>/Svl_Tkgl',
											data : 'active=loadTeaAndSiteInfo&TT_SKJHMXBH=' + $('#TT_KCBH').combobox('getValue') + 
												'&weekNum=' + $('#TT_YJHSKZC_S').combobox('getValues') + '&weekDay=' + $('#TT_YJHDAY_S').combobox('getValue') + 
												'&order=' + $('#TT_YJHJS_S').combobox('getValues'),
											dataType:"json",
											success : function(data) {
												$('#TT_YJHJSMC').val(data[0].teaName);
												$('#TT_YJHCDMC').val(data[0].siteName);
											}
										});
									}
								});
							}
						});
					}
				});
			}
		});
	}
	
	/**创建调整教师和场地信息*/
	function createTeaAndSiteInfo(teaCode, teaName, siteCode, siteName){
		$('#TT_YJHJSMC').val(teaName);
		$('#TT_YJHCDMC').val(siteName);
		
		//加载调整教师相关信息
		var teaCodeArray = teaCode.split(',');
		var teaNameArray = teaName.split(',');
		var teaContent = '';
		var tempName = '';
		
		//生成信息
		for(var i=0; i<teaCodeArray.length; i++){
			tempName = teaNameArray[i];
			teaContent += '<div style="margin-top:5px; margin-left:10px;">';
			if(teaNameArray[i].length == 2){
				tempName = teaName.substring(0, 1) + '<span style="width:12px;"></span>' + teaName.substring(teaName.length-1, teaName.length);
			}else{
				tempName = teaNameArray[i];
			}
			teaContent += tempName + '&nbsp;&nbsp;&nbsp;&nbsp;===》&nbsp;' +
						'<select id="tzhjs_'+i+'" class="easyui-combobox" style="width:150px;">' +
						'</select></div>';
		}
		yjhjsbhArray = teaCodeArray.slice(0);
		yjhjsmcArray = teaNameArray.slice(0);
		$('#teaList').html(teaContent);
		
		//初始化combobox
		for(var i=0; i<teaCodeArray.length; i++){
			//20170425yeq修改,原combotree页面效率太慢,修改为combobox
			$('#tzhjs_' + i).combobox({
				data:teaComboData,
				valueField:'comboValue',
				textField:'comboText',
				editable:true,
				panelHeight:'140', //combobox高度
				onLoadSuccess:function(data){
					if(curData!='' && openMode!='new'){
						if(curData.申请类型 == '1'){
							$('#tzhjs_' + i).combobox('setValue', curData.调整后授课教师编号.split(',')[i]);
						}else{
							$('#tzhjs_' + i).combobox('setValue', teaCodeArray[i]);
						}
					}else{
						$('#tzhjs_' + i).combobox('setValue', teaCodeArray[i]);
					}
				}
			});
			/*
			$('#tzhjs_' + i).combotree({
				data:teaComboData,
	            valueField: 'id',
	            textField: 'text',
	            //required: true,
	            editable: false,
	            panelHeight:'140', //combobox高度
	            onlyLeafCheck:true,
	            //cascadeCheck:false,
	            onLoadSuccess:function(node, data){
					$('#tzhjs_' + i).combotree('tree').tree("collapseAll");
					
					if(curData!='' && openMode!='new'){
						if(curData.申请类型 == '1'){
							$('#tzhjs_' + i).combotree('setValue', curData.调整后授课教师编号.split(',')[i]);
						}else{
							$('#tzhjs_' + i).combotree('setValue', teaCodeArray[i]);
						}
					}else{
						$('#tzhjs_' + i).combotree('setValue', teaCodeArray[i]);
					}
					
					//展开所有选中的节点
					var selArray = $('#tzhjs_' + i).combotree('tree').tree('getSelected');
            		$('#tzhjs_' + i).combotree('tree').tree('expandTo', selArray.target);
	            },
	            onBeforeSelect:function(node){
	            	if(node.id.substring(0, 1) == 'C'){
	            		return false;
	            	}
	            }
			});
			*/
		}
		
		//加载调整场地相关信息
		var siteCodeArray = siteCode.split(',');
		var siteNameArray = siteName.split(',');
		var siteContent = '';
		
		//生成信息
		for(var i=0; i<siteCodeArray.length; i++){
			siteContent += '<div style="margin-top:5px; margin-left:10px;">' +
						siteNameArray[i] + '&nbsp;&nbsp;&nbsp;&nbsp;===》&nbsp;' +
						'<select name="tzhcd_'+i+'" id="tzhcd_'+i+'" class="easyui-combobox" style="width:150px;">' +
						'</select></div>';
		}
		yjhcdbhArray = siteCodeArray.slice(0);
		yjhcdmcArray = siteNameArray.slice(0);
		$('#siteList').html(siteContent);
		
		//初始化combotree
		for(var i=0; i<siteCodeArray.length; i++){
			//20170425yeq修改,原combotree页面效率太慢,修改为combobox
			$('#tzhcd_' + i).combobox({
				data:siteComboData,
				valueField:'comboValue',
				textField:'comboText',
				editable:true,
				panelHeight:'140', //combobox高度
				onLoadSuccess:function(data){
					if(curData!='' && openMode!='new'){
						if(curData.申请类型 == '1'){
							$('#tzhcd_' + i).combobox('setValue', curData.调整后场地编号.split(',')[i]);
						}else{
							$('#tzhcd_' + i).combobox('setValue', siteCodeArray[i]);
						}
					}else{
						$('#tzhcd_' + i).combobox('setValue', siteCodeArray[i]);
					}
				}
			});
			/*
			$('#tzhcd_' + i).combotree({
				data:siteComboData,
	            valueField: 'id',
	            textField: 'text',
	            //required: true,
	            editable: false,
	            panelHeight:'140', //combobox高度
	            onlyLeafCheck:true,
	            //cascadeCheck:false,
	            onLoadSuccess:function(node, data){
					$('#tzhcd_' + i).combotree('tree').tree("collapseAll");
					
					if(curData!='' && openMode!='new'){
						if(curData.申请类型 == '1'){
							$('#tzhcd_' + i).combotree('setValue', curData.调整后场地编号.split(',')[i]);
						}else{
							$('#tzhcd_' + i).combotree('setValue', siteCodeArray[i]);
						}
					}else{
						$('#tzhcd_' + i).combotree('setValue', siteCodeArray[i]);
					}
					
					//展开所有选中的节点
					var selArray = $('#tzhcd_' + i).combotree('tree').tree('getSelected');
            		$('#tzhcd_' + i).combotree('tree').tree('expandTo', selArray.target);
	            },
	            onBeforeSelect:function(node){
	            	if(node.id.length < 3){
	            		return false;
	            	}
	            },
	            //下拉列表值选择事件
				onCheck:function(node, checked){
				}
			});
			*/
		}
	}
	
	//读取可调整的周次，星期等信息
	function loadWeekInfo(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Tkgl',
			data : 'active=loadWeekInfo&TT_XNXQBM=' + tempXnxq + $('#TT_JXXZ').combobox('getValue'),
			dataType:"json",
			success : function(data) {
				if(data!='' && data!=null){
					$('#TT_TZHSKZC_C').combobox({
						data:data[0].weekNum,
						valueField:'comboValue',
						textField:'comboName',
						editable:false,
						panelHeight:'140', //combobox高度
						onLoadSuccess:function(data){
							if(data != ''){
								if(curData!='' && openMode!='new'){
									if(curData.申请类型 == '1'){
										$(this).combobox('setValues', curData.调整后授课周次.split(','));
									}else{
										$(this).combobox('setValue', data[0].comboValue);
									}
								}else{
									$(this).combobox('setValue', data[0].comboValue);
								}
							}
						},
						//下拉列表值改变事件
						onChange:function(data){
						}
					});
				
					$('#TT_TZHDAY_C').combobox({
						data:data[0].dayNum,
						valueField:'comboValue',
						textField:'comboName',
						editable:false,
						panelHeight:'140', //combobox高度
						onLoadSuccess:function(data){
							if(data != ''){
								if(curData!='' && openMode!='new'){
									if(curData.申请类型 == '1'){
										$(this).combobox('setValue', curData.调整后星期);
									}else{
										$(this).combobox('setValue', data[0].comboValue);
									}
								}else{
									$(this).combobox('setValue', data[0].comboValue);
								}
							}
						},
						//下拉列表值改变事件
						onChange:function(data){
						}
					});
					
					$('#TT_TZHJS_C').combobox({
						data:data[0].lessonNum,
						valueField:'comboValue',
						textField:'comboName',
						multiple:true,
						editable:false,
						panelHeight:'140', //combobox高度
						onLoadSuccess:function(data){
							if(data != ''){
								if(curData!='' && openMode!='new'){
									if(curData.申请类型 == '1'){
										$(this).combobox('setValues', curData.调整后时间序列.split(','));
									}else{
										$(this).combobox('setValue', data[0].comboValue);
									}
								}else{
									$(this).combobox('setValue', data[0].comboValue);
								}
							}
						},
						onUnselect:function(record){
							if($(this).combobox('getValues') == ''){
								var data = $(this).combobox('getData');
								$(this).combobox('setValue', data[0].comboValue);
							}
						},
						//下拉列表值改变事件
						onChange:function(data){
						}
					});
					
					$('#TT_TZHSKZC_B').combobox({
						data:data[0].weekNum,
						valueField:'comboValue',
						textField:'comboName',
						multiple:true,
						editable:false,
						panelHeight:'140', //combobox高度
						onLoadSuccess:function(data){
							if(data != ''){
								if(curData!='' && openMode!='new'){
									if(curData.申请类型 == '3'){
										$(this).combobox('setValues', curData.调整后授课周次.split(','));
									}else{
										$(this).combobox('setValue', data[0].comboValue);
									}
								}else{
									$(this).combobox('setValue', data[0].comboValue);
								}
							}
						},
						onUnselect:function(record){
							if($(this).combobox('getValues') == ''){
								var data = $(this).combobox('getData');
								$(this).combobox('setValue', data[0].comboValue);
							}
						},
						//下拉列表值改变事件
						onChange:function(data){
						}
					});
				
					$('#TT_TZHDAY_B').combobox({
						data:data[0].dayNum,
						valueField:'comboValue',
						textField:'comboName',
						editable:false,
						panelHeight:'140', //combobox高度
						onLoadSuccess:function(data){
							if(data != ''){
								if(curData!='' && openMode!='new'){
									if(curData.申请类型 == '3'){
										$(this).combobox('setValue', curData.调整后星期);
									}else{
										$(this).combobox('setValue', data[0].comboValue);
									}
								}else{
									$(this).combobox('setValue', data[0].comboValue);
								}
							}
						},
						//下拉列表值改变事件
						onChange:function(data){
						}
					});
					
					$('#TT_TZHJS_B').combobox({
						data:data[0].lessonNum,
						valueField:'comboValue',
						textField:'comboName',
						multiple:true,
						editable:false,
						panelHeight:'140', //combobox高度
						onLoadSuccess:function(data){
							if(data != ''){
								if(curData!='' && openMode!='new'){
									if(curData.申请类型 == '3'){
										$(this).combobox('setValues', curData.调整后时间序列.split(','));
									}else{
										$(this).combobox('setValue', data[0].comboValue);
									}
								}else{
									$(this).combobox('setValue', data[0].comboValue);
								}
							}
						},
						onUnselect:function(record){
							if($(this).combobox('getValues') == ''){
								var data = $(this).combobox('getData');
								$(this).combobox('setValue', data[0].comboValue);
							}
						},
						//下拉列表值改变事件
						onChange:function(data){
						}
					});
				}
			}
		});
	}
	
	/**读取补课教师和地点下拉框数据*/
	/*
	function loadBkTeaAndSiteComboTree(){
		//补课教师下拉框
		$('#SKJS_B').combotree({
			data:teaComboData,
            valueField: 'id',
            textField: 'text',
            //required: true,
            multiple:true,
            editable: false,
            panelHeight:'140', //combobox高度
            onlyLeafCheck:true,
            //cascadeCheck:false,
            onLoadSuccess:function(node, data){
				if(curData!='' && openMode!='new'){
					if(curData.申请类型 == '3'){
						$('#SKJS_B').combotree('setValues', curData.调整后授课教师编号.split(','));
					}
				}
            },
            onBeforeSelect:function(node){
            	if($('#SKJS_B').combotree('tree').tree('getParent', node.target) == null){
		    		return false;
		    	}
            },
            //下拉列表值选择事件
			onCheck:function(node, checked){
			}
		});
	
		//补课地点下拉框
		$('#SKDD_B').combotree({
			data:siteComboData,
            valueField: 'id',
            textField: 'text',
            //required: true,
            multiple:true,
            editable: false,
            panelHeight:'140', //combobox高度
            onlyLeafCheck:true,
            //cascadeCheck:false,
            onLoadSuccess:function(node, data){
				if(curData!='' && openMode!='new'){
					if(curData.申请类型 == '3'){
						$('#SKDD_B').combotree('setValues', curData.调整后场地编号.split(','));
					}
				}
            },
            onBeforeSelect:function(node){
            	if($('#SKDD_B').combotree('tree').tree('getParent', node.target) == null){
		    		return false;
		    	}
            },
            //下拉列表值选择事件
			onCheck:function(node, checked){
			}
		});
	}
	*/
	
	/**读取补课教师和地点下拉框数据*/
	function loadBkTeaAndSiteCombobox(){
		//补课教师下拉框
		$('#SKJS_B').combobox({
			data:teaComboData,
            valueField: 'comboValue',
            textField: 'comboText',
            //required: true,
            multiple:true,
            editable:true,
            panelHeight:'140', //combobox高度
            onlyLeafCheck:true,
            //cascadeCheck:false,
            onLoadSuccess:function(node, data){
				if(curData!='' && openMode!='new'){
					if(curData.申请类型 == '3'){
						$('#SKJS_B').combobox('setValues', curData.调整后授课教师编号.split(','));
					}
				}
            }
		});
	
		//补课地点下拉框
		$('#SKDD_B').combobox({
			data:siteComboData,
            valueField: 'comboValue',
            textField: 'comboText',
            //required: true,
            multiple:true,
            editable:true,
            panelHeight:'140', //combobox高度
            onlyLeafCheck:true,
            //cascadeCheck:false,
            onLoadSuccess:function(node, data){
				if(curData!='' && openMode!='new'){
					if(curData.申请类型 == '3'){
						$('#SKDD_B').combobox('setValues', curData.调整后场地编号.split(','));
					}
				}
            }
		});
	}
	
	/**读取申请表datagrid数据**/
	function loadRequestNoteData(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Tkgl',
			data : 'active=queRequestNote&page=' + pageNum + '&rows=' + pageSize + '&sAuth=' + sAuth +
				'&TT_XNXQMC_CX=' + encodeURI(TT_XNXQMC_CX) + 
				'&TT_JXXZ_CX=' + TT_JXXZ_CX +
				'&TT_XBMC_CX=' + TT_XBMC_CX + 
				'&TT_KCMC_CX=' + encodeURI(TT_KCMC_CX) +
				'&TT_BJMC_CX=' + encodeURI(TT_BJMC_CX) +
				'&TT_SQLX_CX=' + TT_SQLX_CX +
				'&TT_SXLX=1',
			dataType:"json",
			success : function(data) {
				loadRequestNoteGrid(data[0].listData);
			}
		});
	}
	
	/**加载学年学期课程表datagrid控件，读取页面信息
		@listData 列表数据
	**/
	function loadRequestNoteGrid(listData){
		$('#requestNoteList').datagrid({
			//url:'semesterList.json',
			data:listData,
			title:'课程调整申请单列表',
			width:'100%',
			nowrap: false,
			fit:true, //自适应父节点宽度和高度
			showFooter:true,
			striped:true,
			pagination:true,
			pageSize:pageSize,
			pageNumber:pageNum,
			singleSelect:true,
			rownumbers:true,
			fitColumns: true,
			columns:[[
				//field为读取数据的数据名，title为显示的数据名，width宽度设置，align数字在表格中显示的位置
				//编号,申请类型,学年学期编码,学年学期名称,课程编号,课程名称,班级编号,行政班名称,授课计划明细编号,变更理由,原计划授课周次,
				//原计划星期,原计划时间序列,原计划授课教师名称,原计划场地名称,调整授课周次,调整后星期,调整后时间序列,其他说明,审核状态
				{field:'学年学期名称',title:'学年学期名称',width:fillsize(0.1),align:'center'},
				{field:'课程名称',title:'课程名称',width:fillsize(0.15),align:'center'},
				{field:'行政班名称',title:'班级名称',width:fillsize(0.1),align:'center'},
				{field:'申请类型',title:'申请类型',width:fillsize(0.1),align:'center',
					formatter:function(value,rec){
						if(value == '1') return '调课';
						if(value == '2') return '停课';
						if(value == '3') return '补课';
					}
				},
				{field:'变更理由',title:'变更理由',width:fillsize(0.35),align:'center'},
				{field:'申请人',title:'申请人',width:fillsize(0.1),align:'center'},
				{field:'审核状态',title:'审核状态',width:fillsize(0.1),align:'center',
					formatter:function(value,rec){
						if(value == '0') return '未提交';
						if(value == '1') return '审核中';
						if(value == '2') return '审核通过';
						if(value == '3') return '审核驳回';
					}
				}
			]],
			//双击某行时触发
			onDblClickRow:function(rowIndex,rowData){
				//doToolbar('edit');
			},
			//读取datagrid之前加载
			onBeforeLoad:function(){},
			//单击某行时触发
			onClickRow:function(rowIndex,rowData){
				//主键赋值
				iKeyCode = rowData.编号;
				curData = rowData;
			},
			//加载成功后触发
			onLoadSuccess: function(data){
				iKeyCode = '';
				curData = '';
			}
		});
		
		$("#requestNoteList").datagrid("getPager").pagination({ 
			total:listData.total,
			afterPageText: '页&nbsp;<a href="#" onclick="goEnterPage();">Go</a>&nbsp;&nbsp;&nbsp;共 {pages} 页',
			onSelectPage:function (pageNo, pageSize_1) {
				pageNum = pageNo;
				pageSize = pageSize_1;
				loadRequestNoteData();
			} 
		});
	};
	
	function goEnterPage(){
		var e = jQuery.Event("keydown");//模拟一个键盘事件 
		e.keyCode = 13;//keyCode=13是回车 
		$("input.pagination-num").trigger(e);//模拟页码框按下回车 
	}
	
	/**工具栏按钮调用方法，传入按钮的id
		@id 当前按钮点击事件
	**/
	function doToolbar(id, PK_XNXQBM, nodeId){
		//查询学年学期列表
		if(id == 'queRequestNote'){
			pageNum = 1;
			TT_XNXQMC_CX = $('#TT_XNXQMC_CX').textbox('getValue'); 
			TT_JXXZ_CX = $('#TT_JXXZ_CX').combobox('getValue');
			TT_XBMC_CX = $('#TT_XBMC_CX').combobox('getValue');
			TT_KCMC_CX = $('#TT_KCMC_CX').textbox('getValue');
			TT_BJMC_CX = $('#TT_BJMC_CX').textbox('getValue');
			TT_SQLX_CX = $('#TT_SQLX_CX').combobox('getValue');
			
			loadRequestNoteData();
		}
		
		//新建
		if(id == 'new'){
			openMode = id;
			tkState = '';
			tempXnxq = curXnxq;
			$('#TT_JXXZ').combobox('setValue', '01');
			$('#pass').hide();
			$('#reject').hide();
			$('#submit').linkbutton('disable');
			$('#tkInfo').dialog('open');
		}
		
		//编辑
		if(id == 'edit'){
			openMode = id;
			tkState = curData.审核状态;
			
			if(iKeyCode == ''){
				alertMsg('请先选择一行数据');
				return;
			}
			$('#pass').hide();
			$('#reject').hide();
			
			$('#TT_ID').val(iKeyCode);
			tempXnxq = curData.学年学期编码.substring(0, 5);
			if(curData.审核状态 > 0){
				$('#mask').show();
				$('#save').linkbutton('disable');
				$('#submit').linkbutton('disable');
			}
			//表单赋值
			$('#TT_JXXZ').combobox('setValue', curData.学年学期编码.substring(5));
			if(curData.审核状态 == '0') $('#TT_JXXZ').combobox('disable');
			
			if(curData.申请类型 == '1'){
				$('#TT_BGLY_C').val(curData.变更理由);
				$('#TT_QTSM').val(curData.其他说明);
			}else if(curData.申请类型 == '2'){
				$('#TT_BGLY_S').val(curData.变更理由);
			}else if(curData.申请类型 == '3'){
				$('#TT_BGLY_B').val(curData.变更理由);
			}
			
			$('#tkInfo').dialog('open');
		}
		
		//审核
		if(id == 'audit'){
			openMode = id;
		
			if(iKeyCode == ''){
				alertMsg('请先选择一行数据');
				return;
			}
			if(curData.审核状态 == '0'){
				alertMsg('当前选择的调课申请还未提交，无需审核。');
				return;
			}
			if(curData.审核状态=='2'){
				alertMsg('当前选择的调课申请已审核通过，无需审核。');
				return;
			}
			if(curData.审核状态=='3'){
				alertMsg('当前选择的调课申请已审核驳回，无需审核。');
				return;
			}
			
			$('#save').hide();
			$('#submit').hide();
			$('#mask').show();
			
			$('#TT_ID').val(iKeyCode);
			tempXnxq = curData.学年学期编码.substring(0, 5);
			
			//表单赋值
			$('#TT_JXXZ').combobox('setValue', curData.学年学期编码.substring(5));
			
			if(curData.申请类型 == '1'){
				$('#TT_BGLY_C').val(curData.变更理由);
				$('#TT_QTSM').val(curData.其他说明);
			}else{
				$('#TT_BGLY_S').val(curData.变更理由);
			}
			
			$('#tkInfo').dialog('open');
		}
		
		//保存
		if(id == 'save'){
			if($('#TT_JXXZ').combobox('getValue') == ''){
				alertMsg('请选择教学性质');
				return;
			}
			if($('#TT_XBDM').combobox('getValue') == ''){
				alertMsg('请选择系部');
				return;
			}
			if($('#TT_KCBH').combobox('getValue') == ''){
				alertMsg('请选择课程');
				return;
			}
			if($('#TT_BJBH').combobox('getValue') == ''){
				alertMsg('请选择班级');
				return;
			}
			if($('#TT_SQLX').combobox('getValue') == ''){
				alertMsg('请选择申请类型');
				return;
			}
			
			//根据不同的调整类型，选择不同的提交数据
			//调课
			if($('#TT_SQLX').combobox('getValue') == '1'){
				if($('#TT_YJHJS_C').combobox('getValues') == ''){
					alertMsg('请选择原计划任课时间');
					return;
				}
				if($('#TT_TZHJS_C').combobox('getValues') == ''){
					alertMsg('请选择调整后任课时间');
					return;
				}
				if($('#TT_YJHJS_C').combobox('getValues').length != $('#TT_TZHJS_C').combobox('getValues').length){
					alertMsg('原计划与调整后的节数量必须一致');
					return;
				}
				
				var yjhSkzc = $('#TT_YJHSKZC_C').combobox('getValue');
				var tzhSkzc = $('#TT_TZHSKZC_C').combobox('getValue');
				var yjhXq = $('#TT_YJHDAY_C').combobox('getValue');
				var tzhXq = $('#TT_TZHDAY_C').combobox('getValue');
				var yjhJs = sortTime($('#TT_YJHJS_C').combobox('getValues'));
				var tzhJs = sortTime($('#TT_TZHJS_C').combobox('getValues'));
				
				/**检查选择的调课时间是否符合逻辑**/
				var resultArray = checkTime(yjhSkzc, tzhSkzc, yjhXq, tzhXq, yjhJs, tzhJs);
				if(resultArray[0] == false){
					alertMsg(resultArray[1]);
					return;
				}
				
				$('#TT_BGLY').val($('#TT_BGLY_C').val());//变更理由
				$('#TT_YJHSKZC').val(yjhSkzc);//授课周次
				$('#TT_YJHXQ').val(yjhXq);//时间序列
				$('#TT_YJHSJXL').val(yjhJs);
				
				$('#TT_TZHSKZC').val(tzhSkzc);
				$('#TT_TZHXQ').val(tzhXq);
				$('#TT_TZHSJXL').val(tzhJs);
				
				$('#TT_YJHSKJSBH').val(yjhjsbhArray.toString());//授课教师
				$('#TT_YJHSKJSMC').val(yjhjsmcArray.toString().replace(new RegExp('<span style="width:12px;"></span>', 'g'),''));
				var teaCode = "";
				var teaName = "";
				for(var i=0; i<yjhjsbhArray.length; i++){
					//teaCode += $('#tzhjs_'+i).combotree('getValue')+',';
					//teaName += $('#tzhjs_'+i).combotree('getText')+',';
					teaCode += $('#tzhjs_'+i).combobox('getValue')+',';
					teaName += $('#tzhjs_'+i).combobox('getText')+',';
				}
				$('#TT_TZHSKJSBH').val(teaCode.substring(0, teaCode.length-1));
				$('#TT_TZHSKJSMC').val(teaName.substring(0, teaName.length-1));
				
				$('#TT_YJHCDBH').val(yjhcdbhArray.toString());//授课地点
				$('#TT_YJHCDMC').val(yjhcdmcArray.toString());
				var siteCode = "";
				var siteName = "";
				for(var i=0; i<yjhcdbhArray.length; i++){
					//siteCode += $('#tzhcd_'+i).combotree('getValue')+',';
					//siteName += $('#tzhcd_'+i).combotree('getText').substring(0, $('#tzhcd_'+i).combotree('getText').lastIndexOf('('))+',';
					siteCode += $('#tzhcd_'+i).combobox('getValue')+',';
					siteName += $('#tzhcd_'+i).combobox('getText').substring(0, $('#tzhcd_'+i).combobox('getText').lastIndexOf('（'))+',';
				}
				$('#TT_TZHCDBH').val(siteCode.substring(0, siteCode.length-1));
				$('#TT_TZHCDMC').val(siteName.substring(0, siteName.length-1));
				
				if($('#TT_YJHSKJSBH').val()==''){
					alertMsg('当前选择的课程时间的授课教师不同，无法统一设置，请重新选择任课时间。');
					return;
				}
				
				if($('#TT_YJHCDBH').val()==''){
					alertMsg('当前选择的课程时间的授课地点不同，无法统一设置，请重新选择任课时间。');
					return;
				}
			}
			
			//停课
			if($('#TT_SQLX').combobox('getValue') == '2'){
				if($('#TT_YJHJS_S').combobox('getValues') == ''){
					alertMsg('请选择原计划任课时间');
					return;
				}
				
				$('#TT_BGLY').val($('#TT_BGLY_S').val());
				$('#TT_YJHSKJSBH').val(yjhjsbhArray.toString());//授课教师
				$('#TT_YJHSKJSMC').val(yjhjsmcArray.toString().replace(new RegExp('<span style="width:12px;"></span>', 'g'),''));
				$('#TT_YJHCDBH').val(yjhcdbhArray.toString());//授课地点
				$('#TT_YJHCDMC').val(yjhcdmcArray.toString());
				$('#TT_YJHSKZC').val($('#TT_YJHSKZC_S').combobox('getValues').toString());//授课周次
				$('#TT_YJHXQ').val($('#TT_YJHDAY_S').combobox('getValue'));//时间序列
				$('#TT_YJHSJXL').val(sortTime($('#TT_YJHJS_S').combobox('getValues')));
			}
			
			//补课
			if($('#TT_SQLX').combobox('getValue') == '3'){
				if($('#SKJS_B').combobox('getValue') == undefined){
					alertMsg('请选择授课教师');
					return;
				}
				if($('#SKDD_B').combobox('getValue') == undefined){
					alertMsg('请选择补课地点');
					return;
				}
				$('#TT_YJHJSMC').val('');
				$('#TT_YJHCDMC').val('');
				$('#TT_BGLY').val($('#TT_BGLY_B').val());//变更理由
				$('#TT_TZHSKZC').val(sortTime($('#TT_TZHSKZC_B').combobox('getValues')));//授课周次
				$('#TT_TZHXQ').val($('#TT_TZHDAY_B').combobox('getValue'));//时间序列
				$('#TT_TZHSJXL').val(sortTime($('#TT_TZHJS_B').combobox('getValues')));
				$('#TT_TZHSKJSBH').val($('#SKJS_B').combobox('getValues'));//授课教师
				//$('#TT_TZHSKJSMC').val(yjhjsmcArray.toString().replace(new RegExp('<span style="width:12px;"></span>', 'g'),''));
				$('#TT_TZHSKJSMC').val($('#SKJS_B').combobox('getText'));
				$('#TT_TZHCDBH').val($('#SKDD_B').combobox('getValues'));
				var tempSiteNameArray = $('#SKDD_B').combobox('getText').split(',');
				var tempSiteName = '';
				var siteName = '';
				for(var i=0; i<tempSiteNameArray.length; i++){
					tempSiteName = tempSiteNameArray[i];
					tempSiteName = tempSiteName.substring(0, tempSiteName.lastIndexOf('（'));
					siteName += tempSiteName+',';
				}
				$('#TT_TZHCDMC').val(siteName.substring(0, siteName.length-1));
			}
			$('#TT_KC').val($('#TT_KCBH').combobox('getValue'));
			$('#TT_BJ').val($('#TT_BJBH').combobox('getValue'));
			$('#TT_LX').val($('#TT_SQLX').combobox('getValue'));
			$('#active').val(id);
			$('#TT_SXLX').val('1');
			$('#TT_XNXQBM').val(tempXnxq + $('#TT_JXXZ').combobox('getValue'));
			$("#form1").submit();
			tag = 1;
		}
		
		//提交
		if(id == 'submit'){
			submitFlag = true;
			doToolbar('save');
		}
		
		//通过/驳回
		if(id=='pass' || id=='reject'){
			$('#active').val(id);
			$("#form1").submit();
		}
	}
	
	/**表单提交**/
	$('#form1').form({
		//定位到servlet位置的url
		url:'<%=request.getContextPath()%>/Svl_Tkgl',
		//当点击事件后触发的事件
		onSubmit: function(data){}, 
		//当点击事件并成功提交后触发的事件
		success:function(data){
			var json  =  eval("("+data+")");
			if(json[0].MSG == '保存成功'){
				if($('#active').val() == 'save'){
					$('#TT_ID').val(json[0].ID);
					
					$('#TT_JXXZ').combobox('disable');
					$('#TT_ZYXB').combobox('disable');
					$('#TT_BJBH').combobox('disable');
					$('#TT_KCBH').combobox('disable');
					$('#TT_SQLX').combobox('disable');
					
					if(submitFlag == true){
						submitFlag = false;
						$('#active').val('submit');
						$("#form1").submit();
					}else{
						$('#submit').linkbutton('enable');
						showMsg(json[0].MSG);
					}
				}else{
					$('#tkInfo').dialog('close');
					showMsg(json[0].MSG);
				}
				loadRequestNoteData();
				
				//建立数据对接
				if(tag == 1){
					createSJDJ();
				}
			}else{
				alertMsg(json[0].MSG);
			}
		}
	});
	
	var tag = 0;
	function createSJDJ(){
		/*
		$.ajax({
			type : "POST",
			url : '<-%=request.getContextPath()%>/Svl_Skjh',
			data : 'active=createSJDJ',
			async : false,
			dataType:"json",
			success : function(data) {
							
			}
		});
		*/
		tag = 0;
	}
	
	/**周次和时间序列排序**/
	function sortTime(timeArray){
		/*给每个未确定的位置做循环*/
		for(var unfix=timeArray.length-1; unfix>0; unfix--){
			/*给进度做个记录，比到未确定位置*/
			for(var i=0; i<unfix;i++){
				if(timeArray[i] > timeArray[i+1]){
					var temp = timeArray[i];
					timeArray.splice(i, 1, timeArray[i+1]);
					timeArray.splice(i+1, 1, temp);
				}
			}
		}
		return timeArray.toString();
	}
	
	/**检查选择的调课时间是否符合逻辑**/
	function checkTime(yjhSkzc, tzhSkzc, yjhXq, tzhXq, yjhTimeOrder, tzhTimeOrder){
		var resultArray = new Array();
		var flag = true;
		var msgStr = '';
		
		if(yjhSkzc == tzhSkzc){
			if(yjhXq == tzhXq){
				if(yjhTimeOrder != tzhTimeOrder){
					var yjhTimeArray = yjhTimeOrder.split(',');
					var tzhTimeArray = tzhTimeOrder.split(',');
				
					outerloop://命名外圈语句
					for(var i=0; i<yjhTimeArray.length; i++){
						for(var j=0; j<tzhTimeArray.length; j++){
							if(yjhTimeArray[i] == tzhTimeArray[j]){
								flag = false;
								msgStr = '授课周次和星期相同的情况下，选择的授课节必须完全不相同，请重新选择后保存。';
								break outerloop;
							}
						}
					}
				}
			}
		}else{
			if(yjhXq == tzhXq){
				if(yjhTimeOrder == tzhTimeOrder){
					var yjhSkzcArray = yjhSkzc.split(',');
					var tzhSkzcArray = tzhSkzc.split(',');
					
					outerloop://命名外圈语句
					for(var i=0; i<yjhSkzcArray.length; i++){
						for(var j=0; j<tzhSkzcArray.length; j++){
							if(yjhSkzcArray[i] == tzhSkzcArray[j]){
								flag = false;
								msgStr = '授课星期和节相同的情况下，选择的授课周次必须完全不相同，请重新选择后保存。';
								break outerloop;
							}
						}
					}
				}
			}
		}
		
		resultArray.push(flag);
		resultArray.push(msgStr);
		
		return resultArray;
	}
</script>
</html>