<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<!--
	createTime:2015-6-23
	createUser:刘金璋
	description:添加个人课程
	
	editTime:2015-07-24
	editUser:叶强
	description:添加所有功能
 -->
<html>
<head>
<title>个人课程</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- JQuery 专用4个文件 -->
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/icon.css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/pubTimetable.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/publicScript.js"></script>
	<style>
		.kcbTdStyle{
			font-size:12;
			border-left:1px solid #95B8E7;
			border-bottom:1px solid #95B8E7;
			background-color:#FFFFFF;
			text-align:center;
		}
		
		.kcbTitle{
			 font-weight:bold;
			 font-size:14;
		}
		
		.titleBG{
			background-color:#EFEFEF;
		}
		
		.titleTdStyle{
			font-size:12;
			border-left:1px solid #95B8E7;
			border-bottom:1px solid #95B8E7;
			text-align:center;
		}
		
		.splitTd{
			border-bottom:4px double #95B8E7;
		}
	</style>
</head>
<body class="easyui-layout">
	<div region="west" title="学生列表" style="width:120px;">
		<ul id="stuTree" class="easyui-tree" style="display:none">
		</ul>
	</div>
	<div region="center" style="width:100%;" class="easyui-layout">
		<div id="mask" style="position:absolute; top:0; z-index:99999; width:110%; height:100%; background:url(0);"></div>
		<div id="timeTable" region="north" style="height:370px;">
		</div>
		<div region="center">
			<div style="color:red; font-size:14; float:left; margin-top:5px; margin-left:5px;">注：</div>
			<div style="color:red; font-size:14; float:left; margin-top:5px;">1.选择学生后单击空白位置可添加课程<br/>2.单击已添加课程位置可更改或删除课程</div>
		</div>
		
		<!-- 添加课程窗口 -->
		<div id="addCourseDialog" style="overflow:hidden;">
			<form id="form1" method="post">
				<table>
					<tr>
						<td colspan="2" style="text-align:center;">
							<div id="save" style="float:left; width:65px; height:25px; background:url(0); border:1px solid #FFFFFF; cursor:pointer;" 
								onmouseover="$(this).css('border', '1px solid #95B8E7');$(this).css('background-color', '#E6EFFF');" 
								onmouseout="$(this).css('border', '1px solid #FFFFFF');$(this).css('background-color', '#FFFFFF');" 
								onclick="doToolbar(this.id);">
								<img style=" position:relative; top:3px;" src="<%=request.getContextPath()%>/css/themes/icons/filesave.png"/>&nbsp;
								<span style="font-size:12; position:relative; bottom:1px;">保存</span></div>
							<div id="del" style="width:65px; height:25px; background:url(0); border:1px solid #FFFFFF; cursor:pointer; display:none;" 
								onmouseover="$(this).css('border', '1px solid #95B8E7');$(this).css('background-color', '#E6EFFF');" 
								onmouseout="$(this).css('border', '1px solid #FFFFFF');$(this).css('background-color', '#FFFFFF');" 
								onclick="doToolbar(this.id);">
								<img style="margin-top:4px;" src="<%=request.getContextPath()%>/css/themes/icons/cancel.png"/>&nbsp;
								<span style="font-size:12; position:relative; bottom:4px;">删除</span></div>
						</td>			
					</tr>
				</table>
				<table id="ee" singleselect="true" width="100%" class="tablestyle">
					<tr>
						<td class="titlestyle" style="width:60px;">位置</td>
						<td class="titlestyle" id="kcbPos">
						</td>
					</tr>
					<tr>
						<td class="titlestyle">班级</td>
						<td>
							<select name="PT_BJDM" id="PT_BJDM" class="easyui-combobox" style="width:200px;">
							</select>
						</td>
					</tr>
					<tr>
						<td class="titlestyle">课程</td>
						<td>
							<input name="ic_kcmc" id="ic_kcmc" class="easyui-textbox" style="width:200px;" value="请选择班级" disabled="disabled">
						</td>
					</tr>
			    </table>
				<input type="hidden" id="active" name="active"/>
				<input type="hidden" id="PT_SJXL" name="PT_SJXL"/>
				<input type="hidden" id="PT_XNXQBM" name="PT_XNXQBM"/>
				<input type="hidden" id="PT_ID" name="PT_ID"/>
				<input type="hidden" id="PT_XZBDM" name="PT_XZBDM"/>
				<input type="hidden" id="PT_LX" name="PT_LX"/>
				<input type="hidden" id="PT_KCBMXBH" name="PT_KCBMXBH"/>
				<input type="hidden" id="PT_XH" name="PT_XH"/>
		    </form>
		</div>
	</div>
