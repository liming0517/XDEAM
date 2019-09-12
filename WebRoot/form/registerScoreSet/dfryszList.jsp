<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="java.util.Vector"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.src.develop.store.user.*"%>
<%
	/**
		创建人：yeq
		Create date: 2016.01.29
		功能说明：用于设置登分教师学生信息
		页面类型:列表及模块入口
		修订信息(有多次时,可有多个)
		修订日期:
		原因:
		修订人:
		修订时间:
	**/
 %>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
  <head>
	<title>课程登分信息</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/icon.css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/publicScript.js"></script>
	<style>
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
			 margin-left:-50px;
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
	if(v!=null && v.size()>0){
		for(int i=0; i<v.size(); i++){
			sAuth += "@"+MyTools.StrFiltr(v.get(i))+"@O";
		}
		sAuth = sAuth.substring(0, sAuth.length()-1);
	}
	// 如果无法获取人员信息，则自动跳转到登陆界面
%>
<body class="easyui-layout">
	<div id="north" region="north" title="课程登分信息" style="height:84px;">
		<table id="ee" singleselect="true" width="100%" class="tablestyle">
			<tr>
				<td style="width:150px;" class="titlestyle">学年学期名称</td>
				<td>
					<select name="XNXQMC_CX" id="XNXQMC_CX" class="easyui-combobox" style="width:150px;">
					</select>
				</td>
				<td style="width:150px;" class="titlestyle">教学性质</td><!-- 根据当前登录人权限 -->
				<td>
					<select name="JXXZ_CX" id="JXXZ_CX" class="easyui-combobox" panelHeight="auto" style="width:150px;">
					</select>
				</td>
				<td style="width:150px;" class="titlestyle zycx">专业名称</td>
				<td>
					<input name="ZYMC_CX" id="ZYMC_CX" class="easyui-textbox" style="width:150px;"/>
				</td>
				<td style="width:150px;" align="center">
					<a href="#" onclick="doToolbar(this.id)" id="queList" class="easyui-linkbutton" plain="true" iconcls="icon-search">查询</a>
				</td>
			</tr>
			<tr>
				<td style="width:150px;" class="titlestyle zycx">班级名称</td>
				<td>
					<input name="XZBMC_CX" id="XZBMC_CX" class="easyui-textbox" style="width:150px;"/>
				</td>
				<td style="width:150px;" class="titlestyle zycx">课程名称</td>
				<td>
					<input name="KCMC_CX" id="KCMC_CX" class="easyui-textbox" style="width:150px;"/>
				</td>
				<td style="width:150px;" class="titlestyle zycx">课程类型</td>
				<td>
					<select name="KCLX_CX" id="KCLX_CX" class="easyui-combobox" panelHeight="auto" style="width:150px;" editable="false">
						<option value="">请选择</option>
						<option value="1">普通课程</option>
						<option value="2">添加课程</option>
						<option value="3">选修课程</option>
						<option value="4">分层课程</option>
					</select>
				</td>
				<td></td>
			</tr>
	    </table>
	</div>
	<div region="center">
		<table id="courseList" style="width:100%;"></table>
		<form id="form1" method="post">
			<input type="hidden" id="active" name="active"/>
			<input type="hidden" id="CD_XGBH" name="CD_XGBH"/>
			<input type="hidden" id="CD_DFJSBH" name="CD_DFJSBH"/>
			<input type="hidden" id="CD_DFJSXM" name="CD_DFJSXM"/>
			<input type="hidden" id="CX_XH" name="CX_XH"/>
			<input type="hidden" id="addStuInfo" name="addStuInfo"/>
			<input type="hidden" id="CD_KMBH" name="CD_KMBH"/>
			<!-- 2017/12/7翟旭超加 -->
			<input type="hidden" id="addName" name="addName"/>
		</form>
	</div>
	
	<!-- 登分教师列表 -->
	<div id="teaListDialog">
		<div class="easyui-layout" style="width:100%; height:100%;">
			<div region="north" style="height:64px;">
				<table>
					<tr>
						<td><a href="#" id="saveTea" class="easyui-linkbutton" plain="true" iconcls="icon-save" onClick="doToolbar(this.id);">保存</a></td>
					</tr>
				</table>
				<table id="ee" width="100%" class="tablestyle">
					<tr>
						<td width="80px" class="titlestyle">教师工号</td>
						<td width="135px">
							<input style='width:100%;' class="easyui-textbox" id='ic_teaCode' name='ic_teaCode'/>
						</td>
						<td width="80px" class="titlestyle">教师姓名</td>
						<td  width="135px">
							<input style='width:100%;' class="easyui-textbox" id='ic_teaName' name='ic_teaName'/>
						</td>	
						<td style="width:80px;" align="center">
							<a href="#" onclick="doToolbar(this.id)" id="queTeaList" class="easyui-linkbutton" plain="true" iconcls="icon-search">查询</a>
						</td>				
					</tr>
			    </table>
			</div>
			<div region="center">
				<table id="teaList" style="width:100%;"></table>
			</div>
		</div>
	</div>
	
	<!-- 学生列表 -->
	<div id="stuListDialog">
		<div class="easyui-layout" style="width:100%; height:100%;">
			<div region="north" style="height:34px;">
				<table>
					<tr>
						<td><a href="#" id="saveStu" class="easyui-linkbutton" plain="true" iconcls="icon-save" onClick="doToolbar(this.id);">保存</a></td>
						<td><a href="#" id="addStu" class="easyui-linkbutton" plain="true" iconcls="icon-add" onClick="doToolbar(this.id);">新增</a></td>
						<td><a href="#" id="openDelStu" class="easyui-linkbutton" plain="true" iconcls="icon-cancel" onClick="doToolbar(this.id);">删除</a></td>
						<td><a href="#" id="syncStuList" class="easyui-linkbutton" plain="true" iconcls="icon-synchronization" onClick="doToolbar(this.id);">同步名单</a></td>
					</tr>
				</table>
			</div>
			<div region="center">
				<table id="stuList" style="width:100%;"></table>
			</div>
		</div>
	</div>
	
	<!-- 新增学生 -->
	<div id="addStuDialog">
		<div class="easyui-layout" style="width:100%; height:100%;">
			<div region="north" style="height:34px;">
				<table>
					<tr>
						<td><a href="#" id="closeAddStu" class="easyui-linkbutton" plain="true" iconcls="icon-back" onClick="doToolbar(this.id);">返回</a></td>
						<td><a href="#" id="saveAddStu" class="easyui-linkbutton" plain="true" iconcls="icon-save" onClick="doToolbar(this.id);">保存</a></td>
					</tr>
				</table>
			</div>
			<div id="center" region="center">
				<ul id="classStuTree" class="easyui-tree">
				</ul>
			</div>
		</div>
	</div>
	
	<!-- 删除学生 -->
	<div id="delStuDialog">
		<div class="easyui-layout" style="width:100%; height:100%;">
			<div region="north" style="height:34px;">
				<table>
					<tr>
						<td><a href="#" id="closeDelStu" class="easyui-linkbutton" plain="true" iconcls="icon-back" onClick="doToolbar(this.id);">返回</a></td>
						<td><a href="#" id="delStu" class="easyui-linkbutton" plain="true" iconcls="icon-cancel" onClick="doToolbar(this.id);">删除</a></td>
					</tr>
				</table>
			</div>
			<div region="center">
				<table id="delStuList" style="width:100%;"></table>
			</div>
		</div>
	</div>
	
	<!-- 同步学生名单 -->
	<div id="syncStuDialog">
		<%-- 遮罩层 --%>
    	<div id="divPageMask" class="maskStyle">
    		<div id="maskFont">学生名单同步中...</div>
    	</div>
	
		<div class="easyui-layout" style="width:100%; height:100%;">
			<div region="north" style="height:34px;">
				<table>
					<tr>
						<td><a href="#" id="confirmSync" class="easyui-linkbutton" plain="true" iconcls="icon-ok" onClick="doToolbar(this.id);">确认同步</a></td>
					</tr>
				</table>
			</div>
			<div region="center">
				<table id="classCourseList" style="width:100%;"></table>
			</div>
		</div>
	</div>
	
	
	
	<!-- 2017/12/7翟旭超加 -->
	<div id="test"  class="easyui-window" style="width:695px;height:350px;">
		<iframe id="xiafaxiada" name="xiafaxiada" src='http://180.166.123.122:8087/HRM/form/param/TeaTree1.jsp?u=1' style="width:680px;height:310px;"  frameborder="0"></iframe>
	</div>
	
