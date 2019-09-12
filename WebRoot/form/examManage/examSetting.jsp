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
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.src.develop.store.user.*"%>
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
	
	<body class="easyui-layout" >
		<div data-options="region:'west'" title="" style = "width:28%;">
			<div class="easyui-layout" fit="true">
				<div region="north" split="true" border="false" style="height:119px;"> 
					<table  class = "tablestyle" width = "100%">
						<tr>
							<td class="titlestyle">学年学期</td>
							<td>
								<select id="XNXQ" name="XNXQ" class="easyui-combobox" style="width:200px;"></select>
							</td>
						</tr>
						<tr>
							<td class="titlestyle">教学性质</td>
							<td>
								<select id="JXXZ" name="JXXZ" class="easyui-combobox" style="width:200px;" panelHeight="auto"></select>
							</td>
						</tr>
						<tr>
							<td class="titlestyle">考试周期</td>
							<td>
								<select id="QZQM" name="QZQM" class="easyui-combobox" style="width:200px;" panelHeight="auto"></select>
							</td>
						</tr>
						<tr>
							<td class="titlestyle">考试日期</td>
							<td >
 								<a href="#" onclick="doToolbar(this.id);" id="setExamDate"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-edit'">设置考试日期</a>		
							</td>
						</tr>
					</table>	
				</div> 
				<div region="center" border="false"> 
					<div id="classTree" class="easyui-tree" style="width:100%;">
					</div>
				</div> 
			</div> 		
		</div>

		<div data-options="region:'center'" style="overflow-x:hidden;">
			<div class="easyui-layout" fit="true">
				<div region="north" split="true" border="false" style="height:35px;width:100%;"> 
					<table  class = "tablestyle" width = "100%">
						<tr>
							<td>
								<a href="#" onclick="doToolbar(this.id);" id="newks"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-new'">新建</a>
								<a href="#" onclick="doToolbar(this.id);" id="edit"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-edit'">编辑</a>		
								<a href="#" onclick="doToolbar(this.id);" id="importExam" class="easyui-linkbutton" data-options="iconCls:'icon-site',plain:true">导入考试信息</a> 					
