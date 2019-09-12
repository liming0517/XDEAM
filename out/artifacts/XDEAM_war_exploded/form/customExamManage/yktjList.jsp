<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="java.util.Vector"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.src.develop.store.user.*"%>
<%
	/**
		创建人：yangda
		Create date: 2017.08.31
		功能说明：月考统计
		页面类型:列表及模块入口
		修订信息(有多次时,可有多个)
		修订日期:
		原因:
		修订人:
		修订时间:
	**/
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
    <title>月考统计</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> 
	<!-- JQuery 专用4个文件 -->
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/icon.css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
  	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/publicScript.js"></script>
  	<style>
		#sxsksList {
			width: 100%;
			text-align: center;
			border-left: 1px solid #95B8E7;
			border-top: 1px solid #95B8E7;
			border-collapse: collapse; //collapse的意思是相邻边框合并
		}
		
		.sxsksListTr {
			height: 32px;
		}
		
		.sxsksListTd {
			font-size: 16;
			border-right: 1px solid #95B8E7;
			border-bottom: 1px solid #95B8E7;
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
	UserProfile up = new UserProfile(request,
			MyTools.getSessionUserCode(request));
	String userName = up.getUserName();
	if (userName == null) {
		userName = "";
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
	if(v!=null && v.size()>0){
		for(int i=0; i<v.size(); i++){
			sAuth += "@"+MyTools.StrFiltr(v.get(i))+"@O";
		}
		sAuth = sAuth.substring(0, sAuth.length()-1);
	}
	// 如果无法获取人员信息，则自动跳转到登陆界面
%>
 <body class="easyui-layout" style="width:100%;height:100%;">
	<div id="north" region="north" title="月考统计" style="height:91px;" >
		<table id="ee" width="100%">
			<tr>
				<td>
					<a href="#" id="hztj" class="easyui-linkbutton" plain="true" iconcls="icon-submit" onClick="doToolbar(this.id);">汇总统计</a>
				</td>
			</tr>
		</table>
		<table id="ee" singleselect="true" width="100%" class="tablestyle">
			<tr>
				<td style="width:150px;" class="titlestyle">班级</td>
				<td>
					<select name="ic_bjdm" id="ic_bjdm" class="easyui-combobox"
						style="width:180px;">
					</select>
				</td>
				<td style="width:150px;" class="titlestyle">课程</td>
				<td>
					<select name="ic_kcdm" id="ic_kcdm" class="easyui-combobox"
						style="width:180px;">
					</select>
				</td>
				<td style="width:200px;text-align:center" align="left"><a href="#" id="query"
					class="easyui-linkbutton" plain="true" iconcls="icon-search"
					onclick="doToolbar(this.id)">查询</a></td>
			</tr>
		</table>
	</div>
	<div region="center">
		<table id="ykbjxkList" style="width:100%;"></table>
	</div>
	
	<!-- 月考详情 -->
	<div id="ykxqDialog" style="overflow:hidden;">
		<div style="width:100%; height:100%;" class="easyui-layout">
			<%-- 遮罩层 --%>
		   	<div id="divPageMask" class="maskStyle">
		   		<div id="maskFont">月考信息导出中...</div>
		   	</div>
			<div region="north" class="tablestyle" style="width:100%; height:32px; overflow:hidden;">
				<table id="ee" width="100%" class="tablestyle">
					<tr>
						<td class="titlestyle" style="width:120px;">学年学期</td>
						<td>
							<select name="ic_qsxnxq" id="ic_qsxnxq" class="easyui-combobox" style="width:200px;">
							</select>
							<span style="margin-left:10px; margin-right:10px;">至</span>
							<select name="ic_jsxnxq" id="ic_jsxnxq" class="easyui-combobox" style="width:200px;">
							</select>
						</td>
						<td style="width:190px;">
							<a href="#" onclick="doToolbar(this.id)" id="count" class="easyui-linkbutton" plain="true" iconcls="icon-ok" style="float:left;">统计</a>
							<a href="#" onclick="doToolbar(this.id)" id="qhtb" class="easyui-linkbutton" plain="true" iconcls="icon-reload">切换为图表显示</a>
							<a href="#" onclick="doToolbar(this.id)" id="qhlb" class="easyui-linkbutton" plain="true" iconcls="icon-reload">切换为列表显示</a>
						</td>
					</tr>
				</table>
			</div>
			
			<div region="center">
		    	<table id="loadMask" style="width:100%; height:100%; text-align:center; display:none;">
					<tr><td><img src="<%=request.getContextPath()%>/images/loading_3.gif"></td></tr>
				</table>
		    	<div id="chartDiv" style="height:100%; width:100%;"></div>
		    	<table id="scoredatagrid"></table>
			</div>
		</div>
	</div>
	
	<!-- 成绩明细对话框 -->
	<div id="scoreInfoDialog">
		<div class="easyui-layout" style="width:100%; height:100%;">
			<div region="north" style="height:32px;">
				<table id="ee" width="100%" class="tablestyle">
					<tr>
						<td style="width:80px;" class="titlestyle">姓名</td>
						<td>
							<input id='ic_xm' name='ic_xm' class="easyui-textbox" style="width:140px;"/>
						</td>
						<td style="width:80px;" align="center">
							<a href="#" onclick="doToolbar(this.id)" id="queScoreList" class="easyui-linkbutton" plain="true" iconcls="icon-search">查询</a>
						</td>
					</tr>
			    </table>
			</div>
			<div region="center">
				<table id="scoreInfoList"></table>
			</div>
		</div>
	</div>
	
	<!-- 汇总统计对话框 -->
	<div id="hztjDialog">
		<table id="ee" width="100%" class="tablestyle">
			<tr>
				<td class="titlestyle" style="width:120px;">学年学期</td>
				<td>
					<select name="ic_xnxq" id="ic_xnxq" class="easyui-combobox" style="width:200px;">
					</select>
				</td>
			</tr>
			<tr>
				<td  colspan="2" style="text-align:center">
					<a href="#" onclick="doToolbar(this.id)" id="hztj2" class="easyui-linkbutton" plain="true" iconcls="icon-ok">汇总统计</a>
				</td>
			</tr>
		</table>
	</div>
	
	<!-- 下载excel -->
	<iframe id="exportIframe" src="" style="width:0; height:0;"></iframe>
	
	<!-- 汇总统计导出页面 -->
	<div id="exportHZTJ">
		<!--引入编辑页面用Ifram-->
		<iframe id="exportHZTJiframe" name="exportHZTJiframe" src='' style='width:100%; height:100%;' frameborder="0" scrolling="no"></iframe>
	</div>
	
	<a id="pageOfficeLink" href="#" style="display:none;"></a>
	
</body>
  <script src="<%=request.getContextPath()%>/script/ECharts/echarts-all.js"></script>
  <script>
  	var sAuth = '<%=sAuth%>';
  	var pageNum = 1;   //datagrid初始当前页数
	var pageSize = 20; //datagrid初始页内行数
	var row = '';      //行数据
	var iKeyCode = ''; //定义主键
	var xqtitle = ''; //月考详情dialog标题
	var curXnxq = ''; //当前学年学期
	var saveBJDM = ''; //保存班级代码
	var saveKCDM = ''; //保存课程代码
	var saveBJ = ''; //保存班级
	var saveKC = ''; //保存课程
	var formatType='1';//1是图表2是datagrid
	var loadFlag = 0; //加载次数
	var saveKSXKBH = '';// 保存考试学科编号
	var saveXnxqData = '';// 保存学年学期Data
	var curExamArray = ''; //考试编号集合
	var curKsbh = ''; //考试编号
	var pofOpenType = '<%=MyTools.getProp(request, "Base.pofOpenType")%>';
	var USERCODE="<%=MyTools.getSessionUserCode(request)%>";
	
  	$(document).ready(function(){
		initDialog();//初始化对话框
		initData();//页面初始化加载数据
		$('#qhtb').hide();
	});
	
	//页面初始化加载数据
	function initData(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Yktj',
			data : 'active=initData&page=' + pageNum + '&rows=' + pageSize + '&sAuth=' + sAuth,
			dataType:"json",
			success : function(data) {
				if(data[0].curXnxq != ''){
					curXnxq = data[0].curXnxq;
				}
				loadGrid(data[0].listData);
				initCombobox(data[0].bjData, data[0].kcData, data[0].xnxqData);
			}
		});
	}
	//加载月考班级学科申请列表
	function loadGrid(listData){
		$("#ykbjxkList").datagrid({
			data:listData,
			title:'月考统计信息',
			width:'100%',
			nowrap:false,
			fit:true, //自适应父节点宽度和高度
			showFooter:true,
			striped:true,
			pagination:true,
			pageSize:pageSize,
			singleSelect:true,
			pageNumber:pageNum,
			rownumbers:true,
			fitColumns:true,
			columns:[[
				//field为读取数据的数据名，title为显示的数据名，width宽度设置，align数字在表格中显示的位置
				{field:'BJ',title:'班级',width:fillsize(0.25),align:'center'},
				{field:'KC',title:'课程',width:fillsize(0.25),align:'center'},
				{field:'SKJSXM',title:'授课教师',width:fillsize(0.2),align:'center'},
				{field:'col4',title:'操作',width:fillsize(0.2),align:'center',
					formatter:function(value,rec){
						return "<input type='button' value='[详情]' onclick='loadYKXQ(\""+rec.BJDM+"\",\""+rec.KCDM+"\",\""+rec.BJ+"\",\""+rec.KC+"\",\""+rec.SKJSXM+"\");' style=\"cursor:pointer;\">";
					}
				}
			]],
			//双击某行时触发
			onDblClickRow:function(rowIndex,rowData){},
			//读取datagrid之前加载
			onBeforeLoad:function(){},
			//单击某行时触发
			onClickRow:function(rowIndex,rowData){
			},
			//加载成功后触发
			onLoadSuccess: function(data){
			}
		});
		
		$("#ykbjxkList").datagrid("getPager").pagination({
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
	
	/**读取datagrid数据**/
	function loadData(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Yktj',
			data : 'active=query&page=' + pageNum + '&rows=' + pageSize + '&BJDM=' + $('#ic_bjdm').combobox('getValue') + '&KCDM=' + $('#ic_kcdm').combobox('getValue') + '&sAuth=' + sAuth,
			dataType:"json",
			success : function(data) {
				loadGrid(data[0].listData);
			}
		});
	}
	
	function initDialog(){
		//月考详情
		$('#ykxqDialog').dialog({   
			title:'',
			width: 900,//宽度设置   
			height: 550,//高度设置 
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
				saveBJDM = ''; //保存班级代码
				saveKCDM = ''; //保存课程代码
				saveBJ = ''; //保存班级
				saveKC = ''; //保存课程
			}
		});
		
		$('#scoreInfoDialog').dialog({   
			title: '成绩信息',   
			width: 700,//宽度设置   
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
				$('#ic_xm').textbox('setValue', '');
				$('#scoreInfoList').datagrid('loadData',{total:0,rows:[]});
				saveKSXKBH = '';
			}
		});
		
		$('#hztjDialog').dialog({   
			title: '汇总统计',   
			width: 400,//宽度设置   
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
			}
		});
		
		$('#exportHZTJ').dialog({   
			title: '汇总统计',   
			width: 900,//宽度设置   
			height:600,//高度设置 
			modal:true,
			closed: true,   
			cache: false, 
			maximized:true,
			draggable:true,//是否可移动dialog框设置
			//读取事件
			onLoad:function(data){
			},
			//关闭事件
			onClose:function(data){
			}
		});
		
	}
	
	function initCombobox(bjData, kcData, xnxqData){
		saveXnxqData = xnxqData;
		//班级下拉框
		$('#ic_bjdm').combobox({
			data:bjData,
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			panelHeight:'140', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data != ''){
					//初始化combobox时赋值
					$(this).combobox('setValue', '');
				}
			},
			//下拉列表值改变事件
			onChange:function(data){}
		});
		
		//课程下拉框
		$('#ic_kcdm').combobox({
			data:kcData,
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			panelHeight:'140', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data != ''){
					//初始化combobox时赋值
					$(this).combobox('setValue', '');
				}
			},
			//下拉列表值改变事件
			onChange:function(xnxq){
			}
		});
		
		//起始时间下拉框
		$('#ic_qsxnxq').combobox({
			data:xnxqData,
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			panelHeight:'140', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data != ''){
					if(curXnxq != ''){
						$(this).combobox('setValue', curXnxq);
					}else{
						//初始化combobox时赋值
						$(this).combobox('setValue', '');
					}
				}
			},
			//下拉列表值改变事件
			onChange:function(xnxq){
			}
		});
		
		//结束时间下拉框
		$('#ic_jsxnxq').combobox({
			data:xnxqData,
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			panelHeight:'140', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data != ''){
					if(curXnxq != ''){
						$(this).combobox('setValue', curXnxq);
					}else{
						//初始化combobox时赋值
						$(this).combobox('setValue', '');
					}
				}
			},
			//下拉列表值改变事件
			onChange:function(xnxq){
			}
		});
		
		//起始时间下拉框
		$('#ic_xnxq').combobox({
			data:xnxqData,
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			panelHeight:'140', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data != ''){
					if(curXnxq != ''){
						$(this).combobox('setValue', curXnxq);
					}else{
						//初始化combobox时赋值
						$(this).combobox('setValue', '');
					}
				}
			},
			//下拉列表值改变事件
			onChange:function(xnxq){
			}
		});
		
	}
	
	//按钮功能
	function doToolbar(id){
		//查询
		if(id == 'query'){
			loadData();
		}
		
		//统计
		if(id == 'count'){
// 			loadFlag=0;
			loadChart();
			loadScoreList();
			$('#loadMask').hide();
			$('#qhtb').hide();
			$('#qhlb').show();
// 			if(formatType == '1'){
// 				loadChart();
// 			}else if(formatType == '2'){
// 				loadScoreList();
// 			}
		}
		
		//切换为列表显示
		if(id == "qhlb"){
			$('#chartDiv').hide();
			$('#scoredatagrid').show();
// 			formatType = '2';
			$('#qhtb').show();
			$('#qhlb').hide(); 
		}
		
		if(id == "qhtb"){
			$('#scoredatagrid').hide();
			$('#chartDiv').show();
// 			formatType = '1';
			$('#qhtb').hide();
			$('#qhlb').show();
		}
		
		if(id == 'queScoreList'){
			if(saveKSXKBH !== ''){
				loadScoreDetailzhiding(saveKSXKBH);
			}else{
				loadScoreDetailzhiding2(curKsbh);
			}
		}
		
		if(id == "hztj"){
			$('#ic_xnxq').combobox('loadData',saveXnxqData);
			$('#hztjDialog').dialog('open');
		}
		
		//汇总统计
		if(id == "hztj2"){
			var xnxqbm = $('#ic_xnxq').combobox('getValue');
			
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_Yktj',
				data : "active=ykcount&xnxqbm=" + xnxqbm,
				dataType:"json",
				success : function(data){
					if(data[0].MSG == '当前学年学期有月考数据信息'){
						if(pofOpenType == 'normal'){
							$('#exportHZTJ').dialog('open');
							$("#exportHZTJiframe").attr("src","<%=request.getContextPath()%>/form/customExamManage/exportHztj.jsp?exportType=hztj&xnxqbm=" + xnxqbm + "&sAuth=" + sAuth +"&USERCODE="+USERCODE);
						}else{
							$.ajax({
								type : "POST",
								url : '<%=request.getContextPath()%>/Svl_Yktj',
								data : "active=loadClassPageOfficeLink&exportType=hztj&xnxqbm=" + xnxqbm + "&sAuth=" + sAuth +"&USERCODE="+USERCODE,
								dataType:"json",
								success : function(data){
									$('#pageOfficeLink').attr('href', data[0].linkStr);
									document.getElementById("pageOfficeLink").click();
								}
							});
						}
					}else{
						alertMsg(data[0].MSG);
					}
				}
			});
			
