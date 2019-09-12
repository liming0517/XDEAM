<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<%@ page 
language="java"
contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"
%>
<%@ page import="com.pantech.base.common.tools.*"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="com.pantech.src.develop.system.SystemClass"%>
<%

//是否需要刷新右边的树
String needRefresh=MyTools.StrFiltr(session.getValue("needRefresh") );
session.removeValue("needRefresh");

String onLoadEvent="";
if (needRefresh.equalsIgnoreCase("true")) {
   onLoadEvent="onload=doRefresh();";
}


String id =MyTools.StrFiltr( request.getParameter("id")); //部门代码

Vector v = new Vector();

String sParId="";
String sDeptCode="";
String sState="";
String sCName="";
//String sEname=MyTools.StrFiltr( ((HashMap)v.get(0)).get("Ename"));

String sDeptDesc="";
String sLevel="";
String sOrder="";
String sDeptType="";
//2006 8 2 hades
SystemClass Dept=new SystemClass(request);
if(id.trim().length()>0 ){
	v = Dept.GetDeptInfo(id);
}
if (v.size()>0){

 sParId=MyTools.StrFiltr( ((HashMap)v.get(0)).get("PARID"));
 sDeptCode=MyTools.StrFiltr( ((HashMap)v.get(0)).get("DEPTCODE"));
 sState=MyTools.StrFiltr( ((HashMap)v.get(0)).get("STATE"));
sCName=MyTools.StrFiltr( ((HashMap)v.get(0)).get("CNAME"));
//String sEname=MyTools.StrFiltr( ((HashMap)v.get(0)).get("Ename"));

sDeptDesc=MyTools.StrFiltr( ((HashMap)v.get(0)).get("DEPTDESC"));
sLevel=MyTools.StrFiltr( ((HashMap)v.get(0)).get("LEVEL"));
 sOrder=MyTools.StrFiltr( ((HashMap)v.get(0)).get("ORDER"));
//部门类型，各项目必定不同
  sDeptType=MyTools.StrFiltr( ((HashMap)v.get(0)).get("type"));
  //如果类型为空，则需要智能判定
  if (sDeptType.length()==0 ){
      if (sCName.indexOf(MyTools.getProp(request,session.getAttribute("language"),"[DeptList_01]"))>0){
         sDeptType="2";
      }else if (sCName.indexOf(MyTools.getProp(request,session.getAttribute("language"),"[DeptList_02]"))>0){
         sDeptType="1";
      
      }else{
         sDeptType="0";
      }
      
  }

}
%>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<link href="<%=request.getContextPath()%>/css/wiserun.css" rel="stylesheet" type="text/css">
<script language="JavaScript" src="<%=request.getContextPath()%>/script/VerifyForm.js"></script>
<TITLE><%=MyTools.getProp(request,session.getAttribute("language"),"[DeptList_03]")%></TITLE>
<script>
/**
 * 提交函数
 * 根据不同的按钮提交给servlet不同的参数
 */
