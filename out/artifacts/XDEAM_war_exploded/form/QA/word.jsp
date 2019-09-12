<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="com.zhuozhengsoft.pageoffice.*,java.util.*,com.pantech.base.common.tags.util.AuditConductBean1,java.io.*,javax.servlet.*,javax.servlet.http.*,com.pantech.base.common.tools.MyTools,com.pantech.base.common.qa.page.PageofficeToolbar,com.pantech.base.common.tools.PublicTools;"%>
<%@ taglib uri="http://java.pageoffice.cn" prefix="po" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";	
	String oprateID=MyTools.StrFiltr(request.getParameter("code"));
	//String mouldID=PageofficeToolbar.getMouid(request);
	//String svl=MyTools.StrFiltr(request.getParameter("Svl"));
	PageofficeToolbar.initialize(request,response); 
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>文档在线编辑</title>
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
		src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
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
  	
    <po:PageOfficeCtrl id="PageOfficeCtrl1" />
  </body>
  <script type="text/javascript">
    		var code="";
    		var tablename="";
    		var filed="";
    		var auditskip="<%=PublicTools.getauditskip(PageofficeToolbar.getMouid(request),request)%>";
    		//alert(ssss);
			function Save() {
				document.getElementById("PageOfficeCtrl1").WebSave();
			}
			
			function Submit() {
				Save();
				onSubmit();
				//ConfirmMsg("你确认要删除该记录吗?","$('#form1').submit();","");
				//ConfirmMsg("提交后无法更改,你是否要提交该文档?","onSubmit();","");
			}
			//alert($("#auditskip").val());
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
						
						//alert("aaaaaaaaa="+auditskip);
						//aaa();
						if(auditskip=="Y"){
							showaudit();
						}else{
							window.location.reload(); 
						}
						
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
			
			function showaudit(){
			    var top=(document.body.offsetHeight/2-15);
				var left=(document.body.offsetWidth/2-137);
				window.open("<%=request.getContextPath()%>/common/AduditTable2.jsp?oprateID=<%=oprateID%>", "newwin", "height=15px, width=510px,titlebar=no,toolbar=no,menubar=no,location=no,left="+left+",top="+top);//声明一个newwin的window.open窗口
			}

	</script>
</html>
