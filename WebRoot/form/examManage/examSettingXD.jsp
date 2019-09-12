<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Strict//EN">

<%
	/**   
		创建人：wangzh
		Create date: 2015.06.05
		功能说明：用于设置班级固排禁排
		页面类型:列表及模块入口
		修订信息(有多次时,可有多个)
		原因:
		修订人:
		修订时间:
	**/
 %>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Vector"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.src.develop.store.user.*"%>
<%
	/*
		获得角色信息
	 */
	UserProfile up = new UserProfile(request,MyTools.getSessionUserCode(request));
	String userName = up.getUserName();
	if (userName == null) {
		userName = "";
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
	if (v != null) {
		for (int i = 0; i < v.size(); i++) {
			if (i == v.size() - 1) {
				sAuth += MyTools.StrFiltr(v.get(i));
			} else {
				sAuth += MyTools.StrFiltr(v.get(i)) + "O";
			}
		}
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
		<link rel="stylesheet" href="<%=request.getContextPath()%>/Kalendajs/build/kalendae.css" type="text/css" charset="utf-8">
		<script src="<%=request.getContextPath()%>/Kalendajs/build/kalendae.standalone.js" type="text/javascript" charset="utf-8"></script>
	
	</head>

	<style type="text/css">
		.kalendae .k-days span.closed {
			background:red;
		}
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
		
		#xlTime9{
			width:100%;
			border-right:1px solid #99bbe8;
		}
		#xlTime9{
			width:99.9%;
			border-top:1px solid #99bbe8;
		}
		#xlTime9 td{
			width:300px;
			border-left:1px solid #99bbe8;
			border-bottom:1px solid #99bbe8;
			text-align:center;
			empty-cells:show;
			font-size:11;
		}
		#xlTime9 th{
			width:300px;
			border-left:1px solid #99bbe8;
			border-bottom:1px solid #99bbe8;
			text-align:center;
			empty-cells:show;
		}
		#divPageMask2{background-color:#D2E0F2; filter:alpha(opacity=50);left:0px;top:0px;z-index:100;}
		#divPageMask3{background-color:#D2E0F2; filter:alpha(opacity=50);left:0px;top:0px;z-index:100;}
		
		#maskFontBK{
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
		#maskFontDBK{
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
			font-size:12;
			border-left:1px solid #95B8E7;
			border-bottom:1px solid #95B8E7;
			background-color:#FFFFFF;
			text-align:center;
		}
		
		.kcbTitle{
			 font-weight:bold;
			 font-size:14;
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
			font-size:12;
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
	</style>
	
	<body class="easyui-layout" onselectstart="return (event.srcElement.type=='text');">
		<div id="north" region="north" title="考试信息设置" style="height:91px;" >
			<table>
				<tr>
					<td>
						<a href="#" id="newEX" class="easyui-linkbutton" plain="true" iconcls="icon-new" onClick="doToolbar(this.id);" title="">新建</a>
						<a href="#" id="editEX" class="easyui-linkbutton" plain="true" iconcls="icon-edit" onClick="doToolbar(this.id);" title="">编辑</a>
						<a href="#" id="delEX" class="easyui-linkbutton" plain="true" iconcls="icon-cancel" onClick="doToolbar(this.id);" title="">删除</a>
<!-- 						<a href="#" onclick="doToolbar(this.id);" id="importExam" class="easyui-linkbutton" data-options="iconCls:'icon-site',plain:true">导入考试课程</a> 								 -->
<!-- 						<a href="#" onclick="selectKSXS();" id="export" class="easyui-linkbutton" data-options="iconCls:'icon-submit',plain:true">导出考试信息</a> -->
<!-- 						<a href="#" onclick="doToolbar(this.id);" id="notcourse" class="easyui-linkbutton" data-options="iconCls:'icon-collection_edit',plain:true">未排课程</a> -->
<!-- 					    <a href="#" onclick="doToolbar(this.id);" id="allksxs" class="easyui-linkbutton" data-options="iconCls:'icon-collection_edit',plain:true">考试信息查询</a> -->
					</td>
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

			
					<!-- 新建考试信息 -->
					<div id="newExam" title="新建">
						
							<table class = "tablestyle" width="100%">
								<tr>
									<a href="#" class="easyui-linkbutton" id="saveNewExam" name="saveNewExam" iconCls="icon-save" plain="true" onClick="doToolbar(this.id)">保存</a>
								</tr>	
								<tr>
									<td class="titlestyle" width="70px">班级名称</td>
									<td id="">
										<select name="GS_BJMC" id="GS_BJMC" class="easyui-combobox" panelHeight="auto" style="width:240px;" >
										</select>
									</td>
								</tr>				
								<tr>
									<td class="titlestyle">学科名称</td>
									<td id="">
										<select name="GS_KCMC" id="GS_KCMC" class="easyui-combobox" panelHeight="auto" style="width:240px;" >
										</select>
									</td>
								</tr>
								
							</table>
										
					</div>
										
					<div id="showDialog" style="width:250px;height:141px;">
						<iframe id="iframe" name="iframe" width="100%" height="100%"></iframe>
					</div>
										
					<!-- 考试新建编辑 -->
					<div id="createExam">
						<form id="form1" method='post'>
							<table style="width:100%;" class="tablestyle">
								<tr>
									<td class="titlestyle">学年学期</td>
									<td>
										<select name="ex_xnxq" id="ex_xnxq" class="easyui-combobox" style="width:200px;" ></select>
									</td>
								</tr>
								<tr>
									<td class="titlestyle">教学性质</td>
									<td>
										<select name="ex_jxxz" id="ex_jxxz" class="easyui-combobox" style="width:200px;" ></select>
									</td>
								</tr>
								<tr>
									<td class="titlestyle">考试名称</td>
									<td>
										<input name="ex_ksmc" id="ex_ksmc" class="easyui-textbox" style="width:200px;" required="true">
									</td>
								</tr>
<!-- 								<tr> -->
<!-- 									<td class="titlestyle">考试类型</td> -->
<!-- 									<td> -->
<!-- 										<select id="ex_kslx" name="ex_kslx" class="easyui-combobox" style="width:200px;" panelHeight="auto" editable="false"  > -->
<!-- 										</select> -->
<!-- 									</td> -->
<!-- 								</tr> -->
								
							</table>
							
							<input type="hidden" id="active" name="active"/>
							<input type="hidden" id="ksapbh" name="ksapbh"/>
							<input type="hidden" id="ex_ksrq" name="ex_ksrq"/>
						</form>
					</div>
					
					<!-- 考试信息查询 -->
					<div id="allKSXSDiv" title="编辑">
						<div class="easyui-layout" style="height:100%; width:100%;">
							<div region="north" style="height:92px;overflow:hidden;">
								<table width = "100%">
									<tr>
										<td>
											<a href="#" onclick="doToolbar(this.id);" id="edit"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-edit'">编辑</a>
											<a href="#" onclick="doToolbar(this.id);" id="exportWXksxs"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-submit'">导出未选考试形式</a>
										</td>
									</tr>
								</table >
								<table width = "100%" class="tablestyle">
									<tr>
										<td class="titlestyle" >学年学期</td>
										<td>
											<input name="GS_XNXQ_CX" id="GS_XNXQ_CX" class="easyui-combobox" />
										</td>
										<td class="titlestyle" >考试名称</td>
										<td>
											<input name="GS_KSMC_CX" id="GS_KSMC_CX" class="easyui-combobox" />
										</td>
										<td class="titlestyle">考试形式</td>
								   		<td>
								   			<input name="GS_KSXS_CX" id="GS_KSXS_CX" class="easyui-combobox" />
										</td>
										<td rowspan=2>
											<a href="#" id="queAll" class="easyui-linkbutton" plain="true" iconCls="icon-search" onclick="doToolbar(this.id);">查询</a>
										</td>
									</tr>
									<tr>
										<td class="titlestyle">专业名称</td>
								   		<td>
								   			<input class="easyui-combobox" id="GS_ZYMC_CX" name="GS_ZYMC_CX" />
										</td>
						   				<td class="titlestyle">课程名称</td>
								   		<td>
								   			<input class="easyui-textbox" id="GS_KCMC_CX" name="GS_KCMC_CX" />
										</td>	
						   				<td class="titlestyle" >行政班名称</td>
								   		<td>
								   			<input class="easyui-textbox" id="GS_XZBMC_CX" name="GS_XZBMC_CX" />
										</td>
										
						   			</tr>
								</table>
							</div>
							<div region="center">
								<table id="allksxstable" class = "tablestyle" width="938px">
								</table>
							</div>
						</div>
						
					</div>
					
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
										<div id="north" region="north" title="考试安排" style="height:61px;" >
											<table style="width:100%;">
												<tr>
													<td style="width:340px;">
														<a href="#" id="saveKCAP" class="easyui-linkbutton" plain="true" iconcls="icon-save" onClick="doToolbar(this.id);" title="" disabled="disabled">保存</a>														
													</td>
												</tr>
											</table>
										</div>
										<div region="center" id="" style="">
											<table id="examDataTable" class = "tablestyle" width="100%">
											</table>
										</div>
									</div>
							</div>	
						</div>		
					</div>									
					
					<!-- 考试日期编辑 -->
					<div id="examDateDialog" title="">
							<table id="" class = "tablestyle" width="100%">
								<tr>
									<td>
										<a href="#" class="easyui-linkbutton" id="newExamDate" name="newExamDate" iconCls="icon-new" plain="true" onClick="doToolbar(this.id)">添加</a>	
										<a href="#" class="easyui-linkbutton" id="editExamDate" name="editExamDate" iconCls="icon-edit" plain="true" onClick="doToolbar(this.id)">编辑</a>	
										<a href="#" class="easyui-linkbutton" id="delExamDate" name="delExamDate" iconCls="icon-cancel" plain="true" onClick="doToolbar(this.id)">删除</a>							
									</td>	
								</tr>
							</table>
							<table id="examDateTable" width="100%">													
							</table>
					</div>
					
					<div id="editExamDateDialog" title="">
							<table id="" class = "tablestyle" width="100%">
								<tr>
									<td class="titlestyle">考试日期</td>
								   	<td>
								   		<input class="easyui-datebox" id="DA_KSRQ" name="DA_KSRQ" />
									</td>	
								</tr>
								<tr>
									<td class="titlestyle">开始时间</td>
								   	<td>
								   		<input class="easyui-textbox" id="DA_KSSJ" name="DA_KSSJ" />
									</td>	
								</tr>
								<tr>
									<td class="titlestyle">结束时间</td>
								   	<td>
								   		<input class="easyui-textbox" id="DA_JSSJ" name="DA_JSSJ" />
									</td>	
								</tr>
							</table>
					</div>
					
					<!-- 设置考试课程 -->
<!-- 					<div id="examCourseDialog" > -->
<!-- 						<div class="easyui-layout" style="height:100%; width:100%;"> -->
<!-- 							<div region="north" style="height:62px;overflow:hidden;" >	 -->
<!-- 								<table id="" class = "" width="100%"> -->
<!-- 									<tr> -->
<!-- 										<td> -->
<!-- 											<a href="#" class="easyui-linkbutton" id="saveExamCourse" name="saveExamCourse" iconCls="icon-save" plain="true" onClick="doToolbar(this.id)">保存</a>				 -->
<!-- 										</td>	 -->
<!-- 									</tr> -->
<!-- 								</table> -->
<!-- 								<table class = "tablestyle" width="100%"> -->
<!-- 								<tr> -->
<!-- 									<td width="20%" align="center" >专业名称</td> -->
<!-- 									<td> -->
<!-- 										<input name="kc_zydm" id="kc_zydm" class="easyui-combobox" style="width:300px;"  /> -->
<!-- 									</td> -->
									
<!-- 								</tr>						 -->
<!-- 								</table> -->
<!-- 							</div> -->
<!-- 							<div region="center" > -->
<!-- 								<table id="examCourseTable" class = "tablestyle" > -->
<!-- 								</table> -->
<!-- 							</div> -->
<!-- 						</div> -->
<!-- 					</div> -->
					
					<!-- 导入监考教师 -->
					<div id="teaDeptDialog" title="请选择部门" style="overflow-x:hidden;">					
						<table id="deptInfo" class = "tablestyle" width="286px">
						</table>
					</div>
					
					<!-- 设置监考教师 -->
					<div id="examTeacherDialog" >
						<div class="easyui-layout" style="height:100%; width:100%;">
							<div data-options="region:'north'" style="background:#fafafa;height:65px;">
								<table>
									<tr>
										<td>
											<a id="editTeacher" onclick="doToolbar(this.id)" class="easyui-linkbutton" href="javascript:void(0);" data-options="plain:true,iconCls:'icon-edit'">编辑</a>
										</td>
									</tr>
								</table>
								<!--查询条件-->
						        <table width='100%' class='tablestyle'>
									<tr>
										<td class="titlestyle">教师编号</td>
						                <td>
						                	<input style="width:200px" class="easyui-textbox" id="GT_JSBH" name="GT_JSBH"/>
						                </td>
						                <td class="titlestyle">教师姓名</td>
						                <td>
						                	<input style="width:200px" class="easyui-textbox" id="GT_JSXM" name="GT_JSXM"/>
						                </td>
						                <td class="titlestyle">教师类型</td>
						                <td>
						                	<input style="width:200px" class="easyui-textbox" id="GT_JSLX" name="GT_JSLX"/>
						                </td>
						                <td >
											<a id="searchTea" name="searchTea" onClick="doToolbar(this.id)" class="easyui-linkbutton" href="javascript:void(0);" data-options="plain:true,iconCls:'icon-search'">查询</a>
										</td>
									</tr>
								</table>
							</div>
							<div data-options="region:'center'">
								<table id='examTeacherTable' width="100%" ></table>
							</div>
						</div>
					</div>
					
					<div id="editTeaDialog" title="编辑">
						<div data-options="region:'center'">
							<table class = "tablestyle" width="100%">
								<tr>
									<a href="#" class="easyui-linkbutton" id="saveTeaNum" name="saveTeaNum" iconCls="icon-save" plain="true" onClick="doToolbar(this.id)">保存</a>
								</tr>
								<tr>
									<td class="titlestyle">最大监考次数</td>
									<td>
										<input name="GT_ZDJKCS" id="GT_ZDJKCS" class="easyui-numberbox" style="width:180px;"/>
									</td>
								</tr>
							</table>	
						</div>
					</div>
					
					<!-- 选中的课表课程 -->
					<table id="selectCourse" style="position:absolute; z-index:99999; display:none; font-size:12px; border:1px solid #CCCCCC; background-color:#FFE48D; text-align:center; padding-top:2px;">
						<tr><td id="selectContent"></td></tr>
					</table>
					
					
					<!-- 考试规则编辑 -->
					<div id="editTeaDateDialog" title="编辑">
							<table id="teadatetable" class = "tablestyle" width="100%">
								<tr>
									<td>
										<a href="#" class="easyui-linkbutton" id="submitTeaDate" name="submitTeaDate" iconCls="icon-save" plain="true" onClick="doToolbar(this.id)">保存</a>				
									</td>	
								</tr>
							</table>
							<table class = "tablestyle" width="100%">
							<tr>
								<td width="40%" align="center" >监考日期</td>
								<td>
									<input name="tea_JKRQ" id="tea_JKRQ" class="easyui-combobox" style="width:200px;"  />
								</td>
							</tr>						
							</table>
					</div>
					
					
					<!-- 考试规则 -->
					<div id="scheduleDialog" style="">
						<div class="easyui-layout" style="height:100%; width:100%;">
							<div region="north" style="height:61px;overflow:hidden;">
								<table width = "100%" class = "tablestyle">
									<tr>
										<td>
											<a href="#" onclick="doToolbar(this.id);" id="editgz"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-edit'">编辑</a>	
											<a href="#" onclick="doToolbar(this.id);" id="specialgz" class="easyui-linkbutton" data-options="iconCls:'icon-collection_edit',plain:true">设置特殊课程</a>
											<a href="#" onclick="doToolbar(this.id);" id="cleargz"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-cancel'">清空</a>	
										</td>
									</tr>
								</table >
								<table id="ee" singleselect="true" width="100%" class="tablestyle">
										<tr>
											<td  align="center" >考试类型</td>
											<td>
												<select name="KS_KSLX" id="KS_KSLX" class="easyui-combobox" style="width:200px;" panelHeight="60"  >
												</select>
											</td>
										</tr>
								</table>								
							</div>
							<div region="center" >
								<table id="listKSGZ" class = "tablestyle" style="">
								</table>
							</div>
						</div>		
					</div>
					
	
					<!-- 考试课程导出页面 -->
					<div id="exportTimetable">
						<!--引入编辑页面用Ifram-->
						<iframe id="exportTimetableiframe" name="exportTimetableiframe" src='' style='width:100%; height:100%;' frameborder="0" scrolling="no"></iframe>
					</div>
					
					<!-- 导入考试信息 -->
					<div id="importExamDialog" title="请选择要导入的范围">
						<table width="236px" class="tablestyle">
							<tr>
								<td colspan=2 align="left">
								<a href="#"	onclick="doToolbar(this.id)" id="importExamSubmit" class="easyui-linkbutton" plain="true" iconcls="icon-save">确定</a></td>
							</tr>
							<tr>
								<td class="titlestyle">学年学期</td>
								<td >
									<select id="DR_XNXQ" name="DR_XNXQ" class="easyui-combobox" style="width:150px;" panelHeight="auto"></select>
								</td>
							</tr>
							<tr>
								<td class="titlestyle">考试名称</td>
								<td >
									<select id="DR_KSMC" name="DR_KSMC" class="easyui-combobox" style="width:150px;" panelHeight="auto"></select>
								</td>
							</tr>
						</table>
					</div>

		<!-- 下载excel -->
		<iframe id="exportIframe" src="" style="width:0; height:0;"></iframe>
		
	</body>
	
	<script type="text/javascript">
		var sAuth = '<%=sAuth%>';
		var iUSERCODE="<%=MyTools.getSessionUserCode(request)%>";
		var row = '';      //行数据
		var rowkcap=0; //考场安排rowindex
		var iKeyCode = ''; //定义主键  考试信息设置
		var iKeyCodecx = ''; //定义主键  考试信息查询
		var iKeyCodefl = ''; //定义主键  考试信息查询分类

		var pageNum = 1;   //datagrid初始当前页数
		var pageSize = 20; //datagrid初始页内行数
		var classId = "";//班级号
		var className = "";//班级名称
		var parentId = "";//父节点
		var xnxq_cx = '';//查询条件
		var jxxz_cx = '';
		var xnxq = "";//学年学期下拉框数据
		var jxxz = "";//教学性质下拉框数据
		var qzqm = "";//考试周期下拉框数据
		var xnxqVal = "";//当前学年学期数据
		var jxxzVal = "";//当前教学性质数据
		var qzqmVal = "";//当前期中期末选择
		var ei_xnxq="";//传递学年学期
		var ei_jzzs="";//传递截止周数
		var ei_zbbh="";//考试主表编号
		var mrxq="";//默认学期
		var weeks="";//总周次
		var paike="0";//排课状态

		var kcmc = "";//课程名称
		var jsxm = "";//教师姓名
		var skzcxq = "";//授课周次详情
		var cdyq = "";//场地要求
		var cdmc = "";//场地名称
		var kszq = "";//考试周期（期中期末）
		var zjs = "";//总节数
		var gjs = "";//固排已排节数
		var lj = "";//连节
		var lc = "";//连次
		var lcs= "";//已排连次次数
		var aod= "";//判断添加或删除
		var checkrec="";
		var delinfo = "";
		var lastIndex=-1;//保存上次选中的授课计划
		var classnum="";//班级编号
		var paike="0";//排课状态
		var rowid="";//所选行checkbox的id
		var rownum="";//所选行编号
		var roomsel=0;//选择教室模式
		var clsidarray =new Array();//存放场地编号
		var clsarray =new Array();//存放场地
		var clsinfoidarray =new Array();//存放选择的场地编号
		var clsinfoarray =new Array();//存放选择的场地名称
		//var saveType = "";//判断打开窗口的操作（new or edit）

		var xnxqcx="";//保存第一个学年学期
		var ksmccx="";//保存第一个考试名称
		var NJDM_CX = '';//查询条件
		var ZYDM_CX = '';
		var KCDM_CX = '';
		var KCMC_CX = '';
		var BJMC_CX = '';
		var XNXQ_CX = '';
		var saveType = '';
		var editType = '';
		var searType = '';
		var examinfoidarray =new Array();//存放选择的考试场次
		var delexaminfoidarray =new Array();//存放选择的考试场次
		var delweipaiarray=new Array();//未排课程
		var ksxsarray =new Array();//考试形式
		var teainfoidarray =new Array();//存放选择的任课教师编号
		var teainfoarray =new Array();//存放选择的任课教师姓名
		var teadateidarray =new Array();//存放选择的监考教师编号
		var teadatearray =new Array();//存放选择的监考教师姓名
		var examid="";//考试编号
		var examks="";//考试时间段
		var majorId="";//专业编号
		var cdlx=""; //场地类型
		var ksbh=""; //考试形式编号
		var ksxs=""; //考试形式
		var sfks="";//是否考试
		var ksrq="";//考试日期
		
		var bkarray =new Array();//补考array
		var dbkarray =new Array();//大补考array
		var flag=0;//拆分标记
		var bktype="";//补考类型
		var ksxsinfo="";
		var dbkksxsinfo="";
		var ksrqsjdarray =new Array();//存放考试日期,时间段
		var tskcrqsjdarray =new Array();//存放考试日期,时间段
		var tskc_ksccbh="";//保存考试场次编号
		var tskc_kcmc="";//保存课程名称
		var tskc_bjmc="";//保存班级名称
		var sp_index="";//index
		var sp_ksbh = "";//考试编号;
		var sp_ksccbh = "";//考试场次编号;
		var sp_rqsjd="";//考试日期和时间段
		
		var kssjdarray =new Array();//存放考试时间段
		var kskcarray =new Array();//存放考试课程 
		var ksjsarray =new Array();//存放监考教师 
		var updateArray = new Array();//课程表明细编号,班级排课状态,授课计划明细编号,授课教师编号,授课教师姓名,实际场地编号,实际场地名称,授课周次,授课周次详情
		var titleArray = new Array();//用于保存手动调课时单元格title原来信息
		var selcouArray = new Array();//保存已选课程
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
		var da_rqbh="";
		var da_zbbh="";
		var da_ksrq="";
		var da_kssj="";
		var da_jssj="";
		var deptarray =new Array();//考试形式

		$(document).ready(function(){
			initData();
			initDialog();
			initGridData(classId,xnxqVal,jxxzVal,qzqmVal,"1");//页面初始化加载数据
	 
		});
		
		/**页面初始化加载数据**/
		//页面初始化加载数据
		function initData(){ 
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_examSet',
				data : 'active=initData&page=' + pageNum + '&rows=' + pageSize+'&iUSERCODE='+iUSERCODE,
				dataType:"json",
				success : function(data) {  
					xnxq = data[0].xnxqData;//获取学年学期下拉框数据
					jxxz = data[0].jxxzData;//获取教学性质下拉框数据
					initCombobox(xnxq,jxxz);//初始化下拉框
					loadGridKCAP(); 
				}
			});
			
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_CourseSet',
				data : 'active=initData&page=' + pageNum + '&rows=' + pageSize,
				dataType:"json",
				success : function(data) { 
					initCombobox2(data[0].kclxData, data[0].zydmData, data[0].ksxsData, data[0].dbkksxsData);
					ksxsinfo= data[0].ksxsData;
					dbkksxsinfo= data[0].dbkksxsData;
				}
			});
			
		}
		
		/**加载 dialog控件**/
		function initDialog(){
			$('#createExam').dialog({   
				width: 340,//宽度设置   
				height: 152,//高度设置 
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
						doToolbar('saveEX');
					}
				}],
				//打开事件
				onOpen:function(data){},
				//读取事件
				onLoad:function(data){},
				//关闭事件
				onClose:function(data){
					emptyDialogEX();
				}
			});
		
			$('#classTimetable').dialog({   
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
					$('#saveKCAP').linkbutton('disable');
					lastIndex=-1;
					selcouArray.length = 0;
					$('#examDataTable').html("");
					$('#examDataTable').datagrid('loadData',{total:0,rows:[]});
				}
			});		
	
			
			$('#courseInfo').dialog({   
				width: 300,//宽度设置   
				height: 168,//高度设置 
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
						doToolbar('save');
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
			
		
		
			
			$('#newExam').dialog({
				width : 350,
				height: 120,
				modal : true,
				closed : true,
				onOpen : function(data) {
					$('#newExam').find('table').css('display', 'block');
				},
				onLoad : function(data) {
				},
				onClose : function(data) {
					//emptyDialog();//初始化信息
					//saveType = '';
				}
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
			
			$('#examDateDialog').dialog({   
				title: '设置考试日期',   
				width: 800,//宽度设置   
				height: 500,//高度设置 
				modal:true,
				closed: true,   
				cache: false, 
				draggable:true,//是否可移动dialog框设置
				//读取事件
				onLoad:function(data){},
				//关闭事件
				onClose:function(data){
					//initBlankKCAP(ei_xnxq, ei_zbbh, false, "");
				}
			}); 
			
			$('#editExamDateDialog').dialog({   
				title: '',   
				width: 280,//宽度设置   
				height: 153,//高度设置 
				toolbar:[{
					//保存编辑
					text:'保存',
					iconCls:'icon-save',
					handler:function(){
						//传入save值进入doToolbar方法，用于保存
						doToolbar('saveExamDate');
					}
				}],
				modal:true,
				closed: true,   
				cache: false, 
				draggable:true,//是否可移动dialog框设置
				//读取事件
				onLoad:function(data){},
				//关闭事件
				onClose:function(data){
					$('#DA_KSRQ').datebox('setValue','');
					$('#DA_KSSJ').textbox('setValue','');
					$('#DA_JSSJ').textbox('setValue','');
				}
			});
				
// 			$('#examCourseDialog').dialog({   
// 				title: '设置考试课程',   
// 				maximized:true,
// 				modal:true,
// 				closed: true,   
// 				cache: false, 
// 				draggable:true,//是否可移动dialog框设置
// 				//读取事件
// 				onLoad:function(data){},
// 				//关闭事件
// 				onClose:function(data){
// 					kskcarray.splice(0,kskcarray.length);
// 				}
// 			}); 
			
			$('#teaDeptDialog').dialog({   
				width: 300,//宽度设置   
				height: 450,//高度设置 
				modal:true,
				closed: true,   
				cache: false, 
				draggable:false,//是否可移动dialog框设置
				toolbar:[{
					//保存编辑
					text:'确定',
					iconCls:'icon-submit',
					handler:function(){
						//传入save值进入doToolbar方法，用于保存
						doToolbar("exportks");
					}
				}],
				//打开事件
				onOpen:function(data){},
				//读取事件
				onLoad:function(data){},
				//关闭事件
				onClose:function(data){
					deptarray.splice(0,deptarray.length);
				}
			});
			
			$('#examTeacherDialog').dialog({   
				title: '设置监考教师',   
				maximized:true,
				modal:true,
				closed: true,   
				cache: false, 
				draggable:true,//是否可移动dialog框设置
				//读取事件
				onLoad:function(data){},
				//关闭事件
				onClose:function(data){
					//kskcarray.splice(0,kskcarray.length);
				}
			}); 
			
			$('#allKSXSDiv').dialog({   
				title: '考试信息查询',   
				maximized:true,
				modal:true,
				closed: true,   
				cache: false, 
				draggable:true,//是否可移动dialog框设置
				//读取事件
				onLoad:function(data){},
				//关闭事件
				onClose:function(data){
					$("#GS_XNXQ_CX").combobox('setValue',xnxqcx);
					$("#GS_KSMC_CX").combobox('setValue',ksmccx);
					$("#GS_KCMC_CX").textbox('setValue','');
					$("#GS_XZBMC_CX").textbox('setValue','');
					$("#GS_ZYMC_CX").combobox('setValue','');
					$("#GS_KSXS_CX").combobox('setValue','-1');
				}
			});	

			
			$('#importExamDialog').dialog({   
				width: 250,//宽度设置   
				height: 122,//高度设置 
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
					$("#DR_KSMC").combobox('setValue','0');
				}
			});
			
			$('#teacherDialog').dialog({   
				title: '选择监考教师',   
				width: 680,//宽度设置   
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
					teainfoidarray.splice(0,teainfoidarray.length);
					teainfoarray.splice(0,teainfoarray.length);
				}
			});
			

			
			$('#editTeaDateDialog').dialog({   
				title: '设置监考日期',   
				width:400,
				height:96,
				modal:true,
				closed: true,   
				cache: false, 
				draggable:true,//是否可移动dialog框设置
				//读取事件
				onLoad:function(data){},
				//关闭事件
				onClose:function(data){
					$('#tea_JKRQ').datebox('setValue','');
				}
			});
						
			$('#scheduleDialog').dialog({   
				title: '设置考试规则',   
				maximized:true,
				modal:true,
				closed: true,   
				cache: false, 
				draggable:true,//是否可移动dialog框设置
				//读取事件
				onLoad:function(data){},
				//关闭事件
				onClose:function(data){
					$('#KS_KSLX').combobox('setValue','笔试');
				}
			});
			
			$('#editTeaDialog').dialog({   
				title: '编辑',   
				width:300,
				height:92,
				modal:true,
				closed: true,   
				cache: false, 
				draggable:true,//是否可移动dialog框设置
				//读取事件
				onLoad:function(data){},
				//关闭事件
				onClose:function(data){
					$('#GT_ZDJKCS').numberbox('setValue','');
				}
			});

			
		
		}
	
		function initCombobox(xnxq,jxxz){
			//加载下拉框数据
				$('#JXXZ').combobox({
					data:jxxz,
					valueField:'comboValue',
					textField:'comboName',
					editable:false,
					panelHeight:'100', //combobox高度
					asy:false,
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
				
				$('#XNXQ').combobox({
					data:xnxq,
					valueField:'comboValue',
					textField:'comboName',
					editable:false,
					panelHeight:'140', //combobox高度
					asy:false,
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
				
				$('#ex_xnxq').combobox({
					data:xnxq,
					valueField:'comboValue',
					textField:'comboName',
					editable:false,
					panelHeight:'100', //combobox高度
					onLoadSuccess:function(data){
						//判断data参数是否为空
						if(data != ''){ 
							//初始化combobox时赋值
							$(this).combobox('setValue', data[0].comboValue);
							mrxq=data[0].comboValue;
						}
					},
					//下拉列表值改变事件
					onChange:function(data){
	
					}
				});
				
				$('#ex_jxxz').combobox({
					data:jxxz,
					valueField:'comboValue',
					textField:'comboName',
					editable:false,
					panelHeight:'60', //combobox高度
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
				
				$('#ex_kslx').combobox({
					url:"<%=request.getContextPath()%>/Svl_examSet?active=loadKSLXCombo",
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
					onSelect:function(data){ 
						
					},
					//下拉列表值改变事件
					onChange:function(data){
						if(saveType=="editCreExam"){
							alertMsg("如修改考试类型，需要重新导入考试信息");
						}
					}
				});
				
				
				$('#DR_XNXQ').combobox({
					url:"<%=request.getContextPath()%>/Svl_examSet?active=DRXNXQCombobox",
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
						$('#DR_KSMC').combobox({
							url:"<%=request.getContextPath()%>/Svl_examSet?active=DRKSMCCombobox&DRxnxq="+data,
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
				});		

				$('#KS_XNXQ').combobox({
					url:"<%=request.getContextPath()%>/Svl_examSet?active=DRXNXQCombobox",
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
						$('#KS_KSMC').combobox({
							url:"<%=request.getContextPath()%>/Svl_examSet?active=DRKSMCCombobox"+"&DRxnxq="+data,
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
							onChange:function(data){
			
							}
						});
					}
				});	
				
				$('#GS_XNXQ_CX').combobox({
					url:"<%=request.getContextPath()%>/Svl_examSet?active=DRXNXQCombobox",
					valueField:'comboValue',
					textField:'comboName',
					editable:false,
					panelHeight:'140', //combobox高度
					onLoadSuccess:function(data){
						//判断data参数是否为空
						if(data != ''){
							//初始化combobox时赋值
							$(this).combobox('setValue', data[0].comboValue);
							xnxqcx=data[0].comboValue;
						}
					},
					//下拉列表值改变事件
					onChange:function(data){ 
						$('#GS_KSMC_CX').combobox({
							url:"<%=request.getContextPath()%>/Svl_examSet?active=DRKSMCCombobox"+"&DRxnxq="+data,
							valueField:'comboValue',
							textField:'comboName',
							editable:false,
							panelHeight:'100', //combobox高度
							onLoadSuccess:function(data){
								//判断data参数是否为空
								if(data != ''){
									//初始化combobox时赋值
									$(this).combobox('setValue', data[0].comboValue);
									ksmccx=data[0].comboValue;
								}
							},
							//下拉列表值改变事件
							onChange:function(data){
			
							}
						});
					}
				});	
				
				$('#KS_JXLH').combobox({
					url:"<%=request.getContextPath()%>/Svl_examSet?active=KSJXLHCombobox",
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
						loadGridRoom();
					}
				});
				
				//考试类型combobox
				$('#KS_KSLX').combobox({
					url:"<%=request.getContextPath()%>/Svl_examSet?active=KS_KSLXCombobox",
					valueField:'comboValue',
					textField:'comboName',
					editable:false,
					panelHeight:'80', //combobox高度
					onLoadSuccess:function(data){
						//判断data参数是否为空
						if(data != ''){
							//初始化combobox时赋值
							$(this).combobox('setValue', data[0].comboValue);
			
						}
					},
					//下拉列表值改变事件
					onChange:function(data){
						//loadGridSchedule(data);
					}
				});
				
				//教师类型combobox
				$('#GT_JSLX').combobox({
					url:"<%=request.getContextPath()%>/Svl_examSet?active=GT_JSLXCombobox",
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
	
		//显示考试安排
		function loadGridKCAP(){ 
				$('#semesterList').datagrid({
					url : '<%=request.getContextPath()%>/Svl_examSet?active=listkcap&xnxq='+$('#XNXQ').combobox('getValue')+'&jxxz='+$('#JXXZ').combobox('getValue'),
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
						
						{field:'学年学期编码',title:'学年学期编码',width:fillsize(0.15),align:'center',
							formatter:function(value,rec){
								var xnxqbm=rec.ex_xnxq+rec.ex_jxxz;
								return xnxqbm;
							}
						},
						{field:'学年学期名称',title:'学年学期名称',width:fillsize(0.2),align:'center'},
						{field:'ex_ksmc',title:'考试名称',width:fillsize(0.2),align:'center'},
						{field:'col4',title:'设置',width:fillsize(0.1),align:'center',
							formatter:function(value,rec){
								return "<input type='button' value='[设置]' onclick='examtableDetail(\"" + rec.学年学期名称 + "\",\"" + (rec.ex_xnxq+rec.ex_jxxz) + "\",\"" + rec.ex_ksmc + "\",\"" + rec.考场安排主表编号 + "\");' style=\"cursor:pointer;\">";
							}
						}
					]],
					//双击某行时触发
					onDblClickRow:function(rowIndex,rowData){},
					//读取datagrid之前加载
					onBeforeLoad:function(){},
					//单击某行时触发
					onClickRow:function(rowIndex,rowData){ 
						row=rowData;
						rowkcap=rowIndex;
						iKeyCode=rowData.考场安排主表编号;
						ksrq=rowData.ex_ksrq;
					},
					//加载成功后触发
					onLoadSuccess: function(data){
						
					}
				});
				
		}
		
		//显示详细考试信息
		function examDetail(ex_xnxq,ex_ksmc){  
			searType="openksap";
			
			$('#classTimetable').dialog({
				title:ex_ksmc
			});
			$('#classTimetable').dialog("open");
			ei_xnxq=ex_xnxq;
			
			bktype="";
			loadGridZHOUCI(ex_xnxq);
		}
		
		//显示考试课程 
		var kskcinfo="";
		var allkcdata=null;
		var choice="";//该时间所选课程
		var oldValue="";
		var oldText="";
		var selValue="";
		var selText="";
		function loadGridExamDate(classId){ 
			selcouArray.length = 0;
			isLoad = true; 
			$('#examDataTable').datagrid({
					url : '<%=request.getContextPath()%>/Svl_examSet?active=loadGridExamDate&XNXQ='+ei_xnxq+'&KCAPZBBH='+ei_zbbh+'&XZBDM='+classId,
					title:'',
					nowrap: false,
					fit:true, //自适应父节点宽度和高度
					showFooter:true,
					striped:true,
					pagination:false,
					pageSize:20,
					singleSelect:false,
					pageNumber:pageNum,
					rownumbers:true,
					fitColumns: true,
					columns:[[
						//field为读取数据的数据名，title为显示的数据名，width宽度设置，align数字在表格中显示的位置
						{field:'考试日期',title:'考试日期',width:100,align:'center'},
						{field:'kssj',title:'考试时间',width:100,align:'center',
							formatter:function(value,rec){
								return rec.开始时间+"~"+rec.结束时间;
							}
						},
						{field:'kc',title:'课程',width:100,align:'center',
 							formatter:function(value,rec){
 								return '<select class="easyui-combobox" id="sel_KC'+rec.考试日期明细编号+'" name="sel_KC" style="width:200px;" ></select>';
 							}
 						},
						{field:'jkjs',title:'监考教师人数',width:100,align:'center',editor:'text',
							formatter:function(value,rec){
								return '<input class="easyui-numberbox" id="sel_JS'+rec.考试日期明细编号+'" name="sel_JS" style="width:150px;" maxlength="1" />';
							}
						} 						
					]],
					onClickRow:function(rowIndex,rowData){	
						$('#examDataTable').datagrid("unselectRow", rowIndex);
					},
					//加载成功后触发
					onLoadSuccess: function(data){	
						kskcinfo="";
						var kscou="";
						kskcinfo=data.rows[0].MSG;//该班级所有考试课程 						
						if(kskcinfo!=""){
							kscou=kskcinfo.split(",");
							for(var k=0;k<kscou.length;k=k+4){ 
								selcouArray.push(kscou[k+1]);
							}
						}
						$.each(data.rows, function(rowIndex, rowData){ 
							choice="";//该时间所选课程
							for(var k=0;k<kscou.length;k=k+4){ 
								if(kscou[k]==rowData.考试日期明细编号){
									choice=kscou[k+1];
								}
							}

							$.ajax({
								type : "POST",
								url : '<%=request.getContextPath()%>/Svl_examSet',
								data : "active=sel_KCCombobox&KCAPZBBH="+ei_zbbh+"&XZBDM="+classId+"&selcouArray="+selcouArray+"&choice="+choice ,
								dataType:"json",
								asy:false,
								success : function(data) { 
									var kcdata = data[0].kcinfo;//获取该时间段可选课程 
									allkcdata = data[0].allkcinfo;//获取该班所有课程 
									loadsel_KCcombobox(rowData.考试日期明细编号,kcdata,"1");
								}
							});
						});
						isLoad = false;
					}
				});
				
		}
		
		function loadsel_KCcombobox(kcid,kcdata,type){
			$('#sel_KC'+kcid).combobox({
				//url:"< %=request.getContextPath()%>/Svl_examSet?active=sel_KCCombobox&KCAPZBBH="+ei_zbbh+"&XZBDM="+classId+"&selcouArray="+selcouArray+"&choice="+choice ,
				data:kcdata,
				valueField:'comboValue',
				textField:'comboName',
				editable:false,
				panelHeight:'140', //combobox高度
				asy:false,
				onLoadSuccess:function(data){ 
					//判断data参数是否为空
					if(data != ''){
						//初始化combobox时赋值
						//$(this).combobox('setValue', data[0].comboValue);
						if(type=="1"){
							if(kskcinfo!=""){
								var kscou=kskcinfo.split(",");
								for(var k=0;k<kscou.length;k=k+4){ 
									if(kcid==kscou[k]){ 
										$('#sel_KC'+kscou[k]).combobox('setValue', kscou[k+1]);
										$('#sel_JS'+kscou[k]).val(kscou[k+3]);
									}
								}
							}
						}else if(type=="2"){
							$('#sel_KC'+kcid).combobox('setValue', selValue);		
						}
					}
				},
				onShowPanel:function(){  
					//alert($('#sel_KC'+kcid).combobox('getValue')+"|"+$('#sel_KC'+kcid).combobox('getText'));
					var selkcarray = new Array();
					var obj = document.getElementsByTagName("select");
					for(var i=0;i<obj.length;i++){
						if(obj[i].id.indexOf("sel_KC")>-1){
							var kcorder=obj[i].id.substring(obj[i].id.indexOf("sel_KC")+6,obj[i].id.length);
							if($('#sel_KC'+kcorder).combobox('getValue')!=""){//不是请选择 
								selkcarray.push($('#sel_KC'+kcorder).combobox('getValue'));//编号	
								selkcarray.push($('#sel_KC'+kcorder).combobox('getText'));//课程名称	
							}
						}
					}
					
					var sflag=0;
					var redata,json;
					redata = [];
					//redata.push({"comboValue":"","comboName":"请选择"});

					selValue=$('#sel_KC'+kcid).combobox('getValue')
					selText=$('#sel_KC'+kcid).combobox('getText');

					for(var i=0;i<allkcdata.length;i++){
						sflag=0;
						for(var j=0;j<selkcarray.length;j=j+2){
							if(allkcdata[i].comboValue==selkcarray[j]){//去掉已被选过的课程  
								sflag=1;
							}
						}
						if(allkcdata[i].comboValue==selValue){//添加当前选中的
							sflag=0;
						}
						
						if(sflag==0){
							redata.push({"comboValue":allkcdata[i].comboValue,"comboName":allkcdata[i].comboName});
						}
						
					}
					
					loadsel_KCcombobox(kcid,redata,"2");
                },
				//下拉列表值改变事件
                onSelect:function(item){
					//alert(item.comboValue+"|"+item.comboName);
					$('#sel_JS'+kcid).val("");
				}
			});
		}
		
		
		function loadRQCombobox(id){
			$('#'+id).combobox({
				url:"<%=request.getContextPath()%>/Svl_examSet?active=sel_KSRQCombobox&KCAPZBBH="+ei_zbbh ,
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
		
		//查询部门
		function loadGridDept(){
				isLoad = true;
				$('#deptInfo').datagrid({
					url: '<%=request.getContextPath()%>/Svl_examSet',
		 			queryParams: {"active":"loadGridDept"},
					loadMsg : "信息加载中请稍后!",//载入时信息
					//height:433,
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
						{field:'ck',checkbox:true},
						{field:'DeptCode',title:'部门编号',width:fillsize(0.5),align:'center'},
						{field:'CName',title:'部门名称',width:fillsize(0.5),align:'center'}
					]],
					onSelect:function(rowIndex,rowData){
						if(isLoad == false){
							deptarray.push(rowData.CName);
						}
					},
					onUnselect:function(rowIndex,rowData){
						$.each(deptarray, function(key,value){
							if(value == rowData.CName){
								deptarray.splice(key, 1);
							}
						});
					},
					onSelectAll:function(rows){
						for(var i=0;i<rows.length;i++){
							deptarray.push(rows[i].CName);
						}
					},
					onUnselectAll:function(rows){
						deptarray.splice(0,deptarray.length);
					},
					onLoadSuccess: function(data){
						//$(".datagrid-header-check").html('&nbsp;');//隐藏全选
						//$(".datagrid-header-check").children("input[type='checkbox']").eq(0).attr("checked", true);		
						isLoad = false;
					},
					onLoadError:function(none){
						
					}
				});
		
		}
		
		//显示监考教师 
		function loadGridExamTeacher(){ 
			isLoad = true;	
			$('#examTeacherTable').datagrid({
					url : '<%=request.getContextPath()%>/Svl_examSet?active=loadGridExamTeacher&XNXQ='+ei_xnxq+'&KCAPZBBH='+ei_zbbh+'&teabh='+$('#GT_JSBH').textbox('getValue')+'&teaname='+encodeURI(encodeURI($('#GT_JSXM').textbox('getValue')))+'&tealx='+$('#GT_JSLX').textbox('getValue'),
					title:'',
					nowrap: false,
					fit:true, //自适应父节点宽度和高度
					showFooter:true,
					striped:true,
					pagination:true,
					pageSize:30,
					singleSelect:false,
					pageNumber:pageNum,
					rownumbers:true,
					fitColumns: true,
					columns:[[
						//field为读取数据的数据名，title为显示的数据名，width宽度设置，align数字在表格中显示的位置
						{field:'ext',checkbox:true},
						{field:'监考教师编号',title:'教师编号',width:100,align:'center'},
						{field:'监考教师姓名',title:'教师姓名',width:100,align:'center'},
						{field:'教师类型名称',title:'教师类型',width:100,align:'center'},
						{field:'最大监考次数',title:'最大监考次数',width:100,align:'center'}
// 						{field:'col4',title:'设置',width:fillsize(0.1),align:'center',
// 							formatter:function(value,rec){
// 								return "<input type='button' value='[设置]' onclick='examtableDetail(\"" + rec.学年学期名称 + "\",\"" + (rec.ex_xnxq+rec.ex_jxxz) + "\",\"" + rec.ex_ksmc + "\",\"" + rec.考场安排主表编号 + "\");' style=\"cursor:pointer;\">";
// 							}
// 						}
					]],
					onSelect:function(rowIndex,rowData){
						if(isLoad == false){
							var same=0;
							$.each(ksjsarray, function(key,value){
								if(value == rowData.监考教师编号){
									same=1;
								}
							}); 
							if(same==0){
								ksjsarray.push(rowData.监考教师编号);	
							}	
						}
					},
					onUnselect:function(rowIndex,rowData){
						$.each(ksjsarray, function(key,value){
							if(value == rowData.监考教师编号){
								ksjsarray.splice(key, 1);
							}
						});
					},
					onSelectAll:function(rows){ 
						for(var i=0;i<rows.length;i++){
							var same=0;
							$.each(ksjsarray, function(key,value){
								if(value == rows[i].监考教师编号){
									same=1;
								}
							}); 
							if(same==0){
								ksjsarray.push(rows[i].监考教师编号);
							}			
						}
					},
					onUnselectAll:function(rows){ 
						for(var i=0;i<rows.length;i++){
							$.each(ksjsarray, function(key,value){
								if(value==rows[i].监考教师编号){
									ksjsarray.splice(key, 1);
								}
							}); 
						}	
					},
					//加载成功后触发
					onLoadSuccess: function(data){ 
						if(data){							
							$.each(data.rows, function(rowIndex, rowData){				
								if((ksjsarray+',').indexOf(rowData.监考教师编号+',')>-1){
									$('#examTeacherTable').datagrid('selectRow', rowIndex);
								}
							});
							$('#examTeacherTable').datagrid('scrollTo',0); //移至第一行	
						}
						isLoad = false;
					}
				});
				
		}
		
		
		//检查当前时间是否超过允许排课时间
		function checkpaike(){ 
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_Skjh',
				data : 'active=checkpaike&GS_XNXQBM=' + $('#XNXQ').combobox('getValue')+$('#JXXZ').combobox('getValue'),
				async : false,
				dataType:"json",
				success : function(data) {
					paike=data[0].MSG;
					if(paike==0){
						$('#import').linkbutton('enable');
						$('#zongbiao').linkbutton('enable');
					}else{
						$('#import').linkbutton('disable');
						$('#zongbiao').linkbutton('disable');
					}
				}
			});
		}
		
		//读取考试日期
		function loadKSRQ(){ 
			$('#examDateTable').datagrid({
				url : '<%=request.getContextPath()%>/Svl_examSet?active=loadKSRQ&XNXQ=' + ei_xnxq+'&KCAPZBBH='+ei_zbbh,
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
					{field:'考试名称',title:'考试名称',width:fillsize(0.2),align:'center'},
					{field:'考试日期',title:'考试日期',width:fillsize(0.2),align:'center'},
					{field:'开始时间',title:'开始时间',width:fillsize(0.2),align:'center'},
					{field:'结束时间',title:'结束时间',width:fillsize(0.2),align:'center'}					
				]],
				//双击某行时触发
				onDblClickRow:function(rowIndex,rowData){},
				//读取datagrid之前加载
				onBeforeLoad:function(){},
				//单击某行时触发
				onClickRow:function(rowIndex,rowData){	
					da_rqbh=rowData.考试日期明细编号;
					da_zbbh=rowData.考场安排主表编号;
					da_ksrq=rowData.考试日期;
					da_kssj=rowData.开始时间;
					da_jssj=rowData.结束时间;
				},
				//加载成功后触发
				onLoadSuccess: function(data){
					
				}
			});
			
		}
		
		//保存考试日期
		function saveKSRQ(){ 
			var kssj=$('#DA_KSSJ').textbox('getValue').replace("：",":");
			var jssj=$('#DA_JSSJ').textbox('getValue').replace("：",":");
			if($('#DA_KSRQ').datebox('getValue')==""){
				alertMsg("考试日期不能为空");
				return;
			}
			if(kssj.indexOf(":")==-1||jssj.indexOf(":")==-1){
				alertMsg("填写的时间不规范");
				return;
			}

  			if(parseInt(kssj.substring(0,kssj.indexOf(":")),10) > parseInt(jssj.substring(0,jssj.indexOf(":")),10)){
  				alertMsg("开始时间不能大于结束时间");
  				return;
  			}else if(parseInt(kssj.substring(0,kssj.indexOf(":")),10) == parseInt(jssj.substring(0,jssj.indexOf(":")),10)){
				if(parseInt(kssj.substring(kssj.indexOf(":")+1,kssj.length),10) >= parseInt(jssj.substring(jssj.indexOf(":")+1,jssj.length),10)){
					alertMsg("开始时间需要小于结束时间");
					return;
				}
			}else{
				
			}
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_examSet',
				data : 'active=saveKSRQ&saveType='+saveType+'&XNXQ=' + ei_xnxq+'&KSRQBH='+da_rqbh+'&KCAPZBBH='+ei_zbbh+'&ex_ksrq='+$('#DA_KSRQ').datebox('getValue')+'&kssj='+kssj+'&jssj='+jssj,
				async : false,
				dataType:"json",
				success : function(data) {
					$('#editExamDateDialog').dialog("close");
					showMsg(data[0].MSG);
					loadKSRQ();
				}
			});
		}
		
		//保存考试日期
		function delKSRQ(){ 
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_examSet',
				data : 'active=delKSRQ&KSRQBH='+da_rqbh,
				async : false,
				dataType:"json",
				success : function(data) {
					showMsg(data[0].MSG);
					loadKSRQ();
				}
			});
		}
		
		//保存考试课程 
		function saveKSKC(){ 
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_examSet',
				data : 'active=saveKSKC&XNXQ=' + ei_xnxq+'&KCAPZBBH='+ei_zbbh+'&ei_zydm='+$('#kc_zydm').combobox('getValue')+'&kskcarray='+kskcarray,
				async : false,
				dataType:"json",
				success : function(data) {
					//$('#examCourseDialog').dialog("close");
					showMsg(data[0].MSG);
					loadClassKCAP(ei_xnxq, ei_zbbh, classId);
				}
			});
		}
		
		function treegrid(listData){
			$('#classTree').tree({
				checkbox: false,
				url:'<%=request.getContextPath()%>/Svl_examSet?active=queryTree&level=0&iUSERCODE='+iUSERCODE+'&GG_XNXQBM='+(xnxqVal+jxxzVal),
				onClick:function(node){
					//判断点击的是不是班级
			    	if($('#classTree').tree('getParent', node.target) != null){
			    		if(lastIndex!=node.id){
							classId=node.id;
							className=node.text;
							lastIndex=node.id;
							initGridData(classId,xnxqVal,jxxzVal,qzqmVal,"2");//点击后刷新right页面取值结果	
						}
						parentId=node.id;
			    	}else{
			    		classId="";
			    		parentId="";
			    		majorId=node.id;
			    		if(classId!=""||majorId=="9999999"||majorId=="9999990"){
			    			initGridData(majorId,xnxqVal,jxxzVal,qzqmVal,"2");//页面初始化加载数据
			    		}
			    	}					
				},
// 				onBeforeLoad:function(row,param){     //分层显示treegrid
// 				},
				onBeforeExpand:function(node,param){
			  		$('#classTree').tree('options').url='<%=request.getContextPath()%>/Svl_Skjh?active=queryTree&level=1&parentId='+node.id+'&iUSERCODE='+iUSERCODE+'&GS_XNXQBM='+(xnxqVal+jxxzVal);
				},
				onLoadSuccess:function(data){
					$('#classTree').show(); 
					xnxq_cx = $('#XNXQ').combobox('getValue');
					jxxz_cx = $('#JXXZ').combobox('getValue');
					
			    }
			});
		}
		
		//查询后台数据
		function loadData(xnxq_cx,jxxz_cx){
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_examSet',
				data : 'active=query&page=' + pageNum + '&rows=' + pageSize + '&iUSERCODE='+iUSERCODE+
					'&JXXZ=' + jxxz_cx + '&XNXQ=' + xnxq_cx + '&termid='+(xnxq_cx+jxxz_cx),
				dataType:"json",
				success : function(data) {
					weeks=data[0].MSG;
					initData();//页面初始化加载数据
					//treegrid();
				}
			});
		}
		
		
		//工具按钮
		function doToolbar(id){
			//判断获取参数为new，执行新建操作
			if(id == 'newEX'){
				//打开dialog
				$('#createExam').dialog({   
					title:'新建考试信息'
				});
				saveType = 'createExam';
				$('#createExam').dialog('open');
				
			}
	
			//判断获取参数为edit，执行编辑操作
			if(id == 'editEX'){
				if(iKeyCode == ''){
					alertMsg('请选择一行数据');
					return;
				}
				$('#createExam').dialog({   
					title:'编辑考试信息'
				});
				saveType = 'editCreExam';
				//打开dialog
				if(row!=undefined && row!=''){
					$('#form1').form('load', row);
				}
				
				$('#createExam').dialog('open');
			}
			
			if(id == 'delEX'){
				if(iKeyCode == ''){
					alertMsg('请选择一行数据');
					return;
				}
				ConfirmMsg('是否确定要删除当前选择的考试', 'delCreExam();', '');
			}
			
			//判断获取参数为save，执行保存操作
			if(id == 'saveEX'){
				var ksmc = $('#ex_ksmc').textbox('getValue');		
	
				if(ksmc== ''){
					alertMsg('请填写课程名称');
					return;
				}				
				
				$('#ksapbh').val(iKeyCode);	
				$('#active').val(saveType);
				
				$("#form1").submit();
			}
			//查询
			if(id == 'que'){
				xnxq_cx = $('#XNXQ').combobox('getValue');
				jxxz_cx = $('#JXXZ').combobox('getValue');
				loadData(xnxq_cx,jxxz_cx);
// 				if(!classId==""){
// 					$('#List').treegrid("unselectRow", classId);
// 				}
				xnxqVal=$('#XNXQ').combobox('getValue');
				jxxzVal=$('#JXXZ').combobox('getValue');
				qzqmVal=$('#QZQM').combobox('getValue');
				if(classId!=""){
					initGridData(classId,xnxqVal,jxxzVal,qzqmVal,"1");//点击后刷新right页面取值结果
				}else{
					initGridData(majorId,xnxqVal,jxxzVal,qzqmVal,"1");//点击后刷新right页面取值结果
				}
			}
			
			//编辑考试日期时间 
			if(id == 'newExamDate'){
				saveType="newExamDate";
				$('#editExamDateDialog').dialog({   
					title: '新建考试时间'
				});
				$('#editExamDateDialog').dialog("open");
			}
			if(id == 'editExamDate'){
				saveType="editExamDate";
				$('#editExamDateDialog').dialog({   
					title: '编辑考试时间'
				});
				$('#editExamDateDialog').dialog("open");
				$('#DA_KSRQ').datebox('setValue',da_ksrq);
				$('#DA_KSSJ').textbox('setValue',da_kssj);
				$('#DA_JSSJ').textbox('setValue',da_jssj);
			}
			if(id == 'delExamDate'){
				ConfirmMsg("是否确定删除考试时间？", "delKSRQ();","");
			}

			if(id == 'importExam'){//导入考试信息
				$('#DR_XNXQ').html($('#XNXQ').combobox('getText'));
				
				$('#importExamDialog').dialog("open");
				//checkksgl();
				//checkdabukao();//导入大补考
			}
			if(id == 'importTeacher'){//设置监考教师
// 				if($('#QZQM').combobox('getValue')=="4"){
// 					alertMsg("考试周期不能为全部");
// 					return;
// 				}
// 				var url="importType=teacher&xnxq="+($('#XNXQ').combobox('getValue')+$('#JXXZ').combobox('getValue'))+"&qzqm="+$('#QZQM').combobox('getValue')+"&xnxqtext="+$('#XNXQ').combobox('getText')+"&qzqmtext="+$('#QZQM').combobox('getText'); 
// 	   			url=encodeURI(url); //用了2次encodeURI 
// 				showDialog("< %=request.getContextPath()%>/form/examManage/importExamInfo.jsp?"+url,"导入监考教师");
				$('#teacherDialog').dialog("open");
				loadGridTea();
			}

			//考试信息保存
			if(id == "savesfks"){
				//alert(classnum+"|"+xnxqVal+"|"+jxxzVal+"|"+qzqm+"|");

				$.ajax({
					type : "POST",
					url : '<%=request.getContextPath()%>/Svl_examSet',
					data : 'active=savesfks&GG_SKJHMXBH=' + iKeyCodecx + '&GG_SFKS='+$('#GS_SFKS').combobox('getValue')+ '&GG_CDLX='+$('#GS_CDLX').combobox('getValue') + '&GG_KSZQ='+$('#GS_KSZQ').combobox('getValue') + '&GG_KSXS='+$('#GS_KSXS').combobox('getValue') + '&GG_XNXQBM='+ei_xnxq+'&QZQM='+ei_jzzs,
					dataType:"json",
					success : function(data) { 
						showMsg(data[0].MSG);
						$('#win').dialog("close");
						//initGridData(classnum,xnxqVal,jxxzVal,qzqmVal,"1");
						if(editType=="edit"){
							loadGridAllKSXS();
						}else if(editType=="editqm"){
							loadGridZHOUCI(ei_xnxq,ei_jzzs);				
						}	
					}
				});
			}
			//大补考保存
			if(id == "savedbkksxs"){ 
				//alert(classnum+"|"+xnxqVal+"|"+jxxzVal+"|"+qzqm+"|");dbk_xnxqmc=rowData.学年学期名称;
				if(bktype=="bk"){
					$.ajax({
						type : "POST",
						url : '<%=request.getContextPath()%>/Svl_examSet',
						data : 'active=savedbkksxs&dbkarray='+bkarray+'&dbk_xnxq=' + bk_xnxqbm + '&dbk_ksxs='+$('#DA_KSXS').combobox('getValue') ,
						dataType:"json",
						success : function(data) { 
							showMsg(data[0].MSG);
							bkarray.splice(0,bkarray.length);
							loadGridBUKAO(ei_xnxq);
							$('#dbkksxs').dialog("close");			
						}
					});
				}else{//bktype=="dbk"		
					$.ajax({
						type : "POST",
						url : '<%=request.getContextPath()%>/Svl_examSet',
						data : 'active=savedbkksxs&dbkarray='+dbkarray+'&dbk_xnxq=' + dbk_xnxqbm + '&dbk_ksxs='+$('#DA_KSXS').combobox('getValue') ,
						dataType:"json",
						success : function(data) {
							showMsg(data[0].MSG);
							dbkarray.splice(0,dbkarray.length);
							loadGridDABUKAO(ei_xnxq);
							$('#dbkksxs').dialog("close");
						}
					});
				}
			}
			if(id == "exportks"){//导入选择的部门教师 
				if(deptarray.length==0){
					alertMsg("请选择部门");
					return;
				}
				$.ajax({
					type : "POST",
					url : '<%=request.getContextPath()%>/Svl_examSet',
					data : 'active=importDeptTea&XNXQ='+ei_xnxq+'&KCAPZBBH='+ei_zbbh+'&deptarray='+deptarray,
					dataType:"json",
					success : function(data) {
						$('#teaDeptDialog').dialog("close");
						alertMsg(data[0].MSG);
					}
				});		
			}
			if(id == "resit"){//补考
				$.ajax({
					type : "POST",
					url : '<%=request.getContextPath()%>/Svl_examSet',
					data : 'active=resitInfo&XNXQ=' + xnxqVal + '&JXXZ=' + jxxzVal + '&QZQM=' + qzqmVal,
					dataType:"json",
					success : function(data) {
						alertMsg(data[0].MSG);
					}
				});
			}
			if(id == "exportResit"){//导出补考名册
				$('#exportTimetable').dialog('open');
				$("#exportTimetableiframe").attr("src","<%=request.getContextPath()%>/form/timetableQuery/exportResit.jsp?xnxq=" + xnxqVal + "&jxxz=" + jxxzVal + "&qzqm=" + qzqmVal );
			}
			
			if(id == "submit5"){
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
					
				}else{
				
				}
				
				$('#GS_CDYQ').textbox('setValue',html);

				$('#room').dialog("close");
			}
			//查询
			if(id == 'query'){
				BJMC_CX = $('#BJMC_CX').textbox('getValue'); 
				KCMC_CX = $('#KCMC_CX').textbox('getValue');
				ZYDM_CX = $('#ZYDM_CX').combobox('getValue');
				if(searType=="allksxs"){
					XNXQ_CX = $('#GS_XNXQ_CX').combobox('getValue'); 
					QZQM_CX = $('#GS_KSMC_CX').combobox('getValue'); 
				}else{//searType=="openksap"
					XNXQ_CX = ei_xnxq;
					QZQM_CX = ei_jzzs;
				}
				loadGridaddcc(BJMC_CX,KCMC_CX,ZYDM_CX,XNXQ_CX,QZQM_CX);
			}
			//执行保存操作
			if(id == 'saveselect'){ 
				var skid="";	
				var delexamid="";
				for (var i = 0;i < examinfoidarray.length;i++) {
					skid+=examinfoidarray[i]+",";					
				}
				for (var i = 0;i < delexaminfoidarray.length;i++) {
					delexamid+=delexaminfoidarray[i]+",";					
				}
				
				skid=skid.substring(0,skid.length-1);
				$.ajax({
					type : "POST",
					url : '<%=request.getContextPath()%>/Svl_examSet',
					data : 'active=saveselect&skid='+skid+'&examid='+examid+'&delexamid='+delexamid,
					dataType:"json",
					success : function(data) {
						showMsg(data[0].MSG);
						if(data[0].MSG=="保存成功"){
							emptyic_ksccapedit();	
							$('#ic_ksccapedit').dialog("close");
						}
					}
				});
			}
			//编辑考试信息
			if(id == 'edit'){
				if(iKeyCodecx==""){
					alertMsg("请选择一行数据");
					return;
				}else{
					editType="edit";
					$('#GS_CDLX').combobox('setValue',cdlx);
					$('#GS_CDYQ').textbox('setValue',cdyq);
					$('#GS_KSXS').combobox('setValue',ksbh);
					$('#GS_KSZQ').combobox('setValue',kszq);
					$('#GS_SFKS').combobox('setValue',sfks);
					$('#win').dialog({   
						title: '编辑是否考试'
					});
					$('#win').dialog("open");
					
				}
			}
			//编辑各时间段考试
			if(id == 'editqm'){
				if(iKeyCodecx==""){
					alertMsg("请选择一行数据");
					return;
				}else{
					editType="editqm";
					$('#GS_CDLX').combobox('setValue',cdlx);
					$('#GS_CDYQ').textbox('setValue',cdyq);
					$('#GS_KSXS').combobox('setValue',ksbh);
					$('#GS_KSZQ').combobox('setValue',kszq);
					$('#GS_SFKS').combobox('setValue',sfks);
					$('#win').dialog({   
						title: '编辑是否考试'
					});
					$('#win').dialog("open");
				}
			}
			//编辑补考信息
			if(id == 'editBUKAO'){
				if(bkarray.length==0){
					alertMsg("请选择一行数据");
					return;
				}else{ 
					
					//课程名称
					var region1=bk_kcmc.replace(new RegExp('@','gm'),'$');
					var kcmcinfo=region1.split("$");
					var showkcmc="";
					for(var i=0;i<kcmcinfo.length;i++){
						if(showkcmc.indexOf(kcmcinfo[i])>-1){//名称在显示中以存在
									
						}else{
							showkcmc+=kcmcinfo[i]+"、";
						}
					}
					showkcmc=showkcmc.substring(0,showkcmc.length-1);
					//行政班简称
					var region2=bk_xzbjc.replace(new RegExp('@','gm'),'$');
					var xzbjcinfo=region2.split("$");
					var showxzbjc="";
					for(var i=0;i<xzbjcinfo.length;i++){
						if(showxzbjc.indexOf(xzbjcinfo[i])>-1){//名称在显示中以存在
									
						}else{
							showxzbjc+=xzbjcinfo[i]+"、";
						}
					}
					showxzbjc=showxzbjc.substring(0,showxzbjc.length-1);
							
					if(bkarray.length>1){//多选
					
					}else{
						$('#DA_KCMC').html(showkcmc);
						$('#DA_BJJC').html(showxzbjc);
					}
					$('#DA_KSXS').combobox('setValue',bk_ksxs);
					
					$('#dbkksxs').dialog({   
						title: '编辑补考'
					});
					$('#dbkksxs').dialog("open");
		
				}
			}
			//编辑大补考信息
			if(id == 'editDABUKAO'){
				if(dbkarray.length==0){
					alertMsg("请选择一行数据");
					return;
				}else{
					
					//课程名称
					var region1=dbk_kcmc.replace(new RegExp('@','gm'),'$');
					var kcmcinfo=region1.split("$");
					var showkcmc="";
					for(var i=0;i<kcmcinfo.length;i++){
						if(showkcmc.indexOf(kcmcinfo[i])>-1){//名称在显示中以存在
									
						}else{
							showkcmc+=kcmcinfo[i]+"、";
						}
					}
					showkcmc=showkcmc.substring(0,showkcmc.length-1);
					//行政班简称
					var region2=dbk_xzbjc.replace(new RegExp('@','gm'),'$');
					var xzbjcinfo=region2.split("$");
					var showxzbjc="";
					for(var i=0;i<xzbjcinfo.length;i++){
						if(showxzbjc.indexOf(xzbjcinfo[i])>-1){//名称在显示中以存在
									
						}else{
							showxzbjc+=xzbjcinfo[i]+"、";
						}
					}
					showxzbjc=showxzbjc.substring(0,showxzbjc.length-1);
							
					if(dbkarray.length>1){//多选
					
					}else{
						$('#DA_KCMC').html(showkcmc);
						$('#DA_BJJC').html(showxzbjc);
					}
					$('#DA_KSXS').combobox('setValue',dbk_ksxs);
					
					$('#dbkksxs').dialog({   
						title: '编辑大补考'
					});
					$('#dbkksxs').dialog("open");
					
				}
			}
			//合并大补考
			if(id == 'addDBKinfo'){
				if(bktype=="bk"){
					if(bkarray.length<2){
						alertMsg("请至少选择2条数据合并");
						return;
					}else{ 
						$.ajax({
							type : "POST",
							url : '<%=request.getContextPath()%>/Svl_examSet',
							data : 'active=addDBKinfo&GG_XNXQBM='+(ei_xnxq)+'&dbkarray='+bkarray+'&bktype='+bktype,
							dataType:"json",
							success : function(data) {
								alertMsg(data[0].MSG);
								bkarray.splice(0,bkarray.length);
								loadGridBUKAO(ei_xnxq);	
							}
						});
					}
				}else{//bktype=="dbk"
					if(dbkarray.length<2){
						alertMsg("请至少选择2条数据合并");
						return;
					}else{
						$.ajax({
							type : "POST",
							url : '<%=request.getContextPath()%>/Svl_examSet',
							data : 'active=addDBKinfo&GG_XNXQBM='+(ei_xnxq)+'&dbkarray='+(dbkarray)+'&bktype='+bktype,
							dataType:"json",
							success : function(data) {
								alertMsg(data[0].MSG);
								dbkarray.splice(0,dbkarray.length);
								loadGridDABUKAO(ei_xnxq);	
							}
						});
					}
				}
			}
			if(id == "newks"){//新建考试
				$('#newExam').dialog({   
					title: '新建考试'
				});
				$('#newExam').dialog("open");
				KCMCCombobox();
			}
			if(id == "saveNewExam"){//保存新建考试
				if($('#GS_BJMC').combobox('getValue')==""){
					alertMsg("请选择班级");
					return;
				}
				if($('#GS_KCMC').combobox('getValue')==""){
					alertMsg("请选择课程");
					return;
				}
				$.ajax({
					type : "POST",
					url : '<%=request.getContextPath()%>/Svl_examSet',
					data : 'active=saveNewExam&GG_XZBDM='+$('#GS_BJMC').combobox('getValue')+'&XNXQ='+ei_xnxq+'&QZQM='+ei_jzzs+'&KCDM='+$('#GS_KCMC').combobox('getValue')+'&KCMC='+$('#GS_KCMC').combobox('getText'),
					dataType:"json",
					success : function(data) {
						showMsg(data[0].MSG);
						$('#newExam').dialog("close");
						//initGridData(classnum,xnxqVal,jxxzVal,qzqmVal,"1");
						
					}
				});	
			}
			//添加
			if(id == 'addselect'){
				if(searType=="allksxs"){
					XNXQ_CX = $('#GS_XNXQ_CX').combobox('getValue'); 
					QZQM_CX = $('#GS_KSMC_CX').combobox('getValue'); 
				}else{//searType=="openksap"
					XNXQ_CX = ei_xnxq;
					QZQM_CX = ei_jzzs;
				} 
				
				BJMC_CX = $('#BJMC_CX').textbox('getValue'); 
				KCMC_CX = $('#KCMC_CX').textbox('getValue');
				ZYDM_CX = $('#ZYDM_CX').combobox('getValue');
				$('#ic_ksccapedit').show();
				$('#ic_ksccapedit').dialog("open");
				loadGridaddcc(BJMC_CX,KCMC_CX,ZYDM_CX,XNXQ_CX,QZQM_CX);
			}
			//删除
			if(id == 'delselect'){
				var skjhid="";
				var rObj=document.getElementsByName("ksjh3");
				for (var j=0;j < rObj.length;j++) {
					var checkbox = document.getElementById(rObj[j].id);
					if(checkbox.checked){
						skjhid+="'"+rObj[j].id.substring(4,rObj[j].id.length)+"',";
					}
				}
				skjhid=skjhid.substring(0, skjhid.length-1);
				$.ajax({
					type : "POST",
					url : '<%=request.getContextPath()%>/Svl_examSet',
					data : 'active=delselect&skjhid='+skjhid,
					dataType:"json",
					success : function(data) {
						showMsg(data[0].MSG);
						loadGrideditcc(XNXQ_CX,QZQM_CX);
					}
				});		
			}

			//未选考试形式
			/* if(id == "notksxs"){
				loadGridKSXS();
				$('#notKSXSDiv').dialog("open");
			} */
			if(id == "queAll"){//查询所有考试
				loadGridAllKSXS();
			}
			if(id == "queKSLX"){//按选择查询考试
				loadGridZHOUCI(ei_xnxq,ei_jzzs);
			}
			if(id == "queBUKAO"){
				loadGridBUKAO(ei_xnxq);
			}
			if(id == "queDABUKAO"){
				loadGridDABUKAO(ei_xnxq);
			}
			//导入考试信息
			if(id == "importExamSubmit"){ 
				ConfirmMsg("导入将覆盖所选时间段已有的考试信息，是否继续？", "checkksgl();","");
			}
			//所有考试形式
			if(id == "allksxs"){
				searType="allksxs";
				KSZQCombobox();
				loadGridAllKSXS();
				$('#allKSXSDiv').dialog("open");
			}
			//补考
			if(id == "bukao"){
				bktype="bk";
				loadGridBUKAO(ei_xnxq);
				$('#bukaoDiv').dialog("open");
			}
			//大补考
			if(id == "dabukao"){
				bktype="dbk";
				loadGridDABUKAO(ei_xnxq);
				$('#dabukaoDiv').dialog("open");
			}
			//考试规则
			if(id == "examSchedule"){
				$('#scheduleDialog').dialog("open");
				loadGridSchedule("笔试");
			}
			
			//设置考试日期
			if(id == "setExamDate"){ 
				$('#examDateDialog').dialog("open");		
				loadKSRQ();
			}
			//设置考试课程 
			if(id == "setExamCourse"){ 
				$('#examCourseDialog').dialog("open");		
				loadGridExamCourse();
			}
			//导入监考教师 
			if(id == "importExamTeacher"){ 
				$('#teaDeptDialog').dialog("open");	
				loadGridDept();
			}
			//设置监考教师 
			if(id == "setExamTeacher"){ 
				$('#examTeacherDialog').dialog("open");	
				ksjsarray.splice(0,ksjsarray.length);
				loadGridExamTeacher();
			}
			//查询监考教师 
			if(id == "searchTea"){ 
				ksjsarray.splice(0,ksjsarray.length);
				loadGridExamTeacher();
			}
			//编辑监考教师 
			if(id == "editTeacher"){ 
				if(ksjsarray.length==0){
					alertMsg("请至少选择一位教师");
					return;
				}else{
					$('#editTeaDialog').dialog("open");
				}
			}
			//保存最大监考次数 
			if(id == "saveTeaNum"){
				if($('#GT_ZDJKCS').numberbox('getValue')==""){
					alertMsg("请输入最大监考次数");
					return;
				}
				if($('#GT_ZDJKCS').numberbox('getValue')<0){
					alertMsg("最大监考次数不能为负数");
					return;
				}
				$.ajax({
					type: "POST",
					url: '<%=request.getContextPath()%>/Svl_examSet',
					data: "active=saveTeaNum&KCAPZBBH="+ei_zbbh+"&ksjsarray=" + ksjsarray+"&jkcs=" + $('#GT_ZDJKCS').numberbox('getValue'),
					dataType: 'json',
					success: function(datas){
						showMsg(datas[0].MSG);
						$('#editTeaDialog').dialog("close");
						ksjsarray.splice(0,ksjsarray.length);
						loadGridExamTeacher();
					}
				});
			}
			
			//查询
			if(id == "queHBKC"){
				loadGridselKSKC(sp_ksccbh);
			}
			//删除未排课程
			if(id == "deleteWeipai"){
				if(delweipaiarray.length==0){
					alertMsg("请选择要删除的课程");
					return;
				}
				var weipaiarray="";
				for(var i=0;i<delweipaiarray.length;i++){
					weipaiarray=delweipaiarray[i]+",";
				}
				
				$.ajax({
					type: "POST",
					url: '<%=request.getContextPath()%>/Svl_examSet',
					data: "active=deleteWeipai&weipaiarray=" + weipaiarray,
					dataType: 'json',
					success: function(datas){
						showMsg(datas[0].MSG);
						loadGridCOUR();
					}
				});
			}
			//保存监考教师
			if(id == "saveTea"){
// 				if(teainfoidarray.length==0){
// 					alertMsg("请选择要保存的监考教师");
// 					return;
// 				}

				$.ajax({
					type: "POST",
					url: '<%=request.getContextPath()%>/Svl_examSet',
					data: "active=saveTeacher&XNXQ="+(ei_xnxq)+"&JZZS=" + ei_jzzs +"&teainfoidarray=" + teainfoidarray+"&teainfoarray="+teainfoarray,
					dataType: 'json',
					success: function(datas){ 
						showMsg(datas[0].MSG);
						loadGridTea();
					}
				});
			}
		
			//编辑监考日期
			if(id == 'editTea'){
				if(teadateidarray.length == 0){
					alertMsg('请至少选择一位教师');
					return;
				}			
				$('#editTeaDateDialog').dialog('open');
			}
			//保存规则设置
			if(id == "submitTeaDate"){
				$.ajax({
					type : "POST",
					url : '<%=request.getContextPath()%>/Svl_examSet',
					data : 'active=saveTeaDate&XNXQ='+ei_xnxq+'&kszbbh='+ei_jzzs+'&teadateidarray='+teadateidarray+'&jkrq='+$('#tea_JKRQ').combobox('getValue'),
					dataType:"json",
					asyn:false,
					success : function(data) { 
						showMsg(data[0].MSG);
						teadateidarray.splice(0,teadateidarray.length);
						loadGridTeaDate();
						$('#editTeaDateDialog').dialog("close");
					}
				});	
			}
			
			//保存当前班级课程表
			if(id == 'saveKCAP'){ 
				var kcaparray = new Array();
				var obj = document.getElementsByTagName("select");
				for(var i=0;i<obj.length;i++){
					if(obj[i].id.indexOf("sel_KC")>-1){
						var kcorder=obj[i].id.substring(obj[i].id.indexOf("sel_KC")+6,obj[i].id.length);
						//alert($('#' + obj[i].id).attr('title')	+"|"+$('#kcdm_'+kcorder).val());	
						kcaparray.push(ei_zbbh);//主表编号 
						kcaparray.push(classId);//行政班代码 	
						kcaparray.push(kcorder);//考试时间 
						kcaparray.push($('#sel_JS'+kcorder).val());//监考教师人数	
						kcaparray.push($('#sel_KC'+kcorder).combobox('getText'));//课程名称		
					}
				}
				
// 				var wpkcInfo = new Array();
// 				//解析未排课程json
// 				for(var i=0; i<wpkc.length; i++){
// 					wpkcInfo.push(ei_zbbh);//主表编号 
// 					wpkcInfo.push(classId);//行政班代码 
// 					wpkcInfo.push(wpkc[i].课程代码);
// 					wpkcInfo.push(wpkc[i].课程名称);
// 					wpkcInfo.push(wpkc[i].节数);
// 				}
				
				$('#divPageMask').show();
				
				$.ajax({
					type: "POST",
					url: '<%=request.getContextPath()%>/Svl_examSet',
					data: "active=updateKCB&kcaparray="+kcaparray,
					dataType: 'json',
					success: function(datas){ 
						$('#divPageMask').hide();
						kcaparray.length = 0;
						showMsg(datas[0].MSG);
						loadGridExamDate(classId);		
					}
				});
				
				//$('#active').val('updateKCB');
				//$('#kcbInfo').val(updateArray);
					
				//var wpkcInfo = new Array();
				//解析未排课程json
				//for(var i=0; i<wpkc.length; i++){
				//	wpkcInfo.push(wpkc[i].课程代码);
				//	wpkcInfo.push(wpkc[i].节数);
				//}
					
				//$('#wpkcInfo').val(wpkcInfo);
			}
			
			
			//保存考试教室
			if(id == "saveCls"){
// 				if(clsinfoidarray.length==0){
// 					alertMsg("请选择要保存的教室");
// 					return;
// 				}

				$.ajax({
					type: "POST",
					url: '<%=request.getContextPath()%>/Svl_examSet',
					data: "active=saveClassroom&XNXQ="+(ei_xnxq)+"&JZZS="+(ei_jzzs)+"&jxlh="+$('#KS_JXLH').combobox('getValue')+"&clsinfoidarray=" + clsinfoidarray+"&clsinfoarray="+clsinfoarray,
					dataType: 'json',
					success: function(datas){ 
						showMsg(datas[0].MSG);
						loadGridRoom();
					}
				});
			}
		
			//导出未选考试形式
			if(id == "exportWXksxs"){
				$('#exportTimetable').dialog('open');
				$("#exportTimetableiframe").attr("src","<%=request.getContextPath()%>/form/timetableQuery/exportWXksxs.jsp?exportType=exportWXksxs&xnxq="+$('#GS_XNXQ_CX').combobox('getValue'));
			}
			//导出答疑表
			if(id == "exportdbkDAYI"){
				$('#maskFontDBK').html('大补考答疑表导出中...');
    			$('#divPageMaskDBK').show();
				ExportExcelDBKDY();
			}
			//导出补考安排表
			if(id == "exportBUKAO"){
				$('#maskFontBK').html('补考安排表导出中...');
    			$('#divPageMaskBK').show();
				ExportExcelBKAP();
			}
			//导出大补考安排表
			if(id == "exportDABUKAO"){
				$('#maskFontDBK').html('大补考安排表导出中...');
    			$('#divPageMaskDBK').show();
				ExportExcelDBKAP();
			}
			
			//保存考试日期
			if(id == "saveExamDate"){			
				saveKSRQ();
			}
			//保存考试课程
			if(id == "saveExamCourse"){			
				
				saveKSKC();
			}
			
		}
		
		//删除
		function delCreExam(){
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_examSet',
				data : 'active=delCreExam&ksapbh=' + iKeyCode,
				dataType:"json",
				success : function(data){
					if(data[0].MSG == '删除成功'){
						showMsg(data[0].MSG);
						loadGridKCAP();
					}else{
						alertMsg(data[0].MSG);
					}
				}
			});
		}
		
		/**表单提交**/
		$('#form1').form({
			//定位到servlet位置的url
			url:'<%=request.getContextPath()%>/Svl_examSet',
			//当点击事件后触发的事件
			onSubmit: function(data){
				return $(this).form('validate');//验证
			}, 
			//当点击事件并成功提交后触发的事件
			success:function(data){
				var json = eval("("+data+")");
				
				if(json[0].MSG == '新建成功'||json[0].MSG == '保存成功'){
					showMsg(json[0].MSG);
					$("#active").val('');

					emptyDialogEX();//清空并关闭dialog
					loadGridKCAP();
					$('#createExam').dialog('close');
				}else{
					alertMsg(json[0].MSG);
				}
			}
		});
		
		/**清空Dialog中表单元素数据**/
		function emptyDialogEX(){
			iKeyCode="";
			saveType = '';
			$('#ex_xnxq').combobox('setValue', mrxq);
			$('#ex_ksmc').textbox('setValue', '');
		
			
			
			$('#ex_ksrq').val("");
			$('#semesterList').datagrid('unselectRow',rowkcap);
		}

