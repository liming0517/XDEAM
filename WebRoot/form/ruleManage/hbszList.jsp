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
	</head>
	<style>
	
	</style>
	<body class="easyui-layout" >
		<div data-options="region:'north'" title="合班设置" style="background:#fafafa;height:61px;">
			<table>
				<tr>
					<td>
						<a id="add" onclick="doToolbar(this.id)" class="easyui-linkbutton" href="javascript:void(0);" data-options="plain:true,iconCls:'icon-add'">添加合班记录</a>
					</td>
					<td>
						<a id="del" onclick="doToolbar(this.id)"   class="easyui-linkbutton" href="javascript:void(0);" data-options="plain:true,iconCls:'icon-cancel'">删除合班记录</a>
					</td>
				</tr>
			</table>
		</div>
		<div data-options="region:'center'">
			<table id='list'width="100%" ></table>
		</div>
		<div id="win" title="请选择要合班的专业、班级、课程">
			<form id='fm' method='post' style="margin: 0px">
				<table >
					<tr>
						<td>
							<a href="#" class="easyui-linkbutton" id="ok" name="ok" iconCls="icon-arrow_right" plain="true" onClick="doToolbar(this.id)">下一步</a>
						</td>
					</tr>
				</table>
				<table class = "tablestyle" width="100%">
					<tr>
						<td class="titlestyle" style="width:30%;height:51px">学年学期</td>
						<td>
							<select name="GH_XNXQBM" id="GH_XNXQBM" class="easyui-combobox" panelHeight="auto" editable="false" style="width:500px;"  required="true">
							</select>
						</td>
					</tr>
					<tr>
						<td class="titlestyle" style="height:50px">教学性质</td>
						<td>
							<select name="GH_JXXZ" id="GH_JXXZ" class="easyui-combobox" panelHeight="auto" editable="false" style="width:500px;"  required="true">
							</select>
						</td>
					</tr>
					<tr>
						<td class="titlestyle" style="height:50px">专业名称</td>
		                <td>
		                	<select name="GH_ZYDM" id="GH_ZYDM" class="easyui-combobox" panelHeight="auto" editable="false" style="width:500px;"  required="true">
							</select>
		                </td>
		           	</tr>
		           	<tr>
						<td class="titlestyle" style="height:50px">班级名称</td>
						<td>
							<select name="GH_XZBDM" id="GH_XZBDM" class="easyui-combobox" panelHeight="auto" editable="false" style="width:500px;"  required="true">
							</select>
						</td>
					</tr>
					<tr>
						<td class="titlestyle" style="height:51px">学科名称</td>
		                <td>
		                	<select name="GH_KCDM" id="GH_KCDM" class="easyui-combobox" panelHeight="auto" editable="false" style="width:500px;"  required="true">
							</select>
		                </td>
		           	</tr>
<!-- 					<table id='shoukejihua' style="width: 100%;"></table> -->
				</table>
				<!-- 隐藏属性,传参用 -->
				<input type="hidden" id="active" name="active" />
			</form>
		</div>
		
		<div id="skjh" title="请选择要合班的授课计划">
			<table>
				<tr>
					<td>
						<a href="#" class="easyui-linkbutton" id="back1" name="back1" iconCls="icon-arrow_left" plain="true" onClick="doToolbar(this.id)">上一步</a>
						<a href="#" class="easyui-linkbutton" id="next" name="next" iconCls="icon-arrow_right" plain="true" onClick="doToolbar(this.id)">下一步</a>
					</td>
				</tr>
			</table>	
			<table id='shoukejihua' style="width: 100%;"></table>
		</div>
		
		<div id="skjh2" title="合班">
			<table>
				<tr>
					<td>
						<a href="#" class="easyui-linkbutton" id="back2" name="back2" iconCls="icon-arrow_left" plain="true" onClick="doToolbar(this.id)">上一步</a>
						<a href="#" class="easyui-linkbutton" id="save" name="save" iconCls="icon-save" plain="true" onClick="doToolbar(this.id)">保存合班</a>
						<span style="font-size:13px;color:red;padding-left:10px;">注：请选择一条授课计划为主，会将其任课教师、节数、连节、连次、场地要求、授课周次复制给其它授课计划，并保存</span>