function doSubmit(type){
	var o = eval(document.form1);
	var sValue="";
	//部门代码要求至少3位，允许更多 2006-8-29 hades
	if(type == "add"){ //添加
	    sValue=o.NodeID0.value;
		if( sValue.length<3 || sValue.indexOf("'")!=-1){
			alert("<%=MyTools.getProp(request,session.getAttribute("language"),"[DeptList_04]")%>");
			return;
		}
		
		o.action = "<%=request.getContextPath()%>/Svl_System?type=add";
		o.submit();
	}
	else if(type == "mod" ){//修改
		sValue=o.NodeID.value;
		if( sValue.length<3 || sValue.indexOf("'")!=-1){
			alert("<%=MyTools.getProp(request,session.getAttribute("language"),"[DeptList_04]")%>");
			return;
		}
		
		o.action = "<%=request.getContextPath()%>/Svl_System?type=mod";
		o.submit();
	}
	else if(type == "del"){//删除
		o.action = "<%=request.getContextPath()%>/Svl_System?type=del";
		o.submit();
	}
	else if(type=="up"){//上移
	//alert(parent.location.href);
		o.action = "<%=request.getContextPath()%>/Svl_System?type=up";
		o.submit();
	}
	else if(type=="down"){//下移
		//alert(parent.location.href);
		o.action = "<%=request.getContextPath()%>/Svl_System?type=down";
		o.submit();
	}
	else if(type=="left"){//<-移
		//alert(parent.location.href);
		o.action = "<%=request.getContextPath()%>/Svl_System?type=left";
		o.submit();
	}
	else if(type=="right"){//->移
		//alert(parent.location.href);
		o.action = "<%=request.getContextPath()%>/Svl_System?type=right";
		o.submit();
	}
	//doRefresh(); // 刷新后执行
	//setTimeout("doRefresh();",2100);
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
//	var b = document.form1.oldDeptCode.value;
	//父节点层次
	var level = document.form1.NodeLevel.value;
	//父节点类别
	var state = document.form1.State.value;
	if(id == "dis"){ //用来隐藏表格
		a.style.display = "none";
	}else{
		//节点id作为作为父节点id
		document.form1.ParID0.value = b;			
		document.form1.NodeLevel0.value = parseInt(level)+1;
		//类别
		//document.form1.State0.value = state;
		
		a.style.display = "";
	}

}


</script>
</HEAD>

<BODY <%=onLoadEvent%> leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" class="contentbg">
<form action="" method="Post" name="form1">

