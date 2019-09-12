<!-- 
编写人员：YH
页面作用：事务提醒-事务提醒显示(WorkRemind.jsp)
创建日期：2008-11-06
 -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!-- page 包含 -->
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.pantech.src.develop.common.Cls_CommonHtmlData"%>
<%@ page import="com.pantech.src.develop.store.user.*" %>
<%@ page import="com.pantech.base.common.tags.util.PagerInfo"%>
<%@ page import="com.pantech.base.common.tools.MyTools" %>
<%@ page import="com.pantech.src.develop.manage.workremind.WorkRemind" %>
<%@ taglib uri="/WEB-INF/taglib/util.tld" prefix="util"%>
<%@ taglib uri="/WEB-INF/taglib/securityTag.tld" prefix="security"%>

<!-- 预处理 -->
<%
	//页面通用HTML代码输出类定义
	Cls_CommonHtmlData cchd=new Cls_CommonHtmlData(request.getContextPath());
	String msg = "";
	if(request.getAttribute("MSG")!=null){
		msg = (String)request.getAttribute("MSG");
		request.removeAttribute("MSG");
	}
	//记录JSP地址
	String url = "";
	if(request.getAttribute("URL")!=null){
		url = (String)request.getAttribute("URL");
	}else{
		url = request.getRequestURL().toString();
	}
	
	//模式：详细view 本页面只有查看
	String type=MyTools.StrFiltr(request.getParameter("type"));
	String bzmode = MyTools.StrFiltr(request.getParameter("bzmode"));
	String sql = new String();
	PagerInfo pInfo=new PagerInfo(request);
		
	WorkRemind bean = new WorkRemind(request);
	ArrayList arr = new ArrayList();
	Vector vec=new Vector();
	//获取提醒列表
	try{
		arr=(ArrayList)request.getAttribute("WorkRemind");
		if(arr==null || arr.size()<=0){
			if(bzmode.equals("Read")){
				//arr = bean.getRemindByOld(MyTools.getSessionUserCode(request),"1");
			}else{
				//arr = bean.getRemind(MyTools.getSessionUserCode(request),"1");
			}
			if(arr!=null && arr.size()>1){
				vec = (Vector)arr.get(0);
				pInfo = (PagerInfo)arr.get(2);
			}
		}else{
			vec = (Vector)arr.get(0);
			pInfo = (PagerInfo)arr.get(2);
		}
	}catch(Exception ex){
		vec = null;
		msg = "事务获取异常";
	}
	
%>

<jsp:useBean class="com.pantech.base.common.tags.security.PurViewTools" id="pc" scope="page" />
<%String mcode = pc.GetModuleCodeFromUrl(request);%>
<!-- JS块 -->
<script language="JavaScript" src="<%=request.getContextPath()%>/script/VerifyForm.js"></script>
<script language="JavaScript" src="<%=request.getContextPath()%>/script/meizzDate.js"></script>
<script language="JavaScript" type="text/javascript" src="<%=request.getContextPath()%>/script/jquery-1.3.min.js"></script>
<script language="JavaScript" type="text/javascript" src="<%=request.getContextPath()%>/script/parseXML.js"></script>

<script language="JavaScript">
$(document).ready( function() {
	$(".zt").attr("align","center");

//alert(document.body.clientHeight - ($("#tableContainer").attr("offsetTop") + $("#testHeight").attr("offsetHeight") + 20 ));
	if(document.body.clientHeight - ($("#scrolltable").attr("offsetTop") + 20 ) < 200 ){
		$("#scrolltable").height(200);
	}else{
		$("#scrolltable").height( document.body.clientHeight - ($("#scrolltable").attr("offsetTop") + 20 ) );
	}
});

$(window).resize(function() {
	if(document.body.clientHeight - ($("#scrolltable").attr("offsetTop") +  20 ) < 200 ){
		$("#scrolltable").height(200);
	}else{
		$("#scrolltable").height( document.body.clientHeight - ($("#scrolltable").attr("offsetTop") + 20 ) );
	}
});

function dosubmit(){
	form1.active.value = "que";
	form1.submit();
}

//保存当前操作的DIV号，使用后每次归置为 -1
var icDiv = -1
function setRead(id,divid){
	icDiv = divid;
	$.ajax({
		type: "POST",
		url: "<%=request.getContextPath()%>/Svl_WorkRemind",	
		//"some.jsp",
		data: "active=setRead&id=" + id,		//"name=John&location=Boston",
		success: doReadOK,
		error: function(msg){ alert( "System Load Table request error by Svl_WorkRemind[?]active=setRead&id=" + id  ); }
	});
}

function setUnRead(id){
	$.ajax({
		type: "POST",
		url: "<%=request.getContextPath()%>/Svl_WorkRemind",	
		//"some.jsp",
		data: "active=setUnRead&id=" + id,		//"name=John&location=Boston",
		success: doUnReadOK,
		error: function(msg){ alert( "System Load Table request error by Svl_WorkRemind[?]active=setUnRead&id=" + id  ); }
	});
}

function doReadOK(msg){
	if(msg=="yes"){
		//$("#div" + icDiv).html("已阅");
		var str = "";
		
		str = $("#context" + icDiv).html().replace("<font color='red'>（已阅）</font>","");
 		
		
		$("#context" + icDiv).html(str + "<font color='red'>（已阅）</font>");
		icDiv = -1;
	}else{
		alert("操作失败");
	}
}
</script>


