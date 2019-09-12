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
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.src.develop.store.user.*"%>
<%
UserProfile up = new UserProfile(request,MyTools.getSessionUserCode(request)); 
String userName =up.getUserName();
String userCode =up.getUserCode();
%>
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
		<div data-options="region:'north'" title="" style = "height:103px;width:100%;">
			<table  class = "tablestyle" width = "100%">
				<tr>
					<td class="titlestyle">学年学期</td>
					<td>
						<select id="XNXQ" name="XNXQ" class="easyui-combobox" style="width:150px;"></select>
					</td>
					<!-- <td rowspan="2">
						<a href="#" onclick="doToolbar(this.id);" id="que"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-search'">查询</a>
					</td> -->
				</tr>
				<tr>
					<td class="titlestyle">教学性质</td>
					<td>
						<select id="JXXZ" name="JXXZ" class="easyui-combobox" style="width:150px;" panelHeight="auto"></select>
					</td>
				</tr>
				<tr>
					<td class="titlestyle">考试周期</td>
					<td>
						<select id="QZQM" name="QZQM" class="easyui-combobox" style="width:150px;" panelHeight="auto"></select>
					</td>
				</tr>
				<tr>
					<td class="titlestyle">考试日期</td>
					<td>
						<select id="KSRQ" name="KSRQ" class="easyui-datebox" style="width:150px;" panelHeight="auto" ></select>
					</td>
				</tr>
			</table>			
		</div>
		<div data-options="region:'center'" id='assistance'>
			<table class='tablestyle' id='List' width = "100%"></table>
		</div>
	</body>
	<script type="text/javascript">
		var iUSERCODE="<%=MyTools.getSessionUserCode(request)%>";
		var classId = "";//班级号
		var className = "";//班级名称
		var parentId = "";//父节点
		var xnxq_cx = '';//查询条件
		var jxxz_cx = '';
		var xnxq = "";//学年学期下拉框数据
		var jxxz = "";//教学性质下拉框数据
		var qzqm = "";//考试周期下拉框数据
		var xnxqVal = "";//当前学年学期数据
		var jxxzVal = "";//当前教学性质数据
		var qzqmVal = "";//当前期中期末选择
		var pageNum = 1;   //datagrid初始当前页数
		var pageSize = 20; //datagrid初始页内行数
		var weeks="";//总周次
		var paike="0";//排课状态
		
		$(document).ready(function(){
			initData();//页面初始化加载数据
		});
		
		//页面初始化加载数据
		function initData(){
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_examSet',
				data : 'active=initData&page=' + pageNum + '&rows=' + pageSize+'&iUSERCODE='+iUSERCODE,
				dataType:"json",
				success : function(data) {
					xnxq = data[0].xnxqData;//获取学年学期下拉框数据
					jxxz = data[0].jxxzData;//获取教学性质下拉框数据
					qzqm = data[0].qzqmData;//获取教学性质下拉框数据
					initCombobox(xnxq,jxxz,qzqm);//初始化下拉框
					treegrid(data[0].listData);
					loadData("","");
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
						$('#zongbiao').linkbutton('enable');
					}else{
						$('#import').linkbutton('disable');
						$('#zongbiao').linkbutton('disable');
					}
				}
			});
		}
		
		$('#KSRQ').datebox({
			onSelect: function(date){
				saveKSRQ();
			}
		});
		
		//读取考试日期
		function loadKSRQ(){ 
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_examSet',
				data : 'active=loadKSRQ&GG_XNXQBM=' + (xnxqVal+jxxzVal)+'&QZQM='+qzqmVal,
				async : false,
				dataType:"json",
				success : function(data) {
					$('#KSRQ').datebox('setValue',data[0].MSG);
				}
			});
		}
		
		//保存考试日期
		function saveKSRQ(){ 
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_examSet',
				data : 'active=saveKSRQ&GG_XNXQBM=' + (xnxqVal+jxxzVal)+'&QZQM='+qzqmVal+'&ksrq='+$('#KSRQ').datebox('getValue'),
				async : false,
				dataType:"json",
				success : function(data) {
					
				}
			});
		}
		
		function treegrid(listData){
			$('#List').treegrid({
				data:listData,
				width:'100%',
				loadMsg:'数据加载中，请稍后...',
				rownumbers: true,
				animate:true,
				fit:true,
				fitColumns: true,//设置边距
				idField:'id',
				treeField:'名称',
				columns:[[
	                {field:'名称',title:'<b>班级</b>',width:fillsize(0.35)}
				]],
				onBeforeLoad:function(row,param){     //分层显示treegrid		
						if(row){
							$('#List').treegrid('options').url='<%=request.getContextPath()%>/Svl_examSet?active=queryExamSetTree&parentId='+row.id+'&iUSERCODE='+iUSERCODE;
						}else{
							$('#List').treegrid('options').url='<%=request.getContextPath()%>/Svl_examSet?active=queryExamSetTree&&parentId=""'+'&iUSERCODE='+iUSERCODE;
						}
				},
				onClickRow:function(row){
					classId=row.id;
					parentId=row._parentId;
					className=row.名称;
					xnxqVal=$('#XNXQ').combobox('getValue');
					jxxzVal=$('#JXXZ').combobox('getValue');
					qzqmVal=$('#QZQM').combobox('getValue');
					window.parent.document.frames["right"].initGridData(classId,xnxqVal,jxxzVal,qzqmVal,"1");//点击后刷新right页面取值结果
				},
				onLoadSuccess:function(data){
					xnxqVal=$('#XNXQ').combobox('getValue');
					jxxzVal=$('#JXXZ').combobox('getValue');
					window.parent.document.frames["right"].initGridData("",xnxqVal,jxxzVal,qzqmVal,"1");//点击后刷新right页面取值结果
			    }
			});
		}
		
		//查询后台数据
		function loadData(xnxq_cx,jxxz_cx){
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_examSet',
				data : 'active=query&page=' + pageNum + '&rows=' + pageSize + '&iUSERCODE='+iUSERCODE+
					'&JXXZ=' + jxxz_cx + '&XNXQ=' + xnxq_cx + '&termid='+(xnxq_cx+jxxz_cx),
				dataType:"json",
				success : function(data) {
					//treegrid(data[0].listData);//加载特殊规则列表
					weeks=data[0].MSG;
				}
			});
		}
		
		//加载下拉框数据
		function initCombobox(xnxq,jxxz,qzqm){
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
					loadKSRQ();
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
			
			$('#QZQM').combobox({
				data:qzqm,
				valueField:'comboValue',
				textField:'comboName',
				editable:false,
				panelHeight:'140', //combobox高度
				onLoadSuccess:function(data){
					//判断data参数是否为空
					if(data != ''){
						//初始化combobox时赋值
						$(this).combobox('setValue', data[0].comboValue);
						qzqmVal=data[0].comboValue;
						
					}
				},
				//下拉列表值改变事件
				onChange:function(data){
					checkpaike();
					doToolbar("que");
					loadKSRQ();
				}
			});
		}
		
		//工具按钮
		function doToolbar(id){
			//查询
			if(id == 'que'){
				xnxq_cx = $('#XNXQ').combobox('getValue');
				jxxz_cx = $('#JXXZ').combobox('getValue');
				loadData(xnxq_cx,jxxz_cx);
// 				if(!classId==""){
// 					$('#List').treegrid("unselectRow", classId);
// 				}
				xnxqVal=$('#XNXQ').combobox('getValue');
				jxxzVal=$('#JXXZ').combobox('getValue');
				qzqmVal=$('#QZQM').combobox('getValue');
				window.parent.document.frames["right"].initGridData(classId,xnxqVal,jxxzVal,qzqmVal,"1");//点击后刷新right页面取值结果
			}
			if(id == 'import'){
				window.parent.document.frames["right"].doToolbar("import");
			}
			if(id == 'zongbiao'){
				window.parent.document.frames["right"].doToolbar("zongbiao");
			}
		}
	</script>
</html>