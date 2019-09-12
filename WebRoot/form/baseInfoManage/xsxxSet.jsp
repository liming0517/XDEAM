<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="java.util.Vector"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.src.develop.store.user.*"%>
<%
	/**
		创建人：MaoWei
		Create date: 2016.07.12
		功能说明：学生信息设置
		页面类型:列表及模块入口
		修订信息(有多次时,可有多个)
		修订日期:2017.03.22
		原因:导出功能添加字段，并测试其他功能
		修订人:YangDa
		修订时间:
	**/
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
    <title>学生信息设置</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> 
	<!-- JQuery 专用4个文件 -->
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/icon.css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
    <!-- JQuery 文件导入结束 -->	
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/publicScript.js"></script>
	<style>
		.inputWidth{
			width:160px
		}
		
		.inputWidthLong{
			width:470px
		}
		
		#uploadDiv{
			position:relative;
			width:56px;
			height:26px;
			border:1px solid #ffffff;
			background-color:#ffffff;
			cursor:pointer;
			overflow:hidden;
		}
		
		#File1{
			height:24px;
			position:absolute;
			z-index:99;
			top:0;
			left:-170;
			opacity: 0; 
			filter: "alpha(opacity=0)"; 
			filter: alpha(opacity=0); 
			-moz-opacity: 0;
			cursor:pointer;
		}
		
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
		#maskFont{
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
		#maskFont1{
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
    <div title="学生信息" region="north"  style="height:116px; overflow:hidden;">
    	<form method="post" id="form2" name="form2">
			<table>
				<tr>
					<td><a href="#" onclick="doToolbar(this.id);" id="new" class="easyui-linkbutton" plain="true" iconCls="icon-new">新建</a></td>
					<td><a href="#" onclick="doToolbar(this.id);" id="edit" class="easyui-linkbutton" plain="true" iconCls="icon-edit">编辑</a></td>
					<td><a href="#" onclick="doToolbar(this.id);" id="del" class="easyui-linkbutton" plain="true" iconCls="icon-cancel">删除</a></td>
					<td><a href="<%=request.getContextPath()%>/form/baseInfoManage/StuInfoTemplet.xls" id="export" class="easyui-linkbutton" plain="true" iconcls="icon-submit">模板导出</a></td>
					<td>
						<div id="uploadDiv">
							<a href="#" id="import" class="easyui-linkbutton" plain="true" iconCls="icon-submit" style="">导入</a>
								<input type="file" id="File1" onchange="doToolbar(this.id)" name="File1" style="display:none;"
									onmouseenter="$('#uploadDiv').css('border','1px solid #B7D2FF'); $('#uploadDiv').css('background-color','#E9F1FF');" 
									onmouseleave="$('#uploadDiv').css('border','1px solid #ffffff'); $('#uploadDiv').css('background-color','#ffffff');"/>
						</div>
					</td>
					<td><a href="#" onclick="doToolbar(this.id)" id="exportdialog" class="easyui-linkbutton" plain="true" iconcls="icon-submit">导出</a></td>
					<td><a href="#" onclick="doToolbar(this.id);" id="abn" class="easyui-linkbutton" plain="true" iconCls="icon-pyap">异动信息</a></td>
					<td><a id="uploadAllstuPhoto" onclick="doToolbar(this.id)" class="easyui-linkbutton" href="javascript:void(0);" data-options="plain:true,iconCls:'icon-site'">批量上传学生照片</a></td>
				</tr>
			</table>
			<table id="tabl1" class="tablestyle" name="tabl1" width="100%">
	   			<tr>
	   				<td class="titlestyle">学籍号</td>
			   		<td>
			   			<input class="easyui-textbox" id="JX_XJH_CX" name="JX_XJH_CX" style="width:153px;"/>
					</td>
					<!-- <td class="titlestyle">学籍号</td>
			   		<td>
			   			<input class="easyui-textbox" id="JX_XJH_CX" name="JX_XJH_CX"/>
					</td> -->
					<td class="titlestyle">学号</td>
			   		<td>
			   			<input class="easyui-textbox" id="JX_XH_CX" name="JX_XH_CX" style="width:153px;"/>
					</td>
					<td class="titlestyle">姓名</td>
			   		<td>
			   			<input class="easyui-textbox" id="JX_XM_CX" name="JX_XM_CX" style="width:153px;"/>
					</td>
	   				<td class="titlestyle">行政班名称</td>
			   		<td>
			   			<select class="easyui-combobox" id="JX_XZBDM_CX" name="JX_XZBDM_CX" style="width:153px;">
					   	</select>
					</td>
				</tr>
				<tr>
					<td class="titlestyle">系部名称</td>
			   		<td>
			   			<select class="easyui-combobox" id="JX_XBDM_CX" name="JX_XBDM_CX" style="width:153px;">
				   		</select>
					</td>
					<td class="titlestyle">专业名称</td>
			   		<td>
			   			<select class="easyui-combobox" id="JX_ZYDM_CX" name="JX_ZYDM_CX" style="width:153px;">
				   		</select>
					</td>
					<td class="titlestyle">年级名称</td>
			   		<td>
			   			<select class="easyui-combobox" id="JX_NJDM_CX" name="JX_NJDM_CX" style="width:153px;">
				   		</select>
					</td>
					<td colspan="2">
						<a href="#" id="que" class="easyui-linkbutton" plain="true" iconCls="icon-search" onclick="doToolbar(this.id);">查询</a>
					</td>
					<!-- <td>&nbsp;</td> -->
	   			</tr>
	   		</table>
	   		<input type="hidden" id="active2" name="active2"/>
   			<input type="hidden" name="sxpath" id="sxpath"/>
	   	</form>
   	</div>
   	<%-- 遮罩层 --%>
    	<div id="divPageMask" class="maskStyle">
    		<div id="maskFont"></div>
    	</div>
   	<div region="center">
   		<table id="XSXXList" name="XSXXList"></table>
   	</div>
	 
	<!--  dialog -->
	<div id="DIA_Lead" style="overflow:hidden;">
		<form method="post" id="form1" name="form1">
			<div id="tt" class="easyui-tabs" style="width:586px; height:368px; background:#efefef;">
				<div title="基础信息" style="background-color:#EFEFEF;">
					<table id="stuBaseInfo" class="tablestyle" style="width:100%;">
						<tr>
				   			<td class="titlestyle" style="width:80px;">
				   				姓名<font style="color:red;">&nbsp;*</font>
				   			</td>
				   			<td> 
				   				<input id="JX_XM" name="JX_XM" class="easyui-textbox" required="true"/>
				   			</td>
				   			<td class="titlestyle" style="width:140px;" rowspan="10">
				   				照片<br/><br/>格式<br/> png,gif,jpeg,jpg,bmp<br/><br/>规格 240px*320px
				   				<br/>
				   				<br/>
				   				<br/>
				   				<a href="#" onclick="doToolbar(this.id);" id="delZP" class="easyui-linkbutton" plain="true" iconCls="icon-cancel">删除照片</a>
				   			</td>
				   			<td rowspan="10" style="padding-left:40px;">
				   				<div id="Img">
				   					<img style="width:90px; height:115px;" src="<%=request.getContextPath()%>/images/nopic1.png"/>
				   				</div>
				   			 </td>
				   		</tr>
				   		<tr>
				   			<td class="titlestyle">
				   				学籍号<font style="color:red;">&nbsp;*</font>
				   			</td>
				   			<td>
				   				<input id="JX_XJH" name="JX_XJH" class="easyui-textbox" required="true"/>
				   			</td>
				   		</tr>
				   		<tr>
				   			<td class="titlestyle">
				   				学号<font style="color:red;">&nbsp;*</font>
				   			</td>
				   			<td>
				   				<input id="JX_XH" name="JX_XH" class="easyui-textbox" required="true"/>
				   			</td>
				   		</tr>
				   		<tr>
				   			<td class="titlestyle">拍照号</td>
				   			<td>
				   				<input id="JX_PZH" name="JX_PZH" class="easyui-textbox"/>
				   			</td>
				   		</tr>	
				   		<!-- <tr>
				   			<td class="titlestyle">学生考号</td>
				   			<td>
				   				<input id="JX_ZKZH" name="JX_ZKZH" class="easyui-textbox"/>
				   			</td>
				   		</tr> -->
				   		<tr>
				   			<td class="titlestyle">行政班</td>
				   			<td>
					   			<select class="easyui-combobox" id="JX_XZBDM" name="JX_XZBDM" style="width:153px;">
					   			</select>
				   			</td>
				   		</tr>
				   		<tr>
				   			<td class="titlestyle">班内学号</td>
				   			<td>
					   			<input id="JX_BNXH" name="JX_BNXH" class="easyui-textbox"/>
				   			</td>
				   		</tr>
				   		<tr>
				   			<td class="titlestyle">专业名称</td>
				   			<td>
				   				<select class="easyui-combobox" id="JX_XZZYM" name="JX_XZZYM" style="width:153px;">
				   				</select>
				   			</td>
				   		</tr>
				   		<tr>
				   			<td class="titlestyle">姓名拼音</td>
				   			<td>
				   				<input id="JX_XMPY" name="JX_XMPY" maxlength="20" class="easyui-textbox"/>
				   			</td>
				   		</tr>
				   		<tr>
				   			<td class="titlestyle">曾用名</td>
				   			<td>
				   				<input id="JX_CYM" name="JX_CYM" maxlength="10" class="easyui-textbox"/>
				   			</td>
				   		</tr>
				   		<tr>
				   			<td class="titlestyle">招生类别</td>
				   			<td>
				   				<select id="JX_ZSLBM" name="JX_ZSLBM" class="easyui-combobox" style="width:153px;">
							    </select>
				   			</td>
				   		</tr>
				   		<tr>
				   			<td class="titlestyle">性别</td>
				   			<td>
				   				<select id="JX_XBM" name="JX_XBM" class="easyui-combobox" style="width:153px;">
							    </select>
				   			</td>
				   			<td colspan="2" >
				   				<a id="uplodAA"> 请先输入身份证件号 </a>
				   				<input type="file" class="inputstyle" accept="image/*" style="width:250px;"  id="JX_ZPLJ" name="JX_ZPLJ"  onchange="doToolbar('upload');"/>
				   				
				   			</td> 
				   		</tr>
					</table>
				</div>
				<div title="扩展信息" style="background-color:#EFEFEF;">
					<table id="stuExtendInfo" class="tablestyle" style="width:100%; height:100%;">
						<tr>
							<td class="titlestyle">出生日期</td>
				   			<td>
				   				<input id="JX_CSNY" name="JX_CSNY" class="easyui-datebox inputWidth" editable="false"/>
				   			</td>
				   			<td class="titlestyle" style="width:110px;">入学日期</td>
				   			<td style="width:178px;">
				   				<input id="JX_RXRQ" name="JX_RXRQ" class="easyui-datebox inputWidth" editable="false"/>
				   			</td>
						</tr>
				   		<tr>
				   			<td class="titlestyle">身份证件类型</td>
				   			<td>
				   				<select id="JX_SFZJLXM" name="JX_SFZJLXM"  class="easyui-combobox inputWidth">
				   				</select>
				   			</td>
				   			<td class="titlestyle">身份证件号</td>
				   			<td>
				   				<input id="JX_SFZJH" name="JX_SFZJH" class="easyui-textbox inputWidth" />
				   			</td>
				   		</tr>
				   		<tr>
				   			<td class="titlestyle" style="width:100px;">学生类别</td>
				   			<td style="width:188px;">
				   				<select id="JX_XSLBM" name="JX_XSLBM" class="easyui-combobox inputWidth">
								</select>
				   			</td>
				   			<td class="titlestyle">港澳台侨外</td>
				   			<td>
				   				<select id="JX_GATQM" name="JX_GATQM" class="easyui-combobox inputWidth">
							   </select>
				   			</td>
				   		</tr>
				   		<tr>
				   			<td class="titlestyle">民族</td>
				   			<td>
				   				<select id="JX_MZM" name="JX_MZM" class="easyui-combobox inputWidth">
								</select>
				   			</td>
				   				<td class="titlestyle">血型</td>
				   			<td>
				   				<select id="JX_XXM" name="JX_XXM" class="easyui-combobox inputWidth">
							   </select>
				   			</td>
				   		</tr>
				   		<tr>
				   			<td class="titlestyle">政治面貌</td>
				   			<td> 
				   				<select id="JX_ZZMMM" name="JX_ZZMMM" class="easyui-combobox inputWidth"> 
							   </select>
				   			</td>
				   			<td class="titlestyle">国籍</td>
				   			<td>
				   				<select id="JX_GBM" name="JX_GBM" class="easyui-combobox inputWidth">
							   </select>
				   			</td>
				   		</tr>
				   		<tr>
				   			<td class="titlestyle">健康状况</td>
				   			<td>
				   				<select id="JX_JKZKM" name="JX_JKZKM" class="easyui-combobox inputWidth">
							   </select>
				   			</td>
				   			<td class="titlestyle">籍贯</td>
				   			<td>
				   				<select id="JX_JGM" name="JX_JGM" class="easyui-combobox inputWidth">
							   </select>
				   			</td>
				   		</tr>
				   		<tr>
				   			<td class="titlestyle">婚姻状况</td>
				   			<td>
				   				<select id="JX_HYZK" name="JX_HYZK" class="easyui-combobox inputWidth">
							   </select>
				   			</td>
				   			<td class="titlestyle">出生地</td>
				   			<td>
				   				<select id="JX_CSDZM" name="JX_CSDZM" class="easyui-combobox inputWidth">
							   </select>
				   			</td>
				   		</tr>
				   		<tr>
				   			<td class="titlestyle">户籍地址</td>
				   			<td colspan="3">
				   				<input id="JX_HKSZD" name="JX_HKSZD" class="easyui-textbox inputWidthLong"/>
				   			</td>
				   		</tr>
				   		<tr>
				   			<td class="titlestyle">户籍邮编</td>
				   			<td >
				   				<input id="JX_YZBM" name="JX_YZBM" class="easyui-textbox inputWidth"/>
				   			</td>
				   			<td class="titlestyle">户口类别</td>
				   			<td>
				   				<select id="JX_HKLBM" name="JX_HKLBM" class="easyui-combobox inputWidth">
								</select>
				   			</td>
				   		</tr>
				   		<tr>
				   			<td class="titlestyle">流动人口状况</td>
				   			<td>
				   				<select id="JX_LDRKZK" name="JX_LDRKZK" class="easyui-combobox inputWidth"> 
								</select>
				   			</td>
				   			<td class="titlestyle">所属派出所</td>
				   			<td>
				   				<input id="JX_SSPCS" name="JX_SSPCS" class="easyui-textbox inputWidth"/>
				   			</td>
				   		</tr>
				   		<tr>
				   			<td class="titlestyle">毕业学校</td>
				   			<td>
				   				<input id="JX_BYXX" name="JX_BYXX" class="easyui-textbox inputWidth"/>
				   			</td>
				   			<td class="titlestyle">毕业年份</td>
				   			<td>
				   				<input id="JX_BYNF" name="JX_BYNF" class="easyui-textbox inputWidth"/>
				   			</td>
				   		</tr>
				   		<tr>
				   			<td class="titlestyle">备注</td>
				   			<td colspan="3">
				   				<input id="JX_BZ" name="JX_BZ" class="easyui-textbox inputWidthLong"/>
				   			</td>
				   		</tr>
					</table>
				</div>
				<div title="家长信息" style="background-color:#EFEFEF;">
					<table id="stuParentsInfo" class="tablestyle" style="width:100%;">
						<tr>
				   			<td class="titlestyle" style="width:110px;">父亲姓名</td>
				   			<td>
				   				<input id="JX_FQXM" name="JX_FQXM" class="easyui-textbox inputWidth"/>
				   			</td>
				   			<td class="titlestyle" style="width:110px;">父亲年龄</td>
				   			<td>
				   				<input id="JX_FQNL" name="JX_FQNL" class="easyui-textbox inputWidth"/>
				   			</td>
				   		</tr>
				   		<tr>
				   			<td class="titlestyle">父亲政治面貌</td>
				   			<td>
				   				<select id="JX_FQZZMMM" name="JX_FQZZMMM" class="easyui-combobox inputWidth"> 
							   </select>
				   			</td>
				   			<td class="titlestyle">父亲电话</td>
				   			<td>
				   				<input id="JX_FQDH" name="JX_FQDH" class="easyui-textbox inputWidth"/> 
				   			</td>
				   		</tr>
				   		<tr>
				   			<td class="titlestyle">父亲工作单位</td>
				   			<td  colspan="3">
				   				<input id="JX_FQGZDW" name="JX_FQGZDW" style="width:445px;" class="easyui-textbox"/>
				   			</td>
				   			
				   		</tr>
				   		<tr>
				   			<td class="titlestyle">母亲姓名</td>
				   			<td>
				   				<input id="JX_MQXM" name="JX_MQXM" class="easyui-textbox inputWidth"/>
				   			</td>
				   			<td class="titlestyle">母亲年龄</td>
				   			<td>
				   				<input id="JX_MQNL" name="JX_MQNL" class="easyui-textbox inputWidth"/>
				   			</td>
				   		</tr>
				   		<tr>
				   			<td class="titlestyle">母亲政治面貌</td>
				   			<td>
				   				<select id="JX_MQZZMMM" name="JX_MQZZMMM" class="easyui-combobox inputWidth"> 
							   </select>
				   			</td>
				   			<td class="titlestyle">母亲电话</td>
				   			<td>
				   				<input id="JX_MQDH" name="JX_MQDH" class="easyui-textbox inputWidth"/> 
				   			</td>
				   		</tr>
				   		<tr>
				   			<td class="titlestyle">母亲工作单位</td>
				   			<td  colspan="3">
				   				<input id="JX_MQGZDW" name="JX_MQGZDW" style="width: 445px;" class="easyui-textbox"/>
				   			</td>
				   		</tr>
				   		
				   		<tr>
				   			<td class="titlestyle">其他成员称谓</td>
				   			<td>
				   				<input id="JX_QTCYCW" name="JX_QTCYCW" class="easyui-textbox inputWidth"/>
				   			</td>
				   			<td class="titlestyle">其他成员姓名</td>
				   			<td>
				   				<input id="JX_QTCYXM" name="JX_QTCYXM" class="easyui-textbox inputWidth"/>
				   			</td>
				   		</tr>
				   		<tr>
				   			<td class="titlestyle">其他成员政治面貌</td>
				   			<td>
				   				<select id="JX_QTCYZZMMM" name="JX_QTCYZZMMM" class="easyui-combobox inputWidth"> 
							   </select>
				   			</td>
				   			<td class="titlestyle">其他成员年龄</td>
				   			<td>
				   				<input id="JX_QTCYNL" name="JX_QTCYNL" class="easyui-textbox inputWidth"/>
				   			</td>
				   		</tr>
				   		<tr>
				   			<td class="titlestyle">其他成员工作单位</td>
				   			<td>
				   				<input id="JX_QTCYGZDW" name="JX_QTCYGZDW" class="easyui-textbox inputWidth"/>
				   			</td>
				   			<td class="titlestyle">其他成员电话</td>
				   			<td>
				   				<input id="JX_QTCYDH" name="JX_QTCYDH" class="easyui-textbox inputWidth"/> 
				   			</td>
				   		</tr>
					</table>
				</div>
			</div>
		
		   	<input type="hidden" id="active" name="active" />
		   	<input type="hidden" id="invoke" name="invoke" />
		   	<input type="hidden" id="JX_XZBJ" name="JX_XZBJ" />
		   	<input type="hidden" id="path" name="path" />
		   	<input type="hidden" id="JX_ZP" name="JX_ZP" />
		   	<input id="JX_YXJH" name="JX_YXJH" type="hidden"/>
			<input id="JX_YXH" name="JX_YXH" type="hidden"/>
			<input id="JX_YXM" name="JX_YXM" type="hidden"/>
		</form>
	</div>
	
	<!-- dialog 异动信息 -->
	 <div id="DIA_Abn" style="overflow:hidden;" title="异动信息">
	 	<div class="easyui-layout" style="height:100%; width:100%;">
		 	<div region="north" style="height:32px;">
		 		<table id="tabl1" class="tablestyle" name="tabl1" width="100%">
		   			<tr>
		   				<td class="titlestyle">学号</td>
				   		<td>
				   			<!-- <input class="easyui-textbox" id="JX_KH_Abn" maxlength="10" style="width:180px;"/> -->
				   			<input class="easyui-textbox" id="JX_XH_Abn" maxlength="10" style="width:180px;"/>
						</td>
		   				<td class="titlestyle" >姓名</td>
				   		<td>
				   			<input id="JX_XM_Abn" name="JX_XM_Abn" maxlength="10" class="easyui-textbox"  >
						</td>
						<td colspan="2" style="text-align:center;">
							<a href="#" id="que_Abn" class="easyui-linkbutton" plain="true" iconCls="icon-search" onclick="doToolbar(this.id);">查询</a>
						</td>
		   			</tr>
	   			</table>
		 	</div>
		 	<div region="center">
	   			<table id="XSXXList_Abn" name="XSXXList_Abn"></table>
	   		</div>
   		</div>
	 </div>
	 
	 	<!-- dialog info -->
	 <div id="DIA_yDmsg" style="overflow:hidden;" title="异动信息">
	 	<div class="easyui-layout" style="height:100%; width:100%;">
		 	<div region="center">
	   			<table id="yDmsg" name="yDmsg"></table>
	   		</div>
   		</div>
	 </div>
	 
	 <!-- 异动处理 -->
	 <div id="Abndialog" style="overflow:hidden;">
		<table id="AbnList" class="tablestyle" style="width:100%;">
			<tr>
				<td style="width:95px;" class="titlestyle">学号</td>
				<td style="width:200px;" class="titlestyle" id="stuXH"><!-- id="stuCode" -->
				</td>
				<td style="width:95px;" class="titlestyle">姓名</td>
				<td class="titlestyle" id="stuName">
				</td>
			</tr>
			<tr>
				<td class="titlestyle">异动类型</td>
				<td>
					<select id="JX_YDLXM" name="JX_YDLXM" class="easyui-combobox" style="width:190px;" editable="false" panelHeight="auto">
			   		 </select>
			   	</td>
			   	<td class="titlestyle">异动日期</td>
				<td>
					<input id="JX_YDRQ" name="JX_YDRQ" class="easyui-datebox inputWidth" style="width:190px;" editable="false">
				</td>
			</tr>
			<tr>
				<!-- 
				<td class="titlestyle">异动来源学校</td>
				<td id="JX_YXQ" class="titlestyle" id="stuXH"></td>
				 -->
				<td class="titlestyle">异动去向学校</td>
				<td colspan="3">
					<input id="JX_NEWXQ" name="JX_NEWXQ" class="easyui-textbox" style="width:190px;"/>
		    	</td>
			</tr>
			<!-- 
			<tr>
				<td class="titlestyle">原系名称</td>
				<td id="JX_YXMC" class="titlestyle" id="stuXH" style="width: 200px;"></td>
				<td style="width:120px;" class="titlestyle">新系名称</td>
				<td >
					<select id="JX_NEWX" name="JX_NEWX" class="easyui-combobox" style="width:160px;"  disable="true" panelHeight="auto">
		    		</select>
		    	</td>
			</tr>
			 -->
			<tr>
				<td class="titlestyle">原专业</td>
				<td id="JX_YZYMC" class="titlestyle"></td>
				<td class="titlestyle">新专业</td>
				<td >
					<select id="JX_NEWZY" name="JX_NEWZY" class="easyui-combobox" style="width:190px;"  disable="true" panelHeight="auto">
		    		</select>
		    	</td>
			</tr>
			<tr>
				<td class="titlestyle">原班级</td>
				<td id="JX_YLBJ" class="titlestyle"></td>
				<td class="titlestyle">新班级</td>
				<td >
					<select id="JX_NEWBJ" name="JX_NEWBJ" class="easyui-combobox" style="width:190px;"  disable="true" panelHeight="auto">
		   			</select>
		   		</td>
			</tr>
			<tr>
				<td class="titlestyle">异动原因</td>
				<td colspan="3">
					<textarea id="JX_YDYY" style="width:509px" onpropertychange="if(value.length>500) value=value.substr(0,500)" class="smallArea" cols="40" name="txta" rows="7" maxlength="500"></textarea>
				</td>
			</tr>
		</table>
 	</div> 
	<!-- 下载excel -->
	<iframe id="exportIframe" src="" style="width:0; height:0;"></iframe>
	
	<!-- 导出dialog -->
	<div id="xsxxdaochudialog">
		<%-- 遮罩层 --%>
	   	<div id="stuExportMask" class="maskStyle">
	   		<div id="maskFont1">文件生成中，请稍后...</div>
	   	</div>
	
		<table id="tabdaochu" class="tablestyle" style="width:100%; height:100%;">
	   		<tr>
	   			<td class="titlestyle">班级</td>
			   	<td colspan="5"><input class="easyui-combobox" id="DC_BJBH" name="DC_BJBH"/></td>
			</tr>
			<tr>
				<td colspan="6" align="left">
					<a href="#" onclick="doToolbar(this.id)" id="selAllKs" class="easyui-linkbutton" plain="true" iconcls="icon-ok">全选</a>
					<a href="#" onclick="doToolbar(this.id)" id="selOther" class="easyui-linkbutton" plain="true" iconcls="icon-ok">反选</a>
				</td>
			</tr>
			<tr>
				<!--2017/9/13 <td><input id="KH" name="KH" type="checkbox" value="学籍号" style="cursor:pointer;" checked="checked"><label for="KH" style="cursor:pointer;">学籍号</label></td> -->
				
				<td><input id="XJH" name="XJH" type="checkbox" value="学籍号" style="cursor:pointer;" checked="checked"><label for="XJH" style="cursor:pointer;">学籍号</label></td>
				
				<td><input id="XH" name="XH" type="checkbox" value="学号" style="cursor:pointer;" checked="checked"><label for="XH" style="cursor:pointer;">学号</label></td>
				
				<td><input id="SFZH" name="SFZH" type="checkbox" value="证件号" style="cursor:pointer;" checked="checked"><label for="SFZH" style="cursor:pointer;">证件号</label></td>
				<td><input id="ZJLX" name="ZJLX" type="checkbox" value="证件类型" style="cursor:pointer;" checked="checked"><label for="ZJLX" style="cursor:pointer;">证件类型</label></td>
				<td><input id="XM" name="XM" type="checkbox" value="姓名" style="cursor:pointer;" checked="checked"><label for="XM" style="cursor:pointer;">姓名</label></td>
				<td><input id="XB" name="XB" type="checkbox" value="性别" style="cursor:pointer;" checked="checked"><label for="XB" style="cursor:pointer;">性别</label></td>
			</tr>
			<tr>
				<td><input id="CSNY" name="CSNY" type="checkbox" value="出生日期" style="cursor:pointer;"><label for="CSNY" style="cursor:pointer;">出生日期</label></td>
				<td><input id="ZSLB" name="ZSLB" type="checkbox" value="招生类别" style="cursor:pointer;"><label for="ZSLB" style="cursor:pointer;">招生类别</label></td>
				<td><input id="ZYDM" name="ZYDM" type="checkbox" value="专业代码" style="cursor:pointer;" checked="checked"><label for="ZYDM" style="cursor:pointer;">专业代码</label></td>
				<td><input id="ZYMC" name="ZYMC" type="checkbox" value="专业名称" style="cursor:pointer;" checked="checked"><label for="ZYMC" style="cursor:pointer;">专业名称</label></td>
				<td><input id="BH" name="BH" type="checkbox" value="班号" style="cursor:pointer;"><label for="BH" style="cursor:pointer;">班号</label></td>
				<td><input id="MZ" name="MZ" type="checkbox" value="民族" style="cursor:pointer;"><label for="MZ" style="cursor:pointer;">民族</label></td>
			</tr>
			<tr>
				<td><input id="JG" name="JG" type="checkbox" value="籍贯" style="cursor:pointer;"><label for="JG" style="cursor:pointer;">籍贯</label></td>
				<td><input id="GB" name="GB" type="checkbox" value="国别" style="cursor:pointer;"><label for="GB" style="cursor:pointer;">国别</label></td>
				<td><input id="ZZMM" name="ZZMM" type="checkbox" value="政治面貌" style="cursor:pointer;"><label for="ZZMM" style="cursor:pointer;">政治面貌</label></td>
				<td><input id="GATQB" name="GATQB" type="checkbox" value="港澳台侨胞" style="cursor:pointer;"><label for="GATQB" style="cursor:pointer;">港澳台侨胞</label></td>
				<td><input id="HKXZ" name="HKXZ" type="checkbox" value="户口性质" style="cursor:pointer;"><label for="HKXZ" style="cursor:pointer;">户口性质</label></td>
				<td><input id="HKSZXXDZ" name="HKSZXXDZ" type="checkbox" value="户口所在详细地址" style="cursor:pointer;"><label for="HKSZXXDZ" style="cursor:pointer;">户口所在详细地址</label></td>
			</tr>
			<tr>
				<td><input id="XSLB" name="XSLB" type="checkbox" value="学生类别" style="cursor:pointer;"><label for="XSLB" style="cursor:pointer;">学生类别</label></td>
				<td><input id="RXRQ" name="RXRQ" type="checkbox" value="入学日期" style="cursor:pointer;"><label for="RXRQ" style="cursor:pointer;">入学日期</label></td>
				<td><input id="BYNF" name="BYNF" type="checkbox" value="毕业年份" style="cursor:pointer;"><label for="BYNF" style="cursor:pointer;">毕业年份</label></td>
	   			<td><input id="PZH" name="PZH" type="checkbox" value="拍照号" style="cursor:pointer;"><label for="PZH" style="cursor:pointer;">拍照号</label></td>
				<td><input id="YZBM" name="YZBM" type="checkbox" value="邮政编码" style="cursor:pointer;"><label for="YZBM" style="cursor:pointer;">邮政编码</label></td>
				<td><input id="BZ" name="BZ" type="checkbox" value="备注" style="cursor:pointer;"><label for="BZ" style="cursor:pointer;">备注</label></td>
			</tr>
	   		<tr>
	   			<td><input id="SSPCS" name="SSPCS" type="checkbox" value="所属派出所" style="cursor:pointer;"><label for="SSPCS" style="cursor:pointer;">所属派出所</label></td>
	   			<td><input id="BYXX" name="BYXX" type="checkbox" value="毕业学校" style="cursor:pointer;"><label for="BYXX" style="cursor:pointer;">毕业学校</label></td>
				<td><input id="FQ" name="FQ" type="checkbox" value="父亲" style="cursor:pointer;"><label for="FQ" style="cursor:pointer;">父亲</label></td>
				<td><input id="FQNL" name="FQNL" type="checkbox" value="父亲年龄" style="cursor:pointer;"><label for="FQNL" style="cursor:pointer;">父亲年龄</label></td>
	   			<td><input id="FQZZMM" name="FQZZMM" type="checkbox" value="父亲政治面貌" style="cursor:pointer;"><label for="FQZZMM" style="cursor:pointer;">父亲政治面貌</label></td>
				<td><input id="FQDGZ" name="FQDGZ" type="checkbox" value="父亲的工作单位" style="cursor:pointer;"><label for="FQDGZ" style="cursor:pointer;">父亲的工作单位</label></td>
			</tr>
	   		<tr>
	   			<td><input id="FQDDD" name="FQDDD" type="checkbox" value="父亲的电话" style="cursor:pointer;"><label for="FQDDD" style="cursor:pointer;">父亲的电话</label></td>
	   			<td><input id="MQ" name="MQ" type="checkbox" value="母亲" style="cursor:pointer;"><label for="MQ" style="cursor:pointer;">母亲</label></td>
				<td><input id="MQZZMM" name="MQZZMM" type="checkbox" value="母亲政治面貌" style="cursor:pointer;"><label for="MQZZMM" style="cursor:pointer;">母亲政治面貌</label></td>
	   			<td><input id="MQNL" name="MQNL" type="checkbox" value="母亲年龄" style="cursor:pointer;"><label for="MQNL" style="cursor:pointer;">母亲年龄</label></td>
				<td><input id="MQGZDW" name="MQGZDW" type="checkbox" value="母亲工作单位" style="cursor:pointer;"><label for="MQGZDW" style="cursor:pointer;">母亲工作单位</label></td>
	   			<td><input id="MQDDH" name="MQDDH" type="checkbox" value="母亲的电话" style="cursor:pointer;"><label for="MQDDH" style="cursor:pointer;">母亲的电话</label></td>
			</tr>
			<tr>
				<td><input id="qt_cw" name="qt_cw" type="checkbox" value="其他成员称谓" style="cursor:pointer;" checked="checked"><label for="qt_cw" style="cursor:pointer;">其他成员称谓</label></td>
				<td><input id="qt_name" name="qt_name" type="checkbox" value="其他成员的姓名" style="cursor:pointer;"><label for="qt_name" style="cursor:pointer;">其他成员的姓名</label></td>
				<td><input id="qt_age" name="qt_age" type="checkbox" value="其他成员的年龄" style="cursor:pointer;"><label for="qt_age" style="cursor:pointer;">其他成员的年龄</label></td>
				<td><input id="qt_zzmm" name="qt_zzmm" type="checkbox" value="其他成员的政治面貌" style="cursor:pointer;"><label for="qt_zzmm" style="cursor:pointer;">其他成员的政治面貌</label></td>
				<td><input id="qt_gzdw" name="qt_gzdw" type="checkbox" value="其他成员的工作单位" style="cursor:pointer;"><label for="qt_gzdw" style="cursor:pointer;">其他成员的工作单位</label></td>
	   			<td><input id="qt_tel" name="qt_tel" type="checkbox" value="其他成员的手机号码" style="cursor:pointer;"><label for="qt_tel" style="cursor:pointer;">其他成员的手机号码</label></td>
			</tr>
	   		<tr>
	   			<td colspan="6" align="center"><a href="#" onclick="doToolbar(this.id)" id="exportStu" class="easyui-linkbutton" plain="true" iconcls="icon-submit">导出</a></td>
	   		</tr>
	   		</table>
	   	</div>
