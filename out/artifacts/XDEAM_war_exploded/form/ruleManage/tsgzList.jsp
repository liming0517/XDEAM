<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%
	/**
		创建人：wangzh
		Create date: 2015.06.03
		功能说明：用于设置特殊规则
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
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/publicScript.js"></script>
	</head>
	<body class="easyui-layout" >
		<div data-options="region:'north'" title="特殊规则信息" style="background:#fafafa;height:112px;">
			<table>
				<tr>
					<td>
						<a id="edit" onclick="doToolbar(this.id)" class="easyui-linkbutton" href="javascript:void(0);" data-options="plain:true,iconCls:'icon-edit'">编辑</a>
					</td>
					<td>
						<a id="del" onclick="doToolbar(this.id)"   class="easyui-linkbutton" href="javascript:void(0);" data-options="plain:true,iconCls:'icon-cancel'">删除</a>
					</td>
					<td>
						<a id="history" onclick="doToolbar(this.id)"   class="easyui-linkbutton" href="javascript:void(0);" data-options="plain:true,iconCls:'icon-site'">历史记录</a>
					</td>
					<td>
						<span style="font-size:14px;color:red;padding-left:10px;">注：每天次数、每周次数、每天节次、每周节次设置与自动排课功能有关，最大执教课程数与授课计划的设置有关。</span>
					</td>
				</tr>
			</table>
			<!--查询条件-->
	        <table width='100%' class='tablestyle'>
				<tr>
	            	<td class="titlestyle">学年学期</td>
	                <td>
	                	<input style="width:153px" class='easyui-combobox' name='GT_XNXQBM' id="GT_XNXQBM"/>
	                </td>
	                <td class="titlestyle">教学性质</td>
					<td>
						<input style="width:153px" class='easyui-combobox' name='GT_JXXZ' id="GT_JXXZ"/>
					</td>
	               
<!-- 	                <td class="titlestyle">角色</td> -->
<!-- 					<td> -->
<!-- 						<select name="GT_JS" id="GT_JS" class="easyui-combobox" panelHeight="auto" editable="false" style="width:153px;"  required="true"> -->
<!-- 								<option value="">请选择</option> -->
<!-- 								<option value="C01">高层</option> -->
<!-- 								<option value="C1002">中层</option> -->
<!-- 								<option value="C1003">行政在编教师</option> -->
<!-- 								<option value="C1004">人事代理</option> -->
<!-- 								<option value="C1005">系部任课教师</option> -->
<!-- 								<option value="C1006">外聘任课教师</option> -->
<!-- 						</select> -->
<!-- 					</td> -->
	                <td rowspan=2>
						<a id="query" name="query" onClick="doToolbar(this.id)" class="easyui-linkbutton" href="javascript:void(0);" data-options="plain:true,iconCls:'icon-search'">查询</a>
					</td>
				</tr>
				<tr>
					 <td class="titlestyle">教师编号</td>
	                <td>
	                	<input style="width:153px" class="easyui-textbox" id="GT_JSBH" name="GT_JSBH"/>
	                </td>
	                 <td class="titlestyle">教师姓名</td>
	                <td>
	                	<input style="width:153px" class="easyui-textbox" id="GT_JSXM" name="GT_JSXM"/>
	                </td>
				</tr>
			</table>
		</div>
		<div data-options="region:'center'">
			<table id='list'width="100%" ></table>
		</div>
		<div id="win" title="编辑">
			<form id='fm' method='post' style="margin: 0px">
				<table class = "tablestyle" width="100%">
					<tr>
						<a href="#" class="easyui-linkbutton" id="save" name="save" iconCls="icon-save" plain="true" onClick="doToolbar(this.id)">保存</a>
					</tr>
					<tr>
						<td class="titlestyle">教师姓名</td>
		                <td id="GT_JSXM1" class="titlestyle">
		                </td>
		           	</tr>
		           	<tr>
						<td class="titlestyle">每天次数</td>
						<td>
							<input name="GT_MTCS" id="GT_MTCS" class="easyui-textbox" style="width:250px;"/>
						</td>
					</tr>
					<tr>
						<td class="titlestyle">每周次数</td>
						<td>
							<input name="GT_MZCS" id="GT_MZCS" class="easyui-textbox" style="width:250px;"/>
						</td>
					</tr>
					<tr>
						<td class="titlestyle">每天节次</td>
						<td>
							<input name="GT_MTJC" id="GT_MTJC" class="easyui-textbox" style="width:250px;"/>
						</td>
					</tr>
					<tr>
						<td class="titlestyle">每周节次</td>
						<td>
							<input name="GT_MZJC" id="GT_MZJC" class="easyui-textbox" style="width:250px;"/>
						</td>
					</tr>
					<tr>
						<td class="titlestyle">最大执教课程数</td>
						<td>
							<input name="GT_ZDZJKCS" id="GT_ZDZJKCS" class="easyui-textbox" style="width:250px;"/>
						</td>
					</tr>
				</table>
				<!-- 隐藏属性,传参用 -->
				<input type="hidden" id="active" name="active" />
				<input type="hidden" id="GT_XNXQBM1" name="GT_XNXQBM1" />
				<input type="hidden" id="GT_JSBH1" name="GT_JSBH1" />
			</form>
		</div>
		<div id='historydlg' style="display:none; overflow:hidden;">
			<iframe id="historyiframe" name="historyiframe" src='' style="width:100%;height:100%;" frameborder="0" scrolling="no">
			</iframe>
		</div>
	</body>
	<script type="text/javascript">
		var pageNum = 1;   //datagrid初始当前页数
		var pageSize = 20; //datagrid初始页内行数
		var xnxq = "";//学年学期下拉框数据
		var jxxz = "";//教学性质下拉框数据
		var jsbh = "";//教师编号下拉框数据
		var gh = "";//工号
		var xnxqbm = "";//学年学期编码
		var row = '';//行数据
		var gharr=[];//工号数组
		
		var GT_XNXQBM = '';//查询条件
		var GT_JXXZ = '';
		var GT_JSBH = '';
		var GT_JSXM = '';
		var GT_JS = '';
		
		
		$(document).ready(function(){
			loadDialog();//初始化对话框
			historyLoad();//初始化历史记录
			initData();//页面初始化加载数据
		});
		
		//页面初始化加载数据
		function initData(){
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_Tsgz',
				data : 'active=initData&page=' + pageNum + '&rows=' + pageSize,
				dataType:"json",
				success : function(data) {
					xnxq = data[0].xnxqData;//获取学年学期下拉框数据
					jxxz = data[0].jxxzData;//获取教学性质下拉框数据
					//jsbh = data[0].jsbhData;//获取教师编号下拉框数据
					loadGrid(data[0].listData);//加载特殊规则列表
					initCombobox(xnxq,jxxz);//初始化下拉框
				}
			});
		}
		
		//加载特殊规则列表
		function loadGrid(listData){
			$('#list').datagrid({
				data:listData,
				loadMsg : "信息加载中请稍后!",//载入时信息
				width : '100%',//宽度
				rownumbers: true,
				animate:true,
				striped : true,//隔行变色
				pageSize : pageSize,//每页记录数
				singleSelect : false,//单选模式
				pageNumber : pageNum,//当前页码
				pagination:true,
				fit:true,
				fitColumns: true,//设置边距
				columns:[[
					{field:'1',checkbox:true},
					{field:'工号',title:'教师编号',width:120},
					{field:'GT_JSXM',title:'教师姓名',width:120},
					{field:'GT_MTCS',title:'每天次数',width:fillsize(0.1)},
					{field:'GT_MZCS',title:'每周次数',width:fillsize(0.1)},
					{field:'GT_MTJC',title:'每天节次',width:fillsize(0.1)},
					{field:'GT_MZJC',title:'每周节次',width:fillsize(0.1)},
					{field:'GT_ZDZJKCS',title:'最大执教课程数',width:fillsize(0.1)}
				]],
				
				onClickRow:function(rowIndex, rowData){
					//gh=rowData.工号;//获取工号
					//xnxqbm=rowData.学年学期编码;//获取学年学期编码
					row = rowData;//获取行数据
					//$("#GT_JSXM").html(rowData.GT_JSXM);//获取教师姓名
				},
				onLoadSuccess: function(data){
					
				},
				onLoadError:function(none){
					
				}
			});
			
			$("#list").datagrid("getPager").pagination({ 
				total:listData.total, 
				onSelectPage:function (pageNo, pageSize_1) { 
					pageNum = pageNo;
					pageSize = pageSize_1;
					loadData();
				} 
			});
		}
		
		
		
		//查询后台数据
		function loadData(){
		$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_Tsgz',
				data : 'active=query&page=' + pageNum + '&rows=' + pageSize +
					'&GT_XNXQBM=' + GT_XNXQBM + '&GT_JXXZ=' + GT_JXXZ + 
					'&GT_JSBH=' + GT_JSBH + '&GT_JSXM=' + GT_JSXM ,
				dataType:"json",
				success : function(data) {
					loadGrid(data[0].listData);//加载特殊规则列表
				}
			});
		}
	
		//加载下拉框数据
		function initCombobox(xnxq,jxxz,jsbh){
			$('#GT_XNXQBM').combobox({
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
					}
				},
				//下拉列表值改变事件
				onChange:function(data){}
			});
			
			$('#GT_JXXZ').combobox({
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
					}
				},
				//下拉列表值改变事件
				onChange:function(data){}
			});
			
			loadData();
		}
	
		//工具按钮
		function doToolbar(id){
			var getRows=$('#list').datagrid('getSelections');//获得所有选择行
			
			//查询
			if(id == 'query'){
				GT_XNXQBM = $('#GT_XNXQBM').combobox('getValue');
				GT_JXXZ = $('#GT_JXXZ').combobox('getValue'); 
				GT_JSBH = $('#GT_JSBH').textbox('getValue');
				GT_JSXM = $('#GT_JSXM').textbox('getValue');
				//GT_JS = $('#GT_JS').combobox('getValue');
				loadData();
			}
			//编辑
			if(id == "edit"){
				if(getRows.length!=1){
					alertMsg("请选择一行数据,且只能选择一行数据");
					return;
				}else{
					$('#win').dialog({   
						title: '编辑特殊规则信息'
					});
					$('#win').dialog("open");
					if(getRows.length==1){
						$('#fm').form('load', getRows[0]);
						$("#GT_JSXM1").html(getRows[0].GT_JSXM);
					}
				}
			}
			//删除
			if(id == "del"){
				if(getRows.length==0){
					alertMsg("请选择一行数据");
					return;
				}else{
	        		ConfirmMsg("删除后，可在历史记录中恢复，是否继续？","DelRec();","");
	        	}
			}
			//保存
			if(id == "save"){
				xnxqbm = getRows[0].学年学期编码;
				gh = getRows[0].工号;
				
				$("#active").val(id);
				$("#GT_XNXQBM1").val(xnxqbm);
				$("#GT_JSBH1").val(gh);
				$('#fm').submit();
			}
			//历史记录
			if(id == "history"){
				$('#historydlg').dialog("open");
				$("#historyiframe").attr("src","historyList.jsp");
				$('#historydlg').dialog('maximize');  //DIALOG最大化
			}
		}
		
		//fm提交事件
		$('#fm').form({
			url:'<%=request.getContextPath()%>/Svl_Tsgz',
			onSubmit:function(){
			
			},
			//提交成功
			success:function(datas){
				if($('#active').val()=="save"){
					var json = eval("("+datas+")");
					showMsg(json[0].msg);
					$('#win').dialog("close");
					loadData();
				}
			}
		});
		
		//删除方法
		function DelRec(){
			var getRows=$('#list').datagrid('getSelections');//获得所有选择行
			for(var i=0;i<getRows.length;i++){//循环遍历主键数组
 				gharr.push(getRows[i].工号);
 			}
 			xnxqbm = getRows[0].学年学期编码;
			$.ajax({
				type:'post',
				url:"<%=request.getContextPath()%>/Svl_Tsgz",
				data:"active=del&GT_JSBH="+gharr+"&GT_XNXQBM="+xnxqbm,
				dataType:'json',
				success:function(datas){
					var data=datas[0];
					showMsg(data.msg);
					loadData();
					gharr = [];	//重新初始化
				}
			});
		}
		
		//特殊规则编辑窗口
		function loadDialog() {
			$('#win').dialog({
				width : 350,
				height: 240,
				modal : true,
				closed : true,
				onOpen : function(data) {
					$('#win').find('table').css('display', 'block');
				},
				onLoad : function(data) {
				},
				onClose : function(data) {
					emptyDialog();//初始化信息
				}
			});
		}
		
		//初始化信息
		function emptyDialog(){
			$('#GT_MTCS').textbox('setValue', '');
			$('#GT_MZCS').textbox('setValue', '');
			$('#GT_MTJC').textbox('setValue', '');
			$('#GT_MZJC').textbox('setValue', '');
			$('#GT_ZDZJKCS').textbox('setValue', '');
		}
	
		//加载特殊规则历史记录窗口
		function historyLoad(){
			document.getElementById("historydlg").style.display = "";//显示
			historyDialog();
		}
		
		//特殊规则历史记录窗口
		function historyDialog(){
			$('#historydlg').dialog({
				title: '历史记录',   
				width: 764,	//宽度设置 		
				modal:true,
				height:380,
				closed: true,   
				cache: false, 
				draggable:false,//是否可移动dialog框设置
				onOpen : function(data) {
				},
				//读取事件
				onLoad:function(data){
				},
				//关闭事件
				onClose:function(data){
					
				}
			}); 
		}
	</script>
</html>