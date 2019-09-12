<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.base.common.tools.PublicTools"%>
<%@ page import="com.pantech.src.develop.store.user.*"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.*"%>
<%@ page import="com.pantech.base.common.db.DBSource"%>

<!--
	createTime:2015-12-10
	createUser:lupengfei
	description:教室课表
-->

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
<html>
<head>
<title>楼层课表</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- JQuery 专用4个文件 -->
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/icon.css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/publicScript.js"></script>
</head>

<style>
		#xlTime{
			width:100%;
			border-right:1px solid #99bbe8;
		}
		#xlTime{
			width:99.9%;
			border-top:1px solid #99bbe8;
		}
		#xlTime td{
			width:100px;
			border-left:1px solid #99bbe8;
			border-bottom:1px solid #99bbe8;
			text-align:center;
			empty-cells:show;
			font-size:11;
		}
		#xlTime th{
			width:100px;
			border-left:1px solid #99bbe8;
			border-bottom:1px solid #99bbe8;
			text-align:center;
			empty-cells:show;
		}
		#divPageMask2{background-color:#D2E0F2; filter:alpha(opacity=100);left:0px;top:0px;z-index:100;}
	
	</style>

<body>
	<div id="aa" class="easyui-layout" data-options="fit:true">
		<div data-options="region:'north',title:'教室课表',split:false,minimizable:false" style="height:112px;">
			<table width="100%">
				<tr>
					<td ><a href="#" onclick="doToolbar(this.id);" id="export"
						class="easyui-linkbutton"
						data-options="iconCls:'icon-submit',plain:true">导出</a></td>		
				</tr>
			</table>
			<table class="tablestyle" width="100%">
				<tr>
					<td class="titlestyle" width="15%">学年学期</td>
					<td width="25%"><select name="XNXQ" id="XNXQ" class="easyui-combobox" style="width:90%" panelHeight="auto">
					</select></td>
					<td class="titlestyle" width="15%">教学性质</td>
					<td width="25%"><select name="JXXZ" id="JXXZ" class="easyui-combobox" style="width:90%" panelHeight="auto">
					</select></td>
					
					<td width="15%" rowspan=2><a href="#" onclick="doToolbar(this.id);" id="search"
						class="easyui-linkbutton"
						data-options="iconCls:'icon-search',plain:true">查询</a></td>
				</tr>
				<tr>
					<td class="titlestyle" >校区</td>
					<td ><select name="school" id="school" class="easyui-combobox" style="width:90%" panelHeight="auto">
					</select></td>
					<td class="titlestyle" >星期</td>
					<td ><select name="week" id="week" class="easyui-combobox" style="width:90%" panelHeight="auto">
					</select></td>
<!-- 					<td class="titlestyle" >教学楼</td> -->
<!-- 					<td ><select name="house" id="house" class="easyui-combobox" style="width:90%" panelHeight="auto"> -->
<!-- 					</select></td> -->
<!-- 					<td class="titlestyle">楼层</td> -->
<!-- 					<td ><select name="floor" id="floor" class="easyui-combobox" style="width:90%" panelHeight="auto"> -->
<!-- 					</select></td> -->	
				</tr>
				
			</table>
		</div>
		<div id="index"	data-options="region:'center'" >
			<table id="xlTime" cellspacing="0" cellpadding="0" style="height:75%;border-collapse:collapse;"> 
					<tbody id="content"> 
							 
					</tbody> 
			</table>
			<%-- 遮罩层 --%>
	    	<div id="divPageMask2" style="position:absolute;top:0px;left:0px;width:100%;height:100%;" align="center">
	    		<div style="font-size:20px;margin-top:200px;">信息加载中请稍后!</div>
	    	</div>
		</div>
	</div>
	
	<!-- 课表导出页面 -->
	<div id="exportTimetable">
		<!--引入编辑页面用Ifram-->
		<iframe id="exportTimetableiframe" name="exportTimetableiframe" src='' style='width:100%; height:100%;' frameborder="0" scrolling="no"></iframe>
	</div>
	
	<a id="pageOfficeLink" href="#" style="display:none;"></a>
	
