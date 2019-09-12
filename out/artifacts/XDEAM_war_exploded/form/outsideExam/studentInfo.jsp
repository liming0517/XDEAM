<%
/**
* @Description 学生信息 
* @date 2016-09-12
* @author lupengfei 
* @version V1.0  
*/
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.src.develop.store.user.*"%>
<%@page import="com.pantech.base.common.tools.PublicTools"%>
<%@ page import="java.util.Vector"%>
<%@page import="java.net.URLEncoder" %>
<% 
	String stuId =request.getParameter("stuId");
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
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/form/css/uploadify.css">
		<script type="text/javascript" src="<%=request.getContextPath()%>/form/File/jquery.uploadify-v2.1.4/swfobject.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/form/File/jquery.uploadify-v2.1.4/jquery.uploadify.v2.1.4.min.js"></script>
		<style type="text/css">
			 td{text-align:center;}
		</style>
	</head>
	<form id="form1" name="form1" method="POST" action="<%=request.getContextPath()%>/Svl_Import" enctype="multipart/form-data">
	<body class="easyui-layout">

			<div id="uploadPhoto" title="" style="width:720px; height:100%;" align="center">
				<div class="easyui-layout" style="width:100%; height:100%;">
					<table width="100%" height="100%" class="tablestyle">
						<tr>
							<td style="width:100px;height:50px;">姓名</td>
							<td width="200px" id="ic_stuName"></td>
							<td width="100px">性别</td>
							<td width="200px" id="ic_stuSex"></td>
							<td width="120px" rowspan=4 align="center">
							<div style="position:relative;">
								<div id="headPhoto" style="position:absolute;width:90px;height:120px;margin-top:-60px;margin-left:-45px;border:0px #BAE3FD solid;z-index:10;">
						       		<img id="selpic" src=""  style="width:90px;height:120px;margin-top:0px;"  />
						       	</div>
						       	<div id="viewPhoto" style="">
						       		<a href="#"	style="display:inline-block; width:90px; height:120px;z-index:99;overflow:hidden;">
						      	 		<input type="file" name="browse" id="browse" style="position:absolute;cursor:pointer; width:90px;right:0; top:0; font-size:110px; opacity:0; filter:alpha(opacity=0);" />
									</a>
						       	</div>
						       	</div>
							</td>
						</tr>
						<tr>
							<td style="width:100px;height:50px;">学号</td>
							<td id="ic_stuNum"></td>
							<td>班级</td>
							<td id="ic_stuClass"></td>
						</tr>
						<tr>
							<td style="width:100px;height:50px;">出生年月</td>
							<td id="ic_stuBirthday"></td>
							<td>身份证号</td>
							<td id="ic_stuCard"></td>
						</tr>
						<tr>
							<td style="width:100px;height:50px;">民族</td>
							<td id="ic_stuNation"></td>
							<td>政治面貌</td>
							<td id="ic_stuPolitics"></td>
						</tr>
						<tr>
							<td style="width:100px;height:50px;">联系电话</td>
							<td id="ic_stuPhone"></td>
							<td>电子邮箱</td>
							<td id="ic_stuEmail"></td>
							<td align="center">
								<a href="#"	onclick="doToolbar(this.id)" id="upload" class="easyui-linkbutton" plain="true" iconcls="icon-save" style="display:inline-block;  position:relative; overflow:hidden;" >上传</a>
							</td>
						</tr>
						<tr>
							<td style="width:100px;height:50px;">家庭住址</td>
							<td colspan=4 id="ic_stuAddress"></td>
							
						</tr>
						
					</table>	
					<input type="hidden" id="active" name="active" />
					<input type="hidden" id="path" name="path" />
					<input type="hidden" id="iUSERCODE" name="iUSERCODE" />
				</div>
			</div>
	

	</body>
	</form>

