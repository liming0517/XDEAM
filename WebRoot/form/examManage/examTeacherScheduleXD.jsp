<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="java.util.Vector"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.src.develop.store.user.*"%>
<%
	/**
		创建人：yeq
		Create date: 2015.05.26
		功能说明：用于设置排课
		页面类型:列表及模块入口
		修订信息(有多次时,可有多个)
		修订日期:
		原因:
		修订人:
		修订时间:
	**/
 %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
  <head>
	<title>监考教师查询</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/icon.css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/pubTimetable.js"></script>
	<style>
		body{
			moz-user-select: -moz-none; 
			-moz-user-select: none; 
			-o-user-select:none; 
			-khtml-user-select:none; /* you could also put this in a class */ 
			-webkit-user-select:none;/* and add the CSS class here instead */ 
			-ms-user-select:none; 
			user-select:none;/**禁止选中文字*/ 
		}
		
		.kcbTdStyle{
			font-size:16;
			border-left:1px solid #95B8E7;
			border-bottom:1px solid #95B8E7;
			background-color:#FFFFFF;
			text-align:center;
		}
		
		.kcbTitle{
			 font-weight:bold;
			 font-size:16;
		}
		
		.titleBG{
			background-color:#EFEFEF;
		}
		
		.wpkcTdStyle{
			font-size:12;
			border-left:1px solid #95B8E7;
			border-bottom:1px solid #95B8E7;
			background-color:#FFFFFF;
			text-align:center;
		}
		
		.titleTdStyle{
			font-size:16;
			border-left:1px solid #95B8E7;
			border-bottom:1px solid #95B8E7;
			text-align:center;
		}
		
		.splitTd{
			border-bottom:4px double #95B8E7;
		}
		
		.maskStyle{
			width:100%;
			height:100%;
			overflow:hidden;
			display:none;
			background-color:#D2E0F2;
			filter:alpha(opacity=90);
			position:absolute;
			top:0px;
			left:0px;
			z-index:100;
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
			 margin-left:-80px;
		}
	</style>
  </head>
<%
	/*
		获得角色信息
	*/
	UserProfile up = new UserProfile(request,MyTools.getSessionUserCode(request));
	String userName =up.getUserName();
	if(userName==null){
		userName="";
	}
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
	//up.setUserAuth();
	if(v!=null){
		for(int i=0; i<v.size(); i++){
			if(i == v.size()-1){
				sAuth += MyTools.StrFiltr(v.get(i));
			}else{
				sAuth += MyTools.StrFiltr(v.get(i))+"O";
			}
		}
	}
	// 如果无法获取人员信息，则自动跳转到登陆界面