<!-- 						<a href="#" class="easyui-linkbutton" id="same" name="same" iconCls="icon-submit" plain="true" onClick="doToolbar(this.id)">确定</a> -->
					</td>
				</tr>
			</table>	
			<table id='shoukejihua2' style="width: 100%;"></table>
		</div>
		
	</body>
	<script type="text/javascript">
		var pageNum = 1;   //datagrid初始当前页数
		var pageSize = 20; //datagrid初始页内行数
		var ikeycode = "";//合班列表数据的主键
		var ikeycodeforshouke = "";//所选择的主授课计划的主键
		var bianhao = [];//其他授课计划的主键,由于可能是多个班级的授课计划,所以用数组
		var xnxq = "";
		var jxxz = "";
		var zymc = "";
		var xkmc = "";
		var bjmc = "";
		var savexqbh="";//保存传递过来的学期编码
		var paike="0";
		var skjhback1=0;
		var skjhback2=0;
		
		$(document).ready(function(){
			//进入时,合班不能直接进行保存,所以在初始化时,将其变灰
			//$("#save").linkbutton("disable");
			//$("#ok").linkbutton("disable");
			loadDialog();//初始化对话框
			initData();//页面初始化加载数据
			checkpaike();
		});
		
		//检查当前时间是否超过允许排课时间
		function checkpaike(xnxq){ 
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_Skjh',
				data : 'active=checkpaike&GS_XNXQBM=' + xnxq,
				async : false,
				dataType:"json",
				success : function(data) {
					paike=data[0].MSG;				
				}
			});
		}
		
		//页面初始化加载数据
		function initData(){
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_Hbsz',
				data : 'active=initData&page=' + pageNum + '&rows=' + pageSize,
				dataType:"json",
				success : function(data) {
					xnxq = data[0].xnxqData;//获取学年学期下拉框数据
					jxxz = data[0].jxxzData;//获取教学性质下拉框数据
					zymc = data[0].zymcData;//获取专业名称下拉框数据
					//bjmc = data[0].bjmcData;//获取班级名称下拉框数据
					
					loadGrid(data[0].listData);//加载特殊规则列表
					initCombobox(xnxq,jxxz,zymc);//初始化下拉框
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
				singleSelect : true,//单选模式
				pageNumber : pageNum,//当前页码
				pagination:true,
				fit:true,
				fitColumns: true,//设置边距
				columns:[[
					{field:'学年学期编码',title:'学年学期编码',width:fillsize(0.2)},
					{field:'学科名称',title:'学科名称',width:fillsize(0.4)},
					{field:'合班班级',title:'合班班级',width:fillsize(0.4)}
				]],
				
				onClickRow:function(rowIndex, rowData){
					ikeycode=rowData.编号;
					savexqbh=rowData.学年学期编码;
				},
				onLoadSuccess: function(data){
					
				},
				onLoadError:function(none){
					
				}
			});
			
			$("#list").datagrid("getPager").pagination({
				total:listData.total,
				afterPageText: '页&nbsp;<a href="#" onclick="goEnterPage();">Go</a>&nbsp;&nbsp;&nbsp;共 {pages} 页',
				onSelectPage:function (pageNo, pageSize_1) { 
					pageNum = pageNo;
					pageSize = pageSize_1;
					loadData();
				} 
			});
		}
		
		function goEnterPage(){
			var e = jQuery.Event("keydown");//模拟一个键盘事件 
			e.keyCode = 13;//keyCode=13是回车 
			$("input.pagination-num").trigger(e);//模拟页码框按下回车 
		}
		
		//初始化授课计划列表,传入参数为学年学期编码,教学性质,课程代码,班级编码
		function initShouke(year, xz, xk, bj){
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_Hbsz',
				data : "active=shoukeData&year="+year+"&xz="+xz+"&xk="+xk+"&bj="+bj,
				dataType:"json",
				success : function(data) {
					shoukejihua(data[0].listData);//加载特殊规则列表
				}
			});
		}
		
		//初始化授课计划列表,传入参数为学年学期编码,教学性质,课程代码,班级编码
		function initShouke2(year, xz, xk, bj,bianhao){
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_Hbsz',
				data : "active=shoukeData2&year="+year+"&xz="+xz+"&xk="+xk+"&bj="+bj+"&bianhao="+bianhao,
				dataType:"json",
				success : function(data) {
					shoukejihua2(data[0].listData);//加载特殊规则列表
				}
			});
		}
		
		//加载授课计划列表
		function shoukejihua(shouke){
			isLoad = true;
			$('#shoukejihua').datagrid({
				data:shouke,
				loadMsg : "信息加载中请稍后!",//载入时信息
				width : 886,//宽度
				height: 252,
				rownumbers: true,
				animate:true,
				striped : true,//隔行变色
				pageSize : pageSize,//每页记录数
				singleSelect : false,//单选模式
				multiple:true,
				pageNumber : pageNum,//当前页码
				fitColumns: true,//设置边距
				idField:'授课计划明细编号',
				columns:[[
				    {field:'ck',checkbox:true},
				    {field:'行政班名称',title:'行政班名称',width:fillsize(0.15)},
				    {field:'课程名称',title:'课程名称',width:fillsize(0.1)},
					{field:'授课教师姓名',title:'任课教师',width:fillsize(0.1)},
					{field:'节数',title:'节数',width:fillsize(0.05)},
					{field:'连节',title:'连节',width:fillsize(0.05)},
					{field:'连次',title:'连次',width:fillsize(0.05)},
					{field:'场地名称',title:'场地要求',width:fillsize(0.1)},
					{field:'授课周次',title:'授课周次',width:fillsize(0.1)}
				]],
				onSelect:function(rowIndex,rowData){
					if(isLoad == false){
						bianhao.push(rowData.授课计划明细编号);
					}
				},
				onUnselect:function(rowIndex,rowData){
					$.each(bianhao, function(key,value){
						if(value == rowData.授课计划明细编号){
							bianhao.splice(key, 1);
						}
					});
				},
				onSelectAll:function(rows){
					bianhao.splice(0,bianhao.length);
					for(var i=0;i<rows.length;i++){
						bianhao.push(rows[i].授课计划明细编号);
					}
				},
				onUnselectAll:function(rows){
					bianhao.splice(0,bianhao.length);
				},
				onLoadSuccess: function(data){
					isLoad = false;
				},
				onLoadError:function(none){
					
				}
			});
		}
		
		//加载授课计划列表
		function shoukejihua2(shouke){
			isLoad = true;
			$('#shoukejihua2').datagrid({
				data:shouke,
				loadMsg : "信息加载中请稍后!",//载入时信息
				width : 886,//宽度
				height: 252,
				rownumbers: true,
				animate:true,
				striped : true,//隔行变色
				pageSize : pageSize,//每页记录数
				singleSelect : true,//单选模式
				multiple:false,
				pageNumber : pageNum,//当前页码
				fitColumns: true,//设置边距
				idField:'授课计划明细编号',
				columns:[[
				    {field:'ck',checkbox:true},
				    {field:'行政班名称',title:'行政班名称',width:fillsize(0.15)},
				    {field:'课程名称',title:'课程名称',width:fillsize(0.1)},
					{field:'授课教师姓名',title:'任课教师',width:fillsize(0.1)},
					{field:'节数',title:'节数',width:fillsize(0.05)},
					{field:'连节',title:'连节',width:fillsize(0.05)},
					{field:'连次',title:'连次',width:fillsize(0.05)},
					{field:'场地名称',title:'场地要求',width:fillsize(0.1)},
					{field:'授课周次',title:'授课周次',width:fillsize(0.1)}
				]],
				onClickRow:function(rowIndex, rowData){
					
				},
				onLoadSuccess: function(data){
					isLoad = false;
					//$(".datagrid-header-check").html('&nbsp;');//隐藏全选
				},
				onLoadError:function(none){
					
				}
			});
		}
		
		//查询后台数据
		function loadData(){
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_Hbsz',
				data : 'active=query&page=' + pageNum + '&rows=' + pageSize,
					//'&GT_XNXQBM=' + $('#GT_XNXQBM').combobox('getValue') + 
					//'&GT_JXXZ=' + $('#GT_JXXZ').combobox('getValue') + 
					//'&GT_JSBH=' + $('#GT_JSBH').textbox('getValue'),
				dataType:"json",
				success : function(data) {
					loadGrid(data[0].listData);//加载特殊规则列表
				}
			});
		}
	
		//加载下拉框数据
		function initCombobox(xnxq,jxxz,zymc){
			$('#GH_XNXQBM').combobox({
				data:xnxq,
				valueField:'comboValue',
				textField:'comboName',
				editable:false,
				panelHeight:'140', //combobox高度
				onLoadSuccess:function(data){
					//判断data参数是否为空
					if(data != ''){
						//初始化combobox时赋值
						$(this).combobox('setValue', data[1].comboValue);
					}
				},
				//下拉列表值改变事件
				onChange:function(data){
					$('#GH_ZYDM').combobox('setValue','');	
				}
			});
			
			$('#GH_JXXZ').combobox({
				data:jxxz,
				valueField:'comboValue',
				textField:'comboName',
				editable:false,
				panelHeight:'140', //combobox高度
				onLoadSuccess:function(data){
					//判断data参数是否为空
					if(data != ''){
						//初始化combobox时赋值
						$(this).combobox('setValue', data[1].comboValue);
					}
				},
				//下拉列表值改变事件
				onChange:function(data){
					
				}
			});
			
			$('#GH_ZYDM').combobox({
				data:zymc,
				valueField:'comboValue',
				textField:'comboName',
				editable:true,
				multiple:true,
				panelHeight:'140', //combobox高度
				onLoadSuccess:function(data){
					//判断data参数是否为空
					if(data != ''){
						//初始化combobox时赋值
						$(this).combobox('setValue', '');
					}
				},
				//下拉列表值改变事件
				onSelect:function(data){
					//
					var obj = $(this).combobox('getValues');
					//$(this).combobox('unselect', '');
					if(obj.length>1) {
						$(this).combobox('unselect', '');				
					}
					//loadCourse($('#GH_XNXQBM').combobox('getValue'),$('#GH_JXXZ').combobox('getValue'),data.comboValue);
				},
				//下拉列表值改变事件
				onUnselect:function(data){
					var obj = $(this).combobox('getValues');
					//if(obj.length>1) {
						//$(this).combobox('unselect', '');
						//initShouke($('#GH_XNXQBM').combobox('getValue'), $('#GH_JXXZ').combobox('getValue'), $('#GH_KCDM').combobox('getValue'), $(this).combobox('getValues'));
					//}else {
 					if(obj.length == 0){
 						$(this).combobox('setValue', '');
 					}
					//loadCourse($('#GH_XNXQBM').combobox('getValue'),$('#GH_JXXZ').combobox('getValue'),data.comboValue);
				},
				//下拉列表值改变事件
				onChange:function(data){
					//loadCourse($('#GH_XNXQBM').combobox('getValue'),$('#GH_JXXZ').combobox('getValue'),data);
					loadClass($('#GH_XNXQBM').combobox('getValue'),$('#GH_JXXZ').combobox('getValue'),data);
				}
			});	
		}
		
		//获取选择专业的课程
		function loadCourse(xnxqid,jxxzid,major,classid){
			$('#GH_KCDM').combobox({
				type : "POST",
				url : "<%=request.getContextPath()%>/Svl_Hbsz?active=loadCourse&xnxq="+xnxqid+"&jxxz="+jxxzid+"&zydm="+major+"&classid="+classid,
				valueField:'comboValue',
				textField:'comboName',
				editable:true,
				multiple:true,
				panelHeight:'140', //combobox高度
				onLoadSuccess:function(data){
					//判断data参数是否为空
					if(data != ''){
						//初始化combobox时赋值
						$(this).combobox('setValue', '');
					}
				},
				//下拉列表值改变事件
				onSelect:function(data){
					var obj = $(this).combobox('getValues');
					if(obj.length>1) {
						$(this).combobox('unselect', '');				
					}
// 					initShouke($('#GH_XNXQBM').combobox('getValue'), $('#GH_JXXZ').combobox('getValue'), $(this).combobox('getValues'), $('#GH_XZBDM').combobox('getValues'));
				},
				//下拉列表值改变事件
				onUnselect:function(data){
					var obj = $(this).combobox('getValues');
					//if(obj.length>1) {
						//$(this).combobox('unselect', '');
						//initShouke($('#GH_XNXQBM').combobox('getValue'), $('#GH_JXXZ').combobox('getValue'), $('#GH_KCDM').combobox('getValue'), $(this).combobox('getValues'));
					//}else {
 					if(obj.length == 0){
 						$(this).combobox('setValue', '');
 					}
					//loadClass($('#GH_XNXQBM').combobox('getValue'),$('#GH_JXXZ').combobox('getValue'),data.comboValue);
					//initShouke($('#GH_XNXQBM').combobox('getValue'), $('#GH_JXXZ').combobox('getValue'), $(this).combobox('getValues'), $('#GH_XZBDM').combobox('getValues'));
				},
				//下拉列表值改变事件
				onChange:function(data){
					
				}
			});
		}
		
		//获取选择课程的班级
		function loadClass(xnxqid,jxxzid,major){
			$('#GH_XZBDM').combobox({
				type : "POST",
				url : "<%=request.getContextPath()%>/Svl_Hbsz?active=loadClass&xnxq="+xnxqid+"&jxxz="+jxxzid+"&zydm="+major,
				valueField:'comboValue',
				textField:'comboName',
				editable:true,
				multiple:true,
				panelHeight:'140', //combobox高度
				onLoadSuccess:function(data){
					//判断data参数是否为空
					if(data != ''){
						//初始化combobox时赋值
						$(this).combobox('setValue', '');
					}
				},
				//下拉列表值改变事件
				onSelect:function(data){
					var obj = $(this).combobox('getValues');
					//$(this).combobox('unselect', '');
					if(obj.length>1) {
						$(this).combobox('unselect', '');
						
					}
				},
				//下拉列表值改变事件
				onUnselect:function(data){
					var obj = $(this).combobox('getValues');
					//if(obj.length>1) {
						//$(this).combobox('unselect', '');
						//initShouke($('#GH_XNXQBM').combobox('getValue'), $('#GH_JXXZ').combobox('getValue'), $('#GH_KCDM').combobox('getValue'), $(this).combobox('getValues'));
					//}else {
					if(obj.length == 0){
						$(this).combobox('setValue', '');
					}
					//initShouke($('#GH_XNXQBM').combobox('getValue'), $('#GH_JXXZ').combobox('getValue'), $('#GH_KCDM').combobox('getValues'), $(this).combobox('getValues'));
					
				},
				//下拉列表值改变事件
				onChange:function(data){
// 					if($(this).combobox('getValues')=="") {
// 						initShouke($('#GH_XNXQBM').combobox('getValue'), $('#GH_JXXZ').combobox('getValue'), $('#GH_KCDM').combobox('getValues'), $('#GH_XZBDM').combobox('getValues'));
// 					}
					loadCourse($('#GH_XNXQBM').combobox('getValue'),$('#GH_JXXZ').combobox('getValue'),major,data);
				}
			});
		}
	
		//工具按钮
		function doToolbar(id){
			//查询
			//if(id == 'query'){
				//loadData();
			//}
			//编辑
			if(id == "add"){
				$("#order").linkbutton("enable");
				$('#win').dialog("open");
			}
			//删除
			if(id == "del"){
				if(ikeycode==""){
					alertMsg("请选择一行数据");
					return;
				}else{
					checkpaike(savexqbh);
					if(paike==0){
	        			ConfirmMsg("删除后，数据无法恢复，是否继续？","DelRec();","");
	        		}else{
	        			alertMsg("对不起，所选的合班信息已过排课截止时间，无法删除！");
	        		}
	        	}
			}
			//保存
			if(id == "save"){
				var object = $("#shoukejihua2").datagrid("getSelections");
				ikeycodeforshouke = object[0].授课计划明细编号;
			
				if(ikeycodeforshouke==""){
					alertMsg("请选择1条授课计划");
				}else{
					$("#active").val(id);
					ConfirmMsg("是否确认以该条授课计划为主,更改其它授课计划？","changeShoukejihua();","clearBianhao();");
				}				
			}
			
			//以一条授课计划为主
			if(id == "order") {
				checkpaike($("#GH_XNXQBM").combobox('getValue')+$("#GH_JXXZ").combobox('getValue'));
				if(paike==0){ 
					if(bianhao.length!=1){
						alertMsg("请选择1条授课计划");
					}else{
						$("#order").linkbutton("disable");
						$("#ok").linkbutton("enable");
						
						var object = $("#shoukejihua").datagrid("getSelections");
						ikeycodeforshouke = object[0].授课计划明细编号;
						bianhao.splice(0,bianhao.length);
						$("#shoukejihua").datagrid("selectAll"); 
// 						var object1 = $("#shoukejihua").datagrid("getSelections");
// 						for(var i=0; i<object.length; i++) {
// 							if(object1[i].授课计划明细编号 != ikeycodeforshouke) {
// 								bianhao.push(object1[i].授课计划明细编号);
// 							}
// 						}		
					}
				}else{
					alertMsg("对不起，所选的学期已过排课截止时间，不能添加！");
					$('#GH_ZYDM').combobox('setValue','');	
				}
			}
			//确认
			if(id == "ok"){ 
				if($('#GH_XZBDM').combobox('getValues')==""){
					alertMsg("请选择班级");
					return;
				}
				if($('#GH_KCDM').combobox('getValues')==""){
					alertMsg("请选择课程");
					return;
				}
				
				$('#skjh').dialog("open");
				initShouke($('#GH_XNXQBM').combobox('getValue'), $('#GH_JXXZ').combobox('getValue'), $('#GH_KCDM').combobox('getValues'), $('#GH_XZBDM').combobox('getValues'));
				$('#win').hide();
			}
	
			if(id == "back1"){
				skjhback1=1;
				$('#win').show(); 
				$('#skjh').dialog("close");
			}
			
			if(id == "next"){
				if(bianhao.length<2){
					alertMsg("请至少选择2条授课计划");
				}else{ 
					$('#skjh2').dialog("open");
					initShouke2($('#GH_XNXQBM').combobox('getValue'), $('#GH_JXXZ').combobox('getValue'), $('#GH_KCDM').combobox('getValues'), $('#GH_XZBDM').combobox('getValues'),bianhao);
					$('#skjh').hide();
				}
			}
			
			if(id == "back2"){
				skjhback2=1;
				$('#skjh').show(); 
				$('#skjh2').dialog("close");
			}
			
// 			if(id == "same"){ 
// 				if(ikeycodeforshouke==""){
// 					alertMsg("请选择1条授课计划");
// 				}else{
// 					ConfirmMsg("是否确认以该条授课计划为主,更改其它授课计划？","changeShoukejihua();","clearBianhao();");
// 				}
// 			}
			
		}
		
		//fm提交事件
		function loadForm(){
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_Hbsz',
				data : 'active=save&kcdm='+$('#GH_KCDM').combobox('getValues')+'&bjdm='+$('#GH_XZBDM').combobox('getValues')+'&GH_XNXQBM='+$('#GH_XNXQBM').combobox('getValues')+'&jxxz='+$('#GH_JXXZ').combobox('getValues')+'&bianhao='+bianhao,
				dataType:"json",
				success : function(data) {			
						showMsg(data[0].msg);
						$('#skjh2').dialog("close");
						$('#skjh').dialog("close");
						$('#win').dialog("close");
						clearBianhao();
						initData();				
				}
			});
		}
		
		//fm提交事件
		$('#fm').form({ 
			url:'<%=request.getContextPath()%>/Svl_Hbsz?active=save&kcdm='+$('#GH_KCDM').combobox('getValues'),
			onSubmit:function(){
			
			},
			//提交成功
			success:function(datas){
				if($('#active').val()=="save"){
					var json = eval("("+datas+")");
					showMsg(json[0].msg);
					$('#win').dialog("close");
					initData();
				}
			}
		});
		
		//删除方法
		function DelRec(){
			$.ajax({
				type:'post',
				url:"<%=request.getContextPath()%>/Svl_Hbsz",
				data:"active=del&GT_JSBH="+ikeycode,
				dataType:'json',
				success:function(datas){
					var data=datas[0];
					showMsg(data.msg);
					initData();
				}
			});
		}
		
		//特殊规则编辑窗口
		function loadDialog() {
			$('#win').dialog({
				width : 900,
				height: 320,
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
			
			$('#skjh').dialog({
				width : 900,
				height: 320,
				modal : true,
				closed : true,
				onOpen : function(data) {
					$('#skjh').find('table').css('display', 'block');
				},
				onLoad : function(data) {
				},
				onClose : function(data) {
					bianhao.splice(0,bianhao.length);
					$("#shoukejihua").datagrid("unselectAll");
					if(skjhback1==0){			
						$('#win').show();
						$('#win').dialog("close");
					}else{
						skjhback1=0;
					}
				}
			});
			
			$('#skjh2').dialog({
				width : 900,
				height: 320,
				modal : true,
				closed : true,
				onOpen : function(data) {
					$('#skjh2').find('table').css('display', 'block');
				},
				onLoad : function(data) {
				},
				onClose : function(data) {
					ikeycodeforshouke="";
					$("#shoukejihua2").datagrid("unselectAll");
					if(skjhback2==0){			
						$('#skjh').show();
						$('#skjh').dialog("close");
					}else{
						skjhback2=0;
					}
				}
			});
		}
		
		function changeShoukejihua() {
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_Hbsz',
				data : "active=changeShouke&mainCode="+ikeycodeforshouke+"&otherCode="+bianhao,
				dataType:"json",
				success : function(data) {
					initShouke2($('#GH_XNXQBM').combobox('getValue'), $('#GH_JXXZ').combobox('getValue'), $('#GH_KCDM').combobox('getValues'), $('#GH_XZBDM').combobox('getValues'),bianhao);
					if(data[0].msg2=="0") {								
						loadForm();	
					}else {
						alertMsg(data[0].msg2+"班级的该门课程已经进行过固排禁排的操作,不能进行合班操作,如果需要进行合班,请先删除固排禁排");
					}	
					
				}
			});
		}
		
		function clearBianhao() {
			bianhao = [];
			$("#shoukejihua").datagrid("unselectAll");
			$("#shoukejihua2").datagrid("unselectAll");
		}
		
		//初始化信息
		function emptyDialog(){
			$('#GT_MTCS').textbox('setValue', '');
			$('#GT_MZCS').textbox('setValue', '');
			$('#GT_MTJC').textbox('setValue', '');
			$('#GT_MZJC').textbox('setValue', '');
			$('#GT_ZDZJKCS').textbox('setValue', '');
			$('#GH_ZYDM').combobox('setValue','');		
			$("#order").linkbutton("enable");
		}
	</script>
</html>