</body>
<script type="text/javascript">
	var iUSERCODE="<%=MyTools.getSessionUserCode(request)%>";
	var sAuth="<%=sAuth%>";
	var pofOpenType = '<%=MyTools.getProp(request, "Base.pofOpenType")%>';
	var pageNum = 1;   //datagrid初始当前页数
	var pageSize = 20; //datagrid初始页内行数
	
	$(document).ready(function(){
		//alert(sAuth);
		initData();
		initDialog();
		schoolCombobox2();
		//houseCombobox2();
		//floorCombobox();
		weekCombobox();
		loadTable();
	});

	function loadTable(){
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_Skjh',
				data : 'active=loadTable&school=' + $('#school').combobox('getValue') + '&week=' + $('#week').combobox('getValue') + '&xnxq=' + $('#XNXQ').combobox('getValue') + '&jxxz=' + $('#JXXZ').combobox('getValue'),
				dataType:"json",
				loadMsg : "信息加载中请稍后!",//载入时信息
				success : function(data) {
					//alert(data[0].行政班名称);	
					if(data[0].MSG==""){
						$('#divPageMask2').hide();
						alertMsg("查询不到符合条件的教室");
					}else{
						var classroom=data[0].MSG;
						var timedata=data[0].MSG2;
						var classname=data[0].BJMC;
						var info=data[0].VEC;
						var rooms=classroom.split(",");
						var times=timedata.split(",");
						var cname=classname.split(",");
						
						var count = 1;
						var idNum = "";
						
						var centerwidth=(rooms.length+1)*120;//单元格宽度
						if(centerwidth>1200){
							$('#xlTime').attr('style','width:'+centerwidth+';height:75%;border-collapse:collapse;');
						}
						
						var html="";
							html += '<tr align="center" style="height:30px;">'+
										'<th >'+
			// 								className1 +
										'</th>';
						for(var k=0;k<cname.length;k++){
							var rom=cname[k];
							if(rom=="0000"){
								rom="操场";
							}else if(rom=="0001"){
								rom="其它";
							}else if(rom=="0002"){
								rom="校外";
							}
							
							html +='<th >'+
										rom +
								   '</th>';
						}
						html+='</tr>';
						
						var html2="";	
							for(var i=0;i<times.length;i++){
								idNum = count+i;
								if(idNum < 10){
									idNum = ("0"+idNum);
								}
								html2+= '<tr align="center" style="height:45px;">'+
										'<th class="jc">'+
			 								times[i] +
										'</th>';
								for(var k=0;k<rooms.length;k++){
									html2+= '<td id="'+rooms[k]+idNum+'" class="pkxx" title="">'+
											'</td>';
								}							
								html2+=	'</tr>';
							}
						$('#content').html(html+html2);
						$.parser.parse(('#content'));
						
						for(var j=0;j<info.length;j++){
								for(var k=0;k<rooms.length;k++){	
									if(info.length!=1){
										if(info[j].实际场地编号.indexOf(rooms[k])>-1){
											$("#content td[id='"+rooms[k]+info[j].时间序列.substring(2,4)+"']").html(info[j].行政班名称+"<br />"+info[j].授课教师姓名);
											var clxx=info[j].行政班名称;
											var pkxx=info[j].课程名称;
											var jsxx=info[j].授课教师姓名;
											var cdxx=info[j].实际场地名称;
											pkxx=pkxx.replace(new RegExp('&amp;','gm'),'&');
											jsxx=jsxx.replace(new RegExp('&amp;','gm'),'&');
											cdxx=cdxx.replace(new RegExp('&amp;','gm'),'&');
											$("#content td[id='"+rooms[k]+info[j].时间序列.substring(2,4)+"']").attr('title',(clxx+"\r\n"+pkxx+"\r\n"+jsxx+"\r\n"+info[j].授课周次详情));
										}
									}
								}
						}
						$('#divPageMask2').hide();
					}
				}
			});
			
		
			
		}
				
		
		//页面初始化加载数据
		function initData(){
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_Bjgpjp',
				data : 'active=initData&page=' + pageNum + '&rows=' + pageSize+'&iUSERCODE='+iUSERCODE,
				dataType:"json",
				success : function(data) {
					xnxq = data[0].xnxqData;//获取学年学期下拉框数据
					jxxz = data[0].jxxzData;//获取教学性质下拉框数据
					initCombobox(xnxq,jxxz);//初始化下拉框
				}
			});
		}
		
		//加载下拉框数据
		function initCombobox(xnxq,jxxz){
			$('#XNXQ').combobox({
				data:xnxq,
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
			
			$('#JXXZ').combobox({
				data:jxxz,
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
		
		//加载下拉框数据
		function schoolCombobox2(){
			$('#school').combobox({
				url:"<%=request.getContextPath()%>/Svl_Skjh?active=schoolCombobox2"+"&iUSERCODE="+iUSERCODE+"&sAuth="+sAuth,
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
				onChange:function(data){
					
				}
			});
		}
		
// 		function houseCombobox2(){
// 			$('#house').combobox({
// 				url:"< %=request.getContextPath()%>/Svl_Skjh?active=houseCombobox2",
// 				valueField:'comboValue',
// 				textField:'comboName',
// 				editable:false,
// 				panelHeight:'140', //combobox高度
// 				onLoadSuccess:function(data){
					//判断data参数是否为空
// 					if(data != ''){
						//初始化combobox时赋值
// 						$(this).combobox('setValue', data[0].comboValue);
// 					}
										
// 				},
				//下拉列表值改变事件
// 				onChange:function(data){ 
					
// 				}
// 			});
// 		}
		
// 		function floorCombobox(){
// 			$('#floor').combobox({
// 				url:"< %=request.getContextPath()%>/Svl_Skjh?active=floorCombobox",
// 				valueField:'comboValue',
// 				textField:'comboName',
// 				editable:false,
// 				panelHeight:'140', //combobox高度
// 				onLoadSuccess:function(data){
					//判断data参数是否为空
// 					if(data != ''){
						//初始化combobox时赋值
// 						$(this).combobox('setValue', data[0].comboValue);
// 					}
										
// 				},
				//下拉列表值改变事件
// 				onChange:function(data){ 
					
// 				}
// 			});
// 		}
		
		function weekCombobox(){
			$('#week').combobox({
				url:"<%=request.getContextPath()%>/Svl_Skjh?active=weekCombobox",
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
				onChange:function(data){ 
					
				}
			});
		}
		
		//工具按钮
		function doToolbar(iToolbar){
			if(iToolbar=="search"){
				$('#divPageMask2').show();
				loadTable();
			}
			
			if(iToolbar == "export"){	
				var titleinfo=$('#XNXQ').combobox('getText')+" "+$('#school').combobox('getText')+" "+$('#week').combobox('getText');
				if(pofOpenType == 'normal'){ 
					$('#exportTimetable').dialog('open');	
					$("#exportTimetableiframe").attr("src","<%=request.getContextPath()%>/form/timetableQuery/exportClassTable.jsp?exportType=classTable&school=" + $('#school').combobox('getValue') + "&week=" + $('#week').combobox('getValue') + "&xnxq=" + $('#XNXQ').combobox('getValue') + "&jxxz=" + $('#JXXZ').combobox('getValue') +"&titleinfo="+encodeURI(encodeURI(titleinfo)) );
				}else{  
					$.ajax({
						type : "POST",
						url : '<%=request.getContextPath()%>/Svl_Kbcx',
						data : "active=loadTablePageOfficeLink&exportType=classTable&school=" + $('#school').combobox('getValue') + "&week=" + $('#week').combobox('getValue') + "&xnxq=" + $('#XNXQ').combobox('getValue') + "&jxxz=" + $('#JXXZ').combobox('getValue') +"&titleinfo="+encodeURI(encodeURI(titleinfo)),
						dataType:"json",
						success : function(data){ 
							$('#pageOfficeLink').attr('href', data[0].linkStr);
							document.getElementById("pageOfficeLink").click();
						}
					});
				}			
				
			}
		}
		
		/**加载 dialog控件**/
		function initDialog(){
			$('#exportTimetable').dialog({
				title:'导出预览',
				maximized:true,
				modal:true,
				closed: true,   
				cache: false, 
				draggable:false,//是否可移动dialog框设置
				//打开事件
				onOpen:function(data){},
				//读取事件
				onLoad:function(data){},
				//关闭事件
				onClose:function(data){
					$("#exportTimetableiframe").attr('src', '');
				}
			});
		}
		
		function closeDialog(){
			$('#exportTimetable').dialog('close');
		}
</script>
</html>