</body>
<script type="text/javascript">
	var sAuth = '<%=sAuth%>';
	var admin = '<%=MyTools.getProp(request, "Base.admin")%>';
	var jxzgxz = '<%=MyTools.getProp(request, "Base.jxzgxz")%>';//教学主管校长
	var qxjdzr = '<%=MyTools.getProp(request, "Base.qxjdzr")%>';//全校教导主任
	var qxjwgl = '<%=MyTools.getProp(request, "Base.qxjwgl")%>';//全校教务管理
	var xbjdzr = '<%=MyTools.getProp(request, "Base.xbjdzr")%>';//系部教导主任
	var xbjwgl = '<%=MyTools.getProp(request, "Base.xbjwgl")%>';//系部教务管理
	var tempIndex = '';//行号
	var curSelCode = new Array();
	var curSelName = new Array();
	var isLoad = true;//判断datagrid是否处于加载状态
	var curClassCode = '';
	var loadStuListFlag = false;
	
	$(document).ready(function(){
		if(sAuth.indexOf(admin)<0 && sAuth.indexOf(jxzgxz)<0 && sAuth.indexOf(qxjdzr)<0 && sAuth.indexOf(qxjwgl)<0 && sAuth.indexOf(xbjdzr)<0 && sAuth.indexOf(xbjwgl)<0)
			$('#syncStuList').hide();
	
		loadCourseGrid();
		initDialog();//初始化对话框
		initComboData();//页面初始化加载数据
		
		//2017/12/7翟旭超加
		$('#test').window('close');//初始化关闭人员窗口

	});
	
	/**页面初始化加载数据**/
	function initComboData(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Dfrysz',
			data : 'active=initComboData&sAuth=' + sAuth,
			dataType:"json",
			success : function(data) {
				initCombobox(data[0].xnxqData, data[0].jxxzData);
			}
		});
	}
	
	/**加载 dialog控件**/
	function initDialog(){
		$('#teaListDialog').dialog({   
			title: '登分教师列表',   
			width: 700,//宽度设置   
			height: 400,//高度设置 
			modal:true,
			closed: true,   
			cache: false, 
			draggable:true,//是否可移动dialog框设置
			//读取事件
			onLoad:function(data){
			},
			//关闭事件
			onClose:function(data){
				$('#ic_teaCode').textbox('setValue', '');
				$('#ic_teaName').textbox('setValue', '');
				$('#teaList').datagrid('loadData',{total:0,rows:[]});
			}
		});
		
		$('#stuListDialog').dialog({   
			title: '学生列表',   
			width: 600,//宽度设置   
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
				//$('#ic_stuCode').textbox('setValue', '');
				//$('#ic_stuName').textbox('setValue', '');
				$('#stuList').datagrid('loadData',{total:0,rows:[]});
			}
		});
		
		$('#addStuDialog').dialog({   
			title: '班级学生列表',   
			width: 600,//宽度设置   
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
				$('#center').html('<ul id="classStuTree" class="easyui-tree"></ul>');
			}
		});
		
		$('#delStuDialog').dialog({   
			title: '学生列表',   
			width: 600,//宽度设置   
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
		
		$('#syncStuDialog').dialog({   
			title: '同步课程列表',   
			width: 500,//宽度设置   
			height: 350,//高度设置 
			modal:true,
			closed: true,   
			cache: false, 
			draggable:true,//是否可移动dialog框设置
			//读取事件
			onLoad:function(data){
			},
			//关闭事件
			onClose:function(data){
				$('#classCourseList').datagrid('loadData',{total:0,rows:[]});
			}
		});
	}
	
	/**加载combobox控件
		@xnxqData 学年学期下拉框数据
		@jxxzData 教学性质下拉框数据
	**/
	function initCombobox(xnxqData, jxxzData){
		$('#XNXQMC_CX').combobox({
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
			onChange:function(data){}
		});
		
		$('#JXXZ_CX').combobox({
			data:jxxzData,
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
	}
	
	/**加载课程列表datagrid控件，读取页面信息**/
	function loadCourseGrid(){
		$('#courseList').datagrid({
			url: '<%=request.getContextPath()%>/Svl_Dfrysz',
			queryParams:{'active':'queCourseList','sAuth':sAuth,'XNXQMC_CX':$('#XNXQMC_CX').combobox('getValue'),
				'JXXZ_CX':$('#JXXZ_CX').combobox('getValue'),'ZYMC_CX':encodeURI($('#ZYMC_CX').textbox('getValue')),
				'XZBMC_CX':encodeURI($('#XZBMC_CX').textbox('getValue')),'KCMC_CX':encodeURI($('#KCMC_CX').textbox('getValue')),
				'KCLX_CX':$('#KCLX_CX').combobox('getValue')},
			title:'课程登分信息列表',
			width:'100%',
			nowrap: false,
			fit:true, //自适应父节点宽度和高度
			showFooter:true,
			striped:true,
			pagination:true,
			pageSize:20,
			pageNumber:1,
			singleSelect:true,
			rownumbers:true,
			fitColumns: true,
			columns:[[
				//field为读取数据的数据名，title为显示的数据名，width宽度设置，align数字在表格中显示的位置
				{field:'科目编号',hidden:true},
				{field:'相关编号',hidden:true},
				{field:'学年学期名称',title:'学年学期名称',width:fillsize(0.1),align:'center'},
				{field:'教学性质',title:'教学性质',width:fillsize(0.06),align:'center'},
				{field:'专业名称',title:'专业名称',width:fillsize(0.15),align:'center'},
				{field:'行政班代码',hidden:true},
				{field:'行政班名称',title:'班级名称',width:fillsize(0.15),align:'center'},
				{field:'课程名称',title:'课程名称',width:fillsize(0.25),align:'center'},
				{field:'课程类型',title:'课程类型',width:fillsize(0.06),align:'center'},
				/**{field:'打印锁定',title:'打印锁定',width:fillsize(0.06),align:'center'},*/
				{field:'登分教师编号',hidden:true},
				//2017/12/11添加列翟旭超注释
				/* {field:'登分教师姓名',hidden:true}, */
				//2017/12/11添加列翟旭超加
				{field:'登分教师姓名',title:'登分教师姓名',width:fillsize(0.1),align:'center'},
				{field:'col4',title:'操作',width:fillsize(0.16),align:'center',
						formatter:function(value,rec){
							var result = '';
							
							if(sAuth.indexOf(admin)>-1 || sAuth.indexOf(jxzgxz)>-1 || sAuth.indexOf(qxjdzr)>-1 || sAuth.indexOf(qxjwgl)>-1 || sAuth.indexOf(xbjdzr)>-1 || sAuth.indexOf(xbjwgl)>-1){
								result += "<input type='button' value='[登分教师]' onclick='openDfjsList(\"" + rec.相关编号 + "\",\"" + rec.登分教师编号 + "\",\"" + rec.登分教师姓名 + "\");' style=\"cursor:pointer;\">&nbsp;";
							}
							result += "<input type='button' value='[学生名单]' onclick='openStuList(\"" + rec.相关编号 + "\");' style=\"cursor:pointer;\">";
							
							return result;
				}}
			]],
			//双击某行时触发
			onDblClickRow:function(rowIndex,rowData){},
			//读取datagrid之前加载
			onBeforeLoad:function(){},
			//单击某行时触发
			onClickRow:function(rowIndex,rowData){
				tempIndex = rowIndex;
				curClassCode = rowData.行政班代码;
				$('#CD_KMBH').val(rowData.科目编号);
			},
			//加载成功后触发
			onLoadSuccess: function(data){
				tempIndex = '';
			}
		});
	};
	
	/**打开登分教师列表**/
	function openDfjsList(id, teaCode, teaName){
		if(teaCode != ''){
			curSelCode = teaCode.split(',');
			curSelName = teaName.split(',');
		}else{
			curSelCode.length = 0;
			curSelName.length = 0;
		}
		$('#saveTea').linkbutton('disable');
		$('#queTeaList').linkbutton('disable');
		$('#CD_XGBH').val(id);
		
		loadTeaListData();
		//2017/12/7翟旭超注释
		//$('#teaListDialog').dialog('open');
		//2017/12/7翟旭超加
		showOpen(teaCode);
	}
	
	/**打开学生列表**/
	function openStuList(code){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Dfrysz',
			data : 'active=addStuScoreData&CD_XGBH=' + code,
			dataType:"json",
			success : function(data) {
				if(data[0].MSG == '保存成功'){
					if(data[0].stuCode != '')
						curSelCode = data[0].stuCode.split(',');
					else
						curSelCode.length = 0;
						
					$('#saveStu').linkbutton('disable');
					$('#addStu').linkbutton('disable');
					$('#openDelStu').linkbutton('disable');
					$('#syncStuList').linkbutton('disable');
					$('#CD_XGBH').val(code);
					
					loadStuListFlag = true;
					loadStuListData();
					$('#stuListDialog').dialog('open');
				}else{
					alertMsg(data[0].MSG);
				}
			}
		});
	}
	
	/**工具栏按钮调用方法，传入按钮的id
		@id 当前按钮点击事件
	**/
	function doToolbar(id){
		//查询课程列表
		if(id == 'queList'){
			loadCourseGrid();
		}
		
		//查询登分教师列表
		if(id == 'queTeaList'){
			$('#saveTea').linkbutton('disable');
			$('#' + id).linkbutton('disable');
			loadTeaListData();
		}
		
		//保存登分教师
		if(id == 'saveTea'){
			if(curSelCode.length == 0){
				alertMsg('请至少选择一名登分教师');
				return;
			}
			$('#active').val(id);
			$('#CD_DFJSBH').val(curSelCode.toString());
			$('#CD_DFJSXM').val(curSelName.toString());
			
			$('#form1').submit();
		}
		
		//保存学生
		if(id == 'saveStu'){
			if(curSelCode.length == 0){
				alertMsg('请至少选择一名学生');
				return;
			}
		
			$('#active').val(id);
			$('#CX_XH').val(curSelCode.toString());
			$('#form1').submit();
		}
		
		//新增学生
		if(id == 'addStu'){
			checkZyFlag = false;
			checkBjFlag = false;
			loadClassStuTree();
			$('#addStuDialog').dialog('open');
			document.getElementById('center').scrollTop = 0;
		}
		
		//保存新增学生
		if(id == 'saveAddStu'){
			var totalData = $('#classStuTree').tree('getChecked');
			var stuInfo = '';
			
			for(var i=0; i<totalData.length; i++){
				if(totalData[i].id.indexOf('-') < 0){
					stuInfo += totalData[i].id+','+totalData[i].text+',';
				}
			}
			if(stuInfo.length == 0){
				alertMsg('请至少选择一名学生');
				return;
			}
			stuInfo = stuInfo.substring(0, stuInfo.length-1);
			$('#active').val(id);
			$('#addStuInfo').val(stuInfo);
			$('#form1').submit();
		}
		
		//关闭新增学生对话框
		if(id == 'closeAddStu'){
			$('#addStuDialog').dialog('close');
		}
		
		//打开删除学生对话框
		if(id == 'openDelStu'){
			$('#delStuDialog').dialog('open');
		}
		
		//删除学生
		if(id == 'delStu'){
			var stuData = $('#delStuList').datagrid('getSelections');
			
			if(stuData.length == 0){
				alertMsg('请至少选择一名学生');
				return;
			}
			
			var stuCode = '';
			for(var i=0; i<stuData.length; i++){
				stuCode += stuData[i].学号+',';
			}
			stuCode = stuCode.substring(0, stuCode.length-1);
			
			ConfirmMsg('是否确定要删除选择的学生？', 'delStu("' + stuCode + '");', '');
		}
		
		//关闭删除学生对话框
		if(id == 'closeDelStu'){
			$('#delStuDialog').dialog('close');
		}
		
		//打开同步学生名单对话框
		if(id == 'syncStuList'){
			if($('#stuList').datagrid('getRows').length == 0){
				alertMsg('当前学生名单为空，无法同步！');
			}else{
				$('#syncStuDialog').dialog('open');
				loadClassCourseData();
			}
		}
		
		//确认同步
		if(id == 'confirmSync'){
			var courseData = $('#classCourseList').datagrid('getSelections');
			
			if(courseData.length == 0){
				alertMsg('请至少选择一门课程');
				return;
			}
			
			var courseCode = '';
			for(var i=0; i<courseData.length; i++){
				courseCode += courseData[i].相关编号+','+courseData[i].科目编号+',';
			}
			courseCode = courseCode.substring(0, courseCode.length-1);
			
			ConfirmMsg('是否确定要同步学生名单到选择的课程？', 'syncStuList("' + courseCode + '");', '');
		}
	}
	
	/**删除非本班学生成绩信息*/
	function delStu(stuCode){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Dfrysz',
			data : 'active=delStu&CD_XGBH=' + $('#CD_XGBH').val() + '&delStuCode=' + stuCode,
			dataType:"json",
			success : function(data) {
				if(data[0].MSG == '删除成功'){
					showMsg(data[0].MSG);
					
					var stuCode = data[0].stuCode;
					/*
					$('#courseList').datagrid('updateRow', {
						index:tempIndex,
						row: {
							学生名单:stuCode
						}
					});
					*/
					curSelCode = stuCode.split(',');
					loadDelStuListData();
					loadStuListData();
				}else{
					alertMsg(data[0].MSG);
				}
			}
		});
	}
	
	/**表单提交**/
	$('#form1').form({
		//定位到servlet位置的url
		url:'<%=request.getContextPath()%>/Svl_Dfrysz',
		//当点击事件后触发的事件
		onSubmit: function(data){}, 
		//当点击事件并成功提交后触发的事件
		success:function(data){
			var json  =  eval("("+data+")");
			if(json[0].MSG == '保存成功'){
				showMsg(json[0].MSG);
				
				if($('#active').val() == 'saveTea'){
					$('#courseList').datagrid('updateRow', {
						index:tempIndex,
						row: {
							登分教师编号:$('#CD_DFJSBH').val(),
							登分教师姓名:$('#CD_DFJSXM').val()
						}
					});
					$('#teaListDialog').dialog('close');
				}
				
				if($('#active').val() == 'saveStu'){
					/*
					$('#courseList').datagrid('updateRow', {
						index:tempIndex,
						row: {
							学生名单:$('#CX_XH').val()
						}
					});
					*/
					$('#stuListDialog').dialog('close');
				}
				
				if($('#active').val() == 'saveAddStu'){
					var stuCode = curSelCode.toString();
					var addStuInfo = $('#addStuInfo').val().split(',');
					
					for(var i=0; i<addStuInfo.length; i+=2){
						stuCode += ','+addStuInfo[i];
					}
					/*
					$('#courseList').datagrid('updateRow', {
						index:tempIndex,
						row: {
							学生名单:stuCode
						}
					});
					*/
					curSelCode = stuCode.split(',');
					$('#saveStu').linkbutton('disable');
					$('#addStu').linkbutton('disable');
					$('#openDelStu').linkbutton('disable');
					$('#syncStuList').linkbutton('disable');
					loadStuListFlag = true;
					loadStuListData();
					
					$('#addStuDialog').dialog('close');
				}
			}else{
				alertMsg(json[0].MSG);
			}
		}
	});
	
	/**读取教师datagrid数据**/
	function loadTeaListData(){
		isLoad = true;
		
		$('#teaList').datagrid({
			url :'<%=request.getContextPath()%>/Svl_Dfrysz',
			queryParams: {'active':'queTeaList','DFJSBH_CX':encodeURI($('#ic_teaCode').textbox('getValue')),
				'DFJSMC_CX':encodeURI($('#ic_teaName').textbox('getValue')),
				'CD_DFJSBH':curSelCode.toString()},
			title:'',
			width:'100%',
			nowrap: false,
			fit:true, //自适应父节点宽度和高度
			showFooter:true,
			striped:true,
			pagination:true,
			pageNumber:1,
			pageSize:20,
			singleSelect:false,
			rownumbers:true,
			fitColumns: true,
			columns:[[
				//field为读取数据的数据名，title为显示的数据名，width宽度设置，align数字在表格中显示的位置
				{field:'ck',checkbox:true},
				{field:'工号',title:'工号',width:fillsize(0.45),align:'center'},
				{field:'姓名',title:'姓名',width:fillsize(0.45),align:'center'}
			]],
			onSelect:function(rowIndex,rowData){
				if(isLoad == false){
					curSelCode.push(rowData.工号);
					curSelName.push(rowData.姓名);
				}
			},
			onUnselect:function(rowIndex,rowData){
				$.each(curSelCode, function(key,value){
					if(value == rowData.工号){
						curSelCode.splice(key, 1);
						curSelName.splice(key, 1);
					}
				});
			},
			//加载成功后触发
			onLoadSuccess: function(data){
				$(".datagrid-header-check").html('&nbsp;');//隐藏全选
				//勾选已选授课教师
				if(data){
					$.each(data.rows, function(rowIndex, rowData){
						for(var i=0; i<curSelCode.length; i++){
							if(curSelCode[i] == rowData.工号){
								$('#teaList').datagrid('selectRow', rowIndex);
								break;
							}
						}
					});
				}
				isLoad = false;
				$('#saveTea').linkbutton('enable');
				$('#queTeaList').linkbutton('enable');
			}
		});
	}
	
	/**读取学生datagrid数据**/
	function loadStuListData(){
		isLoad = true;
		
		$('#stuList').datagrid({
			url :'<%=request.getContextPath()%>/Svl_Dfrysz',
			queryParams: {'active':'queStuList','CD_XGBH':$('#CD_XGBH').val()},
			title:'',
			width:'100%',
			nowrap: false,
			fit:true, //自适应父节点宽度和高度
			showFooter:true,
			striped:true,
			singleSelect:false,
			rownumbers:true,
			fitColumns: true,
			columns:[[
				//field为读取数据的数据名，title为显示的数据名，width宽度设置，align数字在表格中显示的位置
				{field:'ck',checkbox:true},
				{field:'行政班名称',title:'行政班名称',width:fillsize(0.3),align:'center'},
				{field:'班内学号',title:'学号',width:fillsize(0.2),align:'center'},
				{field:'姓名',title:'姓名',width:fillsize(0.2),align:'center'},
				{field:'学籍号',title:'学籍号',width:fillsize(0.3),align:'center'}
			]],
			onSelect:function(rowIndex,rowData){
				if(isLoad == false){
					curSelCode.push(rowData.学号);
				}
			},
			onUnselect:function(rowIndex,rowData){
				$.each(curSelCode, function(key,value){
					if(value == rowData.学号){
						curSelCode.splice(key, 1);
					}
				});
			},
			onSelectAll:function(rows){
				curSelCode.length = 0;
				for(var i=0; i<rows.length; i++){
					curSelCode.push(rows[i].学号);
				}
			},
			onUnselectAll:function(rows){
				curSelCode.length = 0;
			},
			//加载成功后触发
			onLoadSuccess: function(data){
				//勾选已选学生
				if(data && data.rows.length>0){
					var firstIndex = -1;
					
					$.each(data.rows, function(rowIndex, rowData){
						for(var i=0; i<curSelCode.length; i++){
							if(curSelCode[i] == rowData.学号){
								if(firstIndex == -1)
									firstIndex = rowIndex;
									
								$('#stuList').datagrid('selectRow', rowIndex);
								break;
							}
						}
					});
					
					if(firstIndex == 0){
						$('#stuList').datagrid('selectRow', 0);
					}else{
						$('#stuList').datagrid('selectRow', 0);
						$('#stuList').datagrid('unselectRow', 0);
					}
				}
				
				isLoad = false;
				$('#saveStu').linkbutton('enable');
				$('#addStu').linkbutton('enable');
				$('#openDelStu').linkbutton('enable');
				$('#syncStuList').linkbutton('enable');
				
				if(loadStuListFlag == true){
					loadDelStuListData();
					loadStuListFlag = false;
				}
			}
		});
	}
	
	/**读取删除学生datagrid数据**/
	function loadDelStuListData(){
		$('#delStuList').datagrid({
			url :'<%=request.getContextPath()%>/Svl_Dfrysz',
			queryParams: {'active':'queDelStuList','CD_XGBH':$('#CD_XGBH').val(),'CD_XZBDM':curClassCode},
			title:'可删除学生列表',
			width:'100%',
			nowrap: false,
			fit:true, //自适应父节点宽度和高度
			showFooter:true,
			striped:true,
			singleSelect:false,
			rownumbers:true,
			fitColumns: true,
			columns:[[
				//field为读取数据的数据名，title为显示的数据名，width宽度设置，align数字在表格中显示的位置
				{field:'ck',checkbox:true},
				{field:'行政班名称',title:'行政班名称',width:fillsize(0.4),align:'center'},
				{field:'学籍号',title:'学籍号',width:fillsize(0.3),align:'center'},
				{field:'姓名',title:'姓名',width:fillsize(0.3),align:'center'}
			]],
			//加载成功后触发
			onLoadSuccess: function(data){
			}
		});
	}
	
	/**读取可同步课程datagrid数据**/
	function loadClassCourseData(){
		$('#classCourseList').datagrid({
			url :'<%=request.getContextPath()%>/Svl_Dfrysz',
			queryParams: {'active':'queClassCourseList','CD_XGBH':$('#CD_XGBH').val(),'CD_XZBDM':curClassCode},
			title:'课程列表',
			width:'100%',
			nowrap: false,
			fit:true, //自适应父节点宽度和高度
			showFooter:true,
			striped:true,
			singleSelect:false,
			rownumbers:true,
			fitColumns: true,
			columns:[[
				//field为读取数据的数据名，title为显示的数据名，width宽度设置，align数字在表格中显示的位置
				{field:'ck',checkbox:true},
				{field:'科目编号',hidden:true},
				{field:'相关编号',hidden:true},
				{field:'课程名称',title:'课程名称',width:fillsize(0.8),align:'center'},
				{field:'课程类型',title:'课程类型',width:fillsize(0.2),align:'center'}
			]],
			//加载成功后触发
			onLoadSuccess: function(data){
			}
		});
	}
	
	/**加载班级学生信息TREE**/
	var checkZyFlag = false;//用于判断是否勾选专业
	var childNum = 0;
	var loadNum = 0;
	var checkBjFlag = false;//用于判断是否勾选班级
	var checkObj = '';
	function loadClassStuTree(){
		$('#classStuTree').tree({
			checkbox: true,
			url:'<%=request.getContextPath()%>/Svl_Dfrysz?active=queClassStuTree&sAuth=' + sAuth,
			onCheck:function(node){
				var checkType = '';
				loadNum = 0;
				childNum = 0;
				
				if(node.id.indexOf('zy') > -1)
					checkType = 'zy';
				else if(node.id.indexOf('bj') > -1)
					checkType = 'bj';
				else 
					checkType = 'xs';
					
				if(node.checked==false && checkType=='zy'){
					checkZyFlag = true;
					checkObj = node.target;
					$(this).tree('expand', checkObj);
					
					var childrenObj = $('#classStuTree').tree('getChildren', node.target);
					if(childrenObj.length > 0){
						loadNum++;
						childNum = 1;
						
						for(var i=0; i<childrenObj.length; i++){
				    		if(childrenObj[i].id.substring(0, 2)=='bj' && childrenObj[i].state=='closed'){
								$(this).tree('expand', childrenObj[i].target);
								childNum++;
							}
		    			}
		    			//childNum = childrenObj.length+1;
					}
				}
				if(node.checked==false && checkType=='bj'){
					if(node.state == 'closed'){
						checkBjFlag = true;
						checkObj = node.target;
						$(this).tree('expand', checkObj);
					}
				}
			},
		    onBeforeExpand:function(node){
		    	$('#classStuTree').tree('options').url="<%=request.getContextPath()%>/Svl_Dfrysz?active=queClassStuTree&classCode=" + curClassCode + "&parentCode=" + node.id + "&CD_XGBH=" + $('#CD_XGBH').val();
			},
			onLoadSuccess:function(node, data){
				if(checkZyFlag == true){
					loadNum++;
					
					if(loadNum == 1){
						childNum = 1;
						var childrenObj = $('#classStuTree').tree('getChildren', node.target);
						for(var i=0; i<childrenObj.length; i++){
							if(childrenObj[i].id.substring(0, 2)=='bj' && childrenObj[i].state=='closed'){
								$(this).tree('expand', childrenObj[i].target);
								childNum++;
							}
		    			}
		    			//childNum = childrenObj.length+1;
					}
					
					if(loadNum == childNum){
						$(this).tree('check', checkObj);
						checkObj = '';
						checkZyFlag = false;
					}
				}
				
				if(checkBjFlag == true){
					$(this).tree('check', checkObj);
					checkObj = '';
					checkBjFlag = false;
				}
			}
		});
	};
	
	/**同步学生名单*/
	function syncStuList(courseCode){
		$('#divPageMask').show();
		
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Dfrysz',
			data : 'active=syncStuList&CD_XGBH=' + $('#CD_XGBH').val() + '&courseCode=' + courseCode + '&CX_XH=' + curSelCode.toString(),
			dataType:"json",
			success : function(data) {
				if(data[0].MSG == '同步成功'){
					showMsg(data[0].MSG);
				}else{
					alertMsg(data[0].MSG);
				}
			$('#divPageMask').hide();
			}
		});
	}
	
	//2017/12/7翟旭超加  begin
	//打开一个人员选择窗口
		function showOpen(teaCode){
			$('#test').window('open');//打开一个window
			openWindow(teaCode);
		}
		// 设置窗口属性
		function openWindow(teaCode){
			$('#test').window({
					title: '人员列表',
					modal: true,
					shadow: false,
					closed: false,
					maximizable:false,
					minimizable:false,
					onOpen:function(){
						//$("#xiafaxiada").attr("src",'http://180.166.123.122:8087/HRM/form/param/TeaTree1.jsp?u='+encodeURIComponent('http://180.166.123.122:8087/SCOA1//getUsers.jsp')+'&s='+teaCode);
						$("#xiafaxiada").attr("src",'http://180.166.123.122:8087/HRM/form/param/TeaTree1.jsp?u='+encodeURIComponent('http://192.168.111.26:8080/XDEAM/form/registerScoreSet//getUsers.jsp')+'&s='+teaCode);
					}
			});
		}
		//关闭人员选择窗口
		function closeWin(){
			$('#test').window('close');
		}
		
		function getUsers(data){
			var text = decodeURI(data).split(',');
			var ids = [];
			var names = [];
			for(var i=0;i<text.length;i++){
				ids.push(text[i].split(':')[0]);
				names.push(text[i].split(':')[1]);
			}
			$('#addName').val(ids.join(','));
			$('#ic_target').val(names.join(','));
			
			if(ids == ""){
				alertMsg('请至少选择一名登分教师');
				return;
			}
			closeWin();
			AddTea(ids,names);
		}
	 	
	 	
	 	//用来做删除问卷提交
  		function AddTea(ids,names){
  			$.ajax({
  				type:"post",
  				url:'<%=request.getContextPath()%>/Svl_Dfrysz', 
  				data:"active=saveTea&CD_XGBH="+$('#CD_XGBH').val()+"&CD_DFJSBH="+ids+"&CD_DFJSXM="+names,   //设置模式
  				dataType:'json',
  				success:function(datas){
  					var data=datas[0];
  						showMsg(data.MSG);
  						loadCourseGrid();
  				},
  				error:function(){
  				
  				}
  			});
  		}
  	//2017/12/7翟旭超加  end
</script>
</html>