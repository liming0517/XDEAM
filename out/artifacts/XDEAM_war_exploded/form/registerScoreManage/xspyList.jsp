<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="java.util.Vector"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.src.develop.store.user.*"%>
<%
	/**
		创建人：ZhaiXuChao
		Create date: 2017.08.22
		功能说明：学生评语
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
	<title>学生评语</title>
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
<body class="easyui-layout">
    <div id="north" region="north" title="班级信息" style="height:59px;">
   		<table class="tablestyle" width="100%" border="0">
   			<!-- <tr>
	   				<td class="titlestyle">学籍号</td>
			   		<td>
			   			<input class="easyui-textbox" id="PY_XJH_CX" name="PY_XJH_CX" style="width:153px;"/>
					</td>
					<td class="titlestyle">姓名</td>
			   		<td>
			   			<input class="easyui-textbox" id="PY_XM_CX" name="PY_XM_CX" style="width:153px;"/>
					</td>
	   				<td class="titlestyle">行政班名称</td>
			   		<td>
			   			<select class="easyui-combobox" id="PY_XZBDM_CX" name="PY_XZBDM_CX" style="width:153px;">
					   	</select>
					</td>
				</tr>
				<tr>
					<td class="titlestyle">系部名称</td>
			   		<td>
			   			<select class="easyui-combobox" id="PY_XBDM_CX" name="PY_XBDM_CX" style="width:153px;">
				   		</select>
					</td>
					<td class="titlestyle">专业名称</td>
			   		<td>
			   			<select class="easyui-combobox" id="PY_ZYDM_CX" name="PY_ZYDM_CX" style="width:153px;">
				   		</select>
					</td>
					<td colspan="2">
						<a href="#" id="que" class="easyui-linkbutton" plain="true" iconCls="icon-search" onclick="doToolbar(this.id);">查询</a>
					</td>
	   			</tr> -->
	   			
	   			<tr>
	   				<td class="titlestyle">系部名称</td>
			   		<td>
			   			<select class="easyui-combobox" id="PY_XBDM_CX" name="PY_XBDM_CX" style="width:153px;">
				   		</select>
					</td>
					<td class="titlestyle">行政班名称</td>
			   		<td>
			   			<select class="easyui-combobox" id="PY_XZBDM_CX" name="PY_XZBDM_CX" style="width:153px;">
					   	</select>
					</td>
	   				<td class="titlestyle">专业名称</td>
			   		<td>
			   			<select class="easyui-combobox" id="PY_ZYDM_CX" name="PY_ZYDM_CX" style="width:153px;">
				   		</select>
					</td>
					<td>
						<a href="#" id="que" class="easyui-linkbutton" plain="true" iconCls="icon-search" onclick="doToolbar(this.id);">查询</a>
					</td>
				</tr>
	    </table>	    				
    </div>
	<div region="center">
		<table id="stuList"></table>
	</div> 
	
	
	<!--  dialog -->
	<!-- <div id="DIA_Lead" style="overflow:hidden;">
		<form method="post" id="form1" name="form1">
			<div id="tt" class="easyui-tabs" style="width:586px; height:368px; background:#efefef;">
				<div title="一年级" style="background-color:#EFEFEF;">
					<table id="stufirstInfo" class="tablestyle" style="width:100%;">
						<tr>
							<td class="titlestyle" align="center">评语:</td>
							<td>
								<textarea name="PY_PY_1" id="PY_PY_1"  style="width:100%; height:110px;" maxlength="1000"></textarea>		
							</td>
						</tr>
						<tr>
							<td colspan="2" align="center">
								<a href="#" id="baocun" class="easyui-linkbutton" plain="true" iconCls="icon-save" onclick="doToolbar(this.id);">保存</a>
							</td>
						</tr>
					</table>
				</div>
				<div title="两年级" style="background-color:#EFEFEF;">
					<table id="stusecondInfo" class="tablestyle" style="width:100%;">
						<tr>
							<td class="titlestyle" align="center">评语:</td>
							<td>
								<textarea name="PY_PY_2" id="PY_PY_2"  style="width:100%; height:110px;" maxlength="1000"></textarea>		
							</td>
						</tr>
						<tr>
							<td colspan="2" align="center">
								<a href="#" id="baocun" class="easyui-linkbutton" plain="true" iconCls="icon-save" onclick="doToolbar(this.id);">保存</a>
							</td>
						</tr>



					</table>
				</div>
				<div title="三年级" style="background-color:#EFEFEF;">
					<table id="stuthirdInfo" class="tablestyle" style="width:100%;">
						
						<tr>
							<td class="titlestyle" align="center">评语:</td>
							<td>
								<textarea name="PY_PY_3" id="PY_PY_3"  style="width:100%; height:110px;" maxlength="1000"></textarea>		
							</td>
						</tr>
						<tr>
							<td colspan="2" align="center">
								<a href="#" id="baocun" class="easyui-linkbutton" plain="true" iconCls="icon-save" onclick="doToolbar(this.id);">保存</a>
							</td>
						</tr>



					</table>
				</div>
				
				<div title="四年级" style="background-color:#EFEFEF;">
					<table id="stuforthInfo" class="tablestyle" style="width:100%;">
						
						<tr>
							<td class="titlestyle" align="center">评语:</td>
							<td>
								<textarea name="PY_PY_4" id="PY_PY_4"  style="width:100%; height:110px;" maxlength="1000"></textarea>		
							</td>
						</tr>
						<tr>
							<td colspan="2" align="center">
								<a href="#" id="baocun" class="easyui-linkbutton" plain="true" iconCls="icon-save" onclick="doToolbar(this.id);">保存</a>
							</td>
						</tr>

					</table>
				</div>
			</div>
		
		   	<input type="hidden" id="active" name="active" />
		   	<input type="hidden" id="PY_XJH" name="PY_XJH" />
		   	<input type="hidden" id="PY_NJ" name="PY_NJ" />
		   	<input type="hidden" id="PY_PY" name="PY_PY" />
		</form>
	</div> -->
	
	
	<!--dialog 班级学生评语输入 -->
 	<div id="DIA_Lead" style="overflow:hidden;">
 		<div style="width:100%; height:100%;" class="easyui-layout">
 			<div region="west" title="学生列表" style="width:200px;">
 				<ul id="classTree" class="easyui-tree" style="display:none">
				</ul>
 			</div>
 			<div region="center" title="学生评语信息" style="overflow:hidden;">
 				<div id="DIA_StuPY" style="overflow:hidden;">
					<form method="post" id="form1" name="form1">
						<div id="tt" class="easyui-tabs" style="width:100%; height:500px; background:#efefef;">
							<div title="一年级" style="background-color:#EFEFEF;overflow:hidden">
								<table id="stufirstInfo" class="tablestyle" style="width:100%;">
									<tr>
										<td colspan="" align="left">
											<a href="#" id="baocun_1" class="easyui-linkbutton" plain="true" iconCls="icon-save" onclick="doToolbar(this.id);">保存</a>
										</td>
									</tr>
								</table>
								
								<div class="easyui-layout" style="width:100%; height:100%;">
									<!--dialog左右边  -->
									<div region="west" title="第一学期" style="width:120px;background-color:#EFEFEF">
										<table style="font-size:12;">
											<tr>
												<td><input type="radio" class="youRadio_1" name="Radio_1" id="youRadio_1" value="1" style="cursor:pointer;"/></td>
												<td><label for="youRadio_1" style="cursor:pointer;">优</label></td>
											</tr>
											<tr>
												<td><input type="radio" class="liangRadio_1" name="Radio_1" id="liangRadio_1" value="2" style="cursor:pointer;"/></td>
												<td><label for="liangRadio_1" style="cursor:pointer;">良</label></td>
											</tr>
											<tr>
												<td><input type="radio" class="jgRadio_1" name="Radio_1" id="jgRadio_1" value="3" style="cursor:pointer;"/></td>
												<td><label for="jgRadio_1" style="cursor:pointer;">及格</label></td>
											</tr>
											<tr>
												<td><input type="radio" class="bjgRadio_1" name="Radio_1" id="bjgRadio_1" value="4" style="cursor:pointer;"/></td>
												<td><label for="bjgRadio_1" style="cursor:pointer;">不及格</label></td>
											</tr>
										</table>
									</div>
									<div region="center" title="第二学期">
										<table id="stufirstInfo" class="tablestyle" style="width:100%;height:348px;">
											<tr>
												<td class="titlestyle" align="center" style="height:290px;width:50px;">评语:</td>
												<td>
													<textarea name="PY_PY_1" id="PY_PY_1"  style="width:100%;height:338px;" maxlength="1000"></textarea>		
												</td>
											</tr>
										</table>
									</div>
								</div>
							</div>
							<div title="两年级" style="background-color:#EFEFEF;overflow:hidden">
								<table id="stufirstInfo" class="tablestyle" style="width:100%;">
									<tr>
										<td colspan="" align="left">
											<a href="#" id="baocun_2" class="easyui-linkbutton" plain="true" iconCls="icon-save" onclick="doToolbar(this.id);">保存</a>
										</td>
									</tr>
								</table>
								
								<div class="easyui-layout" style="width:100%; height:100%;">
									<!--dialog右边  -->
									<div region="west" title="第一学期" style="width:120px;background-color:#EFEFEF" >
										<table style="font-size:12;">
											<tr>
												<td><input type="radio" class="youRadio_2" name="Radio_2" id="youRadio_2" value="1" style="cursor:pointer;"/></td>
												<td><label for="youRadio_2" style="cursor:pointer;">优</label></td>
											</tr>
											<tr>
												<td><input type="radio" class="liangRadio_2" name="Radio_2" id="liangRadio_2" value="2" style="cursor:pointer;"/></td>
												<td><label for="liangRadio_2" style="cursor:pointer;">良</label></td>
											</tr>
											<tr>
												<td><input type="radio" class="jgRadio_2" name="Radio_2" id="jgRadio_2" value="3" style="cursor:pointer;"/></td>
												<td><label for="jgRadio_2" style="cursor:pointer;">及格</label></td>
											</tr>
											<tr>
												<td><input type="radio" class="bjgRadio_2" name="Radio_2" id="bjgRadio_2" value="4" style="cursor:pointer;"/></td>
												<td><label for="bjgRadio_2" style="cursor:pointer;">不及格</label></td>
											</tr>
										</table>
									</div>
									<div region="center" title="第二学期">
										<table id="stufirstInfo" class="tablestyle" style="width:100%;height:348px;">
											<tr>
												<td class="titlestyle" align="center" style="height:290px;width:50px;">评语:</td>
												<td>
													<textarea name="PY_PY_2" id="PY_PY_2"  style="width:100%; height:338px;" maxlength="1000"></textarea>		
												</td>
											</tr>
										</table>
									</div>
								</div>
							</div>
							<div title="三年级" style="background-color:#EFEFEF;overflow:hidden">
								<table id="stufirstInfo" class="tablestyle" style="width:100%;">
									<tr>
										<td colspan="" align="left">
											<a href="#" id="baocun_3" class="easyui-linkbutton" plain="true" iconCls="icon-save" onclick="doToolbar(this.id);">保存</a>
										</td>
									</tr>
								</table>
								
								<div class="easyui-layout" style="width:100%; height:100%;">
									<!--dialog右边  -->
									<div region="west" title="第一学期" style="width:120px;background-color:#EFEFEF" >
										<table style="font-size:12;">
											<tr>
												<td><input type="radio" class="youRadio_3" name="Radio_3" id="youRadio_3" value="1" style="cursor:pointer;"/></td>
												<td><label for="youRadio_3" style="cursor:pointer;">优</label></td>
											</tr>
											<tr>
												<td><input type="radio" class="liangRadio_3" name="Radio_3" id="liangRadio_3" value="2" style="cursor:pointer;"/></td>
												<td><label for="liangRadio_3" style="cursor:pointer;">良</label></td>
											</tr>
											<tr>
												<td><input type="radio" class="jgRadio_3" name="Radio_3" id="jgRadio_3" value="3" style="cursor:pointer;"/></td>
												<td><label for="jgRadio_3" style="cursor:pointer;">及格</label></td>
											</tr>
											<tr>
												<td><input type="radio" class="bjgRadio_3" name="Radio_3" id="bjgRadio_3" value="4" style="cursor:pointer;"/></td>
												<td><label for="bjgRadio_3" style="cursor:pointer;">不及格</label></td>
											</tr>
										</table>
									</div>
									<div region="center" title="第二学期">
										<table id="stufirstInfo" class="tablestyle" style="width:100%;height:348px;">
											<tr>
												<td class="titlestyle" align="center" style="height:290px;width:50px;">评语:</td>
												<td>
													<textarea name="PY_PY_3" id="PY_PY_3"  style="width:100%; height:338px;" maxlength="1000"></textarea>		
												</td>
											</tr>
										</table>
									</div>
								</div>
							</div>
							
							<div title="四年级" style="background-color:#EFEFEF;overflow:hidden">
								<table id="stufirstInfo" class="tablestyle" style="width:100%;">
									<tr>
										<td colspan="" align="left">
											<a href="#" id="baocun_4" class="easyui-linkbutton" plain="true" iconCls="icon-save" onclick="doToolbar(this.id);">保存</a>
										</td>
									</tr>
								</table>
								
								<div class="easyui-layout" style="width:100%; height:100%;">
									<!--dialog右边  -->
									<div region="west" title="第一学期" style="width:120px;background-color:#EFEFEF" >
										<table style="font-size:12;">
											<tr>
												<td><input type="radio" class="youRadio_4" name="Radio_4" id="youRadio_4" value="1" style="cursor:pointer;"/></td>
												<td><label for="youRadio_4" style="cursor:pointer;">优</label></td>
											</tr>
											<tr>
												<td><input type="radio" class="liangRadio_4" name="Radio_4" id="liangRadio_4" value="2" style="cursor:pointer;"/></td>
												<td><label for="liangRadio_4" style="cursor:pointer;">良</label></td>
											</tr>
											<tr>
												<td><input type="radio" class="jgRadio_4" name="Radio_4" id="jgRadio_4" value="3" style="cursor:pointer;"/></td>
												<td><label for="jgRadio_4" style="cursor:pointer;">及格</label></td>
											</tr>
											<tr>
												<td><input type="radio" class="bjgRadio_4" name="Radio_4" id="bjgRadio_4" value="4" style="cursor:pointer;"/></td>
												<td><label for="bjgRadio_4" style="cursor:pointer;">不及格</label></td>
											</tr>
										</table>
									</div>
									<div region="center" title="第二学期">
										<table id="stufirstInfo" class="tablestyle" style="width:100%;height:348px;">
											<tr>
												<td class="titlestyle" align="center" style="height:290px;width:50px;">评语:</td>
												<td>
													<textarea name="PY_PY_4" id="PY_PY_4"  style="width:100%; height:338px;" maxlength="1000"></textarea>		
												</td>
											</tr>
										</table>
									</div>
								</div>
							</div>
						</div>
					
					   	<input type="hidden" id="active" name="active" />
					   	<input type="hidden" id="PY_XJH" name="PY_XJH" />
					   	<input type="hidden" id="PY_NJ" name="PY_NJ" />
					   	<input type="hidden" id="PY_PY" name="PY_PY" />
					   	<input type="hidden" id="PY_DD" name="PY_DD" />
					</form>
				</div>
 			</div>
 		</div>
 	</div>
	
</body>
<script type="text/javascript">
	var sAuth = '<%=sAuth%>';
	var authType = '<%=MyTools.StrFiltr(request.getParameter("authType"))%>';
  	var USERCODE="<%=MyTools.getSessionUserCode(request)%>";
	var iKeyCode = '';
	var pageNum = 1;   //datagrid初始当前页数
	var pageSize = 20; //datagrid初始页内行数
	
	var iKeyCode="";
	
	var stuXJH="";//学籍号
	
	$(document).ready(function(){
		initDialog();//初始化对话框
		initData();//页面初始化加载数据
	});
	
	/**页面初始化加载数据**/
	function initData(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Xspy',
			data : 'active=initData&page=' + pageNum + '&rows=' + pageSize + '&sAuth=' + sAuth,
			dataType:"json",
			success : function(data) {
				initCombobox(data[0].xData, data[0].zyData, data[0].xzbData);
				loadGrid();
			}
		});
	}
	
	
	function initDialog(){
		$('#DIA_Lead').dialog({
			width: 800,//宽度设置   
			height: 500,//高度设置 
			modal:true,
			closed: true,   
			cache: false, 
			draggable:false,//是否可移动dialog框设置
			/* toolbar:[{
				//导出
				text:"保存",
				iconCls:'icon-save',
				handler:function(){
					//传入save值进入doToolbar方法，用于保存
					doToolbar('save');
				}
			}], */
			//打开事件
			onOpen:function(data){
				
			},
			//读取事件
			onLoad:function(data){
			},
			//关闭事件
			onClose:function(data){
				$('#form1').form('clear');
				$('#tt').tabs('select','基础信息');
			}
		});
	}
	
	/**加载 datagrid控件，读取最外层页面信息
		@listData 列表数据
	**/
	function loadGrid(){
		$('#stuList').datagrid({
			url:'<%=request.getContextPath()%>/Svl_Xspy',
			/* queryParams:{"active":"queryRec","PY_XJH":encodeURI($('#PY_XJH_CX').val()),
				"PY_XM":encodeURI($('#PY_XM_CX').val()),"PY_XZBDM":$('#PY_XZBDM_CX').combobox('getValue'),
				"PY_XBDM":$('#PY_XBDM_CX').combobox('getValue'),"PY_ZYDM":$('#PY_ZYDM_CX').combobox('getValue'),
				"sAuth":sAuth
				}, */
				
			queryParams:{"active":"queryRec",
				"PY_XZBDM":$('#PY_XZBDM_CX').combobox('getValue'),
				"PY_XBDM":$('#PY_XBDM_CX').combobox('getValue'),"PY_ZYDM":$('#PY_ZYDM_CX').combobox('getValue'),
				"sAuth":sAuth
				},
			
			title:'班级列表',
			width:'100%',
			nowrap: false,
			fit:true, //自适应父节点宽度和高度
			showFooter:true,
			striped:true,
			pagination:true,
			pageSize:pageSize,
			singleSelect:true,
			pageNumber:pageNum,
			rownumbers:true,
			fitColumns: true,
					
			//下面是表单中加载显示的信息
			columns:[[
				/* {field:'学籍号',title:'学籍号',width:fillsize(0.15),align:'center'},
				{field:'班内学号',title:'学号',width:fillsize(0.1),align:'center'},
				{field:'姓名',title:'姓名',width:fillsize(0.1),align:'center'}, */
				{field:'系部名称',title:'系部名称',width:fillsize(0.3),align:'center'},
				{field:'行政班名称',title:'行政班名称',width:fillsize(0.3),align:'center'},
				{field:'专业名称',title:'专业名称',width:fillsize(0.3),align:'center'},
				/* {field:'类别名称',title:'学生状态',width:fillsize(0.1),align:'center'}, */
				{field:'AA_SET',align:'center',title:'操作',width:fillsize(0.1),formatter:function(value,rec){
					return "<input type='button' value='[评语]' onclick='setStuState(\"" + rec.学籍号 +"\",\"" + rec.行政班代码 +"\",\"" + rec.行政班名称 +"\")' id='setStuState' style='margin-left:5px; cursor:pointer;'>";
				}}
			]],
			onClickRow:function(rowIndex,rowData){ 
				 iKeyCode = rowData.学籍号;
			},
			//双击某行时触发
			onDblClickRow:function(rowIndex,rowData){
			},
			//成功加载时
			onLoadSuccess:function(data){//查询成功时候把后台的查询语句赋值给变量listsql
				iKeyCode = '';
			}
		});
	}
	
	
	
  	function setStuState(xjh,xzbdm,xzbmc){
  		//stuXJH = xjh;
  		loadBJTree(xzbdm);
  		
  		/* CheckStu(xjh);
  		SelectStu(xjh); */
  		$('#DIA_Lead').dialog('setTitle', xzbmc + " " + " 评语信息");
  		$('#DIA_Lead').dialog('open');
  	}
	
	/**加载combobox控件
		@jxxzData 下拉框数据
	**/
	function initCombobox(xData, zyData, xzbData){
		$('#PY_XZBDM_CX').combobox({
			data:xzbData,
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			panelHeight:'140', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data != ''){
					$(this).combobox('setValue', '');
				}
			},
			//下拉列表值改变事件
			onChange:function(data){}
		});
		
		$('#PY_XBDM_CX').combobox({
			data:xData,
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			panelHeight:'140', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data != ''){
					$(this).combobox('setValue', '');
				}
			},
			//下拉列表值改变事件
			onChange:function(data){}
		});
		
		$('#PY_ZYDM_CX').combobox({
			data:zyData,
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
	
	function doToolbar(iToolbar){
		//查询
		if(iToolbar == "que"){
			loadGrid();
		}
		
		if(iToolbar == "baocun_1" || iToolbar == "baocun_2" || iToolbar == "baocun_3" || iToolbar == "baocun_4"){
			var tab = $('#tt').tabs('getSelected');
			var index = $('#tt').tabs('getTabIndex',tab)+1;
			$('#active').val("updateStuComment");
			$('#PY_XJH').val(encodeURI(stuXJH));
			$('#PY_NJ').val(index);
			$('#PY_PY').val($('#PY_PY_'+index).val());//评语
			
			
			$('#PY_DD').val($("input[name='Radio_"+index+"']:checked").val());//等第
			$('#form1').submit();
		}
	}
	
	/**点击评语按钮检查是否存在**/
	function CheckStu(xjh){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Xspy',
			data : 'active=CheckStuRec&PY_XJH=' + xjh ,
			dataType:"json",
			success : function(data) {
				SelectStu(xjh);
			}
		});
	}
	
	/**点击评语按钮查询评语**/
	function SelectStu(xjh){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Xspy',
			data : 'active=selectStuComment&PY_XJH=' + xjh ,
			dataType:"json",
			success : function(data) {
				for(var i = 0;i<data[0].stuData.length;i++){
					$('#PY_PY_'+(data[0].stuData[i].年级)).val(data[0].stuData[i].评语);
					//alert("等第:"+data[0].stuData[i].等第);
					//alert("年级："+data[0].stuData[i].年级);
					if(data[0].stuData[i].等第==''){
						if(data[0].stuData[i].年级=='1'){
							$("input:radio[name=Radio_1]").attr("checked",false);
						}if(data[0].stuData[i].年级=='2'){
							$("input:radio[name=Radio_2]").attr("checked",false);
						}if(data[0].stuData[i].年级=='3'){
							$("input:radio[name=Radio_3]").attr("checked",false);
						}if(data[0].stuData[i].年级=='4'){
							$("input:radio[name=Radio_4]").attr("checked",false);
						}
					}else{
						$("input:radio[name='Radio_"+data[0].stuData[i].年级+"']").eq(data[0].stuData[i].等第-1).prop("checked",'checked');
					}
				}
			}
		});
	}
	
	
	/**表单提交**/
	$('#form1').form({
		//定位到servlet位置的url
		url:'<%=request.getContextPath()%>/Svl_Xspy',
		//当点击事件并成功提交后触发的事件
		success:function(data){
			var json  =  eval("("+data+")");
			if(json[0].MSG == '保存成功'){
				SelectStu(stuXJH);
				//提示信息
				showMsg(json[0].MSG);
			}else{
				alertMsg(json[0].MSG);
			}
		}
	});
	
	
	/**加载班级学生信息TREE**/
	//var checkObj = '';
	function loadBJTree(xzbdm){
		$('#classTree').tree({
			//checkbox: true,
			url:'<%=request.getContextPath()%>/Svl_Xspy?active=loadStuTree&PY_XZBDM=' + xzbdm,
		    onClick:function(node){
		    	if(node.id != null){
		    		//判断点击的班级是不是当前选中的班级
		    		$('#baocun_1').linkbutton('enable');
					$('#baocun_2').linkbutton('enable');
					$('#baocun_3').linkbutton('enable');
					$('#baocun_4').linkbutton('enable');
		    		
		    		stuXJH=node.id;
		    		CheckStu(node.id);
  					/* SelectStu(node.id); */
		    	}else{
		    		$('#baocun_1').linkbutton('disable');
					$('#baocun_2').linkbutton('disable');
					$('#baocun_3').linkbutton('disable');
					$('#baocun_4').linkbutton('disable');
		    	}
		    },
		    onCheck:function(node){
		    	/* checkObj = node.target;
				$(this).tree('expand', checkObj); */
		    },
			onDblClick:function(node){},
		    onBeforeExpand:function(node,param){
		    	$('#classTree').tree('options').url="<%=request.getContextPath()%>/Svl_Xspy?active=loadStuTree&parentCode=" + node.id + "&PY_XZBDM=" + xzbdm;
		    },
			onLoadSuccess:function(node, data){
				/* if(checkObj != ''){
					$(this).tree('check', checkObj);
					checkObj = '';
				} */
				$('#baocun_1').linkbutton('disable');
				$('#baocun_2').linkbutton('disable');
				$('#baocun_3').linkbutton('disable');
				$('#baocun_4').linkbutton('disable');
				$("#classTree").show();
			}
		});
	};
	
</script>
</html>