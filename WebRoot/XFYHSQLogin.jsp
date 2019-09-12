<!-- 
	说明:系统登陆界面
	日期:2013.7.22
	梁振荣
 -->
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page
	language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<!-- 梁振荣   2013.7.22  删除一行多余的代码,原代码是link第一行-->
<html>	
<head>
<title>登录</title>
<style >
#logo{
   float:left;
   width:300px; 
   height:39px;
   border:0px green solid;
   margin-top:160px;
   margin-left:325px; 
 }
 #login{
   float:left;
   margin-top:10px;
   margin-left:460px;
   border:0px green solid;
 }
 #user{
   width: 126px;
   height: 21px; 
 }
 #password{
   width: 126px;
   height: 21px; 
 }
</style>

	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/icon.css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>

<!--[if lt IE 7]>
	<link href="ie_style.css" rel="stylesheet" type="text/css" />
<![endif]-->

</head>

<BODY  style="margin-top:3px;text-align:center;background: url('images/dl_tc.png');background-repeat: repeat; overflow:hidden;">

<!--总div_begin-->
<div style="background:url('images/Login/xdzx_login.png'); margin:0 auto; width:983px; height:520px;">

	<!--顶部LOGO_begin--> 	
		<div id="logo" >
			<!-- <img alt="" src="images/Login/hjxx_denglvmz.png" /> -->
		</div>
	<!--顶部LOGO_end--> 
				 
<!--登陆模块_begin--> 	
<div id="login" >
<form name="form1" id="form1" method="post">
<table cellpadding='1' cellspacing='1'>
  <tr>
  	<!--用户名_begin-->
   	<td height='50px;'><font color="#FFFFFF"><b>用户名：</b></font></td>
   	<td cospan='2'>
		<input id="user" name="user" type="text" class="login_user" onKeyDown="if(event.keyCode==13) document.form1.password.focus();"/>
	</td>   
   	<!--用户名_end-->
  </tr>
  <tr>
  	<!--密码_begin-->
  		<td height='50px;'><font color="#FFFFFF"><b>密&nbsp;&nbsp;&nbsp;码：</b></font></td>
   		<td cospan='2'>
			<input id="password" name="password" type="password" class="login_pass" AUTOCOMPLETE="off" onKeyDown="if(event.keyCode==13) doForm();"/>
		</td>  		
   	<!--密码_end-->
  </tr> 
  <tr>
  	<!--登陆、重置按钮_begin-->
  		<!-- 登陆 -->
   			<td height='65px;'>
   				<input  id="lvhuaju_r7_c4" onClick="doForm()" name="lvhuaju_r7_c4" accesskey="l" value="" tabindex="4" style="background:url('images/Login/hjxx_denglvanniu.png'); width:64px; height:29px; border:0;"/> 
   				<!--  <img id="lvhuaju_r7_c4" name="lvhuaju_r7_c4" src="images/Login/hjxx_denglvanniu.png" onClick="doForm();" style="width:64px; height:29px; border:0;">-->
   			</td>
   		<!-- 重置 -->
   			<td align='right' height='65px;'>   	
   				<input id="reset" accesskey="l" value="" tabindex="4" type="reset" style="background:url('images/Login/hjxx_denglvanniu1.png'); width:64px; height:29px; border:0;"/>
   			</td> 
   	<!--登录、重置按钮_end-->
  </tr>
</table>
<!-- 2013.7.23 梁振荣 根据绿化局项目增加的功能标签 -->
<input type="hidden" name="submittype" id="submittype" value="ajax" >
<input type="hidden" name="logintype" value=""/>
</form>
</div>
<!--登陆模块_end-->
<!--页脚信息_begin--> <!-- 2013.7.23 梁振荣 在调试样式时注释发现页脚信息不影响,在功能确认后进行删除 -->
	<!--  <div id="footer" style="margin-top:90px; left: border:0px green solid; line-height:20px; text-align:center; "><br /><br />
	      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br />
	</div>-->
<!--页脚信息_end-->
</div>
<!--总div_end-->

<script language="JavaScript">
$(document).ready(function(){
	//alert("well");
	document.all("user").focus();
});

  //form表单提交时发生的事件
function doForm(){
	//alert("aaa");
	$("#submittype").val("ajax");
	$("#form1").submit();
}

//form表单提交时发生的事件
$('#form1').form({
	//定位到servlet位置的url
	url:'<%=request.getContextPath()%>/Svl_setSession',
	//当点击事件后触发的事件
    onSubmit: function(data){ 
   // alert("onsubmit")
   		document.all("user").focus();
    }, 
    //当点击事件并成功提交后触发的事件
    success:function(data){
    	var a="";//登陆成功
    	try{
			var json = eval("("+data+")");
    	}catch(e){
    		a="1";
    	}
        //登陆失败：a=1
        if(a=="1"){
			$.messager.alert("提示信息","用户名或密码错误");
			return;
        }else{
        	location.href="<%=request.getContextPath()%>/form/xfyh/XFSQMobile.jsp";
        }  
    }
});
	
	</script>
</body>
</html>
