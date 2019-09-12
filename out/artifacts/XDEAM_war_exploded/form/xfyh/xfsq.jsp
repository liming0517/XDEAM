<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="java.util.Vector"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.src.develop.store.user.*"%>
<%
	/**
		创建人：yangda
		Create date: 2017.09.28
		功能说明：学分申请
		页面类型:列表及模块入口
		修订信息(有多次时,可有多个)
		修订日期:
		原因:
		修订人:
		修订时间:
	**/
%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<html>
  <head>
    <title>学分申请</title>
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
  		.inputWidth{
			width:182px;
		}
		
  	</style>
</head>
<%
	/*
		获得角色信息
	*/
	UserProfile up = new UserProfile(request,MyTools.getSessionUserCode(request));
	String userName =up.getUserName();
	String userCode =up.getUserCode();
	
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
  
<body class="easyui-layout" style="width:100%;height:100%;">
	<div id="north" region="north" title="学分申请" style="height:79px; overflow:hidden;" >
		<table id="ee" singleselect="true" width="100%" class="tablestyle">
			<tr>
				<td class="titlestyle" style="width:150px;">姓名</td>
				<td style="text-align:center;width:150px;">
<!-- 					<input name="ic_xm" id="ic_xm" style="width:200px;" disabled="disabled"> -->
						<span id=ic_xm disabled="disabled"></span>
				</td>
				<td class="titlestyle" style="width:150px;">学号</td>
				<td style="text-align:center">
<!-- 					<input name="ic_xh" id="ic_xh" style="width:200px;" disabled="disabled"> -->
					<span id=ic_xh disabled="disabled"></span>
				</td>
				<td style="width:150px;" class="titlestyle">班级</td>
				<td style="text-align:center">
<!-- 					<input name="ic_bj" id="ic_bj" style="width:200px;" disabled="disabled"> -->
					<span id=ic_bj disabled="disabled"></span>
				</td>
			</tr>
			<tr>
				<td class="titlestyle" style="width:150px;">总分</td>
				<td style="text-align:center;position:relative;">
<!-- 					<input name="ic_zf" id="ic_zf" style="width:150px;" disabled="disabled">  -->
					<span id=ic_zf disabled="disabled"></span>
					<a href="#" onclick="doToolbar(this.id)" id="xq" style="font-size:16px;position:absolute;right:0px;top:2px">[详情]</a>
				</td>
				<td class="titlestyle" style="width:150px;">已用分数</td>
				<td style="text-align:center">
<!-- 					<input name="ic_yyfs" id="ic_yyfs" style="width:200px;" disabled="disabled"> -->
					<span id=ic_yyfs disabled="disabled"></span>
				</td>
				<td style="width:150px;" class="titlestyle">可用分数</td>
				<td style="text-align:center">
