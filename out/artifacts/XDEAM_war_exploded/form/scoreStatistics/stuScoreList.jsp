<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.base.common.tools.PublicTools"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="com.pantech.src.develop.Logs.*"%>
<%@ page import="com.pantech.base.common.tools.*"%>
<%@ page import="com.pantech.src.develop.store.user.*"%>
<%@ page import="com.pantech.src.develop.manage.workremind.WorkRemind"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.*"%>
<%@ page import="com.pantech.base.common.db.DBSource"%>

<!--
	createTime:2016-7-6
	createUser:lupengfei
	description:学生成绩查询
 -->
<%
UserProfile up = new UserProfile(request,MyTools.getSessionUserCode(request)); 
String userName =up.getUserName();
String userCode =up.getUserCode();
%>
<%
		/*
			获得角色信息
		*/
		
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
		sAuth = MyTools.StrFiltr(v.get(0));
		// 如果无法获取人员信息，则自动跳转到登陆界面
%>
<html>
<head>
<title>学生成绩查询</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- JQuery 专用4个文件 -->
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/icon.css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
</head>
<body>
	<div id="aa" class="easyui-layout" data-options="fit:true">
		<div data-options="region:'north',title:'学生成绩查询',split:false,minimizable:false"
			style="height:80px;">
			<table class="tablestyle" width="100%">
				<tr>
					<td class="titlestyle">专业名称</td>
					<td><select id="ZYDM" name="ZYDM" class="easyui-combobox" style="width:250px;"></select></td>
					<td class="titlestyle">行政班名称</td>
					<td><select name="XZBDM" id="XZBDM" class="easyui-combobox" style="width:250px;" panelHeight="auto">
					</select></td>
					<td rowspan=2><a href="#" onclick="doToolbar(this.id);" id="search"
						class="easyui-linkbutton"
						data-options="iconCls:'icon-search',plain:true">查询</a></td>
					
				</tr>
				<tr>
					<td class="titlestyle">学号</td>
					<td><select id="XH" name="XH" class="easyui-textbox" style="width:250px;"></select></td>
					<td class="titlestyle">姓名</td>
					<td><select name="XM" id="XM" class="easyui-textbox" style="width:250px;" >
					</select></td>
				</tr>				
			</table>
		</div>
		<div id="index" data-options="region:'center',split:false,border:false">
			<table class="easyui-datagrid" id="List" ></table>
		</div>

	</div>
	
	<div id="studentInfo" style="position:absolute;width:100%;height:100%;">
		<!--引入编辑页面用Ifram-->
		<iframe id="studentInfoFrame" name="studentInfoFrame" src='' style='width:100%; height:100%;' frameborder="0" scrolling="no"></iframe>
	</div>
	
