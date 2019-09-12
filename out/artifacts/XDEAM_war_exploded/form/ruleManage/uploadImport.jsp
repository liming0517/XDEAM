<%
/**
* @Description 批量导入试题 
* @date 2015-04-09
* @author lupengfei 
* @version V1.0  
*/
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.pantech.base.common.tools.MyTools"%>
<%@page import="com.pantech.src.develop.store.user.*"%>
<%@page import="com.pantech.base.common.tools.PublicTools"%>
<%@page import="java.net.URLEncoder" %>
<% String weeks = request.getParameter("weeks"); %>
<html>
	<head>
		<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/icon.css">
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
	</head>
<form id="form1" name="form1" method="POST"
		action="<%=request.getContextPath()%>/Svl_Import" enctype="multipart/form-data">
	<body class="easyui-layout">

	<div id="importfile" title="" style="">
			<table width="266px" class="tablestyle">
				<tr>
					<td colspan=2 align="left">
					<a href="#"
						onclick="doToolbar(this.id)" id="upload" class="easyui-linkbutton" plain="true" 
						iconcls="icon-save">导入数据</a></td>
				</tr>
				<tr>
					<td colspan=2 ><input type="file" name="excel1" id="excel1" width="100%">
					</td>
				</tr>
				<tr>
					<td class="titlestyle">年级</td>
					<td>
						<select id="ic_XNXQ" name="ic_XNXQ" class="easyui-combobox" panelHeight="40" style="width:130px;"></select>
					</td>
				</tr>
				<tr>
					<td class="titlestyle">专业名称</td>
					<td>
						<select id="ic_ZYMC" name="ic_ZYMC" class="easyui-combobox" panelHeight="40" style="width:130px;"></select>
					</td>
				</tr>
				
				<tr>
					<td width="50%">请选择导入的学期：</td>
					<td width="50%"><input type="checkbox" id="choose0" name="choose0" style="margin-left:30px;" onclick="cs0();" /> 全部</td>
				</tr>
				<tr>
					<td width="50%"><input type="checkbox" id="choose1" name="choose1" style="margin-left:30px;" onclick="checkall();" /> 一</td>
					<td width="50%"><input type="checkbox" id="choose2" name="choose2" style="margin-left:30px;" onclick="checkall();" /> 二</td>
				</tr>
				<tr>
					<td width="50%"><input type="checkbox" id="choose3" name="choose3" style="margin-left:30px;" onclick="checkall();" /> 三</td>
					<td width="50%"><input type="checkbox" id="choose4" name="choose4" style="margin-left:30px;" onclick="checkall();" /> 四</td>
				</tr>
				<tr>
					<td width="50%"><input type="checkbox" id="choose5" name="choose5" style="margin-left:30px;" onclick="checkall();" /> 五</td>
					<td width="50%"><input type="checkbox" id="choose6" name="choose6" style="margin-left:30px;" onclick="checkall();" /> 六</td>
				</tr>
			</table>
	</div>

	<input type="hidden" id="ic_term" name="ic_term" />
	<input type="hidden" id="weeks" name="weeks" />
	<input type="hidden" id="active" name="active" />
	</body>
	</form>
	
