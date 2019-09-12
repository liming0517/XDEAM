<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%
	/**
		创建人：wangzh
		Create date: 2015.06.03
		功能说明：用于存放历史数据及还原功能
		页面类型:列表及模块入口
		修订信息(有多次时,可有多个)
		修订日期:
		原因:
		修订人:
		修订时间:
	**/
 %>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
	<body class="easyui-layout" >
		<div data-options="region:'north'" title="" style="background:#fafafa;height:34px;">
			<table>
				<tr>
					<td>
						<a id="reduction"  onclick="doToolbar(this.id)" class="easyui-linkbutton" href="javascript:void(0);" data-options="plain:true,iconCls:'icon-new'">还原</a>
					</td>
				</tr>
			</table>
		</div>
		<div data-options="region:'center'">
			<table id='list'width="100%" ></table>
		</div>
	</body>
	<script type="text/javascript">
		var gh = "";//工号
		var xnxqbm = "";//学年学期编码
	
		$(document).ready(function(){
			loadGrid();//加载历史记录列表
		});
		
		//加载历史记录列表
		function loadGrid(){
			$('#list').datagrid({
				url: '<%=request.getContextPath()%>/Svl_Tsgz',
				queryParams:{"active":"quehis"},
				loadMsg : "信息加载中请稍后!",//载入时信息
				width : '100%',//宽度
				rownumbers: true,
				animate:true,
				striped : true,//隔行变色
				pageSize : 20,//每页记录数
				singleSelect : true,//单选模式
				pageNumber : 1,//当前页码
				pagination:true,
				fit:true,
				fitColumns: true,//设置边距
				columns:[[
					{field:'姓名',title:'教师姓名',width:120},
					{field:'每天次数',title:'每天次数',width:fillsize(0.1)},
					{field:'每周次数',title:'每周次数',width:fillsize(0.1)},
					{field:'每天节次',title:'每天节次',width:fillsize(0.1)},
					{field:'每周节次',title:'每周节次',width:fillsize(0.1)},
					{field:'最大执教课程数',title:'最大执教课程数',width:fillsize(0.1)}
				]],
				
				onClickRow:function(rowIndex, rowData){
					gh=rowData.工号;//获取工号
					xnxqbm=rowData.学年学期编码;//获取学年学期编码
				},
				onLoadSuccess: function(data){
					
				},
				onLoadError:function(none){
					
				}
			});
		}
		
		//工具按钮
		function doToolbar(id){
			//还原
			if(id == "reduction"){
				if(gh==""){
					alertMsg("请选择一行数据");
					return;
				}else{
	        		ConfirmMsg("是否需要还原？","ReduRec();","");
	        	}
			}
		}
		
		//还原方法
		function ReduRec(){
		$.ajax({
				type:'post',
				url:"<%=request.getContextPath()%>/Svl_Tsgz",
				data:"active=redu&GT_JSBH="+gh+"&GT_XNXQBM="+xnxqbm,
				dataType:'json',
				success:function(datas){
					var data=datas[0];
					showMsg(data.msg);
					loadGrid();
					window.parent.loadData();//刷新父页面
					gh="";
				}
			});
		}
	</script>
</html>
		