</body>
<script type="text/javascript">
	var iUSERCODE="<%=MyTools.getSessionUserCode(request)%>";
	var sAuth="<%=sAuth%>";  //角色权限
	var userName="<%=userName%>";  //用户名
	var pageNum = 1;   //datagrid初始当前页数
	var pageSize = 20; //datagrid初始页内行数
	var xnxqVal="";
	var xzbdmVal="";
	
	$(document).ready(function(){
		loadCombobox();
		initDialog();
		$('#studentInfo').hide();
	});

	function loadGrid(){
		$('#List').datagrid({
				url:'<%=request.getContextPath()%>/Svl_ScoreQuery',
				queryParams:{"active":"loadStudent","ZYDM":$('#ZYDM').combobox('getValue'),"XZBDM":$('#XZBDM').combobox('getValue'),"XH":$('#XH').textbox('getValue'),"XM":$('#XM').textbox('getValue'),"sAuth":sAuth},
				loadMsg : "信息加载中请稍侯!",
				fit:true,
				width:'100%',
				nowrap:false,
				striped:true,
				pageSize:50,
				pageNumber:1,
				showFooter:true,
				rownumbers:true,
				singleSelect:true,
				pagination:true,
				fitColumns:true,
				columns:[[
					{field:'xh',title:'学号',width:fillsize(0.08),align:'center'},
					{field:'xm',title:'姓名',width:fillsize(0.08),align:'center'},
					{field:'行政班名称',title:'班级',width:fillsize(0.12),align:'center'},
					{field:'col',title:'查询',width:60,align:'center',
						formatter:function(value,rec){
							var ksccap="";
							ksccap='<input id="'+rec.xh+'" type="button" value="[成绩]" style="cursor:pointer;" onclick="openScore(\''+rec.xh+'\',\''+rec.xm+'\');" />';
							return ksccap;
						}
					}
				]],
				onClickRow:function(rowIndex,rowData){
				},
				//双击某行时触发
				onDblClickRow:function(rowIndex,rowData){
				},
				//成功加载时
				onLoadSuccess : function(data) {

				}
			});					
	}
		
	function loadGrid2(stuid){
		$('#List').datagrid({
				url:'<%=request.getContextPath()%>/Svl_ScoreQuery',
				queryParams:{"active":"loadScore","XNXQBM":$('#XNXQ').combobox('getValue'),"XZBDM":$('#XZBDM').combobox('getValue'),"iUSERCODE":stuid},
				loadMsg : "信息加载中请稍侯!",
				fit:true,
				width:'100%',
				nowrap:false,
				striped:true,
				pageSize:20,
				pageNumber:1,
				showFooter:true,
				rownumbers:true,
				singleSelect:true,
				pagination:false,
				fitColumns:true,
				columns:[[
					{field:'课程名称',title:'课程',width:fillsize(0.12),align:'center'},
					{field:'总评',title:'总评成绩',width:fillsize(0.08),align:'center',
						formatter:function(value,rec){
							var cj="";
							if(rec.总评==""){
								cj="--";
							}else{
								cj=rec.总评;
							}
							return cj;
						}
					},
					{field:'重修1',title:'重修成绩1',width:fillsize(0.08),align:'center',
						formatter:function(value,rec){
							var cj="";
							if(rec.重修1==""){
								cj="--";
							}else{
								cj=rec.重修1;
							}
							return cj;
						}
					},
					{field:'重修2',title:'重修成绩2',width:fillsize(0.08),align:'center',
						formatter:function(value,rec){
							var cj="";
							if(rec.重修2==""){
								cj="--";
							}else{
								cj=rec.重修2;
							}
							return cj;
						}
					},
					{field:'补考',title:'补考',width:fillsize(0.08),align:'center',
						formatter:function(value,rec){
							var cj="";
							if(rec.补考==""){
								cj="--";
							}else{
								cj=rec.补考;
							}
							return cj;
						}
					},
					{field:'大补考',title:'大补考',width:fillsize(0.08),align:'center',
						formatter:function(value,rec){
							var cj="";
							if(rec.大补考==""){
								cj="--";
							}else{
								cj=rec.大补考;
							}
							return cj;
						}
					},
					{field:'学年学期编码',title:'学期',width:fillsize(0.08),align:'center',
						formatter:function(value,rec){
							var xnxqbm=rec.学年学期编码;
							var xnxq=xnxqbm.substring(0,4)+"学年第"+xnxqbm.substring(4,5)+"学期";
							return xnxq;
						}
					}
					
				]],
				onClickRow:function(rowIndex,rowData){
				},
				//双击某行时触发
				onDblClickRow:function(rowIndex,rowData){
				},
				//成功加载时
				onLoadSuccess : function(data) {

				}
			});					
	}	
		
		//加载下拉框数据
		function loadCombobox(){
			$('#XZBDM').combobox({
				url:"<%=request.getContextPath()%>/Svl_ScoreQuery?active=XZBDMCombobox"+"&iUSERCODE="+iUSERCODE+"&sAuth="+sAuth,
				valueField:'comboValue',
				textField:'comboName',
				editable:false,
				panelHeight:'140', //combobox高度
				onLoadSuccess:function(data){
					//判断data参数是否为空
					if(data != ''){
						//初始化combobox时赋值
						$(this).combobox('setValue', data[1].comboValue);
						xzbdmVal=$('#XZBDM').combobox('getValue');
						if(sAuth=="99"){//学生
							
						}else{
							loadGrid();
						}
					}
				},
				//下拉列表值改变事件
				onChange:function(data){ 
					
				}
			});
			
			$('#ZYDM').combobox({
				url:"<%=request.getContextPath()%>/Svl_ScoreQuery?active=ZYDMCombobox",
				valueField:'comboValue',
				textField:'comboName',
				editable:false,
				panelHeight:'140', //combobox高度
				onLoadSuccess:function(data){
					//判断data参数是否为空
					if(data != ''){
						//初始化combobox时赋值
						$(this).combobox('setValue', data[0].comboValue);
						
						
					}
				},
				//下拉列表值改变事件
				onChange:function(data){}
			});
			
			
		}		
		
		//工具按钮
		function doToolbar(iToolbar){
			if(iToolbar=="search"){
				if(sAuth=="99"){//学生
					
				}else{
				    if($('#ZYDM').combobox('getValue')==""&&$('#XZBDM').combobox('getValue')==""&&$('#XH').textbox('getValue')==""&&$('#XM').textbox('getValue')==""){
				   		alertMsg("查询条件不能都为空");
				   		return;
				    }else{
				   	 	loadGrid();
				    }
					
				}
			}	
			if(iToolbar=="return"){
				$('#aa').show();
				$('#aa').css('top','0px');
				$('#studentInfo').hide();
				$("#studentInfoFrame").attr("src","");
			}
		}
		
		function openScore(stuid,name){	
			$('#aa').css('top','1600px');
			$('#studentInfo').show();
			$('#studentInfo').css('top','0px');
			$("#studentInfoFrame").attr("src","<%=request.getContextPath()%>/form/scoreStatistics/stuScoreInfo2.jsp?listid="+stuid+"&listname="+encodeURI(encodeURI(name))+"&listtype=2" );
		}
		
		
		
		
</script>
</html>