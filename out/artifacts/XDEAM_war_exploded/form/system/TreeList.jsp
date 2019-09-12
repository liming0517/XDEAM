<%
//@author liuyi
//显示生成类别树根据类别节点
%>

<%@ page 
language="java"
contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"
%>
<%@ page import="com.pantech.src.develop.common.Cls_CommonHtmlData"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ taglib uri="/WEB-INF/taglib/securityTag.tld" prefix="security" %>
<%@ taglib uri="/WEB-INF/taglib/util.tld" prefix="util" %>
<script type="text/javascript" src="<%=request.getContextPath()%>/script/tree/js/xtree.js"></script>
<%
String type =MyTools.StrFiltr( request.getParameter("type")).trim(); //处理方式代码
String needRefresh=MyTools.StrFiltr(session.getValue("needRefresh") );
session.removeValue("needRefresh");
String onLoadEvent="";
if (needRefresh.equalsIgnoreCase("true")) {
   onLoadEvent="onload=doRefresh();";
}
 //rootType=&type=TypeConfig
String rootType=MyTools.StrFiltr(request.getParameter("rootType"));
//扩展属性
String iSelectMode ="" +  MyTools.StringToInt(MyTools.StrFiltr( request.getParameter("selectMode"))) ; //可以选择的方式 0-单选 1-多选
String iNodeType=MyTools.StrFiltr( request.getParameter("nodeType")); //部门类型
String iReturnText=MyTools.StrFiltr( request.getParameter("returnText"));  //是否返回显示的文本
String iAction=MyTools.StrFiltr( request.getParameter("action"));
String iCanSubSelected=MyTools.StrFiltr( request.getParameter("CanSubSelected"));
String iTableName=MyTools.StrFiltr( request.getParameter("tableName"));
String iSelectSql=MyTools.StrFiltr( request.getParameter("selectSql"));  //默认选中的项目

if (iTableName.trim().equals("") ){
   iTableName=MyTools.getProp(request,session.getAttribute("language"),"[TreeList_01]");
}
if (iAction.trim().equals("") ){
   iAction="NodeList";
}
%>
<script>
var type="<%=type%>";
//得到被选择的节点
function getSelected(){
     var str = tree.getSelectIds();

	//过滤重复的信息和空值
	var arrChar;
	var strValue="";
	var i=0;
	var j=0;
	arrChar=str.split(",");
	for (i=0;i<arrChar.length;i++){

	   //循环检查每一个值在strValue的位置，如果没有重复则可以用，即只保留头一次的值
	   str=","+strValue+","  ;  //保证唯一性
	   if (str.indexOf(","+arrChar[i]+",")<0 && arrChar[i].length>0){  //必须要加， 否则可能出错
	      if (strValue.length>0){
		      strValue=strValue + "," + arrChar[i];
	      }else{
	          strValue=strValue +  arrChar[i];
	      }
	   }
	}
	document.all.icSelValue.value=strValue;
	//document.all.icTest.value=strValue;
	return strValue;
}
//返回被选择的节点
function getSel(){
	
	var strValue="";
	strValue=getSelected();
	//作为窗口打开	 hades
	//alert(strValue);
	window.returnValue = strValue;
	//window.close();
	window.close();
	
}
/**
 * 将结点的值传递到左边框架中
 * @param id - 结点id
 */
function SendValue(id,name){
    if(type=="") return false;
	//获得左边框架对象
	var leftFrame = eval("window.parent.editarea.location");
	var leftUrl = "";
	leftUrl = eval("window.parent.editarea.document.all.ReqUrl.value");
	
	if(leftUrl.indexOf("?")!=-1){
		leftUrl=leftUrl.substring(0,leftUrl.indexOf("?"));
	}
		
	if (type=="Config"){ 
		leftFrame.href = leftUrl + "?typename=<%=rootType%>&id="+id;
	}else if(type=="TypeConfig"){
		leftUrl_Type = eval("window.parent.editarea.document.all.ReqUrl.value");

	    if(leftUrl_Type.indexOf("?")!=-1){
		leftFrame.href = "<%=request.getContextPath()%>" + leftUrl_Type + "&TypeCode="+id+"&rootType=<%=rootType%>";
	    
	    }
	    else{
		leftFrame.href = "<%=request.getContextPath()%>" + leftUrl + "?TypeCode="+id+"&rootType=<%=rootType%>";
	}
	}
}
//选择根节点
function doSelectRoot(){

   
    if (type=="Config"){ 
  		SendValue("","");
    }
}

