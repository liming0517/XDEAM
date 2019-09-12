<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%
	/**
		创建人：lupengfei
		Create date: 2016.08.19
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
<%@ page import="com.pantech.base.common.tools.PublicTools"%>
<%@ page import="com.pantech.src.develop.store.user.*"%>
<%@ page import="java.util.Vector"%>
<%
UserProfile up = new UserProfile(request,MyTools.getSessionUserCode(request)); 
String userName =up.getUserName();
String userCode =up.getUserCode();
%>
<%
		/*
			获得角色信息
		*/
		
		String root = request.getContextPath();
		//out.println("当前在线人数： "+SessionListener.getCount());
		//人员对象
		
		
		Vector v = new Vector(); 
		String usercode = "";
		usercode = MyTools.getSessionUserCode(request);
		String sAuth = "";
		//int i=0;
		AuthObject auth;
		
		//获取人员所属角色
		v = up.getUserAuth();
		for(int i=0;i<v.size();i++){
			sAuth += MyTools.StrFiltr(v.get(i))+",";
		}
		sAuth=sAuth.substring(0, sAuth.length()-1);
		// 如果无法获取人员信息，则自动跳转到登陆界面
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
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/publicScript.js"></script>
		<style>
			#xlTime{
				width:100%;
				border-right:1px solid #99bbe8;
			}
			#xlTime{
				width:99.9%;
				border-top:1px solid #99bbe8;
			}
			#xlTime td{
				width:300px;
				border-left:1px solid #99bbe8;
				border-bottom:1px solid #99bbe8;
				text-align:center;
				empty-cells:show;
				font-size:11;
			}
			#xlTime th{
				width:300px;
				border-left:1px solid #99bbe8;
				border-bottom:1px solid #99bbe8;
				text-align:center;
				empty-cells:show;
			}
			#divPageMask2{background-color:#D2E0F2; filter:alpha(opacity=50);left:0px;top:0px;z-index:100;}
		</style>
	</head>
	<body class="easyui-layout" onkeydown="myKeyDown()">
		<div data-options="region:'west'" title="" style = "width:28%;">
			<div class="easyui-layout" fit="true">
				<div region="north" split="true" border="false" style="height:56px;width:100%;"> 
<!-- 					<table  class = "tablestyle" width = "100%"> -->
<!-- 						<tr> -->
<!-- 							<td> -->
<!-- 								<a href="#" onclick="doToolbar(this.id);" id="import"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-site'">选修课导入</a> -->
<!-- 							</td> -->
<!-- 						</tr> -->
<!-- 					</table> -->
					<table  class = "tablestyle" width = "100%">
						<tr>
							<td class="titlestyle">学年学期</td>
							<td>
								<select id="XNXQ" name="XNXQ" class="easyui-combobox" style="width:180px;"></select>
							</td>
		<!-- 					<td> -->
		<!-- 						<a href="#" onclick="doToolbar(this.id);" id="que"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-search'">查询</a> -->
		<!-- 					</td> -->
<!-- 							<td rowspan=2> -->
<!-- 								<a href="#" onclick="doToolbar(this.id);" id="queterm"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-search'">详细查询</a> -->
<!-- 							</td> -->
						</tr>
						<tr>
							<td class="titlestyle">教学性质</td>
							<td>
								<select id="JXXZ" name="JXXZ" class="easyui-combobox" style="width:180px;"></select>
							</td>
							
						</tr>
					</table>
				</div> 
				<div region="center" border="false"> 
					<div id="classTree" class="easyui-tree" style="width:100%;">
					</div>
				</div> 
			</div> 	
		</div>
		
		<div id="search" title="查询条件" style="">
			<table width="286px" class="tablestyle">
				<tr>
					<a href="#" class="easyui-linkbutton" id="submit" name="submit" iconCls="icon-submit" plain="true" onClick="doToolbar(this.id)">确定</a>
				</tr>
				<tr>
					<td width="50%" class="titlestyle">校区</td>
					<td  width="50%">
						<select id="ic_school" name="ic_school" class="easyui-combobox" style="width:97%;" panelHeight="auto" ></select>
					</td>
				</tr>
