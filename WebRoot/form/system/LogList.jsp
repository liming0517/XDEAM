<%@ page 
language="java"
contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"
%>
<%
// 登陆日志查询
//2004.12.7
//CREATE BY HADES
//后台包 Com.pantech.base.common.system
%>

<%@ page import ="java.util.*"%>
<%@ page import ="com.pantech.base.common.tags.util.*"%>
<%@ page import ="com.pantech.base.common.system.*"%>
<%@ page import ="java.sql.ResultSet"%>

<%@ page import="com.pantech.base.common.tools.PageMgr"%>

<%@ page errorPage="/form/ErrHandler.jsp" %>
<%@ page import="java.util.HashMap"%> 
<%@ taglib uri="/WEB-INF/taglib/util.tld" prefix="util" %>
<%@ taglib uri="/WEB-INF/taglib/TextToPicture.tld" prefix="TToP" %>
<%@ taglib uri="/WEB-INF/taglib/securityTag.tld" prefix="security" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<jsp:useBean class="com.pantech.base.common.tags.security.PurViewTools" id="pc" scope="page" />
<%
String mcode = pc.GetModuleCodeFromUrl(request);
%>
<security:securityForPage pagePermissionID="<%=mcode%>">
<HTML>
<HEAD>
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<TITLE>::::登陆日志查询::::</TITLE>
<link href="<%=request.getContextPath()%>/css/wiserun.css" rel="stylesheet" type="text/css">
<script language="JavaScript"
	src="<%=request.getContextPath()%>/script/meizzDate.js"></script>
</HEAD>
<%
Vector v = new Vector();
Vector v_title = new Vector();
ArrayList arr = new ArrayList();
PagerInfo pInfo =new PagerInfo(request,1);

int filedcount=0;  //字段数

LoginListBean clsLogList = new LoginListBean();
//取得返回的结果集
arr = (ArrayList)request.getAttribute("LoginListBean");
if(arr != null){
	//返回的记录集合
	v = (Vector)arr.get(0);
	//返回字段标题
	v_title = (Vector)arr.get(1);
	filedcount=v_title.size();
	
	System.out.println("在本页==== "+v.size());
	//分页标签需要的PagerInfo对象
	pInfo = (PagerInfo)arr.get(2);
	//查询条件记录
	clsLogList = (LoginListBean)arr.get(3);
}
%>
<!-- 查询时需包含的文件 -->
<BODY>
<!-- ---------------------------js区域 ----------------------------------->
<script language="javascript"
	src="<%=request.getContextPath()%>/script/common/Verify.js"></script>
<script language="javascript"
	src="<%=request.getContextPath()%>/script/common/function.js"></script>
<script language="vbscript"
	src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
<script language="JavaScript">
function dosubmit()
{
	document.form1.Active.value="query";
	document.form1.submit();
}

function doDelete(incomNum){
	var isDel = window.confirm('这将永久删除全部信息，且不可恢复，确认继续吗？');
	//alert(incomNum);
	if(isDel){
		//alert('信息已删除');
		document.form1.action = "del";
		document.form1.submit();
		
	}else{
		//alert('未删除');
	}

}
</script>
<!-- ---------------------------------------------------------------------->



	<form name="form1" method="post" action="<%=request.getContextPath()%>/Svl_LoginList">
		<CENTER>
			<TABLE cellspacing="1" cellpadding="1" bgcolor="#8000ff" width="90%">
				<TBODY>
					<TR>
						<TD width="100%" align="center" class="ets_zone_top" bgcolor="#ffffff">登陆日志查询</TD>
					</TR>
					<TR>
						<TD height="22" width="888" bgcolor="#f1e7f0">·请在下面输入查询条件来查询您想要的记录</TD>
					</TR>
				</TBODY>
			</TABLE>
		</CENTER>
