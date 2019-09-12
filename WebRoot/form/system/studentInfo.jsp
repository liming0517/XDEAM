<%
/**
Create date: 2012.7.13
说明：用于编辑学员信息
创建人：yeqiang
**/
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><%@page
	language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.pantech.base.common.tools.MyTools" %>
<%@page import="com.pantech.base.common.tools.PublicTools"%>

<html>
	<head>
		<title>人员信息</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/icon.css">
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
	</head>
		
	<form action="<%=request.getContextPath()%>/Svl_UserInfoDet" method="Post" id="form1" name="form1"  >
		<body class="easyui-layout">
			<div style="overflow:hidden;height:60px;background:#fafafa; " title="人员信息" region="north">
				<div>
					<table>
						<tr>
							<td><a href="#" onclick="doToolbar(this.id)" id="save" class="easyui-linkbutton" iconcls="icon-save" plain="true">保存</a></td>
						</tr>
					</table>
				</div>
			</div>
			<div sytle="background:#fafafa;" title="" region="center">
				<table id="ff" iconCLs="icon-edit" width="100%" class="tablestyle">
					<tr>
						<td width="200px"><label>用户编号：</label></td>
						<td>
							<input style="width:145px;color:#A9A9A9" name="ic_userCode" class="easyui-validatebox" id="ic_userCode" maxlength="20" required="true"  title="请填写用户名">
						</td>
						<td width="200px"><label>密码：</label></td>
						<td>
							<input style="width:145px" name="ic_password" class="easyui-validatebox" id="ic_password" maxlength="50" required="true" title="请填写密码">
						</td>
					</tr>
					<tr>
						<td width="200px"><label>用户名：</label></td>
						<td>
							<input style="width:145px" name="ic_userName" class="easyui-validatebox" id="ic_userName" maxlength="50" required="true" title="请填写姓名">
						</td>				
						<td width="200px"><label>帐号：</label></td>				
						<td>
							<input style="width:145px" name="icST_EMAIL" class="easyui-validatebox" id="icST_EMAIL" maxlength="50"  validType="email" title="请填写邮箱">
						</td>
					</tr>
					<tr>
						<td width="200px"><label>联系电话：</label></td>				
						<td>
							<input style="width:145px" name="icST_TEL" class="easyui-validatebox" id="icST_TEL" maxlength="50" title="请填写联系电话">
						</td>
						<td width="200px"><label>状态：</label></td>
						<td>
							<select style="width:145px" name="icST_STATE" id="icST_STATE" class="easyui-combobox"  editable="false">
								<option value="Y">有效</option>
								<option value="N">无效</option>
							</select>
						</td>	
					</tr>
				</table>
			</div>
			<!-- 动作的属性传参用 -->
			<INPUT type="hidden" name="active" id="active">
		</body>
	</form>
	<script type="text/javascript">
	
		var iKeyCode = "<%=MyTools.StrFiltr(request.getParameter("ic_userCode"))%>";
		
		//获取需执行的动作
		var active = "<%=MyTools.StrFiltr(request.getParameter("active"))%>";
		
		//行选择时的定位属性
		var lastIndex = -1;
		
		//页面最先加载的方法，初始化一些内容
		$(document).ready(function(){
			if(active=="view"){
				loadData();
			}
		});
		
		//加载页面属性
		function loadData(){
			$.ajax({
				type: "POST",
				url: "<%=request.getContextPath()%>/Svl_StudentYeq",
				data: {"active":active, "ic_userCode":iKeyCode}, 
				dataType: "json",
				success: function(datas){
					//根据记录状态，判断页面显示数据
					var data = datas[0];
					//页面上显示数据
					if(data.USERCODE != null && data.USERCODE != ""){
						//alert("userName:"+data.ST_NAME+",password:"+"*******"+"STATE:"+data.STATE);
						$("#ic_userCode").attr("readonly",true);  //设置只读
						$("#ic_userCode").val(data.USERCODE);  //用户名
						$("#ic_userName").val(data.ST_NAME);   //姓名
						$("#ic_password").val(data.PASSWORD); //密码
						$("#icST_EMAIL").val(data.ST_EMAIL); //邮箱
						$("#icST_TEL").val(data.ST_TEL);   //电话
						$("#icST_STATE").combobox("setValue", data.STATE);  //状态
					}
				}
			});
		}
		
		//调用工具栏
		function doToolbar(iToolbar){
			if(iToolbar=="save"){
				$("#active").val(iToolbar);
				$("#form1").submit();
			}
		}
		
		//form表单提交时发生的事件
		$("#form1").form({
			//定位到servlet位置的url
			url: "<%=request.getContextPath()%>/Svl_StudentYeq",
			//当点击事件后出发的事件
			onSubmit: function(data){
			
			},
			//当点击事件并成功提交后触发的事件
			success: function(data){
				if($("#active").val() == "save"){
					 var json = eval("("+data+")");
					 //需要返回主关键字，否则再次保存时会再次插入新记录
					 if (json[0].ic_userCode != "undefined"){
						$("#userCode").val(json[0].ic_userCode);  //----保存主关键字
					}
					//为iKeyCode重新赋值
					iKeyCode=json[0].ic_userCode;
					$("#userCode").val(iKeyCode);
					//清除当前active值
					$('#active').val("");
	    		}
	    		//由于各类错误（如后台JAVA代码出错）也在这里返回，因此不可以在这里写死提示
	    		showMsg(json[0].MSG);
			}
		});
	</script>
<html>