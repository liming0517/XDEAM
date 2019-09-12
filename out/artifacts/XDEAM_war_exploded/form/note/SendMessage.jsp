<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>短信发送</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/icon.css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
</head>
<body class="easyui-layout">
	<div region = "north" title="消息发送" align = "left" style = "background:#fafafa;height:58px;width:600px;">
		<a href = "#" onClick = "doToolbar(this.id);" id  = "save" class = "easyui-linkbutton" plain = "true" iconCls = "icon-save">发送</a>
	</div>
	<div region="center" style="background: #fafafa; width: 80%; height: 95%">
		<form id="form1" name="form1" method="POST">
				<center>
					<table class="tablestyle" style="width:100%;height:100%" cellpadding="4">
						<tr>
							<td align="left" width="50px">收件人：</td>
							<td align="left">
								<select
									class="easyui-combogrid" name="name" id="name" style="width:400px">
								</select>
							</td>
						</tr>
						<tr>
							<td align="left" width="50px">内容：</td>
							<td align="left"><textarea id="iccontent" name="iccontent" rows="8" style="width:400px"></textarea></td>
						</tr>
					</table>
				</center>
				<input type="hidden" id="active" name="active"/>
				<input type="hidden" id="icphone" name="icphone"/>
		</form>
	</div>
</body>

<script type="text/javascript">
	$(document).ready(function(){	
  		//loadGrid();
  	});
  	
  	$('#form1').form({
			//定位到servlet位置的url
			url: '<%=request.getContextPath()%>/Svl_MessageList',
			//当点击事件后触发的事件
		    onSubmit: function(data){
		    	//alert($('#modeid').combobox('getValue'));
		    }, 
		    //当点击事件成功提交后触发的事件
		    success:function(data){
		    	//alert("success");
		    	var datas = eval("("+data+")");
		    	//alert(modeid);
		    	ShowMsg("消息发送成功");
		    	$('#form1').form("clear");
		    	//$("#messageedit").window("close");
		    },
		    onLoadError:function(){
		    	//alert("error");
		    }
	});
		
	$('#name').combogrid({
		url: '<%=request.getContextPath()%>/Svl_Rules',
		idField:'员工编号',
        textField:'姓名',
		queryParams:{"active":"query"},
		nowrap: false,
		fit:true,
		showFooter:true,
		striped:true, //隔行变色
		pageSize:10,
		singleSelect:true,
		pageNumber:1,
		rownumbers:true,
		fitColumns: true,
		mode:'remote',
		multiple: true,	
		columns:[[
			{field:'ck',checkbox:true},
			{field:'员工编号',title:'员工编号',width:100,hidden:true},
			{field:'姓名',title:'姓名',width:100}
		]]
	});
 
  	function showPerson(){
  		$("#showperson").window("open");
  	}
  	
	function OpenUserList(){
		var val = showModalDialog('<%=request.getContextPath()%>/form/system/DeptTree.jsp?returnText=1&selectMode=1&includeUser=1','','');
		if(val == "" || val == undefined){
			document.getElementById("icphone").value = '';
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
		document.getElementById("icphone").value = iCode;
	}
	
	function doToolbar(iToolbar) {
		if(iToolbar=="save"){	
				$("#active").val("add");
				$('#icphone').val($('#name').combobox('getValues'));
				$('#form1').submit();
		}
	}
</script>
</html>