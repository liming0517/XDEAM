<%@page import="com.pantech.base.common.tools.MyTools"%>
<%@page import="com.jspsmart.upload.SmartUpload"%>
<%@page contentType="text/html;charset=utf-8"%>
<%@page import="java.io.File"%>
<%@page import="java.net.URLDecoder"%>
<%
	//获取临时路径
	//String filePath = request.getParameter("filePath");
	String filePath = URLDecoder.decode(MyTools.StrFiltr(request.getParameter("filePath")), "UTF-8");
	//新建一个SmartUpload对象
	//com.jspsmart.upload.SmartUpload mySmartUpload = new com.jspsmart.upload.SmartUpload();
	SmartUpload mySmartUpload = new SmartUpload("UTF-8");
	//初始化
	mySmartUpload.initialize(pageContext);
	try{
		//设定contentDisposition为null以禁止浏览器自动打开文件，
		//保证点击链接后是下载文件。若不设定，则下载的文件扩展名为
		//doc时，浏览器将自动用word打开它。扩展名为pdf时，
		//浏览器将用acrobat打开。
		mySmartUpload.setContentDisposition(null);
		
		//中文名处理
		String fileName = new String(filePath.substring(filePath.lastIndexOf("/")+1).getBytes("gbk"),"ISO-8859-1");
		
		//下载文件
		mySmartUpload.downloadFile(filePath, "text/html", fileName);
		
		
		//删除服务器上文件
		File file = new File(filePath);
		//路径为文件且不为空则进行删除  
// 	    if (file.isFile() && file.exists()) {  
// 	        file.delete();
// 	    }
	}catch(Exception e){
		e.printStackTrace();
	}finally{
		//清除OutputStream报错
		out.clear();
		out = pageContext.pushBody();
	}
%>