<!-- 					<input name="ic_kyfs" id="ic_kyfs" style="width:200px;" disabled="disabled"> -->
					<span id=ic_kyfs disabled="disabled"></span>
				</td>
			</tr>
	    </table>
	</div>
	<div region="center">
		<div style="width:100%; height:100%;" class="easyui-layout">
			<div id="north" region="north" style="height:35px;">
				 <table>
					<tr>
						<td><a href="#" id="new" class="easyui-linkbutton" plain="true" iconcls="icon-new" onClick="doToolbar(this.id);" title="">新建</a></td>
						<td><a href="#" id="edit" class="easyui-linkbutton" plain="true" iconcls="icon-edit" onClick="doToolbar(this.id);" title="">编辑</a></td>
						<td><a href="#" id="del" class="easyui-linkbutton" plain="true" iconCls="icon-cancel" onclick="doToolbar(this.id)">删除</a></td>
					</tr>
				</table>
			</div>
			
			<div region="center">
				<table id="xfsqList" style="width:100%;"></table>
			</div>
		</div>
	</div>
	
	<!-- 学分历史详情页面 -->
	<div id="xflsxq" style="overflow:hidden;">
		<div style="width:100%; height:100%;" class="easyui-layout">
			<div id="north" region="north" style="height:28px; overflow:hidden;">
				<table  class="tablestyle" singleselect="true" width="100%">
					<tr>
						<td class="titlestyle">学年学期</td>
						<td>
							<select name="ic_xnxq" id="ic_xnxq" class="easyui-combobox" style="width:150px;" panelHeight="auto">
							</select>
						</td>
					</tr>
				</table>
			</div>
			<div region="center">
				<table id="xflsxqList">
				</table>
			</div>
		</div>
	</div>
	
	<!-- 新建&编辑对话框 -->
	<div id="newDialog" style="width:100%; height:100%;" class="easyui-layout">
		<div region="north" style="height:34px;" >
			<table>
				<tr>
					<td><a href="#" id="save" class="easyui-linkbutton" plain="true" iconcls="icon-save" onClick="doToolbar(this.id);" title="">保存</a></td>
					<td><a href="#" id="submit" class="easyui-linkbutton" plain="true" iconcls="icon-submit" onClick="doToolbar(this.id);" title="">提交</a></td>
				</tr>
			</table>
		</div>
		<div region="center" style="background:#EFEFEF; overflow:hidden;">
			<form id="form1" method='post' style="width:100%; height:100%;">
				<table class="tablestyle" style="width:400px;">
					<tr>
						<td style="width:80px;" class="titlestyle">学年学期</td>
						<td>
							<input name="ic_xnxq2" id="ic_xnxq2" style="width:290px;" disabled="disabled">
						</td>
					</tr>
					<tr>
						<td class="titlestyle">课程名称</td>
						<td>
							<select name="ic_kcmc" id="ic_kcmc" class="easyui-combobox inputWidth" style="width:290px;">
							</select>
						</td>
					</tr>
					<tr>
						<td class="titlestyle">期末成绩</td>
						<td>
							<input name="ic_qmcj" id="ic_qmcj" style="width:290px;" disabled="disabled">
						</td>
					</tr>
					<tr>
						<td class="titlestyle">加分</td>
						<td>
							<input name="ic_jf" id="ic_jf" type="text" class="easyui-numberbox" style="width:290px;" data-options="min:0,precision:0" required="true">
						</td>
					</tr>
					<tr>
						<td class="titlestyle">备注:</td>
						<td colspan="2">
							<textarea id="ic_bz" name="ic_bz" style="width:290px;" rows="5" maxlength="500"></textarea>
						</td>
					</tr>
				</table>
				
				<input type="hidden" id="active2" name="active"/>
				<input type="hidden" id="XX_BZ" name="XX_BZ"/>
				<input type="hidden" id="XX_JF" name="XX_JF"/>
				<input type="hidden" id="XNXQMC" name="XNXQMC"/>
				<input type="hidden" id="XX_XGBH" name="XX_XGBH"/>
				<input type="hidden" id="XX_CJ" name="XX_CJ"/>
				<input type="hidden" id="XX_XH" name="XX_XH"/>
				<input type="hidden" id="XX_XM" name="XX_XM"/>
				<input type="hidden" id="XX_BJBH" name="XX_BJBH"/>
				<input type="hidden" id="XX_BJMC" name="XX_BJMC"/>
				<input type="hidden" id="tj" name="tj"/>
				<input type="hidden" id="tj2" name="tj2"/>
				<input type="hidden" id="XX_ID" name="XX_ID"/>
			</form>
		</div>
	</div>
