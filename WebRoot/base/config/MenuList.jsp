<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<%@ page 
language="java"
contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"
%>
<%@ page import="com.pantech.base.common.config.MenuInfoBean"%>
<%@ page import="com.pantech.base.common.tools.*"%>
<%
//是否需要刷新右边的树
//String needRefresh="";
String needRefresh=MyTools.StrFiltr(session.getValue("needRefresh") );
session.removeValue("needRefresh");

String onLoadEvent="";
if (needRefresh.equalsIgnoreCase("true")) {
   onLoadEvent="onload=doRefresh();";
}

String id = request.getParameter("id");
MenuInfoBean menuInfo = new MenuInfoBean(request);
if(id != null){
	int i = MyTools.StringToInt(id);
	//System.out.println("节点id==" + id);
	menuInfo = new MenuInfoBean(i,request);
}
%>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<link href="<%=request.getContextPath()%>/css/wiserunoa.css" rel="stylesheet" type="text/css">
<TITLE>MenueList.jsp</TITLE>
<script>
/**
 * 提交函数
 * 根据不同的按钮提交给servlet不同的参数
 */
function doSubmit(type){
	var o = eval(document.form1);
	//2005-6-26 HADES
	o.action = "<%=request.getContextPath()%>/Svl_System?type="+type;
	o.submit();
	
	doRefresh();

}

/**
 * 在右边框架点击树结点时候触发此函数
 * 根据点击的结点属性（叶子结点还是父结点）来屏蔽部分按钮
 * @param id 结点ID
 */
function Check(id){
	if(id == 0){ //如果是父结点，只能进行修改，增加操作
		
	}

}
/**
 * 刷新右边框架
 */
function doRefresh(){
	//alert("refresh---");
	parent.right.location.reload();
}



/**
 * 显示新增结点信息的表格
 */
function SwapDiv(id,parId){
	var a = document.getElementById("NodeInfo");
	//父节点id
	var b = document.form1.NodeID.value;
	//父节点层次
	var level = document.form1.MenuLevel.value;
	if (level.length==0){
	   level=0;
	}
	//父节点类别
	var state = document.form1.State.value;
	if(id == "dis"){ //用来隐藏表格
		a.style.display = "none";
	}else{
		//节点id作为作为父节点id
		document.form1.ParID0.value = b;			
		
		document.form1.MenuLevel0.value = parseInt(level)+1;
		//类别
		document.form1.State0.value = state;
		
		a.style.display = "";
	}

}
//选择根节点
function doSelectRoot(){
  if (type=="Config"){ 
  	SendValue("","");
  }
  
}

</script>
<style type="text/css">
<!--
body {
	margin-left: 10px;
	margin-top: 10px;
}
-->
</style></HEAD>
<BODY <%=onLoadEvent%> class="contentbg">
<form action="" method="Post" name="form1">

