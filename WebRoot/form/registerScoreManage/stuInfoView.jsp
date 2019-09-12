<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="java.io.File"%>
<%@page import="com.pantech.base.common.tools.*"%>
<%@page import="com.pantech.develop.pageoffice.*"%>
<%@page import="com.pantech.devolop.baseInfoManage.XsxxSetBean"%>
<%@ taglib uri="http://java.pageoffice.cn" prefix="po"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String exportType = MyTools.StrFiltr(request.getParameter("exportType"));
	XsxxSetBean bean = new XsxxSetBean(request);
	String JX_XJH ="";
	String BJDM ="";
	String XSLX ="";
	String NJDM ="";
	String XBDM ="";
	String SSZY="";
	String BJBH ="";
	String	filePath ="";
	String XM="";
	if("classKcb".equalsIgnoreCase(exportType)){
			 JX_XJH = MyTools.StrFiltr(request.getParameter("JX_XJH"));
			 BJBH = MyTools.StrFiltr(request.getParameter("BJBH"));
			 XSLX = MyTools.StrFiltr(request.getParameter("XSLX"));
			 XM =MyTools.StrFiltr(request.getParameter("XM"));
			 filePath =bean.exportSingleTimetable(request,exportType,JX_XJH,BJBH,XSLX,XM); 
	}else{
			 NJDM = MyTools.StrFiltr(request.getParameter("NJDM"));
			 XBDM = MyTools.StrFiltr(request.getParameter("XBDM"));//导出班级
			 SSZY = MyTools.StrFiltr(request.getParameter("SSZY"));
			 BJBH = MyTools.StrFiltr(request.getParameter("BJBH"));
			 filePath =bean.exportScoreInfo(request,XBDM,NJDM,SSZY,BJBH); 
	} 


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
	<body style="margin:0;" scroll="no">
		<!-- *********************pageoffice组件的使用 **************************-->
		<script language="javascript" type="text/javascript">
			var viewFilePath = '<%=filePath%>';
			var exportType = '<%=exportType%>';
			function exportExcel(){
				document.getElementById("PageOfficeCtrl1").ShowDialog(3); 
			}
			//打印
			function print(){
				document.getElementById("PageOfficeCtrl1").PrintPreview();
				//document.getElementById("PageOfficeCtrl1").PrintOut(true);
			}
			//下载
			function download(){
				 downXJK();//下载学生学籍卡 单
			}
		
			//全屏
			function SetFullScreen() {
				document.getElementById("PageOfficeCtrl1").FullScreen = !document.getElementById("PageOfficeCtrl1").FullScreen;
			}
			
			//文档打开后执行
			function AfterDocumentOpened(){
				renameFile();
				$('#mask').hide();
				//document.getElementById("PageOfficeCtrl1").PrintPreview();
				//delViewFile();
			}
			
			//文档关闭后执行
			function AfterDocumentClosed(){
				 delViewFile();
				//alert("viewFilePath --"+viewFilePath);
				
			}
			/**改变预览文件名字文件**/
			function renameFile(){
				$.ajax({
					type : "POST",
					url : '<%=request.getContextPath()%>/Svl_XsxxSet',
					data : 'active=renameViewFile&filePath=' +viewFilePath+'&JX_XM='+'<%=XM%>',
					dataType:"json",
					success : function(data){
						if(data[0].MSG == '修改成功') 
						viewFilePath = data[0].filePath;
					}
				}); 
			}
			
				/**删除预览文件**/
			function delViewFile(){
				$.ajax({
					type : "POST",
					url : '<%=request.getContextPath()%>/Svl_Yktj',
					data : 'active=delViewFile&filePath=' +viewFilePath,
					dataType:"json",
					success : function(data){
						if(data[0].MSG == '删除成功') viewFilePath = '';
					}
				});  
			}
				/**下载学生学籍卡*/
		  	function downXJK(){
		  		$("#exportIframe").attr("src", '<%=request.getContextPath()%>/download_xjk.jsp?filePath=' +encodeURI(encodeURI(viewFilePath)));
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