</body>
	<script>
		var pageNum = 1;   //datagrid初始当前页数
		var pageSize = 20; //datagrid初始页内行数
		var row = '';      //行数据
		var iKeyCode = ''; //定义主键
		var XSXX;//学生信息
		var dqxnxq='';// 当前学年学期
		var zxf;//总学分
		var xm; //姓名
		var xh;//学号
		var xzbmc;//班级名称
		var xzbdm;//班级代码
		var tj='';
		var tj2='';
		var shzt =''; //审核状态
		var xgbh = '';//相关编号
		var cj = '';//成绩
		var jf = '';//加分
		var bz = '';//备注
		var dqxnxqbm ='';//当前学年学期编码
		var zgcj;//最高成绩
		var msg='';//提示信息
		
		$(document).ready(function(){
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_xfsq',
				data : 'active=initData&page=' + pageNum + '&rows=' + pageSize,
				dataType:"json",
				success : function(data) {
					initDialog();//初始化对话框
					loadGrid(data[0].listData);//页面初始化加载数据
					initCombobox(data[0].kcmcData);
					dqxnxq = data[0].curXnxq;
					dqxnxqbm = data[0].curXnxqbm;
					$.ajax({
						type : "POST",
						url : '<%=request.getContextPath()%>/Svl_xfsq',
						data : 'active=xsxx',
						dataType:"json",
						success : function(data) {
							loadXSXX(data);
						}
					});
				}
			});
		});
		
		function loadAll(){
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_xfsq',
				data : 'active=initData&page=' + pageNum + '&rows=' + pageSize,
				dataType:"json",
				success : function(data) {
					initDialog();//初始化对话框
					loadGrid(data[0].listData);//页面初始化加载数据
					initCombobox(data[0].kcmcData);
					dqxnxq = data[0].curXnxq;
					$.ajax({
						type : "POST",
						url : '<%=request.getContextPath()%>/Svl_xfsq',
						data : 'active=xsxx',
						dataType:"json",
						success : function(data) {
							loadXSXX(data);
						}
					});
				}
			});
		}
		//学生信息
		function loadXSXX(XSXX){
			$('#ic_xm').html(XSXX.xm);
			$('#ic_xh').html(XSXX.xh);
			$('#ic_bj').html(XSXX.xzbmc);
			$('#ic_zf').html(XSXX.zf);
			$('#ic_yyfs').html(XSXX.yyfs);
			$('#ic_kyfs').html(XSXX.kyfs);
			zxf =  parseInt(XSXX.kyfs);
			xm = XSXX.xm;
			xh = XSXX.xh;
			xzbmc = XSXX.xzbmc;
			xzbdm = XSXX.xzbdm;
		}
		
		
		function initDialog(){
			//新建/编辑学分窗口
			$('#newDialog').dialog({   
				width: 400,//宽度设置   
				height: 260,//高度设置 
				modal:true,
				closed: true,   
				cache: false, 
				draggable:false,//是否可移动dialog框设置
// 				toolbar:[{
					//保存编辑
// 					text:'保存',
// 					iconCls:'icon-save',
// 					handler:function(){
						//传入save值进入doToolbar方法，用于保存
// 						doToolbar('xbjsszsave');
// 					}
// 				}],
				//打开事件
				onOpen:function(data){
				},
				//读取事件
				onLoad:function(data){},
				//关闭事件
				onClose:function(data){
					$('#ic_kcmc').combobox('setValue', '');
					$('#ic_qmcj').val('');
					$('#ic_jf').numberbox('setValue', '');
					$('#ic_jf').numberbox({
					    prefix:''
					});
					$('#ic_bz').val('');
					$('#ic_xnxq2').val('');
					iKeyCode='';
					tj='';
					tj2='';
					loadAll();
				}
			});
			
			//学生历史学分窗口
			$('#xflsxq').dialog({   
				title:'学分历史详情',
				width: 700,//宽度设置   
				height: 400,//高度设置 
				modal:true,
				closed: true,   
				cache: false, 
				draggable:false,//是否可移动dialog框设置
// 				toolbar:[{
					//保存编辑
// 					text:'保存',
// 					iconCls:'icon-save',
// 					handler:function(){
						//传入save值进入doToolbar方法，用于保存
// 						doToolbar('xbjsszsave');
// 					}
// 				}],
				//打开事件
				onOpen:function(data){
				},
				//读取事件
				onLoad:function(data){},
				//关闭事件
				onClose:function(data){
				
				}
			});
		}
		
		
	//加载学分申请列表
	function loadGrid(listData){
		$("#xfsqList").datagrid({
			data:listData,
			title:'学分申请信息列表',
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
				{field:'XNXQMC',title:'学年学期编码',width:fillsize(0.1),align:'center'},
				{field:'KCMC',title:'课程名称',width:fillsize(0.1),align:'center'},
				{field:'QM',title:'期末分数',width:fillsize(0.1),align:'center'},
				{field:'JF',title:'加分',width:fillsize(0.1),align:'center'},
				{field:'BZ',title:'备注',width:fillsize(0.2),align:'center'},
				{field:'CJSJ',title:'申请日期',width:fillsize(0.1),align:'center'},
				{field:'SHZT',title:'审核状态',width:fillsize(0.1),align:'center',
					formatter:function(value,rec){
						if(value == '0') return '未提交';
						if(value == '1') return '待审核';
						if(value == '2') return '审核通过';
						if(value == '3') return '审核驳回';
					}
				},
				{field:'SHXX',title:'审核信息',width:fillsize(0.2),align:'center'}
			]],
			//双击某行时触发
			onDblClickRow:function(rowIndex,rowData){},
			//读取datagrid之前加载
			onBeforeLoad:function(){},
			//单击某行时触发
			onClickRow:function(rowIndex,rowData){
				shzt = rowData.SHZT;
				iKeyCode = rowData.ID;
				xgbh = rowData.XGBH;
				cj = rowData.QM;
				jf = rowData.JF;
				bz = rowData.BZ;
			},
			//加载成功后触发
			onLoadSuccess: function(data){
			}
		});
		
		$("#xfsqList").datagrid("getPager").pagination({
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
	
	function loadData(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_xfsq',
			data : 'active=query&page=' + pageNum + '&rows=' + pageSize,
			dataType:"json",
			success : function(data) {
				loadGrid(data[0].listData);
			}
		});
	}
	
	//按钮功能
	function doToolbar(id){
		//新建
		if(id == 'new'){
			$('#save').linkbutton('enable');
			$('#submit').linkbutton('disable');
			$('#newDialog').dialog({
				title:'新建'
			});
			$('#newDialog').dialog('open');
			$('#ic_xnxq2').val(dqxnxq);
			$('#ic_jf').numberbox('enable',true);
			$('#ic_bz').attr("disabled",false);
			$('#ic_kcmc').combobox('enable');
		}
		
		//详情
		if(id == 'xq'){
			$('#xflsxq').dialog('open');
			//加载下拉框和datagrid数据
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_xfsq',
				data : 'active=xfxq',
				dataType:"json",
				success : function(data) {
					loadXNXQCombo(data[0].xnxqData);
				}
			});
		}
		
		//保存
		if(id == 'save'){
			if($("#ic_kcmc").combobox("getValue") == ''){
				alertMsg("请选择课程!");
				return;
			}
			if($("#ic_jf").numberbox("getValue") == ''){
				alertMsg("请输入分值!");
				return;
			}
			
			if($("#ic_jf").numberbox("getValue") > zxf){
				alertMsg("您输入的分值已超出可用分数!");
				return;
			}
			if(zgcj == $('#ic_qmcj').val()){
				alertMsg("此分数已为最高分数，不能加分！");
				return;
			}
			
// 			if($("#ic_jf").numberbox("getValue") == '0'){
// 				alertMsg("特殊成绩无法加分！");
// 				return;
// 			}
			
			if(msg == '特殊成绩无法加分'){
				alertMsg("特殊成绩无法加分！");
				return;
			}
			
			if(parseInt($("#ic_jf").numberbox("getValue")) == 0){
				alertMsg("输入的分值要大于零!");
				return;
			}
			
			//当成绩加上加分超过100时，显示最大的加分数
// 			if((parseFloat($("#ic_jf").numberbox("getValue"))+parseFloat($('#ic_qmcj').val())) > parseFloat(zgcj)){
// 				var cz = parseFloat(parseFloat(zgcj)-parseFloat($('#ic_qmcj').val()));
// 				$("#ic_jf").numberbox("setValue",cz);
// 			}
			if((parseInt($("#ic_jf").numberbox("getValue"))+parseInt($('#ic_qmcj').val())) > parseInt(zgcj)){
				var cz = parseInt(zgcj)-parseInt($('#ic_qmcj').val());
				$("#ic_jf").numberbox("setValue",cz);
				$.messager.confirm('提示', '本学科最高分:'+zgcj+'分,最高加分:'+cz+'分,是否要继续操作？', function(r) {
	               if (r) {
					 	$('#XX_BZ').val($("#ic_bz").val());
						$('#XX_JF').val($("#ic_jf").numberbox("getValue"));
						$('#XX_XGBH').val($("#ic_kcmc").combobox("getValue"));
						$('#active2').val(id);
						$('#XX_CJ').val($("#ic_qmcj").val());
						$('#XNXQMC').val($("#ic_xnxq2").val());
						$('#XX_XH').val(xh);
						$('#XX_XM').val(xm);
						$('#tj').val(tj);
						$('#tj2').val(tj2);
						$('#XX_ID').val(iKeyCode);
						$('#XX_BJBH').val(xzbdm);
						$('#XX_BJMC').val(xzbmc);
						$('#form1').submit();
			       }
	            });
			}else{
				$('#XX_BZ').val($("#ic_bz").val());
				$('#XX_JF').val($("#ic_jf").numberbox("getValue"));
				$('#XX_XGBH').val($("#ic_kcmc").combobox("getValue"));
				$('#active2').val(id);
				$('#XX_CJ').val($("#ic_qmcj").val());
				$('#XNXQMC').val($("#ic_xnxq2").val());
				$('#XX_XH').val(xh);
				$('#XX_XM').val(xm);
				$('#tj').val(tj);
				$('#tj2').val(tj2);
				$('#XX_ID').val(iKeyCode);
				$('#XX_BJBH').val(xzbdm);
				$('#XX_BJMC').val(xzbmc);
				$('#form1').submit();
			}
			
			
			
			
		}
		
		//提交
		if(id == 'submit'){
			if($("#ic_kcmc").combobox("getValue") == ''){
				alertMsg("请选择课程!");
				return;
			}
			if($("#ic_jf").numberbox("getValue") == ''){
				alertMsg("请输入分值!");
				return;
			}
			if($("#ic_jf").numberbox("getValue") > zxf){
				alertMsg("您输入的分值已超出可用分数!");
				return;
			}
			
			//----------------------------------------------------
			
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_xfsq',
				data : 'active=queryZgcj&XNXQMC=' + $("#ic_xnxq2").val() + '&XX_XGBH=' + $("#ic_kcmc").combobox("getValue"),
				dataType:"json",
				success : function(data) {
					zgcj = data[0].zgcj;//学科最高期末考试成绩
					if(zgcj == 0){
						zgcj = '';
					}
					msg = data[0].MSG;
				}
			});
			
			//-----------------------------------------------------
			
			if(zgcj == $('#ic_qmcj').val()){
				alertMsg("此分数已为最高分数，不能加分！");
				return;
			}
			