// 		function loadGridKSXS(){
// 				isLoad = true;
// 				$('#nksxstable').datagrid({
// 					url: '< %=request.getContextPath()%>/Svl_examSet',
// 		 			queryParams: {"active":"showNotKSXS","GG_XNXQBM":xnxqVal+jxxzVal,"QZQM":qzqmVal},
// 					loadMsg : "信息加载中请稍后!",//载入时信息
// 					height:433,
// 					rownumbers: true,
// 					animate:true,
// 					striped : true,//隔行变色
// 					pageSize : pageSize,//每页记录数
// 					singleSelect : true,//单选模式
// 					pageNumber : pageNum,//当前页码
// 					pagination:false,
// 					fit:false,
// 					fitColumns: true,//设置边距
// 					columns:[[
// 						{field:'是否考试',title:'是否考试',width:50,align:'center',
// 							formatter:function(value,rec,row){
								//var sfksbox='<input type="checkbox" id="sfks'+rec.授课计划明细编号+'" name="ksjh" onclick="editExamInfo(this.id,'+row+');" />';
// 								var sfksbox="";
// 								if(rec.是否考试=='1'){
// 									sfksbox="是";
// 								}else{
// 									sfksbox="否";
// 								}
// 								return sfksbox;
// 							}
// 						},
// 						{field:'课程代码',title:'课程代码',width:60,align:'center'},
// 						{field:'课程名称',title:'课程名称',width:90,align:'center'},
// 						{field:'专业名称',title:'专业名称',width:120,align:'center'},
// 						{field:'考试形式',title:'考试形式',width:50,align:'center'},
// 						{field:'行政班名称',title:'行政班名称',width:150,align:'center'},
// 						{field:'上课周期',title:'上课周期',width:60,align:'center'},
						