</body>
<script type="text/javascript">
	var curXnxq = window.parent.curSelXnxq;
	var curClass = window.parent.curSelClass;
	var mzts = window.parent.mzts;//每周天数
	var sw = window.parent.sw;//上午
	var zw = window.parent.zw;//中午
	var xw = window.parent.xw;//下午
	var ws = window.parent.ws;//晚上
	var weekNameArray = window.parent.weekNameArray;
	var oddArray = window.parent.oddArray;
	var evenArray = window.parent.evenArray;
	var curSelStu = '';
	
	$(document).ready(function(){
		$('#PT_XNXQBM').val(curXnxq);
		$('#PT_XZBDM').val(curClass);
		initDialog();//初始化对话框
		initData();//页面初始化加载数据
	});
	
	/**加载 dialog控件**/
	function initDialog(){
		$('#addCourseDialog').dialog({   
			title:'添加',
			width: 300,//宽度设置   
			height: 142,//高度设置 
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
				if($('#PT_BJDM').combobox('getValue') != '没有符合条件的班级'){
					$('#PT_BJDM').combobox('setValue', '');
					$('#ic_kcmc').textbox('setValue', '请选择班级');
				}
				$('#PT_KCBMXBH').val('');
			}
		});
	}
	
	/**页面初始化加载数据**/
	function initData(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Pksz',
			data : 'active=initAddStuCourse&PT_XNXQBM=' + curXnxq + '&PT_XZBDM=' + curClass ,
			dataType:"json",
			success : function(data) {
				initBlankKcb(data[0].kcbData, data[0].addCourseData);
				initCombobox(data[0].classData);
				loadStuTree();
			}
		});
	}
	
	/**加载学生信息TREE
		@curXnxq 学年学期编码
		@curClass 当前班级
	**/
	function loadStuTree(){
		$('#stuTree').tree({
			checkbox: false,
			url:'<%=request.getContextPath()%>/Svl_Pksz?active=queStuTree&PT_XZBDM=' + curClass,
		    onClick:function(node){
	    		//判断点击的学生是不是当前选中的学生
	    		if(node.id != curSelStu){
	    			curSelStu = node.id;
	    			//查询当前选择的学生的添加课程信息
	    			loadStuAddCourse();
	    			$('#mask').hide();
	    		}
			},
		    onBeforeExpand:function(node,param){
			},
			onLoadSuccess:function(node, data){
				$("#stuTree").show();
			}
		});
	};
	
	/**查询当前选择的学生的添加课程信息**/
	function loadStuAddCourse(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Pksz',
			data : 'active=loadStuAddCourse&PT_XNXQBM=' + curXnxq + '&PT_XZBDM=' + curClass + '&PT_XH=' + curSelStu,
			dataType:"json",
			success : function(data) {
				if(data.MSG == '已设置'){
					var addCourseInfo = data.courseInfo;
					var content = '';
					var tempTitle = '';
					var tempTimeOrder = '';
					
					for(var i=0; i<addCourseInfo.length; i++){
						tempTimeOrder = addCourseInfo[i].timeOrder;
						if(addCourseInfo[i].xzbmc.length > 7){
							content += addCourseInfo[i].xzbmc.substring(0, 7)+'...';
						}else{
							content += addCourseInfo[i].xzbmc;
						}
						content += '</br>';
						if(addCourseInfo[i].kcmc.length > 7){
							content += addCourseInfo[i].kcmc.substring(0, 7)+'...';
						}else{
							content += addCourseInfo[i].kcmc;
						}
						content += '<input type="hidden" id="code_' + tempTimeOrder + '" value="' + addCourseInfo[i].code + '">' +
							'<input type="hidden" id="kcbCode_' + tempTimeOrder + '" value="' + addCourseInfo[i].kcbCode + '">' + 
							'<input type="hidden" id="bjdm_' + tempTimeOrder + '" value="' + addCourseInfo[i].xzbdm + '">' +
							'<input type="hidden" id="kcmc_' + tempTimeOrder + '" value="' + addCourseInfo[i].kcmc + '">';
						tempTitle = addCourseInfo[i].xzbmc + '\n' +  addCourseInfo[i].kcmc + '\n' + addCourseInfo[i].skjsxm + '\n' + addCourseInfo[i].sjcdmc;
						tempTitle += '\n' + parseWeekShow(addCourseInfo[i].skzc, oddArray, evenArray);//修改授课周次显示内容
						$('#kcb_' + tempTimeOrder).html(content);
						$('#kcb_' + tempTimeOrder).attr('title', tempTitle);
					}
				}
			}
		});
	};
	
	/**生成课程表
		@kcbData 课表信息
		@addCourseData 添加的课程信息
	**/
	function initBlankKcb(kcbData, addCourseData){
		var zjs = sw+zw+xw+ws;
		var jcsj = window.parent.jcsj;
		var tempOrder = '';
		var indexNum = 0;
		var addFlag = false;
		
		var tdWidth = 100/(mzts+1);
		var kcbContent = '<table style="width:100%; height:100%; border-top:1px solid #95B8E7; border-right:1px solid #95B8E7;" cellspacing="0" cellpadding="0">';
		
		for(var i=-1; i<(sw+zw+xw+ws); i++){
			kcbContent += '<tr class="kcbTrStyle"';
			
			if(i == -1){
				kcbContent += ' style="height:45px;"';	
			}
			kcbContent += '>';
			
			for(var j=-1; j<mzts; j++){
				//添加左上角空单元格
				if(i==-1 && j==-1){
					kcbContent += '<td colspan="3" class="kcbTdStyle titleBG splitTd">&nbsp;</td>';
					continue;
				}
				
				//判断添加星期数
				if(i==-1 && j>-1){
					//判断是小周还是大周版
					if(mzts < 8){
						kcbContent += '<td class="kcbTdStyle kcbTitle titleBG splitTd">' + weekNameArray[j] + '</td>';	
					}else{
						kcbContent += '<td class="kcbTdStyle kcbTitle titleBG splitTd">大周' + (j+1) + '</td>';
					}
					continue;
				}
				
				//判断添加节次数
				if(i>-1 && j==-1){
					//上午课程
					if(i<sw &&　sw>0){
						//判断第一行
						if(i == 0){
							kcbContent += '<td id="width_sw" rowspan="'+sw+'" class="titleTdStyle kcbTitle titleBG splitTd">上午</td>';
						}
						
						kcbContent += '<td id="width_xh" class="titleTdStyle titleBG';
						//判断中午有课
						if((i+1)==sw && zw>0){
							kcbContent += ' splitTd';
						}
						kcbContent += '">' + (i+1) + '</td>';
						
						kcbContent += '<td id="width_sjd" class="titleTdStyle titleBG';
						//判断中午有课
						if(((i+1)==sw && zw>0)){
							kcbContent += ' splitTd';
						}
						kcbContent += '">';
						if(jcsj.length > i){
							kcbContent += jcsj[i];
						}else{
							kcbContent += '&nbsp;';
						}
						kcbContent += '</td>';
					}
					
					//中午课程
					if(i>=sw && i<(sw+zw) && zw>0){
						//判断第一行
						if(i == sw){
							kcbContent += '<td rowspan="'+zw+'" class="titleTdStyle kcbTitle titleBG splitTd">中午</td>';
						}
						
						kcbContent += '<td class="titleTdStyle titleBG';
						//判断下午有课
						if((i+1)==(sw+zw) && xw>0){
							kcbContent += ' splitTd';
						}
						kcbContent += '">' + (i+1) + '</td>';
						kcbContent += '<td class="titleTdStyle titleBG';
						//判断下午有课
						if((i+1)==(sw+zw) && xw>0){
							kcbContent += ' splitTd';
						}
						kcbContent += '">';
						if(jcsj.length > i){
							kcbContent += jcsj[i];
						}else{
							kcbContent += '&nbsp;';
						}
						kcbContent += '</td>';
					}
					
					//下午课程
					if(i>=(sw+zw) && i<(sw+zw+xw) && xw>0){
						//判断第一行
						if(i == sw+zw){
							kcbContent += '<td rowspan="'+xw+'" class="titleTdStyle kcbTitle titleBG splitTd">下午</td>';
						}
						
						kcbContent += '<td class="titleTdStyle titleBG';
						//判断晚上有课
						if((i+1)==(sw+zw+xw) && ws>0){
							kcbContent += ' splitTd';
						}
						kcbContent += '">' + (i+1) + '</td>';
						kcbContent += '<td class="titleTdStyle titleBG';
						//判断晚上有课
						if((i+1)==(sw+zw+xw) && ws>0){
							kcbContent += ' splitTd';
						}
						kcbContent += '">';
						if(jcsj.length > i){
							kcbContent += jcsj[i];
						}else{
							kcbContent += '&nbsp;';
						}
						kcbContent += '</td>';
					}
					
					//晚上课程
					if(i>=(sw+zw+xw) && i<zjs && ws>0){
						//判断第一行
						if(i == (sw+zw+xw)){
							kcbContent += '<td rowspan="'+ws+'" class="titleTdStyle titleBG kcbTitle">晚上</td>';
						}
						kcbContent += '<td class="titleTdStyle titleBG">' + (i+1) + '</td>'
									+ '<td class="titleTdStyle titleBG">';
						if(jcsj.length > i){
							kcbContent += jcsj[i];
						}else{
							kcbContent += '&nbsp;';
						}
						kcbContent += '</td>';
					}
					
					continue;
				}
				
				//拼接时间序列
				if((j+1) < 10){
					tempOrder = '0'+(j+1);
				}else{
					tempOrder = j+1;
				}
				if((i+1) < 10){
					tempOrder += '0'+(i+1);
				}else{
					tempOrder += i+1;
				}
				
				//添加普通单元格
				kcbContent += '<td id="kcb_' + tempOrder + '" class="kcbTdStyle kcbTdCentent';
				if(((i+1)==sw && zw>0) || ((i+1)==(sw+zw) && xw>0) || ((i+1)==(sw+zw+xw) && ws>0)){
					kcbContent += ' splitTd';
				}
				
				//遍历添加的课程,查询当前时间序列是否有添加课程
				for(var a=0; a<addCourseData.length; a++){
					if(addCourseData[a].时间序列 == tempOrder){
						kcbContent += '">' + addCourseData[a].课程名称  + '<br/>' + addCourseData[a].授课教师姓名;
						addFlag = true;
					}
				}
				//判断当前时间序列没有添加过课程，查询课表信息中当前时间序列信息
				if(addFlag == false){
					//获取该时间序列的授课周次在数组中的位置
					indexNum = ((parseInt(tempOrder.substring(0, 2), 10)-1)*zjs + parseInt(tempOrder.substring(2), 10)) - 1;
					
					//判断是否有课程
					if(kcbData[indexNum].班级排课状态 == ''){
						kcbContent += '" onmouseover="tdMouseOver(this.id);" onmouseout="tdMouseOut(this.id);" ' +
								'onclick="checkAddCourse(this.id);" style="color:red; cursor:pointer;">&nbsp;';
					}else{
						//判断是否为禁排
						if(kcbData[indexNum].班级排课状态 == '3'){
							kcbContent += '">禁排';
						}else{
							kcbContent += '">' + kcbData[indexNum].课程名称  + '<br/>' + kcbData[indexNum].授课教师姓名;
						}
					}
				}
				kcbContent += '</td>';
				addFlag = false;
			}
			kcbContent += '</tr>';
		}
		
		kcbContent += '</table>';
		$('#timeTable').html(kcbContent);
		
		$('.kcbTdStyle').css('width', tdWidth+'%');
		$('#width_sw').css('width', '50px');
		$('#width_xh').css('width', '20px');
		$('#width_sjd').css('width', '130px');
		$('.kcbTrStyle').css('height', parseInt($('#timeTable').css('height').substring(0, $('#timeTable').css('height').length-2)-1, 10)/(sw+xw+ws+1)+'px');
	}
	
	/**课程表及未排课程单元格鼠标移入事件
		@id 当前td的id
	**/
	function tdMouseOver(tdID){
		curTargetId = tdID;
		$('#' + tdID).css('background-color', '#FFE48D');
	}
	
	/**课程表及未排课程单元格鼠标移出事件
		@tdID 单元格id
	**/
	function tdMouseOut(tdID){
		curTargetId = '';
		$('#' + tdID).css('background-color', "#FFFFFF");
	}
	
	/**加载combobox控件
		@classData 班级下拉框数据
	**/
	function initCombobox(classData){
		$('#PT_BJDM').combobox({
			data:classData,
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			panelHeight:'140', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data != ''){
					if(data.length == 1){
						$(this).combobox('setValue','没有符合条件的班级');
						$(this).combobox('disable');
					}else{
						//初始化combobox时赋值
						$(this).combobox('setValue', '');
					}
				}
			},
			//下拉列表值改变事件
			onChange:function(data){
				if(data == ''){
					$('#ic_kcmc').textbox('setValue', '请选择班级');
					$('#PT_KCBMXBH').val('');
				}else{
					$.ajax({
						type : "POST",
						url : '<%=request.getContextPath()%>/Svl_Pksz',
						data : 'active=loadKcbCourseCombo&PT_XNXQBM=' + curXnxq + '&PT_XZBDM=' + $('#PT_BJDM').combobox('getValue') + '&PT_SJXL=' + $('#PT_SJXL').val(),
						dataType:"json",
						success : function(data) {
							$('#ic_kcmc').textbox('setValue', data[0].comboName);
							$('#PT_KCBMXBH').val(data[0].comboValue);
						}
					});
				}
			}
		});
	}
	
	/**检查新建或编辑
		@tdId
	**/
	function checkAddCourse(tdId){
		var type = '';
		var timeOrder = tdId.substring(tdId.indexOf('_')+1);
		$('#PT_SJXL').val(timeOrder);
		$('#PT_ID').val($('#code_' + timeOrder).val());
		
		if($('#' + tdId).html() == '&nbsp;'){
			type = 'add';
			$('#addCourseDialog').dialog({   
				title:'添加课程'
			});
			$('#del').css('display', 'none');
			$('#delImg').css('display', 'none');
			$('#active').val('addStuCourse');
		}else{
			type = 'edit';
			$('#addCourseDialog').dialog({   
				title:'编辑课程'
			});
			$('#PT_BJDM').combobox('setValue', $('#bjdm_' + timeOrder).val());
			$('#del').css('display', '');
			$('#delImg').css('display', '');
			$('#active').val('editStuCourse');
		}
		
		openAddCourse(type, timeOrder);
	}
	
	/**打开添加课程对话框
		@type 类型
		@timeOrder 时间序列
	**/
	function openAddCourse(type, timeOrder){
		var day = parseInt(timeOrder.substring(0, 2), 10);
		var num = parseInt(timeOrder.substring(2), 10);
		$('#kcbPos').html(weekNameArray[day-1] + '第' + num + '节');
		$('#addCourseDialog').dialog('open');
	}
	
	/**工具栏按钮调用方法，传入按钮的id
		@id 当前按钮点击事件
	**/
	function doToolbar(id){
		if(id == 'save'){
			if($('#PT_BJDM').combobox('getValue') == ''){
				alertMsg('请选择班级');
				return;
			}
			if($('#PT_KCBMXBH').val() == ''){
				alertMsg('请选择有可用课程的班级');
				return;
			}
			
			$('#PT_LX').val('3');
			$('#PT_XH').val(curSelStu);
			$("#form1").submit();
		}
		
		if(id == 'del'){
			ConfirmMsg('是否确定要删除添加的课程？', 'deleteAddCourse()', '');
		}
	}
	
	/**表单提交**/
	$('#form1').form({
		//定位到servlet位置的url
		url:'<%=request.getContextPath()%>/Svl_Pksz',
		//当点击事件后触发的事件
		onSubmit: function(data){
		}, 
		//当点击事件并成功提交后触发的事件
		success:function(data){
			var json  =  eval("("+data+")");
			if(json[0].MSG == '保存成功'){
				var timeOrder = $('#PT_SJXL').val();
				var content = '';
				if(json[0].className.length > 7){
					content += json[0].className.substring(0, 7)+'...';
				}else{
					content += json[0].className;
				}
				content += '</br>';
				if(json[0].courseName.length > 7){
					content += json[0].courseName.substring(0, 7)+'...';
				}else{
					content += json[0].courseName;
				}
				content += '<input type="hidden" id="code_' + timeOrder + '" value="' + json[0].ID + '"/>' +
						'<input type="hidden" id="kcbCode_' + timeOrder + '" value="' + json[0].kcbCode + '"/>' +
						'<input type="hidden" id="bjdm_' + timeOrder + '" value="' + json[0].classCode + '"/>' +
						'<input type="hidden" id="kcmc_' + timeOrder + '" value="' + json[0].courseName + '">';
				var tempTitle = json[0].className + '\n' + json[0].courseName + '\n' + json[0].teaName + '\n' + json[0].siteName; 
				tempTitle += '\n' + parseWeekShow(json[0].weekNum, oddArray, evenArray);//修改授课周次显示内容
				$('#kcb_' + timeOrder).html(content);
				$('#kcb_' + timeOrder).attr('title', tempTitle.replace(/\&amp;/g, ''));
				$('#addCourseDialog').dialog('close');
				showMsg(json[0].MSG);
			}else{
				alertMsg(json[0].MSG);
			}
		}
	});
	
	/**删除设置的课程**/
	function deleteAddCourse(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Pksz',
			data : 'active=delAddCourse&PT_ID=' + $('#PT_ID').val(),
			dataType:"json",
			success : function(data) {
				if(data[0].MSG == '删除成功'){
					$('#kcb_' + $('#PT_SJXL').val()).html('&nbsp;');
					$('#kcb_' + $('#PT_SJXL').val()).attr('title', '');
					$('#addCourseDialog').dialog('close');
					showMsg(data[0].MSG);
				}else{
					alertMsg(data[0].MSG);
				}
			}
		});
	}
</script>
</html>