// 			if(pofOpenType == 'normal'){
// 				$('#exportHZTJ').dialog('open');
// 				$("#exportHZTJiframe").attr("src","< %=request.getContextPath()%>/form/customExamManage/exportHztj.jsp?exportType=hztj&xnxqbm=" + xnxqbm );
// 			}else{
// 				$.ajax({
// 					type : "POST",
// 					url : '< %=request.getContextPath()%>/Svl_Yktj',
// 					data : "active=loadClassPageOfficeLink&exportType=hztj&xnxqbm=" + xnxqbm,
// 					dataType:"json",
// 					success : function(data){
// 						$('#pageOfficeLink').attr('href', data[0].linkStr);
// 						document.getElementById("pageOfficeLink").click();
// 					}
// 				});
// 			}
		}
		
	}
	
	//【月考详情】
	function loadYKXQ(bjdm,kcdm,bj,kc,skjsxm){
// 		loadFlag = 0;
		saveBJDM = bjdm; //保存班级代码
		saveKCDM = kcdm; //保存课程代码
		saveBJ = bj; //保存班级
		saveKC = kc; //保存课程
		$('#ykxqDialog').dialog('open');
		if(skjsxm=="undefined"){ 
			xqtitle = bj+" "+kc+" 月考详情";
		}else{
			xqtitle = bj+" "+kc+"("+skjsxm+"老师) 月考详情";
		} 
		$('#ykxqDialog').panel({title: xqtitle});
		$('#ic_qsxnxq').combobox('loadData',saveXnxqData);
		$('#ic_jsxnxq').combobox('loadData',saveXnxqData);
		$('#loadMask').show();
		loadChart();
		loadScoreList();
// 		if(formatType == '1'){
// 			loadChart();
// 		}else if(formatType == '2'){
// 			loadScoreList();
// 		}
	}
	
	/**读取图表*/
	function loadChart(){
		$.ajax({
			type: "POST",
			url: '<%=request.getContextPath()%>/Svl_Yktj',
			async:false,
			data: "active=loadGradeyktjChart&BJDM=" + saveBJDM + "&KCDM=" + saveKCDM + "&QSXNXQ=" + $('#ic_qsxnxq').combobox('getValue') + "&JSXNXQ=" + $('#ic_jsxnxq').combobox('getValue')
					+ "&BJ=" + saveBJ + "&KC=" + saveKC,		
			dataType: 'json',
			success: function(datas){
				$('#loadMask').show();
// 				loadFlag++;
				curExamArray = datas[0].examInfo.split(',');
				//$('#loadMask').hide();
				showChart(datas[0].chartData);
// 				loadScoreList();
// 				if(loadFlag < 2){
// 					loadScoreList();
// 				}
			}
		});
	}
	
	/**显示图表*/
	function showChart(data){
		var chartDiv = echarts.init(document.getElementById('chartDiv'));
		if(data.series != undefined){
			chartDiv.showLoading({text:'正在努力的读取数据中...'});
			chartDiv.setOption(data);
			chartDiv.on(echarts.config.EVENT.CLICK, clickPoint);
			chartDiv.hideLoading();
			
			var option = {
				tooltip: {
		              trigger: 'axis', 
		              formatter: function(datas) 
		              {
		                  var res = datas[0].name + '<br/>';
		                  var val='';
		                  for(var i = 0, length = datas.length; i < length; i++) {
		                  if(datas[i].value!=''){
			                  	if(i!=0 && i%3==0){
			                  		val = (datas[i].value) + '%';
			                  	}else{
			                  		val = (datas[i].value);
			                  	}
		                  }else{
		                  		if(i!=0 && i%3==0){
			                  		val = '-%';
			                  	}else{
			                  		val = '-';
			                  	}
		                  }
		                    res += datas[i].seriesName + '：' + val + '<br/>';
		                  }
		                  return res;
		              }
		          }
			};
			chartDiv.setOption(option); 
		}else{
			chartDiv.showLoading({
				text : '暂无数据',
				effect : 'bubble',
				textStyle : {
					fontSize : 28
				}
			});
			
		}
		
		$('#loadMask').hide();
		$('#chartDiv').show();
		
// 		if(formatType == '1'){
// 			$('#loadMask').hide();
// 			$('#chartDiv').show();
// 		}
	}
	/**点击图标节点调用*/
	function clickPoint(param){
		curKsbh = curExamArray[param.dataIndex]; //考试编号
		$('#scoreInfoDialog').dialog('open');
		loadScoreDetailzhiding2(curKsbh);
		
	}
	
	
	//datagrid中的数据
	function loadScoreList(){
		//读取科目信息和成绩信息
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Yktj',
			data : "active=loadGradeyktjList&BJDM=" + saveBJDM + "&KCDM=" + saveKCDM + "&QSXNXQ=" + $('#ic_qsxnxq').combobox('getValue') + "&JSXNXQ=" + $('#ic_jsxnxq').combobox('getValue')
					+ "&BJ=" + saveBJ + "&KC=" + saveKC,
			dataType:"json",
			success : function(data){
				$('#scoredatagrid').datagrid({
					data:data[0].listData,
					nowrap: false,
					fit:true,
					toolbar:[{
						text:'导出',
						iconCls:'icon-submit',
						handler:function(){
							ExportExcel();
						}
					}],
					striped:true, //隔行变色
					singleSelect:true,
					fitColumns:true,
// 					frozenColumns:[[
// 						{field:'KSMC',title:'考试名称',width:fillsize(0.15),align:'center'}
// 					]],
					columns:[[
						//field为读取数据的数据名，title为显示的数据名，width宽度设置，align数字在表格中显示的位置
						{field:'KSMC',title:'考试名称',width:fillsize(0.2),align:'center'},
						{field:'ZGF',title:'最高分',width:fillsize(0.15),align:'center'},
						{field:'ZDF',title:'最低分',width:fillsize(0.15),align:'center'},
						{field:'PJF',title:'平均分',width:fillsize(0.15),align:'center'},
						{field:'JGL',title:'及格率',width:fillsize(0.15),align:'center',
							formatter:function(value,rec){
								var str = '';
								if(value != ''){
									str = changeTwoDecimal_f(value)+"%";
								}
								return str;
							}
						},
						{field:'col4',title:'操作',width:fillsize(0.2),align:'center',
							formatter:function(value,rec){
								return "<input type='button' value='[详情]' onclick='loadKSXQ(\""+rec.KSBH+"\",\""+rec.KSXKBH+"\",\""+saveBJDM+"\",\""+saveKCDM+"\");' style=\"cursor:pointer;\">";
							}
						}
					]],
					//双击某行时触发
					onDblClickRow:function(rowIndex,rowData){},
					//读取datagrid之前加载
					onBeforeLoad:function(){},
					onClickCell:function(rowIndex, field, value){
					},
					//单击某行时触发
					onClickRow:function(rowIndex, rowData){
					},
					//加载成功后触发
					onLoadSuccess: function(data){
// 						loadFlag++;
// 						if(loadFlag < 2){
// 						loadChart();
// 						}
						
						$('#loadMask').hide();
						$('#scoredatagrid').show();
						
// 						if(formatType == '2'){
// 							$('#loadMask').hide();
// 							$('#scoredatagrid').show();
// 						}
					}
				});
			}
		});
	}
	
	
	//打开详情
	function loadKSXQ(ksbh, ksxkbh, bjdm, kcdm ){
		$('#scoreInfoDialog').dialog('open');
		saveKSXKBH = ksxkbh;
		loadScoreDetailzhiding(ksxkbh); 
	} 
	
	
	/**读取学生成绩详情列表*/
	function loadScoreDetailzhiding(ksxkbh){
		$('#scoreInfoList').datagrid({
			url:'<%=request.getContextPath()%>/Svl_Yktj',
			queryParams:{"active":"queClassStuScoreList","KSXKBH":ksxkbh,"STUNAME":encodeURI($('#ic_xm').textbox('getValue'))},
			loadMsg : "信息加载中请稍侯!",
			fit:true,
			width:'100%',
			nowrap:false,
			striped:true,
			showFooter:true,
			singleSelect:true,
			fitColumns:true,
			columns:[[
				//field为读取数据的数据名，title为显示的数据名，width宽度设置，align数字在表格中显示的位置
				{field:'xm',title:'姓名',width:fillsize(0.3),align:'center'},
				{field:'cj',title:'成绩',width:fillsize(0.3),align:'center'},
				{field:'bjpm',title:'班级排名',width:fillsize(0.3),align:'center'}
			]],
			//成功加载时
			onLoadSuccess : function(data) {
			}
		});
	}
	
	/**读取学生成绩详情列表*/
	function loadScoreDetailzhiding2(ksbh){
		$('#scoreInfoList').datagrid({
			url:'<%=request.getContextPath()%>/Svl_Yktj',
			queryParams:{"active":"queClassStuScoreList2","KSBH":ksbh,"STUNAME":encodeURI($('#ic_xm').textbox('getValue')),"BJDM":saveBJDM,"KCDM":saveKCDM},
			loadMsg : "信息加载中请稍侯!",
			fit:true,
			width:'100%',
			nowrap:false,
			striped:true,
			showFooter:true,
			singleSelect:true,
			fitColumns:true,
			columns:[[
				//field为读取数据的数据名，title为显示的数据名，width宽度设置，align数字在表格中显示的位置
				{field:'xm',title:'姓名',width:fillsize(0.3),align:'center'},
				{field:'cj',title:'成绩',width:fillsize(0.3),align:'center'},
				{field:'bjpm',title:'班级排名',width:fillsize(0.3),align:'center'}
			]],
			//成功加载时
			onLoadSuccess : function(data) {
			}
		});
	}
	
	//导出学生考试成绩信息
	function ExportExcel(){
		$('#divPageMask').show();
		$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_Yktj',
				data : 'active=exportYK&BJ=' + saveBJ + '&KC=' + saveKC + '&QSXNXQ=' + $('#ic_qsxnxq').combobox('getValue')
						+ '&JSXNXQ=' + $('#ic_jsxnxq').combobox('getValue') + '&BJDM=' + saveBJDM + '&KCDM=' + saveKCDM,
				dataType:"json",
				success : function(data){
					if(data[0].MSG == '文件生成成功'){
						//下载文件到本地
						$("#exportIframe").attr("src", '<%=request.getContextPath()%>/form/scoreQuery/download.jsp?filePath=' + encodeURI(data[0].filePath));
					}else{
						alertMsg(data[0].MSG);
					}
					$('#divPageMask').hide();
				}
			});
	}
	
	//计算使用率 百分比后添加两位小数
	function changeTwoDecimal_f(x){
		var f_x = parseFloat(x);
		if (isNaN(f_x)){
			return 0;
		}
		var f_x = f_x.toFixed(4) * 100;
		var s_x = f_x.toString();
		if(s_x.length > 5){
			s_x = s_x.substring(0, 5);
			f_x = f_x.toFixed(2);
			s_x = f_x.toString();
		}
		var pos_decimal = s_x.indexOf('.');
		if (pos_decimal < 0){
			pos_decimal = s_x.length;
			s_x += '.';
		}
		while (s_x.length <= pos_decimal + 2){
			s_x += '0';
		}
		return s_x;
	}
	
  </script>
</html>