<script language="JavaScript" type="text/JavaScript">
	var iUSERCODE="<%=stuId%>"; 
	
	//页面初始化
	$(document).ready(function(){
		getStuInfo();
		getPhotoPath();
		
		var ex=getOs();
		if(ex=="MSIE"){ 
				if(navigator.appName == "Microsoft Internet Explorer" && navigator.appVersion.match(/7./i)=="7."){ 
					ostype=1;
					$('#headPhoto').attr('style','position:absolute;width:90px;height:120px;margin-top:-60px;margin-left:-45px;border:0px #BAE3FD solid;z-index:10;');
					$('#viewPhoto').attr('style','position:absolute;margin-top:-60px;margin-left:-45px;z-index:20;');
				}else if(navigator.appName == "Microsoft Internet Explorer" && navigator.appVersion.match(/8./i)=="8."){ 
					ostype=1;
					$('#headPhoto').attr('style','position:absolute;width:90px;height:120px;margin-top:-60px;margin-left:-45px;border:0px #BAE3FD solid;z-index:10;');
					$('#viewPhoto').attr('style','position:absolute;margin-top:-60px;margin-left:-45px;z-index:20;');
				}else if(navigator.appName == "Microsoft Internet Explorer" && navigator.appVersion.match(/6./i)=="6."){ 
					ostype=1;
					$('#headPhoto').attr('style','position:absolute;width:90px;height:120px;margin-top:-60px;margin-left:-45px;border:0px #BAE3FD solid;z-index:10;');
					$('#viewPhoto').attr('style','position:absolute;margin-top:-60px;margin-left:-45px;z-index:20;');
				}else{
					ostype=2;
					$('#headPhoto').attr('style','position:absolute;width:90px;height:120px;margin-top:-60px;margin-left:10px;border:0px #BAE3FD solid;z-index:10;');
					$('#viewPhoto').attr('style','position:absolute;margin-top:-60px;margin-left:10px;z-index:20;');
				} 
		}else if(!!window.ActiveXObject || "ActiveXObject" in window){
				ostype=3;
				$('#headPhoto').attr('style','position:absolute;width:90px;height:120px;margin-top:-60px;margin-left:10px;border:0px #BAE3FD solid;z-index:10;');
				$('#viewPhoto').attr('style','position:absolute;margin-top:-60px;margin-left:10px;z-index:20;');
		}else{
				ostype=2;
				$('#headPhoto').attr('style','position:absolute;width:90px;height:120px;margin-top:-60px;margin-left:10px;border:0px #BAE3FD solid;z-index:10;');
					$('#viewPhoto').attr('style','position:absolute;margin-top:-60px;margin-left:10px;z-index:20;');
		}			

	});
	
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
	
	//工具栏操作
	function doToolbar(id){
		//上传照片
		if(id == "uploadPic"){
			var obj = document.getElementById('browse') ; 
			obj.outerHTML=obj.outerHTML;  
			getPhotoPath();		
		}
		//上传
		if(id == "upload"){
			var filename=$('#browse').val();
			if(filename==''||filename==null){//未选择文件
					alertMsg('请选择文件!',0) ;
					//alert('请选择文件');
					return;
			}
			$("#active").val("uploadPhoto");
			$('#path').val(escape(filename));
			$('#iUSERCODE').val(iUSERCODE);
			$("#form1").attr("enctype","multipart/form-data");
			$("#form1").attr("encoding", "multipart/form-data");
			$("#form1").submit();
		}
	}
	
	/**表单提交**/
	$('#form1').form({
		//定位到servlet位置的url
		url:'<%=request.getContextPath()%>/Svl_outsideExam?active=uploadPhoto&iUSERCODE='+iUSERCODE+'&path='+$('#path').val(),
		//当点击事件后触发的事件
		onSubmit: function(data){
			return $(this).form('validate');//验证
		}, 
		//当点击事件并成功提交后触发的事件
		success:function(data){
			$("#form1").attr("enctype","application/x-www-form-urlencoded");
			$("#form1").attr("encoding", "application/x-www-form-urlencoded");
			var json = eval("("+data+")");
			if(json[0].MSG == '保存成功'){
				showMsg(json[0].MSG);
				getPhotoPath();
			}else{
				alertMsg(json[0].MSG);
			}
		}
	});
	
	function getPhotoPath(){
		$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_outsideExam',
				data : 'active=getPhotoPath&iUSERCODE='+iUSERCODE,
				dataType:"json",
				success : function(data){
					var filename=data[0].MSG;
					if(data[0].MSG==""){//未上传过照片
						$("#selpic").attr('src','<%=request.getContextPath()%>/images/qsczp/qsczp2.jpg');		
						$('#uploadPhoto').dialog('open');
					}else{
						//var extName = filename.substring(filename.lastIndexOf("."),filename.length);  
	    				fipath='<%=MyTools.getProp(request, "Base.upLoadPathFile")%>'+'/studentPhoto/'+filename;
						$("#selpic").attr('src','<%=request.getContextPath()%>/Svl_outsideExam?active=showImg&name='+filename+'&path='+fipath);		
						$('#uploadPhoto').dialog('open');
					}
				}
		});
	}
	
	function getStuInfo(){
		$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_outsideExam',
				data : 'active=getStuInfo&iUSERCODE='+iUSERCODE,
				dataType:"json",
				success : function(data){
					$('#ic_stuName').html(data[0].姓名);
					$('#ic_stuSex').html(data[0].性别);
					$('#ic_stuNum').html(data[0].学号);
					$('#ic_stuClass').html(data[0].班级);
					$('#ic_stuBirthday').html(data[0].出生年月);
					$('#ic_stuCard').html(data[0].身份证号);
					$('#ic_stuNation').html(data[0].民族);
					$('#ic_stuPolitics').html(data[0].政治面貌);
					$('#ic_stuPhone').html(data[0].联系电话);
					$('#ic_stuEmail').html(data[0].电子邮箱);
					$('#ic_stuAddress').html(data[0].家庭住址);
				}
		});
	}
	
	
</script>
</html>
