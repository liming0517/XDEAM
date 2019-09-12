<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="java.util.Vector"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.src.develop.store.user.*"%>
<%@ page import="com.pantech.base.common.tools.PublicTools"%>
<%
	/**
		创建人：翟旭超
		Create date: 2016.06.27
		功能说明：用于设置问卷信息
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
	<title>问卷信息列表</title>
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
	<div id="north1"  region="north" title="问卷信息" style="height:91px;" >
		<table>
			<tr>
				<td><a href="#" id="new" class="easyui-linkbutton" plain="true" iconcls="icon-add" onClick="doToolbar(this.id);" title="">新建</a></td>
				<td><a href="#" id="edit" class="easyui-linkbutton" plain="true" iconcls="icon-edit" onClick="doToolbar(this.id);" title="">编辑</a></td>
				<td><a href="#" id="del" class="easyui-linkbutton" plain="true" iconcls="icon-cancel" onClick="doToolbar(this.id);" title="">删除</a></td>
				<td><a href="#" id="copy" class="easyui-linkbutton" plain="true" iconcls="icon-save" onClick="doToolbar(this.id);" title="">复制</a></td>
			</tr>
		</table>
		<table id="ee" singleselect="true" width="100%" class="tablestyle">
			<tr>
				<td class="titlestyle">标题</td>
				<td>
					<input name="WW_BT_CX" id="WW_BT_CX" class="easyui-textbox" style="width:150px;">
				</td>
				<td class="titlestyle">学年学期名称</td>
				<td>
					<select name="WW_XNXQBM_CX" id="WW_XNXQBM_CX" class="easyui-combobox" style="width:150px;">
							
					</select>
				</td>
				<td class="titlestyle">
  					开始时间
  				</td>
				<td>
   					<input id="WW_KSSJ_CX" name="WW_KSSJ_CX" class="easyui-datebox" style="width:150px;" type="text" >
   				</td>
   				<td class="titlestyle">
  					结束时间
  				</td>
   				<td>
					<input id="WW_JSSJ_CX" name="WW_JSSJ_CX" class="easyui-datebox" style="width:150px;" type="text">
				</td>
   				<td  class="titlestyle" colspan="8" align="left" style="width: 12%; ">
					<a href="#" onclick="doToolbar(this.id)" id="search" class="easyui-linkbutton" iconcls = "icon-search" plain = "true">查询</a>
				</td>		
			</tr>
	    </table>
	</div>
	<div region="center">
		<table id = "formlist"  name= "formlist" width = "100%">
		</table>
		<form id="form6" name="form6" method='post'>
			<input type="hidden" name="active" id="active">
			<input id="WW_WJBH" name="WW_WJBH" type="hidden"> <!-- 问卷编号 -->
			<input id="WW_WJBH_CYM" name="WW_WJBH_CYM" type="hidden"> <!-- 问卷编号1 -->
		  	<input id="WW_WJLX_CYM" name="WW_WJLX_CYM" type="hidden"><!-- 问卷类型 -->
		  	 <input type="hidden" id="WW_ZYDM" name="WW_ZYDM"/>
		  	<input id="xztARRAY" name="xztARRAY" type="hidden">
		  	<input id="bdtARRAY" name="bdtARRAY" type="hidden">
		  	
		  	<input id="WW_GH" name="WW_GH" type="hidden">
		 	<input id="WW_XM" name="WW_XM" type="hidden"> 
			<input id="WW_XZBDM" name="WW_XZBDM" type="hidden">
			<input id="WW_XZBDM1" name="WW_XZBDM1" type="hidden"><!-- 之前的行政班代码 -->
			
			
		</form>
	</div>
	
	<!-- 问卷信息新建编辑 -->
	<div id="questionDialog" style="overflow:hidden;">
		<form id="form1" name="form1" method='post'>
			<table id="table1" name="table1" style="width:100%; height:100%;" class="tablestyle">
				<tr>
					<td class="titlestyle" >问卷编号</td>
					<td class="titlestyle" id="WJBH" ></td>
				</tr>
				<tr>
					<td class="titlestyle" >问卷类型</td>
					<td>
						<select name="WW_WJLX" id="WW_WJLX" style="width:205px;" class="easyui-combobox" panelHeight="auto" editable="false">
							<option id="" value="" >请选择</option>
							<option id="" value="01">单选题/多选题</option>
							<option id="" value="02">表单题1</option>
							<option id="" value="03">表单题2</option>
						</select>
					</td>
				</tr>
				<tr>
					<td class="titlestyle" >标题</td>
					<td>
						<input name="WW_BT" id="WW_BT" style="width:205px;" class="easyui-textbox" maxlength="500"></input>		
					</td>
				</tr>
				<tr>
					<td class="titlestyle" >学年学期名称</td>
					<td>
						<select name="WW_XNXQBM" id="WW_XNXQBM" style="width:205px;" class="easyui-combobox" panelHeight="auto" editable="false">
							
						</select>		
					</td>
				</tr>
				<tr>
					<td class="titlestyle" >开始时间</td>
					<td>
						<input name="WW_KSSJ" id="WW_KSSJ"  style="width:205px;" class="easyui-datebox"  maxlength="500"></input>		
					</td>
				</tr>
				<tr>
					<td class="titlestyle" >结束时间</td>
					<td>
						<input name="WW_JSSJ" id="WW_JSSJ" style="width:205px;" class="easyui-datebox"  maxlength="500"></input>		
					</td>
				</tr>
			</table>
			 <input type="hidden" name="active" id="active">
			 <input id="WW_WJBH1" name="WW_WJBH1" type="hidden">
		</form>
	</div> 
	<!-- 问卷里的选择题 -->
	 <div id="wjSetDialog">
		<div class="easyui-layout" style="width:100%; height:100%;">
			<div region="north" style="height:120px;">
				<table id="table2" name="table2" style="width:100%; height:100%;" class="tablestyle" >
					<tr>
						<td class="titlestyle" style="width:150px;">问卷编号</td>
						<td class="titlestyle" id="WJ_WJBH"></td>
					</tr>
					<tr>
						<td class="titlestyle">问卷类型</td>
							<td class="titlestyle" id="WJ_WJLX"></td>
					</tr>
					<tr>
						<td class="titlestyle">问卷标题</td>
						<td class="titlestyle" id="WJ_WJBT"></td>
					</tr>
					<tr>
						<td class="titlestyle">学年学期名称</td>
						<td class="titlestyle" id="WJ_XNXQMC"></td>
					</tr>
				</table>
			</div>
			<div region="center">
				<table id="optionList" name="optionList"></table>
			</div>
		</div>
	</div>
	
	<!-- 查询编辑选择题 -->
	<div id="CxxxtDialog" >
		<div class="easyui-layout" style="width:100%; height:100%;">
			<div region="center">
					<table id="optionList_CXXXT" name="optionList_CXXXT"></table>
			</div>
		</div>
	</div>
	
	<!-- 问卷里的表单题 -->
	 <div id="wjbdtSetDialog">
		<div class="easyui-layout" style="width:100%; height:100%;">
			<div region="north" style="height:120px;">
				<table id="table3" name="table3" style="width:100%; height:100%;" class="tablestyle" >
					<tr>
						<td class="titlestyle" style="width:150px;">问卷编号</td>
						<td class="titlestyle" id="WJ_WJBH_BDT"></td>
					</tr>
					<tr>
						<td class="titlestyle">问卷类型</td>
							<td class="titlestyle" id="WJ_WJLX_BDT"></td>
					</tr>
					<tr>
						<td class="titlestyle">问卷标题</td>
						<td class="titlestyle" id="WJ_WJBT_BDT"></td>
					</tr>
					<tr>
						<td class="titlestyle">学年学期名称</td>
						<td class="titlestyle" id="WJ_XNXQMC_BDT"></td>
					</tr>
				</table>
			</div>
			<div region="center">
				<table id="optionList_BDT" name="optionList_BDT"></table>
			</div>
		</div>
	</div>
	
	
	<!-- 查询编辑表单题 -->
	<div id="CxbdtDialog" >
		<div class="easyui-layout" style="width:100%; height:100%;" >
				<div region="center">
					<table id="optionList_CXBDT" style="width:100%;"></table>
				</div>
		</div>
	</div>
	
	<!-- 问卷里的辅导员   -->
	<div id="wjfdySetDialog">
		<div class="easyui-layout" style="width:100%; height:100%;">
		<div region="north" style="overflow:hidden;width:100%; height:213px;">
			<div style="height:120px;">
				<table id="table4" name="table4" style="width:100%; height:100%;" class="tablestyle" >
					<tr>
						<td class="titlestyle" style="width:150px;">问卷编号</td>
						<td class="titlestyle" id="WJ_WJBH_FDY"></td>
					</tr>
					<tr>
						<td class="titlestyle">问卷类型</td>
							<td class="titlestyle" id="WJ_WJLX_FDY"></td>
					</tr>
					<tr>
						<td class="titlestyle">问卷标题</td>
						<td class="titlestyle" id="WJ_WJBT_FDY"></td>
					</tr>
					<tr>
						<td class="titlestyle">学年学期名称</td>
						<td class="titlestyle" id="WJ_XNXQMC_FDY"></td>
					</tr>
				</table>
			</div>
			<div class="easyui-layout">
				<div id="north2" style="height:91px;" title="问卷辅导员列表" region="center">
					<table>
						<tr>
							<td><a href="#" id="saveOptionfdy" class="easyui-linkbutton" plain="true" iconcls="icon-add" onClick="doToolbar(this.id);" title="">添加</a></td>
							<td><a href="#" id="editOptionfdy" class="easyui-linkbutton" plain="true" iconcls="icon-edit" onClick="doToolbar(this.id);" title="">编辑</a></td>
							<td><a href="#" id="delOptionfdy" class="easyui-linkbutton" plain="true" iconcls="icon-cancel" onClick="doToolbar(this.id);" title="">删除</a></td>
						</tr>
					</table>
					<table id="ee2" width="585px" height="30px" class="tablestyle">
						<tr>
							<td  class="titlestyle">姓名</td>
							<td>
								<input name="WW_XM_FDY" id="WW_XM_FDY" class="easyui-textbox" style="width:180px;">
							</td>
							<td  class="titlestyle">行政班名称</td>
							<td>
								<input name="WW_XZBMC_FDY" id="WW_XZBMC_FDY" class="easyui-textbox" style="width:180px;">
							</td>
							<td style="width:100px;" align="center">
								<a href="#" onclick="doToolbar(this.id)" id="chaxun" class="easyui-linkbutton" plain="true" align="right" iconcls="icon-search">查询</a>
							</td>				
						</tr>
		    		</table>
		   		</div>
		   	</div>
		 </div>
		    	<div region="center">
					<table id = "optionList_FDY"  name= "optionList_FDY"  width = "100%">
					</table>
				</div>
		</div>
	</div>
	
	<!-- 查询编辑辅导员 -->
	<div id="CxbjfdyDialog" >
		<div class="easyui-layout" style="width:100%; height:100%;"  region="north" style="overflow:hidden;">
				<table id="table6" name="table6" style="width:100%;" class="tablestyle" >
					<tr>
						<td class="titlestyle" style="width:150px;">行政班名称</td>
						<td>
							<select name="WW_XZBMC" id="WW_XZBMC" style="width:300px;" class="easyui-combobox" panelHeight="auto" editable="false">
									
							</select>
						</td>
					</tr>
					<tr>
						<td class="titlestyle" style="width:150px;">辅导员姓名</td>
						<td>
							<select name="WW_FDYXM" id="WW_FDYXM" style="width:300px;" class="easyui-combobox" panelHeight="auto" editable="false">
									
							</select>
						</td>
					</tr>
				</table>
				 <input id="WW_WJBH_TJ" name="WW_WJBH_TJ" type="hidden"> 
				 <input id="WW_XNXQBM_TJ" name="WW_XNXQBM_TJ" type="hidden">
		</div>
	</div>
	
	<!--无限制专业列表 -->
	<div id="majorListDialog">
		<div class="easyui-layout" style="width:100%; height:100%;">
			<div region="north" style="height:34px;">
				<table>
					<tr>
						<td><a href="#" id="addlb" class="easyui-linkbutton" plain="true" iconcls="icon-add" onClick="doToolbar(this.id);">添加</a></td>
						<td><a href="#" id="dellb" class="easyui-linkbutton" plain="true" iconcls="icon-cancel" onClick="doToolbar(this.id);">删除</a></td>
					</tr>
				</table>
			</div>
			<div region="center">
				<table id="majorList" style="width:100%;"></table>
			</div>
		</div>
	</div>
	
	<!--专业设置 -->
	<div id="addMajorDialog">
		<div class="easyui-layout" style="width:100%; height:100%;">
			<div region="north" style="height:34px;">
				<table>
					<tr>
						<td><a href="#" id="savezysz" class="easyui-linkbutton" plain="true" iconcls="icon-save" onClick="doToolbar(this.id);">保存</a></td>
					</tr>
				</table>
			</div>
			<div region="center">
				<table style="width:100%;" class="tablestyle">
					<tr>
						<td class="titlestyle">学年学期名称</td>
						<td  class="titlestyle" id="xnxqmc_addMajorDialog">
						</td>
					</tr>
					<tr>
						<td  class="titlestyle">问卷名称</td>
						<td class="titlestyle" id="wjmc_addMajorDialog">
						</td>
					</tr>
					<tr>
						<td class="titlestyle">专业名称</td>
						<td>
							<select name="ZYDM" id="ZYDM" class="easyui-combobox">
							</select>
						</td>
					</tr>
			    </table>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
	var row = '';      //行数据
	var iKeyType='';//类型
	var iKeyCode = ''; //定义主键
	var pageNum = 1;   //datagrid初始当前页数
	var pageSize = 20; //datagrid初始页内行数
	var USERCODE="<%=MyTools.getSessionUserCode(request)%>";
	var xztARRAY = new Array();
	var bdtARRAY = new Array();
	var fdyARRAY=new Array();
	var wjdcInfo = '';
	
	var openType = '';//判断辅导员是新增还是修改
	
	var iKeyCidefdy='';
	var iKeyCidexzbbm='';
	var iKeyCidexzbmc='';
	var iKeyCidefdyxm='';//辅导员姓名
	var iKeyCidefdygh='';//辅导员工号
	
	$(document).ready(function(){
		initDialog();//初始化对话框
		loadGrid();
		initCombobox();//下拉框赋值
	});
	
	/**加载 datagrid控件，读取最外层页面信息
		@listData 列表数据
	**/
		function loadGrid(){
				$('#formlist').datagrid({
					url:'<%=request.getContextPath()%>/Svl_Wjsz',
					queryParams:{"active":"query",
								"WW_BT":encodeURI($('#WW_BT_CX').val()),
								"WW_XNXQBM":encodeURI($('#WW_XNXQBM_CX').combobox('getValue')),
								"WW_KSSJ":encodeURI($('#WW_KSSJ_CX').datebox('getValue')),
								"WW_JSSJ":encodeURI($('#WW_JSSJ_CX').datebox('getValue'))
					},
					title:'问卷信息列表',
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
						{field:'问卷编号',title:'问卷编号',width:fillsize(0.4),align:'center'},
						{field:'标题',title:'标题',width:fillsize(0.7),align:'center'},
						{field:'学年学期名称',title:'学年学期名称',width:fillsize(0.5),align:'center'},
						{field:'开始时间',title:'开始时间',width:fillsize(0.3),align:'center'},
						{field:'结束时间',title:'结束时间',width:fillsize(0.3),align:'center'},
						{field:'AA_SET',align:'center',title:'操作',width:fillsize(0.4),formatter:function(value,rows){
							var str = '';
						    if(rows.问卷类型 == '01'){
						    	str = "<input type='button' value='题目设置' style='cursor:pointer;width:60px;' onclick='openSetDialog(\"" + rows.问卷编号 + "\",\""+rows.问卷类型+"\",\""+rows.问卷类型名称+"\",\""+rows.标题+"\",\""+rows.学年学期名称+"\",\""+rows.开始时间+"\",\""+rows.结束时间+"\")'>";
						    }
								
							if(rows.问卷类型 == '02'){
								str = "<input type='button' value='设置' style='cursor:pointer;width:60px;' onclick='openSetDialog(\"" + rows.问卷编号 + "\",\""+rows.问卷类型+"\",\""+rows.问卷类型名称+"\",\""+rows.标题+"\",\""+rows.学年学期名称+"\",\""+rows.开始时间+"\",\""+rows.结束时间+"\")'>";
							}
								
							if(rows.问卷类型 == '03'){
								str = "<input type='button' value='题目设置' style='cursor:pointer;width:60px;' onclick='openSetDialog(\"" + rows.问卷编号 + "\",\""+rows.问卷类型+"\",\""+rows.问卷类型名称+"\",\""+rows.标题+"\",\""+rows.学年学期名称+"\",\""+rows.开始时间+"\",\""+rows.结束时间+"\")'>"+
										"&nbsp;<input type='button' value='辅导员设置' style='cursor:pointer;width:70px;' onclick='openSetDialog1(\"" + rows.问卷编号 + "\",\""+rows.问卷类型+"\",\""+rows.问卷类型名称+"\",\""+rows.标题+"\",\""+rows.学年学期编码+"\",\""+rows.学年学期名称+"\",\""+rows.开始时间+"\",\""+rows.结束时间+"\")'>";
							}
							return str;
						}
					}
					]],
					onClickRow:function(rowIndex,rowData){ 
						iKeyCode = rowData.问卷编号;
						row = rowData;
						iKeyType=rowData.问卷类型; 
						wjdcInfo = rowData;
					},
					//双击某行时触发
					onDblClickRow:function(rowIndex,rowData){
							doToolbar("edit");
					},
					//成功加载时
					onLoadSuccess:function(data){//查询成功时候把后台的查询语句赋值给变量listsql
						iKeyCode = "";
						iKeyType=="";
						row = ''; 
						wjdcInfo = "";
						
					}
			});
		}
		/**加载问卷里的选择题题目 datagrid控件，读取页面信息
		@listData 列表数据
		**/
		function loadGridWJ(){
				$('#optionList').datagrid({
					url:'<%=request.getContextPath()%>/Svl_Wjsz',
					queryParams:{"active":"queryWJXZT",
							"WW_WJBH":encodeURI($('#WJ_WJBH').html())
					},
					title:'问卷题目列表',
					width:'100%',
					nowrap: false,
					fit:true, //自适应父节点宽度和高度
					showFooter:true,
					striped:true,
					pagination:true,
					pageSize:pageSize,
					singleSelect:false,
					pageNumber:pageNum,
					rownumbers:true,
					fitColumns: true,
					toolbar:[{
						//添加
						text:'添加',
						iconCls:'icon-add',
						handler:function(){
							//传入save值进入doToolbar方法，用于保存
							doToolbar('saveOption');
						}
						},
						{
						//删除
						text:'删除',
						iconCls:'icon-cancel',
						handler:function(){
							//传入save值进入doToolbar方法，用于保存
							doToolbar('delOption');
						} 
				}],
					//下面是表单中加载显示的信息
					columns:[[
						{field:'ck',checkbox:true},
						{field:'题目编号',title:'题目编号',width:fillsize(0.35),align:'center'
                        },
                        {field:'题目类型名称',title:'题目类型',width:fillsize(0.35),align:'center'
                        },
						{field:'题目内容',title:'题目内容',width:fillsize(0.8),align:'center'
						}
						
					]],
					onClickRow:function(rowIndex,rowData,field){
						row = rowData;
						
					},
					//双击某行时触发
					onDblClickRow:function(rowIndex,rowData){
							
					},
					//成功加载时
					onLoadSuccess:function(data){//查询成功时候把后台的查询语句赋值给变量listsql
						row = '';
					}
			});
		}
		/**加载问卷里的题目 datagrid控件，读取页面信息
			把选择题添加到问卷
		@listData 列表数据
		**/
		function loadGridWJCXXXT(){		
			$('#optionList_CXXXT').datagrid({
				url:'<%=request.getContextPath()%>/Svl_Wjsz',
				queryParams:{"active":"querySyxzt","WW_WJBH":$('#WW_WJBH').val()
				},
				//title:'选择题题目列表',
				width:'100%',
				nowrap: false,
				fit:true, //自适应父节点宽度和高度
				showFooter:true,
				striped:true,
				pagination:true,
				pageSize:pageSize,
				singleSelect:false,
				pageNumber:pageNum,
				rownumbers:true,
				fitColumns: true,
				//下面是表单中加载显示的信息
				columns:[[
					{field:'ck',checkbox:true},
					{field:'题目编号',title:'题目编号',width:fillsize(0.28),align:'center'},
	                {field:'题目类型名称',title:'题目类型',width:fillsize(0.2),align:'center'},
					{field:'题目内容',title:'题目内容',width:fillsize(0.5),align:'center'}
					
				]],
				onSelect:function(rowIndex,rowData){
						xztARRAY.push(rowData.题目编号);
				},
				//取消勾选时触发
				onUnselect:function(rowIndex,rowData){
					 $.each(xztARRAY, function(key,value){
						if(value == rowData.题目编号){
							xztARRAY.splice(key, 1);
						}
					}); 
				},
				//选择全部时触发
				onSelectAll:function(rows){
					 xztARRAY.length = 0;
					for(var i=0; i<rows.length; i++){
						xztARRAY.push(rows[i].题目编号);
					} 
				},
				//取消选择全部时触发
				onUnselectAll:function(rows){
					xztARRAY.length = 0;
				},
				//双击某行时触发
				onDblClickRow:function(rowIndex,rowData){},
				//读取datagrid之前加载
				onBeforeLoad:function(){},
				//单击某行时触发
				onClickRow:function(rowIndex,rowData){},
				//加载成功后触发
				onLoadSuccess: function(data){}
			}); 
		}
		
		/**加载问卷里的题目 datagrid控件，读取页面信息
			把表单题添加到问卷
		@listData 列表数据
		**/
		 function loadGridWJCXBDT(){
			$('#optionList_CXBDT').datagrid({
				url:'<%=request.getContextPath()%>/Svl_Wjsz',
				queryParams:{"active":"querySybdt","WW_WJBH":$('#WW_WJBH').val()
				},
				//title:'表单题题目列表',
				width:'100%',
				nowrap: false,
				fit:true, //自适应父节点宽度和高度
				showFooter:true,
				striped:true,
				pagination:true,
				pageSize:pageSize,
				singleSelect:false,
				pageNumber:pageNum,
				rownumbers:true,
				fitColumns: true,
			//下面是表单中加载显示的信息
			columns:[[
				{field:'ck',checkbox:true},
				{field:'题目编号',title:'题目编号',width:fillsize(0.2),align:'center'},
                {field:'题目类型名称',title:'题目类型',width:fillsize(0.15),align:'center'},
				{field:'题目内容',title:'题目内容',width:fillsize(0.35),align:'center'},
				{field:'题目要点',title:'题目要点',width:fillsize(0.2),align:'center'},
				{field:'分值',title:'分值',width:fillsize(0.1),align:'center'}
			]],
			onSelect:function(rowIndex,rowData){
					bdtARRAY.push(rowData.题目编号);
			},
			//取消勾选时触发
			onUnselect:function(rowIndex,rowData){
				 $.each(bdtARRAY, function(key,value){
					if(value == rowData.题目编号){
						bdtARRAY.splice(key, 1);
					}
				}); 
			},
			//选择全部时触发
			onSelectAll:function(rows){
				 bdtARRAY.length = 0;
				for(var i=0; i<rows.length; i++){
					bdtARRAY.push(rows[i].题目编号);
				} 
			},
			//取消选择全部时触发
			onUnselectAll:function(rows){
				bdtARRAY.length = 0;
			},
			//双击某行时触发
			onDblClickRow:function(rowIndex,rowData){},
			//读取datagrid之前加载
			onBeforeLoad:function(){},
			//单击某行时触发
			onClickRow:function(rowIndex,rowData){},
			//加载成功后触发
			onLoadSuccess: function(data){}
		}); 
	}
	
	/**加载问卷里的表单题 datagrid控件，读取页面信息 **/
	function loadGridWJBDT(){
		$('#optionList_BDT').datagrid({
			url:'<%=request.getContextPath()%>/Svl_Wjsz',
			queryParams:{"active":"queryWJBDT",
					"WW_WJBH":encodeURI($('#WJ_WJBH_BDT').html())
			},
			title:'问卷题目列表',
			width:'100%',
			nowrap: false,
			fit:true, //自适应父节点宽度和高度
			showFooter:true,
			striped:true,
			pagination:true,
			pageSize:pageSize,
			singleSelect:false,
			pageNumber:pageNum,
			rownumbers:true,
			fitColumns: true,
			toolbar:[{
				 //新建
				text:'添加',
				iconCls:'icon-add',
				handler:function(){
					//传入save值进入doToolbar方法，用于保存
					doToolbar('saveOptionbdt');
				}
			},{
				//删除
				text:'删除',
				iconCls:'icon-cancel',
				handler:function(){
					//传入save值进入doToolbar方法，用于保存
					doToolbar('delOptionbdt');
				} 
			}],
			//下面是表单中加载显示的信息
			columns:[[
				{field:'ck',checkbox:true},
				{field:'题目编号',title:'题目编号',width:fillsize(0.2),align:'center'},
                   {field:'题目类型名称',title:'题目类型',width:fillsize(0.15),align:'center'},
				{field:'题目内容',title:'题目内容',width:fillsize(0.35),align:'center'},
				{field:'题目要点',title:'题目要点',width:fillsize(0.2),align:'center'},
                   {field:'分值',title:'分值',width:fillsize(0.1),align:'center'}
			]],
			onClickRow:function(rowIndex,rowData,field){ 
				row = rowData;
			},
			//双击某行时触发
			onDblClickRow:function(rowIndex,rowData){
			},
			//成功加载时
			onLoadSuccess:function(data){//查询成功时候把后台的查询语句赋值给变量listsql
				row = '';
			}
		});
	}
		
	/**加载问卷里的所有辅导员 datagrid控件，读取页面信息*/
	function loadGridWJFDY(){		
		$('#optionList_FDY').datagrid({
			url:'<%=request.getContextPath()%>/Svl_Wjsz',
			queryParams:{"active":"querySyfdy",
						"WW_XM":$('#WW_XM_FDY').val(),	
						"WW_XZBMC":$('#WW_XZBMC_FDY').val(),
						"WW_WJBH":$('#WJ_WJBH_FDY').html()
			},
			//title:'问卷辅导员列表',
			title:'',
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
				{field:'工号',title:'工号',width:fillsize(0.3),align:'center'},
                      {field:'姓名',title:'姓名',width:fillsize(0.2),align:'center'},
				{field:'行政班名称',title:'行政班名称',width:fillsize(0.5),align:'center'}
			]],
			onClickRow:function(rowIndex,rowData,field){ 
				iKeyCidefdy=rowData.题目编号;
				iKeyCidexzbbm=rowData.行政班代码;
				row = rowData;
				iKeyCidexzbmc=rowData.行政班名称;
				iKeyCidefdyxm=rowData.姓名;
				iKeyCidefdygh=rowData.工号;
				
			},
			//双击某行时触发
			onDblClickRow:function(rowIndex,rowData){
					
			},
			//成功加载时
			onLoadSuccess:function(data){//查询成功时候把后台的查询语句赋值给变量listsql
				iKeyCidefdy="";
				iKeyCidexzbbm="";
				iKeyCidexzbmc="";
				iKeyCidefdyxm="";
				row = '';
			}
		});
	}
		
	/**加载datagrid控件，读取页面信息
		无限制专业信息列表
	**/						
	function loadMajorListData(id){
		$('#majorList').datagrid({
			url: '<%=request.getContextPath()%>/Svl_Wjsz',
			queryParams:{'active':'queMajorList','WW_WJBH':id},
			title:'无限制专业信息列表',
			width:'100%',
			nowrap: false,
			fit:true, //自适应父节点宽度和高度
			showFooter:true,
			striped:true,
			pagination:true,
			pageSize:20,
			pageNumber:1,
			singleSelect:false,
			rownumbers:true,
			fitColumns: true,
			columns:[[
				{field:'ck',checkbox:true},
				{field:'专业代码',title:'专业代码',width:fillsize(0.4),align:'center'},
				{field:'专业名称',title:'专业名称',width:fillsize(0.6),align:'center'}
			]],
			//双击某行时触发
			onDblClickRow:function(rowIndex,rowData){},
			//读取datagrid之前加载
			onBeforeLoad:function(){},
			//单击某行时触发
			onClickRow:function(rowIndex,rowData){},
			//加载成功后触发
			onLoadSuccess: function(data){}
		});
	};
		
		
		/**加载 dialog控件**/
	 function initDialog(){
	 	//界面上点击新建编辑的界面
		$('#questionDialog').dialog({   
			width: 310,//宽度设置   
			height: 240,//高度设置 
			resizable:true,//大小
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
					doToolbar('save');
				}
			}], 
			//打开事件
			onOpen:function(data){},
			//读取事件
			onLoad:function(data){},
			//关闭事件
			onClose:function(data){
			}
		});
		
		//题目设置选择题dialog
		$('#wjSetDialog').dialog({
			title:'问卷题目设置', 
			width: 800,//宽度设置   
			height: 600,//高度设置 
			resizable:true,//大小
			modal:true,
			closed: true, 
			cache: false, 
			draggable:true,//是否可移动dialog框设置
			//打开事件
			onOpen:function(data){},
			//读取事件
			onLoad:function(data){},
			//关闭事件
			onClose:function(data){
				$('#WJ_WJBH').html('');
				$('#WJ_WJLX').html('');
				$('#WJ_WJBT').html('');
			}
		});
		
		//添加选择题列表
		$('#CxxxtDialog').dialog({
			title:'问卷题目设置(选择题)', 
			width: 800,//宽度设置   
			height: 600,//高度设置 
			resizable:true,//大小
			modal:true,
			closed: true, 
			cache: false, 
			draggable:true,//是否可移动dialog框设置
			toolbar:[{
				 //保存
				text:'保存',
				iconCls:'icon-save',
				handler:function(){
					//传入save值进入doToolbar方法，用于保存
					doToolbar('addXzt');
				}
			}],
			//打开事件
			onOpen:function(data){},
			//读取事件
			onLoad:function(data){},
			//关闭事件
			onClose:function(data){
			}
		});
		
		//查询所有表单题 	
		$('#CxbdtDialog').dialog({
			title:'问卷题目设置(表单题)', 
			width: 800,//宽度设置   
			height: 600,//高度设置 
			resizable:true,//大小
			modal:true,
			closed: true, 
			cache: false, 
			draggable:true,//是否可移动dialog框设置
			toolbar:[{
				 //保存
				text:'保存',
				iconCls:'icon-save',
				handler:function(){
					//传入save值进入doToolbar方法，用于保存
					doToolbar('saveBdt');
				}
			}],
			//打开事件
			onOpen:function(data){},
			//读取事件
			onLoad:function(data){},
			//关闭事件
			onClose:function(data){
			}
		});
		
		//问卷里的表单题
		$('#wjbdtSetDialog').dialog({
			title:'问卷题目设置', 
			width: 800,//宽度设置   
			height: 600,//高度设置 
			resizable:true,//大小
			modal:true,
			closed: true, 
			cache: false, 
			draggable:true,//是否可移动dialog框设置
			
			//打开事件
			onOpen:function(data){},
			//读取事件
			onLoad:function(data){},
			//关闭事件
			onClose:function(data){
				$('#WJ_WJBH_BDT').html('');
				$('#WJ_WJLX_BDT').html('');
				$('#WJ_WJBT_BDT').html('');
			}
		});
		
		//查询所有辅导员	
		$('#wjfdySetDialog').dialog({
				title:'问卷辅导员设置', 
				width: 600,//宽度设置   
				height: 600,//高度设置 
				resizable:true,//大小
				modal:true,
				closed: true, 
				cache: false, 
				draggable:true,//是否可移动dialog框设置
				
				//打开事件
				onOpen:function(data){},
				//读取事件
				onLoad:function(data){},
				//关闭事件
				onClose:function(data){
				}
		});
		
		//查询编辑辅导员
		$('#CxbjfdyDialog').dialog({
			title:'添加辅导员', 
			width: 450,//宽度设置   
			height: 120,//高度设置 
			resizable:true,//大小
			modal:true,
			closed: true, 
			cache: false, 
			draggable:true,//是否可移动dialog框设置
			toolbar:[{
			 //新建
				text:'保存',
				iconCls:'icon-save',
				handler:function(){
					//传入save值进入doToolbar方法，用于保存
					doToolbar('saveBJFDY');
				}
			}],
			//打开事件
			onOpen:function(data){},
			//读取事件
			onLoad:function(data){},
			//关闭事件
			onClose:function(data){
			}
		});
		
		//专业无限制dialog
		$('#majorListDialog').dialog({   
			title: '专业列表',   
			width: 600,//宽度设置   
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
			}
		});
		 
		//专业无限制dialog新增
		$('#addMajorDialog').dialog({   
			title: '添加专业',   
			width: 358,//宽度设置   
			height: 147,//高度设置 
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
	
	/**打开题目设置对话框**/
	function openSetDialog(id,type,name, biaoti, xnxqmc, kssj, jssj){
	//单选多选
		if(type == '01'){
			$('#WJ_WJBH').html(id);
			$('#WJ_WJLX').html(name);
			$('#WJ_XNXQMC').html(xnxqmc);
			if($('#WJ_XNXQMC').html()=='undefined'||$('#WJ_XNXQMC').html()==''||$('#WJ_XNXQMC').html()==null){
				$('#WJ_XNXQMC').html('无');
			}
			$('#WJ_WJBT').html(biaoti);
			loadGridWJ();
			$('#wjSetDialog').dialog('open'); 
		}
		if(type == '03'){
		//表单题
			$('#WJ_WJBH_BDT').html(id);
			$('#WJ_WJLX_BDT').html(name);
			$('#WJ_XNXQMC_BDT').html(xnxqmc);
			if($('#WJ_XNXQMC_BDT').html()=='undefined'||$('#WJ_XNXQMC_BDT').html()==''||$('#WJ_XNXQMC_BDT').html()==null){
				$('#WJ_XNXQMC_BDT').html('无');
			}
			$('#WJ_WJBT_BDT').html(biaoti);
			loadGridWJBDT();
			$('#wjbdtSetDialog').dialog('open');
		}
		//设置按钮
		if(type=='02'){
			loadMajorListData(id);
			$('#majorListDialog').dialog('open');
		}
		
	}
	
	function openSetDialog1(id,type,name, biaoti,xnxqbm,xnxqmc, kssj, jssj){
		//辅导员
		$('#WJ_WJBH_FDY').html(id);
		$('#WJ_WJLX_FDY').html(name);
		$('#WJ_XNXQMC_FDY').html(xnxqmc);
		if($('#WJ_XNXQMC_FDY').html()=='undefined'||$('#WJ_XNXQMC_FDY').html()==''||$('#WJ_XNXQMC_FDY').html()==null){
			$('#WJ_XNXQMC_FDY').html('无');
		}
		$('#WJ_WJBT_FDY').html(biaoti);
		$('#WW_XNXQBM_TJ').val(xnxqbm);
		
		loadGridWJFDY();
		$('#wjfdySetDialog').dialog('open');
	}
	
		
	function doToolbar(iToolbar){
		//最外层界面查询
		if(iToolbar == "search"){
			loadGrid();
		}
		//最外层新建按钮
		if (iToolbar=="new"){
			$('#WJBH').html('系统自动生成');
			$('#WW_WJLX').combobox('enable', true);
			$('#WW_WJLX').combobox('setValue', '');
			$('#WW_XNXQBM').combobox('setValue', '');
			$('#WW_BT').textbox('setValue','');
			$('#WJBH').css('color', '#CCCCCC');
			$('#WW_KSSJ').datebox('setValue','');
			$('#WW_JSSJ').datebox('setValue','');
			$('#questionDialog').dialog('setTitle','新建');
			$('#questionDialog').dialog('open'); 
		}
		//最外层界面新建编辑保存按钮
		if (iToolbar=="save"){
			if($('#WW_WJLX').combobox('getText')=='请选择'){
				$('#WW_WJLX').combobox('setValue','');
			}
			if($('#WW_WJLX').combobox('getText')=='单选题/多选题'){
				$('#WW_WJLX').combobox('setValue','01');
			}
			if($('#WW_WJLX').combobox('getText')=='表单题1'){
				$('#WW_WJLX').combobox('setValue','02');
			}
			if($('#WW_WJLX').combobox('getText')=='表单题2'){
				$('#WW_WJLX').combobox('setValue','03');
			}
			if($('#WW_WJLX').combobox('getValue')==''){
				alertMsg("请选择问卷类型");
				return;
			}
			if($('#WW_BT').val()==""){
				alertMsg("请填写标题");
				return;
			}
			if($('#WW_XNXQBM').combobox('getValue')==""){
				alertMsg("请选择学年学期名称");
				return;
			}
			if($('#WW_KSSJ').datebox('getValue')==""){
				alertMsg("请选择开始时间");
				return;
			}
			if($('#WW_JSSJ').datebox('getValue')==""){
				alertMsg("请选择结束时间");
				return;
			}
			//开始时间小于结束时间
			if($('#WW_KSSJ').datebox('getValue')>$('#WW_JSSJ').datebox('getValue')){
				alertMsg("开始时间必须小于结束时间");
				return;
			}
				$('#WW_WJLX').combobox('getValue');
				$('#WW_XNXQBM').combobox('getValue');
				$('#questionDialog #active').val("save");
				$('#WW_WJBH1').val($('#WJBH').html());
				if($('#WW_WJBH1').val()=='系统自动生成'){
					$('#WW_WJBH1').val('');
				}
				$('#form1').submit();
		}
		//最外层编辑按钮
		if(iToolbar == "edit"){
			if(iKeyCode==""){
				alertMsg("请选择一条数据");
			}else{
				$('#WJBH').html(iKeyCode);
				$('#WJBH').css('color', '#000000');
				$('#WW_WJLX').combobox('setText', row.问卷类型名称);
				
				$('#WW_BT').textbox('setValue',row.标题);
				$('#WW_XNXQBM').combobox('setValue', row.学年学期编码);
				$('#WW_KSSJ').datebox('setValue',row.开始时间);
				$('#WW_JSSJ').datebox('setValue',row.结束时间);
				$('#questionDialog').dialog('setTitle','编辑');
				$('#WW_WJLX').combobox('disable', true);
				$('#questionDialog').dialog('open');
			}
		}
		//最外层删除按钮
		if(iToolbar=="del"){
			if(iKeyCode==""){
				alertMsg("请选择一条数据");
				return;
			}else{
					ConfirmMsg("删除问卷后将会删除所有相关信息，是否确定要删除?", "Del()", "");
				 }
		}
		//复制按钮
		if(iToolbar=="copy"){
			if(iKeyCode==""){
				alertMsg("请选择一条数据");
			}else{
				if(row.问卷类型=='01'||row.问卷类型=='03'){
					$('#WW_WJBH_CYM').val(row.问卷编号);
					$('#WW_WJLX_CYM').val(row.问卷类型);
					$('#active').val("Adddxdxbdt");
					$('#WW_WJBH').val('');
					
					ConfirmMsg("您是否确认复制", "Copy()", "");
				}
				if(row.问卷类型=='02'){
					$('#WW_WJBH_CYM').val(row.问卷编号);
					$('#WW_WJLX_CYM').val(row.问卷类型);
					$('#active').val("Adddxdxbdt");
					$('#WW_WJBH').val('');
					
					ConfirmMsg("您是否确认复制当前选择的问卷？", "Copy2()", "");
				}
			}
		}
		//选择题问卷题目列表添加
		if(iToolbar=="saveOption"){
			$('#WW_WJBH').val($('#WJ_WJBH').html());
			loadGridWJCXXXT();
			$('#CxxxtDialog').dialog('open');
		}
		//添加选择题
		if(iToolbar=="addXzt"){
			if(xztARRAY==""){
				alertMsg("请选择题目");
			}
			else{
				$('#xztARRAY').val(xztARRAY);
				$('#active').val("AddWjtm");
				$('#WW_WJBH').val($('#WJ_WJBH').html());
				$('#form6').submit();
			} 
		}
		//问卷里的选择题删除按钮
		if(iToolbar=="delOption"){
			var majorData1 = $('#optionList').datagrid('getSelections');
				
			if(majorData1.length == 0){
				alertMsg('请至少选择一个条数据');
				return;
			}
				
			var majorCode1 = '';
			for(var i=0; i<majorData1.length; i++){
				majorCode1 += majorData1[i].题目编号+',';
			}
			majorCode1 = majorCode1.substring(0, majorCode1.length-1);
			$('#WW_WJBH').val($('#WJ_WJBH').html());
			ConfirmMsg('是否确定要删除已选择的题目？', 'delMajor1("' + majorCode1 + '");', '');
		}
		//新增表单题
		if(iToolbar=="saveBdt"){
			if(bdtARRAY==""){
				alertMsg("请选择题目");
			}
			else{
				$('#bdtARRAY').val(bdtARRAY);
				$('#active').val("AddWjtmbdt");
				$('#WW_WJBH').val($('#WJ_WJBH_BDT').html());
				$('#form6').submit();
				bdtARRAY.length=0;
			} 
		}
		
		//添加表单题按钮
		if(iToolbar=="saveOptionbdt"){
			$('#WW_WJBH').val($('#WJ_WJBH_BDT').html());
			loadGridWJCXBDT();
			$('#CxbdtDialog').dialog('open');
		}
		//表单题删除
		if(iToolbar=="delOptionbdt"){
			var majorData1 = $('#optionList_BDT').datagrid('getSelections');
					
			if(majorData1.length == 0){
				alertMsg('请至少选择一个条数据');
				return;
			}
					
			var majorCode1 = '';
			for(var i=0; i<majorData1.length; i++){
				majorCode1 += majorData1[i].题目编号+',';
			}
			majorCode1 = majorCode1.substring(0, majorCode1.length-1);
			$('#WW_WJBH').val($('#WJ_WJBH_BDT').html());
			ConfirmMsg('是否确定要删除选择的专业？', 'delMajor1("' + majorCode1 + '");', '');
		}
		
		//辅导员设置里的查询 
		if(iToolbar=="chaxun"){
			loadGridWJFDY();
		}
		
		//问卷辅导员列表里的添加
		if(iToolbar=="saveOptionfdy"){
			openType='new';
			
			iKeyCidexzbbm = '';
			$('#WW_WJBH_TJ').val($('#WJ_WJBH_FDY').html());
			BDXXBMC();
			$('#WW_FDYXM').combobox('setValue','');
			$('#CxbjfdyDialog').dialog('setTitle','添加辅导员');
			$('#CxbjfdyDialog').dialog('open');
		}
		//编辑问卷里的辅导员
		if(iToolbar=="editOptionfdy"){
			if(iKeyCidefdy==""){
				alertMsg("请选择一条数据");
				return
			}else{
				openType='edit';
				
				$('#WW_XZBDM1').val(iKeyCidexzbbm);
				$('#WW_WJBH_TJ').val($('#WJ_WJBH_FDY').html());
			
				$('#WW_FDYXM').combobox('setValue',iKeyCidefdygh);
				BDXXBMC();//行政班名称
				$('#CxbjfdyDialog').dialog('setTitle','编辑辅导员');
				
				$('#CxbjfdyDialog').dialog('open');
			}
		}
		//保存辅导员
		if(iToolbar=="saveBJFDY"){ 
			if(openType =='new'){
				if($('#WW_XZBMC').combobox('getValue')==""||$('#WW_XZBMC').combobox('getValue')==null){
					alertMsg("请选择行政班名称");
					return;
				}
				if($('#WW_FDYXM').combobox('getValue')==""||$('#WW_FDYXM').combobox('getValue')==null){
					alertMsg("请选择辅导员");
					return;
				}
					$('#active').val("AddBJFDY");
					var gh=$('#WW_FDYXM').combobox('getValue');
					$('#WW_GH').val(gh);
					$('#WW_XM').val($('#WW_FDYXM').combobox('getText'));
					$('#WW_XZBDM').val($('#WW_XZBMC').combobox('getValue'));
					$('#WW_WJBH').val($('#WJ_WJBH_FDY').html());
					openType='';
					$('#form6').submit();
			}
			
			if(openType =='edit'){
				if($('#WW_XZBMC').combobox('getValue')==""||$('#WW_XZBMC').combobox('getValue')==null){
					alertMsg("请选择行政班名称");
					return;
				}
				if($('#WW_FDYXM').combobox('getValue')==""||$('#WW_FDYXM').combobox('getValue')==null){
					alertMsg("请选择辅导员");
					return;
				}
				
				$('#active').val("ModRecFDY");
					var gh=$('#WW_FDYXM').combobox('getValue');
					$('#WW_GH').val(gh);
					$('#WW_XM').val($('#WW_FDYXM').combobox('getText'));
					$('#WW_XZBDM').val($('#WW_XZBMC').combobox('getValue'));
					$('#WW_WJBH').val($('#WJ_WJBH_FDY').html());
					openType='';
					$('#form6').submit();
			}
		}
		
		//辅导员删除
		if(iToolbar=="delOptionfdy"){
			if(iKeyCidefdy==""){
				alertMsg("请选择一条数据");
				return
			}else{
				$('#WW_XZBDM').val(iKeyCidexzbbm);
				ConfirmMsg("删除后无法恢复，您是否确认删除", "DelFDY()", "");
			}
		}
		
		//专业无限制
		//添加专业
		if(iToolbar== 'addlb'){
			$('#xnxqmc_addMajorDialog').html(wjdcInfo.学年学期名称);
			$('#wjmc_addMajorDialog').html(wjdcInfo.问卷类型名称);
			ZYDMcombobox();
		}
		
		//保存专业
		if(iToolbar == 'savezysz'){
			var selMajor = $('#ZYDM').combobox('getValues');
			if(selMajor.length==1 && selMajor[0]==''){
				alertMsg('请至少选择一个专业');
				return;
			}
			
			$('#active').val('savezy');   
			$('#WW_WJBH').val(wjdcInfo.问卷编号);
			$('#WW_ZYDM').val(selMajor.toString());
			
			$('#form6').submit();
		}
		
		//删除专业
		if(iToolbar == 'dellb'){
			var majorData = $('#majorList').datagrid('getSelections');
			
			if(majorData.length == 0){
				alertMsg('请至少选择一个专业');
				return;
			}
			
			var majorCode = '';
			for(var i=0; i<majorData.length; i++){
				majorCode += majorData[i].专业代码+',';
			}
			majorCode = majorCode.substring(0, majorCode.length-1);
			
			ConfirmMsg('是否确定要删除选择的专业？', 'delMajor("' + majorCode + '");', '');
		}
	}
	
		/**删除选择题*/
		function delMajor1(majorCode1){
			
			$('#active').val('delDXDXT'); 
			$('#WW_ZYDM').val(majorCode1.toString());
			$('#form6').submit();
		}
		
		/**删除专业*/
		function delMajor(majorCode){
			$('#active').val('delzy'); 
			$('#WW_WJBH').val(wjdcInfo.问卷编号);
			$('#WW_ZYDM').val(majorCode.toString());
			$('#form6').submit();
		}
		
	function initCombobox(){
		//学年学期下拉框
		$('#WW_XNXQBM_CX').combobox({
				url:'<%=request.getContextPath()%>/Svl_Wjsz?active=XNXQMCcombobox', 
				valueField:'comboValue',
				textField:'comboName',
				editable:false,
				panelHeight:'100',
				onLoadSuccess:function(data){
				if(data!=""){
				$(this).combobox('setValue','');
				}
			}
		});
		//新建编辑页面的学年学期名称下拉框
		$('#WW_XNXQBM').combobox({
				url:'<%=request.getContextPath()%>/Svl_Wjsz?active=XNXQMCcombobox',
				valueField:'comboValue',
				textField:'comboName',
				editable:false,
				selectOnNavigation:true,
				panelHeight:'100',
				onLoadSuccess:function(data){
				if(data!=""){
					$(this).combobox('setValue','');
					}
				} 
			});
			
			$('#WW_FDYXM').combobox({
				url:'<%=request.getContextPath()%>/Svl_Wjsz?active=FDYXMcombobox&WW_WJBH_TJ='+$('#WW_WJBH_TJ').val(),
				valueField:'comboValue',
				textField:'comboName',
				editable:true,
				selectOnNavigation:true,
				panelHeight:'100',
				onLoadSuccess:function(data){
					if(data!=""){
						$(this).combobox('setValue','');
					}
				} 
			});
		}
	
	//专业名称下拉框
		function ZYDMcombobox(){
			$('#ZYDM').combobox({
				url:'<%=request.getContextPath()%>/Svl_Wjsz?active=loadMajorCombo&WW_WJBH=' + wjdcInfo.问卷编号,
				valueField:'comboValue',
				textField:'comboName',
				multiple:true,
				editable:false,
				width:250,
				panelHeight:'140', //combobox高度
				onLoadSuccess:function(data){
					//判断data参数是否为空
					if(data != ''){
						//初始化combobox时赋值
						$(this).combobox('setValue', '');
					}
					
					$('#addMajorDialog').dialog('open');
				},
				onSelect:function(record){
					var data = $(this).combobox('getValues');
					
					if(data.length > 1){
						$(this).combobox('unselect', '');
					}
				},
				onUnselect:function(record){
					var data = $(this).combobox('getValues');
					
					if(data.length == 0){
						$(this).combobox('setValue', '');
					}
				}
			});
		}
	
	//用来绑定行政班名称combobox
  		function BDXXBMC(){
  			$('#WW_XZBMC').combobox({
				url:'<%=request.getContextPath()%>/Svl_Wjsz?active=XZBMCcombobox&WW_WJBH_TJ='+$('#WW_WJBH_TJ').val()+'&WW_XNXQBM_TJ='+$('#WW_XNXQBM_TJ').val()+'&WW_XZBDM='+iKeyCidexzbbm,
				valueField:'comboValue',
				textField:'comboName',
				editable:true,
				selectOnNavigation:true,
				panelHeight:'100',
				onLoadSuccess:function(data){
					if(data!=""){
						$(this).combobox('setValue',iKeyCidexzbbm);
					}
				} 
			});
  		}
		
	//用来做删除问卷提交
  		function Del(){
  			$.ajax({
  				type:"post",
  				url:'<%=request.getContextPath()%>/Svl_Wjsz',  
  				data:"active=del&WW_WJBH="+iKeyCode+"&WW_WJLX="+iKeyType,   //设置模式
  				dataType:'json',
  				success:function(datas){
  					var data=datas[0];
  						showMsg(data.MSG);
  						loadGrid();
  				},
  				error:function(){
  				
  				}
  			});
  		}
  		
  		//用来做删除问卷里的辅导员提交
  		function DelFDY(){
  			$.ajax({
  				type:"post",
  				url:'<%=request.getContextPath()%>/Svl_Wjsz',  
  				data:"active=Delfdy&WW_XZBDM="+$('#WW_XZBDM').val()+"&WW_WJBH="+$('#WJ_WJBH_FDY').html(),   //设置模式   
  				dataType:'json',
  				success:function(datas){
  					var data=datas[0];
  						showMsg(data.MSG);
  						loadGridWJFDY();
  				},
  				error:function(){
  				
  				}
  			});
  		}
  		
  		//复制按钮测试(单选多选表单题)
  		function Copy(){
  			$.ajax({
  				type:"post",
  				url:'<%=request.getContextPath()%>/Svl_Wjsz',  
  				data:"active=Adddxdxbdt&WW_WJBH="+$('#WW_WJBH').val()+
  						"&WW_WJBH_CYM="+$('#WW_WJBH_CYM').val()+
  						"&WW_WJLX="+$('#WW_WJLX_CYM').val(),
  				dataType:'json',
  				success:function(datas){
  					var data=datas[0];
						showMsg(data.MSG);
						$('#active').val("");
						loadGrid();
  				},
  				error:function(){
  				
  				}
  			});
  		}
  		
	//复制按钮测试(专业无限制表)
	function Copy2(){
		$.ajax({
			type:"post",
			url:'<%=request.getContextPath()%>/Svl_Wjsz',  
			data:"active=Addzyjswxzb&WW_WJBH="+$('#WW_WJBH').val()+
					"&WW_WJBH_CYM="+$('#WW_WJBH_CYM').val()+
					"&WW_WJLX="+$('#WW_WJLX_CYM').val(),   
			dataType:'json',
			success:function(datas){
				var data=datas[0];
					showMsg(data.MSG);
					$('#active').val("");
					loadGrid();
			},
			error:function(){
			
			}
		});
	}
	
	//编辑保存提交(最外层页面)
	$('#form1').form({
		url:'<%=request.getContextPath()%>/Svl_Wjsz',
		onSubmit: function(){
		},
		success:function(data){
			var json  =  eval("("+data+")");
			if(json[0].MSG == '保存成功'){
				//提示信息
				showMsg(json[0].MSG);
				loadGrid();
				$('#active').val("");
				$('#questionDialog').dialog('close');
			}else{
				alertMsg(json[0].MSG);
			}
		}
	});
	
	/**表单提交**/
	$('#form6').form({
		url:'<%=request.getContextPath()%>/Svl_Wjsz',
		onSubmit: function(){
		},
		success:function(data){
			var json  =  eval("("+data+")");
			//添加选择题
			if($('#active').val() == 'AddWjtm'){
				if(json[0].MSG == '保存成功'){
				//提示信息
					showMsg(json[0].MSG);
					$('#active').val("");
					loadGridWJ();
					$('#CxxxtDialog').dialog('close');
				}else{
					alertMsg(json[0].MSG);
				}
				xztARRAY.length = 0;
			}
			//删除单选多选表单题
			if($('#active').val() == 'delDXDXT'){
				if(json[0].MSG == '删除成功'){
					showMsg(json[0].MSG);
					loadGridWJ();
					loadGridWJBDT();
				}else{
					alertMsg(json[0].MSG);
				}
			}
			//添加表单题
			if($('#active').val() == 'AddWjtmbdt'){
				if(json[0].MSG == '保存成功'){
				//提示信息
				showMsg(json[0].MSG);
				$('#active').val("");
				loadGridWJBDT();
				$('#CxbdtDialog').dialog('close');
				}else{
					alertMsg(json[0].MSG);
				}
			}
			
			//添加辅导员
			if($('#active').val() == 'AddBJFDY'){
				if(json[0].MSG == '保存成功'){
				//提示信息
					showMsg(json[0].MSG);
					$('#active').val("");
					loadGridWJFDY();
					$('#CxbjfdyDialog').dialog('close');
				}else{
					alertMsg(json[0].MSG);
				}
			}
			
			//添加专业
			if($('#active').val() == 'savezy'){
				if(json[0].MSG == '保存成功'){
					showMsg(json[0].MSG);
					loadMajorListData(wjdcInfo.问卷编号);
					$('#addMajorDialog').dialog('close');
				}else{
					alertMsg(json[0].MSG);
				}
			}
			
			//删除专业
			if($('#active').val() == 'delzy'){
				if(json[0].MSG == '删除成功'){
					showMsg(json[0].MSG);
					loadMajorListData(wjdcInfo.问卷编号);
				}else{
					alertMsg(json[0].MSG);
				}
			}
			
			//编辑辅导员信息
			if($('#active').val() == 'ModRecFDY'){
				if(json[0].MSG == '保存成功'){
					showMsg(json[0].MSG);
					$('#active').val("");
					loadGridWJFDY();
					$('#CxbjfdyDialog').dialog('close');
				}else{
					alertMsg(json[0].MSG);
				}
			}
		}
	});
</script>
</html>