<input type="hidden" value="Menu" name="hModuleID">
<table width="98%" border="0" cellspacing="0" cellpadding="0"
			bgcolor="FFFFFF" >
			<tr>


				<td width="1%" height="28"><img src="<%=request.getContextPath()%>/images/border_r1_c1.gif"
					width="16" height="28"></td>


				<td width="97%" class="tabletop">
				<p class="talbetitle">菜单信息 设定者: <%=MyTools.getSessionUserCode(request)%></p>
				</td>


				<td width="2%"><img src="<%=request.getContextPath()%>/images/border_r1_c4.gif" width="16"
					height="28"></td>
			</tr>
			<tr>


				<td class="tableleft">&nbsp;</td>
				<td>
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td>


				<TABLE width="100%" cellpadding="0" cellspacing="1" class="tableborder">
					<TBODY>
						<TR class="trstyle" style="display:none">
							<TD width="159">父结点ID</TD>
							<TD colspan="4" width="458" align=""left""><INPUT type="text"
								name="ParID" value="<%=menuInfo.getParId()%>"></TD>
						</TR>
						<TR class="trstyle" style="display:none">
							<TD width="159">结点ID</TD>
							<TD colspan="4" width="458" align=""left""><INPUT type="text"
								name="NodeID" value="<%=menuInfo.getId()%>" readonly>
							(不可修改)
							</TD>
						</TR>
						<TR class="trstyle">
							<TD width="159">结点文本</TD>
							<TD colspan="4" width="458" align=""left""><INPUT type="text"
								name="NodeName" value="<%=menuInfo.getNodeName()%>"></TD>
						</TR>
						<TR class="trstyle">
							<TD width="159"><FONT COLOR=RED>对应的模块ID</FONT></TD>
							<TD colspan="4" width="458" align=""left""><INPUT type="text"
								name="ModuleID" value="<%=menuInfo.getModuleID()%>">
							*（为空则自动设置）
							</TD>
						</TR>
						<TR class="trstyle">
							<TD width="159">对应模块URL</TD>
							<TD width="458" align=""left"" colspan="4"><INPUT type="text"
								name="ModuleURL" value="<%=menuInfo.getModuleURL()%>">
							
							</TD>
						</TR>
						<TR class="trstyle">
							<TD width="159">菜单类别</TD>
							<TD width="458" align=""left"" colspan="4">
							
							<select name="State" >
							  
							     <option value="A" <%=MyTools.WriteSelect("A",menuInfo.getState())%>>横向菜单</option>
							     <option value="B" <%=MyTools.WriteSelect("B",menuInfo.getState())%>>纵向菜单</option>
							     <option value="C" <%=MyTools.WriteSelect("C",menuInfo.getState())%>>独立模块(隐藏)</option>
							  
							  </select>
							  

							</TD>
						</TR>
						<TR class="trstyle" style="display:none">
							<TD width="159">菜单层次</TD>
							<TD width="458" align=""left"" colspan="4"><INPUT type="hidden"
								name="MenuLevel" value="<%=menuInfo.getLevel()%>"></TD>
						</TR>
						<TR class="trstyle" style="display:none">
							<TD width="159">菜单顺序</TD>
							<TD width="458" align="left" colspan="4"><INPUT type="text"
								name="MenuOrder" value="<%=menuInfo.getOrder()%>"></TD>
						</TR>
						<TR class="trstyle">
							<TD width="159">操作</TD>
							<TD align="left" colspan="4">
							
							<INPUT type="button" value="新增" name="show" onClick="SwapDiv('NodeInfo',document.form1.ParID.value)">
							<INPUT type="button" value="修改" name="mod"  onclick="doSubmit(this.name)">
							<INPUT type="button" value="删除" name="del"  onclick="doSubmit(this.name)">
							 
							<input type="button" value="上移" name="up" onClick="doSubmit(this.name)">
							<input type="button" value="下移" name="down" onClick="doSubmit(this.name)">
							 
							<input type="button" value="左移" name="left" onClick="doSubmit(this.name)">
							<input type="button" value="右移" name="right" onClick="doSubmit(this.name)">
			
							</TD>
						</TR>
					</TBODY>
				</TABLE>
				</td>
					</tr>
				</table>


				</td>


				<td class="tableright">&nbsp;</td>
			</tr>
			<tr>


				<td valign="top"><img src="<%=request.getContextPath()%>/images/border_r3_c1.gif" width="16" height="19"></td>


				<td class="tablebottom">&nbsp;</td>


				<td valign="top"><img src="<%=request.getContextPath()%>/images/border_r3_c4.gif" width="16" height="19"></td>
			</tr>
  </table>

