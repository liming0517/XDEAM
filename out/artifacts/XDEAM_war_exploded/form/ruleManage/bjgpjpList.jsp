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
			sAuth += "@"+MyTools.StrFiltr(v.get(i))+"@,";
		}
		if(sAuth!=""){
			sAuth=sAuth.substring(0, sAuth.length()-1);
		}	
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
			
			#xlTime9{
				width:100%;
				border-right:1px solid #99bbe8;
			}
			#xlTime9{
				width:99.9%;
				border-top:1px solid #99bbe8;
			}
			#xlTime9 td{
				width:300px;
				border-left:1px solid #99bbe8;
				border-bottom:1px solid #99bbe8;
				text-align:center;
				empty-cells:show;
				font-size:11;
			}
			#xlTime9 th{
				width:300px;
				border-left:1px solid #99bbe8;
				border-bottom:1px solid #99bbe8;
				text-align:center;
				empty-cells:show;
			}
			#divPageMask2{background-color:#D2E0F2; filter:alpha(opacity=50);left:0px;top:0px;z-index:100;}
			#divPageMask3{background-color:#D2E0F2; filter:alpha(opacity=50);left:0px;top:0px;z-index:100;}
			
		</style>
	</head>
	
	<body class="easyui-layout" onkeydown="myKeyDown()">
		
		<div data-options="region:'west'" title="" style = "width:28%;">
			<div class="easyui-layout" fit="true">
				<div region="north" split="true" border="false" style="height:86px;"> 
					<table  class = "tablestyle" width = "100%">
						<tr>
							<td>
<!-- 								<a href="#" onclick="doToolbar(this.id);" id="import"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-site'">选修课导入</a> -->
								<a href="#" onclick="doToolbar(this.id);" id="zongbiao"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-site'">禁排总表</a>
<!-- 								<a href="#" onclick="doToolbar(this.id);" id="notcourse" class="easyui-linkbutton" data-options="iconCls:'icon-collection_edit',plain:true">未排课程</a> -->
							</td>
						</tr>
					</table>
					<table  class = "tablestyle" width = "100%">
						<tr>
							<td class="titlestyle">学年学期</td>
							<td>
								<select id="XNXQ" name="XNXQ" class="easyui-combobox" style="width:180px;"></select>
							</td>
							<!-- <td rowspan="2">
								<a href="#" onclick="doToolbar(this.id);" id="que"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-search'">查询</a>
							</td> -->
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
 	
		
		<div data-options="region:'center'" style="overflow-x:hidden;">
			<div class="easyui-layout" fit="true">
				<div region="north" split="true" border="false" style="height:130px;width:100%;"> 
					<table id='list' style="width:100%;height:20%;">
					</table>
				</div> 
				<div region="center" border="false"> 
					<div id="setTime" style="overflow:hidden;">
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
						</form>
					</div>
					
					<%-- 遮罩层 --%>
			    	<div id="divPageMask2" style="position:absolute;top:0px;left:0px;width:100%;height:100%;">
			    	</div>
					
					<div id="bjzb" style="position:absolue;width:100%;height:100%;border:1px solid;">
