<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.base.common.tools.PublicTools"%>
<%@ page import="com.pantech.src.develop.Logs.*"%>
<%@ page import="com.pantech.base.common.tools.*"%>
<%@ page import="com.pantech.src.develop.store.user.*"%>
<%@ page import="com.pantech.src.develop.manage.workremind.WorkRemind"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.*"%>
<%@ page import="com.pantech.base.common.db.DBSource"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> 
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/icon.css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
<title>审核流程定义</title>
</head>

	<body class="easyui-layout">
		
		<div region="north" align="left" title="审核流程配置"
			style="overflow: hidden; height: 50px; width: 800px;">
			<form id="form1" name="form1" method="POST">
			<a href="#"
				 onclick="doToolbar(this.id);"
				id="add" name="add" class="easyui-linkbutton" plain="true"
				iconCls="icon-add">添加审核环节</a>	
			<a href="#"
				 onclick="doToolbar(this.id);"
				id="initial" name="initial" class="easyui-linkbutton" plain="true"
				iconCls="icon-edit">模块配置修改</a>
			<a href="#"
				 onclick="doToolbar(this.id);"
				id="edit" name="edit" class="easyui-linkbutton" plain="true"
				iconCls="icon-edit">审核配置修改</a>
			<a href="#"
				 onclick="doToolbar(this.id);"
				id="cancel" name="cancel" class="easyui-linkbutton" plain="true"
				iconCls="icon-cancel">删除</a>
				</form>
		</div>
		<div region="center" style="background: #fafafa; width: 100%;height:100%">
				<div style="height: 100%;">
					<table id="audit" name="audit"></table>
					<input type="hidden" id="active" name="active" ></input>
					<input type="hidden" id="deleteauditid" name="deleteauditid" ></input>
				</div>
				
		</div>
		



<div id="aduditedit" name="aduditedit" title="审核配置" class="easyui-window"  style="width:600px" resizable=false closed=true modal=true>
<form id="form2" name="form2" method="POST">
	<center>
	<table class="tablestyle" style="width:100%" cellpadding="4">
	<tr>
	<td>显示名称</td>
	<td>
		<input id="showname" name="showname"></input>
	</td>
	<td>环节号</td>
	<td>
		<input id="linknum" name="linknum"></input>
	</td>
	</tr>
	<tr>
	<td>人员类型</td>
	<td>
	<select
		class="easyui-combobox" name="audittype" id="audittype" style="width: 200px;">
		<option value="ry">人员</option>
		<option value="js">职位</option>
		<option value="zd">字段</option>
	</select>
	</td>
		<td>具体名称</td>
	<td>
	<select
		class="easyui-combobox" name="auditname" id="auditname" style="width: 200px;">
	</select>
	</td>
	</tr>
	
	<tr>
	<td>审核类型</td>
	<td>
	<select
		class="easyui-combobox" name="type" id="type" style="width: 200px;">
		<option value="sh">审核</option>
		<option value="sy">审阅</option>
		<option value="sp">审批</option>
	</select>
	</td>
	<td columns=2></td>
	</tr>
	
	</table>
	</center>
	<input type="hidden" id="IDname" name="IDname" ></input>
	<input type="hidden" id="coactive" name="coactive" ></input>
	<input type="hidden" id="coedtion" name="coedtion" ></input>
	<input type="hidden" id="comodeid" name="comodeid" ></input>
	<input type="hidden" id="auditid" name="auditid" ></input>
	</form>
