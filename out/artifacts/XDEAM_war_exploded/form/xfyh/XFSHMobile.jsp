<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="java.util.Vector"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.src.develop.store.user.*"%>
<%
	/**
		创建人：yangda
		Create date: 2017.09.28
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
	    <base href="<%=basePath%>">
	    <meta http-equiv="x-ua-compatible" content="IE=7; IE=9" />
	    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no"/>
	    
	    <title>学分审核</title>
	   
	    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/bootstrap.css"/>
	    
	    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/themes/default/easyui.css"/>
	    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/mobile.css">  
	     <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/icon.css">
	    <script type="text/javascript" src="<%=request.getContextPath()%>/script/angular.min.js"></script>
	    <script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
<!-- 	    <script type="text/javascript" src="< %=request.getContextPath()%>/script/jquery-1.7.2.min.js"></script> -->
	    <script type="text/javascript" src="<%=request.getContextPath()%>/script/jquery.min.js"></script>  
	    <script src="<%=request.getContextPath()%>/script/bootstrap.min.js"></script>
<!-- 	    <script type="text/javascript" src="< %=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script> -->
	    <script type="text/javascript" src="<%=request.getContextPath()%>/script/jquery.easyui.min.js"></script> 
	    <script type="text/javascript" src="<%=request.getContextPath()%>/script/jquery.easyui.mobile.js"></script> 
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
  		<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/publicScript.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
		<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css"/>
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
  
<body ng-app="">
<!-- 	<div ng-include="'< %=request.getContextPath()%>/form/xfyh/top.jsp'" style='background:rgba(0, 168, 255, 0.4);height:70px'></div> -->
    
<div class="container">
<!--   <div class="row"> -->
<!--         <div class="col-lg-10 col-md-10 col-sm-12 col-xs-12"> -->
<!--             <div class="content-border"> -->
<!--                 <div class="ManageTitle"> -->
<!--                     <h5><b>审核申请</b></h5> -->
<!--                 </div> -->
<!--             </div> -->
<!--         </div> -->
<!--     </div> -->
    <div class="bg-box">
        <form class="colorForm" id='form_post' method='post'>
        	<div class="row">
        		<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
        			<label>学年学期：</label>
        			<select name="ic_xnxq" id="ic_xnxq" style="width:74%;height:34px;display: inline-block;"  class="form-control easyui-combobox" >
        			</select>
        		</div>
        		<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12 text-right mar-right hide1">
        			<label>姓名：</label>
        			<input name="ic_xm" id="ic_xm" style="width:74%;height:34px;display: inline-block;" type="text" class="form-control">
        		</div>
        		<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12 text-right mar-right hide1">
        			<label>学号：</label>
        			<input name="ic_xh" id="ic_xh" style="width:74%;height:34px;display: inline-block;" type="text" class="form-control">
        		</div>
        	</div>
        	<div class="row">
        		<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12 hide1" >
        			<label>班级名称：</label>
        			<input name="ic_bjmc" id="ic_bjmc" style="width:74%;height:34px;display: inline-block;" type="text" class="form-control">
        		</div>
        		<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12 text-right mar-top hide1" >
        			<label>课程名称：</label>
        			<input name="ic_kcmc" id="ic_kcmc" style="width:74%;height:34px;display: inline-block;" type="text" class="form-control">
        		</div>
        		<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12 text-right mar-top hide1" >
        			<label>审核状态：</label>
        			<select name="ic_shzt" id="ic_shzt" style="width:74%;height:34px;display: inline-block; " class="form-control easyui-combobox">
        			</select>
        		</div>
        	</div>
        	
            <div class="row btn-box" style="margin-top:3%;">
               <div class="col-lg-4 col-md-4 col-sm-2 col-xs-2"></div>
               <div class="col-lg-4 col-md-4 col-sm-8 col-xs-8" style="text-align: center;padding:0">
                   <input onclick="doToolbar(this.id)" id="query" class="btn btn-primary" type="button" value="查询">
                   <input onclick="emptyDialog()" class="btn btn-default" type="button" value="重置" style="margin-left:10%;margin-right:4%">
                   <input onclick="doToolbar(this.id)" id="button-hidden"  class="btn btn-info" type="button" value="....." style="float:right;left:-9%;">
               </div>
               <div class="col-lg-4 col-md-4 col-sm-2 col-xs-2"></div>
           </div>
        </form>
    </div>
</div>
<div class="manage-list">
    <div class="row">
        <div class="container">
            <div class="col-lg-6 col-md-6 col-sm-5 col-xs-5" style="padding:0;margin-top: -0.5%;"><h4 style="font-size:18px"><b>学生申请列表</b></h4></div>
			<div class="col-lg-6 col-md-6 col-sm-7 col-xs-7">
				<div class="text-right">
	                <input type="button" style="width: 50px" onclick="doToolbar(this.id)" id='audit' class="btn form-control btn-success btn-sm" value="审核"/>
	                <input type="button" style="width: 80px" onclick="doToolbar(this.id)" id='plsh' class="btn form-control btn-success btn-sm" value="批量审核"/>
	            </div>
			</div>
		</div>
	</div>
</div>
<div class="container">
 <div class="list-table" id="list-table" style="height:80%;">
	 <table id="bg" style="display: none;" >  
<!-- 	     <thead>   -->
<!-- 	         <tr>   -->
<!-- 	             <th data-options="field:'XQXN',width:fillsize(0.1),align:'center'">学年学期</th> -->
<!-- 	             <th data-options="field:'XH',width:fillsize(0.1),align:'center'">学号</th>   -->
<!-- 	             <th data-options="field:'XM',width:fillsize(0.1),align:'center'">姓名</th>   -->
<!-- 	             <th data-options="field:'BJ',width:fillsize(0.1),align:'center'">班级</th>   -->
<!-- 	             <th data-options="field:'KC',width:fillsize(0.1),align:'center'">课程</th> -->
<!-- 	             <th data-options="field:'QMFS',width:fillsize(0.1),align:'center'">期末</th>   -->
<!-- 	             <th data-options="field:'JF',width:fillsize(0.1),align:'center'">加分</th>   -->
<!-- 	             <th data-options="field:'BZ',width:fillsize(0.3),align:'center'">备注</th>   -->
<!-- 	         </tr> -->
<!-- 	     </thead>   -->
	 </table>
</div>
</div>
   <!-- 审核 -->
	<div id="Need_QlCount" style="display:none;overflow-x:hidden;">
		<form  id='form_post2' method='post' style="padding: 0px 10px 0px 10px;">
			<table style="width:100%;font-size:100%;">
				<tr>
					<td style="width:25%;height:24px;">学年学期：</td>
<!-- 					<td><input id='ic_xnxq2' name='ic_xnxq2' style="width:100%;height:34px;" type="text" class="form-control" disabled></td> -->
					<td style="height:24px;"><span id=ic_xnxq2 disabled="disabled"></span></td>
				</tr>
				<tr>
					<td style="width:25%;height:24px;">学号：</td>
<!-- 					<td><input id='ic_xh2' name='ic_xh2' style="width:100%;height:34px;"  type="text" class="form-control" disabled></td> -->
					<td style="height:24px;"><span id=ic_xh2 disabled="disabled"></span></td>
				</tr>
				<tr>
					<td style="width:25%;height:24px;">姓名：</td>
<!-- 					<td><input id='ic_xm2' name='ic_xm2' style="width:100%;height:34px;"  type="text" class="form-control" disabled></td> -->
					<td style="height:24px;"><span id=ic_xm2 disabled="disabled"></span></td>
				</tr>
				<tr>
					<td style="width:25%;height:24px;">班级名称：</td>
<!-- 					<td><input id='ic_bjmc2' name='ic_bjmc2' style="width:100%;height:34px;"  type="text" class="form-control" disabled></td> -->
					<td style="height:24px;"><span id=ic_bjmc2 disabled="disabled"></span></td>
				</tr>
				<tr>
					<td style="width:25%;height:24px;">课程名称：</td>
<!-- 					<td><input id='ic_kcmc2' name='ic_kcmc2' style="width:100%;height:34px;"  type="text" class="form-control" disabled></td> -->
					<td style="height:24px;"><span id=ic_kcmc2 disabled="disabled"></span></td>
				</tr>
				<tr>
					<td style="width:25%;height:24px;">期末成绩：</td>
<!-- 					<td><input id='ic_cj2' name='ic_cj2' style="width:100%;height:34px;"  type="text" class="form-control" disabled></td> -->
					<td style="height:24px;"><span id=ic_cj2 disabled="disabled"></span></td>
				</tr>
				<tr>
					<td style="width:25%;height:24px;">加分：</td>
<!-- 					<td><input id='ic_jf2' name='ic_jf2' style="width:100%;height:34px;"  type="text" class="form-control" disabled></td> -->
					<td style="height:24px;"><span id=ic_jf2 disabled="disabled"></span></td>
				</tr>
				<tr>
					<td>审核信息：</td>
				</tr>
				<tr>
					<td colspan=2><textarea id="ic_shxx" name='ic_shxx' class="form-control" style="width:100%;height:60px;resize : none" maxlength='100'></textarea></td>
				</tr>
			</table>
			
			<input type="hidden" id="active2" name="active"/>
			<input type="hidden" id="XX_SHXX" name="XX_SHXX"/>
			<input type="hidden" id="XX_ID" name="XX_ID"/>
			<input type="hidden" id="XX_JF" name="XX_JF"/>
			<input type="hidden" id="XX_XH" name="XX_XH"/>
				
		</form>
		
		<div class="row btn-box" style="margin-top:5%;margin-bottom:5%;text-align:center;">
			<div class="col-lg-1 col-md-1 col-sm-1 col-xs-1"></div>
            <div class="col-lg-10 col-md-10 col-sm-10 col-xs-10">
                <input class="btn btn-danger" onclick="doToolbar(this.id)" id="pass" type="button" value="通过">
                <input class="btn btn-primary" onclick="doToolbar(this.id)" id="back" type="button" value="驳回" style="margin:0px 2px 0px 2px">
                <input class="btn btn-info" onclick="$('#Need_QlCount').dialog('close');" type="button" value="取消">
            </div>
            <div class="col-lg-1 col-md-1 col-sm-1 col-xs-1"></div>
        </div>
	</div>
	
	<!-- 批量审核 -->
	<div id="plNeed_QlCount" style="display:none;overflow:hidden;">
		<form  id='form_post3' method='post' style="padding: 0px 10px 0px 10px;">
			<table style="width:100%;font-size:100%;">
				<tr>
					<td>审核信息：</td>
				</tr>
				<tr>
					<td colspan=2><textarea id="ic_shxx2" name='ic_shxx2' class="form-control" style="width:100%;height:60px;resize : none" maxlength='100'></textarea></td>
				</tr>
			</table>
			
			<input type="hidden" id="active3" name="active"/>
			<input type="hidden" id="XX_SHXX2" name="XX_SHXX"/>
			<input type="hidden" id="XX_ID2" name="XX_ID"/>
			<input type="hidden" id="XX_JF2" name="XX_JF"/>
			<input type="hidden" id="XX_XH2" name="XX_XH"/>
				
		</form>
		
		<div class="row btn-box" style="margin-top:5%;margin-bottom:5%;text-align:center;">
			<div class="col-lg-1 col-md-1 col-sm-1 col-xs-1"></div>
            <div class="col-lg-10 col-md-10 col-sm-10 col-xs-10">
                <input class="btn btn-danger" onclick="doToolbar(this.id)" id="plpass" type="button" value="通过">
                <input class="btn btn-primary" onclick="doToolbar(this.id)" id="plback" type="button" value="驳回" style="margin:0px 2px 0px 2px">
                <input class="btn btn-info" onclick="$('#plNeed_QlCount').dialog('close');" type="button" value="取消">
            </div>
            <div class="col-lg-1 col-md-1 col-sm-1 col-xs-1"></div>
        </div>
	</div>
    <script>
    var MI_iKeyCode = ''; //定义主键

	//点击.....按钮显示隐藏项
    $('#button-hidden').click(function () {
        $('.hide1').fadeToggle();
//         $('.hide2').fadeToggle();
    });
    
    var pageNum = 1;   //datagrid初始当前页数
	var pageSize = 10; //datagrid初始页内行数
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
		//审核学分窗口
		$('#Need_QlCount').dialog({   
			title:'学分申请审核',
			width: 300,//宽度设置   
			height: 370,//高度设置 
// 			width:fixWidth(0.4),
			modal:true, //是否是模式对话框  
			closed: true,   
			cache: false, 
			draggable:false,//是否可移动dialog框设置
			collapsible:true,  
	        resizable:true,
	        doSize:true, 
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
				loadData();
			}
		});
		
		//批量审核学分窗口
		$('#plNeed_QlCount').dialog({   
			title:'批量学分申请审核',
			width: 300,//宽度设置   
			height: 200,//高度设置 
// 			width:fixWidth(0.4),
			modal:true, //是否是模式对话框  
			closed: true,   
			cache: false, 
			draggable:false,//是否可移动dialog框设置
			collapsible:true,  
	        resizable:true,
	        doSize:true, 
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
				loadData();
			}
		});
		
	}
	
	
	//加载学分申请列表
	function loadGrid(listData){
		$("#bg").datagrid({
			data:listData,
			loadMsg : "信息加载中请稍侯!", //载入时信息
			title:'学分审核信息列表',
			width:'100%',
			nowrap:false,
// 			fit:true, //自适应父节点宽度和高度
			showFooter:true,
			striped:true,
			pagination:true,
			pageSize:pageSize,
// 			singleSelect:true,
			pageNumber:pageNum,
			rownumbers:true,
			fitColumns:true,
			scrollbarSize:0,
			columns:[[
				{field:'ck',checkbox:true},
				//field为读取数据的数据名，title为显示的数据名，width宽度设置，align数字在表格中显示的位置
				{field:'XNXQMC',title:'学年学期',width:fillsize(0.2),align:'center'},
// 				{field:'XH',title:'学号',width:fillsize(0.15),align:'center'},
				{field:'XM',title:'姓名',width:fillsize(0.2),align:'center'},
// 				{field:'BJMC',title:'班级名称',width:fillsize(0.1),align:'center'},
				{field:'KCMC',title:'课程名称',width:fillsize(0.2),align:'center'},
// 				{field:'CJ',title:'期末成绩',width:fillsize(0.1),align:'center'},
				{field:'JF',title:'加分',width:fillsize(0.2),align:'center'},
				{field:'SHZT',title:'审核状态',width:fillsize(0.2),align:'center',
					formatter:function(value,rec){
						if(value == '1') return '待审核';
						if(value == '2') return '审核通过';
						if(value == '3') return '审核驳回';
					}
				}
// 				{field:'BZ',title:'备注',width:fillsize(0.2),align:'center'}
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
		
// 		if (screen.width < 768){
//           $('#bg').datagrid('fixColumnSize'); 
          $('#bg').datagrid({height:'90%'}); 
//           $('#bg').datagrid('hideColumn','BZ');
// 	         $('#bg').datagrid('hideColumn','BJ');
// 	         $('#bg').datagrid('hideColumn','QMFS');
// 	         $('#bg').datagrid('hideColumn','XH'); 
//         }
		
		$("#bg").datagrid("getPager").pagination({
			total:listData.total,
			pageSize:pageSize,
			pageNumber:pageNum,
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
       
	
    /* 按钮方法 */
    function doToolbar(id){
    
    	//查询
		if(id == 'query'){
			loadData();
			
		}
		
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
			$('#Need_QlCount').dialog('open');
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
			
			var rows = $('#bg').datagrid('getSelections');
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
			
			
			$('#plNeed_QlCount').dialog('open');
			
			
		}
		
		//审核通过
		if(id == 'pass'){
			$('#active2').val('pass');
			$('#XX_SHXX').val($('#ic_shxx').val());
			$('#XX_ID').val(iKeyCode);
			$('#form_post2').submit();
		}
		
		//审核驳回
		if(id == 'back'){
			$('#active2').val('back');
			$('#XX_SHXX').val($('#ic_shxx').val());
			$('#XX_ID').val(iKeyCode);
			$('#form_post2').submit();
		}
		
		//批量审核通过
		if(id == 'plpass'){
			$('#active3').val('plpass');
			$('#XX_JF2').val(jf);
			$('#XX_XH2').val(xh);
			$('#XX_SHXX2').val($('#ic_shxx2').val());
			$('#XX_ID2').val(iKeyCode);
			$('#form_post3').submit();
		}
		
		//批量审核驳回
		if(id == 'plback'){
			$('#active3').val('plback');
			$('#XX_SHXX2').val($('#ic_shxx2').val());
			$('#XX_ID2').val(iKeyCode);
			$('#form_post3').submit();
		}
	}
	
	/**表单提交**/
	$('#form_post2').form({
		//定位到servlet位置的url
		url:'<%=request.getContextPath()%>/Svl_xfsh',
		//当点击事件后触发的事件
		onSubmit: function(data){}, 
		//当点击事件并成功提交后触发的事件
		success:function(data){
			var json  =  eval("("+data+")");
			if(json[0].MSG == '审核成功'){
				showMsg(json[0].MSG);
			}else{
				alertMsg(json[0].MSG);
			}
			$('#Need_QlCount').dialog('close');
		}
	});
	
	/**表单提交**/
	$('#form_post3').form({
		//定位到servlet位置的url
		url:'<%=request.getContextPath()%>/Svl_xfsh',
		//当点击事件后触发的事件
		onSubmit: function(data){}, 
		//当点击事件并成功提交后触发的事件
		success:function(data){
			var json  =  eval("("+data+")");
			if(json[0].MSG == '审核成功'){
				showMsg(json[0].MSG);
			}else{
				alertMsg(json[0].MSG);
			}
			$('#plNeed_QlCount').dialog('close');
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
	
	//清空查询表单
	function emptyDialog(){
		$('#ic_xnxq').combobox('setText','请选择');
		$('#ic_xnxq').combobox('setValue','');
		$('#ic_xh').val('');
		$('#ic_xm').val('');
		$('#ic_bjmc').val('');
		$('#ic_kcmc').val('');
		$('#ic_shzt').combobox('setText','待审核');
		$('#ic_shzt').combobox('setValue','1');
	}
	
	
    </script>
</body>
</html>
