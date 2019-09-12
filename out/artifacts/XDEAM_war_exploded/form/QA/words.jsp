<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="com.zhuozhengsoft.pageoffice.*,java.util.*,java.io.*,javax.servlet.*,javax.servlet.http.*,com.pantech.base.common.tools.MyTools,com.pantech.base.common.qa.page.PageofficeToolbar,
	com.pantech.base.common.tags.util.AuditConductBean1,com.pantech.base.common.tools.PublicTools;" %>
<%@ taglib uri="http://java.pageoffice.cn" prefix="po" %>
<%@ taglib uri="/WEB-INF/taglib/util.tld" prefix="util" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";	
	//String svl=MyTools.StrFiltr(request.getParameter("Svl"));
	PageofficeToolbar.initializeaudit(request,response); 
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>文档审核</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

	<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/css/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/css/themes/icon.css">
<script type="text/javascript"
	src="<%=request.getContextPath()%>/script/JQueryUI/jquery-1.4.4.min.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
<script  type="text/javascript" src="<%=request.getContextPath()%>/script/parseXML1.js"></script>

</head>
  <jsp:useBean class="com.pantech.base.common.tags.security.PurViewTools"
	id="pc" scope="page" />
  <body>    	
    <script type="text/javascript">
    		var code="";
    		var tablename="";
    		var filed="";
			function Save() {
				document.getElementById("PageOfficeCtrl1").WebSave();
			}
			
			function Submit() {
				onSubmit();
				//ConfirmMsg("你确认要删除该记录吗?","$('#form1').submit();","");
				//ConfirmMsg("提交后无法更改,你是否要提交该文档?","onSubmit();","");
			}
			
			function onSubmit(){
				code=getQueryString("code");
				tablename=getQueryString("tablename");
				filed=getQueryString("filed");
				$.ajax({
					type: "POST",
					url: '<%=request.getContextPath()%>/Svl_PageSubmit',
					//必要的参数
					data: "active=submit&code=" + code + "&tablename=" + tablename + 
							"&filed="+filed,
					success: function(result){ 
						//$('#auditedit').window('close');
						//extend();
						window.location.reload(); 
						
					}, //modfiby by hades 
					error: function(msg){ //20110721 modify by hades 出错释放锁
						//alertMsg("审核信息错误，请[重试]或[刷新]后操作。");
					}
				});
			}
			
			function getQueryString(name) {
			    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
			    var r = window.location.search.substr(1).match(reg);
			    if (r != null) return unescape(r[2]); return null;
    		}
    		
    		function show_auditedit(){
    			var top=(document.body.offsetHeight/2-145);
				var left=(document.body.offsetWidth/2-137);
				//var width=$("#auditedit").css("width");
				//var height=$("#auditedit").css("height");
				//alert(width);
    			OpenWindow=window.open("", "newwin", "height=260px, width=510px,titlebar=no,toolbar=no,menubar=no,location=no,left="+left+",top="+top);//声明一个newwin的window.open窗口
				OpenWindow.document.close();
				OpenWindow.document.writeln('<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/default/easyui.css">');
				OpenWindow.document.writeln('<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/icon.css">');
				OpenWindow.document.writeln('<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery-1.4.4.min.js"><\/script>');
				OpenWindow.document.writeln('<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"><\/script>');
				OpenWindow.document.writeln('<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"><\/script>');
				OpenWindow.document.writeln('<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"><\/script>');
				OpenWindow.document.writeln('<script type="text/javascript" src="<%=request.getContextPath()%>/script/parseXML1.js"><\/script>');
				OpenWindow.document.writeln("<body style='background:#EFEFEF'>");//html代码可以是动态拼接
				OpenWindow.document.writeln(document.getElementById("auditedit").innerHTML);//html代码可以是动态拼接
				OpenWindow.document.writeln("</body>");
				//alert();
			}
			
			function adudittables_toggle(){
    			var top=(document.body.offsetHeight/2-145);
				var left=(document.body.offsetWidth/2-137);
				var width=$("#adudittables").css("width");
				var height=$("#adudittables").css("height");
				//alert();
    			OpenWindow=window.open("", "newwin", "height="+height+", width="+width+",toolbar=no,menubar=no,left="+left+",top="+top);//声明一个newwin的window.open窗口
				OpenWindow.document.close();
				OpenWindow.document.writeln('<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/default/easyui.css">');
				OpenWindow.document.writeln('<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/icon.css">');
				OpenWindow.document.writeln('<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery-1.4.4.min.js"><\/script>');
				OpenWindow.document.writeln('<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"><\/script>');
				OpenWindow.document.writeln('<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"><\/script>');
				OpenWindow.document.writeln('<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"><\/script>');
				OpenWindow.document.writeln('<script type="text/javascript" src="<%=request.getContextPath()%>/script/parseXML1.js"><\/script>');
				OpenWindow.document.writeln("<body style='background:#EFEFEF'>");//html代码可以是动态拼接
				OpenWindow.document.writeln(document.getElementById("adudittables").innerHTML);//html代码可以是动态拼接
				OpenWindow.document.writeln("</body>");
				//alert();
			}
			
			function showaudit(){
    			var top=(document.body.offsetHeight/2-145);
				var left=(document.body.offsetWidth/2-137);
				//var width=$("#auditedit").css("width");
				//var height=$("#auditedit").css("height");
				//alert(width);
    			OpenWindow=window.open("", "newwin", "height=260px, width=510px,titlebar=no,toolbar=no,menubar=no,location=no,left="+left+",top="+top);//声明一个newwin的window.open窗口
				OpenWindow.document.close();
				OpenWindow.document.writeln('<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/default/easyui.css">');
				OpenWindow.document.writeln('<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/icon.css">');
				OpenWindow.document.writeln('<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"><\/script>');
				OpenWindow.document.writeln('<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"><\/script>');
				OpenWindow.document.writeln('<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"><\/script>');
				OpenWindow.document.writeln('<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"><\/script>');
				OpenWindow.document.writeln('<script type="text/javascript" src="<%=request.getContextPath()%>/script/parseXML1.js"><\/script>');
				OpenWindow.document.writeln("<body style='background:#EFEFEF'>");//html代码可以是动态拼接
				OpenWindow.document.writeln(document.getElementById("audittable2").innerHTML);//html代码可以是动态拼接
				OpenWindow.document.writeln("</body>");
				//alert();
			}
			
			function extend(SHCROSS){
				code=getQueryString("code");
				tablename=getQueryString("tablename");
				filed=getQueryString("filed");
				//alert();
				$.ajax({
					type: "POST",
					url: '<%=request.getContextPath()%>/Svl_PageSubmit',
					//必要的参数
					async:false,
					data: "active=shjs&code=" + code + "&tablename=" + tablename + 
							"&filed="+filed+"&SHCROSS="+SHCROSS,
					success: function(result){ 
						//$('#auditedit').window('close');
						//extend();
						//window.location.reload(); 
						
					}, //modfiby by hades 
					error: function(msg){ //20110721 modify by hades 出错释放锁
						//alertMsg("审核信息错误，请[重试]或[刷新]后操作。");
					}
				});
			}
			
			        //显示痕迹
        function ShowRevisions() {
            document.getElementById("PageOfficeCtrl1").ShowRevisions = true;
        }

        //隐藏痕迹
        function HiddenRevisions() {
            document.getElementById("PageOfficeCtrl1").ShowRevisions = false;
        }
        
        //领导圈阅签字
        function StartHandDraw() {
            document.getElementById("PageOfficeCtrl1").HandDraw.SetPenWidth(5);
            document.getElementById("PageOfficeCtrl1").HandDraw.Start();
        }
		// 插入键盘批注
        function StartRemark() {
            var appObj = document.getElementById("PageOfficeCtrl1").WordInsertComment();

        }	
        //分层显示手写批注
        function ShowHandDrawDispBar() {
            document.getElementById("PageOfficeCtrl1").HandDraw.ShowLayerBar(); ;
        }

        //全屏/还原
        function IsFullScreen() {
            document.getElementById("PageOfficeCtrl1").FullScreen = !document.getElementById("PageOfficeCtrl1").FullScreen;
        }
	</script>
	
    <po:PageOfficeCtrl id="PageOfficeCtrl1" />
  </body>
</html>