<BODY>
<center>
<link href="<%=request.getContextPath()%>/css/remind.css" type="text/css" rel="stylesheet"/>
<form action="<%=request.getContextPath()%>/Svl_RemindSet" method="Post" name="form1">

<INPUT type="hidden" name="ReqUrl" value="<%=url%>">
<INPUT type="hidden" name="active">
<input type="hidden" name="sqlstate" value="<%=sql%>">
<input type="hidden" name="bzmode" value="<%=bzmode%>">

<table width="800" height="133" background="<%=request.getContextPath()%>/images/9.png">
	<tr>
		<td  align="left" valign="bottom">
		    &nbsp;&nbsp;&nbsp;&nbsp;
		    <a href="#" onclick="location='<%=request.getContextPath()%>/form/manage/WorkRemind/WorkRemind.jsp?bzmode=UnRead'">
				<img border="0" src="<%=request.getContextPath()%>/images/Read.gif" width="44" height="38" /></a>
		    &nbsp;&nbsp;&nbsp;&nbsp;
		    <a href="#" onclick="location='<%=request.getContextPath()%>/form/manage/WorkRemind/WorkRemind.jsp?bzmode=Read'">
				<img border="0" src="<%=request.getContextPath()%>/images/UnRead.gif" width="44" height="38" /></a>
	    </td>	
	</tr>
</table>

		  <div id="scrolltable" class="scrolltable">
            <table width="780px" border="1" cellspacing="0" cellpadding="5" class="tablestyle">
              <thead class="fixedHeader">
              <th>事务建立日期</th>
              <th>事务类别</th>
              <th>事务描述</th>
              <th>事务等级</th>
			</thead>
              <tbody>
              <%
              	int maxCol = 6;
                if(vec!=null && vec.size()>0){
					String strUrl = "";
					for(int i=0;i<vec.size();i+=maxCol){
						strUrl = "";
						if( !MyTools.StrFiltr(vec.get(i+5)).equals("") ){
							strUrl = "window.parent.location.href='" + request.getContextPath() + MyTools.StrFiltr(vec.get(i+5)) + "'\"";
						}
						if(bzmode.equals("Read")){
							out.print("<tr class=\"colorchange\">");
						}else{
							out.print("<tr onclick=\"setRead(" + MyTools.StrFiltr(vec.get(i+0)) + "," + MyTools.StrFiltr(vec.get(i+0)) + ");" + strUrl + "\" class=\"colorchange\">");
						}
						out.print("	   <td class='height' width='150px' align='center'>" + MyTools.StrFiltr(vec.get(i+1)) + "</td>");
						out.print("    <td class='height' width='80px' align='center'>" + MyTools.subs(MyTools.StrFiltr(vec.get(i+2)),0,MyTools.StrFiltr(vec.get(i+2)).indexOf("_")) + "</td>");
						out.print("    <td class='height' style='text-align:left' id='context" + MyTools.StrFiltr(vec.get(i+0)) + "'>" + MyTools.StrFiltr(vec.get(i+3)) + "</td>");
						out.print("    <td class='height' class='import' width='80px'align='center'>" + MyTools.StrFiltr(vec.get(i+4)) + "</td></tr>");
						//后续操作方到TR的事件中
						//如果URL字段不为空，则需要跳转，否则，只需要显示确定按钮即可
						//if( !MyTools.StrFiltr(vec.get(i+5)).equals("") ){
						//	out.print("    <td class='height' align='center'><img src=\"" + request.getContextPath() + "/images/But/xx.gif\" " +
						//				" onclick=\"setRead(" + MyTools.StrFiltr(vec.get(i+0)) + ");location.href='" + request.getContextPath() + MyTools.StrFiltr(vec.get(i+5)) + "'\"></td></tr>");
						//	
						//}else{
						//	out.print("    <td class='height' align='center'><div id='div" + i + "'><img src=\"" + request.getContextPath() + "/images/But/YES.gif\" " + 
						//				" onclick=\"setRead('" + MyTools.StrFiltr(vec.get(i+0)) + "','" + i + "')\"></td></tr>");
						//}
						
					}
				}else{
					out.print("<tr><td class='height' colspan='4' align='center'>" + "暂无事务</td></tr>");
				}
				%>
              </tbody>
              <tfoot>
                <tr>
                  <td align="center" colspan="4"><util:pager url="<%=url%>" params="" currPage="<%=pInfo.getCurrPage()%>" pageCount="<%=pInfo.getPageCount()%>" pageSize="<%=pInfo.getPageSize()%>" rowCount="<%=pInfo.getRowCount()%>"/></td>
                </tr>
              </tfoot>
            </table>
        </div>
  <script language="javascript">   
    var rows = document.getElementsByTagName("tr");   
    for(var i=0; i<rows.length; i++){   
        rows[i].onmouseover = function(){   
            this.className += 'altrow';   
        }   
        rows[i].onmouseout = function(){   
            this.className = this.className.replace( /altrow/," ");   
        }   
    }   
</script> 

</form>
</center>
<%out.print(cchd.setHtmlBottom());//页面尾部分%>