</div>
<div style="width:500px" title="模块配置" class="easyui-window" id="start" name="start" resizable=false closed=true modal=true>
	<form id="form3" name="form3" method="POST">
		<center>
		<table class="tablestyle" style="width:100%">
		<tr>
		<td>
			审核类型:
		</td>
		<td>
			<select
				class="easyui-combobox" name="way" id="way" style="width: 150px;">
				<option value="001">普通</option>
				<option value="002">流转</option>
			</select>
		</td>
		<td>
			审核模块:
		</td>
		<td>
		<select class="easyui-combobox"
				name="modeid" id="modeid" style="width: 150px;">
		</select> 
		</td>
	</tr>
	<tr>
		<td>
			多次审核:
		</td>
		<td>
			<select
				class="easyui-combobox" name="repeat" id="repeat" style="width: 150px;">
				<option value="Y">允许</option>
				<option value="N">不允许</option>
			</select>
		</td>
		<td>
			启用时间:
		</td>
		<td>
			<input type="text"  id="startdate"  name="startdate"  class="easyui-datebox"  style="width:150px">
		</td>
	</tr>
	<tr>
			<td>
				是否提醒:
			</td>
			<td>
				<select
					class="easyui-combobox" name="remind" id="remind" style="width: 150px;">
					<option value="Y">是</option>
					<option value="N">否</option>
				</select>
			</td>
			
			<td>
				允许跳步:
			</td>
			<td>
				<select
					class="easyui-combobox" name="leap" id="leap" style="width: 150px;">
					<option value="Y">是</option>
					<option value="N">否</option>
				</select>
			</td>
		</tr>
		<tr>
			<td>
				允许驳回:
			</td>
			<td>
				<select
					class="easyui-combobox" name="reject" id="reject" style="width: 150px;">
					<option value="Y">是</option>
					<option value="N">否</option>
				</select>
			</td>
			<td>
			</td>
		</tr>
		</table>
		</center>
		<input type="hidden" id="wayid" name="wayid" ></input>
		<input type="hidden" id="edition" name="edition" ></input>
		<input type="hidden" id="active2" name="active2" ></input>
	</form>
</div>
	</body>
