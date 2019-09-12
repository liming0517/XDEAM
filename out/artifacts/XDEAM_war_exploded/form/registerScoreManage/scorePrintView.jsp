<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="com.pantech.base.common.tools.*"%>
<%@page import="com.pantech.develop.pageoffice.*"%>
<%@page import="com.pantech.devolop.registerScoreManage.CjcxBean"%>
<%@ taglib uri="http://java.pageoffice.cn" prefix="po"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String xnxq = MyTools.StrFiltr(request.getParameter("XNXQ"));
	String jsxm = MyTools.StrFiltr(request.getParameter("JSXM"));
	String kkb = MyTools.StrFiltr(request.getParameter("KKB"));
	
	CjcxBean bean = new CjcxBean(request);
	String filePath = bean.loadScorePrintView(request, xnxq, jsxm, kkb);
%>
<html>
<head>
  <base href="<%=basePath%>">
	<title>打印预览</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
</head>
	<body style="margin:0;">
		<!-- *********************pageoffice组件的使用 **************************-->
		<script language="javascript" type="text/javascript">
			//window.parent.viewFilePath = '<-%=filePath%>';
			var viewFilePath = '<%=filePath%>';
			var xnxq = '<%=xnxq%>';
			var jsxm = '<%=jsxm%>';
			var kkb = '<%=kkb%>';
		
			//打印
			function print(){
				document.getElementById("PageOfficeCtrl1").PrintPreview();
				//document.getElementById("PageOfficeCtrl1").PrintOut(true);
				//锁定成绩状态
				//window.parent.lockScore();
				lockScore();
			}
		
			//全屏
			function SetFullScreen() {
				document.getElementById("PageOfficeCtrl1").FullScreen = !document.getElementById("PageOfficeCtrl1").FullScreen;
			}
			
			//返回
			function closeWindow() {
				//window.parent.closeDialog();
			}
			
			//文档打开后执行
			function AfterDocumentOpened(){
				$('#mask').hide();
				//document.getElementById("PageOfficeCtrl1").PrintPreview();
				//window.parent.delViewFile();
				delViewFile();
			}
			
			//文档关闭后执行
			function AfterDocumentClosed(){
				//window.parent.delViewFile();
			}
			
			/**锁定课程成绩**/
			function lockScore(){
				$.ajax({
					type:"POST",
					url:'<%=request.getContextPath()%>/Svl_Cjcx',
					data:'active=lockScore&XNXQ=' + xnxq + '&JSXM=' + jsxm + '&iKeyCode=' + kkb,
					dataType:"json",
					success:function(data) {
						/*
						if(data[0].MSG == '锁定成功'){
							showMsg(data[0].MSG);
						}else{
							alertMsg(data[0].MSG);
						}
						*/
					}
				});
			}
			
			/**删除预览文件**/
			function delViewFile(){
				$.ajax({
					type : "POST",
					url : '<%=request.getContextPath()%>/Svl_Cjcx',
					data : 'active=delViewFile&filePath=' + viewFilePath,
					dataType:"json",
					success : function(data){
						if(data[0].MSG == '删除成功') viewFilePath = '';
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
	</body>
</html>