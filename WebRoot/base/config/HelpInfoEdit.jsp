<%
//@author liuyi
//生成文章编辑控件
//创建日期：2005.5.16
%>
<%@ page 
language="java"
contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"
%>
<script>
var HostUrl="<%=request.getContextPath()%>\/uploadimages\/";
var doc_html_html="";
var ImagesPath="<%=request.getContextPath()%>\/images\/rte.gif";
if(parent.parent && parent.parent.doc_html_html){
	doc_html_html=parent.parent.doc_html_html;
}else{
	document.write("<script src='<%=request.getContextPath()%>\/script\/rte.js'><\/script>");
}
if(doc_html_html==""){
	setTimeout("location.reload()",1000);
}else{
	document.write(doc_html_html);
}
</script>