%>
<body class="easyui-layout" onselectstart="return (event.srcElement.type=='text');">
	<div id="loadingMask" style="width:110%; height:100%; background-color:#FFFFFF; position:absolute; z-index:99999; top:0;"></div>
	<form id="form1" method="post">
		<div id="north" region="north" title="监考教师查询" style="height:55px;" >
			<table id="ee" singleselect="true" width="100%" class="tablestyle">
					<tr>
						<td class="titlestyle">学年学期</td>
						<td>
							<select id="XNXQ" name="XNXQ" class="easyui-combobox" style="width:180px;"></select>
						</td>
						<td class="titlestyle">教学性质</td>
						<td>
							<select id="JXXZ" name="JXXZ" class="easyui-combobox" style="width:180px;"></select>
						</td>
			
					</tr>
			</table>
		</div>
		<div region="center">
			<table id="semesterList" style="width:100%;"></table>
		</div>
		
		<input type="hidden" id="active" name="active"/>
		<input type="hidden" id="kcbInfo" name="kcbInfo"/>
		<input type="hidden" id="wpkcInfo" name="wpkcInfo"/>
		<input type="hidden" id="PK_XNXQBM" name="PK_XNXQBM"/>
		<input type="hidden" id="PK_XZBDM" name="PK_XZBDM"/>
		<input type="hidden" id="PK_SJXL" name="PK_SJXL"/>
		<input type="hidden" id="PK_KCBMXBH" name="PK_KCBMXBH"/>
		<input type="hidden" id="PK_SKJHMXBH" name="PK_SKJHMXBH"/>
		<input type="hidden" id="PT_SKJSBH" name="PT_SKJSBH"/>
		<input type="hidden" id="PT_SKJSMC" name="PT_SKJSMC"/>
		<input type="hidden" id="PT_CDBH" name="PT_CDBH"/>
		<input type="hidden" id="PT_CDMC" name="PT_CDMC"/>
		<input type="hidden" id="PT_SKZC" name="PT_SKZC"/>
		<input type="hidden" id="xqzc" name="xqzc"/>
	</form>
	
	
	<!-- 课程表详情页面 -->
	<div id="classTimetable" style="overflow:hidden;">
		<div style="width:100%; height:100%;" class="easyui-layout">
			<%-- 遮罩层 --%>
	    	<div id="divPageMask2" class="maskStyle">
	    		<div id="maskFont">信息加载中...</div>
	    	</div>
			<div region="west" title="" style="width:300px;">
				<div fit="true" class="easyui-layout">
					<div region="north" split="false" border="false" style="height:62px;" id="CXFSTable">
						<table  class = "tablestyle" width = "100%">
							<tr>
								<td class="titlestyle">查询方式:</td>
								<td colspan="2" class="titlestyle">
									<select id="CXFS" name="CXFS" class="easyui-combobox" style="width:200px;"></select>
								</td>
							</tr>
							<tr id="jscxtj">
								<td class="titlestyle">教师姓名:</td>
								<td class="titlestyle">
									 <input name="ic_jsxm" id="ic_jsxm" style="width:120px;"/>
								</td>
								<td class="titlestyle">
									<a href="#" id="query" class="easyui-linkbutton" plain="true" iconcls="icon-search" onclick="doToolbar(this.id)">查询</a>
								</td>
							</tr>
						</table>
<!-- 						<table  class = "tablestyle" width = "100%" id="jscxtj"> -->
<!-- 							<tr> -->
<!-- 								<td class="titlestyle">教师姓名:</td> -->
<!-- 								<td class="titlestyle"> -->
<!-- 									 <input name="ic_jsxm" id="ic_jsxm" style="width:120px;"/> -->
<!-- 								</td> -->
<!-- 								<td class="titlestyle"> -->
<!-- 									<a href="#" id="query" class="easyui-linkbutton" plain="true" iconcls="icon-search" onclick="doToolbar(this.id)">查询</a> -->
<!-- 								</td> -->
<!-- 							</tr> -->
<!-- 						</table> -->
					</div>
					<div region="center" border="false">
						<ul id="Tree" class="easyui-tree" style="display: none">
						</ul>
					</div>
				</div>
			</div>    
			<div region="center" style="overflow:hidden;">
				<%-- 遮罩层 --%>
		    	<div id="divPageMask" class="maskStyle">
		    		<div id="maskFont">监考信息加载中...</div>
		    	</div>
				<div id="kcbMain" style="width:100%; height:100%;" class="easyui-layout">
					<div region="center" id="kcbContent">
					</div>
				</div>
			</div>
		</div>
	</div>
	
	
