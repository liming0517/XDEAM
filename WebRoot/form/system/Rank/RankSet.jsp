<%
//页面名称：form/system/Rank/RankSet.jsp
//页面作用：权限信息管理，主要用于管理系统中所有用到的明细级权限
//制作人员：YH
//创建时间：2009.8.25
//其他说明：重要功能，框架性模块
%>
<%@ page 
language="java"
contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"
%>
<%@ page import="com.pantech.src.develop.common.Cls_CommonHtmlData"%>
<%@ page import="com.pantech.src.develop.common.rank.RankInfoBean"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ taglib uri="/WEB-INF/taglib/util.tld" prefix="util" %>
<%
//界面类
Cls_CommonHtmlData cchd=new Cls_CommonHtmlData(request.getContextPath());
//记录JSP地址
String url = "";
if(request.getAttribute("URL")!=null){
	url = (String)request.getAttribute("URL");
}else{
	url = request.getRequestURL().toString();
}

//界面数据类
RankInfoBean bean=(RankInfoBean)request.getAttribute("RankInfo");
if(bean==null){
	try{
		bean=new RankInfoBean(MyTools.StrFiltr(request.getParameter("id")).trim(),request);
	}catch(Exception e){
		e.printStackTrace();
		bean=new RankInfoBean(request);
	}
}

//处理页面同步刷新
String needRefresh=MyTools.StrFiltr(session.getValue("needRefresh")).trim();
session.removeValue("needRefresh");
String onLoadEvent="";
if (needRefresh.equalsIgnoreCase("true")) {
   onLoadEvent="onload=doRefresh();";
}
%>

<%out.print(cchd.setHtmlTop());//页面头部分%>
<%out.print(cchd.setBodyTop(onLoadEvent));%>

<form action="<%=request.getContextPath()%>/Svl_RankManage" method="Post" name="form1">

<%out.print(cchd.setTableTop("权限信息设置 <font color=red>"+bean.getMsg()+"</font>"));%>

<table width="100%" border="0" cellspacing="1" class="tableborder" cellpadding="0">
	<tr class="trstyle">
		<td>权限代码：</td>
		<td><input type="text" size="25" maxlength="20" name="icRankCode" value="<%=bean.getRankCode()%>" readonly="readonly"></td>
	</tr>
	<tr class="trstyle">
		<td>类别名称：</td>
		<td><input type="text" size="25" maxlength="20" name="icRankName" value="<%=bean.getRankName()%>"></td>
	</tr>
	<tr class="trstyle">
		<td>类别描述：</td>
		<td><textarea rows="3" cols="40" name="icRankDesc"><%=MyTools.convertChar(bean.getRankDesc())%></textarea></td>
	</tr>
	<!--tr class="trstyle">
		<td>权限赋予对象：</td>
		<td><a onClick="OpenUserList()">添加</a>&nbsp;<a onClick="ClareList()">清除</a><br/>
			<select size='10' style="width=160px" name="icRankName" id="icRankName">
				<option>1</option>
				<option>2</option>
			</select>
			<input type="hidden" name="icRankValue" value="<%//=bean.getRankValue()%>">
			</td>
	</tr-->
	<tr class="trstyle">
		<td>状态：</td>
		<td><select name="icRankState">
				<option value="1" <%=MyTools.WriteSelect("1",bean.getRankState())%>>有效</option>
				<option value="0" <%=MyTools.WriteSelect("0",bean.getRankState())%>>无效</option>
			</select>
			</td>
	</tr>
	<TR class="trstyle">
		<TD colspan="2" align="center">
			<img src="<%=request.getContextPath()%>/images/But/xz.gif" name="add" onclick="doSubmit(this.name)">
			<!--INPUT type="button" value="新增" name="show" onclick="SwapDiv('')"-->
			<img src="<%=request.getContextPath()%>/images/But/xg.gif" name="mod" onclick="doSubmit(this.name)">
			<!--INPUT type="button" value="修改" name="mod" onclick="doSubmit(this.name)"-->
			<img src="<%=request.getContextPath()%>/images/But/shanc.gif" name="del" onclick="doSubmit(this.name)">
			<!--INPUT type="button" value="删除" name="del" onclick="doSubmit(this.name)"-->
			<!--img src="<%=request.getContextPath()%>/images/But/bj.gif" name="up" onclick="doSubmit(this.name)"-->
			<!--input type="button" value="上移" name="up" onclick="doSubmit(this.name)"-->
			<!--img src="<%=request.getContextPath()%>/images/But/bj.gif" name="down" onclick="doSubmit(this.name)"-->
			<!--input type="button" value="下移" name="down" onclick="doSubmit(this.name)"-->
			<input type="hidden" name="ReqUrl" value="<%=url%>">
			<input type="hidden" name="active">
			<input type="hidden" name="CurCode" value="<%=bean.getRankCode()%>">
			
		</TD>
	</TR>
</table>

<%out.print(cchd.setTableBottom());%>

</form>

<%out.print(cchd.setHtmlBottom());%>


<script>
function OpenUserList(){
	var val = showModalDialog('<%=request.getContextPath()%>/form/system/DeptTree.jsp?returnText=1&selectMode=1&includeUser=1','','');
	if(val == "" || val == undefined){
		document.getElementById("icPJ_SALES").value = '';
		return;
	}
	var arr = val.split(",");
	var iName="";
	var iCode="";
	for(var i=0;i<arr.length;i++){
		var arr1=arr[i].split(";");
		iName=iName+arr1[1]+",";
		iCode=iCode+arr1[0]+",";
	}
	iName=iName.substring(0,iName.length-1);
	iCode=iCode.substring(0,iCode.length-1);
	//document.getElementById("icRankName").value = iName;
	
	document.getElementById("icRankValue").value = iCode;
}

function ClareList(){
	//document.getElementById("icRankName").options.length=0;
	var obj = eval(document.getElementById("icRankName"));
	alert(obj.options.length);
}
//删除某一个option
//document.getElementById("ddlResourceType").options.remove(indx); 
//添加某一个option
//document.getElementById("ddlResourceType").options.add(new Option(text,value)); 
//取值
//function getvalue(obj)
   // {
    //    var m=obj.options[obj.selectedIndex].value
   //     alert(m);//获取value
  //      var n=obj.options[obj.selectedIndex].text
 //       alert(n);//获取文本
//    }
// 检测是否有选中 
 //	if (objSelect.selectedIndex > - 1 ) { 
// 说明选中 
	//} else { 
// 说明没有选中 
	//} 
 //
 //2 删除被选中的项 
//objSelect.options[objSelect.selectedIndex] = null ; 
//
//3 增加项 
//objSelect.options[objSelect.length] = new Option( " 你好 " , " hello " ); 
//
//4 修改所选择中的项 
//objSelect.options[objSelect.selectedIndex] = new Option( " 你好 " , " hello " ); 
//
//5 得到所选择项的文本 
//objSelect.options[objSelect.selectedIndex].text; 
//
//6 得到所选择项的值 
//objSelect.options[objSelect.selectedIndex].value; 




//显示新增结点信息的表格
function SwapDiv(id){
	if(id == "cancel"){ //用来隐藏表格
		NodeInfo.style.display = "none";
	}else{
		NodeInfo.style.display = "";
	}
}
//提交页面
function doSubmit(id){
	if(checkform(id)){
		document.form1.active.value=id;
		document.form1.submit();
	}
}
//页面信息检查
function checkform(id){
	return true;
}
//刷新类别树的页面
function doRefresh(){
	parent.treearea.location.reload();
}
</script>