// 						{field:'考试场次安排',title:'考试场次',width:60,align:'center',
// 							formatter:function(value,rec){
// 								var ksccap="";
// 								if(rec.是否考试=='1'){
// 									ksccap='<input id="ksap'+rec.授课计划明细编号+'" type="button" value="[设置]" style="cursor:pointer;display:block;" onclick="openksap(\''+this.id+'\',\''+rec.期中期末+'\');" />';
// 								}else{
// 									ksccap='';							
// 								}
// 								return ksccap;
// 							}
// 						}							
// 					]],
// 					onClickRow:function(rowIndex, rowData){
						
// 						iKeyCode = rowData.授课计划明细编号;
// 						cdlx=rowData.场地类型;
// 						cdyq=rowData.场地要求;
// 						ksbh=rowData.编号;
// 					},
// 					onLoadSuccess: function(data){
						
// 						isLoad = false;
// 					},
// 					onLoadError:function(none){
						
// 					}
// 				});
				
// 		}
		
		//考试形式查询
		function loadGridAllKSXS(){
				isLoad = true;
				$('#allksxstable').datagrid({
					url: '<%=request.getContextPath()%>/Svl_examSet',
		 			queryParams: {"active":"showAllKSXS","GG_XNXQBM":$("#GS_XNXQ_CX").combobox('getValue'),"QZQM":$("#GS_KSMC_CX").combobox('getValue'),"GS_KCMC":$("#GS_KCMC_CX").val(),
		 							"GS_ZYMC":$("#GS_ZYMC_CX").combobox('getValue'),"GS_KSXS":$("#GS_KSXS_CX").combobox('getValue'),"GS_XZBMC":$("#GS_XZBMC_CX").val()},
					loadMsg : "信息加载中请稍后!",//载入时信息
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
						{field:'是否考试',title:'是否考试',width:50,align:'center',
							formatter:function(value,rec,row){
								//var sfksbox='<input type="checkbox" id="sfks'+rec.授课计划明细编号+'" name="ksjh" onclick="editExamInfo(this.id,'+row+');" />';
								var sfksbox="";
								if(rec.是否考试=='1'){
									sfksbox="是";
								}else{
									sfksbox="否";
								}
								return sfksbox;
							}
						},
						{field:'课程代码',title:'课程代码',width:60,align:'center'},
						{field:'课程名称',title:'课程名称',width:90,align:'center'},
						{field:'专业名称',title:'专业名称',width:120,align:'center'},
						{field:'考试形式',title:'考试形式',width:70,align:'center'},
						{field:'行政班名称',title:'行政班名称',width:150,align:'center'},
						{field:'上课周期',title:'上课周期',width:60,align:'center'},
						{field:'考试名称',title:'考试名称',width:60,align:'center'},
						{field:'考试场次安排',title:'考试场次',width:60,align:'center',
							formatter:function(value,rec){
								var ksccap="";
								if(rec.是否考试=='1'){
									ksccap='<input id="ksap'+rec.授课计划明细编号+'" type="button" value="[设置]" style="cursor:pointer;display:block;" onclick="openksap(\'ksap'+rec.授课计划明细编号+'\',\''+rec.期中期末+'\');" />';
								}else{
									ksccap='';							
								}
								return ksccap;
							}
						}							
					]],
					onClickRow:function(rowIndex, rowData){
						//$('#list').datagrid('unselectRow',rowIndex);
						iKeyCodecx = rowData.授课计划明细编号;
						cdlx=rowData.场地类型;
						cdyq=rowData.场地要求;
						ksbh=rowData.编号;
						kszq=rowData.期中期末;
						sfks=rowData.是否考试; 
					},
					onLoadSuccess: function(data){
						//$(".datagrid-header-check").html('&nbsp;');//隐藏全选
						isLoad = false;
						
					},
					onLoadError:function(none){
						
					}
				});
				
		}
		
		//考试信息查询
		function loadGridZHOUCI(ex_xnxq){
				isLoad = true;
				$('#listzhouci').datagrid({
					url: '<%=request.getContextPath()%>/Svl_examSet',
		 			queryParams: {"active":"showAllKSXS","GG_XNXQBM":ex_xnxq,"GS_KCMC":$("#EI_KCMC_CX").val(),
		 							"GS_ZYMC":$("#EI_ZYMC_CX").combobox('getValue'),"GS_KSXS":$("#EI_KSXS_CX").combobox('getValue'),"GS_XZBMC":$("#EI_XZBMC_CX").val()},
					loadMsg : "信息加载中请稍后!",//载入时信息
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
						{field:'是否考试',title:'是否考试',width:50,align:'center',
							formatter:function(value,rec,row){
								//var sfksbox='<input type="checkbox" id="sfks'+rec.授课计划明细编号+'" name="ksjh" onclick="editExamInfo(this.id,'+row+');" />';
								var sfksbox="";
								if(rec.是否考试=='1'){
									sfksbox="是";
								}else{
									sfksbox="否";
								}
								return sfksbox;
							}
						},
						{field:'课程代码',title:'课程代码',width:60,align:'center'},
						{field:'课程名称',title:'课程名称',width:90,align:'center'},
						{field:'专业名称',title:'专业名称',width:120,align:'center'},
						{field:'考试形式',title:'考试形式',width:70,align:'center'},
						{field:'行政班名称',title:'行政班名称',width:150,align:'center'},
						{field:'上课周期',title:'上课周期',width:60,align:'center'},
						{field:'考试名称',title:'考试名称',width:60,align:'center'},
						{field:'考试场次安排',title:'考试场次',width:60,align:'center',
							formatter:function(value,rec){
								var ksccap="";
								if(rec.是否考试=='1'){
									ksccap='<input id="ksap'+rec.授课计划明细编号+'" type="button" value="[设置]" style="cursor:pointer;display:block;" onclick="openksap(\'ksap'+rec.授课计划明细编号+'\',\''+rec.期中期末+'\');" />';
								}else{
									ksccap='';							
								}
								return ksccap;
							}
						}							
					]],
					onClickRow:function(rowIndex, rowData){
						//$('#list').datagrid('unselectRow',rowIndex);
						iKeyCodecx = rowData.授课计划明细编号;
						cdlx=rowData.场地类型;
						cdyq=rowData.场地要求;
						ksbh=rowData.编号;
						kszq=rowData.期中期末;
						sfks=rowData.是否考试;
					},
					onLoadSuccess: function(data){
						//$(".datagrid-header-check").html('&nbsp;');//隐藏全选
						isLoad = false;
					},
					onLoadError:function(none){
						
					}
				});
		}
		
		
		//补考查询
		function loadGridBUKAO(ex_xnxq){
				isLoad = true;
				$('#bukaotable').datagrid({
					url: '<%=request.getContextPath()%>/Svl_examSet',
		 			queryParams: {"active":"showBUKAO","GG_XNXQBM":ex_xnxq,"QZQM":qzqmVal,"GS_KCMC":$("#BK_KCMC_CX").val(),
		 							"GS_KSXS":$("#BK_KSXS_CX").combobox('getText'),"GS_XZBMC":$("#BK_XZBMC_CX").val()},
					loadMsg : "信息加载中请稍后!",//载入时信息
					width:'100%',
					nowrap: false,
					fit:true, //自适应父节点宽度和高度
					showFooter:true,
					striped:true,
					pagination:true,
					pageSize:pageSize,
					singleSelect:false,
					pageNumber:pageNum,
					rownumbers:true,
					fitColumns: true,
					columns:[[
						{field:'ck',checkbox:true},
						{field:'学年学期',title:'学年学期',width:60,align:'center',
							formatter:function(value,rec){ 
								var xqname="";
								xqname=rec.学年学期.substring(0,4)+"学年第"+rec.学年学期.substring(4,5)+"学期";
								return xqname;
							}
						},
						{field:'课程名称',title:'课程名称',width:160,align:'center',
							formatter:function(value,rec){ 
								var region=rec.课程名称.replace(new RegExp('@','gm'),'$');
								var kcmcinfo=region.split("$");
								var showkcmc="";
								for(var i=0;i<kcmcinfo.length;i++){
									if(showkcmc.indexOf(kcmcinfo[i])>-1){//名称在显示中以存在
									
									}else{
										showkcmc+=kcmcinfo[i]+"、";
									}
								}
								showkcmc=showkcmc.substring(0,showkcmc.length-1);
											
								return showkcmc;
							}
						},
						{field:'人数',title:'人数',width:60,align:'center',
							formatter:function(value,rec){
								var region=rec.人数+"";
								region=region.replace(new RegExp('@','gm'),'$');
								var kcmcinfo="";
								var showkcmc=0;
								if(region.indexOf("$")>-1){
									kcmcinfo=region.split("$");
									for(var i=0;i<kcmcinfo.length;i++){
										showkcmc+=parseInt(kcmcinfo[i]);
									}
								}else{
									showkcmc=region;
								}
								return showkcmc;
							}
						},
						{field:'考试形式',title:'考试形式',width:0,align:'center'},
						{field:'系名称',title:'系名称',width:60,align:'center',
							formatter:function(value,rec){ 
								var region=rec.系名称.replace(new RegExp('@','gm'),'$');
								var kcmcinfo=region.split("$");
								var showkcmc="";
								for(var i=0;i<kcmcinfo.length;i++){
									if(showkcmc.indexOf(kcmcinfo[i])>-1){//名称在显示中以存在
									
									}else{
										showkcmc+=kcmcinfo[i]+"、";
									}
								}
								showkcmc=showkcmc.substring(0,showkcmc.length-1);
								return showkcmc;
							}
						},
						{field:'行政班简称',title:'行政班简称',width:100,align:'center',
							formatter:function(value,rec){
								var region=rec.行政班简称.replace(new RegExp('@','gm'),'$');
								var kcmcinfo=region.split("$");
								var showkcmc="";
								for(var i=0;i<kcmcinfo.length;i++){
									if(showkcmc.indexOf(kcmcinfo[i])>-1){//名称在显示中以存在
									
									}else{
										showkcmc+=kcmcinfo[i]+"、";
									}
								}
								showkcmc=showkcmc.substring(0,showkcmc.length-1);
								return showkcmc;
							}
						},
						
						{field:'设置',title:'设置',width:60,align:'center',
							formatter:function(value,rec){
								var shezhi="";
								var hbinfo=rec.授课计划明细编号;
								var region=rec.人数+"";
								if(region.indexOf("@")>0){
									shezhi='<input id="" type="button" value="[拆分]" style="cursor:pointer;display:block;" onclick="flag=1;split(\''+hbinfo+'\')" />';
								}else{
									shezhi='';							
								}
								return shezhi;
							}
						}							
					]],
					
					onSelect:function(rowIndex,rowData){
						bk_skjhmx=rowData.授课计划明细编号;
						bk_xnxqbm=rowData.学年学期;
						//k_xnxqmc=rowData.学年学期名称;
						bk_kcmc=rowData.课程名称;
						bk_rs=rowData.人数;
						bk_ksxs=rowData.考试形式编号;
						bk_xmc=rowData.系名称;
						bk_xzbjc=rowData.行政班简称;
						bk_xzbmc=rowData.行政班名称;
						if(isLoad == false&&flag==0){
							bkarray.push(rowData.授课计划明细编号);
						}
						
					},
					onUnselect:function(rowIndex,rowData){
						$.each(bkarray, function(key,value){
							if(value == rowData.授课计划明细编号){
								bkarray.splice(key, 1);
							}
						});
					},
					onLoadSuccess: function(data){
						$(".datagrid-header-check").html('&nbsp;');//隐藏全选
						isLoad = false;
						
						//页数赋值
						pageNum=$(this).datagrid('options').pageNumber;
						pageSize=$(this).datagrid('options').pageSize;
					},
					onLoadError:function(none){
						
					}
				});
				
				/* $("#bukaotable").datagrid("getPager").pagination({ 
					onSelectPage:function (pageNo, pageSize_1) { 
						pageNum = pageNo;
						pageSize = pageSize_1;
						loadGridBUKAO(ei_xnxq);
					} 
				}); */
		}
		
		//大补考查询
		function loadGridDABUKAO(ex_xnxq){
				isLoad = true;
				$('#dabukaotable').datagrid({
					url: '<%=request.getContextPath()%>/Svl_examSet',
		 			queryParams: {"active":"showDABUKAO","GG_XNXQBM":ex_xnxq,"QZQM":qzqmVal,"GS_KCMC":$("#DA_KCMC_CX").val(),
		 							"GS_KSXS":$("#DA_KSXS_CX").combobox('getText'),"GS_XZBMC":$("#DA_XZBMC_CX").val()},
					loadMsg : "信息加载中请稍后!",//载入时信息
					width:'100%',
					nowrap: false,
					fit:true, //自适应父节点宽度和高度
					showFooter:true,
					striped:true,
					pagination:true,
					pageSize:pageSize,
					singleSelect:false,
					pageNumber:pageNum,
					rownumbers:true,
					fitColumns: true,
					columns:[[
						{field:'ck',checkbox:true},
						{field:'学年学期',title:'学年学期',width:60,align:'center',
							formatter:function(value,rec){ 
								var xqname="";
								xqname=rec.学年学期.substring(0,4)+"学年第"+rec.学年学期.substring(4,5)+"学期";
								return xqname;
							}
						},
						{field:'课程名称',title:'课程名称',width:160,align:'center',
							formatter:function(value,rec){ 
								var region=rec.课程名称.replace(new RegExp('@','gm'),'$');
								var kcmcinfo=region.split("$");
								var showkcmc="";
								for(var i=0;i<kcmcinfo.length;i++){
									if(showkcmc.indexOf(kcmcinfo[i])>-1){//名称在显示中以存在
										var sq=0;
										var newkcmc=showkcmc.split("、");
										for(var j=0;j<newkcmc.length;j++){
											if(newkcmc[j]==kcmcinfo[i]){
												sq=1;
											}
										}
										if(sq==0){
											showkcmc+=kcmcinfo[i]+"、";
										}
									}else{
										showkcmc+=kcmcinfo[i]+"、";
									}
								}
								showkcmc=showkcmc.substring(0,showkcmc.length-1);
											
								return showkcmc;
							}
						},
						{field:'人数',title:'人数',width:60,align:'center',
							formatter:function(value,rec){
								var region=rec.人数+"";
								region=region.replace(new RegExp('@','gm'),'$');
								var kcmcinfo="";
								var showkcmc=0;
								if(region.indexOf("$")>-1){
									kcmcinfo=region.split("$");
									for(var i=0;i<kcmcinfo.length;i++){
										showkcmc+=parseInt(kcmcinfo[i]);
									}
								}else{
									showkcmc=region;
								}
								return showkcmc;
							}
						},
						{field:'考试形式',title:'考试形式',width:0,align:'center'},
						{field:'系名称',title:'系名称',width:60,align:'center',
							formatter:function(value,rec){ 
								var region=rec.系名称.replace(new RegExp('@','gm'),'$');
								var kcmcinfo=region.split("$");
								var showkcmc="";
								for(var i=0;i<kcmcinfo.length;i++){
									if(showkcmc.indexOf(kcmcinfo[i])>-1){//名称在显示中以存在
									
									}else{
										showkcmc+=kcmcinfo[i]+"、";
									}
								}
								showkcmc=showkcmc.substring(0,showkcmc.length-1);
								return showkcmc;
							}
						},
						{field:'行政班简称',title:'行政班简称',width:100,align:'center',
							formatter:function(value,rec){
								var region=rec.行政班简称.replace(new RegExp('@','gm'),'$');
								var kcmcinfo=region.split("$");
								var showkcmc="";
								for(var i=0;i<kcmcinfo.length;i++){
									if(showkcmc.indexOf(kcmcinfo[i])>-1){//名称在显示中以存在
									
									}else{
										showkcmc+=kcmcinfo[i]+"、";
									}
								}
								showkcmc=showkcmc.substring(0,showkcmc.length-1);
								return showkcmc;
							}
						},
						
						{field:'设置',title:'设置',width:60,align:'center',
							formatter:function(value,rec){
								var shezhi="";
								var hbinfo=rec.授课计划明细编号;
								var region=rec.人数+"";
								if(region.indexOf("@")>0){
									shezhi='<input id="" type="button" value="[拆分]" style="cursor:pointer;display:block;" onclick="flag=1;split(\''+hbinfo+'\')" />';
								}else{
									shezhi='';							
								}
								return shezhi;
							}
						}							
					]],
					
					onSelect:function(rowIndex,rowData){
						dbk_skjhmx=rowData.授课计划明细编号;
						dbk_xnxqbm=rowData.学年学期;
						//dbk_xnxqmc=rowData.学年学期名称;
						dbk_kcmc=rowData.课程名称;
						dbk_rs=rowData.人数;
						dbk_ksxs=rowData.考试形式编号;
						dbk_xmc=rowData.系名称;
						dbk_xzbjc=rowData.行政班简称;
						dbk_xzbmc=rowData.行政班名称;
						if(isLoad == false&&flag==0){
							dbkarray.push(rowData.授课计划明细编号);
						}
						
					},
					onUnselect:function(rowIndex,rowData){
						$.each(dbkarray, function(key,value){
							if(value == rowData.授课计划明细编号){
								dbkarray.splice(key, 1);
							}
						});
					},
					onLoadSuccess: function(data){
						$(".datagrid-header-check").html('&nbsp;');//隐藏全选
						isLoad = false;
						
						//页数赋值
						pageNum=$(this).datagrid('options').pageNumber;
						pageSize=$(this).datagrid('options').pageSize;
					},
					onLoadError:function(none){
						
					}
				});
				
// 				$("#dabukaotable").datagrid("getPager").pagination({ 
// 					onSelectPage:function (pageNo, pageSize_1) { 
// 						pageNum = pageNo;
// 						pageSize = pageSize_1;
// 						loadGridBUKAO(ei_xnxq);
// 					} 
// 				});
		}
		
		//拆分信息
		function split(dbkinfo){
			ConfirmMsg("拆分这条信息，是否继续？", "splitDBKinfo(\""+dbkinfo+"\");","");
		}
		function splitDBKinfo(dbkinfo){
			if(bktype=="bk"){
				$.ajax({
					type : "POST",
					url : '<%=request.getContextPath()%>/Svl_examSet',
					data : 'active=splitDBKinfo&dbkinfo='+dbkinfo+'&GG_XNXQBM='+(ei_xnxq)+'&bktype='+bktype,
					dataType:"json",
					success : function(data) {
						alertMsg(data[0].MSG);
						flag=0;
						loadGridBUKAO(ei_xnxq);
					}
				});
			}else{//bktype="dbk"
				
				$.ajax({
					type : "POST",
					url : '<%=request.getContextPath()%>/Svl_examSet',
					data : 'active=splitDBKinfo&dbkinfo='+dbkinfo+'&GG_XNXQBM='+(ei_xnxq)+'&bktype='+bktype,
					dataType:"json",
					success : function(data) {
						alertMsg(data[0].MSG);
						flag=0;
						loadGridDABUKAO(ei_xnxq);
					}
				});
			}
		}
		
		//显示特殊课程
		function loadGridSpecialCourse(){
				isLoad = true;
				$('#specialCourseTable').datagrid({
					url: '<%=request.getContextPath()%>/Svl_examSet',
		 			queryParams: {"active":"loadGridSpecialCourse","GG_XNXQBM":ei_xnxq,"QZQM":ei_jzzs},
					loadMsg : "信息加载中请稍后!",//载入时信息
					width:'100%',
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
						{field:'考试名称',title:'考试名称',width:80,align:'center'},
						{field:'考试场次编号',title:'考试场次编号',width:100,align:'center'},
						{field:'课程名称',title:'课程名称',width:100,align:'center',
							formatter:function(value,rec){ 
								var region=rec.课程名称;
								var kcmcinfo=region.split(",");
								var showkcmc="";
								for(var i=0;i<kcmcinfo.length;i++){ 
									if(showkcmc.indexOf(kcmcinfo[i])>-1){//名称在显示中以存在
									
									}else{
										showkcmc+=kcmcinfo[i]+"、";
									}
								}
								showkcmc=showkcmc.substring(0,showkcmc.length-1);
											
								return showkcmc;
							}
						},
						{field:'班级名称',title:'班级名称',width:200,align:'center'},
						{field:'日期时间段',title:'日期时间段',width:300,align:'center'}									
					]],
					onClickRow:function(rowIndex, rowData){
						//$('#list').datagrid('unselectRow',rowIndex);
						sp_index=rowIndex;
						sp_ksbh = rowData.考试编号;
						sp_ksccbh = rowData.考试场次编号;
						sp_rqsjd = rowData.日期时间段;
					},
					onLoadSuccess: function(data){
						//$(".datagrid-header-check").html('&nbsp;');//隐藏全选
						isLoad = false;
					},
					onLoadError:function(none){
						
					}
				});
		}
		
		
		//设置监考教师
		function loadGridTea(){
			isLoad = true;
			$('#teatable').datagrid({
				url: '<%=request.getContextPath()%>/Svl_examSet',
	 			queryParams: {"active":"openTeacher","teaId":$('#ic_teaId').val(),"teaName":$('#ic_teaName').val(),"XNXQ":(ei_xnxq),"JZZS":ei_jzzs},
				loadMsg : "信息加载中请稍后!",//载入时信息
				width:666,
				height:434,
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
					{field:'jkt',checkbox:true},
					{field:'工号',title:'工号',width:100,align:'center',
						formatter:function(value,rec,row){
							var teaid=rec.工号;
							if(teaid.indexOf("#")>-1){
								teaid=teaid.substring(0,teaid.indexOf("#"));
							}
							return teaid;
						}
					},
					{field:'姓名',title:'姓名',width:100,align:'center'}					
				]],
				onSelect:function(rowIndex,rowData){
					if(isLoad == false){
						teainfoidarray.push(rowData.工号);
						teainfoarray.push(rowData.姓名);
					}
				},
				onUnselect:function(rowIndex,rowData){
					$.each(teainfoidarray, function(key,value){
						if(value == rowData.工号){
							teainfoidarray.splice(key, 1);
							teainfoarray.splice(key, 1);
						}
					});
				},
				onSelectAll:function(rows){
					teainfoidarray.splice(0,teainfoidarray.length);
					teainfoarray.splice(0,teainfoarray.length);
					for(var i=0;i<rows.length;i++){
						teainfoidarray.push(rows[i].工号);
						teainfoarray.push(rows[i].姓名);
					}
				},
				onUnselectAll:function(rows){
					teainfoidarray.splice(0,teainfoidarray.length);
					teainfoarray.splice(0,teainfoidarray.length);
				},
				onLoadSuccess: function(data){ 
					//$(".datagrid-header-check").html('&nbsp;');//隐藏全选
					//勾选已选授课教师
					if(data){
						$.each(data.rows, function(rowIndex, rowData){
							if(rowData.sel=="1"){//选中的教师
								$('#teatable').datagrid('selectRow', rowIndex);
							}
						});
						$('#teatable').datagrid('scrollTo',0); //移至第一行
					}
		 			isLoad = false;
				},
				onLoadError:function(none){
					
				}
			});
		}
		
		//设置监考教师
		function loadGridTeaDate(){
			isLoad = true;
			$('#teatable2').datagrid({
				url: '<%=request.getContextPath()%>/Svl_examSet',
	 			queryParams: {"active":"openTeacherDate","teaId":$('#ic_teaId').val(),"teaName":$('#ic_teaName').val(),"XNXQ":(ei_xnxq),"JZZS":ei_jzzs},
				loadMsg : "信息加载中请稍后!",//载入时信息
				width:666,
				height:434,
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
					{field:'jkt',checkbox:true},
					{field:'工号',title:'工号',width:100,align:'center'},
					{field:'姓名',title:'姓名',width:100,align:'center'},	
					{field:'监考日期',title:'监考日期',width:100,align:'center'}		
				]],
				onSelect:function(rowIndex,rowData){
					if(isLoad == false){
						teadateidarray.push(rowData.工号);
						teadatearray.push(rowData.姓名);
					}
				},
				onUnselect:function(rowIndex,rowData){
					$.each(teadateidarray, function(key,value){
						if(value == rowData.工号){
							teadateidarray.splice(key, 1);
							teadatearray.splice(key, 1);
						}
					});
				},
				onSelectAll:function(rows){
					teadateidarray.splice(0,teadateidarray.length);
					teadatearray.splice(0,teadatearray.length);
					for(var i=0;i<rows.length;i++){
						teadateidarray.push(rows[i].工号);
						teadatearray.push(rows[i].姓名);
					}
				},
				onUnselectAll:function(rows){
					teadateidarray.splice(0,teadateidarray.length);
					teadatearray.splice(0,teadatearray.length);
				},
				onLoadSuccess: function(data){ 
					//$(".datagrid-header-check").html('&nbsp;');//隐藏全选
					//勾选已选授课教师
					if(data){
						$.each(data.rows, function(rowIndex, rowData){
							if(rowData.sel=="1"){//选中的教师
								$('#teatable2').datagrid('selectRow', rowIndex);
							}
						});
						$('#teatable2').datagrid('scrollTo',0); //移至第一行
					}
		 			isLoad = false;
				},
				onLoadError:function(none){
					
				}
			});
		}
		
		//设置考试教室
		function loadGridRoom(){
			isLoad = true;
			$('#ksjstable').datagrid({
				url: '<%=request.getContextPath()%>/Svl_examSet',
	 			queryParams: {"active":"openRoom","roomId":$('#KS_JXLH').combobox('getValue'),"XNXQ":(ei_xnxq),"JZZS":ei_jzzs},
				loadMsg : "信息加载中请稍后!",//载入时信息
				width:666,
				height:405,
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
					{field:'rmt',checkbox:true},
					{field:'教室编号',title:'教室编号',width:100,align:'center'},
					{field:'教室名称',title:'教室名称',width:100,align:'center'}					
				]],
				onSelect:function(rowIndex,rowData){
					if(isLoad == false){
						clsinfoidarray.push(rowData.教室编号);
						clsinfoarray.push(rowData.教室名称);
					}
				},
				onUnselect:function(rowIndex,rowData){
					$.each(clsinfoidarray, function(key,value){
						if(value == rowData.教室编号){
							clsinfoidarray.splice(key, 1);
							clsinfoarray.splice(key, 1);
						}
					});
				},
				onSelectAll:function(rows){
					clsinfoidarray.splice(0,clsinfoidarray.length);
					clsinfoarray.splice(0,clsinfoarray.length);
					for(var i=0;i<rows.length;i++){
						clsinfoidarray.push(rows[i].教室编号);
						clsinfoarray.push(rows[i].教室名称);
					}
				},
				onUnselectAll:function(rows){
					clsinfoidarray.splice(0,clsinfoidarray.length);
					clsinfoarray.splice(0,clsinfoarray.length);
				},
				onLoadSuccess: function(data){ 
					//$(".datagrid-header-check").html('&nbsp;');//隐藏全选
					//勾选已选授课教师
					if(data){
						$.each(data.rows, function(rowIndex, rowData){
							if(rowData.sel=="1"){//选中的教师
								$('#ksjstable').datagrid('selectRow', rowIndex);
							}
						});
						$('#ksjstable').datagrid('scrollTo',0); //移至第一行
					}
		 			isLoad = false;
				},
				onLoadError:function(none){
					
				}
			});
		}
		
		//--------------------------------------------------------------------------
		
		//显示对话操作框
		function showDialog(src, title) {
			$(function(){
				$('#iframe').attr("src",src);
				$('#showDialog').dialog({
					title:title
	//				title:'对话框',//对话框的标题
	//				collapsible:true,//定义是否显示可折叠按钮
	//				minimizable:true,//定义是否显示最小化按钮
	//				maximizable:true//定义是否显示最大化按钮
	//				resizable:true,	//定义对话框是否可编辑大小
				});
			});
		}
		
		//检查考试管理表，与授课计划同步
		function checkksgl(){
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_examSet',
				data : 'active=checkksgl&XNXQ='+$("#DR_XNXQ").combobox('getValue')+'&QZQM='+$("#DR_KSMC").combobox('getValue'),
				dataType:"json",
				success : function(data) {
					$('#importExamDialog').dialog("close");
					showMsg(data[0].MSG);
				}
			});
		}
		//导入大补考信息
		function checkdabukao(){
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_examSet',
				data : 'active=checkdabukao&XNXQ='+(xnxqVal+jxxzVal),
				dataType:"json",
				success : function(data) {
					alertMsg(data[0].MSG);
				}
			});
		}
		
		//页面初始化加载数据
		function initGridData(classId,xnxqVal,jxxzVal,qzqmVal,n){
			classnum=classId;
			checkpaike();
	
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_examSet',
				data : 'active=initGridData&page=' + pageNum + '&rows=' + pageSize + '&XZBDM=' + classId + '&XNXQ=' + xnxqVal + '&JXXZ=' + jxxzVal + '&QZQM=' + qzqmVal,
				dataType:"json",
				success : function(data) { 
					//xkmc = data[0].xkmcData;//获取学科名称下拉框数据
					//rkjs = data[0].rkjsData;//获取任课教师下拉框数据					
					loadGrid(data[0].listData);//加载特殊规则列表
				}
			});
		}
		
		/**页面初始化加载数据**/
		function initData2(){
			
		}
		
		//检查当前时间是否超过允许排课时间
		function checkpaike(){ 
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_Skjh',
				data : 'active=checkpaike&GS_XNXQBM=' + (xnxqVal+jxxzVal),
				async : false,
				dataType:"json",
				success : function(data) {
					paike=data[0].MSG;
				}
			});
		}
		
		var editIndex = undefined;
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
					{field:'是否考试',title:'是否排考试',width:50,align:'center',
						formatter:function(value,rec,row){
							//var sfksbox='<input type="checkbox" id="sfks'+rec.授课计划明细编号+'" name="ksjh" onclick="editExamInfo(this.id,'+row+');" />';
							var sfksbox="";
							if(rec.是否考试=='1'){
								sfksbox="是";
							}else{
								sfksbox="否";
							}
							return sfksbox;
						}
					},
					{field:'课程名称',title:'科目名称',width:140},
					{field:'考试形式',title:'考试形式',width:60},
					{field:'试卷类型',title:'试卷类型',width:60},
					{field:'名称',title:'场地类型',width:80},
					{field:'上课周期',title:'上课周期',width:60},
					{field:'考试名称',title:'考试名称',width:60},
					{field:'考试场次安排',title:'考试场次',width:60,align:'center',
						formatter:function(value,rec){
							var ksccap="";
							if(rec.是否考试=='1'){
								ksccap='<input id="ksap'+rec.授课计划明细编号+'" type="button" value="[设置]" style="cursor:pointer;display:block;" onclick="openksap(\'ksap'+rec.授课计划明细编号+'\',\''+rec.期中期末+'\');" />';
							}else{
								ksccap='';							
							}
							return ksccap;
						}
					}
