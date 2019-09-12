<%@ page 
language="java"
contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"
%>
<!--
权限设定　左边界面　显示　角色　部门列表
ＭＯＤＩＦＹ　ＢＹ　ＨＡＤＥＳ
２００５－６－２６

-->
<%@ taglib uri="/WEB-INF/taglib/securityTag.tld" prefix="security" %>
<%@ page import = "com.pantech.base.common.config.*"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import = "java.util.Vector"%>
<%@page import="com.pantech.base.common.config.PurView"%>
<%
PurView purview=new PurView(request);
Vector v1 = purview.GetUserAuth();
Vector v2 = purview.GetUserDept();

 //获取消息
String sMsg="";
sMsg=sMsg + " " + MyTools.convertChar((String) request.getAttribute("MSG"));
request.removeAttribute("MSG");
%>
<html>
<head>
<link href="<%=request.getContextPath()%>/css/wiserunoa.css" rel="stylesheet" type="text/css">
<script>
var AuthCode=""; //被选中的角色或部门

function showTree(code){
	//左边框架将部门或者角色code传递到右边，并显示该部门/角色的权限树。

    document.all.icTitle.innerText="目前正在设置 " + code + "的权限";

	AuthCode=code;
	//获得右边框架对象
	var rightFrame = eval("window.parent.treearea.location");
	rightFrame.href = "<%=request.getContextPath()%>/form/system/TreeList.jsp?type=Config&selectMode=1&tableName=V_Node_Module&selectSql=节点代码 in(select id from V_Node_AuthModule where permissionCode='" +code + "')";
}

/**
 * 切换显示/隐藏
 * @author HueVan
 */
function Show(obj){
 /*
 var a = document.all[obj];
 for(var i=0; i<a.length; i++){
 	a[i].style.display = (a[i].style.display=="none"?"":"none"); 	
 }
 */
 var a=eval("document.all."+obj);
 a.style.display =a.style.display=="none"?"":"none";
 

}
/**
 * 初始化了，列表显示/隐藏<br>
 * 默认为未展开
 * @author HueVan
 */
function init(obj){
/*
var a = document.all[obj];
 for(var i=0; i<a.length; i++){
 	a[i].style.display = "none"; 	
 }
 */
  var a=eval("document.all."+obj);
 a.style.display ="none"; 	
}

/**
 * 显示选中的权限
 */
function UpdateAuth(){
	//alert(document.form1.hPerCode.value);
	//var percode = document.form1.hPerCode.value;
	if(AuthCode == null || AuthCode == ""){
		alert("请在左边选择角色或者部门后再设置！");
		return;
	}
	//获得右边框架对象
	var rightFrame = eval("parent.right.document");
	rightFrame.all.icShowSel.click();
	document.all.icSelValue.value=rightFrame.all.icSelValue.value;  //获得被选择的权限
	//alert(rightFrame.all.icSelValue.value);
	document.form1.action="<%=request.getContextPath()%>/Svl_UpdateAuth";
	document.form1.icPerCode.value=AuthCode;
	document.form1.hOpType.value="update";
	document.form1.submit();
}

//var title="";

</script>
</head>
<body onload = "init('auth');init('dept')" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" class="contentbg">

