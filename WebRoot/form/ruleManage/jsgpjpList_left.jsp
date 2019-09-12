<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%
	/**
		创建人：wangzh
		Create date: 2015.06.05
		功能说明：用于设置班级固排禁排
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
		<div data-options="region:'north'" title="" style = "height:83px;width:100%;">
			<table  class = "tablestyle" width = "100%">
				<tr>
					<td>
						<a href="#" onclick="doToolbar(this.id);" id="import"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-site'">选修课导入</a>
					</td>
				</tr>
			</table>
			<table  class = "tablestyle" width = "100%">
				<tr>
					<td class="titlestyle">学年学期</td>
					<td>
						<select id="XNXQ" name="XNXQ" class="easyui-combobox" style="width:150px;"></select>
					</td>
<!-- 					<td> -->
<!-- 						<a href="#" onclick="doToolbar(this.id);" id="que"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-search'">查询</a> -->
<!-- 					</td> -->
					<td rowspan=2>
						<a href="#" onclick="doToolbar(this.id);" id="queterm"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-search'">详细查询</a>
					</td>
				</tr>
				<tr>
					<td class="titlestyle">教学性质</td>
					<td>
						<select id="JXXZ" name="JXXZ" class="easyui-combobox" style="width:150px;"></select>
					</td>
					
				</tr>
			</table>			
		</div>
		<div data-options="region:'center'" id='assistance'>
			<table class='tablestyle' id='List' width = "100%"></table>
		</div>
		
		<div id="search" title="查询条件" style="">
			<table width="286px" class="tablestyle">
				<tr>
					<a href="#" class="easyui-linkbutton" id="submit" name="submit" iconCls="icon-submit" plain="true" onClick="doToolbar(this.id)">确定</a>
				</tr>
				<tr>
					<td width="50%" class="titlestyle">教师工号</td>
					<td  width="50%">
						<input style='width:96%' class='easyui-validate' id='ic_teaId' name='ic_teaId'/>
					</td>
				</tr>
				<tr>
					<td width="50%" class="titlestyle">教师姓名</td>
					<td  width="50%">
						<input style='width:96%' class='easyui-validate' id='ic_teaName' name='ic_teaName'/>
					</td>													
				</tr>
			</table>
		</div>
	</body>
	<script type="text/javascript">
		var classId = "";//班级号
		var className = "";//班级名称
		var parentId = "";//父节点
		var xnxq_cx = '';//查询条件
		var jxxz_cx = '';
		var xnxq = "";//学年学期下拉框数据
		var jxxz = "";//教学性质下拉框数据
		var xnxqVal = "";//当前学年学期数据
		var jxxzVal = "";//当前教学性质数据
		var pageNum = 1;   //datagrid初始当前页数
		var pageSize = 20; //datagrid初始页内行数
		var weeks="";//总周次
		var teaId="";//教师工号
		var teaName="";//教师姓名
		var paike="0";//排课状态
		
		$(document).ready(function(){
			$('#search').hide();
			//去掉分页栏文字
			if ($.fn.pagination){ 
				$.fn.pagination.defaults.beforePageText = ''; 
				$.fn.pagination.defaults.afterPageText = ''; 
				$.fn.pagination.defaults.displayMsg = ''; 
			} 
			initData();//页面初始化加载数据
		});
		
		//页面初始化加载数据
		function initData(){
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_Jsgpjp',
				data : 'active=initData',
				dataType:"json",
				success : function(data) {
					xnxq = data[0].xnxqData;//获取学年学期下拉框数据
					jxxz = data[0].jxxzData;//获取教学性质下拉框数据
					initCombobox(xnxq,jxxz);//初始化下拉框
					datagrid();
				}
			});
		}
		
		//检查当前时间是否超过允许排课时间
		function checkpaike(){ 
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_Skjh',
				data : 'active=checkpaike&GS_XNXQBM=' + $('#XNXQ').combobox('getValue')+$('#JXXZ').combobox('getValue'),
				async : false,
				dataType:"json",
				success : function(data) {
					paike=data[0].MSG;
					if(paike==0){
						$('#import').linkbutton('enable');
					}else{
						$('#import').linkbutton('disable');
					}
				}
			});
		}
		
		function datagrid(){
			$('#List').datagrid({
				url : '<%=request.getContextPath()%>/Svl_Jsgpjp',
				queryParams : {"active":"queryTree","teaId":$('#ic_teaId').val(),"teaName":$('#ic_teaName').val()},
				width:'100%',
				loadMsg:'数据加载中，请稍后...',
				rownumbers: true,
				animate:true,
				striped : true,//隔行变色
				pageSize : pageSize,//每页记录数
				singleSelect : true,//单选模式
				pageNumber : pageNum,//当前页码
				pagination:true,
				fit:true,
				fitColumns: true,//设置边距
				columns:[[
	                {field:'工号',title:'<b>工号</b>',width:fillsize(0.20)},
	                {field:'姓名',title:'<b>姓名</b>',width:fillsize(0.35)}
				]],
				onClickRow:function(rowIndex, rowData){
					classId=rowData.工号;//实际是教师编号
					className=rowData.姓名;//实际是教师姓名
					xnxqVal=$('#XNXQ').combobox('getValue');
					jxxzVal=$('#JXXZ').combobox('getValue');
					window.parent.document.frames["right"].initGridData(classId,className,xnxqVal,jxxzVal,"1");//点击后刷新right页面取值结果
				},
				onLoadSuccess:function(data){ 
					
			    }
			});
		}
		
		//查询后台数据
		function loadData(xnxq_cx,jxxz_cx){
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_Jsgpjp',
				data : 'active=query&JXXZ=' + jxxz_cx + '&XNXQ=' + xnxq_cx + '&termid='+(xnxq_cx+jxxz_cx),
				dataType:"json",
				success : function(data) {
					weeks=data[0].MSG;
				}
			});
		}
		
		//加载下拉框数据
		function initCombobox(xnxq,jxxz){
			$('#XNXQ').combobox({
				data:xnxq,
				valueField:'comboValue',
				textField:'comboName',
				editable:false,
				panelHeight:'140', //combobox高度
				onLoadSuccess:function(data){
					//判断data参数是否为空
					if(data != ''){
						//初始化combobox时赋值
						$(this).combobox('setValue', data[0].comboValue);
						xnxqVal=data[0].comboValue;
					}
				},
				//下拉列表值改变事件
				onChange:function(data){
					checkpaike();
					doToolbar("que");
				}
			});
			
			$('#JXXZ').combobox({
				data:jxxz,
				valueField:'comboValue',
				textField:'comboName',
				editable:false,
				panelHeight:'140', //combobox高度
				onLoadSuccess:function(data){
					//判断data参数是否为空
					if(data != ''){
						//初始化combobox时赋值
						$(this).combobox('setValue', data[0].comboValue);
						jxxzVal=data[0].comboValue;
					}
				},
				//下拉列表值改变事件
				onChange:function(data){}
			});
		}
		
		//工具按钮
		function doToolbar(id){
			//查询
			if(id == 'que'){
				xnxq_cx = $('#XNXQ').combobox('getValue');
				jxxz_cx = $('#JXXZ').combobox('getValue');
				loadData(xnxq_cx,jxxz_cx);
				datagrid();
			}
			if(id == 'import'){
				window.parent.document.frames["right"].doToolbar("import");
			}
			if(id == 'queterm'){
				openSearch();
				loadData(xnxq_cx,jxxz_cx);
			}
			if(id == 'submit'){
				datagrid();
				$('#search').dialog("close");
			}
		}
		
		
		//打开teacher编辑窗口
		function openSearch(){
			$('#search').show();
			$('#search').dialog({   
				title: '查询条件',   
				width: 300,//宽度设置   
				height: 114,//高度设置 
				modal:true,
				closed: true,   
				cache: false, 
				draggable:true,//是否可移动dialog框设置
				//读取事件
				onLoad:function(data){
					
				},
				//关闭事件
				onClose:function(data){
					
				}
			});
			$('#search').dialog("open");
		}
		
	</script>
</html>