// 					{field:'参加考试学生',title:'参考学生',width:80,align:'center',
// 						formatter:function(value,rec){
// 							var ksccap="";
// 							if(rec.是否考试=='1'){
// 								ksccap='<input id="ckxs'+rec.授课计划明细编号+'" type="button" value="[参考学生]" style="cursor:pointer;display:block;" onclick="openckxs(this.id);" />';
// 							}else{
// 								ksccap='';							
// 							}
// 							return ksccap;
// 						}
// 					}
// 					{field:'GS_SKZCXQ',title:'授课周次详情',width:50,
// 						formatter:function(value,rec){
// 							var skzcxq2=rec.GS_SKZCXQ;
// 							skzcxq2=skzcxq2.replace(new RegExp('odd','gm'),'单周');
// 							skzcxq2=skzcxq2.replace(new RegExp('even','gm'),'双周');
// 							return skzcxq2;
// 						}
// 					}
				]],
				onClickRow:function(rowIndex, rowData){
					//$('#list').datagrid('unselectRow',rowIndex);
					
					iKeyCode = rowData.授课计划明细编号;
					cdlx=rowData.场地类型;
					cdyq=rowData.场地要求;
					ksbh=rowData.编号;
					kszq=rowData.期中期末;
					
					//qzqm=rowData.期中期末;
// 					kcmc = rowData.课程名称;
// 					jsxm = rowData.授课教师姓名;
// 					skzcxq = rowData.GS_SKZCXQ;
// 					lj = rowData.GS_LJ;
// 					lc = rowData.GS_LC;
// 					cdyq=rowData.GS_CDYQ;
// 					cdmc=rowData.GS_CDMC;
// 					classId = window.parent.document.frames["left"].classId;
// 					xnxqVal = window.parent.document.frames["left"].xnxqVal;
// 					jxxzVal = window.parent.document.frames["left"].jxxzVal;
// 					weeks = window.parent.document.frames["left"].weeks;
	
				},
				onClickCell:function(index, field){
					//alert(index+"|"+field);
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
							
					
					if(data.total == 0){
						$('#save').linkbutton('disable');
					}else{
						$('#save').linkbutton('enable');
					}
					setTimeout("$('#divPageMask2').hide()",1000);
					setTimeout("$('#divPageMask3').hide()",1000);
				},
				onLoadError:function(none){
					
				}
				//onClickCell:onClickCell
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
		
		function editExamInfo(id,row){
			rowid=id;
			rownum=row;
			var checkbox = document.getElementById(id);
			var currentBtn = document.getElementById("ksap"+id.substring(4,id.length));
			var editkszq="";
			if(checkbox.checked){//勾选
				currentBtn.style.display = "block"; //style中的display属性
				//$('#list').datagrid('beginEdit', rownum);
				//editkszq = $('#list').datagrid('getEditor', { index: rownum, field: '考试周期' }); 
			}else{
				currentBtn.style.display = "none";
				//editkszq = $('#list').datagrid('getEditor', { index: rownum, field: '考试周期' }); 
				//$(editkszq.target).val("");
				//$('#list').datagrid('endEdit', rownum); 
			}	
			
		    
		}
		
		function openksap(id,ks){ 
			if($('#GS_KSMC_CX').combobox('getValue')=="0"&&searType=="allksxs"){
				alertMsg("要设置考试场次，考试名称不能为全部，请先选择考试名称");
				return;
			} 
			examid=id;
			examks=ks;
			if(searType=="allksxs"){
				XNXQ_CX = $('#GS_XNXQ_CX').combobox('getValue'); 
				QZQM_CX = $('#GS_KSMC_CX').combobox('getValue'); 
			}else{//searType=="openksap"
				XNXQ_CX = ei_xnxq;
				QZQM_CX = ei_jzzs;
			}
			$('#ic_ksccap').show();
			$('#ic_ksccap').dialog("open");	
			examinfoidarray.splice(0, examinfoidarray.length);
			loadGrideditcc(XNXQ_CX,QZQM_CX);
		}
		
		function openckxs(id){ 
			examid=id;
			$('#ic_cjksxs').show();
			$('#ic_cjksxs').dialog("open");	
			loadGrid5();
		}

		
		function emptyic_ksccapedit(){ 
			examinfoidarray.splice(0,examinfoidarray.length);//清空examinfoidarray
			delexaminfoidarray.splice(0,delexaminfoidarray.length);//清空delexaminfoidarray	
			$('#BJMC_CX').textbox('setValue','');
			$('#KCMC_CX').textbox('setValue','');
			$('#ZYDM_CX').combobox('setValue','');
			var rObj = document.getElementsByName("ksjh2");
			for (var i = 0;i < rObj.length;i ++) {
				$('#'+rObj[i].id).removeAttr("checked");				
			}
			loadGrideditcc(XNXQ_CX,QZQM_CX);
		}
		
		//打开room编辑窗口
		function openroom(){

			document.getElementById("win").focus(); 
			
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
			
			loadGridCls();
            schoolCombobox();
			houseCombobox();
			classtypeCombobox();	
			
			$('#room').show();
			
			$('#room').dialog("open");
		}
		
		function loadGridCls(){
			$('#clstable').datagrid({
				url: '<%=request.getContextPath()%>/Svl_Skjh',
	 			queryParams: {"active":"openRoom","selschool":$('#school').combobox('getValue'),"selhouse":$('#house').combobox('getValue'),"seltype":$('#clstype').combobox('getValue')},
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
		
		/**选中当前的课程表课程单元格
			@tdID 单元格id
			@area 选中的单元格所在的区域
		**/
		function selectKcbCourse(tdID, area){		
			//判断选中的是否班级 
			if(classflag==false){
				return;
			}
			//判断在课表区域选择的单元格是否可以手动调整
			if($('#'+tdID).html()=='&nbsp;'){
				return;
			}
			
			var e = window.event || arguments.callee.caller.arguments[0];
			//判断是不是左键点击
			if(e.button!=2 && e.button!=3){				
				$('#' + tdID).css('background-color', '#FFE48D');
				selectArea = area;
				curSelectId = tdID;
				
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
		
		/**课程表及未排课程单元格鼠标移入事件
			@id 当前td的id
		**/
		function tdMouseOver(tdID){
			//判断选中的是否班级 
			if(classflag==false){
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
			//判断选中的是否班级 
			if(classflag==false){
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
				if(curTargetId=='' || curTargetId==curSelectId){
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
				targetArea = curTargetId.substring(0, curTargetId.indexOf('_'));
				
				var selContent = '';
				var tagContent = '';
				var tempContent = '';
				var selTitle = '';
				var tagTitle = '';
				var pjType = '';
				var tempTagContent = '';
				var curSelectNum=curSelectId;
				var curTargetNum=curTargetId;
				var selOrder = curSelectNum.substring(curSelectNum.indexOf('_')+1);
				var tagOrder = curTargetNum.substring(curTargetNum.indexOf('_')+1);
				selTitle = $('#' + curSelectNum).attr('title');	
				selName = $('#kcdm_' + selOrder).val(); 
				tagTitle = $('#' + curTargetNum).attr('title');	
				if(tagTitle==undefined){
					tagTitle=' ';
				}
				tagName = $('#kcdm_' + tagOrder).val(); 
				if(tagName==undefined){
					tagName='';
				}
				var changeType = selectArea+'to'+targetArea;
				
				//判断是否为课程表区域内两节课互换
				if(changeType == 'kcbtokcb'){
					//清空课程表单元格内容
					$('#' + curSelectId).removeAttr('title');
					$('#' + curSelectId).html('&nbsp;');
					
					$('#kcdm_' + tagOrder).val(selName);
					$('#' + curTargetNum).attr('title', selTitle);
					var titleContent = selTitle+'<input type="hidden" id="kcdm_'+tagOrder+'" value="'+selName+'"/>';//课程代码 
					$('#' + curTargetNum).html(titleContent);
					
					$('#kcdm_' + selOrder).val(tagName);
					$('#' + curSelectNum).attr('title', tagTitle);
					var titleContent = tagTitle+'<input type="hidden" id="kcdm_'+selOrder+'" value="'+tagName+'"/>';//课程代码 
					$('#' + curSelectNum).html(titleContent);
					
					//更新课程表数据
					updateKcb(changeType, selName);
				}
				
				//判断是将课程表区域的内容拖拽到未排课程区域
				if(changeType == 'kcbtowpkc'){					
					//清空课程表单元格内容
					$('#' + curSelectId).removeAttr('title');
					$('#' + curSelectId).html('&nbsp;');
					//var tempArray = updateKcb(changeType);
					updateWpkc(changeType, selName);
				}
				
				//判断是否将未排课程拖拽到课程表
				if(changeType == 'wpkctokcb'){ 
					var tempWpkcCdbh = '';
					var tempCdbh = '';
					var tempWpkcCdmc = '';
					var tempCdmc = '';
					//var siteCodeArrayAll = $('#wpkccdbh_' + selOrder).val().split('&');
					//var siteNameArrayAll = $('#wpkccdmc_' + selOrder).val().split('&');
					//var skzcArray = $('#wpkczcxq_' + selOrder).val().split('&');
					//var tempStuNum = stuNum;//学生人数
					//var selHbFlag = false;
					//var hbCourseName = '';
					//var hbClassName = '';
					var tips = '';
												
					$('#kcdm_' + tagOrder).val(selName);
					$('#' + curTargetNum).attr('title', selTitle);
					var titleContent = selTitle+'<input type="hidden" id="kcdm_'+tagOrder+'" value="'+selName+'"/>';//课程代码 
					$('#' + curTargetNum).html(titleContent);
					
					//var tempArray = updateKcb(changeType);
					updateWpkc(changeType,selName);
				}
				
				selectArea = '';
			});
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
		
		/**更新未排课程数据
			@type 调整课程表的方式
			@skjhmxbh 授课计划明细编号
		**/
		function updateWpkc(type,kcdm){	
			//判断是将课程表区域的内容拖拽到未排课程区域
			if(type == 'kcbtowpkc'){
				//更新未排课程信息
				for(var i=0; i<wpkc.length; i++){
					if(wpkc[i].课程代码 == kcdm){
						wpkc[i].节数 = parseInt(wpkc[i].节数, 10)+1;
						break;
					}
				}
			}
			//判断将未排课程放入课程表中
			if(type == 'wpkctokcb'){ 
				//更新未排课程信息
				for(var i=0; i<wpkc.length; i++){
					if(wpkc[i].课程代码 == kcdm){ 
						wpkc[i].节数 = parseInt(wpkc[i].节数, 10)-1;
						break;
					}
				}
			}
			//清空未排课程表格内容
			$('.wpkcTdCentent').removeAttr('title');
			$('.wpkcTdCentent').html('&nbsp;');
			fillWpkc();//填充未排课程表格
		}
		
		/**更新课程表数据
			@type 调整课程表的方式
			@pjType 是否拼接
			@wpkcCdbh 未排课程随机到的场地编号
			@wpkcCdmc 未排课程随机到的场地名称
		**/
		function updateKcb(type){
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
						tempSelectArray.push('');
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
		
		//加载下拉框数据
		function roomtypeCombobox(){
			$('#GS_CDLX').combobox({
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
				},
				//下拉列表值改变事件
				onChange:function(data){
				
				}
			});
		}
		
		//加载下拉框数据
		function papertypeCombobox(){
			$('#GS_SJLX').combobox({
				url:"<%=request.getContextPath()%>/Svl_Skjh?active=papertypeCombobox",
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
		}
		
		function KCMCCombobox(){
			$('#GS_KCMC').combobox('setValue','请先选择班级');
			$('#GS_KCMC').combobox('disable');
			$('#GS_BJMC').combobox({
				url:"<%=request.getContextPath()%>/Svl_examSet?active=BJMCCombobox&GG_XNXQBM=" + ei_xnxq,
				valueField:'comboValue',
				textField:'comboName',
				editable:true,
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
					if(data==""){
						$('#GS_KCMC').combobox('setValue','请先选择班级');
						$('#GS_KCMC').combobox('disable');
					}else{
						$('#GS_KCMC').combobox({
							url:"<%=request.getContextPath()%>/Svl_examSet?active=KCMCCombobox&GG_XZBDM=" + data +"&GG_XNXQBM=" + ei_xnxq,
							valueField:'comboValue',
							textField:'comboName',
							editable:true,
							panelHeight:'140', //combobox高度
							onLoadSuccess:function(data){
								//判断data参数是否为空
								if(data != ''){
									//初始化combobox时赋值
									$(this).combobox('setValue', '');
									$('#GS_KCMC').combobox('enable');
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
		
		function KSZQCombobox(){
			$('#GS_KSZQ').combobox({
				url:"<%=request.getContextPath()%>/Svl_examSet?active=KSZQCombobox"+"&DRxnxq="+$('#GS_XNXQ_CX').combobox('getValue'),
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
				onChange:function(data){
						
				}
			});
		}
		
		function KSZQDRCombobox(){
			$('#DR_KSZQ').combobox({
				url:"<%=request.getContextPath()%>/Svl_examSet?active=KSZQDRCombobox",
				valueField:'comboValue',
				textField:'comboName',
				editable:false,
				panelHeight:'140', //combobox高度
				onLoadSuccess:function(data){
					//判断data参数是否为空
					if(data != ''){
						//初始化combobox时赋值
						$(this).combobox('setValue', $('#QZQM').combobox('getValue'));
					}
				},
				//下拉列表值改变事件
				onChange:function(data){
						
				}
			});
		}
		
		
		
			
		/**加载 datagrid控件，读取页面信息
			@listData 列表数据
		**/
		//添加删除合班考试
		function loadGridaddcc(BJMC_CX,KCMC_CX,ZYDM_CX,XNXQ_CX,QZQM_CX){
			$('#courseList2').datagrid({
				url : '<%=request.getContextPath()%>/Svl_examSet?active=listkskc&XNXQ='+XNXQ_CX+'&QZQM='+QZQM_CX+'&BJMC_CX='+encodeURI(BJMC_CX)+'&KCMC_CX='+encodeURI(KCMC_CX)+'&ZYDM_CX='+ZYDM_CX+'&examid='+examid,
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
					{field:'sele',title:'',width:fillsize(0.2),align:'center',
						formatter:function(value,rec,row){
							var sfksbox='<input type="checkbox" id="'+rec.授课计划明细编号+'" name="ksjh2" onclick="editExamID(this.id);" />';
							return sfksbox;
						}
					},
					{field:'行政班名称',title:'班级名称',width:fillsize(0.2),align:'center'},
					{field:'课程名称',title:'课程名称',width:fillsize(0.2),align:'center'},
					{field:'专业名称',title:'所属专业',width:fillsize(0.2),align:'center'}
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
					for(var i=0;i<examinfoidarray.length;i++){
						$('#'+examinfoidarray[i]).attr("checked","checked");
					} 			
				}
			});
			
			
		};
		
		//显示一起考试课程
		function loadGrideditcc(XNXQ_CX,QZQM_CX){
			$('#courseList3').datagrid({
				url : '<%=request.getContextPath()%>/Svl_examSet?active=listtskc&XNXQ='+XNXQ_CX+'&QZQM='+QZQM_CX+'&examid='+examid,
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
							var sfksbox='<input type="checkbox" id="sfks'+rec.授课计划明细编号+'" name="ksjh3" onclick="" />';
							if(rec.授课计划明细编号.substring(rec.授课计划明细编号.indexOf("_"),rec.授课计划明细编号.length)==rec.考试场次编号.substring(rec.考试场次编号.indexOf("_"),rec.考试场次编号.length)){
								sfksbox="";
							}
							examinfoidarray.push(rec.授课计划明细编号);							
							return sfksbox;
						}
					},
					{field:'考试场次编号',title:'考试场次编号',width:fillsize(0.2),align:'center'},
					{field:'行政班名称',title:'班级名称',width:fillsize(0.2),align:'center'},
					{field:'课程名称',title:'课程名称',width:fillsize(0.2),align:'center'},
					{field:'专业名称',title:'所属专业',width:fillsize(0.2),align:'center'}
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
					
				}
			});
			
			
		};
		
		//显示参考学生
		function loadGrid5(){
			$('#courseList5').datagrid({
				url : '<%=request.getContextPath()%>/Svl_examSet?active=listckxs&XNXQ='+xnxqVal+'&JXXZ='+jxxzVal+'&QZQM='+qzqmVal+'&examid='+examid,
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
							var ksxxbox='<input type="checkbox" id="ksxx'+rec.授课计划明细编号+'" name="ksxx3" onclick="" />';
														
							return ksxxbox;
						}
					},
					{field:'行政班名称',title:'班级名称',width:fillsize(0.2),align:'center'},
					{field:'学号',title:'学号',width:fillsize(0.2),align:'center'},
					{field:'姓名',title:'姓名',width:fillsize(0.2),align:'center'}
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
					
				}
			});
			
		};
		
		//保存选择的考试信息
		function editExamID(id){
			var checkbox = document.getElementById(id);
			if(checkbox.checked){//勾选
				examinfoidarray.push(checkbox.id);
				for(var i=0;i<delexaminfoidarray.length;i++){
					if(checkbox.id==delexaminfoidarray[i]){
						delexaminfoidarray.splice(i,1);
					}
				}	
			}else{//不勾选
				delexaminfoidarray.push(checkbox.id);
				for(var i=0;i<examinfoidarray.length;i++){
					if(checkbox.id==examinfoidarray[i]){
						examinfoidarray.splice(i,1);
					}
				}
			}
		}
		
		
		function goEnterPage(){
			var e = jQuery.Event("keydown");//模拟一个键盘事件
			e.keyCode = 13;//keyCode=13是回车 
			$("input.pagination-num").trigger(e);//模拟页码框按下回车 
		}
		
		/**读取datagrid数据**/
		function loadData(){
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_CourseSet',
				data : 'active=query&page=' + pageNum + '&rows=' + pageSize + 
					'&KCDM_CX=' + encodeURI(KCDM_CX) + 
					'&KCMC_CX=' + encodeURI(KCMC_CX) + 
					'&ZYDM_CX=' + encodeURI(ZYDM_CX),
				dataType:"json",
				success : function(data) {
					//loadGridaddcc(data[0].listData);
				}
			});
		}
		
		
		
		//删除
		function delClass(){
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_CourseSet',
				data : 'active=del&KCDM=' + iKeyCode,
				dataType:"json",
				success : function(data){
					if(data[0].MSG == '删除成功'){
						showMsg(data[0].MSG);
						loadData();
					}else{
						alertMsg(data[0].MSG);
					}
				}
			});
		}
		
		//显示考试规则
		function loadGridSchedule(kslx){  
			isLoad = true;
			$('#listKSGZ').datagrid({
				url: '<%=request.getContextPath()%>/Svl_examSet',
	 			queryParams: {"active":"loadGridSchedule","kszbbh":ei_jzzs,"kslx":kslx},
				loadMsg : "信息加载中请稍后!",//载入时信息
				width:'100%',
				rownumbers: true,
				animate:true,
				striped : true,//隔行变色
				pageSize : pageSize,//每页记录数
				singleSelect : false,//单选模式
				pageNumber : pageNum,//当前页码
				pagination:false,
				fit:true,
				fitColumns: true,//设置边距
				columns:[[
					{field:'exs',checkbox:true},
					{field:'考试日期',title:'考试日期',width:100,align:'center'},
					{field:'考试时间段',title:'考试时间段',width:100,align:'center'},
					{field:'考试类型',title:'考试类型',width:100,align:'center'},
					{field:'考试年级',title:'考试年级',width:100,align:'center'},
					{field:'课程类型',title:'课程类型',width:100,align:'center'},
					{field:'教室人数',title:'教室人数',width:100,align:'center'},
					{field:'考场数量',title:'考场数量',width:100,align:'center'},
					{field:'可用大教室',title:'可用大教室',width:100,align:'center'}							
				]],
				onSelect:function(rowIndex,rowData){
					if(isLoad == false){
						ksrqsjdarray.push(rowData.考试日期+"#"+rowData.考试时间段);
					}
				},
				onUnselect:function(rowIndex,rowData){
					$.each(ksrqsjdarray, function(key,value){
						if(value == rowData.考试日期+"#"+rowData.考试时间段){
							ksrqsjdarray.splice(key, 1);
						}
					});
				},
				onSelectAll:function(rows){
					ksrqsjdarray.splice(0,ksrqsjdarray.length);
					for(var i=0;i<rows.length;i++){
						ksrqsjdarray.push(rows[i].考试日期+"#"+rows[i].考试时间段);
					}
				},
				onUnselectAll:function(rows){
					ksrqsjdarray.splice(0,ksrqsjdarray.length);
				},
				onLoadSuccess: function(data){
					//$(".datagrid-header-check").html('&nbsp;');//隐藏全选
					//勾选已选授课教师
					if(data){
						//$.each(data.rows, function(rowIndex, rowData){

						//});
						$('#listKSGZ').datagrid('scrollTo',0); //移至第一行
					}
		 			isLoad = false;
				},
				onLoadError:function(none){
					
				}
			});
		}
		
		//左边datagrid
		function loadGridselRQ(rqsjd){ 
			isLoad = true;
			$('#tskc_rqsjd').datagrid({
				url: '<%=request.getContextPath()%>/Svl_examSet',
	 			queryParams: {"active":"loadGridselRQ","kszbbh":ei_jzzs},
				loadMsg : "信息加载中请稍后!",//载入时信息
				width:'100%',
				height:'100%',
				rownumbers: true,
				animate:true,
				striped : true,//隔行变色
				pageSize : pageSize,//每页记录数
				singleSelect : false,//单选模式
				pageNumber : pageNum,//当前页码
				pagination:false,
				fit:true,
				fitColumns: true,//设置边距
				columns:[[
					{field:'exs',checkbox:true},
					{field:'考试日期',title:'考试日期',width:100,align:'center'},
					{field:'考试时间段',title:'考试时间段',width:100,align:'center'}	
				]],
				onSelect:function(rowIndex,rowData){
					if(isLoad == false){
						tskcrqsjdarray.push(rowData.考试日期+"#"+rowData.考试时间段);
					}
				},
				onUnselect:function(rowIndex,rowData){
					$.each(tskcrqsjdarray, function(key,value){
						if(value == rowData.考试日期+"#"+rowData.考试时间段){
							tskcrqsjdarray.splice(key, 1);
						}
					});
				},
				onSelectAll:function(rows){
					tskcrqsjdarray.splice(0,tskcrqsjdarray.length);
					for(var i=0;i<rows.length;i++){
						tskcrqsjdarray.push(rows[i].考试日期+"#"+rows[i].考试时间段);
					}
				},
				onUnselectAll:function(rows){
					tskcrqsjdarray.splice(0,tskcrqsjdarray.length);
				},
				onLoadSuccess: function(data){
					//$(".datagrid-header-check").html('&nbsp;');//隐藏全选
					//勾选已选授课教师
					if(data){
						$.each(data.rows, function(rowIndex, rowData){
							if(!rqsjd==""){
								var sjxl=rqsjd.split(",");
								for(var i=0;i<sjxl.length;i++){
									if(rowData.考试日期+"#"+rowData.考试时间段==sjxl[i]){
										$('#tskc_rqsjd').datagrid('selectRow', rowIndex);
									}
								}
							}
						});
						$('#tskc_rqsjd').datagrid('scrollTo',0); //移至第一行
						
					}
		 			isLoad = false;
				},
				onLoadError:function(none){
					
				}
			});
		}
		
		//右侧datagrid
		function loadGridselKSKC(sp_ksccbh){ 
				isLoad = true;
				$('#tskc_kscc').datagrid({
					url: '<%=request.getContextPath()%>/Svl_examSet',
		 			queryParams: {"active":"loadGridselKSKC","kszbbh":ei_jzzs,"kcmc":$('#tskc_KCMC').textbox('getValue'),"bjmc":$('#tskc_BJMC').textbox('getValue'),"ksccbh":sp_ksccbh,"kstype":bktype,"GG_XNXQBM":ei_xnxq},
					loadMsg : "信息加载中请稍后!",//载入时信息
					width:'100%',
					height:'100%',
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
						{field:'授课计划明细编号',title:'考试场次编号',width:60,align:'center'},
						{field:'课程名称',title:'课程名称',width:90,align:'center',
							formatter:function(value,rec,row){
								var kcmc="";						
								if(rec.课程名称.indexOf(",")>-1){
									var kcs=rec.课程名称.split(",");
									kcmc=kcs[0];
									for(var i=1;i<kcs.length;i++){		
										if(kcmc.indexOf(kcs[i])>-1){//不添加
										
										}else{
											kcmc+=","+kcs[i];
										}
									}
								}else{
									kcmc=rec.课程名称;
								}						
								return kcmc;
							}
						},
						{field:'行政班名称',title:'行政班名称',width:120,align:'center'}													
					]],
					onClickRow:function(rowIndex, rowData){
						//$('#list').datagrid('unselectRow',rowIndex);
// 						iKeyCodecx = rowData.授课计划明细编号;
 						tskc_ksccbh= rowData.授课计划明细编号;
 						tskc_kcmc=rowData.课程名称;
 						tskc_bjmc=rowData.行政班名称;
					},
					onLoadSuccess: function(data){
						//$(".datagrid-header-check").html('&nbsp;');//隐藏全选
						isLoad = false;
						if(data){
							$.each(data.rows, function(rowIndex, rowData){
								if(rowData.sel=="1"){
									$('#tskc_kscc').datagrid('selectRow', rowIndex);
								}
							});
							//$('#tskc_rqsjd').datagrid('scrollTo',0); //移至第一行						
						}
					},
					onLoadError:function(none){
						
					}
				});
				
		}
		
		//设置 
		function examtableDetail(PK_XNXQMC, PK_XNXQBM, ex_ksmc, PK_KSZBBH){ 
			var useFlag = false;
			var msgStr = '';
			lastIndex=-1;
			
			$('#classTimetable').dialog({
				title:ex_ksmc,
				toolbar:[{
						text:'设置考试日期',
						iconCls:'icon-collection_edit',
						handler:function(){
							doToolbar("setExamDate");
						}
					},{
						text:'导入监考教师',
						iconCls:'icon-site',
						handler:function(){
							doToolbar("importExamTeacher");
						}
					},{
						text:'设置监考教师',
						iconCls:'icon-collection_edit',
						handler:function(){
							doToolbar("setExamTeacher");
						}
					}
				]
			});
			$('#classTimetable').dialog("open");
			ei_xnxq=PK_XNXQBM;
			ei_zbbh=PK_KSZBBH;
			//加载班级信息TREE
			loadClassTree(PK_XNXQBM, PK_KSZBBH);

			//初始化空白课程表
			//initBlankKCAP(PK_XNXQBM, PK_KSZBBH, useFlag, msgStr);			
			
			//专业代码 combobox
// 			$('#kc_zydm').combobox({
//				url:"< %=request.getContextPath()%>/Svl_examSet?active=kc_zydmCombobox&XNXQ="+ei_xnxq ,
// 				valueField:'comboValue',
// 				textField:'comboName',
// 				editable:false,
// 				panelHeight:'140', //combobox高度
// 				onLoadSuccess:function(data){
// 					//判断data参数是否为空
// 					if(data != ''){
// 						//初始化combobox时赋值
// 						$(this).combobox('setValue', data[0].comboValue);
		
// 					}
// 				},
// 				//下拉列表值改变事件
// 				onChange:function(data){
// 					kskcarray.splice(0,kskcarray.length);
// 					loadGridExamCourse();
// 				}
// 			});
			
		}
		
		/**加载班级信息TREE
			@PK_ZYDM 专业代码
		**/
		function loadTeaTree(PK_XNXQBM,PK_KSZBBH){  
			$('#teacherTree').tree({
				checkbox: false,
				url:'<%=request.getContextPath()%>/Svl_examSet?active=queTeaTree&sAuth=' + sAuth + '&level=0&PK_XNXQBM=' + PK_XNXQBM + '&PK_KSZBBH=' + PK_KSZBBH,
			    onClick:function(node){
			    	//判断点击的是不是老师
			    	loadClassKCJS(PK_XNXQBM, node.id, node.text);
				},
				onDblClick:function(node){
				
				},
			    onBeforeExpand:function(node,param){
				  	//$('#teacherTree').tree('options').url="< %=request.getContextPath()%>/Svl_Kbcx?active=queTeaTree&sAuth=" + sAuth + "&level=1&PK_XNXQBM=" + PK_XNXQBM + "&PK_ZYDM=" + PK_ZYDM + "&parentCode="+node.id;
				},
				onLoadSuccess:function(node, data){ 
					$("#teacherTree").show();
				}
			});
		};
		
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
				    			loadGridExamDate(classId);
				    			$('#saveKCAP').linkbutton('enable');
				    		//}
						}
						parentId=node.id;
			    	}else{
			    		//loadGridExamDate("");
			    		classflag=false;
			    	}					
				},
//  				onBeforeLoad:function(row,param){     //分层显示treegrid
// 					$('#classTree').tree('options').url='< %=request.getContextPath()%>/Svl_Skjh?active=queryTree&AUTH='+sAuth+'&parentId='+'&iUSERCODE='+iUSERCODE+'&GS_XNXQBM='+(xnxqVal+jxxzVal);
//  				},
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
					wpkc = data[0].wpkc;//授课计划明细编号,课程名称,授课教师姓名,场地要求,场地名称,授课周次,剩余节数
										
					fillKcb(PK_XNXQBM, classId);//填充当前课程表单元格内的课程信息
					fillWpkc();//填充未排课程表格
					$('#divPageMask').hide();
					loadFlag = false;
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
					tempContent = classKCB[i].课程名称;
					tempTitle = classKCB[i].课程名称;
					
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
				if(wpkc[i].节数 > 0){
					tempContent = wpkc[i].课程名称;
					tempTitle = wpkc[i].课程名称;

					$('#wpkc_' + num).attr('title', tempTitle);
					tempContent += '<input type="hidden" id="kcdm_'+num+'" value="'+classKCB[i].课程代码+'"/>';//课程代码
					$('#wpkc_' + num).html(tempContent);
					num++; 	
				}
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
										'onmousedown="selectKcbCourse(this.id, \'kcb\');" ondblclick="">&nbsp;';
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
		
		
		
		
		/**清空Dialog中表单元素数据**/
		function emptyDialog(){
			saveType = '';
			$('#KCDM').html("");
			$('#KCMC').textbox('setValue', '');
			$('#ZYDM').combobox('setValue', '');
			$('#KCLX').combobox('setValue', '');
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
		
		//导出大补考答疑表==================================================================================
		function ExportExcelDBKDY(){
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_examSet',
				data : 'active=ExportExcelDBKDY&GG_XNXQBM='+(ei_xnxq),
				dataType:"json",
				success : function(data) {
					$('#divPageMaskDBK').hide();
					if(data[0].MSG == '文件生成成功'){
						//下载文件到本地
						$("#exportIframe").attr("src", '<%=request.getContextPath()%>/form/elective/fileDownload.jsp?filePath=' + encodeURI(encodeURI(data[0].filePath)));
					}else{
						alertMsg(data[0].MSG);
					}
				}
			});
		}
		
		//导出补考安排表==================================================================================
		function ExportExcelBKAP(){ 
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_examSet',
				data : 'active=ExportExcelBKAP&GG_XNXQBM='+(ei_xnxq),
				dataType:"json",
				success : function(data) {
					$('#divPageMaskBK').hide();
					if(data[0].MSG == '文件生成成功'){
						//下载文件到本地
						$("#exportIframe").attr("src", '<%=request.getContextPath()%>/form/elective/fileDownload.jsp?filePath=' + encodeURI(encodeURI(data[0].filePath)));
					}else{
						alertMsg(data[0].MSG);
					}
				}
			});
		}
		//导出大补考安排表==================================================================================
		function ExportExcelDBKAP(){
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_examSet',
				data : 'active=ExportExcelDBKAP&GG_XNXQBM='+(ei_xnxq),
				dataType:"json",
				success : function(data) {
					$('#divPageMaskDBK').hide();
					if(data[0].MSG == '文件生成成功'){
						//下载文件到本地
						$("#exportIframe").attr("src", '<%=request.getContextPath()%>/form/elective/fileDownload.jsp?filePath=' + encodeURI(encodeURI(data[0].filePath)));
					}else{
						alertMsg(data[0].MSG);
					}
				}
			});
		}
		
		//防JS注入
		/*
		function checkData(str) {
	        var  entry = { "'": "&apos;", '"': '&quot;', '<': '&lt;', '>': '&gt;' };
	        str = v.replace(/(['")-><&\\\/\.])/g, function ($0){
	        	return entry[$0] || $0;
	        });
	        return str;
	    }*/
		
		
		
		//处理键盘事件
		// 禁止后退键（Backspace）密码或单行、多行文本框除外
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
		
	</script>
</html>