function loadWindow(){ 
   if(parent.editarea.document.readyState != "complete"){
       setTimeout("loadWindow()", 1000);
   }
}
if(parent.editarea != undefined){
	loadWindow();
}
</script>

<%
	Cls_CommonHtmlData cchd=new Cls_CommonHtmlData(request.getContextPath());
%>

<%out.print(cchd.setHtmlTop());//页面头部分%>
<%out.print(cchd.setBodyTop());%>
<%out.print(cchd.setTableTop(MyTools.getProp(request,session.getAttribute("language"),"[TreeList_02]"),"width:100%"));%>

<INPUT type="hidden" name="URL" size="20">
		<table width="100%" border="0" align="center" cellpadding="0" cellspacing="1" class="tableborder">
		<tr>
			
			<td align="center">
              <!--这里放树-->
              <input type="hidden" onclick="getSel();" value="<%=MyTools.getProp(request,session.getAttribute("language"),"[TreeList_03]")%>">
              <% if (!type.equalsIgnoreCase("Config")&&!type.equalsIgnoreCase("TypeConfig")){ %>
              <input type="button" onclick="getSel();" value="<%=MyTools.getProp(request,session.getAttribute("language"),"[TreeList_04]")%>">
              <% } %>
              <img src="<%=request.getContextPath()%>/images/But/qbzk.gif" name="show" onclick="tree.expandAll();">
              <!--input type="button" onclick="tree.expandAll();" value="<%=MyTools.getProp(request,session.getAttribute("language"),"[TreeList_05]")%>"-->
              <img src="<%=request.getContextPath()%>/images/But/qbsl.gif" name="show" onclick="tree.collapseAll();">
              <!--input type="button" onclick="tree.collapseAll();" value="<%=MyTools.getProp(request,session.getAttribute("language"),"[TreeList_06]")%>"-->

			</td>
			
		</tr>
		<tr>
			
			<td >
              <util:TreeList selectMode="<%=MyTools.StringToInt(iSelectMode)%>"  nodeType="<%=iNodeType%>" returnText="<%=iReturnText%>"  canSubSelected="<%=iCanSubSelected%>" rootType="<%=rootType%>" selectAction="<%=iAction%>" tableName="<%=iTableName%>"  selectSql="<%=iSelectSql%>"/>

			</td>
			
		</tr>

		

	  <tr>
			
			<td align="center">
              <!--这里放树-->
              
              <input type="hidden" id="icShowSel" name="icShowSel"  onclick="getSelected();" value="<%=MyTools.getProp(request,session.getAttribute("language"),"[TreeList_03]")%>">
              
              
              <input type="hidden" id="icSelValue" name="icSelValue" value="">
              <% if (!type.equalsIgnoreCase("Config")&&!type.equalsIgnoreCase("TypeConfig")){ %>
              <input type="button" onclick="getSel();" value="<%=MyTools.getProp(request,session.getAttribute("language"),"[TreeList_04]")%>">
              <% } %>
              <img src="<%=request.getContextPath()%>/images/But/qbzk.gif" name="show" onclick="tree.expandAll();">
              <!--input type="button" onclick="tree.expandAll();" value="<%=MyTools.getProp(request,session.getAttribute("language"),"[TreeList_05]")%>"-->
              <img src="<%=request.getContextPath()%>/images/But/qbsl.gif" name="show" onclick="tree.collapseAll();">
              <!--input type="button" onclick="tree.collapseAll();" value="<%=MyTools.getProp(request,session.getAttribute("language"),"[TreeList_06]")%>"-->

				<input type="hidden" onclick="tree.select(tree);" value="<%=MyTools.getProp(request,session.getAttribute("language"),"[TreeList_07]")%>">
                <input type="hidden" onclick="tree.doSelectSubTree(tree);" value="<%=MyTools.getProp(request,session.getAttribute("language"),"[TreeList_08]")%>">
			</td>
			
		</tr>
		</TABLE>
<%out.print(cchd.HtmlBottom());//页面尾部分%>

<SCRIPT language="JavaScript">
if (type=="Config"){ 
	if(document.all.URL.value==""){
		leftUrl = eval("window.parent.editarea.location.href");
		if(leftUrl.indexOf("?")!=-1){
			leftUrl=leftUrl.substring(0,leftUrl.indexOf("?"));
		}
		document.all.URL.value = leftUrl;
	}
}
</SCRIPT>