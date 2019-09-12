<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="java.util.Vector"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.src.develop.store.user.*"%>
<%
	/**
		创建人：yangda
		Create date: 2017.09.30
		功能说明：学分审核
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
    <title>学分审核</title>
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
  
<body class="easyui-layout">
	<div id="north" region="north" title="学分审核" style="height:116px;" >
		<table id="ee2" width="100%">
			<tr>
				<td>
					<a href="#" id="audit" class="easyui-linkbutton" plain="true" iconcls="icon-audit" onClick="doToolbar(this.id);" title="" >审核</a>
					<a href="#" id="plsh" class="easyui-linkbutton" plain="true" iconcls="icon-audit" onClick="doToolbar(this.id);" title="" >批量审核</a>
				</td>
			</tr>
	    </table>
		<table id="ee" singleselect="true" width="100%" class="tablestyle">
			<tr>
				<td class="titlestyle" style="width:150px;">学年学期名称</td>
				<td>
					<select name="ic_xnxq" id="ic_xnxq" class="easyui-combobox"style="width:180px;">
					</select>
				</td>
				<td class="titlestyle" style="width:150px;">学号</td>
				<td>
					<input name="ic_xh" id="ic_xh" style="width:200px;">
				</td>
				<td style="width:150px;" class="titlestyle">姓名</td>
				<td>
					<input name="ic_xm" id="ic_xm" style="width:200px;">
				</td>
				<td style="text-align:center;">
					<a href="#" id="query" class="easyui-linkbutton" plain="true" iconcls="icon-search" onClick="doToolbar(this.id);" title="">查询</a>
				</td>
			</tr>
			<tr>
				<td class="titlestyle" style="width:150px;">班级名称</td>
				<td>
					<input name="ic_bjmc" id="ic_bjmc" style="width:180px;">
				</td>
				<td class="titlestyle" style="width:150px;">课程名称</td>
				<td>
					<input name="ic_kcmc" id="ic_kcmc" style="width:200px;">
				</td>
				<td class="titlestyle" style="width:150px;">审核状态</td>
				<td>
					<select name="ic_shzt" id="ic_shzt" class="easyui-combobox"style="width:200px;">
					</select>
				</td>
				<td>
				</td>
			</tr>
	    </table>
	</div>
	<div region="center">
		<table id="xssqList" style="width:100%;"></table>
	</div>
	
	<!-- 审核对话框 -->
	<div id="newDialog" style="width:100%; height:100%;" class="easyui-layout">
		<div region="north" style="background:#EFEFEF; overflow:hidden;">
			<form id="form1" method='post' style="width:100%;height:47%;">
				<table class="tablestyle" style="width:400px;" >
					<tr>
						<td style="width:100px;" class="titlestyle">学年学期名称</td>
						<td>
<!-- 							<input name="ic_xnxq2" id="ic_xnxq2" style="width:200px;" disabled="disabled"> -->
								<span id="ic_xnxq2" disabled="disabled"></span>
						</td>
					</tr>
					<tr>
						<td class="titlestyle">学号</td>
						<td>
<!-- 							<input name="ic_xh2" id="ic_xh2" style="width:200px;" disabled="disabled"> -->
								<span id=ic_xh2 disabled="disabled"></span>
						</td>
					</tr>
					<tr>
						<td class="titlestyle">姓名</td>
						<td>
<!-- 							<input name="ic_xm2" id="ic_xm2" style="width:200px;" disabled="disabled"> -->
								<span id=ic_xm2 disabled="disabled"></span>
						</td>
					</tr>
					<tr>
						<td class="titlestyle">班级名称</td>
						<td>
<!-- 							<input name="ic_bjmc2" id="ic_bjmc2" style="width:200px;" disabled="disabled"> -->
							<span id=ic_bjmc2 disabled="disabled"></span>
						</td>
					</tr>
					<tr>
						<td class="titlestyle">课程名称</td>
						<td>
<!-- 							<input name="ic_kcmc2" id="ic_kcmc2" style="width:200px;" disabled="disabled"> -->
							<span id=ic_kcmc2 disabled="disabled"></span>
						</td>
					</tr>
					<tr>
						<td class="titlestyle">期末成绩</td>
						<td>
<!-- 							<input name="ic_cj2" id="ic_cj2" style="width:200px;" disabled="disabled"> -->
							<span id=ic_cj2 disabled="disabled"></span>
						</td>
					</tr>
					<tr>
						<td class="titlestyle">加分</td>
						<td>
<!-- 							<input name="ic_jf2" id="ic_jf2" style="width:200px;" disabled="disabled"> -->
							<span id=ic_jf2 disabled="disabled"></span>
						</td>
					</tr>
					<tr>
						<td class="titlestyle">审核信息</td>
						<td colspan="2">
							<textarea id="ic_shxx" name="ic_shxx" style="width:270px;" rows="5" maxlength="500"></textarea>
						</td>
					</tr>
				</table>
				
				<input type="hidden" id="active2" name="active"/>
				<input type="hidden" id="XX_SHXX" name="XX_SHXX"/>
				<input type="hidden" id="XX_ID" name="XX_ID"/>
				<input type="hidden" id="XX_JF" name="XX_JF"/>
				<input type="hidden" id="XX_XH" name="XX_XH"/>
				
			</form>
		</div>
		<div region="center">
			<div style="text-align:center; margin-top:5px; width:400px;">
				<a href="#" id="pass" class="easyui-linkbutton" plain="true" iconcls="icon-ok" onClick="doToolbar(this.id);">通过</a>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<a href="#" id="back" class="easyui-linkbutton" plain="true" iconcls="icon-cancel" onClick="doToolbar(this.id);">驳回</a>
			</div>
		</div>
	</div>
	
	
	<!-- 批量设置审核对话框 -->
	<div id="plnewDialog" style="width:100%; height:100%;" class="easyui-layout">
		<div region="north" style="background:#EFEFEF; overflow:hidden;">
			<form id="form2" method='post' style="width:100%;height:13%;">
				<table class="tablestyle" style="width:400px;">
					<tr>
						<td style="width:100px;" class="titlestyle">审核信息</td>
						<td colspan="2">
							<textarea id="ic_shxx2" name="ic_shxx2" style="width:270px;" rows="5" maxlength="500"></textarea>
						</td>
					</tr>
				</table>
				
				<input type="hidden" id="active3" name="active"/>
				<input type="hidden" id="XX_SHXX2" name="XX_SHXX"/>
				<input type="hidden" id="XX_ID2" name="XX_ID"/>
				<input type="hidden" id="XX_JF2" name="XX_JF"/>
				<input type="hidden" id="XX_XH2" name="XX_XH"/>
			</form>
		</div>
		<div region="center">
			<div style="text-align:center; margin-top:5px; width:400px;">
				<a href="#" id="plpass" class="easyui-linkbutton" plain="true" iconcls="icon-ok" onClick="doToolbar(this.id);">通过</a>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<a href="#" id="plback" class="easyui-linkbutton" plain="true" iconcls="icon-cancel" onClick="doToolbar(this.id);">驳回</a>
			</div>
		</div>
	</div>
</body>
	<script>
		var pageNum = 1;   //datagrid初始当前页数
		var pageSize = 20; //datagrid初始页内行数
		var row = '';      //行数据
		var iKeyCode = ''; //定义主键
		var xnxqmc = ''; //学年学期名称
		var xh = ''; //学号
		var xm = ''; //姓名
		var bjmc = ''; //班级名称
		var kcmc = ''; //课程名称
		var cj = ''; //期末成绩
		var jf = ''; //加分
		var shzt = ''; //审核状态
		
		
		$(document).ready(function(){
			initDialog();//初始化对话框
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_xfsh',
				data : 'active=initData&page=' + pageNum + '&rows=' + pageSize + '&XX_SHZT=' + '1',
				dataType:"json",
				success : function(data) {
					loadGrid(data[0].listData);//页面初始化加载数据
					initCombobox(data[0].xnxqData,data[0].shztData);
				}
			});
		});
		
				
		function initDialog(){
			//学分审核窗口
			$('#newDialog').dialog({   
				title:'学分申请审核',
				width: 400,//宽度设置   
				height: 340,//高度设置 
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
					iKeyCode = ''; //定义主键
					xnxqmc = ''; //学年学期名称
					xh = ''; //学号
					xm = ''; //姓名
					bjmc = ''; //班级名称
					kcmc = ''; //课程名称
					cj = ''; //期末成绩
					jf = ''; //加分
					shzt = ''; //审核状态
					$('#ic_shxx').val(''); //清空文本框
				}
			});
			
			//批量学分审核窗口
			$('#plnewDialog').dialog({   
				title:'批量学分申请审核',
				width: 400,//宽度设置   
				height: 160,//高度设置 
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
					iKeyCode = ''; //定义主键
					xnxqmc = ''; //学年学期名称
					xh = ''; //学号
					xm = ''; //姓名
					bjmc = ''; //班级名称
					kcmc = ''; //课程名称
					cj = ''; //期末成绩
					jf = ''; //加分
					shzt = ''; //审核状态
					$('#ic_shxx2').val(''); //清空文本框
				}
			});
			
		}
		
		
	//加载学分申请列表
	function loadGrid(listData){
		$("#xssqList").datagrid({
			data:listData,
			title:'学分审核信息列表',
			width:'100%',
			nowrap:false,
			fit:true, //自适应父节点宽度和高度
			showFooter:true,
			striped:true,
			pagination:true,
			pageSize:pageSize,
// 			singleSelect:true,
			pageNumber:pageNum,
			rownumbers:true,
			fitColumns:true,
			columns:[[
				//field为读取数据的数据名，title为显示的数据名，width宽度设置，align数字在表格中显示的位置
				{field:'ck',checkbox:true},
				{field:'XNXQMC',title:'学年学期名称',width:fillsize(0.15),align:'center'},
				{field:'XH',title:'学号',width:fillsize(0.15),align:'center'},
				{field:'XM',title:'姓名',width:fillsize(0.1),align:'center'},
				{field:'BJMC',title:'班级名称',width:fillsize(0.1),align:'center'},
				{field:'KCMC',title:'课程名称',width:fillsize(0.1),align:'center'},
				{field:'CJ',title:'期末成绩',width:fillsize(0.05),align:'center'},
				{field:'JF',title:'加分',width:fillsize(0.05),align:'center'},
				{field:'BZ',title:'备注',width:fillsize(0.2),align:'center'},
				{field:'SHZT',title:'审核状态',width:fillsize(0.1),align:'center',
					formatter:function(value,rec){
						if(value == '1') return '待审核';
						if(value == '2') return '审核通过';
						if(value == '3') return '审核驳回';
					}
				}
			]],
			//双击某行时触发
			onDblClickRow:function(rowIndex,rowData){},
			//读取datagrid之前加载
			onBeforeLoad:function(){},
			//单击某行时触发
			onClickRow:function(rowIndex,rowData){
				iKeyCode = rowData.ID; //定义主键
				xnxqmc = rowData.XNXQMC; //学年学期名称
				xh = rowData.XH; //学号
				xm = rowData.XM; //姓名
				bjmc = rowData.BJMC; //班级名称
				kcmc = rowData.KCMC; //课程名称
				cj = rowData.CJ; //期末成绩
				jf = rowData.JF; //加分
				shzt = rowData.SHZT; //审核状态
			},
			//加载成功后触发
			onLoadSuccess: function(data){
			}
		});
		
		$("#xssqList").datagrid("getPager").pagination({
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
			url : '<%=request.getContextPath()%>/Svl_xfsh',
			data : 'active=query&page=' + pageNum + '&rows=' + pageSize + '&XX_XNXQBM=' + $('#ic_xnxq').combobox('getValue') + '&XX_XH=' + $('#ic_xh').val() 
					+ '&XX_XM=' + $('#ic_xm').val() + '&XX_BJMC=' + $('#ic_bjmc').val() + '&KCMC=' + $('#ic_kcmc').val() + '&XX_SHZT=' + $('#ic_shzt').combobox('getValue'),
			dataType:"json",
			success : function(data) {
				loadGrid(data[0].listData);
			}
		});
	}
	
	//按钮功能
	function doToolbar(id){
		//审核
		if(id == 'audit'){
			if(iKeyCode == ''){
				alertMsg('请选择一行数据');
				return
			}
			if(shzt != 1){
				alertMsg('该条申请已被审核');
				return
			}
			
			$('#newDialog').dialog('open');
			$('#ic_xnxq2').html(xnxqmc);
			$('#ic_xh2').html(xh);
			$('#ic_xm2').html(xm);
			$('#ic_bjmc2').html(bjmc);
			$('#ic_kcmc2').html(kcmc);
			$('#ic_cj2').html(cj);
			$('#ic_jf2').html(jf);
		}
		
		//批量审核
		if(id == 'plsh'){
			iKeyCode = ''; //定义主键
			xnxqmc = ''; //学年学期名称
			xh = ''; //学号
			xm = ''; //姓名
			bjmc = ''; //班级名称
			kcmc = ''; //课程名称
			cj = ''; //期末成绩
			jf = ''; //加分
			shzt = ''; //审核状态
			$('#ic_shxx2').val(''); //清空文本框
			
			var rows = $('#xssqList').datagrid('getSelections');
			if(rows.length <= 0 ){
				alertMsg('请选择一行数据');
				return
			}
			for(var i=0; i<rows.length; i++){
				xh +=rows[i].XH+',';
				xm +=rows[i].XM+',';
				iKeyCode += rows[i].ID+',';
				jf +=rows[i].JF+',';
				xnxqmc +=rows[i].XNXQMC+',';
			}
			xh = xh.substring(0, xh.length-1);
			xm = xm.substring(0, xm.length-1);
			iKeyCode = iKeyCode.substring(0, iKeyCode.length-1);
			jf = jf.substring(0, jf.length-1);
			xnxqmc = xnxqmc.substring(0, xnxqmc.length-1);
			
			$('#plnewDialog').dialog('open');
		}
		
		//审核通过
		if(id == 'pass'){
			$('#active2').val('pass');
			$('#XX_JF').val(jf);
			$('#XX_XH').val(xh);
			$('#XX_SHXX').val($('#ic_shxx').val());
			$('#XX_ID').val(iKeyCode);
			$('#form1').submit();
		}
		
		//审核驳回
		if(id == 'back'){
			$('#active2').val('back');
			$('#XX_SHXX').val($('#ic_shxx').val());
			$('#XX_ID').val(iKeyCode);
			$('#form1').submit();
		}
		
		//批量审核通过
		if(id == 'plpass'){
			$('#active3').val('plpass');
			$('#XX_JF2').val(jf);
			$('#XX_XH2').val(xh);
			$('#XX_SHXX2').val($('#ic_shxx2').val());
			$('#XX_ID2').val(iKeyCode);
			$('#form2').submit();
		}
		
		//批量审核驳回
		if(id == 'plback'){
			$('#active3').val('plback');
			$('#XX_SHXX2').val($('#ic_shxx2').val());
			$('#XX_ID2').val(iKeyCode);
			$('#form2').submit();
		}
		
		//查询
		if(id == 'query'){
			loadData();
		}
	}
	
	/**表单提交**/
	$('#form1').form({
		//定位到servlet位置的url
		url:'<%=request.getContextPath()%>/Svl_xfsh',
		//当点击事件后触发的事件
		onSubmit: function(data){}, 
		//当点击事件并成功提交后触发的事件
		success:function(data){
			var json  =  eval("("+data+")");
			if(json[0].MSG == '审核成功'){
				loadData();
				showMsg(json[0].MSG);
				$('#newDialog').dialog('close');
			}else{
				alertMsg(json[0].MSG);
			}
		}
	});
	
	/**表单提交**/
	$('#form2').form({
		//定位到servlet位置的url
		url:'<%=request.getContextPath()%>/Svl_xfsh',
		//当点击事件后触发的事件
		onSubmit: function(data){}, 
		//当点击事件并成功提交后触发的事件
		success:function(data){
			var json  =  eval("("+data+")");
			if(json[0].MSG == '审核成功'){
				loadData();
				showMsg(json[0].MSG);
				$('#plnewDialog').dialog('close');
			}else{
				alertMsg(json[0].MSG);
			}
		}
	});
	
	function initCombobox(xnxqData,shztData){
		//学年学期下拉框
		$('#ic_xnxq').combobox({
			data:xnxqData,
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
			}
		});	
		
		//审核状态下拉框
		$('#ic_shzt').combobox({
			data:shztData,
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			panelHeight:'140', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data != ''){
					//初始化combobox时赋值
					$(this).combobox('setValue', '1');
				}
			},
			//下拉列表值改变事件
			onChange:function(data){
			}
		});	
	}

	</script>
</html>