// 			if($("#ic_jf").numberbox("getValue") == '0'){
// 				alertMsg("特殊成绩无法加分！");
// 				return;
// 			}

			if(msg == '特殊成绩无法加分'){
				alertMsg("特殊成绩无法加分！");
				return;
			}
			
			if(parseInt($("#ic_jf").numberbox("getValue")) == 0){
				alertMsg("输入的分值要大于零!");
				return;
			}
			
			//当成绩加上加分超过最高成绩时，显示最大的加分数
// 			if((parseFloat($("#ic_jf").numberbox("getValue"))+parseFloat($('#ic_qmcj').val())) > parseFloat(zgcj)){
// 				var cz = parseFloat(parseFloat(zgcj)-parseFloat($('#ic_qmcj').val()));
// 				$("#ic_jf").numberbox("setValue",cz);
// 			}
			if((parseInt($("#ic_jf").numberbox("getValue"))+parseInt($('#ic_qmcj').val())) > parseInt(zgcj)){
				var cz = parseInt(parseInt(zgcj)-parseInt($('#ic_qmcj').val()));
				$("#ic_jf").numberbox("setValue",cz);
				$.messager.confirm('提示', '本学科最高分:'+zgcj+'分,最高加分:'+cz+'分,是否要继续操作？', function(r) {
	               if (r) {
					 	$.ajax({
							type : "POST",
							url : '<%=request.getContextPath()%>/Svl_xfsq',
							data : 'active=submit&XX_ID=' + iKeyCode + '&XX_XGBH=' + $("#ic_kcmc").combobox("getValue") + '&XX_JF=' + $("#ic_jf").numberbox("getValue") + '&XX_BZ=' + $("#ic_bz").val() + '&XX_CJ=' + $("#ic_qmcj").val(),
							dataType:"json",
							success : function(data) {
								//var json = eval("("+data+")");
								if(data[0].MSG == '提交成功'){
									showMsg(data[0].MSG);
								}else{
									alertMsg(data[0].MSG);
								}
								
								$('#newDialog').dialog('close');
							}
						});
			       }
	            });
			}else{
				$.ajax({
					type : "POST",
					url : '<%=request.getContextPath()%>/Svl_xfsq',
					data : 'active=submit&XX_ID=' + iKeyCode + '&XX_XGBH=' + $("#ic_kcmc").combobox("getValue") + '&XX_JF=' + $("#ic_jf").numberbox("getValue") + '&XX_BZ=' + $("#ic_bz").val() + '&XX_CJ=' + $("#ic_qmcj").val(),
					dataType:"json",
					success : function(data) {
						//var json = eval("("+data+")");
						if(data[0].MSG == '提交成功'){
							showMsg(data[0].MSG);
						}else{
							alertMsg(data[0].MSG);
						}
						
						$('#newDialog').dialog('close');
					}
				});
			}
			
		}
		
		//删除
		if(id == 'del'){
			if(iKeyCode == ''){
				alertMsg('请选择一行数据');
				return
			}
			
			if(shzt == '0'){
				$.ajax({
					type : "POST",
					url : '<%=request.getContextPath()%>/Svl_xfsq',
					data : 'active=del&XX_ID='+iKeyCode,
					dataType:"json",
					success : function(data) {
						if(data[0].MSG == '删除成功'){
							showMsg(data[0].MSG);
						}else{
							alertMsg(data[0].MSG);
						}
						loadAll();
					}
				});
			}else{
				alertMsg("审核完成信息和待审核信息不能删除！");
			}
			
		}
		
		
		//编辑
		if(id == 'edit'){
			if(iKeyCode == ''){
				alertMsg('请选择一行数据');
				return
			}
			tj = 'edit';
			
			$('#newDialog').dialog({
				title:'编辑'
			});
			$('#newDialog').dialog('open');
			
			if(shzt == '0'){
				$('#submit').linkbutton('enable');
				$('#save').linkbutton('enable');
				
				$('#ic_xnxq2').val(dqxnxq);
				$('#ic_kcmc').combobox('setValue', xgbh);
				$('#ic_kcmc').combobox('enable');
				$('#ic_qmcj').val(cj);
				$('#ic_jf').numberbox('setValue', jf);
				$('#ic_jf').numberbox('enable',true);
				$('#ic_bz').val(bz);
				$('#ic_bz').attr("disabled",false);
			}else{
				$('#submit').linkbutton('disable');
				$('#save').linkbutton('disable');
				
				$('#ic_xnxq2').val(dqxnxq);
				$('#ic_kcmc').combobox('setValue', xgbh);
				$('#ic_kcmc').combobox('disable');
				$('#ic_qmcj').val(cj);
				$('#ic_jf').numberbox('setValue', jf);
				$('#ic_jf').numberbox('disable',true);
				$('#ic_bz').val(bz);
				$('#ic_bz').attr("disabled",true);
			}
		}
	}
	
	/**表单提交**/
	$('#form1').form({
		//定位到servlet位置的url
		url:'<%=request.getContextPath()%>/Svl_xfsq',
		//当点击事件后触发的事件
		onSubmit: function(data){}, 
		//当点击事件并成功提交后触发的事件
		success:function(data){
			var json  =  eval("("+data+")");
			if(json[0].MSG == '保存成功' || json[0].MSG == '修改成功'){
				iKeyCode = json[0].bh;
				showMsg(json[0].MSG);
				tj2 = "add"; //添加标识
				$("#submit").linkbutton('enable');
			}else{
				alertMsg(json[0].MSG);
			}
		}
	});
	
	function loadxfxqGrid(xfsqData){
		$("#xflsxqList").datagrid({
			data:xfsqData,
			title:'学分详情列表',
			width:'100%',
			nowrap:false,
			fit:true, //自适应父节点宽度和高度
// 			showFooter:true,
			striped:true,
// 			pagination:true,
			pageSize:pageSize,
			singleSelect:true,
			pageNumber:pageNum,
			rownumbers:true,
			fitColumns:true,
			columns:[[
				//field为读取数据的数据名，title为显示的数据名，width宽度设置，align数字在表格中显示的位置
				{field:'XNXQMC',title:'学年学期名称',width:fillsize(0.2),align:'center'},
				{field:'XZBMC',title:'班级名称',width:fillsize(0.2),align:'center'},
				{field:'KCMC',title:'课程名称',width:fillsize(0.2),align:'center'},
				{field:'SKJS',title:'授课教师',width:fillsize(0.2),align:'center'},
				{field:'ZP',title:'学分',width:fillsize(0.2),align:'center'}
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
	}
	
	function initCombobox(kcmcData){
		//课程名称下拉框
		$('#ic_kcmc').combobox({
			data:kcmcData,
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
			onChange:function(data){
				$.ajax({
					type : "POST",
					async: false,
					url : '<%=request.getContextPath()%>/Svl_xfsq',
					data : 'active=querycj&kcmc='+data+'&xnxqmc='+dqxnxq,
					dataType:"json",
					success : function(data) {
						if($('#ic_kcmc').combobox('getValue') == ''){
							$('#ic_jf').numberbox('enable');
							$('#ic_jf').numberbox('clear');
							$('#ic_qmcj').val('');
						}else{
							var cj = data[0].kscj;//学科期末考试成绩
							zgcj = data[0].zgcj;//学科最高期末考试成绩
							if(zgcj == 0){
								zgcj = '';
							}
							$('#ic_qmcj').val(cj);
							//考试成绩等于最高考试成绩
							if(cj == zgcj){
								$('#ic_jf').numberbox({
								    prefix:'此分数已为最高分数，不能加分！                                                '
								});
								$('#ic_jf').numberbox('setValue','0');
								$('#ic_jf').numberbox('disable',true);
							}else{
								$('#ic_jf').numberbox('enable');
								$('#ic_jf').numberbox('clear');
								$('#ic_jf').numberbox({
								    prefix:''
								});
							}
							
							msg = data[0].MSG;
							//考试成绩等于最高考试成绩
							if(msg == '特殊成绩无法加分'){
								$('#ic_jf').numberbox({
								    prefix:'特殊成绩无法加分！                                                '
								});
								$('#ic_jf').numberbox('setValue','0');
								$('#ic_jf').numberbox('disable',true);
							}
							
						}
					}
				});
			}
		});	
	}
	
	function loadXNXQCombo(xnxqData){
		//学年学期下拉框
		$('#ic_xnxq').combobox({
			data:xnxqData,
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			panelHeight:'140', //combobox高度
			onLoadSuccess:function(data){
				//初始化combobox时赋值
				$(this).combobox('setValue', dqxnxqbm);
			},
			//下拉列表值改变事件
			onChange:function(data){
				$.ajax({
					type : "POST",
					url : '<%=request.getContextPath()%>/Svl_xfsq',
					data : 'active=queryXfxq&XX_XNXQBM='+$('#ic_xnxq').combobox('getValue'),
					dataType:"json",
					success : function(data) {
						loadxfxqGrid(data[0].xfxqData);
					}
				});
			}
		});	
	}
	</script>
</html>