</body>
<script type="text/javascript">
	var sAuth = '<%=sAuth%>';
	var iUSERCODE="<%=MyTools.getSessionUserCode(request)%>";
	var jxzgxz = '<%=MyTools.getProp(request, "Base.jxzgxz").replaceAll("@","")%>';//教学主管校长
	var qxjdzr = '<%=MyTools.getProp(request, "Base.qxjdzr").replaceAll("@","")%>';//全校教导主任
	var xbjdzr = '<%=MyTools.getProp(request, "Base.xbjdzr").replaceAll("@","")%>';//系部教导主任
	var qxjwgl = '<%=MyTools.getProp(request, "Base.qxjwgl").replaceAll("@","")%>';//全校教务管理
	var xbjwgl = '<%=MyTools.getProp(request, "Base.xbjwgl").replaceAll("@","")%>';//系部教务管理
	var admin = '<%=MyTools.getProp(request, "Base.admin").replaceAll("@","")%>';//管理员

	var iKeyCode = ''; //定义主键
	var pageNum = 1;   //datagrid初始当前页数
	var pageSize = 20; //datagrid初始页内行数
	var enterColor = '';//鼠标进入前颜色
	
	var mzts = 0;//每周天数
	var sw = 0;//上午
	var zw = 0;//中午
	var xw = 0;//下午
	var ws = 0;//晚上
	var zjs = 0;//总节数
	var jcsj = '';//节次时间
	var xnxq = '';
	var jxxz = '';
	var row = '';
	var classId = '';
	
	var timeOrderArray = new Array();
	var classKCB = '';//课程表明细编号,时间序列,授课计划明细编号,课程名称,授课教师姓名,授课教师编号,实际场地编号,实际场地名称,授课周次
	var stuNum = 0;//当前班级人数
	var curSelectId = ''; //选中单元格id
	var curTargetId = ''; //目标单元格id
	var selectArea = '';//选择的课程的区域
	var targetArea = '';//目标区域
	var lastIndex=-1;//保存上次选中的授课计划
	var PK_XNXQMC='';
	var PK_XNXQBM='';
	var PK_KSZBBH='';
	
	$(document).ready(function(){
		initData();//页面初始化加载数据
		initDialog();
		$('#loadingMask').hide();
		if(sAuth.indexOf(jxzgxz)>-1 || sAuth.indexOf(qxjdzr)>-1 || sAuth.indexOf(admin)>-1 || sAuth.indexOf(xbjwgl)>-1 || sAuth.indexOf(qxjwgl)>-1 || sAuth.indexOf(xbjdzr)>-1){
			
		}else{
			$("#CXFSTable").hide();
		}
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
				initCombobox(xnxq,jxxz);//初始化下拉框
				loadGridKCAP(); 
			}
		});
	}
	
	function initCombobox(xnxq,jxxz){
		//加载下拉框数据
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
	
				}
			},
			//下拉列表值改变事件
			onChange:function(data){
				loadGridKCAP();
			}
		});
				
		$('#JXXZ').combobox({
			data:jxxz,
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			panelHeight:'100', //combobox高度
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
		

	//显示考试安排
	function loadGridKCAP(){
		$('#semesterList').datagrid({
			url : '<%=request.getContextPath()%>/Svl_examSet?active=listkcap'+'&xnxq='+$('#XNXQ').combobox('getValue')+'&jxxz='+$('#JXXZ').combobox('getValue'),
			title:'',
			width:'100%',
			nowrap: false,
			fit:true, //自适应父节点宽度和高度
			showFooter:true,
			striped:true,
			pagination:false,
			pageSize:pageSize,
			singleSelect:true,
			pageNumber:pageNum,
			rownumbers:true,
			fitColumns: true,
			columns:[[
 				//field为读取数据的数据名，title为显示的数据名，width宽度设置，align数字在表格中显示的位置
				{field:'学年学期编码',title:'学年学期编码',width:fillsize(0.15),align:'center',
					formatter:function(value,rec){
						var xnxqbm=rec.ex_xnxq+rec.ex_jxxz;
						return xnxqbm;
					}
				},
				{field:'学年学期名称',title:'学年学期名称',width:fillsize(0.4),align:'center'},
				{field:'ex_ksmc',title:'考试名称',width:fillsize(0.4),align:'center'},
// 				{field:'ex_kslx',title:'考试类型',width:fillsize(0.2),align:'center',
// 					formatter:function(value,rec){
// 						var kslxnum="";
// 						if(rec.ex_kslx=="1"){
// 							kslxnum="普通考试";
// 						}else if(rec.ex_kslx=="2"){
// 							kslxnum="补考";
// 						}else if(rec.ex_kslx=="3"){
// 							kslxnum="大补考";
// 						}
// 						return kslxnum;
// 					}
// 				},
// 				{field:'ex_jzzs',title:'上课截止周数',width:fillsize(0.1),align:'center'},
// 				{field:'ex_ksrq',title:'考试日期',width:fillsize(0.4),align:'center'},
				{field:'col4',title:'设置',width:fillsize(0.2),align:'center',
					formatter:function(value,rec){
						return "<input type='button' value='[详情]' onclick='examtableDetail(\"" + rec.学年学期名称 + "\",\"" + (rec.ex_xnxq+rec.ex_jxxz) + "\",\"" + rec.考场安排主表编号 + "\");' style=\"cursor:pointer;\">";
					}
				}
			]],
			//双击某行时触发
			onDblClickRow:function(rowIndex,rowData){},
			//读取datagrid之前加载
			onBeforeLoad:function(){},
			//单击某行时触发
			onClickRow:function(rowIndex,rowData){
				row=rowData;
				iKeyCode=rowData.考场安排主表编号;
				ksrq=rowData.ex_ksrq;
				PK_XNXQMC=rowData.学年学期名称;
				PK_XNXQBM=(rowData.ex_xnxq+rowData.ex_jxxz);
				PK_KSZBBH=rowData.考场安排主表编号;
			},
			//加载成功后触发
			onLoadSuccess: function(data){
				
			}
		});
				
	}			
	
	/**显示班级考试列表
		@PK_XNXQMC 学年学期名称
		@PK_XNXQBM 学年学期编码
	**/
	function examtableDetail(PK_XNXQMC, PK_XNXQBM, PK_KSZBBH){ 
		lastIndex=-1;
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_examSet',
			data : 'active=cxfsCombobx',
			dataType:"json",
			success : function(data) { 
				//加载查询方式下拉框数据
				$('#CXFS').combobox({
					data:data[0].cxfs,
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
					onChange:function(data){
						var msgStr = '';
						$('#classTimetable').dialog({
							title:(PK_XNXQMC+'监考教师查询').replace(/ /g, "")
						});
						lastIndex=-1;
						if(data == 'jscx'){
							$('#CXFSTable').panel('resize',{height:62});
							$('#jscxtj').show();
							
							//加载教师信息TREE
							loadTeaTree(PK_XNXQBM, PK_KSZBBH);
						}else{
							$('#CXFSTable').panel('resize',{height:31});
							$('#jscxtj').hide();
							$('#ic_jsxm').val('');
							//加载班级信息TREE
							loadClassTree2(PK_XNXQBM, PK_KSZBBH);
						}
						//初始化空白课程表
						initBlankKCAP(PK_XNXQBM, PK_KSZBBH, msgStr);
					}
				});
				
			}
		});
		
	}
	
	/**加载教师信息TREE
		@PK_ZYDM 专业代码
	**/
	function loadTeaTree(PK_XNXQBM,PK_KSZBBH){ 
		$('#Tree').tree({
			checkbox: false,
			url:'<%=request.getContextPath()%>/Svl_examSet?active=queTeaTree&AUTH=' + sAuth + '&level=0&PK_XNXQBM=' + PK_XNXQBM + '&PK_KSZBBH=' + PK_KSZBBH +'&iUSERCODE='+iUSERCODE +'&ic_jsxm='+encodeURI(encodeURI($('#ic_jsxm').val())),
		    onClick:function(node){
		    	//判断点击的是不是老师
		    	loadClassKCJS(PK_XNXQBM, node.id, node.text);
			},
			onDblClick:function(node){
			
			},
		    onBeforeExpand:function(node,param){
			  	//$('#teacherTree').tree('options').url="< %=request.getContextPath()%>/Svl_Kbcx?active=queTeaTree&sAuth=" + sAuth + "&level=1&PK_XNXQBM=" + PK_XNXQBM + "&PK_ZYDM=" + PK_ZYDM + "&parentCode="+node.id;
			},
			onLoadSuccess:function(node, data){ 
				$("#Tree").show();
			}
		});
	};
	
	/**加载班级信息TREE
	@PK_XNXQBM 学年学期编码
	**/
