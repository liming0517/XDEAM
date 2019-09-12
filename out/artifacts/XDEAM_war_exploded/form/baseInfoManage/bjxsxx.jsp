<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="java.util.Vector"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.src.develop.store.user.*"%>
<%
	/**
		创建人：yeq
		Create date: 2016.01.08
		功能说明：用于设置班级信息
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
	<title>教学班信息列表</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/icon.css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
 	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/publicScript.js"></script>
  </head>
  <style>
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
  </style>
 <%
	/*
		获得角色信息
	 */
	UserProfile up = new UserProfile(request,
			MyTools.getSessionUserCode(request));
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
	if(v!=null && v.size()>0){
		for(int i=0; i<v.size(); i++){
			sAuth += "@"+MyTools.StrFiltr(v.get(i))+"@O";
		}
		sAuth = sAuth.substring(0, sAuth.length()-1);
	}
	// 如果无法获取人员信息，则自动跳转到登陆界面
%>
<body  class="easyui-layout">
	<div id="north" region="north" title="班级信息" style="height:91px; overflow:hidden;" >
		<table>
			<tr>
				<td><a href="#" onclick="doToolbar(this.id)" id="exportdialog" class="easyui-linkbutton" plain="true" iconcls="icon-submit">导出</a></td>
			</tr>
		</table>
		<table id="ee" singleselect="true" width="100%" class="tablestyle">
			<tr>
				<td style="width:90px;" class="titlestyle">年级名称</td>
				<td>
					<select name="NJDM_CX" id="NJDM_CX" class="easyui-combobox" style="width:170px;">
					</select>
				</td>
				<td style="width:90px;" class="titlestyle">所属系部</td>
				<td>
					<select name="XBDM_CX" id="XBDM_CX" class="easyui-combobox" style="width:170px;">
					</select>
				</td>
				<td style="width:90px;" class="titlestyle">所属专业</td>
				<td>
					<select name="SSZY_CX" id="SSZY_CX" class="easyui-combobox" style="width:170px;">
					</select>
				</td>
				<td style="width:90px;" class="titlestyle">班级名称</td>
				<td>
					<select name="BJMC_CX" id="BJMC_CX" class="easyui-combobox" style="width:170px;">
					</select>
				</td>
				<td colspan="2">
					<a href="#" onclick="doToolbar(this.id)" id="query" class="easyui-linkbutton" plain="true" iconcls="icon-search">查询</a>
				</td>				
			</tr>
	    </table>
	</div>
	<div region="center">
		<table id="classList" style="width:100%;"></table>
	</div>
	<!-- 学生名单 -->
	<div id="xsmdDialog">
		<div class="easyui-layout" style="height:100%; width:100%;">
			<!-- <div region="north">
				<table width="100%" class="tablestyle">
					<tr>
						<td style="width:110px;" class="titlestyle">学籍号</td>
						<td>
							<input id="XQ_XJH" name="XQ_XJH" class="easyui-textbox"/>
						</td>
						<td colspan="2" class="titlestyle" style="width:30px; text-align:center;">
							<a href="#" id="que_XSXQ" class="easyui-linkbutton" plain="true" iconCls="icon-search" onclick="doToolbar(this.id);">查询</a>
						</td>
		   			
					    </tr>
				</table>
			</div>
			 --><div region="center">
				<table id="xsmdList" style="width:100%;">
				</table>
			</div>
			</div>
	</div>
	
	
	<!-- 学生详情 -->
	<div id="DIA_Lead" style="overflow:hidden;">
		<form method="post" id="form1" name="form1">
			<div id="tt" class="easyui-tabs" style="width:586px; height:368px; background:#efefef;">
				<div title="基础信息" style="background-color:#EFEFEF;">
					<table id="stuBaseInfo" class="tablestyle" style="width:100%;">
						<tr>
				   			<td class="titlestyle" style="width:100px;">
				   				姓名
				   			</td>
				   			<td id="JX_XM" name="JX_XM"> 
				   			</td>
				   			<td rowspan="11" style="padding-left:40px;">
				   				<div id="Img" style="text-align:center">
				   					<img style="width:90px; height:115px;" src="<%=request.getContextPath()%>/images/nopic1.png"/>
				   				</div>
				   			 </td>
				   		</tr>
				   		<tr>
				   			<td class="titlestyle">
				   				学籍号
				   			</td>
				   			<td id="JX_XJH" name="JX_XJH">
				   			</td>
				   		</tr>
				   		<tr>
				   			<td class="titlestyle">
				   				学号
				   			</td>
				   			<td id="JX_XH" name="JX_XH">
				   			</td>
				   		</tr>
				   		<tr>
				   			<td class="titlestyle">拍照号</td>
				   			<td id="JX_PZH" name="JX_PZH">
				   			</td>
				   		</tr>	
				   		<tr>
				   			<td class="titlestyle">行政班</td>
				   			<td id="JX_XZBDM" name="JX_XZBDM">
					   		</td>
				   		</tr>
				   		<tr>
				   			<td class="titlestyle">班内学号</td>
				   			<td id="JX_BNXH" name="JX_BNXH">
					   		</td>
				   		</tr>
				   		<tr>
				   			<td class="titlestyle">专业名称</td>
				   			<td id="JX_XZZYM" name="JX_XZZYM">
				   			</td>
				   		</tr>
				   		<tr>
				   			<td class="titlestyle">姓名拼音</td>
				   			<td id="JX_XMPY" name="JX_XMPY">
				   			</td>
				   		</tr>
				   		<tr>
				   			<td class="titlestyle">曾用名</td>
				   			<td id="JX_CYM" name="JX_CYM">
				   			</td>
				   		</tr>
				   		<tr>
				   			<td class="titlestyle">招生类别</td>
				   			<td id="JX_ZSLBM" name="JX_ZSLBM">
				   			</td>
				   		</tr>
				   		<tr>
				   			<td class="titlestyle">性别</td>
				   			<td id="JX_XBM" name="JX_XBM">
				   			</td>
				   		</tr>
					</table>
				</div>
				<div title="扩展信息" style="background-color:#EFEFEF;">
					<table id="stuExtendInfo" class="tablestyle" style="width:100%; height:100%;">
						<tr>
							<td class="titlestyle">出生日期</td>
				   			<td id="JX_CSNY" name="JX_CSNY">
				   			</td>
				   			<td class="titlestyle" style="width:110px;">入学日期</td>
				   			<td style="width:178px;" id="JX_RXRQ" name="JX_RXRQ">
				   			</td>
						</tr>
				   		<tr>
				   			<td class="titlestyle">身份证件类型</td>
				   			<td id="JX_SFZJLXM" name="JX_SFZJLXM">
				   			</td>
				   			<td class="titlestyle">身份证件号</td>
				   			<td id="JX_SFZJH" name="JX_SFZJH">
				   			</td>
				   		</tr>
				   		<tr>
				   			<td class="titlestyle" style="width:100px;">学生类别</td>
				   			<td style="width:188px;" id="JX_XSLBM" name="JX_XSLBM">
				   			</td>
				   			<td class="titlestyle">港澳台侨外</td>
				   			<td id="JX_GATQM" name="JX_GATQM"> 
				   			</td>
				   		</tr>
				   		<tr>
				   			<td class="titlestyle">民族</td>
				   			<td id="JX_MZM" name="JX_MZM">
				   			</td>
				   				<td class="titlestyle">血型</td>
				   			<td id="JX_XXM" name="JX_XXM">
				   			</td>
				   		</tr>
				   		<tr>
				   			<td class="titlestyle">政治面貌</td>
				   			<td id="JX_ZZMMM" name="JX_ZZMMM"> 
				   			</td>
				   			<td class="titlestyle">国籍</td>
				   			<td  id="JX_GBM" name="JX_GBM">
				   			</td>
				   		</tr>
				   		<tr>
				   			<td class="titlestyle">健康状况</td>
				   			<td id="JX_JKZKM" name="JX_JKZKM">
				   			</td>
				   			<td class="titlestyle">籍贯</td>
				   			<td id="JX_JGM" name="JX_JGM">
				   			</td>
				   		</tr>
				   		<tr>
				   			<td class="titlestyle">婚姻状况</td>
				   			<td id="JX_HYZK" name="JX_HYZK">
				   			</td>
				   			<td class="titlestyle">出生地</td>
				   			<td id="JX_CSDZM" name="JX_CSDZM" >
				   			</td>
				   		</tr>
				   		<tr>
				   			<td class="titlestyle">户籍地址</td>
				   			<td colspan="3" id="JX_HKSZD" name="JX_HKSZD">
				   			</td>
				   		</tr>
				   		<tr>
				   			<td class="titlestyle">户籍邮编</td>
				   			<td id="JX_YZBM" name="JX_YZBM">
				   			</td>
				   			<td class="titlestyle">户口类别</td>
				   			<td id="JX_HKLBM" name="JX_HKLBM">
				   			</td>
				   		</tr>
				   		<tr>
				   			<td class="titlestyle">流动人口状况</td>
				   			<td id="JX_LDRKZK" name="JX_LDRKZK">
				   			</td>
				   			<td class="titlestyle">所属派出所</td>
				   			<td id="JX_SSPCS" name="JX_SSPCS">
				   			</td>
				   		</tr>
				   		<tr>
				   			<td class="titlestyle">毕业学校</td>
				   			<td id="JX_BYXX" name="JX_BYXX">
				   			</td>
				   			<td class="titlestyle">毕业年份</td>
				   			<td id="JX_BYNF" name="JX_BYNF">
				   			</td>
				   		</tr>
				   		<tr>
				   			<td class="titlestyle">备注</td>
				   			<td colspan="3" id="JX_BZ" name="JX_BZ">
				   			</td>
				   		</tr>
					</table>
				</div>
				<div title="家长信息" style="background-color:#EFEFEF;">
					<table id="stuParentsInfo" class="tablestyle" style="width:100%;">
						<tr>
				   			<td class="titlestyle" style="width:110px;">父亲姓名</td>
				   			<td id="JX_FQXM" name="JX_FQXM" style="width:180px;">
				   			</td>
				   			<td class="titlestyle" style="width:110px;">父亲年龄</td>
				   			<td id="JX_FQNL" name="JX_FQNL" style="width:180px;">
				   			</td>
				   		</tr>
				   		<tr>
				   			<td class="titlestyle">父亲政治面貌</td>
				   			<td id="JX_FQZZMMM" name="JX_FQZZMMM" style="width:180px;">
				   			</td>
				   			<td class="titlestyle">父亲电话</td>
				   			<td id="JX_FQDH" name="JX_FQDH" style="width:180px;">
				   			</td>
				   		</tr>
				   		<tr>
				   			<td class="titlestyle">父亲工作单位</td>
				   			<td  colspan="3" id="JX_FQGZDW" name="JX_FQGZDW" style="width:180px;">
				   			</td>
				   			
				   		</tr>
				   		<tr>
				   			<td class="titlestyle">母亲姓名</td>
				   			<td id="JX_MQXM" name="JX_MQXM" style="width:180px;">
				   			</td>
				   			<td class="titlestyle">母亲年龄</td>
				   			<td id="JX_MQNL" name="JX_MQNL" style="width:180px;">
				   			</td>
				   		</tr>
				   		<tr>
				   			<td class="titlestyle">母亲政治面貌</td>
				   			<td id="JX_MQZZMMM" name="JX_MQZZMMM" style="width:180px;">
				   			</td>
				   			<td class="titlestyle">母亲电话</td>
				   			<td  id="JX_MQDH" name="JX_MQDH" style="width:180px;">
				   			</td>
				   		</tr>
				   		<tr>
				   			<td class="titlestyle">母亲工作单位</td>
				   			<td  colspan="3" id="JX_MQGZDW" name="JX_MQGZDW" style="width:180px;">
				   			</td>
				   		</tr>
				   		
				   		<tr>
				   			<td class="titlestyle">其他成员称谓</td>
				   			<td id="JX_QTCYCW" name="JX_QTCYCW" style="width:180px;">
				   			</td>
				   			<td class="titlestyle">其他成员姓名</td>
				   			<td id="JX_QTCYXM" name="JX_QTCYXM" style="width:180px;">
				   			</td>
				   		</tr>
				   		<tr>
				   			<td class="titlestyle">其他成员政治面貌</td>
				   			<td id="JX_QTCYZZMMM" name="JX_QTCYZZMMM" style="width:180px;">
				   			</td>
				   			<td class="titlestyle">其他成员年龄</td>
				   			<td id="JX_QTCYNL" name="JX_QTCYNL" style="width:180px;">
				   			</td>
				   		</tr>
				   		<tr>
				   			<td class="titlestyle">其他成员工作单位</td>
				   			<td id="JX_QTCYGZDW" name="JX_QTCYGZDW" style="width:180px;">
				   			</td>
				   			<td class="titlestyle">其他成员电话</td>
				   			<td id="JX_QTCYDH" name="JX_QTCYDH" style="width:180px;">
				   			</td>
				   		</tr>
					</table>
				</div>
			</div>
			<input type="hidden" id="active" name="active" />
		   	<input type="hidden" id="XSBH" name="XSBH" />
		</form>
	</div>
	
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
	   	<!-- 下载excel -->
		<iframe id="exportIframe" src="" style="width:0; height:0;"></iframe>
