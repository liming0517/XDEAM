<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="java.util.Vector"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.src.develop.store.user.*"%>
<%
	/**
		创建人：yeq
		Create date: 2016.03.25
		功能说明：用于成绩查询
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
	<title>成绩查询</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/icon.css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/publicScript.js"></script>
	<style>
		.maskStyle{
			width:110%;
			height:100%;
			overflow:hidden;
			display:none;
			background-color:#D2E0F2;
			filter:alpha(opacity=80);
			position:absolute;
			top:0px;
			left:0px;
			z-index:9999;
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
			 margin-left:-150px;
			 font-weight:bold;
		}
	
		.queryTable{
			width:100%;
			height:100%;
			border-left:1px solid #CCCCCC;
		}
		
		.tableTitle{
			width:80px;
			font-size:12;
			text-align:right;
			padding-right:10px;
		}
		
		.scoreTitle{
			width:38px;
			font-size:12;
			text-align:right;
			padding-right:5px;
		}
		
		.scoreTd{
			width:60px;
		}
		
		.scoreInput{
			width:50px;
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
	<div id="north" region="north" title="成绩查询" style="height:172px;">
		<table>
			<tr>
				<td><a href="#" id="print" class="easyui-linkbutton" plain="true" iconcls="icon-print" onClick="doToolbar(this.id);">教师成绩打印</a></td>
				<td><a href="#" id="reportPrint" class="easyui-linkbutton" plain="true" iconcls="icon-print" onClick="doToolbar(this.id);">成绩单打印</a></td>
				<td><a href="#" id="export" class="easyui-linkbutton" plain="true" iconcls="icon-submit" onClick="doToolbar(this.id);">成绩导出</a></td>
				<!-- <td><a href="#" id="ksmcExport" class="easyui-linkbutton" plain="true" iconcls="icon-submit" onClick="doToolbar(this.id);">考试名册导出</a></td> -->
				<td><a href="#" id="exportClassInfo" class="easyui-linkbutton" plain="true" iconcls="icon-pyxy" onClick="doToolbar(this.id);">整班未登分信息</a></td>
				<!-- <td><a href="#" id="openBkInfo" class="easyui-linkbutton" plain="true" iconcls="icon-pyxy" onClick="doToolbar(this.id);">补考统计</a></td> -->
				<!-- <td><a href="#" id="openLjInfo" class="easyui-linkbutton" plain="true" iconcls="icon-pyxy" onClick="doToolbar(this.id);">留级统计</a></td> -->
				<!-- <td><a href="#" id="openJdInfo" class="easyui-linkbutton" plain="true" iconcls="icon-pyxy" onClick="doToolbar(this.id);">绩点统计</a></td> -->
				<!-- <td><a href="#" id="openDbkInfo" class="easyui-linkbutton" plain="true" iconcls="icon-pyxy" onClick="doToolbar(this.id);">大补考统计</a></td> -->
				<td><a href="#" id="config" class="easyui-linkbutton" plain="true" iconcls="icon-pyap" onClick="doToolbar(this.id);">查分设置</a></td>
				<td><a href="#" id="sync" class="easyui-linkbutton" plain="true" iconcls="icon-synchronization" onClick="doToolbar(this.id);">同步查分数据</a></td>
			</tr>
		</table>
		<table style="width:100%; background-color:#EFEFEF; border-top:1px solid #CCCCCC;" cellspacing="0" cellpadding="0">
			<tr>
				<td style="width:280px;">
					<table class="queryTable">
						<tr>
							<td class="tableTitle">学号</td>
							<td>
								<input name="ic_stuCode" id="ic_stuCode" style="width:150px;"/>
							</td>
							<td style="width:100px;" align="center">
								<a href="#" onclick="doToolbar(this.id)" id="queStuScore" class="easyui-linkbutton" plain="true" iconcls="icon-search">查询</a>
							</td>
						</tr>
						<tr>
							<td class="tableTitle">姓名</td>
							<td>
								<input name="ic_stuName" id="ic_stuName" style="width:150px;"/>
							</td>
							<td>&nbsp;</td>
						</tr>
						<tr><td colspan="3">&nbsp;</td></tr>
						<tr><td colspan="3">&nbsp;</td></tr>
					</table>
				</td>
				<td style="width:550px;">
					<table class="queryTable">
						<tr>
							<td class="tableTitle">年级名称</td>
							<td>
								<select name="ic_njmc" id="ic_njmc" class="easyui-combobox">
								</select>
							</td>
							<td class="tableTitle">学年学期</td>
							<td>
								<select name="ic_xnxq" id="ic_xnxq" class="easyui-combobox">
								</select>
							</td>
							<td style="width:65px;">&nbsp;</td>
						</tr>
						<tr>
							<td class="tableTitle">专业名称</td>
							<td>
								<input name="ic_zymc" id="ic_zymc" style="width:150px;"/>
							</td>
							<td class="tableTitle">课程名称</td>
							<td>
								<input name="ic_kcmc" id="ic_kcmc" style="width:150px;"/>
							</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td class="tableTitle">班级名称</td>
							<td>
								<input name="ic_bjmc" id="ic_bjmc" style="width:150px;"/>
							</td>
							<td class="tableTitle">教师姓名</td>
							<td>
								<input name="ic_jsxm" id="ic_jsxm" style="width:150px;"/>
							</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td class="tableTitle">成绩范围</td>
							<td>
								<select name="ic_cjfw" id="ic_cjfw" class="easyui-combobox" panelHeight="auto" style="width:150px;" editable="false">
									<option value="all" selected="selected">全部</option>
									<option value="bjg">不及格</option>
								</select>
							</td>
							<td class="tableTitle">成绩类型</td>
							<td>
								<select name="ic_cjlx" id="ic_cjlx" class="easyui-combobox" panelHeight="auto" style="width:150px;" editable="false">
									<option value="all" selected="selected">全部</option>
									<option value="zxzp">只限总评</option>
								</select>
							</td>
							<td style="text-align:right;">
								<a href="#" onclick="doToolbar(this.id)" id="queScore" class="easyui-linkbutton" plain="true" iconcls="icon-search">查询</a>
							</td>
						</tr>
				    </table>
				</td>
				<td>&nbsp;</td>
			</tr>
		</table>
	</div>
	<div region="center" class="easyui-layout">
		<form id="form1" method="post">
			<div region="north" style="height:37px;">
				<table class="queryTable" style="height:100%;" cellspacing="0" cellpadding="0">
					<tr>
						<td class="scoreTitle">平时</td><td class="scoreTd"><input class="scoreInput" id="PSCJ" name="PSCJ"/></td>
						<td class="scoreTitle">期中</td><td class="scoreTd"><input class="scoreInput" id="QZCJ" name="QZCJ"/></td>
						<td class="scoreTitle">实训</td><td class="scoreTd"><input class="scoreInput" id="SXCJ" name="SXCJ"/></td>
						<td class="scoreTitle">期末</td><td class="scoreTd"><input class="scoreInput" id="QMCJ" name="QMCJ"/></td>
						<td class="scoreTitle">总评</td><td class="scoreTd"><input class="scoreInput" id="ZPCJ" name="ZPCJ"/></td>
						<td class="scoreTitle">重修1</td><td class="scoreTd"><input class="scoreInput" id="CXCJ1" name="CXCJ1"/></td>
						<td class="scoreTitle">重修2</td><td class="scoreTd"><input class="scoreInput" id="CXCJ2" name="CXCJ2"/></td>
						<td class="scoreTitle">补考</td><td class="scoreTd"><input class="scoreInput" id="BKCJ" name="BKCJ"/></td>
						<td class="scoreTitle">大补考</td><td class="scoreTd"><input class="scoreInput" id="DBKCJ" name="DBKCJ"/></td>
						<!-- <td class="scoreTitle" style="width:55px;">打印成绩</td><td class="scoreTd"><input class="scoreInput" id="DYCJ" name="DYCJ"/></td> -->
						<td>
							<a href="#" onclick="doToolbar(this.id);" id="saveScore" class="easyui-linkbutton" plain="true" iconcls="icon-edit">修改</a>
							<a href="#" onclick="doToolbar(this.id);" id="scoreCover" class="easyui-linkbutton" plain="true" iconcls="icon-edit">成绩覆盖</a>
							<a href="#" onClick="doToolbar(this.id);" id="dmsm" class="easyui-linkbutton" plain="true" iconcls="icon-assets" >成绩代码说明</a>
						</td>
					</tr>
				</table>
			</div>
			<div region="center">
				<table id="scoreList" style="width:100%;" class="easyui-datagrid" toolbar="#divToolbar"></table>
			</div>
			
			<input type="hidden" id="active" name="active"/>
			<input type="hidden" id="iKeyCode" name="iKeyCode"/>
			
			<input type="hidden" id="kfxnxq" name="kfxnxq"/>
			<input type="hidden" id="kfcjlx" name="kfcjlx"/>
			<input type="hidden" id="beginDate" name="beginDate"/>
			<input type="hidden" id="endDate" name="endDate"/>
			
			<input type="hidden" id="export_startSem" name="export_startSem"/>
			<input type="hidden" id="export_endSem" name="export_endSem"/>
			<input type="hidden" id="export_dept" name="export_dept"/>
			<input type="hidden" id="export_grade" name="export_grade"/>
			<input type="hidden" id="export_major" name="export_major"/>
			<input type="hidden" id="export_class" name="export_class"/>
			<input type="hidden" id="export_range" name="export_range"/>
			<input type="hidden" id="export_zbwdfFlag" name="export_zbwdfFlag"/>
			<input type="hidden" id="export_scoreType" name="export_scoreType"/>
			<input type="hidden" id="export_stuType" name="export_stuType"/>
			<input type="hidden" id="export_subject" name="export_subject"/>
		</form>
	</div>
	
	<div id="divToolbar">
		<a href="#" id="changeStuScoreState" class="easyui-linkbutton" iconCls="icon-cancel" plain="true" onclick="doToolbar(this.id);">删除</a>
		<a href="#" id="changeClassScoreSate" class="easyui-linkbutton" iconCls="icon-cancel" plain="true" onclick="doToolbar(this.id);">整班删除</a>
		<a href="#" id="openInvalidScore" class="easyui-linkbutton" iconCls="icon-new" plain="true" onclick="doToolbar(this.id);">无效成绩信息</a>
	</div>
	
	<%-- 遮罩层 --%>
   	<div id="divPageMask" class="maskStyle">
   		<div id="maskFont">文件生成中，请稍后...</div>
   	</div>
	
	<!-- 成绩导出 -->
	<div id="exportDialog" style="overflow:hidden;">
		<table style="width:100%;" class="tablestyle">
			<tr>
				<td style="width:145px;" class="titlestyle">起始学年学期</td>
				<td>
					<select name="export_qsxnxq" id="export_qsxnxq" class="easyui-combobox exportCombo">
					</select>
				</td>
			</tr>
			<tr>
				<td class="titlestyle">结束学年学期</td>
				<td>
					<select name="export_jsxnxq" id="export_jsxnxq" class="easyui-combobox exportCombo">
					</select>
				</td>
			</tr>
			<tr>
				<td class="titlestyle">系部</td>
				<td>
					<select name="export_xb" id="export_xb" class="easyui-combobox exportCombo">
					</select>
				</td>
			</tr>
			<tr>
				<td class="titlestyle">年级</td>
				<td>
					<select name="export_nj" id="export_nj" class="easyui-combobox exportCombo">
					</select>
				</td>
			</tr>
			<tr>
				<td class="titlestyle">专业</td>
				<td>
					<select name="export_zy" id="export_zy" class="easyui-combobox exportCombo">
					</select>
				</td>
			</tr>
			<tr>
				<td class="titlestyle">班级</td>
				<td>
					<select name="export_bj" id="export_bj" class="easyui-combobox exportCombo" disabled="disabled">
						<option value="">请先选择年级</option>
					</select>
				</td>
			</tr>
			<tr>
				<td class="titlestyle">成绩范围</td>
				<td>
					<select name="export_cjfw" id="export_cjfw" class="easyui-combobox exportCombo" panelHeight="auto" style="width:250px;" editable="false">
						<option value="all" selected="selected">全部</option>
						<option value="bjg">不及格</option>
					</select>
				</td>
			</tr>
			<tr>
				<td class="titlestyle">整班未登分</td>
				<td>
					<select name="export_zbwdf" id="export_zbwdf" class="easyui-combobox" style="width:250px;" panelHeight="auto" editable="false">
						<option value="include">包含</option>
						<option value="exclude" selected="selected">不包含</option>
					</select>
				</td>
			</tr>
			<tr>
				<td class="titlestyle">成绩类型</td>
				<td>
					<select name="export_cjlx" id="export_cjlx" class="easyui-combobox exportCombo">
					</select>
				</td>
			</tr>
			<tr>
				<td class="titlestyle">招生类别</td>
				<td>
					<select name="export_zslb" id="export_zslb" class="easyui-combobox exportCombo">
					</select>
				</td>
			</tr>
			<tr>
				<td class="titlestyle">学科名称</td>
				<td>
					<select name="export_xkmc" id="export_xkmc" class="easyui-combobox exportCombo">
					</select>
				</td>
			</tr>
			<tr>
				<td colspan="2" style="text-align:center;">
					<a id="skcjExport" href="#" onclick="doToolbar(id);" class="easyui-linkbutton" plain="true" iconcls="icon-submit">成绩导出</a>
					<a id="hzcjExport" href="#" onclick="doToolbar(id);" class="easyui-linkbutton" plain="true" iconcls="icon-submit">汇总成绩导出</a>
				</td>				
			</tr>
	    </table>
	</div>
	
	<!-- 整班未登分导出 -->
	<!-- <div id="exportClassDialog" style="overflow:hidden;">
		<table style="width:100%;" class="tablestyle">
			<tr>
				<td style="width:35%;" class="titlestyle">起始学年学期</td>
				<td>
					<select name="exportClass_qsxnxq" id="exportClass_qsxnxq" class="easyui-combobox exportCombo">
					</select>
				</td>
			</tr>
			<tr>
				<td style="width:35%;" class="titlestyle">结束学年学期</td>
				<td>
					<select name="exportClass_jsxnxq" id="exportClass_jsxnxq" class="easyui-combobox exportCombo">
					</select>
				</td>
			</tr>
			<tr>
				<td style="width:35%;" class="titlestyle">年级</td>
				<td>
					<select name="exportClass_nj" id="exportClass_nj" class="easyui-combobox exportCombo">
					</select>
				</td>
			</tr>
			<tr>
				<td style="width:35%;" class="titlestyle">考试周次</td>
				<td>
					<select name="exportClass_kszc" id="exportClass_kszc" class="easyui-combobox exportCombo" style="width:250px;" editable="false" panelHeight="auto">
						<option value="all">全部</option>
						<option value="1">1-9周</option>
						<option value="2">1-14周</option>
						<option value="3">1-18周</option>
					</select>
				</td>
			</tr>
			<tr>
				<td colspan="2" style="text-align:center;">
					<a id="zbwdfExport" href="#" onclick="doToolbar(id);" class="easyui-linkbutton" plain="true" iconcls="icon-submit">导出</a>
				</td>				
			</tr>
	    </table>
	</div> -->
	
	<!-- 考试名册导出 -->
	<!-- 
	<div id="ksmcExportDialog" style="overflow:hidden;">
		<table style="width:100%; height:100%; text-align:center;">
			<tr>
				<td><a href="#" id="openBkInfo" class="easyui-linkbutton" plain="true" iconcls="icon-pyxy" onClick="doToolbar(this.id);">补考导出</a></td>
				<td><a href="#" id="openDbkInfo" class="easyui-linkbutton" plain="true" iconcls="icon-pyxy" onClick="doToolbar(this.id);">大补考导出</a></td>
			</tr>
	    </table>
	</div>
	 -->
	
	<!-- 查分设置 -->
	<div id="configDialog" style="overflow:hidden;">
		<table style="width:100%;" class="tablestyle">
			<tr>
				<td style="width:100px;" class="titlestyle">开放学年学期</td>
				<td>
					<select name="ic_kfxnxq" id="ic_kfxnxq" class="easyui-combobox">
					</select>
				</td>
			</tr>
			<tr>
				<td class="titlestyle">开放成绩类型</td>
				<td>
					<select name="ic_kfcjlx" id="ic_kfcjlx" class="easyui-combobox">
					</select>
				</td>
			</tr>
			<tr>
				<td class="titlestyle">开始日期</td>
				<td>
					<input style="width:350px;" class="easyui-datebox" id="ic_ksrq" name="ic_ksrq" editable="false" required="true"/>
				</td>
			</tr>
			<tr>
				<td class="titlestyle">结束日期</td>
				<td>
					<input style="width:350px;" class="easyui-datebox" id="ic_jsrq" name="ic_jsrq" editable="false" required="true"/>
				</td>
			</tr>
		</table>
	</div>
	
	<!-- 打印 -->
	<div id="printDialog" style="overflow:hidden;">
		<table style="width:100%;" class="tablestyle">
			<tr>
				<td style="width:35%;" class="titlestyle">学年学期</td>
				<td>
					<select name="print_xnxq" id="print_xnxq" class="easyui-combobox printCombo">
					</select>
				</td>
			</tr>
			<tr>
				<td style="width:35%;" class="titlestyle">教师姓名</td>
				<td>
					<select name="print_jsxm" id="print_jsxm" class="easyui-combobox printCombo">
					</select>
				</td>
			</tr>
			<tr>
				<td style="width:35%;" class="titlestyle">开课班</td>
				<td>
					<select name="print_kkb" id="print_kkb" class="easyui-combobox printCombo" style="width:250px;" disabled="disabled">
						<option value="">请先选择教师姓名</option>
					</select>
				</td>
			</tr>
			<tr>
				<td colspan="2" style="text-align:center;">
					<a id="printView" href="#" onclick="doToolbar(id);" class="easyui-linkbutton" plain="true" iconcls="icon-search">打印预览</a>
					<a href="#" onclick="doToolbar(this.id)" id="unlock" class="easyui-linkbutton" plain="true" iconcls="icon-unlock">解锁</a>
					<a href="#" onclick="doToolbar(this.id)" id="wdyExport" class="easyui-linkbutton" plain="true" iconcls="icon-submit">未打印导出</a>
				</td>				
			</tr>
	    </table>
	</div>
	
	<!-- 成绩单打印 -->
	<div id="reportPrintDialog" style="overflow:hidden;">
		<table style="width:100%;" class="tablestyle">
			<tr>
				<td style="width:35%;" class="titlestyle">起始学年学期</td>
				<td>
					<select name="print_qsxnxq" id="print_qsxnxq" class="easyui-combobox reportPrintCombo">
					</select>
				</td>
			</tr>
			<tr>
				<td style="width:35%;" class="titlestyle">结束学年学期</td>
				<td>
					<select name="print_jsxnxq" id="print_jsxnxq" class="easyui-combobox reportPrintCombo">
					</select>
				</td>
			</tr>
			<tr>
				<td class="titlestyle">班级</td>
				<td>
					<select name="print_bj" id="print_bj" class="easyui-combobox">
					</select>
				</td>
			</tr>
			<tr>
				<td class="titlestyle">学籍状态</td>
				<td>
					<select name="print_stuState" id="print_stuState" class="easyui-combobox">
					</select>
				</td>
			</tr>
			<tr>
				<td class="titlestyle">学号姓名</td>
				<td>
					<select name="print_xh" id="print_xh" class="easyui-combobox" style="width:250px;" panelHeight="140">
					</select>
				</td>
			</tr>
			<tr>
				<td colspan="2" style="text-align:center;">
					<a id="reportPrintView" href="#" onclick="doToolbar(id);" class="easyui-linkbutton" plain="true" iconcls="icon-search">打印预览</a>
				</td>				
			</tr>
	    </table>
	</div>
	
	<!-- 整班成绩删除 -->
	<div id="delClassDialog" style="overflow:hidden;">
		<div class="easyui-layout" style="width:100%; height:100%;">
			<div id="north" region="north" title="" style="height:105px; overflow:hidden;">
				<table style="width:100%;" class="tablestyle">
					<tr>
						<td style="width:150px;" class="titlestyle">学年学期</td>
						<td>
							<select name="delClass_xnxq" id="delClass_xnxq" class="easyui-combobox">
							</select>
						</td>
					</tr>
					<tr>
						<td class="titlestyle">班级类型</td>
						<td>
							<select name="delClass_bjlx" id="delClass_bjlx" class="easyui-combobox">
							</select>
						</td>
					</tr>
					<tr>
						<td class="titlestyle">班级名称</td>
						<td>
							<select name="delClass_bjdm" id="delClass_bjdm" class="easyui-combobox">
							</select>
						</td>
					</tr>
					<tr>
						<td class="titlestyle">课程名称</td>
						<td>
							<select name="delClass_kcdm" id="delClass_kcdm" class="easyui-combobox" style="width:250px;">
							</select>
						</td>
					</tr>
			    </table>
		    </div>
		    <div region="center">
				<table id="stuScoreList" style="width:100%;" class="easyui-datagrid" toolbar="#divToolbar_delClass"></table>
			</div>
		</div>
	</div>
	
	<div id="divToolbar_delClass">
		<a href="#" id="changeMultiStuScoreState" class="easyui-linkbutton" iconCls="icon-cancel" plain="true" onclick="doToolbar(this.id);">删除</a>
	</div>
	
	<!-- 整班未登分查询-->
	<div id="zbwdfInfoDialog">
		<!--引入编辑页面用Ifram-->
		<iframe id="zbwdfInfoIframe" name="zbwdfInfoIframe" src='' style='width:100%; height:100%;' frameborder="0" scrolling="no"></iframe>
	</div>
	
	<!-- 成绩覆盖 -->
	<div id="scoreCoverDialog">
		<!--引入编辑页面用Ifram-->
		<iframe id="scoreCoverIframe" name="scoreCoverIframe" src='' style='width:100%; height:100%;' frameborder="0" scrolling="no"></iframe>
	</div>
	
	<!-- 无效成绩信息 -->
	<div id="invalidScoreDialog">
		<!--引入编辑页面用Ifram-->
		<iframe id="invalidScoreIframe" name="invalidScoreIframe" src='' style='width:100%; height:100%;' frameborder="0" scrolling="no"></iframe>
	</div>
	
	<!-- 补考名单 -->
	<div id="bkInfoDialog">
		<!--引入编辑页面用Ifram-->
		<iframe id="bkInfoIframe" name="bkInfoIframe" src='' style='width:100%; height:100%;' frameborder="0" scrolling="no"></iframe>
	</div>
	
	<!-- 留级名单 -->
	<div id="ljInfoDialog">
		<!--引入编辑页面用Ifram-->
		<iframe id="ljInfoIframe" name="ljInfoIframe" src='' style='width:100%; height:100%;' frameborder="0" scrolling="no"></iframe>
	</div>
	
	<!-- 绩点信息 -->
	<div id="jdInfoDialog">
		<!--引入编辑页面用Ifram-->
		<iframe id="jdInfoIframe" name="jdInfoIframe" src='' style='width:100%; height:100%;' frameborder="0" scrolling="no"></iframe>
	</div>
	
	<!-- 大补考信息 -->
	<div id="dbkInfoDialog">
		<!--引入编辑页面用Ifram-->
		<iframe id="dbkInfoIframe" name="dbkInfoIframe" src='' style='width:100%; height:100%;' frameborder="0" scrolling="no"></iframe>
	</div>
	
	<!-- 成绩打印预览页面 -->
	<div id="printViewDialog">
		<!--引入编辑页面用Ifram-->
		<iframe id="printViewIframe" name="printViewIframe" src='' style='width:100%; height:100%;' frameborder="0" scrolling="no"></iframe>
	</div>
	
	<!-- 文字成绩代码说明 -->
	<div id="dmsmDialog" style="overflow:hidden;">
		<table class="tablestyle" style="width:100%; height:100%; text-align:center;" cellspacing="0" cellpadding="0">
			<tr><td style="width:50%; font-weight:bold;">代码</td><td style="width:50%; font-weight:bold;">名称</td></tr>
			<tr><td>-1</td><td>免修</td></tr>
			<tr><td>-2</td><td>作弊</td></tr>
			<tr><td>-3</td><td>取消资格</td></tr>
			<tr><td>-4</td><td>缺考</td></tr>
			<tr><td>-5</td><td>缓考</td></tr>
			<!-- <tr><td>-6</td><td>优</td></tr> -->
			<!-- <tr><td>-7</td><td>良</td></tr> -->
			<!-- <tr><td>-8</td><td>中</td></tr> -->
			<tr><td>-9</td><td>及格</td></tr>
			<tr><td>-10</td><td>不及格</td></tr>
			<!-- <tr><td>-11</td><td>合格</td></tr> -->
			<!-- <tr><td>-12</td><td>不合格</td></tr> -->
			<!-- <tr><td>-13</td><td>补及</td></tr> -->
			<!-- <tr><td>-15</td><td>达标</td></tr> -->
			<!-- <tr><td>-16</td><td>重考</td></tr> -->
			<tr><td>-17</td><td>免考</td></tr>
		</table>
	</div>
	
	<!-- 下载excel -->
	<iframe id="exportIframe" src="" style="width:0; height:0;"></iframe>
	
	<a id="pageOfficeLink" href="#" style="display:none;"></a>
</body>
<script type="text/javascript">
	var sAuth = '<%=sAuth%>';
	var pofOpenType = '<%=MyTools.getProp(request, "Base.pofOpenType")%>';
	var iKeyCode = '';
	var rowNum = '';
	var scoreData = '';
	var queType = '';
	var viewFilePath = '';
	var curXnxq = '';
	var curXn = '';
	var wzcjShowArray = '';
	
	$(document).ready(function(){
		initDialog();
		initData();
		loadScoreList('');
		initScoreInput();
	});
	
	/**页面初始化加载数据**/
	var curKfxnxq = "";
	var curKfcjlx = "";
	function initData(){
		$.ajax({
			type:"POST",
			url:'<%=request.getContextPath()%>/Svl_Cjcx',
			data:'active=initData',
			dataType:"json",
			success:function(data) {
				curXnxq = data[0].curXnxq;
				curXn = curXnxq.substring(0, 4);
				curKfxnxq = data[0].kfxnxq;
				curKfcjlx = data[0].kfcjlx;
				$("#ic_ksrq").datebox('setValue', data[0].beginDate);
				$("#ic_jsrq").datebox('setValue', data[0].endDate);
				wzcjShowArray = data[0].wzcjShowData;
				
				initCombobox(data[0].njdmData, data[0].xnxqData, data[0].exportXnxqData, data[0].exportXbData, data[0].exportNjData, data[0].exportZyData, data[0].exportStuTypeData, data[0].exportSubData, data[0].printBjData, data[0].printXsData);
				
				/*查询速度较慢,单独查询*/
				$('#print_jsxm').combobox({
					url:'<%=request.getContextPath()%>/Svl_Cjcx?active=loadPrintJsCombo',
					valueField:'comboValue',
					textField:'comboName',
					width:'250',
					panelHeight:'140', //combobox高度
					onLoadSuccess:function(data){
						//判断data参数是否为空
						if(data != ''){
							//初始化combobox时赋值
							$(this).combobox('setValue', '');
						}
					},
					onChange:function(data){
						if(checkChinese(data) || data==undefined || data==''){
							$('#print_kkb').combobox('setText', '请先选择教师姓名');
							$('#print_kkb').combobox('disable');
						}else{
							loadKkbCombo();
						}
					},
					onSelect:function(data){
					},
					onUnselect:function(data){
					}
				});
			}
		});
	}
	
	/**检查是否为中文*/
	function checkChinese(str){     
		var reg = new RegExp("[\\u4E00-\\u9FFF]+","g");
		if(reg.test(str)){     
			return true;     
		}else{
			return false;
		}     
	}
	
	/**加载combobox控件
		@njdmData 年级下拉框数据
		@xnxqData 学年学期下拉框数据
	**/
	function initCombobox(njdmData, xnxqData, exportXnxqData, exportXbData, exportNjData, exportZyData, exportStuType, exportSubData, printBjData, printXsData){
		$('#ic_njmc').combobox({
			data:njdmData,
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			width:'150',
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
		
		$('#ic_xnxq').combobox({
			data:xnxqData,
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			width:'150',
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
		
		$('#export_qsxnxq').combobox({
			data:exportXnxqData,
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			width:'250',
			panelHeight:'140', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data != ''){
					//初始化combobox时赋值
					$(this).combobox('setValue', 'all');
				}
			}
		});
		
		$('#print_qsxnxq').combobox({
			data:exportXnxqData,
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			width:'250',
			panelHeight:'140', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data != ''){
					//初始化combobox时赋值
					$(this).combobox('setValue', 'all');
				}
			}
		});
		
		$('#export_jsxnxq').combobox({
			data:exportXnxqData,
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			width:'250',
			panelHeight:'140', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data != ''){
					//初始化combobox时赋值
					$(this).combobox('setValue', 'all');
				}
			}
		});
		
		$('#print_jsxnxq').combobox({
			data:exportXnxqData,
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			width:'250',
			panelHeight:'140', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data != ''){
					//初始化combobox时赋值
					$(this).combobox('setValue', 'all');
				}
			}
		});
		
		$('#export_xb').combobox({
			data:exportXbData,
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			multiple:true,
			width:'250',
			panelHeight:'140', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data != ''){
					//初始化combobox时赋值
					$(this).combobox('setValue', 'all');
				}
			},
			onSelect:function(data){
				var obj = $(this).combobox('getValues');
				
				if(obj.length > 1){
					$(this).combobox('unselect', 'all');
				}
				
				if(data.comboValue == 'all'){
					$(this).combobox('clear');
					$(this).combobox('setValue', 'all');
				}
			},
			onUnselect:function(data){
				var obj = $(this).combobox('getValues');
				
				if(obj.length < 1){
					$(this).combobox('select', 'all');
				}
			},
			onChange:function(data){
				loadExportClassCombo();
			}
		});
		
		$('#export_nj').combobox({
			data:exportNjData,
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			multiple:true,
			width:'250',
			panelHeight:'140', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data != ''){
					//初始化combobox时赋值
					$(this).combobox('setValue', 'all');
				}
			},
			onSelect:function(data){
				var obj = $(this).combobox('getValues');
				
				if(obj.length > 1){
					$(this).combobox('unselect', 'all');
				}
				
				if(data.comboValue == 'all'){
					$(this).combobox('clear');
					$(this).combobox('setValue', 'all');
				}
			},
			onUnselect:function(data){
				var obj = $(this).combobox('getValues');
				
				if(obj.length < 1){
					$(this).combobox('select', 'all');
				}
			},
			onChange:function(data){
				loadExportClassCombo();
			}
		});
		
		$('#export_zy').combobox({
			data:exportZyData,
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			multiple:true,
			width:'250',
			panelHeight:'140', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data != ''){
					//初始化combobox时赋值
					$(this).combobox('setValue', 'all');
				}
			},
			onSelect:function(data){
				var obj = $(this).combobox('getValues');
				
				if(obj.length > 1){
					$(this).combobox('unselect', 'all');
				}
				
				if(data.comboValue == 'all'){
					$(this).combobox('clear');
					$(this).combobox('setValue', 'all');
				}
			},
			onUnselect:function(data){
				var obj = $(this).combobox('getValues');
				
				if(obj.length < 1){
					$(this).combobox('select', 'all');
				}
			},
			onChange:function(data){
				loadExportClassCombo();
			}
		});
		
		$('#export_zslb').combobox({
			data:exportStuType,
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			multiple:true,
			width:'250',
			panelHeight:'140', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data != ''){
					//初始化combobox时赋值
					$(this).combobox('setValue', 'all');
				}
			},
			onSelect:function(data){
				var obj = $(this).combobox('getValues');
				
				if(obj.length > 1){
					$(this).combobox('unselect', 'all');
				}
				
				if(data.comboValue == 'all'){
					$(this).combobox('clear');
					$(this).combobox('setValue', 'all');
				}
			},
			onUnselect:function(data){
				var obj = $(this).combobox('getValues');
				
				if(obj.length < 1){
					$(this).combobox('select', 'all');
				}
			}
		});
		
		$('#export_xkmc').combobox({
			data:exportSubData,
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			multiple:true,
			width:'250',
			panelHeight:'140', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data != ''){
					//初始化combobox时赋值
					$(this).combobox('setValue', 'all');
				}
			},
			onSelect:function(data){
				var obj = $(this).combobox('getValues');
				
				if(obj.length > 1){
					$(this).combobox('unselect', 'all');
				}
				
				if(data.comboValue == 'all'){
					$(this).combobox('clear');
					$(this).combobox('setValue', 'all');
				}
			},
			onUnselect:function(data){
				var obj = $(this).combobox('getValues');
				
				if(obj.length < 1){
					$(this).combobox('select', 'all');
				}
			}
		});
		
		var cjlxData = [{'id':'all','text':'全部'},{'id':'ps','text':'平时'},{'id':'qz','text':'期中'},{'id':'sx','text':'实训'},{'id':'qm','text':'期末'},
		                {'id':'zp','text':'总评'},{'id':'cx1','text':'重修1'},{'id':'cx2','text':'重修2'},{'id':'bk','text':'补考'},{'id':'dbk','text':'大补考'}];
		$('#export_cjlx').combobox({
			data:cjlxData,
			valueField:'id',
			textField:'text',
			editable:false,
			multiple:true,
			width:'250',
			panelHeight:'140', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data != ''){
					//初始化combobox时赋值
					$(this).combobox('setValue', 'all');
				}
			},
			onSelect:function(data){
				var obj = $(this).combobox('getValues');
				
				if(obj.length > 1){
					$(this).combobox('unselect', 'all');
				}
				
				if(data.id == 'all'){
					$(this).combobox('clear');
					$(this).combobox('setValue', 'all');
				}
			},
			onUnselect:function(data){
				var obj = $(this).combobox('getValues');
				
				if(obj.length < 1){
					$(this).combobox('select', 'all');
				}
			}
		});
		
		$('#print_bj').combobox({
			data:printBjData,
			valueField:'comboValue',
			textField:'comboName',
			width:'250',
			panelHeight:'140', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data != ''){
					//初始化combobox时赋值
					$(this).combobox('setValue', '');
				}
				$('#print_xh').combobox('setText', '请先选择班级');
				$('#print_xh').combobox('disable');
			},
			onChange:function(record){
				if(record == ''){
					$('#print_xh').combobox('setText', '请先选择班级');
					$('#print_xh').combobox('disable');
				}else{
					$('#print_xh').combobox('enable');
					
					$('#print_xh').combobox({
						url:'<%=request.getContextPath()%>/Svl_Cjcx?active=loadPrintXsCombo&BJMC=' + record + '&stuState=' + $('#print_stuState').combobox('getValue'),
						valueField:'comboValue',
						textField:'comboName',
						width:'250',
						panelHeight:'140', //combobox高度
						onLoadSuccess:function(data){
							//判断data参数是否为空
							if(data != ''){
								//初始化combobox时赋值
								$(this).combobox('setValue', '');
							}
							//判断data参数是否为空
							if(data.length == 1){
								$(this).combobox('setText', '没有可选学生');
								$(this).combobox('disable');
							}else{
								$(this).combobox('enable');
							}
						}
					});
				}
			}
		});
		
		$('#print_stuState').combobox({
			data:[{'comboName':'全部','comboValue':'all'},{'comboName':'正常学籍','comboValue':'normal'}],
			valueField:'comboValue',
			textField:'comboName',
			width:'250',
			panelHeight:'auto', //combobox高度
			editable:false,
			onLoadSuccess:function(data){
				$(this).combobox('setValue', 'normal');
			},
			onChange:function(record){
				if($('#print_bj').combobox('getValue') != ''){
					$('#print_xh').combobox({
						url:'<%=request.getContextPath()%>/Svl_Cjcx?active=loadPrintXsCombo&BJMC=' + $('#print_bj').combobox('getValue') + '&stuState=' + record,
						valueField:'comboValue',
						textField:'comboName',
						width:'250',
						panelHeight:'140', //combobox高度
						onLoadSuccess:function(data){
							//判断data参数是否为空
							if(data != ''){
								//初始化combobox时赋值
								$(this).combobox('setValue', '');
							}
							//判断data参数是否为空
							if(data.length == 1){
								$(this).combobox('setText', '没有可选学生');
								$(this).combobox('disable');
							}else{
								$(this).combobox('enable');
							}
						}
					});
				}
			}
		});
		
		$('#print_xnxq').combobox({
			data:exportXnxqData,
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			multiple:true,
			width:'250',
			panelHeight:'140', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data != ''){
					//初始化combobox时赋值
					$(this).combobox('setValue', 'all');
				}
			},
			onChange:function(data){
			},
			onSelect:function(data){
				var obj = $(this).combobox('getValues');
				
				if(obj.length > 1){
					$(this).combobox('unselect', 'all');
				}
				
				if(data.comboValue == 'all'){
					$(this).combobox('clear');
					$(this).combobox('setValue', 'all');
				}
				
				if($('#print_jsxm').combobox('getValue') != ''){
					loadKkbCombo();
				}
			},
			onUnselect:function(data){
				var obj = $(this).combobox('getValues');
				
				if(obj.length < 1){
					$(this).combobox('select', 'all');
				}
				
				if($('#print_jsxm').combobox('getValue') != ''){
					loadKkbCombo();
				}
			}
		});
		
		$('#exportClass_qsxnxq').combobox({
			data:exportXnxqData,
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			width:'250',
			panelHeight:'140', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data != ''){
					//初始化combobox时赋值
					$(this).combobox('setValue', 'all');
				}
			}
		});
		
		$('#exportClass_jsxnxq').combobox({
			data:exportXnxqData,
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			width:'250',
			panelHeight:'140', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data != ''){
					//初始化combobox时赋值
					$(this).combobox('setValue', 'all');
				}
			}
		});
		
		$('#exportClass_nj').combobox({
			data:exportNjData,
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			multiple:true,
			width:'250',
			panelHeight:'140', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data != ''){
					//初始化combobox时赋值
					$(this).combobox('setValue', 'all');
				}
			},
			onSelect:function(data){
				var obj = $(this).combobox('getValues');
				
				if(obj.length > 1){
					$(this).combobox('unselect', 'all');
				}
				
				if(data.comboValue == 'all'){
					$(this).combobox('clear');
					$(this).combobox('setValue', 'all');
				}
			},
			onUnselect:function(data){
				var obj = $(this).combobox('getValues');
				
				if(obj.length < 1){
					$(this).combobox('select', 'all');
				}
			},
			onChange:function(data){
			}
		});
		
		$('#delClass_xnxq').combobox({
			data:exportXnxqData,
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			width:'250',
			panelHeight:'140', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data != ''){
					//初始化combobox时赋值
					$(this).combobox('setValue', 'all');
				}
				
				var bjlxData = [{"comboName":"行政班","comboValue":"xzb"},{"comboName":"分层班","comboValue":"fcb"},{"comboName":"选修班","comboValue":"xxb"}];
				$('#delClass_bjlx').combobox({
					data:bjlxData,
					valueField:'comboValue',
					textField:'comboName',
					width:'250',
					panelHeight:'auto', //combobox高度
					editable:false,
					onLoadSuccess:function(data){
						//判断data参数是否为空
						if(data != ''){
							//初始化combobox时赋值
							$(this).combobox('setValue', 'xzb');
						}
					},
					onChange:function(record){
						loadClassCourse();
					}
				});
			},
			onChange:function(record){
				if($('#delClass_bjlx').combobox('getValue') == 'xzb'){
					if($('#delClass_bjdm').combobox('getValue') == ''){
						$('#delClass_kcdm').combobox('setValue', '');
						$('#delClass_kcdm').combobox('setText', '请先选择班级');
						$('#delClass_kcdm').combobox('disable');
					}else{
						$('#delClass_kcdm').combobox('enable');
						$('#delClass_kcdm').combobox({
							url:'<%=request.getContextPath()%>/Svl_Cjcx?active=loadDelCourseCombo&XNXQ=' + $('#delClass_xnxq').combobox('getValues') + 
								'&ZYMC=' + $('#delClass_bjlx').combobox('getValue') + '&BJMC=' + $('#delClass_bjdm').combobox('getValue'),
							valueField:'comboValue',
							textField:'comboName',
							width:'250',
							panelHeight:'140', //combobox高度
							onLoadSuccess:function(data){
								//判断data参数是否为空
								if(data != ''){
									//初始化combobox时赋值
									$(this).combobox('setValue', '');
								}
								//判断data参数是否为空
								if(data.length == 1){
									$(this).combobox('setText', '没有可选课程');
									$(this).combobox('disable');
								}else{
									$(this).combobox('enable');
								}
							},
							onChange:function(record){
								loadDelClassScoreList();
							}
						});
					}
				}else{
					loadClassCourse();
				}
			}
		});
		
		$('#ic_kfxnxq').combobox({
			data:exportXnxqData,
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			multiple:true,
			width:'350',
			panelHeight:'140', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data != ''){
					//初始化combobox时赋值
					//$(this).combobox('setValue', 'all');
					$("#ic_kfxnxq").combobox('setValues', curKfxnxq.split(','));
				}
			},
			onSelect:function(data){
				var obj = $(this).combobox('getValues');
				
				if(obj.length > 1){
					$(this).combobox('unselect', 'all');
				}
				
				if(data.comboValue == 'all'){
					$(this).combobox('clear');
					$(this).combobox('setValue', 'all');
				}
			},
			onUnselect:function(data){
				var obj = $(this).combobox('getValues');
				
				if(obj.length < 1){
					$(this).combobox('select', 'all');
				}
			},
			onChange:function(data){
			}
		});
		
		var kfcjlxData = [{'comboValue':'all','comboName':'全部'},{'comboValue':'zp','comboName':'总评成绩'},
		                  {'comboValue':'cx1','comboName':'重修成绩1'},{'comboValue':'cx2','comboName':'重修成绩2'},
		                  {'comboValue':'bk','comboName':'补考成绩'},{'comboValue':'dbk','comboName':'大补考成绩'}];
		$('#ic_kfcjlx').combobox({
			data:kfcjlxData,
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			multiple:true,
			width:'350',
			panelHeight:'auto', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data != ''){
					//初始化combobox时赋值
					//$(this).combobox('setValue', 'all');
					$("#ic_kfcjlx").combobox('setValues', curKfcjlx.split(','));
				}
			},
			onSelect:function(data){
				var obj = $(this).combobox('getValues');
				
				if(obj.length > 1){
					$(this).combobox('unselect', 'all');
				}
				
				if(data.comboValue == 'all'){
					$(this).combobox('clear');
					$(this).combobox('setValue', 'all');
				}
			},
			onUnselect:function(data){
				var obj = $(this).combobox('getValues');
				
				if(obj.length < 1){
					$(this).combobox('select', 'all');
				}
			},
			onChange:function(data){
			}
		});
	}
	
	function loadExportClassCombo(){
		$('#export_bj').combobox({
			url:'<%=request.getContextPath()%>/Svl_Cjcx?active=loadExportBjCombo'+
				'&exportXb=' + $('#export_xb').combobox('getValues').toString() + 
				'&exportNj=' + $('#export_nj').combobox('getValues').toString() + 
				'&exportZy=' + $('#export_zy').combobox('getValues').toString(),
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			multiple:true,
			width:'250',
			panelHeight:'140', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data != ''){
					//初始化combobox时赋值
					$(this).combobox('setValue', 'all');
				}
				//判断data参数是否为空
				if(data.length == 1){
					$(this).combobox('setText', '没有可选班级');
					$(this).combobox('disable');
				}else{
					$(this).combobox('enable');
				}
			},
			onSelect:function(data){
				var obj = $(this).combobox('getValues');
				
				if(obj.length > 1){
					$(this).combobox('unselect', 'all');
				}
				
				if(data.comboValue == 'all'){
					$(this).combobox('clear');
					$(this).combobox('setValue', 'all');
				}
			},
			onUnselect:function(data){
				var obj = $(this).combobox('getValues');
				
				if(obj.length < 1){
					$(this).combobox('select', 'all');
				}
			}
		});
	}
	
	function loadClassCourse(){
		$('#delClass_bjdm').combobox({
			url:'<%=request.getContextPath()%>/Svl_Cjcx?active=loadDelClassCombo&XNXQ=' + $('#delClass_xnxq').combobox('getValues') + '&ZYMC=' + $('#delClass_bjlx').combobox('getValue'),
			valueField:'comboValue',
			textField:'comboName',
			width:'250',
			panelHeight:'140', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data != ''){
					//初始化combobox时赋值
					$(this).combobox('setValue', '');
				}
				//判断data参数是否为空
				if(data.length == 1){
					$(this).combobox('setText', '没有可选班级');
					$(this).combobox('disable');
				}else{
					$(this).combobox('enable');
				}
				$('#delClass_kcdm').combobox('setValue', '');
				$('#delClass_kcdm').combobox('setText', '请先选择班级');
				$('#delClass_kcdm').combobox('disable');
			},
			onChange:function(record){
				if(record == ''){
					$('#delClass_kcdm').combobox('setValue', '');
					$('#delClass_kcdm').combobox('setText', '请先选择班级');
					$('#delClass_kcdm').combobox('disable');
				}else{
					$('#delClass_kcdm').combobox('enable');
					$('#delClass_kcdm').combobox({
						url:'<%=request.getContextPath()%>/Svl_Cjcx?active=loadDelCourseCombo&XNXQ=' + $('#delClass_xnxq').combobox('getValues') + 
							'&ZYMC=' + $('#delClass_bjlx').combobox('getValue') + '&BJMC=' + record,
						valueField:'comboValue',
						textField:'comboName',
						width:'250',
						editable:false,
						panelHeight:'140', //combobox高度
						onLoadSuccess:function(data){
							//判断data参数是否为空
							if(data != ''){
								//初始化combobox时赋值
								if($('#delClass_bjlx').combobox('getValue') == 'xzb'){
									$(this).combobox('setValue', '');
								}else{
									if(data.length > 1){
										$(this).combobox('setValue', data[1].comboValue);
									}else{
										$(this).combobox('setValue', '');
									}
								}
							}
							//判断data参数是否为空
							if(data.length == 1){
								$(this).combobox('setText', '没有可选课程');
								$(this).combobox('disable');
							}else{
								$(this).combobox('enable');
							}
						},
						onChange:function(record){
							loadDelClassScoreList();
						}
					});
				}
			}
		});
	}
	
	/**读取开课班下拉框数据**/
	function loadKkbCombo(){
		//读取开课版下拉框
		$('#print_kkb').combobox({
			url:'<%=request.getContextPath()%>/Svl_Cjcx?active=loadPrintKkbCombo&xnxq=' + $('#print_xnxq').combobox('getValues') + '&jsxm=' + $('#print_jsxm').combobox('getValue'),
			valueField:'comboValue',
			textField:'comboName',
			editable:true,
			panelHeight:'140', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data.length == 1){
					$(this).combobox('setText', '没有可打印成绩信息');
					$(this).combobox('disable');
				}else{
					$(this).combobox('enable');
					//初始化combobox时赋值
					$(this).combobox('setValue', '');
				}
			}
		});
	}
	
	/**初始化输入框事件**/
	function initScoreInput(){
		$('.scoreInput').bind('keydown', function(e){
			//ie火狐兼容
			e = e || window.event;
			var curKey = e.which || e.keyCode;
			var curValue = $(this).val();
			
			//禁用shift+数字键
	        if(e.shiftKey && (curKey>=48 && curKey<=57 || curKey==189)) return false;
	        
	        //禁用shift+.
	        if(e.shiftKey && curKey==190) return false;
	        
	        //判断如果已经输入过0
	        if(curValue == '0'){
	        	//backspace/delete/tab
				if(curKey==8 || curKey==9 || curKey==46) return true;
				//上下左右键
				if(curKey==37 || curKey==38 || curKey==39 || curKey==40) return true;
				//.
				if(curKey==190 || curKey==110) return true;
			}
			//判断是小数点后第几位
			else if(curValue.indexOf('.')>-1 && curValue.substring(curValue.indexOf('.')).length == 2){
				//backspace/delete/tab
				if(curKey==8 || curKey==9 || curKey==46) return true;
				//上下左右键
				if(curKey==37 || curKey==38 || curKey==39 || curKey==40) return true;
			}else{
				//判断如果已经输入过-
				if(curValue == '-'){
					//减号
					if(curKey==109 || curKey==189) return false;
					//0
					if(curKey==48 || curKey==96) return false;
					//.
					if(curKey==190 || curKey==110) return false;
				}
				
				//判断是否输入过小数点
				if(curValue.indexOf('.')<0 && curValue.length>0){
					//.
					if(curKey==190 || curKey==110) return true;
				}
				
				//数字
				if(curKey>=48 && curKey<=57) return true;
				//小数字键盘
				if(curKey>=96 && curKey<=105) return true;
				//减号
				if(curValue=='' && (curKey==109 || curKey==189)) return true;
				//backspace/delete/tab
				if(curKey==8 || curKey==9 || curKey==46) return true;
				//上下左右键
				if(curKey==37 || curKey==38 || curKey==39 || curKey==40) return true;
			}
			
			return false;
		}).bind('blur',function(){
			var score = $(this).val();
			
			if(score != ''){
				//判断是否为数字
				if(isNaN(score)){
					score = '';
					alertMsg('请输入正确数字');
					$(this).val(score);
				}
				
				if(score.substring(score.length-2) == '.0'){
					score = score.substring(0, score.length-2);
					$(this).val(score);
				}
				
				if(score == '0'){
					ConfirmMsg('确定当前输入的成绩是<span style="width:15px; color:red; text-align:center;">0</span>分吗？', '', 'cancelEnterZero("'+$(this).attr('id')+'");');
					return;
				}
			}
		}).bind('keyup', function(e){
			//ie火狐兼容
			e = e || window.event;
			var curKey = e.which || e.keyCode;
			var score = $(this).val();
			
			//应对中文输入法输入负号时，键值为229被屏蔽的情况。
			if(score=='' && curKey=='189'){
				$(this).val('-');
			}
			
			if(score > 100){
				score = '100';
				$(this).val(score);
			}
			if(score < -16){
				score = '-16';
				$(this).val(score);
			}
		});	
	}
	
	/**取消输入的0分**/
	function cancelEnterZero(id){
		$('#' + id).val('');
		$('#' + id).focus();
	}
	
	/**加载 dialog控件**/
	function initDialog(){
		$('#exportDialog').dialog({   
			title: '成绩导出',   
			width: 420,//宽度设置   
			height: 341,//高度设置 
			modal:true,
			closed: true,   
			cache: false, 
			draggable:true,//是否可移动dialog框设置
			//关闭事件
			onClose:function(data){
				$('.exportCombo').combobox('clear');
				$('.exportCombo').combobox('setValue', 'all');
				$('#export_zbwdf').combobox('setValue', 'exclude');
			}
		});
		
		/* 
		$('#exportClassDialog').dialog({   
			title: '整班未登分信息导出',   
			width: 420,//宽度设置   
			height: 170,//高度设置 
			modal:true,
			closed: true,   
			cache: false, 
			draggable:true,//是否可移动dialog框设置
			//关闭事件
			onClose:function(data){
				$('.exportCombo').combobox('clear');
				$('.exportCombo').combobox('setValue', 'all');
			}
		});
		*/
		
		$('#delClassDialog').dialog({   
			title: '整班成绩删除',   
			width: 750,//宽度设置   
			height: 550,//高度设置
			modal:true,
			closed: true,   
			cache: false, 
			draggable:true,//是否可移动dialog框设置
			//关闭事件
			onClose:function(data){
				$('#delClass_xnxq').combobox('clear');
				$('#delClass_xnxq').combobox('setValue', 'all');
				if($('#delClass_bjlx').combobox('setValue') == 'xzb'){
					$('#delClass_bjdm').combobox('setValue', '');
				}else{
					$('#delClass_bjlx').combobox('setValue', 'xzb');
				}
			}
		});
		
		/*
		$('#ksmcExportDialog').dialog({   
			title: '考试名册导出',   
			width: 250,//宽度设置   
			height: 80,//高度设置 
			modal:true,
			closed: true,   
			cache: false, 
			draggable:true,//是否可移动dialog框设置
			//关闭事件
			onClose:function(data){
				$('.exportCombo').combobox('clear');
				$('.exportCombo').combobox('setValue', 'all');
			}
		});
		*/
		
		$('#printDialog').dialog({   
			title: '教师成绩打印',   
			width: 420,//宽度设置   
			height: 144,//高度设置 
			modal:true,
			closed: true,
			cache: false, 
			draggable:true,//是否可移动dialog框设置
			//关闭事件
			onClose:function(data){
				$('#print_xnxq').combobox('setValue', 'all');
				$('#print_jsxm').combobox('setValue', '');
				$('#print_kkb').combobox('setText', '请先选择教师姓名');
				$('#print_kkb').combobox('disable');
			}
		});
		
		$('#reportPrintDialog').dialog({   
			title: '学生成绩单打印',   
			width: 420,//宽度设置   
			height: 194,//高度设置 
			modal:true,
			closed: true,
			cache: false, 
			draggable:true,//是否可移动dialog框设置
			//关闭事件
			onClose:function(data){
				$('.reportPrintCombo').combobox('clear');
				$('.reportPrintCombo').combobox('setValue', 'all');
				$('#print_bj').combobox('setValue', '');
				$('#print_stuState').combobox('setValue', 'normal');
			}
		});
		
		$('#printViewDialog').dialog({
			title:'打印预览',
			maximized:true,
			modal:true,
			closed: true,   
			cache: false, 
			draggable:false,//是否可移动dialog框设置
			//关闭事件
			onClose:function(data){
				$("#printViewIframe").attr('src', '');
				//删除预览文件
				delViewFile();
			}
		});
		
		$('#configDialog').dialog({
			title:'查分系统相关设置',
			width: 500,//宽度设置   
			height: 171,//高度设置 
			modal:true,
			closed: true,   
			cache: false, 
			draggable:false,//是否可移动dialog框设置
			toolbar:[{
				text:'保存',
				iconCls:'icon-save',
				handler:function(){
					doToolbar('saveConfig');
				}
			}]
		});
		
		$('#ljInfoDialog').dialog({   
			title: '留级信息',   
			width: 900,//宽度设置   
			height: 600,//高度设置 
			modal:true,
			closed: true,   
			cache: false, 
			draggable:true,//是否可移动dialog框设置
			//关闭事件
			onClose:function(data){
				$('#ljInfoIframe').attr('src', '');
			}
		});
		
		$('#bkInfoDialog').dialog({   
			title: '补考信息',   
			width: 800,//宽度设置   
			height: 600,//高度设置 
			modal:true,
			closed: true,   
			cache: false, 
			draggable:true,//是否可移动dialog框设置
			//关闭事件
			onClose:function(data){
				$('#bkInfoIframe').attr('src', '');
			}
		});
		
		$('#jdInfoDialog').dialog({   
			title: '绩点信息',   
			width: 900,//宽度设置   
			height: 600,//高度设置 
			modal:true,
			closed: true,
			cache: false, 
			draggable:true,//是否可移动dialog框设置
			//关闭事件
			onClose:function(data){
				$('#jdInfoIframe').attr('src', '');
			}
		});
		
		$('#zbwdfInfoDialog').dialog({
			title: '整班未登分信息',   
			width: 800,//宽度设置   
			height: 600,//高度设置 
			modal:true,
			closed: true,   
			cache: false, 
			draggable:true,//是否可移动dialog框设置
			//关闭事件
			onClose:function(data){
				$('#zbwdfInfoIframe').attr('src', '');
			}
		});
		
		$('#dbkInfoDialog').dialog({   
			title: '大补考信息',   
			width: 800,//宽度设置   
			height: 600,//高度设置 
			modal:true,
			closed: true,   
			cache: false, 
			draggable:true,//是否可移动dialog框设置
			//关闭事件
			onClose:function(data){
				$('#dbkInfoIframe').attr('src', '');
			}
		});
		
		$('#invalidScoreDialog').dialog({   
			title: '无效成绩信息',   
			width: 850,//宽度设置   
			height: 600,//高度设置 
			modal:true,
			closed: true,   
			cache: false, 
			draggable:true,//是否可移动dialog框设置
			//关闭事件
			onClose:function(data){
				$('#invalidScoreIframe').attr('src', '');
			}
		});
		
		$('#scoreCoverDialog').dialog({   
			title: '成绩覆盖',   
			width: 700,//宽度设置   
			height: 550,//高度设置
			modal:true,
			closed: true,
			cache: false, 
			draggable:true,//是否可移动dialog框设置
			//关闭事件
			onClose:function(data){
				$('#scoreCoverIframe').attr('src', '');
			}
		});
		
		$('#dmsmDialog').dialog({   
			title: '文字成绩代码说明',
			width: 300,//宽度设置   
			height: 275,//高度设置 
			modal:true,
			closed: true,   
			cache: false, 
			draggable:true//是否可移动dialog框设置
		});
	}
	
	/**查询成绩列表
		@type 类型
	**/
	function loadScoreList(type){
		$('#scoreList').datagrid({
			url: '<%=request.getContextPath()%>/Svl_Cjcx',
			queryParams:{'active':'queScoreList','sAuth':sAuth,'type':type,
				'STUCODE':encodeURI($('#ic_stuCode').val()),'STUNAME':encodeURI($('#ic_stuName').val()),
				'NJDM':$('#ic_njmc').combobox('getValue'),'XNXQ':$('#ic_xnxq').combobox('getValue'),
				'ZYMC':encodeURI($('#ic_zymc').val()),'KCMC':encodeURI($('#ic_kcmc').val()),
				'BJMC':encodeURI($('#ic_bjmc').val()),'JSXM':encodeURI($('#ic_jsxm').val()),
				'CJFW':$('#ic_cjfw').combobox('getValue'),'CJLX':$('#ic_cjlx').combobox('getValue')},
			title:'成绩信息列表',
			width:'100%',
			nowrap: false,
			fit:true, //自适应父节点宽度和高度
			showFooter:true,
			striped:true,
			pagination:true,
			pageSize:50,
			pageNumber:1,
			singleSelect:true,
			rownumbers:true,
			//fitColumns: true,
			frozenColumns:[[
				{field:'id',hidden:true},
				{field:'stuCode',title:'学号',width:80,align:'center'},
				{field:'stuName',title:'姓名',width:80,align:'center'},
				{field:'XZBMC',title:'班级名称',width:180,align:'center'},
				{field:'XNXQ',title:'学年学期名称',width:120,align:'center'},
				{field:'KCMC',title:'课程名称',width:200,align:'center'}
			]],
			columns:[[
				//field为读取数据的数据名，title为显示的数据名，width宽度设置，align数字在表格中显示的位置
				{field:'PSCJ',title:'平时',width:60,align:'center',
					formatter:function(value,rec){
						return parseScoreShow(value);
				}},
				{field:'QZCJ',title:'期中',width:60,align:'center',
					formatter:function(value,rec){
						return parseScoreShow(value);
				}},
				{field:'SXCJ',title:'实训',width:60,align:'center',
					formatter:function(value,rec){
						return parseScoreShow(value);
				}},
				{field:'QMCJ',title:'期末',width:60,align:'center',
					formatter:function(value,rec){
						return parseScoreShow(value);
				}},
				{field:'ZPCJ',title:'总评',width:60,align:'center',
					formatter:function(value,rec){
						return parseScoreShow(value);
				}},
				{field:'CXCJ1',title:'重修1',width:60,align:'center',
					formatter:function(value,rec){
						return parseScoreShow(value);
				}},
				{field:'CXCJ2',title:'重修2',width:60,align:'center',
					formatter:function(value,rec){
						return parseScoreShow(value);
				}},
				{field:'BKCJ',title:'补考',width:60,align:'center',
					formatter:function(value,rec){
						return parseScoreShow(value);
				}},
				{field:'DBKCJ',title:'大补考',width:60,align:'center',
					formatter:function(value,rec){
						return parseScoreShow(value);
				}}
				//{field:'DYCJ',title:'打印成绩',width:80,align:'center'}
			]],
			//单击某行时触发
			onClickRow:function(rowIndex,rowData){
				iKeyCode = rowData.id;
				rowNum = rowIndex;
				//$('#form1').form('load', rowData);
				$('#PSCJ').val(rowData.PSCJ);
				$('#QZCJ').val(rowData.QZCJ);
				$('#SXCJ').val(rowData.SXCJ);
				$('#QMCJ').val(rowData.QMCJ);
				$('#ZPCJ').val(rowData.ZPCJ);
				$('#CXCJ1').val(rowData.CXCJ1);
				$('#CXCJ2').val(rowData.CXCJ2);
				$('#BKCJ').val(rowData.BKCJ);
				$('#DBKCJ').val(rowData.DBKCJ);
				$('#DYCJ').val(rowData.DYCJ);
			},
			//加载成功后触发
			onLoadSuccess: function(data){
				iKeyCode = '';
				rowNum = '';
				scoreData = '';
			}
		});
	}
	
	function parseScoreShow(scoreStr){
		var result = scoreStr;
		
		for(var i=0; i<wzcjShowArray.length; i++){
			if(scoreStr == wzcjShowArray[i].id){
				result = wzcjShowArray[i].text;
				break;
			}
		}
		
		if(scoreStr!='' && ((scoreStr>=0 && scoreStr<60) || scoreStr==-2 || scoreStr==-3 || scoreStr==-4 || scoreStr==-10 || scoreStr==-12)){
			result = '<font color="red">'+result+'</font>';
		}
		
		return result;
	}
	
	/**工具栏按钮调用方法，传入按钮的id
		@id 当前按钮点击事件
	**/
	function doToolbar(id){ 
		//打印
		if(id == 'print'){
			$('#printDialog').dialog('open');
		}
		
		//打印预览
		if(id == 'printView'){
			if($('#print_jsxm').combobox('getValue')==undefined || $('#print_jsxm').combobox('getValue')==''){
				alertMsg('请选择教师姓名');
				return;
			}
			if($('#print_kkb').combobox('getValue')==undefined || $('#print_kkb').combobox('getValue')==''){
				alertMsg('请选择开课班');
				return;
			}
			
			/*
			$('#printViewDialog').dialog('open');
			$("#printViewIframe").attr("src","<-%=request.getContextPath()%>/form/registerScoreManage/scorePrintView.jsp?XNXQ=" + $('#print_xnxq').combobox('getValues') + 
					"&JSXM=" + $('#print_jsxm').combobox('getValue') + "&KKB=" + $('#print_kkb').combobox('getValues'));
			*/
			if(pofOpenType == 'normal'){
				$('#printViewDialog').dialog('open');
				$("#printViewIframe").attr("src","<%=request.getContextPath()%>/form/registerScoreManage/scorePrintView.jsp?XNXQ=" + $('#print_xnxq').combobox('getValues') + 
						"&JSXM=" + $('#print_jsxm').combobox('getValue') + "&KKB=" + $('#print_kkb').combobox('getValues'));
			}else{
				$.ajax({
					type : "POST",
					url : '<%=request.getContextPath()%>/Svl_Cjcx',
					data : "active=loadTeaPageOfficeLink&XNXQ=" + $('#print_xnxq').combobox('getValues') + 
						"&JSXM=" + $('#print_jsxm').combobox('getValue') + "&KKB=" + $('#print_kkb').combobox('getValues'),
					dataType:"json",
					success : function(data){
						$('#pageOfficeLink').attr('href', data[0].linkStr);
						document.getElementById("pageOfficeLink").click();
					}
				});
			}
		}
		
		//成绩单打印
		if(id == 'reportPrint'){
			$('#reportPrintDialog').dialog('open');
		}
		
		//成绩单打印预览
		if(id == 'reportPrintView'){
			if($('#print_bj').combobox('getValue')=='' && $('#print_xh').combobox('getValue')==''){
				alertMsg('班级、学号、姓名必须填写至少一项');
				return;
			}
			
			if(pofOpenType == 'normal'){
				$('#printViewDialog').dialog('open');
				$("#printViewIframe").attr("src","<%=request.getContextPath()%>/form/registerScoreManage/stuReportPrintView.jsp?QSXNXQ=" + $('#print_qsxnxq').combobox('getValue') + 
						"&JSXNXQ=" + $('#print_jsxnxq').combobox('getValue') + "&BJBH=" + $('#print_bj').combobox('getValue') + "&STUSTATE=" + $('#print_stuState').combobox('getValue') + 
						"&STUCODE=" + $('#print_xh').combobox('getValue'));
			}else{
				$.ajax({
					type : "POST",
					url : '<%=request.getContextPath()%>/Svl_Cjcx',
					data : "active=loadStuPageOfficeLink&QSXNXQ=" + $('#print_qsxnxq').combobox('getValue') + 
						"&JSXNXQ=" + $('#print_jsxnxq').combobox('getValue') + "&BJBH=" + $('#print_bj').combobox('getValue') + "&STUSTATE=" + $('#print_stuState').combobox('getValue') + "&STUCODE=" + $('#print_xh').combobox('getValue'),
					dataType:"json",
					success : function(data){
						$('#pageOfficeLink').attr('href', data[0].linkStr);
						document.getElementById("pageOfficeLink").click();
					}
				});
			}
		}
		
		//解锁
		if(id == 'unlock'){
			if($('#print_kkb').combobox('getValues') == ''){
				alertMsg('请选择教师姓名');
				return;
			}
			
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_Cjcx',
				data : 'active=unlock&XNXQ=' + $('#print_xnxq').combobox('getValues') + '&iKeyCode=' + $('#print_kkb').combobox('getValues') + '&JSXM=' + $('#print_jsxm').combobox('getValue'),
				dataType:"json",
				success : function(data){
					if(data[0].MSG == '解锁成功'){
						showMsg(data[0].MSG);
					}else{
						alertMsg(data[0].MSG);
					}
				}
			});
		}
		
		//未打印清单导出
		if(id == 'wdyExport'){
			$('#maskFont').html('文件生成中，请稍后...');
			$('#divPageMask').show();
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_Cjcx',
				data : 'active=wdyExport&XNXQ=' + $('#print_xnxq').combobox('getValues'),
				dataType:"json",
				success : function(data){
					if(data[0].MSG == '文件生成成功'){
						//下载文件到本地
						$("#exportIframe").attr("src", '<%=request.getContextPath()%>/form/registerScoreManage/download.jsp?filePath=' + encodeURIComponent(data[0].filePath));
					}else{
						alertMsg(data[0].MSG);
					}
					$('#divPageMask').hide();
				}
			});
		}
		
		//导出
		if(id == 'export'){
			$('#exportDialog').dialog('open');
		}
		
		//整班未登分导出
		if(id == 'exportClassInfo'){
			$('#zbwdfInfoIframe').attr('src', '<%=request.getContextPath()%>/form/registerScoreManage/zbwdfdc.jsp');
			$('#zbwdfInfoDialog').dialog('open');
			//$('#exportClassDialog').dialog('open');
		}
		
		//考试名册导出
		/*
		if(id == 'ksmcExport'){
			$('#ksmcExportDialog').dialog('open');
		}
		*/
		
		//打开补考名单
		if(id == "openBkInfo"){
			$('#bkInfoIframe').attr('src', '<%=request.getContextPath()%>/form/registerScoreManage/bkInfo.jsp');
			$('#bkInfoDialog').dialog('open');
		}
		
		//打开留级名单
		if(id == "openLjInfo"){
			$('#ljInfoIframe').attr('src', '<%=request.getContextPath()%>/form/registerScoreManage/ljInfo.jsp?curXn=' + curXn);
			$('#ljInfoDialog').dialog('open');
		}
		
		//打开绩点信息列表
		if(id == "openJdInfo"){
			$('#jdInfoIframe').attr('src', '<%=request.getContextPath()%>/form/registerScoreManage/jdInfo.jsp?curXnxq=' + curXnxq);
			$('#jdInfoDialog').dialog('open');
		}
		
		//打开大补考名单
		if(id == "openDbkInfo"){
			$('#dbkInfoIframe').attr('src', '<%=request.getContextPath()%>/form/registerScoreManage/dbkInfo.jsp');
			$('#dbkInfoDialog').dialog('open');
		}
		
		//成绩导出
		if(id=='skcjExport' || id=='hzcjExport'){
			$('#maskFont').html('文件生成中，请稍后...');
			$('#divPageMask').show();
			$('#export_startSem').val($('#export_qsxnxq').combobox('getValues').toString());
			$('#export_endSem').val($('#export_jsxnxq').combobox('getValues').toString());
			$('#export_dept').val($('#export_xb').combobox('getValues').toString());
			$('#export_grade').val($('#export_nj').combobox('getValues').toString());
			$('#export_major').val($('#export_zy').combobox('getValues').toString());
			$('#export_class').val($('#export_bj').combobox('getValues').toString());
			$('#export_range').val($('#export_cjfw').combobox('getValue'));
			$('#export_zbwdfFlag').val($('#export_zbwdf').combobox('getValue'));
			$('#export_scoreType').val($('#export_cjlx').combobox('getValues').toString());
			$('#export_stuType').val($('#export_zslb').combobox('getValues').toString());
			$('#export_subject').val($('#export_xkmc').combobox('getValues').toString());
			$('#active').val(id);
			$('#form1').submit();
		}
		
		//整班未登分成绩导出
		/*
		if(id == 'zbwdfExport'){
			$('#maskFont').html('文件生成中，请稍后...');
			$('#divPageMask').show();
			$('#export_startSem').val($('#exportClass_qsxnxq').combobox('getValues').toString());
			$('#export_endSem').val($('#exportClass_jsxnxq').combobox('getValues').toString());
			$('#export_grade').val($('#exportClass_nj').combobox('getValues').toString());
			$('#export_range').val($('#exportClass_kszc').combobox('getValue'));
			$('#active').val(id);
			$('#form1').submit();
		}
		*/
		
		//查询学生成绩列表
		if(id == 'queStuScore'){
			if($('#ic_stuCode').val()=='' && $('#ic_stuName').val()==''){
				alertMsg('请填写学生学号或姓名');
				return;
			}
			
			queType = id;
			loadScoreList(queType);
		}
		
		//查询成绩列表
		if(id == 'queScore'){
			queType = id;
			loadScoreList(queType);
		}
		
		//修改成绩
		if(id == 'saveScore'){
			if(iKeyCode == ''){
				alertMsg('请选择一条学生的成绩');
				return;
			}
			
			$('#active').val(id);
			$('#iKeyCode').val(iKeyCode);
			$('#form1').submit();
		}
		
		//成绩覆盖
		if(id == 'scoreCover'){
			$('#scoreCoverIframe').attr('src', '<%=request.getContextPath()%>/form/registerScoreManage/scoreCover.jsp');
			$('#scoreCoverDialog').dialog('open');
		}
		
		//代码说明
		if(id == 'dmsm'){
			$('#dmsmDialog').dialog('open');
		}
		
		//查分设置
		if(id == 'config'){
			$('#configDialog').dialog('open');
		}
		
		//保存查分设置
		if(id == 'saveConfig'){
			var beginDate = $("#ic_ksrq").datebox('getValue');
			var endDate = $("#ic_jsrq").datebox('getValue');
			
			if(beginDate == ''){
				alertMsg("请选择开始日期");
				return;
			}
			
			if(endDate == ''){
				alertMsg("请选择结束日期");
				return;
			}
			
			//判断开始时间是否大于结束时间
			if(beginDate > endDate){
				alertMsg("开始日期必须在结束日期之前");
				return;
			}
			
			$('#active').val(id);
			$('#kfxnxq').val($('#ic_kfxnxq').combobox('getValues').toString());
			$('#kfcjlx').val($('#ic_kfcjlx').combobox('getValues').toString());
			$('#beginDate').val(beginDate);
			$('#endDate').val(endDate);
			$("#form1").submit();
		}
		
		//修改学生成绩信息为无效
		if(id == 'changeStuScoreState'){
			if(iKeyCode == ''){
				alertMsg('请选择一条成绩数据');
				return;
			}
			
			ConfirmMsg('是否确定要删除当前选中的成绩信息？', 'changeStuScoreState();', '');
		}
		
		//修改整班学生成绩信息为无效
		if(id == 'changeClassScoreSate'){
			loadDelClassScoreList();
			$('#delClassDialog').dialog('open');
		}
		
		//批量删除学生成绩信息（修改状态）
		if(id == 'changeMultiStuScoreState'){
			var stuData = $('#stuScoreList').datagrid('getSelections');
			
			if(stuData.length == 0){
				alertMsg('请至少选择一条成绩');
				return;
			}
			
			var stuCode = '';
			for(var i=0; i<stuData.length; i++){
				stuCode += stuData[i].编号+',';
			}
			stuCode = stuCode.substring(0, stuCode.length-1);
			
			ConfirmMsg('是否确定要删除选择的学生成绩？', 'changeMultiStuScoreState("' + stuCode + '");', '');
		}
		
		//同步查分数据
		if(id == 'sync'){
			$('#maskFont').html('同步数据中，请稍后...');
			$('#divPageMask').show();
			syncData();
		}
		
		//打开无效成绩列表
		if(id == 'openInvalidScore'){
			$('#invalidScoreIframe').attr('src', '<%=request.getContextPath()%>/form/registerScoreManage/invalidScoreInfo.jsp');
			$('#invalidScoreDialog').dialog('open');
		}
	}
	
	/**修改成绩状态为无效*/
	function changeStuScoreState(){
		$.ajax({
			type:"POST",
			url:'<%=request.getContextPath()%>/Svl_Cjcx',
			data:'active=changeStuScoreState&iKeyCode=' + iKeyCode,
			dataType:"json",
			success:function(data) {
				if(data[0].MSG == '删除成功'){
					showMsg(data[0].MSG);
					$('#scoreList').datagrid('deleteRow', rowNum);
				}else{
					alertMsg(data[0].MSG);
				}
			}
		});
	}
	
	/**同步查分数据*/
	function syncData(){
		$.ajax({
			type:"POST",
			url:'<%=request.getContextPath()%>/Svl_Cjcx',
			data:'active=syncData',
			dataType:"json",
			success:function(data) {
				if(data[0].MSG == '同步成功'){
					showMsg(data[0].MSG);
				}else{
					alertMsg(data[0].MSG);
				}
				$('#divPageMask').hide();
			}
		});
	}
	
	/**表单提交**/
	$('#form1').form({
		//定位到servlet位置的url
		url:'<%=request.getContextPath()%>/Svl_Cjcx',
		//当点击事件后触发的事件
		onSubmit: function(data){}, 
		//当点击事件并成功提交后触发的事件
		success:function(data){
			var json  =  eval("("+data+")");
			
			if($('#active').val()=='skcjExport' || $('#active').val()=='hzcjExport' /* || $('#active').val()=='zbwdfExport' */){
				if(json[0].MSG == '成绩文件生成成功'){
					//下载文件到本地
					$("#exportIframe").attr("src", '<%=request.getContextPath()%>/form/registerScoreManage/download.jsp?filePath=' + encodeURIComponent(json[0].filePath));
				}else{
					alertMsg(json[0].MSG);
				}
				$('#divPageMask').hide();
			}else{
				if(json[0].MSG == '保存成功'){
					showMsg(json[0].MSG);
					
					if($('#active').val()=='saveConfig'){
						$('#configDialog').dialog('close');
					}else{
						$('#form1').form('clear');
						loadScoreList(queType);
					}
				}else{
					alertMsg(json[0].MSG);
				}	
			}
		}
	});
	
	function closeDialog(){
		$('#printViewDialog').dialog('close');
	}
	
	/**锁定课程成绩**/
	function lockScore(){
		$.ajax({
			type:"POST",
			url:'<%=request.getContextPath()%>/Svl_Cjcx',
			data:'active=lockScore&XNXQ=' + $('#print_xnxq').combobox('getValues') + 
				'&JSXM=' + $('#print_jsxm').combobox('getValue') + '&iKeyCode=' + $('#print_kkb').combobox('getValues'),
			dataType:"json",
			success:function(data) {
				/*
				if(data[0].MSG == '锁定成功'){
					showMsg(data[0].MSG);
				}else{
					alertMsg(data[0].MSG);
				}
				*/
			}
		});
	}
	
	/**删除预览文件**/
	function delViewFile(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Cjcx',
			data : 'active=delViewFile&filePath=' + viewFilePath,
			dataType:"json",
			success : function(data){
				if(data[0].MSG == '删除成功') viewFilePath = '';
			}
		});
	}
	
	/**下载成绩单*/
	function downloadStuReport(){
		$('#printViewDialog').panel('minimize');
		$('#maskFont').html('文件生成中，请稍后...');
		$('#divPageMask').show();
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Cjcx',
			data : 'active=exportStuReport&QSXNXQ=' + $('#print_qsxnxq').combobox('getValue') + "&JSXNXQ=" + $('#print_jsxnxq').combobox('getValue') + 
				"&BJMC=" + $('#print_bj').combobox('getValue') + "&STUCODE=" + $('#print_xh').combobox('getValue'),
			dataType:"json",
			success : function(data){
				if(data[0].MSG == '文件生成成功'){
					//下载文件到本地
					$("#exportIframe").attr("src", '<%=request.getContextPath()%>/form/registerScoreManage/download.jsp?filePath=' + encodeURIComponent(data[0].filePath));
				}else{
					alertMsg(data[0].MSG);
				}
				$('#printViewDialog').dialog('open');
				$('#divPageMask').hide();
			}
		});
	}
	
	/**加载学生成绩列表datagrid控件，读取页面信息**/
	function loadDelClassScoreList(){
		$('#stuScoreList').datagrid({
			url: '<%=request.getContextPath()%>/Svl_Cjcx',
			queryParams:{'active':'queDelClassScoreList','iKeyCode':$('#delClass_kcdm').combobox('getValue')},
			title:'学生成绩信息列表',
			width:'100%',
			nowrap: false,
			fit:true, //自适应父节点宽度和高度
			showFooter:true,
			striped:true,
			singleSelect:false,
			rownumbers:true,
			fitColumns: true,
			columns:[[
				//field为读取数据的数据名，title为显示的数据名，width宽度设置，align数字在表格中显示的位置
				{field:'ck',checkbox:true},
				{field:'编号',hidden:true},
				{field:'班内学号',title:'学号',width:fillsize(0.06),align:'center'},
				{field:'姓名',title:'姓名',width:fillsize(0.1),align:'center'},
				{field:'学号',title:'学籍号',width:fillsize(0.17),align:'center'},
				{field:'学年学期',title:'学年学期',width:fillsize(0.09),align:'center'},
				{field:'平时',title:'平时',width:fillsize(0.06),align:'center'},
				{field:'期中',title:'期中',width:fillsize(0.06),align:'center'},
				{field:'实训',title:'实训',width:fillsize(0.06),align:'center'},
				{field:'期末',title:'期末',width:fillsize(0.06),align:'center'},
				{field:'总评',title:'总评',width:fillsize(0.06),align:'center'},
				{field:'重修1',title:'重修1',width:fillsize(0.07),align:'center'},
				{field:'重修2',title:'重修2',width:fillsize(0.07),align:'center'},
				{field:'补考',title:'补考',width:fillsize(0.06),align:'center'},
				{field:'大补考',title:'大补考',width:fillsize(0.07),align:'center'}
			]],
			//加载成功后触发
			onLoadSuccess: function(data){
			}
		});
	};
	
	/**批量删除学生成绩(修改成绩状态)*/
	function changeMultiStuScoreState(code){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Cjcx',
			data : 'active=changeMultiStuScoreState&iKeyCode=' + code,
			dataType:"json",
			success : function(data) {
				if(data[0].MSG == '删除成功'){
					showMsg(data[0].MSG);
					loadDelClassScoreList();
				}else{
					alertMsg(data[0].MSG);
				}
			}
		});
	}
</script>
</html>