function loadClassTree2(PK_XNXQBM, PK_KSZBBH){ 
	$('#Tree').tree({
		checkbox: false,
		url:'<%=request.getContextPath()%>/Svl_examSet?active=queryTree&level=0'+'&AUTH='+sAuth+'&parentId='+'&iUSERCODE='+iUSERCODE+'&XNXQ='+PK_XNXQBM,
		onClick:function(node){ 
			//判断点击的是不是班级
	    	if($('#Tree').tree('getParent', node.target) != null){
	    		if(lastIndex!=node.id){
					classId=node.id;
					lastIndex=node.id;
	    			loadClassKCAP(PK_XNXQBM, PK_KSZBBH, classId);
	    			$('#saveKCAP').linkbutton('enable');
				}
				parentId=node.id;
	    	}
		},
//			onBeforeLoad:function(row,param){     //分层显示treegrid
//				$('#classTree').tree('options').url='< %=request.getContextPath()%>/Svl_Skjh?active=queryTree&AUTH='+sAuth+'&parentId='+'&iUSERCODE='+iUSERCODE+'&GS_XNXQBM='+(xnxqVal+jxxzVal);
//			},
		onBeforeExpand:function(node,param){
	  		$('#Tree').tree('options').url='<%=request.getContextPath()%>/Svl_Skjh?active=queryTree&level=1'+'&AUTH='+sAuth+'&parentId='+node.id+'&iUSERCODE='+iUSERCODE+'&GS_XNXQBM='+PK_XNXQBM;
		},
		onLoadSuccess:function(data){
			$('#Tree').show();
	    }
	});
};
	
		
	/**读取当前班级课程表和未排课程表格
		@PK_XNXQBM 学年学期编码
		@classCode 班级编号
	**/
	function loadClassKCJS(PK_XNXQBM, teacherCode, teachername){
		$('#divPageMask').show();//监考信息遮罩层
		document.getElementById('kcbContent').scrollTop = 0;
		
		//清空课程表内容
		$('.kcbTdCentent').removeAttr('title');
		$('.kcbTdCentent').html('&nbsp;');
		$('.kcbTdCentent').css('color', '');
		
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_examSet',
			data : 'active=loadClassKCJS&sAuth=' + sAuth + '&GG_XNXQBM=' + PK_XNXQBM + '&teacherCode=' + teacherCode,
			dataType:"json",
			success : function(data) {
				classKCB = data[0].kcb;//课程表明细编号,班级排课状态,时间序列,授课计划明细编号,授课教师编号,课程名称,授课教师姓名,实际场地编号,实际场地名称,授课周次,授课周次详情
				
				fillKCJS(PK_XNXQBM, teacherCode, teachername);//填充当前课程表单元格内的课程信息
				$('#divPageMask').hide();
			}
		});
	}
	
	/**读取当前班级课程表和未排课程表格
	@PK_XNXQBM 学年学期编码
	@classCode 班级编号
**/
function loadClassKCAP(PK_XNXQBM, PK_KSZBBH, classId){
	$('#divPageMask').show();//监考信息遮罩层
	document.getElementById('kcbContent').scrollTop = 0;
	//清空课程表内容
	$('.kcbTdCentent').removeAttr('title');
	$('.kcbTdCentent').html('&nbsp;');
	$('.kcbTdCentent').css('color', '');
	
	$.ajax({
		type : "POST",
		url : '<%=request.getContextPath()%>/Svl_examSet',
		data : 'active=loadClassKCAP&sAuth=' + sAuth + '&XNXQ=' + PK_XNXQBM + '&KCAPZBBH=' + PK_KSZBBH + '&XZBDM=' + classId,
		dataType:"json",
		success : function(data) {
			classKCB = data[0].kcb;//课程表明细编号,班级排课状态,时间序列,授课计划明细编号,授课教师编号,课程名称,授课教师姓名,实际场地编号,实际场地名称,授课周次,授课周次详情
								
			fillKcb(PK_XNXQBM, classId);//填充当前课程表单元格内的课程信息
			
			$('#divPageMask').hide();
		}
	});
}
	
	
	/**填充当前课程表单元格内的课程信息
		@PK_XNXQBM 学年续期编码
		@classCode 班级编号
	**/
	function fillKCJS(PK_XNXQBM, teacherCode, teachername){
		var tempContent = '';
		var tempTitle = '';
		
		for(var i=0; i<classKCB.length; i++){
			tempTitle = '';
			if(classKCB[i].考场安排明细编号 != ''){ 
// 				var chgsjxl="0"+(parseInt(classKCB[i].时间序列.split("#")[0])+1)+"0"+(parseInt(classKCB[i].时间序列.split("#")[1])+1);
				var chgsjxl=classKCB[i].时间序列;
			
				tempTitle = classKCB[i].课程名称 + '\n' + classKCB[i].监考教师姓名 + '\n' + classKCB[i].行政班名称;
// 				if(classKCB[i].场地名称 != '' && typeof(classKCB[i].场地名称)!='undefined'){
// 					tempTitle += '\n' + classKCB[i].场地名称;
// 				}
				
				//判断如果当前单元的状态是固排或者课程类型不符，禁止操作该单元格。
				
				tempContent = '<font>' + classKCB[i].课程名称 + '<br/><br/>' + classKCB[i].监考教师姓名 + '<br/><br/>' + classKCB[i].行政班名称 ;
				
// 				if(classKCB[i].场地名称 != '' && typeof(classKCB[i].场地名称)!='undefined'){
// 					tempContent += '<br/><br/>' + classKCB[i].场地名称;
// 				}
				tempContent += '</font>'
			
				$('#kcb_' + chgsjxl).attr('title', tempTitle);
				$('#kcb_' + chgsjxl).html(tempContent);
			}
			
			$('#kcb_' + chgsjxl).attr('name', classKCB[i].考场安排明细编号);
		}
		
	

	}
	
	/**填充当前课程表单元格内的课程信息
	@PK_XNXQBM 学年续期编码
	@classCode 班级编号
	**/
	function fillKcb(PK_XNXQBM, classCode){
		var tempContent = '';
		var tempTitle = '';
		
		for(var i=0; i<classKCB.length; i++){
			var tempContent = '';
			var tempTitle = '';
			//判断节数是否大于0
			if(classKCB[i].节数 > 0){
				tempTitle = '';
				tempContent = '<font>' + classKCB[i].课程名称 + '<br/><br/>' + classKCB[i].监考教师姓名 ;
				
// 				if(classKCB[i].场地名称 != '' && typeof(classKCB[i].场地名称)!='undefined'){
// 					tempContent += '<br/><br/>' + classKCB[i].场地名称;
// 				}
				tempContent += '</font>'
				
				tempTitle = classKCB[i].课程名称  + '\n' + classKCB[i].监考教师姓名 ;
// 				if(classKCB[i].场地名称 != '' && typeof(classKCB[i].场地名称)!='undefined'){
// 					tempTitle += '\n' + classKCB[i].场地名称;
// 				}
				
				$('#kcb_' + classKCB[i].时间序列).attr('title', tempTitle);
				$('#kcb_' + classKCB[i].时间序列).html(tempContent);
			}
		}
	
	}
	
	
	/**初始化空白课程表和未排课程表格
		@PK_XNXQBM 学年学期编码
		@PK_ZYDM 专业代码
		@msgStr 提示信息
	**/
	function initBlankKCAP(PK_XNXQBM, PK_KSZBBH, msgStr){ 
		//查询当前学期设置的每周天数和每天节数
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_examSet',
			data : 'active=initBlankKCAP&GG_XNXQBM=' + PK_XNXQBM + '&PK_KSZBBH=' + PK_KSZBBH,
			dataType:"json",
			success : function(data) { 
				sw = parseInt(data[0].sw, 10);//上午
				zw = parseInt(data[0].zw, 10);//中午
				xw = parseInt(data[0].xw, 10);//下午
				ws = parseInt(data[0].ws, 10);//晚上
				zjs = sw+zw+xw+ws;
				jcsj = data[0].jcsj.split(',');
				
				//生成全部时间序列数组
				var ksts=(data[0].ksrq+"").split(","); 
				for(var i=1; i<ksts.length+1; i++){
					for(var j=1; j<zjs+1; j++){
						timeOrderArray.push((i<10?'0'+i:''+i) + (j<10?'0'+j:''+j));
						timeOrderArray.push('');
					}
				}	
				var tdWidth = 100/(ksts.length+1);
				var tempNum = 0;
				var kcbContent = '<table style="width:100%; height:100%; border-top:1px solid #95B8E7; border-right:1px solid #95B8E7;" cellspacing="0" cellpadding="0">';
				
				for(var i=-1; i<zjs; i++){
					kcbContent += '<tr class="kcbTrStyle" onmouseout="window.event? window.event.cancelBubble = true : e.stopPropagation();"';
					
					if(i == -1){
						kcbContent += ' style="height:45px;"';	
					}
					kcbContent += '>';

					for(var j=-1; j<ksts.length; j++){ 
						
						//添加左上角空单元格
						if(i==-1 && j==-1){
							kcbContent += '<td colspan="3" class="kcbTdStyle titleBG splitTd" onmouseout="window.event? window.event.cancelBubble = true : e.stopPropagation();">&nbsp;</td>';
							continue;
						}
						
						//判断添加星期数
						if(i==-1 && j>-1){
							kcbContent += '<td class="kcbTdStyle kcbTitle titleBG splitTd" onmouseout="window.event? window.event.cancelBubble = true : e.stopPropagation();">' + ksts[j] + '</td>';	
							continue;
						}
						
						//判断添加节次数
						if(i>-1 && j==-1){
							//上午课程
							if(i<sw &&　sw>0){
								//判断第一行
								if(i == 0){
									kcbContent += '<td id="width_sw" rowspan="'+sw+'" class="titleTdStyle kcbTitle titleBG';
									//判断是否还有课
									if(i+sw < zjs){
										kcbContent += ' splitTd';
									}
									kcbContent += '"></td>';
								}
								
								kcbContent += '<td id="width_xh" class="titleTdStyle titleBG';
								//判断是否还有课
								if((i+1)==sw && (i+1)<zjs){
									kcbContent += ' splitTd';
								}
								kcbContent += '">' + (i+1) + '</td>';
								
								kcbContent += '<td id="width_sjd" class="titleTdStyle titleBG';
								//判断是否还有课
								if((i+1)==sw && (i+1)<zjs){
									kcbContent += ' splitTd';
								}
								kcbContent += '">';
								if(jcsj.length > i){
									kcbContent += jcsj[i];
								}else{
									kcbContent += '&nbsp;';
								}
								kcbContent += '</td>';
							}
							
							//中午课程
							if(i>=sw && i<(sw+zw) && zw>0){
								//判断第一行
								if(i == sw){
									kcbContent += '<td rowspan="'+zw+'" class="titleTdStyle kcbTitle titleBG';
									//判断是否还有课
									if(i+zw < zjs){
										kcbContent += ' splitTd';
									}
									kcbContent += '"></td>';
								}
								
								kcbContent += '<td class="titleTdStyle titleBG';
								//判断是否还有课
								if((i+1)==(sw+zw) && (i+1)<zjs){
									kcbContent += ' splitTd';
								}
								kcbContent += '">' + (i+1) + '</td>';
								kcbContent += '<td class="titleTdStyle titleBG';
								//判断是否还有课
								if((i+1)==(sw+zw) && (i+1)<zjs){
									kcbContent += ' splitTd';
								}
								kcbContent += '">';
								if(jcsj.length > i){
									kcbContent += jcsj[i];
								}else{
									kcbContent += '&nbsp;';
								}
								kcbContent += '</td>';
							}
							
							//下午课程
							if(i>=(sw+zw) && i<(sw+zw+xw) && xw>0){
								//判断第一行
								if(i == sw+zw){
									kcbContent += '<td rowspan="'+xw+'" class="titleTdStyle kcbTitle titleBG';
									//判断是否还有课
									if(i+xw < zjs){
										kcbContent += ' splitTd';
									}
									kcbContent += '"></td>';
								}
								
								kcbContent += '<td class="titleTdStyle titleBG';
								//判断是否还有课
								if((i+1)==(sw+zw+xw) && (i+1)<zjs){
									kcbContent += ' splitTd';
								}
								kcbContent += '">' + (i+1) + '</td>';
								kcbContent += '<td class="titleTdStyle titleBG';
								//判断是否还有课
								if((i+1)==(sw+zw+xw) && (i+1)<zjs){
									kcbContent += ' splitTd';
								}
								kcbContent += '">';
								if(jcsj.length > i){
									kcbContent += jcsj[i];
								}else{
									kcbContent += '&nbsp;';
								}
								kcbContent += '</td>';
							}
							
							//晚上课程
							if(i>=(sw+zw+xw) && i<(sw+zw+xw+ws) && ws>0){
								//判断第一行
								if(i == (sw+zw+xw)){
									kcbContent += '<td rowspan="'+ws+'" class="titleTdStyle titleBG kcbTitle"></td>';
								}
								kcbContent += '<td class="titleTdStyle titleBG">' + (i+1) + '</td>'
											+ '<td class="titleTdStyle titleBG">';
								if(jcsj.length > i){
									kcbContent += jcsj[i];
								}else{
									kcbContent += '&nbsp;';
								}
								kcbContent += '</td>';
							}
							
							continue;
						}
						
						//拼接时间序列
						if((j+1) < 10){
							tempOrder = '0'+(j+1);
						}else{
							tempOrder = j+1;
						}
						if((i+1) < 10){
							tempOrder += '0'+(i+1);
						}else{
							tempOrder += i+1;
						}

						//添加普通单元格
						kcbContent += '<td id="kcb_' + tempOrder + '" class="kcbTdStyle kcbTdCentent';
						if(((i+1)==sw && zw>0) || ((i+1)==(sw+zw) && xw>0) || ((i+1)==(sw+zw+xw) && ws>0)){
							kcbContent += ' splitTd';
						}
						kcbContent += '" onmouseover="tdMouseOver(this.id);" onmouseout="tdMouseOut(this.id);" ' + 
									' ondblclick="">&nbsp;</td>';
					}
					kcbContent += '</tr>';
				}
				
				kcbContent += '</table>';
				var mask = '<div id="timetableMask" style="position:absolute; top:0; z-index:99999; width:100%; height:100%; background:url(0);"></div>';
				$('#kcbContent').html(kcbContent);
				mask = '<div id="wpkcMask" style="position:absolute; top:0; z-index:99999; width:100%; height:100%; background:url(0);"></div>';
				
				$('#classTimetable').dialog('open');
				$('.kcbTdStyle').css('width', tdWidth+'%');
				$('#width_sw').css('width', '50px');
				$('#width_xh').css('width', '20px');
				$('#width_sjd').css('width', '130px');
				$('.kcbTrStyle').css('height', parseInt($('#kcbContent').css('height').substring(0, $('#kcbContent').css('height').length-2)-1, 10)/(sw+xw+ws+1)+'px');
				
			}
		});
	}
	
	/**加载 dialog控件**/
	function initDialog(){
		$('#classTimetable').dialog({
			maximized:true,
			modal:true,
			closed: true,   
			cache: false, 
			draggable:false,//是否可移动dialog框设置
			toolbar:[{
				text:'返回',
				iconCls:'icon-back',
				handler:function(){
					$('#classTimetable').dialog('close');
				}
			}],
			onBeforeClose:function(){
			},
			//关闭事件
			onClose:function(data){
				$('#kcbContent').html('');
				$('#ic_jsxm').val('');
			}
		});
	}
	
	/**课程表及未排课程单元格鼠标移入事件
		@id 当前td的id
	**/
	function tdMouseOver(tdID){
		curTargetId = tdID;
		enterColor = $('#' + tdID).css('background-color');
		$('#' + tdID).css('background-color', '#FFE48D');
	}
	
	/**课程表及未排课程单元格鼠标移出事件
		@tdID 单元格id
	**/
	function tdMouseOut(tdID){
		curTargetId = '';
		//判断当前不是选中课程状态  或  选中课程状态下当前移出的单元格不是选择的单元格
		if(selectArea=='' || tdID!=curSelectId){
			$('#' + tdID).css('background-color', enterColor);
		}
		//阻止冒泡事件
		window.event? window.event.cancelBubble = true : e.stopPropagation();
	}
	
	function doToolbar(id){
		//查询
		if(id == 'query'){
			examtableDetail(PK_XNXQMC, PK_XNXQBM, PK_KSZBBH);
		}
	}
	

</script>
</html>