<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="java.util.Vector"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.src.develop.store.user.*"%>
<%
	/**
		创建人：yeq
		Create date: 2016.03.23
		功能说明：用于设置学生
		页面类型:列表及模块入口
		修订信息(有多次时,可有多个)
		修订日期:2017.03.24
		原因:修改批量设置(根据成绩类型查询并修改)
		修订人:yangda
		修订时间:
	**/
 %>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
  <head>
	<title>缓考设置</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/icon.css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/publicScript.js"></script>
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
	<div id="north" region="north" title="班级信息" style="height:116px;">
		<table>
			<tr>
				<td><a href="#" id="hksz" class="easyui-linkbutton" plain="true" iconcls="icon-edit" onClick="doToolbar(this.id);">缓考设置</a></td>
				<td><a href="#" id="plsz" class="easyui-linkbutton" plain="true" iconcls="icon-edit" onClick="doToolbar(this.id);">批量设置</a></td>
			</tr>
		</table>
		<table id="ee" singleselect="true" width="100%" class="tablestyle">
			<tr>
				<td style="width:150px;" class="titlestyle zycx">班级名称</td>
				<td>
					<input name="XZBMC_CX" id="XZBMC_CX" class="easyui-textbox" style="width:150px;"/>
				</td>
				<td style="width:150px;" class="titlestyle zycx">专业名称</td>
				<td>
					<input name="ZYMC_CX" id="ZYMC_CX" class="easyui-textbox" style="width:150px;"/>
				</td>
				<td style="width:150px;" class="titlestyle zycx">学年学期名称</td>
				<td>
					<input name="XNXQMC_CX" id="XNXQMC_CX" class="easyui-textbox" style="width:150px;"/>
				</td>
				<td style="width:150px;" align="center">
					<a href="#" onclick="doToolbar(this.id)" id="queList" class="easyui-linkbutton" plain="true" iconcls="icon-search">查询</a>
				</td>
			</tr>
			<tr>
				<td style="width:150px;" class="titlestyle zycx">班级类型</td>
				<td>
					<select name="BJLX_CX" id="BJLX_CX" class="easyui-combobox" style="width:150px;" editable="false" panelHeight="auto">
						<option value="">请选择</option>
						<option value="xzb">行政班</option>
						<option value="jxb">教学班</option>
						<option value="xxb">选修班</option>
						<option value="fcb">分层班</option>
					</select>
				</td>
				<td style="width:150px;" class="titlestyle zycx">学号</td>
				<td>
					<input name="XH_CX" id="XH_CX" style="width:150px; color:#A0A0A0;" value="请输入学生完整学号"/>
				</td>
				<td style="width:150px;" class="titlestyle zycx">姓名</td>
				<td>
					<input name="XM_CX" id="XM_CX" style="width:150px; color:#A0A0A0;" value="请输入学生完整姓名"/>
				</td>
				<td>&nbsp;</td>
			</tr>
	    </table>
	</div>
	<div region="center">
		<table id="classList" style="width:100%;"></table>
	</div>
	
	<!-- 缓考设置窗口 -->
	<div id="hkszDialog" style="overflow:hidden;">
		<form id="form1" method="post">
			<!-- 
			<table>
				<tr>
					<td colspan="2" style="text-align:center;">
						<div id="saveHkStu" style="float:left; width:65px; height:25px; background:url(0); border:1px solid #FFFFFF; cursor:pointer;" 
							onmouseover="$(this).css('border', '1px solid #95B8E7');$(this).css('background-color', '#E6EFFF');" 
							onmouseout="$(this).css('border', '1px solid #FFFFFF');$(this).css('background-color', '#FFFFFF');" 
							onclick="doToolbar(this.id);">
							<img style=" position:relative; top:3px;" src="<-%=request.getContextPath()%>/css/themes/icons/filesave.png"/>&nbsp;
							<span style="font-size:12; position:relative; bottom:1px;">保存</span></div>
					</td>			
				</tr>
			</table>
			 -->
			<table id="ee" singleselect="true" width="100%" class="tablestyle">
				<tr>
					<td class="titlestyle" style="width:100px;">班级名称</td>
					<td class="titlestyle" id="xzbmc"></td>
				</tr>
				<tr>
					<td class="titlestyle">学年学期</td>
					<td>
						<select name="ic_xnxq" id="ic_xnxq" class="easyui-combobox" style="width:270px;">
						</select>
					</td>
				</tr>
				<tr>
					<td class="titlestyle">课程名称</td>
					<td>
						<select name="ic_kcmc" id="ic_kcmc" class="easyui-combobox" style="width:270px;" disabled="disabled">
							<option value="">请选择学年学期</option>
						</select>
					</td>
				</tr>
				<tr>
					<td class="titlestyle">成绩类型</td>
					<td>
						<select name="ic_cjlx" id="ic_cjlx" class="easyui-combobox" style="width:270px;">
							<option value="">请选择</option>
							<!-- 
							<option value="ps">平时</option>
							<option value="qz">期中</option>
							<option value="sx">实训</option>
							<option value="qm">期末</option>
							--> 
						</select>
					</td>
				</tr>
				<tr>
					<td class="titlestyle">学生名单</td>
					<td>
						<input id="ic_xsmd" name="ic_xsmd" value="请先选择课程" style="width:100%; cursor:pointer;" onclick="openStuList(id);" disabled="disabled"/>
					</td>
				</tr>
		    </table>
			<input type="hidden" id="active" name="active"/>
			<input type="hidden" id="CX_XGBH" name="CX_XGBH"/>
			<input type="hidden" id="CX_XNXQ" name="CX_XNXQ"/>
			<input type="hidden" id="CX_XH" name="CX_XH"/>
			<input type="hidden" id="CX_XM" name="CX_XM"/>
			<input type="hidden" id="plcjlx" name="plcjlx"/> <!-- 批量成绩类型的隐藏域 -->
	    </form>
	</div>
	
	<!-- 登分学生列表 -->
	<div id="stuListDialog">
		<div class="easyui-layout" style="width:100%; height:100%;">
			<div region="north" style="height:34px;">
				<table>
					<tr>
						<td><a href="#" id="saveHkStu" class="easyui-linkbutton" plain="true" iconcls="icon-save" onClick="doToolbar(this.id);">保存</a></td>
					</tr>
				</table>
			</div>
			<div region="center">
				<table id="stuList" style="width:100%;"></table>
			</div>
		</div>
	</div>
	
	<!-- 批量设置对话框 -->
	<div id="plszDialog">
		<div class="easyui-layout" style="width:100%; height:100%;">
			<div region="north" style="height:32px; overflow:hidden;">
				<table id="ee" singleselect="true" width="100%" class="tablestyle">
					<tr>
						<td style="width:150px;" class="titlestyle zycx">学年学期</td>
						<td>
							<select name="pl_xnxq" id="pl_xnxq" class="easyui-combobox" style="width:150px;">
							</select>
						</td>
						<td style="width:150px;" class="titlestyle zycx">学号</td>
						<td>
							<input name="pl_xh" id="pl_xh" style="width:150px; color:#A0A0A0;" value="请输入学生完整学号"/>
						</td>
						<td style="width:150px;" class="titlestyle zycx">姓名</td>
						<td>
							<input name="pl_xm" id="pl_xm" style="width:150px; color:#A0A0A0;" value="请输入学生完整姓名"/>
						</td>
						<td style="width:150px;" align="center">
							<a href="#" onclick="doToolbar(this.id)" id="queStuCourseList" class="easyui-linkbutton" plain="true" iconcls="icon-search">查询</a>
						</td>
					</tr>
				</table>
			</div>
			<div region="center" title="学生课程信息">
				<table id="ee" singleselect="true" width="100%" class="tablestyle">
					<tr>
						<td style="width:100px;" class="titlestyle zycx">成绩类型</td>
						<td>
							<select name="ic_plcjlx" id="ic_plcjlx" class="easyui-combobox" style="width:150px;" disabled="disabled">
								<option value="请先查询学生课程">请先查询学生课程</option>
							</select>
						</td>
						<td style="width:800px; text-align:left">
							<a href="#" onclick="doToolbar(this.id)" id="saveHkPlsz" class="easyui-linkbutton" plain="true" iconCls="icon-save">保存</a>
						</td>
					</tr>
				</table>
				<table id="stuCourseList" style="width:100%;"></table>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
	var sAuth = '<%=sAuth%>';
	var classCode = '';
	var className = '';
	var classType = '';
	var classXnxq = '';
	var stuCode = '';
	var stuName = '';
	var curXnxq = '';
	
	$(document).ready(function(){
		loadClassGrid();
		initDialog();//初始化对话框
		initData();//页面初始化加载数据
		initStuDatagrid();
		initStuCourseDatagrid();
		
		$('#XH_CX').bind('focus',function(){
			if($(this).val() == '请输入学生完整学号'){
				$(this).css('color', '#000000');
				$(this).val('');
			}
		}).bind('blur',function(){
			if($(this).val() == ''){
				$(this).css('color', '#A0A0A0');
				$(this).val('请输入学生完整学号');
			}
		});
		
		$('#XM_CX').bind('focus',function(){
			if($(this).val() == '请输入学生完整姓名'){
				$(this).css('color', '#000000');
				$(this).val('');
			}
		}).bind('blur',function(){
			if($(this).val() == ''){
				$(this).css('color', '#A0A0A0');
				$(this).val('请输入学生完整姓名');
			}
		});
		
		$('#pl_xh').bind('focus',function(){
			if($(this).val() == '请输入学生完整学号'){
				$(this).css('color', '#000000');
				$(this).val('');
			}
		}).bind('blur',function(){
			if($(this).val() == ''){
				$(this).css('color', '#A0A0A0');
				$(this).val('请输入学生完整学号');
			}
		});
		
		$('#pl_xm').bind('focus',function(){
			if($(this).val() == '请输入学生完整姓名'){
				$(this).css('color', '#000000');
				$(this).val('');
			}
		}).bind('blur',function(){
			if($(this).val() == ''){
				$(this).css('color', '#A0A0A0');
				$(this).val('请输入学生完整姓名');
			}
		});
	});
	
	/**页面初始化加载数据**/
	function initData(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Hksz',
			data : 'active=initData&sAuth=' + sAuth,
			dataType:"json",
			success : function(data) {
				curXnxq = data[0].curXnxq;
				initCombobox(data[0].xnxqData);
			}
		});
	}
	
	/**加载 dialog控件**/
	function initDialog(){
		$('#hkszDialog').dialog({   
			title: '缓考设置',   
			width: 400,//宽度设置   
			height: 160,//高度设置 
			modal:true,
			closed: true,   
			cache: false, 
			draggable:true,//是否可移动dialog框设置
			//读取事件
			onLoad:function(data){
			},
			//关闭事件
			onClose:function(data){
				$('#xzbmc').html('');
				$('#ic_xnxq').combobox('setValue', '');
				$('#stuList').datagrid('loadData',{total:0,rows:[]});
				$('#ic_cjlx').combobox('setValue', '');
			}
		});
		
		$('#stuListDialog').dialog({   
			title: '学生列表',   
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
				//$('#stuList').datagrid('loadData',{total:0,rows:[]});
			}
		});
		
		$('#plszDialog').dialog({   
			title: '缓考批量设置',   
			width: 750,//宽度设置   
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
				$('#pl_xh').css('color', '#A0A0A0');
				$('#pl_xh').val('请输入学生完整学号');
				$('#pl_xm').css('color', '#A0A0A0');
				$('#pl_xm').val('请输入学生完整姓名');
				$('#pl_xnxq').combobox('setValue', curXnxq);
				$('#stuCourseList').datagrid('loadData',{total:0,rows:[]});
			}
		});
	}
	
	/**加载combobox控件
		@xnxqData 学年学期下拉框数据
	**/
	function initCombobox(xnxqData){
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
				if(data == ''){
					$('#ic_kcmc').combobox('setValue', '');
					$('#ic_kcmc').combobox('setText', '请先选择学年学期');
					$('#ic_kcmc').combobox('disable');
					$('#ic_xsmd').val('请先选择学年学期');
					$('#ic_xsmd').attr('readonly', 'readonly');
					$('#ic_xsmd').prop('disabled', true);
				}else{
					$('#ic_xsmd').val('请先选择课程');
					$('#ic_xsmd').attr('readonly', 'readonly');
					$('#ic_xsmd').prop('disabled', true);
					//加载课程名称combobox控件
					loadic_kcmc();
				}
			}
		});
		
		$('#pl_xnxq').combobox({
			data:xnxqData,
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			panelHeight:'140', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data != ''){
					//初始化combobox时赋值
					$(this).combobox('setValue', curXnxq);
				}
			}
		});
	}
	
	/**加载课程名称combobox控件**/
	function loadic_kcmc(){
		$('#ic_kcmc').combobox({
			url:'<%=request.getContextPath()%>/Svl_Hksz?active=loadCourseCombo&classCode=' + classCode + '&xnxqbm=' + $('#ic_xnxq').combobox('getValue') + '&classType=' + classType,
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			panelHeight:'140', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data != ''){
					if(data.length == 1){
						$(this).combobox('setText','没有可选课程');
						$(this).combobox('disable');
					}else{
						$(this).combobox('enable');
						$(this).combobox('setValue', '');
						if(classType!='xzb' && classType!='jxb'){
							$(this).combobox('setValue', data[1].comboValue);
						}
					}
				}
			},
			//下拉列表值改变事件
			onChange:function(data){
				if(data == ''){
					$('#ic_xsmd').val('请先选择课程');
					$('#ic_xsmd').removeAttr('readonly');
					$('#ic_xsmd').prop('disabled', true);
				}else{
					//判断成绩类型是否为空
					if($('#ic_cjlx').combobox('getValue') == ""){
						$('#ic_xsmd').val('请先选择成绩类型');
						$('#ic_xsmd').removeAttr('readonly');
						$('#ic_xsmd').prop('disabled', true);
						//加载成绩类型combobox控件
						loadic_cjlx();
					}
					//获取学生缓考名单
					loadHKXX();
				}
			}
		})
	}
	
	/**加载成绩类型combobox控件**/
	function loadic_cjlx(){
		$('#ic_cjlx').combobox({
			editable:false,
			panelHeight:'auto', //combobox高度
			valueField:'value',
			textField:'text',
			 data : [
			 {
			 	"value":"",
			 	"text":"请选择",
			 	"selected":true
			 },{
			 	"value":"ps",
			 	"text":"平时"
			 },{
			 	"value":"qz",
			 	"text":"期中"
			 },{
			 	"value":"sx",
			 	"text":"实训"
			 },{
			 	"value":"qm",
			 	"text":"期末"
			 }
			 ],
			onChange:function(data){
				if(data == "" && $('#ic_kcmc').combobox('getValue') !== ""){
					$('#ic_xsmd').val('请先选择成绩类型');
					$('#ic_xsmd').removeAttr('readonly');
					$('#ic_xsmd').prop('disabled', true);
				}else{
					//判断课程名称是否为空
					if($('#ic_kcmc').combobox('getValue') == ""){
						$('#ic_xsmd').val('请先选择课程');
						$('#ic_xsmd').prop('disabled', true);
						$('#ic_xsmd').attr('readonly', 'readonly');
						//加载课程名称combobox控件
						loadic_kcmc()
					}
					//获取学生缓考名单
					loadHKXX();
				}
			}
		})
	}
	
	//读取当前课程学生缓考名单
	function loadHKXX(){
		if($('#ic_kcmc').combobox('getValue') == ""){
			$('#ic_xsmd').val('请先选择课程');
			$('#ic_xsmd').removeAttr('readonly');
			$('#ic_xsmd').prop('disabled', true);
		}
		if($('#ic_cjlx').combobox('getValue') == ""){
			$('#ic_xsmd').val('请先选择成绩类型');
			$('#ic_xsmd').removeAttr('readonly');
			$('#ic_xsmd').prop('disabled', true);
		}
		if($('#ic_kcmc').combobox('getValue') !== "" && $('#ic_cjlx').combobox('getValue') !== ""){
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_Hksz',
				data : 'active=loadStuHkmd&CX_XGBH=' + $('#ic_kcmc').combobox('getValue') + '&ic_cjlx=' + $('#ic_cjlx').combobox('getValue'),
				dataType:"json",
				success : function(data) {
					$('#ic_xsmd').removeAttr('disabled');
					$('#ic_xsmd').attr('readonly', 'readonly');
					
					if(data[0].stuCode == ''){
						$('#ic_xsmd').css('color', '#A0A0A0');
						$('#ic_xsmd').val('点击选择缓考学生');
						stuCode = '';
					}else{
						$('#ic_xsmd').css('color', '#000000');
						$('#ic_xsmd').val(data[0].stuName);
						stuCode = data[0].stuCode;
					}
					loadStuListData();
				}
			});
		}
	}
	
	/**加载批量成绩类型combobox控件**/
	function loadic_plcjlx(){
		$('#ic_plcjlx').combobox({
			editable:false,
			panelHeight:'auto', //combobox高度
			valueField:'value',
			textField:'text',
			 data : [
			 {
			 	"value":"",
			 	"text":"请选择"
			 },{
			 	"value":"ps",
			 	"text":"平时"
			 },{
			 	"value":"qz",
			 	"text":"期中"
			 },{
			 	"value":"sx",
			 	"text":"实训"
			 },{
			 	"value":"qm",
			 	"text":"期末"
			 }
			 ],
			 //当批量成绩类型下拉框改变时，根据所选的信息查找缓考人员信息
			onChange:function(data){
					//当成绩类型选择为‘请选择’时清空复选框 否则请求加载
					if(data !== ""){
						$.ajax({
						type : "POST",
						url : '<%=request.getContextPath()%>/Svl_Hksz',
						data : 'active=loadStuPLHKmd&CX_XNXQ=' + $('#pl_xnxq').combobox('getValue') + 
								'&CX_XH=' + encodeURI($('#pl_xh').val()) + '&CX_XM=' + encodeURI($('#pl_xm').val()) +
								'&ic_plcjlx=' + data,
						dataType:"json",
						success : function(datas) {
							//加载成功后先清空所有勾选项
							$('#stuCourseList').datagrid('clearSelections');
							stuCode = datas[0].stuCode;
							stuCode = stuCode.split(',');
							if(stuCode.length > 0){
								var rows = $("#stuCourseList").datagrid("getRows");
								for(var i=0;i<rows.length;i++){
									for(var j=0;j<stuCode.length;j++){
										if(rows[i].编号 == stuCode[j]){
											$('#stuCourseList').datagrid('selectRow', i);
										}
									}
								}
							}
							
						}
					});
				}else{
					$('#stuCourseList').datagrid('clearSelections');
				}
			}
		})
	}
	
	/**加载班级列表datagrid控件，读取页面信息**/
	function loadClassGrid(){
		$('#classList').datagrid({
			url: '<%=request.getContextPath()%>/Svl_Hksz',
			queryParams:{'active':'queClassList','sAuth':sAuth,'ZYMC_CX':encodeURI($('#ZYMC_CX').textbox('getValue')),
				'XZBMC_CX':encodeURI($('#XZBMC_CX').textbox('getValue')),'BJLX_CX':$('#BJLX_CX').combobox('getValue'),
				'XNXQMC_CX':encodeURI($('#XNXQMC_CX').textbox('getValue')),'XH_CX':encodeURI($('#XH_CX').val()),
				'XM_CX':encodeURI($('#XM_CX').val())},
			title:'班级信息列表',
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
				{field:'班级代码',title:'班级代码',width:fillsize(0.25),align:'center'},
				{field:'班级名称',title:'班级名称',width:fillsize(0.25),align:'center'},
				{field:'专业名称',title:'专业名称',width:fillsize(0.25),align:'center'},
				{field:'学年学期名称',title:'学年学期名称',width:fillsize(0.15),align:'center'},
				{field:'班级类型',title:'班级类型',width:fillsize(0.1),align:'center'}
			]],
			//双击某行时触发
			onDblClickRow:function(rowIndex,rowData){
				doToolbar('hksz');
			},
			//读取datagrid之前加载
			onBeforeLoad:function(){},
			//单击某行时触发
			onClickRow:function(rowIndex,rowData){
				classCode = rowData.班级代码;
				className = rowData.班级名称;
				classType = rowData.班级类型代码;
				classXnxq = rowData.学年学期编码;
			},
			//加载成功后触发
			onLoadSuccess: function(data){
				classCode = '';
				className = '';
			}
		});
	};
	
	/**打开登分学生列表**/
	function openStuList(id){
		$('#' + id).blur();
		$('#stuListDialog').dialog('open');
	}
	
	/**工具栏按钮调用方法，传入按钮的id
		@id 当前按钮点击事件
	**/
	function doToolbar(id){
		//查询班级列表
		if(id == 'queList'){
			loadClassGrid();
		}
		
		//缓考设置
		if(id == 'hksz'){
			if(classCode == ''){
				alertMsg('请选择一个班级');
				return;
			}
			$('#xzbmc').html(className);
			if(classType=='xzb' || classType=='jxb'){
				$('#ic_xnxq').combobox('enable');
				$('#ic_xnxq').combobox('setValue', curXnxq);
			}else{
				$('#ic_xnxq').combobox('setValue', classXnxq);
				$('#ic_xnxq').combobox('disable');
			}
			$('#hkszDialog').dialog('open');
		}
		
		//批量设置
		if(id == 'plsz'){
			$('#plszDialog').dialog('open');
			loadic_plcjlx();
			$('#ic_plcjlx').combobox('setValue','请先查询学生课程');
		}
		
		//保存缓考学生
		if(id == 'saveHkStu'){
			var data = $('#stuList').datagrid('getSelections');
			stuCode = '';
			stuName = '';
			
			if(data.length > 0){
				for(var i=0; i<data.length; i++){
					stuCode += data[i].学号+',';
					stuName += data[i].姓名+'、';
				}
				stuCode = stuCode.substring(0, stuCode.length-1);
				stuName = stuName.substring(0, stuName.length-1);
			}
			$('#active').val(id);
			$('#CX_XGBH').val($('#ic_kcmc').combobox('getValue'));
			$('#CX_XH').val(stuCode);
			$('#form1').submit();
		}
		
		//查询学生课程列表
		if(id == 'queStuCourseList'){
			if($('#pl_xnxq').combobox('getValue') == ''){
				alertMsg('请选择学年学期');
				$('#ic_plcjlx').combobox('setValue','请先查询学生课程')
				$('#ic_plcjlx').combobox('disable',true);
				$('#stuCourseList').datagrid('loadData', { total: 0, rows: [] }); 
				return;
			}
		
			if($('#pl_xh').val()=='请输入学生完整学号' && $('#pl_xm').val()=='请输入学生完整姓名'){
				alertMsg('请输入学号或姓名');
				$('#ic_plcjlx').combobox('setValue','请先查询学生课程')
				$('#ic_plcjlx').combobox('disable',true);
				$('#stuCourseList').datagrid('loadData', { total: 0, rows: [] }); 
				return;
			}
			//将成绩类型下拉框 解锁 并默认为请选择
			$('#ic_plcjlx').combobox('setValue','')
			$('#ic_plcjlx').combobox('enable');
			
			loadStuCourseListData();
		}
		
		//保存批量设置
		if(id == 'saveHkPlsz'){
			if($('#ic_plcjlx').combobox('getValue') == ""){
				alertMsg('请选择成绩类型');
				return;
			}
			if($('#ic_plcjlx').combobox('getValue') == "请先查询学生课程"){
				alertMsg('请先查询学生课程');
				return;
			}
			var data = $('#stuCourseList').datagrid('getSelections');
			var code = '';
			if(data.length > 0){
				for(var i=0; i<data.length; i++){
					code += data[i].编号+',';
				}
				code = code.substring(0, code.length-1);
			}
			
			$('#active').val(id);
			$('#CX_XGBH').val(code);
			$('#plcjlx').val($("#ic_plcjlx").combobox('getValue'));
			$('#form1').submit();
		}
	}
	
	/**表单提交**/
	$('#form1').form({
		//定位到servlet位置的url
		url:'<%=request.getContextPath()%>/Svl_Hksz',
		//当点击事件后触发的事件
		onSubmit: function(data){},
		//当点击事件并成功提交后触发的事件
		success:function(data){
			var json  =  eval("("+data+")");
			if(json[0].MSG == '保存成功'){
				showMsg(json[0].MSG);
				
				if($('#active').val() == 'saveHkStu'){
					if(stuCode == ''){
						$('#ic_xsmd').css('color', '#A0A0A0');
						$('#ic_xsmd').val('点击选择缓考学生');
						stuCode = '';
					}else{
						$('#ic_xsmd').css('color', '#000000');
						$('#ic_xsmd').val(stuName);
					}
					$('#stuListDialog').dialog('close');
				}
			}else{
				alertMsg(json[0].MSG);
			}
		}
	});
	
	/**初始化学生datagrid**/
	function initStuDatagrid(){
		$('#stuList').datagrid({
			//url :'<-%=request.getContextPath()%>/Svl_Hksz',
			//queryParams: {'active':'queStuList','CX_XGBH':$('#ic_kcmc').combobox('getValue')},
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
				{field:'ck',checkbox:true},
				{field:'班内学号',title:'学号',width:fillsize(0.3),align:'center'},
				{field:'姓名',title:'姓名',width:fillsize(0.3),align:'center'},
				{field:'学籍号',title:'学籍号',width:fillsize(0.4),align:'center'}
			]],
			//加载成功后触发
			onLoadSuccess: function(data){
				//勾选已选学生
				if(data && data.rows.length>0){
					var firstIndex = -1;
					stuCode = stuCode.split(',');
					
					$.each(data.rows, function(rowIndex, rowData){
						for(var i=0; i<stuCode.length; i++){
							if(stuCode[i] == rowData.学号){
								if(firstIndex == -1)
									firstIndex = rowIndex;
									
								$('#stuList').datagrid('selectRow', rowIndex);
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
			}
		});
	}
	
	/**读取学生datagrid数据**/
	function loadStuListData(){
		$('#stuList').datagrid({
			url :'<%=request.getContextPath()%>/Svl_Hksz',
			queryParams: {'active':'queStuList','CX_XGBH':$('#ic_kcmc').combobox('getValue')}
		});
	}
	
	/**初始化学生课程datagrid**/
	function initStuCourseDatagrid(){
		$('#stuCourseList').datagrid({
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
				{field:'学号',title:'学号',width:fillsize(0.15),align:'center'},
				{field:'姓名',title:'姓名',width:fillsize(0.15),align:'center'},
				{field:'课程名称',title:'课程名称',width:fillsize(0.5),align:'center'},
				{field:'课程类型',title:'课程类型',width:fillsize(0.2),align:'center'}
			]],
			//加载成功后触发
			onLoadSuccess: function(data){
				
			}
		});
	}
	
	/**读取学生datagrid数据**/
	function loadStuCourseListData(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Hksz',
			data : 'active=loadStuCourseListData&CX_XNXQ=' + $('#pl_xnxq').combobox('getValue') + 
				'&CX_XH=' + encodeURI($('#pl_xh').val()) + '&CX_XM=' + encodeURI($('#pl_xm').val()),
			dataType:"json",
			success : function(data) {
				$('#stuCourseList').datagrid({
					data:data[0].listData
				});
				
				$('#CX_XNXQ').val(data[0].semCode);
				$('#CX_XH').val(data[0].stuCode);
				$('#CX_XM').val(data[0].stuName);
			}
		});
	}
</script>
</html>