<form name="form1" action="AuthDeptList.jsp" method="post">
<TABLE border="0" width="100%" cellpadding="0" cellspacing="0">
	<TBODY >
		<TR>
			<TD width="1%" height="28"><IMG
				src="<%=request.getContextPath()%>/images/border_r1_c1.gif"
				width="16" height="28"></TD>
			<TD width="97%" class="tabletop">
			<P class="talbetitle">权限设置    <font color=red><%=sMsg%></font> </P>
			</TD>
			<TD width="2%"><IMG
				src="<%=request.getContextPath()%>/images/border_r1_c4.gif"
				width="16" height="28"></TD>
		</TR>
		
		<TR  id="icTrTitle" >
			<TD class="tableleft">&nbsp;</TD>
			<TD align="center"  class="tabletop" id="icTitle" >
			    注意:请先选定角色/部门/人员后再进行操作
			   
			</TD>
			<TD class="tableright">&nbsp;</TD>
			
		</TR>
		
		<TR align="center">
		<TD class="tableleft">&nbsp;</TD>
		<TD>
		　　<input type=button value="更新权限" onclick="UpdateAuth()">
			
		</TD>
		<TD class="tableright">&nbsp;</TD>
		</TR>
		
		
		<TR onclick="Show('auth')" style="cursor:hand">
			<TD class="tableleft">&nbsp;</TD>
			<TD align="center"  class="tabletop" >【角色列表】←点击可以收缩或者展开</TD>
			<TD class="tableright">&nbsp;</TD>
		</TR>
		
		
		
		<TR id=auth>
		    <TD class="tableleft">&nbsp;</TD>
    		<TD>
			<TABLE width="100%" cellpadding="0" cellspacing="1" class="tableborder">
			  <%for (int i = 0; i < v1.size(); i+=2) {%>
			  <TR class="trstyle">
					<TD valign="top" width="30%">
					   <input type="radio" id="icAuth" name="icAuth" onclick="showTree('<%=v1.get(i).toString()%>')">
					   <A href="#"
						onclick="showTree('<%=v1.get(i).toString()%>')"><%=v1.get(i).toString()%>&nbsp;<%=v1.get(i+1).toString()%></A></TD>
					<TD valign="top" width="70%">
						<TABLE  width="100%" cellpadding="0" cellspacing="1" class="tableborder">
							<security:showUser code="<%=v1.get(i).toString()%>" />
						</TABLE>
					</TD>
			  </TR>
			  <%}%>
			</TABLE>
			</TD>	
			<TD class="tableright">&nbsp;</TD>
		</TR>
		
		
		
		<TR onclick="Show('dept')" style="cursor:hand">
			<TD class="tableleft">&nbsp;</TD>
			<TD align="center" bgcolor="#b5b5ff" class="tabletop" >【部门列表】←点击可以收缩或者展开</TD>
			<TD class="tableright">&nbsp;</TD>
		</TR>
		
		
		<TR id=dept>
		 <TD class="tableleft">&nbsp;</TD>
    		<TD>
			<TABLE width="100%" cellpadding="0" cellspacing="1" class="tableborder">
			 <%for (int i = 0; i < v2.size(); i+=2) {%>
			  <TR class="trstyle">
					<TD valign="top" width="30%">
					<input type="radio" id="icAuth" name="icAuth" onclick="showTree('<%=v2.get(i).toString()%>')">
					<A href="#"
						onclick="showTree('<%=v2.get(i).toString()%>')"><%=v2.get(i).toString()%>&nbsp;<%=v2.get(i+1).toString()%></A></TD>
					<TD valign="top" width="70%">
					<TABLE  width="100%" cellpadding="0" cellspacing="1" class="tableborder">
						<security:showUser code="<%=v2.get(i).toString()%>" />
					</TABLE>
					</TD>
			  </TR>
			  <%}%>
			</TABLE>
			</TD>	
			<TD class="tableright">&nbsp;</TD>
		</TR>
		<TR align="center">
		<TD class="tableleft">&nbsp;</TD>
		<TD>
		　　<input type=button value="更新权限" onclick="UpdateAuth()">
			<input type="hidden" name="ReqUrl" value="<%=request.getContextPath()%>/base/purview/AuthDeptList.jsp">
			<input type="hidden" id="icSelValue" name="icSelValue" value="">
			<input type="hidden" name="icPerCode" value="">			 <!--用于判定权限代码-->
			<input type="hidden" name="hOpType" value="">			 <!--存盘标记-->
			
		</TD>
		<TD class="tableright">&nbsp;</TD>
		</TR>
		
		<TR>
			<TD valign=top><IMG src="<%=request.getContextPath()%>/images/border_r3_c1.gif"
				width="16" height="19"></TD>
			<TD class="tablebottom">&nbsp;</TD>
			<TD valign=top><IMG src="<%=request.getContextPath()%>/images/border_r3_c4.gif"
				width="16" height="19"></TD>
		</TR>
	</TBODY>
</TABLE>
</form>
</body>
</html>