</body>
<script type="text/javascript">
	var sAuth = '<%=sAuth%>';
	var row = '';      //行数据
	var Sturow='';//学生行数据
	var iKeyCode = ''; //定义主键
	var iStuCode='';//学生名单主键
	var pageNum = 1;   //datagrid初始当前页数
	var pageSize = 20; //datagrid初始页内行数
	var pageNum_XSXX = 1;   //datagrid初始当前页数
	var pageSize_XSXX = 20; //datagrid初始页内行数
	var NJDM_CX = '';//查询条件
	var XBDM_CX = '';
	var SSZY_CX = '';
	//var BJBH_CX = '';
	var BJMC_CX = '';
	var bzrgh="";//班主任工号
	var rowInfo="";//学生行数据
	var PhotoPath = '<%=MyTools.getProp(request, "Base.PhotoPath")%>';
	var lieInfo=new Array();//获取导出的列
	$(document).ready(function(){
		initDialog();//初始化对话框
		initData();//页面初始化加载数据
		});
	
	/**页面初始化加载数据**/
	function initData(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Bjxsxx',
			data : 'active=initData&page=' + pageNum + '&rows=' + pageSize+'&sAuth=' + sAuth,
			dataType:"json",
			success : function(data) {
				loadGrid(data[0].listData);
				initCombobox(data[0].njdmData, data[0].xbdmData, data[0].sszyData, data[0].bjlxData );
			}
		});
	}
	

	function initCombobox(njdmData, xbdmData, sszyData, bjlxData){
		$('#NJDM_CX').combobox({
			data:njdmData,
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
	
		
		$('#XBDM_CX').combobox({
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
			}
		});
	
		
		$('#SSZY_CX').combobox({
			data:sszyData,
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

		

		$('#BJMC_CX').combobox({
			data:bjlxData,
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
		
		$('#DC_BJBH').combobox({
			data : bjlxData,
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
			height: 400,//高度设置 
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
				$('#form1').form('clear');
				$('#tt').tabs('select','基础信息');
			}
		});
		$('#xsmdDialog').dialog({
			title: '学生名单',
			width: 900,//宽度设置   
			height: 500,//高度设置 
			modal:true,
			closed: true,   
			cache: false, 
			draggable:true,//是否可移动dialog框设置
			//打开事件
			onOpen:function(data){
			},
			//读取事件
			onLoad:function(data){
			},
			//关闭事件
			onClose:function(data){
			bdtARRAY.length = 0;
			//loadData();
				$('#xsmdList').datagrid('loadData',{total:0,rows:[]});
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
}
	/**加载 datagrid控件，读取页面信息
		@listData 列表数据
	**/
	function loadGrid(listData){
		$('#classList').datagrid({
			data:listData,
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
				{field:'BJBH',title:'班级编号',width:fillsize(0.12),align:'center'},
				{field:'BJMC',title:'班级名称',width:fillsize(0.15),align:'center'},
				{field:'NJDM',title:'年级代码',hidden:true},
				{field:'NJMC',title:'年级名称',width:fillsize(0.1),align:'center'},
				{field:'XBDM',title:'系部代码',hidden:true},
				{field:'XBMC',title:'系部名称',width:fillsize(0.1),align:'center'},
				{field:'BZRGH',title:'班主任工号',hidden:true},
				{field:'ZYDM',title:'所属专业',hidden:true},
				{field:'ZYMC',title:'专业名称',width:fillsize(0.15),align:'center'},
				{field:'col',title:'操作',width:fillsize(0.12),align:'center',formatter:function(value,rec){
					return "<input type='button' value='[学生名单]' onclick='loadStuList(\"" + rec.BJBH +"\",\"" + rec.BJMC +"\")' style='cursor:pointer;'>";
				}}
			]],
			onClickRow:function(rowIndex,rowData){
				//主键赋值
				iKeyCode = rowData.BJBH;
				row = rowData;
				jsbh=rowData.JSBH;
				jsmc=rowData.JSMC;
				$('#BZRGH').val(rowData.BZRGH);
			},
			//当用户双击某行时触发
			onDblClickRow:function(rowIndex, rowData){
				doToolbar('edit');
			},
			//加载成功后触发
			onLoadSuccess: function(data){
				iKeyCode = '';
			}
		});
		
		$("#classList").datagrid("getPager").pagination({
			total:listData.total,
			afterPageText: '页&nbsp;<a href="#" onclick="goEnterPage();">Go</a>&nbsp;&nbsp;&nbsp;共 {pages} 页',
			onSelectPage:function (pageNo, pageSize_1) { 
				pageNum = pageNo;
				pageSize = pageSize_1;
				loadData();
			} 
		});
	};
	
	function goEnterPage(){
		var e = jQuery.Event("keydown");//模拟一个键盘事件
		e.keyCode = 13;//keyCode=13是回车 
		$("input.pagination-num").trigger(e);//模拟页码框按下回车 
	}
	
	/**读取datagrid数据**///+ '&BJBH_CX=' + encodeURI(BJBH_CX) 
	function loadData(){
		$.ajax({
			type:"POST",
			url:'<%=request.getContextPath()%>/Svl_Bjxsxx',
			data:'active=query&page=' + pageNum + '&rows=' + pageSize + '&BJBH='+$("#BJMC_CX").combobox('getValue') + '&NJDM=' + $("#NJDM_CX").combobox('getValue') + '&XBDM=' + $("#XBDM_CX").combobox('getValue') + '&SSZY=' + $("#SSZY_CX").combobox('getValue')+'&sAuth=' + sAuth ,
			dataType:"json",
			success:function(data) {
				loadGrid(data[0].listData);
			}
		});
	}
	
	//查询学生名单
  	function loadStuList(classCode,BJMC){
  		$('#xsmdDialog').dialog('open');
  	//	init(classCode,njdm,xbdm);
  		$('#xsmdList').datagrid({
  			url:'<%=request.getContextPath()%>/Svl_Bjxsxx',
			queryParams:{"active":"loadStuList","BJBH":classCode,},
			width:'100%',
			nowrap: false,
			fit:true, //自适应父节点宽度和高度
			showFooter:true,
			striped:true,
			pageSize:pageSize_XSXX, 
			pageNumber:pageNum_XSXX,
			//pagination:true,
			singleSelect:true,
			rownumbers:true,
			fitColumns: true,
			columns:[[
				{field:'XH',title:'学号',width:fillsize(0.25),align:'center'},
				{field:'XM',title:'姓名',width:fillsize(0.25),align:'center'},
				{field:'ZYMC',title:'专业名称',width:fillsize(0.25),align:'center'},
				{field:'ZSLB',title:'招生类别',width:fillsize(0.25),align:'center'},
				{field:'col',title:'操作',width:fillsize(0.12),align:'center',formatter:function(value,rec){
					return "<input type='button' value='[详情]' onclick='loadStuListXQ(\"" + rec.XH +"\",\"" + rec.ZYMC +"\",\"" + rec.ZSLB +"\",\"" + BJMC +"\")' id='setStuXQ' style='margin-left:5px; cursor:pointer;'>";
				}}
			]],
			onClickRow:function(rowIndex,rowData){
				//主键赋值
				iStuCode = rowData.XH;
				Sturow = rowData;
			},
			//加载成功后触发
			onLoadSuccess: function(data){
				iStuCode = '';
			}
		});
  	}
  	
 	/**学生名单详情**/
	function loadStuListXQ(XH,zymc,ZSLB,BJMC){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Bjxsxx',
			data : 'active=StuXq&JX_XJH=' + XH,
			dataType:"json",
			success : function(data) {
				if(ZSLB=="undefined" || ZSLB==''){
					ZSLB="";
				}
				rowInfo=data[0];
				$('#JX_XJH').html(rowInfo.学籍号);
				$('#JX_XM').html(rowInfo.姓名);
				$('#JX_YXH').html(rowInfo.学号);
				$('#JX_XH').html(rowInfo.学号);
				$('#JX_PZH').html(rowInfo.拍照号);
				$('#JX_XZBDM').html(BJMC);
				$('#JX_BNXH').html(rowInfo.班内学号);
				$('#JX_XZZYM').html(zymc);
				$('#JX_XMPY').html(rowInfo.姓名拼音);
				$('#JX_CYM').html(rowInfo.曾用名);
				$('#JX_XBM').html(rowInfo.性别名称);
				$('#JX_RXRQ').html(rowInfo.入学日期);
				$('#JX_BYNF').html(rowInfo.毕业年份);
				$('#JX_XSLBM').html(rowInfo.学生类别);
				$('#JX_SFZJH').html(rowInfo.身份证件号);
				$('#JX_CSNY').html(rowInfo.出生日期);
				$('#JX_MZM').html(rowInfo.民族);
				$('#JX_XXM').html(rowInfo.血型);
				$('#JX_GATQM').html(rowInfo.港澳台侨外);
				$('#JX_ZZMMM').html(rowInfo.政治面貌);
				$('#JX_FQZZMMM').html(rowInfo.父亲政治面貌);
				$('#JX_MQZZMMM').html(rowInfo.母亲政治面貌);
				$('#JX_QTCYZZMMM').html(rowInfo.其他成员政治面貌);
				$('#JX_GBM').html(rowInfo.国籍);
				$('#JX_JKZKM').html(rowInfo.健康状况);
				$('#JX_JGM').html(rowInfo.籍贯);
				$('#JX_HYZK').html(rowInfo.婚姻状况);
				$('#JX_CSDZM').html(rowInfo.出生地);
				$('#JX_BYXX').html(rowInfo.毕业学校);
				$('#JX_HKSZD').html(rowInfo.户籍地址);
				$('#JX_HKLBM').html( rowInfo.户口类别);
				$('#JX_LDRKZK').html(rowInfo.是否是流动人口);
				$('#JX_SSPCS').html(rowInfo.所属派出所);
				$('#JX_YZBM').html(rowInfo.户籍邮编);
				$('#JX_ZSLBM').html(ZSLB);
				$('#JX_PZH').html( rowInfo.拍照号);
				$('#JX_SFZJLXM').html( rowInfo.身份证件);
				$('#JX_QTCYDH').html(rowInfo.其他成员电话);
				$('#JX_QTCYNL').html(rowInfo.其他成员年龄);
				$('#JX_QTCYGZDW').html(rowInfo.其他成员工作单位);
				$('#JX_QTCYCW').html( rowInfo.其他成员称谓);
				$('#JX_QTCYXM').html(rowInfo.其他成员姓名);	
				$('#JX_MQDH').html(rowInfo.母亲电话);
				$('#JX_MQNL').html(rowInfo.母亲年龄);
				$('#JX_MQGZDW').html(rowInfo.母亲工作单位);
				$('#JX_MQXM').html(rowInfo.母亲);
				$('#JX_FQXM').html(rowInfo.父亲);
				$('#JX_FQNL').html(rowInfo.父亲年龄);
				$('#JX_FQDH').html(rowInfo.父亲电话);
				$('#JX_FQGZDW').html(rowInfo.父亲工作单位);
				$('#JX_BZ').html(rowInfo.备注);	
 				if(rowInfo.照片路径!=undefined && rowInfo.照片路径!=''){
 					$('#Img').html("<img style='width:90px; height:115px;' src='" + PhotoPath+rowInfo.照片路径 + "'/>");
				}
				$("#DIA_Lead").dialog('setTitle', rowInfo.姓名+'个人信息');
				$("#DIA_Lead").dialog("open");
			}
		});
	}
	function ExportExcelStudentall(lieInfo){
		$('#stuExportMask').show();
	
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Bjxsxx',
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

	/**工具栏按钮调用方法，传入按钮的id
		@id 当前按钮点击事件
	**/
	function doToolbar(id){
	//查询
		if(id == 'query'){
			NJDM_CX = $('#NJDM_CX').combobox('getValue');
			XBDM_CX = $('#XBDM_CX').combobox('getValue');
			SSZY_CX = $('#SSZY_CX').combobox('getValue');
			BJMC_CX = $('#BJMC_CX').combobox('getValue');
			loadData();
		}
		
		if(id == "exportdialog"){
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
		//导出
		if(id == "exportStu"){
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
   			 if($('#DC_BJBH').combobox('getValue')==""){
   			 	alertMsg("请选择班级");
   			 	return;
   			 }
   			 ExportExcelStudentall(lieInfo);
		}
		//导出全选
		if(id == "selAllKs"){
		
		 $("#tabdaochu input:checkbox").each(function () {
       		 $(this).attr("checked",true);
  			 });
  			
  			 $("#tabdaochu input:checkbox").prop('checked', true);
		}
		//导出反选
		if(id == 'selOther'){
			$("#tabdaochu input:checkbox").each(function () {
				if($(this).is(':checked')){
					$(this).prop("checked", false);
				}else{
					$(this).prop("checked", true);
				}
 			});
		}
	}

</script>
</html>