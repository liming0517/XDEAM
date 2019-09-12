<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="java.util.Vector"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.src.develop.store.user.*"%>
<%
	/**
		创建人：yeq
		Create date: 2016.12.07
		功能说明：用于设置分层课程相关信息
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
	<title>分层课程信息</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/icon.css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/pubTimetable.js"></script>
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
	<div id="north" region="north" title="分层课程信息" style="height:116px;">
		<table>
			<tr>
				<td><a href="#" id="addCourse" class="easyui-linkbutton" plain="true" iconcls="icon-new" onClick="doToolbar(this.id);" title="">新建</a></td>
				<td><a href="#" id="editCourse" class="easyui-linkbutton" plain="true" iconcls="icon-edit" onClick="doToolbar(this.id);" title="">编辑</a></td>
				<td><a href="#" id="delCourse" class="easyui-linkbutton" plain="true" iconcls="icon-cancel" onClick="doToolbar(this.id);" title="">删除</a></td>
			</tr>
		</table>
		<table id="ee" singleselect="true" width="100%" class="tablestyle">
			<tr>
				<td style="width:100px;" class="titlestyle">学年学期名称</td>
				<td>
					<select name="XNXQMC_CX" id="XNXQMC_CX" class="easyui-combobox" style="width:200px;">
					</select>
				</td>
				<td style="width:150px;" class="titlestyle">教学性质</td>
				<td>
					<select name="JXXZ_CX" id="JXXZ_CX" class="easyui-combobox" style="width:200px;">
					</select>
				</td>
				<td style="width:100px;" class="titlestyle">系名称</td>
				<td>
					<select name="XDM_CX" id="XDM_CX" class="easyui-combobox" style="width:200px;">
					</select>
				</td>
			</tr>
			<tr>
				<td style="width:100px;" class="titlestyle">年级名称</td>
				<td>
					<select name="NJDM_CX" id="NJDM_CX" class="easyui-combobox" style="width:200px;">
					</select>
				</td>
				<td style="width:100px;" class="titlestyle">课程名称</td>
				<td>
					<input name="KCMC_CX" id="KCMC_CX" class="easyui-textbox" style="width:200px;"/>
				</td>
				<td colspan="2">
					<a href="#" onclick="doToolbar(this.id)" id="queList" class="easyui-linkbutton" plain="true" iconcls="icon-search">查询</a>
				</td>
			</tr>
	    </table>
	</div>
	<div region="center">
		<table id="courseList" style="width:100%;"></table>
	</div>
	
	<!-- 分层课程信息新建编辑 -->
	<div id="fckcInfoDialog" style="overflow:hidden;">
		<form id="form1" method='post'>
			<table style="width:100%;" class="tablestyle">
				<tr>
					<td class="titlestyle" style="width:100px;">学年学期名称</td>
					<td>
						<select name="GF_XNXQBM" id="GF_XNXQBM" class="easyui-combobox" style="width:250px;">
						</select>
					</td>
				</tr>
				<tr>
					<td style="width:150px;" class="titlestyle">教学性质</td>
					<td>
						<select name="GF_JXXZ" id="GF_JXXZ" class="easyui-combobox" style="width:250px;">
						</select>
					</td>
				</tr>
				<tr>
					<td class="titlestyle">系名称</td>
					<td>
						<select name="GF_XDM" id="GF_XDM" class="easyui-combobox" style="width:250px;">
						</select>
					</td>
				</tr>
				<tr>
					<td class="titlestyle">年级名称</td>
					<td>
						<select name="GF_NJDM" id="GF_NJDM" class="easyui-combobox" style="width:250px;">
						</select>
					</td>
				</tr>
				<tr>
					<td class="titlestyle">课程名称</td>
					<td>
						<select name="GF_KCDM" id="GF_KCDM" class="easyui-combobox" style="width:250px;">
						</select>
					</td>
				</tr>
				<tr>
					<td class="titlestyle">授课周次</td>
					<td>
						<input name="GF_SKZCMC" id="GF_SKZCMC" style="width:250px;" readonly="readonly" onclick="openWeekList(this.id);"/>
					</td>
				</tr>
				<tr>
					<td class="titlestyle">学分</td>
					<td>
						<input name="GF_XF" id="GF_XF" class="easyui-numberbox" data-options="min:0,precision:1" style="width:250px;"/>
					</td>
				</tr>
				<tr>
					<td class="titlestyle">总课时</td>
					<td>
						<input name="GF_ZKS" id="GF_ZKS" class="easyui-numberbox" data-options="min:0,precision:0" style="width:250px;"/>
					</td>
				</tr>
				<tr>
					<td class="titlestyle">考试形式</td>
					<td>
						<select name="GF_KSXS" id="GF_KSXS" class="easyui-combobox" style="width:250px;">
						</select>
					</td>
				</tr>
			</table>
			
			<input type="hidden" id="active" name="active"/>
			<input type="hidden" id="GF_FCKCBH" name="GF_FCKCBH"/>
			<input type="hidden" id="GF_KCMC" name="GF_KCMC"/>
			<input type="hidden" id="GF_SKZC" name="GF_SKZC"/>
			<input type="hidden" id="GF_FCBBH" name="GF_FCBBH"/>
			<input type="hidden" id="GF_FCBMC" name="GF_FCBMC"/>
			<input type="hidden" id="GF_SKJSBH" name="GF_SKJSBH"/>
			<input type="hidden" id="GF_SKJSXM" name="GF_SKJSXM"/>
			<input type="hidden" id="GF_KMBH" name="GF_KMBH"/>
			<input type="hidden" id="GF_XH" name="GF_XH"/>
		</form>
	</div>
	
	<!-- 分层班信息 -->
	<div id="fcbListDialog">
		<div class="easyui-layout" style="width:100%; height:100%;">
			<div region="center">
				<table id="classList" style="width:100%;"></table>
			</div>
		</div>
	</div>
	
	<!-- 分层班新建编辑 -->
	<div id="fcbInfoDialog" style="overflow:hidden;">
		<table style="width:100%;" class="tablestyle">
			<tr>
				<td class="titlestyle" style="width:100px;">分层班名称</td>
				<td>
					<input id="GF_BJMC" name="GF_BJMC" class="easyui-textbox" style="width:250px"/>
				</td>
			</tr>
			<tr>
				<td class="titlestyle">授课教师</td>
					<td>
						<select name="GF_SKJS" id="GF_SKJS" class="easyui-combobox" style="width:250px;">
						</select>
					</td>
				</tr>
		</table>
	</div>
	
	<!-- 分层班学生列表 -->
	<div id="stuListDialog">
		<div class="easyui-layout" style="width:100%; height:100%;">
			<div region="center">
				<table id="stuList" style="width:100%;"></table>
			</div>
		</div>
	</div>
	
	<!-- 学生数据 -->
	<div id="addStuDialog">
		<div class="easyui-layout" style="width:100%; height:100%;">
			<div region="north" style="height:34px;">
				<table>
					<tr>
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
	
	<!-- 更改课程信息授课周次 -->
	<div id="weekNumDialog">
		<table style="width:100%;">
			<tr>
				<td><a href="#" id="confirmWeek" class="easyui-linkbutton" plain="true" iconcls="icon-ok" onClick="doToolbar(this.id);">确认</a></td>
			</tr>
		</table>
		<table id="weekList" style="font-size:12px; width:100%; border-top:1px solid #95B8E7; border-right:1px solid #95B8E7; text-align:center;" cellpadding="0" cellspacing="0">
		</table>
	</div>
	
	<!-- 导入学生名单 -->
	<div id="importStuDialog" style="overflow:hidden;">
		<%-- 遮罩层 --%>
    	<div id="divPageMask" class="maskStyle">
    		<div id="maskFont">学生名单同步中...</div>
    	</div>
    	
    	<table>
			<tr>
				<td><a href="#" id="confirmImport" class="easyui-linkbutton" plain="true" iconcls="icon-ok" onClick="doToolbar(this.id);">确认</a></td>
			</tr>
		</table>
		<table style="width:100%;" class="tablestyle">
			<tr>
				<td class="titlestyle">目标分层班</td>
				<td id="GF_MBFCB" class="titlestyle">&nbsp;</td>
			</tr>
			<tr>
				<td class="titlestyle">来源分层班</td>
				<td>
					<select name="GF_LYFCB" id="GF_LYFCB" class="easyui-combobox" style="width:250px;">
					</select>
				</td>
			</tr>
		</table>
	</div>
</body>
<script type="text/javascript">
	var sAuth = '<%=sAuth%>';
	var courseRowData = '';
	var classRowData = '';
	var curSelCourse = '';
	var jxxzFirst = '';
	var curFckcbh = '';
	var curKmbh = '';
	var curFcbbh = '';
	var curFcbmc = '';
	
	$(document).ready(function(){
		loadCourseGrid();
		initComboData();//页面初始化加载数据
		initDialog();//初始化对话框
	});
	
	/**页面初始化加载数据**/
	function initComboData(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_FckcSet',
			data : 'active=initComboData',
			dataType:"json",
			success : function(data) {
				initCombobox(data[0].xnxqData, data[0].jxxzData, data[0].xdmData, data[0].njdmData, data[0].ksxsData, data[0].skjsData);
			}
		});
	}
	
	/**加载combobox控件
		@xnxqData 学年学期下拉框数据
		@jxxzData 教学性质下拉框数据
		@xdmData 系下拉框数据
		@njdmData 年级下拉框数据
		@ksxsData 考试形式下拉框数据
		@skjsData 授课教师下拉框数据
	**/
	function initCombobox(xnxqData, jxxzData, xdmData, njdmData, ksxsData, skjsData){
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
		
		$('#XDM_CX').combobox({
			data:xdmData,
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
		
		$('#NJDM_CX').combobox({
			data:njdmData,
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
		
		$('#GF_XNXQBM').combobox({
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
				loadCourseCombo();
			}
		});
		
		$('#GF_JXXZ').combobox({
			data:jxxzData,
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			panelHeight:'auto', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data != ''){
					if(data.length > 1){
						jxxzFirst = data[1].comboValue;
						$(this).combobox('setValue', jxxzFirst);
					}else{
						//初始化combobox时赋值
						$(this).combobox('setValue', '');
					}
				}
				
			},
			//下拉列表值改变事件
			onChange:function(data){}
		});
		
		$('#GF_XDM').combobox({
			data:xdmData,
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
				loadCourseCombo();
			}
		});
		
		$('#GF_NJDM').combobox({
			data:njdmData,
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
				loadCourseCombo();
			}
		});
		
		$('#GF_KSXS').combobox({
			data:ksxsData,
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
			}
		});
		
		$('#GF_SKJS').combobox({
			data:skjsData,
			valueField:'comboValue',
			textField:'comboName',
			editable:true,
			multiple:true,
			panelHeight:'140', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data != ''){
					//初始化combobox时赋值
					$(this).combobox('setValue', '');
				}
			},
			onSelect:function(data){
				var obj = $(this).combobox('getValues');
				
				if(obj.length > 1){
					$(this).combobox('unselect', '');
				}
				
				if(data.comboValue == ''){
					$(this).combobox('clear');
					$(this).combobox('setValue', '');
				}
			},
			onUnselect:function(data){
				var obj = $(this).combobox('getValues');
				
				if(obj.length < 1){
					$(this).combobox('select', '');
				}
			}
		});
		
		loadCourseCombo();
	}
	
	/**读取课程下拉框数据*/
	function loadCourseCombo(){
		if($('#GF_XNXQBM').combobox('getValue')!='' && $('#GF_JXXZ').combobox('getValue')!='' && $('#GF_XDM').combobox('getValue')!='' && $('#GF_NJDM').combobox('getValue')!=''){
			$('#GF_KCDM').combobox({
				url:'<%=request.getContextPath()%>/Svl_FckcSet?active=loadCourseCombo&GF_XNXQBM=' + $('#GF_XNXQBM').combobox('getValue') + 
					'&GF_JXXZ=' + $('#GF_JXXZ').combobox('getValue') + '&GF_XDM=' + $('#GF_XDM').combobox('getValue') + 
					'&GF_NJDM=' + $('#GF_NJDM').combobox('getValue'),
				valueField:'comboValue',
				textField:'comboName',
				editable:true,
				panelHeight:'140', //combobox高度
				onLoadSuccess:function(data){
					if(data.length == 1){
						$('#GF_KCDM').combobox('setText', '没有可选课程');
						$('#GF_KCDM').combobox('disable');
					}else{
						if(curSelCourse == ''){
							$('#GF_KCDM').combobox('enable');
							$(this).combobox('setValue', '');
						}else{
							$(this).combobox('setValue', curSelCourse);
							$('#GF_KCDM').combobox('disable');
						}
					}
				}
			});
		}else{
			$('#GF_KCDM').combobox('setText', '请先选择学年学期、系、年级信息');
			$('#GF_KCDM').combobox('disable');
		}
	}
	
	/**读取分层班下拉框数据*/
	function loadClassCombo(){
		$('#GF_LYFCB').combobox({
			url:'<%=request.getContextPath()%>/Svl_FckcSet?active=loadClassCombo&GF_FCKCBH=' + curFckcbh + '&GF_FCBBH=' + curFcbbh,
			valueField:'comboValue',
			textField:'comboName',
			editable:true,
			panelHeight:'140', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data != ''){
					//初始化combobox时赋值
					$(this).combobox('setValue', '');
				}
			}
		});
	}
	
	/**加载 dialog控件**/
	function initDialog(){
		$('#fckcInfoDialog').dialog({   
			width: 400,//宽度设置   
			height: 293,//高度设置 
			modal:true,
			closed: true,   
			cache: false, 
			draggable:true,//是否可移动dialog框设置
			toolbar:[{
				//保存编辑
				text:'保存',
				iconCls:'icon-save',
				handler:function(){
					//传入save值进入doToolbar方法，用于保存
					doToolbar('saveCourse');
				}
			}],
			//读取事件
			onLoad:function(data){
			},
			//关闭事件
			onClose:function(data){
				$('#GF_XNXQBM').combobox('enable');
				$('#GF_JXXZ').combobox('enable');
				$('#GF_XDM').combobox('enable');
				$('#GF_NJDM').combobox('enable');
				$('#GF_XNXQBM').combobox('setValue', '');
				$('#GF_JXXZ').combobox('setValue', jxxzFirst);
				$('#GF_XDM').combobox('setValue', '');
				$('#GF_NJDM').combobox('setValue', '');
				curSelCourse = '';
				$('#GF_SKZCMC').val('');
				$('#GF_SKZC').val('');
				$('#GF_XF').numberbox('setValue', '');
				$('#GF_ZKS').numberbox('setValue', '');
				$('#GF_KSXS').combobox('setValue', '');
			}
		});
		
		$('#fcbListDialog').dialog({
			title: '分层班信息',
			width: 700,//宽度设置   
			height: 400,//高度设置 
			modal:true,
			closed: true,   
			cache: false, 
			draggable:true,//是否可移动dialog框设置
			toolbar:[{
				text:'新增',
				iconCls:'icon-add',
				handler:function(){
					//传入save值进入doToolbar方法，用于保存
					doToolbar('addClass');
				}
			},{
				text:'编辑',
				iconCls:'icon-edit',
				handler:function(){
					//传入save值进入doToolbar方法，用于保存
					doToolbar('editClass');
				}
			},{
				text:'删除',
				iconCls:'icon-cancel',
				handler:function(){
					//传入save值进入doToolbar方法，用于保存
					doToolbar('delClass');
				}
			}],
			//读取事件
			onLoad:function(data){
			},
			//关闭事件
			onClose:function(data){
				$('#classList').datagrid('loadData',{total:0,rows:[]});
			}
		});
		
		$('#fcbInfoDialog').dialog({   
			width: 400,//宽度设置   
			height: 118,//高度设置 
			modal:true,
			closed: true,   
			cache: false, 
			draggable:true,//是否可移动dialog框设置
			toolbar:[{
				//保存编辑
				text:'保存',
				iconCls:'icon-save',
				handler:function(){
					//传入save值进入doToolbar方法，用于保存
					doToolbar('saveClass');
				}
			}],
			//读取事件
			onLoad:function(data){
			},
			//关闭事件
			onClose:function(data){
				$('#GF_BJMC').textbox('setValue', '');
				$('#GF_SKJS').combobox('setValue', '');
			}
		});
		
		$('#stuListDialog').dialog({
			title: '分层班学生信息',
			width: 700,//宽度设置   
			height: 500,//高度设置 
			modal:true,
			closed: true,   
			cache: false, 
			draggable:true,//是否可移动dialog框设置
			toolbar:[{
				text:'新增',
				iconCls:'icon-add',
				handler:function(){
					doToolbar('openStuTree');
				}
			},{
				text:'删除',
				iconCls:'icon-cancel',
				handler:function(){
					doToolbar('delStu');
				}
			},{
				text:'导入',
				iconCls:'icon-submit',
				handler:function(){
					doToolbar('importStu');
				}
			}],
			//读取事件
			onLoad:function(data){
			},
			//关闭事件
			onClose:function(data){
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
		
		$('#importStuDialog').dialog({
			title:'学生导入',
			width: 400,//宽度设置   
			height: 118,//高度设置 
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
	
	/**加载课程列表datagrid控件，读取页面信息**/
	function loadCourseGrid(){
		$('#courseList').datagrid({
			url: '<%=request.getContextPath()%>/Svl_FckcSet',
			queryParams:{'active':'queCourseList','sAuth':sAuth,'XNXQMC_CX':$('#XNXQMC_CX').combobox('getValue'),
				'XDM_CX':$('#XDM_CX').combobox('getValue'),'NJDM_CX':$('#NJDM_CX').combobox('getValue'),
				'KCMC_CX':encodeURI($('#KCMC_CX').textbox('getValue'))},
			title:'分层课程信息列表',
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
				{field:'学年学期名称',title:'学年学期名称',width:fillsize(0.1),align:'center'},
				{field:'教学性质',title:'教学性质',width:fillsize(0.05),align:'center'},
				{field:'系名称',title:'系名称',width:fillsize(0.15),align:'center'},
				{field:'年级名称',title:'年级名称',width:fillsize(0.1),align:'center'},
				{field:'课程名称',title:'课程名称',width:fillsize(0.25),align:'center'},
				{field:'授课周次名称',title:'授课周次',width:fillsize(0.1),align:'center'},
				{field:'学分',title:'学分',width:fillsize(0.05),align:'center'},
				{field:'总课时',title:'总课时',width:fillsize(0.05),align:'center'},
				{field:'考试形式',title:'考试形式',width:fillsize(0.05),align:'center'},
				{field:'col4',title:'操作',width:fillsize(0.1),align:'center',
					formatter:function(value,rec){
						return "<input type='button' value='[分层班设置]' onclick='openFcbList(\"" + rec.分层课程编号 + "\",\"" + rec.科目编号 + "\");' style=\"cursor:pointer;\">";
				}}
			]],
			//双击某行时触发
			onDblClickRow:function(rowIndex,rowData){},
			//读取datagrid之前加载
			onBeforeLoad:function(){},
			//单击某行时触发
			onClickRow:function(rowIndex,rowData){
				courseRowData = rowData;
			},
			//加载成功后触发
			onLoadSuccess: function(data){
				courseRowData = '';
			}
		});
	};
	
	/**打开分层班列表**/
	function openFcbList(fckcbh, kmbh){
		curFckcbh = fckcbh;
		curKmbh = kmbh;
		
		loadClassListData();
		$('#fcbListDialog').dialog('open');
	}
	
	/**打开分层班学生列表**/
	function openStuList(fcbbh, fcbmc){
		curFcbbh = fcbbh;
		curFcbmc = fcbmc;
		
		loadStuListData();
		$('#stuListDialog').dialog('open');
	}
	
	/**工具栏按钮调用方法，传入按钮的id
		@id 当前按钮点击事件
	**/
	function doToolbar(id){
		//查询分层课程列表
		if(id == 'queList'){
			loadCourseGrid();
		}
		
		//新建分层课程
		if(id == 'addCourse'){
			$('#active').val(id);
			
			$('#fckcInfoDialog').dialog({
				title:'新建分层课程'
			});
			$('#fckcInfoDialog').dialog('open');
		}
		
		//编辑分层课程
		if(id == 'editCourse'){
			if(courseRowData == ''){
				alertMsg('请选择一条数据');
				return;
			}
		
			$('#active').val(id);
			$('#GF_FCKCBH').val(courseRowData.分层课程编号);
			$('#GF_XNXQBM').combobox('setValue', courseRowData.学年学期编码);
			$('#GF_JXXZ').combobox('setValue', courseRowData.教学性质代码);
			$('#GF_XDM').combobox('setValue', courseRowData.系代码);
			$('#GF_NJDM').combobox('setValue', courseRowData.年级代码);
			curSelCourse = courseRowData.课程代码;
			$('#GF_SKZC').val(courseRowData.授课周次);
			$('#GF_SKZCMC').val(courseRowData.授课周次名称);
			$('#GF_XF').numberbox('setValue', courseRowData.学分);
			$('#GF_ZKS').numberbox('setValue', courseRowData.总课时);
			$('#GF_KSXS').combobox('setValue', courseRowData.编号);
			$('#GF_XNXQBM').combobox('disable');
			$('#GF_JXXZ').combobox('disable');
			$('#GF_XDM').combobox('disable');
			$('#GF_NJDM').combobox('disable');
		
			$('#fckcInfoDialog').dialog({
				title:'编辑分层课程'
			});
			$('#fckcInfoDialog').dialog('open');
		}
		
		//保存分层课程
		if(id == 'saveCourse'){
			if($('#GF_XNXQBM').combobox('getValue') == ''){
				alertMsg('请选择学年学期');
				return;
			}
			
			if($('#GF_JXXZ').combobox('getValue') == ''){
				alertMsg('请选择教学性质');
				return;
			}
			
			if($('#GF_XDM').combobox('getValue') == ''){
				alertMsg('请选择系名称');
				return;
			}
			
			if($('#GF_NJDM').combobox('getValue') == ''){
				alertMsg('请选择年级名称');
				return;
			}
			
			if($('#GF_KCDM').combobox('getValue')=='' || $('#GF_KCDM').combobox('getValue')==undefined){
				alertMsg('请选择课程名称');
				return;
			}
			
			if($('#GF_SKZC').val() == ''){
				alertMsg('请选择授课周次');
				return;
			}
			
			if($('#GF_XF').numberbox('getValue') == ''){
				alertMsg('请填写学分');
				return;
			}
			
			if($('#GF_ZKS').numberbox('getValue') == ''){
				alertMsg('请填写总课时');
				return;
			}
			
			if($('#GF_KSXS').combobox('getValue') == ''){
				alertMsg('请选择考试形式');
				return;
			}
			
			$('#GF_KCMC').val($('#GF_KCDM').combobox('getText'));
			$("#form1").submit();
		}
		
		//删除分层课程
		if(id == 'delCourse'){
			if(courseRowData == ''){
				alertMsg('请选择一条数据');
				return;
			}
			
			ConfirmMsg('是否确定要删除选择的分层课程<font color="red">（将会删除该分层课程相关的分层班信息、学生信息、考试信息、成绩信息）</font>？', 'delCourse();', '');
		}
		
		//新建分层班
		if(id == 'addClass'){
			$('#active').val(id);
			
			$('#fcbInfoDialog').dialog({
				title:'新建分层班'
			});
			$('#fcbInfoDialog').dialog('open');
		}
		
		//编辑分层班
		if(id == 'editClass'){
			if(classRowData == ''){
				alertMsg('请选择一条数据');
				return;
			}
		
			$('#active').val(id);
			$('#GF_FCBBH').val(classRowData.分层班编号);
			$('#GF_BJMC').textbox('setValue', classRowData.分层班名称);
			$('#GF_SKJS').combobox('setValues', classRowData.授课教师编号.split(','));
		
			$('#fcbInfoDialog').dialog({
				title:'编辑分层班'
			});
			$('#fcbInfoDialog').dialog('open');
		}
		
		//保存分层班
		if(id == 'saveClass'){
			if($('#GF_BJMC').textbox('getValue') == ''){
				alertMsg('请填写分层班名称');
				return;
			}
			
			if($('#GF_SKJS').combobox('getValue')=='' || $('#GF_SKJS').combobox('getValue')==undefined){
				alertMsg('请选择授课教师');
				return;
			}
			
			$('#GF_FCKCBH').val(curFckcbh);
			$('#GF_KMBH').val(curKmbh);
			$('#GF_FCBMC').val($('#GF_BJMC').textbox('getValue'));
			$('#GF_SKJSBH').val($('#GF_SKJS').combobox('getValues').toString());
			$('#GF_SKJSXM').val($('#GF_SKJS').combobox('getText'));
			$("#form1").submit();
		}
		
		//删除分层班
		if(id == 'delClass'){
			if(classRowData == ''){
				alertMsg('请选择一条数据');
				return;
			}
			
			ConfirmMsg('是否确定要删除选择的分层班<font color="red">（将会删除该分层班相关的学生信息、考试信息、成绩信息）</font>？', 'delClass();', '');
		}
		
		//打开学生树
		if(id == 'openStuTree'){
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
					stuInfo += totalData[i].id+',';
				}
			}
			if(stuInfo.length == 0){
				alertMsg('请至少选择一名学生');
				return;
			}
			stuInfo = stuInfo.substring(0, stuInfo.length-1);
			
			$('#active').val(id);
			$('#GF_FCBBH').val(curFcbbh);
			$('#GF_XH').val(stuInfo);
			$('#form1').submit();
		}
		
		//删除学生
		if(id == 'delStu'){
			var stuData = $('#stuList').datagrid('getSelections');
			
			if(stuData.length == 0){
				alertMsg('请至少选择一名学生');
				return;
			}
			
			var stuCode = '';
			for(var i=0; i<stuData.length; i++){
				stuCode += stuData[i].学号+',';
			}
			stuCode = stuCode.substring(0, stuCode.length-1);
			
			ConfirmMsg('是否确定要删除选择的学生<font color="red">（将删除相关学生成绩信息）</font>？', 'delStu("' + stuCode + '");', '');
		}
		
		//打开打入学生对话框
		if(id == 'importStu'){
			loadClassCombo();
			$('#GF_MBFCB').html(curFcbmc);
			$('#importStuDialog').dialog('open');
		}
		
		//确认导入学生名单
		if(id == 'confirmImport'){
			if($('#GF_LYFCB').combobox('getValue')=='' || $('#GF_LYFCB').combobox('getValue')==undefined){
				alertMsg('请选择来源分层班');
				return;
			}
		
			importStuList();
		}
		
		//确认更改周次
		if(id == 'confirmWeek'){
			var selWeek = '';
			$('.allNum').each(function(){
				if($(this).val() == '1'){
					selWeek += $(this).attr('name')+'#';
				}
			});
			
			if(selWeek.length == 0){
				alertMsg('请至少选择一个周次');
				return;
			}
			selWeek = selWeek.substring(0, selWeek.length-1);
			var curSelWeekArray = parseSkzc(selWeek);
			
			//修改周次为正确格式
			var weekName = '第';
			var weekCode = '';
			var flag = true;
			var oddFlag = true;
			var evenFlag = true;
			
			if(oddArray.length == curSelWeekArray.length){
				for(var i=0; i<oddArray.length; i++){
					if(oddArray[i] != curSelWeekArray[i]){
						oddFlag = false;
						break;
					}
				}
			}else{
				oddFlag = false;
			}
			
			if(evenArray.length == curSelWeekArray.length){
				for(var i=0; i<evenArray.length; i++){
					if(evenArray[i] != curSelWeekArray[i]){
						evenFlag = false;
						break;
					}
				}
			}else{
				evenFlag = false;
			}
			
			if(oddFlag == true){
				weekName = "单周";
				for(var i=0; i<curSelWeekArray.length; i++){
					weekCode += curSelWeekArray[i]+'#';
				}
				weekCode = weekCode.substring(0, weekCode.length-1);
			}else if(evenFlag == true){
				weekName = "双周";
				for(var i=0; i<curSelWeekArray.length; i++){
					weekCode += curSelWeekArray[i]+'#';
				}
				weekCode = weekCode.substring(0, weekCode.length-1);
			}else{
				for(var i=0; i<curSelWeekArray.length; i++){
					weekName += curSelWeekArray[i]+',';
					weekCode += curSelWeekArray[i]+'#';
					
					if(i < curSelWeekArray.length-1){
						if(parseInt(curSelWeekArray[i], 10)+1 != curSelWeekArray[i+1]){
							flag = false;
						}
					}
				}
				weekName = weekName.substring(0, weekName.length-1)+'周';
				weekCode = weekCode.substring(0, weekCode.length-1);
				
				if(flag==true && curSelWeekArray.length>1){
					weekName = curSelWeekArray[0]+'-'+curSelWeekArray[curSelWeekArray.length-1]+'周';
					weekCode = weekName.substring(0, weekName.length-1);
				}
			}
			
			$('#GF_SKZCMC').val(weekName);
			$('#GF_SKZC').val(weekCode);
			$('#weekNumDialog').dialog('close');
		}
	}
	
	/**表单提交**/
	$('#form1').form({
		//定位到servlet位置的url
		url:'<%=request.getContextPath()%>/Svl_FckcSet',
		//当点击事件后触发的事件
		onSubmit: function(data){}, 
		//当点击事件并成功提交后触发的事件
		success:function(data){
			var json  =  eval("("+data+")");
			if(json[0].MSG == '保存成功'){
				showMsg(json[0].MSG);
				
				if($('#active').val()=='addCourse' || $('#active').val()=='editCourse'){
					loadCourseGrid();
					$('#fckcInfoDialog').dialog('close');
				}
				
				if($('#active').val()=='addClass' || $('#active').val()=='editClass'){
					loadClassListData();
					$('#fcbInfoDialog').dialog('close');
				}
				
				if($('#active').val()=='saveAddStu'){
					loadStuListData();
					$('#addStuDialog').dialog('close');
				}
			}else{
				alertMsg(json[0].MSG);
			}
		}
	});
	
	/**删除分层课程*/
	function delCourse(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_FckcSet',
			data : 'active=delCourse&GF_FCKCBH=' + courseRowData.分层课程编号 + '&GF_KMBH=' + courseRowData.科目编号,
			dataType:"json",
			success : function(data) {
				if(data[0].MSG == '删除成功'){
					showMsg(data[0].MSG);
					loadCourseGrid();
				}else{
					alertMsg(data[0].MSG);
				}
			}
		});
	}
	
	/**删除分层班*/
	function delClass(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_FckcSet',
			data : 'active=delClass&GF_FCBBH=' + classRowData.分层班编号,
			dataType:"json",
			success : function(data) {
				if(data[0].MSG == '删除成功'){
					showMsg(data[0].MSG);
					loadClassListData();
				}else{
					alertMsg(data[0].MSG);
				}
			}
		});
	}
	
	/**删除分层班学生*/
	function delStu(stuCode){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_FckcSet',
			data : 'active=delStu&GF_FCBBH=' + curFcbbh + '&GF_XH=' + stuCode,
			dataType:"json",
			success : function(data) {
				if(data[0].MSG == '删除成功'){
					showMsg(data[0].MSG);
					loadStuListData();
				}else{
					alertMsg(data[0].MSG);
				}
			}
		});
	}
	
	/**读取分层班datagrid数据**/
	function loadClassListData(){
		$('#classList').datagrid({
			url :'<%=request.getContextPath()%>/Svl_FckcSet',
			queryParams: {'active':'queClassList','GF_FCKCBH':curFckcbh},
			title:'',
			width:'100%',
			nowrap: false,
			fit:true, //自适应父节点宽度和高度
			showFooter:true,
			striped:true,
			singleSelect:true,
			rownumbers:true,
			fitColumns: true,
			columns:[[
				//field为读取数据的数据名，title为显示的数据名，width宽度设置，align数字在表格中显示的位置
				/**{field:'ck',checkbox:true},*/
				{field:'分层班编号',hidden:true},
				{field:'分层班名称',title:'分层班名称',width:fillsize(0.4),align:'center'},
				{field:'授课教师编号',hidden:true},
				{field:'授课教师姓名',title:'授课教师姓名',width:fillsize(0.4),align:'center'},
				{field:'col4',title:'操作',width:fillsize(0.2),align:'center',
					formatter:function(value,rec){
						return "<input type='button' value='[学生名单]' onclick='openStuList(\"" + rec.分层班编号 + "\",\"" + rec.分层班名称 + "\");' style=\"cursor:pointer;\">";
				}}
			]],
			//单击某行时触发
			onClickRow:function(rowIndex,rowData){
				classRowData = rowData;
			},
			//加载成功后触发
			onLoadSuccess: function(data){
				classRowData = '';
			}
		});
	}
	
	/**读取学生datagrid数据**/
	function loadStuListData(){
		$('#stuList').datagrid({
			url :'<%=request.getContextPath()%>/Svl_FckcSet',
			queryParams: {'active':'queStuList','GF_FCBBH':curFcbbh},
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
				{field:'行政班名称',title:'行政班名称',width:fillsize(0.4),align:'center'},
				{field:'学号',title:'学号',width:fillsize(0.3),align:'center'},
				{field:'姓名',title:'姓名',width:fillsize(0.3),align:'center'}
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
			url:'<%=request.getContextPath()%>/Svl_FckcSet?active=queClassStuTree&GF_FCKCBH=' + curFckcbh,
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
		    	$('#classStuTree').tree('options').url="<%=request.getContextPath()%>/Svl_FckcSet?active=queClassStuTree&parentCode=" + node.id + "&GF_FCBBH=" + curFcbbh;
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
	
	/**打开周次选择对话框*/
	var curXqzc = '';
	var oddArray = new Array();
	var evenArray = new Array();
	function openWeekList(id){
		$('#' + id).blur();
		if($('#GF_XNXQBM').combobox('getValue') == ''){
			alertMsg('请先选择学年学期');
			return;
		}
		if($('#GF_JXXZ').combobox('getValue') == ''){
			alertMsg('请先选择性质');
			return;
		}
		var curSelSkzc = $('#GF_SKZC').val();
	
		//查询当前学期总周次
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_FckcSet',
			data : 'active=loadXqzc&GF_XNXQBM=' + $('#GF_XNXQBM').combobox('getValue')+$('#GF_JXXZ').combobox('getValue'),
			dataType:"json",
			success : function(data) {
				curXqzc = data[0].xqzc;//学期总周次
				
				//单双周周次
				var weekArray = parseWeekArray(curXqzc);
				oddArray = weekArray[0];
				evenArray = weekArray[1];
				
				//生成周次调整对话框内容
				var dialogHeight = 99;
				dialogHeight += parseInt(curXqzc/3, 10)*30;
				if(curXqzc%3 > 0){
					dialogHeight += 30;
				}
				$('#weekNumDialog').dialog({   
					title: '授课周次',   
					width: 400,
					height: dialogHeight,
					modal: true,
					closed: true,   
					cache: false, 
					draggable: true,//是否可移动dialog框设置
					//读取事件
					onLoad:function(data){
					},
					//关闭事件
					onClose:function(data){
						$('.weekNumTd').css('background-color', '#FFFFFF');
						$('.weekNumTd').removeAttr('title');
						$('.weekNumTd').css('cursor', 'pointer');
						$('.allNum').val('0');
					}
				});
				
				var contentStr = '';
				contentStr = '<tr style="background-color:#EFEFEF;"><td style="width:33.3%; height:30px; border-bottom:1px solid #95B8E7;">' + 
						'<input id="week_all" type="button" value="全选" style="cursor:pointer;" onclick="selWeekNum(this.id);"></td>' +
					'<td style="width:33.3%; border-bottom:1px solid #95B8E7;">' +
						'<input id="week_odd" type="button" value="单周" style="cursor:pointer;" onclick="selWeekNum(this.id);"></td>' +
					'<td style="border-bottom:1px solid #95B8E7;">' +
						'<input id="week_even" type="button" value="双周" style="cursor:pointer;" onclick="selWeekNum(this.id);"></td></tr>';
				
				for(var i=1; i<parseInt(curXqzc, 10)+1; i++){
					if(i%3 == 1){
						contentStr += '<tr>';
					}
					
					contentStr += '<td id="week_' + i + '" class="weekNumTd" ' +
						'style="height:30px; border-left:1px solid #95B8E7; border-bottom:1px solid #95B8E7; cursor:pointer;" ' + 
						'>'+i+'<input type="hidden" id="weekNumFlag_' + i + '" name="'+i+'" class="allNum" value="0"></td>'; //0未选中：1选中：2不可选
					
					if(i%3 == 0){
						contentStr += '</tr>';
					}
				}
				if(curXqzc%3 != 0){
					for(var i=0; i<3-curXqzc%3; i++){
						contentStr += '<td style="width:130px; height:30px; border-left:1px solid #95B8E7; border-bottom:1px solid #95B8E7;">&nbsp;</td>';
					}
					contentStr += '</tr>';
				}
				$('#weekList').html(contentStr);
				
				$('.weekNumTd').bind('mouseover', function(){
					var curNum = $(this).attr('id').substring(5);
					if($('#weekNumFlag_' + curNum).val() == '2'){
						return;
					}
				
					enterColor = $(this).css('background-color');
					$(this).css('background-color', '#FFE48D');
				}).bind('mouseout', function(){
					var curNum = $(this).attr('id').substring(5);
					if($('#weekNumFlag_' + curNum).val() == '2'){
						return;
					}
				
					$(this).css('background-color', enterColor);
					//阻止冒泡事件
					window.event? window.event.cancelBubble = true : e.stopPropagation();
				}).bind('click', function(){
					var curNum = $(this).attr('id').substring(5);
					if($('#weekNumFlag_' + curNum).val() == '2'){
						return;
					}
					
					if($('#weekNumFlag_' + curNum).val() == '0'){
						enterColor = '#E7FECB';
						$('#weekNumFlag_' + curNum).val('1');
					}else{
						enterColor = '#FFFFFF';
						$('#weekNumFlag_' + curNum).val('0');
					}
				});
				
				//选中当前授课周次
				var skzcArray = parseSkzc(curSelSkzc);
				for(var i=0; i<skzcArray.length; i++){
					$('#week_' + skzcArray[i]).css('background-color', '#E7FECB');
					$('#weekNumFlag_' + skzcArray[i]).val('1');
				}
				
				$('#weekNumDialog').dialog('open');
			}
		});
	}
	
	/**选择周次**/
	function selWeekNum(id){
		var curNum = '';
		var tempObj = '';
	
		if(id == 'week_all'){
			$('.weekNumTd').each(function(){
				curNum = $(this).attr('id').substring(5);
				tempObj = $('#weekNumFlag_' + curNum);
				
				if(tempObj.val()!='1' && tempObj.val()!='2'){
					$(this).css('background-color', '#E7FECB');
					tempObj.val('1');
				}
			});
		}else{
			var numArray = new Array();
			
			if(id == 'week_odd')
				numArray = ['1', '0'];
			if(id == 'week_even')
				numArray = ['0', '1'];
			
			$('.weekNumTd').each(function(){
				curNum = $(this).attr('id').substring(5);
				tempObj = $('#weekNumFlag_' + curNum);
				
				if(curNum%2==numArray[0] && tempObj.val()!='1' && tempObj.val()!='2'){
					$(this).css('background-color', '#E7FECB');
					tempObj.val('1');
				}else if(curNum%2==numArray[1] && tempObj.val()!='2'){
					$(this).css('background-color', '#FFFFFF');
					tempObj.val('0');
				}
			});
		}
	}
	
	/**同步学生名单*/
	function importStuList(){
		$('#divPageMask').show();
		
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_FckcSet',
			data : 'active=importStuList&GF_FCBBH=' + curFcbbh + '&GF_FCBMC=' + $('#GF_LYFCB').combobox('getValue'),
			dataType:"json",
			success : function(data) {
				$('#divPageMask').hide();
			
				if(data[0].MSG == '导入成功'){
					loadStuListData();
				
					showMsg(data[0].MSG);
					$('#importStuDialog').dialog('close');
				}else{
					alertMsg(data[0].MSG);
				}
			}
		});
	}
</script>
</html>