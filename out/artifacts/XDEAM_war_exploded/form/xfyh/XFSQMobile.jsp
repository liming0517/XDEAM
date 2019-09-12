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
	    <base href="<%=basePath%>">
	    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no"/>
	    <meta http-equiv="x-ua-compatible" content="IE=7; IE=9" />
	    <title>学分申请</title>
	   
	    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/bootstrap.css"/>
	    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/themes/default/easyui.css"/>
	    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/mobile.css">  
	    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/icon.css">
	    <script type="text/javascript" src="<%=request.getContextPath()%>/script/angular.min.js"></script>
	    <script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
<!-- 		<script type="text/javascript" src="< %=request.getContextPath()%>/script/jquery-1.7.2.min.js"></script> -->
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

<!-- <div ng-include="'< %=request.getContextPath()%>/form/xfyh/top.jsp'" style='background:rgba(0, 168, 255, 0.4);height:70px'></div> -->
    
<div class="container">
<!--     <div class="row"> -->
<!--         <div class="col-lg-10 col-md-10 col-sm-12 col-xs-12"> -->
<!--             <div class="content-border"> -->
<!--                 <div class="ManageTitle"> -->
<!--                     <h5 ><b>学生加分申请</b></h5> -->
<!--                 </div> -->
<!--             </div> -->
<!--         </div> -->
<!--     </div> -->
    <div class="bg-box">
        <form class="colorForm" id='form_post' method='post'>
	        <div class="row">
        		<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
        			<label>姓名：</label>
        			<input name="ic_xm" id="ic_xm" style="width:34%;height:34px;display: inline-block;margin-right:2%" type="text" class="form-control" disabled>
        			<label>可用学分：</label>
        			<input  name="ic_kyfs" id="ic_kyfs" style="width:20%;height:34px;display: inline-block;" class="form-control " disabled="disabled">
        		</div>
        		<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 hide1 " style="margin-top:2%;">
        			<label>总分：</label>
        			<input name="ic_zf" id="ic_zf" style="width:18%;height:34px;display: inline-block;" type="text" class="form-control" disabled>
					<a style="display:inline-block;margin-right:2%;width:15%;" id="detail" onclick="doToolbar(this.id)">[详情]</a>
        			<label>已用分数：</label>
        			<input name="ic_yyfs" id="ic_yyfs" style="width:20%;height:34px;display: inline-block;" type="text" class="form-control"  disabled>
        		</div>
        	</div>
        	<div class="row hide2" style="margin-top:2%;">
        		<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
        			<label>学号：</label>
        			<input name="ic_xh" id="ic_xh" style="width:78%;height:34px;display: inline-block;margin-right:3%" type="text" class="form-control" disabled>
        		</div>
        	</div>
        	<div class="row hide2" style="margin-top:2%;">
        		<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
        			<label>班级：</label>
        			<input name="ic_bj" id="ic_bj" style="width:78%;height:34px;display: inline-block;margin-right:3%" type="text" class="form-control" disabled>
        		</div>
        	</div>
	        	
	      
            <div class="row btn-box" id='button-hidden'>
               <div class="col-lg-4 col-md-4 col-sm-4 col-xs-4"></div>
               <div class="col-lg-4 col-md-4 col-sm-4 col-xs-4" style="text-align: center">
                    <input class="btn btn-info"  id="more" type="button" value=".....">
               </div>
               <div class="col-lg-4 col-md-4 col-sm-4 col-xs-4"></div>
          	</div>
        </form>
    </div>
</div>
<div class="manage-list">
    <div class="row">
        <div class="container">
            <div class="col-lg-6 col-md-6 col-sm-4 col-xs-4" style="padding:0;margin-top: -0.7%;"><h4 style="font-size: 20px"><b>个人信息</b></h4></div>
			<div class="col-lg-6 col-md-6 col-sm-8 col-xs-8">
				<div class="text-right">
	                <input type="button" style="width: 50px" onclick="doToolbar(this.id)" id='new' class="btn form-control btn-success btn-sm" value="新增"/>
				    <input type="button" style="width: 50px" onclick="doToolbar(this.id)" id="edit" class="btn form-control btn-warning btn-sm" value="编辑"/>
					<input type="button" style="width: 50px" onclick="doToolbar(this.id)" id="delete" class="btn form-control btn-danger btn-sm" value="删除"/>
	            </div>
			</div>
		</div>
	</div>
</div>

 <div class="container" >
 	<div class="list-table" id="list-table" style="height:80%;">
		 <table id="bg">  