<!-- 								<a href="#" onclick="doToolbar(this.id);" id="importTeacher" class="easyui-linkbutton" data-options="iconCls:'icon-site',plain:true">导入监考教师</a> -->
<!-- 								<a href="#" onclick="doToolbar(this.id);" id="importClassroom" class="easyui-linkbutton" data-options="iconCls:'icon-site',plain:true">导入考试教室</a> -->
								<a href="#" onclick="selectKSXS();" id="export" class="easyui-linkbutton" data-options="iconCls:'icon-submit',plain:true">导出考试信息</a>
								<a href="#" onclick="doToolbar(this.id);" id="notcourse" class="easyui-linkbutton" data-options="iconCls:'icon-collection_edit',plain:true">未排课程</a>
								<a href="#" onclick="doToolbar(this.id);" id="allksxs" class="easyui-linkbutton" data-options="iconCls:'icon-collection_edit',plain:true">考试信息查询</a>
								<a href="#" onclick="doToolbar(this.id);" id="bukao" class="easyui-linkbutton" data-options="iconCls:'icon-collection_edit',plain:true">补考信息</a>
								<a href="#" onclick="doToolbar(this.id);" id="dabukao" class="easyui-linkbutton" data-options="iconCls:'icon-collection_edit',plain:true">大补考信息</a>
							</td>
						</tr>
					</table>
				</div> 
				<div region="center" border="false"> 
					<table id='list' style="width:100%;height:20%;">
			
					</table>
					
					<%-- 遮罩层 --%>
			    	<div id="divPageMask2" style="position:absolute;top:0px;left:0px;width:100%;height:100%;">
			    	</div>
					
					<!-- 场次安排 -->
					<div id="ic_ksccap" style="width:786px;height:91px;float:left;" >
						<div style="width:100%;height:464px;">
							<div style="border-left:1px solid #95B8E7;">
							<table >
								<tr>
									<td ><a href="#" id="addselect" class="easyui-linkbutton" plain="true" iconcls="icon-add" onClick="doToolbar(this.id);" title="">添加</a></td>
									<td ><a href="#" id="delselect" class="easyui-linkbutton" plain="true" iconcls="icon-cancel" onClick="doToolbar(this.id);" title="">删除</a></td>
								</tr>
							</table>
							</div>
							<div style="width:100%;height:432px;">
								<table id="courseList3" style="width:100%;"></table>
							</div>
						</div>
					</div>
					
					<div id="ic_ksccapedit" style="width:786px;height:91px;float:left;" >
						<div style="width:100%;height:400px;">
							<div>
							<table>
								<tr>
									<td><a href="#" id="saveselect" class="easyui-linkbutton" plain="true" iconcls="icon-save" onClick="doToolbar(this.id);" title="">保存</a></td>
								</tr>
							</table>
							<table id="ee" singleselect="true" width="100%" class="tablestyle">
								<tr>	
									<td style="width:80px;" class="titlestyle">班级名称</td>
									<td>
										<input name="BJMC_CX" id="BJMC_CX" class="easyui-textbox" style="width:120px;"/>
									</td>
									<td style="width:80px;" class="titlestyle">课程名称</td>
									<td>
										<input name="KCMC_CX" id="KCMC_CX" class="easyui-textbox" style="width:120px;"/>
									</td>
									<td style="width:80px;" class="titlestyle">所属专业</td>
									<td>
										<select name="ZYDM_CX" id="ZYDM_CX" class="easyui-combobox" style="width:120px;">
										</select>
									</td>
									<td colspan=2 style="width:80px;" align="center">
										<a href="#" onclick="doToolbar(this.id)" id="query" class="easyui-linkbutton" plain="true" iconcls="icon-search">查询</a>
									</td>				
								</tr>
						    </table>
						    </div>
						    <div style="width:100%;height:402px;">
								<table id="courseList2" style="width:100%;"></table>
							</div>
						</div>
					</div>
					
					<!-- 参考学生 -->
					<div id="ic_cjksxs" style="width:786px;height:91px;float:left;" >
						<div style="width:100%;height:464px;">
							<div style="border-left:1px solid #95B8E7;">
							<table >
								<tr>
									<td ><a href="#" id="addselect" class="easyui-linkbutton" plain="true" iconcls="icon-add" onClick="" title="">添加</a></td>
									<td ><a href="#" id="delselect" class="easyui-linkbutton" plain="true" iconcls="icon-cancel" onClick="" title="">删除</a></td>
								</tr>
							</table>
							</div>
							<div style="width:100%;height:432px;">
								<table id="courseList5" style="width:100%;"></table>
							</div>
						</div>
					</div>
					
					<!-- 编辑考试信息 -->
					<div id="win" title="编辑">
						<form id='fm' method='post' style="margin: 0px">
							<table class = "tablestyle" width="100%">
								<tr>
									<a href="#" class="easyui-linkbutton" id="savesfks" name="savesfks" iconCls="icon-save" plain="true" onClick="doToolbar(this.id)">保存</a>
								</tr>
								
								<tr>
									<td class="titlestyle" id="GS_KCLXN">是否考试</td>
									<td id="CH_KCLX">
										<select name="GS_SFKS" id="GS_SFKS" class="easyui-combobox" panelHeight="auto" style="width:250px;">
											<option value="1">是</option>
											<option value="2">否</option>
										</select>
									</td>
								</tr>
								<tr>
									<td class="titlestyle" >考试形式</td>
									<td onclick="">
										<select name="GS_KSXS" id="GS_KSXS" class="easyui-combobox" style="width:250px;" panelHeight="auto">
										</select>
									</td>
								</tr>
								<tr>
									<td class="titlestyle" >场地类型</td>
									<td onclick="">
										<select name="GS_CDLX" id="GS_CDLX" class="easyui-combobox" style="width:250px;" panelHeight="auto">
										</select>
									</td>
								</tr>
								<tr>
									<td class="titlestyle" >考试周期</td>
									<td >
										<select name="GS_KSZQ" id="GS_KSZQ" class="easyui-combobox" style="width:250px;" panelHeight="auto">
										</select>
									</td>
								</tr>
								<tr>
									<td class="titlestyle" >试卷类型</td>
									<td onclick="">
										<select name="GS_SJLX" id="GS_SJLX" class="easyui-combobox" style="width:250px;" panelHeight="auto">
										</select>
									</td>
								</tr>
								
							</table>
							
							<!-- 隐藏属性,传参用 -->
							<input type="hidden" id="active" name="active" />
							<input type="hidden" id="GS_SKJHMXBH" name="GS_SKJHMXBH" /><!-- 授课计划明细编号 -->
							<input type="hidden" id="GS_XZBDM" name="GS_XZBDM" />
							<input type="hidden" id="GS_XNXQBM" name="GS_XNXQBM" />
							<input type="hidden" id="GS_KCDM" name="GS_KCDM" />
							<input type="hidden" id="GS_KCMCDM" name="GS_KCMCDM" />
							<input type="hidden" id="GS_SKJSBH" name="GS_SKJSBH" /><!-- 授课教师编号 -->
							<input type="hidden" id="GS_CDMC" name="GS_CDMC" /><!-- 场地名称（存的场地要求）；GS_CDYQ存的场地名称 -->
							<input type="hidden" id="GS_SKZCXQ" name="GS_SKZCXQ" /><!-- 授课周次详情（存的授课周次）；GS_SKZC存的授课周次详情 -->
							<input type="hidden" id="iUSERCODE" name="iUSERCODE" />
						</form>
					</div>
									
					<!-- 编辑考试信息 -->
					<div id="dbkksxs" title="编辑">
						<form id='fm2' method='post' style="margin: 0px">
							<table class = "tablestyle" width="100%">
								<tr>
									<a href="#" class="easyui-linkbutton" id="savedbkksxs" name="savedbkksxs" iconCls="icon-save" plain="true" onClick="doToolbar(this.id)">保存</a>
								</tr>
								
								<tr>
									<td class="titlestyle" >课程名称</td>
									<td id="DA_KCMC">
										
									</td>
								</tr>
								<tr>
									<td class="titlestyle" >班级简称</td>
									<td id="DA_BJJC">
										
									</td>
								</tr>
								<tr>
									<td class="titlestyle" >考试形式</td>
									<td onclick="">
										<select name="DA_KSXS" id="DA_KSXS" class="easyui-combobox" style="width:150px;" panelHeight="auto">
										</select>
									</td>
								</tr>
				
								
							</table>
							
							<!-- 隐藏属性,传参用 -->
			
						</form>
					</div>
					
					<!-- 新建考试信息 -->
					<div id="newExam" title="新建">
						
							<table class = "tablestyle" width="100%">
								<tr>
									<a href="#" class="easyui-linkbutton" id="saveNewExam" name="saveNewExam" iconCls="icon-save" plain="true" onClick="doToolbar(this.id)">保存</a>
								</tr>					
								<tr>
									<td class="titlestyle">学科名称</td>
									<td id="CH_KCDM">
										<select name="GS_KCMC" id="GS_KCMC" class="easyui-combobox" panelHeight="auto" style="width:250px;" >
										</select>
									</td>
								</tr>
								
							</table>
										
					</div>
					
					<div id="room" title="编辑">
						<div >
							<table id="roomtable" class = "tablestyle" width="566px" >
								<tr>
									<a href="#" class="easyui-linkbutton" id="submit5" name="submit5" iconCls="icon-submit" plain="true" onClick="doToolbar(this.id)">确定</a>
								</tr>
								<tr>
									<td width="25%" align="center"><input type="checkbox" id="choosetype" name="" onclick="chooseType();" /> 只选类型</td>
									<td width="25%"></td><td width="25%"></td><td width="25%"></td>
								</tr>
								<tr>
									<td width="25%" align="center">普通教室</td>
									<td width="25%" align="center"><input class="easyui-numberbox" id="normalroom" name="" /></td>
									<td width="25%" align="center">多媒体教室</td>
									<td width="25%" align="center"><input class="easyui-numberbox" id="meidaroom" name="" /></td>
								</tr>
								<tr>
									<td width="25%" align="center"><input type="checkbox" id="chooseroom" name="" onclick="chooseRoom();" /> 指定教室</td>
									<td width="25%"></td><td width="25%"></td><td width="25%"><br><br></td>
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
					
					<div id="showDialog" style="width:250px;height:141px;">
						<iframe id="iframe" name="iframe" width="100%" height="100%"></iframe>
					</div>
										
					<div id="notCourseDiv" title="编辑">
						<table width = "100%">
							<tr><a id="deleteWeipai" onclick="doToolbar(this.id)" class="easyui-linkbutton" href="javascript:void(0);" data-options="plain:true,iconCls:'icon-cancel'">删除</a></tr>
						</table>
						<table id="ncourtable" class = "tablestyle" width="886px">
						</table>
					</div>
					
					<!-- 设置考试日期 -->
					<div id="examDateDiv" title="编辑">
						<table  class = "tablestyle" width = "100%">
							<tr>
								<a href="#" class="easyui-linkbutton" id="saveExamDate" name="saveExamDate" iconCls="icon-save" plain="true" onClick="doToolbar(this.id)">保存</a>
							</tr>
							<tr>
								<td class="titlestyle">考试日期</td>
								<td>
									<input type="text"  id="kcKSRQ" style="width:196px">
										<script type="text/javascript" charset="utf-8">	
											var picker = new Kalendae.Input('kcKSRQ',{
												//attachTo:document.body,
												months:2,
												mode:'multiple',
												format:'YYYY-MM-DD',
												titleFormat:'YYYY,MM',
												selected:[Kalendae.moment().subtract({M:1}), Kalendae.moment().add({M:1})]
											});
											//alert(picker.getSelectedAsText());
										</script>
								</td>
							</tr>
							<tr>
								<td class="titlestyle">选修课考试日期</td>
								<td>
									<select id="xxkKSRQ" name="xxkKSRQ" class="easyui-datebox" style="width:200px;" panelHeight="auto" ></select>
								</td>
							</tr>							
						</table>
					</div> 
					
					<div id="allKSXSDiv" title="编辑">
						<div class="easyui-layout" style="height:100%; width:100%;">
							<div region="north" style="height:64px;">
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
						   				<td class="titlestyle">课程名称</td>
								   		<td>
								   			<input class="easyui-textbox" id="GS_KCMC_CX" name="GS_KCMC_CX"/>
										</td>
										<td class="titlestyle">专业名称</td>
								   		<td>
								   			<input class="easyui-combobox" id="GS_ZYMC_CX" name="GS_ZYMC_CX"/>
										</td>
										<td class="titlestyle">考试形式</td>
								   		<td>
								   			<input name="GS_KSXS_CX" id="GS_KSXS_CX" class="easyui-combobox"/>
										</td>
						   				<td class="titlestyle" >行政班名称</td>
								   		<td>
								   			<input class="easyui-textbox" id="GS_XZBMC_CX" name="GS_XZBMC_CX">
										</td>
										<td >
											<a href="#" id="queAll" class="easyui-linkbutton" plain="true" iconCls="icon-search" onclick="doToolbar(this.id);">查询</a>
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
					
					<!-- 补考 -->
					<div id="bukaoDiv" title="编辑">
						<div class="easyui-layout" style="height:100%; width:100%;">
							<div region="north" style="height:64px;">
								<table width = "100%">
									<tr>
										<td>
											<a href="#" onclick="doToolbar(this.id);" id="editBUKAO"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-edit'">编辑</a>
											<a href="#" onclick="doToolbar(this.id);" id="addDBKinfo"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add'">合并</a>
											<a href="#" onclick="doToolbar(this.id);" id="exportdbkDAYI"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-submit'">导出答疑表</a>
											<a href="#" onclick="doToolbar(this.id);" id="exportDABUKAO"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-submit'">导出补考安排表</a>
										</td>
									</tr>
								</table >
								<table width = "100%" class="tablestyle">
									<tr>
						   				<td class="titlestyle">课程名称</td>
								   		<td>
								   			<input class="easyui-textbox" id="BK_KCMC_CX" name="BK_KCMC_CX"/>
										</td>
										
										<td class="titlestyle">考试形式</td>
								   		<td>
								   			<input name="BK_KSXS_CX" id="BK_KSXS_CX" class="easyui-combobox"/>
										</td>
						   				<td class="titlestyle" >行政班名称</td>
								   		<td>
								   			<input class="easyui-textbox" id="BK_XZBMC_CX" name="BK_XZBMC_CX">
										</td>
										<td >
											<a href="#" id="queBUKAO" class="easyui-linkbutton" plain="true" iconCls="icon-search" onclick="doToolbar(this.id);">查询</a>
										</td>
						   			</tr>
								</table>
							</div>
							<div region="center">
								<%-- 遮罩层 --%>
						    	<div id="divPageMask4" class="maskStyle">
						    		<div id="maskFont2"></div>
						    	</div>
								<table id="bukaotable" class = "tablestyle" width="938px">
								</table>
							</div>
						</div>
					</div>
					
					<!-- 大补考 -->
					<div id="dabukaoDiv" title="编辑">
						<div class="easyui-layout" style="height:100%; width:100%;">
							<div region="north" style="height:64px;">
								<table width = "100%">
									<tr>
										<td>
											<a href="#" onclick="doToolbar(this.id);" id="editDABUKAO"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-edit'">编辑</a>
											<a href="#" onclick="doToolbar(this.id);" id="addDBKinfo"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add'">合并</a>
											<a href="#" onclick="doToolbar(this.id);" id="exportdbkDAYI"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-submit'">导出答疑表</a>
											<a href="#" onclick="doToolbar(this.id);" id="exportDABUKAO"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-submit'">导出大补考安排表</a>
										</td>
									</tr>
								</table >
								<table width = "100%" class="tablestyle">
									<tr>
						   				<td class="titlestyle">课程名称</td>
								   		<td>
								   			<input class="easyui-textbox" id="DA_KCMC_CX" name="DA_KCMC_CX"/>
										</td>
										
										<td class="titlestyle">考试形式</td>
								   		<td>
								   			<input name="DA_KSXS_CX" id="DA_KSXS_CX" class="easyui-combobox"/>
										</td>
						   				<td class="titlestyle" >行政班名称</td>
								   		<td>
								   			<input class="easyui-textbox" id="DA_XZBMC_CX" name="DA_XZBMC_CX">
										</td>
										<td >
											<a href="#" id="queDABUKAO" class="easyui-linkbutton" plain="true" iconCls="icon-search" onclick="doToolbar(this.id);">查询</a>
										</td>
						   			</tr>
								</table>
							</div>
							<div region="center">
								<%-- 遮罩层 --%>
						    	<div id="divPageMask4" class="maskStyle">
						    		<div id="maskFont2"></div>
						    	</div>
								<table id="dabukaotable" class = "tablestyle" width="938px">
								</table>
							</div>
						</div>
						
					</div>
					
					<!-- 选择考试形式 -->
					<div id="ksxsInfo" title="请选择考试形式">
						<table id="ksxstable" class = "tablestyle" width="286px">
						</table>
					</div>
					
					<!-- 考试课程导出页面 -->
					<div id="exportTimetable">
						<!--引入编辑页面用Ifram-->
						<iframe id="exportTimetableiframe" name="exportTimetableiframe" src='' style='width:100%; height:100%;' frameborder="0" scrolling="no"></iframe>
					</div>
					
					<!-- 导入考试信息 -->
					<div id="importExamDialog" title="请选择要导入的时间范围">
						<table width="236px" class="tablestyle">
								<tr>
									<td colspan=2 align="left">
									<a href="#"	onclick="doToolbar(this.id)" id="importExamSubmit" class="easyui-linkbutton" plain="true" iconcls="icon-save">确定</a></td>
								</tr>
								<tr>
								<td class="titlestyle">学年学期</td>
								<td id="DR_XNXQ" name="DR_XNXQ">
								</td>
							</tr>
							<tr>
								<td class="titlestyle">考试周期</td>
								<td>
									<select id="DR_KSZQ" name="DR_KSZQ" class="easyui-combobox" style="width:150px;" panelHeight="auto"></select>
								</td>
							</tr>
						</table>
					</div>
					
				</div> 
			</div>
		</div>
		
		<!-- 下载excel -->
		<iframe id="exportIframe" src="" style="width:0; height:0;"></iframe>
	</body>
	
	<script type="text/javascript">
		var iUSERCODE="<%=MyTools.getSessionUserCode(request)%>";
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
		var pageNum = 1;   //datagrid初始当前页数
		var pageSize = 20; //datagrid初始页内行数
		var weeks="";//总周次
		var paike="0";//排课状态
		
		var iKeyCode = "";//授课计划明细编号
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

		var iKeyCode = ''; //定义主键
		var NJDM_CX = '';//查询条件
		var ZYDM_CX = '';
		var KCDM_CX = '';
		var KCMC_CX = '';
		var BJMC_CX = '';
		var saveType = '';
		var examinfoidarray =new Array();//存放选择的考试场次
		var delexaminfoidarray =new Array();//存放选择的考试场次
		var delweipaiarray=new Array();//未排课程
		var ksxsarray =new Array();//考试形式
		var examid="";//考试编号
		var examks="";//考试时间段
		var majorId="";//专业编号
		var cdlx=""; //场地类型
		var ksbh=""; //考试形式编号
		var ksxs=""; //考试形式
		
		var bk_skjhmx=""; //授课计划明细编号;
		var bk_xnxqbm=""; //学年学期编码;
		var bk_kcmc=""; //课程名称;
		var bk_rs=""; //人数;
		var bk_ksxs=""; //考试形式;
		var bk_xmc=""; //系名称;
		var bk_xzbjc=""; //.行政班简称;
		var bk_xzbmc=""; //行政班名称;
		
		var dbk_skjhmx=""; //授课计划明细编号;
		var dbk_xnxqbm=""; //学年学期编码;
		var dbk_kcmc=""; //课程名称;
		var dbk_rs=""; //人数;
		var dbk_ksxs=""; //考试形式;
		var dbk_xmc=""; //系名称;
		var dbk_xzbjc=""; //.行政班简称;
		var dbk_xzbmc=""; //行政班名称;
		
		var bkarray =new Array();//补考array
		var dbkarray =new Array();//大补考array
		var flag=0;//拆分标记
		var bktype="";//补考类型

		
		$(document).ready(function(){
			loadXQWEEK();
			initDialog();//初始化dialog
			initGridData(classId,xnxqVal,jxxzVal,qzqmVal,"1");//页面初始化加载数据
			initData();
			roomtypeCombobox();
			papertypeCombobox();
			KSZQCombobox();
		});
		
		//页面初始化加载数据
		function loadXQWEEK(){
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_examSet',
				data : 'active=initData&page=' + pageNum + '&rows=' + pageSize+'&iUSERCODE='+iUSERCODE,
				dataType:"json",
				success : function(data) {
					xnxq = data[0].xnxqData;//获取学年学期下拉框数据
					jxxz = data[0].jxxzData;//获取教学性质下拉框数据
					qzqm = data[0].qzqmData;//获取教学性质下拉框数据
					xnxqCombobox(xnxq,jxxz,qzqm);//初始化下拉框
					treegrid(data[0].listData);
					loadData("","");
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
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_examSet',
				data : 'active=loadKSRQ&GG_XNXQBM=' + (xnxqVal+jxxzVal)+'&QZQM='+qzqmVal,
				async : false,
				dataType:"json",
				success : function(data) {
					//$('#KSRQ').datebox('setValue',data[0].MSG);
					$('#kcKSRQ').val(data[0].DAY);
					$('#xxkKSRQ').datebox('setValue',data[0].xxkDAY);
				}
			});
		}
		
		//保存考试日期
		function saveKSRQ(){ 
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_examSet',
				data : 'active=saveKSRQ&GG_XNXQBM=' + (xnxqVal+jxxzVal)+'&QZQM='+qzqmVal+'&kcksrq='+$('#kcKSRQ').val()+'&xxkksrq='+$('#xxkKSRQ').datebox('getValue'),
				async : false,
				dataType:"json",
				success : function(data) {
					$('#examDateDiv').dialog("close");
					showMsg(data[0].MSG);
				}
			});
		}
		
		function treegrid(listData){
			$('#classTree').tree({
				checkbox: false,
				url:'<%=request.getContextPath()%>/Svl_examSet?active=queryTree&level=0&parentId=""'+'&iUSERCODE='+iUSERCODE+'&GG_XNXQBM='+(xnxqVal+jxxzVal),
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
					treegrid();
				}
			});
		}
		
		//加载下拉框数据
		function xnxqCombobox(xnxq,jxxz,qzqm){
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
					checkpaike();
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
			
			$('#QZQM').combobox({
				data:qzqm,
				valueField:'comboValue',
				textField:'comboName',
				editable:false,
				panelHeight:'140', //combobox高度
				onLoadSuccess:function(data){
					//判断data参数是否为空
					if(data != ''){
						//初始化combobox时赋值
						$(this).combobox('setValue', data[0].comboValue);
						qzqmVal=data[0].comboValue;
						
					}
				},
				//下拉列表值改变事件
				onChange:function(data){
					checkpaike();
					doToolbar("que");
				}
			});
		}
		
		//工具按钮
		function doToolbar(id){
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
			if(id == 'importExam'){//导入考试信息
				KSZQDRCombobox();
				$('#DR_XNXQ').html($('#XNXQ').combobox('getText'));
				
				$('#importExamDialog').dialog("open");
				//checkksgl();
				//checkdabukao();//导入大补考
			}
			if(id == 'importTeacher'){//导入监考教师
				if($('#QZQM').combobox('getValue')=="4"){
					alertMsg("考试周期不能为全部");
					return;
				}
				var url="importType=teacher&xnxq="+($('#XNXQ').combobox('getValue')+$('#JXXZ').combobox('getValue'))+"&qzqm="+$('#QZQM').combobox('getValue')+"&xnxqtext="+$('#XNXQ').combobox('getText')+"&qzqmtext="+$('#QZQM').combobox('getText'); 
	   			url=encodeURI(url); //用了2次encodeURI 
				showDialog("<%=request.getContextPath()%>/form/examManage/importExamInfo.jsp?"+url,"导入监考教师");
			}
			if(id == 'importClassroom'){//导入考试教室
				if($('#QZQM').combobox('getValue')=="4"){
					alertMsg("考试周期不能为全部");
					return;
				}
				var url="importType=classroom&xnxq="+($('#XNXQ').combobox('getValue')+$('#JXXZ').combobox('getValue'))+"&qzqm="+$('#QZQM').combobox('getValue')+"&xnxqtext="+$('#XNXQ').combobox('getText')+"&qzqmtext="+$('#QZQM').combobox('getText'); 
	   			url=encodeURI(url); //用了2次encodeURI 
				showDialog("<%=request.getContextPath()%>/form/examManage/importExamInfo.jsp?"+url,"导入考试教室");
			}
			//考试信息保存
			if(id == "savesfks"){
				//alert(classnum+"|"+xnxqVal+"|"+jxxzVal+"|"+qzqm+"|");

				$.ajax({
					type : "POST",
					url : '<%=request.getContextPath()%>/Svl_examSet',
					data : 'active=savesfks&GG_SKJHMXBH=' + iKeyCode + '&GG_SFKS='+$('#GS_SFKS').combobox('getValue')+ '&GG_CDLX='+$('#GS_CDLX').combobox('getValue') + '&GG_KSZQ='+$('#GS_KSZQ').textbox('getValue') + '&GG_SJLX='+$('#GS_SJLX').combobox('getValue') + '&GG_KSXS='+$('#GS_KSXS').combobox('getValue') + '&GG_XNXQBM='+(xnxqVal+jxxzVal)+'&QZQM='+qzqmVal,
					dataType:"json",
					success : function(data) {
						showMsg(data[0].MSG);
						$('#win').dialog("close");
						initGridData(classnum,xnxqVal,jxxzVal,qzqmVal,"1");	
						loadGridAllKSXS();
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
							loadGridBUKAO();
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
							loadGridDABUKAO();
							$('#dbkksxs').dialog("close");
						}
					});
				}
			}
			if(id == "export"){//导出
				if(ksxsarray.length==0){
					alertMsg("请选择考试形式");
					return;
				}
				var ksxs="";
				for(var i=0;i<ksxsarray.length;i++){
					ksxs+="'"+ksxsarray[i]+"',";
				}
				ksxs=ksxs.substring(0, ksxs.length-1);
				$('#exportTimetable').dialog('open');
				var titleinfo=xnxqVal+","+jxxzVal+","+qzqmVal;
				$("#exportTimetableiframe").attr("src","<%=request.getContextPath()%>/form/timetableQuery/exportExamCourse.jsp?exportType=examCourse&xnxq=" + xnxqVal + "&jxxz=" + jxxzVal + "&qzqm=" + qzqmVal + "&ksxs=" + ksxs +"&titleinfo="+encodeURI(encodeURI(titleinfo)) );
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
				loadGrid2(BJMC_CX,KCMC_CX,ZYDM_CX);
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
				if(iKeyCode==""){
					alertMsg("请选择一行数据");
					return;
				}else{
					$('#GS_CDLX').combobox('setValue',cdlx);
					$('#GS_CDYQ').textbox('setValue',cdyq);
					$('#GS_KSXS').combobox('setValue',ksbh);
					$('#GS_KSZQ').combobox('setValue',kszq);
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
							data : 'active=addDBKinfo&GG_XNXQBM='+(xnxqVal+jxxzVal)+'&dbkarray='+(bkarray)+'&bktype='+bktype,
							dataType:"json",
							success : function(data) {
								alertMsg(data[0].MSG);
								bkarray.splice(0,bkarray.length);
								loadGridBUKAO();	
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
							data : 'active=addDBKinfo&GG_XNXQBM='+(xnxqVal+jxxzVal)+'&dbkarray='+(dbkarray)+'&bktype='+bktype,
							dataType:"json",
							success : function(data) {
								alertMsg(data[0].MSG);
								dbkarray.splice(0,dbkarray.length);
								loadGridDABUKAO();	
							}
						});
					}
				}
			}
			if(id == "newks"){//新建考试
				if(classId==""&&majorId!="9999999"&&majorId!="9999990"){
					alertMsg("请选择班级");
					return;
				}
				$('#newExam').dialog({   
						title: '新建考试'
				});
				$('#newExam').dialog("open");
				KCMCCombobox();
			}
			if(id == "saveNewExam"){//保存新建考试
				if(classnum==""){
					alertMsg("请选择班级或选修课");
					return;
				}
				$.ajax({
					type : "POST",
					url : '<%=request.getContextPath()%>/Svl_examSet',
					data : 'active=saveNewExam&GG_XZBDM='+classnum+'&XNXQ='+xnxqVal+'&JXXZ='+jxxzVal+'&QZQM='+qzqmVal+'&KCDM='+$('#GS_KCMC').combobox('getValue')+'&KCMC='+$('#GS_KCMC').combobox('getText'),
					dataType:"json",
					success : function(data) {
						showMsg(data[0].MSG);
						$('#newExam').dialog("close");
						initGridData(classnum,xnxqVal,jxxzVal,qzqmVal,"1");
						
					}
				});	
			}
			//添加
			if(id == 'addselect'){
				$('#ic_ksccapedit').show();
				$('#ic_ksccapedit').dialog("open");
				BJMC_CX = $('#BJMC_CX').textbox('getValue'); 
				KCMC_CX = $('#KCMC_CX').textbox('getValue');
				ZYDM_CX = $('#ZYDM_CX').combobox('getValue');
				loadGrid2(BJMC_CX,KCMC_CX,ZYDM_CX);
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
						loadGrid3();
					}
				});		
			}
			//未排课程
			if(id == "notcourse"){
				loadGridCOUR();
			}
			//未选考试形式
			/* if(id == "notksxs"){
				loadGridKSXS();
				$('#notKSXSDiv').dialog("open");
			} */
			if(id == "queAll"){
				loadGridAllKSXS();
			}
			if(id == "queDABUKAO"){
				loadGridDABUKAO();
			}
			//导入考试信息
			if(id == "importExamSubmit"){ 
				ConfirmMsg("导入前将删除所选时间段的考试信息，是否继续？", "checkksgl();","");
			}
			//所有考试形式
			if(id == "allksxs"){
				loadGridAllKSXS();
				$('#allKSXSDiv').dialog("open");
			}
			//补考
			if(id == "bukao"){
				bktype="bk";
				loadGridBUKAO();
				$('#bukaoDiv').dialog("open");
			}
			//大补考
			if(id == "dabukao"){
				bktype="dbk";
				loadGridDABUKAO();
				$('#dabukaoDiv').dialog("open");
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
		
			//导出未选考试形式
			if(id == "exportWXksxs"){
				$('#exportTimetable').dialog('open');
				$("#exportTimetableiframe").attr("src","<%=request.getContextPath()%>/form/timetableQuery/exportWXksxs.jsp?exportType=exportWXksxs&xnxq="+(xnxqVal+jxxzVal));
			}
			//导出答疑表
			if(id == "exportdbkDAYI"){
				$('#maskFont2').html('大补考答疑表导出中...');
    			$('#divPageMask4').show();
				ExportExcelDBKDY();
			}
			//导出大补考安排表
			if(id == "exportDABUKAO"){
				$('#maskFont2').html('大补考安排表导出中...');
    			$('#divPageMask4').show();
				ExportExcelDBKAP();
			}
			//设置考试日期
			if(id == "setExamDate"){
				$('#examDateDiv').dialog("open");
				loadKSRQ();
			}
			//保存考试日期
			if(id == "saveExamDate"){
				if($('#kcKSRQ').val()==""){
					alertMsg("请选择考试日期");
					return;
				}
				saveKSRQ();
			}	
		}
		
		function loadGridCOUR(){
			isLoad = true;
				$('#ncourtable').datagrid({
					url: '<%=request.getContextPath()%>/Svl_examSet',
		 			queryParams: {"active":"showNotCourse","GG_XNXQBM":xnxqVal+jxxzVal},
					loadMsg : "信息加载中请稍后!",//载入时信息
					height:433,
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
						{field:'学年学期编码',title:'学年学期',width:70,align:'center',
							formatter:function(value,rec){
								var xnxqbm=rec.学年学期编码;
								var xnxq=xnxqbm.substring(0,4)+"学年第"+xnxqbm.substring(4,5)+"学期";
								return xnxq;
							}
						},
						{field:'课程代码',title:'课程代码',width:60,align:'center'},
						{field:'课程名称',title:'课程名称',width:90,align:'center'},
						{field:'行政班名称',title:'行政班名称',width:120,align:'center'},
						{field:'上课周期',title:'上课周期',width:40,align:'center'}
					]],
					onSelect:function(rowIndex,rowData){
						if(isLoad == false){
							delweipaiarray.push(rowData.授课计划明细编号);
						}
					},
					onUnselect:function(rowIndex,rowData){
						$.each(delweipaiarray, function(key,value){
							if(value == rowData.授课计划明细编号){
								delweipaiarray.splice(key, 1);
							}
						});
					},
					onLoadSuccess: function(data){
						$(".datagrid-header-check").html('&nbsp;');//隐藏全选
						isLoad = false;
					},
					onLoadError:function(none){
						
					}
				});
				$('#notCourseDiv').dialog("open");
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
		 			queryParams: {"active":"showAllKSXS","GG_XNXQBM":xnxqVal+jxxzVal,"QZQM":qzqmVal,"GS_KCMC":$("#GS_KCMC_CX").val(),
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
						{field:'考试周期',title:'考试周期',width:60,align:'center',
							formatter:function(value,rec){
								var kszq="";
								if(rec.期中期末=='1'){
									kszq="前十周";
								}else if(rec.期中期末=='2'){
									kszq="前十五周";
								}else if(rec.期中期末=='3'){
									kszq="期末";						
								}
								return kszq;
							}
						},
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
						iKeyCode = rowData.授课计划明细编号;
						cdlx=rowData.场地类型;
						cdyq=rowData.场地要求;
						ksbh=rowData.编号;
						kszq=rowData.期中期末;
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
		function loadGridBUKAO(){
				isLoad = true;
				$('#bukaotable').datagrid({
					url: '<%=request.getContextPath()%>/Svl_examSet',
		 			queryParams: {"active":"showBUKAO","GG_XNXQBM":xnxqVal+jxxzVal,"QZQM":qzqmVal,"GS_KCMC":$("#BK_KCMC_CX").val(),
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
						{field:'学年学期',title:'学年学期',width:60,align:'center'},
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
							bkarray.push(rowData.授课计划明细编号+"#"+rowData.学年学期+"#"+rowData.课程名称+"#"+rowData.人数+"#"+rowData.考试形式+"#"+rowData.系名称+"#"+rowData.行政班简称+"#"+rowData.行政班名称);
						}
						
					},
					onUnselect:function(rowIndex,rowData){
						$.each(bkarray, function(key,value){
							if(value == (rowData.授课计划明细编号+"#"+rowData.学年学期+"#"+rowData.课程名称+"#"+rowData.人数+"#"+rowData.考试形式+"#"+rowData.系名称+"#"+rowData.行政班简称+"#"+rowData.行政班名称)){
								bkarray.splice(key, 1);
							}
						});
					},
					onLoadSuccess: function(data){
						$(".datagrid-header-check").html('&nbsp;');//隐藏全选
						isLoad = false;
						
						//页数赋值
						pageNum=data.rows[0].pageNum;
						pageSize=data.rows[0].pageSize;	
					},
					onLoadError:function(none){
						
					}
				});
		}
		
		//大补考查询
		function loadGridDABUKAO(){
				isLoad = true;
				$('#dabukaotable').datagrid({
					url: '<%=request.getContextPath()%>/Svl_examSet',
		 			queryParams: {"active":"showDABUKAO","GG_XNXQBM":xnxqVal+jxxzVal,"QZQM":qzqmVal,"GS_KCMC":$("#DA_KCMC_CX").val(),
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
						{field:'学年学期',title:'学年学期',width:60,align:'center'},
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
							dbkarray.push(rowData.授课计划明细编号+"#"+rowData.学年学期+"#"+rowData.课程名称+"#"+rowData.人数+"#"+rowData.考试形式+"#"+rowData.系名称+"#"+rowData.行政班简称+"#"+rowData.行政班名称);
						}
						
					},
					onUnselect:function(rowIndex,rowData){
						$.each(dbkarray, function(key,value){
							if(value == (rowData.授课计划明细编号+"#"+rowData.学年学期+"#"+rowData.课程名称+"#"+rowData.人数+"#"+rowData.考试形式+"#"+rowData.系名称+"#"+rowData.行政班简称+"#"+rowData.行政班名称)){
								dbkarray.splice(key, 1);
							}
						});
					},
					onLoadSuccess: function(data){
						$(".datagrid-header-check").html('&nbsp;');//隐藏全选
						isLoad = false;
						
						//页数赋值
						pageNum=data.rows[0].pageNum;
						pageSize=data.rows[0].pageSize;	
					},
					onLoadError:function(none){
						
					}
				});
		}
		
		//拆分信息
		function split(dbkinfo){
			ConfirmMsg("拆分这条信息，是否继续？", "splitDBKinfo(\""+dbkinfo+"\");","");
		}
		function splitDBKinfo(dbkinfo){
			if(bktype="bk"){
				$.ajax({
					type : "POST",
					url : '<%=request.getContextPath()%>/Svl_examSet',
					data : 'active=splitDBKinfo&dbkinfo='+dbkinfo+'&GG_XNXQBM='+(xnxqVal+jxxzVal)+'&bktype='+bktype,
					dataType:"json",
					success : function(data) {
						alertMsg(data[0].MSG);
						flag=0;
						loadGridBUKAO();
					}
				});
			}else{//bktype="dbk"
				$.ajax({
					type : "POST",
					url : '<%=request.getContextPath()%>/Svl_examSet',
					data : 'active=splitDBKinfo&dbkinfo='+dbkinfo+'&GG_XNXQBM='+(xnxqVal+jxxzVal)+'&bktype='+bktype,
					dataType:"json",
					success : function(data) {
						alertMsg(data[0].MSG);
						flag=0;
						loadGridDABUKAO();
					}
				});
			}
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
				data : 'active=checkksgl&XNXQ='+(xnxqVal+jxxzVal)+'&QZQM='+$("#DR_KSZQ").combobox('getValue'),
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
		function initData(){
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_CourseSet',
				data : 'active=initData&page=' + pageNum + '&rows=' + pageSize,
				dataType:"json",
				success : function(data) { 
					initCombobox(data[0].kclxData, data[0].zydmData, data[0].ksxsData, data[0].dbkksxsData);
				}
			});
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
					{field:'课程名称',title:'科目名称',width:140},
					{field:'考试形式',title:'考试形式',width:60},
					{field:'试卷类型',title:'试卷类型',width:60},
					{field:'名称',title:'场地类型',width:80},
					{field:'上课周期',title:'上课周期',width:60},
					{field:'期中期末',title:'考试周期',width:60,
						formatter:function(value,rec){
							var kszq="";
							if(rec.期中期末=='1'){
								kszq="前十周";
							}else if(rec.期中期末=='2'){
								kszq="前十五周";
							}else if(rec.期中期末=='3'){
								kszq="期末";						
							}
							return kszq;
						}
					},
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
			examid=id;
			examks=ks;
			$('#ic_ksccap').show();
			$('#ic_ksccap').dialog("open");	
			examinfoidarray.splice(0, examinfoidarray.length);
			loadGrid3();
		}
		
		function openckxs(id){ 
			examid=id;
			$('#ic_cjksxs').show();
			$('#ic_cjksxs').dialog("open");	
			loadGrid5();
		}
				
		function initDialog(){
						
			$('#ic_ksccap').dialog({   
				title: '设置同时开考科目',   
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
			
			$('#ic_ksccapedit').dialog({   
				title: '添加考试场次安排',   
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
					emptyic_ksccapedit();
				}
			});
			
			$('#ic_cjksxs').dialog({   
				title: '参加考试学生',   
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
			
			$('#win').dialog({
				width : 350,
				height: 189,
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
					
			$('#dbkksxs').dialog({
				width : 300,
				height: 139,
				modal : true,
				closed : true,
				onOpen : function(data) {
					$('#dbkksxs').find('table').css('display', 'block');
				},
				onLoad : function(data) {
				},
				onClose : function(data) {
					emptyDialogDBK();//初始化信息
	
				}
			});
			
			$('#newExam').dialog({
				width : 350,
				height: 89,
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
					//clsinfoidarray.splice(0,teainfoidarray.length);
					//clsinfoarray.splice(0,teainfoarray.length);
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
			
			$('#notCourseDiv').dialog({   
				title: '未排课程',   
				width: 900,//宽度设置   
				height: 500,//高度设置 
				modal:true,
				closed: true,   
				cache: false, 
				draggable:true,//是否可移动dialog框设置
				//读取事件
				onLoad:function(data){},
				//关闭事件
				onClose:function(data){
					//clsinfoidarray.splice(0,teainfoidarray.length);
					//clsinfoarray.splice(0,teainfoarray.length);
				}
			});
			
			$('#examDateDiv').dialog({   
				title: '设置考试日期',   
				width: 400,//宽度设置   
				height: 120,//高度设置 
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
			
			$('#allKSXSDiv').dialog({   
				title: '所有考试形式',   
				width: 1000,//宽度设置   
				height: 560,//高度设置 
				modal:true,
				closed: true,   
				cache: false, 
				draggable:true,//是否可移动dialog框设置
				//读取事件
				onLoad:function(data){},
				//关闭事件
				onClose:function(data){
					$("#GS_KCMC_CX").textbox('setValue','');
					$("#GS_XZBMC_CX").textbox('setValue','');
					$("#GS_ZYMC_CX").combobox('setValue','');
					$("#GS_KSXS_CX").combobox('setValue','-1');
				}
			});
			
			$('#bukaoDiv').dialog({   
				title: '补考信息',   
				width: 1000,//宽度设置   
				height: 560,//高度设置 
				modal:true,
				closed: true,   
				cache: false, 
				draggable:true,//是否可移动dialog框设置
				//读取事件
				onLoad:function(data){},
				//关闭事件
				onClose:function(data){
					$("#GS_KCMC_CX").textbox('setValue','');
					$("#GS_XZBMC_CX").textbox('setValue','');
					$("#GS_ZYMC_CX").combobox('setValue','');
					$("#GS_KSXS_CX").combobox('setValue','-1');
					bkarray.splice(0, bkarray.length);
					pageNum=1;
				}
			});
			
			$('#dabukaoDiv').dialog({   
				title: '大补考信息',   
				width: 1000,//宽度设置   
				height: 560,//高度设置 
				modal:true,
				closed: true,   
				cache: false, 
				draggable:true,//是否可移动dialog框设置
				//读取事件
				onLoad:function(data){},
				//关闭事件
				onClose:function(data){
					$("#GS_KCMC_CX").textbox('setValue','');
					$("#GS_XZBMC_CX").textbox('setValue','');
					$("#GS_ZYMC_CX").combobox('setValue','');
					$("#GS_KSXS_CX").combobox('setValue','-1');
					dbkarray.splice(0, dbkarray.length);
					pageNum=1;
				}
			});
			
			$('#ksxsInfo').dialog({   
				width: 300,//宽度设置   
				height: 494,//高度设置 
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
						doToolbar("export");
					}
				}],
				//打开事件
				onOpen:function(data){},
				//读取事件
				onLoad:function(data){},
				//关闭事件
				onClose:function(data){
					ksxsarray.splice(0,ksxsarray.length);
				}
			});
			
			$('#importExamDialog').dialog({   
				width: 250,//宽度设置   
				height: 118,//高度设置 
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
			loadGrid3();
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
			$('#GS_KCMC').combobox({
				url:"<%=request.getContextPath()%>/Svl_examSet?active=KCMCCombobox&GG_XZBDM=" + classnum+"&GG_XNXQBM=" + xnxqVal+jxxzVal,
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
		
		function KSZQCombobox(){
			$('#GS_KSZQ').combobox({
				url:"<%=request.getContextPath()%>/Svl_examSet?active=KSZQCombobox",
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
		
		/**加载combobox控件
			@jxxzData 下拉框数据
		**/
		function initCombobox(kclxData, zydmData, ksxsData, dbkksxsData){
			$('#KCLX').combobox({
				data:kclxData,
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
					if(saveType=='new'){
						if(data=="01"){
							$('#ZYDM').combobox('setValue', '');
							$('#ZYDM').combobox("disable");
						}else if(data=="02"){
							$('#ZYDM').combobox("enable");
						}else{
							$('#ZYDM').combobox("disable");
						}
					}
				}
			});
			
			$('#ZYDM_CX').combobox({
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
	
			$('#ZYDM').combobox({
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
			
			//加载下拉框数据
			$('#GS_KSXS').combobox({
				data:ksxsData,
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
				}
			});	
			//加载下拉框数据
			 $('#GS_KSXS_CX').combobox({
				data:ksxsData,
				valueField:'comboValue',
				textField:'comboName',
				editable:false,
				panelHeight:'140', //combobox高度
				onLoadSuccess:function(data){
					//判断data参数是否为空
					if(data != ''){
						//初始化combobox时赋值
						$(this).combobox('setValue', '-1');
					}
				}
			}); 
			 $('#GS_ZYMC_CX').combobox({
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
			
			//补考
			$('#BK_KSXS_CX').combobox({
				data:dbkksxsData,
				valueField:'comboValue',
				textField:'comboName',
				editable:false,
				panelHeight:'140', //combobox高度
				onLoadSuccess:function(data){
					//判断data参数是否为空
					if(data != ''){
						//初始化combobox时赋值
						$(this).combobox('setValue', '-1');
					}
				}
			}); 
			
			//大补考
			$('#DA_KSXS_CX').combobox({
				data:dbkksxsData,
				valueField:'comboValue',
				textField:'comboName',
				editable:false,
				panelHeight:'140', //combobox高度
				onLoadSuccess:function(data){
					//判断data参数是否为空
					if(data != ''){
						//初始化combobox时赋值
						$(this).combobox('setValue', '-1');
					}
				}
			});  
			
			$('#DA_KSXS').combobox({
				data:dbkksxsData,
				valueField:'comboValue',
				textField:'comboName',
				editable:false,
				panelHeight:'140', //combobox高度
				onLoadSuccess:function(data){
					//判断data参数是否为空
					if(data != ''){
						//初始化combobox时赋值
						//$(this).combobox('setValue', '-1');
					}
				}
			});
		}
		
			
		/**加载 datagrid控件，读取页面信息
			@listData 列表数据
		**/
		//添加删除合班考试
		function loadGrid2(BJMC_CX,KCMC_CX,ZYDM_CX){
			$('#courseList2').datagrid({
				url : '<%=request.getContextPath()%>/Svl_examSet?active=listkskc&XNXQ='+xnxqVal+'&JXXZ='+jxxzVal+'&QZQM='+examks+'&BJMC_CX='+encodeURI(BJMC_CX)+'&KCMC_CX='+encodeURI(KCMC_CX)+'&ZYDM_CX='+ZYDM_CX+'&examid='+examid,
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
		function loadGrid3(){
			$('#courseList3').datagrid({
				url : '<%=request.getContextPath()%>/Svl_examSet?active=listtskc&XNXQ='+xnxqVal+'&JXXZ='+jxxzVal+'&QZQM='+examks+'&examid='+examid,
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
					//loadGrid2(data[0].listData);
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
		
		/**表单提交**/
		$('#form1').form({
			//定位到servlet位置的url
			url:'<%=request.getContextPath()%>/Svl_CourseSet',
			//当点击事件后触发的事件
			onSubmit: function(data){
				return $(this).form('validate');//验证
			}, 
			//当点击事件并成功提交后触发的事件
			success:function(data){
				var json = eval("("+data+")");
				
				if(json[0].MSG == '保存成功'){
					showMsg(json[0].MSG);
					$("#active").val('');
					
					emptyDialog();//清空并关闭dialog
					loadData();
					$('#courseInfo').dialog('close');
				}else{
					alertMsg(json[0].MSG);
				}
			}
		});
		
		//查询考试形式
		function selectKSXS(){
				isLoad = true;
				$('#ksxstable').datagrid({
					url: '<%=request.getContextPath()%>/Svl_examSet',
		 			queryParams: {"active":"selectKSXS"},
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
						{field:'编号',title:'编号',width:fillsize(0.5),align:'center'},
						{field:'考试形式',title:'考试形式',width:fillsize(0.5),align:'center'}
					]],
					onSelect:function(rowIndex,rowData){
						if(isLoad == false){
							ksxsarray.push(rowData.编号);
						}
					},
					onUnselect:function(rowIndex,rowData){
						$.each(ksxsarray, function(key,value){
							if(value == rowData.编号){
								ksxsarray.splice(key, 1);
							}
						});
					},
					onSelectAll:function(rows){
						for(var i=0;i<rows.length;i++){
							ksxsarray.push(rows[i].编号);
						}
					},
					onUnselectAll:function(rows){
						ksxsarray.splice(0,ksxsarray.length);
					},
					onLoadSuccess: function(data){
						//$(".datagrid-header-check").html('&nbsp;');//隐藏全选
						//$(".datagrid-header-check").children("input[type='checkbox']").eq(0).attr("checked", true);		
						isLoad = false;
					},
					onLoadError:function(none){
						
					}
				});
				
			$('#ksxsInfo').dialog('open');			
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
				data : 'active=ExportExcelDBKDY&GG_XNXQBM='+(xnxqVal+jxxzVal),
				dataType:"json",
				success : function(data) {
					$('#divPageMask4').hide();
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
				data : 'active=ExportExcelDBKAP&GG_XNXQBM='+(xnxqVal+jxxzVal),
				dataType:"json",
				success : function(data) {
					$('#divPageMask4').hide();
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
