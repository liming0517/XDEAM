<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%
	/**
		创建人：lupengfei
		Create date: 2016.02.04
		功能说明：用于设置课程信息
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
	<title>课程信息列表</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/icon.css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
 	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/publicScript.js"></script>
  </head>
  <style>
  
  </style>
<body  class="easyui-layout">
	<div id="north" region="north" title="课程信息" style="height:112px;" >
		<table>
			<tr>
				<td><a href="#" id="new" class="easyui-linkbutton" plain="true" iconcls="icon-new" onClick="doToolbar(this.id);" title="">新建</a></td>
				<td><a href="#" id="edit" class="easyui-linkbutton" plain="true" iconcls="icon-edit" onClick="doToolbar(this.id);" title="">编辑</a></td>
				<td><a href="#" id="del" class="easyui-linkbutton" plain="true" iconcls="icon-cancel" onClick="doToolbar(this.id);" title="">删除</a></td>
			</tr>
		</table>
		<table id="ee" singleselect="true" width="100%" class="tablestyle">
			<tr>	
				<td style="width:150px;" class="titlestyle">课程代码</td>
				<td>
					<input name="KCDM_CX" id="KCDM_CX" class="easyui-textbox" style="width:180px;"/>
				</td>
				<td style="width:150px;" class="titlestyle">课程名称</td>
				<td>
					<input name="KCMC_CX" id="KCMC_CX" class="easyui-textbox" style="width:180px;"/>
				</td>
				<td style="width:150px;" class="titlestyle">课程类型</td>
				<td>
					<select name="KCLX_CX" id="KCLX_CX" class="easyui-combobox" style="width:180px;">
					</select>
				</td>
				<td style="width:150px;" align="center" rowspan=2>
					<a href="#" onclick="doToolbar(this.id)" id="query" class="easyui-linkbutton" plain="true" iconcls="icon-search">查询</a>
				</td>				
			</tr>
			<tr>	
				<td style="width:150px;" class="titlestyle">所属专业</td>
				<td>
					<select name="ZYDM_CX" id="ZYDM_CX" class="easyui-combobox" style="width:180px;">
					</select>
				</td>
				<td style="width:150px;" class="titlestyle">所属系部</td>
				<td colspan=3>
					<select name="XBDM_CX" id="XBDM_CX" class="easyui-combobox" style="width:340px;">
					</select>
				</td>
			</tr>
	    </table>
	</div>
	<div region="center">
		<table id="courseList" style="width:100%;"></table>
	</div>
	
	<!-- 课程信息新建编辑 -->
	<div id="courseInfo">
		<form id="form1" method='post'>
			<table style="width:100%;" class="tablestyle">
				<tr>
					<td class="titlestyle">课程代码</td>
					<td id="KCDM"></td>
				</tr>
				<tr>
					<td class="titlestyle">课程名称</td>
					<td>
						<input name="KCMC" id="KCMC" class="easyui-textbox" style="width:220px;" required="true">
					</td>
				</tr>				
				<tr>
					<td class="titlestyle" >课程类型</td>
					<td id="CH_KCLX">
						<select name="KCLX" id="KCLX" class="easyui-combobox" panelHeight="auto" style="width:220px;" >
						</select>
					</td>
				</tr>
				<tr>
					<td class="titlestyle">所属系部</td>
					<td>
						<select name="XBDM" id="XBDM" class="easyui-combobox" panelHeight="auto" style="width:220px;">
						</select>
					</td>
				</tr>
				<tr>
					<td class="titlestyle">所属专业</td>
					<td>
						<select name="ZYDM" id="ZYDM" class="easyui-combobox" panelHeight="auto" style="width:220px;">
						</select>
					</td>
				</tr>
				<tr>
					<td class="titlestyle">考试形式</td>
					<td>
						<select name="KSXS" id="KSXS" class="easyui-combobox" style="width:220px;">
						</select>
					</td>
				</tr>
			</table>
			
			<input type="hidden" id="active" name="active"/>
			<input type="hidden" id="kcbh" name="kcbh"/>
			<input type="hidden" id="XBBH" name="XBBH"/>
			<input type="hidden" id="XBMC" name="XBMC"/>
			<input type="hidden" id="oldKCMC" name="oldKCMC"/>
		</form>
	</div>
</body>
<script type="text/javascript">
	var row = '';      //行数据
	var iKeyCode = ''; //定义主键
	var pageNum = 1;   //datagrid初始当前页数
	var pageSize = 20; //datagrid初始页内行数
	var NJDM_CX = '';//查询条件
	var ZYDM_CX = '';
	var KCDM_CX = '';
	var KCMC_CX = '';
	var KCLX_CX = '';
	var saveType = '';
	var kclx="";//课程类型
	var ksxs="";//考试形式
	var kcmc="";//课程名称
	var tempKclx = '';
	var tempZydm = '';
	var tempSsxb = '';
	var xibunum=0;
	
	$(document).ready(function(){
		initDialog();//初始化对话框
		initData();//页面初始化加载数据
	});
	
	/**页面初始化加载数据**/
	function initData(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_CourseSet',
			data : 'active=initData&page=' + pageNum + '&rows=' + pageSize,
			dataType:"json",
			success : function(data) {
				loadGrid(data[0].listData);
				initCombobox(data[0].kclxData, data[0].zydmData, data[0].ksxsData, data[0].xbdmData);
			}
		});
	}
	
	/**加载combobox控件
		@jxxzData 下拉框数据
	**/
	function initCombobox(kclxData, zydmData ,ksxsData ,xbdmData){ 
		$('#KCLX').combobox({
			data:kclxData,
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			panelHeight:'auto', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data != ''){
					//初始化combobox时赋值
					$(this).combobox('setValue', '');
				}
			},
			//下拉列表值改变事件
			onChange:function(data){
				//if(saveType=='new'){
					if(data=="01"){//公共课
						$('#ZYDM').combobox('setValue', '');
						$('#ZYDM').combobox("disable");
					}else if(data=="02"){//专业课
						$('#ZYDM').combobox("enable");
					}else if(data=="03"){//选修课
						$('#ZYDM').combobox("disable");
					}else{
						$('#ZYDM').combobox("disable");
					}
				//}
			}
		});

		$('#KCLX_CX').combobox({
			data:kclxData,
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			panelHeight:'auto', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data != ''){
					//初始化combobox时赋值
					$(this).combobox('setValue', '');
				}
			}
		});
	
		$('#ZYDM_CX').combobox({
			data:zydmData,
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

		$('#ZYDM').combobox({
			data:zydmData,
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

		$('#KSXS').combobox({
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
			},
			//下拉列表值改变事件
			onChange:function(data){}
		});
		
		$('#XBDM_CX').combobox({
			url:"<%=request.getContextPath()%>/Svl_CourseSet?active=loadXBDMCombo",
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			multiple:true,
			panelHeight:'140', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data != ''){
					//初始化combobox时赋值
					$(this).combobox('setValue', '');
					xibunum=data[0].MSG; 
				}
			},
			onSelect:function(record){
				var val = $(this).combobox('getValues')+""; 
 				var selnum=val.split(","); 
 				if(selnum.length==xibunum){
 					$(this).combobox('setValues', '');
 				}else{
 					if(val.substring(0,1)==","){	
						$(this).combobox('setValues', val.substring(1,val.length));
	 				}else if(val.substring(val.length-1,val.length)==","){//全部
	 					var data = $(this).combobox('getData');	
	 					$(this).combobox('setValue', data[0].comboValue);
	 				}else{
						
	 				}
 				}
			},
			onUnselect:function(record){
				if($(this).combobox('getValues') == ''){
					var data = $(this).combobox('getData');
					$(this).combobox('setValue', data[0].comboValue);
				}
			},
			//下拉列表值改变事件
			onChange:function(data){

			}
		});

		$('#XBDM').combobox({
			url:"<%=request.getContextPath()%>/Svl_CourseSet?active=loadXBDMCombo",
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			multiple:true,
			panelHeight:'140', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data != ''){
					//初始化combobox时赋值
					$(this).combobox('setValue', '');
					xibunum=data[0].MSG; 
				}
			},
			onSelect:function(record){
				var val = $(this).combobox('getValues')+""; 
 				var selnum=val.split(","); 
 				if(selnum.length==xibunum){
 					$(this).combobox('setValues', '');
 				}else{
 					if(val.substring(0,1)==","){	
						$(this).combobox('setValues', val.substring(1,val.length));
	 				}else if(val.substring(val.length-1,val.length)==","){//全部
	 					var data = $(this).combobox('getData');	
	 					$(this).combobox('setValue', data[0].comboValue);
	 				}else{
						
	 				}
 				}
			},
			onUnselect:function(record){
				if($(this).combobox('getValues') == ''){
					var data = $(this).combobox('getData');
					$(this).combobox('setValue', data[0].comboValue);
				}
			},
			//下拉列表值改变事件
			onChange:function(data){
			
			}
		});
	}
	
	/**加载 dialog控件**/
	function initDialog(){
		$('#courseInfo').dialog({   
			width: 300,//宽度设置   
			height: 218,//高度设置 
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
					doToolbar('save');
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

	/**加载 datagrid控件，读取页面信息
		@listData 列表数据
	**/
	function loadGrid(listData){
		$('#courseList').datagrid({
			//url:'semesterList.json',
			data:listData,
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
			columns:[[
				//field为读取数据的数据名，title为显示的数据名，width宽度设置，align数字在表格中显示的位置
				{field:'KCDM',title:'课程代码',width:fillsize(0.2),align:'center'},
				{field:'KCMC',title:'课程名称',width:fillsize(0.2),align:'center'},
 				{field:'KCLXMC',title:'课程类型',width:fillsize(0.2),align:'center'},
 				{field:'ZYMC',title:'所属专业',width:fillsize(0.2),align:'center'},
 				{field:'XBMC',title:'所属系部',width:fillsize(0.3),align:'center'}
			]],
			//双击某行时触发
			onDblClickRow:function(rowIndex,rowData){},
			//读取datagrid之前加载
			onBeforeLoad:function(){},
			//单击某行时触发
			onClickRow:function(rowIndex,rowData){
				//主键赋值
				iKeyCode = rowData.KCDM;
				row = rowData;
				kclx = rowData.KCLX;
				ksxs = rowData.KSXS;
				kcmc = rowData.KCMC;
			},
			//加载成功后触发
			onLoadSuccess: function(data){
				iKeyCode = '';
			}
		});
		
		$("#courseList").datagrid("getPager").pagination({
			total:listData.total,
			afterPageText: '页&nbsp;<a href="#" onclick="goEnterPage();">Go</a>&nbsp;&nbsp;&nbsp;共 {pages} 页',
			onSelectPage:function (pageNo, pageSize_1) { 
				pageNum = pageNo;
				pageSize = pageSize_1;
				loadData();
			} 
		});
	};
	
	function goEnterPage(){
		var e = jQuery.Event("keydown");//模拟一个键盘事件
		e.keyCode = 13;//keyCode=13是回车 
		$("input.pagination-num").trigger(e);//模拟页码框按下回车 
	}
	
	/**读取datagrid数据**/
	function loadData(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_CourseSet',
			data : 'active=query&page=' + pageNum + '&rows=' + pageSize + 
				'&KCDM_CX=' + encodeURI(KCDM_CX) + 
				'&KCMC_CX=' + encodeURI(KCMC_CX) + 
				'&ZYDM_CX=' + encodeURI(ZYDM_CX) +
				'&KCLX_CX=' + encodeURI(KCLX_CX) ,
			dataType:"json",
			success : function(data) {
				loadGrid(data[0].listData);
			}
		});
	}
	
	/**工具栏按钮调用方法，传入按钮的id
		@id 当前按钮点击事件
	**/
	function doToolbar(id){
		//查询
		if(id == 'query'){
			KCDM_CX = $('#KCDM_CX').textbox('getValue'); 
			KCMC_CX = $('#KCMC_CX').textbox('getValue');
			ZYDM_CX = $('#ZYDM_CX').combobox('getValue');
			KCLX_CX = $('#KCLX_CX').combobox('getValue');
			loadData();
		}
		
		//判断获取参数为new，执行新建操作
		if(id == 'new'){
			//打开dialog
			$('#courseInfo').dialog({   
				title: '新建课程信息'
			});
			saveType = 'new';
			$('#courseInfo').dialog('open');
			$('#ZYDM').combobox("disable");
			$('#KCLX').combobox("enable");
		}

		//判断获取参数为edit，执行编辑操作
		if(id == 'edit'){
			if(iKeyCode == ''){
				alertMsg('请选择一行数据');
				return;
			}
			
			//打开dialog
			$('#courseInfo').dialog({   
				title: '编辑课程信息'   
			});
			if(row!=undefined && row!=''){
				$('#form1').form('load', row);
				$('#KCDM').html(row.KCDM); 
				tempKclx = row.KCLX;
				tempZydm = row.ZYDM;
				tempSsxb = row.XBDM; 
		
				$('#KCLX').combobox('setValue', tempKclx);
				$('#ZYDM').combobox('setValue', tempZydm);
				$('#oldKCMC').val(kcmc);
	
				if(tempKclx != '02'){
					$('#ZYDM').combobox('setValue', '');
					$('#ZYDM').combobox("disable");
				}else{
					$('#ZYDM').combobox("enable");
				}
// 				if(row.ZYDM=="000000"){
// 					$('#ZYDM').combobox('setValue','');
// 					$('#KCLX').combobox('setValue','01');		
// 				}else if(row.ZYDM=="900000"){
// 					$('#ZYDM').combobox('setValue','');
// 					$('#KCLX').combobox('setValue','03');	
// 				}else{
// 					$('#KCLX').combobox('setValue','02');		
// 				}

				//$('#ZYDM').combobox("disable");	
				//$('#KCLX').combobox("disable");			
			}
			saveType = 'edit';
			$('#courseInfo').dialog('open');
		}
		
		if(id == 'del'){
			if(iKeyCode == ''){
				alertMsg('请选择一行数据');
				return;
			}
			ConfirmMsg('是否确定要删除当前选择的课程', 'delClass();', '');
		}
		
		//判断获取参数为save，执行保存操作
		if(id == 'save'){ 
			var KCMC = $('#KCMC').textbox('getValue');

			if(KCMC == ''){
				alertMsg('请填写课程名称');
				return;
			}
			if(KCMC.length > 50){
				alertMsg('课程名称长度超出50位');
				return;
			}
			if($('#KCLX').combobox('getValue') == '' ){
				alertMsg('请选择课程类型');
				return;
			}
			if($('#KCLX').combobox('getValue') == '02' && $('#ZYDM').combobox('getValue') == ''){
				alertMsg('请选择所属专业');
				return;
			}
			$('#kcbh').val($('#KCDM').html());
			$('#XBBH').val($('#XBDM').combobox('getValues'));
			$('#XBMC').val($('#XBDM').combobox('getText'));
			$('#active').val(saveType);

			if(saveType=='edit'){
				$.ajax({
					type : "POST",
					url : '<%=request.getContextPath()%>/Svl_CourseSet',
					data : 'active=checkCourseUsed&KCDM=' + iKeyCode,
					dataType:"json",
					success : function(data){
						if(data[0].MSG == '0'){//未被使用
							$("#form1").submit();
						}else{ 
							if($('#KCLX').combobox('getValue')!=tempKclx || $('#XBDM').combobox('getValues')!=tempSsxb){
								alertMsg('当前课程已设置授课计划，无法修改课程类型，所属系部和所属专业!');
							}else{
								if(tempZydm==undefined){
									$("#form1").submit();
								}else{ 
									if($('#ZYDM').combobox('getValue')!=tempZydm){
										alertMsg('当前课程已设置授课计划，无法修改课程类型，所属系部和所属专业!');
									}else{
										$("#form1").submit();
									}
								}
							}
						}
					}
				});		
			}else{
				$("#form1").submit();
			}
		}
	}
	
	//删除
	function delClass(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_CourseSet',
			data : 'active=del&KCDM=' + iKeyCode+'&KCLX='+kclx,
			dataType:"json",
			success : function(data){
				if(data[0].MSG == '删除成功'){
					showMsg(data[0].MSG);
					loadData();
				}else{
					alertMsg(data[0].MSG);
				}
			}
		});
	}
	
	/**表单提交**/
	$('#form1').form({
		//定位到servlet位置的url
		url:'<%=request.getContextPath()%>/Svl_CourseSet',
		//当点击事件后触发的事件
		onSubmit: function(data){
			return $(this).form('validate');//验证
		}, 
		//当点击事件并成功提交后触发的事件
		success:function(data){
			var json = eval("("+data+")");
			
			if(json[0].MSG == '保存成功'){
				showMsg(json[0].MSG);
				$("#active").val('');
				
				emptyDialog();//清空并关闭dialog
				loadData();
				$('#courseInfo').dialog('close');
			}else{
				alertMsg(json[0].MSG);
			}
		}
	});
	
	/**清空Dialog中表单元素数据**/
	function emptyDialog(){
		saveType = '';
		$('#KCDM').html("");
		$('#KCMC').textbox('setValue', '');
		$('#ZYDM').combobox('setValue', '');
		$('#KCLX').combobox('setValue', '');
		$('#KSXS').combobox('setValue', '');
		$('#XBDM').combobox('setValue', '');
	}
	
	//防JS注入
	/*
	function checkData(str) {
        var  entry = { "'": "&apos;", '"': '&quot;', '<': '&lt;', '>': '&gt;' };
        str = v.replace(/(['")-><&\\\/\.])/g, function ($0){
        	return entry[$0] || $0;
        });
        return str;
    }*/
	
	
	//处理键盘事件
	// 禁止后退键（Backspace）密码或单行、多行文本框除外
	function banBackSpace(e){
	    var ev = e || window.event;//获取event对象
	    var obj = ev.target || ev.srcElement;//获取事件源
	    var t = obj.type || obj.getAttribute('type');//获取事件源类型
	    
	    //获取作为判断条件的事件类型
	    var vReadOnly = obj.getAttribute('readonly');
	    var vEnabled = obj.getAttribute('enabled');
	
	    //处理null值情况
	    vReadOnly = (vReadOnly == null)?false:vReadOnly;
	    vEnabled = (vEnabled == null)?true:vEnabled;
	    
	    //当敲Backspace键时，事件源类型为密码或单行、多行文本的，
	    //并且readonly属性为true或enabled属性为false的，则退格键失效
	    var flag1=(ev.keyCode == 8 && (t=="password"|| t=="text"|| t=="textarea") && (vReadOnly==true|| vEnabled!=true))?true:false;
	
	    //当敲Backspace键时，事件源类型非密码或单行、多行文本的，则退格键失效
	    var flag2=(ev.keyCode == 8 && t != "password"&& t != "text"&& t != "textarea")?true:false;
	
	    //判断
	    if(flag2)
	        return false;
	
	    if(flag1)
	        return false;
	}
	
	//禁止后退键(作用于Firefox、Opera)
	document.onkeypress=banBackSpace;
	//禁止后退键 (作用于IE、Chrome)
	document.onkeydown=banBackSpace;
</script>
</html>