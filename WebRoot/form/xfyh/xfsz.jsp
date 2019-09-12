<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="java.util.Vector"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.src.develop.store.user.*"%>
<%
	/**
		创建人：yangda
		Create date: 2017.10.20
		功能说明：学分设置
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
    <title>学分设置</title>
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
	<div id="north" region="north" title="学分设置" style="height:116px;">
		<table>
			<tr>
				<td><a href="#" id="new" class="easyui-linkbutton" plain="true" iconcls="icon-new" onClick="doToolbar(this.id);" title="">新建</a></td>
				<td><a href="#" id="edit" class="easyui-linkbutton" plain="true" iconcls="icon-edit" onClick="doToolbar(this.id);" title="">编辑</a></td>
				<td><a href="#" id="del" class="easyui-linkbutton" plain="true" iconCls="icon-cancel" onclick="doToolbar(this.id)">删除</a></td>
			</tr>
		</table>
<!-- 		<table id="ee2" singleselect="true" width="100%" class="tablestyle"> -->
<!-- 			<tr> -->
<!-- 				<td> -->
<!-- 					<a href="#" id="xfsz" class="easyui-linkbutton" plain="true" iconcls="icon-audit" onClick="doToolbar(this.id);" title="" >学分设置</a> -->
<!-- 				</td> -->
<!-- 			</tr> -->
<!-- 	    </table> -->
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
				<td class="titlestyle" style="width:150px;">名称</td>
				<td>
					<input name="ic_mc" id="ic_mc" style="width:180px;">
				</td>
				<td class="titlestyle" style="width:150px;">授课教师</td>
				<td>
					<input name="ic_skjs" id="ic_skjs" style="width:200px;">
				</td>
				<td class="titlestyle" style="width:150px;">类型名称</td>
				<td>
					<select name="ic_lx" id="ic_lx" class="easyui-combobox"style="width:200px;">
					</select>
				</td>
				<td>
				</td>
			</tr>
	    </table>
	</div>
	<div region="center">
		<table id="xfszList" style="width:100%;"></table>
	</div>
	
	<!-- 设置窗口 -->
	<div id="xfszDialog" style="overflow:hidden; display:none;">
		<form id="form1" method="post">
			<table>
				<tr>
					<td><a href="#" id="save" class="easyui-linkbutton" plain="true" iconCls="icon-save" onclick="doToolbar(this.id)">保存</a></td>			
				</tr>
			</table>
			<table id="ee" singleselect="true" width="100%" class="tablestyle">
				<tr>
					<td class="titlestyle" style="width:80px;">学年学期</td>
					<td>
						<select name="ic_xnxq2" id="ic_xnxq2" class="easyui-combobox" style="width:250px;">
						</select>
					</td>
				</tr>
				<tr>
					<td class="titlestyle">名称</td>
					<td>
						<input id="ic_mc2" name="ic_mc2" class="easyui-textbox" style="width:250px;" required="true"/>
					</td>
				</tr>
				<tr>
					<td class="titlestyle">类型</td>
					<td>
						<select name="ic_lx2" id="ic_lx2" class="easyui-combobox" style="width:250px;">
						</select>
					</td>
				</tr>
				<tr>
					<td class="titlestyle">授课教师</td>
					<td>
						<input id="ic_skjs2" name="ic_skjs2" value="点击选择授课教师" style="width:250px; color:#A0A0A0; cursor:pointer;" onclick="openTeaList(id);" />
					</td>
				</tr>
				<tr>
					<td class="titlestyle">学分</td>
					<td>
						<input id="ic_xf2" name="ic_xf2"  style="width:250px;" type="text" class="easyui-numberbox" data-options="min:0,precision:0" required="true"/>
					</td>
				</tr>
				<tr>
					<td class="titlestyle">学生名单</td>
					<td>
						<input id="ic_xsmd" name="ic_xsmd" value="点击选择学生名单" style="width:250px; color:#A0A0A0; cursor:pointer;" onclick="openStuList(id);"  />
					</td>
				</tr>
		    </table>
			<input type="hidden" id="active" name="active"/>
			<input type="hidden" id="XX_XNXQBM" name="XX_XNXQBM"/>
			<input type="hidden" id="XX_XH" name="XX_XH"/>
			<input type="hidden" id="XX_XM" name="XX_XM"/>
			<input type="hidden" id="XX_MC" name="XX_MC"/>
			<input type="hidden" id="XX_LX" name="XX_LX"/>
			<input type="hidden" id="XX_SKJS" name="XX_SKJS"/>
			<input type="hidden" id="XX_SKJSGH" name="XX_SKJSGH"/>
			<input type="hidden" id="XX_XF" name="XX_XF"/>
			<input type="hidden" id="XX_ID" name="XX_ID"/>
	    </form>
	</div>
	
	<!-- 学生列表 -->
	<div id="stuListDialog">
		<div class="easyui-layout" style="width:100%; height:100%;">
			<div region="north" style="height:64px; overflow:hidden;">
				<table>
					<tr>
						<td><a href="#" id="saveXfStu" class="easyui-linkbutton" plain="true" iconCls="icon-ok" onClick="doToolbar(this.id);">确认</a></td>
					</tr>
				</table>
				<table id="ee" singleselect="true" width="100%" class="tablestyle">
					<tr>
						<td class="titlestyle" style="width:100px;">学号</td>
						<td>
							<input name="ic_xh_2" id="ic_xh_2" style="width:150px;">
						</td>
						<td style="width:100px;" class="titlestyle">姓名</td>
						<td>
							<input name="ic_xm_2" id="ic_xm_2" style="width:150px;">
						</td>
						<td style="text-align:center;">
							<a href="#" id="queryStu" class="easyui-linkbutton" plain="true" iconcls="icon-search" onClick="doToolbar(this.id);" title="">查询</a>
						</td>
					</tr>
	    		</table>
			</div>
			<div region="center">
				<table id="stuList" style="width:100%;"></table>
			</div>
		</div>
	</div>
	
	<!-- 授课老师列表 -->
	<div id="teaListDialog"">
		<div class="easyui-layout" style="width:100%; height:100%;">
			<div region="north" style="height:64px; overflow:hidden;">
				<table>
					<tr>
						<td><a href="#" id="saveTea" class="easyui-linkbutton" plain="true" iconCls="icon-ok" onClick="doToolbar(this.id);">确认</a></td>
					</tr>
				</table>
				<table id="ee" singleselect="true" width="100%" class="tablestyle">
					<tr>
						<td class="titlestyle" style="width:100px;">工号</td>
						<td>
							<input name="ic_gh_3" id="ic_gh_3" style="width:150px;">
						</td>
						<td style="width:100px;" class="titlestyle">姓名</td>
						<td>
							<input name="ic_xm_3" id="ic_xm_3" style="width:150px;">
						</td>
						<td style="text-align:center;">
							<a href="#" id="queryTea" class="easyui-linkbutton" plain="true" iconcls="icon-search" onClick="doToolbar(this.id);" title="">查询</a>
						</td>
					</tr>
	    		</table>
			</div>
			<div region="center">
				<table id="teaList" style="width:100%;"></table>
			</div>
		</div>
	</div>
</body>
	<script>
		var pageNum = 1;   //datagrid初始当前页数
		var pageSize = 20; //datagrid初始页内行数
		var row = '';      //行数据
		var iKeyCode = ''; //定义主键
		var stuCode=''; //学生学号
		var stuName=''; //学生姓名
		var teaCode=''; //教师工号
		var teaName=''; //教师姓名
		var xnxqmc = ''; //学年学期名称
		var xnxqbm = ''; //学年学期编码
		var mc = ''; //名称
		var lxdm = ''; //类型代码
		var xf = ''; //学分
		var zt = '';//状态
		var teacodearray = new Array();//存放教师编号
		var teanamearray = new Array();//存放教师
		var teainfocodearray = new Array();//存放教师编号(保存结果)
		var teainfonamearray = new Array();//存放教师(保存结果)
		var stucodearray = new Array();//存放学生编号
		var stunamearray = new Array();//存放学生
		var stuinfocodearray = new Array();//存放学生编号(保存结果)
		var stuinfonamearray = new Array();//存放学生(保存结果)
		var tempKyxf='';//查询出最低可用学分(判断是否可以修改学分)
		
		$(document).ready(function(){
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_xfsz',
				data : 'active=initData&page=' + pageNum + '&rows=' + pageSize,
				dataType:"json",
				success : function(data) {
					initDialog();//初始化对话框
					loadGrid(data[0].listData);//页面初始化加载数据
					initCombobox(data[0].xnxqData,data[0].lxData);
				}
			});
		});
		
		
		function initDialog(){
			$('#xfszDialog').show();
			
			//新建/编辑学分窗口
			$('#xfszDialog').dialog({   
				title:'新建/编辑学分',
				width: 350,//宽度设置   
				height: 219,//高度设置 
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
					$("#ic_xnxq2").combobox("setValue",'');
					$("#ic_xnxq2").combobox("setText",'请选择');
					$("#ic_xf2").numberbox("setValue",'');
					$("#ic_mc2").textbox('setValue', '');
					$("#ic_lx2").combobox("setValue",'');
					$("#ic_lx2").combobox("setText",'请选择');
					$('#ic_skjs2').css('color', '#A0A0A0');
					$("#ic_skjs2").val('点击选择授课教师');
					$('#ic_xsmd').css('color', '#A0A0A0');
					$("#ic_xsmd").val('点击选择学生名单');
					stuCode='';
					stuName='';
					teaCode='';
					teaName='';
					iKeyCode='';
					tempKyxf='';
					zt = '';//状态
				}
			});
			
			//学生列表
			$('#stuListDialog').dialog({   
				title: '学生列表',   
				width: 700,//宽度设置   
				height: 400,//高度设置 
				modal:true,
				closed:true,   
				cache: false, 
				draggable:true,//是否可移动dialog框设置
				//读取事件
				onLoad:function(data){
				},
				//关闭事件
				onClose:function(data){
					$('#ic_xh_2').val('');
					$('#ic_xm_2').val('');
					stuinfocodearray.splice(0,stuinfocodearray.length);
					stuinfonamearray.splice(0,stuinfonamearray.length);
					stucodearray.splice(0,stucodearray.length);
					stunamearray.splice(0,stunamearray.length);
				}
			});
			
			//授课教师列表
			$('#teaListDialog').dialog({   
				title: '授课教师列表',   
				width: 700,//宽度设置   
				height: 400,//高度设置 
				modal:true,
				closed:true,   
				cache: false, 
				draggable:true,//是否可移动dialog框设置
				//读取事件
				onLoad:function(data){
				},
				//关闭事件
				onClose:function(data){
					$('#ic_gh_3').val('');
					$('#ic_xm_3').val('');
					teainfocodearray.splice(0,teainfocodearray.length);
					teainfonamearray.splice(0,teainfonamearray.length);
					teacodearray.splice(0,teacodearray.length);
					teanamearray.splice(0,teanamearray.length);
				}
			});
			
		}
		
		
	//加载学分设置列表
	function loadGrid(listData){
		$("#xfszList").datagrid({
			data:listData,
			title:'学分设置信息列表',
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
				{field:'XNXQMC',title:'学年学期名称',width:fillsize(0.2),align:'center'},
				{field:'XH',title:'学号',width:fillsize(0.2),align:'center'},
				{field:'XM',title:'姓名',width:fillsize(0.15),align:'center'},
				{field:'MC',title:'名称',width:fillsize(0.2),align:'center'},
				{field:'LXMC',title:'类型名称',width:fillsize(0.15),align:'center'},
				{field:'SKJS',title:'授课教师',width:fillsize(0.2),align:'center'},
				{field:'XF',title:'学分',width:fillsize(0.1),align:'center'}
				
			]],
			//双击某行时触发
			onDblClickRow:function(rowIndex,rowData){},
			//读取datagrid之前加载
			onBeforeLoad:function(){},
			//单击某行时触发
			onClickRow:function(rowIndex,rowData){
				iKeyCode = rowData.ID;
				stuCode=rowData.XH; //学生学号
				stuName=rowData.XM; //学生姓名
				teaCode=rowData.SKJSGH; //教师工号
				teaName=rowData.SKJS; //教师姓名
				xnxqmc = rowData.XNXQMC; //学年学期名称
				xnxqbm = rowData.XNXQBM; //学年学期编码
				mc = rowData.MC; //名称
				lxdm = rowData.LXDM; //类型代码
				xf = rowData.XF; //学分
			},
			//加载成功后触发
			onLoadSuccess: function(data){
			}
		});
		
		$("#xfszList").datagrid("getPager").pagination({
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
			url : '<%=request.getContextPath()%>/Svl_xfsz',
			data : 'active=query&page=' + pageNum + '&rows=' + pageSize + '&XX_XNXQBM=' + $('#ic_xnxq').combobox('getValue') + '&XX_XH=' + $('#ic_xh').val()
					+ '&XX_XM=' + $('#ic_xm').val() + '&XX_MC=' + $('#ic_mc').val() + '&XX_SKJS=' + $('#ic_skjs').val() + '&XX_LX=' + $('#ic_lx').combobox('getValue'),
			dataType:"json",
			success : function(data) {
				loadGrid(data[0].listData);
			}
		});
	}
	
	//按钮功能
	function doToolbar(id){
		//查询
		if(id == 'query'){
			loadData();
		}
	
		//新建
		if(id == 'new'){
			$('#ic_xnxq2').combobox('enable');
			stuCode='';
			stuName='';
			teaCode='';
			teaName='';
			$('#xfszDialog').dialog('setTitle', '新建');
			$('#xfszDialog').dialog('open');
		}
		
		//保存
		if(id == 'save'){
			if($("#ic_xnxq2").combobox("getValue") == ''){
				alertMsg("请选择学年学期!");
				return;
			}
			if($("#ic_mc2").textbox('getValue') == ''){
				alertMsg("请输入名称!");
				return;
			}
			if($("#ic_lx2").combobox("getValue") == ''){
				alertMsg("请选择类型!");
				return;
			}
			if($("#ic_skjs2").val() == '点击选择授课教师'){
				alertMsg("请选择教师!");
				return;
			}
			if($("#ic_xf2").numberbox("getValue") == ''){
				alertMsg("请输入学分!");
				return;
			}
			if(Number($("#ic_xf2").numberbox("getValue")) == 0){
				alertMsg("输入的分值要大于零!");
				return;
			}
			if($("#ic_xsmd").val() == '点击选择学生名单'){
				alertMsg("请选择学生!");
				return;
			}
			
			if(zt == 'edit'){
				if(Number($("#ic_xf2").numberbox("getValue")) < (Number(xf) - Number(tempKyxf))){
					alertMsg("部分学分已被使用!可允许修改的最小值为:" + (Number(xf) - Number(tempKyxf)));
					$("#ic_xf2").numberbox("setValue",(Number(xf) - Number(tempKyxf)));
					return;
				}
			
				$('#active').val('edit');
			}else{
				$('#active').val(id);
			}
			
			$('#XX_XNXQBM').val($("#ic_xnxq2").combobox("getValue"));
			$('#XX_XH').val(stuCode);
			$('#XX_XM').val(stuName);
			$('#XX_MC').val($("#ic_mc2").textbox('getValue'));
			$('#XX_LX').val($("#ic_lx2").combobox("getValue"));
			$('#XX_SKJS').val(teaName);
			$('#XX_ID').val(iKeyCode);
			$('#XX_SKJSGH').val(teaCode);
			$('#XX_XF').val($("#ic_xf2").numberbox("getValue"));
			$('#form1').submit();
			
		}
		
		
		//删除
		if(id == 'del'){
			if(iKeyCode == ''){
				alertMsg('请选择一行数据');
				return
			}
			
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_xfsz',
				data : 'active=queryKyxf&XX_XNXQBM=' + xnxqbm + '&XX_XH=' + stuCode,
				dataType:"json",
				success : function(data) {
					tempKyxf = data[0].curKyxf;
					if(Number(xf) > Number(tempKyxf)){
						alertMsg("部分学分已被使用!不能删除!");
						return;
					}
					$.ajax({
						type : "POST",
						url : '<%=request.getContextPath()%>/Svl_xfsz',
						data : 'active=del&XX_ID='+iKeyCode,
						dataType:"json",
						success : function(data) {
							if(data[0].MSG == '删除成功'){
								loadData();
								showMsg(data[0].MSG);
							}else{
								alertMsg(data[0].MSG);
							}
						}
					});
				}
			});
		}
		
		//编辑
		if(id == 'edit'){
			if(iKeyCode == ''){
				alertMsg('请选择一行数据');
				return
			}
			$('#xfszDialog').dialog('setTitle', '编辑');
			$('#xfszDialog').dialog('open');
			
			$('#ic_xnxq2').combobox('setValue',xnxqbm);
			$('#ic_xnxq2').combobox('disable',true);
			$("#ic_mc2").textbox('setValue', mc);
			$('#ic_lx2').combobox('setValue',lxdm);
			$('#ic_skjs2').css('color', '#000000');
			$('#ic_skjs2').val(teaName);
			$('#ic_xf2').numberbox('setValue',xf);
			$('#ic_xsmd').css('color', '#000000');
			$('#ic_xsmd').val(stuName);
			zt = 'edit';//状态
			
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_xfsz',
				data : 'active=queryKyxf&XX_XNXQBM=' + xnxqbm + '&XX_XH=' + stuCode,
				dataType:"json",
				success : function(data) {
					tempKyxf = data[0].curKyxf;
				}
			});
			
		}
		
		//保存学分学生
		if(id == 'saveXfStu'){
// 			var data = $('#stuList').datagrid('getSelections');
// 			stuCode = '';
// 			stuName = '';
			
// 			if(data.length > 0){
// 				for(var i=0; i<data.length; i++){
// 					stuCode += data[i].XH+',';
// 					stuName += data[i].XM+',';
// 				}
// 				stuCode = stuCode.substring(0, stuCode.length-1);
// 				stuName = stuName.substring(0, stuName.length-1);
				
// 				$('#ic_xsmd').val(stuName);
// 			}

			stuCode = '';
			stuName = '';
			
			stuinfocodearray=stucodearray;
			stuinfonamearray=stunamearray;
			for (var i = 0;i < stuinfocodearray.length;i++) {
				if(stuName==''){
					stuCode+=stuinfocodearray[i];
					stuName+=stuinfonamearray[i];
				}else{
					stuCode+=","+stuinfocodearray[i];
					stuName+=","+stuinfonamearray[i];
				}
			}
			
			if(stuName == ''){
				$('#ic_xsmd').css('color', '#A0A0A0');
				$("#ic_xsmd").val('点击选择学生名单');
			}else{
				$('#ic_xsmd').css('color', '#000000');
				$('#ic_xsmd').val(stuName);
			}
			
			stuinfocodearray=new Array();
			stuinfonamearray=new Array();
			stucodearray=new Array();
			stunamearray=new Array();

			$('#stuListDialog').dialog('close');
		}
		
		//保存教师
		if(id == 'saveTea'){
// 			var data = $('#teaList').datagrid('getSelections');
			
// 			teaCode = '';
// 			teaName = '';
			
			
// 			if(data.length > 0){
// 				for(var i=0; i<data.length; i++){
// 					teaCode += data[i].GH+',';
// 					teaName += data[i].XM+',';
// 				}
// 				teaCode = teaCode.substring(0, teaCode.length-1);
// 				teaName = teaName.substring(0, teaName.length-1);
				
// 				$('#ic_skjs2').val(teaName);
// 			}
			
			teaCode = '';
			teaName = '';
			
			teainfocodearray=teacodearray;
			teainfonamearray=teanamearray;
			for (var i = 0;i < teainfocodearray.length;i++) {
				if(teaName==''){
					teaCode+=teainfocodearray[i];
					teaName+=teainfonamearray[i];
				}else{
					teaCode+=","+teainfocodearray[i];
					teaName+=","+teainfonamearray[i];
				}
			}
			
			if(teaName == ''){
				$('#ic_skjs2').css('color', '#A0A0A0');
				$("#ic_skjs2").val('点击选择授课教师');
			}else{
				$('#ic_skjs2').css('color', '#000000');
				$('#ic_skjs2').val(teaName);
			}
			
			teainfocodearray=new Array();
			teainfonamearray=new Array();
			teacodearray=new Array();
			teanamearray=new Array();
			
			$('#teaListDialog').dialog('close');
		}
		
		//查询学生
		if(id == 'queryStu'){
			loadStuList();
		}
		
		//查询老师
		if(id == 'queryTea'){
			loadTeaList();
		}
		
	}
	
	/**表单提交**/
	$('#form1').form({
		//定位到servlet位置的url
		url:'<%=request.getContextPath()%>/Svl_xfsz',
		//当点击事件后触发的事件
		onSubmit: function(data){}, 
		//当点击事件并成功提交后触发的事件
		success:function(data){
			var json  =  eval("("+data+")");
			if(json[0].MSG == '保存成功' || json[0].MSG == '修改成功'){
				loadData();
				$('#xfszDialog').dialog('close');
				showMsg(json[0].MSG);
			}else{
				alertMsg(json[0].MSG);
			}
		}
	});
	
	
	function initCombobox(xnxqData,lxData){
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
		
		//学年学期下拉框
		$('#ic_xnxq2').combobox({
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
		
		//类型下拉框
		$('#ic_lx').combobox({
			data:lxData,
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
		
		//类型下拉框
		$('#ic_lx2').combobox({
			data:lxData,
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
		
		
	}
	
	
	/**打开学生列表**/
	function openStuList(id){
		$('#' + id).blur();
		if(stuCode!=""){
			var stuid=stuCode.split(",");
			var stuna=stuName.split(",");
			for(var i=0;i<stuid.length;i++){
				stuinfocodearray.push(stuid[i]);
				stuinfonamearray.push(stuna[i]);
				stucodearray.push(stuid[i]);
				stunamearray.push(stuna[i]);				
			}
	 	}else{
	 		stuinfocodearray.splice(0,stuinfocodearray.length);
			stuinfonamearray.splice(0,stuinfonamearray.length);
			stucodearray.splice(0,stucodearray.length);
			stunamearray.splice(0,stunamearray.length);
	 	}
		$('#stuListDialog').dialog('open');
		loadStuList();
	}
	
	/**打开授课教师列表**/
	function openTeaList(id){
		$('#' + id).blur();
		if(teaCode!=""){
			var teaid=teaCode.split(",");
			var teana=teaName.split(",");
			for(var i=0;i<teaid.length;i++){
				teainfocodearray.push(teaid[i]);
				teainfonamearray.push(teana[i]);
				teacodearray.push(teaid[i]);
				teanamearray.push(teana[i]);				
			}
	 	}else{
	 		teainfocodearray.splice(0,teainfocodearray.length);
			teainfonamearray.splice(0,teainfonamearray.length);
			teacodearray.splice(0,teacodearray.length);
			teanamearray.splice(0,teanamearray.length);
	 	}
		 
		$('#teaListDialog').dialog('open');
		loadTeaList();
	}
	
	function loadStuList(){
		$('#stuList').datagrid({
			url :'<%=request.getContextPath()%>/Svl_xfsz',
			queryParams: {'active':'queStuList','XX_XH':$('#ic_xh_2').val(),'XX_XM':$('#ic_xm_2').val(),'XZXSXH':stuCode},
			title:'学生信息列表',
			width:'100%',
// 			idField:'XH',
			nowrap: false,
			fit:true, //自适应父节点宽度和高度
			showFooter:true,
			striped:true,
			singleSelect:false,
			rownumbers:true,
			fitColumns: true,
			pagination:true,
			pageSize:pageSize,
			pageNumber:pageNum,
			columns:[[
				//field为读取数据的数据名，title为显示的数据名，width宽度设置，align数字在表格中显示的位置
				{field:'ck',checkbox:true},
				{field:'XH',title:'学号',width:fillsize(0.5),align:'center'},
				{field:'XM',title:'姓名',width:fillsize(0.5),align:'center'}
			]],
			//加载成功后触发
// 			onLoadSuccess: function(data){
				//勾选已选学生
// 				if(data && data.rows.length>0){
// 					var firstIndex = -1;
// 					var stuCode_1 = stuCode.split(',');
					
// 					$.each(data.rows, function(rowIndex, rowData){
// 						for(var i=0; i<stuCode_1.length; i++){
// 							if(stuCode_1[i] == rowData.XH){
// 								if(firstIndex == -1)
// 									firstIndex = rowIndex;
									
// 								$('#stuList').datagrid('selectRow', rowIndex);
// 							}
// 						}
// 					});
					
// 					if(firstIndex == 0){
// 						$('#stuList').datagrid('selectRow', 0);
// 					}else{
// 						$('#stuList').datagrid('selectRow', 0);
// 						$('#stuList').datagrid('unselectRow', 0);
// 					}
// 				}
// 			},
// 			onClickRow:function(rowIndex,rowData){
				
// 			}
			onSelect:function(rowIndex,rowData){
				if(stucodearray.length > 0 ){
					var b = true;
					for(var i=0; i<stucodearray.length; i++){
						if(stucodearray[i] == rowData.XH){
							b = false;
						}
					}
					if(b){
						if(isLoad == false){
							stucodearray.push(rowData.XH);
							stunamearray.push(rowData.XM);
						}
					}
				}else{
					if(isLoad == false){
						stucodearray.push(rowData.XH);
						stunamearray.push(rowData.XM);
					}
				}
				
			},
			onUnselect:function(rowIndex,rowData){
				$.each(stuinfocodearray, function(key,value){
					if(value == rowData.XH){
						stucodearray.splice(key, 1);
						stunamearray.splice(key, 1);
					}
				});
				$.each(stucodearray, function(key,value){
					if(value == rowData.XH){
						stucodearray.splice(key, 1);
						stunamearray.splice(key, 1);
					}
				});
			},
			onLoadSuccess: function(data){
				$(".datagrid-header-check").html('&nbsp;');//隐藏全选
				//勾选已选授课教师
				if(data){
					if(stuCode!=undefined){
						var stuCode_1=stuCode.split(",");
						$.each(data.rows, function(rowIndex, rowData){
							for(var i=0; i<stuCode_1.length; i++){
								if(stuCode_1[i] == rowData.XH){
									$('#stuList').datagrid('selectRow', rowIndex);
								}
							}
						});
					}
				}
	 			isLoad = false;
			},
			onLoadError:function(none){
				
			}
		});
	}
	
	function loadTeaList(){
		isLoad = true;
		$('#teaList').datagrid({
			url :'<%=request.getContextPath()%>/Svl_xfsz',
			queryParams: {'active':'queTeaList','XX_SKJSGH':$('#ic_gh_3').val(),'XX_SKJS':$('#ic_xm_3').val(),'XZSKJSGH':teaCode},
			title:'教师信息列表',
			width:'100%',
// 			idField:'GH',
			nowrap: false,
			fit:true, //自适应父节点宽度和高度
			showFooter:true,
			striped:true,
			singleSelect:false,
			rownumbers:true,
			fitColumns: true,
			pagination:true,
			pageSize:pageSize,
			pageNumber:pageNum,
			columns:[[
				//field为读取数据的数据名，title为显示的数据名，width宽度设置，align数字在表格中显示的位置
				{field:'ck',checkbox:true},
				{field:'GH',title:'工号',width:fillsize(0.5),align:'center'},
				{field:'XM',title:'姓名',width:fillsize(0.5),align:'center'}
			]],
			//加载成功后触发
// 			onLoadSuccess: function(data){
				//勾选已选教师
// 				if(data && data.rows.length>0){
// 					var firstIndex = -1;
// 					var teaCode_1 = teaCode.split(',');
					
// 					$.each(data.rows, function(rowIndex, rowData){
// 						for(var i=0; i<teaCode_1.length; i++){
// 							if(teaCode_1[i] == rowData.GH){
// 								if(firstIndex == -1)
// 									firstIndex = rowIndex;
									
// 								$('#teaList').datagrid('selectRow', rowIndex);
// 							}
// 						}
// 					});
					
// 					if(firstIndex == 0){
// 						$('#teaList').datagrid('selectRow', 0);
// 					}else{
// 						$('#teaList').datagrid('selectRow', 0);
// 						$('#teaList').datagrid('unselectRow', 0);
// 					}
// 				}
// 			}
			onSelect:function(rowIndex,rowData){
				if(teacodearray.length > 0 ){
					var b = true;
					for(var i=0; i<teacodearray.length; i++){
						if(teacodearray[i] == rowData.GH){
							b = false;
						}
					}
					if(b){
						if(isLoad == false){
							teacodearray.push(rowData.GH);
							teanamearray.push(rowData.XM);
						}
					}
				}else{
					if(isLoad == false){
						teacodearray.push(rowData.GH);
						teanamearray.push(rowData.XM);
					}
				}
				
			},
			onUnselect:function(rowIndex,rowData){
				$.each(teainfocodearray, function(key,value){
					if(value == rowData.GH){
						teacodearray.splice(key, 1);
						teanamearray.splice(key, 1);
					}
				});
				$.each(teacodearray, function(key,value){
					if(value == rowData.GH){
						teacodearray.splice(key, 1);
						teanamearray.splice(key, 1);
					}
				});
				
			},
			onLoadSuccess: function(data){
				$(".datagrid-header-check").html('&nbsp;');//隐藏全选
				//勾选已选授课教师
				if(data){
					if(teaCode!=undefined){
						var teaCode_1=teaCode.split(",");
						$.each(data.rows, function(rowIndex, rowData){
							for(var i=0; i<teaCode_1.length; i++){
								if(teaCode_1[i] == rowData.GH){
									$('#teaList').datagrid('selectRow', rowIndex);
								}
							}
						});
					}
				}
	 			isLoad = false;
			},
			onLoadError:function(none){
				
			}
		});
	}
	</script>
</html>
