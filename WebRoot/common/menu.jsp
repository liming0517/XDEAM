<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="com.pantech.src.develop.Logs.*" %>
<%@ page import="com.pantech.base.common.tools.MyTools" %>
<%@ page import="com.pantech.src.develop.store.user.*" %>
<%@ page import="java.util.Vector"%>
<%@ taglib uri="/WEB-INF/taglib/TextToPicture.tld" prefix="TToP" %>
<%@ taglib uri="/WEB-INF/taglib/util.tld" prefix="util" %>
<%
String root = request.getContextPath();
//out.println("当前在线人数： "+SessionListener.getCount());
%>
<HEAD>
<LINK type="text/css" rel="stylesheet" href="<%=root%>/skins/officexp/officexp.css">
<SCRIPT type="text/javascript" src="<%=root%>/script/poslib.js"></SCRIPT>
<SCRIPT type="text/javascript" src="<%=root%>/script/scrollbutton.js"></SCRIPT>
<SCRIPT type="text/javascript" src="<%=root%>/script/menu4.js"></SCRIPT>
<SCRIPT type="text/javascript" src="<%=root%>/script/browser.js"></SCRIPT>
<SCRIPT type="text/javascript" src="<%=root%>/script/mymenubar.js"></SCRIPT>
<LINK href="<%=request.getContextPath()%>/css/crm.css" rel="stylesheet" type="text/css">
<LINK href="<%=request.getContextPath()%>/css/top.css" rel="stylesheet" type="text/css">
<%
out.println(MyTools.WriteMenuJS(request));
String iSelectMode ="2" ; //可以选择的方式  无选择框
String iNodeType="A"; //菜单类型 纵向菜单
String iAction="OpenUrl(\"[@@URL]\")";
String iTableName="V_Node_Module";  //设定目标
//增加权限筛选
String filterSql="";
filterSql=MyTools.GetMenuFilterSql(request);
%>

<SCRIPT>

function switchSysBar(){
//alert('---');
	if (switchPoint.innerText==5){
		//alert(document.wincontrol.value);
		switchPoint.innerText=6
		parent.main.rows='30,*,46'
		//alert(parent.top.document.body.scrollTop);
		window.scrollTo(0,document.body.scrollHeight)
		//parent.top.document.scrollTo(0,document.body.scrollHeight);
//		parent.top.document.body.scrollTop = 50;
	}else{
		switchPoint.innerText=5
		parent.main.rows='71,*,46'
	}
}
</SCRIPT>
</HEAD>
<body>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td>
            <!--SCRIPT type="text/javascript" src="<%=root%>/script/writemenubar.js"></SCRIPT-->
		</td>
	</tr>
</TABLE>

</body>