<!-- 		     <thead>   -->
<!-- 		         <tr>   -->
<!-- 		             <th data-options="field:'XQXN',width:fillsize(0.1),align:'center'">学年学期</th> -->
<!-- 		             <th data-options="field:'KC',width:fillsize(0.1),align:'center'">课程名称</th> -->
<!-- 		             <th data-options="field:'QM',width:fillsize(0.1),align:'center'">期末分数</th>  -->
<!-- 		             <th data-options="field:'JF',width:fillsize(0.1),align:'center'">加分</th>  -->
<!-- 		             <th data-options="field:'BZ',width:fillsize(0.2),align:'center'">备注</th>   -->
<!-- 		             <th data-options="field:'SQRQ',width:fillsize(0.1),align:'center'">申请日期</th>   -->
<!-- 		             <th data-options="field:'ZT',width:fillsize(0.1),align:'center'">审核状态</th>   -->
<!-- 		             <th data-options="field:'SHXX',width:fillsize(0.2),align:'center'">审核信息</th> -->
<!-- 		         </tr> -->
<!-- 		     </thead>   -->
		 </table>
	 </div>
</div>


	<!-- 新增/编辑 -->
	<div id="Need_QlCount" style="display:none;overflow-x:hidden;">
		<form  id='form_post2' method='post' style="padding: 0px 10px 0px 10px;">
			<table style="width:100%;font-size:100%;">
				<tr>
					<td>学年学期：</td>
					<td><input id='ic_xnxq2' name='ic_xnxq2' style="width:100%;height:34px;" type="text" class="form-control" disabled></td>
				</tr>
				<tr>
					<td>课程名称：</td>
					<td><select id='ic_kcmc' name='ic_kcmc' style="width:100%;"  class="easyui-combobox" >
					</select></td>
				</tr>
				<tr>
					<td>期末成绩：</td>
					<td><input id='ic_qmcj' name='ic_qmcj' style="width:100%;height:34px;"  type="text" class="form-control" disabled></td>
				</tr>
				<tr>
					<td>加分：</td>
					<td><input id='ic_jf' name='ic_jf' class="easyui-numberbox" data-options="min:0,precision:0" style="width:100%;height:34px;"  class="form-control"></td>
				</tr>
				<tr>
					<td>备注：</td>
				</tr>
				<tr>
					<td colspan=2><textarea id="ic_bz" name='ic_bz' class="form-control" style="width:100%;height:60px;resize:none;" maxlength='100'></textarea></td>
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
		<div class="row btn-box" style="margin-top:5%">
            <div class="col-lg-2 col-md-2 col-sm-2 col-xs-2"></div>
            <div class="col-lg-8 col-md-8 col-sm-8 col-xs-8">
                <input class="btn btn-danger" onclick="doToolbar(this.id)" id="save" type="button" value="保存">
                <input class="btn btn-primary" onclick="doToolbar(this.id)" id="submit" type="button" value="提交" style="margin:0px 2px 0px 2px">
                <input class="btn btn-info" onclick="$('#Need_QlCount').dialog('close');" type="button" value="取消">
            </div>
            <div class="col-lg-2 col-md-2 col-sm-2 col-xs-2"></div>
       	</div>
	</div>
	
	<!-- 详情 -->
	<div id="Need_QlCount1" style="display:none;overflow-x:hidden;">
		<div class="list-table " >
		<div class="row">
            <div class="col-lg-6 col-md-6 col-sm-12 col-xs-12" style="padding:0;text-align: center;">
            	<label>学年学期：</label>
               <select id='ic_xnxq' name='ic_xnxq' style="width:50%;display: inline-block;"  class="easyui-combobox" >
				</select>
            </div>
			
		</div>
		 <table id="bg1" >  
<!-- 		     <thead>   -->
<!-- 		         <tr>   -->
<!-- 		         	 <th data-options="field:'XQXN',width:fillsize(0.1),align:'center'">学年学期</th> -->
<!-- 		         	 <th data-options="field:'BJ',width:fillsize(0.1),align:'center'">班级</th>  -->
<!-- 		             <th data-options="field:'KC',width:fillsize(0.1),align:'center'">课程</th>  -->
<!-- 		             <th data-options="field:'SKJS',width:fillsize(0.1),align:'center'">教师</th>  -->
<!-- 		             <th data-options="field:'CJ',width:fillsize(0.1),align:'center'">成绩</th>  -->
		             
<!-- 		         </tr> -->
<!-- 		     </thead>   -->
		 </table>
	</div>
	
	
			
			<div class="row btn-box" style="margin-top:5%;margin-bottom:5%">
                <div class="col-lg-4 col-md-4 col-sm-4 col-xs-4"></div>
                <div class="col-lg-4 col-md-4 col-sm-4 col-xs-4" style="text-align: center;">
                    <input class="btn btn-danger" onclick="$('#Need_QlCount1').dialog('close');" type="button" value="确定">
                </div>
                <div class="col-lg-4 col-md-4 col-sm-4 col-xs-4"></div>
           	</div>
	</div>

