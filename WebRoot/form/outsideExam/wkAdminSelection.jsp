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
		.maskStyle{
			width:110%;
			height:100%;
			overflow:hidden;
			display:none;
			background-color:#D2E0F2;
			filter:alpha(opacity=80);
			position:absolute;
			top:0px;
			left:0px;
			z-index:9999;
		}
		#maskFont{
			 font-size:16px;
			 color:#2B2B2B;
			 width:200px;
			 height:100%;
			 position:absolute;
			 top:50%;
			 left:50%;
			 margin-top:-10px;
			 margin-left:-150px;
			 font-weight:bold;
		}
			
	</style>
  </head>

<body  class="easyui-layout">
	<div id="north" region="north" title="课程信息" style="height:59px;" >
		<!-- 
		<table width="100%">
			<tr>
				<td>
					<a href="#" class="easyui-linkbutton" id="importCost" name="addStu" iconCls="icon-site" plain="true" onClick="doToolbar(this.id)">导入交费信息</a>
					<a href="#" class="easyui-linkbutton" id="importScore" name="addStu" iconCls="icon-site" plain="true" onClick="doToolbar(this.id)">导入学生成绩</a>
				</td>
			</tr>
		</table>
		 -->
		<table  class = "tablestyle" width = "100%">
				<tr>
					<td class="titlestyle" width="20%" >外考科目名称</td>
					<td >
						<input id="SC_KSMC" name="SC_KSMC" class="easyui-textbox" style="width:200px;"/>
					</td>
					
					<td align="center" style="width:12%">
						<a href="#" class="easyui-linkbutton" id="searchExam" name="searchExam" iconCls="icon-search" plain="true" onClick="doToolbar(this.id)">查询</a>
					</td>	
				</tr>
		</table>
	</div>
	<div region="center">
		<table id="selectionList" style="width:100%;"></table>
	</div>
	
	<%-- 遮罩层 --%>
   	<div id="divPageMask" class="maskStyle">
   		<div id="maskFont">文件生成中，请稍后...</div>
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
					<a href="#" class="easyui-linkbutton" id="delStu" name="delStu" iconCls="icon-cancel" plain="true" onClick="doToolbar(this.id)">删除</a>
				</tr>
				</table>
			</div>
			<div style="">
				<table id="classInfo" class = "tablestyle" width="100%"></table>
			</div>	
	</div>
	
	<div id="student" title="编辑" style="width:100%;">
		<div class="easyui-layout" style="width:100%; height:100%;">
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
	
	
	<div id="import" title="" style="">
		<form id="form1" name="form1" method="POST"
		action="<%=request.getContextPath()%>/Svl_Import" enctype="multipart/form-data">
			<table width="236px" class="tablestyle">
				<tr>
					<td colspan=2 align="left">
					<a href="#"
						onclick="doToolbar(this.id)" id="upload" class="easyui-linkbutton" plain="true" 
						iconcls="icon-submit">导入</a></td>
				</tr>
				<tr>
					<td colspan=2 ><input type="file" name="excel1" id="excel1" width="100%">
					</td>
				</tr>
				
			</table>	
			<input type="hidden" id="active" name="active" />
	</form>	
	</div>
	
	<!-- 截止日期 -->
	<div id="expiry">
		<table width="300px" class="tablestyle">
			<tr>
				<a href="#" class="easyui-linkbutton" id="" iconCls="icon-submit" plain="true" onClick="exportWKClass()">确定</a>
			</tr>
			<tr>
				<td>
					<input style='width:50px;' class='easyui-textbox' id='ic_exYear' name='ic_exYear'/>年
					<input style='width:50px;' class='easyui-textbox' id='ic_exMonth' name='ic_exMonth'/>月
					<input style='width:50px;' class='easyui-textbox' id='ic_exDate' name='ic_exDate'/>日
					<input style='width:50px;' class='easyui-textbox' id='ic_exTime' name='ic_exTime'/>时间
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
	var pageSize = 20; //datagrid初始页内行数
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
	var xxkskjhmx="";//外考考试编号
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
	var wkid="";//外考编号
	var isLoad = true;//判断datagrid是否处于加载状态
	var stuidarray =new Array();//存放学生编号,换班+删除
	var stuxmarray =new Array();//存放学生编号,换班+删除
	var stunumarray=new Array();//存放学生编号,添加
	var stunamearray=new Array();//存放学生姓名,添加
	
	$(document).ready(function(){
		initDialog();//初始化对话框
		initCombobox();
		loadGrid();
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
						
					}
				},
				//下拉列表值改变事件
				onChange:function(data){}
		});
		
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
						$('#EL_XNXQ').combobox('setValue', data[0].comboValue);
					
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
					loadGridClass(xxkskjhmx);
				}
		});
		
		$('#selectClass').dialog({   
				title: '外考报名信息修改',   
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
		
		$('#import').dialog({   
				title: '导入',   
				width: 250,//宽度设置   
				height: 91,//高度设置 
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
		
		$('#expiry').dialog({   
				title: '请输入截止时间',   
				width: 314,//宽度设置   
				height: 89,//高度设置 
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
	}

	/**加载 datagrid控件，读取页面信息
		@listData 列表数据
	**/
	function loadGrid(){
		$('#selectionList').datagrid({
				url: '<%=request.getContextPath()%>/Svl_outsideExam?active=loadOutsideExam&SC_KSMC='+encodeURI(encodeURI($('#SC_KSMC').textbox('getValue'))),
				loadMsg : "信息加载中请稍后!",//载入时信息
				width : '100%',//宽度
				nowrap:false,
				showFooter:true,
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
					{field:'WK_KSMC',title:'外考考试名称',width:fillsize(0.15),align:'center'},
					{field:'WK_BMKSSJ',title:'报名开始时间',width:fillsize(0.1),align:'center'},
					{field:'WK_BMJSSJ',title:'报名结束时间',width:fillsize(0.1),align:'center'},
					{field:'WK_KSRQ',title:'考试日期',width:fillsize(0.1),align:'center'},			
					{field:'WK_BMFY',title:'报名费用',width:fillsize(0.1),align:'center'},
					{field:'WK_BMRS',title:'报名人数',width:fillsize(0.1),align:'center'},
					{field:'info',title:'操作',width:fillsize(0.08),align:'center',
						formatter:function(value,rec){
							var button="";
							button='<input id="'+rec.WK_KSBH+'" type="button" value="[详情]" style="cursor:pointer;" onclick="selectionInfo(this.id);" /> ';
							button+='<input id="op'+rec.WK_KSBH+'" type="button" value="[导出]" style="cursor:pointer;" onclick="output(this.id);" />';
							return button;
						}
					}			
				]],
				onClickRow:function(rowIndex, rowData){
					row=rowData;
					iKeyCode=rowData.WK_KSBH;
					bmrs=rowData.WK_BMRS;
					$('#WK_KSBH').val(iKeyCode);
				},
				onLoadSuccess: function(data){
					
				},
				onLoadError:function(none){
					
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
	}
	
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
					{field:'1',title:'选择',width:70,align:'center',
							formatter:function(value,rec){
								var xxk='<input type="radio" id="'+rec.授课计划明细编号+'" name="stucb" onclick="" />';
								return xxk;
							}
					},
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
					$('#electiveList').datagrid("unselectRow", rowIndex);
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
		isLoad = true;
		$('#classInfo').datagrid({
				url : '<%=request.getContextPath()%>/Svl_outsideExam?active=loadGridWKStu&WK_KSBH='+id,
				loadMsg : "信息加载中请稍后!",//载入时信息
				width:886,
				height:437,
				rownumbers: true,
				animate:true,
				striped : true,//隔行变色
				pageSize : pageSize,//每页记录数
				singleSelect : false,//单选模式
				pageNumber : pageNum,//当前页码
				pagination:true,
				fit:false,
				fitColumns: true,//设置边距
				columns:[[
					{field:'ck',checkbox:true},
					{field:'学号',title:'学号',width:100,align:'center'},
					{field:'姓名',title:'姓名',width:100,align:'center'},
					{field:'外考考试名称',title:'外考考试名称',width:100,align:'center'}
					/*
					{field:'是否交费',title:'是否交费',width:100,align:'center',
						formatter:function(value,rec){
							var sfjf="";
							if(rec.是否交费=="0"){
								sfjf="否";
							}else{
								sfjf="是";
							}
							return sfjf;
						}
					}
					*/
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
		isLoad = true;
		$('#stutable').datagrid({
				url : '<%=request.getContextPath()%>/Svl_outsideExam?active=loadGridStuExam&stuid='+$('#ic_stuId').textbox('getValue')+'&stuname='+encodeURI(encodeURI($('#ic_stuName').textbox('getValue')))+'&WK_KSBH='+xxkskjhmx,
				loadMsg : "信息加载中请稍后!",//载入时信息
				width:886,
				height:408,
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
		if(id=="searchExam"){
			loadGrid();
		}
		if(id=="searchChange"){
			loadGridElc();
		}
		//添加学生
		if(id=="addStu"){
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
				url : '<%=request.getContextPath()%>/Svl_outsideExam',
				data : 'active=submitStuExam&WK_KSBH='+xxkskjhmx+'&stunumarray='+stunumarray+'&stunamearray='+stunamearray,
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
		//导入交费信息
		if(id=="importCost"){
			var obj = document.getElementById('excel1') ; 
			obj.outerHTML=obj.outerHTML;  
			$('#import').dialog('open');
			$("#active").val("uploadImportCost");
		}
		//导入学生成绩
		if(id=="importScore"){
			
		}
		//导入
		if(id=="upload"){
			var uploadFile = $("#excel1").val();
			if(uploadFile == ''){
				alertMsg('请选择文件');
				return;
			}
		
			$('#form1').submit();	
		}
	}
	
	//打开详情
	function selectionInfo(id){
		xxkskjhmx=id;
		$('#selectClass').dialog('open');
		loadGridClass(id);
	}
	
	//导出
	function output(id){
		wkid=id.substring(2,id.length);
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_outsideExam',
			data : 'active=checkWKBMRS&WK_KSBH='+wkid,
			dataType:"json",
			success : function(data){
				if(data[0].MSG == '0'){
					alertMsg("没有学生报名");
				}else{
					$('#expiry').dialog("open");
				}
			}
		});
		
	}
	
	function exportWKClass(){
		if($('#ic_exYear').textbox('getValue')==""){
			alertMsg("请输入年份");
			return;
		}
		if($('#ic_exMonth').textbox('getValue')==""){
			alertMsg("请输入月份");
			return;
		}
		if($('#ic_exDate').textbox('getValue')==""){
			alertMsg("请输入日期");
			return;
		}
		if($('#ic_exTime').textbox('getValue')==""){
			alertMsg("请输入时间");
			return;
		}
		
		$('#maskFont').html('文件生成中，请稍后...');
		$('#divPageMask').show();
		
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_outsideExam',
			data : 'active=exportWKClass&WK_KSBH='+wkid+'&exYear='+$('#ic_exYear').textbox('getValue')+'&exMonth='+$('#ic_exMonth').textbox('getValue')+'&exDate='+$('#ic_exDate').textbox('getValue')+'&exTime='+$('#ic_exTime').textbox('getValue'),
			dataType:"json",
			success : function(data){
				if(data[0].MSG == '文件生成成功'){
					//下载文件到本地
					$("#exportIframe").attr("src", '<%=request.getContextPath()%>/form/registerScoreManage/download.jsp?filePath=' + encodeURIComponent(data[0].filePath));
				}else{
					alertMsg(data[0].MSG);
				}
				$('#divPageMask').hide();
			}
		});
	
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
				url : '<%=request.getContextPath()%>/Svl_outsideExam',
				data : 'active=delStudentExam&WK_KSBH='+xxkskjhmx+'&stuidarray='+stuidarray,
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
		url:'<%=request.getContextPath()%>/Svl_Import',
		//当点击事件后触发的事件
	    onSubmit: function(data){ 
	    }, 
	    //当点击事件并成功提交后触发的事件
	    success:function(data){
	     	var json = eval("("+data+")");
   		    $('#import').dialog('close');
   		    showMsg(json[0].MSG);
   		    if(json[0].MSG=="导入完成!"){
   		    	loadGrid();
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
</script>
</html>