<input type="hidden" value="Dept" name="hModuleID"> <!--必须有-->
<table width="98%" border="0" cellspacing="0" cellpadding="0"
			bgcolor="FFFFFF" >
			<tr>


				<td width="1%" height="28"><img src="<%=request.getContextPath()%>/images/border_r1_c1.gif"
					width="16" height="28"></td>


				<td width="97%" class="tabletop">
				<p class="talbetitle"><%=MyTools.getProp(request,session.getAttribute("language"),"[DeptList_03]")%></p>
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


				<TABLE width="100%" cellpadding="0" cellspacing="1" class="tableborder" >
					<TBODY>
						<TR class="trstyle">
							<TD width="280"><%=MyTools.getProp(request,session.getAttribute("language"),"[DeptList_28]")%></TD>
							<TD colspan="1" width="328" align="left">
							 <INPUT type="text"	name="ParID" value="<%=sParId%>" readonly>
							 (<%=MyTools.getProp(request,session.getAttribute("language"),"[DeptList_06]")%>)
							</TD>
						</TR>
						<TR class="trstyle">
							<TD><%=MyTools.getProp(request,session.getAttribute("language"),"[DeptList_29]")%></TD>
							<TD align="left"><INPUT type="text" name="NodeID" maxlength="10" value="<%=sDeptCode%>" readonly >
								<INPUT type="hidden" name="OldNodeID"  value="<%=sDeptCode%>">
								(<%=MyTools.getProp(request,session.getAttribute("language"),"[DeptList_08]")%>)
							</TD>
						</TR>
						<TR class="trstyle">
							<TD><%=MyTools.getProp(request,session.getAttribute("language"),"[DeptList_30]")%></TD>
							<TD align="left"><INPUT type="text" name="NodeName" maxlength="20" value="<%=sCName%>">
							    <INPUT type="hidden" name="OldNodeName" value="<%=sCName%>"></TD>
						</TR>
						<TR class="trstyle">
							<TD><%=MyTools.getProp(request,session.getAttribute("language"),"[DeptList_10]")%>：</TD>
							<TD align="left"><%=MyTools.getProp(request,session.getAttribute("language"),"[DeptList_11]")%><INPUT type="radio" name="State" value="Y" <%=MyTools.WriteChecked("Y",sState)%> >
							    <%=MyTools.getProp(request,session.getAttribute("language"),"[DeptList_12]")%><INPUT type="radio" name="State" value="N" <%=MyTools.WriteChecked("N",sState)%> >
							    <INPUT type="hidden"    name="NodeState">
							</TD>
						</TR>
						<!--部门类型，可自定义，默认为空 2006-8-29 hades-->
						<TR class="trstyle">
							<TD><%=MyTools.getProp(request,session.getAttribute("language"),"[DeptList_35]")%>：</TD>
							<TD align="left">
							<SELECT name="DeptType">
								<OPTION value="0" <%=MyTools.WriteSelect( sDeptType,"0",1)%>><%=MyTools.getProp(request,session.getAttribute("language"),"[DeptList_350]")%></OPTION>
								<OPTION value="1" <%=MyTools.WriteSelect( sDeptType,"1")%>><%=MyTools.getProp(request,session.getAttribute("language"),"[DeptList_351]")%></OPTION>
								<OPTION value="2" <%=MyTools.WriteSelect( sDeptType,"2")%>><%=MyTools.getProp(request,session.getAttribute("language"),"[DeptList_352]")%></OPTION>
								<OPTION value="3" <%=MyTools.WriteSelect( sDeptType,"3")%>><%=MyTools.getProp(request,session.getAttribute("language"),"[DeptList_353]")%></OPTION>
							</SELECT>
							</TD>
						</TR>
						<!-- -->
						<TR class="trstyle">
							<TD><%=MyTools.getProp(request,session.getAttribute("language"),"[DeptList_34]")%>:</TD>
							<TD align="left" colspan="3"><TEXTAREA rows="5" cols="50"  name="NodeDesc"><%=sDeptDesc%></TEXTAREA></TD>
						</TR>
						<TR class="trstyle" style="display:none">
							<TD ><%=MyTools.getProp(request,session.getAttribute("language"),"[DeptList_13]")%></TD><!--自动处理-->
							<TD colspan="1"><INPUT type="text"
								name="NodeLevel" value="<%=sLevel%>"></TD>
						</TR>
						<TR class="trstyle" style="display:none">
							<TD ><%=MyTools.getProp(request,session.getAttribute("language"),"[DeptList_14]")%></TD><!--自动处理-->
							<TD colspan="1"><INPUT type="text"
								name="NodeOrder" value="<%=sOrder%>"></TD>
						</TR>
						<TR class="trstyle">
							<TD width="159"><%=MyTools.getProp(request,session.getAttribute("language"),"[DeptList_15]")%></TD>
							<TD align="center">
							<INPUT type="button" value="<%=MyTools.getProp(request,session.getAttribute("language"),"[DeptList_16]")%>" name="show" onclick="SwapDiv('NodeInfo',document.form1.ParID.value)">
							<INPUT type="button" value="<%=MyTools.getProp(request,session.getAttribute("language"),"[DeptList_17]")%>" name="mod"  onclick="doSubmit(this.name)">
							<INPUT type="button" value="<%=MyTools.getProp(request,session.getAttribute("language"),"[DeptList_18]")%>" name="del"  onclick="doSubmit(this.name)">
						<!-- 	<input type="button" value="< %=MyTools.getProp(request,session.getAttribute("language"),"[DeptList_19]")%>" name="up" onclick="doSubmit(this.name)">
							<input type="button" value="< %=MyTools.getProp(request,session.getAttribute("language"),"[DeptList_20]")%>" name="down" onclick="doSubmit(this.name)">
							 
							<input type="button" value="< %=MyTools.getProp(request,session.getAttribute("language"),"[DeptList_21]")%>" name="left" onclick="doSubmit(this.name)">
							<input type="button" value="< %=MyTools.getProp(request,session.getAttribute("language"),"[DeptList_22]")%>" name="right" onclick="doSubmit(this.name)"> -->

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
				<p class="talbetitle"><%=MyTools.getProp(request,session.getAttribute("language"),"[DeptList_31]")%></p>
				</td>


				<td width="2%"><img src="<%=request.getContextPath()%>/images/border_r1_c4.gif" width="16"
					height="28"></td>
			</tr>
			<tr>


				<td class="tableleft">&nbsp;</td>
				<td>

					<TABLE border="0" width="100%" cellpadding="1" cellspacing="1" class="tableborder">
						<TBODY>
							<TR class="trstyle">
								<TD width="50%"><%=MyTools.getProp(request,session.getAttribute("language"),"[DeptList_28]")%></TD>
								<TD colspan="3" align="left" width="591"><INPUT type="text" name="ParID0" readonly></TD>
							</TR>
							<TR class="trstyle">
								<TD><%=MyTools.getProp(request,session.getAttribute("language"),"[DeptList_33]")%><font color=red>*</font></TD>
								<TD colspan="3" align="left" width="591"><INPUT type="text" name="NodeID0" id="NodeID0"
									value="" maxlength="10"></TD>
							</TR>
							<TR class="trstyle">
								<TD><%=MyTools.getProp(request,session.getAttribute("language"),"[DeptList_30]")%></TD>
								<TD colspan="3" align="left" width="591"><INPUT type="text"
									name="NodeName0" maxlength="20"></TD>
							</TR>
							<TR class="trstyle">
								<TD><%=MyTools.getProp(request,session.getAttribute("language"),"[DeptList_34]")%>：</TD>
								<TD align="left" colspan="3"><TEXTAREA rows="5" cols="50"  name="NodeDesc0"></TEXTAREA></TD>
							</TR>
							<!--部门类型，可自定义，默认为空 2006-8-29 hades-->
							<TR class="trstyle">
								<TD><%=MyTools.getProp(request,session.getAttribute("language"),"[DeptList_35]")%>：</TD>
								<TD align="left">
								<SELECT name="DeptType0">
									<OPTION value="0" <%=MyTools.WriteSelect( sDeptType,"0",1)%>><%=MyTools.getProp(request,session.getAttribute("language"),"[DeptList_350]")%></OPTION>
									<OPTION value="1" <%=MyTools.WriteSelect( sDeptType,"1")%>><%=MyTools.getProp(request,session.getAttribute("language"),"[DeptList_351]")%></OPTION>
									<OPTION value="2" <%=MyTools.WriteSelect( sDeptType,"2")%>><%=MyTools.getProp(request,session.getAttribute("language"),"[DeptList_352]")%></OPTION>
									<OPTION value="3" <%=MyTools.WriteSelect( sDeptType,"3")%>><%=MyTools.getProp(request,session.getAttribute("language"),"[DeptList_353]")%></OPTION>
								</SELECT>
								</TD>
							</TR>
							<!-- -->
							<TR class="trstyle" style="display:none">
								<TD><%=MyTools.getProp(request,session.getAttribute("language"),"[DeptList_13]")%><font color="#999900">[<%=MyTools.getProp(request,session.getAttribute("language"),"[DeptList_25]")%>]</font></TD>
								<TD align="center" colspan="3" width="591"><INPUT type="text"
									name="NodeLevel0"></TD>
							</TR>
							<TR class="trstyle" style="display:none">
								<TD><%=MyTools.getProp(request,session.getAttribute("language"),"[DeptList_14]")%></TD>
								<TD align="center" colspan="3" width="591"><INPUT type="text"
									name="NodeOrder0" value="-1"><!--默认需要在后台处理-->
								</TD>
							</TR>
							<TR class="trstyle">
								<TD><%=MyTools.getProp(request,session.getAttribute("language"),"[DeptList_15]")%></TD>
								<TD align="center" colspan="3"><INPUT type="button"
									name="add" value="<%=MyTools.getProp(request,session.getAttribute("language"),"[DeptList_26]")%>" onclick="doSubmit(this.name)">
									<INPUT type="button" name="dis"
									value="<%=MyTools.getProp(request,session.getAttribute("language"),"[DeptList_27]")%>" onclick="SwapDiv(this.name)"></TD>
							</TR>
						</TBODY>
					</TABLE>
				</td>
				<td class="tableright">&nbsp;</td>
			</tr>		
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
