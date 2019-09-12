<%
/*
文件说明
Create date: 2012.09.20
页面类型:联系单
创建人：zhangjiping
*/
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<!-- 设置字符集 utf-8 -->
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!--用户区 ：JAVA引用-这里可以列出需要引用的包  -->
<%@ page import="com.pantech.base.common.tools.MyTools" %>
<%@ page import="com.pantech.base.common.tools.PublicTools"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.*"%>
<%@ page import="com.pantech.base.common.db.DBSource" %>
<!--用户区 ：JAVA引用-结束  -->
<html>
	<head>
		<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<!-- JQuery 专用的文件 -->
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/icon.css">
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
	</head>
<!-- form  表单开始 post方式提交 -->
	<form method="Post" id="form1" >
		<body class="easyui-layout"><!-- easyui-layout  页面布局样式 -->
			<div region = "north" align = "left" title ="联系单"style = "background:#fafafa;height:68px;width:100%;">
				<!-- 菜单区域 -->
				<div  style="padding:5px;background:#fafafa;height:40px;width:100%;">
					<a href="#" onclick="doToolbar(this.id);" id="save"  class="easyui-linkbutton" plain="true" iconCls="icon-save">保存</a>
					<a href="#" onclick="doToolbar(this.id);" id="fankui"  class="easyui-linkbutton" plain="true" iconCls="icon-new">反馈</a>
					<a href="#" onclick="doToolbar(this.id);" id="print"  class="easyui-linkbutton" plain="true" iconCls="icon-print">打印</a>
				</div>
			</div>
			<div  region = "center" align = "left" style = "background:#fafafa;width:100%;">
					<table class = "tablestyle" width = "100%">
						<tr>
							<td colspan="5" width="80%" align="center">
							<span id="icWC_YEAR" name="icWC_YEAR" style="font-family:'黑体';font-size:20px;" ></span>
							<span style="font-family:'黑体';font-size:20px">年度信息化“联系人”制度联系情况表</span></td>
							<td id="icWC_CODE1" name="icWC_CODE1" style="font-family:'黑体';font-size:20px;" align="center"></td>
						</tr>
						<tr>
							<td class="titlestyle" width="16%">
								联系单位名称
							</td>
							<td width="16%">
								<input id="icWC_NAME" name="icWC_NAME"  type="text" style="width:100%;" maxlength="100"/>
							</td>
							<td class="titlestyle" width="16%">
								联系时间
							</td>
							<td width="16%">
								<input class="easyui-datebox" id="icWC_TIME" name="icWC_TIME" type="text" style="width:235%;" editable="false" />
							</td>
							<td class="titlestyle" rowspan="2" width="16%">
								联系方式
							</td>
							<td width="16%">
								<input type="checkbox" id="icWC_LXFS1" name="icWC_LXFS1" value="1" />电话联系
							</td>
						</tr>

						<tr>
							<td class="titlestyle" width="16%">
								联系人
							</td>
							<td width="16%">
								<input id="icWC_LXR" name="icWC_LXR" type="easyui-combobox" style="width:235%" maxlength="10"/>
							</td>
							<td class="titlestyle" width="16%">
								受访人
							</td>
							<td>
								<input id="icWC_SFR" name="icWC_SFR" type="text" style="width:100%;" maxlength="10"/>
							</td>
							<td width="16%">
								<input type="checkbox" id="icWC_LXFS2" name="icWC_LXFS2" value="0" />实地联系
							</td>
						</tr>

						<tr>
							<td colspan="2" align="center">
								联系内容
							</td>
							<td colspan="4" align="center">
								联系结果
							</td>
						</tr>

						<tr>
							<td class="titlestyle" width="16%">
								重点工作
							</td>
							<td width="16%">
								<textarea id="icWC_ZDGZ1" name="icWC_ZDGZ1" style="overflow:auto;width:100%" rows="4"></textarea>
							</td>
							<td colspan="4" width="64%">
								<textarea id="icWC_ZDGZ2" name="icWC_ZDGZ2" style="overflow:auto;width:100%" rows="4"></textarea>
							</td>
						</tr>

						<tr>
							<td class="titlestyle" width="16%">
								常规工作
							</td>
							<td width="16%">
								<textarea id="icWC_CGGZ1" name="icWC_CGGZ1" style="overflow:auto;width:100%" rows="4"></textarea>
							</td>
							<td colspan="4" width="64%">
								<textarea id="icWC_CGGZ2" name="icWC_CGGZ2" style="overflow:auto;width:100%" rows="4"></textarea>
							</td>
						</tr>

						<tr>
							<td class="titlestyle" width="16%">
								其他工作
							</td>
							<td width="16%">
								<textarea id="icWC_QTGZ1" name="icWC_QTGZ1" style="overflow:auto;width:100%" rows="4"></textarea>
							</td>
							<td colspan="4" width="64%">
								<textarea id="icWC_QTGZ2" name="icWC_QTGZ2" style="overflow:auto;width:100%" rows="4"></textarea>
							</td>
						</tr>

						<tr>
							<td class="titlestyle" width="16%">
								意见建议
							</td>
							<td colspan="5" width="80%">
								<textarea id="icWC_YJJY" name="icWC_YJJY" style="overflow:auto;width:100%" rows="4"></textarea>
							</td>
						</tr>

						<tr>
							<td class="titlestyle" width="16%">
								签报人
							</td>
							<td width="16%">
								<input id="icWC_QBR" name="icWC_QBR" type="text" style="width:100%;" maxlength="10"/>
							</td>
							<td class="titlestyle" width="16%">
								签报日期
							</td>
							<td width="16%">
								<input class="easyui-datebox" id="icWC_QBRQ" name="icWC_QBRQ" type="text" style="width:235%;" editable="false"/>
							</td>
							<td class="titlestyle" width="16%">
								移交日期
							</td>
							<td width="16%">
								<input class="easyui-datebox" id="icWC_YJRQ" name="icWC_YJRQ" type="text" style="width:235%;" editable="false"/>
							</td>
						</tr>

						<tr>
							<td colspan="6" align="center">反馈人填写</td>
						</tr>

						<tr>
							<td class="titlestyle" width="16%">
								反馈结果
							</td>
							<td colspan="3" width="48%">
								<textarea id="icWC_FKJG" name="icWC_FKJG" style="overflow:auto;width:100%" rows="4"></textarea>
							</td>
							<td class="titlestyle" width="16%">
								分管领导签字
							</td>
							<td width="16%">
								<textarea id="icWC_FGLDQZ" name="icWC_FGLDQZ" style="overflow:auto;width:100%;" rows="4" maxlength="10"></textarea>
							</td>
						</tr>

						<tr>
							<td class="titlestyle" width="16%">
								反馈科室负责人
							</td>
							<td width="16%"><!-- multiple表示  下拉框 可多选 -->
								<input id="icWC_FKKSFZR" name="icWC_FKKSFZR" class="easyui-combobox" style="width:235%;">
							</td>
							<td class="titlestyle" width="16%">
								反馈人
							</td>
							<td width="16%">
								<input id="icWC_FKR" name="icWC_FKR" class="easyui-combobox" style="width:235%;">
							</td>
							<td class="titlestyle" width="16%">
								反馈日期
							</td>
							<td width="16%">
								<input class="easyui-datebox" id="icWC_FKRQ" name="icWC_FKRQ" type="text" style="width:235%;" editable="false"/>
							</td>
						</tr>
						<tr>
							<td class="titlestyle" width="16%">
								对方对反馈结果的评价
							</td>
							<td colspan="5" width="80%">
								<textarea id="icWC_DFDFKJGDPJ" name="icWC_DFDFKJGDPJ" style="overflow:auto;width:100%" rows="4"></textarea>
							</td>
						</tr>

						<tr>
							<td class="titlestyle" width="16%">
								存档人
							</td>
							<td colspan="3" width="48%">
								<input id="icWC_CDR" name="icWC_CDR" type="text" style="width:100%" maxlength="10"/>
							</td>
							<td class="titlestyle" width="16%">
								存档日期
							</td>
							<td width="16%">
								<input class="easyui-datebox" id="icWC_CDRQ" name="icWC_CDRQ" type="text" style="width:235%;" editable="false"/>
							</td>
						</tr>	
					</table>
			</div>
		</body>
			<!--用户区 ：页面隐藏变量 -->
			<input id="active" name="active" type="hidden" >
	        <input id="icWC_CODE" name="icWC_CODE" type="hidden"  >
	        <input id="icUSERCODE" name="icUSERCODE"  type="hidden" >
	</form>