<!-- <div id="xsxxdaochudialog">
		<table id="tabdaochu" class="tablestyle" style="width:100%; height:100%;">
	   		<tr>
	   			<td class="titlestyle">班级</td>
			   	<td colspan="5"><input class="easyui-combobox" id="DC_BJBH" name="DC_BJBH"/></td>
			</tr>
			<tr>
				<td colspan="6" align="left">
					<a href="#" onclick="doToolbar(this.id)" id="selAllKs" class="easyui-linkbutton" plain="true" iconcls="icon-ok">全选</a>
					<a href="#" onclick="doToolbar(this.id)" id="selOther" class="easyui-linkbutton" plain="true" iconcls="icon-ok">反选</a>
				</td>
			</tr>
			<tr>
				<td><input id="KH" name="KH" type="checkbox" value="学号" style="cursor:pointer;" checked="checked"><label for="KH" style="cursor:pointer;">学籍号</label></td>
				<td><input id="XM" name="XM" type="checkbox" value="姓名" style="cursor:pointer;" checked="checked"><label for="XM" style="cursor:pointer;">姓名</label></td>
				<td><input id="BJ" name="BJ" type="checkbox" value="行政班名称" style="cursor:pointer;" checked="checked"><label for="BJ" style="cursor:pointer;">班级</label></td>
				<td><input id="XB" name="XB" type="checkbox" value="性别" style="cursor:pointer;" checked="checked"><label for="XB" style="cursor:pointer;">性别</label></td>
				<td><input id="BJ" name="BJ" type="checkbox" value="拍照号" style="cursor:pointer;"><label for="PZH" style="cursor:pointer;">拍照号</label></td>
				<td><input id="LXDH" name="LXDH" type="checkbox" value="本人手机" style="cursor:pointer;"><label for="LXDH" style="cursor:pointer;">联系电话</label></td>
			</tr>
			<tr>
				<td><input id="XMPY" name="XMPY" type="checkbox" value="姓名拼音" style="cursor:pointer;"><label for="XMPY" style="cursor:pointer;">姓名拼音</label></td>
				<td><input id="CYM" name="CYM" type="checkbox" value="曾用名" style="cursor:pointer;"><label for="CYM" style="cursor:pointer;">曾用名</label></td>
				<td><input id="BJ" name="BJ" type="checkbox" value="招生类别" style="cursor:pointer;"><label for="ZSLB" style="cursor:pointer;">招生类别</label></td>
				<td><input id="RXNY" name="RXNY" type="checkbox" value="入学日期" style="cursor:pointer;"><label for="RXNY" style="cursor:pointer;">入学日期</label></td>
				<td><input id="MZ" name="MZ" type="checkbox" value="民族" style="cursor:pointer;"><label for="MZ" style="cursor:pointer;">民族</label></td>
				<td><input id="CSNY" name="CSNY" type="checkbox" value="出生日期" style="cursor:pointer;"><label for="CSNY" style="cursor:pointer;">出生日期</label></td>
			</tr>
			<tr>
				<td><input id="SFZH" name="SFZH" type="checkbox" value="身份证件号" style="cursor:pointer;"><label for="SFZH" style="cursor:pointer;">身份证号</label></td>
				<td><input id="XZZ" name="XZZ" type="checkbox" value="常住地址" style="cursor:pointer;"><label for="XZZ" style="cursor:pointer;">现住址</label></td>
				<td><input id="HKSZD" name="HKSZD" type="checkbox" value="户籍地址" style="cursor:pointer;"><label for="HKSZD" style="cursor:pointer;">户籍地址</label></td>
				<td><input id="YZBM" name="YZBM" type="checkbox" value="户籍邮编" style="cursor:pointer;"><label for="YZBM" style="cursor:pointer;">户籍邮编</label></td>
				<td><input id="DZYX" name="DZYX" type="checkbox" value="电子信箱" style="cursor:pointer;"><label for="DZYX" style="cursor:pointer;">电子信箱</label></td>
				<td><input id="TC" name="TC" type="checkbox" value="特长" style="cursor:pointer;"><label for="TC" style="cursor:pointer;">特长</label></td>
	   		</tr>
	   		<tr>
				<td><input id="SFZH" name="SFZH" type="checkbox" value="港澳台侨外码" style="cursor:pointer;"><label for="GATQ" style="cursor:pointer;">港澳台侨</label></td>
				<td><input id="XZZ" name="XZZ" type="checkbox" value="血型码" style="cursor:pointer;"><label for="XX" style="cursor:pointer;">血型</label></td>
				<td><input id="HKSZD" name="HKSZD" type="checkbox" value="政治面貌码"style="cursor:pointer;"><label for="ZZMM" style="cursor:pointer;">政治面貌</label></td>
				<td><input id="YZBM" name="YZBM" type="checkbox" value="[国籍/ 地区码]" style="cursor:pointer;"><label for="GJ" style="cursor:pointer;">国籍</label></td>
				<td><input id="DZYX" name="DZYX" type="checkbox" value="健康状况码" style="cursor:pointer;"><label for="JKZK" style="cursor:pointer;">健康状况</label></td>
				<td><input id="TC" name="TC" type="checkbox" value="籍贯" style="cursor:pointer;"><label for="JG" style="cursor:pointer;">籍贯</label></td>
	   		</tr>
	   		<tr>
				<td><input id="SFZH" name="SFZH" type="checkbox" value="婚姻状况码" style="cursor:pointer;"><label for="HYZK" style="cursor:pointer;">婚姻状况</label></td>
				<td><input id="XZZ" name="XZZ" type="checkbox" value="出生地码" style="cursor:pointer;"><label for="CSD" style="cursor:pointer;">出生地</label></td>
				<td><input id="HKSZD" name="HKSZD" type="checkbox" value="户口类别码"style="cursor:pointer;"><label for="HKLB" style="cursor:pointer;">户口类别</label></td>
				<td><input id="YZBM" name="YZBM" type="checkbox" value="是否是流动人口" style="cursor:pointer;"><label for="LDRKZK" style="cursor:pointer;">流动人口状况</label></td>
				<td><input id="DZYX" name="DZYX" type="checkbox" value="所属派出所" style="cursor:pointer;"><label for="SSPCS" style="cursor:pointer;">所属派出所</label></td>
				<td><input id="TC" name="TC" type="checkbox" value="毕业学校" style="cursor:pointer;"><label for="BYXX" style="cursor:pointer;">毕业学校</label></td>
	   		</tr>
	   		<tr>
				<td><input id="SFZH" name="SFZH" type="checkbox" value="备注" style="cursor:pointer;"><label for="BZ" style="cursor:pointer;">备注</label></td>
				<td><input id="XZZ" name="XZZ" type="checkbox" value="毕业年份" style="cursor:pointer;"><label for="BYNF" style="cursor:pointer;">毕业年份</label></td>
				<td><input id="HKSZD" name="HKSZD" type="checkbox" value="父亲" style="cursor:pointer;"><label for="FQXM" style="cursor:pointer;">父亲姓名</label></td>
				<td><input id="YZBM" name="YZBM" type="checkbox" value="父亲年龄" style="cursor:pointer;"><label for="FQNL" style="cursor:pointer;">父亲年龄</label></td>
				<td><input id="DZYX" name="DZYX" type="checkbox" value="父亲政治面貌" style="cursor:pointer;"><label for="FQZZMM" style="cursor:pointer;">父亲政治面貌</label></td>
				<td><input id="TC" name="TC" type="checkbox" value="父亲电话" style="cursor:pointer;"><label for="FQDH" style="cursor:pointer;">父亲电话</label></td>
	   		</tr>
	   		<tr>
				<td><input id="SFZH" name="SFZH" type="checkbox" value="父亲工作单位" style="cursor:pointer;"><label for="FQGZDW" style="cursor:pointer;">父亲工作单位</label></td>
				<td><input id="XZZ" name="XZZ" type="checkbox" value="母亲" style="cursor:pointer;"><label for="MQXM" style="cursor:pointer;">母亲姓名</label></td>
				<td><input id="HKSZD" name="HKSZD" type="checkbox" value="母亲政治面貌" style="cursor:pointer;"><label for="MQZZMM" style="cursor:pointer;">母亲政治面貌</label></td>
				<td><input id="YZBM" name="YZBM" type="checkbox" value="母亲年龄" style="cursor:pointer;"><label for="MQNL" style="cursor:pointer;">母亲年龄</label></td>
				<td><input id="DZYX" name="DZYX" type="checkbox" value="母亲电话" style="cursor:pointer;"><label for="MQDH" style="cursor:pointer;">母亲电话</label></td>
				<td><input id="TC" name="TC" type="checkbox" value="母亲工作单位" style="cursor:pointer;"><label for="MQGZDW" style="cursor:pointer;">母亲工作单位</label></td>
	   		</tr>
	   		<tr>
				<td><input id="SFZH" name="SFZH" type="checkbox" value="其他成员称谓" style="cursor:pointer;"><label for="QTCYCW" style="cursor:pointer;">其他成员称谓</label></td>
				<td><input id="XZZ" name="XZZ" type="checkbox" value="其他成员姓名" style="cursor:pointer;"><label for="QTCYXM" style="cursor:pointer;">其他成员姓名</label></td>
				<td><input id="HKSZD" name="HKSZD" type="checkbox" value="其他成员政治面貌" style="cursor:pointer;"><label for="QTCYZZMM" style="cursor:pointer;">其他成员政治面貌</label></td>
				<td><input id="YZBM" name="YZBM" type="checkbox" value="其他成员年龄" style="cursor:pointer;"><label for="QTCYNL" style="cursor:pointer;">其他成员年龄</label></td>
				<td><input id="DZYX" name="DZYX" type="checkbox" value="其他成员工作单位" style="cursor:pointer;"><label for="QTCYGZDW" style="cursor:pointer;">其他成员工作单位</label></td>
				<td><input id="TC" name="TC" type="checkbox" value="其他成员电话" style="cursor:pointer;"><label for="QTCYDH" style="cursor:pointer;">其他成员电话</label></td>
	   		</tr>
	   		<tr>
	   			<td colspan="6" align="center"><a href="#" onclick="doToolbar(this.id)" id="exportStu" class="easyui-linkbutton" plain="true" iconcls="icon-submit">导出</a></td>
	   		</tr>
	   		</table>
	   	</div> -->
	   		<!-- 成绩打印预览页面 -->
	<div id="printViewDialog">
		<!--引入编辑页面用Ifram-->
		<iframe id="printViewIframe" name="printViewIframe" src='' style='width:100%; height:100%;' frameborder="0" scrolling="no"></iframe>
	</div>
	<a id="pageOfficeLink" href="#" style="display:none;"></a>
	
	<div id="stuPhoto" title="" style="overflow: hidden;" align="center">
		<form id="form3" name="form3" method="POST"    action="<%=request.getContextPath()%>/Svl_Import" enctype="multipart/form-data">
			<table  class="tablestyle" style="width:100%;height:100%; ">
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
				<tr>
					<td style="text-align:center;">允许上传zip、rar格式的压缩文件</td>
				</tr>
			</table>	
			<input type="hidden" id="pathPL" name="pathPL" />	
		</form>			
	</div>
  </body>