<script type="text/javascript">
		var lastIndex=-1;
		var renyuanbianhao="";
		var renyuanname="";
		var curAuditID="";
		
		var modeneirong="";
		var qamodeneirong=<%=PublicTools.getComboboxOptions("V_模版配置表","模版编号","模板名称","where 状态='3'",request)%>;
		var qafiled=<%=PublicTools.getComboboxOptions("V_模版配置表","模版编号","模板名称","where 1=1",request)%>;
		var modeid="<%= MyTools.StrFiltr(request.getParameter("modeid"))%>";
		var edition="<%=MyTools.StrFiltr(request.getParameter("edition"))%>";
		var user=<%=PublicTools.getComboboxOptions("V_USER_AUTH","distinct usercode","username","where authdesc not in('工作人员','临时2')",request)%>;
		var role=<%=PublicTools.getComboboxOptions("V_USER_AUTH","distinct authcode","authdesc","where authdesc not in('工作人员','临时2')",request)%>;
		
		var wayid="<%=PublicTools.getStrings("审核方式","V_审核标签配置","where 模块编号='"+MyTools.StrFiltr(request.getParameter("modeid"))+"' and 版本号='"+MyTools.StrFiltr(request.getParameter("edition"))+"'",request)%>";
		
		var leap="<%=PublicTools.getStrings("是否跳步","V_审核标签配置","where 模块编号='"+MyTools.StrFiltr(request.getParameter("modeid"))+"' and 版本号='"+MyTools.StrFiltr(request.getParameter("edition"))+"'",request)%>";
		//alert(wayid);
		var repeatid="<%=PublicTools.getStrings("重复提交","V_审核标签配置","where 模块编号='"+MyTools.StrFiltr(request.getParameter("modeid"))+"' and 版本号='"+MyTools.StrFiltr(request.getParameter("edition"))+"'",request)%>";
		
		var startdateid="<%=PublicTools.getStrings("convert(nvarchar(10),开启时间,20)","V_审核标签配置","where 模块编号='"+MyTools.StrFiltr(request.getParameter("modeid"))+"' and 版本号='"+MyTools.StrFiltr(request.getParameter("edition"))+"'",request)%>";
		
		var remindid="<%=PublicTools.getStrings("审核提醒","V_审核标签配置","where 模块编号='"+MyTools.StrFiltr(request.getParameter("modeid"))+"' and 版本号='"+MyTools.StrFiltr(request.getParameter("edition"))+"'",request)%>";
		var reject="<%=PublicTools.getStrings("驳回","V_审核标签配置","where 模块编号='"+MyTools.StrFiltr(request.getParameter("modeid"))+"' and 版本号='"+MyTools.StrFiltr(request.getParameter("edition"))+"'",request)%>";
		
		var canshu="";
		//var rolename="";
		//var IDtype="";
		
		var rows = null;
		var fieldlist="";
		//var fields="";
		$(document).ready(function(){
			loadcombox();
			if(modeid!=""){
				//$('#way').combobox('select', wayid);
				//alert(modeid);
				$('#modeid').combobox('select', modeid.replace(/[.]/g,"_"));
				//return;
			}
			
			loadGrid();
			
		});
		
		$('#aduditedit').dialog({
				toolbar:[{
					text:'保存',
					iconCls:'icon-save',
					handler:function(){
						//alert('save')
						$('#IDname').val($('#auditname').combobox('getText'));
						//alert($("#IDname").val());
						$('#form2').submit();
					}
				}]
		});
		
		$('#way').combobox({
			onSelect:function(record){
				if($('#way').combobox('getValue')=="001"){
					$('#modeid').combobox({
							data:modeneirong,
							valueField:'comboValue',
							textField:'comboName',
							onSelect:function(record){
								//alert(record.comboValue);
								//alert(modeneirong);
								getfieldlist();
								
								//loadGrid();
							}
					});
				}else if($('#way').combobox('getValue')=="002"){
					$('#modeid').combobox({
							data:qamodeneirong,
							valueField:'comboValue',
							textField:'comboName',
							onSelect:function(record){
								getqafieldlist();
								//alert(record.comboValue);
								//getfieldlist();
								//alert();
								//loadGrid();
							}
					});
				}
			}
		});
		
		
		$('#audittype').combobox({
			onSelect:function(record){
				//alert();
				if($('#audittype').combobox('getValue')=="ry"){
					$('#auditname').combobox({
							data:user,
							valueField:'comboValue',
							textField:'comboName'
					});
				}
				else if($('#audittype').combobox('getValue')=="js"){
					$('#auditname').combobox({
							data:role,
							valueField:'comboValue',
							textField:'comboName'
					});
				}
				else if($('#audittype').combobox('getValue')=="zd"){
					$('#auditname').combobox({
							data:fieldlist,
							valueField:'Name',
							textField:'Name'
					});
				}
				else{
					$('#auditname').combobox('loadData','');
				}
			}
		});
		
		$("#linknum").numberbox({min:1,precision:0});
		
		$('#form1').form({
			//定位到servlet位置的url
			url: '<%=request.getContextPath()%>/Svl_Auditconfig',
			//当点击事件后触发的事件
		    onSubmit: function(data){
		    	//alert($('#modeid').combobox('getValue'));
		    }, 
		    //当点击事件成功提交后触发的事件
		    success:function(data){
		    	//alert("success");
		    	var datas = eval("("+data+")");
		    	modeid=$('#modeid').combobox("getValue");
		    	showMsg(datas[0].MSG);
		    	//alert(modeid);
		    	loadGrid();
		    },
		    onLoadError:function(){
		    	//alert("error");
		    }
		});
		
		$('#form2').form({
			url: '<%=request.getContextPath()%>/Svl_Auditedit',
			//当点击事件后触发的事件
		    onSubmit: function(data){
		    }, 
		    //当点击事件成功提交后触发的事件
		    success:function(data){
		   		var datas = eval("("+data+")");
		    	$("#aduditedit").window("close");
		    	edition=datas[0].EDTION;
		    	doToolbar("save");
		    	//alert($('#modeid').combobox('getValue'));
		    }
		});
		
		$('#form3').form({
			//定位到servlet位置的url
			url: '<%=request.getContextPath()%>/Svl_Auditconfig',
			//当点击事件后触发的事件
		    onSubmit: function(data){
		    	//alert($('#modeid').combobox('getValue'));
		    }, 
		    //当点击事件成功提交后触发的事件
		    success:function(data){
		    	//alert("success");
		    	var datas = eval("("+data+")");
		    	modeid=datas[0].MODEID;
		    	wayid=datas[0].WAYID;
		    	repeatid=datas[0].REPEATID;
		    	startdateid=datas[0].STARTDATEID;
		    	remindid=datas[0].REMINDID;
		    	leap=datas[0].LEAP;
		    	showMsg(datas[0].MSG);
		    	loadGrid();
		    },
		    onLoadError:function(){
		    	//alert("error");
		    }
		});
		
		$('#start').dialog({
			toolbar:[{
				text:'保存',
				iconCls:'icon-save',
				handler:function(){
					if($('#modeid').combobox("getValue")!=""){
						$("#start").window('close');
						doToolbar(canshu);
					}else{
						alertMsg("请选择审核模块");
					}
				}
			}]
		});
		
		function loadcombox(){
			loadmodeneirong();
			doToolbarAuth();
		}
		
		function loadGrid(){
			$('#audit').datagrid({
				title:'审核列表',
				iconCls:'icon-edit',
				singleSelect:true,
				idField:'itemid',
				width: '100%',
				nowrap: false,
				fit:true,
				showFooter:true,
				striped:true, //隔行变色
				pagination:true,
				pageSize:10,
				singleSelect:true,
				pageNumber:1,
				rownumbers:true,
				fitColumns: true,
				url: '<%=request.getContextPath()%>/Svl_Auditconfig',
				queryParams:{"active":"queaudit","modeid":modeid,"edition":edition},
				columns:[[
					{field:'审核编号',title:'审核编号',width:100,rowspan:2,hidden:true},
					{field:'编号',title:'编号',width:100,rowspan:2,hidden:true},//角色与人员共用

					{field:'审核名称',title:'显示名称',width:100,rowspan:2},
					{title:'审核人员设置',colspan:2,width:260},
					{field:'步骤',title:'步骤',width:100,rowspan:2},
					{field:'通过条件',title:'通过条件',width:60,rowspan:2,align:'center',
						formatter:function(value,row,index){
							var btn="";
							if(row.审核编号!=undefined&&row.审核编号!=""&&row.审核编号!=null){			
								btn="<input type='button' onclick='showtg(\""+row.审核编号+"\",\""+row.步骤+"\",\""+modeid+"\")' value='通过条件设置'>";
							}else{
								btn="<input type='button' onclick='doToolbar(\"sp\")' value='通过条件设置' disabled=true>";
							}
							return btn;
						}
					},
					{field:'spcondition',title:'前置条件',width:60,rowspan:2,align:'center',
						formatter:function(value,row,index){
							var btn="";
							//var rows=row.AuditID;
							//alert(row.AuditID);
							if(row.审核编号!=undefined&&row.审核编号!=""&&row.审核编号!=null){			
								btn="<input type='button' onclick='showsp(\""+row.审核编号+"\",\""+row.步骤+"\")' value='设置前置条件'>";
							}else{
								btn="<input type='button' onclick='doToolbar(\"sp\")' value='设置前置条件' disabled=true>";
							}
							return btn;
						}
					}
				],
				[
					{field:'编号类型',title:'审核角色类型',width:100,rowspan:2,formatter:function(value,row,index){
							if(value=="ry"){
								return "人员";
							}
							if(value=="js"){
								return "职位";
							}
							if(value=="zd"){
								return "字段";
							}
						}
					},//分辨该编号是角色还是人员
					{field:'审核角色',title:'审核角色',width:100}
				]],
				onClickRow:function(rowIndex,rowData){
					curAuditID=rowData.审核编号;
				},
				onLoadSuccess:function(){
				}
			});
			doToolbarAuth();
		}
		
		function getfieldlist(){
			$.ajax({
				type: "POST",
				url: '<%=request.getContextPath()%>/Svl_Auditconfig',   //设置 SVL
				async:false,
				data: "active=fieldlist&modeid=" + $('#modeid').combobox("getValue"),  // 设置模式，一般为view ，主关键字值		
				dataType: 'json',
				success: function(datas){
					fieldlist=datas;
				}
			});
		}
		
		
		function getqafieldlist(){
			$.ajax({
				type: "POST",
				url: '<%=request.getContextPath()%>/Svl_Auditconfig',   //设置 SVL
				async:false,
				data: "active=qafieldlist",  // 设置模式，一般为view ，主关键字值		
				dataType: 'json',
				success: function(datas){
					fieldlist=datas;
				}
			});
		}
		
		function doToolbar(id){
			//alert($('#modeid').combobox('getValue'));
			$('#modeid').combobox('setValue',$('#modeid').combobox('getValue'));
			
			if(id=="save"){
				$("#active2").val("saveaudit");
				$('#edition').val(edition);
				$("#wayid").val($('#way').combobox('getValue'));
				if($('#modeid').combobox('getValue')==""){
					alertMsg("请选择审核模块");
					return;
				}				
				$('#form3').submit();
			}
			if(id=="cancel"){
				if(curAuditID==""){
					alertMsg("请选择一条数据");
				}else{
					$('#audit').datagrid('endEdit', lastIndex);
					$("#active").val("deleteaudit");
					//alert(curAuditID);
					$("#deleteauditid").val(curAuditID);
					ConfirmMsg("你确认要删除该记录吗?","$('#form1').submit();","");
				}
			}
			if(id=="add"){
				if($('#modeid').combobox("getValue")!=""){
					$('#modeid').combobox('disable');
					$('#way').combobox('disable');
					$('#audittype').combobox('clear');
					$('#type').combobox('clear');
					$('#auditname').combobox('loadData','');
					$('#form2').form('clear');
					$('#coedtion').val(edition);
					$("#comodeid").val($('#modeid').combobox("getValue"));
					$("#coactive").val("add");
					$("#aduditedit").window('open');
				}
				else{
					canshu="add";
					$("#start").window('open');
					//alertMsg("请选择审核模块");
				}
			}
			if(id=="edit"){
				if(curAuditID==""){
					alertMsg("请选择一条数据");
				}else{
					rows = $('#audit').datagrid('getSelected');
					$("#auditid").val(rows.审核编号);
					$("#comodeid").val($('#modeid').combobox("getValue"));
					$("#coactive").val("edit");
					$('#coedtion').val(edition);
					$("#showname").val(rows.审核名称);
					$("#linknum").val(rows.步骤);
					//alert(rows.审核类型);
					$('#audittype').combobox('select', rows.编号类型);
					$('#auditname').combobox('setValue', rows.编号);
					$('#type').combobox('setValue', rows.审核类型);
					$("#aduditedit").window('open');
				}
			}
			
			if(id=="initial"){
				canshu="save";
				$("#start").window('open');
			}
			
		}
		
		function doToolbarAuth(){
			if(modeid!=""){
				$('#modeid').combobox('disable');
				$('#way').combobox('disable');
				//alert(wayid);
				$('#way').combobox('select', wayid);
				$('#repeat').combobox('setValue', repeatid);
				$('#remind').combobox('setValue', remindid);
				$('#reject').combobox('setValue', reject);
				$('#leap').combobox('setValue', leap);
				$('#startdate').datebox("setValue",startdateid);
				$("#initial").linkbutton('enable');
				$("#edit").linkbutton('enable');
				$("#save").linkbutton('enable');
				$("#cancel").linkbutton('enable');
			}else{
				$("#initial").linkbutton('disable');
				$("#edit").linkbutton('disable');
				$("#save").linkbutton('disable');
				$("#cancel").linkbutton('disable');
			}
		}
		
		function loadmodeneirong(){
			modeneirong=<%=PublicTools.getComboboxOptions("V_模块初始化定义","模块编号","模块名称","where 1=1",request)%>;		
			for(var i=0;i<modeneirong.length;i++){
				modeneirong[i].comboValue=modeneirong[i].comboValue.replace(/[.]/g,"_");//将"."转换为"_"方便combox编译
			}
		}
		
		
		function showsp(cs,bz){
			window.showModalDialog("Auditsp.jsp?auditid="+cs+"&modeid="+modeid+"&bz="+bz+"&edition="+edition,window);
		}
		
		function showtg(cs,bz,md){
			window.showModalDialog("Audittg.jsp?auditid="+cs+"&bz="+bz+"&modeid="+md+"&edition="+edition);
		}
	</script>
</html>