<br>
		<div align="center">
		<table width="98%" border="0" cellspacing="0" cellpadding="0"
			bgcolor="FFFFFF" id="NodeInfo" style="display: none">
			<tr>


				<td width="1%" height="28"><img src="<%=request.getContextPath()%>/images/border_r1_c1.gif"
					width="16" height="28"></td>


				<td width="97%" class="tabletop">
				<p class="talbetitle">增加(子)菜单</p>
				</td>


				<td width="2%"><img src="<%=request.getContextPath()%>/images/border_r1_c4.gif" width="16"
					height="28"></td>
			</tr>
			<tr>


				<td class="tableleft">&nbsp;</td>
				<td>
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td>

					<TABLE border="0" width="100%" cellpadding="1" cellspacing="1">
						<TBODY>
							<TR class="tableline">
								<TD align="center" class="tabletop" width="5"></TD>
								<TD colspan="4" align="center" class="tabletop">
								<P class="talbetitle"></P>
								</TD>
							</TR>
							<TR class="tableline" style="display:none">
								<TD width="5"></TD>
								<TD width="305">该菜单所属父节点ID</TD>
								<TD colspan="3" ><INPUT type="text" name="ParID0"></TD>
							</TR>
							<TR class="tableline">
								<TD width="5"></TD>
								<TD width="305">结点ID</TD>
								<TD colspan="3" ><INPUT type="text" name="NodeID0"
									value="此id将由系统自动生成" readonly></TD>
							</TR>
							<TR class="tableline">
								<TD width="5"></TD>
								<TD width="305">结点文本</TD>
								<TD colspan="3" ><INPUT type="text"
									name="NodeName0"></TD>
							</TR>
							<TR class="tableline">
								<TD width="5"></TD>
								<TD width="305"><FONT COLOR=RED>对应的模块ID</FONT></TD>
								<TD colspan="3" ><INPUT type="text"
									name="ModuleID0"></TD>
							</TR>
							<TR class="tableline">
								<TD width="5"></TD>
								<TD width="305">对应模块URL</TD>
								<TD  colspan="3" ><INPUT type="text"
									name="ModuleURL0" value="/form/.."></TD>
							</TR>
							<TR class="tableline">
								<TD ></TD>
								<TD >菜单类别</TD>
								<TD  colspan="3" >
								  
									 <select name="State0" >
								  
									     <option value="A" <%=MyTools.WriteSelect("A",menuInfo.getState())%>>横向菜单</option>
									     <option value="B" <%=MyTools.WriteSelect("B",menuInfo.getState(),1)%>>纵向菜单</option>
									     <option value="C" <%=MyTools.WriteSelect("C",menuInfo.getState())%>>独立模块(隐藏)</option>
								  
							      </select>	
								</TD>
							</TR>
							<TR class="tableline" style="display:none">
								<TD width="5"></TD>
								<TD width="305">菜单层次<font color="#999900">[自动生成,除非添加0级菜单,否则不需修改]</font></TD>
								<TD  colspan="3" ><INPUT type="text"
									name="MenuLevel0"></TD>
							</TR>
							<TR class="tableline" style="display:none">
								<TD width="5"></TD>
								<TD width="305">菜单顺序</TD>
								<TD  colspan="3" ><INPUT type="text"
									name="MenueOrder0" value="1"></TD>
							</TR>
							<TR class="tableline">
								<TD width="5"></TD>
								<TD width="305">操作</TD>
								<TD align="center" colspan="2"><INPUT type="button"
									name="add" value="提交" onClick="doSubmit(this.name)"></TD>
								<TD align="center"><INPUT type="button" name="dis"
									value="取消" onClick="SwapDiv(this.name)"></TD>
 								<input type="hidden" name="ReqUrl" value="<%=request.getContextPath()%>/base/config/MenuList.jsp">
							</TR>
						</TBODY>
					</TABLE>
					</td>
					</tr>
				</table>


				</td>


				<td class="tableright">&nbsp;</td>
			</tr>
			<!--<tr>


				<td valign="top"><img src="<%=request.getContextPath()%>/images/border_r3_c1.gif" width="16" height="19"></td>


				<td class="tablebottom"></td>


				<td valign="top"><img src="<%=request.getContextPath()%>/images/border_r3_c4.gif" width="16" height="19"></td>
			</tr>-->
			
			<tr>
			<td valign="top"><img src="<%=request.getContextPath()%>/images/border_r3_c1.gif"
				width="16" height="19"></td>
			<td class="tablebottom">&nbsp;</td>
			<td valign="top"><img src="<%=request.getContextPath()%>/images/border_r3_c4.gif"
				width="16" height="19"></td>
		</tr>
		</table>
		</div>
</form>
</BODY>
</HTML>
