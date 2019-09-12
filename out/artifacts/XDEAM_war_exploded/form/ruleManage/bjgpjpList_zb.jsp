<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%
	/**
		创建人：wangzh
		Create date: 2015.06.05
		功能说明：用于设置班级固排禁排
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
		<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/icon.css">
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
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
			width:300px;
			border-left:1px solid #99bbe8;
			border-bottom:1px solid #99bbe8;
			text-align:center;
			empty-cells:show;
			font-size:11;
		}
		#xlTime th{
			width:300px;
			border-left:1px solid #99bbe8;
			border-bottom:1px solid #99bbe8;
			text-align:center;
			empty-cells:show;
		}
	</style>
	<body class="easyui-layout">
		<div data-options="region:'north'" title="" style = "height:32px;width:100%;">
			<table  class = "tablestyle" width = "100%">
				<tr>
					<td>
						<a href="#" onclick="doToolbar(this.id);" id="import"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-site'">选修课导入</a>
						<a href="#" onclick="doToolbar(this.id);" id="zongbiao"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-site'">班级固排总表</a>
					</td>
				</tr>
			</table>
		</div>
		<div data-options="region:'center'" style="overflow-x:hidden;">
		
			<div id="setTime9" style="overflow:hidden;">
				<form id='fm9' method='post' style="margin: 0px;padding-top: 5px;">
					<table id="xlTime9" cellspacing="0" cellpadding="0" style="height:74%;border-collapse:collapse;"> 						
						<tbody id="content9"> 
							
						</tbody> 
					</table>
					<input type="hidden" id="active" name="active"/>
					<input type="hidden" id="GG_XZBDM" name="GG_XZBDM"/>
					<input type="hidden" id="GG_XNXQBM" name="GG_XNXQBM"/>
					<input type="hidden" id="GG_SJXL" name="GG_SJXL"/>
					<input type="hidden" id="KCJS" name="KCJS"/>
					<input type="hidden" id="SJXL" name="SJXL"/>
					<input type="hidden" id="CDYQ" name="CDYQ"/>
					<input type="hidden" id="CDMC" name="CDMC"/>
					<input type="hidden" id="SAVETYPE" name="SAVETYPE"/>
					<input type="hidden" id="GG_SKJHMXBH" name="GG_SKJHMXBH"/>
				</form>
			</div>
			
			
		</div>
		

		
	</body>
	<script type="text/javascript">
		var iKeyCode = "";//授课计划明细编号
		var pageNum = 1;   //datagrid初始当前页数
		var pageSize = 20; //datagrid初始页内行数
		//var xkmc = "";//学科名称下拉框数据
		//var rkjs = "";//任课教师下拉框数据
		var kcmc = "";//课程名称
		var jsxm = "";//教师姓名
		var skzcxq = "";//授课周次详情
		var cdyq = "";//场地要求
		var cdmc = "";//场地名称
		var zjs = "";//总节数
		var gjs = "";//固排已排节数
		var lj = "";//连节
		var lc = "";//连次
		var lcs= "";//已排连次次数
		var aod= "";//判断添加或删除
		var checkrec="";
		var delinfo = "";
		var classId1 = "";
		var xnxqVal1 = "";
		var jxxzVal1 = "";
		var weeks1="";
		
		//var lastIndex = -1;//使datagrid选中行取消，以便下次选择从新开始
		//var saveType = "";//判断打开窗口的操作（new or edit）
		
		
		$(document).ready(function(){
			//initDialog();
			//initGridData(classId1,xnxqVal1,jxxzVal1);//页面初始化加载数据
			
			loadTime();
		});
		
		//页面初始化加载数据
		function initGridData(classId,xnxqVal,jxxzVal){
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_Bjgpjp',
				data : 'active=initGridData&page=' + pageNum + '&rows=' + pageSize + '&XZBDM=' + classId + '&XNXQ=' + xnxqVal + '&JXXZ=' + jxxzVal,
				dataType:"json",
				success : function(data) {
					//xkmc = data[0].xkmcData;//获取学科名称下拉框数据
					//rkjs = data[0].rkjsData;//获取任课教师下拉框数据					
					loadTime(data[0].timeData,data[0].gpjpData);
					loadGrid(data[0].listData);//加载特殊规则列表
					//initCombobox(xkmc,rkjs);//初始化下拉框
					if(jsxm==""||skzcxq==""||weeks1==""||cdyq==""){
		
					}else{
						checkTeaCls(jsxm,skzcxq,weeks1,cdyq,iKeyCode,xnxqVal1+jxxzVal1,classId);
					}
				}
			});
		}
		
		function loadGrid(listData){
			$('#list').datagrid({
				data:listData,
				loadMsg : "信息加载中请稍后!",//载入时信息
				rownumbers: true,
				animate:true,
				striped : true,//隔行变色
				pageSize : pageSize,//每页记录数
				singleSelect : true,//单选模式
				pageNumber : pageNum,//当前页码
				pagination:false,
				fit:false,
				fitColumns: true,//设置边距
				columns:[[
					{field:'课程名称',title:'科目名称',width:100},
					{field:'授课教师姓名',title:'任课教师',width:100},
					{field:'GS_CDMC',title:'场地要求',width:100},
					{field:'GS_GPYPJS',title:'已排节数',width:50},
					{field:'GS_MPJS',title:'未排节数',width:50},
					{field:'GS_SKZCXQ',title:'授课周次详情',width:50,
						formatter:function(value,rec){
							var skzcxq2=rec.GS_SKZCXQ;
							skzcxq2=skzcxq2.replace(new RegExp('odd','gm'),'单周');
							skzcxq2=skzcxq2.replace(new RegExp('even','gm'),'双周');
							return skzcxq2;
						}
					}
				]],
				onClickRow:function(rowIndex, rowData){
					//lastIndex = rowIndex;
					iKeyCode = rowData.授课计划明细编号;
					//iKeyCodeZb = rowData.授课计划主表编号;
					kcmc = rowData.课程名称;
					jsxm = rowData.授课教师姓名;
					skzcxq = rowData.GS_SKZCXQ;
					lj = rowData.GS_LJ;
					lc = rowData.GS_LC;
					cdyq=rowData.GS_CDYQ;
					cdmc=rowData.GS_CDMC;
					//row=rowData;
					classId1 = "";	
					xnxqVal1 = "";	
					jxxzVal1 = "";	
					weeks1 = "";				
					checkTeaCls(jsxm,skzcxq,weeks1,cdyq,iKeyCode,xnxqVal1+jxxzVal1,classId1);
				},
				onLoadSuccess: function(data){
					//parentId=window.parent.document.frames["left"].parentId;
					//if(parentId==""){
					//	$('#new').linkbutton('disable');
					//	$('#edit').linkbutton('disable');
					//}else{
					//	$('#new').linkbutton('enable');
					//	$('#edit').linkbutton('enable');
					//}
					//iKeyCode="";
					if(data.total == 0){
						$('#save').linkbutton('disable');
					}else{
						$('#save').linkbutton('enable');
					}
					
				},
				onLoadError:function(none){
					
				}
			});
			
			$("#list").datagrid("getPager").pagination({ 
				total:listData.total, 
				onSelectPage:function (pageNo, pageSize_1) { 
					pageNum = pageNo;
					pageSize = pageSize_1;
					loadData();
				} 
			});
		}
		
		//检查可以排的格子
		function checkTeaCls(jsxm,skzcxq,weeks1,cdyq,iKeyCode,termid,classId){		
					$.ajax({
						type:'post',
						url:"<%=request.getContextPath()%>/Svl_Bjgpjp",
						data:"active=checkTeaCls&jsxm="+encodeURIComponent(jsxm)+"&skzcxq="+encodeURIComponent(skzcxq)+"&weeks="+encodeURIComponent(weeks1)+"&cdyq="+encodeURIComponent(cdyq)+"&iKeyCode="+iKeyCode+"&termid="+termid+"&classId="+classId,
						dataType:'json',
						asy:false,
						success:function(datas){
							//alert(datas[0].msg);
							var times=datas[0].msg;
							var time=times.split(",");
							var tab=document.getElementById("content");
				    		var rows=tab.rows;
				    		for(var i=1;i<rows.length;i++){
					        	for(var j=1;j<rows[i].cells.length;j++){
					        		$('#' + rows[i].cells[j].id).css('background-color', '#E7FECB');	
					        	}
					        }
							for(var i=0;i<time.length;i++){
								$('#' + time[i]).css('background-color', '#FFFFFF');	
							}
							for(var i=1;i<6;i++){
								$('#0'+i+'09').css('background-color', '#FFFFFF');
								$('#0'+i+'10').css('background-color', '#FFFFFF');
							}
						}
					});				
		}
		
		function loadTime(){
			$.ajax({
					type:'post',
					url:"<%=request.getContextPath()%>/Svl_Bjgpjp",
					data:"active=loadJCSJ&XNXQ=" + "20151" + "&JXXZ=" + "01",
					dataType:'json',
					asy:false,
					success:function(datas){
							//alert(datas[0].msg);
							
							var timeData=datas[0].timeData;
							var gpjpData=datas[0].gpjpData;
							
							var className1 = "";	
							var count = 1;
							var idNum = "";
							if(timeData != ""){
								$('#setTime9').show();
								var html='';
								html += '<tr align="center" style="height:20px;">'+
											'<th>'+
												className1 +
											'</th>'+
											'<th>'+
												'星期一' +
											'</th>'+
											'<th>'+
												'星期二' +
											'</th>'+
											'<th>'+
												'星期三' +
											'</th>'+
											'<th>'+
												'星期四' +
											'</th>'+
											'<th>'+
												'星期五' +
											'</th>'+
										'</tr>';
								
								for(var i=0;i<timeData.length;i++){
									idNum = count+i;
									if(idNum < 10){
										idNum = ("0"+idNum);
									}
									html += '<tr align="center" style="height:45px;">'+
											'<th class="jc">'+
												timeData[i].节次 +
											'</th>'+
											'<td id="01'+idNum+'" class="pkxx" title="">'+
											'</td>'+
											'<td id="02'+idNum+'" class="pkxx" title="">'+
											'</td>'+
											'<td id="03'+idNum+'" class="pkxx" title="">'+
											'</td>'+
											'<td id="04'+idNum+'" class="pkxx" title="">'+
											'</td>'+
											'<td id="05'+idNum+'" class="pkxx" title="">'+
											'</td>'+
										'</tr>';
								}
							
								$('#content9').html(html);
								$.parser.parse(('#content9'));
								alert($('#content9').html());
								if(gpjpData.length > 0){
									for(var j=0;j<gpjpData.length;j++){
										if(gpjpData[j].类型==2){
											$('#'+gpjpData[j].时间序列+'').html(gpjpData[j].排课信息);
											var pkxx=gpjpData[j].排课信息;
											var cdxx=gpjpData[j].场地名称;
											pkxx=pkxx.replace(new RegExp('&amp;','gm'),'&');
											cdxx=cdxx.replace(new RegExp('&amp;','gm'),'&');
											$('#'+gpjpData[j].时间序列+'').attr('title',(pkxx+"\r\n"+cdxx+"\r\n"+gpjpData[j].授课周次));
										}
										if(gpjpData[j].类型==3){
											$('#'+gpjpData[j].时间序列+'').html('禁排');
										}
									}
								}else{
									
								}
								editTable();
							}else{
								$('#setTime9').hide();
							}
					}
			});
			
			
		}
		
		function editTable(){
			$("#content9 td").click(function(){ 
				var idVal = $(this).attr("id");
				var fir = idVal.substr(0, 2);
				var sec = idVal.substr(2, 1);
				var thi = idVal.substr(3, 1);
				var kcjsn=$('#'+idVal).html();
				
				//alert($('#'+idVal).attr("id"));
				kcjsn=kcjsn.replace(new RegExp('&amp;','gm'),'&');
				//检查可以是添加还是删除，及可以排的课数量
					
			}); 
		}
		
		window.onload = function(){
			//去掉默认的contextmenu事件，否则会和右键事件同时出现。
			document.oncontextmenu = function(e){
				return false;
			};
			document.getElementById("content9").onmousedown = function(){
				if(event.button ==2){
					var className1 = "";
					var td = event.srcElement;
					var td1 = ($(td).html()).substr(0, 2);
					if($(td).html()=="星期一"||$(td).html()=="星期二"||$(td).html()=="星期三"||$(td).html()=="星期四"||$(td).html()=="星期五"||$(td).html()==className1||td1=="上午"||td1=="下午"||td1=="晚上"){
						return;
					}else{
						var idValcon="'"+td.id+"'";
						if($(td).html()==""){
							$(td).html("禁排");
							savepb(idValcon,"add");
						}else{
							if($(td).html()=="禁排"){
								$(td).html('');
								savepb(idValcon,"deljin");
							}else{
								delinfo = $(td).html();
								delRec(idValcon);
								$(td).html('');
							}
						}
					}
				}
			};
		};
		
		function addRec(gpjs,idValcon,kcjs){
			$.ajax({
				type:'post',
				url:"<%=request.getContextPath()%>/Svl_Bjgpjp",
				data:"active=add&SKJHMXBH="+iKeyCode+"&gpjs="+gpjs,
				dataType:'json',
				asy:false,
				success:function(datas){
					if(datas[0].msg=="1"){
						savepb(idValcon,"add");
					}
				}
			});
		}
		
		function delRec(idValcon){
			xnxqVal1=window.parent.document.frames["left"].xnxqVal;
			jxxzVal1=window.parent.document.frames["left"].jxxzVal;
			classId1=window.parent.document.frames["left"].classId;
			delinfo=delinfo.replace(new RegExp('&amp;','gm'),'&');
			var url="active=del&QCXX=" + encodeURIComponent(delinfo) + '&XNXQ=' + xnxqVal1 + '&JXXZ=' + jxxzVal1 + '&XZBDM=' + classId1 + '&SKJHMXBH=' + iKeyCode + '&idValcon='+idValcon;
			$.ajax({
				type:'post',
				url:"<%=request.getContextPath()%>/Svl_Bjgpjp",
				data:url,//"active=del&QCXX=" + delinfo + '&XNXQ=' + xnxqVal1 + '&JXXZ=' + jxxzVal1 + '&XZBDM=' + classId1 + '&SKJHMXBH=' + iKeyCode,
				dataType:'json',
				asy:false,
				success:function(datas){
					if(datas[0].msg=="1"){
						savepb(idValcon,"del");
					}
				}
			});
		}
		
		//工具按钮
		function doToolbar(iToolbar){
			if(iToolbar == "import"){
				$('#xxkImport').dialog('open');
			}
			if(iToolbar == "savexxk"){
				
			}
			if(iToolbar == "zongbiao"){
				openbjzb();
			}
		}
		
		//打开bjzb编辑窗口
		function openbjzb(){

			$('#bjzb').show();
			
			$('#bjzb').dialog({   
				title: '任课老师  授课周次  场地要求',   
				width: 800,//宽度设置   
				height: 480,//高度设置 
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
			$('#bjzb').dialog("open");
			
		}
		
		//保存
		function savepb(idValcon,savetype){
			xnxqVal1=window.parent.document.frames["left"].xnxqVal;
			jxxzVal1=window.parent.document.frames["left"].jxxzVal;
			classId1=window.parent.document.frames["left"].classId;
			
			$("#active").val("save");
			$('#GG_XZBDM').val(classId1);
			$('#GG_XNXQBM').val(xnxqVal1+jxxzVal1);
			$('#GG_SKJHMXBH').val(iKeyCode);
			
			//取所有content值，保存不为空的
		    var tab=document.getElementById("content");
    		var rows=tab.rows;
    		var b="";
    		for(var i=1;i<rows.length;i++){
	        	for(var j=1;j<rows[i].cells.length;j++){
	        		if((rows[i].cells[j].innerHTML)!=""){
	        			b+=rows[i].cells[j].id+",";
	        		}
	        	}
	        }
	        b=b.substring(0,b.length-1);
		    $('#GG_SJXL').val(b);
		    
		    $('#SJXL').val(idValcon);
		    $('#CDYQ').val(cdyq);
		    $('#CDMC').val(cdmc);
		    $('#SAVETYPE').val(savetype);
		    
		    var c = $('.pkxx');
		    var d="";
			for(var i=0;i<c.length;i++){
				d+=$(c[i]).html()+",";
		    } 
		    d=d.substring(0,d.length-1);
		  	$('#KCJS').val(d);
		  	
			$('#fm').submit();
		}
		
		//fm提交事件
		$('#fm').form({
			url:'<%=request.getContextPath()%>/Svl_Bjgpjp',
			onSubmit:function(){
				
			},
			//提交成功
			success:function(datas){
				if($('#active').val()=="save"){
					var json = eval("("+datas+")");
					//showMsg(json[0].msg);
					initGridData(classId1,xnxqVal1,jxxzVal1);
				}
			}
		});
		
		function initDialog(){
			$('#xxkImport').dialog({   
				title: '选修课导入',
				width: 350,//宽度设置   
				height: 170,//高度设置 
				modal:true,
				closed: true,   
				cache: false, 
				draggable:false,//是否可移动dialog框设置
				toolbar:[{
					//保存编辑
					text:'保存',
					iconCls:'icon-save',
					handler:function(){
						//传入save值进入doToolbar方法，用于保存
						doToolbar('savexxk');
					}
				}],
				//打开事件
				onOpen:function(data){},
				//读取事件
				onLoad:function(data){},
				//关闭事件
				onClose:function(data){
					emptyDialog();
				}
			});
		}
	</script>
</html>