<!--查询条件区  -->
	<CENTER>
	<TABLE cellpadding="1" cellspacing="1" border="0" width="90%">
		<TBODY>
			<TR>
				<TD class="ets_normal_top" align="center">请输入查询条件</TD>
			</TR>
			<TR>
				<TD>
				<CENTER>
				<TABLE class="cssT" cellspacing="1" cellpadding="1" border="0" width="100%">
					<TBODY>
						<TR >
							<TD colspan="8" bgcolor="#f8ecfb">&nbsp;</TD>
						</TR>
						<TR >
							<TD width="12%" align="center" bgcolor="#f8ecfb">用户帐号</TD>
							<TD width="12%" bgcolor="#f8ecfb"><INPUT type="text"
								name="icUserAccount" value="<%=clsLogList.getUserAccount()%>"></TD>
							<TD align="center" bgcolor="#f8ecfb" width="12%">用户IP地址</TD>
							<TD bgcolor="#f8ecfb" colspan="5" ><INPUT type="text"
								name="icIpAddress" value="<%=clsLogList.getIpAddress()%>"></TD>
						
						</TR>

						<TR >
							<TD align="center" bgcolor="#f8ecfb" width="12%">登陆时间</TD>
							<TD valign="middle" bgcolor="#f8ecfb" align="left" width="12%">
								<INPUT type="text" name="icLoginDate_Start" onfocus="WdatePicker({skin:'default'})" class="Wdate" value="<%=clsLogList.getLoginDate_Start()%>" readonly></TD>
							<TD align="middle" bgcolor="#f8ecfb" width="12%">至</TD>
							<TD align="left" bgcolor="#f8ecfb" colspan="5"><INPUT type="text"
											value="<%=clsLogList.getLoginDate_End()%>" name="icLoginDate_End" onfocus="WdatePicker({skin:'default'})" class="Wdate"
											readonly></TD>
						</TR>

						

						<TR >
							<TD colspan="8" bgcolor="#f8ecfb">&nbsp;</TD>
						</TR>

						<TR >
							<TD colspan="8" align="center" bgcolor="#f8ecfb"><INPUT
								type="button" name="button" value="提交" onclick="dosubmit();"> <input type="hidden" name="ReqUrl" value="form/system/LogList.jsp"><INPUT
								type="button" value="返回" onclick="javascript:history.go(-1);"></TD>
						</TR>
						<TR >
							<TD colspan="8" bgcolor="#f8ecfb">&nbsp;</TD>
						</TR>
					</TBODY>
				</TABLE>
				</CENTER>
	<!--查询条件区  -->			
	<!--查询结果区  -->
				
					<table class=tablestyle cellspacing="1" cellpadding="0">
						<tr >
							<!--td class="ets_normal_top" align="center">查询结果</td-->
						</tr>
						<tr class="fieldbg">
							<td class="tablefield">
								<CENTER>
									<TABLE class=tablestyle cellspacing="1" cellpadding="0" width="100%">
										<TBODY>
											<TR class="fieldbg">
												<TD width="16%" align="center">用户帐号</TD>
												<TD width="16%" align="center">IP地址</TD>
												<TD width="16%" align="center">时间</TD>
												<TD width="16%" align="center">类型</TD>
												<TD width="16%" align="center">操作系统</TD>
												<TD width="16%" align="center">浏览器类型</TD>
												
											</TR>
											<%if(v.size() == 0){%>
											<TR class="tableline">
											<TD colspan=6>没有记录．．．．</TD>
											</TR>
											<%}else{
											//循环显示本页记录
											for (int i = 0; i < v.size(); i +=filedcount) {
											%>
											<TR class="tableline">
											   <% 
											     for (int j = 0; j < filedcount; j ++) {
											   %>  
												<TD  align="center" bgcolor="#ffffff"><%=(String) v.get(i+j)%></TD>
											   <%
											     }
											   %>
											</TR>
											<%
												}
											}%>
			
											<TR >
												<TD width="16%" align="center"  colspan="6">
												<%String para =  "Active=" + clsLogList.getActive() ;%> <!--分页处理-->
												<util:pager url="<%=request.getRequestURL().toString()%>" params="<%=para%>" currPage="<%=pInfo.getCurrPage()%>" pageCount="<%=pInfo.getPageCount()%>" pageSize="<%=pInfo.getPageSize()%>" rowCount="<%=pInfo.getRowCount()%>"/>
												</TD>
											</TR>
										</TBODY>
									</TABLE>
								</CENTER>
							<!-- 隐藏文本域 --> <INPUT type="hidden"
								name="Active" value="">
							</td>
						</tr>
					</table>
				</TD>
			</TR>
		</TBODY>
	</TABLE>
	</CENTER>
		<!--查询结果区  -->
				<BR>
				<BR>
				<BR>
				<BR>
</form>
		
</BODY>

</HTML>
</security:securityForPage>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/script/My97DatePicker/WdatePicker.js"></script>