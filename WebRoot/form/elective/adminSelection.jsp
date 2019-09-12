<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%
	/**
		创建人：lupengfei
		Create date: 2016.02.04
		功能说明：用于设置课程信息
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
<%@ page import="com.pantech.base.common.tools.PublicTools"%>
<%
UserProfile up = new UserProfile(request,MyTools.getSessionUserCode(request)); 
String userName =up.getUserName();
String userCode =up.getUserCode();
%>
<html>
  <head>
	<title>学生选课</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/icon.css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
	
	 <style>
		#File1{
			height:26px;
			position:absolute;
			z-index:99;
			top:0;
			left:-170;
			opacity: 0; 
			filter: "alpha(opacity=0)"; 
			filter: alpha(opacity=0); 
			-moz-opacity: 0;
			cursor:pointer;
		}
		
		.maskStyle{
			width:100%;
			height:100%;
			overflow:hidden;
			display:none;
			background-color:#D2E0F2;
			filter:alpha(opacity=80);
			position:absolute;
			top:0px;
			left:0px;
			z-index:100;
		}
		#maskFont{
			 font-size:16px;
			 font-weight:bold;
			 color:#2B2B2B;
			 width:200px;
			 height:100%;
			 position:absolute;
			 top:50%;
			 left:50%;
			 margin-top:-10px;
			 margin-left:-80px;
		}
		#maskFont2{
			 font-size:16px;
			 font-weight:bold;
			 color:#2B2B2B;
			 width:200px;
			 height:100%;
			 position:absolute;
			 top:50%;
			 left:50%;
			 margin-top:-10px;
			 margin-left:-80px;
		}
  </style>
	
  </head>
<body  class="easyui-layout">
	<div id="north" region="north" title="课程信息" style="height:91px;" >
		<table>
 			<tr>
 				<td><a href="#" onclick="doToolbar(this.id)" id="export" class="easyui-linkbutton" plain="true" iconcls="icon-submit">导出开课情况表</a></td>
 				<td><a href="#" onclick="doToolbar(this.id)" id="exportStu" class="easyui-linkbutton" plain="true" iconcls="icon-submit">导出学生点名单</a></td>
<!-- 				<td><a href="#" onclick="doToolbar(this.id)" id="sourcebuzu" name="sourcebuzu" class="easyui-linkbutton" plain="true" iconcls="icon-pyxy">学分不足学生名单</a></td> -->
<!-- 				<td><a href="#" onclick="doToolbar(this.id)" id="delelecStu" name="sourcebuzu" class="easyui-linkbutton" plain="true" iconcls="icon-cancel">删除留级和不在校学生选课信息</a></td> -->
 			</tr>
 		</table>
		<table  class = "tablestyle" width = "100%">
				<tr>
					<td class="titlestyle">学年学期</td>
					<td >
						<select id="SC_XNXQ" name="SC_XNXQ" class="easyui-combobox" style="width:200px;"></select>
					</td>
					
					<td align="center" style="width:12%">
						<a href="#" class="easyui-linkbutton" id="searchElective" name="searchElective" iconCls="icon-search" plain="true" onClick="doToolbar(this.id)">查询</a>
					</td>	
				</tr>
		</table>
	</div>
	<div region="center">
	<%-- 遮罩层 --%>
    	<div id="divPageMask" class="maskStyle">
    		<div id="maskFont"></div>
    	</div>
		<table id="selectionList" style="width:100%;"></table>
	</div>
	
	<div id="tsc" title="编辑" style="overflow-x:hidden;">
			<table id="tsctable" class = "tablestyle" width="566px">
														
			</table>		
	</div>

	<div id="selectClass" title="选修班信息" style="width:100%;">
			<div style="height:27px;">
				<table width="100%" class="tablestyle">
				<tr>
					<a href="#" class="easyui-linkbutton" id="addStu" name="addStu" iconCls="icon-add" plain="true" onClick="doToolbar(this.id)">添加</a>
					<a href="#" class="easyui-linkbutton" id="changeClass" name="changeClass" iconCls="icon-reload" plain="true" onClick="doToolbar(this.id)">换班</a>
					<a href="#" class="easyui-linkbutton" id="delStu" name="delStu" iconCls="icon-cancel" plain="true" onClick="doToolbar(this.id)">删除</a>
				</tr>
				</table>
			</div>
			<div style="">
				<table id="classInfo" class = "tablestyle" width="100%"></table>
			</div>	
	</div>
	
	<div id="student" title="编辑" style="width:100%;">
			<div style="height:56px;">
				<table width="100%" class="tablestyle">
				<tr>
					<a href="#" class="easyui-linkbutton" id="submitStu" name="submitStu" iconCls="icon-submit" plain="true" onClick="doToolbar(this.id)">确定</a>
				</tr>
				<tr>
					<td width="10%" class="titlestyle">学号</td>
					<td  width="20%">
						<input style='width:200px;' class='easyui-textbox' id='ic_stuId' name='ic_stuId'/>
					</td>
					<td width="10%" class="titlestyle">姓名</td>
					<td  width="20%">
						<input style='width:200px;' class='easyui-textbox' id='ic_stuName' name='ic_stuName'/>
					</td>							
					<td align="center" style='width:12%'>
						<a href="#" class="easyui-linkbutton" id="searchStu" name="searchStu" iconCls="icon-search" plain="true" onClick="doToolbar(this.id)">查询</a>
					</td>					
				</tr>
				</table>
			</div>
			<div style="">
				<table id="stutable" class = "tablestyle" width="100%"></table>
			</div>	
	</div>
	
	<div id="elective" title="编辑" style="width:100%;">
			<div title="课程信息" style="height:30px;" >
				<table  class = "tablestyle" width = "100%">
					<tr>
						<a href="#" class="easyui-linkbutton" id="changeStu" name="changeStu" iconCls="icon-submit" plain="true" onClick="doToolbar(this.id)">确定</a>
					</tr>
						<tr>
							<td class="titlestyle">学年学期</td>
							<td >
								<select id="EL_XNXQ" name="EL_XNXQ" class="easyui-combobox" style="width:200px;"></select>
							</td>
							
							<td align="center" style="width:12%">
								<a href="#" class="easyui-linkbutton" id="searchChange" name="searchChange" iconCls="icon-search" plain="true" onClick="doToolbar(this.id)">查询</a>
							</td>	
						</tr>
				</table>
			</div>
			<div style="">
				<table id="electiveList" class = "tablestyle" width="100%" ></table>
			</div>
	</div>
	
	<!-- 查询学分不满的学生信息 -->
	<div id="CxSourcebumanDialog" >
		<div class="easyui-layout" style="width:100%; height:100%;" >
			<div region="north" style="height:89px;">
			<table>
	   			<tr>
 					<td><a href="#" onclick="doToolbar(this.id)" id="exportStuXFBM" class="easyui-linkbutton" plain="true" iconcls="icon-submit">导出</a></td>
	   			</tr>
	   		</table>
				<table id="table2" name="table2" class="tablestyle" singleselect="true" width="100%">
					<tr>
						<td class="titlestyle">年级</td>
						<td>
							<select id="XF_NJ" name="XF_NJ" class="easyui-combobox" style="width:152px;"></select>
						</td>
						<td class="titlestyle">学号</td>
						<td>
							<input class='easyui-textbox' id='XF_XH' name='XF_XH'/>
						</td>
					</tr>
					<tr>
						<td class="titlestyle">姓名</td>
						<td>
							<input class='easyui-textbox' id='XF_XM' name='XF_XM'/>
						</td>
						<td class="titlestyle">班级</td>
						<td>
							<input class='easyui-textbox' id='XF_BJ' name='XF_BJ'/>
						</td>
						<td>
							<a href="#" class="easyui-linkbutton" id="chaxunxf" name="chaxunxf" iconCls="icon-search" plain="true" onClick="doToolbar(this.id)">查询</a>
						</td>
					</tr>
				</table>
			</div>
			<div region="center">
				<%-- 遮罩层 --%>
		    	<div id="divPageMask2" class="maskStyle">
		    		<div id="maskFont2"></div>
		    	</div>
				<table id="optionList_buman" style="width:100%;"></table>
			</div>
		</div>
	</div>
	
	<!-- 查询学分不满的学生信息详情-->
	<div id="CxSourcebumanxqDialog" >
		<div class="easyui-layout" style="width:100%; height:100%;" >
			<div region="center">
				<table id="optionList_bumanxq" style="width:100%;"></table>
			</div>
		</div>
	</div>
	
	<!-- 删除留级和不在校学生选课信息-->
	<div id="elecStuDialog" title="请选择当前学期" >
		<table width="236px" class="tablestyle">
			<tr>
				<td colspan=2 align="left">
				<a href="#"	onclick="doToolbar(this.id)" id="delelecSubmit" class="easyui-linkbutton" plain="true" iconcls="icon-save">确定</a></td>
			</tr>
			<tr>
				<td class="titlestyle">学年学期</td>
				<td ><select id="DR_XNXQ" name="DR_XNXQ" class="easyui-combobox" style="width:150px;"></select>
				</td>
			</tr>

		</table>
	</div>
	
	<!-- 下载excel -->
	<iframe id="exportIframe" src="" style="width:0; height:0;"></iframe>
</body>
<script type="text/javascript">
	var iUSERCODE="<%=MyTools.getSessionUserCode(request)%>";
	var row = '';      //行数据
	var iKeyCode = ''; //定义主键
	
	var pageNum = 1;   //datagrid初始当前页数
	var pageSize = 50; //datagrid初始页内行数
	var NJDM_CX = '';//查询条件
	var ZYDM_CX = '';
	var KCDM_CX = '';
	var KCMC_CX = '';
	var saveType = '';
	var xnxqbm="";
	var xxbmc="";
	var skjsbh="";
	var skjsxm="";
	var kcdm="";
	var cdyq="";
	var cdmc="";
	var skzc="";
	var sksj="";
	var bmrs="";
	var zrs="";
	var bkbmzybh="";
	var skjhzbbh="";
	var selStuid="";
	var xxkskjhmx="";//选修课授课计划明细
	var kbmzybh=""; //可报名专业编号
	
	var chgiKeyCode='';//换班授课计划编号
	var chgskjhzbbh='';//换班授课计划主表编号;
	var chgxnxqbm='';//换班学年学期编码;
	var chgkcdm='';//换班课程代码;
	var chgskzc='';//换班授课周次;
	var chgsksj='';//换班上课时间;
	var chgbmrs='';//换班报名人数;
	var chgzrs='';//换班总人数;
	var chgkbmzybh=""; //换班可报名专业编号
	
	var stuidarray =new Array();//存放学生编号,换班+删除
	var stuxmarray =new Array();//存放学生编号,换班+删除
	var stunumarray=new Array();//存放学生编号,添加
	var stunamearray=new Array();//存放学生姓名,添加
	var isLoad = true;//判断datagrid是否处于加载状态
	
	var xuehao="";
	var xingming="";
	var xqtitle="";
	$(document).ready(function(){
		initDialog();//初始化对话框
		initCombobox();
		
	});
	
	/**页面初始化加载数据**/
		
	/**加载combobox控件
		@jxxzData 下拉框数据
	**/
	function initCombobox(){
		$('#SC_XNXQ').combobox({
				url:"<%=request.getContextPath()%>/Svl_CourseSet?active=xnxqCombobox",
				valueField:'comboValue',
				textField:'comboName',
				editable:false,
				panelHeight:'140', //combobox高度
				onLoadSuccess:function(data){
					//判断data参数是否为空
					if(data != ''){
						//初始化combobox时赋值
						$('#SC_XNXQ').combobox('setValue', data[0].comboValue);
						loadGrid();//页面初始化加载数据
					}
				},
				//下拉列表值改变事件
				onChange:function(data){}
		});
			
		$('#DR_XNXQ').combobox({
				url:"<%=request.getContextPath()%>/Svl_CourseSet?active=xnxqCombobox",
				valueField:'comboValue',
				textField:'comboName',
				editable:false,
				panelHeight:'140', //combobox高度
				onLoadSuccess:function(data){
					//判断data参数是否为空
					if(data != ''){
						//初始化combobox时赋值
						$('#DR_XNXQ').combobox('setValue', data[0].comboValue);
					
					}
				},
				//下拉列表值改变事件
				onChange:function(data){}
		});
		
		$('#XF_NJ').combobox({
				url:"<%=request.getContextPath()%>/Svl_CourseSet?active=XF_NJCombobox",
				valueField:'comboValue',
				textField:'comboName',
				editable:false,
				panelHeight:'140', //combobox高度
				onLoadSuccess:function(data){
					//判断data参数是否为空
					if(data != ''){
						//初始化combobox时赋值
						$('#EL_XNXQ').combobox('setValue', data[0].comboValue);
					}
				},
				//下拉列表值改变事件
				onChange:function(data){}
		});
	}
	
	function huanbanCombobox(xnxq){
		$('#EL_XNXQ').combobox({
				url:"<%=request.getContextPath()%>/Svl_CourseSet?active=xnxqCombobox",
				valueField:'comboValue',
				textField:'comboName',
				editable:false,
				panelHeight:'140', //combobox高度
				onLoadSuccess:function(data){
					//判断data参数是否为空
					if(data != ''){
						//初始化combobox时赋值
						$('#EL_XNXQ').combobox('setValue', xnxq);
					
					}
				},
				//下拉列表值改变事件
				onChange:function(data){}
		});
	}
	
	
	/**加载 dialog控件**/
	function initDialog(){
		$('#courseInfo').dialog({   
			width: 300,//宽度设置   
			height: 168,//高度设置 
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
					doToolbar('save');
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
		
		$('#tsc').dialog({   
				title: '个人选课信息',   
				width: 680,//宽度设置   
				height: 150,//高度设置 
				modal:true,
				closed: true,   
				cache: false, 
				draggable:true,//是否可移动dialog框设置
				//读取事件
				onLoad:function(data){
					
				},
				//关闭事件
				onClose:function(data){
					loadGrid();
				}
		});
		
		$('#student').dialog({   
				title: '请选择学生',   
				width: 900,//宽度设置   
				height: 500,//高度设置 
				modal:true,
				closed: true,   
				cache: false, 
				draggable:true,//是否可移动dialog框设置
				//读取事件
				onLoad:function(data){
					
				},
				//关闭事件
				onClose:function(data){
					$('#ic_stuId').textbox('setValue','');
					$('#ic_stuName').textbox('setValue','');
					stunumarray.splice(0, stunumarray.length);
					stunamearray.splice(0, stunamearray.length);
				}
		});
		
		$('#elective').dialog({   
				title: '请选择选修班',   
				width: 900,//宽度设置   
				height: 500,//高度设置 
				modal:true,
				closed: true,   
				cache: false, 
				draggable:true,//是否可移动dialog框设置
				//读取事件
				onLoad:function(data){
					
				},
				//关闭事件
				onClose:function(data){
					stuidarray.splice(0, stuidarray.length);
					stuxmarray.splice(0, stuxmarray.length);
					loadGridClass(xxkskjhmx);
				}
		});
		
		$('#selectClass').dialog({   
				title: '选修信息修改',   
				width: 900,//宽度设置   
				height: 500,//高度设置 
				modal:true,
				closed: true,   
				cache: false, 
				draggable:true,//是否可移动dialog框设置
				//读取事件
				onLoad:function(data){
					
				},
				//关闭事件
				onClose:function(data){
					loadGrid();
				}
		});
		
		$('#CxSourcebumanDialog').dialog({   
				title: '学分未达标的学生信息',   
				width: 800,//宽度设置   
				height: 500,//高度设置 
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
		
		$('#CxSourcebumanxqDialog').dialog({   
				width: 700,//宽度设置   
				height: 450,//高度设置 
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
		
		$('#elecStuDialog').dialog({   
				width: 250,//宽度设置   
				height: 92,//高度设置 
				modal:true,
				closed: true,   
				cache: false, 
				draggable:false,//是否可移动dialog框设置
				//打开事件
				onOpen:function(data){},
				//读取事件
				onLoad:function(data){},
				//关闭事件
				onClose:function(data){
					
				}
		});
	}

	/**加载 datagrid控件，读取页面信息
		@listData 列表数据
	**/
	function loadGrid(){
		$('#selectionList').datagrid({
			url : '<%=request.getContextPath()%>/Svl_CourseSet?active=loadAdminSelection&page=' + pageNum + '&rows=' + pageSize+'&&SC_XNXQ='+$('#SC_XNXQ').combobox('getValue'),
			title:'',
			width:'100%',
			nowrap: false,
			fit:true, //自适应父节点宽度和高度
			showFooter:true,
			striped:true,
			pagination:true,
			pageSize:50,
			singleSelect:true,
			pageNumber:pageNum,
			rownumbers:true,
			fitColumns: true,
			columns:[[
				//field为读取数据的数据名，title为显示的数据名，width宽度设置，align数字在表格中显示的位置
				{field:'学年学期编码',title:'学年学期编码',width:fillsize(0.15),align:'center',
					formatter:function(value,rec){
						var xq=rec.学年学期编码;
						xq=xq.substring(0,4)+"年第"+xq.substring(4,5)+"学期";
						return xq;
					}
				},
				{field:'选修班名称',title:'选修班名称',width:fillsize(0.25),align:'center'},
				{field:'授课教师姓名',title:'授课教师姓名',width:fillsize(0.15),align:'center'},
				{field:'场地名称',title:'场地名称',width:fillsize(0.1),align:'center'},
				{field:'授课周次',title:'授课周次',width:fillsize(0.1),align:'center'},
				{field:'上课时间',title:'上课时间',width:fillsize(0.2),align:'center',
					formatter:function(value,rec){
							if(rec.上课时间==""||rec.上课时间==undefined){
								return "";
							}
							var sksj=rec.上课时间;
							var sksj2=sksj.split(",");
							var week=sksj2[0].substring(0,2);
							if(week=="01"){
								week="周一";
							}else if(week=="02"){
								week="周二";
							}else if(week=="03"){
								week="周三";
							}else if(week=="04"){
								week="周四";
							}else if(week=="05"){
								week="周五";
							}else{
							
							}
							var apm=parseInt(sksj2[0].substring(2,4));
							if(apm<5){
								apm="上午";
							}else if(apm>6){
								apm="下午";
							}
							var num=(parseInt(sksj2[0].substring(2,4))-2)+"-"+(parseInt(sksj2[sksj2.length-1].substring(2,4))-2);
							return week+apm+"第"+num+"节课";
					}
				},
				{field:'报名人数',title:'报名人数',width:fillsize(0.1),align:'center'},
				{field:'总人数',title:'总人数',width:fillsize(0.1),align:'center'},
				{field:'info',title:'详情',width:fillsize(0.1),align:'center',
					formatter:function(value,rec){
						var delbutton="";
						button='<input id="'+rec.授课计划明细编号+'" type="button" value="[详情]" style="cursor:pointer;display:block;" onclick="selectionInfo(this.id,\''+rec.学年学期编码+'\');" />';
						return button;
					}
				}
				
			]],
			//双击某行时触发
			onDblClickRow:function(rowIndex,rowData){},
			//读取datagrid之前加载
			onBeforeLoad:function(){},
			//单击某行时触发
			onClickRow:function(rowIndex,rowData){
				//主键赋值
				iKeyCode = rowData.授课计划明细编号;
				skjhzbbh = rowData.授课计划主表编号;
				xnxqbm=rowData.学年学期编码;
				kcdm=rowData.课程代码;
				skzc=rowData.授课周次;
				sksj=rowData.上课时间;
				bmrs=rowData.报名人数;
				zrs=rowData.总人数;
				kbmzybh=rowData.可报名专业编号;
			},
			//加载成功后触发
			onLoadSuccess: function(data){
				iKeyCode = '';	
				stuidarray.splice(0, stuidarray.length);
				stuxmarray.splice(0, stuxmarray.length);
			}
		});
		
// 		$("#selectionList").datagrid("getPager").pagination({
// 			total:listData.total,
// 			afterPageText: '页&nbsp;<a href="#" onclick="goEnterPage();">Go</a>&nbsp;&nbsp;&nbsp;共 {pages} 页',
// 			onSelectPage:function (pageNo, pageSize_1) { 
// 				pageNum = pageNo;
// 				pageSize = pageSize_1;
// 				initData();
// 			} 
// 		});
	};
	
	//显示所有选修班
	function loadGridElc(id){
		$('#electiveList').datagrid({
				url : '<%=request.getContextPath()%>/Svl_CourseSet?active=loadAdminSelection&page=' + pageNum + '&rows=' + pageSize+'&&SC_XNXQ='+$('#EL_XNXQ').combobox('getValue'),
				loadMsg : "信息加载中请稍后!",//载入时信息
				width:886,
				height:407,
				rownumbers: true,
				animate:true,
				striped : true,//隔行变色
				pageSize : 50,//每页记录数
				singleSelect : true,//单选模式
				pageNumber : pageNum,//当前页码
				pagination:true,
				fit:false,
				fitColumns: true,//设置边距
				columns:[[
					//field为读取数据的数据名，title为显示的数据名，width宽度设置，align数字在表格中显示的位置
					{field:'ck',checkbox:true},
					{field:'选修班名称',title:'选修班名称',width:fillsize(0.25),align:'center'},
					{field:'授课教师姓名',title:'授课教师姓名',width:fillsize(0.15),align:'center'},
					{field:'场地名称',title:'场地名称',width:fillsize(0.1),align:'center'},
					{field:'授课周次',title:'授课周次',width:fillsize(0.1),align:'center'},
					{field:'上课时间',title:'上课时间',width:fillsize(0.2),align:'center',
						formatter:function(value,rec){
								if(rec.上课时间==""){
									return "";
								}
								var sksj=rec.上课时间;
								var sksj2=sksj.split(",");
								var week=sksj2[0].substring(0,2);
								if(week=="01"){
									week="周一";
								}else if(week=="02"){
									week="周二";
								}else if(week=="03"){
									week="周三";
								}else if(week=="04"){
									week="周四";
								}else if(week=="05"){
									week="周五";
								}else{
								
								}
								var apm=parseInt(sksj2[0].substring(2,4));
								if(apm<5){
									apm="上午";
								}else if(apm>6){
									apm="下午";
								}
								var num=(parseInt(sksj2[0].substring(2,4))-2)+"-"+(parseInt(sksj2[sksj2.length-1].substring(2,4))-2);
								return week+apm+"第"+num+"节课";
						}
					},
					{field:'报名人数',title:'报名人数',width:fillsize(0.1),align:'center'},
					{field:'总人数',title:'总人数',width:fillsize(0.1),align:'center'}		
				]],
				onClickRow:function(rowIndex, rowData){
					//$('#electiveList').datagrid("unselectRow", rowIndex);
					//主键赋值
					chgiKeyCode = rowData.授课计划明细编号;
					chgskjhzbbh = rowData.授课计划主表编号;
					chgxnxqbm=rowData.学年学期编码;
					chgkcdm=rowData.课程代码;
					chgskzc=rowData.授课周次;
					chgsksj=rowData.上课时间;
					chgbmrs=rowData.报名人数;
					chgzrs=rowData.总人数;
					chgkbmzybh=rowData.可报名专业编号;
				},
				onLoadSuccess: function(data){	
					iKeyCode = '';	
					$(".datagrid-header-check").html('&nbsp;');//隐藏全选
				},
				onLoadError:function(none){
					
				}
		});
	};
	
	
	function goEnterPage(){
		var e = jQuery.Event("keydown");//模拟一个键盘事件
		e.keyCode = 13;//keyCode=13是回车 
		$("input.pagination-num").trigger(e);//模拟页码框按下回车 
	}
	
	//显示所选选修班的学生
	function loadGridClass(id){
		isLoad=true;
		$('#classInfo').datagrid({
				url : '<%=request.getContextPath()%>/Svl_CourseSet?active=loadGridClass&XX_XXKMXBH='+id,
				loadMsg : "信息加载中请稍后!",//载入时信息
				width:886,
				height:437,
				nowrap: false,
				rownumbers: true,
				animate:true,
				striped : true,//隔行变色
				pageSize : 50,//每页记录数
				singleSelect : false,//单选模式
				pageNumber : pageNum,//当前页码
				pagination:true,
				fit:false,
				fitColumns: true,//设置边距
				columns:[[
					{field:'ck',checkbox:true},
					{field:'学号',title:'学号',width:100,align:'center'},
					{field:'姓名',title:'姓名',width:100,align:'center'},
					{field:'行政班名称',title:'行政班名称',width:100,align:'center'}	
				]],
				onSelect:function(rowIndex,rowData){
					if(isLoad == false){
						stuidarray.push(rowData.学号);
						stuxmarray.push(rowData.姓名);
					}
				},
				onUnselect:function(rowIndex,rowData){
					$.each(stuidarray, function(key,value){
						if(value == rowData.学号){
							stuidarray.splice(key, 1);
							stuxmarray.splice(key, 1);
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
	};
		
	//显示所有学生
	function loadGridStuClass(){
		isLoad=true;
		$('#stutable').datagrid({
				url : '<%=request.getContextPath()%>/Svl_CourseSet?active=loadGridStuClass&stuid='+$('#ic_stuId').textbox('getValue')+'&stuname='+encodeURI(encodeURI($('#ic_stuName').textbox('getValue')))+'&XX_XXKMXBH='+xxkskjhmx,
				loadMsg : "信息加载中请稍后!",//载入时信息
				width:886,
				height:407,
				rownumbers: true,
				animate:true,
				striped : true,//隔行变色
				pageSize : 20,//每页记录数
				singleSelect : false,//单选模式
				pageNumber : pageNum,//当前页码
				pagination:true,
				fit:false,
				fitColumns: true,//设置边距
				columns:[[
					{field:'ck',checkbox:true},
					{field:'学号',title:'学号',width:100,align:'center'},
					{field:'姓名',title:'姓名',width:100,align:'center'},
					{field:'行政班名称',title:'行政班名称',width:100,align:'center'}	
				]],
				onSelect:function(rowIndex,rowData){
					if(isLoad == false){
						stunumarray.push(rowData.学号);
						stunamearray.push(rowData.姓名);
					}
				},
				onUnselect:function(rowIndex,rowData){
					$.each(stunumarray, function(key,value){
						if(value == rowData.学号){
							stunumarray.splice(key, 1);
							stunamearray.splice(key, 1);
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
	};
	
	
	/**工具栏按钮调用方法，传入按钮的id
		@id 当前按钮点击事件
	**/
	function doToolbar(id){
		//详情
		if(id == 'query'){
			KCDM_CX = $('#KCDM_CX').textbox('getValue'); 
			KCMC_CX = $('#KCMC_CX').textbox('getValue');
			ZYDM_CX = $('#ZYDM_CX').combobox('getValue');
			loadData();
		}
		
		//查询
		if(id == 'query'){
			KCDM_CX = $('#KCDM_CX').textbox('getValue'); 
			KCMC_CX = $('#KCMC_CX').textbox('getValue');
			ZYDM_CX = $('#ZYDM_CX').combobox('getValue');
			loadData();
		}
		
		//判断获取参数为new，执行新建操作
		if(id == 'new'){
			//打开dialog
			$('#courseInfo').dialog({   
				title: '新建课程信息'
			});
			saveType = 'new';
			$('#courseInfo').dialog('open');
			$('#ZYDM').combobox("disable");
			$('#KCLX').combobox("enable");
		}

		//判断获取参数为edit，执行编辑操作
		if(id == 'edit'){
			if(iKeyCode == ''){
				alertMsg('请选择一行数据');
				return;
			}
			
			//打开dialog
			$('#courseInfo').dialog({   
				title: '编辑班级信息'   
			});
			if(row!=undefined && row!=''){
				$('#form1').form('load', row);
				$('#KCDM').html(row.KCDM);
				if(row.ZYDM=="000000"){
					$('#ZYDM').combobox('setValue','');
					$('#KCLX').combobox('setValue','01');		
				}else if(row.ZYDM=="900000"){
					$('#ZYDM').combobox('setValue','');
					$('#KCLX').combobox('setValue','03');	
				}else{
					$('#KCLX').combobox('setValue','02');		
				}
				$('#ZYDM').combobox("disable");	
				$('#KCLX').combobox("disable");			
			}
			saveType = 'edit';
			$('#courseInfo').dialog('open');
		}
		
		if(id == 'del'){
			if(iKeyCode == ''){
				alertMsg('请选择一行数据');
				return;
			}
			ConfirmMsg('是否确定要删除当前选择的课程', 'delClass();', '');
		}
		
		//判断获取参数为save，执行保存操作
		if(id == 'save'){
			var KCMC = $('#KCMC').textbox('getValue');

			if(KCMC == ''){
				alertMsg('请填写课程名称');
				return;
			}
			if(KCMC.length > 50){
				alertMsg('课程名称长度超出50位');
				return;
			}
			if($('#KCLX').combobox('getValue') == '' ){
				alertMsg('请选择课程类型');
				return;
			}
			if($('#KCLX').combobox('getValue') == '02' && $('#ZYDM').combobox('getValue') == ''){
				alertMsg('请选择所属专业');
				return;
			}
			
			$('#kcbh').val($('#KCDM').html());
			$('#active').val(saveType);
			$("#form1").submit();
		}
		//查询选修课
		if(id=="searchElective"){
			loadGrid();
		}
		if(id=="searchChange"){
			loadGridElc();
		}
		//添加学生
		if(id=="addStu"){
			if(bmrs==zrs){
					alertMsg("对不起，该课程报名人数已满，请选择其它课程！");
					return;
			}
			$('#student').dialog('open');
			loadGridStuClass();
		}
		//查询学生
		if(id=="searchStu"){
			loadGridStuClass();
		}
		//添加所选学生
		if(id=="submitStu"){
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_CourseSet',
				data : 'active=submitStu&XX_XXKMXBH='+xxkskjhmx+'&stunumarray='+stunumarray+'&stunamearray='+stunamearray+'&bmrs='+bmrs+'&zrs='+zrs+'&XX_XNXQ='+xnxqbm+'&XX_SKZC='+skzc+'&XX_SJXL='+sksj+'&XX_KCDM='+kcdm+'&XX_ZYBH='+kbmzybh,
				dataType:"json",
				success : function(data){
					if(data[0].MSG=="添加成功"){
						showMsg(data[0].MSG);
						loadGridClass(xxkskjhmx);
						$('#student').dialog('close');
					}else{
						alertMsg(data[0].MSG);
					}
				}
			});
		}
		//换班
		if(id=="changeClass"){
			if(stuidarray.length==0){
				alertMsg("请选择学生");
				return;
			}
			$('#elective').dialog('open');
			loadGridElc();	
		}
		//保存换班
		if(id=="changeStu"){
			if(chgiKeyCode ==""){
				alertMsg("请选择要换的选修课");
				return;
			}
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_CourseSet',
				data : 'active=changeStudent&xxkskjhmx='+xxkskjhmx+'&XX_XXKMXBH='+chgiKeyCode+'&stuidarray='+stuidarray+'&stuxmarray='+stuxmarray+'&bmrs='+chgbmrs+'&zrs='+chgzrs+'&XX_XNXQ='+chgxnxqbm+'&XX_SKZC='+chgskzc+'&XX_SJXL='+chgsksj+'&XX_KCDM='+chgkcdm+'&skzc='+skzc+'&sksj='+sksj+'&XX_ZYBH='+chgkbmzybh,
				dataType:"json",
				success : function(data){
					if(data[0].MSG=="保存成功"){
						showMsg(data[0].MSG);
						loadGridClass(xxkskjhmx);
						$('#elective').dialog('close');
						stuidarray.splice(0, stuidarray.length);
						stuxmarray.splice(0, stuxmarray.length);
					}else{
						alertMsg(data[0].MSG);
						loadGridClass(xxkskjhmx);
					}
				}
			});
		}
		//删除
		if(id=="delStu"){
			if(stuidarray.length==0){
				alertMsg("请选择学生");
				return;
			}
			ConfirmMsg('是否确定要删除所选学生', 'delstudent();', '');
		}
		//导出
		if(id == 'export'){
			$('#maskFont').html('开课情况导出中...');
    		$('#divPageMask').show();
			ExportExcel();
		}
		//导出点名单
		if(id == 'exportStu'){
			$('#maskFont').html('学生点名单导出中...');
    		$('#divPageMask').show();
			ExportExcelmingdan();
		}
		
		//学分不足学生名单
		if(id == 'sourcebuzu'){
			$('#XF_NJ').combobox('setValue','');
			$('#XF_XH').textbox('setValue','');
			$('#XF_XM').textbox('setValue','');
			$('#XF_BJ').textbox('setValue','');
			loadGridbuman();
    		$('#CxSourcebumanDialog').dialog('open');
		}
		
		//查询
		if(id == 'chaxunxf'){
			loadGridbuman();
		}
		
		//导出点名单
		if(id == 'exportStuXFBM'){
			$('#maskFont2').html('学分未达标的学生信息导出中...');
    		$('#divPageMask2').show();
			ExportExcelStuXFBM();
		}
		
		//删除留级和不在校学生选课信息
		if(id == "delelecStu"){
			$('#elecStuDialog').dialog("open");
		}
		if(id == "delelecSubmit"){
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_CourseSet',
				data : 'active=delelecSubmit&XX_XNXQ='+$('#DR_XNXQ').combobox('getValue'),
				dataType:"json",
				success : function(data){
					if(data[0].MSG=="删除成功"){
						$('#elecStuDialog').dialog("close");
						showMsg(data[0].MSG);
					}else{
						$('#elecStuDialog').dialog("close");
						alertMsg(data[0].MSG);				
					}
				}
			});
		}
	}
	
	//打开详情
	function selectionInfo(id,xnxq){
		xxkskjhmx=id;
		$('#selectClass').dialog('open');
		loadGridClass(id);
		huanbanCombobox(xnxq);
	}
	
	//选中学生
	function selectStuid(id,xm){
		var checkbox = document.getElementById(id);
		if(checkbox.checked){//勾选
			stuidarray.push(id);
			stuxmarray.push(xm);
		}else{//不勾选
			for(var i=0;i<stuidarray.length;i++){
				if(checkbox.id==stuidarray[i]){
					stuidarray.splice(i,1);
					stuxmarray.splice(i,1);
				}
			}
		}
	}
	
	//选中学生
	function editStuInfo(id,name){
		var checkbox = document.getElementById(id);
		if(checkbox.checked){//勾选
			stunumarray.push(id);
			stunamearray.push(name);
		}else{//不勾选
			for(var i=0;i<stunumarray.length;i++){
				if(checkbox.id==stunumarray[i]){
					stunumarray.splice(i,1);
					stunamearray.splice(i,1);
				}
			}
		}
	}
	
	//删除学生
	function delstudent(){
		$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_CourseSet',
				data : 'active=delStudent&XX_XXKMXBH='+xxkskjhmx+'&stuidarray='+stuidarray,
				dataType:"json",
				success : function(data){
					showMsg(data[0].MSG);
					loadGridClass(xxkskjhmx);
					stuidarray.splice(0, stuidarray.length);
					stuxmarray.splice(0, stuxmarray.length);
				}
		});
	}
	
	//保存选课
	function saveSelection(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_CourseSet',
			data : 'active=saveSelection&iKeyCode='+iKeyCode+'&iUSERCODE='+selStuid+'&xnxqbm='+xnxqbm,
			dataType:"json",
			success : function(data){
				if(data[0].MSG == '报名成功'){
					showMsg(data[0].MSG);
					initData();
				}else{
					showMsg(data[0].MSG);
				}
			}
		});
	}
	
	//删除选课
	function delSelection(id){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_CourseSet',
			data : 'active=delSelection&iKeyCode='+id+'&iUSERCODE='+selStuid,
			dataType:"json",
			success : function(data){
				if(data[0].MSG == '删除成功'){
					showMsg(data[0].MSG);
					loadGrid2();	
				}else{
					showMsg(data[0].MSG);
				}
			}
		});
	}
	
	/**表单提交**/
	$('#form1').form({
		//定位到servlet位置的url
		url:'<%=request.getContextPath()%>/Svl_CourseSet',
		//当点击事件后触发的事件
		onSubmit: function(data){
			return $(this).form('validate');//验证
		}, 
		//当点击事件并成功提交后触发的事件
		success:function(data){
			var json = eval("("+data+")");
			
			if(json[0].MSG == '保存成功'){
				showMsg(json[0].MSG);
				$("#active").val('');
				
				emptyDialog();//清空并关闭dialog
				loadData();
				$('#courseInfo').dialog('close');
			}else{
				alertMsg(json[0].MSG);
			}
		}
	});
	
	/**清空Dialog中表单元素数据**/
	function emptyDialog(){
		saveType = '';
		$('#KCDM').html("");
		$('#KCMC').textbox('setValue', '');
		$('#ZYDM').combobox('setValue', '');
		$('#KCLX').combobox('setValue', '');
	}
	
	//防JS注入
	/*
	function checkData(str) {
        var  entry = { "'": "&apos;", '"': '&quot;', '<': '&lt;', '>': '&gt;' };
        str = v.replace(/(['")-><&\\\/\.])/g, function ($0){
        	return entry[$0] || $0;
        });
        return str;
    }*/
	
	
	//处理键盘事件
	// 禁止后退键（Backspace）密码或单行、多行文本框除外
	function banBackSpace(e){
	    var ev = e || window.event;//获取event对象
	    var obj = ev.target || ev.srcElement;//获取事件源
	    var t = obj.type || obj.getAttribute('type');//获取事件源类型
	    
	    //获取作为判断条件的事件类型
	    var vReadOnly = obj.getAttribute('readonly');
	    var vEnabled = obj.getAttribute('enabled');
	
	    //处理null值情况
	    vReadOnly = (vReadOnly == null)?false:vReadOnly;
	    vEnabled = (vEnabled == null)?true:vEnabled;
	    
	    //当敲Backspace键时，事件源类型为密码或单行、多行文本的，
	    //并且readonly属性为true或enabled属性为false的，则退格键失效
	    var flag1=(ev.keyCode == 8 && (t=="password"|| t=="text"|| t=="textarea") && (vReadOnly==true|| vEnabled!=true))?true:false;
	
	    //当敲Backspace键时，事件源类型非密码或单行、多行文本的，则退格键失效
	    var flag2=(ev.keyCode == 8 && t != "password"&& t != "text"&& t != "textarea")?true:false;
	
	    //判断
	    if(flag2)
	        return false;
	
	    if(flag1)
	        return false;
	}
	
	//禁止后退键(作用于Firefox、Opera)
	document.onkeypress=banBackSpace;
	//禁止后退键 (作用于IE、Chrome)
	document.onkeydown=banBackSpace;
	
	
	//导出选修课课程信息==================================================================================
	function ExportExcel(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_CourseSet',
			data : 'active=ExportExcelXXKSubject&SC_XNXQ='+$('#SC_XNXQ').combobox('getValue')+'&SC_XNXQMC='+$('#SC_XNXQ').combobox('getText'),
			dataType:"json",
			success : function(data) {
				$('#divPageMask').hide();
				if(data[0].MSG == '文件生成成功'){
					//下载文件到本地
					$("#exportIframe").attr("src", '<%=request.getContextPath()%>/form/elective/fileDownload.jsp?filePath=' + encodeURI(encodeURI(data[0].filePath)));
				}else{
					alertMsg(data[0].MSG);
				}
			}
		});
	}
	function ExportExcelmingdan(){
		 $.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_CourseSet',
			data : 'active=ExportExcelStudianming&SC_XNXQ='+$('#SC_XNXQ').combobox('getValue')+'&SC_XNXQMC='+$('#SC_XNXQ').combobox('getText'),
			dataType:"json",
			success : function(data) {
				$('#divPageMask').hide();
				if(data[0].MSG == '文件生成成功'){ 
					//下载文件到本地
					$("#exportIframe").attr("src", '<%=request.getContextPath()%>/form/elective/fileDownload.jsp?filePath=' + encodeURI(encodeURI(data[0].filePath)));
				}else{
					alertMsg(data[0].MSG);
				}
			}
		});
	}
	
	
	/**加载 datagrid控件
		查询学分不满的学生信息
	**/
	function loadGridbuman(){
	$('#optionList_buman').datagrid({
			url:'<%=request.getContextPath()%>/Svl_CourseSet',
			queryParams:{"active":"querySourcebuman",
								"nj":encodeURI($('#XF_NJ').combobox('getValue')),
								"xh":encodeURI($('#XF_XH').textbox('getValue')),
								"xm":encodeURI($('#XF_XM').textbox('getValue')),
								"banji":encodeURI($('#XF_BJ').textbox('getValue'))
					},
			title:'',
			width:'100%',
			nowrap: false,
			fit:true, //自适应父节点宽度和高度
			showFooter:true,
			striped:true,
			singleSelect:true,
			pageNumber:pageNum,
			rownumbers:true,
			fitColumns: true,
			pagination:true,
			pageSize:50,
			//下面是表单中加载显示的信息
			columns:[[
				{field:'学号',title:'学号',width:fillsize(0.1),align:'center'},
				{field:'姓名',title:'姓名',width:fillsize(0.1),align:'center'},
				{field:'行政班名称',title:'行政班名称',width:fillsize(0.2),align:'center'},
				{field:'已选课的学分',title:'已选课的学分',width:fillsize(0.1),align:'center'},
				{field:'达标学分',title:'达标学分',width:fillsize(0.1),align:'center'},
				{field:'还差多少学分',title:'还差多少学分',width:fillsize(0.1),align:'center',formatter:function(value,row,index){
					if(value>0)
						return "<font color='red'>"+value+"</font>";
				}},
				{field:'AA_SET',align:'center',title:'操作',width:fillsize(0.1),formatter:function(value,rows){
						var str = '';
						str = "<input type='button' value='详情' style='cursor:pointer;width:60px;' onclick='openSetDialog(\"" + rows.学号+ "\",\""+rows.姓名+"\")'>";
						return str;
					}
				}
			]],
			onClickRow:function(rowIndex,rowData){ 
			},
			//双击某行时触发
			onDblClickRow:function(rowIndex,rowData){
			},
			//成功加载时
			onLoadSuccess:function(data){//查询成功时候把后台的查询语句赋值给变量listsql
				
			}
		});
	}
	
	/**打开题目设置对话框**/
	function openSetDialog(xuehao1, xingming1){
			xuehao=xuehao1;
			xingming=xingming1;
			xqtitle=xingming+"选课信息";
			loadGridxiangqing();
			$('#CxSourcebumanxqDialog').dialog('setTitle',xqtitle);
			$('#CxSourcebumanxqDialog').dialog('open'); 
	}
	
	
	
	//详情
	function loadGridxiangqing(){
		$('#optionList_bumanxq').datagrid({
			url : '<%=request.getContextPath()%>/Svl_CourseSet?active=showSelectiongeren&iUSERCODE='+xuehao,
			width:'100%',
			nowrap: false,
			fit:true, //自适应父节点宽度和高度
			showFooter:true,
			striped:true,
			singleSelect:true,
			pageNumber:pageNum,
			rownumbers:true,
			fitColumns: true,
			pagination:true,
			pageSize:50,
			columns:[[
				//field为读取数据的数据名，title为显示的数据名，width宽度设置，align数字在表格中显示的位置
				{field:'学年学期编码',title:'学年学期编码',width:fillsize(0.15),align:'center',
					formatter:function(value,rec){
						var xq=rec.学年学期编码;
						xq=xq.substring(0,4)+"年第"+xq.substring(4,5)+"学期";
						return xq;
					}
				},
				{field:'学号',title:'学号',width:fillsize(0.1),align:'center'},
				{field:'姓名',title:'姓名',width:fillsize(0.1),align:'center'},
				{field:'选修班名称',title:'选修班名称',width:fillsize(0.2),align:'center'},
				{field:'学分',title:'学分',width:fillsize(0.1),align:'center'}
			]],
			//双击某行时触发
			onDblClickRow:function(rowIndex,rowData){},
			//读取datagrid之前加载
			onBeforeLoad:function(){},
			//单击某行时触发
			onClickRow:function(rowIndex,rowData){
				$('#optionList_bumanxq').datagrid("unselectRow", rowIndex);
			},
			//加载成功后触发
			onLoadSuccess: function(data){
			}
		});
	};
	
	//导出学分不满的学生信息==================================================================================
	function ExportExcelStuXFBM(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_CourseSet',
			data : 'active=ExportExcelStuXFBM&nj='+encodeURI($('#XF_NJ').combobox('getValue'))+'&xh='+encodeURI($('#XF_XH').textbox('getValue'))+'&xm='+encodeURI($('#XF_XM').textbox('getValue'))+'&banji='+encodeURI($('#XF_BJ').textbox('getValue')),
			dataType:"json",
			success : function(data) {
				$('#divPageMask2').hide();
				if(data[0].MSG == '文件生成成功'){
					//下载文件到本地
					$("#exportIframe").attr("src", '<%=request.getContextPath()%>/form/elective/fileDownload.jsp?filePath=' + encodeURI(encodeURI(data[0].filePath)));
				}else{
					alertMsg(data[0].MSG);
				}
			}
		});
	}
</script>
</html>