<!-- 				<iframe id="gpiframe" name="gpiframe" src='<%= request.getContextPath()%>/form/ruleManage/bjgpjpList_zb.jsp' style="width:100%;height:100%;" frameborder="0" scrolling="no"></iframe> -->
						<%-- 遮罩层 --%>
		    			<div id="divPageMask3" style="position:absolute;top:0px;left:0px;width:100%;height:100%;">
		    			</div>
						
						<div id="setTime9" style="overflow:hidden;">
							<form id='fm9' method='post' style="margin: 0px;padding-top: 3px;">
								<table id="xlTime9" cellspacing="0" cellpadding="0" style="height:74%;border-collapse:collapse;"> 
									<tbody id="content9"> 
										 
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
								<input type="hidden" id="SAVETYPE" name="SAVETYPE"/>
								<input type="hidden" id="GG_SKJHMXBH" name="GG_SKJHMXBH"/>
							</form>
						</div>
					</div>
				</div> 
			</div>
		</div>
		
		<div id="notCourseDiv" title="编辑">
			<table width = "100%">
				<tr><a id="deleteWeipai" onclick="doToolbar(this.id)" class="easyui-linkbutton" href="javascript:void(0);" data-options="plain:true,iconCls:'icon-cancel'">删除</a></tr>
			</table>
			<table id="ncourtable" class = "tablestyle" width="886px">
			</table>
		</div>
		
		<!-- 选修课导入 -->
		<div id="xxkImport" style="overflow:hidden;display:none;" >
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
<!-- 					<tr> -->
<!-- 						<td colspan="2" align="center"> -->
<!-- 							<input id="Select" name="Select" class="easyui-textbox" type='button' style="text-align:center;width:80px;" value='[附件选择]'  onclick='selectfile()'/> -->
<!-- 						</td> -->
<!-- 					</tr> -->
<!-- 					<tr> -->
<!-- 						<td colspan="2" align="center"> -->
<!-- 							<input id="Upload" name="Upload" class="easyui-textbox" type='button' style="text-align:center;width:80px;" value='[上传]'  onclick='show()'/> -->
<!-- 						</td> -->
<!-- 					</tr> -->
				</table>
				<input type="hidden" id="active" name="active"/>
			</form>
		</div>
		
	</body>
	<script type="text/javascript">
		var iUSERCODE="<%=MyTools.getSessionUserCode(request)%>";
		var sAuth="<%=sAuth%>";  //角色权限
		var jxzgxz="<%=MyTools.getProp(request, "Base.jxzgxz")%>";
		var qxjdzr="<%=MyTools.getProp(request, "Base.qxjdzr")%>";
		var qxjwgl="<%=MyTools.getProp(request, "Base.qxjwgl")%>";
		var classId = "";//班级号
		var className = "";//班级名称
		var parentId = "";//父节点

		var xnxq = "";//学年学期下拉框数据
		var jxxz = "";//教学性质下拉框数据
		var xnxqVal = "";//当前学年学期数据
		var jxxzVal = "";//当前教学性质数据
		var pageNum = 1;   //datagrid初始当前页数
		var pageSize = 20; //datagrid初始页内行数
		var weeks="";//总周次
		var paike="0";//排课状态
		var lastIndex=-1;//上次选中的行
		
		var iKeyCode = "";//授课计划明细编号
		//var xkmc = "";//学科名称下拉框数据
		//var rkjs = "";//任课教师下拉框数据
		var kcmc = "";//课程名称
		var jsbh = "";//教师教师
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

		var savexqbh1="";//保存传递过来的学期编码
		var savexqbh2="";//保存传递过来的学期编码
		var paike="0";//排课状态
		var gpjs=0;
		var idValconTrans="";
		var kclx=""; //课程类型
		var wpjs=0;//未排节数
		//var saveType = "";//判断打开窗口的操作（new or edit）
		var delweipaiarray=new Array();//未排课程
		
		$(document).ready(function(){
			loadXQWEEK();//初始化下拉框	
			//initDialog();
			$('#bjzb').hide();
			
			if(sAuth.indexOf("@01@")>-1||sAuth.indexOf(jxzgxz)>-1||sAuth.indexOf(qxjdzr)>-1||sAuth.indexOf(qxjwgl)>-1){
				$("#zongbiao").linkbutton("enable");
			}else{
				$("#zongbiao").linkbutton("disable");
			}

		});
		
		//获取学年学期combobox，学期周数
		function loadXQWEEK(){
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_Bjgpjp',
				data : 'active=initData&page=' + pageNum + '&rows=' + pageSize+'&iUSERCODE='+iUSERCODE,
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
		function loadData(xnxqVal,jxxzVal){
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_Bjgpjp',
				data : 'active=query&page=' + pageNum + '&rows=' + pageSize + '&iUSERCODE='+iUSERCODE+
					'&JXXZ=' + jxxzVal + '&XNXQ=' + xnxqVal + '&termid='+(xnxqVal+jxxzVal),
				dataType:"json",
				success : function(data) {
					weeks=data[0].MSG;
					initData(weeks);//页面初始化加载数据
					treegrid();
				}
			});
		}
		
		//页面初始化加载数据
		function initData(weeks){
			initGridData("",xnxqVal,jxxzVal,"1");//页面初始化加载数据
		}
		
		function treegrid(){
			$('#classTree').tree({
				checkbox: false,
				url:'<%=request.getContextPath()%>/Svl_Skjh?active=queryTree&AUTH='+sAuth+'&level=0&parentId=""'+'&iUSERCODE='+iUSERCODE+'&GS_XNXQBM='+(xnxqVal+jxxzVal),
				onClick:function(node){
					//判断点击的是不是班级
			    	if($('#classTree').tree('getParent', node.target) != null){
			    		if(lastIndex!=node.id){
							classId=node.id;
							lastIndex=node.id;
							initGridData(classId,xnxqVal,jxxzVal,"1");//点击后刷新right页面取值结果	
						}
						parentId=node.id;
			    	}else{
			    		initGridData("",xnxqVal,jxxzVal,"1");//页面初始化加载数据
			    		parentId="";
			    		lastIndex="";
			    	}					
				},
// 				onBeforeLoad:function(row,param){     //分层显示treegrid
// 				},
				onBeforeExpand:function(node,param){
			  		$('#classTree').tree('options').url='<%=request.getContextPath()%>/Svl_Skjh?active=queryTree&level=1&parentId='+node.id+'&iUSERCODE='+iUSERCODE+'&GS_XNXQBM='+(xnxqVal+jxxzVal);
				},
				onLoadSuccess:function(data){
					$('#classTree').show();
					xnxqVal = $('#XNXQ').combobox('getValue');
					jxxzVal = $('#JXXZ').combobox('getValue');
					
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
						//$('#zongbiao').linkbutton('enable');
					}else{
						$('#import').linkbutton('disable');
						//$('#zongbiao').linkbutton('disable');
					}
				}
			});
		}	
		
		//工具按钮
		function doToolbar(iToolbar){
			//查询
			if(iToolbar == 'que'){
				xnxqVal = $('#XNXQ').combobox('getValue');
				jxxzVal = $('#JXXZ').combobox('getValue');
				jsxm="";
				skzcxq="";
				cdyq="";
				iKeyCode="";
				classId="";
				jsbh="";
				treegrid();
				initGridData("",xnxqVal,jxxzVal,"2");//页面初始化加载数据	
			}
			if(iToolbar == "import"){
				$('#xxkImport').show();
				$('#xxkImport').dialog('open');
			}
			if(iToolbar == "savexxk"){
				
			}
			if(iToolbar == "zongbiao"){
				openbjzb();
			}
			//未排课程
			if(iToolbar == "notcourse"){
				loadGridCOUR();
			}
			//删除未排课程
			if(iToolbar == "deleteWeipai"){
				if(delweipaiarray.length==0){
					alertMsg("请选择要删除的课程");
					return;
				}
				var weipaiarray="";
				for(var i=0;i<delweipaiarray.length;i++){
					weipaiarray=delweipaiarray[i]+",";
				}
				
				$.ajax({
					type: "POST",
					url: '<%=request.getContextPath()%>/Svl_Bjgpjp',
					data: "active=deleteWeipai&weipaiarray=" + weipaiarray,
					dataType: 'json',
					success: function(datas){
						showMsg(datas[0].MSG);
						loadGridCOUR();
					}
				});
			}
		}
		
		//----------------------------------------------------------------
		
		function myKeyDown(){
			var k=window.event.keyCode;
			if (8 == k){
				event.keyCode=0;//取消按键操作
			}
		}	
		
				
		//页面初始化加载数据
		function initGridData(classId,xnxqVal,jxxzVal,n){ 
			checkpaike();
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_Bjgpjp',
				data : 'active=initGridData&page=' + pageNum + '&rows=' + pageSize + '&XZBDM=' + classId + '&XNXQ=' + xnxqVal + '&JXXZ=' + jxxzVal,
				dataType:"json",
				success : function(data) { 
					//xkmc = data[0].xkmcData;//获取学科名称下拉框数据
					//rkjs = data[0].rkjsData;//获取任课教师下拉框数据		
					if(n!="3"){
						loadGrid(data[0].listData);//加载特殊规则列表
					}			
					loadTime(data[0].timeData,data[0].gpjpData);
					loadTime9(data[0].timeData,data[0].gpjpData2);
					//initCombobox(xkmc,rkjs);//初始化下拉框
			
					if(n=="1"){//第一次加载
						iKeyCode = "";
						$('#list').datagrid("unselectRow", lastIndex);
					}else if(iKeyCode!=""){
						checkTeaCls(jsxm,skzcxq,weeks,cdyq,iKeyCode,xnxqVal+jxxzVal,classId,jsbh);
					}
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
					{field:'授课教师姓名',title:'任课教师',width:100},
					{field:'GS_CDMC',title:'场地要求',width:100},
					{field:'GS_GPYPJS',title:'已排节数',width:50},
					{field:'GS_MPJS',title:'未排节数',width:50},
					{field:'GS_SKZCXQ',title:'授课周次详情',width:50,
						formatter:function(value,rec){
							var skzcxq2=rec.GS_SKZCXQ;
							skzcxq2=skzcxq2.replace(new RegExp('odd','gm'),'单周');
							skzcxq2=skzcxq2.replace(new RegExp('even','gm'),'双周');
							return skzcxq2;
						}
					}
				]],
				onClickRow:function(rowIndex, rowData){
					lastIndex = rowIndex;
					iKeyCode = rowData.授课计划明细编号;
					//iKeyCodeZb = rowData.授课计划主表编号;
					kcmc = rowData.课程名称;
					jsbh = rowData.GS_SKJSXM;
					jsxm = rowData.授课教师姓名;
					skzcxq = rowData.GS_SKZCXQ;
					lj = rowData.GS_LJ;
					lc = rowData.GS_LC;
					cdyq=rowData.GS_CDYQ;
					cdmc=rowData.GS_CDMC;
					kclx=rowData.课程类型;
					wpjs=rowData.GS_MPJS;
					
					initGridData(classId,xnxqVal,jxxzVal,"3");	
					//if(iKeyCode!=""){	
					//	checkTeaCls(jsxm,skzcxq,weeks,cdyq,iKeyCode,xnxqVal+jxxzVal,classId,jsbh);
					//}
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
					setTimeout("$('#divPageMask2').hide()",1000);
					setTimeout("$('#divPageMask3').hide()",1000);
				},
				onLoadError:function(none){
					
				}
			});
			
			$("#list").datagrid("getPager").pagination({ 
				total:listData.total, 
				onSelectPage:function (pageNo, pageSize_1) { 
					pageNum = pageNo;
					pageSize = pageSize_1;
					loadData("","");
				} 
			});
		}
		
		//检查可以排的格子
		var clearjinpai="";
		function checkTeaCls(jsxm,skzcxq,weeks,cdyq,iKeyCode,termid,classId,jsbh){	 
					$('#divPageMask2').show();
// 					for(var i=0;i<clearjinpai.length;i=i+3){	
// 						$("#content td[id='"+clearjinpai[i]+"']").html('');			
// 						$("#content td[id='"+clearjinpai[i]+"']").attr('title','');
// 					}
					
					$.ajax({
						type:'post',
						url:"<%=request.getContextPath()%>/Svl_Bjgpjp",
						data:"active=checkTeaCls&jsxm="+encodeURIComponent(jsxm)+"&skzcxq="+encodeURIComponent(skzcxq)+"&weeks="+encodeURIComponent(weeks)+"&cdyq="+encodeURIComponent(cdyq)+"&iKeyCode="+iKeyCode+"&termid="+termid+"&classId="+encodeURIComponent(classId)+"&jsbh="+encodeURIComponent(jsbh),
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
										kyjs="可用教室："+kyjs.substring(1,kyjs.length-1);
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
								$("#content td[id='"+gpjpData[j].时间序列+"']").html(gpjpData[j].课程名称+"<br />"+gpjpData[j].授课教师姓名);
								var pkxx=gpjpData[j].课程名称;
								var jsxx=gpjpData[j].授课教师姓名;
								var cdxx=gpjpData[j].场地名称;
								var skzc=gpjpData[j].授课周次;
								pkxx=pkxx.replace(new RegExp('&amp;','gm'),'&');
								jsxx=jsxx.replace(new RegExp('&amp;','gm'),'&');
								cdxx=cdxx.replace(new RegExp('&amp;','gm'),'&');
								skzc=skzc.replace(new RegExp('&amp;','gm'),'&');
								$("#content td[id='"+gpjpData[j].时间序列+"']").attr('title',(pkxx+"\r\n"+jsxx+"\r\n"+cdxx+"\r\n"+skzc));
							}
							if(gpjpData[j].类型==3){
								$("#content td[id='"+gpjpData[j].时间序列+"']").html('禁排');	
								//$("#content td[id='"+gpjpData[j].时间序列+"']").attr('title',("此位置班级禁排 "));
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
													//$('#'+idVal).html('');
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
										//$('#'+idVal).html('');
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
											}else{	//排1节
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
				
				if(classId==""){
					return;
				}
				
				if(event.button ==2){
					
					var td = event.srcElement;
					var td1 = ($(td).html()).substr(0, 2);
					if($(td).html()=="星期一"||$(td).html()=="星期二"||$(td).html()=="星期三"||$(td).html()=="星期四"||$(td).html()=="星期五"||($(td).html()==className&&className!="")||td1=="上午"||td1=="下午"||td1=="晚上"){
						return;
					}else{
						var idValcon="'"+td.id+"'";
						if($(td).html()==""){
							$(td).html("禁排");
							savepb(idValcon,"addjin");
						}else{
							if($(td).html()=="禁排"){
								$.ajax({
									type:'post',
									url:"<%=request.getContextPath()%>/Svl_Bjgpjp",
									data:"active=checkdeljin&SKJHMXBH="+iKeyCode+"&idVal="+idValcon,
									dataType:'json',
									asy:false,
									success:function(datas){
										if(datas[0].msg=="0"){//可以删除
											$(td).html('');
											savepb(idValcon,"deljin");
										}else{//是属于禁排总表的信息，不能删除
											alertMsg("该禁排由禁排总表设置，无法删除!");
										}
									}
								});
							}else{
// 								delinfo = $(td).html();
// 								delRec(idValcon);
// 								$(td).html('');
							}
						}
					}
				}
			};
			
			document.getElementById("content9").onmousedown = function(){
				var idVal = $(this).attr("id");
				if(event.button ==2){
				
					var td = event.srcElement;
					var td1 = ($(td).html()).substr(0, 2);
					if($(td).html()=="星期一"||$(td).html()=="星期二"||$(td).html()=="星期三"||$(td).html()=="星期四"||$(td).html()=="星期五"||($(td).html()==className&&className!="")||td1=="上午"||td1=="下午"||td1=="晚上"){
						return;
					}else{
						var idValcon="'"+td.id+"'";
						if($(td).html()==""){
							$.ajax({
								type:'post',
								url:"<%=request.getContextPath()%>/Svl_Bjgpjp",
								data:"active=checkAddAll&SKJHMXBH="+iKeyCode+"&idVal="+idValcon,
								dataType:'json',
								asy:false,
								success:function(datas){
									if(datas[0].msg=="nobjgp"){//该位置没有班级固排过
										$(td).html("禁排");
										savepb(idValcon,"addall");
									}else{
										var delinfo="";
										for(var i=0;i<datas.length;i++){
											delinfo+=datas[i].行政班名称+"，";
										}
										$.messager.confirm("提示",delinfo+"在此时间段已有固排课程，此操作将删除其相关的固排信息，是否确定？",function(t){
											if(t){
												$(td).html("禁排");
												savepb(idValcon,"addall");
											}
										});
									}
								}
							});
							
						}else{
							if($(td).html()=="禁排"){
								$(td).html('');
								savepb(idValcon,"delall");
							}else{
								delinfo = $(td).html();
								//delRec(idValcon);
								savepb(idValcon,"del");
								$(td).html('');
							}
						}
					}
				}
			};
		};
		
			
		//读取禁排总表数据
		function loadTime9(timeData,gpjpData){

			var count = 1;
			var idNum = "";
			if(timeData != ""){
				$('#setTime9').show();
				var html='';
				html += '<tr align="center" style="height:20px;">'+
							'<th>'+
								
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
				$('#content9').html(html);
				$.parser.parse(('#content9'));
				$.parser.parse(('#content9'));
					var tt=gpjpData.split(",");
					for(var j=0;j<tt.length;j++){
						$("#content9 td[id='"+tt[j]+"']").html('禁排');	
					}
				
				editTable9();
			}else{
				$('#setTime9').hide();
			}
		}
		
		function editTable9(){
			$("#content9 td").click(function(){ 
				var idVal = $(this).attr("id");
				var fir = idVal.substr(0, 2);
				var sec = idVal.substr(2, 1);
				var thi = idVal.substr(3, 1);
				var kcjsn=$('#'+idVal).html();
				
				//alert($('#'+idVal).attr("id"));
				kcjsn=kcjsn.replace(new RegExp('&amp;','gm'),'&');
				//检查可以是添加还是删除，及可以排的课数量
	
			}); 
		}
		
		
		
		function addRec(idValcon){ 
			$.ajax({
				type:'post',
				url:"<%=request.getContextPath()%>/Svl_Bjgpjp",
				data:"active=add&SKJHMXBH="+iKeyCode+"&gpjs="+gpjs,
				dataType:'json',
				asy:false,
				success:function(datas){
// 					if(datas[0].msg=="1"){
// 						savepb(idValcon,"add");
// 					}
					wpjs=wpjs-gpjs;
					initGridData(classId,xnxqVal,jxxzVal,"2");
				}
			});
		}
		
		function delRec(idValcon){
			delinfo=delinfo.replace(new RegExp('&amp;','gm'),'&');
			var url="active=del&QCXX=" + encodeURIComponent(delinfo) + '&XNXQ=' + xnxqVal + '&JXXZ=' + jxxzVal + '&XZBDM=' + classId + '&SKJHMXBH=' + iKeyCode + '&idValcon='+idValcon;
			$.ajax({
				type:'post',
				url:"<%=request.getContextPath()%>/Svl_Bjgpjp",
				data:url,//"active=del&QCXX=" + delinfo + '&XNXQ=' + xnxqVal + '&JXXZ=' + jxxzVal + '&XZBDM=' + classId + '&SKJHMXBH=' + iKeyCode,
				dataType:'json',
				asy:false,
				success:function(datas){
					if(datas[0].msg=="1"){//删除成功
						savepb(idValcon,"del");
						wpjs=wpjs+parseInt(datas[0].msg2);
					}
				}
			});
		}
		
		
		function openbjzb(){
			$('#bjzb').show();
			$('#bjzb').dialog({
			    title : '禁排总表',
				width : 800,
				height: 514,
				modal : true,
				closed : true,
				onOpen : function(data) {
					
				},
				onLoad : function(data) {
				},
				onClose : function(data) {
					
				}
			});
			$('#bjzb').dialog("open");
		}
		
		function savepb(idValcon,savetype){

			$("#active").val("save");
			$('#GG_XZBDM').val(classId);
			$('#GG_XNXQBM').val(xnxqVal+jxxzVal);
			$('#GG_SKJHMXBH').val(iKeyCode);
			if(savetype=="addall"||savetype=="delall"){
				$('#divPageMask3').show();
			}else{
				$('#divPageMask2').show();
			}
	
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
			url:'<%=request.getContextPath()%>/Svl_Bjgpjp',
			onSubmit:function(){
				
			},
			//提交成功
			success:function(datas){
				if($('#active').val()=="save"){ 
					var json = eval("("+datas+")");
					if(json[0].msg=="没有可以使用的教室"){
						alertMsg(json[0].msg);
						initGridData(classId,xnxqVal,jxxzVal,"2");
						$('#divPageMask2').hide();
						$('#divPageMask3').hide();
					}else{
						if($('#SAVETYPE').val()=="add"){
							addRec(idValconTrans);
						}else{
							initGridData(classId,xnxqVal,jxxzVal,"2");
							$('#divPageMask2').hide();
							$('#divPageMask3').hide();
						}
					}
					
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
			
			$('#notCourseDiv').dialog({   
				title: '未排课程',   
				width: 900,//宽度设置   
				height: 500,//高度设置 
				modal:true,
				closed: true,   
				cache: false, 
				draggable:true,//是否可移动dialog框设置
				//读取事件
				onLoad:function(data){},
				//关闭事件
				onClose:function(data){
					//clsinfoidarray.splice(0,teainfoidarray.length);
					//clsinfoarray.splice(0,teainfoarray.length);
				}
			});
		}
		
		function loadGridCOUR(){
			isLoad = true;
				$('#ncourtable').datagrid({
					url: '<%=request.getContextPath()%>/Svl_Bjgpjp',
		 			queryParams: {"active":"showNotCourse","GG_XNXQBM":xnxqVal+jxxzVal},
					loadMsg : "信息加载中请稍后!",//载入时信息
					height:433,
					rownumbers: true,
					animate:true,
					striped : true,//隔行变色
					pageSize : pageSize,//每页记录数
					singleSelect : false,//单选模式
					pageNumber : pageNum,//当前页码
					pagination:false,
					fit:false,
					fitColumns: true,//设置边距
					columns:[[
						{field:'ck',checkbox:true},
						{field:'学年学期编码',title:'学年学期',width:70,align:'center',
							formatter:function(value,rec){
								var xnxqbm=rec.学年学期编码;
								var xnxq=xnxqbm.substring(0,4)+"学年第"+xnxqbm.substring(4,5)+"学期";
								return xnxq;
							}
						},
						{field:'课程代码',title:'课程代码',width:60,align:'center'},
						{field:'课程名称',title:'课程名称',width:90,align:'center'},
						{field:'行政班名称',title:'行政班名称',width:120,align:'center'},
						{field:'上课周期',title:'上课周期',width:40,align:'center'}
					]],
					onSelect:function(rowIndex,rowData){
						if(isLoad == false){
							delweipaiarray.push(rowData.授课计划明细编号);
						}
					},
					onUnselect:function(rowIndex,rowData){
						$.each(delweipaiarray, function(key,value){
							if(value == rowData.授课计划明细编号){
								delweipaiarray.splice(key, 1);
							}
						});
					},
					onLoadSuccess: function(data){
						$(".datagrid-header-check").html('&nbsp;');//隐藏全选
						isLoad = false;
					},
					onLoadError:function(none){
						
					}
				});
				$('#notCourseDiv').dialog("open");
		}
		
	</script>
</html>