<script type="text/javascript">
	var iKeyCode='';//学生学号
	var pofOpenType = '<%=MyTools.getProp(request, "Base.pofOpenType")%>';
	var rowInfo='';//行数据
	var curPageNumber = 1;   //datagrid初始当前页数
	var curPageSize = 20; //datagrid初始页内行数
	var pageNumber = 1;   //datagrid初始当前页数
	var pageSize = 20; //datagrid初始页内行数
	//var imgPath = '<-%=MyTools.getProp(request, "Base.headImg")%>';
	//var PhotoPath = '<-%=MyTools.getProp(request, "Base.Photo")%>';
	var imgPath = '<%=MyTools.getProp(request, "Base.headPhoto")%>';
	var PhotoPath = '<%=MyTools.getProp(request, "Base.PhotoPath")%>';
	var xhInfo = new Array();
	var NameInfo = new Array();
	var exportType='';
	var lieInfo=new Array();//获取导出的列
	
	$(document).ready(function(){
		initDialog();//初始化dialog
		LoadDatagrid();//初始化对话框
		initGridData();
		loadComboboxData();//初始化combobox
		//设置输入框限制长度
  		setInputMaxLen('JX_XM', 50);
  		setInputMaxLen('JX_XH', 20);
  		setInputMaxLen('JX_PZH', 20);
  		setInputMaxLen('JX_XJH', 20);
  		setInputMaxLen('JX_XMPY', 20);
  		setInputMaxLen('JX_CYM', 10);
  		setInputMaxLen('JX_SFZJH', 30);
  		setInputMaxLen('JX_BYXX', 100);
  		setInputMaxLen('JX_HKSZD', 200);
  		setInputMaxLen('JX_SSPCS', 100);
  		setInputMaxLen('JX_YZBM', 10);
  		setInputMaxLen('JX_TC', 200);
  		setInputMaxLen('JX_XL', 30);
  		setInputMaxLen('JX_BNXH', 2);
	});
  	
  	//下拉框初始化
	function loadComboboxData(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_XsxxSet',
			data : 'active=initData',
			dataType:"json",
			success : function(data) {
				initCombobox(data[0].bjData,data[0].ydData,data[0].xbData,data[0].xslbData,data[0].mzData,data[0].xxData,
						data[0].gatqData,data[0].zzmmData,data[0].gbData,data[0].jkztData,data[0].csData,data[0].hkxzData,data[0].ldrkzkData,data[0].allbjData,
						data[0].zslbData,data[0].sfzjlxData,data[0].hyzkData,data[0].xmcData,data[0].zymcData,data[0].njmcData);
			}
		});
	}
  	
  	//下拉框赋值
  	function initCombobox(bjData,ydData,xbData,xslbData,mzData,xxData,gatqData,zzmmData,gbData,jkztData,csData,hkxzData,ldrkzkData,allbjData,zslbData,sfzjlxData,hyzkData,xmcData,zymcData,njmcData){
  		//系部
		$('#JX_XBDM_CX').combobox({
			data : xmcData,
			valueField : 'comboValue',
			textField : 'comboName',
			editable : false,
			panelHeight : 'auto',
			onLoadSuccess : function(data) {
				if (data != "") {
					$(this).combobox('setValue', '');
				}
			}
		});
  	
  		//专业名称
  		$('#JX_XZZYM').combobox({
			data : zymcData,
			valueField : 'comboValue',
			textField : 'comboName',
			editable : false,
			panelHeight : '140',
			onLoadSuccess : function(data) {
				if (data != "") {
					$(this).combobox('setValue', '');
				}
			}
		});
		
		$('#JX_ZYDM_CX').combobox({
			data : zymcData,
			valueField : 'comboValue',
			textField : 'comboName',
			panelHeight : '140',
			onLoadSuccess : function(data) {
				if (data != "") {
					$(this).combobox('setValue', '');
				}
			}
		});
		
		$('#JX_NJDM_CX').combobox({
			data : njmcData,
			valueField : 'comboValue',
			textField : 'comboName',
			panelHeight : '140',
			onLoadSuccess : function(data) {
				if (data != "") {
					$(this).combobox('setValue', '');
				}
			}
		});
		
  		$('#JX_XZBDM').combobox({
			data : bjData,
			valueField : 'comboValue',
			textField : 'comboName',
			editable : false,
			panelHeight : '140',
			onLoadSuccess : function(data) {
				if (data != "") {
					$(this).combobox('setValue', '');
				}
			}
		});
		
		$('#JX_XZBDM_CX').combobox({
			data : bjData,
			valueField : 'comboValue',
			textField : 'comboName',
			panelHeight : '140',
			onLoadSuccess : function(data) {
				if (data != "") {
					$(this).combobox('setValue', '');
				}
			}
		});
		
		//异动类型
		$('#JX_YDLXM').combobox({
			data : ydData,
			valueField : 'comboValue',
			textField : 'comboName',
			editable : false,
			panelHeight : 'auto',
			onLoadSuccess : function(data) {
				if (data != "") {
					$(this).combobox('setValue', '');
				}
			},
			onChange:function(data){
				/*
			 	if(data=='01' | data=='02' | data=='03' | data=='04' |  data=='05' | data=='06'){
			 		AbnXMCConbobox(xmcData,zymcData,bjData);
				}else{
					//$('#JX_NEWX').combobox('setText', '当前异动类型无需选择系');
					//$('#JX_NEWX').combobox('disable');
					$('#JX_NEWZY').combobox('setText', '当前异动类型无需选择专业');
					$('#JX_NEWZY').combobox('disable');
					$('#JX_NEWBJ').combobox('setText', '当前异动类型无需选择班级');
					$('#JX_NEWBJ').combobox('disable');
				} 
				*/
				if(data=='' || data=='08' || data=='10'){
					$('#JX_NEWXQ').textbox('setValue', '当前异动类型无需填写去向学校');
  					$('#JX_NEWXQ').textbox('disable');
  					$('#JX_NEWZY').combobox('setText', '当前异动类型无需选择专业');
			  		$('#JX_NEWZY').combobox('disable');
			  		$('#JX_NEWBJ').combobox('setText', '当前异动类型无需选择班级');
			  		$('#JX_NEWBJ').combobox('disable');
				}else if(data=='02' || data=='06' || data=='18'){
					AbnXMCConbobox(xmcData, zymcData, bjData);
				}else{
					$('#JX_NEWXQ').textbox('setValue', '');
  					$('#JX_NEWXQ').textbox('enable');
				}
			}
		});
		
		$('#JX_CSDZM').combobox({
			data : csData,
			valueField : 'comboValue',
			textField : 'comboName',
			editable : false,
			panelHeight : '140',
			onLoadSuccess : function(data) {
				if (data != "") {
					$(this).combobox('setValue', '');
				}
			}
		});
		
		$('#JX_HKLBM').combobox({
			data : hkxzData,
			valueField : 'comboValue',
			textField : 'comboName',
			editable : false,
			panelHeight : 'auto',
			onLoadSuccess : function(data) {
				if (data != "") {
					$(this).combobox('setValue', '');
				}
			}
		});
		
		$('#JX_LDRKZK').combobox({
			data : ldrkzkData,
			valueField : 'comboValue',
			textField : 'comboName',
			editable : false,
			panelHeight : 'auto',
			onLoadSuccess : function(data) {
				if (data != "") {
					$(this).combobox('setValue', '');
				}
			}
		});
		
		$('#JX_JGM').combobox({
			data : csData,
			valueField : 'comboValue',
			textField : 'comboName',
			editable : false,
			panelHeight : '140',
			onLoadSuccess : function(data) {
				if (data != "") {
					$(this).combobox('setValue', '');
				}
			}
		});
		
		$('#JX_JKZKM').combobox({
			data : jkztData,
			valueField : 'comboValue',
			textField : 'comboName',
			editable : false,
			panelHeight : '140',
			onLoadSuccess : function(data) {
				if (data != "") {
					$(this).combobox('setValue', '');
				}
			}
		});
		
		$('#JX_GBM').combobox({
			data : gbData,
			valueField : 'comboValue',
			textField : 'comboName',
			editable : false,
			panelHeight : '140',
			onLoadSuccess : function(data) {
				if (data != "") {
					$(this).combobox('setValue', '');
				}
			}
		});
		
		$('#JX_ZZMMM').combobox({
			data : zzmmData,
			valueField : 'comboValue',
			textField : 'comboName',
			editable : false,
			panelHeight : '140',
			onLoadSuccess : function(data) {
				if (data != "") {
					$(this).combobox('setValue', '');
				}
			}
		});
		
		$('#JX_GATQM').combobox({
			data : gatqData,
			valueField : 'comboValue',
			textField : 'comboName',
			editable : false,
			panelHeight : '140',
			onLoadSuccess : function(data) {
				if (data != "") {
					$(this).combobox('setValue', '');
				}
			}
		});
		
		$('#JX_XXM').combobox({
			data : xxData,
			valueField : 'comboValue',
			textField : 'comboName',
			editable : false,
			panelHeight : '140',
			onLoadSuccess : function(data) {
				if (data != "") {
					$(this).combobox('setValue', '');
				}
			}
		});
		
		$('#JX_XSLBM').combobox({
			data : xslbData,
			valueField : 'comboValue',
			textField : 'comboName',
			editable : false,
			panelHeight : '140',
			onLoadSuccess : function(data) {
				if (data != "") {
					$(this).combobox('setValue', '');
				}
			}
		});
		
		$('#JX_XBM').combobox({
			data : xbData,
			valueField : 'comboValue',
			textField : 'comboName',
			editable : false,
			panelHeight : 'auto',
			onLoadSuccess : function(data) {
				if (data != "") {
					$(this).combobox('setValue', '');
				}
			}
		});
		
		$('#JX_MZM').combobox({
			data : mzData,
			valueField : 'comboValue',
			textField : 'comboName',
			editable : false,
			panelHeight : '140',
			onLoadSuccess : function(data) {
				if (data != "") {
					$(this).combobox('setValue', '');
				}
			}
		});
		
		$('#JX_ZSLBM').combobox({
			data : zslbData,
			valueField : 'comboValue',
			textField : 'comboName',
			editable : false,
			panelHeight : '140',
			onLoadSuccess : function(data) {
				if (data != "") {
					$(this).combobox('setValue', '');
				}
			}
		});
		
		$('#JX_SFZJLXM').combobox({
			data : sfzjlxData,
			valueField : 'comboValue',
			textField : 'comboName',
			editable : false,
			panelHeight : '140',
			onLoadSuccess : function(data) {
				if (data != "") {
					$(this).combobox('setValue', '');
				}
			}
		});
		
		$('#JX_HYZK').combobox({
			data : hyzkData,
			valueField : 'comboValue',
			textField : 'comboName',
			editable : false,
			panelHeight : '140',
			onLoadSuccess : function(data) {
				if (data != "") {
					$(this).combobox('setValue', '');
				}
			}
		});
		
		$('#JX_FQZZMMM').combobox({
			data : zzmmData,
			valueField : 'comboValue',
			textField : 'comboName',
			editable : false,
			panelHeight : '140',
			onLoadSuccess : function(data) {
				if (data != "") {
					$(this).combobox('setValue', '');
				}
			}
		});
		
		$('#JX_MQZZMMM').combobox({
			data : zzmmData,
			valueField : 'comboValue',
			textField : 'comboName',
			editable : false,
			panelHeight : '140',
			onLoadSuccess : function(data) {
				if (data != "") {
					$(this).combobox('setValue', '');
				}
			}
		});
		
		$('#JX_QTCYZZMMM').combobox({
			data : zzmmData,
			valueField : 'comboValue',
			textField : 'comboName',
			editable : false,
			panelHeight : '140',
			onLoadSuccess : function(data) {
				if (data != "") {
					$(this).combobox('setValue', '');
				}
			}
		});
		
		$('#DC_BJBH').combobox({
			data : allbjData,
			valueField : 'comboValue',
			textField : 'comboName',
			editable : false,
			panelHeight : '140',
			onLoadSuccess : function(data) {
				if (data != "") {
					$(this).combobox('setValue', '');
				}
			}
		});
		
		$('#DC_ZSLBM').combobox({
			data : zslbData,
			valueField : 'comboValue',
			textField : 'comboName',
			editable : false,
			panelHeight : '140',
			onLoadSuccess : function(data) {
				if (data != "") {
					$(this).combobox('setValue', '');
				}
			}
		});
		
		$('#JX_SFZJH').textbox({  
		  onChange: function(value){  
			 if(value!=''){
					document.getElementById("uplodAA").style.display="none";
					document.getElementById("JX_ZPLJ").style.display="block";
				}else{
					document.getElementById("uplodAA").style.display="block";
					document.getElementById("JX_ZPLJ").style.display="none";
				}
		  }  
		});  

 	 }
  	
 	 function AbnXMCConbobox(xmcData,zymcData,bjData){
 	 	/*
 	 	$('#JX_NEWX').combobox({
			data : xmcData,
			valueField : 'comboValue',
			textField : 'comboName',
			editable : false,
			panelHeight : '140',
			onLoadSuccess : function(data) {
				if (data != "") {
					$(this).combobox('setValue', '');
				}
			},
			onChange:function(data){
			} 
		});
		*/
		 $('#JX_NEWBJ').combobox({
			data : bjData,
			valueField : 'comboValue',
			textField : 'comboName',
			editable : false,
			panelHeight : '140',
			onLoadSuccess : function(data) {
				if (data != "") {
					$(this).combobox('setValue', '');
				}
			}
		});
		$('#JX_NEWZY').combobox({
			data : zymcData,
			valueField : 'comboValue',
			textField : 'comboName',
			editable : false,
			panelHeight : '140',
			onLoadSuccess : function(data) {
				if (data != "") {
					$(this).combobox('setValue', '');
				}
			}
		});
 	 }
 	 
  	
  	/**加载 dialog控件**/
	function initDialog(){
		$('#DIA_Lead').dialog({
			width: 600,//宽度设置   
			height: 436,//高度设置 
			modal:true,
			closed: true,   
			cache: false, 
			draggable:false,//是否可移动dialog框设置
			toolbar:[{
				//导出
				text:"保存",
				iconCls:'icon-save',
				handler:function(){
					//传入save值进入doToolbar方法，用于保存
					doToolbar('save');
				}
			}],
			//打开事件
			onOpen:function(data){
				
			},
			//读取事件
			onLoad:function(data){
			},
			//关闭事件
			onClose:function(data){
				if($('#active').val()==""){
					emptyFile();
				}
				$('#form1').form('clear');
				$('#tt').tabs('select','基础信息');
				emptyDialog();
			}
		});
		
		//异动info dialog
		$('#DIA_yDmsg').dialog({
			title: '异动详情',
			width: 750,//宽度设置   
			height: 440,//高度设置 
			modal:true,
			closed: true,   
			cache: false, 
			draggable:false,//是否可移动dialog框设置
			//打开事件
			onOpen:function(data){
			},
			//读取事件
			onLoad:function(data){
			},
			//关闭事件
			onClose:function(data){
				$('#JX_XH_Abn').textbox('setValue','');
				$('#JX_XM_Abn').textbox('setValue','');
			}
		});
		
		//异动信息dialog
		$('#DIA_Abn').dialog({
			title: '异动信息',
			width: 850,//宽度设置   
			height: 500,//高度设置 
			modal:true,
			closed: true,   
			cache: false, 
			draggable:false,//是否可移动dialog框设置
			//打开事件
			onOpen:function(data){
			},
			//读取事件
			onLoad:function(data){
			},
			//关闭事件
			onClose:function(data){
				$('#JX_XH_Abn').textbox('setValue','');
				$('#JX_XM_Abn').textbox('setValue','');
			}
		});
		
		//学生异动dialog
		$('#Abndialog').dialog({
			title: '异动处理',
			width: 650,//宽度设置   
			height: 313,//高度设置 
			modal:true,
			closed: true,   
			cache: false, 
			draggable:false,//是否可移动dialog框设置
			toolbar:[{
				//导出
				text:"保存",
				iconCls:'icon-save',
				handler:function(){
					//传入save值进入doToolbar方法，用于保存
					doToolbar('save_Abn');
				}
			}],
			//打开事件
			onOpen:function(data){
			},
			//读取事件
			onLoad:function(data){
			},
			//关闭事件
			onClose:function(data){
 				emptyAbnDialg();
			}
		});
		
		//导出学生信息dialog
		$('#xsxxdaochudialog').dialog({
			title: '导出学生信息',
			width: 635,//宽度设置   
			height: 350,//高度设置 
			modal:true,
			closed: true,   
			cache: false, 
			draggable:false,//是否可移动dialog框设置
			//打开事件
			onOpen:function(data){
			},
			//读取事件
			onLoad:function(data){
			},
			//关闭事件
			onClose:function(data){
				
			}
		});
		//打印预览
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
		
		
	}
  	
  	function emptyAbnDialg(){
		$('#JX_NEWXQ').textbox('setValue','');
		$('#stuXH').html('');
  		$('#stuName').html('');
		$('#JX_YLBJ').html('');
		$('#JX_YDYY').val('');
		$('#JX_YDLXM').combobox('setValue','');
	}
  	
  	//验证图片格式是否正确
  	 function bindPhotEvent(){
		    var filePath=$("#JX_ZPLJ").val();
			var pattern=/^[^\s]*$/;
			var fname=filePath.substring(filePath.lastIndexOf("\\")+1, filePath.length);
			if(!pattern.test(fname)){//匹配文件名
				alertMsg('图片文件名中不能有空格!',0);
				return;
			}
			var extStart=filePath.lastIndexOf(".");
			var ext=filePath.substring(extStart,filePath.length).toLowerCase();
			if(ext!=".bmp"&&ext!=".png"&&ext!=".gif"&&ext!=".jpg"&&ext!=".jpge"){//图片类型
				alertMsg('图片限于png,gif,jpeg,jpg,bmp格式!',0) ;
				return;
			}
		    var arr=filePath.split('\\');
		    var fileName=arr[arr.length-1];
		   	$("#form1").attr("enctype","multipart/form-data");
			$("#form1").attr("encoding", "multipart/form-data");
			$('#path').val(escape(filePath));
	}
  		
  		
	//主界面datagrid初始化
	function LoadDatagrid(){
		$('#XSXXList').datagrid({
			url:'<%=request.getContextPath()%>/Svl_XsxxSet',
			queryParams:{"active":"queStudent","JX_XJH_CX":encodeURI($('#JX_XJH_CX').val()),"JX_XM_CX":encodeURI($('#JX_XM_CX').val()),
				"JX_XZBDM_CX":$('#JX_XZBDM_CX').combobox('getValue'),"JX_XBDM_CX":$('#JX_XBDM_CX').combobox('getValue'),
				"JX_ZYDM_CX":$('#JX_ZYDM_CX').combobox('getValue'),"JX_NJDM_CX":$('#JX_NJDM_CX').combobox('getValue'),"JX_XH_CX":encodeURI($('#JX_XH_CX').val())},
			//loadMsg:"学生列表加载中请稍后！",  //当远程加载数据时，现实的等待信息提示
			title:'学生列表',
			width:'100%',
			nowrap: false,
			fit:true, //自适应父节点宽度和高度
			showFooter:true,
			striped:true,
			pagination:true,
			pageSize:curPageSize,
			pageList: [20,50,100,150,200], 
			singleSelect:false,
			pageNumber:curPageNumber,
			rownumbers:true,
			fitColumns: true,
			columns:[[
				{field:'ck',checkbox:true},
				{field:'学籍号',title:'学籍号',width:fillsize(0.15),align:'center'},
				{field:'学号',title:'学号',width:fillsize(0.1),align:'center'},
				{field:'班内学号',title:'班内学号',width:fillsize(0.1),align:'center'},
				{field:'姓名',title:'姓名',width:fillsize(0.1),align:'center'},
				{field:'系部名称',title:'系部名称',width:fillsize(0.13),align:'center'},
				{field:'行政班名称',title:'行政班名称',width:fillsize(0.15),align:'center'},
				{field:'专业名称',title:'专业名称',width:fillsize(0.2),align:'center'},
				{field:'类别名称',title:'学生状态',width:fillsize(0.1),align:'center'},
				{field:'AA_SET',align:'center',title:'操作',width:fillsize(0.13),formatter:function(value,rec){
					return "<input type='button' value='[异动处理]' onclick='setStuState(\"" + rec.学号 +"\",\"" + rec.姓名 +"\",\"" + rec.行政班名称 + "\",\"" + rec.专业名称 +"\",\"" + rec.学生状态 +"\")' id='setStuState' style='margin-left:5px; cursor:pointer;'>";
					/*"<input type='button' value='[学籍卡]' onclick='stuInfo(\"" + rec.学籍号 +"\",\"" + rec.姓名 +"\",\"" + rec.行政班名称 + "\",\"" + rec.专业名称 +"\",\"" + rec.学生状态 +"\",\"" + rec.行政班代码 +"\")' id='stuInfo' style='margin-left:5px; cursor:pointer;'>";*/
			  }}
			]],
			//当用户点击某行时触发
			//选中某行既给iKeyCode赋值当前行学科代码
			onClickRow:function(rowIndex, rowData){
				iKeyCode = rowData.学号;
				rowInfo = rowData;
			},
			//当用户双击某行时触发
			onDblClickRow:function(rowIndex, rowData){
				doToolbar('edit');
			},
			onSelect:function(rowIndex,rowData){
				var falg=false;
				if(xhInfo.length == 0){
					NameInfo.push(rowData.姓名);
					xhInfo.push(rowData.学号);
				}else{
					for(var i=0;i<xhInfo.length;i++){
						if(xhInfo[i]==rowData.学号){
							falg=false;
							break;
						}else{
							falg=true;
						}
					}
					if(falg){
						NameInfo.push(rowData.姓名);
						xhInfo.push(rowData.学号);
					}
				}
			},
			onUnselect:function(rowIndex,rowData){
				$.each(xhInfo, function(key,value){
					if(value == rowData.学号){
						xhInfo.splice(key, 1);
						NameInfo.splice(key, 1);
					}
				});
			},
			//界面加载成功后触发
			onLoadSuccess:function(data){
				iKeyCode = "";
				rowInfo = '';
				curPageSize = $(this).datagrid('options').pageSize;
				curPageNumber = $(this).datagrid('options').pageNumber;
				
				//我写的
				$('#File1').show();
				$('#import').linkbutton('enable');
		    	$('#export').linkbutton('enable');
		    	
		    	if(data){
					$.each(data.rows, function(rowIndex, rowData){
						for(var i=0; i<xhInfo.length; i++){
							if(xhInfo[i] == rowData.学号){
								$('#XSXXList').datagrid('selectRow', rowIndex);
								break;
							}
						}
					});
				}
			} 
 		});
 	}
 	
 	
 	//初始化异动info
  	function LoadAbnInfoDatagrid(stuXH){
  		$('#yDmsg').datagrid({
  			url:'<%=request.getContextPath()%>/Svl_XsxxSet',
  			queryParams:{"active":"queStudent_AbnInfo","JX_XH_Abn":encodeURI($('#JX_XH_Abn').val()),"JX_XM_Abn":encodeURI($('#JX_XM_Abn').val()),"JX_XH":stuXH},
			//loadMsg:"教师列表加载中请稍后！",  //当远程加载数据时，现实的等待信息提示
			width:'100%',
			nowrap: false,
			fit:true, //自适应父节点宽度和高度
			showFooter:true,
			striped:true,
			singleSelect:true,
			rownumbers:true,
			fitColumns: true,
			columns:[[
				{field:'学号',title:'学籍号',width:fillsize(0.2),align:'center'},
				{field:'姓名',title:'学生姓名',width:fillsize(0.1),align:'center'},
				{field:'类别名称',title:'异动类型',width:fillsize(0.1),align:'center'},
				{field:'异动日期',title:'异动日期',width:fillsize(0.2),align:'center'},
				{field:'异动说明',title:'异动原因',width:fillsize(0.4),align:'center'}
			]],
			//当用户点击某行时触发
			onClickRow:function(rowIndex, rowData){
			},
			//当用户双击某行时触发
			onDblClickRow:function(rowIndex, rowData){
			},
			//界面加载成功后触发
			onLoadSuccess:function(data){
				} 
  		});
  	}
  	
  	
  	//初始化异动信息
  	function LoadAbnDatagrid(){
  		$('#XSXXList_Abn').datagrid({
  			url:'<%=request.getContextPath()%>/Svl_XsxxSet',
  			queryParams:{"active":"queStudent_Abn","JX_XH_Abn":encodeURI($('#JX_XH_Abn').val()),"JX_XM_Abn":encodeURI($('#JX_XM_Abn').val())},
			//loadMsg:"教师列表加载中请稍后！",  //当远程加载数据时，现实的等待信息提示
			width:'100%',
			nowrap: false,
			fit:true, //自适应父节点宽度和高度
			showFooter:true,
			striped:true,
			pagination:true,
			pageSize:pageSize,
			singleSelect:true,
			pageNumber:pageNumber,
			rownumbers:true,
			fitColumns: true,
			columns:[[
				{field:'学号',title:'学号',width:fillsize(0.2),align:'center'},
				{field:'班内学号',title:'班内学号',width:fillsize(0.1),align:'center'},
				{field:'姓名',title:'姓名',width:fillsize(0.1),align:'center'},
				{field:'系部名称',title:'系部名称',width:fillsize(0.1),align:'center'},
				{field:'行政班名称',title:'行政班名称',width:fillsize(0.2),align:'center'},
				{field:'专业名称',title:'专业名称',width:fillsize(0.2),align:'center'},
				{field:'类别名称',title:'学生状态',width:fillsize(0.1),align:'center'},
				{field:'AA_SET',align:'center',title:'操作',width:fillsize(0.35),formatter:function(value,rec){
					return "<input type='button' value='[异动详情]' onclick='yDmsg(\"" + rec.学号 +"\",\"" + rec.姓名 +"\",\"" + rec.行政班名称 + "\",\"" + rec.专业名称 +"\",\"" + rec.学生状态 +"\")' id='yDmsg' style='margin-left:5px; cursor:pointer;'>" + 
						"<input type='button' value='[异动处理]' onclick='setStuState(\"" + rec.学号 +"\",\"" + rec.姓名 +"\",\"" + rec.行政班名称 + "\",\"" + rec.专业名称 +"\",\"" + rec.学生状态 +"\")' id='setStuState' style='margin-left:5px; cursor:pointer;'>";
				}}  
			]],
			//当用户点击某行时触发
			onClickRow:function(rowIndex, rowData){
				iKeyCode = rowData.学号;
				rowInfo = rowData;
				},
			//当用户双击某行时触发
			onDblClickRow:function(rowIndex, rowData){
				doToolbar('edit');
			},
			onSelect:function(rowIndex,rowData){
				var falg=false;
				if(xhInfo.length == 0){
					NameInfo.push(rowData.姓名);
					xhInfo.push(rowData.学号);
				}else{
					for(var i=0;i<xhInfo.length;i++){
						if(xhInfo[i]==rowData.学号){
							falg=false;
							break;
						}else{
							falg=true;
						}
					}
					if(falg){
						NameInfo.push(rowData.姓名);
						xhInfo.push(rowData.学号);
					}
				}
			},
			onUnselect:function(rowIndex,rowData){
				$.each(xhInfo, function(key,value){
					if(value == rowData.学号){
						xhInfo.splice(key, 1);
						NameInfo.splice(key, 1);
					}
				});
			},
			//界面加载成功后触发
			onLoadSuccess:function(data){
				iKeyCode = "";
				rowInfo = '';
				curPageSize = $(this).datagrid('options').pageSize;
				curPageNumber = $(this).datagrid('options').pageNumber;
				
				//我写的
				$('#File1').show();
				$('#import').linkbutton('enable');
		    	$('#export').linkbutton('enable');
		    	
		    	if(data){
					$.each(data.rows, function(rowIndex, rowData){
						for(var i=0; i<xhInfo.length; i++){
							if(xhInfo[i] == rowData.学号){
								$('#XSXXList_Abn').datagrid('selectRow', rowIndex);
								break;
							}
						}
					});
				}
			} 
  		});
  	}
  	
  	//切换学籍状态
  	function changeState(stuXH){
  		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_XsxxSet',
			data : 'active=changeState&JX_XH=' + stuXH,
			dataType:"json",
			success : function(data){
				if(data[0].MSG == '保存成功'){
					showMsg(data[0].MSG);
					LoadDatagrid();
				}else{
					alertMsg(data[0].MSG);
				}
			}
		});
  	}
  	//异动信息
  	function yDmsg(stuXH, stuName, BJMC, YZYMC, stuState){
  		if(stuState == '07'){
  			alertMsg('当前学生已毕业，无法进行异动操作！');
  			return;
  		}
  		$('#stuXH').html(stuXH);
  		$('#stuName').html(stuName);
  		$('#JX_YLBJ').html(BJMC);
  		$('#JX_YZYMC').html(YZYMC);
  		$('#JX_YDRQ').datebox('setValue', getCurTime());
  		LoadAbnInfoDatagrid(stuXH);
  		$("#DIA_yDmsg").dialog("open");
  		$('#JX_NEWXQ').textbox('setValue', '当前异动类型无需填写去向学校');
  		$('#JX_NEWXQ').textbox('disable');
  		$('#JX_NEWZY').combobox('setText', '当前异动类型无需选择专业');
  		$('#JX_NEWZY').combobox('disable');
  		$('#JX_NEWBJ').combobox('setText', '当前异动类型无需选择班级');
  		$('#JX_NEWBJ').combobox('disable');
  	}
  	
  	//异动处理
  	function setStuState(stuXH, stuName, BJMC, YZYMC, stuState){
  		if(stuState == '07'){
  			alertMsg('当前学生已毕业，无法进行异动操作！');
  			return;
  		}
  	
  		$('#stuXH').html(stuXH);
  		$('#stuName').html(stuName);
  		$('#JX_YLBJ').html(BJMC);
  		$('#JX_YZYMC').html(YZYMC);
  		$('#JX_YDRQ').datebox('setValue', getCurTime());
  		$('#Abndialog').dialog('open');
  		$('#JX_NEWXQ').textbox('setValue', '当前异动类型无需填写去向学校');
  		$('#JX_NEWXQ').textbox('disable');
  		$('#JX_NEWZY').combobox('setText', '当前异动类型无需选择专业');
  		$('#JX_NEWZY').combobox('disable');
  		$('#JX_NEWBJ').combobox('setText', '当前异动类型无需选择班级');
  		$('#JX_NEWBJ').combobox('disable');
  	}
  	
  	//学籍卡
  	function stuInfo(stuXH, stuName, BJMC, YZYMC, stuState,bjdm){
  			if(pofOpenType == 'normal'){
  			
				$('#printViewDialog').dialog('open');			
				$("#printViewIframe").attr("src","<%=request.getContextPath()%>/form/registerScoreManage/stuInfoView.jsp?exportType=classKcb");
			}else{
				$.ajax({
					type : "POST",
					url : '<%=request.getContextPath()%>/Svl_XsxxSet',
					data : "active=loadTeaPageOfficeLink&exportType=classKcb"+"&JX_XJH="+stuXH+"&BJBH="+bjdm,
					dataType:"json",
					success : function(data){
						$('#pageOfficeLink').attr('href', data[0].linkStr);
						document.getElementById("pageOfficeLink").click();
					}
				});
			}
  	}
  	//保存图片
  	function saveImp(){
	  	$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_XsxxSet',
			data : 'active=savePhoto&SFZJH=' + $('#JX_SFZJH').textbox('getValue')+'&savePath=' + $('#path').val(),
			dataType:"json",
			success : function(data){
				$("#JX_ZP").val(data[0].msg);
				$('#form1').submit();
			}
		});
  	}
  	
  	//删除学生
  	function delStudent(){
	  	$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_XsxxSet',
			data : 'active=del&JX_XH=' + xhInfo,
			dataType:"json",
			success : function(data){
				if(data[0].MSG == '删除成功'){
					showMsg(data[0].MSG);
					LoadDatagrid();
					xhInfo.length=0;
					NameInfo.length=0;
				}else{
					alertMsg(data[0].MSG);
				}
			},
			error:function(data){}
		});
  	}
  	
  	//from表单提交
  	$('#form1').form({
		url:'<%=request.getContextPath()%>/Svl_XsxxSet',
	    onSubmit: function(){
	    },
	    success:function(data){
	    	//页面提交成功
	    	$("#form1").attr("enctype","application/x-www-form-urlencoded");
			$("#form1").attr("encoding", "application/x-www-form-urlencoded");
			var json = eval("("+data+")");
			if($('#active').val()=="save"){
				if(json[0].MSG=="保存成功"){
					showMsg(json[0].MSG);
					LoadDatagrid();
					$('#DIA_Lead').dialog('close');
				}else{
					alertMsg(json[0].MSG);
				}
			}
			if($('#active').val()==""){
				if($('#path').val(json[0].msg)=="系统正在维护"){
					alertMsg(json[0].MSG);
				}else{
					var aa="<img style='width:90px;height:115px;margin-left: 2%' src='"+imgPath+json[0].msg+"'/>";
					$("#Img").html(aa);
					$('#path').val(json[0].msg);
				}
			}
	    },
	   	error:function(data){}
	});
  	
  	//ajax删除学生照片
  		function delStudentZP(){
		  	$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_XsxxSet',
				data : 'active=delZP&JX_XH=' + rowInfo.身份证件号,
				dataType:"json",
				success : function(data){
					if(data[0].MSG == '删除成功'){
						$('#Img').html("<img style='width:90px; height:115px;' src='<%=request.getContextPath()%>/images/nopic1.png'/>");
						LoadDatagrid();
					}else{
						alertMsg(data[0].MSG);
					}
				},
				error:function(data){}
			});
  		}
  	
  	
  	/**清空Dialog中表单元素数据**/
	function emptyDialog(){
		xhInfo.length = 0;
		NameInfo.length = 0;
		
		$('#Img').html("<img style='width:90px; height:115px;' src='<%=request.getContextPath()%>/images/nopic1.png'/>");
		$('#JX_XM').textbox('setValue','');
		$('#JX_XH').textbox('setValue', '');
		$('#JX_PZH').textbox('setValue', '');
		$('#JX_ZSLBM').combobox('setValue', '');
		$('#JX_XZZYM').combobox('setValue','');
		$('#JX_XZBDM').combobox('setValue','');
		$('#JX_XZZYM').combobox('setValue','');
		$('#JX_XBM').combobox('setValue', '');
		$('#JX_XSLBM').combobox('setValue', '');
		$('#JX_FQZZMMM').combobox('setValue', '');
		$('#JX_MQZZMMM').combobox('setValue', '');
		$('#JX_RXRQ').datebox('setValue', '');
		$('#JX_SFZJH').textbox('setValue', '');
		$('#JX_CSNY').datebox('setValue', '');
		$('#JX_BYXX').textbox('setValue', '');
		$('#JX_HKSZD').textbox('setValue', '');
		$('#JX_SSPCS').textbox('setValue', '');
		$('#JX_QTCYZZMMM').combobox('setValue', '');
		$('#JX_GBM').combobox('setValue', '');
		$('#JX_JKZKM').combobox('setValue', '');
		$('#JX_JGM').combobox('setValue', '');
		$('#JX_HYZK').combobox('setValue', '');
		$('#JX_CSDZM').combobox('setValue','');
		$('#JX_HKLBM').combobox('setValue', '');
		$('#JX_MZM').combobox('setValue', '');
		$('#JX_XXM').combobox('setValue', '');
		$('#JX_GATQM').combobox('setValue', '');
		$('#JX_ZZMMM').combobox('setValue', '');
		$('#JX_LDRKZK').combobox('setValue', '');
		$('#JX_SFZJLXM').combobox('setValue', '');
		$('#JX_XMPY').textbox('setValue', '');
		$('#JX_YZBM').textbox('setValue', '');
		$('#JX_PZH').textbox('setValue', '');
		$('#JX_QTCYDH').textbox('setValue', '');
		$('#JX_QTCYNL').textbox('setValue', '');
		$('#JX_QTCYGZDW').textbox('setValue', '');
		$('#JX_QTCYCW').textbox('setValue', '');
		$('#JX_QTCYXM').textbox('setValue', '');	
		$('#JX_MQDH').textbox('setValue', '');
		$('#JX_MQNL').textbox('setValue', '');
		$('#JX_MQGZDW').textbox('setValue', '');
		$('#JX_MQXM').textbox('setValue', '');
		$('#JX_FQXM').textbox('setValue', '');
		$('#JX_FQNL').textbox('setValue', '');
		$('#JX_FQDH').textbox('setValue', '');
		$('#JX_FQGZDW').textbox('setValue', '');
		$('#JX_BZ').textbox('setValue', '');
  	}
  	
  	function doToolbar(iToolbar){
		//查询
		if(iToolbar == "que"){
			curPageNumber = 1;
			xhInfo.length=0;
			NameInfo.length=0;
			LoadDatagrid();
		}
		
		if(iToolbar=="upload"){
			var pd=$('#JX_SFZJH').textbox('getValue');
			if(pd==''){
				alertMsg("请先输入身份证件号");
			}else{
				$('#active').val('');	
				bindPhotEvent();
				$('#form1').submit();
			}
			
		}
		
		if(iToolbar == "del"){
			if(xhInfo.length == 0){
				alertMsg('请选择一行数据');
				return;
			}
			ConfirmMsg('当前选择的学生<font color="red">（'+NameInfo.toString().replace(/,/g,"，")+'）</font>信息删除后无法恢复，是否确定要删除？', 'delStudent();', '');
		}
		
		if(iToolbar == "delZP"){
			ConfirmMsg('照片删除后无法恢复，是否确定要删除？', 'delStudentZP();', '');
		}
		
		if(iToolbar == "new"){
			$('#JX_XJH').textbox('readonly', false);
			$("#DIA_Lead").dialog("open");
			$("#DIA_Lead").dialog('setTitle','新建');
			$('#invoke').val(iToolbar);
		}
		if(iToolbar == "edit"){
			if(iKeyCode == ""){
				alertMsg("请选择一条数据");
				return;
			}else{
				$('#invoke').val(iToolbar);
				$('#JX_YXJH').val(rowInfo.学籍号);
				$('#JX_XJH').textbox('setValue', rowInfo.学籍号);
				//$('#JX_XJH').textbox('readonly', true); //设置输入框为禁用
				$('#JX_YXM').val(rowInfo.姓名);
				$('#JX_XM').textbox('setValue', rowInfo.姓名);
				$('#JX_YXH').val(rowInfo.学号);
				$('#JX_XH').textbox('setValue', rowInfo.学号);
				$('#JX_PZH').textbox('setValue', rowInfo.拍照号);
				$('#JX_XZBDM').combobox('setValue',rowInfo.行政班代码);
				$('#JX_BNXH').textbox('setValue',rowInfo.班内学号);
				/*
				if(rowInfo.学籍号==undefined || rowInfo.学籍号==''){
					$('#JX_XZBDM').combobox('disable');
					$('#JX_BNXH').textbox('disable');
				}else{
					$('#JX_XZBDM').combobox('enable');
					$('#JX_BNXH').textbox('enable');
				}
				*/
				$('#JX_XZZYM').combobox('setValue',rowInfo.专业代码);
				$('#JX_XMPY').textbox('setValue', rowInfo.姓名拼音);
				$('#JX_CYM').textbox('setValue', rowInfo.曾用名);
				$('#JX_XBM').combobox('setValue', rowInfo.性别码);
				$('#JX_RXRQ').datebox('setValue', rowInfo.入学日期);
				$('#JX_BYNF').textbox('setValue', rowInfo.毕业年份);
				$('#JX_XSLBM').combobox('setValue', rowInfo.学生类别);
				$('#JX_SFZJH').textbox('setValue', rowInfo.身份证件号);
				if($('#JX_SFZJH').textbox('getValue')!=''){
					document.getElementById("uplodAA").style.display="none";
					document.getElementById("JX_ZPLJ").style.display="block";
				}else{
					document.getElementById("uplodAA").style.display="block";
					document.getElementById("JX_ZPLJ").style.display="none";
				}
				$('#JX_CSNY').datebox('setValue', rowInfo.出生日期);
				$('#JX_MZM').combobox('setValue', rowInfo.民族码);
				$('#JX_XXM').combobox('setValue', rowInfo.血型码);
				$('#JX_GATQM').combobox('setValue', rowInfo.港澳台侨外码);
				$('#JX_ZZMMM').combobox('setValue', rowInfo.政治面貌码);
				$('#JX_FQZZMMM').combobox('setValue', rowInfo.父亲政治面貌);
				$('#JX_MQZZMMM').combobox('setValue', rowInfo.母亲政治面貌);
				$('#JX_QTCYZZMMM').combobox('setValue', rowInfo.其他成员政治面貌);
				$('#JX_GBM').combobox('setValue', rowInfo.国籍);
				$('#JX_JKZKM').combobox('setValue', rowInfo.健康状况码);
				$('#JX_JGM').combobox('setValue', rowInfo.籍贯);
				$('#JX_HYZK').combobox('setValue', rowInfo.婚姻状况码);
				$('#JX_CSDZM').combobox('setValue', rowInfo.出生地码);
				$('#JX_BYXX').textbox('setValue', rowInfo.毕业学校);
				$('#JX_HKSZD').textbox('setValue', rowInfo.户籍地址);
				$('#JX_HKLBM').combobox('setValue', rowInfo.户口类别码);
				$('#JX_LDRKZK').combobox('setValue', rowInfo.是否是流动人口);
				$('#JX_SSPCS').textbox('setValue', rowInfo.所属派出所);
				$('#JX_YZBM').textbox('setValue', rowInfo.户籍邮编);
				$('#JX_ZSLBM').combobox('setValue', rowInfo.招生类别);
				$('#JX_PZH').textbox('setValue', rowInfo.拍照号);
				$('#JX_SFZJLXM').combobox('setValue', rowInfo.身份证件类型码);
				$('#JX_QTCYDH').textbox('setValue', rowInfo.其他成员电话);
				$('#JX_QTCYNL').textbox('setValue', rowInfo.其他成员年龄);
				$('#JX_QTCYGZDW').textbox('setValue', rowInfo.其他成员工作单位);
				$('#JX_QTCYCW').textbox('setValue', rowInfo.其他成员称谓);
				$('#JX_QTCYXM').textbox('setValue', rowInfo.其他成员姓名);	
				$('#JX_MQDH').textbox('setValue', rowInfo.母亲电话);
				$('#JX_MQNL').textbox('setValue', rowInfo.母亲年龄);
				$('#JX_MQGZDW').textbox('setValue', rowInfo.母亲工作单位);
				$('#JX_MQXM').textbox('setValue', rowInfo.母亲);
				$('#JX_FQXM').textbox('setValue', rowInfo.父亲);
				$('#JX_FQNL').textbox('setValue', rowInfo.父亲年龄);
				$('#JX_FQDH').textbox('setValue', rowInfo.父亲电话);
				$('#JX_FQGZDW').textbox('setValue', rowInfo.父亲工作单位);
				$('#JX_BZ').textbox('setValue', rowInfo.备注);	
 				if(rowInfo.照片路径!=undefined && rowInfo.照片路径!=''){
 					$('#Img').html("<img style='width:90px; height:115px;' src='" + PhotoPath+rowInfo.照片路径 + "'/>");
				}
				$("#DIA_Lead").dialog('setTitle', rowInfo.姓名+'个人信息');
				$("#DIA_Lead").dialog("open");
				
			}
			
		}
		if(iToolbar == "save"){
			if($('#JX_XM').textbox('getValue') == ''){
				alertMsg('姓名不能为空');
				return;
			}
			
			if($('#JX_XH').textbox('getValue') == ''){
				alertMsg('学号不能为空');
				return;
			}
			
			$('#JX_XZBJ').val($('#JX_XZBDM').combobox('getValue'));
			$('#active').val(iToolbar);
			//如果照片路径是空的就直接提交表单不上传图片
			if($("#path").val()==''){
				$('#form1').submit();
			}else{
				saveImp();
			}
		}
		//导出
		/*
		if(iToolbar == 'export'){
			$('#maskFont').html('学生信息模板导出中...');
    		$('#divPageMask').show();
			ExportExcelStudent();
		}
		*/
		//导入
		if(iToolbar == "File1"){
    		var uploadFile = $("#File1").val();
			var extension = uploadFile.substring(uploadFile.lastIndexOf('.')+1, uploadFile.length);//导入文件后缀名
			if(''==uploadFile){//未选择需要导入的文件
				alertMsg('请选择需要导入的文件!',0);
				//alert('请选择需要导入的文件');
				return;
			}
			if(extension != ('xls') && extension != ('xlsx')){//根据后缀判断是否为excel文件
				alertMsg('只允许导入excel文件，请重新选择导入文件!',0);
				return;
			}
			$('#maskFont').html('学生基本信息导入中...');
    		$('#divPageMask').show();
			//importExcel();
			$('#sxpath').val(escape(uploadFile));
			$('#form2').attr("enctype","multipart/form-data");
			$('#form2').attr("encoding", "multipart/form-data");
			$('#active2').val("Savestudentimportxls");
			$('#form2').submit();
    	}
    	
    	if(iToolbar == "abn"){
    		LoadAbnDatagrid();
    		$("#DIA_Abn").dialog("open");
		}	
    	
		if(iToolbar == "que_Abn"){
			pageNumber = 1;
			LoadAbnDatagrid();
		}
		
		if(iToolbar == "save_Abn"){
			var YDLXM=$('#JX_YDLXM').combobox('getValue');
			if(YDLXM == ''){
				alertMsg('请选择异动类型');
				return;
			}
		
			if($('#JX_YDRQ').datebox('getValue') == ''){
				alertMsg('请选择异动日期');
				return;
			}
			
			if(YDLXM=='02' || YDLXM=='06' || YDLXM == '18'){
				if($('#JX_NEWZY').combobox('getValue') == ''){
					alertMsg('请选择新专业');
					return;
				}
				
				if($('#JX_NEWBJ').combobox('getValue') == ''){
					alertMsg('请选择新班级');
					return;
				}
			}
			
			AbnSubmit();
		}
		
		if(iToolbar == "exportdialog"){
			$('#DC_BJBH').combobox('setValue','');
			  $("#tabdaochu input:checkbox").each(function () {
	       		  $(this).prop("checked",false);
   			 });
   			 
   			 $('#XJH').prop('checked', true);
   			 
   			 $('#XH').prop('checked', true);
   			 
   			 $('#SFZH').prop("checked", true);
   			 $('#ZJLX').prop("checked", true);
   			 $('#XM').prop("checked", true);
   			 $('#ZYDM').prop("checked", true);
   			 $('#ZYMC').prop("checked", true);
   			$("#xsxxdaochudialog").dialog("open");
		}
		
		if(iToolbar == "exportStu"){
			lieInfo.length=0;
			var type=0;
			 $("#tabdaochu input:checkbox").each(function () {
	       		 var ischecked = $(this).prop("checked");
	       		 if(ischecked == true){
	       		 	lieInfo.push($(this).val());
	       		 	 type+=1;
	       		 }
   			 });
   			 if(type==0){
   			 	alertMsg("请至少选择一项");
   			 	return;
   			 }
   			 ExportExcelStudentall(lieInfo);
		}
		
		if(iToolbar == "selAllKs"){
			
			 $("#tabdaochu input:checkbox").each(function () {
	       		 $(this).attr("checked",true);
   			 });
   			
   			 $("#tabdaochu input:checkbox").prop('checked', true);
		}
		
		if(iToolbar == 'selOther'){
			$("#tabdaochu input:checkbox").each(function () {
				if($(this).is(':checked')){
					$(this).prop("checked", false);
				}else{
					$(this).prop("checked", true);
				}
 			});
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
			var fileType = filename.substring(filename.lastIndexOf(".")+1);
			if(fileType=''||(fileType!='rar'&&fileType!='zip')){
				alertMsg('上传文件格式不符合规范',0) ;
				//alert('请选择文件');
				return;
			}
			$('#pathPL').val(escape(filename));
			$("#stuPhoto").dialog("close");
			$('#divPageMask').show();
			$("#form3").attr("enctype","multipart/form-data");
			$("#form3").attr("encoding", "multipart/form-data");
			$("#form3").submit();
		}
	}
	
	/**导出学生基本信息*/
	function ExportExcelStudent(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_XsxxSet',
			data : 'active=ExportExcelStudent',
			dataType:"json",
			success : function(data) {
				$('#divPageMask').hide();
				if(data[0].MSG == '文件生成成功'){
					//下载文件到本地
					$("#exportIframe").attr("src", '<%=request.getContextPath()%>/form/scoreQuery/download.jsp?filePath=' + encodeURI(encodeURI(data[0].filePath)));
				}else{
					alertMsg(data[0].MSG);
				}
			}
		});
	}
	
	$('#form2').form({
   		//定位到servlet位置的url
   		url:'<%=request.getContextPath()%>/Svl_XsxxSet',
   		//当点击事件后触发的事件
   		onSubmit : function(data) {
   			$('#File').hide();
   			$('.easyui-linkbutton').linkbutton('disable');
   		},
   		//当点击事件并成功提交后触发的事件
   		success : function(data) {
   			$('#divPageMask').hide();
   			$('#form2').attr("enctype","application/x-www-form-urlencoded");
   			$('#form2').attr("encoding", "application/x-www-form-urlencoded");
   			$('#File').show();
   			$('.easyui-linkbutton').linkbutton('enable');
   			//根据记录状态，判断页面显示数据
   			var json = eval("(" + data + ")");
   			var afile = $("#File1");
   			afile.replaceWith(afile.clone());
   			
   			if(json[0].MSG == '导入成功'){//反馈信息不为空
				showMsg(json[0].MSG);
				
				if(json[0].PromptMsg!=undefined && json[0].PromptMsg!=''){
					var msgArray = json[0].PromptMsg.split('@');
					alertMsg('<font color="red">'+msgArray[0]+'</font><br/>'+msgArray[1]);//返回提示信息
				}
				LoadDatagrid();
			}else{
				alertMsg(json[0].MSG);
			}
   			$('#File1').val("");
   		}
    });
    
    function AbnSubmit(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_XsxxSet',
			data : 'active=save_Abn&JX_XJH=' + rowInfo.学籍号 + '&JX_XH=' + rowInfo.学号 + '&JX_XM=' + rowInfo.姓名 + '&JX_SFZJH=' + rowInfo.身份证件号 +
				'&JX_YDLXM='+$('#JX_YDLXM').combobox('getValue') + '&JX_YDRQ='+$('#JX_YDRQ').datebox('getValue') +
				'&JX_YZYM=' + rowInfo.专业代码 + '&JX_XZYM=' + $('#JX_NEWZY').combobox('getValue') + 
				'&JX_YBH=' + rowInfo.行政班代码 + '&JX_XBH=' + $('#JX_NEWBJ').combobox('getValue') + 
				'&JX_YDQXXX=' + $('#JX_NEWXQ').textbox('getValue') + '&JX_YDYY=' + $('#JX_YDYY').val(), 
			dataType:"json",
			success : function(data) {
				if(data[0].MSG=="保存成功"){
					showMsg(data[0].MSG);
					LoadAbnDatagrid();
					LoadDatagrid();
					$('#Abndialog').dialog('close');
				}else{
					alertMsg(data[0].MSG);
				}
			}
		});
	}

	/**导出学生信息*/
	function ExportExcelStudentall(lieInfo){
		$('#stuExportMask').show();
	
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_XsxxSet',
			data : 'active=ExportExcelStuScoreall&lieInfo='+lieInfo+'&bjbh='+$('#DC_BJBH').combobox('getText')+'&bjdm='+$('#DC_BJBH').combobox('getValue'),
			dataType:"json",
			success : function(data) {
				$('#stuExportMask').hide();
				if(data[0].MSG == '文件生成成功'){
					//下载文件到本地
					$("#exportIframe").attr("src", '<%=request.getContextPath()%>/form/scoreQuery/download.jsp?filePath=' + encodeURI(encodeURI(data[0].filePath)));
				}else{
					alertMsg(data[0].MSG);
				}
			}
		});
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
	
	
	//页面初始化加载数据
	function initGridData(){ 
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
		
		
	function loadEx(){
		if(ostype==1){
			$('#stuPhoto').dialog({   
				title: '批量上传学生照片',   
				width: 266,//宽度设置   
				height: 125,//高度设置   110
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
		}else if(ostype==3){
			$('#stuPhoto').dialog({   
				title: '批量上传学生照片',   
				width: 284,//宽度设置   
				height: 115,//高度设置  105
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
		}else{
			$('#stuPhoto').dialog({   
				title: '批量上传学生照片',   
				width: 275,//宽度设置   260
				height: 132,//高度设置    105
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
	}
		
	/**表单提交 批量上传学生照片**/
	$('#form3').form({
		//定位到servlet位置的url
		url:'<%=request.getContextPath()%>/Svl_XsxxSet?active=uploadAllstuPhoto&pathPL='+escape($('#pathPL').val()),
		//当点击事件后触发的事件
		onSubmit: function(data){
			return $(this).form('validate');//验证
		}, 
		//当点击事件并成功提交后触发的事件
		success:function(data){ 
			$("#form3").attr("enctype","application/x-www-form-urlencoded");
			$("#form3").attr("encoding", "application/x-www-form-urlencoded");
			var json = eval("("+data+")");
			if(json[0].MSG == '上传失败'){
				$('#divPageMask').hide();
				alertMsg(json[0].MSG);
			}else{
				$('#divPageMask').hide();
				alertMsg(json[0].MSG);
				LoadDatagrid();
			}
		}
	});
	
	
	//=======================================================
  	function emptyFile(){
  		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_XsxxSet',
			data : 'active=emptyfile',
			dataType:"json",
			success : function(data){
			}
		});
  	}
  //=======================================================
  	</script>
</html>