</body>

<script type="text/javascript">

	
	var MI_iKeyCode = ''; //定义主键
	var pageNum = 1;   //datagrid初始当前页数
	var pageSize = 10; //datagrid初始页内行数
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

	//点击.....按钮显示隐藏项
    $('#more').click(function () {
    	$('.hide').fadeToggle();
     	$('.hide1').fadeToggle();
     	$('.hide2').fadeToggle();
    
//     	if($('.hide2').css('display')=='none'){
// 	   		$('.hide').fadeToggle();
// 	        $('.hide1').fadeToggle();
// 	        $('.hide2').fadeToggle();
// 	        $('#bg').datagrid({height:'55%'}); 
//      	}else{
//         	$('.hide').fadeToggle();
// 	        $('.hide1').fadeToggle();
// 	        $('.hide2').fadeToggle();
// 	        alert($('#list-table').css('height'))
// 	       	$('#bg').datagrid({height:'85%'}); 
//     	}
        
    });
    
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
	
	//学生信息
	function loadXSXX(XSXX){
		$('#ic_xm').val(XSXX.xm);
		$('#ic_xh').val(XSXX.xh);
		$('#ic_bj').val(XSXX.xzbmc);
		$('#ic_zf').val(XSXX.zf);
		$('#ic_yyfs').val(XSXX.yyfs);
		$('#ic_kyfs').val(XSXX.kyfs);
		zxf =  parseInt(XSXX.kyfs);
		xm = XSXX.xm;
		xh = XSXX.xh;
		xzbmc = XSXX.xzbmc;
		xzbdm = XSXX.xzbdm;
	}
	
	function initDialog(){
		//新建/编辑学分窗口
		$('#Need_QlCount').dialog({   
			title:'新建/编辑学分',
			width: 300,//宽度设置   
			height: 370,//高度设置 
			modal:true,
			closed: true,   
			cache: false, 
			draggable:false,//是否可移动dialog框设置
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
		
		//打开详情弹框
		$('#Need_QlCount1').dialog({
			title:'学分历史详情',
			width: 300,//宽度设置   
			height: 400,//高度设置 
			modal:true,//是否是模式对话框  
			closed: true,   
			cache: false, 
			draggable:false,//是否可移动dialog框设置
			collapsible:true,  
         	resizable:true, 
         	doSize:true, 
			//打开事件
			onOpen:function(data){
// 				if (screen.width < 768){
// 					$('#bg1').datagrid({height:'250',}); 
// 		        }
			},
			//读取事件
			onLoad:function(data){},
			//关闭事件
			onClose:function(data){
// 				$('#bg1').datagrid('loadData',{total:0,rows:[]});
			}
		});
	}
	
	/**加载 datagrid控件，读取页面信息
		@listData 列表数据
	**/
	
	//加载学分申请列表
	function loadGrid(listData){
		$("#bg").datagrid({
			data:listData,
			title:'学分申请信息列表',
			width:'100%',
			nowrap:false,
// 			fit:true, //自适应父节点宽度和高度
			showFooter:true,
			striped:true,
			pagination:true,
			singleSelect:true,
			rownumbers:true,
			fitColumns:true,
			scrollbarSize :0 ,
			pageNumber:pageNum,
			pageSize:pageSize,
// 			pageList: [5, 10, 15, 20, 25, 30], //页面显示条目数
			columns:[[
				//field为读取数据的数据名，title为显示的数据名，width宽度设置，align数字在表格中显示的位置
				{field:'XNXQMC',title:'学年学期',width:fillsize(0.2),align:'center'},
				{field:'KCMC',title:'课程名称',width:fillsize(0.2),align:'center'},
				{field:'QM',title:'期末分数',width:fillsize(0.2),align:'center'},
				{field:'JF',title:'加分',width:fillsize(0.12),align:'center'},
// 				{field:'BZ',title:'备注',width:fillsize(0.2),align:'center'},
// 				{field:'CJSJ',title:'申请日期',width:fillsize(0.1),align:'center'},
				{field:'SHZT',title:'审核状态',width:fillsize(0.2),align:'center',
					formatter:function(value,rec){
						if(value == '0') return '未提交';
						if(value == '1') return '待审核';
						if(value == '2') return '审核通过';
						if(value == '3') return '审核驳回';
					}
				}
// 				{field:'SHXX',title:'审核信息',width:fillsize(0.2),align:'center'}
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
// 		if (screen.width < 768){
//          $('#bg').datagrid('fixColumnSize');  
        	$('#bg').datagrid({height:'87%'}); 
//          $('#bg').datagrid('hideColumn','BZ');
// 	        $('#bg').datagrid('hideColumn','CJSJ');
// 	        $('#bg').datagrid('hideColumn','SHXX'); 
//      	}
		
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
			url : '<%=request.getContextPath()%>/Svl_xfsq',
			data : 'active=query&page=' + pageNum + '&rows=' + pageSize,
			dataType:"json",
			success : function(data) {
				loadGrid(data[0].listData);
			}
		});
	}
	
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
	
	/**表单提交**/
	$('#form_post2').form({
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
				$('#submit').attr('disabled',false);
			}else{
				alertMsg(json[0].MSG);
			}
		}
	});
	
	
	function loadxfxqGrid(xfsqData){
		$("#bg1").datagrid({
			data:xfsqData,
			title:'学分详情列表',
			width:'100%',
			nowrap:false,
// 			fit:true, //自适应父节点宽度和高度
// 			showFooter:true,
			striped:true,
// 			pagination:true,
			pageSize:pageSize,
			singleSelect:true,
			pageNumber:pageNum,
			rownumbers:true,
			scrollbarSize:0,
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
	
    /* 按钮方法 */
    function doToolbar(id){
    
    	//详情
		if(id == 'detail'){
			$('#Need_QlCount1').dialog('open');
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
		
		//新增
		if(id == 'new'){
			$('#Need_QlCount').dialog('open');
			$('#save').attr("disabled",false);
			$('#submit').attr("disabled",true);
			$('#ic_xnxq2').val(dqxnxq);
			$('#ic_jf').numberbox('enable',true);
			$('#ic_bz').attr("disabled",false);
			$('#ic_kcmc').combobox('enable');
			
		}
		
		//编辑
		if(id == 'edit'){
			if(iKeyCode == ''){
				alertMsg('请选择一行数据');
				return
			}
			tj = 'edit';
			
			if(shzt == '0'){
				$('#Need_QlCount').dialog('open');
				$('#save').attr("disabled",false);
				$('#submit').attr("disabled",false);
				
				$('#ic_xnxq2').val(dqxnxq);
				$('#ic_kcmc').combobox('setValue', xgbh);
				$('#ic_kcmc').combobox('enable');
				$('#ic_qmcj').val(cj);
				$('#ic_jf').numberbox('setValue', jf);
				$('#ic_jf').numberbox('enable',true);
				$('#ic_bz').val(bz);
				$('#ic_bz').attr("disabled",false);
			}else{
				$('#Need_QlCount').dialog('open');
				$('#save').attr("disabled",true);
				$('#submit').attr("disabled",true);
				
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
		//删除
		if(id == 'delete'){
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
		
		//新增编辑弹框保存
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
				alertMsg("您输入的分值已超出可用学分!");
				return;
			}
			if(zgcj == $('#ic_qmcj').val()){
				alertMsg("此分数已为最高分数，不能加分！");
				return;
			}
			
			if(msg == '特殊成绩无法加分'){
				alertMsg("特殊成绩无法加分！");
				return;
			}
			
			if(parseInt($("#ic_jf").numberbox("getValue")) == 0){
				alertMsg("输入的分值要大于零!");
				return;
			}
			
			//当成绩加上加分超过100时，显示最大的加分数
// 			if((parseFloat($("#ic_jf").numberbox("getValue"))+parseFloat($('#ic_qmcj').val())) > 100){
// 				var cz = 100.00-parseFloat($('#ic_qmcj').val());
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
						$('#form_post2').submit();
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
				$('#form_post2').submit();
			}
		}
		//新增编辑弹框提交
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
				alertMsg("您输入的分值已超出可用学分!");
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
			
			if(msg == '特殊成绩无法加分'){
				alertMsg("特殊成绩无法加分！");
				return;
			}
			
			if(parseInt($("#ic_jf").numberbox("getValue")) == 0){
				alertMsg("输入的分值要大于零!");
				return;
			}
			
			//当成绩加上加分超过100时，显示最大的加分数
// 			if((parseFloat($("#ic_jf").numberbox("getValue"))+parseFloat($('#ic_qmcj').val())) > 100){
// 				var cz = 100.00-parseFloat($('#ic_qmcj').val());
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
								
								$('#Need_QlCount').dialog('close');
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
						
						$('#Need_QlCount').dialog('close');
					}
				});
			}
		}
	}
	

		
		
    
</script>


</html>
