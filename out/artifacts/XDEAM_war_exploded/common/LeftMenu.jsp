<%@ page 
language="java"
contentType="text/html; charset=gb2312"
pageEncoding="GBK"
%>

<%@ page import="java.io.*,java.util.*,com.pantech.base.common.purview.*"%>
<%@ page import="com.pantech.base.common.config.*"%>
<%@ taglib uri="/WEB-INF/taglib/util.tld" prefix="util" %>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.src.develop.store.user.UserProfile"%>
<%@ page import="com.pantech.src.develop.store.user.ModuleObject"%>

<%@ page import="com.pantech.base.common.db.*"%>
<%
//modify by hades 2005-7-13
//使用通用树


String iSelectMode ="2" ; //可以选择的方式  无选择框
String iNodeType="B"; //菜单类型 纵向菜单
String iAction="OpenUrl(\"[@@URL]\")";  //20081113 modify by hades 必须使用JS参数' 否则可能出错
//String iTableName="V_Node_Module";  //设定目标
//使用多语言菜单 2007-04-26 hades
String iTableName=MyTools.getProp(request,session.getAttribute("language"),"[LeftMenu_01]");



//2006-9-14 hades 增加权限筛选 
//20081113 modify by hades 必须使用转义参数，否则参数若超过2K则出错
String filterSql="";
filterSql="@@@GetMenuFilterSql@@@";  //MyTools.GetMenuFilterSql(request);

%>


<html>
<head>
<title>tree</title>
<link href="<%=request.getContextPath()%>/css/wiserunoa.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<%=request.getContextPath()%>/script/tree/js/xtree.js"></script>
<script type="text/javascript">
/**
 * 将结点的值传递到左边框架中
 * @param id - 结点id
 */
function SendValue(id){
	//获得左边框架对象
	var leftFrame = eval("window.parent.left.location");
	leftFrame.href = "MenuList.jsp?id="+id;
}
//选择根节点
function doSelectRoot(){

   

}

/**
 * 打开路径
 */
function OpenUrl(url){
   //如果是外部地址，则不引用根目录 
    //  alert("request.getContextPath() ====:" + "<%=request.getContextPath()%>");
	var urlStr;
	if (url=="" || url==" "  || url=="#.#" || url=="/form/.."){
		return false;
	}
	if (url.indexOf("/")==0){
	  urlStr= '<%=request.getContextPath()%>'+url;
	 }else{
	  urlStr= url;
	 }
	window.parent.right.location=urlStr;
}

</script>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312"><style type="text/css">
<!--
body {
	margin-left: 10px;
	margin-top: 10px;
}
-->
</style></head>

<!--onload="tree.expandAll()"  2006 9 20 hades 不要全部展开-->
<body eftmargin="0">
<form name="form1" action="" method="post">
	<TABLE width="160" border="0" cellspacing="0" cellpadding="0">
		<TBODY>
			<TR>
				<TD  >
	   			<!--MenuTree.jsp-->
					<script type="text/javascript">
					   treeRootDir='<%=request.getContextPath()%>'+"/"; 
					</script>

					<util:TreeList selectMode="<%=MyTools.StringToInt(iSelectMode)%>"  nodeType="<%=iNodeType%>"   selectAction="<%=iAction%>" tableName="<%=iTableName%>" filterSql="<%=filterSql%>" treeType="" />

					 <!-- 接受从左边传递过来的值 --> 
					<input type="hidden" name="PerCode" value="">
                </TD> 
			</TR>	
			
		</TBODY>
	</TABLE> 
</form>
</body>
</html>
	
	
