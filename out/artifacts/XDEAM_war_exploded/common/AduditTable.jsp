<%@ page pageEncoding="UTF-8"%>
<table width="100%" class="tablestyle"> 
	<%for(int i=0;i<auditbean.getMaxStep();i++){%>
		<% if(!auditbean.getVecOprateStep().get(i).equals(AuditConductBean.AUDIT_SKIP_STEP)){ %>
		<tr>
			<td width="9%" class="titlestyle2"><%=auditbean.getUserAuth(i+1)%></td>
			<td width="5%" class="titlestyle">意见</td>
			<td width="24%" id="Audit_Idea<%=i+1%>">
			&nbsp;<%=auditbean.getUserIdea(i+1)%>
			<%if (!"".equals(auditbean.getUserCode(i + 1))) { %>
				&nbsp;&nbsp;&nbsp;
				<img src="<%=request.getContextPath() %>/images/Names/<%=auditbean.getUserCode(i + 1) %>Name.jpg">
			<%} %>
			</td>
			<td width="5%" class="titlestyle">时间</td>
			<td width="24%" id="Audit_Time<%=i+1%>">&nbsp;<%=auditbean.getUserTime(i+1)%></td>
			<td width="5%" class="titlestyle">状态</td>
			<td width="28%" id="Audit_State<%=i+1%>">&nbsp;<%=auditbean.getUserState(i+1)%></td>
		</tr>
		<% } %>
	<%}%>
</table>

