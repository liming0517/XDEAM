<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="com.pantech.base.common.tools.*"%>
<%@page import="com.pantech.devolop.registerScoreStatistics.XkkszlfxBean"%>
<%@page import="java.net.URLDecoder"%>
<%@ taglib uri="http://java.pageoffice.cn" prefix="po"%>
<%
	//String path = request.getContextPath();
	//String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	XkkszlfxBean bean = new XkkszlfxBean(request);
	bean.setXNXQBM(MyTools.StrFiltr(request.getParameter("XNXQBM"))); //学年学期编码
	bean.setCOURSENAME(URLDecoder.decode(URLDecoder.decode(MyTools.StrFiltr(request.getParameter("COURSENAME")), "UTF-8"), "UTF-8"));//课程名称
	bean.setEXAMTYPE(MyTools.StrFiltr(request.getParameter("EXAMTYPE"))); //考试类型
	bean.setTEACODE(MyTools.StrFiltr(request.getParameter("TEACODE"))); //教师工号
	bean.setTEANAME(URLDecoder.decode(URLDecoder.decode(MyTools.StrFiltr(request.getParameter("TEANAME")), "UTF-8"), "UTF-8")); //教师姓名
	Vector infoVec = bean.loadPrintView();
	String filePath = MyTools.StrFiltr(infoVec.get(0));
%>
<html>
<head>
	<title>打印预览</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
</head>
	<body style="margin:0;" scroll="no">
		<!-- *********************pageoffice组件的使用 **************************-->
		<script language="javascript" type="text/javascript">
			var viewFilePath = '<%=filePath%>';
			var semCode = '<%=bean.getXNXQBM()%>';
			var courseName = '<%=bean.getCOURSENAME()%>';
			var teaCode = '<%=bean.getTEACODE()%>';
			var teaName = '<%=bean.getTEANAME()%>';
			var examType = '<%=bean.getEXAMTYPE()%>';
		
			//打印
			function print(){
				saveDocumentContent();
				document.getElementById("PageOfficeCtrl1").PrintPreview();
				//document.getElementById("PageOfficeCtrl1").PrintOut(true);
				//锁定成绩状态
				//window.parent.lockScore();
			}
		
			//全屏
			function SetFullScreen() {
				document.getElementById("PageOfficeCtrl1").FullScreen = !document.getElementById("PageOfficeCtrl1").FullScreen;
			}
			
			//下载
			function download(){
				saveDocumentContent("download");
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
				delViewFile();
			}
			
			//保存信息
			function saveDocumentContent(type){
				document.getElementById("PageOfficeCtrl1").EnableExcelCalling();//退出编辑状态，高版本pageoffice可用
				var tempStr_1 = document.getElementById("PageOfficeCtrl1").Document.Worksheets(1).Range("B11").Value;
				var tempStr_2 = document.getElementById("PageOfficeCtrl1").Document.Worksheets(1).Range("B14").Value;
				
				$.ajax({
					type : "POST",
					url : '<%=request.getContextPath()%>/Svl_Xkkszlfx',
					data : 'active=saveInfo&XNXQBM=' + semCode + '&COURSENAME=' + encodeURI(courseName) + 
						'&TEACODE=' + teaCode + '&EXAMTYPE=' + examType + 
						'&PROBLEM=' + (tempStr_1==undefined?'':tempStr_1) +
						'&MEASURE=' + (tempStr_2==undefined?'':tempStr_2),
					dataType:"json",
					success : function(data){
						if(data[0].MSG == '保存成功'){
							showMsg(data[0].MSG);
							
							if(type == 'download'){
								//window.parent.downloadStuReport();
								downloadStuReport();
							}
						}
					}
				});
			}
			
			/**删除预览文件**/
			function delViewFile(){
				$.ajax({
					type : "POST",
					url : '<%=request.getContextPath()%>/Svl_Xkkszlfx',
					data : 'active=delViewFile&filePath=' + viewFilePath,
					dataType:"json",
					success : function(data){
						if(data[0].MSG == '删除成功') viewFilePath = '';
					}
				});
			}
			
			/**下载*/
			function downloadStuReport(){
				$('#mask').show();
				$.ajax({
					type : "POST",
					url : '<%=request.getContextPath()%>/Svl_Xkkszlfx',
					data : 'active=exportReport&EXAMTYPE=' + examType + '&XNXQBM=' + semCode + '&COURSENAME=' + encodeURI(courseName) + '&TEACODE=' + teaCode + '&TEANAME=' + encodeURI(teaName),
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
			<po:PageOfficeCtrl id="PageOfficeCtrl1"></po:PageOfficeCtrl>
		</div>
		
		<!-- 下载excel -->
		<iframe id="exportIframe" src="" style="width:0; height:0;"></iframe>
	</body>
</html>