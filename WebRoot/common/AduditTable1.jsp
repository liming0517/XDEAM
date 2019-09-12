<%@ page pageEncoding="UTF-8"%>
<%
		String mouldID = pc.GetModuleCodeFromUrl(request);
		if("".equals(mouldID)){
			mouldID=PageofficeToolbar.getMouid(request);
		}
		AuditConductBean1 auditbean = new AuditConductBean1(request);
		auditbean.setMouldID(mouldID);
		auditbean.setOprateID(oprateID);
		auditbean.Auditresult();
%>
<div id="adudittables" name="adudittables" title="审核详细情况" class="easyui-window"  style="width:890px" closed=true resizable=false>
	<table width="100%" class="tablestyle"> 
		<%if(auditbean.getAuditjilu()){
			for(int i=0;i<auditbean.getSHYJarry().size();i++){
		%>
				<tr>
					<td width="30%"><br/><font size='4'><%=auditbean.getAuditnamearry().get(i)%>:</font>&nbsp;
					<%if (!"".equals(auditbean.getCodearry().get(i))) { %>
						&nbsp;
						<%if(PublicTools.fileexists(request.getRealPath("/")+"\\images\\Names\\" + auditbean.getCodearry().get(i) + "Name.jpg")){ %>
							<img src="<%=request.getContextPath() %>/images/Names/<%=auditbean.getCodearry().get(i) %>Name.jpg">
						<%}else{ %>
							<font size='4' color='black'><b><%=auditbean.getNamearry().get(i) %></b></font>
						<%} %>
					<%} %>
					<br/><font size='4'>批准日期:</font>&nbsp;&nbsp;<%=auditbean.getAuditdatearry().get(i)%>
					</td>
					<td width="70%" id="Audit_Idea<%=i%>">
						<font size='4'>意见:</font>&nbsp;&nbsp;
						<%if (!"0".equals(auditbean.getSHCROSSarry().get(i))) {%>
							<%if(PublicTools.fileexists(request.getRealPath("/")+"\\images\\Names\\" + auditbean.getCodearry().get(i) + "Y.jpg")){ %>
								<img src="<%=request.getContextPath() %>/images/Names/<%=auditbean.getCodearry().get(i) %>Y.jpg">
							<%}else{ %>
								<font size='4' color='black'><b>同意</b></font>
							<%} %>
						<%} else{%>
							<%if(PublicTools.fileexists(request.getRealPath("/")+"\\images\\Names\\" + auditbean.getCodearry().get(i) + "N.jpg")){ %>
								<img src="<%=request.getContextPath() %>/images/Names/<%=auditbean.getCodearry().get(i) %>N.jpg">
							<%}else{ %>
								<font size='4' color='black'><b>不同意</b></font>
							<%} %>
						<%} %>
						&nbsp;<%=auditbean.getSHYJarry().get(i)%>
					</td>
				</tr>
			<%}%>
				<tr>
					<td colspan="2" align="center" ><%=auditbean.getMESSAGE() %></td>
				</tr>
		<%}else{ %>
			<tr>
				<td align="center">尚未进行审核</td>
			</tr>
		<%} %>
	</table>
</div>