<script language="JavaScript" type="text/JavaScript">
	var choose0=0;
	var term="";
	var weeks =  "<%=weeks%>";

	//页面初始化
	$(document).ready(function(){
		//cs0();
		xnxqIMCombobox();
		zymcIMCombobox();
	});
	
	//工具栏操作
	function doToolbar(iToolbar){
	
		if(iToolbar == "upload"){
			var excel1 = $('#excel1').val();
			var excelsuffix=excel1.substring(excel1.lastIndexOf("."),excel1.length);

			if(excel1==""){
				//alertMsg("请添加文件!");
				parent.show1();		
				return;
			}
			if(excelsuffix!=".xls" && excelsuffix!=".xlsx"){  
           		parent.show2();		
           		return;
        	} 
        	if(term==""){
        		parent.show3();		
           		return;
        	}
        	if($('#ic_ZYMC').combobox('getValue')==""){
        		parent.show4();		
           		return;
        	}
			$("#active").val("uploadImport");
			$("#weeks").val(weeks);
			parent.$('#showDialog').dialog("close");
			parent.$('#maskFont2').html('培养计划导入中，请稍候...');
			parent.$('#divPageMask4').show();
			doForm();
			
		}
	}
	
	//form提交
	function doForm(){
		$('#form1').submit();	
	}
	
	$('#form1').form({
		//定位到servlet位置的url
		url:'<%=request.getContextPath()%>/Svl_Import',
		//当点击事件后触发的事件
	    onSubmit: function(data){ 
	    }, 
	    //当点击事件并成功提交后触发的事件
	    success:function(data){
	     	var json = eval("("+data+")");
	    
   		    if(json[0].MSG=="导入完成!"){
   		    	parent.doToolbar("que");
   		    	parent.$('#divPageMask4').hide();
   		    }else{
   		   		parent.$('#divPageMask4').hide();
   		    }	
   		    
   		    //if(json[0].SKTERM==""){
   		    	if(json[0].MSG2==""){
   		    		parent.showMSG(json[0].MSG);
   		    	}else{
   		    		parent.showMSG(json[0].MSG+"<br />"+json[0].MSG2+"课程不存在，如需导入请先在课程信息中添加。");
   		    	}
	     	//}else{
	     		//parent.showMSG(json[0].MSG+"<br />"+json[0].SKTERM);
	     	//}
		}
	});
	
	//选择学期
	function cs0(){
		if(choose0==1){
			$('#choose0').attr("checked",false);
			$('#choose1').attr("checked",false);
			$('#choose2').attr("checked",false);
			$('#choose3').attr("checked",false);
			$('#choose4').attr("checked",false);
			$('#choose5').attr("checked",false);
			$('#choose6').attr("checked",false);
			choose0=0;
			term="";
			$("#ic_term").val(term);
		}else{
			$('#choose0').attr("checked","checked");
			$('#choose1').attr("checked","checked");
			$('#choose2').attr("checked","checked");
			$('#choose3').attr("checked","checked");
			$('#choose4').attr("checked","checked");
			$('#choose5').attr("checked","checked");
			$('#choose6').attr("checked","checked");
			choose0=1;
			term="0,1,2,3,4,5";
			$("#ic_term").val(term);
		}
	}
	
	function checkall(){
		var checkbox="";
		term="";
		for(var i=0;i<6;i++){
			checkbox= document.getElementById("choose"+(i+1));
			if(checkbox.checked){//勾选
				term+=i+",";
			}
		}
		term=term.substring(0, term.length-1);
		if(term=="0,1,2,3,4,5"){
			$('#choose0').attr("checked","checked");
		}else{
			$('#choose0').attr("checked",false);
		}
		$("#ic_term").val(term);
	}
	
	//学年学期
	function xnxqIMCombobox(){
			$('#ic_XNXQ').combobox({
				url:"<%=request.getContextPath()%>/Svl_Skjh?active=xnxqIMCombobox",
				valueField:'comboValue',
				textField:'comboName',
				editable:false,
				panelHeight:'100', //combobox高度
				onLoadSuccess:function(data){
					$('#ic_XNXQ').combobox('setValue',data[0].comboValue);
				},
				//下拉列表值改变事件
				onChange:function(data){ 
					
				}
			});
	}	
		
	//专业
	function zymcIMCombobox(){
			$('#ic_ZYMC').combobox({
				url:"<%=request.getContextPath()%>/Svl_Skjh?active=zymcIMCombobox",
				valueField:'comboValue',
				textField:'comboName',
				editable:false,
				panelHeight:'100', //combobox高度
				onLoadSuccess:function(data){
					$('#ic_ZYMC').combobox('setValue',data[0].comboValue);
				},
				//下拉列表值改变事件
				onChange:function(data){ 
					
				}
			});
	}	
	
</script>
</html>