<script type="text/javascript">
	var ContextPath = "<%=request.getContextPath()%>"; //当前URL根路径
	var icWC_CODE = "<%=MyTools.StrFiltr(request.getParameter("icWC_CODE"))%>"; //主键 联系单编号
	var USERCODE = "<%=MyTools.getSessionUserCode(request)%>"; //当前  登陆用户
	var FKR="";//反馈人
	var FKKSFZR="";//反馈科室负责人
	var USE="";//数据库中该联系单录入人
	var WC_CDRQ="";//存档日期
	var LXR="";//联系人
	var MSG="";//提示信息
	//页面初始化
	$(document).ready(function(){
		setComboxValue();//初始化下拉框的值
		loadData();//初始化  表单的值
	});



	//form以ajax方式提交
	$('#form1').form({
		url:ContextPath + "/Svl_RemindConc",
		onSubmit: function(){
			
		},
		success:function(data){
			if ($('#active').val()=="save"){
				var json = eval("("+data+")");
				if (json[0].icWC_CODE != 'undefined'){//判断联系单编号 是否定义。
					MSG = json[0].MSG;//设置 提示信息
					icWC_CODE = json[0].icWC_CODE; //设主键值   后台sverlet  设置的联系单编号
					$("#icWC_CODE").val(icWC_CODE); //设主键值，隐含域
			 	}
			}
			loadData();//根据主键  重新加载数据
			showMsg(MSG);
			window.parent.opener.loadGrid();//调用父页面方法刷新父页面.
		},
		error:function(data){
			showMsg(MSG);
		}
	});
	//点击 按钮 事件
	function doToolbar(iToolbar){
		//保存  控制各字段长度
		if (iToolbar=="save"){
		    if(!maxLength("icWC_ZDGZ1",100,"重点工作联系内容")){
				return;
			}
			if(!maxLength("icWC_ZDGZ2",200,"重点工作联系结果")){
				return;
			}
			if(!maxLength("icWC_CGGZ1",100,"常规工作联系内容")){
				return;
			}
			if(!maxLength("icWC_CGGZ2",200,"常规工作联系结果")){
				return;
			}
			if(!maxLength("icWC_QTGZ1",100,"其他工作联系内容")){
				return;
			}
			if(!maxLength("icWC_QTGZ2",200,"其他工作联系结果")){
				return;
			}
			if(!maxLength("icWC_YJJY",200,"意见建议")){
				return;
			}
		    $('#active').val("save");
		    $('#icUSERCODE').val(USERCODE); //登陆用户 赋值，隐含域
		    $('#icWC_CODE').val(icWC_CODE); //主键 赋值，隐含域
		    $('#form1').submit();
		}

		//反馈
		if (iToolbar=="fankui"){
			// 字数验证
			if(!maxLength("icWC_FKJG",200,"反馈结果")){ 
				return;
			}
			if(!maxLength("icWC_DFDFKJGDPJ",200,"对方对反馈结果的评价")){ 
				return;
			}
			fankui();
		}
		//打印
		if (iToolbar=="print"){
			if(icWC_CODE==""){
				alertMsg("请先保存");
				return;
			}
			window.open("RemindConcPrint.jsp?icWC_CODE="+icWC_CODE);
		}
	}
	//加载数据，根据主键
	function loadData() {
		$.ajax({
			type: "POST",
			url: ContextPath + "/Svl_RemindConc",
			data: "active=view&icWC_CODE=" + icWC_CODE,  
			dataType: 'json',
			success: function(datas){
				var data = datas[0];
				$("#icWC_CODE1").html(data.WC_CODE);//编号    页面左上角 显示的联系单编号
				$("#icWC_YEAR").html(data.WC_TIME.substring(0,4));//年份
				$("#icWC_NAME").val(data.WC_NAME); //联系单位名称
				$("#icWC_TIME").datebox('setValue',data.WC_TIME); //联系时间

				//联系方式切割
				if(data.WC_LXFS!=""){
					var b =data.WC_LXFS.split(",");
					for(var i=0;i<b.length;i++){
						$("input:checkbox[value='"+b[i]+"']").attr('checked','true');
					}
				}

				$("#icWC_LXR").combobox('setValue',icWC_CODE==""?USERCODE:data.WC_LXR); //联系人
				$("#icWC_SFR").val(data.WC_SFR); //受访人
				$("#icWC_ZDGZ1").val(data.WC_ZDGZ1); //重点工作联系内容
				$("#icWC_ZDGZ2").val(data.WC_ZDGZ2); //重点工作联系结果
				$("#icWC_CGGZ1").val(data.WC_CGGZ1); //常规工作联系内容
				$("#icWC_CGGZ2").val(data.WC_CGGZ2); //常规工作联系结果
				$("#icWC_QTGZ1").val(data.WC_QTGZ1); //其他工作联系内容
				$("#icWC_QTGZ2").val(data.WC_QTGZ2); //其他工作联系结果
				$("#icWC_YJJY").val(data.WC_YJJY); //意见建议
				$("#icWC_QBR").val(data.WC_QBR); //签报人
				$("#icWC_QBRQ").datebox('setValue',data.WC_QBRQ); //签报日期
				$("#icWC_YJRQ").datebox('setValue',data.WC_YJRQ); //移交日期
				$("#icWC_FKJG").val(data.WC_FKJG); //反馈结果
				$("#icWC_FGLDQZ").val(data.WC_FGLDQZ); //分管领导签字
				unionFartherComboboxFKKSFZR(data.WC_FKKSFZR.split(','),data.WC_FKR.split(','));//反馈可是负责人和反馈人赋值
				$("#icWC_FKRQ").datebox('setValue',data.WC_FKRQ); //反馈日期
				$("#icWC_DFDFKJGDPJ").val(data.WC_DFDFKJGDPJ); //对方对反馈结果的评价
				$("#icWC_CDR").val(data.WC_CDR); //存档人
				$("#icWC_CDRQ").datebox('setValue',data.WC_CDRQ); //存档日期
				$("#icUSERCODE").val(data.USERCODE);//USERCODE

				USE=data.USERCODE;
				FKKSFZR=data.WC_FKKSFZR;//反馈科室负责人
				FKR=data.WC_FKR;//反馈人
				WC_CDRQ=data.WC_CDRQ;//存档日期
				LXR=data.WC_LXR;//  联系人
				doToolbarAuth(); //按钮 显示 控制
			}
		});
	}
	//按钮全暗
	function doToolbarAuth1(){
		$("#save").attr({"disabled":"disabled"});//保存按钮不可用（暗）
		$("#fankui").attr({"disabled":"disabled"});//反馈按钮不可用（暗）
	}
	//按钮亮暗控制
	function doToolbarAuth(){
		doToolbarAuth1();//如果当前登录用户 是数据录入用户  并且该联系单 没有存档 那么 保存按钮  可用
		if(USERCODE==USE||USE==""||USERCODE==LXR){
			$("#save").removeAttr("disabled");
			$("#icWC_FKJG").attr({"disabled":"disabled"});
			$("#icWC_FGLDQZ").attr({"disabled":"disabled"});
			$("#icWC_FKRQ").datetimebox("disable");
			$("#icWC_DFDFKJGDPJ").attr({"disabled":"disabled"});
			$("#icWC_CDR").attr({"disabled":"disabled"});
			$("#icWC_CDRQ").datetimebox("disable");
		}
		//切割数据库中负责人
		if(FKR!=""){ //判断 反馈人是否为空
			var a=FKR.split(",");
			for(var i=0;i<a.length;i++){
				if(USERCODE==a[i]){//控制反馈按钮，当前登录用户 如果是反馈 人，那么反馈按钮 可用
					$("#fankui").removeAttr("disabled");
					$("#icWC_NAME").attr({"disabled":"disabled"});
					$("#icWC_TIME").datetimebox("disable");
					$("#icWC_LXFS1").attr({"disabled":"disabled"});
					$("#icWC_LXR").combobox("disable");
					$("#icWC_SFR").attr({"disabled":"disabled"});
					$("#icWC_LXFS2").attr({"disabled":"disabled"});
					$("#icWC_ZDGZ1").attr({"disabled":"disabled"});
					$("#icWC_ZDGZ2").attr({"disabled":"disabled"});
					$("#icWC_CGGZ1").attr({"disabled":"disabled"});
					$("#icWC_CGGZ2").attr({"disabled":"disabled"});
					$("#icWC_QTGZ1").attr({"disabled":"disabled"});
					$("#icWC_QTGZ2").attr({"disabled":"disabled"});
					$("#icWC_YJJY").attr({"disabled":"disabled"})
					$("#icWC_QBR").attr({"disabled":"disabled"});
					$("#icWC_QBRQ").datetimebox("disable");
					$("#icWC_YJRQ").datetimebox("disable");
					$("#icWC_FKKSFZR").combobox("disable");
					$("#icWC_FKR").combobox("disable");
				}
			}
		}
		//切割数据库中反馈科室负责人
		if(FKKSFZR!=""){//判断  反馈科室负责人 是否为空
			var b=FKKSFZR.split(",");
			for(var j=0;j<b.length;j++){
				if(USERCODE==b[j]){//控制反馈按钮，  当前用户如果是反馈科室负责人，那么反馈按钮也 可用
					$("#fankui").removeAttr("disabled");//反馈按钮可用（亮）
					$("#icWC_NAME").attr({"disabled":"disabled"});
					$("#icWC_TIME").datetimebox("disable");
					$("#icWC_LXFS1").attr({"disabled":"disabled"});
					$("#icWC_LXR").combobox("disable");
					$("#icWC_SFR").attr({"disabled":"disabled"});
					$("#icWC_LXFS2").attr({"disabled":"disabled"});
					$("#icWC_ZDGZ1").attr({"disabled":"disabled"});
					$("#icWC_ZDGZ2").attr({"disabled":"disabled"});
					$("#icWC_CGGZ1").attr({"disabled":"disabled"});
					$("#icWC_CGGZ2").attr({"disabled":"disabled"});
					$("#icWC_QTGZ1").attr({"disabled":"disabled"});
					$("#icWC_QTGZ2").attr({"disabled":"disabled"});
					$("#icWC_YJJY").attr({"disabled":"disabled"})
					$("#icWC_QBR").attr({"disabled":"disabled"});
					$("#icWC_QBRQ").datetimebox("disable");
					$("#icWC_YJRQ").datetimebox("disable");
					$("#icWC_FKKSFZR").combobox("disable");
					$("#icWC_FKR").combobox("disable");
				}
			}
		}
	}
	//反馈
	function fankui(){//提交反馈 时 执行的方法，根据弹话框选择确定 提交反馈，那么执行本方法
		$.ajax({//发 ajax请求 同时传递 反馈的 数据，
			type: "POST",
			url: ContextPath + "/Svl_RemindConc",
			data: "active=fankui&icWC_CODE=" + icWC_CODE+"&icWC_FKJG="+encodeURI($("#icWC_FKJG").val())+"&icWC_FGLDQZ="+encodeURI($("#icWC_FGLDQZ").val())+"&icWC_DFDFKJGDPJ="+encodeURI($("#icWC_DFDFKJGDPJ").val())+"&icWC_FKRQ="+encodeURI($("#icWC_FKRQ").datebox('getValue'))+"&icWC_CDR="+encodeURI($("#icWC_CDR").val())+"&icWC_CDRQ="+encodeURI($("#icWC_CDRQ").datebox('getValue')),
			dataType: 'json',
			success: function(datas){
				doToolbarAuth();
				var data = datas[0];
				showMsg(data.MSG);
				loadData(); //加载数据，根据主键
				try{
					window.parent.opener.loadGrid();//调用父页面方法，刷新父页面
				}catch(e){}
			}
		});
	}
	//设 下拉框 值
	function setComboxValue(){
		//联系人
		$('#icWC_LXR').combobox({
			data:<%=PublicTools.getComboboxOptions("dbo.V_USERINFO","UserCode","UserName","where state='Y' order by UserName",request)%>,
			valueField:'comboValue',//用户编号  id
			textField:'comboName',//用户姓名  id
			panelHeight:'100',//下拉框高度设定， 超过此高度 出现滚动条
			onLoadSuccess:function(data){
				if(data != ""){
					
				}
			}
		});
	}
	
	//反馈科室负责人和反馈人级联下拉列表
	function unionFartherComboboxFKKSFZR(str1,str2){
		var data3=<%=PublicTools.getComboboxOptions("dbo.V_USER_AUTH A inner join dbo.V_USERINFO B on A.UserCode=B.UserCode","distinct B.UserCode","B.UserName","where B.state='Y' and A.AuthCode in('04','14','06','16') order by B.UserName",request)%>;
		$('#icWC_FKKSFZR').combobox({
				data:data3,
				valueField:'comboValue',
				textField:'comboName',
				editable:true,
				multiple:true,
				panelHeight:'80',
				onLoadSuccess:function(data){
					$(this).combobox('setValues',str1);
				},
				onChange:function(data){
					unionSonComboboxFKR($(this).combobox('getValues'),str2);//combobox级联查询
					str2="";
				}
		})
	}
	function unionSonComboboxFKR(str1,str2){
		$('#icWC_FKR').combobox({
					url:"<%=request.getContextPath()%>/Svl_RemindConc?active=search&searchStr="+str1,
					valueField:'comboValue',
					textField:'comboName',
					editable:true,
					multiple:true,
					panelHeight:'100',
					onLoadSuccess:function(data){
						$(this).combobox('setValues',str2);
					}
		})
	}
</script>
</html>