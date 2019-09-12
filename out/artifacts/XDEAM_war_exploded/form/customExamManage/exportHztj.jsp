<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="com.pantech.base.common.tools.*"%>
<%@page import="com.pantech.develop.pageoffice.*"%>
<%@page import="com.pantech.devolop.customExamManage.*"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="java.net.URLDecoder"%>
<%@ taglib uri="http://java.pageoffice.cn" prefix="po"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String xnxqbm = MyTools.StrFiltr(request.getParameter("xnxqbm"));
	String exportType = MyTools.StrFiltr(request.getParameter("exportType"));
	
	String sAuth = MyTools.StrFiltr(request.getParameter("sAuth"));
	String usercode = MyTools.StrFiltr(request.getParameter("USERCODE"));
	YktjBean yktjBean = new YktjBean(request);
	String filePath = yktjBean.exportHztjb(request, xnxqbm,sAuth,usercode);
	
%>
<html>
<head>
  <base href="<%=basePath%>">
	<title>导出预览</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
</head>
	<body style="margin:0;" scroll="no">
		<!-- *********************pageoffice组件的使用 **************************-->
		<script language="javascript" type="text/javascript">
			var viewFilePath = '<%=filePath%>';
			
			//保存
			function exportExcel(){
				document.getElementById("PageOfficeCtrl1").ShowDialog(3); 
			}
			
			//全屏
			function SetFullScreen() {
				document.getElementById("PageOfficeCtrl1").FullScreen = !document.getElementById("PageOfficeCtrl1").FullScreen;
			}
			
			//返回
			function goBack() {
				window.parent.closeDialog();
			}
			
			//下载
			function download(){
				downloadYk();
			}
			
			//打印
			function print(){
				document.getElementById("PageOfficeCtrl1").PrintPreview();
			}
			
			//文档打开后执行
			function AfterDocumentOpened(){
				$('#mask').hide();
				//document.getElementById("PageOfficeCtrl1").PrintPreview();
				delViewFile();
			}
			
			//文档关闭后执行
			function AfterDocumentClosed(){
				delViewFile();
			}
			
			/**删除预览文件**/
			function delViewFile(){
				$.ajax({
					type : "POST",
					url : '<%=request.getContextPath()%>/Svl_Yktj',
					data : 'active=delViewFile&filePath=' + viewFilePath,
					dataType:"json",
					success : function(data){
						if(data[0].MSG == '删除成功') viewFilePath = '';
					}
				});
			}
			
			/**月考成绩导出*/
			function downloadYk(){
				$('#mask').show();
				$.ajax({
					type : "POST",
					url : '<%=request.getContextPath()%>/Svl_Yktj',
					data : 'active=exportYKXQ&xnxqbm=' + '<%=xnxqbm%>'+'&sAuth='+'<%=sAuth%>'+'&usercode='+'<%=usercode%>',
					dataType:"json",
					success : function(data){
						if(data[0].MSG == '文件生成成功'){
							//下载文件到本地
							$("#exportIframe").attr("src", '<%=request.getContextPath()%>/download.jsp?filePath=' + encodeURIComponent(data[0].filePath));
						}else{
							alertMsg(data[0].MSG);
						}
						$('#mask').hide();
					}
				});
			}
			
		</script>
		<table id="mask" style="width:100%; height:100%; text-align:center; display:none;">
			<tr><td><img src="<%=request.getContextPath()%>/images/loading_3.gif"/></td></tr>
		</table>
		<div id="mainDiv" style="width:100%; height:100%;">
			<po:PageOfficeCtrl id="PageOfficeCtrl1" />
		</div>
		
		<!-- 下载excel -->
		<iframe id="exportIframe" src="" style="width:0; height:0;"></iframe>
	</body>
</html>