<!-- 				<tr> -->
<!-- 					<td width="50%" class="titlestyle">建筑物</td> -->
<!-- 					<td  width="50%"> -->
<!-- 						<select id="ic_building" name="ic_building" class="easyui-combobox" style="width:97%;" panelHeight="auto"></select> -->
<!-- 					</td>													 -->
<!-- 				</tr> -->
				<tr>
					<td width="50%" class="titlestyle">教室类型</td>
					<td  width="50%">
						<select id="ic_classtype" name="ic_classtype" class="easyui-combobox" style="width:97%;" panelHeight="auto"></select>
					</td>
				</tr>
				<tr>
					<td width="50%" class="titlestyle">教室</td>
					<td  width="50%">
						<input style='width:96%' class='easyui-validate' id='ic_classroom' name='ic_teaName'/>
					</td>													
				</tr>
			</table>
		</div>
		
		<div data-options="region:'center'" style="overflow-x:hidden;">
			<div class="easyui-layout" fit="true">
				<div region="north" split="true" border="false" style="height:130px;width:100%;"> 
					<table id='list' style="width:100%;height:20%;">
					</table>
				</div> 
				<div region="center" border="false"> 
					<div id="setTime" style="overflow-x:hidden;">
						<form id='fm' method='post' style="margin: 0px;padding-top: 1px;">
							<table id="xlTime" cellspacing="0" cellpadding="0" style="height:74%;border-collapse:collapse;"> 
								<tbody id="content"> 
									 
								</tbody> 
							</table>
							<input type="hidden" id="active" name="active"/>
							<input type="hidden" id="GG_XZBDM" name="GG_XZBDM"/>
							<input type="hidden" id="GG_XNXQBM" name="GG_XNXQBM"/>
							<input type="hidden" id="GG_SJXL" name="GG_SJXL"/>
							<input type="hidden" id="KCJS" name="KCJS"/>
							<input type="hidden" id="SJXL" name="SJXL"/>
							<input type="hidden" id="CDYQ" name="CDYQ"/>
							<input type="hidden" id="CDMC" name="CDMC"/>
							<input type="hidden" id="SKZC" name="SKZC"/>
							<input type="hidden" id="SAVETYPE" name="SAVETYPE"/>
							<input type="hidden" id="GG_SKJHMXBH" name="GG_SKJHMXBH"/>
							<input type="hidden" id="JSBH" name="JSBH"/>
							<input type="hidden" id="JSXM" name="JSXM"/>
						</form>
					</div>
					
					<%-- 遮罩层 --%>
			    	<div id="divPageMask2" style="position:absolute;top:0px;left:0px;width:100%;height:100%;">
			    	</div>
				</div> 
			</div>
		</div>
		
		<!-- 选修课导入 -->
		<div id="xxkImport" style="overflow:hidden;display:none;">
			<form id="form1" method='post'>
				<table style="width:100%;" class="tablestyle">
					<tr>
						<td class="titlestyle">学年学期</td>
						<td>
							<select name="IM_XNXQ" id="IM_XNXQ" class="easyui-combobox" panelHeight="auto" editable="false" style="width:150px;"  required="true">
							</select>
						</td>
					</tr>
					<tr>
						<td class="titlestyle">教学性质</td>
						<td>
							<select name="IM_JXXZ" id="IM_JXXZ" class="easyui-combobox" panelHeight="auto" editable="false" style="width:150px;">
							</select>
						</td>
					</tr>
					<tr>
						<td colspan="2" align="center">
							<input id="Select" name="Select" class="easyui-textbox" type='button' style="text-align:center;width:80px;" value='[附件选择]'  onclick='selectfile()'/>
						</td>
					</tr>
					<tr>
						<td colspan="2" align="center">
							<input id="Upload" name="Upload" class="easyui-textbox" type='button' style="text-align:center;width:80px;" value='[上传]'  onclick='show()'/>
						</td>
					</tr>
				</table>
				<input type="hidden" id="active" name="active"/>
			</form>
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
		var paike="0";//排课状态
		
		
		var iUSERCODE="<%=MyTools.getSessionUserCode(request)%>";   //获得用户登录code
		var sAuth="<%=sAuth%>";  //角色权限
		var iKeyCode = "";//授课计划明细编号
		//var xkmc = "";//学科名称下拉框数据
		//var rkjs = "";//任课教师下拉框数据
		var kcmc = "";//课程名称
		var jsbh = "";//教师编号
		var jsxm = "";//教师姓名
		var skzc = "";//授课周次
		var skzcxq = "";//授课周次详情
		var cdyq = "";//场地要求
		var cdmc = "";//场地名称
		var zjs = "";//总节数
		var gjs = "";//固排已排节数
		var lj = "";//连节
		var lc = "";//连次
		var lcs= "";//已排连次次数
		var aod= "";//判断添加或删除
		var checkrec="";
		var delinfo = "";
		var classdm = "";//行政班代码
		var classmc = "";//行政班名称

		var weeks="";
		var lastIndex = -1;
		var xnxqVal="";//保存传递过来的学期编码
		var jxxzVal="";//保存传递过来的学期编码
		var paike="0";//排课状态
		var gpjs=0;
		var idValconTrans="";
		var kclx=""; //课程类型
		//var saveType = "";//判断打开窗口的操作（new or edit）
		var wpjs=0;//未排节数
		
		$(document).ready(function(){
			$('#search').hide();
			loadXQWEEK();//页面初始化加载数据
			initDialog();
			initGridData(classId,className,xnxqVal,jxxzVal,"1");//页面初始化加载数据
			
		});
		
		//获取学年学期combobox，学期周数
		function loadXQWEEK(){
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_Jsgpjp',
				data : 'active=initData',
				dataType:"json",
				success : function(data) {
					xnxq = data[0].xnxqData;//获取学年学期下拉框数据
					jxxz = data[0].jxxzData;//获取教学性质下拉框数据
					xnxqCombobox(xnxq,jxxz);
					loadData("","");
				}
			});
		}
		
		//加载下拉框数据
		function xnxqCombobox(xnxq,jxxz){
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
		
		//查询后台数据
		function loadData(xnxq_cx,jxxz_cx){
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_Jsgpjp',
				data : 'active=query&JXXZ=' + jxxz_cx + '&XNXQ=' + xnxq_cx + '&termid='+(xnxq_cx+jxxz_cx),
				dataType:"json",
				success : function(data) {
					weeks=data[0].MSG;
					initData(weeks);
					treegrid();
				}
			});
		}
		
		//页面初始化加载数据
		function initData(weeks){
			initGridData("","",xnxqVal,jxxzVal,"1");//页面初始化加载数据
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
		
// 		function treegrid(){
// 			$('#List').treegrid({
// 				url : '< %=request.getContextPath()%>/Svl_Cdgpjp',
// 				queryParams : {"active":"queryTree","school":$('#ic_school').combobox('getText'),"classtype":$('#ic_classtype').combobox('getText'),"classroom":$('#ic_classroom').val()},
// 				width:'100%',
// 				loadMsg:'数据加载中，请稍后...',
// 				rownumbers: true,
// 				animate:true,
// 				fit:true,
// 				fitColumns: true,//设置边距
// 				idField:'id',
// 				treeField:'名称',
// 				columns:[[
// 					{field:'校区名称',title:'<b>校区</b>',width:fillsize(0.2)},				
// 					{field:'教室类型',title:'<b>教室类型</b>',width:fillsize(0.2)},
// 	                {field:'教室名称',title:'<b>教室</b>',width:fillsize(0.2)},
// 	                {field:'实际容量',title:'<b>容量（人）</b>',width:fillsize(0.2)}
// 				]],
// 				onClickRow:function(row){
// 					classId=row.id;//实际是场地要求
// 					parentId=row._parentId;//实际是场地名称
// 					className=row.教室名称;
// 					xnxqVal=$('#XNXQ').combobox('getValue');
// 					jxxzVal=$('#JXXZ').combobox('getValue');
// 					lastIndex = "";
// 					iKeyCode = "";
// 					kcmc = "";
// 					jsbh = "";
// 					jsxm = "";
// 					skzcxq = "";
// 					lj = "";
// 					lc = "";
// 					cdyq="";
// 					cdmc="";
// 					kclx="";
// 					wpjs="";
// 					initGridData(classId,className,xnxqVal,jxxzVal,"1");//点击后刷新right页面取值结果
// 				},
// 				onLoadSuccess:function(data){
// 					xnxqVal=$('#XNXQ').combobox('getValue');
// 					jxxzVal=$('#JXXZ').combobox('getValue');
// 			    }
// 			});
// 		}
		
		function treegrid(){ 
			$('#classTree').tree({
				checkbox: false,
				url:'<%=request.getContextPath()%>/Svl_Cdgpjp?active=queryTree&sAuth='+sAuth+'&level=0&parentId=""'+'&iUSERCODE='+iUSERCODE+'&GG_XNXQBM='+(xnxqVal+jxxzVal),
				onClick:function(node){
					//判断点击的是不是班级
			    	if($('#classTree').tree('getParent', node.target) != null){
			    		if(lastIndex!=node.id){
							classId=node.id;
							className=node.text;
							lastIndex=node.id;
							xnxqVal=$('#XNXQ').combobox('getValue');
							jxxzVal=$('#JXXZ').combobox('getValue');
							iKeyCode = "";
							kcmc = "";
							jsbh = "";
							jsxm = "";
							skzcxq = "";
							lj = "";
							lc = "";
							cdyq="";
							cdmc="";
							kclx="";
							wpjs="";
							initGridData(classId,className,xnxqVal,jxxzVal,"1");//点击后刷新right页面取值结果
						}
						parentId=node.id;
			    	}else{
			    		initGridData("","",xnxqVal,jxxzVal,"1");//页面初始化加载数据
			    		parentId="";
			    		lastIndex="";
			    	}					
				},
// 				onBeforeLoad:function(row,param){     //分层显示treegrid
// 				},
				onBeforeExpand:function(node,param){
			  		$('#classTree').tree('options').url='<%=request.getContextPath()%>/Svl_Cdgpjp?active=queryTree&level=1&parentId='+node.id+'&iUSERCODE='+iUSERCODE+'&GG_XNXQBM='+(xnxqVal+jxxzVal);
				},
				onLoadSuccess:function(data){
					$('#classTree').show();
					xnxqVal = $('#XNXQ').combobox('getValue');
					jxxzVal = $('#JXXZ').combobox('getValue');
					
			    }
			});
		}
		
		function schoolCombobox(){
			$('#ic_school').combobox({
				url:"<%=request.getContextPath()%>/Svl_Cdgpjp?active=schoolCombobox",
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
		}
		
		function classtypeCombobox(){
			$('#ic_classtype').combobox({
				url:"<%=request.getContextPath()%>/Svl_Cdgpjp?active=classtypeCombobox",
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
		}
		
		//工具按钮
		function doToolbar(id){
			//查询
			if(id == 'que'){
				xnxq_cx = $('#XNXQ').combobox('getValue');
				jxxz_cx = $('#JXXZ').combobox('getValue');
				jsxm="";
				skzcxq="";
				cdyq="";
				iKeyCode="";
				classId="";
				jsbh="";
				if(!classId==""){
					$('#List').treegrid("unselectRow", classId);
				}			
				treegrid();
			}
			if(id == 'queterm'){
				openSearch();
			}
			if(id == 'submit'){
				treegrid();
				$('#search').dialog("close");
			}
			if(id == "import"){
				$('#xxkImport').show();
				$('#xxkImport').dialog('open');
			}
			if(id == "savexxk"){
				
			}
		}
		
		//打开teacher编辑窗口
		function openSearch(){
			schoolCombobox();
			classtypeCombobox();
			
			$('#search').show();
			$('#search').dialog({   
				title: '查询条件',   
				width: 300,//宽度设置   
				height: 139,//高度设置 
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
		
		//---------------------------------------------------------------------------------
		
		function myKeyDown(){
			var k=window.event.keyCode;
			if (8 == k){
				event.keyCode=0;//取消按键操作
			}
		}
	
		//页面初始化加载数据
		function initGridData(classId,className,xnxqVal,jxxzVal,n){ 
			checkpaike();
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_Cdgpjp',
				data : 'active=initGridData&page=' + pageNum + '&rows=' + pageSize + '&JSBH=' + classId + '&JSXM=' + className + '&XNXQ=' + xnxqVal + '&JXXZ=' + jxxzVal+'&iUSERCODE='+iUSERCODE+'&sAuth='+sAuth,
				dataType:"json",
				success : function(data) { 
					//xkmc = data[0].xkmcData;//获取学科名称下拉框数据
					//rkjs = data[0].rkjsData;//获取任课教师下拉框数据					
					if(n!="3"){
						loadGrid(data[0].listData);//加载特殊规则列表
					}
					loadTime(data[0].timeData,data[0].gpjpData);
					//initCombobox(xkmc,rkjs);//初始化下拉框
					if(n=="1"){
						iKeyCode = "";
						$('#list').datagrid("unselectRow", lastIndex);
					}else if(iKeyCode!=""){
						checkTeaCls(jsxm,skzcxq,weeks,cdyq,iKeyCode,xnxqVal+jxxzVal,classId,classdm,jsbh);
						//queryjinpai(xnxqVal,jxxzVal,iKeyCode);
					}
				}
			});
		}
		
		//检查当前时间是否超过允许排课时间
		function checkpaike(){ 
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_Skjh',
				data : 'active=checkpaike&GS_XNXQBM=' + (xnxqVal+jxxzVal),
				async : false,
				dataType:"json",
				success : function(data) {
					paike=data[0].MSG;
				}
			});
		}
		
		function loadGrid(listData){
			$('#list').datagrid({
				data:listData,
				loadMsg : "信息加载中请稍后!",//载入时信息
				rownumbers: true,
				animate:true,
				striped : true,//隔行变色
				pageSize : pageSize,//每页记录数
				singleSelect : true,//单选模式
				pageNumber : pageNum,//当前页码
				pagination:false,
				fit:true,
				fitColumns: true,//设置边距
				columns:[[
					{field:'课程名称',title:'科目名称',width:100},
					{field:'班级名称',title:'班级名称',width:100},
					{field:'授课教师姓名',title:'任课教师',width:100,
						formatter:function(value,rec){
							var skzcxq2=rec.授课教师姓名;
							skzcxq2=skzcxq2.replace(new RegExp('@','gm'),'&');
							return skzcxq2;
						}
					},
					{field:'固排已排节数',title:'已排节数',width:50},
					{field:'未排节数',title:'未排节数',width:50},
					{field:'授课周次详情',title:'授课周次',width:50,
						formatter:function(value,rec){
							var skzcxq2=rec.授课周次详情;
							skzcxq2=skzcxq2.replace(new RegExp('odd','gm'),'单周');
							skzcxq2=skzcxq2.replace(new RegExp('even','gm'),'双周');
							skzcxq2=skzcxq2.replace(new RegExp('@','gm'),'&');
							return skzcxq2;
						}
					}
				]],
				onClickRow:function(rowIndex, rowData){
					lastIndex = rowIndex;
					iKeyCode = rowData.授课计划明细编号;
					//iKeyCodeZb = rowData.授课计划主表编号;
					kcmc = rowData.课程名称;
					jsbh = rowData.授课教师编号;
					jsxm = rowData.授课教师姓名;
					classdm=rowData.行政班代码;
					classmc=rowData.行政班名称;
					lj = rowData.连节;
					lc = rowData.连次;
					cdyq=rowData.场地要求;
					cdmc=rowData.场地名称;
					skzcxq = rowData.授课周次详情;
					cdyq=cdyq.replace(new RegExp('@','gm'),'&');
					cdmc=cdmc.replace(new RegExp('@','gm'),'&');
					skzcxq=skzcxq.replace(new RegExp('@','gm'),'&');
					kclx=rowData.课程类型;
					wpjs=rowData.未排节数;

					initGridData(classId,className,xnxqVal,jxxzVal,"3");
					//queryjinpai(xnxqVal,jxxzVal,iKeyCode);
					//checkTeaCls(jsxm,skzcxq,weeks,cdyq,iKeyCode,xnxqVal+jxxzVal,classId,classdm,jsbh);
					
				},
				onLoadSuccess: function(data){
					if(iKeyCode!=""){
						$('#list').datagrid("selectRow", lastIndex);
					}
					if(data.total == 0){
						$('#save').linkbutton('disable');
					}else{
						$('#save').linkbutton('enable');
					}
					$('#divPageMask2').hide();
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
		
		//查询是否禁排	
		function queryjinpai(xnxq,jxxz,skjh){			
			$.ajax({
					type:'post',
					url:"<%=request.getContextPath()%>/Svl_Jsgpjp",
					data:"active=loadjinpai&GG_XNXQBM="+(xnxq+jxxz)+"&GG_SKJHMXBH="+skjh,
					dataType:'json',
					asy:false,
					success:function(data){
						if(data[0].loadjinpai.length==0){
							for(var i=0;i<data[0].loadallsjxl.length;i++){
								if($('#'+data[0].loadallsjxl[i].时间序列).html()=='禁排'){
									$('#'+data[0].loadallsjxl[i].时间序列).html('');
								}		
							}
						}else{
							for(var i=0;i<data[0].loadjinpai.length;i++){
								$('#'+data[0].loadjinpai[i].时间序列).html('禁排');
							}
						}
					}
			});
		}
		
		//检查可以排的格子
		var clearjinpai="";
		function checkTeaCls(jsxm,skzcxq,weeks,cdyq,iKeyCode,termid,cdyqId,classdm,jsbh){		
					$('#divPageMask2').show();
// 					for(var i=0;i<clearjinpai.length;i=i+3){	
// 						$("#content td[id='"+clearjinpai[i]+"']").html('');			
// 						$("#content td[id='"+clearjinpai[i]+"']").attr('title','');
// 					}
					$.ajax({
						type:'post',
						url:"<%=request.getContextPath()%>/Svl_Cdgpjp",
						data:"active=checkTeaCls&jsxm="+encodeURIComponent(jsxm)+"&skzcxq="+encodeURIComponent(skzcxq)+"&weeks="+encodeURIComponent(weeks)+"&cdyq="+encodeURIComponent(cdyq)+"&iKeyCode="+iKeyCode+"&termid="+termid+"&cdyqId="+encodeURIComponent(cdyqId)+"&classdm="+encodeURIComponent(classdm)+"&jsbh="+encodeURIComponent(jsbh),
						dataType:'json',
						asy:false,
						success:function(datas){
							var times=datas[0].msg;
							var time=times.split(",");
							var tab=document.getElementById("content");
				    		var rows=tab.rows;
				    		for(var i=1;i<rows.length;i++){
					        	for(var j=1;j<rows[i].cells.length;j++){
					        		$('#' + rows[i].cells[j].id).css('background-color', '#E7FECB');	
					        	}
					        }
							for(var i=0;i<time.length;i++){
								$('#' + time[i]).css('background-color', '#FFFFFF');	
							}
// 							for(var i=1;i<6;i++){
// 								for(var j=1;j<rows.length;j++){
// 									if(rows[j].cells[0].innerHTML.substring(0,2)=="晚上"){
// 										if(j<10){
// 											$('#0'+i+'0'+j).css('background-color', '#FFFFFF');
// 										}else{
// 											$('#0'+i+j).css('background-color', '#FFFFFF');
// 										}
// 									}
// 								}							
// 							}
							
							var sames=datas[0].msg2;
							var rooms=datas[0].msg3;
							var yysjxl=datas[0].msg4;
							var jinpaixl=datas[0].msg5;
							var same=sames.split(",");
							var yysj=yysjxl.split("@");
							var jinpai=""; 
							if(jinpaixl!=""){
								jinpai=jinpaixl.split(",");
								clearjinpai=jinpaixl.split(",");
							} 
						
							for(var i=0;i<same.length;i=i+5){
								if(rooms.indexOf(same[i])==-1&&$("#content td[id='"+same[i]+"']").html()==""&&($('#'+same[i]).css("background-color")=="#ffffff"||$('#'+same[i]).css("background-color")=="rgb(255, 255, 255)")){
									if(same[i+4]=="0000"){
										same[i+4]="操场";
									}else if(same[i+4]=="0001"){
										same[i+4]="其它";
									}else if(same[i+4]=="0002"){
										same[i+4]="校外";
									}
									var kyjs="";//可用教室
									for(var j=0;j<yysj.length;j++){
										//当前时间序列在已被占用时间序列内
										if(yysj[j].substring(yysj[j].indexOf("#"),yysj[j].length).indexOf(same[i])>-1){
										
										}else{
											kyjs+=yysj[j].substring(0,yysj[j].indexOf("#"))+",";
										}
									}
									if(kyjs==","){
										kyjs="";
									}else{
										kyjs="可用教室"+kyjs.substring(1,kyjs.length-1);
									}
									$("#content td[id='"+same[i]+"']").attr('title',("此位置 "+same[i+1]+"老师 已安排为 \r\n"+same[i+2]+" "+same[i+3]+" "+same[i+4]+" \r\n"+kyjs));
								} 
							}
						
							if(jinpaixl!=""){
								for(var i=0;i<jinpai.length;i=i+3){					
									if(jinpai[i+2]=="hb"){
										//$("#content td[id='"+jinpai[i]+"']").html('禁排');			
										$("#content td[id='"+jinpai[i]+"']").attr('title',("该授课计划与 "+jinpai[i+1]+" 合班上课，此位置 "+jinpai[i+1]+" 班级 禁排 "));
									}else if(jinpai[i+2]=="bj"){
										//$("#content td[id='"+jinpai[i]+"']").html('禁排');			
										$("#content td[id='"+jinpai[i]+"']").attr('title',("此位置 "+jinpai[i+1]+" 班级 禁排 "));
									}else if(jinpai[i+2]=="js"){
										//$("#content td[id='"+jinpai[i]+"']").html('禁排');			
										$("#content td[id='"+jinpai[i]+"']").attr('title',("此位置 "+jinpai[i+1]+" 老师 禁排 "));
									}else if(jinpai[i+2]=="cd"){
										//$("#content td[id='"+jinpai[i]+"']").html('禁排');			
										$("#content td[id='"+jinpai[i]+"']").attr('title',("此位置 "+jinpai[i+1]+" 教室 禁排 "));
									}
								}
							}
							$('#divPageMask2').hide();
						}
					});				
		}
		
		function loadTime(timeData,gpjpData){
			var count = 1;
			var idNum = "";
			if(timeData != ""){
				$('#setTime').show();
				var html='';
				html += '<tr align="center" style="height:35px;">'+
							'<th>'+
								className +
							'</th>'+
							'<th>'+
								'星期一' +
							'</th>'+
							'<th>'+
								'星期二' +
							'</th>'+
							'<th>'+
								'星期三' +
							'</th>'+
							'<th>'+
								'星期四' +
							'</th>'+
							'<th>'+
								'星期五' +
							'</th>'+
						'</tr>';
				
				for(var i=0;i<timeData.length;i++){
					idNum = count+i;
					if(idNum < 10){
						idNum = ("0"+idNum);
					}
					html += '<tr align="center" style="height:45px;">'+
							'<th class="jc">'+
								timeData[i].节次 +
							'</th>'+
							'<td id="01'+idNum+'" class="pkxx" title="">'+
							'</td>'+
							'<td id="02'+idNum+'" class="pkxx" title="">'+
							'</td>'+
							'<td id="03'+idNum+'" class="pkxx" title="">'+
							'</td>'+
							'<td id="04'+idNum+'" class="pkxx" title="">'+
							'</td>'+
							'<td id="05'+idNum+'" class="pkxx" title="">'+
							'</td>'+
						'</tr>';
				}
				$('#content').html(html);
				$.parser.parse(('#content'));
				if(classId!=""){
					if(gpjpData.length > 0){
						for(var j=0;j<gpjpData.length;j++){
							if(gpjpData[j].类型==2){
								$('#'+gpjpData[j].时间序列+'').html(gpjpData[j].课程名称+"<br />"+gpjpData[j].行政班名称);
								var pkxx=gpjpData[j].课程名称;
								var bjxx=gpjpData[j].行政班名称;
								var cdxx=gpjpData[j].预设场地名称;
								var skzc=gpjpData[j].授课周次;
								pkxx=pkxx.replace(new RegExp('&amp;','gm'),'&');
								bjxx=bjxx.replace(new RegExp('&amp;','gm'),'&');
								cdxx=cdxx.replace(new RegExp('&amp;','gm'),'&');
								skzc=skzc.replace(new RegExp('&amp;','gm'),'&');
								$('#'+gpjpData[j].时间序列+'').attr('title',(pkxx+"\r\n"+bjxx+"\r\n"+cdxx+"\r\n"+skzc));
							}
							
							if(gpjpData[j].类型==3){
								$('#'+gpjpData[j].时间序列+'').html('禁排');
								//$("#content td[id='"+gpjpData[j].时间序列+"']").attr('title',("此位置场地禁排 "));
							}
						}
					}else{
						
					}
				}
				editTable();
			}else{
				$('#setTime').hide();
			}
		}
		
		function editTable(){
			$("#content td").click(function(){ 
				if(paike==1){
					alertMsg("排课截止日期已过，不能进行排课操作");
					return;
				}
				var idVal = $(this).attr("id");
				var fir = idVal.substr(0, 2);
				var sec = idVal.substr(2, 1);
				var thi = idVal.substr(3, 1);
				var kcjsn=$('#'+idVal).html();
				
				if(iKeyCode==""){
					alertMsg("请先选择上方列表的“科目名称”以及“任课教师”");
					return;
				}
						
				//判断公共课是否确认，如已确认不允许修改公共课授课计划
				if(kclx=="01"){ 	
					$.ajax({
					   type: "POST",
					   url: '<%=request.getContextPath()%>/Svl_Skjh',
					   data: "active=checkGGK&GS_XNXQBM=" + (xnxqVal+jxxzVal) ,
					   dataType: 'json',
					   asy:false,
					   success: function(datas){
		                    var data = datas[0];
							if(datas[0].MSG=="0"){//公共课未确认
								//alert($('#'+idVal).attr("id"));
								kcjsn=kcjsn.replace(new RegExp('&amp;','gm'),'&');
								//检查可以是添加还是删除，及可以排的课数量
								$.ajax({
									type:'post',
									url:"<%=request.getContextPath()%>/Svl_Bjgpjp",
									data:"active=checkRec&SKJHMXBH="+iKeyCode+"&idVal="+idVal,
									dataType:'json',
									asy:false,
									success:function(datas){
										checkrec=datas[0].msg;
										lcs=datas[0].lcs;
										aod=datas[0].aod;
										if((kcmc+jsxm)==""){
											alertMsg("请先选择上方列表的“科目名称”以及“任课教师”");
											return;
										}else{
											//alert($('#'+idVal).html()+"|"+(kcmc+jsxm));
											if($('#'+idVal).html()=="禁排"){
												return;
											}
											if(jsxm=="请选择"){
												alertMsg("教师不能为请选择，请修改授课计划！");
												return;
											}
											
											var idValcon="";
											if(aod==1){//删除
												delinfo = $('#'+idVal).html();
												idValcon="'"+idVal+"'";
												delRec(idValcon);
												if(checkrec!="minus"){
													$('#'+idVal).html('');
												}
											}else{//添加
			
												if($('#'+idVal).css("background-color")!="#ffffff"&&$('#'+idVal).css("background-color")!="rgb(255, 255, 255)"&&$('#'+idVal).css("background-color")!="transparent"){//背景不是白色
													if(wpjs==0){
																	
													}else{															
														if(lj>1&&((lc-lcs)>0||lc==0)){
															gpjs=0;
															var idValcon="";
															if(checkrec!="no"){
																var kpjs=parseInt(checkrec);//可以排的节数
																if(kpjs>lj){
																	kpjs=lj;
																}
																			
																for(var i=0;i<kpjs;i++){
																	if(parseInt(sec)==1){//10+
																		if($('#'+fir+sec+(parseInt(thi)+i)+'').css("background-color")!="#ffffff"&&$('#'+fir+sec+(parseInt(thi)+i)).css("background-color")!="rgb(255, 255, 255)"&&$('#'+fir+sec+(parseInt(thi)+i)).css("background-color")!="transparent"&&$('#'+fir+sec+(parseInt(thi)+i)).css("background-color")!=undefined){
																			for(var i=0;i<kpjs;i++){
																				$('#'+fir+sec+(parseInt(thi)+i)+'').html("");
																			}
																			gpjs=0;
																			idValcon="";
																			break;
																		}else{ 
																			idValcon+="'"+fir+sec+(parseInt(thi)+i)+"',";
																			gpjs++;
																		}		
																	}else{//1-9
																		if((parseInt(thi)+i)>10){ 
																			if($('#'+fir+(parseInt(thi)+i)+'').css("background-color")!="#ffffff"&&$('#'+fir+(parseInt(thi)+i)).css("background-color")!="rgb(255, 255, 255)"&&$('#'+fir+(parseInt(thi)+i)).css("background-color")!="transparent"&&$('#'+fir+(parseInt(thi)+i)).css("background-color")!=undefined){
																				idValcon+="'"+fir+(parseInt(thi)+i)+"',";
																				gpjs++;
																			}else{		
																				gpjs=0;
																				idValcon="";
																				break;
																			}
																		}else{
																			if($('#'+fir+sec+(parseInt(thi)+i)+'').css("background-color")!="#ffffff"&&$('#'+fir+sec+(parseInt(thi)+i)).css("background-color")!="rgb(255, 255, 255)"&&$('#'+fir+sec+(parseInt(thi)+i)).css("background-color")!="transparent"&&$('#'+fir+(parseInt(thi)+i)).css("background-color")!="undefined"){	
																				if(parseInt(thi)+i>9){
																					idValcon+="'"+fir+(parseInt(thi)+i)+"',";
																				}else{
																					idValcon+="'"+fir+"0"+(parseInt(thi)+i)+"',";
																				}
																				gpjs++;
																			}else{				
																				gpjs=0;
																				idValcon="";
																				break;
																			}
																		}						
																	}																
																}
																idValcon=idValcon.substring(0, idValcon.length-1);
															}
															if(gpjs==0){
																		
															}else{
																for(var i=0;i<kpjs;i++){
																	if((parseInt(thi)+i)>9){
																		$('#'+fir+(parseInt(thi)+i)+'').html(kcmc+jsxm);
																	}else{
																		$('#'+fir+sec+(parseInt(thi)+i)+'').html(kcmc+jsxm);
																	}
																				
																}
																//addRec(gpjs,idValcon);
																savepb(idValcon,"add");
																idValconTrans=idValcon;
															}								
														}else{	
															//if(parseInt(thi)>8||parseInt(sec)==1){
															if(1==2){
																				
															}else{
																	if(checkrec!="no"){
																		$('#'+idVal).html(kcmc+jsxm);	
																		//$('#'+idVal).attr('title','aa'); 	
																	}	
																	idValcon="'"+idVal+"'";
																	//addRec("1",idValcon);
																	gpjs=1;
																	savepb(idValcon,"add");
																	idValconTrans=idValcon;
															}
																				
														}
													}
												}
											}	
										}				
									}
								});	
							}else{
								alertMsg("公共课已确认，不能修改公共课固排信息");
								return;
							}	
					   }
					});
				}else{
					//alert($('#'+idVal).attr("id"));
					kcjsn=kcjsn.replace(new RegExp('&amp;','gm'),'&');
					//检查可以是添加还是删除，及可以排的课数量
					$.ajax({
						type:'post',
						url:"<%=request.getContextPath()%>/Svl_Bjgpjp",
						data:"active=checkRec&SKJHMXBH="+iKeyCode+"&idVal="+idVal,
						dataType:'json',
						asy:false,
						success:function(datas){
							checkrec=datas[0].msg;
							lcs=datas[0].lcs;
							aod=datas[0].aod;
							if((kcmc+jsxm)==""){
								alertMsg("请先选择上方列表的“科目名称”以及“任课教师”");
								return;
							}else{
								//alert($('#'+idVal).html()+"|"+(kcmc+jsxm));
								if($('#'+idVal).html()=="禁排"){
									return;
								}
								if(jsxm=="请选择"){
									alertMsg("教师不能为请选择，请修改授课计划！");
									return;
								}
								var idValcon="";
								if(aod==1){//删除
									delinfo = $('#'+idVal).html();
									idValcon="'"+idVal+"'";
									delRec(idValcon);
									if(checkrec!="minus"){
										$('#'+idVal).html('');
									}
								}else{//添加
										
									if($('#'+idVal).css("background-color")!="#ffffff"&&$('#'+idVal).css("background-color")!="rgb(255, 255, 255)"&&$('#'+idVal).css("background-color")!="transparent"){//背景不是白色
										if(wpjs==0){
														
										}else{															
											if(lj>1&&((lc-lcs)>0||lc==0)){
												gpjs=0;
												var idValcon="";
												if(checkrec!="no"){
													var kpjs=parseInt(checkrec);//可以排的节数
													if(kpjs>lj){
														kpjs=lj;
													}
																
													for(var i=0;i<kpjs;i++){
																	if(parseInt(sec)==1){//10+
																		if($('#'+fir+sec+(parseInt(thi)+i)+'').css("background-color")!="#ffffff"&&$('#'+fir+sec+(parseInt(thi)+i)).css("background-color")!="rgb(255, 255, 255)"&&$('#'+fir+sec+(parseInt(thi)+i)).css("background-color")!="transparent"&&$('#'+fir+sec+(parseInt(thi)+i)).css("background-color")!=undefined){
																			for(var i=0;i<kpjs;i++){
																				$('#'+fir+sec+(parseInt(thi)+i)+'').html("");
																			}
																			gpjs=0;
																			idValcon="";
																			break;
																		}else{ 
																			idValcon+="'"+fir+sec+(parseInt(thi)+i)+"',";
																			gpjs++;
																		}		
																	}else{//1-9
																		if((parseInt(thi)+i)>10){ 
																			if($('#'+fir+(parseInt(thi)+i)+'').css("background-color")!="#ffffff"&&$('#'+fir+(parseInt(thi)+i)).css("background-color")!="rgb(255, 255, 255)"&&$('#'+fir+(parseInt(thi)+i)).css("background-color")!="transparent"&&$('#'+fir+(parseInt(thi)+i)).css("background-color")!=undefined){
																				idValcon+="'"+fir+(parseInt(thi)+i)+"',";
																				gpjs++;
																			}else{		
																				gpjs=0;
																				idValcon="";
																				break;
																			}
																		}else{
																			if($('#'+fir+sec+(parseInt(thi)+i)+'').css("background-color")!="#ffffff"&&$('#'+fir+sec+(parseInt(thi)+i)).css("background-color")!="rgb(255, 255, 255)"&&$('#'+fir+sec+(parseInt(thi)+i)).css("background-color")!="transparent"&&$('#'+fir+(parseInt(thi)+i)).css("background-color")!="undefined"){	
																				if(parseInt(thi)+i>9){
																					idValcon+="'"+fir+(parseInt(thi)+i)+"',";
																				}else{
																					idValcon+="'"+fir+"0"+(parseInt(thi)+i)+"',";
																				}
																				gpjs++;
																			}else{				
																				gpjs=0;
																				idValcon="";
																				break;
																			}
																		}						
																	}																
													}
													idValcon=idValcon.substring(0, idValcon.length-1);
												}
												if(gpjs==0){
															
												}else{
													for(var i=0;i<kpjs;i++){
														if((parseInt(thi)+i)>9){
															$('#'+fir+(parseInt(thi)+i)+'').html(kcmc+jsxm);
														}else{
															$('#'+fir+sec+(parseInt(thi)+i)+'').html(kcmc+jsxm);
														}
																	
													}
													//addRec(gpjs,idValcon);
													savepb(idValcon,"add");
													idValconTrans=idValcon;
												}								
											}else{	
												//if(parseInt(thi)>8||parseInt(sec)==1){
												if(1==2){
																	
												}else{
														if(checkrec!="no"){
															$('#'+idVal).html(kcmc+jsxm);	
															//$('#'+idVal).attr('title','aa'); 	
														}	
														idValcon="'"+idVal+"'";
														//addRec("1",idValcon);
														gpjs=1;
														savepb(idValcon,"add");
														idValconTrans=idValcon;
												}
																	
											}
										}
									}
								}	
							}				
						}
					});	
				}
			}); 
		}
		
		window.onload = function(){
			//去掉默认的contextmenu事件，否则会和右键事件同时出现。
			document.oncontextmenu = function(e){
				return false;
			};
			document.getElementById("content").onmousedown = function(){
				if(paike==1){
					return;
				}
				
				if(classId==""){//场地编号
					return;
				}
				
				if(event.button ==2){
					var td = event.srcElement;
					var td1 = ($(td).html()).substr(0, 2);
					if($(td).html()=="星期一"||$(td).html()=="星期二"||$(td).html()=="星期三"||$(td).html()=="星期四"||$(td).html()=="星期五"||$(td).html()==className||td1=="上午"||td1=="下午"||td1=="晚上"){
						return;
					}else{
						var idValcon="'"+td.id+"'";
						if($(td).html()==""){
							$(td).html("禁排");
							savepb(idValcon,"addjin");
						}else{
							if($(td).html()=="禁排"){
								$(td).html('');
								savepb(idValcon,"deljin");
							}else{
// 								delinfo = $(td).html();
// 								delRec(idValcon);
// 								$(td).html('');
							}
						}
					}
				}
			};
		};
		
		function addRec(idValcon){
			$.ajax({
				type:'post',
				url:"<%=request.getContextPath()%>/Svl_Cdgpjp",
				data:"active=add&SKJHMXBH="+iKeyCode+"&gpjs="+gpjs,
				dataType:'json',
				asy:false,
				success:function(datas){
// 					if(datas[0].msg=="1"){
// 						savepb(idValcon,"add");
// 					}
					wpjs=wpjs-gpjs;
					initGridData(classId,className,xnxqVal,jxxzVal,"2");
				}
			});
		}
		
		function delRec(idValcon){
			delinfo=delinfo.replace(new RegExp('&amp;','gm'),'&');
			var url="active=del&QCXX=" + encodeURIComponent(delinfo) + '&XNXQ=' + xnxqVal + '&JXXZ=' + jxxzVal + '&XZBDM=' + classId + '&SKJHMXBH=' + iKeyCode + '&idValcon='+idValcon;
			$.ajax({
				type:'post',
				url:"<%=request.getContextPath()%>/Svl_Cdgpjp",
				data:url,//"active=del&QCXX=" + delinfo + '&XNXQ=' + xnxqVal + '&JXXZ=' + jxxzVal + '&XZBDM=' + classId + '&SKJHMXBH=' + iKeyCode,
				dataType:'json',
				asy:false,
				success:function(datas){
					if(datas[0].msg=="1"){
						savepb(idValcon,"del");
						wpjs=wpjs+parseInt(datas[0].msg2);
					}
				}
			});
		}

		
		function savepb(idValcon,savetype){
			$("#active").val("save");
			$('#GG_XZBDM').val(classdm);
			$('#JSBH').val(classId);
			$('#JSXM').val(className);
			$('#GG_XNXQBM').val(xnxqVal+jxxzVal);
			$('#GG_SKJHMXBH').val(iKeyCode);
			$('#divPageMask2').show();
			
			//取所有content值，保存不为空的
		    var tab=document.getElementById("content");
    		var rows=tab.rows;
    		var b="";
    		for(var i=1;i<rows.length;i++){
	        	for(var j=1;j<rows[i].cells.length;j++){
	        		if((rows[i].cells[j].innerHTML)!=""){
	        			b+=rows[i].cells[j].id+",";
	        		}
	        	}
	        }
	        b=b.substring(0,b.length-1);
		    $('#GG_SJXL').val(b);
		    
		    $('#SJXL').val(idValcon);
		    $('#CDYQ').val(cdyq);
		    $('#CDMC').val(cdmc);
		    $('#SKZC').val(skzcxq);
		    $('#SAVETYPE').val(savetype);
		    
		    var c = $('.pkxx');
		    var d="";
			for(var i=0;i<c.length;i++){
				d+=$(c[i]).html()+",";
		    } 
		    d=d.substring(0,d.length-1);
		  	$('#KCJS').val(d);
		  	
			$('#fm').submit();
		}
		
		//fm提交事件
		$('#fm').form({
			url:'<%=request.getContextPath()%>/Svl_Cdgpjp',
			onSubmit:function(){
				
			},
			//提交成功
			success:function(datas){
				if($('#active').val()=="save"){
					var json = eval("("+datas+")");
					if(json[0].msg=="没有可以使用的教室"){
						alertMsg(json[0].msg);
						initGridData(classId,className,xnxqVal,jxxzVal,"2");
						$('#divPageMask2').hide();
						$('#divPageMask3').hide();
					}else{
						if($('#SAVETYPE').val()=="add"){
							addRec(idValconTrans);
						}
					}
					//showMsg(json[0].msg);
					initGridData(classId,className,xnxqVal,jxxzVal,"2");
				}
			}
		});
		
		function initDialog(){
			$('#xxkImport').dialog({   
				title: '选修课导入',
				width: 350,//宽度设置   
				height: 170,//高度设置 
				modal:true,
				closed: true,   
				cache: false, 
				draggable:false,//是否可移动dialog框设置
				toolbar:[{
					//保存编辑
					text:'保存',
					iconCls:'icon-save',
					handler:function(){
						//传入save值进入doToolbar方法，用于保存
						doToolbar('savexxk');
					}
				}],
				//打开事件
				onOpen:function(data){},
				//读取事件
				onLoad:function(data){},
				//关闭事件
				onClose:function(data){
					emptyDialog();
				}
			});
		}
	</script>
</html>
